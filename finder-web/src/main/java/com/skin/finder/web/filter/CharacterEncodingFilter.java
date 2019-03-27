/*
 * $RCSfile: CharacterEncodingFilter.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web.filter;

import java.io.IOException;

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

import com.skin.finder.web.Request;

/**
 * <p>Title: CharacterEncodingFilter</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class CharacterEncodingFilter implements Filter {
    private String encoding;
    private static final Logger logger = LoggerFactory.getLogger("accessLogger");

    /**
     *
     */
    public CharacterEncodingFilter() {
    }

    /**
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.encoding = filterConfig.getInitParameter("encoding");
    }

    /**
     * @param encoding
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
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
            throw new ServletException("CharacterEncodingFilter just supports HTTP requests");
        }

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String ip = Request.getRemoteAddress(request);
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        String userAgent = request.getHeader("User-Agent");
        String referer = request.getHeader("Referer");

        if(queryString != null) {
            logger.info("{},{},{}?{},\"{}\",\"{}\"", ip, method, requestURI, queryString, referer, userAgent);
        }
        else {
            logger.info("{},{},{},\"{}\",\"{}\"", ip, method, requestURI, referer, userAgent);
        }

        request.setAttribute("FILTER_REQUEST_URI", request.getRequestURI());
        request.setAttribute("FILTER_REQUEST_URL", request.getRequestURL());
        doFilterInternal(request, response, filterChain);
    }

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(this.encoding != null && request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(this.encoding);
            response.setCharacterEncoding(this.encoding);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * destroy
     */
    @Override
    public void destroy() {
    }
}
