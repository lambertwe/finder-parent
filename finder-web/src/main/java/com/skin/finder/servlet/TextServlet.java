/*
 * $RCSfile: TextServlet.java,v $
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
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.FileRange;
import com.skin.finder.Finder;
import com.skin.finder.Less;
import com.skin.finder.acl.AccessController;
import com.skin.finder.cluster.Agent;
import com.skin.finder.cluster.WorkspaceManager;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.IO;
import com.skin.finder.util.Path;
import com.skin.finder.util.StringUtil;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;
import com.skin.finder.web.util.CurrentUser;

/**
 * <p>Title: TextServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class TextServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final long MIN_SIZE = 256L * 1024L;
    private static final long MAX_SIZE = 5L * 1024L * 1024L;
    private static final Logger logger = LoggerFactory.getLogger(TextServlet.class);

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.getText")
    public void getText(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        /**
         * 256KB - 5M
         */
        long size = this.getLong(request, "size", MIN_SIZE);

        if(size <= 0 || size > MAX_SIZE) {
            size = MIN_SIZE;
        }
        execute(request, response, size);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.save")
    public void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String userName = CurrentUser.getUserName(request);
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String content = request.getParameter("content");
        String home = Finder.getWork(request, workspace);

        if(home == null) {
            Ajax.error(request, response, 403, "Workspace not exists.");
            return;
        }

        if(WorkspaceManager.getReadonly(workspace)) {
            Ajax.error(request, response, 403, "Workspace is readonly.");
            return;
        }

        String realPath = Finder.getRealPath(home, path);

        if(realPath == null) {
            Ajax.error(request, response, 404, path + " not exists !");
            return;
        }

        if(!AccessController.getWrite(userName, workspace, path)) {
            Ajax.denied(request, response);
            return;
        }

        String parent = Path.getParent(path);

        if(!AccessController.getWrite(userName, workspace, parent)) {
            Ajax.denied(request, response);
            return;
        }

        File file = new File(realPath);
        long start = this.getLong(request, "start", 0L);
        long end = this.getLong(request, "end", 0L);
        long size = this.getLong(request, "size", 0L);
        long lastModified = this.getLong(request, "modified", 0L);
        logger.info("save: {}-{}/{}, path", start, end, size, path);

        if(size != file.length() || lastModified != file.lastModified()) {
            logger.info("save error: {}/{}, {}/{}, {}", size, file.length(), lastModified, file.lastModified(), path);
            Ajax.error(request, response, "文件已经被修改，请重新载入并编辑之后提交。");
            return;
        }

        if(start > 0 || (end + 1) < size) {
            Ajax.error(request, response, "文件内容过长，不允许在线编辑。");
            return;
        }

        if(content != null) {
            IO.write(file, content.getBytes("utf-8"));
        }
        else {
            IO.touch(file);
        }

        String range = this.getRange(start, file.length() - 1, file.length(), file.lastModified());
        Ajax.success(request, response, range);
        return;
    }

    /**
     * @param request
     * @param response
     * @param maxSize
     * @throws ServletException
     * @throws IOException
     */
    public static void execute(HttpServletRequest request, HttpServletResponse response, long maxSize) throws ServletException, IOException {
        String userName = CurrentUser.getUserName(request);
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String charset = request.getParameter("charset");
        String home = Finder.getWork(request, workspace);

        if(home == null) {
            error(request, response, 403, "Workspace not exists.");
            return;
        }

        String realPath = Finder.getRealPath(home, path);

        if(realPath == null) {
            error(request, response, 404, path + " not exists !");
            return;
        }

        if(!AccessController.getRead(userName, workspace, path)) {
            denied(request, response);
            return;
        }

        if(StringUtil.isBlank(charset)) {
            charset = "utf-8";
        }

        File file = new File(realPath);

        if(!file.exists() || !file.isFile()) {
            error(request, response, 404, path + " not exists !");
            return;
        }

        RandomAccessFile raf = null;
        FileRange range = Less.getText(file, maxSize);

        try {
            raf = new RandomAccessFile(file, "r");
            Less.success(response, raf, range, charset);
        }
        finally {
            IO.close(raf);
        }
    }

    /**
     * @param start
     * @param end
     * @param length
     * @param lastModified
     * @return String
     */
    private String getRange(long start, long end, long length, long lastModified) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{\"start\":");
        buffer.append(start);
        buffer.append(",\"end\":");
        buffer.append(end);
        buffer.append(",\"size\":");
        buffer.append(length);
        buffer.append(",\"modified\":");
        buffer.append(lastModified);
        buffer.append("}");
        return buffer.toString();
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    private static void denied(HttpServletRequest request, HttpServletResponse response) throws IOException {
        error(request, response, 403, "Access Denied.");
    }

    /**
     * @param request
     * @param response
     * @param status
     * @param message
     * @throws IOException
     */
    private static void error(HttpServletRequest request, HttpServletResponse response, int status, String message) throws IOException {
        response.setHeader("Finder-Version", "1.0");
        response.setHeader("Finder-Status", Integer.toString(status));

        if(message != null) {
            response.setHeader("Finder-Message", URLEncoder.encode(message, "utf-8"));
        }
    }
}
