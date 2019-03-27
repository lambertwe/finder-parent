/*
 * $RCSfile: StatusServlet.java,v $
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
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.cluster.Agent;
import com.skin.finder.config.App;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.StringUtil;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: StatusServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class StatusServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final byte[] OK = "ok".getBytes();
    private static final byte[] FAILED = "failed".getBytes();

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.status")
    public void status(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        response.setContentType("text/plain; charset=utf-8");
        response.setContentLength(OK.length);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(OK, 0, OK.length);
        outputStream.flush();
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.test")
    public void test(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String token = request.getParameter("token");
        response.setContentType("text/plain; charset=utf-8");
        OutputStream outputStream = response.getOutputStream();

        if(StringUtil.notBlank(token) && token.equals(App.getToken())) {
            response.setContentLength(FAILED.length);
            outputStream.write(FAILED, 0, FAILED.length);
            outputStream.flush();
            return;
        }

        response.setContentLength(OK.length);
        outputStream.write(OK, 0, OK.length);
        outputStream.flush();
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.getStatus")
    public void getStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        try {
            Ajax.success(request, response, App.getStatus());
        }
        catch(Exception e) {
            Ajax.error(request, response, "finder.system.error");
        }
    }
}
