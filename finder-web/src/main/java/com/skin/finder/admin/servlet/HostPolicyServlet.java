/*
 * $RCSfile: HostPolicyServlet.java,v $
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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.acl.SimpleUserManager;
import com.skin.finder.acl.User;
import com.skin.finder.acl.UserHostManager;
import com.skin.finder.acl.UserManager;
import com.skin.finder.admin.AdminController;
import com.skin.finder.cluster.Agent;
import com.skin.finder.cluster.ClusterManager;
import com.skin.finder.cluster.Host;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.util.Ajax;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: HostPolicyServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class HostPolicyServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("admin.policy.getHostPolicy")
    public void getHostPolicy(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        if(AdminController.check(request, response, true)) {
            return;
        }

        String userName = request.getParameter("userName");
        List<String> hosts = UserHostManager.getHostList(userName);
        Ajax.success(request, response, Ajax.stringify(hosts));
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("admin.policy.host.add")
    public void add(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        if(AdminController.check(request, response, true)) {
            return;
        }

        String userName = request.getParameter("userName");
        String hostName = request.getParameter("hostName");

        UserManager userManager = SimpleUserManager.getInstance();
        User user = userManager.getByName(userName);

        if(user == null) {
            Ajax.error(request, response, 501, "用户不存在！");
            return;
        }

        Host host = ClusterManager.getHost(hostName);

        if(host == null) {
            Ajax.error(request, response, 501, "主机不存在！");
            return;
        }

        UserHostManager.add(userName, hostName);
        Ajax.success(request, response, "true");
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("admin.policy.host.remove")
    public void remove(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        if(AdminController.check(request, response, true)) {
            return;
        }

        String userName = request.getParameter("userName");
        String hostName = request.getParameter("hostName");
        UserHostManager.remove(userName, hostName);
        Ajax.success(request, response, "true");
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("admin.policy.host.flush")
    public void flush(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        if(AdminController.check(request, response, true)) {
            return;
        }

        String userName = request.getParameter("userName");
        UserHostManager.flush(userName);
        Ajax.success(request, response, "true");
    }
}
