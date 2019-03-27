/*
 * $RCSfile: API.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.cluster;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.config.ConfigFactory;
import com.skin.finder.config.Constants;
import com.skin.finder.util.IO;
import com.skin.finder.util.JSONParser;
import com.skin.finder.util.ReturnValue;

/**
 * <p>Title: API</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class API {
    private static final Logger logger = LoggerFactory.getLogger(API.class);

    /**
     * @param address
     * @param data
     * @return ReturnValue<?>
     * @throws IOException
     */
    public static ReturnValue<?> invoke(String address, String data) throws IOException {
        URL url = null;
        HttpURLConnection connection = null;
        OutputStream outputStream = null;
        byte[] bytes = null;
        String securityKey = ConfigFactory.getSecurityKey();

        try {
            url = new URL(address);
            connection = (HttpURLConnection)(url.openConnection());
            connection.setConnectTimeout(1 * 60 * 1000);
            connection.setReadTimeout(1 * 60 * 1000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Security-Key", securityKey);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            if(data != null) {
                bytes = data.getBytes("utf-8");
            }

            if(bytes != null) {
                connection.setRequestProperty("Content-Length", Integer.toString(bytes.length));
                outputStream = connection.getOutputStream();
                outputStream.write(bytes, 0, bytes.length);
                outputStream.flush();
            }
            else {
                connection.setRequestProperty("Content-Length", "0");
            }

            int status = connection.getResponseCode();

            if(status == 200) {
                String body = IO.toString(connection.getInputStream(), "utf-8");
                logger.debug("{}: {}", address, body);
                return API.parse(body);
            }
            else {
                return ReturnValue.error(Integer.toString(status));
            }
        }
        finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * @param address
     * @param bytes
     * @throws IOException
     */
    protected static boolean send(String address, byte[] bytes) throws IOException {
        URL url = null;
        HttpURLConnection connection = null;
        OutputStream outputStream = null;
        String securityKey = ConfigFactory.getSecurityKey();

        try {
            url = new URL(address);
            connection = (HttpURLConnection)(url.openConnection());
            connection.setConnectTimeout(1 * 60 * 1000);
            connection.setReadTimeout(1 * 60 * 1000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty(Constants.HTTP_SECURITY_KEY, securityKey);
            connection.setRequestProperty("Content-Type", "application/octet-stream");

            if(bytes != null) {
                connection.setRequestProperty("Content-Length", Integer.toString(bytes.length));
                outputStream = connection.getOutputStream();
                outputStream.write(bytes, 0, bytes.length);
                outputStream.flush();
            }
            else {
                connection.setRequestProperty("Content-Length", "0");
            }

            int status = connection.getResponseCode();
            String body = IO.toString(connection.getInputStream(), "utf-8");
            logger.debug("body: {}, {}", status, body);

            ReturnValue<Object> returnValue = API.parse(body);
            return returnValue.success();
        }
        finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * @param content
     * @return Map<String, Object>
     */
    @SuppressWarnings("unchecked")
    public static ReturnValue<Object> parse(String content) {
        try {
            Object json = new JSONParser().parse(content);

            if(json instanceof Map<?, ?>) {
                Map<String, Object> map = (Map<String, Object>)(json);
                int status = getInt(map, "status", 999);
                String message = getString(map, "message", "unknown");
                Object value = map.get("value");
                return new ReturnValue<Object>(status, message, value);
            }
        }
        catch(Exception e) {
        }
        return ReturnValue.error("finder.system.error");
    }

    /**
     * @param map
     * @return boolean
     */
    public static boolean success(Map<String, Object> map) {
        if(map == null) {
            return false;
        }

        Object status = map.get("status");

        if(status instanceof Number) {
            return (((Number)status).intValue() == ReturnValue.SUCCESS);
        }
        return false;
    }

    /**
     * @param map
     * @param name
     * @param defaultValue
     * @return int
     */
    public static int getInt(Map<String, Object> map, String name, int defaultValue) {
        if(map == null || name == null) {
            return defaultValue;
        }

        Object value = map.get(name);

        if(value instanceof Number) {
            return ((Number)value).intValue();
        }
        return defaultValue;
    }

    /**
     * @param map
     * @param name
     * @param defaultValue
     * @return String
     */
    public static String getString(Map<String, Object> map, String name, String defaultValue) {
        if(map == null || name == null) {
            return defaultValue;
        }

        Object value = map.get(name);

        if(value != null) {
            return value.toString();
        }
        return defaultValue;
    }
}
