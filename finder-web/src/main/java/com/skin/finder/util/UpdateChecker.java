/*
 * $RCSfile: UpdateChecker.java,v $
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
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.config.App;

/**
 * <p>Title: UpdateChecker</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class UpdateChecker {
    private static Map<String, String> context = new java.util.concurrent.ConcurrentHashMap<String, String>();
    private static final Logger logger = LoggerFactory.getLogger(UpdateChecker.class);

    /**
     * 检查是否存在新版本
     */
    public static void check() {
        Map<String, String> map = execute();

        if(map != null) {
            context.clear();
            context.putAll(map);
        }

        String appUrl = App.getUrl();
        String oldVersion = App.getVersion();
        String newVersion = getVersion();

        if(compare(oldVersion, newVersion) > 0) {
            logger.info("new update(s) found: {} [{}]", newVersion, appUrl);
        }
        else {
            logger.info("version - old: {}, new: {}", oldVersion, newVersion);
        }
    }

    /**
     * 检查是否存在新版本
     * @return boolean
     */
    public static boolean has() {
        String oldVersion = App.getVersion();
        String newVersion = getVersion();
        return (compare(oldVersion, newVersion) > 0);
    }

    /**
     * @param s1
     * @param s2
     * @return int
     */
    public static int compare(String s1, String s2) {
        if(s1 == null && s2 == null) {
            return 0;
        }

        if(s1 != null && s2 == null) {
            return -9999;
        }

        if(s1 == null && s2 != null) {
            return 9999;
        }
        return s2.compareTo(s1);
    }

    /**
     * @return String
     */
    public static String getVersion() {
        return context.get("version");
    }

    /**
     * @return String
     */
    public static String getDownloadURL() {
        return context.get("download");
    }

    /**
     * @return Map<String, String>
     */
    public static Map<String, String> execute() {
        String appUrl = App.getUrl();
        String appName = App.getName();
        String appVersion = App.getVersion();

        if(StringUtil.isBlank(appUrl)) {
            return null;
        }

        try {
            StringBuilder url = new StringBuilder();
            url.append(appUrl);
            url.append("/version.txt?v=");
            url.append(URLEncoder.encode(appVersion, "utf-8"));
            url.append("&b=");
            url.append(URLEncoder.encode(getBuildTime(), "utf-8"));
            url.append("&t=");
            url.append(App.getTimestamp());
            logger.info("checking for available updated version of {}, {}", appName, url);

            String body = Http.get(url.toString());
            String config = getConfig(body);
            logger.info("config: {}", config);

            if(config != null) {
                return parse(config);
            }
        }
        catch(Exception e) {
            logger.debug("check version failed.");
        }
        return null;
    }

    /**
     * @return String
     */
    private static String getBuildTime() {
        String buildType = Manifest.getBuildType();
        String buildTime = Manifest.getBuildTime();
        StringBuilder buffer = new StringBuilder();
        buffer.append(buildType);
        buffer.append("-");

        if(buildTime != null) {
            char c;

            for(int i = 0; i < buildTime.length(); i++) {
                c = buildTime.charAt(i);

                switch(c) {
                    case '_':
                    case ' ':
                    case ':': {
                        buffer.append('-');
                        break;
                    }
                    default: {
                        buffer.append(c);
                        break;
                    }
                }
            }
        }
        else {
            buffer.append("unknown");
        }
        return buffer.toString();
    }

    /**
     * @param config
     * @return Map<String, String>
     * @throws IOException
     */
    private static Map<String, String> parse(String config) throws IOException {
        StringReader stringReader = new StringReader(config);
        BufferedReader buffer = new BufferedReader(stringReader);
        Map<String, String> map = new HashMap<String, String>();
        String lastUpdateTime = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        map.put("lastUpdateTime", lastUpdateTime);

        String line = null;
        String name = null;
        String value = null;

        while((line = buffer.readLine()) != null) {
            line = line.trim();

            if(line.length() < 1) {
                continue;
            }

            /**
             * 配置项
             */
            if(!line.startsWith("$# ")) {
                continue;
            }

            int k = line.indexOf(":");

            if(k > -1) {
                name = line.substring(3, k).trim();
                value = line.substring(k + 1).trim();
                map.put(name, value);
            }
            else {
                map.put(line, "true");
            }
        }
        return map;
    }

    /**
     * @param body
     * @return String
     */
    private static String getConfig(String body) {
        if(body == null) {
            return null;
        }

        String tag = "$# ........................................................";
        int s = body.indexOf(tag);

        if(s < 0) {
            return null;
        }

        int e = body.indexOf(tag, s + tag.length());

        if(e < 0) {
            return null;
        }

        String result = body.substring(s + tag.length(), e);
        result = Html.remove(result);
        result = Html.decode(result);
        result = replaceCRLF(result);
        return StringUtil.rtrim(result);
    }

    /**
     * @param text
     * @return String
     */
    private static String replaceCRLF(String text) {
        if(text == null) {
            return "";
        }

        char c;
        int i = 0;
        int length = text.length();
        StringBuilder buffer = new StringBuilder();

        while(i < length) {
            c = text.charAt(i);

            if(c == '\r') {
                i++;
            }
            else if(c == '\n') {
                buffer.append("\r\n");
                i++;
            }
            else {
                buffer.append(c);
                i++;
            }
        }
        return buffer.toString();
    }

    /**
     * @return Runnable
     */
    public static Runnable getWorker() {
        final Logger log = logger;

        return new Runnable() {
            @Override
            public void run() {
                log.debug("UpdateChecker.check...");
                UpdateChecker.check();
            }
        };
    }

    /**
     * 
     */
    private static ScheduledExecutorService service;

    /**
     * 
     */
    private static ScheduledExecutorService schedule() {
        Runnable runnable = getWorker();
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 30L * 60L, 30L * 60L, TimeUnit.SECONDS);
        return service;
    }

    /**
     * 
     */
    public static void start() {
        shutdown();
        service = schedule();
    }

    /**
     * shutdown
     */
    public static void shutdown() {
        if(service != null) {
            try {
                logger.debug("ScheduledExecutorService.shutdown...");
                service.shutdown();

                if(!service.awaitTermination(30000, TimeUnit.MILLISECONDS)){
                    service.shutdownNow();
                }
                logger.debug("ScheduledExecutorService.shutdown");
            }
            catch(InterruptedException e) {
                service.shutdownNow();
            }
        }
    }
}
