/*
 * $RCSfile: EditServlet.java,v $
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

import com.skin.finder.servlet.template.EditPlusTemplate;
import com.skin.finder.servlet.template.EditorTemplate;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: EditorServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class EditorServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.editplus")
    public void editplus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EditPlusTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.edit")
    public void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String host = request.getParameter("host");
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String charset = request.getParameter("charset");

        request.setAttribute("host", host);
        request.setAttribute("workspace", workspace);
        request.setAttribute("path", path);
        request.setAttribute("charset", charset);
        EditorTemplate.execute(request, response);
    }
}
