/*
 * $RCSfile: Manifest.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: Manifest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Manifest {
    private static final Logger logger = LoggerFactory.getLogger(Manifest.class);
    private static final Properties properties = load();

    /**
     * @return String
     */
    public static String getVersion() {
        return getValue("Manifest-Version");
    }

    /**
     * @return String
     */
    public static String getCreated() {
        return getValue("Created-By");
    }

    /**
     * @return String
     */
    public static String getDeveloper() {
        return getValue("Developer");
    }

    /**
     * @return String
     */
    public static String getBuilt() {
        return getValue("Build-By");
    }

    /**
     * @return String
     */
    public static String getBuildType() {
        return getValue("Build-Type");
    }

    /**
     * @return String
     */
    public static String getBuildTime() {
        return getValue("Build-Time");
    }

    /**
     * @param name
     * @return String
     */
    public static String getValue(String name) {
        return properties.getProperty(name);
    }

    /**
     * @return Properties
     */
    public static Properties load() {
        Properties properties = new Properties();
        File jarFile = ClassUtil.getJarFile(Manifest.class);

        if(jarFile != null) {
            Map<String, Object> entry = getEntry(jarFile, "META-INF/MANIFEST.MF");

            if(entry != null) {
                try {
                    Long time = (Long)(entry.get("time"));
                    String content = (String)(entry.get("content"));
                    load(new StringReader(content), properties);

                    if(time != null && time.longValue() > 0) {
                        Date buildTime = new Date(time);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        properties.setProperty("Build-Time", dateFormat.format(buildTime));
                    }
                }
                catch(Exception e) {
                }
            }
        }
        else {
            properties.setProperty("Build-Type", "UC");
        }

        String buildType = properties.getProperty("Build-Type");
        String buildTime = properties.getProperty("Build-Time");

        if(buildType == null || buildType.trim().length() < 1) {
            properties.setProperty("Build-Type", "UJ");
        }

        if(buildTime == null || buildTime.trim().length() < 1) {
            properties.setProperty("Build-Time", "Unknown");
        }
        return properties;
    }

    /**
     * @param file
     * @param name
     * @return Map<String, Object>
     */
    private static Map<String, Object> getEntry(File file, String name) {
        JarFile jarFile = null;

        try {
            jarFile = new JarFile(file);
            ZipEntry zipEntry = jarFile.getEntry(name);

            if(zipEntry != null) {
                long time = zipEntry.getTime();
                String content = IO.toString(jarFile.getInputStream(zipEntry), "utf-8");

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", name);
                map.put("time", time);
                map.put("content", content);
                return map;
            }
        }
        catch(Exception e) {
            logger.error(e.getMessage());
        }
        finally {
            if(jarFile != null) {
                try {
                    jarFile.close();
                }
                catch(IOException e) {
                }
            }
        }
        return null;
    }

    /**
     * @param reader
     * @return Properties
     * @throws IOException
     */
    private static Properties load(Reader reader, Properties properties) throws IOException {
        String line = null;
        BufferedReader buffer = new BufferedReader(reader);

        while((line = buffer.readLine()) != null) {
            line = line.trim();

            if(line.length() < 1) {
                continue;
            }

            int i = line.indexOf(":");
            logger.debug(line);

            if(i > -1) {
                String name = line.substring(0, i).trim();
                String value = line.substring(i + 1).trim();

                if(name.length() > 0 && value.length() > 0) {
                    properties.setProperty(name, value);
                }
            }
        }
        return properties;
    }
}
