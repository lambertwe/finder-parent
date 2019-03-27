/*
 * $RCSfile: UserPolicyServlet.java,v $
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

import com.skin.finder.admin.AdminController;
import com.skin.finder.admin.servlet.template.UserFilePolicyTemplate;
import com.skin.finder.admin.servlet.template.UserHostEditTemplate;
import com.skin.finder.admin.servlet.template.UserHostPolicyTemplate;
import com.skin.finder.cluster.Cluster;
import com.skin.finder.cluster.ClusterManager;
import com.skin.finder.cluster.Host;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: UserPolicyServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class UserPolicyServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    /**
     * 用户可访问的host列表页面
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.user.host.policy")
    public void userHostPolicy(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(AdminController.check(request, response)) {
            return;
        }
        UserHostPolicyTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.user.file.policy")
    public void userFilePolicy(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(AdminController.check(request, response)) {
            return;
        }
        String[] hosts = this.getHostNameList();
        request.setAttribute("hosts", hosts);
        UserFilePolicyTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.user.host.edit")
    public void userHostEdit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(AdminController.check(request, response)) {
            return;
        }

        String[] hosts = this.getHostNameList();
        request.setAttribute("hosts", hosts);
        UserHostEditTemplate.execute(request, response);
    }

    /**
     * @return String[]
     */
    private String[] getHostNameList() {
        Cluster cluster = ClusterManager.getInstance();
        List<Host> hosts = cluster.getHosts();

        if(hosts == null || hosts.isEmpty()) {
            return new String[0];
        }

        String[] names = new String[hosts.size()];

        for(int i = 0; i < names.length; i++) {
            Host host = hosts.get(i);
            names[i] = host.getName();
        }
        return names;
    }
}
