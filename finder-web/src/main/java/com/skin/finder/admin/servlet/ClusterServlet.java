/*
 * $RCSfile: ClusterServlet.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 * http://www.finderweb.net
 */
package com.skin.finder.admin.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.admin.AdminController;
import com.skin.finder.cluster.Agent;
import com.skin.finder.cluster.Cluster;
import com.skin.finder.cluster.ClusterManager;
import com.skin.finder.cluster.ClusterUtil;
import com.skin.finder.cluster.Host;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.IO;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: ClusterServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ClusterServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ClusterServlet.class);

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("admin.host.sync")
    public void sync(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        if(AdminController.check(request, response, true)) {
            return;
        }

        try {
            ClusterManager.sync();
            Ajax.success(request, response, "true");
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
            Ajax.error(request, response, "finder.system.error");
        }
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("admin.host.pull")
    public void pull(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        Cluster cluster = ClusterManager.getInstance();
        String xml = ClusterUtil.build(cluster);
        byte[] bytes = xml.getBytes("utf-8");

        response.setContentLength(bytes.length);
        response.setContentType("text/xml; charset=utf-8");
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("admin.host.push")
    public void push(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        if(AdminController.check(request, response, true)) {
            return;
        }

        String hostName = request.getParameter("hostName");
        Host host = ClusterManager.getHost(hostName);

        if(host == null) {
            Ajax.error(request, response, 500, "admin.host.not.exists");
            return;
        }

        try {
            boolean success = ClusterManager.send(host);
            Ajax.success(request, response, Boolean.toString(success));
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
            Ajax.error(request, response, "finder.system.error");
        }
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    @UrlPattern("admin.host.accept")
    public void accept(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!AdminController.hasSecurityKey(request)) {
            Ajax.denied(request, response);
            return;
        }

        String encoding = request.getCharacterEncoding();

        if(encoding == null) {
            encoding = "utf-8";
        }

        try {
            byte[] bytes = IO.getBytes(request.getInputStream());
            ClusterManager.restore(bytes, true);
            Ajax.success(request, response, "true");
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
            Ajax.error(request, response, "finder.system.error");
        }
    }
}
