/*
 * $RCSfile: ContentEntry.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web;

/**
 * <p>Title: ContentEntry</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ContentEntry {
    private String name;
    private int type;
    private boolean memory;
    private byte[] bytes;
    private long lastModified;

    /**
     * bin
     */
    public static final int BIN = 0;

    /**
     * gzip
     */
    public static final int ZIP = 1;

    /**
     * @param name
     * @param type
     * @param memory
     * @param lastModified
     * @param bytes
     */
    public ContentEntry(String name, int type, boolean memory, long lastModified, byte[] bytes) {
        this.name = name;
        this.type = type;
        this.memory = memory;
        this.bytes = bytes;
        this.lastModified = lastModified;
    }

    /**
     * @return String
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public int getType() {
        return this.type;
    }

    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * @return the memory
     */
    public boolean getMemory() {
        return this.memory;
    }

    
    /**
     * @param memory the memory to set
     */
    public void setMemory(boolean memory) {
        this.memory = memory;
    }

    /**
     * @return the bytes
     */
    public byte[] getBytes() {
        return this.bytes;
    }

    /**
     * @param bytes the bytes to set
     */
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * @return long
     */
    public long getLastModified() {
        return this.lastModified;
    }

    /**
     * @param lastModified
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}
