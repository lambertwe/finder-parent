/*
 * $RCSfile: AdminCtrlServlet.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 * http://www.finderweb.net
 */
package com.skin.finder.admin.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.Finder;
import com.skin.finder.admin.AdminController;
import com.skin.finder.admin.servlet.template.AdminTemplate;
import com.skin.finder.cache.Cache;
import com.skin.finder.cache.CacheFactory;
import com.skin.finder.cluster.Agent;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.util.Ajax;
import com.skin.finder.web.Response;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: AdminServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class AdminServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AdminServlet.class);

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.index")
    public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.lock")
    public void lock(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }

        String location = ConfigFactory.getLocation();
        File file = new File(location, "host.lock");

        if(!file.exists()) {
            file.createNewFile();
        }
        Ajax.success(request, response, "true");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.tail.close")
    public void close(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }

        boolean type = this.getBoolean(request, "type", true);
        File file = Finder.getFile(request);
        String realPath = file.getAbsolutePath();
        logger.info("tail.close: {}", realPath);

        Cache cache = CacheFactory.getInstance();

        if(type) {
            cache.put(realPath, 24 * 60 * 60, Boolean.TRUE);
        }
        else {
            cache.remove(realPath);
        }
        Response.write(request, response, "text/plain; charset=utf-8", type + ": success");
    }
}

