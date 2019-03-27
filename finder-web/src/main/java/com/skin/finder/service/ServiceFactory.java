/*
 * $RCSfile: ServiceFactory.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.acl.DefaultSessionManager;
import com.skin.finder.acl.SessionManager;
import com.skin.finder.acl.SimpleUserManager;
import com.skin.finder.acl.UserManager;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.util.ClassUtil;

/**
 * <p>Title: ServiceFactory</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ServiceFactory {
    private static UserManager userManager;
    private static SessionManager sessionManager;
    private static final Logger logger = LoggerFactory.getLogger(ServiceFactory.class);

    static {
        init();
    }

    /**
     * 应用方需要注入的实现只有这两个，所以采用简单的实现
     */
    private static void init() {
        userManager = (UserManager)(create(UserManager.class.getName(), SimpleUserManager.class.getName()));
        sessionManager = (SessionManager)(create(SessionManager.class.getName(), DefaultSessionManager.class.getName()));
    }

    /**
     * @param name
     * @param defalutClassName
     * @return Object
     */
    private static Object create(String name, String defalutClassName) {
        String className = ConfigFactory.getString(name, defalutClassName);

        try {
            logger.info("create {}", className);
            return ClassUtil.getInstance(className);
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * @return UserManager
     */
    public static UserManager getUserManager() {
        return userManager;
    }

    /**
     * @return UserManager
     */
    public static SessionManager getSessionManager() {
        return sessionManager;
    }
}
