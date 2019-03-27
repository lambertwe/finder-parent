/*
 * $RCSfile: PermissionManager.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2005 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.acl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.cache.Cache;
import com.skin.finder.cache.CacheFactory;
import com.skin.finder.cache.NULL;

/**
 * <p>Title: PermissionManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class PermissionManager {
    private static final int EXPIRY = 15 * 60;
    private static final String CACHE_KEY_PREFIX = "user_permission_";
    private static final Cache cache = CacheFactory.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(PermissionManager.class);

    /**
     * @param userName
     * @return UserPermission
     */
    public static UserPermission getPermission(String userName) {
        String key = CACHE_KEY_PREFIX + userName;
        Object value = cache.get(key);

        if(value instanceof NULL) {
            return new UserPermission(userName);
        }

        UserPermission userPermission = (UserPermission)value;

        if(userPermission == null) {
            userPermission = FilePolicyManager.getByUserName(userName);
            setCache(key, userPermission);
        }
        return userPermission;
    }

    /**
     * @param userPermission
     */
    public static void save(UserPermission userPermission) {
        String userName = userPermission.getUserName();
        FilePolicyManager.save(userPermission);
        flush(userName);
    }

    /**
     * @param userName
     * @param content
     */
    public static void save(String userName, String content) {
        FilePolicyManager.save(userName, content);
        flush(userName);
    }

    /**
     * @param key
     * @param userPermission
     */
    public static void setCache(String key, UserPermission userPermission) {
        if(userPermission != null) {
            cache.put(key, EXPIRY, userPermission);
        }
        else {
            cache.put(key, EXPIRY, NULL.NONE);
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
