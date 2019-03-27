/*
 * $RCSfile: Cache.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2005 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.cache;

/**
 * <p>Title: Cache</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author Cache
 * @version 1.0
 */
public interface Cache {
    /**
     * @param key
     * @param expires
     * @param object
     */
    public void put(String key, int expires, Object object);

    /**
     * @param key
     * @param expires
     * @param object
     * @param version
     */
    public void put(String key, int expires, long version, Object object);

    /**
     * @param key
     * @return Object
     */
    public Object get(String key);

    /**
     * @param key
     * @param version
     * @return Object
     */
    public Object get(String key, long version);

    /**
     * @param key
     * @return Object
     */
    Object remove(String key);
}
