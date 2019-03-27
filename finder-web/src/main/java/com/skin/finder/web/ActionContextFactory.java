/*
 * $RCSfile: ActionContextFactory.java,v $
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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: ActionContextFactory</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ActionContextFactory {
    private ClassLoader classLoader;
    private static final Class<HttpServlet> SERVLET = HttpServlet.class;
    private static final Logger logger = LoggerFactory.getLogger(ActionContextFactory.class);

    /**
     * @param classLoader
     */
    public ActionContextFactory(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * @param servletContext
     * @param packages
     * @return ActionContext
     */
    public ActionContext create(ServletContext servletContext, String[] packages) {
        ActionContext actionContext = new ActionContext(servletContext);
        logger.info("scan action...");

        if(!logger.isInfoEnabled()) {
            return actionContext;
        }

        if(packages != null) {
            for(int i = 0; i < packages.length; i++) {
                this.load(actionContext, packages[i]);
            }
            actionContext.init();
        }
        logger.info("init complete.");
        return actionContext;
    }

    /**
     * @param packageName
     */
    private void load(ActionContext actionContext, String packageName) {
        String path = packageName.replace('.', '/');
        java.util.Enumeration<URL> urls = null;

        try {
            urls = this.getClassLoader().getResources(path);
        }
        catch(IOException e) {
            return;
        }

        while(urls.hasMoreElements()) {
            String urlPath = urls.nextElement().getFile();

            try {
                urlPath = URLDecoder.decode(urlPath, "utf-8");
            }
            catch(UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            if(urlPath.startsWith("file:")) {
                urlPath = urlPath.substring(5);
            }

            if(urlPath.indexOf('!') > 0) {
                urlPath = urlPath.substring(0, urlPath.indexOf('!'));
            }

            File file = new File(urlPath);

            if(file.isDirectory()) {
                loadFromDirectory(actionContext, path, file);
            }
            else {
                loadFromJar(actionContext, path, file);
            }
        }
    }

    /**
     * @param parent
     * @param location
     */
    private void loadFromDirectory(ActionContext actionContext, String parent, File location) {
        File files[] = location.listFiles();

        for(int i = 0, length = files.length; i < length; i++) {
            File file = files[i];
            StringBuilder buffer = new StringBuilder(100);
            buffer.append(parent).append("/").append(file.getName());
            String packageOrClass = parent != null ? buffer.toString() : file.getName();

            if(file.isDirectory()) {
                loadFromDirectory(actionContext, packageOrClass, file);
                continue;
            }

            if(file.getName().endsWith(".class")) {
                this.add(actionContext, packageOrClass);
            }
        }
    }

    /**
     * @param parent
     * @param jarfile
     */
    private void loadFromJar(ActionContext actionContext, String parent, File jarfile) {
        InputStream inputStream = null;
        JarInputStream jarInputStream = null;

        try {
            JarEntry entry = null;
            inputStream = new FileInputStream(jarfile);
            jarInputStream = new JarInputStream(new FileInputStream(jarfile));

            while((entry = jarInputStream.getNextJarEntry()) != null) {
                String name = entry.getName();

                if(!entry.isDirectory() && name.startsWith(parent) && name.endsWith(".class")) {
                    add(actionContext, name);
                }
            }
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if(jarInputStream != null) {
                try {
                    jarInputStream.close();
                }
                catch(IOException e) {
                }
            }

            if(inputStream != null) {
                try {
                    inputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
    }

    /**
     * @param actionContext
     * @param fqn
     */
    private void add(ActionContext actionContext, String fqn) {
        String path = null;
        Class<?> type = null;
        String className = fqn.substring(0, fqn.indexOf('.')).replace('/', '.');

        try {
            type = getClassLoader().loadClass(className);
        }
        catch(ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(Modifier.isAbstract(type.getModifiers())) {
            return;
        }

        if(Modifier.isInterface(type.getModifiers())) {
            return;
        }

        if(!SERVLET.isAssignableFrom(type)) {
            return;
        }

        Namespace namespace = type.getAnnotation(Namespace.class);

        if(namespace != null) {
            path = namespace.value();
        }

        if(path == null) {
            path = "";
        }

        Method[] methods = type.getMethods();

        for(Method method : methods) {
            if(!Modifier.isPublic(method.getModifiers())) {
                continue;
            }

            UrlPattern urlPattern = method.getAnnotation(UrlPattern.class);

            if(urlPattern == null) {
                continue;
            }

            String[] values = urlPattern.value();

            if(values == null || values.length < 1) {
                continue;
            }

            for(String url : values) {
                Method old = actionContext.getMethod(url);

                if(old != null) {
                    Class<?> clazz = old.getDeclaringClass();

                    if(!className.equals(clazz.getName())) {
                        throw new RuntimeException("class: " + clazz.getName() + " - " + url + " already exists: " + type.getName());
                    }
                    else {
                        /**
                         * ignore
                         */
                    }
                }
                else {
                    actionContext.setAction(url, method);
                    logger.info("[{}]: {}.{}", url, type.getName(), method.getName());
                }
            }
        }
    }

    /**
     * @return ClassLoader
     */
    public ClassLoader getClassLoader() {
        return (this.classLoader != null ? this.classLoader : Thread.currentThread().getContextClassLoader());
    }

    /**
     * @param classLoader
     */
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
}
