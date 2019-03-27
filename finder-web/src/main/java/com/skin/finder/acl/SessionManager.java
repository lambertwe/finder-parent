/*
 * $RCSfile: SessionManager.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.acl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Title: SessionManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public interface SessionManager {
    /**
     * @param request
     * @return UserSession
     */
    UserSession getSession(HttpServletRequest request);

    /**
     * @param request
     * @param response
     * @param userSession
     * @param expiry
     */
    void registe(HttpServletRequest request, HttpServletResponse response, UserSession userSession, int expiry);

    /**
     * @param response
     */
    void logout(HttpServletResponse response);
}
