/*
 * $RCSfile: SessionFilter.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.filter;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.Less;
import com.skin.finder.acl.UserSession;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.config.Constants;
import com.skin.finder.util.StringUtil;
import com.skin.finder.web.Request;
import com.skin.finder.web.util.Client;

/**
 * <p>Title: SessionFilter</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SessionFilter implements Filter {
    protected String loginUrl;
    private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

    /**
     * @param filterConfig
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.loginUrl = filterConfig.getInitParameter("loginUrl");
        this.loginUrl = (this.loginUrl != null ? this.loginUrl.trim() : "");

        if(this.loginUrl == null || this.loginUrl.length() < 1) {
            this.loginUrl = "/finder?action=finder.login";
        }
    }

    /**
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        if(!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse)) {
            throw new ServletException("SessionFilter just supports HTTP requests");
        }

        HttpServletRequest request = (HttpServletRequest)(servletRequest);
        HttpServletResponse response = (HttpServletResponse)(servletResponse);

        if(check(request, response, this.loginUrl)) {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * @param request
     * @param response
     * @return boolean
     * @throws IOException
     */
    public static boolean check(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String loginUrl = getLoginURL(request);
        return check(request, response, loginUrl);
    }

    /**
     * @param request
     * @param response
     * @param loginUrl
     * @return boolean
     * @throws IOException 
     */
    public static boolean check(HttpServletRequest request, HttpServletResponse response, String loginUrl) throws IOException {
        String ip = Request.getRemoteAddress(request);
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        String action = Request.getAction(request);

        if(action == null) {
            action = "index";
        }

        if(action.equals("res")) {
            return true;
        }

        if(action.startsWith("api")) {
            return true;
        }

        long userId = 0L;
        String userName = null;
        UserSession userSession = Client.getSession(request);

        if(userSession != null) {
            userId = userSession.getUserId();
            userName = userSession.getUserName();
        }

        if(queryString != null) {
            logger.info("{}, {}|{}|{}?{}", ip, userId, userName, requestURI, queryString);
        }
        else {
            logger.info("{}, {}|{}|{}", ip, userId, userName, requestURI);
        }

        if(action.equals("finder.login") || action.equals("finder.status")) {
            return true;
        }

        String key = request.getHeader(Constants.HTTP_SECURITY_KEY);

        if(key != null) {
            String securityKey = ConfigFactory.getSecurityKey();

            if(logger.isDebugEnabled()) {
                logger.debug("SKey: {}", StringUtil.hide(key));
                logger.debug("LKey: {}", StringUtil.hide(securityKey));
            }

            if(key.equals(securityKey)) {
                return true;
            }
        }

        if(userId < 1L) {
            logger.debug("user not login: {}", queryString);

            if(action.equals("less.getTail") || action.equals("less.getRange") || action.equals("grep.find")) {
                logger.debug("{}, {}, redirect: finder?action=finder.login", ip, action);
                Less.redirect(response, "?action=finder.login");
                return false;
            }
            login(request, response, loginUrl);
            return false;
        }
        return true;
    }

    /**
     * @param request
     * @param response
     * @param loginUrl
     * @throws IOException
     */
    public static void login(HttpServletRequest request, HttpServletResponse response, String loginUrl) throws IOException {
        if(loginUrl.startsWith("http://") || loginUrl.startsWith("https://")) {
            response.sendRedirect(loginUrl);
            return;
        }

        /**
         * 当前请求的访问地址使用相对地址, 避免在使用反向代理的情况下暴露主机的IP和端口号
         */
        String contextPath = Request.getContextPath(request);
        String requestURL = getRequestURL(request);
        StringBuilder buffer = new StringBuilder();
        buffer.append(contextPath);
        buffer.append(loginUrl);

        if(StringUtil.notBlank(requestURL)) {
            if(loginUrl.indexOf('?') > -1) {
                buffer.append("&redirect=");
                buffer.append(URLEncoder.encode(requestURL, "utf-8"));
            }
            else {
                buffer.append("?redirect=");
                buffer.append(URLEncoder.encode(requestURL, "utf-8"));
            }
        }

        String url = buffer.toString();
        logger.info("loginUrl: {}", url);
        response.sendRedirect(url);
    }

    /**
     * @param request
     * @return String
     */
    public static String getLoginURL(HttpServletRequest request) {
        String loginUrl = request.getRequestURI();
        String contextPath = Request.getContextPath(request);

        if(contextPath.length() > 0) {
            loginUrl = loginUrl.substring(contextPath.length());
        }
        return loginUrl + "?action=finder.login";
    }

    /**
     * @param request
     * @return String
     */
    public static String getRequestURL(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();

        if(StringUtil.isBlank(queryString)) {
            return null;
        }
        return requestURI + "?" + queryString;
    }

    /**
     * 
     */
    @Override
    public void destroy() {
    }
}
