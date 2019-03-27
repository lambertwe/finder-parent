/*
 * $RCSfile: ConfigFactory.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.util.Base64;
import com.skin.finder.util.IO;
import com.skin.finder.util.Loader;
import com.skin.finder.util.StringUtil;

/**
 * <p>Title: ConfigFactory</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ConfigFactory {
    private static final Logger logger = LoggerFactory.getLogger(ConfigFactory.class);
    private static final String APP_NAME = App.getName();
    private static final String APP_CONF = App.getConf();
    private static final String LOCATION = getLocation("");
    private static final String RESOURCE = "finder.conf";
    private static final Config CONFIG = ConfigFactory.load(RESOURCE, "utf-8");

    /**
     * disabled
     */
    private ConfigFactory() {
    }

    /**
     * @return String
     */
    public static String getMaster() {
        return ConfigFactory.getString(Constants.CLUSTER_MASTER_NAME);
    }

    /**
     * @return String
     */
    public static String getHostName() {
        return ConfigFactory.getString(Constants.CLUSTER_NODE_NAME);
    }

    /**
     * @return String
     */
    public static String getAdmin() {
        return ConfigFactory.getString(Constants.CLUSTER_SECURITY_ROOT);
    }

    /**
     * @return String
     */
    public static String getSecurityKey() {
        return ConfigFactory.getString(Constants.CLUSTER_SECURITY_KEY, "00000000-0000-0000-0000-00000000");
    }

    /**
     * @return String
     */
    public static String getPublicKey() {
        return ConfigFactory.getString(Constants.PUBLIC_KEY, Constants.DEFAULT_PUBLIC_KEY);
    }

    /**
     * @return String
     */
    public static String getPrivateKey() {
        return ConfigFactory.getString(Constants.PRIVATE_KEY, Constants.DEFAULT_PRIVATE_KEY);
    }

    /**
     * @return String
     */
    public static String getSessionKey() {
        return ConfigFactory.getString(Constants.SESSION_KEY, "00000000-0000-0000-0000-00000000");
    }

    /**
     * @return String
     */
    public static String getSessionName() {
        return ConfigFactory.getString(Constants.SESSION_NAME, "passport");
    }

    /**
     * @return String
     */
    public static String getSessionTimeout() {
        return ConfigFactory.getString(Constants.SESSION_TIMEOUT);
    }

    /**
     * @return String
     */
    public static String getOperateButton() {
        return ConfigFactory.getString(Constants.DISPLAY_OPERATE_BUTTON);
    }

    /**
     * @return String
     */
    public static String getTextType() {
        return ConfigFactory.getString(Constants.TEXT_TYPE, "log, txt, text, js, css, htm, html, xml, ini, conf");
    }

    /**
     * @return String
     */
    public static String getUploadPartSize() {
        return ConfigFactory.getString(Constants.UPLOAD_PART_SIZE, "2M");
    }

    /**
     * @return boolean
     */
    public static boolean getUpdateCheck() {
        return ConfigFactory.getBoolean(Constants.UPDATE_CHECK, true);
    }

    /**
     * @return String
     */
    public static String getDemoUserName() {
        return ConfigFactory.getString(Constants.DEMO_USERNAME, "");
    }

    /**
     * @return String
     */
    public static String getDemoPassword() {
        return ConfigFactory.getString(Constants.DEMO_PASSWORD, "");
    }

    /**
     * @return String
     */
    public static String getAccessCode() {
        String code = ConfigFactory.getString(Constants.ACCESS_CODE);

        if(StringUtil.notBlank(code)) {
            return Base64.decode(code, "utf-8");
        }
        return "";
    }

    /**
     * @return String
     */
    public static String getEnvName() {
        return ConfigFactory.getString(Constants.ENV_NAME, "prod");
    }

    /**
     * @return int
     */
    public static int getVersion() {
        return ConfigFactory.getInteger(Constants.CONF_VERSION, 1);
    }

    /**
     * @param name
     * @param value
     */
    public static void setValue(String name, String value) {
        CONFIG.setValue(name, value);
    }

    /**
     * @param name
     * @return String
     */
    public static String getValue(String name) {
        return CONFIG.getValue(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static String getValue(String name, String defaultValue) {
        return CONFIG.getValue(name, defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public static String getString(String name) {
        return CONFIG.getString(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static String getString(String name, String defaultValue) {
        return CONFIG.getString(name, defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public static Character getCharacter(String name) {
        return CONFIG.getCharacter(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static Character getCharacter(String name, Character defaultValue) {
        return CONFIG.getCharacter(name, defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public static Boolean getBoolean(String name) {
        return CONFIG.getBoolean(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static Boolean getBoolean(String name, Boolean defaultValue) {
        return CONFIG.getBoolean(name, defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public static Byte getByte(String name) {
        return CONFIG.getByte(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static Byte getByte(String name, Byte defaultValue) {
        return CONFIG.getByte(name, defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public static Short getShort(String name) {
        return CONFIG.getShort(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static Short getShort(String name, Short defaultValue) {
        return CONFIG.getShort(name, defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public static Integer getInteger(String name) {
        return CONFIG.getInteger(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static Integer getInteger(String name, Integer defaultValue) {
        return CONFIG.getInteger(name, defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public static Float getFloat(String name) {
        return CONFIG.getFloat(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static Float getFloat(String name, Float defaultValue) {
        return CONFIG.getFloat(name, defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public static Double getDouble(String name) {
        return CONFIG.getDouble(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static Double getDouble(String name, Double defaultValue) {
        return CONFIG.getDouble(name, defaultValue);
    }

    /**
     * @param name
     * @return String
     */
    public static Long getLong(String name) {
        return CONFIG.getLong(name);
    }

    /**
     * @param name
     * @param defaultValue
     * @return String
     */
    public static Long getLong(String name, Long defaultValue) {
        return CONFIG.getLong(name, defaultValue);
    }

    /**
     * @param name
     * @param pattern
     * @return String
     */
    public static Date getDate(String name, String pattern) {
        return CONFIG.getDate(name, pattern);
    }

    /**
     * @param name
     * @param type
     * @return String
     */
    public static <T> T getObject(String name, Class<T> type) {
        return CONFIG.getObject(name, type);
    }

    /**
     * @param name
     * @return String
     */
    public static boolean has(String name) {
        return CONFIG.has(name);
    }

    /**
     * @param name
     * @param value
     * @return String
     */
    public static boolean contains(String name, String value) {
        return CONFIG.contains(name, value);
    }

    /**
     * @param config
     */
    public static void extend(Config config) {
        CONFIG.extend(config);
    }

    /**
     * @param config
     */
    public static void copy(Config config) {
        CONFIG.copy(config);
    }

    /**
     * @param map
     */
    public static void extend(Map<String, String> map) {
        CONFIG.extend(map);
    }

    /**
     * @param name
     * @param charset
     * @return Config
     */
    public static Config load(String name, String charset) {
        Properties properties = getProperties(name, charset);
        return new Config(properties);
    }

    /**
     * 
     */
    public static void save() {
        File file = getFile(RESOURCE);
        OutputStream outputStream = null;

        try {
            String content = build(RESOURCE, CONFIG);
            outputStream = new FileOutputStream(file);
            outputStream.write(content.getBytes("utf-8"));
            outputStream.flush();
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            IO.close(outputStream);
        }
    }

    /**
     * @param name
     * @param charset
     * @return Properties
     */
    public static Properties getProperties(String name, String charset) {
        InputStream inputStream = null;
        Properties properties = new Properties();

        try {
            inputStream = ConfigFactory.getInputStream(name);

            if(inputStream != null) {
                Map<String, String> map = Loader.getProperties(new InputStreamReader(inputStream, charset));
                properties.putAll(map);
            }
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            IO.close(inputStream);
        }
        return properties;
    }

    /**
     * @param name
     * @param config
     * @return String
     * @throws IOException
     */
    public static String build(String name, Config config) throws IOException {
        Reader reader = null;
        InputStream inputStream = null;

        try {
            if(config == null) {
                throw new NullPointerException("config is null.");
            }

            inputStream = ConfigFactory.getInputStream(name);

            if(inputStream == null) {
                throw new RuntimeException(name + " not exists.");
            }

            reader = new InputStreamReader(inputStream, "utf-8");
            return Loader.build(reader, config.getMap());
        }
        finally {
            IO.close(reader);
            IO.close(inputStream);
        }
    }

    /**
     * @param name
     * @return InputStream
     */
    public static InputStream getInputStream(String name) {
        File file = getFile(name);
        logger.debug("load: {}", file.getAbsolutePath());

        if(file.exists() && file.isFile()) {
            try {
                return new FileInputStream(file);
            }
            catch(IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * @return URL
     */
    public static String getLocation() {
        return LOCATION;
    }

    /**
     * @param name
     * @return File
     */
    public static File getFile(String name) {
        return new File(LOCATION, name);
    }

    /**
     * @param name
     * @return URL
     * @throws IOException
     */
    public static URL getResource(String name) throws IOException {
        return new File(LOCATION, name).toURI().toURL();
    }

    /**
     * 返回配置文件的位置
     * 用户目录优先
     * @return URL
     * @throws IOException
     */
    private static String getLocation(String name) {
        try {
            URL url = null;
            String conf = APP_CONF;

            if(conf == null || (conf = conf.trim()).length() < 1) {
                conf = "META-INF/conf/";
            }

            String location = conf + name;
            File file = ConfigFactory.getUserFile(location);

            if(file.exists()) {
                logger.info("file: {}", file.getAbsolutePath());
                return file.getAbsolutePath();
            }

            logger.info("classPath: {}", location);
            url = Loader.getResource(location);
            file = IO.getFile(url);

            if(file != null) {
                return file.getAbsolutePath();
            }
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @param resource
     * @return File
     */
    private static File getUserFile(String resource) {
        String userHome = System.getProperty("user.home");

        if(resource.startsWith("/") || resource.startsWith("\\")) {
            return new File(userHome, "skinx/" + APP_NAME + resource);
        }
        else {
            return new File(userHome, "skinx/" + APP_NAME + "/" + resource);
        }
    }
}
