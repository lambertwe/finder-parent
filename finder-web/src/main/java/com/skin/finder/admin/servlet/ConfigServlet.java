/*
 * $RCSfile: ConfigServlet.java,v $
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
import com.skin.finder.admin.servlet.template.ConfigEditTemplate;
import com.skin.finder.admin.servlet.template.ConfigListTemplate;
import com.skin.finder.cluster.Agent;
import com.skin.finder.cluster.Cluster;
import com.skin.finder.cluster.ClusterManager;
import com.skin.finder.cluster.ConfigManager;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.config.Constants;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.StringUtil;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: ConfigServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ConfigServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ConfigServlet.class);

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.config.list")
    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), false)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }

        String masterName = ConfigFactory.getMaster();
        int confVersion = ConfigFactory.getVersion();
        Cluster cluster = ClusterManager.getInstance();

        request.setAttribute("masterName", masterName);
        request.setAttribute("confVersion", confVersion);
        request.setAttribute("cluster", cluster);
        ConfigListTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.config.edit")
    public void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), false)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }
        ConfigEditTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.config.save")
    public void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }

        int version = ConfigFactory.getVersion() + 1;
        String sessionTimeout = this.getInputValue(request, "sessionTimeout");
        String textType = this.getInputValue(request, "textType");
        String operateButton = this.getInputValue(request, "operateButton");
        String uploadPartSize = this.getInputValue(request, "uploadPartSize");
        String demoUserName = this.getInputValue(request, "demoUserName");
        String demoPassword = this.getInputValue(request, "demoPassword");
        String updateCheck = this.getInputValue(request, "updateCheck");
        String masterName = ConfigFactory.getMaster();

        if(StringUtil.isBlank(demoUserName) || StringUtil.isBlank(demoUserName)) {
            demoUserName = "";
            demoPassword = "";
        }

        if(StringUtil.notBlank(operateButton)) {
            operateButton = getOperateButton(operateButton);
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.CLUSTER_MASTER_NAME, masterName);
        params.put(Constants.SESSION_TIMEOUT, sessionTimeout);
        params.put(Constants.TEXT_TYPE, textType);
        params.put(Constants.DISPLAY_OPERATE_BUTTON, operateButton);
        params.put(Constants.UPLOAD_PART_SIZE, uploadPartSize);
        params.put(Constants.DEMO_USERNAME, demoUserName);
        params.put(Constants.DEMO_PASSWORD, demoPassword);
        params.put(Constants.UPDATE_CHECK, updateCheck);
        params.put(Constants.CONF_VERSION, Integer.toString(version));

        try {
            ConfigFactory.extend(params);
            ConfigFactory.save();
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
            Ajax.error(request, response, "finder.system.error");
            return;
        }

        try {
            ConfigManager.sync(params);
            Ajax.success(request, response, "true");
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
            Ajax.error(request, response, "保存成功，同步集群失败！请稍后手动同步！");
            return;
        }
    }

    /**
     * @param value
     * @return String
     */
    private static String getOperateButton(String value) {
        if(StringUtil.isBlank(value)) {
            return "";
        }

        String[] array = value.split(",");
        String[] buttons = new String[]{"open", "download", "delete"};
        StringBuilder buffer = new StringBuilder();

        if(StringUtil.contains(array, "tail")
                || StringUtil.contains(array, "less")
                || StringUtil.contains(array, "grep")) {
            buffer.append("tail, less, grep");
        }

        for(int i = 0; i < buttons.length; i++) {
            if(StringUtil.contains(array, buttons[i])) {
                if(buffer.length() > 0) {
                    buffer.append(", ");
                }
                buffer.append(buttons[i]);
            }
        }
        return buffer.toString();
    }
}
