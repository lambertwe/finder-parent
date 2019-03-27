/*
 * $RCSfile: LRULinkedHashMap.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>Title: LRULinkedHashMap</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 * @param <K>
 * @param <V>
 */
public class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 1L;
    private final int maxSize;

    /**
     * @param maxSize
     */
    public LRULinkedHashMap(int maxSize) {
        super((int) Math.ceil(maxSize / 0.75) + 1, 0.75f, true);
        this.maxSize = maxSize;
    }

    /**
     * @param eldest
     * @return boolean
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > this.maxSize;
    }
}
