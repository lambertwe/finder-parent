/*
 * $RCSfile: UploadServlet.java,v $
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
import com.skin.finder.acl.AccessController;
import com.skin.finder.cluster.Agent;
import com.skin.finder.cluster.WorkspaceManager;
import com.skin.finder.servlet.page.Display;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.IO;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;
import com.skin.finder.web.upload.MultipartHttpRequest;
import com.skin.finder.web.upload.Part;
import com.skin.finder.web.util.CurrentUser;

/**
 * <p>Title: UploadServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class UploadServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UploadServlet.class);

    /**
     * default
     */
    public UploadServlet() {
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
    @UrlPattern("finder.upload")
    public void upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String userName = CurrentUser.getUserName(request);
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String work = Finder.getWork(request, workspace);

        if(work == null) {
            Ajax.error(request, response, 403, "Workspace not exists.");
            return;
        }

        if(WorkspaceManager.getReadonly(workspace)) {
            Ajax.error(request, response, 403, "Workspace is readonly.");
            return;
        }

        if(!AccessController.getWrite(userName, workspace, path)) {
            Ajax.denied(request, response);
            return;
        }

        long t1 = System.nanoTime();
        int maxFileSize = 20 * 1024 * 1024;
        int maxBodySize = 24 * 1024 * 1024;
        String repository = System.getProperty("java.io.tmpdir");
        MultipartHttpRequest multipartRequest = null;
        Part uploadFile = null;

        try {
            multipartRequest = MultipartHttpRequest.parse(request, repository, maxFileSize, maxBodySize);
            uploadFile = multipartRequest.getFileItem("uploadFile");

            if(uploadFile == null || !uploadFile.isFileField()) {
                Ajax.error(request, response, 404, "Bad Request.");
                return;
            }

            String fileName = uploadFile.getFileName();
            String realPath = Finder.getRealPath(work, path);
            logger.info("fileName: {}", fileName);

            if(fileName.endsWith(".link.tail")) {
                Ajax.denied(request, response);
                return;
            }

            if(realPath == null) {
                Ajax.denied(request, response);
                return;
            }

            File dir = new File(realPath);

            if(!dir.exists() || !dir.isDirectory()) {
                Ajax.error(request, response, 404, "File not exists.");
                return;
            }

            boolean exists = true;
            File target = new File(dir, fileName);
            long offset = multipartRequest.getLong("offset", 0L);
            long lastModified = multipartRequest.getLong("lastModified", 0L);

            if(!target.exists()) {
                try {
                    exists = target.createNewFile();
                }
                catch(IOException e) {
                    exists = false;
                    logger.error(e.getMessage(), e);
                }
            }

            if(!exists) {
                Ajax.error(request, response, 500, "Create file failed.");
                return;
            }

            IO.write(uploadFile.getFile(), target, offset, lastModified);
            Ajax.success(request, response, Boolean.toString(true));
            this.time("upload.times", t1);
            return;
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
            Ajax.error(request, response, 500, "finder.system.error");
            return;
        }
        finally {
            if(uploadFile != null) {
                uploadFile.delete();
            }
            if(multipartRequest != null) {
                multipartRequest.destroy();
            }
        }
    }

    private void time(String name, long start) {
        if(logger.isDebugEnabled()) {
            long t2 = System.nanoTime();
            logger.debug("{}: {}", name, (((t2 - start) / 10000) / 100.0f));
        }
    }

    /**
     * destory
     */
    @Override
    public void destroy() {
    }
}
