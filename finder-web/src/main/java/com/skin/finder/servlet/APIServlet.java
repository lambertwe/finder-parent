/*
 * $RCSfile: APIServlet.java,v $
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
import java.nio.charset.Charset;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.FileItem;
import com.skin.finder.FileItemList;
import com.skin.finder.Finder;
import com.skin.finder.FinderManager;
import com.skin.finder.Operation;
import com.skin.finder.acl.AccessController;
import com.skin.finder.cache.Cache;
import com.skin.finder.cache.CacheFactory;
import com.skin.finder.cluster.Agent;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.security.Digest;
import com.skin.finder.servlet.page.Display;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.Hex;
import com.skin.finder.util.Path;
import com.skin.finder.util.StringUtil;
import com.skin.finder.Key;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;
import com.skin.finder.web.servlet.Httpd;
import com.skin.finder.web.util.CurrentUser;

/**
 * <p>Title: APIServlet</p>
 * <p>Description: 为外部应用程序提供免登录访问finder的能力</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class APIServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Charset UTF8 = Charset.forName("utf-8");
    private static final String SALT = UUID.randomUUID().toString();
    private static final int EXPIRES = 2 * 60 * 60;
    private static final Logger logger = LoggerFactory.getLogger(APIServlet.class);

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
    @UrlPattern("api.key")
    public void getKey(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        /**
         * 外部应用程序免登录访问finder流程
         * 第一种: 全局访问, 尚未实现
         * 使用安全key访问, 安全key位于http header中
         * 
         * 第二种: 临时访问, 本类实现
         * 临时访问由当前登录用户发起, 应用场景:
         * 1. 使用第三方文件下载工具进行文件下载，例如在页面上使用迅雷下载文件。
         *    第三方下载工具无法获得当前登录用户的cookie，所以需要提供给第三方工具免登录下载的接口。
         * 
         * 2. 调用第三方工具打开文件。例如使用第三方的office在线预览接口。
         * 
         * 调用流程：
         * 1. 客户端发起ajax请求, 调用finder.key, 系统产生一个key返回给客户端;
         * 2. 客户端使用带key的url地址调用第三方工具;
         * 
         * 以文件下载为例:
         * 1. ajax获取key.
         * 2. 下载的URL地址: ?action=api.download&host=xx&workspace=xx&path=xx&key=xx
         */
        String userName = CurrentUser.getUserName(request);
        String host = ConfigFactory.getHostName();
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String work = Finder.getWork(request, workspace);
        String realPath = Finder.getRealPath(work, path);

        if(StringUtil.isBlank(userName)) {
            Ajax.denied(request, response);
            return;
        }

        if(realPath == null) {
            Ajax.error(request, response, path + " not exists !");
            return;
        }

        File file = new File(realPath);
        String relativePath = Path.getRelativePath(work, realPath);

        if(!file.exists()) {
            Ajax.error(request, response, 404, "file not exists.");
            return;
        }

        /**
         * 使用salt产生唯一ID, 保证重复调用不会产生不同的ID
         * 如果不用salt, 其他人可以根据host, workspace, path算出key, 根据key尝试访问有可能命中
         */
        String id = this.getId(host, workspace, relativePath);
        int mode = AccessController.getMode(userName, workspace, relativePath);

        /**
         * 读权限
         */
        if((mode & Operation.READ) <= 0) {
            Ajax.denied(request, response);
            return;
        }

        Key key = new Key();
        key.setId(id);
        key.setUserName(userName);
        key.setHost(host);
        key.setWorkspace(workspace);
        key.setPath(path);
        key.setMode(mode);

        Cache cache = CacheFactory.getInstance();
        cache.put(id, EXPIRES, key);

        logger.info("user.key: {}, {}", userName, id);
        Ajax.success(request, response, "\"" + id + "\"");
        return;
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("api.getFile")
    public void getFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        Key key = this.getKey(request);

        if(key == null) {
            Ajax.denied(request, response);
            return;
        }

        File file = this.getFile(request);

        if(file == null || !file.exists()) {
            Ajax.success(request, response, "null");
            return;
        }

        FileItem fileItem = FinderManager.getFileItem(file);
        fileItem.setMode(key.getMode());
        Ajax.success(request, response, FileItemList.getJSONString(fileItem));
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("api.getFileList")
    public void getFileList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        Key key = this.getKey(request);

        if(key == null) {
            Ajax.denied(request, response);
            return;
        }

        String userName = key.getUserName();
        String workspace = key.getWorkspace();
        String path = key.getPath();
        String work = Finder.getWork(request, workspace);

        FinderManager finderManager = new FinderManager(work);
        FileItemList fileItemList = finderManager.list(userName, workspace, path);
        Ajax.success(request, response, fileItemList.getJSONString());
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("api.get")
    public void get(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        File file = this.getFile(request);

        if(file == null || !file.exists() || !file.isFile()) {
            Ajax.denied(request, response);
            return;
        }
        Httpd.service(request, response, file, false);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("api.download")
    public void download(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        File file = this.getFile(request);

        if(file == null || !file.exists() || !file.isFile()) {
            Ajax.denied(request, response);
            return;
        }
        Httpd.service(request, response, file, true);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("api.upload")
    public void upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Ajax.error(request, response, 404, "UnsupportOperation");
    }

    /**
     * @param workspace
     * @param path
     * @return String
     */
    private String getId(String host, String workspace, String path) {
        String input = host + "@" + workspace + "/" + path;
        byte[] bytes = Digest.md5(input.getBytes(UTF8), SALT.getBytes(UTF8));
        return Hex.encode(bytes);
    }

    /**
     * @param request
     * @param key
     * @return File
     */
    private File getFile(HttpServletRequest request) {
        Key key = this.getKey(request);

        if(key == null) {
            return null;
        }
        return this.getFile(request, key);
    }

    /**
     * @param request
     * @param key
     * @return File
     */
    private File getFile(HttpServletRequest request, Key key) {
        String workspace = key.getWorkspace();
        String path = key.getPath();
        String work = Finder.getWork(request, workspace);
        String realPath = Finder.getRealPath(work, path);
        return new File(realPath);
    }

    /**
     * @param request
     * @return Key
     */
    private Key getKey(HttpServletRequest request) {
        String id = request.getParameter("key");

        if(StringUtil.isBlank(id)) {
            return null;
        }

        Cache cache = CacheFactory.getInstance();
        Object value = cache.get(id);

        if(value instanceof Key) {
            return (Key)value;
        }
        return null;
    }
}
