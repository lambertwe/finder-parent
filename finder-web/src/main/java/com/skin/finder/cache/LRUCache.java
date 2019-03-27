/*
 * $RCSfile: LRUCache.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.cache;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Title: LRUCache</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class LRUCache implements Cache {
    private LRULinkedHashMap<String, CacheEntry> context;
    private ReentrantLock lock;

    /**
     * @param maxSize
     */
    public LRUCache(int maxSize) {
        this.context = new LRULinkedHashMap<String, CacheEntry>(maxSize);
        this.lock = new ReentrantLock(false);
    }

    @Override
    public void put(String key, int expires, Object object) {
        this.lock.lock();

        try {
            CacheEntry cacheEntry = new CacheEntry(object);
            cacheEntry.setExpires(System.currentTimeMillis() + (expires * 1000));
            this.context.put(key, cacheEntry);
        }
        finally {
            this.lock.unlock();
        }
    }

    @Override
    public void put(String key, int expires, long version, Object object) {
        this.lock.lock();

        try {
            CacheEntry cacheEntry = new CacheEntry(object);
            cacheEntry.setExpires(System.currentTimeMillis() + (expires * 1000));
            cacheEntry.setVersion(version);
            this.context.put(key, cacheEntry);
        }
        finally {
            this.lock.unlock();
        }
    }

    @Override
    public Object get(String key) {
        this.lock.lock();

        try {
            CacheEntry cacheEntry = this.context.get(key);

            if(cacheEntry != null) {
                if(System.currentTimeMillis() < cacheEntry.getExpires()) {
                    int hits = cacheEntry.getHits();
                    cacheEntry.setHits(hits + 1);
                    return cacheEntry.getValue();
                }
                this.context.remove(key);
            }
            return null;
        }
        finally {
            this.lock.unlock();
        }
    }


    @Override
    public Object get(String key, long version) {
        this.lock.lock();

        try {
            CacheEntry cacheEntry = this.context.get(key);

            if(cacheEntry != null) {
                if(cacheEntry.getExpires() > System.currentTimeMillis() && cacheEntry.getVersion() == version) {
                    int hits = cacheEntry.getHits();
                    cacheEntry.setHits(hits + 1);
                    return cacheEntry.getValue();
                }
                else {
                    this.context.remove(key);
                }
            }
            return null;
        }
        finally {
            this.lock.unlock();
        }
    }

    @Override
    public Object remove(String key) {
        this.lock.lock();

        try {
            CacheEntry cacheEntry = this.context.remove(key);

            if(cacheEntry != null) {
                return cacheEntry.getValue();
            }
            return null;
        }
        finally {
            this.lock.unlock();
        }
    }
}
