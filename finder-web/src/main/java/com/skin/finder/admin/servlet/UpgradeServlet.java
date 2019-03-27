/*
 * $RCSfile: UpgradeServlet.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 * http://www.finderweb.net
 */
package com.skin.finder.admin.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.admin.AdminController;
import com.skin.finder.admin.servlet.template.UpgradeTemplate;
import com.skin.finder.cluster.Agent;
import com.skin.finder.cluster.Cluster;
import com.skin.finder.cluster.ClusterManager;
import com.skin.finder.config.App;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.security.Digest;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.ClassUtil;
import com.skin.finder.util.Hex;
import com.skin.finder.util.IO;
import com.skin.finder.util.Path;
import com.skin.finder.util.StringUtil;
import com.skin.finder.util.UpdateChecker;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;
import com.skin.finder.web.upload.MultipartHttpRequest;
import com.skin.finder.web.upload.Part;

/**
 * <p>Title: UpgradeServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class UpgradeServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UpgradeServlet.class);

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.upgrade.index")
    public void list(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), false)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }

        String masterName = ConfigFactory.getMaster();
        String masterVersion = App.getVersion();
        Cluster cluster = ClusterManager.getInstance();

        request.setAttribute("masterName", masterName);
        request.setAttribute("masterVersion", masterVersion);
        request.setAttribute("cluster", cluster);
        UpgradeTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.upgrade.url")
    public void getDownloadURL(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response, ConfigFactory.getMaster(), false)) {
            return;
        }

        try {
            String downloadURL = null;
            Map<String, String> context = UpdateChecker.execute();

            if(context != null) {
                downloadURL = context.get("download");
            }

            if(StringUtil.isBlank(downloadURL)) {
                logger.info("查询新版本失败！");
                Ajax.error(request, response, "finder.system.error");
                return;
            }
            Ajax.success(request, response, "\"" + downloadURL + "\"");
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
            Ajax.error(request, response, "finder.system.error");
        }
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.upgrade.upload")
    public void upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response, true)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }

        int maxFileSize = 20 * 1024 * 1024;
        int maxBodySize = 24 * 1024 * 1024;
        String repository = System.getProperty("java.io.tmpdir");
        MultipartHttpRequest multipartRequest = null;
        Part uploadFile = null;
        String token = null;

        try {
            multipartRequest = MultipartHttpRequest.parse(request, repository, maxFileSize, maxBodySize);
            uploadFile = multipartRequest.getFileItem("uploadFile");
            token = multipartRequest.getParameter("token");

            if(uploadFile == null || !uploadFile.isFileField()) {
                Ajax.error(request, response, 404, "Bad Request.");
                return;
            }

            if(StringUtil.isBlank(token)) {
                Ajax.error(request, response, 404, "Bad Request.");
                return;
            }

            boolean exists = true;
            String tmpFileName = this.getFileName(token);
            File target = new File(repository, tmpFileName);
            long offset = multipartRequest.getLong("offset", 0L);

            if(!Path.contains(repository, target.getAbsolutePath(), true)) {
                Ajax.error(request, response, "非法的token！");
                return;
            }

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
                Ajax.error(request, response, 500, "create file failed.");
                return;
            }

            IO.write(uploadFile.getFile(), target, offset, 0L);
            Ajax.success(request, response, Boolean.toString(true));
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

    /**
     * 执行升级
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.upgrade.execute")
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response, true)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }

        String token = request.getParameter("token");
        String fileName = request.getParameter("fileName");

        if(StringUtil.isBlank(fileName)) {
            Ajax.error(request, response, "系统错误，缺少参数！");
            return;
        }

        if(!fileName.endsWith(".jar")) {
            Ajax.error(request, response, "系统错误，缺少参数！");
            return;
        }

        String repository = System.getProperty("java.io.tmpdir");
        File uploadFile = this.getUploadFile(repository, token);

        if(!Path.contains(repository, uploadFile.getAbsolutePath(), true)) {
            Ajax.error(request, response, "非法的token！");
            return;
        }

        if(!uploadFile.exists() || !uploadFile.isFile()) {
            Ajax.error(request, response, "文件不存在，请重新上传！");
            return;
        }

        File current = ClassUtil.getJarFile(UpgradeServlet.class);
        File lib = current.getParentFile();
        File jarFile = new File(lib, fileName);
        logger.info("upgrade: {}", jarFile.getAbsolutePath());

        if(!Path.contains(lib.getAbsolutePath(), jarFile.getAbsolutePath(), true)) {
            Ajax.error(request, response, "非法的文件名！");
            return;
        }

        this.delete(current);
        IO.copy(uploadFile, jarFile, true);
        jarFile.setReadable(true);
        jarFile.setWritable(true);
        jarFile.setExecutable(true);
        Ajax.success(request, response, "true");
        uploadFile.delete();
        return;
    }

    /**
     * @param repository
     * @param token
     * @return File
     */
    private File getUploadFile(String repository, String token) {
        String fileName = this.getFileName(token);
        return new File(repository, fileName);
    }

    /**
     * @param token
     * @return String
     */
    private String getFileName(String token) {
        return token + "-finder.jar";
    }

    /**
     * @param file
     */
    private boolean delete(File file) {
        if(file.exists() && file.isFile()) {
            try {
                file.delete();
            }
            catch(Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        try {
            Thread.sleep(500);
        }
        catch(InterruptedException e) {
            logger.error(e.getMessage());
        }

        if(file.exists() && file.isFile()) {
            logger.warn("delete {} failed.", file.getAbsolutePath());
            return false;
        }
        return true;
    }

    /**
     * @param file
     * @param md5
     * @return boolean
     */
    protected boolean check(File file, String md5) {
        try {
            byte[] bytes = Digest.digest("MD5", file);
            String hex = Hex.encode(bytes);
            return hex.equals(md5);
        }
        catch(IOException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }
}
