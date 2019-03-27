/*
 * $RCSfile: ServletInfo.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.util;

import javax.servlet.ServletContext;

/**
 * <p>Title: ServletInfo</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ServletInfo {
    private String serverInfo = null;
    private int majorVersion = 0;
    private int minorVersion = 0;
    private String servletVersion = null;
    private String schema;
    private String localAddr = null;
    private int serverPort = 80;
    private String contextPath = null;
    private String servletPath = null;

    /**
     * @param servletContext
     */
    public ServletInfo(ServletContext servletContext) {
        this.serverInfo = servletContext.getServerInfo();
        this.majorVersion = servletContext.getMajorVersion();
        this.minorVersion = servletContext.getMinorVersion();
        this.servletVersion = new StringBuffer().append(this.majorVersion).append(".").append(this.minorVersion).toString();
    }

    /**
     * @return the serverInfo
     */
    public String getServerInfo() {
        return this.serverInfo;
    }

    /**
     * @param serverInfo the serverInfo to set
     */
    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }

    /**
     * @return the majorVersion
     */
    public int getMajorVersion() {
        return this.majorVersion;
    }

    /**
     * @param majorVersion the majorVersion to set
     */
    public void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    /**
     * @return the minorVersion
     */
    public int getMinorVersion() {
        return this.minorVersion;
    }

    /**
     * @param minorVersion the minorVersion to set
     */
    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    /**
     * @return the servletVersion
     */
    public String getServletVersion() {
        return this.servletVersion;
    }

    /**
     * @param servletVersion the servletVersion to set
     */
    public void setServletVersion(String servletVersion) {
        this.servletVersion = servletVersion;
    }

    /**
     * @return the schema
     */
    public String getSchema() {
        return this.schema;
    }

    /**
     * @param schema the schema to set
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * @return the localAddr
     */
    public String getLocalAddr() {
        return this.localAddr;
    }

    /**
     * @param localAddr the localAddr to set
     */
    public void setLocalAddr(String localAddr) {
        this.localAddr = localAddr;
    }

    
    /**
     * @return the serverPort
     */
    public int getServerPort() {
        return this.serverPort;
    }

    /**
     * @param serverPort the serverPort to set
     */
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * @return the contextPath
     */
    public String getContextPath() {
        return this.contextPath;
    }

    /**
     * @param contextPath the contextPath to set
     */
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    /**
     * @return the servletPath
     */
    public String getServletPath() {
        return this.servletPath;
    }

    /**
     * @param servletPath the servletPath to set
     */
    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    /**
     * @return String
     */
    public String getAppPath() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.schema);
        buffer.append("://");
        buffer.append(this.getLocalAddr());

        if(this.serverPort != 80) {
            buffer.append(":");
            buffer.append(this.getServerPort());
        }
        buffer.append(this.contextPath);
        buffer.append(this.servletPath);
        return buffer.toString();
    }
}
