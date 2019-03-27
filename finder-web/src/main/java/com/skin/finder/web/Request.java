/*
 * $RCSfile: Request.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web;

import javax.servlet.http.HttpServletRequest;

import com.skin.finder.util.StringUtil;

/**
 * <p>Title: Request</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Request {
    /**
     * @param request
     * @return String
     */
    public static String getContextPath(HttpServletRequest request) {
        String contextPath = request.getContextPath();

        if(contextPath == null || contextPath.equals("/")) {
            return "";
        }
        return contextPath;
    }

    /**
     * @param request
     * @return String
     */
    public static String getRequestURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();

        if(queryString == null || queryString.length() < 1) {
            return requestURL.toString();
        }
        return requestURL.append("?").append(queryString).toString();
    }

    /**
     * @param request
     * @return String
     */
    public static String getAction(HttpServletRequest request) {
        String queryString = request.getQueryString();

        if(queryString == null) {
            return null;
        }
 
        int i = queryString.indexOf("action=");

        if(i < 0) {
            return null;
        }

        i += 7;
        int j = queryString.indexOf('&', i);

        if(j < 0) {
            return queryString.substring(i);
        }
        return queryString.substring(i, j);
    }

    /**
     * @param request
     * @return String
     */
    public static String getRemoteAddress(HttpServletRequest request) {
        String ip = null;
        String[] headers = {
                "X-Forward-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"};

        for(String header : headers) {
            ip = request.getHeader(header);

            if(StringUtil.notBlank(ip) && !ip.equalsIgnoreCase("unknown")) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
}
