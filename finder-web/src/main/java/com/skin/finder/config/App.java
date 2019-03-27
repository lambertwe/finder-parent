/*
 * $RCSfile: App.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.config;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.util.Loader;
import com.skin.finder.util.StringUtil;
import com.skin.finder.util.Version;
import com.skin.finder.util.Manifest;

/**
 * <p>Title: App</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final Properties properties = load();
    private static final String HASH = getRandomString(4);
    private static final String TOKEN = UUID.randomUUID().toString();
    private static final long TIMESTAMP = System.currentTimeMillis();

    /**
     * disabled
     */
    private App() {
    }

    /**
     * @return Properties
     */
    private static Properties load() {
        String name = null;
        String conf = null;
        String url = null;
        Map<String, String> map = null;
        String version = Version.getInstance().getVersion();

        /**
         * 加载类: Manifest
         * 加载类: Version
         */
        logger.info("try load 'classpath:/app.properties'.");

        try {
            map = Loader.getProperties("app.properties", "utf-8");
        }
        catch(IOException e) {
            logger.warn("app.properties not exists. use default app name: finder.");
        }

        if(map != null) {
            name = map.get("name");
            conf = map.get("conf");
            url = map.get("url");
        }

        if(StringUtil.isBlank(name)) {
            name = "finder";
        }

        if(StringUtil.isBlank(conf)) {
            conf = "";
        }

        if(StringUtil.isBlank(url)) {
            url = "http://www.finderweb.net";
        }

        Properties props = new Properties();
        props.setProperty("name", name);
        props.setProperty("conf", conf);
        props.setProperty("url", url);
        props.setProperty("version", version);
        logger.debug("app.name: {}, app.url: {}, app.version: {}", name, url, version);
        logger.info("Manifest.version: {}", Manifest.getVersion());
        return props;
    }

    /**
     * <p>Note: app.properties有且只有三个配置项可以指定:</p> 
     * <p>name: 应用的名称</p>
     * <p>conf: 应用的数据地址</p>
     * <p>url:  应用的官网地址</p>
     * <p>Finder自己使用的配置项均在finder.conf中配置.</p>
     * @return String
     */
    public static String getName() {
        return properties.getProperty("name");
    }

    /**
     * @return String
     */
    public static String getConf() {
        return getConf("META-INF/conf/");
    }

    /**
     * @param defaultValue
     * @return String
     */
    public static String getConf(String defaultValue) {
        String value = properties.getProperty("conf");

        if(StringUtil.isBlank(value)) {
            return defaultValue;
        }
        return value;
    }

    /**
     * @return String
     */
    public static String getUrl() {
        return properties.getProperty("url");
    }

    /**
     * @return String
     */
    public static String getVersion() {
        return properties.getProperty("version");
    }

    /**
     * 返回应用启动时产生的token
     * @return String
     */
    public static String getToken() {
        return TOKEN;
    }

    /**
     * 返回应用的启动时间
     * @return long
     */
    public static long getTimestamp() {
        return TIMESTAMP;
    }

    /**
     * @return String
     */
    public static String hash() {
        return HASH;
    }

    /**
     * @param cbuf
     * @param min
     * @param max
     * @return String
     */
    private static String getRandomString(int length) {
        Random random = new Random();
        String hex = "0123456789abcdef";
        char[] chars = new char[length];

        for(int i = 0; i < length; i++) {
            chars[i] = hex.charAt(random.nextInt(16));
        }
        return new String(chars);
    }

    /**
     * @return String
     */
    public static String getStatus() {
        String version = App.getVersion();
        StringBuilder buffer = new StringBuilder();
        buffer.append("{\"token\":\"");
        buffer.append(App.getToken());
        buffer.append("\",\"timestamp\":");
        buffer.append(App.getTimestamp());
        buffer.append(",\"version\":\"");
        buffer.append(version);
        buffer.append("\"}");
        return buffer.toString();
    }
}
