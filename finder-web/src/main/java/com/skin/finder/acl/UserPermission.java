/*
 * $RCSfile: UserPermission.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.acl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: UserPermission</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class UserPermission {
    private String userName;
    private Map<String, Permission> domains;

    /**
     * default
     */
    public UserPermission() {
        this.domains = new HashMap<String, Permission>();
    }

    /**
     * @param userName
     */
    public UserPermission(String userName) {
        this.userName = userName;
        this.domains = new HashMap<String, Permission>();
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
     * @param domain
     * @param permission
     */
    public void add(String domain, Permission permission) {
        this.domains.put(domain, permission);
    }

    /**
     * @param domain
     * @return Permission
     */
    public Permission getPermission(String domain) {
        return this.domains.get(domain);
    }

    /**
     * @param domain
     * @return boolean
     */
    public boolean allow(String domain) {
        return (this.domains.get(domain) != null);
    }

    /**
     * @return boolean
     */
    public boolean empty() {
        return this.domains.isEmpty();
    }

    /**
     * @return List<String>
     */
    public List<String> getKeys() {
        List<String> list = new ArrayList<String>();
        list.addAll(this.domains.keySet());
        return list;
    }

    /**
     * @return List<Permission>
     */
    public List<Permission> getValues() {
        List<Permission> list = new ArrayList<Permission>();
        list.addAll(this.domains.values());
        return list;
    }
}
