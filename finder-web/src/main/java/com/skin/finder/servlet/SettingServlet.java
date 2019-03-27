/*
 * $RCSfile: IndexServlet.java,v $
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

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.servlet.page.Display;
import com.skin.finder.servlet.template.LangTemplate;
import com.skin.finder.servlet.template.SettingMenuTemplate;
import com.skin.finder.servlet.template.SettingTemplate;
import com.skin.finder.servlet.template.SettingXmlTemplate;
import com.skin.finder.servlet.template.TextConfigTemplate;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: IndexServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SettingServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    /**
     * default
     */
    public SettingServlet() {
    }

    /**
     * @param servletContext
     */
    public SettingServlet(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * @param servletConfig
     */
    @Override
    public void init(ServletConfig servletConfig) {
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Display.notFound(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.setting")
    public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SettingTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("setting.menu")
    public void menu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SettingMenuTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("setting.getMenuXml")
    public void getMenuXml(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SettingXmlTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("setting.text")
    public void text(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        TextConfigTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("setting.lang")
    public void lang(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LangTemplate.execute(request, response);
    }
}
