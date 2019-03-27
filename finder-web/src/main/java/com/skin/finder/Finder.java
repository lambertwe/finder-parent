/*
 * $RCSfile: Finder.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.skin.finder.cluster.WorkspaceManager;
import com.skin.finder.util.Path;

/**
 * <p>Title: Finder</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Finder {
    /**
     * servletContext
     */
    public static final String SERVLETCONTEXT = "servletContext";

    /**
     * @param request
     * @return File
     * @throws ServletException
     */
    public static File getFile(HttpServletRequest request) throws ServletException {
        String workspace = request.getParameter("workspace");
        String path = request.getParameter("path");
        String work = Finder.getWork(request, workspace);

        if(work == null) {
            return null;
        }

        String realPath = Finder.getRealPath(work, path);

        if(realPath == null) {
            return null;
        }
        return new File(realPath);
    }

    /**
     * @param parent
     * @param path
     * @return File
     */
    public static File getRealFile(String parent, String path) {
        String realPath = Finder.getRealPath(parent, path);

        if(realPath != null) {
            return new File(realPath);
        }
        return null;
    }

    /**
     * @param parent
     * @param path
     * @return String
     */
    public static String getRealPath(String parent, String path) {
        String temp = null;

        if(path != null) {
            temp = Path.getStrictPath(path).trim();
        }
        else {
            temp = "/";
        }

        if(temp.length() < 1 || temp.equals("/")) {
            return parent;
        }

        String work = Path.join(parent, "");
        String full = Path.join(parent, temp);

        if(full.startsWith(work)) {
            return full;
        }
        else {
            return null;
        }
    }

    /**
     * @param request
     * @param name
     * @return String
     */
    public static String getWork(HttpServletRequest request, String name) {
        if(name == null) {
            return null;
        }

        String work = WorkspaceManager.getWork(name);

        if(work == null) {
            return null;
        }

        if(work.startsWith("file:")) {
            work = work.substring(5);
        }
        else if(work.startsWith("contextPath:")) {
            ServletContext servletContext = getServletContext(request);
            work = servletContext.getRealPath(work.substring(12));
        }

        File home = new File(work);

        if(!home.exists() || !home.isDirectory()) {
            return null;
        }
        return home.getAbsolutePath();
    }

    /**
     * @param dir
     * @param name
     * @return File
     * @throws IOException 
     */
    public static File getFile(File dir, String name) throws IOException {
        String prefix = null;
        String extension = null;
        int k = name.lastIndexOf('.');

        if(k > -1) {
            prefix = name.substring(0, k);
            extension = name.substring(k);
        }
        else {
            prefix = name;
            extension = "";
        }

        int i = 1;
        int count = 0;
        File file = new File(dir, name);

        if(!file.exists()) {
            file.createNewFile();
            return file;
        }

        while((count < 1000000)) {
            file = new File(dir, prefix + "(" + i + ")" + extension);
            i++;

            if(!file.exists()) {
                file.createNewFile();
                return file;
            }
        }
        return null;
    }

    /**
     * @param request
     * @return ServletContext
     */
    public static ServletContext getServletContext(HttpServletRequest request) {
        return (ServletContext)(request.getAttribute(SERVLETCONTEXT));
    }
}
