/*
 * $RCSfile: FinderServlet.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 * http://www.finderweb.net
 */
package com.skin.finder;

/**
 * <p>Title: Key</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Key implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String userName;
    private String host;
    private String workspace;
    private String path;
    private int mode;

    /**
     * @return the id
     */
    public String getId() {
        return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

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
}

