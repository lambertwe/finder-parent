/*
 * $RCSfile: FilePolicyServlet.java,v $
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
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.acl.FilePolicyManager;
import com.skin.finder.acl.Permission;
import com.skin.finder.acl.PermissionManager;
import com.skin.finder.acl.UserPermission;
import com.skin.finder.admin.AdminController;
import com.skin.finder.cluster.Agent;
import com.skin.finder.cluster.ClusterManager;
import com.skin.finder.cluster.Host;
import com.skin.finder.cluster.Workspace;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.StringUtil;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: FilePolicyServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class FilePolicyServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(FilePolicyServlet.class);

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("admin.policy.getFilePolicy")
    public void getFilePolicy(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        if(AdminController.check(request, response, true)) {
            return;
        }

        String userName = request.getParameter("userName");
        List<String> workspaces = this.getWorkspaceNameList();
        UserPermission userPermission = PermissionManager.getPermission(userName);
        String result = this.stringify(workspaces, userPermission);
        Ajax.success(request, response, result);
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("admin.policy.file.save")
    public void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        if(AdminController.check(request, response, true)) {
            return;
        }

        String userName = request.getParameter("userName");
        String content = request.getParameter("content");
        PermissionManager.save(userName, content);
        Ajax.success(request, response, "true");
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("admin.policy.file.test")
    public void test(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(AdminController.check(request, response, true)) {
            return;
        }

        String operation = request.getParameter("operation");
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String content = request.getParameter("content");

        String key = operation + "@" + workspace;
        UserPermission userPermission = FilePolicyManager.parse("testx", new StringReader(content));
        Permission permission = userPermission.getPermission(key);

        if(permission == null) {
            Ajax.success(request, response, "Finder.system.error");
            return;
        }

        logger.info("content: {}", content);
        logger.info("test: {}, {}, {}", operation, workspace, path);
        boolean flag = permission.match(path);
        Ajax.success(request, response, Boolean.toString(flag));
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("admin.policy.file.flush")
    public void flush(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        if(AdminController.check(request, response, true)) {
            return;
        }

        String userName = request.getParameter("userName");
        PermissionManager.flush(userName);
        Ajax.success(request, response, "true");
    }

    /**
     * @return List<String>
     */
    private List<String> getWorkspaceNameList() {
        String hostName = ConfigFactory.getHostName();
        Host host = ClusterManager.getHost(hostName);
        List<Workspace> workspaces = host.getWorkspaces();

        if(workspaces == null) {
            return null;
        }

        List<String> result = new ArrayList<String>();

        for(Workspace workspace : workspaces) {
            result.add(workspace.getName());
        }
        return result;
    }

    /**
     * @param oldPermission
     * @param newPermission
     */
    protected void copy(UserPermission oldPermission, UserPermission newPermission) {
        if(oldPermission == null || newPermission == null) {
            return;
        }

        List<String> keys = oldPermission.getKeys();

        if(keys != null && keys.size() > 0) {
            for(String domain : keys) {
                Permission permission = oldPermission.getPermission(domain);

                if(permission != null) {
                    newPermission.add(domain, permission);
                }
            }
        }
    }

    /**
     * @param workspaces
     * @param userPermission
     * @return String
     */
    protected String stringify(List<String> workspaces, UserPermission userPermission) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{\"workspaces\":");
        buffer.append(this.stringify(workspaces));
        buffer.append(",\"permissions\":");
        buffer.append(this.stringify(userPermission));
        buffer.append("}");
        return buffer.toString();
    }

    /**
     * @param list
     * @return String
     */
    protected String stringify(List<String> list) {
        if(list == null) {
            return "null";
        }

        if(list.isEmpty()) {
            return "[]";
        }

        StringBuilder buffer = new StringBuilder();
        buffer.append("[");

        for(String item : list) {
            buffer.append("\"");
            buffer.append(StringUtil.escape(item));
            buffer.append("\",");
        }

        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * @param userPermission
     * @return String
     */
    protected String stringify(UserPermission userPermission) {
        if(userPermission == null) {
            return "null";
        }

        List<String> keys = userPermission.getKeys();

        if(keys == null || keys.size() < 1) {
            return "null";
        }

        String workspace = null;
        StringBuilder buffer = new StringBuilder();
        buffer.append("[");

        for(String domain : keys) {
            Permission permission = userPermission.getPermission(domain);

            if(permission == null) {
                continue;
            }

            workspace = permission.getWorkspace();

            if(workspace == null || workspace.equals("@")) {
                continue;
            }

            List<String> includes = permission.getIncludes();
            List<String> excludes = permission.getExcludes();

            if(includes != null && includes.size() > 0) {
                for(String pattern : includes) {
                    buffer.append("{\"workspace\":\"");
                    buffer.append(permission.getWorkspace());
                    buffer.append("\",\"pattern\":\"");
                    buffer.append(pattern);
                    buffer.append("\",\"action\":\"");
                    buffer.append(permission.getAction());
                    buffer.append("\"},");
                }
            }

            if(excludes != null && excludes.size() > 0) {
                for(String pattern : excludes) {
                    buffer.append("{\"workspace\":\"");
                    buffer.append(permission.getWorkspace());
                    buffer.append("\",\"pattern\":\"!");
                    buffer.append(pattern);
                    buffer.append("\",\"action\":\"");
                    buffer.append(permission.getAction());
                    buffer.append("\"},");
                }
            }
        }

        if(buffer.charAt(buffer.length() - 1) == ',') {
            buffer.deleteCharAt(buffer.length() - 1);
        }
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * @param userName
     * @param permission
     * @return String
     */
    protected String stringify(String userName, Permission permission) {
        StringBuilder buffer = FilePolicyManager.build(new StringBuilder(), userName, permission);
        String content = buffer.toString();
        buffer.setLength(0);
        buffer.append("\"");
        buffer.append(StringUtil.escape(content));
        buffer.append("\"");
        return buffer.toString();
    }
}
