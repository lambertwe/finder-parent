/*
 * $RCSfile: ConfigFactory.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: Loader</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Loader {
    /**
     * @param name
     * @return InputStream
     */
    public static URL getResource(String name) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(name);

        if(url != null) {
            return url;
        }
        return Loader.class.getClassLoader().getResource(name);
    }

    /**
     * @param name
     * @return InputStream
     */
    public static InputStream getInputStream(String name) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(name);

        if(inputStream != null) {
            return inputStream;
        }
        return Loader.class.getClassLoader().getResourceAsStream(name);
    }

    /**
     * @param name
     * @param charset
     * @return Map<String, String>
     * @throws IOException 
     */
    public static Map<String, String> getProperties(String name, String charset) throws IOException {
        InputStream inputStream = getInputStream(name);

        if(inputStream == null) {
            throw new IOException(name + " not found.");
        }

        try {
            return getProperties(new InputStreamReader(inputStream, charset));
        }
        finally {
            IO.close(inputStream);
        }
    }

    /**
     * @param bytes
     * @param charset
     * @return Properties
     * @throws IOException 
     */
    public static Map<String, String> getProperties(byte[] bytes, String charset) throws IOException {
        String text = new String(bytes, charset);
        return getProperties(new StringReader(text));
    }

    /**
     * @param inputStream
     * @param charset
     * @return Properties
     * @throws IOException 
     */
    public static Map<String, String> getProperties(InputStream inputStream, String charset) throws IOException {
        return getProperties(new InputStreamReader(inputStream, charset));
    }

    /**
     * @param reader
     * @return Properties
     * @throws IOException
     */
    public static Map<String, String> getProperties(Reader reader) throws IOException {
        Map<String, String> properties = new HashMap<String, String>();

        if(reader != null) {
            String line = null;
            BufferedReader buffer = new BufferedReader(reader);

            while((line = buffer.readLine()) != null) {
                line = line.trim();

                if(line.length() < 1) {
                    continue;
                }

                if(line.startsWith("#")) {
                    continue;
                }

                int i = line.indexOf("=");

                if(i > -1) {
                    String name = line.substring(0, i).trim();
                    String value = line.substring(i + 1).trim();

                    if(name.length() > 0 && value.length() > 0) {
                        properties.put(name, value);
                    }
                }
            }
        }
        return properties;
    }

    /**
     * 根据原始文件重新构建内容
     * 可以保留原始文件的格式，例如注释等
     * @param reader
     * @param properties
     * @return String
     * @throws IOException
     */
    public static String build(Reader reader, Map<String, String> properties) throws IOException {
        String line = null;
        String key = null;
        String value = null;
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder buffer = new StringBuilder();
        Map<String, String> map = new HashMap<String, String>();

        while((line = bufferedReader.readLine()) != null) {
            line = line.trim();

            if(line.length() < 1) {
                buffer.append("\r\n");
                continue;
            }

            if(line.startsWith("#")) {
                buffer.append(line);
                buffer.append("\r\n");
                continue;
            }

            int i = line.indexOf("=");

            if(i < 0) {
                buffer.append(line);
                buffer.append("\r\n");
                continue;
            }

            /**
             * 没有name忽略
             */
            key = line.substring(0, i).trim();
            value = properties.get(key);

            if(key.length() > 0) {
                map.put(key, "1");
                buffer.append(key);
                buffer.append(" = ");

                if(value != null) {
                    buffer.append(value.trim());
                }
                buffer.append("\r\n");
            }
        }

        /**
         * 处理新增项
         */
        for(Map.Entry<String, String> entry : properties.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();

            if(map.get(key) == null) {
                buffer.append(key);
                buffer.append(" = ");
                buffer.append(value.trim());
                buffer.append("\r\n");
            }
        }
        return buffer.toString();
    }
}
