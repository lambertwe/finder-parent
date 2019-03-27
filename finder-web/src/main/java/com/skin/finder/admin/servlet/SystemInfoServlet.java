/*
 * $RCSfile: SystemInfoServlet.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 * http://www.finderweb.net
 */
package com.skin.finder.admin.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.admin.AdminController;
import com.skin.finder.admin.servlet.template.SystemInfoTemplate;
import com.skin.finder.cluster.Agent;
import com.skin.finder.config.ConfigFactory;
import com.skin.finder.util.IP;
import com.skin.finder.util.ServletInfo;
import com.skin.finder.util.SystemInfo;
import com.skin.finder.util.Version;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: SystemInfoServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SystemInfoServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.system.info")
    public void info(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        if(AdminController.check(request, response)) {
            return;
        }

        String hostName = ConfigFactory.getHostName();
        SystemInfo systemInfo = new SystemInfo();
        ServletInfo servletInfo = new ServletInfo(this.getServletContext());
        Version version = Version.getInstance();

        servletInfo.setSchema(request.getScheme());
        servletInfo.setLocalAddr(this.getLocalAddr(request));
        servletInfo.setServerPort(request.getServerPort());
        servletInfo.setContextPath(this.getContextPath(request));
        servletInfo.setServletPath(request.getServletPath());

        request.setAttribute("hostName", hostName);
        request.setAttribute("version", version);
        request.setAttribute("systemInfo", systemInfo);
        request.setAttribute("servletInfo", servletInfo);
        request.setAttribute("reqeustHeaders", this.getReqeustHeaders(request));
        SystemInfoTemplate.execute(request, response);
    }

    /**
     * @param request
     * @return String
     */
    private String getLocalAddr(HttpServletRequest request) {
        String localAddr = request.getLocalAddr();

        if(localAddr == null || localAddr.equals("localhost") || localAddr.equals("127.0.0.1")) {
            return IP.LOCAL;
        }
        return localAddr;
    }

    /**
     * @param request
     * @return String
     */
    private String getReqeustHeaders(HttpServletRequest request) {
        Map<String, List<String>> map = this.getHeaderMap(request);
        StringBuilder buffer = new StringBuilder();

        for(Map.Entry<String, List<String>> entry : map.entrySet()) {
            String name = entry.getKey();
            List<String> values = entry.getValue();

            if(values != null && values.size() > 0) {
                for(String value : values) {
                    if(name.equalsIgnoreCase("cookie")) {
                        buffer.append(name);
                        buffer.append(": ******\r\n");
                    }
                    else {
                        buffer.append(name);
                        buffer.append(": ");
                        buffer.append(value);
                        buffer.append("\r\n");
                    }
                }
            }
        }
        return buffer.toString();
    }

    /**
     * @param request
     * @return Map<String, List<String>>
     */
    private Map<String, List<String>> getHeaderMap(HttpServletRequest request) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        Enumeration<?> enums = request.getHeaderNames();

        while(enums.hasMoreElements()) {
            Object name = enums.nextElement();

            if(name != null) {
                map.put(name.toString(), this.getHeaders(request, name.toString()));
            }
        }
        return map;
    }

    /**
     * @param request
     * @param name
     * @return List<String>
     */
    private List<String> getHeaders(HttpServletRequest request, String name) {
        List<String> values = new ArrayList<String>();
        Enumeration<?> enums = request.getHeaders(name);

        while(enums.hasMoreElements()) {
            Object value = enums.nextElement();

            if(value != null) {
                values.add(value.toString());
            }
        }
        return values;
    }
}
