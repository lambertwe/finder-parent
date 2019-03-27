/*
 * $RCSfile: ZipListServlet.java,v $
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.FileType;
import com.skin.finder.Finder;
import com.skin.finder.Operation;
import com.skin.finder.ZipItem;
import com.skin.finder.ZipItemList;
import com.skin.finder.cluster.Agent;
import com.skin.finder.cluster.WorkspaceManager;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.servlet.page.Display;
import com.skin.finder.servlet.template.ZipViewTemplate;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.IO;
import com.skin.finder.util.MimeType;
import com.skin.finder.util.Path;
import com.skin.finder.util.StringUtil;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: ZipListServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ZipListServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final String[] SUPPORT_TYPES = new String[]{"zip", "jar", "war"};
    private static final Logger logger = LoggerFactory.getLogger(ZipListServlet.class);

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.zip.view")
    public void view(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String home = Finder.getWork(request, workspace);

        if(home == null) {
            Display.error(request, response, 404, "Workspace not exists.");
            return;
        }

        String realPath = Finder.getRealPath(home, path);

        if(realPath == null) {
            logger.debug("can't access - {}: {}: {}", workspace, home, path);
            Display.error(request, response, 404, "File not exists.");
            return;
        }

        /**
         * zip, jar, rar, tar, 7z ...
         */
        String relativePath = Path.getRelativePath(home, realPath);
        String extension = FileType.getExtension(path).toLowerCase();

        if(!StringUtil.contains(SUPPORT_TYPES, extension)) {
            Display.error(request, response, 404, "Unsupported file type.");
            return;
        }

        File zipFile = new File(realPath);

        if(!zipFile.exists() || !zipFile.isFile()) {
            Display.error(request, response, 404, "File not exists.");
            return;
        }
        request.setAttribute("host", ConfigFactory.getHostName());
        request.setAttribute("workspace", workspace);
        request.setAttribute("path", relativePath);
        request.setAttribute("mode", 0);
        ZipViewTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.zip.show")
    public void show(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String home = Finder.getWork(request, workspace);

        if(home == null) {
            Display.error(request, response, 404, "Workspace not exists.");
            return;
        }

        String realPath = Finder.getRealPath(home, path);

        if(realPath == null) {
            logger.debug("can't access - {}: {}: {}", workspace, home, path);
            Display.error(request, response, 404, "File not exists.");
            return;
        }

        /**
         * zip, jar, rar, tar, 7z ...
         */
        String relativePath = Path.getRelativePath(home, realPath);
        String extension = FileType.getExtension(path).toLowerCase();

        if(!extension.equals("zip") && !extension.equals("jar")) {
            Display.error(request, response, 404, "Unsupported file type.");
            return;
        }

        File zipFile = new File(realPath);

        if(!zipFile.exists() || !zipFile.isFile()) {
            Display.error(request, response, 404, "File not exists.");
            return;
        }
        request.setAttribute("host", ConfigFactory.getHostName());
        request.setAttribute("workspace", workspace);
        request.setAttribute("path", relativePath);
        request.setAttribute("mode", 0);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.zip.getItemList")
    public void getItemList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String sub = request.getParameter("sub");
        String home = Finder.getWork(request, workspace);

        if(home == null) {
            Ajax.error(request, response, 404, "Workspace not exists.");
            return;
        }

        String realPath = Finder.getRealPath(home, path);

        if(realPath == null) {
            logger.debug("can't access - {}: {}: {}", workspace, home, path);
            Ajax.error(request, response, 404, "file not exists.");
            return;
        }

        /**
         * zip, jar, rar, tar, 7z ...
         */
        String extension = FileType.getExtension(path).toLowerCase();

        if(!StringUtil.contains(SUPPORT_TYPES, extension)) {
            Ajax.error(request, response, 404, "Unsupported file type.");
            return;
        }

        File zipFile = new File(realPath);

        if(!zipFile.exists() || !zipFile.isFile()) {
            Ajax.error(request, response, 404, "File not exists.");
            return;
        }

        if(StringUtil.isBlank(sub)) {
            sub = "/";
        }

        logger.debug("path: {}, sub: {}", path, sub);
        long t1 = System.nanoTime();
        List<ZipItem> zipItemList = getItems(zipFile, sub);
        ZipItemList result = new ZipItemList();
        result.setHost(ConfigFactory.getHostName());
        result.setWorkspace(workspace);
        result.setPath(path);
        result.setMode(Operation.READ);
        result.setZipItemList(zipItemList);

        Ajax.success(request, response, result.getJSONString());
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
    @UrlPattern("finder.zip.download")
    public void download(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String sub = request.getParameter("sub");

        if(WorkspaceManager.getReadonly(workspace)) {
            Ajax.error(request, response, 404, "Workspace is readonly.");
            return;
        }

        String home = Finder.getWork(request, workspace);

        if(home == null) {
            Ajax.error(request, response, 404, "Workspace not exists.");
            return;
        }

        String realPath = Finder.getRealPath(home, path);

        if(realPath == null) {
            logger.debug("can't access - {}: {}: {}", workspace, home, path);
            Ajax.error(request, response, 404, "file not exists.");
            return;
        }

        /**
         * zip, jar, rar, tar, 7z ...
         */
        String extension = FileType.getExtension(path).toLowerCase();

        if(!extension.equals("zip") && !extension.equals("jar")) {
            Ajax.error(request, response, 501, "Unsupported file type.");
            return;
        }

        File zipFile = new File(realPath);

        if(!zipFile.exists() || !zipFile.isFile()) {
            Ajax.error(request, response, 404, "File not exists.");
            return;
        }

        this.download(response, zipFile, sub);
        logger.info("zipFile: {}", zipFile.getAbsolutePath());
    }

    /**
     * @param file
     * @param path
     * @return List<ZipItem>
     * @throws IOException
     */
    public static List<ZipItem> getItems(File file, String path) throws IOException {
        String name = null;
        ZipFile zipFile = null;
        ZipEntry zipEntry = null;
        List<ZipItem> itemList = new ArrayList<ZipItem>();

        try {
            zipFile = new ZipFile(file);
            Enumeration<?> enums = zipFile.entries();

            while(enums.hasMoreElements()) {
                zipEntry = (ZipEntry)(enums.nextElement());
                name = zipEntry.getName();

                if(!name.startsWith("/")) {
                    name = "/" + name;
                }

                if(name.endsWith("/")) {
                    name = name.substring(0, name.length() - 1);
                }

                if(!name.startsWith(path)) {
                    continue;
                }

                String relativePath = name.substring(path.length());

                if(relativePath.startsWith("/")) {
                    relativePath = relativePath.substring(1);
                }

                if(relativePath.length() > 0 && relativePath.indexOf('/') < 0) {
                    ZipItem zipItem = new ZipItem();
                    zipItem.setFileName(relativePath);
                    zipItem.setFileSize(zipEntry.getSize());
                    zipItem.setCompressedSize(zipEntry.getCompressedSize());
                    zipItem.setLastModified(zipEntry.getTime());
                    zipItem.setIsFile(!zipEntry.isDirectory());
                    itemList.add(zipItem);
                }
            }
        }
        finally {
            if(zipFile != null) {
                try {
                    zipFile.close();
                }
                catch(IOException e) {
                }
            }
        }
        return itemList;
    }

    /**
     * @param response
     * @param file
     * @param path
     * @throws IOException
     */
    private void download(HttpServletResponse response, File file, String path) throws IOException {
        ZipFile zipFile = null;
        ZipEntry zipEntry = null;
        InputStream inputStream = null;

        try {
            String name = path;

            if(name.startsWith("/")) {
                name.substring(1);
            }

            zipFile = new ZipFile(file);
            zipEntry = zipFile.getEntry(path);

            if(zipEntry == null || zipEntry.isDirectory()) {
                response.sendError(404);
                return;
            }

            long contentLength = zipEntry.getSize();
            String contentType = MimeType.getMimeType(path);
            response.setContentType(contentType);
            response.setCharacterEncoding("utf-8");
            response.setContentLength((int)contentLength);

            inputStream = zipFile.getInputStream(zipEntry);
            IO.copy(inputStream, response.getOutputStream());
        }
        finally {
            IO.close(inputStream);

            if(zipFile != null) {
                try {
                    zipFile.close();
                }
                catch(IOException e) {
                }
            }
        }
    }
}
