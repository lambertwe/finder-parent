/*
 * $RCSfile: ZipServlet.java,v $
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.FileType;
import com.skin.finder.Finder;
import com.skin.finder.FinderManager;
import com.skin.finder.acl.AccessController;
import com.skin.finder.cluster.Agent;
import com.skin.finder.cluster.WorkspaceManager;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.DateUtil;
import com.skin.finder.util.JarUtil;
import com.skin.finder.util.Path;
import com.skin.finder.util.ZipUtil;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;
import com.skin.finder.web.util.CurrentUser;

/**
 * <p>Title: ZipServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ZipServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ZipServlet.class);

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.zip")
    public void zip(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String userName = CurrentUser.getUserName(request);
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String[] fileList = request.getParameterValues("file");
        Charset charset = getCharset(request.getParameter("charset"));
        String home = Finder.getWork(request, workspace);

        if(home == null) {
            Ajax.error(request, response, 404, "Workspace not exists.");
            return;
        }

        if(fileList == null || fileList.length < 1) {
            Ajax.error(request, response, 404, "Bad Request.");
            return;
        }

        /**
         * 对当前目录拥有读权限
         */
        if(!AccessController.getRead(userName, workspace, path)) {
            Ajax.denied(request, response);
            return;
        }

        this.zip(home, path, fileList, charset);
        Ajax.success(request, response, "true");
        return;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.jar")
    public void jar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String userName = CurrentUser.getUserName(request);
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String[] fileList = request.getParameterValues("file");
        Charset charset = getCharset(request.getParameter("charset"));
        String home = Finder.getWork(request, workspace);

        if(home == null) {
            Ajax.error(request, response, 404, "Workspace not exists.");
            return;
        }

        if(fileList == null || fileList.length < 1) {
            Ajax.error(request, response, 404, "Bad Request.");
            return;
        }

        /**
         * 对当前目录拥有读权限
         */
        if(!AccessController.getRead(userName, workspace, path)) {
            Ajax.denied(request, response);
            return;
        }

        this.jar(home, path, fileList, charset);
        Ajax.success(request, response, "true");
        return;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.unzip")
    public void unzip(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String workspace = request.getParameter("workspace");
        String file = request.getParameter("file");

        if(WorkspaceManager.getReadonly(workspace)) {
            Ajax.error(request, response, 501, "Workspace is readonly.");
            return;
        }

        String home = Finder.getWork(request, workspace);

        if(home == null) {
            Ajax.error(request, response, 404, "Workspace not exists.");
            return;
        }

        String realPath = Finder.getRealPath(home, file);

        if(realPath == null) {
            logger.debug("can't access - {}: {}: {}", workspace, home, file);
            Ajax.error(request, response, 501, "file not exists.");
            return;
        }

        String extension = FileType.getExtension(file).toLowerCase();

        if(!extension.equals("zip") && !extension.equals("jar")) {
            Ajax.error(request, response, 501, "Unsupported file type.");
            return;
        }

        File zipFile = new File(realPath);

        if(!zipFile.exists() || !zipFile.isFile()) {
            Ajax.error(request, response, 501, "file not exists.");
            return;
        }

        FinderManager finderManager = new FinderManager(home);

        String fileName = zipFile.getName();
        String parentPath = Path.getRelativePath(home, zipFile.getParent());
        Charset charset = getCharset(request.getParameter("charset"));
        File target = finderManager.mkdir(parentPath, fileName.substring(0, fileName.length() - 4));

        if(extension.equals("zip")) {
            ZipUtil.unzip(zipFile, target, charset);
        }
        else {
            JarUtil.unzip(zipFile, target, charset);
        }
        Ajax.success(request, response, "true");
    }

    /**
     * @param home
     * @param path
     * @param fileList
     * @param charset
     * @throws IOException
     */
    private void zip(String home, String path, String[] fileList, Charset charset) throws IOException {
        FinderManager finderManager = new FinderManager(home);
        File target = finderManager.create(path, DateUtil.format(new Date(), "yyyy_MM_dd_HH_mm_ss") + ".zip");
        OutputStream outputStream = null;
        ZipOutputStream zipOutputStream = null;

        try {
            outputStream = new FileOutputStream(target);
            zipOutputStream = ZipUtil.getZipOutputStream(outputStream, charset);

            for(String file : fileList) {
                File realFile = Finder.getRealFile(home, path + "/" + file);
                ZipUtil.compress(realFile, zipOutputStream, "");
            }

            zipOutputStream.finish();
            zipOutputStream.flush();
            outputStream.flush();
        }
        finally {
            if(zipOutputStream != null) {
                try {
                    zipOutputStream.close();
                }
                catch(IOException e) {
                }
            }

            if(outputStream != null) {
                try {
                    outputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
    }

    /**
     * @param home
     * @param path
     * @param fileList
     * @param charset
     * @throws IOException
     */
    private void jar(String home, String path, String[] fileList, Charset charset) throws IOException {
        FinderManager finderManager = new FinderManager(home);
        File target = finderManager.create(path, DateUtil.format(new Date(), "yyyy_MM_dd_HH_mm_ss") + ".jar");
        OutputStream outputStream = null;
        JarOutputStream jarOutputStream = null;

        try {
            outputStream = new FileOutputStream(target);
            jarOutputStream = JarUtil.getJarOutputStream(outputStream, charset);

            for(String file : fileList) {
                File realFile = Finder.getRealFile(home, path + "/" + file);
                JarUtil.compress(realFile, jarOutputStream, "");
            }

            jarOutputStream.finish();
            jarOutputStream.flush();
            outputStream.flush();
        }
        finally {
            if(jarOutputStream != null) {
                try {
                    jarOutputStream.close();
                }
                catch(IOException e) {
                }
            }

            if(outputStream != null) {
                try {
                    outputStream.close();
                }
                catch(IOException e) {
                }
            }
        }
    }

    /**
     * @param charset
     * @return Charset
     */
    private Charset getCharset(String charset) {
        if(charset != null) {
            try {
                return Charset.forName(charset);
            }
            catch(Exception e) {
                logger.warn(e.getMessage());
            }
        }
        return null;
    }
}
