/*
 * $RCSfile: LessServlet.java,v $
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.FileRange;
import com.skin.finder.FileType;
import com.skin.finder.Finder;
import com.skin.finder.Less;
import com.skin.finder.TextType;
import com.skin.finder.acl.AccessController;
import com.skin.finder.cache.Cache;
import com.skin.finder.cache.CacheFactory;
import com.skin.finder.cluster.Agent;
import com.skin.finder.cluster.ClusterManager;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.servlet.page.Display;
import com.skin.finder.servlet.template.LessTemplate;
import com.skin.finder.servlet.template.TailTemplate;
import com.skin.finder.util.IO;
import com.skin.finder.util.Path;
import com.skin.finder.util.StringUtil;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;
import com.skin.finder.web.util.CurrentUser;

/**
 * <p>Title: LessServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class LessServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(LessServlet.class);

    /**
     * default
     */
    public LessServlet() {
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.less")
    public void less(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            LessTemplate.execute(request, response);
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.tail")
    public void tail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            TailTemplate.execute(request, response);
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("less.getRange")
    public void getRange(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        long t1 = System.nanoTime();
        String userName = CurrentUser.getUserName(request);
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");

        if(!AccessController.getRead(userName, workspace, path)) {
            Less.denied(response);
            return;
        }

        File file = Finder.getFile(request);

        if(!file.exists() || !file.isFile()) {
            Less.error(response, 404, "File Not Found.");
            return;
        }

        if(!TextType.allow(file.getName())) {
            Less.redirect(response, "?action=tail.error");
            return;
        }

        String realPath = file.getAbsolutePath();
        Cache cache = CacheFactory.getInstance();
        Object message = cache.get(realPath);

        if(message != null) {
            Less.redirect(response, "?action=tail.error");
            return;
        }

        this.getRange(request, response, file);
        long t2 = System.nanoTime();
        logger.debug("times: {} ms", (((t2 - t1) / 10000) / 100.0f));
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("less.getTail")
    public void getTail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String userName = CurrentUser.getUserName(request);
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");

        if(!AccessController.getRead(userName, workspace, path)) {
            Less.denied(response);
            return;
        }

        File file = Finder.getFile(request);

        if(!file.exists() || !file.isFile()) {
            Less.error(response, 404, "file not exists.");
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
        getTail(request, response, file);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("tail.error")
    public void error(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Display.error(request, response, 403, "该操作已被管理员关闭！");
    }

    /**
     * @param request
     * @param response
     * @return boolean
     * @throws ServletException
     * @throws IOException
     */
    public static boolean prepare(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String host = ConfigFactory.getHostName();
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String charset = request.getParameter("charset");
        String work = Finder.getWork(request, workspace);

        if(work == null) {
            Display.error(request, response, 404, "\"" + workspace + "\" not exists.");
            return false;
        }

        String realPath = Finder.getRealPath(work, path);

        if(realPath == null) {
            Display.error(request, response, 404, "\"" + path + "\" not exists.");
            return false;
        }

        if(charset == null || charset.trim().length() < 1) {
            charset = ClusterManager.getCharset(host, workspace);
        }

        File file = new File(realPath);

        if(!file.exists() || !file.isFile()) {
            Display.error(request, response, 404, "\"" + path + "\" not exists.");
            return false;
        }

        /**
         * 
         */
        String allows = ConfigFactory.getTextType();
        String extension = FileType.getExtension(file.getName()).toLowerCase();

        if(!StringUtil.contains(allows, extension)) {
            Display.error(request, response, 403, "\"" + path + "\" not allowed.");
            return false;
        }

        String parent = Path.getRelativePath(work, file.getParent());
        String relativePath = Path.getRelativePath(work, realPath);

        request.setAttribute("host", host);
        request.setAttribute("workspace", workspace);
        request.setAttribute("work", work);
        request.setAttribute("path", relativePath);
        request.setAttribute("parent", parent);
        request.setAttribute("charset", charset);
        request.setAttribute("fileName", file.getName());
        return true;
    }

    /**
     * @param request
     * @param response
     * @param file
     * @throws ServletException
     * @throws IOException
     */
    private void getRange(HttpServletRequest request, HttpServletResponse response, File file) throws ServletException, IOException {
        long position = this.getLong(request, "position", 0L);
        int type = this.getInteger(request, "type", 0);
        int rows = this.getInteger(request, "rows", 10);
        String charset = this.getTrimString(request, "charset");

        if(rows > Less.MAX_ROWS) {
            rows = Less.MAX_ROWS;
        }

        if(charset.length() < 1) {
            charset = "utf-8";
        }

        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(file, "r");

            if(type == 1) {
                FileRange range = Less.next(raf, position, rows);
                Less.success(response, raf, range, charset);
                return;
            }
            else {
                FileRange range = Less.prev(raf, position, rows);
                Less.success(response, raf, range, charset);
                return;
            }
        }
        catch(IOException e) {
            logger.error(e.getMessage(), e);
            Less.error(response, 500, "error");
            return;
        }
        finally {
            IO.close(raf);
        }
    }

    /**
     * @param request
     * @param response
     * @param file
     * @throws ServletException
     * @throws IOException
     */
    private void getTail(HttpServletRequest request, HttpServletResponse response, File file) throws ServletException, IOException {
        int rows = this.getInteger(request, "rows", 10);
        long position = this.getLong(request, "position", 0L);
        String charset = this.getTrimString(request, "charset");

        if(rows > Less.MAX_ROWS) {
            rows = Less.MAX_ROWS;
        }

        if(charset.length() < 1) {
            charset = "utf-8";
        }

        long t1 = System.nanoTime();
        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(file, "r");
            FileRange range = Less.tail(raf, position, rows);
            range.setTimestamp(file.lastModified());
            Less.success(response, raf, range, charset);
            long t2 = System.nanoTime();
            logger.debug("tail.time: {}", ((t2 - t1) / 1000000.0f));
        }
        catch(IOException e) {
            logger.error(e.getMessage(), e);
            Less.error(response, 500, "error");
            return;
        }
        finally {
            IO.close(raf);
        }
    }
}

