/*
 * $RCSfile: Permission.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.acl;

import java.util.List;

import com.skin.finder.util.PathMatcher;

/**
 * <p>Title: Permission</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Permission {
    private String id;
    private String userName;
    private String action;
    private String workspace;
    private List<String> includes;
    private List<String> excludes;

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
     * @return the action
     */
    public String getAction() {
        return this.action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
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
     * @return the includes
     */
    public List<String> getIncludes() {
        return this.includes;
    }

    /**
     * @param includes the includes to set
     */
    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    /**
     * @return the excludes
     */
    public List<String> getExcludes() {
        return this.excludes;
    }

    /**
     * @param excludes the excludes to set
     */
    public void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }

    /**
     * @param path
     * @return boolean
     */
    public boolean match(String path) {
        if(path == null) {
            return false;
        }

        List<String> includes = this.getIncludes();
        List<String> excludes = this.getExcludes();

        if(includes == null || includes.isEmpty()) {
            return false;
        }

        if(excludes != null && excludes.size() > 0) {
            if(PathMatcher.match(path, excludes, true)) {
                return false;
            }
        }

        if(PathMatcher.match(path, includes, false)) {
            return true;
        }
        else {
            return false;
        }
    }
}
