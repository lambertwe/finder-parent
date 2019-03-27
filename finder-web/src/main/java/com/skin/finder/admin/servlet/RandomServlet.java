/*
 * $RCSfile: RandomServlet.java,v $
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
import java.security.SecureRandom;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.util.Ajax;
import com.skin.finder.web.UrlPattern;
import com.skin.finder.web.servlet.BaseServlet;

/**
 * <p>Title: RandomServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class RandomServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;
    private static final char[] cbuf = new char[] {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
        'u', 'v', 'w', 'x', 'y', 'z',
        '!', '@', '#', '$', '%', '&', '*', '+', '?', '='
    };

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.random.uuid")
    public void getUUID(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = UUID.randomUUID().toString();
        Ajax.success(request, response, "\"" + uuid + "\"");
    }

    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @UrlPattern("admin.random.string")
    public void getRandomString(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int length = this.getInteger(request, "length", 32);
        String result = getRandomString(length, length, 4);
        Ajax.success(request, response, "\"" + result + "\"");
    }

    /**
     * @param min
     * @param max
     * @param factor
     * @return String
     */
    private static String getRandomString(int min, int max, int factor) {
        int length = min;
        SecureRandom random = new SecureRandom();
        length = min + random.nextInt(max - min + 1);
        length = Math.max(length, min);

        char[] chars = new char[length];

        for(int i = 0; i < length; i++) {
            if((i + 1) % factor != 0) {
                chars[i] = cbuf[random.nextInt(36)];
            }
            else {
                chars[i] = cbuf[36 + random.nextInt(10)];
            }
        }
        return new String(chars);
    }
}

