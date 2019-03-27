/*
 * $RCSfile: I18N.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.i18n;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: I18N</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class I18N {
    private static final Logger logger = LoggerFactory.getLogger(I18N.class);

    public static void main(String[] args) {
        print(Locale.CHINA);
        print(Locale.CHINESE);
        print(Locale.JAPAN);
        print(Locale.JAPANESE);
    }

    /**
     * default
     */
    private I18N() {
    }

    /**
     * @param request
     * @return LocalizationContext
     */
    public static LocalizationContext getBundle(HttpServletRequest request) {
        Locale locale = getLocale(request);
        LocalizationContext bundle = BundleManager.getInstance().getBundle("", locale);

        if(bundle == null) {
            bundle = BundleManager.getInstance().getBundle("", Locale.ENGLISH);

            if(bundle == null) {
                bundle = new LocalizationContext(null, Locale.ENGLISH);
                bundle.setLang("en");
            }
        }
        return bundle;
    }

    /**
     * @param value
     * @param variant
     * @return LocalizationContext
     */
    public static LocalizationContext getBundle(String value, String variant) {
        Locale locale = getLocale(value, variant);
        LocalizationContext bundle = BundleManager.getInstance().getBundle("", locale);

        if(bundle == null) {
            bundle = BundleManager.getInstance().getBundle("", Locale.ENGLISH);
        }
        return bundle;
    }

    /**
     * @param request
     * @return Locale
     */
    public static Locale getLocale(HttpServletRequest request) {
        String lang = getCookie(request, "finder_lang");

        if(lang != null && lang.trim().length() > 0) {
            return getLocale(lang, null);
        }

        Locale locale = request.getLocale();

        if(locale == null) {
            locale = Locale.getDefault();
        }
        return locale;
    }

    /**
     * @param value
     * @param variant
     * @return Locale
     */
    public static Locale getLocale(String value, String variant) {
        int i = 0;
        char ch = '\000';
        int length = value.length();
        StringBuilder buffer = new StringBuilder();

        for(; i < length; i++) {
            ch = value.charAt(i);

            if(ch != '_' && ch != '-') {
                buffer.append(ch);
            }
            else {
                break;
            }
        }

        String language = buffer.toString();
        String country = "";

        if((ch == '_') || (ch == '-')) {
            buffer.setLength(0);

            for(i++; i < length; i++) {
                ch = value.charAt(i);

                if(ch != '_' && ch != '-') {
                    buffer.append(ch);
                }
                else {
                    break;
                }
            }
            country = buffer.toString();
        }

        if(variant != null && variant.length() > 0) {
            return new Locale(language, country, variant);
        }
        return new Locale(language, country);
    }

    /**
     * @param request
     * @param name
     * @return Cookie
     */
    public static String getCookie(HttpServletRequest request, String name) {
        if(name != null) {
            Cookie[] cookies = request.getCookies();

            if(cookies != null && cookies.length > 0) {
                for(Cookie cookie : cookies) {
                    if(name.equals(cookie.getName())) {
                        try {
                            return URLDecoder.decode(cookie.getValue(), "utf-8");
                        }
                        catch(UnsupportedEncodingException e) {
                            logger.error(e.getMessage());
                        }
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param locale
     */
    public static void print(Locale locale) {
        System.out.println("===============================");
        System.out.println("country: " + locale.getCountry());
        System.out.println("displayCountry: " + locale.getDisplayCountry());
        System.out.println("language: " + locale.getLanguage());
        System.out.println("displayLanguage: " + locale.getDisplayLanguage());
        System.out.println("variant: " + locale.getVariant());
        System.out.println("displayVariant: " + locale.getDisplayVariant());
    }
}
