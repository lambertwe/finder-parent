/*
 * $RCSfile: AgentServlet.java,v $
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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.admin.AdminController;
import com.skin.finder.cluster.Agent;
import com.skin.finder.cluster.Cluster;
import com.skin.finder.cluster.ClusterManager;
import com.skin.finder.cluster.ClusterRestart;
import com.skin.finder.config.App;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.config.Constants;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.StringUtil;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.command.Restart;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: AgentServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class AgentServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AgentServlet.class);

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("agent.node.rename")
    public void registe(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!AdminController.hasSecurityKey(request)) {
            Ajax.denied(request, response);
            return;
        }

        String masterName = request.getParameter("masterName");
        String hostName = request.getParameter("hostName");

        if(StringUtil.isBlank(masterName) || StringUtil.isBlank(hostName)) {
            Ajax.error(request, response, "Illegal argument: masterName, hostName");
            return;
        }

        try {
            ConfigFactory.setValue(Constants.CLUSTER_MASTER_NAME, masterName);
            ConfigFactory.setValue(Constants.CLUSTER_NODE_NAME, hostName);
            ConfigFactory.save();
            Ajax.success(request, response, "true");
            return;
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
            Ajax.error(request, response, "Illegal argument: masterName, hostName");
            return;
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("agent.conf.sync")
    public void setConfig(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!AdminController.hasSecurityKey(request)) {
            Ajax.denied(request, response);
            return;
        }

        try {
            Map<?, ?> map = request.getParameterMap();
            Map<String, String> params = new HashMap<String, String>();

            for(Map.Entry<?, ?> entry : map.entrySet()) {
                if(entry.getKey() == null) {
                    continue;
                }

                String name = entry.getKey().toString();
                String value = request.getParameter(name);

                if(!name.startsWith("finder.") || value == null) {
                    continue;
                }

                logger.info("{} = {}", name, StringUtil.hide(value));
                params.put(name, value);
            }

            if(params.size() > 0) {
                ConfigFactory.extend(params);
                ConfigFactory.save();
            }
            Ajax.success(request, response, "true");
            return;
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
            Ajax.error(request, response, "finder.system.error");
            return;
        }
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("agent.host.status")
    public void getHostStatus(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        if(!AdminController.hasSecurityKey(request)) {
            if(AdminController.check(request, response, true)) {
                return;
            }
        }

        try {
            Ajax.success(request, response, App.getStatus());
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
    @UrlPattern("agent.host.version")
    public void getHostVersion(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        if(!AdminController.hasSecurityKey(request)) {
            if(AdminController.check(request, response, true)) {
                return;
            }
        }

        try {
            Cluster cluster = ClusterManager.getInstance();
            long version = cluster.getVersion();
            Ajax.success(request, response, Long.toString(version));
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
    @UrlPattern("agent.conf.version")
    public void getConfVersion(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        if(!AdminController.hasSecurityKey(request)) {
            if(AdminController.check(request, response, true)) {
                return;
            }
        }

        try {
            int version = ConfigFactory.getVersion();
            Ajax.success(request, response, Integer.toString(version));
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
    @UrlPattern("agent.node.restart")
    public void restart(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        if(!AdminController.hasSecurityKey(request)) {
            if(AdminController.check(request, response, true)) {
                return;
            }
        }

        try {
            String self = App.getToken();
            String token = request.getParameter("token");

            if(token != null && token.equals(self)) {
                Ajax.success(request, response, "false");
                return;
            }

            Restart.execute(1000L);
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
    @UrlPattern("agent.cluster.restart")
    public void reboot(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(!AdminController.hasSecurityKey(request)) {
            if(AdminController.check(request, response, true)) {
                return;
            }
        }

        try {
            ClusterRestart.execute();
            Ajax.success(request, response, "true");
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
            Ajax.error(request, response, "finder.system.error");
        }
    }
}
