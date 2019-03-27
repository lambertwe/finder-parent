/*
 * $RCSfile: Agent.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.cluster;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.config.ConfigFactory;
import com.skin.finder.servlet.page.Display;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.StringUtil;
import com.skin.finder.util.URLParameter;
import com.skin.finder.web.servlet.ProxyDispatcher;

/**
 * <p>Title: Agent</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Agent {
    private static final Logger logger = LoggerFactory.getLogger(Agent.class);

    /**
     * @param request
     * @param response
     * @return boolean
     * @throws IOException
     * @throws ServletException
     */
    public static boolean dispatch(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String queryString = request.getQueryString();
        URLParameter params = URLParameter.parse(queryString);
        return dispatch(request, response, params.getString("host"), true);
    }

    /**
     * @param request
     * @param response
     * @param ajax
     * @return boolean
     * @throws IOException
     * @throws ServletException
     */
    public static boolean dispatch(HttpServletRequest request, HttpServletResponse response, boolean ajax) throws IOException, ServletException {
        String queryString = request.getQueryString();
        URLParameter params = URLParameter.parse(queryString);
        return dispatch(request, response, params.getString("host"), ajax);
    }

    /**
     * @param request
     * @param response
     * @param hostName
     * @param ajax
     * @return boolean
     * @throws IOException
     * @throws ServletException
     */
    public static boolean dispatch(HttpServletRequest request, HttpServletResponse response, String hostName, boolean ajax) throws IOException, ServletException {
        if(StringUtil.isBlank(hostName)) {
            return false;
        }

        String self = ConfigFactory.getHostName();

        if(hostName.equals(self)) {
            return false;
        }

        Host node = ClusterManager.getHost(hostName);

        if(node == null) {
            if(ajax) {
                Ajax.error(request, response, hostName + " not exists.");
            }
            else {
                Display.error(request, response, 404, hostName + " not exists.");
            }
            return true;
        }

        String url = node.getUrl();
        String queryString = URLParameter.remove(request.getQueryString(), "host");

        if(StringUtil.notBlank(queryString)) {
            url = url + "?" + queryString;
        }

        logger.debug("agent: {}, {}", hostName, url);

        try {
            ProxyDispatcher.dispatch(request, response, url);
        }
        catch(Exception e) {
            if(ajax) {
                Ajax.error(request, response, "'" + hostName + "' is unavailable.");
            }
            else {
                Display.error(request, response, 404, "'" + hostName + "' is unavailable.");
            }
        }
        return true;
    }
}
