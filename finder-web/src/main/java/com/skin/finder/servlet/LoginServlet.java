/*
 * $RCSfile: LoginServlet.java,v $
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
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.acl.SimpleUserManager;
import com.skin.finder.acl.User;
import com.skin.finder.acl.UserManager;
import com.skin.finder.acl.UserSession;
import com.skin.finder.cache.Cache;
import com.skin.finder.cache.CacheFactory;
import com.skin.finder.cluster.Agent;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.security.Password;
import com.skin.finder.security.SecurityParameter;
import com.skin.finder.servlet.template.LoginTemplate;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.URLParameter;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;
import com.skin.finder.web.util.Client;

/**
 * <p>Title: LoginServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class LoginServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.login")
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String master = ConfigFactory.getMaster();

        if(Agent.dispatch(request, response, master, true)) {
            return;
        }

        URLParameter parameters = SecurityParameter.getURLParameter(request, new String[]{"userName", "password"});
        String userName = parameters.getTrimString("userName");
        String password = parameters.getTrimString("password");
        long timestamp = parameters.getLong("timestamp", 0L);
        response.setHeader("Cluster-Node", request.getLocalAddr() + ":" + request.getLocalPort());

        if(userName.length() < 1 || password.length() < 1) {
            response.setHeader("Finder-Signin", "false");
            LoginTemplate.execute(request, response);
            return;
        }

        if(Math.abs(System.currentTimeMillis() - timestamp) > 300L * 1000L) {
            response.setHeader("Finder-Signin", "false");
            response.setHeader("Signin-Timeout", Long.toString(timestamp));
            Ajax.error(request, response, 501, "登录超时，请刷新页面重试！");
            return;
        }

        UserManager userManager = SimpleUserManager.getInstance();
        User user = userManager.getByName(userName);
        logger.info("login: {}, ******", userName);

        if(user == null) {
            Ajax.error(request, response, 501, "用户不存在！");
            return;
        }

        String userSalt = user.getUserSalt();
        String userPass = Password.encode(password, userSalt);

        if(userPass.equals(user.getPassword())) {
            long userId = user.getUserId();
            String clientId = String.valueOf(System.currentTimeMillis());
            Date createTime = new Date();

            UserSession userSession = new UserSession();
            userSession.setAppId(1L);
            userSession.setUserId(userId);
            userSession.setUserName(userName);
            userSession.setNickName(userName);
            userSession.setClientId(clientId);
            userSession.setCreateTime(createTime);
            userSession.setLastAccessTime(createTime);

            int sessionTimeout = getSessionTimeOut();
            String certificate = Client.registe(request, response, userSession, null, "/", sessionTimeout, false);

            /**
             * 只有master提供登录服务, 验证会话只在master上
             */
            Cache cache = CacheFactory.getInstance();
            cache.put("session:" + userName, 60 * 60, certificate);
            Ajax.success(request, response, "true");
        }
        else {
            Ajax.error(request, response, 501, "密码错误！");
            return;
        }
    }

    /**
     * @param request
     * @param response
     * @param userName
     */
    public static void login(HttpServletRequest request, HttpServletResponse response, String userName) {
        Date sysTime = new Date();
        String clientId = String.valueOf(System.currentTimeMillis());
        UserSession userSession = new UserSession();
        userSession.setAppId(1L);
        userSession.setUserId(1L);
        userSession.setUserName(userName);
        userSession.setNickName(userName);
        userSession.setClientId(clientId);
        userSession.setCreateTime(sysTime);
        userSession.setLastAccessTime(sysTime);
        request.setAttribute(Client.SESSION_NAME, userSession);
    }

    /**
     * @return int
     */
    private static int getSessionTimeOut() {
        String value = ConfigFactory.getSessionTimeout();
        int timeout = parse(value);
        return (timeout >= 60 ? timeout : 0);
    }

    /**
     * @param value
     * @return int
     */
    private static int parse(String value) {
        if(value == null) {
            return 0;
        }

        String temp = value.trim();

        if(temp.length() < 1) {
            return 0;
        }

        char c;
        char u = 's';
        String n = temp;

        for(int i = 0; i < temp.length(); i++) {
            c = temp.charAt(i);

            /**
             * '.' = 46
             * '0' = 48
             * '9' = 57
             */
            if((c >= 48 && c <= 57) || c == 46) {
                continue;
            }
            else {
                n = temp.substring(0, i);
                u = Character.toLowerCase(temp.charAt(i));
                break;
            }
        }

        float v = 0.0f;

        try {
            v = Float.parseFloat(n);
        }
        catch(NumberFormatException e) {
        }

        if(Float.isNaN(v)) {
            return 0;
        }

        if(u == 'd') {
            return (int)(v * 24 * 60 * 60);
        }
        else if(u == 'h') {
            return (int)(v * 60 * 60);
        }
        else if(u == 'm') {
            return (int)(v * 60);
        }
        else if(u == 's') {
            return (int)(v);
        }
        else {
            return 0;
        }
    }
}
