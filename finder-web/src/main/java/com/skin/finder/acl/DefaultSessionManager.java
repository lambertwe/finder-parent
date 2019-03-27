/*
 * $RCSfile: DefaultSessionManager.java,v $
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

import com.skin.finder.web.util.Client;

/**
 * <p>Title: DefaultSessionManager</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class DefaultSessionManager implements SessionManager {
    /**
     * @param request
     * @return UserSession
     */
    @Override
    public UserSession getSession(HttpServletRequest request) {
        return Client.getSession(request);
    }

    /**
     * @param request
     * @param response
     * @param userSession
     * @param expiry
     */
    @Override
    public void registe(HttpServletRequest request, HttpServletResponse response, UserSession userSession, int expiry) {
        Client.registe(request, response, userSession, null, "/", expiry, false);
    }

    /**
     * @param response
     */
    @Override
    public void logout(HttpServletResponse response) {
        Client.remove(response);
    }
}
