/*
 * $RCSfile: WorkspaceServlet.java,v $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.admin.AdminController;
import com.skin.finder.admin.servlet.template.WorkspaceEditTemplate;
import com.skin.finder.admin.servlet.template.WorkspacesTemplate;
import com.skin.finder.cluster.Agent;
import com.skin.finder.cluster.Cluster;
import com.skin.finder.cluster.ClusterManager;
import com.skin.finder.cluster.Host;
import com.skin.finder.cluster.Workspace;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.servlet.page.Display;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.Naming;
import com.skin.finder.util.StringUtil;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: WorkspaceServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class WorkspaceServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(WorkspaceServlet.class);

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("admin.workspace.list")
    public void list(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), false)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }

        String hostName = request.getParameter("hostName");

        if(StringUtil.isBlank(hostName)) {
            Display.error(request, response, 500, "系统错误，缺少参数！");
            return;
        }

        Host host = ClusterManager.getHost(hostName);
        request.setAttribute("host", host);
        WorkspacesTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.workspace.edit")
    public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(AdminController.check(request, response, true)) {
            return;
        }

        String hostName = request.getParameter("hostName");
        String workspaceName = request.getParameter("workspaceName");

        if(StringUtil.isBlank(hostName)) {
            Display.error(request, response, 500, "系统错误，缺少参数！");
            return;
        }

        if(StringUtil.notBlank(workspaceName)) {
            Workspace workspace = ClusterManager.getWorkspace(hostName, workspaceName);

            if(workspace == null) {
                Display.error(request, response, 404, "host not exists!");
                return;
            }
            request.setAttribute("oldName", workspaceName);
            request.setAttribute("workspace", workspace);
        }
        request.setAttribute("hostName", hostName);
        WorkspaceEditTemplate.execute(request, response);
    }

    /**
     * 删除host
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.workspace.setValue")
    public void setValue(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        if(AdminController.check(request, response, true)) {
            return;
        }

        String hostName = request.getParameter("hostName");
        String workspaceName = request.getParameter("workspaceName");
        String displayName = request.getParameter("displayName");
        String work = request.getParameter("work");
        String charset = request.getParameter("charset");
        Boolean readonly = this.getBoolean(request, "readonly");
        Integer orderNum = this.getInteger(request, "orderNum");

        Cluster cluster = ClusterManager.getInstance();
        Workspace workspace = cluster.getWorkspace(hostName, workspaceName);

        if(workspace == null) {
            Ajax.error(request, response, "工作空间不存在！");
            return;
        }

        if(StringUtil.notBlank(displayName)) {
            workspace.setDisplayName(displayName);
        }

        if(StringUtil.notBlank(work)) {
            workspace.setWork(work);
        }

        if(charset != null) {
            workspace.setCharset(charset);
        }

        if(readonly != null) {
            workspace.setReadonly(readonly.booleanValue());
        }

        if(orderNum != null) {
            workspace.setOrderNum(orderNum.intValue());
        }
        cluster.modified();
        ClusterManager.restore();
        Ajax.error(request, response, "true");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.workspace.save")
    public void save(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        if(AdminController.check(request, response, true)) {
            return;
        }

        /**
         * 只检查编辑, 其他情况都不检查
         */
        if(AdminController.getLock()) {
            Ajax.error(request, response, "编辑功能已被锁定，请先登录服务器手动解锁。");
            return;
        }

        String hostName = this.getTrimString(request, "hostName");
        String oldName = this.getTrimString(request, "oldName");

        if(StringUtil.isBlank(hostName)) {
            Display.error(request, response, 500, "系统错误，缺少参数！");
            return;
        }

        if(StringUtil.isBlank(oldName)) {
            this.create(request, response);
        }
        else {
            this.update(request, response, oldName);
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.workspace.delete")
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        if(AdminController.check(request, response, true)) {
            return;
        }

        String hostName = this.getTrimString(request, "hostName");
        String workspaceName = this.getTrimString(request, "workspaceName");
        Cluster cluster = ClusterManager.getInstance();
        Host host = cluster.getHost(hostName);

        if(host == null) {
            Ajax.success(request, response, "true");
            return;
        }

        host.remove(workspaceName);
        cluster.modified();
        ClusterManager.restore();
        Ajax.success(request, response, "true");
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    private void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String hostName = this.getTrimString(request, "hostName");
        String workspaceName = this.getTrimString(request, "workspaceName");
        String displayName = this.getTrimString(request, "displayName");
        String work = this.getTrimString(request, "work");
        String charset = this.getTrimString(request, "charset");
        boolean readonly = this.getBoolean(request, "readonly", true);
        logger.info("create: hostName: {}, workspaceName: {}, work: {}", hostName, workspaceName, work);

        if(!Naming.check(workspaceName)) {
            Ajax.error(request, response, "非法的名称！");
            return;
        }

        Cluster cluster = ClusterManager.getInstance();
        Host host = cluster.getHost(hostName);
        Workspace workspace = ClusterManager.getWorkspace(hostName, workspaceName);

        if(host == null) {
            Ajax.error(request, response, "添加失败，主机不存在！");
            return;
        }

        if(workspace != null) {
            Ajax.error(request, response, "添加失败，工作空间已存在！");
            return;
        }

        workspace = new Workspace();
        workspace.setName(workspaceName);
        workspace.setDisplayName(displayName);
        workspace.setWork(work);
        workspace.setCharset(charset);
        workspace.setReadonly(readonly);
        workspace.setOrderNum(host.size() + 1);

        host.add(workspace);
        cluster.modified();
        ClusterManager.restore();
        Ajax.success(request, response, "true");
    }

    /**
     * @param request
     * @param response
     */
    private void update(HttpServletRequest request, HttpServletResponse response, String oldName) throws IOException {
        String hostName = this.getTrimString(request, "hostName");
        String workspaceName = this.getTrimString(request, "workspaceName");
        String displayName = this.getTrimString(request, "displayName");
        String work = this.getTrimString(request, "work");
        String charset = this.getTrimString(request, "charset");
        boolean readonly = this.getBoolean(request, "readonly", true);
        logger.info("update: hostName: {}, workspaceName: {}, work: {}", hostName, workspaceName, work);

        if(!Naming.check(hostName)) {
            Ajax.error(request, response, "非法的主机名！");
            return;
        }

        boolean modified = !(oldName.equals(workspaceName));
        Cluster cluster = ClusterManager.getInstance();
        Host host = cluster.getHost(hostName);
        Workspace oldWorkspace = cluster.getWorkspace(hostName, oldName);
        Workspace newWorkspace = cluster.getWorkspace(hostName, workspaceName);

        if(host == null) {
            Ajax.error(request, response, "更新失败，主机不存在！");
            return;
        }

        if(oldWorkspace == null) {
            Ajax.error(request, response, "更新失败，工作空间不存在或者已经被删除！");
            return;
        }

        /**
         * 
         */
        if(modified && newWorkspace != null) {
            Ajax.error(request, response, "更新失败，工作空间已存在！");
            return;
        }

        oldWorkspace.setName(workspaceName);
        oldWorkspace.setDisplayName(displayName);
        oldWorkspace.setWork(work);
        oldWorkspace.setCharset(charset);
        oldWorkspace.setReadonly(readonly);

        if(modified) {
            host.remove(oldName);
            host.add(oldWorkspace);
        }

        cluster.modified();
        ClusterManager.restore();
        Ajax.success(request, response, "true");
    }
}
