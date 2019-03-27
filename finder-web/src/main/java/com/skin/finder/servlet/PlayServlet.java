/*
 * $RCSfile: PlayServlet.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 * http://www.finderweb.net
 */
package com.skin.finder.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.Finder;
import com.skin.finder.acl.AccessController;
import com.skin.finder.cluster.Agent;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.servlet.page.Display;
import com.skin.finder.servlet.template.PlayTemplate;
import com.skin.finder.util.IP;
import com.skin.finder.util.Path;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;
import com.skin.finder.web.util.CurrentUser;

/**
 * <p>Title: PlayServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class PlayServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.play")
    public void play(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String userName = CurrentUser.getUserName(request);
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String home = Finder.getWork(request, workspace);

        if(home == null) {
            Display.error(request, response, 404, "The workspace \"" + workspace + "\" does not exist or is not a directory!");
            return;
        }

        String realPath = Finder.getRealPath(home, path);

        if(realPath == null) {
            Display.denied(request, response);
            return;
        }

        if(!AccessController.getRead(userName, workspace, path)) {
            Display.denied(request, response);
            return;
        }

        File file = new File(realPath);

        if(!file.exists() || file.isDirectory()) {
            Display.error(request, response, 404, path + " not exists !");
            return;
        }

        String relativePath = Path.getRelativePath(home, realPath);
        request.setAttribute("localIp", IP.LOCAL);
        request.setAttribute("host", ConfigFactory.getHostName());
        request.setAttribute("workspace", workspace);
        request.setAttribute("work", home);
        request.setAttribute("path", relativePath);
        PlayTemplate.execute(request, response);
    }
}
