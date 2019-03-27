/*
 * $RCSfile: FileServlet.java,v $
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.Finder;
import com.skin.finder.FinderManager;
import com.skin.finder.acl.AccessController;
import com.skin.finder.cluster.Agent;
import com.skin.finder.cluster.WorkspaceManager;
import com.skin.finder.servlet.page.Display;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.IO;
import com.skin.finder.util.Path;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;
import com.skin.finder.web.util.CurrentUser;

/**
 * <p>Title: FileServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class FileServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(FileServlet.class);

    /**
     * default
     */
    public FileServlet() {
    }

    /**
     *
     */
    @Override
    public void init() {
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
    @UrlPattern("finder.touch")
    public void touch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String userName = CurrentUser.getUserName(request);
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String home = Finder.getWork(request, workspace);

        if(home == null) {
            Ajax.error(request, response, 403, "Workspace not exists.");
            return;
        }

        if(WorkspaceManager.getReadonly(workspace)) {
            Ajax.error(request, response, 403, "Workspace is readonly.");
            return;
        }

        String parent = Path.getParent(path);

        if(!AccessController.getWrite(userName, workspace, parent)) {
            Ajax.denied(request, response);
            return;
        }

        String realPath = Finder.getRealPath(home, path);

        if(realPath == null) {
            Ajax.error(request, response, 404, path + " not exists !");
            return;
        }

        File file = new File(realPath);

        if(!file.exists()) {
            file.createNewFile();
        }
        Ajax.success(request, response, Boolean.toString(file.exists()));
        return;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.rename")
    public void rename(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String userName = CurrentUser.getUserName(request);
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String newName = request.getParameter("newName");
        String home = Finder.getWork(request, workspace);

        if(home == null) {
            Ajax.error(request, response, 403, "Workspace not exists.");
            return;
        }

        if(WorkspaceManager.getReadonly(workspace)) {
            Ajax.error(request, response, 403, "Workspace is readonly.");
            return;
        }

        String parent = Path.getParent(path);

        if(!AccessController.getWrite(userName, workspace, parent)) {
            Ajax.denied(request, response);
            return;
        }

        FinderManager finderManager = new FinderManager(home);
        int count = finderManager.rename(path, newName);
        Ajax.success(request, response, Boolean.toString(count > 0));
        return;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.mkdir")
    public void mkdir(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String userName = CurrentUser.getUserName(request);
        String workspace = request.getParameter("workspace");
        String work = Finder.getWork(request, workspace);

        if(work == null) {
            Ajax.error(request, response, 403, "Workspace not exists.");
            return;
        }

        String path = request.getParameter("path");
        String name = request.getParameter("name");

        if(WorkspaceManager.getReadonly(workspace)) {
            Ajax.error(request, response, 403, "Workspace is readonly.");
            return;
        }

        if(!AccessController.getWrite(userName, workspace, path)) {
            Ajax.denied(request, response);
            return;
        }

        FinderManager finderManager = new FinderManager(work);
        File dir = finderManager.mkdir(path, name);
        Ajax.success(request, response, Boolean.toString(dir != null));
        return;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.delete")
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String userName = CurrentUser.getUserName(request);
        String workspace = request.getParameter("workspace");
        String[] paths = request.getParameterValues("path");

        if(WorkspaceManager.getReadonly(workspace)) {
            Ajax.error(request, response, 501, "Workspace is readonly.");
            return;
        }

        if(paths == null || paths.length < 1) {
            Ajax.error(request, response, 501, "Bad Request.");
            return;
        }

        for(String path : paths) {
            if(!AccessController.getDelete(userName, workspace, path)) {
                Ajax.denied(request, response);
                return;
            }
        }

        int count = 0;
        String home = Finder.getWork(request, workspace);

        if(home == null) {
            Ajax.error(request, response, 403, "Workspace not exists.");
            return;
        }

        FinderManager finderManager = new FinderManager(home);

        for(int i = 0; i < paths.length; i++) {
            count += finderManager.delete(paths[i]);
        }
        Ajax.success(request, response, Integer.toString(count));
        return;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.cut")
    public void cut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String workspace = request.getParameter("workspace");

        if(WorkspaceManager.getReadonly(workspace)) {
            Ajax.error(request, response, 501, "Workspace is readonly.");
            return;
        }
        this.move(request, response, true);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.copy")
    public void copy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String workspace = request.getParameter("workspace");

        if(WorkspaceManager.getReadonly(workspace)) {
            Ajax.error(request, response, 501, "Workspace is readonly.");
            return;
        }
        this.move(request, response, false);
    }

    /**
     * @param request
     * @param response
     * @param delete
     * @throws ServletException
     * @throws IOException
     */
    private void move(HttpServletRequest request, HttpServletResponse response, boolean delete) throws ServletException, IOException {
        String userName = CurrentUser.getUserName(request);
        String sourceWorkspace = request.getParameter("sourceWorkspace");
        String sourcePath = request.getParameter("sourcePath");
        String targetWorkspace = request.getParameter("workspace");
        String targetPath = request.getParameter("path");
        String[] fileList = request.getParameterValues("file");
        String sourceHome = Finder.getWork(request, sourceWorkspace);
        String targetHome = Finder.getWork(request, targetWorkspace);

        if(sourceHome == null) {
            Ajax.error(request, response, 403, "sourceHome not exists.");
            return;
        }

        if(targetHome == null) {
            Ajax.error(request, response, 403, "targetHome not exists.");
            return;
        }

        if(fileList == null || fileList.length < 1) {
            Ajax.error(request, response, 404, "Bad Request.");
            return;
        }

        if(!AccessController.getRead(userName, sourceWorkspace, sourcePath)) {
            Ajax.denied(request, response);
            return;
        }

        if(!AccessController.getWrite(userName, targetWorkspace, targetPath)) {
            Ajax.denied(request, response);
            return;
        }

        if(delete) {
            for(String file : fileList) {
                if(!AccessController.getDelete(userName, sourceWorkspace, sourcePath + "/" + file)) {
                    Ajax.denied(request, response);
                    return;
                }
            }
        }

        for(String file : fileList) {
            String sourceFile = Finder.getRealPath(sourceHome, sourcePath + "/" + file);
            String targetFile = Finder.getRealPath(targetHome, targetPath + "/" + file);

            File f1 = new File(sourceFile);
            File f2 = new File(targetFile);

            if(f1.equals(f2)) {
                if(delete) {
                    continue;
                }
                else {
                    f2 = Finder.getFile(f2.getParentFile(), f2.getName());
                }
            }

            logger.info("move: {}, {} to {}", delete, f1.getAbsolutePath(), f2.getAbsolutePath());
            IO.copy(f1, f2, true);

            if(delete) {
                IO.delete(f1);
            }
        }
        Ajax.success(request, response, "true");
        return;
    }

    /**
     * destory
     */
    @Override
    public void destroy() {
    }
}
