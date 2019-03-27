/*
 * $RCSfile: SecurityParameter.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.security;

import java.security.PrivateKey;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.config.ConfigFactory;
import com.skin.finder.util.Base64;
import com.skin.finder.util.StringUtil;
import com.skin.finder.util.URLParameter;

/**
 * <p>Title: SecurityParameter</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class SecurityParameter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityParameter.class);

    /**
     * default
     */
    private SecurityParameter() {
    }

    /**
     * @param request
     * @param names
     * @return URLParameter
     */
    public static URLParameter getURLParameter(HttpServletRequest request, String[] names) {
        long ts = 0L;
        String timestamp = request.getParameter("timestamp");

        if(StringUtil.notBlank(timestamp)) {
            try {
                ts = Long.parseLong(timestamp);
            }
            catch(NumberFormatException e) {
            }
        }
        return getURLParameter(request, names, ts);
    }

    /**
     * @param request
     * @param names
     * @param timestamp
     * @return URLParameter
     */
    public static URLParameter getURLParameter(HttpServletRequest request, String[] names, long timestamp) {
        URLParameter params = new URLParameter();

        if(timestamp <= 0L) {
            return params;
        }

        try {
            for(String name : names) {
                String value = decrypt(request, name);

                if(StringUtil.isBlank(value)) {
                    return params;
                }

                int j = value.lastIndexOf("|");

                if(j < 0) {
                    return params;
                }

                long ts = Long.parseLong(value.substring(j +  1));
                value = value.substring(0, j);

                if(ts != timestamp) {
                    return params;
                }

                params.setParameter(name, value);
                params.setParameter("timestamp", timestamp);
            }
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return params;
    }

    /**
     * @param request
     * @param name
     * @return String
     */
    public static String decrypt(HttpServletRequest request, String name) {
        String value = request.getParameter(name);

        if(StringUtil.isBlank(value)) {
            return null;
        }

        if(value.length() > RSA.MAX_DECRYPT_BLOCK) {
            logger.warn("bad value - " + name + ": " + value);
            return null;
        }

        try {
            PrivateKey privateKey = RSAKeyFactory.getPrivateKey(ConfigFactory.getPrivateKey());
            byte[] result = RSA.decrypt(Base64.decode(value.getBytes()), privateKey);
            return new String(result, "utf-8");
        }
        catch(Exception e) {
            logger.error("rsa.decrypt error - " + name + ": " + value, e);
        }
        return null;
    }
}
