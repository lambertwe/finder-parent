package com.skin.finder;

import java.util.List;

import com.skin.finder.util.StringUtil;

/**
 * <p>Title: ZipItemList</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ZipItemList {
    private String host;
    private String workspace;
    private String path;
    private int mode;
    private List<ZipItem> zipItemList;

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
     * @return the zipItemList
     */
    public List<ZipItem> getZipItemList() {
        return this.zipItemList;
    }

    /**
     * @param zipItemList the zipItemList to set
     */
    public void setZipItemList(List<ZipItem> zipItemList) {
        this.zipItemList = zipItemList;
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

        if(this.zipItemList != null && this.zipItemList.size() > 0) {
            for(ZipItem zipItem : this.zipItemList) {
                if(zipItem == null) {
                    continue;
                }

                append(buffer, zipItem);
                buffer.append(",");
            }
            buffer.deleteCharAt(buffer.length() - 1);
        }
        buffer.append("]}");
        return buffer.toString();
    }

    /**
     * @param zipItemList
     * @return String
     */
    public static String getJSONString(List<ZipItem> zipItemList) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("[");

        if(zipItemList != null && zipItemList.size() > 0) {
            for(ZipItem zipItem : zipItemList) {
                if(zipItem == null) {
                    continue;
                }

                append(buffer, zipItem);
                buffer.append(",");
            }
            buffer.deleteCharAt(buffer.length() - 1);
        }
        buffer.append("]");
        return buffer.toString();
    }

    /**
     * @param zipItem
     * @return String
     */
    public static String getJSONString(ZipItem zipItem) {
        return append(new StringBuilder(), zipItem).toString();
    }

    /**
     * @param buffer
     * @param zipItem
     * @return StringBuilder
     */
    public static StringBuilder append(StringBuilder buffer, ZipItem zipItem) {
        buffer.append("{\"name\":\"");
        buffer.append(zipItem.getFileName());
        buffer.append("\",\"size\":");
        buffer.append(zipItem.getFileSize());
        buffer.append(",\"modified\":");
        buffer.append(zipItem.getLastModified());
        buffer.append(",\"file\":");

        if(zipItem.getIsFile()) {
            buffer.append("1");
        }
        else {
            buffer.append("0");
        }

        buffer.append(",\"mode\":0}");
        return buffer;
    }
}
