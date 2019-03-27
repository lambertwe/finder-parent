/*
 * $RCSfile: PasswordServlet.java,v $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.acl.SimpleUserManager;
import com.skin.finder.acl.User;
import com.skin.finder.cluster.Agent;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.security.Password;
import com.skin.finder.security.SecurityParameter;
import com.skin.finder.servlet.template.PasswordTemplate;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.RandomUtil;
import com.skin.finder.util.StringUtil;
import com.skin.finder.util.URLParameter;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;
import com.skin.finder.web.util.Client;
import com.skin.finder.web.util.CurrentUser;

/**
 * <p>Title: PasswordServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class PasswordServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("user.password")
    public void password(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = CurrentUser.getUserName(request);
        boolean allow = this.allow(userName);
        request.setAttribute("allow", allow);
        PasswordTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("user.password.update")
    public void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String master = ConfigFactory.getMaster();

        if(Agent.dispatch(request, response, master, true)) {
            return;
        }

        String userName = CurrentUser.getUserName(request);

        if(!this.allow(userName)) {
            Ajax.error(request, response, 403, "finder.system.access.denied.");
            return;
        }

        URLParameter parameters = SecurityParameter.getURLParameter(request, new String[]{"oldPass", "newPass"});
        String oldPassword = parameters.getTrimString("oldPass");
        String newPassword = parameters.getTrimString("newPass");
        long timestamp = parameters.getLong("timestamp", 0L);

        if(StringUtil.isBlank(oldPassword)) {
            Ajax.error(request, response, 504, "finder.system.password.oldpassword.empty.");
            return;
        }

        if(StringUtil.isBlank(newPassword)) {
            Ajax.error(request, response, 504, "finder.system.password.newPassword.empty.");
            return;
        }

        if(Math.abs(System.currentTimeMillis() - timestamp) > 300L * 1000L) {
            Ajax.error(request, response, 504, "finder.system.password.timeout.");
            return;
        }

        SimpleUserManager userManager = SimpleUserManager.getInstance();
        User user = userManager.getByName(userName);

        if(user == null) {
            Ajax.error(request, response, 504, "finder.system.user.not.exists.");
            return;
        }

        String oldSalt = user.getUserSalt();
        String oldPass = Password.encode(oldPassword, oldSalt);

        if(!oldPass.equals(user.getPassword())) {
            Ajax.error(request, response, 504, "finder.system.user.oldpassword.wrong.");
            return;
        }

        String newSalt = RandomUtil.getRandString(8, 8);
        String newPass = Password.encode(newPassword, newSalt);

        user.setUserName(userName);
        user.setUserSalt(newSalt);
        user.setPassword(newPass);
        userManager.update(user);
        Client.remove(response);
        Ajax.success(request, response, "true");
        return;
    }

    /**
     * @param userName
     * @return boolean
     */
    private boolean allow(String userName) {
        String root = ConfigFactory.getAdmin();

        if(!root.equals(userName)) {
            String envName = ConfigFactory.getEnvName();

            if(envName.equals("demo")) {
                return false;
            }
        }
        return true;
    }
}
