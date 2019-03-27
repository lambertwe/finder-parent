/*
 * $RCSfile: UserServlet.java,v $
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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.acl.SimpleUserManager;
import com.skin.finder.acl.User;
import com.skin.finder.acl.UserManager;
import com.skin.finder.admin.AdminController;
import com.skin.finder.admin.servlet.template.UserEditTemplate;
import com.skin.finder.admin.servlet.template.UserListTemplate;
import com.skin.finder.admin.servlet.template.UserQueryListTemplate;
import com.skin.finder.cluster.Agent;
import com.skin.finder.cluster.ConfigManager;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.config.Constants;
import com.skin.finder.security.Password;
import com.skin.finder.security.SecurityParameter;
import com.skin.finder.service.ServiceFactory;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.RandomUtil;
import com.skin.finder.util.StringUtil;
import com.skin.finder.util.URLParameter;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: UserServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class UserServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AgentServlet.class);

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.user.list")
    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), false)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }

        int pageNum = this.getInteger(request, "pageNum", 1);
        int pageSize = this.getInteger(request, "pageSize", 20);
        UserManager userManager = SimpleUserManager.getInstance();

        int userCount = userManager.getUserCount();
        List<User> userList = userManager.getUserList(pageNum, pageSize);

        request.setAttribute("pageNum", pageNum);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("userCount", userCount);
        request.setAttribute("userList", userList);
        UserListTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.user.query")
    public void query(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), false)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }

        String userName = this.getTrimString(request, "userName");

        if(StringUtil.notBlank(userName)) {
            UserManager userManager = SimpleUserManager.getInstance();
            List<User> userList = new ArrayList<User>();
            User user = userManager.getByName(userName);

            if(user != null) {
                userList.add(user);
            }

            request.setAttribute("userName", userName);
            request.setAttribute("userList", userList);
            request.setAttribute("userCount", userList.size());
            request.setAttribute("pageNum", 1);
            request.setAttribute("pageSize", 20);
        }
        UserQueryListTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.user.edit")
    public void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), false)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }

        String userName = this.getTrimString(request, "userName");
        request.setAttribute("userName", userName);
        UserEditTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.user.delete")
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }

        String userName = this.getTrimString(request, "userName");

        if(userName.length() < 1) {
            Ajax.error(request, response, 501, "用户名不能为空！");
            return;
        }

        SimpleUserManager userManager = SimpleUserManager.getInstance();
        userManager.delete(userName);
        Ajax.success(request, response, "true");
        return;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.user.save")
    public void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }

        URLParameter parameters = SecurityParameter.getURLParameter(request, new String[]{"userName", "password"});
        String userName = parameters.getTrimString("userName");
        String password = parameters.getTrimString("password");
        SimpleUserManager userManager = SimpleUserManager.getInstance();

        if(userName.length() < 1 || password.length() < 1) {
            Ajax.error(request, response, 501, "用户名或者密码不能为空！");
            return;
        }

        if(!userManager.check(userName)) {
            Ajax.error(request, response, 501, "用户名只能包含英文字母或者数字且不少于4个字符！");
            return;
        }

        if(password.length() > 20) {
            Ajax.error(request, response, 501, "密码不能超过20个字符！");
            return;
        }

        String userSalt = RandomUtil.getRandString(8, 8);
        String userPass = Password.encode(password, userSalt);
        User user = userManager.getByName(userName);

        if(user == null) {
            user = new User();
            user.setUserName(userName);
            user.setUserSalt(userSalt);
            user.setPassword(userPass);
            userManager.create(user);
        }
        else {
            user.setUserName(userName);
            user.setUserSalt(userSalt);
            user.setPassword(userPass);
            userManager.update(user);
        }
        Ajax.success(request, response, "true");
        return;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.user.asadmin")
    public void asAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), true)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }

        String userName = request.getParameter("userName");

        if(StringUtil.isBlank(userName)) {
            Ajax.error(request, response, "缺少参数！");
            return;
        }

        try {
            UserManager userManager = ServiceFactory.getUserManager();
            User user = userManager.getByName(userName);

            if(user == null) {
                Ajax.error(request, response, "用户不存在！");
                return;
            }

            ConfigFactory.setValue(Constants.CLUSTER_SECURITY_ROOT, userName);
            ConfigFactory.save();

            ConfigManager.sync(Constants.CLUSTER_SECURITY_ROOT, userName);
            Ajax.success(request, response, "true");
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
            Ajax.error(request, response, "finder.system.error");
        }
    }
}
