/*
 * $RCSfile: CacheEntry.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.cache;

/**
 * <p>Title: CacheEntry</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class CacheEntry {
    private int hits = 0;
    private long expires = 0L;
    private long version = 0L;
    private Object value = null;

    /**
     * @param value
     */
    public CacheEntry(Object value) {
        this.value = value;
    }

    /**
     * @param value
     * @param expires
     */
    public CacheEntry(Object value, long expires) {
        this.value = value;
        this.expires = expires;
    }

    /**
     * @return the hits
     */
    public int getHits() {
        return this.hits;
    }

    /**
     * @param hits the hits to set
     */
    public void setHits(int hits) {
        this.hits = hits;
    }

    /**
     * @return the expires
     */
    public long getExpires() {
        return this.expires;
    }

    /**
     * @param expires the expires to set
     */
    public void setExpires(long expires) {
        this.expires = expires;
    }

    /**
     * @return the version
     */
    public long getVersion() {
        return this.version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(long version) {
        this.version = version;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
