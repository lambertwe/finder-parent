/*
 * $RCSfile: TreeMenuServlet.java,v $
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

import com.skin.finder.acl.SimpleUserManager;
import com.skin.finder.acl.UserManager;
import com.skin.finder.admin.AdminController;
import com.skin.finder.admin.servlet.template.MenuXmlTemplate;
import com.skin.finder.admin.servlet.template.TreeMenuTemplate;
import com.skin.finder.service.ServiceFactory;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: TreeMenuServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class TreeMenuServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("admin.menu")
    public void list(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(AdminController.check(request, response)) {
            return;
        }
        TreeMenuTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @UrlPattern("admin.menu.xml")
    public void getMenuXml(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(AdminController.check(request, response)) {
            return;
        }

        UserManager userManager = ServiceFactory.getUserManager();
        boolean userSupport = userManager.getClass().getName().equals(SimpleUserManager.class.getName());
        request.setAttribute("userSupport", userSupport);
        MenuXmlTemplate.execute(request, response);
    }
}
