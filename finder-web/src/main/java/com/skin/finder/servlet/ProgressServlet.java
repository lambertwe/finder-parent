/*
 * $RCSfile: ProgressServlet.java,v $
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

import com.skin.finder.cache.Cache;
import com.skin.finder.cache.CacheFactory;
import com.skin.finder.cluster.Agent;
import com.skin.finder.command.Progress;
import com.skin.finder.util.Ajax;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: ProgressServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ProgressServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    /**
     * ?action=finder.cancel&host=192.168.1.1&token=1234-abcd-5678-efgh
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.operation.cancel")
    public void cancel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String token = this.getTrimString(request, "token");

        if(token.length() < 1) {
            Ajax.error(request, response, 404, "Bad Request.");
            return;
        }

        Cache cache = CacheFactory.getInstance();
        Progress progress = (Progress)(cache.get(token));

        if(progress == null) {
            Ajax.error(request, response, 404, "Bad Request.");
            return;
        }

        progress.cancel();
        Ajax.success(request, response, "true");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("finder.operation.getProgress")
    public void getProgress(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(Agent.dispatch(request, response)) {
            return;
        }

        String token = this.getTrimString(request, "token");

        if(token.length() < 1) {
            Ajax.error(request, response, 404, "Bad Request.");
            return;
        }

        Cache cache = CacheFactory.getInstance();
        Progress progress = (Progress)(cache.get(token));

        if(progress == null) {
            Ajax.error(request, response, 404, "Bad Request.");
            return;
        }

        /**
         * 状态码
         * 200 - 正在处理
         * 299 - 处理完成
         * 500 - 处理失败
         */
        if(progress.getComplete()) {
            cache.remove(token);
        }

        if(progress.getError()) {
            cache.remove(token);
            Ajax.error(request, response, progress.getStatus(), progress.getMessage());
            return;
        }

        String json = this.stringify(progress);
        Ajax.success(request, response, json);
    }

    /**
     * @param progress
     * @return String
     */
    private String stringify(Progress progress) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{\"current\":");
        buffer.append("\"");
        buffer.append(progress.getCurrent());
        buffer.append("\",\"loaded\":");
        buffer.append(progress.getLoaded());
        buffer.append(",\"total\":");
        buffer.append(progress.getTotal());
        buffer.append("}");
        return buffer.toString();
    }
}
