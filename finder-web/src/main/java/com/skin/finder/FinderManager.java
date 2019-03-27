/*
 * $RCSfile: FinderManager.java,v $
 * $Revision: 1.1 $
 * $Date: 2013-04-02 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.acl.AccessController;
import com.skin.finder.acl.Permission;
import com.skin.finder.acl.PermissionManager;
import com.skin.finder.acl.UserPermission;
import com.skin.finder.cluster.Host;
import com.skin.finder.cluster.Workspace;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.util.Html;
import com.skin.finder.util.Path;
import com.skin.finder.util.StringUtil;

/**
 * <p>Title: FinderManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class FinderManager {
    private String work;
    private static final boolean WINDOWS = (System.getProperty("os.name").indexOf("Windows") > -1);
    private static final Logger logger = LoggerFactory.getLogger(FinderManager.class);

    /**
     * @param work
     */
    public FinderManager(String work) {
        this.work = work;
    }

    /**
     * @param userName
     * @param workspace
     * @param src
     * @return FileItemList
     */
    public FileItemList list(String userName, String workspace, String src) {
        String realPath = Finder.getRealPath(this.work, src);

        if(realPath == null) {
            return null;
        }

        File dir = new File(realPath);
        String path = Path.getRelativePath(this.work, realPath);
        List<File> fileList = this.getFileList(dir.listFiles());
        List<FileItem> fileItemList = new ArrayList<FileItem>();

        if(path == null || path.length() < 1) {
            path = "/";
        }

        if(fileList.size() > 0) {
            this.filter(userName, workspace, this.work, fileList);

            for(File file : fileList) {
                if(file.isDirectory()) {
                    FileItem fileItem = getFileItem(file);
                    fileItemList.add(fileItem);
                }
            }

            for(File file : fileList) {
                if(file.isFile()) {
                    FileItem fileItem = getFileItem(file);
                    fileItemList.add(fileItem);
                }
            }
        }

        int mode = AccessController.getMode(userName, workspace, path);
        FileItemList result = new FileItemList();
        result.setHost(ConfigFactory.getHostName());
        result.setWorkspace(workspace);
        result.setPath(path);
        result.setMode(mode);
        result.setFileList(fileItemList);
        return result;
    }

    /**
     * @param list
     * @param listUrl
     * @param xmlUrl
     * @return String
     */
    public static String getHostXml(List<Host> list, String listUrl, String xmlUrl) {
        StringBuilder buffer = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        buffer.append("<tree>\r\n");

        if(list != null && list.size() > 0) {
            String name = null;
            String displayName = null;

            for(Host host : list) {
                name = Html.encode(host.getName());

                if(StringUtil.isBlank(host.getDisplayName())) {
                    displayName = name;
                }
                else {
                    displayName = Html.encode(host.getDisplayName());
                }

                buffer.append("<treeNode");
                buffer.append(" icon=\"host.gif\"");
                buffer.append(" value=\"");
                buffer.append(name);
                buffer.append("\"");
                buffer.append(" title=\"");
                buffer.append(displayName);
                buffer.append("\"");
                buffer.append(" href=\"javascript:void(0);\"");
                buffer.append(" nodeXmlSrc=\"");
                buffer.append(xmlUrl);
                buffer.append("host=");
                buffer.append(name);
                buffer.append("\"/>");
            }
        }
        buffer.append("</tree>");
        return buffer.toString();
    }

    /**
     * @param workspaces
     * @param listUrl
     * @param xmlUrl
     * @return String
     */
    public static String getWorkspaceXml(List<Workspace> workspaces, String listUrl, String xmlUrl) {
        StringBuilder buffer = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        buffer.append("<tree>\r\n");

        if(workspaces != null && workspaces.size() > 0) {
            String name = null;
            String displayName = null;

            for(Workspace workspace : workspaces) {
                name = Html.encode(workspace.getName());
                displayName = workspace.getDisplayName();

                if(StringUtil.isBlank(displayName)) {
                    displayName = name;
                }
                else {
                    displayName = Html.encode(displayName);
                }

                buffer.append("<treeNode");

                if(workspace.getReadonly()) {
                    buffer.append(" icon=\"iws.gif\"");
                }
                else {
                    buffer.append(" icon=\"jws.gif\"");
                }

                buffer.append(" value=\"");
                buffer.append(name);
                buffer.append("\"");
                buffer.append(" title=\"");
                buffer.append(displayName);
                buffer.append("\"");
                buffer.append(" href=\"");
                buffer.append(listUrl);
                buffer.append("workspace=");
                buffer.append(name);
                buffer.append("\"");
                buffer.append(" nodeXmlSrc=\"");
                buffer.append(xmlUrl);
                buffer.append("workspace=");
                buffer.append(name);
                buffer.append("\"/>");
            }
        }
        buffer.append("</tree>");
        return buffer.toString();
    }

    /**
     * @param file
     * @return FileItem
     */
    public static FileItem getFileItem(File file) {
        if(file.isFile()) {
            FileItem fileItem = new FileItem();
            fileItem.setFileName(file.getName());
            fileItem.setFileSize(file.length());
            fileItem.setLastModified(file.lastModified());
            fileItem.setIsFile(true);
            return fileItem;
        }

        if(file.isDirectory()) {
            FileItem fileItem = new FileItem();
            fileItem.setFileName(file.getName());
            fileItem.setFileSize(0L);
            fileItem.setLastModified(file.lastModified());
            fileItem.setIsFile(false);
            return fileItem;
        }
        return null;
    }

    /**
     * @param userName
     * @param workspace
     * @param path
     * @param listUrl
     * @param xmlUrl
     * @return String
     */
    public String getFolderXml(String userName, String workspace, String path, String listUrl, String xmlUrl) {
        String realPath = Finder.getRealPath(this.work, path);
        StringBuilder buffer = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        buffer.append("<tree>\r\n");

        if(realPath != null) {
            File dir = new File(realPath);
            List<File> fileList = this.getFolderList(userName, workspace, this.work, dir);

            if(fileList != null && fileList.size() > 0) {
                String encodeWorkspace = Html.encode(workspace);
                String relativePath = Path.getRelativePath(this.work, realPath);

                for(File file : fileList) {
                    if(file.isDirectory()) {
                        String name = Html.encode(file.getName());
                        String url = Html.encode(urlEncode(relativePath + "/" + file.getName(), "utf-8"));

                        buffer.append("<treeNode");
                        buffer.append(" icon=\"folder.gif\"");
                        buffer.append(" value=\"");
                        buffer.append(name);
                        buffer.append("\"");
                        buffer.append(" title=\"");
                        buffer.append(name);
                        buffer.append("\"");
                        buffer.append(" href=\"");
                        buffer.append(listUrl);
                        buffer.append("workspace=");
                        buffer.append(encodeWorkspace);
                        buffer.append("&amp;path=");
                        buffer.append(url);
                        buffer.append("\"");

                        if(this.hasChildFolder(file)) {
                            buffer.append(" nodeXmlSrc=\"");
                            buffer.append(xmlUrl);
                            buffer.append("workspace=");
                            buffer.append(encodeWorkspace);
                            buffer.append("&amp;path=");
                            buffer.append(url);
                            buffer.append("\"");
                        }
                        buffer.append("/>\r\n");
                    }
                }
            }
        }
        buffer.append("</tree>");
        return buffer.toString();
    }

    /**
     * @param userName
     * @param workspace
     * @param work
     * @param dir
     * @return List<File>
     */
    public List<File> getFolderList(String userName, String workspace, String work, File dir) {
        File[] files = dir.listFiles();

        if(files == null || files.length < 1) {
            return new ArrayList<File>();
        }

        List<File> fileList = new ArrayList<File>(files.length);

        for(File file : files) {
            if(file.exists() && file.isDirectory()) {
                fileList.add(file);
            }
        }

        this.filter(userName, workspace, work, fileList);

        if(!WINDOWS && fileList.size() > 1) {
            java.util.Collections.sort(fileList, FileComparator.getInstance());
        }
        return fileList;
    }

    /**
     * @param list
     * @param files
     * @return List<File>
     */
    private List<File> getFileList(File[] files) {
        if(files == null || files.length < 1) {
            return new ArrayList<File>();
        }

        List<File> fileList = new ArrayList<File>(files.length);

        for(File file : files) {
            fileList.add(file);
        }
        return fileList;
    }

    /**
     * @param file
     * @param name
     * @return int
     */
    public int rename(String file, String name) {
        String realPath = Finder.getRealPath(this.work, file);

        if(realPath == null) {
            return 0;
        }

        if(name.endsWith(".link.tail")) {
            return 0;
        }

        File oldFile = new File(realPath);
        File newFile = new File(oldFile.getParent(), name);
        String oldName = oldFile.getName();
        logger.info("rename {} to {}", oldFile.getAbsolutePath(), newFile.getAbsolutePath());

        /**
         * 完全相等
         */
        if(name.equals(oldName)) {
            return 1;
        }

        // windows系统不区分文件名大小写
        boolean ignoreNameCase = (WINDOWS && name.equalsIgnoreCase(oldName));

        if(newFile.exists()) {
            if(!ignoreNameCase) {
                return 0;
            }
        }

        if(ignoreNameCase) {
            String tmpName = UUID.randomUUID().toString();
            File tmpFile = new File(oldFile.getParent(), tmpName);
            oldFile.renameTo(tmpFile);
            tmpFile.renameTo(newFile);
        }
        else {
            oldFile.renameTo(newFile);
        }
        return (newFile.exists() ? 1 : 0);
    }

    /**
     * @param path
     * @param name
     * @return File
     */
    public File mkdir(String path, String name) {
        String realPath = Finder.getRealPath(this.work, path);

        if(realPath == null) {
            return null;
        }

        int i = 1;
        File dir = new File(realPath, name);

        while(dir.exists()) {
            dir = new File(realPath, name + "(" + i + ")");
            i++;
        }

        logger.info("mkdir {}", dir.getAbsolutePath());
        return (dir.mkdir() ? dir : null);
    }

    /**
     * @param path
     * @param name
     * @return File
     * @throws IOException
     */
    public File create(String path, String name) throws IOException {
        String realPath = Finder.getRealPath(this.work, path);

        if(realPath == null) {
            return null;
        }

        int i = 1;
        File file = new File(realPath, name);
        logger.info("create {}", file.getAbsolutePath());

        while(file.exists()) {
            file = new File(realPath, name + "(" + i + ")");
            i++;
        }
        return (file.createNewFile() ? file : null);
    }

    /**
     * @param path
     * @return int
     */
    public int delete(String path) {
        String realPath = Finder.getRealPath(this.work, path);

        if(realPath == null) {
            return 0;
        }

        File file = new File(realPath);

        if(file.isFile()) {
            try {
                boolean flag = file.delete();

                if(!flag) {
                    logger.warn("delete file " + file.getAbsolutePath() + " failed !");
                }
            }
            catch(Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
        else {
            this.delete(file);
        }
        return (file.exists() ? 0 : 1);
    }

    /**
     * @param file
     */
    public void delete(File file) {
        if(file.isDirectory()) {
            File[] files = file.listFiles();

            for(File f : files) {
                if(f.isDirectory()) {
                    delete(f);
                }
                else {
                    boolean flag = f.delete();

                    if(!flag) {
                        logger.warn("delete file " + file.getAbsolutePath() + " failed !");
                    }
                }
            }
        }

        boolean flag = file.delete();

        if(!flag) {
            logger.warn("delete file " + file.getAbsolutePath() + " failed !");
        }
    }

    /**
     * @param userName
     * @param workspace
     * @param path
     * @return Object
     */
    public List<FileItem> suggest(String userName, String workspace, String path) {
        String relativePath = Path.getStrictPath(path);
        String realPath = Finder.getRealPath(this.work, path);
        List<FileItem> fileItemList = new ArrayList<FileItem>();
        logger.info("relativePath: {}, realPath: {}", relativePath, realPath);

        if(realPath == null) {
            return fileItemList;
        }

        String prefix = null;
        int k = relativePath.lastIndexOf("/");

        if(k > -1 && (k + 1) < relativePath.length()) {
            prefix = relativePath.substring(k + 1);
        }

        File file = new File(realPath);
        List<File> list = null;

        if(prefix != null && prefix.length() > 0) {
            file = file.getParentFile();
        }

        if(file.exists()) {
            if(file.isDirectory()) {
                list = this.getFileList(file.listFiles());
            }
            else {
                String encoding = System.getProperty("file.encoding");
                File[] fileList = file.getParentFile().listFiles();
                list = new ArrayList<File>();

                if(encoding.equals("utf-8")) {
                    for(File f : fileList) {
                        String fileName = f.getName();

                        if(prefix == null || prefix.length() < 1) {
                            list.add(f);
                        }
                        else if(fileName.toLowerCase().startsWith(prefix)) {
                            list.add(f);
                        }
                    }
                }
                else {
                    try {
                        for(File f : fileList) {
                            String fileName = new String(f.getName().getBytes("utf-8"), "utf-8");

                            if(prefix == null || prefix.length() < 1) {
                                list.add(f);
                            }
                            else if(fileName.toLowerCase().startsWith(prefix)) {
                                list.add(f);
                            }
                        }
                    }
                    catch(UnsupportedEncodingException e) {
                    }
                }
            }
        }

        if(list != null && list.size() > 0) {
            this.filter(userName, workspace, this.work, list);

            for(File f : list) {
                FileItem fileItem = FinderManager.getFileItem(f);

                if(fileItem != null) {
                    fileItemList.add(fileItem);
                }
            }
        }
        return fileItemList;
    }

    /**
     * 查找文件
     * @param userName
     * @param workspace
     * @param path
     * @param searchment
     * @return List<File>
     */
    public List<File> find(String userName, String workspace, String path, String searchment) {
        String realPath = Finder.getRealPath(this.work, path);
        List<File> fileList = new ArrayList<File>();

        if(realPath == null) {
            return fileList;
        }

        File file = new File(realPath);

        if(file.isDirectory() == false) {
            return fileList;
        }

        List<String> stack = new ArrayList<String>();
        stack.add(file.getAbsolutePath());

        for(int i = 0; i < stack.size(); i++) {
            File[] list = new File(stack.get(i)).listFiles();

            if(list != null) {
                for(File f : list) {
                    if(f.isDirectory()) {
                        stack.add(f.getAbsolutePath());
                    }

                    if(f.getName().toLowerCase().indexOf(searchment) > -1) {
                        fileList.add(f);
                    }
                }
            }
        }

        for(int i = stack.size() - 1; i > -1; i--) {
            File f = new File(stack.get(i));

            if(f.getName().toLowerCase().indexOf(searchment) > -1) {
                fileList.add(f);
            }
        }

        if(fileList.size() > 0) {
            this.filter(userName, workspace, workspace, fileList);
        }
        return fileList;
    }

    /**
     * 过滤掉没有读权限的文件
     * @param userName
     * @param work
     * @param list
     */
    private List<File> filter(String userName, String workspace, String work, List<File> list) {
        if(list == null || list.isEmpty()) {
            return list;
        }

        String root = ConfigFactory.getAdmin();

        if(userName.equals(root)) {
            return list;
        }

        UserPermission userPermission = PermissionManager.getPermission(userName);
        Permission permission = userPermission.getPermission("read@" + workspace);

        if(permission == null) {
            list.clear();
            return list;
        }

        if(logger.isDebugEnabled()) {
            logger.debug("{}@{}, includes: {}, excludes: {}",
                    permission.getAction(),
                    permission.getWorkspace(),
                    permission.getIncludes(),
                    permission.getExcludes());
        }

        Iterator<File> iterator = list.iterator();

        while(iterator.hasNext()) {
            File file = iterator.next();
            String relativePath = Path.getRelativePath(work, file.getAbsolutePath());
            boolean flag = permission.match(relativePath);
            logger.debug("path: {}, {}", flag, relativePath);

            if(!flag) {
                iterator.remove();
            }
        }
        return list;
    }

    /**
     * @param file
     * @return boolean
     */
    private boolean hasChildFolder(File file) {
        File[] fileList = file.listFiles();

        if(fileList != null) {
            for(File f : fileList) {
                if(f.isDirectory()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param source
     * @param encoding
     * @return String
     */
    private static String urlEncode(String source, String encoding) {
        try {
            return URLEncoder.encode(source, encoding);
        }
        catch(UnsupportedEncodingException e) {
        }
        return "";
    }

    /**
     * @return the work
     */
    public String getWork() {
        return this.work;
    }

    /**
     * @param work the work to set
     */
    public void setHome(String work) {
        this.work = work;
    }
}
