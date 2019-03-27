/*
 * $RCSfile: AdminChecker.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2005 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.admin;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.config.ConfigFactory;
import com.skin.finder.config.Constants;
import com.skin.finder.servlet.page.Display;
import com.skin.finder.util.Ajax;
import com.skin.finder.web.util.CurrentUser;

/**
 * <p>Title: AdminChecker</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class AdminController {
    /**
     * @param request
     * @param response
     * @return boolean
     * @throws ServletException
     * @throws IOException
     */
    public static boolean check(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        return check(request, response, false);
    }

    /**
     * @param request
     * @param response
     * @param ajax
     * @return boolean
     * @throws ServletException
     * @throws IOException
     */
    public static boolean check(HttpServletRequest request, HttpServletResponse response, boolean ajax) throws IOException, ServletException {
        String root = ConfigFactory.getAdmin();
        String userName = CurrentUser.getUserName(request);

        if(!root.equals(userName)) {
            if(ajax) {
                Ajax.denied(request, response);
            }
            else {
                Display.denied(request, response);
            }
            return true;
        }
        return false;
    }

    /**
     * @param request
     * @return boolean
     */
    public static boolean hasSecurityKey(HttpServletRequest request) {
        String securityKey = ConfigFactory.getSecurityKey();
        String key = request.getHeader(Constants.HTTP_SECURITY_KEY);
        return (key != null && key.equals(securityKey));
    }

    /**
     * @return boolean
     */
    public static boolean getLock() {
        String location = ConfigFactory.getLocation();
        File file = new File(location, "host.lock");
        return file.exists();
    }
}
