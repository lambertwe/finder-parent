/*
 * $RCSfile: ResourceServlet.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 * http://www.finderweb.net
 */
package com.skin.finder.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.config.ConfigFactory;
import com.skin.finder.i18n.ScriptFactory;
import com.skin.finder.util.ClassUtil;
import com.skin.finder.util.IO;
import com.skin.finder.util.Loader;
import com.skin.finder.util.MimeType;
import com.skin.finder.web.ContentEntry;
import com.skin.finder.web.JarResourceManager;
import com.skin.finder.web.MemResourceManager;
import com.skin.finder.web.ResourceManager;
import com.skin.finder.web.Startup;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;
import com.skin.finder.web.servlet.Httpd;

/**
 * <p>Title: ResourceServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
@Startup(value = 0)
public class ResourceServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    private ResourceManager resourceManager;
    private static final int MAX_AGE = 30 * 60; // 7 * 24 * 60 * 60;
    private static final Logger logger = LoggerFactory.getLogger(ResourceServlet.class);

    /**
     *
     */
    @Override
    public void init() {
        this.resourceManager = getResourceManager("/resource");
    }

    /**
     * @param servletConfig
     */
    @Override
    public void init(ServletConfig servletConfig) {
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("res")
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        service(request, response, this.resourceManager);
    }

    /**
     * @param request
     * @param response
     * @param resourceManager
     * @throws ServletException
     * @throws IOException
     */
    public static void service(HttpServletRequest request, HttpServletResponse response, ResourceManager resourceManager) throws ServletException, IOException {
        String path = request.getParameter("path");
        ContentEntry contentEntry = resourceManager.get(path);

        if(contentEntry == null) {
            response.setHeader("NotFound", path);
            response.sendError(404);
            return;
        }

        long timeMillis = System.currentTimeMillis();
        long lastModified = contentEntry.getLastModified();
        String contentType = MimeType.getMimeType(contentEntry.getName());
        String eTag = "W/\"f-" + lastModified + "\"";

        if(!Httpd.checkIfHeaders(request, response, eTag, lastModified)) {
            return;
        }

        response.addHeader("Cache-Control", "max-age=" + MAX_AGE);
        response.addHeader("Cache-Control", "public");
        response.setDateHeader("Expires", timeMillis + (MAX_AGE * 1000L));
        response.setDateHeader("Last-Modified", lastModified);
        response.setDateHeader("Date", lastModified);
        response.setHeader("ETag", eTag);
        response.setContentType(contentType);

        int type = contentEntry.getType();
        byte[] bytes = contentEntry.getBytes();
        OutputStream outputStream = response.getOutputStream();
        String acceptEncoding = request.getHeader("Accept-Encoding");

        if(type == ContentEntry.ZIP) {
            if(acceptEncoding != null && acceptEncoding.indexOf("gzip") > -1) {
                response.setContentLength(bytes.length);
                response.setHeader("Content-Encoding", "gzip");
                outputStream.write(bytes, 0, bytes.length);
            }
            else {
                byte[] buffer = ResourceManager.ungzip(bytes);
                response.setContentLength(buffer.length);
                outputStream.write(buffer, 0, buffer.length);
            }
        }
        else {
            response.setContentLength(bytes.length);
            outputStream.write(bytes, 0, bytes.length);
        }
        outputStream.flush();
    }

    /**
     * @param timeMillis
     * @return String
     */
    protected String format(long timeMillis) {
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(new Date(timeMillis));
    }

    /**
     * @param home
     * @return ResourceManager
     */
    public static ResourceManager getResourceManager(String home) {
        ResourceManager resourceManager = null;
        Class<?> type = ResourceServlet.class;
        File file = ClassUtil.getJarFile(type);

        try {
            if(file != null) {
                logger.info("load resource from {}", file.getAbsolutePath());
                resourceManager = new JarResourceManager(file.getAbsolutePath(), home);
            }
            else {
                return new MemResourceManager(".");
            }

            loadI18N(resourceManager);
            loadPlugins(resourceManager);
            return resourceManager;
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @param resourceManager
     * @return ResourceManager
     */
    public static ResourceManager loadI18N(ResourceManager resourceManager) {
        try {
            long lastModified = System.currentTimeMillis();

            String[] list = new String[]{
                    "en.properties",
                    "zh_cn.properties",
                    "zh_tw.properties"
            };

            for(String name : list) {
                ContentEntry contentEntry = resourceManager.get("/lang/" + name);

                if(contentEntry != null) {
                    byte[] bytes = contentEntry.getBytes();

                    if(contentEntry.getType() == ContentEntry.ZIP) {
                        bytes = ResourceManager.ungzip(bytes);
                    }

                    Map<String, String> properties = Loader.getProperties(bytes, "utf-8");
                    String script = ScriptFactory.build(properties);
                    String path = "/finder/lang/" + name.substring(0, name.length() - 11) + ".js";
                    resourceManager.put(path, true, lastModified, script.getBytes("utf-8"));
                }
            }
        }
        catch(IOException e) {
           logger.error(e.getMessage(), e);
        }
        return resourceManager;
    }

    /**
     * @param resourceManager
     * @return ResourceManager
     */
    public static ResourceManager loadPlugins(ResourceManager resourceManager) {
        try {
            byte[] bytes = null;
            URL url = ConfigFactory.getResource("plugins/plugins.js");
            File file = IO.getFile(url);

            if(file != null && file.exists() && file.isFile()) {
                bytes = IO.read(file);
            }
            else {
                bytes = "Finder.plugins = [];".getBytes();
            }

            String path = "/finder/plugins.js";
            long lastModified = System.currentTimeMillis();
            resourceManager.put(path, true, lastModified, bytes);
        }
        catch(IOException e) {
           logger.error(e.getMessage(), e);
        }
        return resourceManager;
    }

    /**
     * destory
     */
    @Override
    public void destroy() {
        if(this.resourceManager != null) {
            this.resourceManager.destroy();
        }
    }
}

