/*
 * $RCSfile: SecurityConfigServlet.java,v $
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
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.acl.User;
import com.skin.finder.acl.UserManager;
import com.skin.finder.admin.AdminController;
import com.skin.finder.admin.servlet.template.SecurityConfigEditTemplate;
import com.skin.finder.cluster.Agent;
import com.skin.finder.cluster.ConfigManager;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.config.Constants;
import com.skin.finder.security.KeyGenerator;
import com.skin.finder.service.ServiceFactory;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.StringUtil;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: SecurityConfigServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SecurityConfigServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfigServlet.class);

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.security.rsa.generate")
    public void getRandomString(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int keySize = this.getInteger(request, "keySize", 512);

        if(keySize < 128) {
            keySize = 128;
        }

        if(keySize > 2048) {
            keySize = 2048;
        }

        try {
            KeyPair keyPair = KeyGenerator.getKeyPair("RSA", keySize);
            String publicKey = KeyGenerator.getPublicKey(keyPair);
            String privateKey = KeyGenerator.getPrivateKey(keyPair);

            StringBuilder buffer = new StringBuilder();
            buffer.append("{\"privateKey\":\"");
            buffer.append(privateKey);
            buffer.append("\",\"publicKey\":\"");
            buffer.append(publicKey);
            buffer.append("\"}");
            Ajax.success(request, response, buffer.toString());
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
            Ajax.error(request, response, "finder.system.error");
        }
    }

    /**
     * 接收master的rename指令
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.security.config.edit")
    public void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), false)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }
        SecurityConfigEditTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.security.config.save")
    public void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }

        int version = ConfigFactory.getVersion() + 1;
        String adminName = this.getInputValue(request, "adminName");
        String securityKey = this.getInputValue(request, "securityKey");
        String privateKey = this.getInputValue(request, "privateKey");
        String publicKey = this.getInputValue(request, "publicKey");
        String sessionName = this.getInputValue(request, "sessionName");
        String sessionKey = this.getInputValue(request, "sessionKey");
        String masterName = ConfigFactory.getMaster();
        UserManager userManager = ServiceFactory.getUserManager();
        User admin = userManager.getByName(adminName);

        if(admin == null) {
            Ajax.error(request, response, "管理员帐号不存在。");
            return;
        }

        if(StringUtil.isBlank(securityKey)) {
            securityKey = UUID.randomUUID().toString();
        }

        if(StringUtil.isBlank(sessionKey)) {
            sessionKey = UUID.randomUUID().toString();
        }

        if(StringUtil.isBlank(sessionName)) {
            sessionName = "passport";
        }

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.CLUSTER_MASTER_NAME, masterName);
        params.put(Constants.CLUSTER_SECURITY_KEY, securityKey);
        params.put(Constants.CLUSTER_SECURITY_ROOT, adminName);
        params.put(Constants.PUBLIC_KEY, publicKey);
        params.put(Constants.PRIVATE_KEY, privateKey);
        params.put(Constants.SESSION_NAME, sessionName);
        params.put(Constants.SESSION_KEY, sessionKey);
        params.put(Constants.CONF_VERSION, Integer.toString(version));

        boolean save = false;
        boolean sync = false;

        try {
            ConfigManager.sync(params);
            sync = true;
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }

        try {
            /**
             * 保存到本地
             */
            ConfigFactory.extend(params);
            ConfigFactory.save();
            save = true;
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }

        if(sync && save) {
            Ajax.success(request, response, "true");
        }
        else {
            Ajax.error(request, response, getError(save, sync));
        }
    }

    /**
     * @param save
     * @param sync
     * @return String
     */
    private String getError(boolean save, boolean sync) {
        StringBuilder error = new StringBuilder();

        if(save) {
            error.append("1. 保存配置成功！\r\n");
        }
        else {
            error.append("1. 保存配置失败！\r\n");
        }

        if(sync) {
            error.append("2. 同步集群成功！\r\n");
        }
        else {
            error.append("2. 同步集群失败，请稍后手动同步！\r\n");
        }
        return error.toString();
    }
}
