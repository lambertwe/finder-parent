/*
 * $RCSfile: CacheFactory.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2005 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.cache;

/**
 * <p>Title: CacheFactory</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class CacheFactory {
    private static final Cache instance = new LRUCache(10000);

    /**
     * default
     */
    private CacheFactory() {
    }

    /**
     * @return Cache
     */
    public static Cache getInstance() {
        return instance;
    }
}
