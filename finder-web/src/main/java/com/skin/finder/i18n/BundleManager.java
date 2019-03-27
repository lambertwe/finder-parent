/*
 * $RCSfile: BundleManager.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.i18n;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.util.ClassUtil;

/**
 * <p>Title: BundleManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class BundleManager {
    private ConcurrentHashMap<String, FutureTask<LocalizationContext>> cache;
    private static final String BASEDIR = "resource/lang/";
    private static final BundleManager instance = new BundleManager();
    private static final Logger logger = LoggerFactory.getLogger(BundleManager.class);

    /**
     * default
     */
    private BundleManager() {
        this.cache = new ConcurrentHashMap<String, FutureTask<LocalizationContext>>();
    }

    /**
     * @return BundleManager
     */
    public static BundleManager getInstance() {
        return instance;
    }

    /**
     * @param baseName
     * @param locale
     * @return LocalizationContext
     */
    public LocalizationContext getBundle(final String baseName, final Locale locale) {
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String variant = locale.getVariant();
        StringBuilder buffer = new StringBuilder();

        if(baseName != null && baseName.length() > 0) {
            buffer.append(baseName);
            buffer.append("_");
        }

        buffer.append(language);
        int length = buffer.length();

        if(country != null && country.length() > 0) {
            buffer.append("_");
            buffer.append(country);
        }

        if(variant != null && variant.length() > 0) {
            buffer.append("_");
            buffer.append(variant);
        }

        LocalizationContext localizationContext = this.getCachedBundle(buffer.toString().toLowerCase(), locale);

        if(localizationContext != null) {
            localizationContext.setLang(buffer.toString().toLowerCase());
            return localizationContext;
        }

        buffer.setLength(length);

        if(country != null && country.length() > 0) {
            buffer.append("_");
            buffer.append(country);
        }
        localizationContext = this.getCachedBundle(buffer.toString().toLowerCase(), locale);

        if(localizationContext != null) {
            localizationContext.setLang(buffer.toString().toLowerCase());
            return localizationContext;
        }

        localizationContext = this.getCachedBundle(buffer.toString().toLowerCase(), locale);

        if(localizationContext != null) {
            localizationContext.setLang(buffer.toString().toLowerCase());
        }
        return localizationContext;
    }

    /**
     * @param fullName
     * @param locale
     * @return LocalizationContext
     */
    public LocalizationContext getCachedBundle(final String fullName, final Locale locale) {
        FutureTask<LocalizationContext> f = this.cache.get(fullName);

        if(f == null) {
            Callable<LocalizationContext> callable = new Callable<LocalizationContext>() {
                /**
                 * @throws InterruptedException
                 */
                @Override
                public LocalizationContext call() throws InterruptedException {
                    try {
                        ResourceBundle resourceBundle = getBaseBundle(fullName);

                        if(resourceBundle != null) {
                            return new LocalizationContext(resourceBundle, locale);
                        }
                        return null;
                    }
                    catch(Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };

            FutureTask<LocalizationContext> futureTask = new FutureTask<LocalizationContext>(callable);
            f = this.cache.putIfAbsent(fullName, futureTask);

            if(f == null) {
                f = futureTask;
                f.run();
            }
        }

        LocalizationContext localizationContext = null;

        try {
            localizationContext = f.get();
        }
        catch(Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return localizationContext;
    }

    /**
     * @param baseName
     * @param locale
     * @return String
     */
    protected String getFullName(String baseName, Locale locale) {
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String variant = locale.getVariant();

        StringBuilder buffer = new StringBuilder();
        buffer.append(baseName);
        buffer.append("_");
        buffer.append(language);

        if(country != null && country.length() > 0) {
            buffer.append("_");
            buffer.append(country);
        }

        if(variant != null && variant.length() > 0) {
            buffer.append("_");
            buffer.append(variant);
        }
        return buffer.toString();
    }

    /**
     * @param name
     * @return ResourceBundle
     */
    public ResourceBundle getBaseBundle(String name) {
        String path = BASEDIR + name.replace('.', '/') + ".properties";
        File file = ClassUtil.getJarFile(BundleManager.class);
        logger.info("load: {}", path);

        if(file == null) {
            return null;
        }

        JarFile jarFile = null;
        logger.info("load: {}, {}", file.getAbsolutePath(), path);

        try {
            jarFile = new JarFile(file);
            JarEntry jarEntry = jarFile.getJarEntry(path);

            if(jarEntry == null) {
                return null;
            }
            return new PropertyResourceBundle(jarFile.getInputStream(jarEntry));
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
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
}
