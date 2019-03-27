/*
 * $RCSfile: IO.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: IO</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class IO {
    /**
     * disabled
     */
    private IO() {
    }

    /**
     * @param url
     * @return File
     * @throws IOException
     */
    public static File getFile(URL url) throws IOException {
        if(url != null && "file".equals(url.getProtocol())) {
            String path = URLDecoder.decode(url.getFile(), "utf-8");
            return new File(path);
        }
        return null;
    }

    /**
     * @param file
     * @return byte[]
     * @throws IOException
     */
    public static byte[] read(File file) throws IOException {
        InputStream inputStream = null;

        try {
            int length = (int)(file.length());
            byte[] data = new byte[length];
            inputStream = new FileInputStream(file);
            inputStream.read(data, 0, length);
            return data;
        }
        finally {
            IO.close(inputStream);
        }
    }

    /**
     * @param inputStream
     * @param bufferSize
     * @return byte[]
     * @throws IOException
     */
    public static byte[] read(InputStream inputStream, int bufferSize) throws IOException {
        int length = 0;
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        while((length = inputStream.read(buffer, 0, 2048)) > 0) {
            bos.write(buffer, 0, length);
        }
        return bos.toByteArray();
    }

    /**
     * @param file
     * @param charset
     * @return String
     * @throws IOException
     */
    public static List<String> list(File file, String charset) throws IOException {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;

        try {
            inputStream = new FileInputStream(file);

            if(charset == null || charset.length() < 1) {
                inputStreamReader = new InputStreamReader(inputStream);
            }
            else {
                inputStreamReader = new InputStreamReader(inputStream, charset);
            }
            return list(inputStreamReader);
        }
        finally {
            close(inputStreamReader);
            close(inputStream);
        }
    }

    /**
     * @param reader
     * @return List<String>
     * @throws IOException
     */
    public static List<String> list(Reader reader) throws IOException {
        BufferedReader buffer = null;
        List<String> list = new ArrayList<String>();

        if(reader instanceof BufferedReader) {
            buffer = (BufferedReader)reader;
        }
        else {
            buffer = new BufferedReader(reader);
        }

        try {
            String line = null;
            while((line = buffer.readLine()) != null) {
                list.add(line);
            }
            return list;
        }
        finally {
            // never closed
            if(reader == null) {
                buffer.close();
            }
        }
    }

    /**
     * @param inputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        copy(inputStream, bos, 4096);
        return bos.toByteArray();
    }

    /**
     * @param file
     * @throws IOException
     */
    public static void touch(File file) throws IOException {
        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
        }
        finally {
            close(outputStream);
        }
    }

    /**
     * @param file
     * @param buffer
     * @throws IOException
     */
    public static void write(File file, byte[] buffer) throws IOException {
        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);

            if(buffer != null && buffer.length > 0) {
                outputStream.write(buffer);
                outputStream.flush();
            }
        }
        finally {
            close(outputStream);
        }
    }

    /**
     * @param source
     * @param target
     * @param offset
     * @param lastModified
     * @throws IOException
     */
    public static void write(File source, File target, long offset, long lastModified) throws IOException {
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(source);
            write(inputStream, target, offset);

            if(lastModified > 0L) {
                target.setLastModified(lastModified);
            }
        }
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
    }

    /**
     * @param inputStream
     * @param file
     * @param offset
     * @throws IOException
     */
    public static void write(InputStream inputStream, File file, long offset) throws IOException {
        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(file, "rw");
            raf.setLength(offset);

            if(offset > 0) {
                raf.seek(offset);
            }
            copy(inputStream, raf, 8192);
        }
        finally {
            if(raf != null) {
                try {
                    raf.close();
                }
                catch(IOException e) {
                }
            }
        }
    }

    /**
     * @param inputStream
     * @param raf
     * @param bufferSize
     * @throws IOException
     */
    public static void copy(InputStream inputStream, RandomAccessFile raf, int bufferSize) throws IOException {
        int length = 0;
        byte[] buffer = new byte[bufferSize];

        while((length = inputStream.read(buffer, 0, bufferSize)) > 0) {
            raf.write(buffer, 0, length);
        }
    }

    /**
     * @param source
     * @param target
     * @param lastModified
     * @throws IOException
     */
    public static void copy(File source, File target, boolean lastModified) throws IOException {
        if(source.isFile()) {
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                int bufferSize = 0;
                long length = source.length();

                if(length < 4096) {
                    bufferSize = 2048;
                }
                else {
                    bufferSize = 8192;
                }

                inputStream = new FileInputStream(source);
                outputStream = new FileOutputStream(target);
                copy(inputStream, outputStream, bufferSize);
            }
            finally {
                close(inputStream);
                close(outputStream);
            }

            if(lastModified) {
                target.setLastModified(source.lastModified());
            }
        }
        else {
            if(!target.exists()) {
                target.mkdirs();
            }

            File[] files = source.listFiles();

            for(File file : files) {
                copy(file, new File(target, file.getName()), lastModified);
            }
        }
    }

    /**
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        copy(inputStream, outputStream, 8192);
    }

    /**
     * @param inputStream
     * @param outputStream
     * @param bufferSize
     * @throws IOException
     */
    public static void copy(InputStream inputStream, OutputStream outputStream, int bufferSize) throws IOException {
        int length = 0;
        byte[] buffer = new byte[bufferSize];

        while((length = inputStream.read(buffer, 0, bufferSize)) > -1) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
    }

    /**
     * @param inputStream
     * @param outputStream
     * @param bufferSize
     * @param size
     * @throws IOException
     */
    public static void copy(InputStream inputStream, OutputStream outputStream, int bufferSize, long size) throws IOException {
        if(size > 0) {
            int readBytes = 0;
            long count = size;
            int length = Math.min(bufferSize, (int)(size));
            byte[] buffer = new byte[length];

            while(count > 0) {
                if(count > length) {
                    readBytes = inputStream.read(buffer, 0, length);
                }
                else {
                    readBytes = inputStream.read(buffer, 0, (int)count);
                }

                if(readBytes > 0) {
                    outputStream.write(buffer, 0, readBytes);
                    count -= readBytes;
                }
                else {
                    break;
                }
            }
            outputStream.flush();
        }
    }

    /**
     * @param raf
     * @param outputStream
     * @param size
     * @throws IOException
     */
    public static void copy(RandomAccessFile raf, OutputStream outputStream, long size) throws IOException {
        int readBytes = 0;
        long count = size;
        int bufferSize = getBufferSize(size, 8192);
        byte[] buffer = new byte[bufferSize];

        while(count > 0) {
            if(count > bufferSize) {
                readBytes = raf.read(buffer, 0, bufferSize);
            }
            else {
                readBytes = raf.read(buffer, 0, (int)count);
            }

            if(readBytes > 0) {
                outputStream.write(buffer, 0, readBytes);
                count -= readBytes;
            }
            else {
                break; 
            }
        }
        outputStream.flush();
    }

    /**
     * @param reader
     * @param writer
     * @throws IOException
     */
    public static void copy(Reader reader, Writer writer) throws IOException {
        copy(reader, writer, 2048);
    }

    /**
     * @param reader
     * @param writer
     * @param bufferSize
     * @throws IOException
     */
    public static void copy(Reader reader, Writer writer, int bufferSize) throws IOException {
        int length = 0;
        char[] buffer = new char[bufferSize];

        while((length = reader.read(buffer, 0, bufferSize)) > -1) {
            writer.write(buffer, 0, length);
        }
        writer.flush();
    }

    /**
     * @param file
     */
    public static void delete(File file) {
        if(file.isDirectory()) {
            File[] files = file.listFiles();

            for(File f : files) {
                if(f.isDirectory()) {
                    delete(f);
                }
                else {
                    f.delete();
                }
            }
        }
        file.delete();
    }

    /**
     * @param file
     * @throws IOException
     */
    public static void toucn(File file) throws IOException {
        OutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
        }
        finally {
            close(outputStream);
        }
    }

    /**
     * @param path
     * @return String
     */
    public static String getFileName(String path) {
        if(path != null && path.length() > 0) {
            int i = path.length() - 1;

            for(; i > -1; i--) {
                char c = path.charAt(i);

                if(c == '/' || c == '\\' || c == ':') {
                    break;
                }
            }
            return path.substring(i + 1);
        }
        return "";
    }

    /**
     * @param path
     * @return String
     */
    public static String getExtension(String path) {
        int i = path.lastIndexOf(".");

        if(i > -1) {
            return path.substring(i);
        }
        return "";
    }

    /**
     * @param file
     * @param start
     * @param length
     * @param charset
     * @return String
     * @throws IOException
     */
    public static String getString(File file, long start, int length, String charset) throws IOException {
        InputStream inputStream = null;
        byte[] bytes = new byte[length];

        try {
            inputStream = new FileInputStream(file);

            if(start > 0) {
                inputStream.skip(start);
            }

            int readBytes = inputStream.read(bytes, 0, length);
            return new String(bytes, 0, readBytes, charset);
        }
        finally {
            IO.close(inputStream);
        }
    }

    /**
     * @param file
     * @param charset
     * @return String
     * @throws IOException
     */
    public static String toString(File file, String charset) throws IOException {
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);

            int length = (int)(file.length());
            byte[] buffer = new byte[length];
            inputStream.read(buffer, 0, length);

            if(charset != null) {
                return new String(buffer, 0, buffer.length, charset);
            }
            else {
                return new String(buffer, 0, buffer.length);
            }
        }
        finally {
            close(inputStream);
        }
    }

    /**
     *
     * @param inputStream
     * @return String
     * @throws IOException
     */
    public static String toString(InputStream inputStream) throws IOException {
        return toString(new InputStreamReader(inputStream));
    }

    /**
     *
     * @param inputStream
     * @param charset
     * @return String
     * @throws IOException
     */
    public static String toString(InputStream inputStream, String charset) throws IOException {
        Reader reader = null;

        try {
            reader = new java.io.InputStreamReader(inputStream, charset);
            return toString(reader);
        }
        finally {
            IO.close(reader);
        }
    }

    /**
     *
     * @param reader
     * @return String
     * @throws IOException
     */
    public static String toString(Reader reader) throws IOException {
        return toString(reader, 2048);
    }

    /**
     *
     * @param reader
     * @param bufferSize
     * @return String
     * @throws IOException
     */
    public static String toString(Reader reader, int bufferSize) throws IOException {
        try {
            int length = 0;
            char[] buffer = new char[Math.max(bufferSize, 2048)];
            StringWriter out = new StringWriter();

            while((length = reader.read(buffer)) > -1) {
                out.write(buffer, 0, length);
            }
            return out.toString();
        }
        finally {
        }
    }

    /**
     * @param length
     * @param maxSize
     * @return int
     */
    public static int getBufferSize(long length, int maxSize) {
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
    public static int getBufferSize(int length) {
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
     * @param resource
     */
    public static void close(Closeable resource) {
        if(resource != null) {
            try {
                resource.close();
            }
            catch(IOException e) {
            }
        }
    }
}
