/*
 * $RCSfile: UserHostManager.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.acl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.cache.Cache;
import com.skin.finder.cache.CacheFactory;
import com.skin.finder.cache.NULL;

/**
 * <p>Title: UserHostManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class UserHostManager {
    private static final int EXPIRY = 15 * 60;
    private static final String CACHE_KEY_PREFIX = "user_host_list_";
    private static final Cache cache = CacheFactory.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(UserHostManager.class);

    /**
     * @param userName
     * @return List<String>
     */
    @SuppressWarnings("unchecked")
    public static List<String> getHostList(String userName) {
        String key = CACHE_KEY_PREFIX + userName;
        Object value = cache.get(key);

        if(value instanceof NULL) {
            return null;
        }

        List<String> list = (List<String>)value;

        if(list == null) {
            list = HostPolicyManager.load(userName);
            setCache(key, list);
        }

        if(list != null) {
            List<String> temp = new ArrayList<String>();
            temp.addAll(list);
            return temp;
        }
        return null;
    }

    /**
     * @param userName
     * @param hostName
     */
    public static void add(String userName, String hostName) {
        List<String> list = getHostList(userName);

        if(list == null) {
            list = new ArrayList<String>();
        }

        list.add(hostName);
        HostPolicyManager.save(userName, list);
        flush(userName);
    }

    /**
     * @param userName
     * @param hostName
     * @return String
     */
    public static String remove(String userName, String hostName) {
        List<String> list = getHostList(userName);

        if(list == null || list.isEmpty()) {
            return null;
        }

        boolean flag = false;
        Iterator<String> iterator = list.iterator();

        while(iterator.hasNext()) {
            String name = iterator.next();

            if(name.equals(hostName)) {
                flag = true;
                iterator.remove();
            }
        }

        if(flag) {
            HostPolicyManager.save(userName, list);
            flush(userName);
            return hostName;
        }
        return null;
    }

    /**
     * @param key
     * @param hosts
     */
    public static void setCache(String key, List<String> hosts) {
        if(hosts != null) {
            cache.put(key, EXPIRY, hosts);
        }
        else {
            cache.put(key, EXPIRY, hosts);
        }
    }

    /**
     * @param userName
     */
    public static void flush(String userName) {
        String key = CACHE_KEY_PREFIX + userName;
        logger.debug("remove: {}", key);

        /**
         * 删除缓存
         */
        cache.remove(key);
    }
}
