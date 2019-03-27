/*
 * $RCSfile: Host.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Title: Host</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Host implements Comparable<Host> {
    private String name;
    private String displayName;
    private String url;
    private int orderNum;
    private Map<String, Workspace> workspaces;

    /**
     * default
     */
    public Host() {
        this.workspaces = new ConcurrentHashMap<String, Workspace>();
    }

    /**
     * @param workspace
     */
    public void add(Workspace workspace) {
        String name = workspace.getName();

        if(name == null || (name = name.trim()).length() < 1) {
            throw new NullPointerException("name must be not null");
        }
        this.workspaces.put(name, workspace);
    }

    /**
     * @param name
     * @return Workspace
     */
    public Workspace remove(String name) {
        return this.workspaces.remove(name);
    }

    /**
     * @param name
     * @return Workspace
     */
    public Workspace getWorkspace(String name) {
        return this.workspaces.get(name);
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the orderNum
     */
    public int getOrderNum() {
        return this.orderNum;
    }

    /**
     * @param orderNum the orderNum to set
     */
    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * @return List<Workspace>
     */
    public List<Workspace> getWorkspaces() {
        List<Workspace> list = new ArrayList<Workspace>();
        list.addAll(this.workspaces.values());
        java.util.Collections.sort(list);
        return list;
    }

    /**
     * @return int
     */
    public int size() {
        return this.workspaces.size();
    }

    @Override
    public int compareTo(Host host) {
        if(host == null) {
            return 1;
        }
        return (this.orderNum - host.orderNum);
    }

    /**
     * destroy
     */
    public void destroy() {
        if(this.workspaces != null) {
            this.workspaces.clear();
            this.workspaces = null;
        }
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{name: ").append(this.name);
        buffer.append(", displayName: ").append(this.displayName);
        buffer.append(", url: ").append(this.url);
        buffer.append("}");
        return buffer.toString();
    }
}
