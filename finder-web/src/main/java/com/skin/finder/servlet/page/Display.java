/*
 * $RCSfile: Display.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.servlet.page;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.servlet.template.ErrorTemplate;

/**
 * <p>Title: Display</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Display {
    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public static void error(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        error(request, response, 500, "系统错误，请稍后再试！");
    }

    /**
     * @param request
     * @param response
     * @param code
     * @param message
     * @throws ServletException
     * @throws IOException
     */
    public static void error(HttpServletRequest request, HttpServletResponse response, int code, String message) throws IOException, ServletException {
        message(request, response, code, "Error - " + code, message);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public static void notFound(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        message(request, response, 404, "Not Found", "请求的资源不存在。");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public static void denied(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        message(request, response, 403, "Access Denied", "请联系系统管理员开通权限。");
    }

    /**
     * @param request
     * @param response
     * @param code
     * @param title
     * @param message
     * @throws IOException
     * @throws ServletException
     */
    public static void message(HttpServletRequest request, HttpServletResponse response, int code, String title, String message) throws IOException, ServletException {
        request.setAttribute("code", code);
        request.setAttribute("title", title);
        request.setAttribute("message", message);
        ErrorTemplate.execute(request, response);
    }
}
