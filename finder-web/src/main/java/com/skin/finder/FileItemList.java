/*
 * $RCSfile: FileItemList.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder;

import java.util.List;

import com.skin.finder.util.StringUtil;

/**
 * <p>Title: FileItemList</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class FileItemList {
    private String host;
    private String workspace;
    private String path;
    private int mode;
    private List<FileItem> fileList;

    /**
     * @return the host
     */
    public String getHost() {
        return this.host;
    }

    
    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    
    /**
     * @return the workspace
     */
    public String getWorkspace() {
        return this.workspace;
    }

    
    /**
     * @param workspace the workspace to set
     */
    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the mode
     */
    public int getMode() {
        return this.mode;
    }


    
    /**
     * @param mode the mode to set
     */
    public void setMode(int mode) {
        this.mode = mode;
    }


    /**
     * @return the fileList
     */
    public List<FileItem> getFileList() {
        return this.fileList;
    }

    /**
     * @param fileList the fileList to set
     */
    public void setFileList(List<FileItem> fileList) {
        this.fileList = fileList;
    }

    /**
     * @return String
     */
    public String getJSONString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{\"host\":\"");
        buffer.append(StringUtil.escape(this.getHost()));
        buffer.append("\",\"workspace\":\"");
        buffer.append(StringUtil.escape(this.getWorkspace()));
        buffer.append("\",\"path\":\"");
        buffer.append(StringUtil.escape(this.getPath()));
        buffer.append("\",\"mode\":\"");
        buffer.append(this.getMode());
        buffer.append("\",\"fileList\":[");

        if(this.fileList != null && this.fileList.size() > 0) {
            for(FileItem fileItem : this.fileList) {
                if(fileItem == null) {
                    continue;
                }

                append(buffer, fileItem);
                buffer.append(",");
            }
            buffer.deleteCharAt(buffer.length() - 1);
        }
        buffer.append("]}");
        return buffer.toString();
    }

    /**
     * @param fileItemList
     * @return String
     */
    public static String getJSONString(List<FileItem> fileItemList) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[");

        if(fileItemList != null && fileItemList.size() > 0) {
            for(FileItem fileItem : fileItemList) {
                if(fileItem == null) {
                    continue;
                }

                append(buffer, fileItem);
                buffer.append(",");
            }
            buffer.deleteCharAt(buffer.length() - 1);
        }
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * @param fileItem
     * @return String
     */
    public static String getJSONString(FileItem fileItem) {
        return append(new StringBuilder(), fileItem).toString();
    }

    /**
     * @param buffer
     * @param fileItem
     * @return StringBuilder
     */
    public static StringBuilder append(StringBuilder buffer, FileItem fileItem) {
        buffer.append("{\"name\":\"");
        buffer.append(fileItem.getFileName());
        buffer.append("\",\"size\":");
        buffer.append(fileItem.getFileSize());
        buffer.append(",\"modified\":");
        buffer.append(fileItem.getLastModified());
        buffer.append(",\"file\":");

        if(fileItem.getIsFile()) {
            buffer.append("1");
        }
        else {
            buffer.append("0");
        }

        buffer.append(",\"mode\":");
        buffer.append(fileItem.getMode());
        buffer.append("}");
        return buffer;
    }
}
