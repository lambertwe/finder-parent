/*
 * $RCSfile: HostServlet.java,v $
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
import com.skin.finder.admin.servlet.template.HostEditTemplate;
import com.skin.finder.admin.servlet.template.HostListTemplate;
import com.skin.finder.cluster.Agent;
import com.skin.finder.cluster.Cluster;
import com.skin.finder.cluster.ClusterManager;
import com.skin.finder.cluster.ConfigManager;
import com.skin.finder.cluster.Host;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.config.Constants;
import com.skin.finder.servlet.page.Display;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.Http;
import com.skin.finder.util.Naming;
import com.skin.finder.util.StringUtil;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: HostServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class HostServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(HostServlet.class);

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.host.list")
    public void list(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), false)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }

        String masterName = ConfigFactory.getMaster();
        Cluster cluster = ClusterManager.getInstance();

        request.setAttribute("cluster", cluster);
        request.setAttribute("masterName", masterName);
        HostListTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.host.edit")
    public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(AdminController.check(request, response, true)) {
            return;
        }

        Host host = null;
        String hostName = request.getParameter("hostName");

        if(StringUtil.notBlank(hostName)) {
            host = ClusterManager.getHost(hostName);

            if(host == null) {
                Display.error(request, response, 404, "host not exists!");
                return;
            }
            request.setAttribute("oldName", hostName);
            request.setAttribute("host", host);
        }
        HostEditTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.host.setValue")
    public void setValue(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        if(AdminController.check(request, response, true)) {
            return;
        }

        /**
         * 此处不允许hostName
         */
        String hostName = request.getParameter("hostName");
        String displayName = request.getParameter("displayName");
        String url = request.getParameter("url");
        Integer orderNum = this.getInteger(request, "orderNum");

        Cluster cluster = ClusterManager.getInstance();
        Host host = cluster.getHost(hostName);

        if(host == null) {
            Ajax.error(request, response, "主机不存在！");
            return;
        }

        if(StringUtil.notBlank(displayName)) {
            host.setDisplayName(displayName);
        }

        if(StringUtil.notBlank(url)) {
            host.setUrl(url);
        }

        if(orderNum != null) {
            host.setOrderNum(orderNum.intValue());
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
    @UrlPattern("admin.host.save")
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

        String oldName = this.getTrimString(request, "oldName");

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
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("admin.host.test")
    public void test(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        if(test(request, response, request.getParameter("hostUrl"))) {
            Ajax.success(request, response, "true");
        }
        else {
            Ajax.error(request, response, "目标机的URL访问失败！");
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.host.delete")
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        if(AdminController.check(request, response, true)) {
            return;
        }

        String hostName = this.getTrimString(request, "hostName");
        String masterName = ConfigFactory.getMaster();

        if(StringUtil.isBlank(hostName)) {
            Ajax.error(request, response, "系统错误，缺少参数。");
            return;
        }

        if(masterName.equals(hostName)) {
            Ajax.error(request, response, "不能删除Master节点！");
            return;
        }

        Cluster cluster = ClusterManager.getInstance();
        Host host = cluster.remove(hostName);

        if(host != null) {
            host.destroy();
            cluster.modified();
            ClusterManager.restore();
        }
        Ajax.success(request, response, "true");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.host.reload")
    public void reload(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        boolean flag = ClusterManager.reload();

        if(flag) {
            Ajax.success(request, response, "true");
        }
        else {
            Ajax.error(request, response, "重载失败，请稍后再试！");
        }
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    private void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String hostName = this.getTrimString(request, "hostName");
        String displayName = this.getTrimString(request, "displayName");
        String hostUrl = this.getTrimString(request, "hostUrl");
        logger.info("hostName: {}, displayName: {}, hostUrl: {}", hostName, displayName, hostUrl);

        if(!Naming.check(hostName)) {
            Ajax.error(request, response, "非法的主机名！");
            return;
        }

        String masterName = ConfigFactory.getMaster();
        Cluster cluster = ClusterManager.getInstance();
        Host host = cluster.getHost(hostName);

        if(host != null) {
            Ajax.error(request, response, "添加失败，主机已存在！");
            return;
        }

        if(!test(request, response, hostUrl)) {
            Ajax.error(request, response, "目标机的URL访问失败！");
            return;
        }

        if(!ConfigManager.rename(hostUrl, masterName, hostName)) {
            Ajax.error(request, response, "目标机重命名失败，请确保安全Key一致！");
            return;
        }

        host = new Host();
        host.setName(hostName);
        host.setDisplayName(displayName);
        host.setUrl(hostUrl);
        host.setOrderNum(cluster.size() + 1);
        cluster.add(host);
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
        String displayName = this.getTrimString(request, "displayName");
        String hostUrl = this.getTrimString(request, "hostUrl");
        logger.info("hostName: {}, displayName: {}, hostUrl: {}", hostName, displayName, hostUrl);

        if(!Naming.check(hostName)) {
            Ajax.error(request, response, "非法的主机名！");
            return;
        }

        boolean modified = !(oldName.equals(hostName));
        String masterName = ConfigFactory.getMaster();
        Cluster cluster = ClusterManager.getInstance();
        Host oldHost = cluster.getHost(oldName);
        Host newHost = cluster.getHost(hostName);

        if(oldHost == null) {
            Ajax.error(request, response, "更新失败，主机不存在或者已经被删除！");
            return;
        }

        /**
         * 
         */
        if(modified && newHost != null) {
            Ajax.error(request, response, "更新失败，主机已存在！");
            return;
        }

        if(!test(request, response, hostUrl)) {
            Ajax.error(request, response, "目标机的URL访问失败！");
            return;
        }

        if(modified) {
            if(masterName.equals(oldName)) {
                masterName = hostName;
                ConfigFactory.setValue(Constants.CLUSTER_MASTER_NAME, masterName);
                ConfigFactory.setValue(Constants.CLUSTER_NODE_NAME, hostName);
                ConfigFactory.save();
            }
            else {
                if(!ConfigManager.rename(hostUrl, masterName, hostName)) {
                    Ajax.error(request, response, "目标机重命名失败，请确保安全Key一致！");
                    return;
                }
            }
        }

        oldHost.setName(hostName);
        oldHost.setDisplayName(displayName);
        oldHost.setUrl(hostUrl);

        if(modified) {
            cluster.remove(oldName);
            cluster.add(oldHost);
        }

        cluster.modified();
        ClusterManager.restore();
        Ajax.success(request, response, "true");
    }

    /**
     * @param request
     * @param response
     * @param url
     * @return boolean
     */
    private static boolean test(HttpServletRequest request, HttpServletResponse response, String url) {
        try {
            String body = Http.get(url + "?action=finder.status");

            if(body != null && body.trim().equals("ok")) {
                return true;
            }
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }
}
