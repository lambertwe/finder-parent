/*
 * $RCSfile: FinderServlet.java,v $
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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.FileItem;
import com.skin.finder.FileItemList;
import com.skin.finder.Finder;
import com.skin.finder.FinderManager;
import com.skin.finder.acl.AccessController;
import com.skin.finder.cluster.Agent;
import com.skin.finder.config.App;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.servlet.page.Display;
import com.skin.finder.servlet.template.DisplayTemplate;
import com.skin.finder.servlet.template.FinderTemplate;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.Path;
import com.skin.finder.util.StringUtil;
import com.skin.finder.util.UpdateChecker;
import com.skin.finder.web.Startup;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;
import com.skin.finder.web.servlet.Httpd;
import com.skin.finder.web.util.CurrentUser;

/**
 * <p>Title: FinderServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
@Startup(value = 0)
public class FinderServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(FinderServlet.class);
    private static final Logger accessLogger = LoggerFactory.getLogger("accessLogger");

    /**
     * default
     */
    public FinderServlet() {
    }

    /**
     *
     */
    @Override
    public void init() {
        logger.info("init");

        if(!logger.isInfoEnabled()) {
            throw new RuntimeException("logger init failed.");
        }

        if(!accessLogger.isInfoEnabled()) {
            throw new RuntimeException("logger init failed.");
        }

        if(ConfigFactory.getUpdateCheck()) {
            UpdateChecker.start();
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Display.error(request, response, 404, "Not Found !");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.getFile")
    public void getFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String userName = CurrentUser.getUserName(request);
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String work = Finder.getWork(request, workspace);

        if(work == null) {
            Ajax.error(request, response, "workspace not exists !");
            return;
        }

        String realPath = Finder.getRealPath(work, path);

        if(realPath == null) {
            Ajax.error(request, response, path + " not exists !");
            return;
        }

        if(!AccessController.getRead(userName, workspace, path)) {
            Ajax.denied(request, response);
            return;
        }

        File file = new File(realPath);
        String relativePath = Path.getRelativePath(work, realPath);

        if(!file.exists()) {
            Ajax.error(request, response, 404, "file not exists.");
            return;
        }

        int mode = AccessController.getMode(userName, workspace, relativePath);
        FileItem fileItem = FinderManager.getFileItem(file);
        fileItem.setMode(mode);
        Ajax.success(request, response, FileItemList.getJSONString(fileItem));
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.getFileList")
    public void getFileList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String userName = CurrentUser.getUserName(request);
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String work = Finder.getWork(request, workspace);

        if(work == null) {
            Ajax.error(request, response, "workspace not exists !");
            return;
        }

        String realPath = Finder.getRealPath(work, path);

        if(realPath == null) {
            Ajax.error(request, response, path + " not exists !");
            return;
        }

        File file = new File(realPath);

        if(!file.exists() || !file.isDirectory()) {
            Ajax.error(request, response, path + " is not directory !");
            return;
        }

        long t1 = System.nanoTime();
        FinderManager finderManager = new FinderManager(work);
        FileItemList fileItemList = finderManager.list(userName, workspace, path);

        Ajax.success(request, response, fileItemList.getJSONString());
        long t2 = System.nanoTime();

        if(logger.isDebugEnabled()) {
            logger.debug("time: {} ns", (t2 - t1));
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.suggest")
    public void suggest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String userName = CurrentUser.getUserName(request);
        String path = request.getParameter("path");
        String workspace = request.getParameter("workspace");
        String home = Finder.getWork(request, workspace);

        if(home == null) {
            Ajax.error(request, response, 404, "workspace not exists.");
            return;
        }

        FinderManager finderManager = new FinderManager(home);
        List<FileItem> fileItemList = finderManager.suggest(userName, workspace, path);
        String json = FileItemList.getJSONString(fileItemList);
        Ajax.success(request, response, json);
        return;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.display")
    public void display(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            Display.error(request, response, 404, path + " not exists !");
            return;
        }

        File file = new File(realPath);

        if(!file.exists()) {
            Display.error(request, response, 404, path + " not exists !");
            return;
        }

        if(file.isFile()) {
            this.view(request, response);
            return;
        }

        String relativePath = Path.getRelativePath(home, realPath);

        if(StringUtil.isBlank(relativePath)) {
            relativePath = "/";
        }

        if(!AccessController.getRead(userName, workspace, relativePath)) {
            Display.denied(request, response);
            return;
        }

        int mode = AccessController.getMode(userName, workspace, relativePath);
        request.setAttribute("host", ConfigFactory.getHostName());
        request.setAttribute("workspace", workspace);
        request.setAttribute("path", relativePath);
        request.setAttribute("mode", mode);
        request.setAttribute("version", App.getVersion());
        FinderTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void view(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String host = ConfigFactory.getHostName();
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");

        request.setAttribute("host", host);
        request.setAttribute("workspace", workspace);
        request.setAttribute("path", path);
        DisplayTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.get")
    public void get(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }
        this.execute(request, response, false);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.download")
    public void download(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }
        this.execute(request, response, true);
    }

    /**
     * @param request
     * @param response
     * @param download
     * @throws ServletException
     * @throws IOException
     */
    private void execute(HttpServletRequest request, HttpServletResponse response, boolean download) throws ServletException, IOException {
        String userName = CurrentUser.getUserName(request);
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String home = Finder.getWork(request, workspace);

        if(home == null) {
            Display.error(request, response, 403, "Workspace not exists.");
            return;
        }

        String realPath = Finder.getRealPath(home, path);

        if(realPath == null) {
            logger.debug("can't access - {}: {}: {}", workspace, home, path);
            Display.error(request, response, 404, "File not exists.");
            return;
        }

        File file = new File(realPath);

        if(!file.exists() || !file.isFile()) {
            logger.debug("file not exists: {}", file.getAbsolutePath());
            Display.error(request, response, 404, "File not exists.");
            return;
        }

        if(!AccessController.getRead(userName, workspace, path)) {
            Display.denied(request, response);
            return;
        }
        Httpd.service(request, response, file, download);
    }

    /**
     * destory
     */
    @Override
    public void destroy() {
        UpdateChecker.shutdown();
    }
}
