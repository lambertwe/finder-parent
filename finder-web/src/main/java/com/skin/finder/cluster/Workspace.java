/*
 * $RCSfile: Workspace.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.cluster;

/**
 * <p>Title: Workspace</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Workspace implements Comparable<Workspace> {
    private String name;
    private String displayName;
    private String work;
    private String charset;
    private boolean readonly;
    private int orderNum;

    /**
     * default
     */
    public Workspace() {
        this.readonly = true;
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
     * @return the work
     */
    public String getWork() {
        return this.work;
    }

    /**
     * @param work the work to set
     */
    public void setWork(String work) {
        this.work = work;
    }

    /**
     * @return the charset
     */
    public String getCharset() {
        return this.charset;
    }

    /**
     * @param charset the charset to set
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * @return the readonly
     */
    public boolean getReadonly() {
        return this.readonly;
    }

    /**
     * @param readonly the readonly to set
     */
    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
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

    @Override
    public int compareTo(Workspace workspace) {
        if(workspace == null) {
            return 1;
        }
        return (this.orderNum - workspace.orderNum);
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{name: ").append(this.name);
        buffer.append(", displayName: ").append(this.displayName);
        buffer.append(", work: ").append(this.work);
        buffer.append(", readonly: ").append(this.readonly);
        buffer.append("}");
        return buffer.toString();
    }
}
