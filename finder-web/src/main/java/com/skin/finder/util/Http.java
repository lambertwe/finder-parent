/*
 * $RCSfile: HttpUtil.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

/**
 * <p>Title: HttpUtil</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Http {
    private static final String USER_AGENT = getUserAgent();

    /**
     * @param url
     * @return String
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        InputStreamReader inputStreamReader = null;

        try {
            URL realUrl = new URL(url);

            /**
             * 使用代理的方式
             * connection = (HttpURLConnection)(realUrl.openConnection(getProxy("127.0.0.1", 8888)));
             */
            connection = (HttpURLConnection)(realUrl.openConnection());
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Connection", "close");
            connection.setRequestProperty("Content-Length", "0");
            connection.setUseCaches(false);

            String contentEncoding = getContentEncoding(connection, "utf-8");
            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, contentEncoding);
            return toString(inputStreamReader, 4096);
        }
        finally {
            close(outputStream);
            close(inputStreamReader);
            close(inputStream);

            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * @param url
     * @param body
     * @return String
     * @throws IOException
     */
    public static String post(String url, String body) throws IOException {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        InputStreamReader inputStreamReader = null;

        try {
            URL realUrl = new URL(url);
            byte[] bytes = (body != null ? body.getBytes("utf-8") : new byte[0]);

            /**
             * 使用代理的方式
             * connection = (HttpURLConnection)(realUrl.openConnection(getProxy("127.0.0.1", 8888)));
             */
            connection = (HttpURLConnection)(realUrl.openConnection());
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Connection", "close");
            connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
            connection.setUseCaches(false);

            if(bytes.length > 0) {
                outputStream = connection.getOutputStream();
                outputStream.write(bytes, 0, bytes.length);
                outputStream.flush();
            }

            String contentEncoding = getContentEncoding(connection, "utf-8");
            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, contentEncoding);
            return toString(inputStreamReader, 4096);
        }
        finally {
            close(outputStream);
            close(inputStreamReader);
            close(inputStream);

            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * @param ip
     * @param port
     * @return Proxy
     */
    protected static Proxy getProxy(String ip, int port) throws IOException {
        InetAddress inetAddress = InetAddress.getByName(ip);
        InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, port);
        return new Proxy(Proxy.Type.HTTP, inetSocketAddress);
    }

    /**
     * @param connection
     * @param defaultContentEncoding
     * @return String
     */
    protected static String getContentEncoding(HttpURLConnection connection, String defaultContentEncoding) {
        String contentEncoding = connection.getContentEncoding();

        if(contentEncoding == null) {
            String contentType = connection.getHeaderField("Content-Type");
            contentEncoding = getContentEncoding(contentType);
        }
        return (contentEncoding != null ? contentEncoding : defaultContentEncoding);
    }

    /**
     * @param contentType
     * @return String
     */
    protected static String getContentEncoding(String contentType) {
        String contentEncoding = null;

        if(contentType == null) {
            return null;
        }

        int k = contentType.indexOf("charset=");

        if(k > -1) {
            contentEncoding = contentType.substring(k + 8).trim();

            if(contentEncoding.endsWith(";")) {
                contentEncoding = contentEncoding.substring(0, contentEncoding.length() - 1);
            }
        }
        return contentEncoding;
    }

    /**
     * @return String
     */
    public static String getUserAgent() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Java/");
        buffer.append(System.getProperty("java.runtime.version"));
        buffer.append(" (");
        buffer.append(System.getProperty("os.name"));
        buffer.append("; ");
        buffer.append(System.getProperty("os.version"));
        buffer.append(")");
        return buffer.toString();
    }

    /**
     * @param reader
     * @param bufferSize
     * @return String
     * @throws IOException
     */
    public static String toString(Reader reader, int bufferSize) throws IOException {
        int length = 0;
        char[] buffer = new char[Math.max(bufferSize, 4096)];
        StringBuilder out = new StringBuilder();

        while((length = reader.read(buffer)) > -1) {
            out.append(buffer, 0, length);
        }
        return out.toString();
    }

    /**
     * @param closeable
     */
    public static void close(java.io.Closeable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            }
            catch(IOException e) {
            }
        }
    }
}
