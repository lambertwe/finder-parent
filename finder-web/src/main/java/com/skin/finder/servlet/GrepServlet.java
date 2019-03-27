/*
 * $RCSfile: GrepServlet.java,v $
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
import java.io.RandomAccessFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.FileRange;
import com.skin.finder.Finder;
import com.skin.finder.Grep;
import com.skin.finder.Less;
import com.skin.finder.TextType;
import com.skin.finder.acl.AccessController;
import com.skin.finder.cache.Cache;
import com.skin.finder.cache.CacheFactory;
import com.skin.finder.cluster.Agent;
import com.skin.finder.servlet.page.Display;
import com.skin.finder.servlet.template.GrepTemplate;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.IO;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;
import com.skin.finder.web.util.CurrentUser;

/**
 * <p>Title: GrepServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class GrepServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.grep")
    public void grep(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String userName = CurrentUser.getUserName(request);
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");

        if(!AccessController.getRead(userName, workspace, path)) {
            Display.denied(request, response);
            return;
        }

        if(LessServlet.prepare(request, response)) {
            GrepTemplate.execute(request, response);
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("grep.find")
    public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String userName = CurrentUser.getUserName(request);
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");

        if(!AccessController.getRead(userName, workspace, path)) {
            Ajax.denied(request, response);
            return;
        }

        File file = Finder.getFile(request);

        if(!file.exists() || !file.isFile()) {
            Less.error(response, 404, "File Not Found.");
            return;
        }

        if(!TextType.allow(file.getName())) {
            Less.denied(response);
            return;
        }

        String realPath = file.getAbsolutePath();
        Cache cache = CacheFactory.getInstance();
        Object message = cache.get(realPath);

        if(message != null) {
            Less.redirect(response, "?action=tail.error");
            return;
        }

        String keyword = this.getTrimString(request, "keyword");
        boolean regexp = this.getBoolean(request, "regexp", false);
        long position = this.getLong(request, "position", 0L);
        int rows = this.getInteger(request, "rows", 10);
        String charset = this.getTrimString(request, "charset");

        if(rows > Less.MAX_ROWS) {
            rows = Less.MAX_ROWS;
        }

        if(charset.length() < 1) {
            charset = "utf-8";
        }

        if(!file.exists() || !file.isFile()) {
            Ajax.error(request, response, 404, "file not exists.");
            return;
        }

        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(file, "r");
            FileRange range = Grep.find(raf, keyword, regexp, position, rows, charset);
            Less.success(response, range, charset);
            return;
        }
        catch(IOException e) {
            Less.error(response, 500, "error");
            return;
        }
        finally {
            IO.close(raf);
        }
    }
}
