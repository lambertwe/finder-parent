/*
 * $RCSfile: Multipart.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web.upload;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: Multipart</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Multipart {
    private String repository;
    private int maxFileSize;
    private int maxBodySize;
    private byte[] buffer;
    private PushbackInputStream inputStream;
    private static final byte[] LF = new byte[]{0x0A};
    private static final byte[] CRLF = new byte[]{0x0D, 0x0A};
    private static final byte[] END = new byte[]{'-', '-'};
    private static final Logger logger = LoggerFactory.getLogger(Multipart.class);

    /**
     *
     */
    public Multipart() {
        this.maxFileSize = 2 * 1024 * 1024;
        this.maxBodySize = 2 * 1024 * 1024;
    }

    /**
     * @param request
     * @return Map<String, Part>
     * @throws IOException
     */
    public List<Part> parse(HttpServletRequest request) throws IOException {
        String method = request.getMethod();
        String contentType = request.getContentType();
        int contentLength = request.getContentLength();
        String boundary = null;

        if(!method.equalsIgnoreCase("post")) {
            throw new IOException("BadHttpMethodException: " + method);
        }

        if(contentLength > -1 && contentLength > this.getMaxBodySize()) {
            throw new IOException("stream body too large: " + contentLength + ", maxBodySize: " + this.maxBodySize);
        }

        int k = contentType.indexOf(";");

        if(k > -1) {
            boundary = contentType.substring(k + 1).trim();
            contentType = contentType.substring(0, k + 1).toLowerCase();
        }
        else {
            throw new RuntimeException("BadContentTypeException: " + method);
        }

        if(!contentType.equals("multipart/form-data;")) {
            throw new RuntimeException("BadContentTypeException: " + contentType);
        }

        if(boundary.startsWith("boundary=")) {
            boundary = boundary.substring(9).trim();
        }
        else {
            throw new RuntimeException("BadContentTypeException: " + contentType);
        }
        return this.parse(request.getInputStream(), contentLength, "--" + boundary, request.getCharacterEncoding());
    }

    /**
     * @param inputStream
     * @param contentLength
     * @param seperator
     * @param charset
     * @return List<Part>
     * @throws IOException
     */
    public List<Part> parse(InputStream inputStream, long contentLength, String seperator, String charset) throws IOException {
        long bodyBytes = 0L;
        byte[] bytes = this.getAsciiBytes(seperator);
        int bufferSize = getBufferSize(contentLength, 8192);
        this.buffer = new byte[bufferSize];
        this.inputStream = new PushbackInputStream(inputStream, bufferSize);
        List<Part> items = new ArrayList<Part>();

        try {
            while(this.readBoundary(bytes)) {
                HttpHeader httpHeader = this.readHeaders(charset);
                String content = httpHeader.getHeader("Content-Disposition");
                String contentType = httpHeader.getHeader("Content-Type");
                ContentDisposition disposition = ContentDisposition.parse(content);
                String name = disposition.getProperty("name");
                String fileName = disposition.getProperty("filename");
                String boundary = disposition.getProperty("boundary");

                if(boundary != null) {
                    bytes = this.getAsciiBytes(boundary);
                }

                Part part = new Part();
                part.setName(name);
                part.setFileName(fileName);
                part.setContentType(contentType);
                part.setCharset(charset);
                part.setHttpHeader(httpHeader);
                items.add(part);

                if(fileName != null) {
                    if(this.repository == null) {
                        this.repository = System.getProperty("java.io.tmpdir");
                    }

                    OutputStream outputStream = null;
                    File file = this.getTempFile(this.repository);
                    part.setFile(file);

                    try {
                        outputStream = new FileOutputStream(file);
                        long length = this.readBody(bytes, new BufferedOutputStream(outputStream, 4096));
                        part.setLength(length);
                    }
                    finally {
                        close(outputStream);
                    }
                }
                else {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    long length = this.readBody(bytes, bos);
                    part.setLength(length);
                    part.setInputStream(new ByteArrayInputStream(bos.toByteArray()));
                }

                bodyBytes += part.length();

                if(bodyBytes >= this.maxBodySize) {
                    throw new IOException("stream body too large: " + bodyBytes + ", maxBodySize: " + this.maxBodySize);
                }
            }
            return items;
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);

            for(Part part : items) {
                part.delete();
            }

            if(e instanceof IOException) {
                throw (IOException)e;
            }
            else {
                throw new IOException(e);
            }
        }
    }

    /**
     * @return Map<String, String>
     * @throws IOException
     */
    protected HttpHeader readHeaders(String charset) throws IOException {
        HttpHeader httpHeader = new HttpHeader();

        while(true) {
            byte[] bytes = this.readLine();

            if(Arrays.equals(bytes, CRLF) || Arrays.equals(bytes, LF)) {
                break;
            }

            String header = new String(bytes, charset);
            int k = header.indexOf(":");

            if(k > -1) {
                String name = header.substring(0, k).trim();
                String value = header.substring(k + 1).trim();
                httpHeader.addHeader(name, value);
            }
        }
        return httpHeader;
    }

    /**
     * @param boundary
     * @param outputStream
     * @throws IOException
     */
    protected long readBody(byte[] boundary, OutputStream outputStream) throws IOException {
        byte[] bytes = new byte[boundary.length + 2];
        bytes[0] = 0x0D;
        bytes[1] = 0x0A;
        System.arraycopy(boundary, 0, bytes, 2, boundary.length);
        long readBytes = this.copy(this.inputStream, outputStream, bytes, this.maxFileSize);
        this.readCRLF();
        return readBytes;
    }

    /**
     * @return boolean
     * @throws IOException
     */
    protected boolean readBoundary(byte[] boundary) throws IOException {
        byte[] buffer = this.buffer;
        int length = this.inputStream.read(buffer, 0, boundary.length + 2);

        if(!equals(buffer, boundary, 0)) {
            throw new IOException("bad boundary: " + new String(buffer, 0, length, "ascii"));
        }

        if(equals(buffer, CRLF, boundary.length)) {
            return true;
        }

        if(equals(buffer, END, boundary.length)) {
            return false;
        }
        throw new IOException("bad boundary.");
    }

    /**
     * @throws IOException
     */
    protected void readCRLF() throws IOException {
        byte[] buf = new byte[2];
        this.inputStream.read(buf, 0, 2);

        if(buf[0] != 0x0D || buf[1] != 0x0A) {
            throw new IOException("CRLF expected at end of boundary. [" + (char)buf[0] + "] - [" + (char)buf[1] + "]");
        }
    }

    /**
     * @return byte[]
     * @throws IOException
     */
    protected byte[] readLine() throws IOException {
        int length = 0;
        byte[] buffer = this.buffer;
        int bufferSize = this.buffer.length;
        ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);

        /**
         * 返回数据包含换行符
         */
        while((length = this.inputStream.read(buffer, 0, bufferSize)) > -1) {
            for(int i = 0; i < length; i++) {
                if(buffer[i] == '\n') {
                    bos.write(buffer, 0, i + 1);

                    if(i + 1 < length) {
                        this.inputStream.unread(buffer, i + 1, length - i - 1);
                    }
                    return bos.toByteArray();
                }
            }
            bos.write(buffer, 0, length);
        }
        return bos.toByteArray();
    }

    /**
     * @param content
     * @return byte[]
     * @throws UnsupportedEncodingException
     */
    protected byte[] getAsciiBytes(String content) throws UnsupportedEncodingException {
        return content.getBytes("US-ASCII");
    }

    /**
     * @param outputStream
     * @return long
     * @throws IOException
     */
    protected long copy(PushbackInputStream pushback, OutputStream outputStream, byte[] boundary, int limit) throws IOException {
        int k = 0;
        int length = 0;
        long readBytes = 0L;
        byte[] buffer = this.buffer;
        int bufferSize = this.buffer.length;
        boolean found = false;

        while((length = pushback.read(buffer, 0, bufferSize)) > 0) {
            /**
             * 一般情况下IO层到达的数据还不足以将buffer读满
             * 有可能读到的数据长度太小, 当长度小于boundary.length的时候将会出现错误
             * 如果发生这种情况, 继续读数据, 直到读满buffer
             */
            if(length < boundary.length) {
                /**
                 * 尝试读满buffer, 可能会阻塞
                 */
                logger.debug("error.length: {}", length);
                length += Math.max(read(pushback, buffer, length, bufferSize - length), 0);
            }

            /**
             * length < boundary.length
             * 如果出现这种情况, 可能造成文件不完整出现脏数据
             */
            if(length < boundary.length) {
                pushback.unread(buffer,  0, length);
                logger.error("error.length: {}, {}\r\n{}", length, boundary.length);
                throw new IOException("error.length: " + length);
            }

            k = indexOf(buffer, boundary, 0, length);

            if(k > -1) {
                readBytes += k;
                pushback.unread(buffer, k, length - k);
                outputStream.write(buffer, 0, k);
                found = true;
                break;
            }

            readBytes += (length - boundary.length);

            if(readBytes > limit) {
                throw new IOException("stream body too large: " + readBytes);
            }

            outputStream.write(buffer, 0, length - boundary.length);
            pushback.unread(buffer, length - boundary.length, boundary.length);
        }

        if(!found) {
            throw new IOException("BadFormData: expect boundary, but not found.");
        }

        outputStream.flush();
        return readBytes;
    }

    /**
     * @param inputStream
     * @param buffer
     * @param offset
     * @param length
     * @return int
     * @throws IOException
     */
    protected int read(InputStream inputStream, byte[] buffer, int offset, int length) throws IOException {
        int readBytes = 0;
        int remain = length;
        int pos = offset;

        while((readBytes = inputStream.read(buffer, pos, remain)) > -1) {
            remain -= readBytes;

            if(remain <= 0) {
                break;
            }
            pos += readBytes;
        }
        return (remain < length ? (length - remain) : readBytes);
    }

    /**
     * @return OutputStream
     * @throws IOException
     */
    protected File getTempFile(String work) throws IOException {
        if(work == null) {
            throw new IOException("'work' must be not null.");
        }

        long timeMillis = System.currentTimeMillis();
        File file = new File(work, timeMillis + ".tmp");

        while(file.exists()) {
            timeMillis++;
            file = new File(work, timeMillis + ".tmp");
        }
        file.createNewFile();
        return file;
    }

    /**
     * @param bytes
     * @param searchment
     * @param offset
     * @return int
     */
    private int indexOf(byte[] bytes, byte[] searchment, int offset, int length) {
        for(int j = offset; j < offset + length; j++) {
            if(equals(bytes, searchment, j)) {
                return j;
            }
        }
        return -1;
    }

    /**
     * @return the repository
     */
    public String getRepository() {
        return this.repository;
    }

    /**
     * @param repository the repository to set
     */
    public void setRepository(String repository) {
        this.repository = repository;
    }

    /**
     * @return the maxFileSize
     */
    public int getMaxFileSize() {
        return this.maxFileSize;
    }

    /**
     * @param maxFileSize the maxFileSize to set
     */
    public void setMaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    /**
     * @return the maxBodySize
     */
    public int getMaxBodySize() {
        return this.maxBodySize;
    }

    /**
     * @param maxBodySize the maxBodySize to set
     */
    public void setMaxBodySize(int maxBodySize) {
        this.maxBodySize = maxBodySize;
    }

    /**
     * @param buf1
     * @param buf2
     * @param offset
     * @param length
     * @return boolean
     */
    private static boolean equals(byte[] buf1, byte[] buf2, int offset) {
        int length = buf2.length;

        if(buf1.length - offset < length) {
            return false;
        }

        for(int i = 0; i < length; i++) {
            if(buf1[offset + i] != buf2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param length
     * @return int
     */
    private static int getBufferSize(long length, int maxSize) {
        if(length <= 0 || length >= maxSize) {
            return maxSize;
        }
        else {
            return getBufferSize((int)length);
        }
    }

    /**
     * @param length
     * @return int
     */
    private static int getBufferSize(int length) {
        int capacity = length;
        capacity |= (capacity >>>  1);
        capacity |= (capacity >>>  2);
        capacity |= (capacity >>>  4);
        capacity |= (capacity >>>  8);
        capacity |= (capacity >>> 16);
        capacity++;
        return capacity;
    }

    /**
     * @param closeable
     */
    public static void close(Closeable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            }
            catch(IOException e) {
            }
        }
    }
}
