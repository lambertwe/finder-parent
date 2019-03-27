/*
 * $RCSfile: JarResourceManager.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.util.IO;

/**
 * <p>Title: JarResourceManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class JarResourceManager extends ResourceManager {
    private String home;
    private static final Logger logger = LoggerFactory.getLogger(JarResourceManager.class);

    /**
     * @param file
     * @param home
     */
    public JarResourceManager(String file, String home) {
        super(file);
        this.home = home;
        this.reload();
    }

    /**
     * reload
     */
    public void reload() {
        File file = new File(this.file);
        Map<String, ContentEntry> temp = this.cache;
        this.cache = this.load(file, this.home);
        this.lastModified = file.lastModified();

        if(temp != null) {
            temp.clear();
        }
    }

    /**
     * @param file
     * @param home
     * @return Map<String, ContentEntry>
     */
    protected Map<String, ContentEntry> load(File file, String home) {
        InputStream inputStream = null;
        JarInputStream jarInputStream = null;
        JarFile jarFile = null;
        Map<String, ContentEntry> map = new HashMap<String, ContentEntry>(512);

        if(!file.exists() || !file.isFile()) {
            logger.error("{} not exists. load resource failed.");
            return map;
        }

        try {
            JarEntry entry = null;
            jarFile = new JarFile(file);
            inputStream = new FileInputStream(file);
            jarInputStream = new JarInputStream(inputStream);
            Pattern pattern = Pattern.compile(this.compress);

            while((entry = jarInputStream.getNextJarEntry()) != null) {
                if(entry.isDirectory()) {
                    continue;
                }

                String name = entry.getName();

                if(!name.startsWith("/")) {
                    name = "/" + name;
                }

                if(name.endsWith(".class") || !name.startsWith(home)) {
                    continue;
                }

                if(home.length() > 1) {
                    name = name.substring(home.length());
                }

                String extension = getExtension(name);
                byte[] bytes = IO.read(jarFile.getInputStream(entry), 4096);
                long lastModified = entry.getTime();
                logger.info("cache: {}", name);

                if(pattern.matcher(extension).matches()) {
                    map.put(name, new ContentEntry(name, ContentEntry.ZIP, true, lastModified, gzip(bytes)));
                }
                else {
                    map.put(name, new ContentEntry(name, ContentEntry.BIN, true, lastModified, bytes));
                }
            }
        }
        catch(IOException e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            IO.close(jarInputStream);
            IO.close(inputStream);

            if(jarFile != null) {
                try {
                    jarFile.close();
                }
                catch(IOException e) {
                }
            }
        }
        return map;
    }
}
