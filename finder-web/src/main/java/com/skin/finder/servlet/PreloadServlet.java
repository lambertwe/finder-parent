/*
 * $RCSfile: PreloadServlet.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 * http://www.finderweb.net
 */
package com.skin.finder.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.servlet.page.Display;
import com.skin.finder.util.ClassUtil;
import com.skin.finder.util.UpdateChecker;
import com.skin.finder.web.Startup;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.command.RestartThread;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: PreloadServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
@Startup(value = 0)
public class PreloadServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(PreloadServlet.class);
    private static final Logger accessLogger = LoggerFactory.getLogger("accessLogger");

    /**
     * default
     */
    public PreloadServlet() {
    }

    /**
     *
     */
    @Override
    public void init() {
        logger.info("init");

        if(!logger.isInfoEnabled()) {
            throw new RuntimeException("logger init failed.");
        }

        if(!accessLogger.isInfoEnabled()) {
            throw new RuntimeException("logger init failed.");
        }

        /**
         * preload class
         */
        ClassUtil.preload("com.skin.finder.web.command.Kill");
        ClassUtil.preload("com.skin.finder.web.command.OS");
        ClassUtil.preload("com.skin.finder.web.command.ReadThread");
        ClassUtil.preload("com.skin.finder.web.command.Restart");
        ClassUtil.preload("com.skin.finder.web.command.RestartThread");
        ClassUtil.preload("com.skin.finder.web.command.Self");

        /**
         * init RestartThread class
         */
        logger.info("classpath: {}", RestartThread.getClassPath());
        new Thread(UpdateChecker.getWorker()).start();
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    @UrlPattern("finder.preload")
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Display.error(request, response, 404, "Not Found !");
    }

    /**
     * destory
     */
    @Override
    public void destroy() {
    }
}
