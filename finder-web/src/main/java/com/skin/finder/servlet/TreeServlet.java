/*
 * $RCSfile: TreeServlet.java,v $
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.Finder;
import com.skin.finder.FinderManager;
import com.skin.finder.acl.Permission;
import com.skin.finder.acl.PermissionManager;
import com.skin.finder.acl.UserHostManager;
import com.skin.finder.acl.UserPermission;
import com.skin.finder.cluster.Agent;
import com.skin.finder.cluster.Cluster;
import com.skin.finder.cluster.ClusterManager;
import com.skin.finder.cluster.Host;
import com.skin.finder.cluster.Workspace;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.web.Response;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;
import com.skin.finder.web.util.CurrentUser;

/**
 * <p>Title: TreeServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class TreeServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final String EMPTY = "<?xml version=\"1.0\" encoding=\"utf-8\"?><tree></tree>";
    private static final Logger logger = LoggerFactory.getLogger(TreeServlet.class);

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.getHostXml")
    public void getHostXml(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), false)) {
            return;
        }

        String userName = CurrentUser.getUserName(request);
        String requestURI = request.getRequestURI();
        String listUrl = requestURI + "?action=finder.display&amp;";
        String xmlUrl = requestURI + "?action=finder.getWorkspaceXml&amp;";
        List<Host> hosts = this.getHosts(userName);

        String xml = FinderManager.getHostXml(hosts, listUrl, xmlUrl);
        Response.setCache(response, 0);
        Response.write(request, response, "text/xml; charset=UTF-8", xml);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.getWorkspaceXml")
    public void getWorkspaceXml(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String hostName = ConfigFactory.getHostName();
        String userName = CurrentUser.getUserName(request);
        String requestURI = request.getRequestURI();
        String listUrl = requestURI + "?action=finder.display&amp;host=" + hostName + "&amp;";
        String xmlUrl = requestURI + "?action=finder.getFolderXml&amp;host=" + hostName + "&amp;";
        List<Workspace> workspaces = this.getWorkspaces(userName, hostName);
        logger.debug("self: {}", hostName);

        String xml = FinderManager.getWorkspaceXml(workspaces, listUrl, xmlUrl);
        Response.setCache(response, 0);
        Response.write(request, response, "text/xml; charset=utf-8", xml);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.getFolderXml")
    public void getFolderXml(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String host = ConfigFactory.getHostName();
        String userName = CurrentUser.getUserName(request);
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String work = Finder.getWork(request, workspace);

        if(work == null) {
            Response.write(request, response, "text/xml; charset=utf-8", EMPTY);
            return;
        }

        String requestURI = request.getRequestURI();
        String listUrl = requestURI + "?action=finder.display&amp;host=" + URLEncoder.encode(host, "utf-8") + "&amp;";
        String xmlUrl = requestURI + "?action=finder.getFolderXml&amp;host=" + URLEncoder.encode(host, "utf-8") + "&amp;";
        FinderManager finderManager = new FinderManager(work);

        String xml = finderManager.getFolderXml(userName, workspace, path, listUrl, xmlUrl);
        Response.setCache(response, 0);
        Response.write(request, response, "text/xml; charset=utf-8", xml);
    }

    /**
     * @param userName
     * @return List<Host>
     */
    private List<Host> getHosts(String userName) {
        String root = ConfigFactory.getAdmin();
        Cluster cluster = ClusterManager.getInstance();

        if(userName.equals(root)) {
            return cluster.getHosts();
        }

        List<Host> hosts = cluster.getHosts();
        List<String> names = UserHostManager.getHostList(userName);

        if(names == null || names.isEmpty()) {
            return null;
        }

        List<Host> list = new ArrayList<Host>();

        for(Host host : hosts) {
            if(names.contains(host.getName())) {
                list.add(host);
            }
        }
        return list;
    }

    /**
     * @param userName
     * @param hostName
     * @return List<Workspace>
     */
    private List<Workspace> getWorkspaces(String userName, String hostName) {
        String root = ConfigFactory.getAdmin();
        Cluster cluster = ClusterManager.getInstance();
        Host host = cluster.getHost(hostName);

        if(host == null) {
            return null;
        }

        if(userName.equals(root)) {
            return host.getWorkspaces();
        }

        UserPermission userPermission = PermissionManager.getPermission(userName);
        Permission permission = userPermission.getPermission("read@@");

        if(permission == null) {
            return null;
        }

        List<String> includes = permission.getIncludes();

        if(includes == null || includes.isEmpty()) {
            return null;
        }

        if(includes.contains("*")) {
            return host.getWorkspaces();
        }

        List<Workspace> workspaces = host.getWorkspaces();
        List<Workspace> result = new ArrayList<Workspace>();

        for(Workspace workspace : workspaces) {
            if(includes.contains(workspace.getName())) {
                result.add(workspace);
            }
        }
        return result;
    }
}

