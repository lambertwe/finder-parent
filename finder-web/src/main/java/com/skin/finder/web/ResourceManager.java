/*
 * $RCSfile: ResourceManager.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: ResourceManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public abstract class ResourceManager {
    protected String file;
    protected long lastModified;
    protected Map<String, ContentEntry> cache;
    protected String compress = "^js$|^css$|^xml$|^txt$|^text$|^htm$|^html$";
    private static final Logger logger = LoggerFactory.getLogger(ResourceManager.class);

    /**
     * @param file
     */
    public ResourceManager(String file) {
        this.file = file;
    }

    /**
     * @param path
     * @return ByteEntry
     */
    public ContentEntry get(String path) {
        if(path == null) {
            return null;
        }
        return this.cache.get(path);
    }

    /**
     * @param name
     * @param bytes
     * @param memory
     * @param lastModified
     * @return ContentEntry
     * @throws IOException
     */
    public ContentEntry put(String name, boolean memory, long lastModified, byte[] bytes) throws IOException {
        if(name == null) {
            throw new NullPointerException("name must be not null.");
        }

        if(bytes == null) {
            throw new NullPointerException("bytes must be not null.");
        }

        String extension = getExtension(name);
        Pattern pattern = Pattern.compile(this.compress);
        ContentEntry contentEntry = null;
        logger.info("cache: {}", name);

        if(pattern.matcher(extension).matches()) {
            contentEntry = new ContentEntry(name, ContentEntry.ZIP, memory, lastModified, gzip(bytes));
        }
        else {
            contentEntry = new ContentEntry(name, ContentEntry.BIN, memory, lastModified, bytes);
        }
        this.cache.put(name, contentEntry);
        return contentEntry;
    }

    /**
     * @param name
     * @return ContentEntry
     */
    public ContentEntry remove(String name) {
        return this.cache.remove(name);
    }

    /**
     * @param contentEntry
     * @param charset
     * @return String
     * @throws IOException
     */
    public static String getString(ContentEntry contentEntry, String charset) throws IOException {
        if(contentEntry == null) {
            return null;
        }

        byte[] bytes = contentEntry.getBytes();

        if(contentEntry.getType() == ContentEntry.ZIP) {
            bytes = ungzip(contentEntry.getBytes());
        }
        else {
            bytes = contentEntry.getBytes();
        }

        if(charset != null) {
            return new String(bytes, charset);
        }
        return new String(bytes, "utf-8");
    }

    /**
     * @param path
     * @return String
     */
    public static String getExtension(String path) {
        if(path == null || path.length() < 1) {
            return "";
        }

        char c = '0';
        int i = path.length() - 1;

        for(; i > -1; i--) {
            c = path.charAt(i);

            if(c == '.' ) {
                break;
            }
            else if(c == '/' || c == '\\' || c == ':') {
                break;
            }
        }

        if(c == '.') {
            return path.substring(i + 1);
        }
        return "";
    }

    /**
     * @param bytes
     * @return byte[]
     * @throws IOException
     */
    public static byte[] gzip(byte[] bytes) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
        gzipOutputStream.write(bytes, 0, bytes.length);
        gzipOutputStream.finish();
        gzipOutputStream.flush();
        return outputStream.toByteArray();
    }

    /**
     * @param bytes
     * @return byte[]
     * @throws IOException
     */
    public static byte[] ungzip(byte[] bytes) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream, bytes.length);
        copy(gzipInputStream, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        int length = 0;
        int bufferSize = 2048;
        byte[] buffer = new byte[bufferSize];

        while((length = inputStream.read(buffer, 0, bufferSize)) > -1) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.flush();
    }

    /**
     * destroy
     */
    public void destroy() {
        if(this.cache != null) {
            this.cache.clear();
            this.cache = null;
        }
    }
}
