/*
 * $RCSfile: LogChatServlet.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 * http://www.finderweb.net
 */
package com.skin.finder.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.cluster.Agent;
import com.skin.finder.servlet.template.LogChatTemplate;
import com.skin.finder.util.Ajax;
import com.skin.finder.util.StringUtil;
import com.skin.finder.web.Request;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;
import com.skin.finder.web.util.CurrentUser;

/**
 * <p>Title: LogChatServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class LogChatServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(LogChatServlet.class);

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.chat")
    public void chat(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = CurrentUser.getUserName(request);
        request.setAttribute("userName", userName);
        request.setAttribute("remoteIp", Request.getRemoteAddress(request));
        LogChatTemplate.execute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.chat.send")
    public void send(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /**
         * 每台机器一个房间
         */
        if(Agent.dispatch(request, response)) {
            return;
        }

        String remoteIp = Request.getRemoteAddress(request);
        String userName = CurrentUser.getUserName(request);
        String nickName = request.getParameter("nickName");
        String message = request.getParameter("message");

        if(StringUtil.isBlank(nickName)) {
            nickName = userName;
        }

        if(StringUtil.isBlank(message)) {
            Ajax.error(request, response, "bad message.");
            return;
        }

        if(message.length() > 200) {
            Ajax.error(request, response, "message is too large.");
            return;
        }

        logger.info("{}@{}: {}", remoteIp, nickName, message);
        Ajax.success(request, response, "true");
    }

    /**
     * @param message
     * @return String
     */
    public String getResult(String message) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{\"status\":200,\"message\":\"");
        buffer.append(StringUtil.escape(message));
        buffer.append("\"}");
        return message;
    }
}
