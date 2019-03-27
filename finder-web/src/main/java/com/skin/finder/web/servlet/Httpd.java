/*
 * $RCSfile: Httpd.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URLEncoder;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.util.GMTUtil;
import com.skin.finder.util.IO;
import com.skin.finder.util.MimeType;
import com.skin.finder.util.StringUtil;
import com.skin.finder.web.Range;

/**
 * <p>Title: Httpd</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Httpd {
    private static final Logger logger = LoggerFactory.getLogger(Httpd.class);

    /**
     * @param request
     * @param response
     * @param file
     * @param download
     * @throws IOException
     */
    public static void service(HttpServletRequest request, HttpServletResponse response, File file, boolean download) throws IOException {
        long length = file.length();
        long lastModified = file.lastModified();
        String eTag = getETag(length, lastModified);

        if(!logger.isInfoEnabled()) {
            return;
        }

        if(!checkIfHeaders(request, response, eTag, lastModified)) {
            return;
        }

        Range range = Range.parse(request, length);
        String httpDate = GMTUtil.format(lastModified);
        String contentType = MimeType.getMimeType(file.getName());
        logger.debug("[{}], {}", length, file.getName());

        // cache
        // response.setHeader("Cache-Control", "private");
        // response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("ETag", eTag);
        response.setHeader("Last-Modified", GMTUtil.format(lastModified));
        response.setHeader("Date", httpDate);
        response.setDateHeader("Expires", System.currentTimeMillis() + 60L * 60L * 1000L);
        response.setContentType(contentType);

        if(range == null) {
            response.setHeader("Content-Length", String.valueOf(length));

            if(download) {
                String userAgent = request.getHeader("User-Agent");
                response.setHeader("Content-Disposition", "attachment; " + getAttachmentName(file.getName(), userAgent));
            }

            InputStream inputStream = null;

            try {
                inputStream = new FileInputStream(file);
                IO.copy(inputStream, response.getOutputStream(), 8192, length);
            }
            catch(IOException e) {
            }
            finally {
                IO.close(inputStream);
            }
            logger.debug("download end: {}", file.getName());
        }
        else {
            RandomAccessFile raf = null;
            long size = range.getSize();
            long maxBodySize = 5L * 1024L * 1024L;
            String contentRange = range.getContentRange();
            response.setStatus(206);
            response.setHeader("Content-Range", contentRange);
            response.setHeader("Content-Length", String.valueOf(size));
            response.setHeader("Part-Size", String.valueOf(maxBodySize));
            response.setHeader("Content-Type", contentType);

            try {
                raf = new RandomAccessFile(file, "r");

                if(range.start > 0 && range.start < length) {
                    raf.seek(range.start);
                }
                logger.debug("response.size: {}, response.range: {}", size, contentRange);

                long t1 = System.currentTimeMillis();
                IO.copy(raf, response.getOutputStream(), size);
                long t2 = System.currentTimeMillis();
                logger.debug("response.times: {}", (t2 - t1));
            }
            catch(IOException e) {
                logger.debug(e.getMessage());
            }
            finally {
                IO.close(raf);
            }
        }
    }

    /**
     * @param fileName
     * @param userAgent
     * @return String
     */
    private static String getAttachmentName(String fileName, String userAgent) {
        String utf8 = "utf-8";

        try {
            if(StringUtil.isBlank(userAgent)) {
                return "filename=\"" + URLEncoder.encode(fileName, utf8) + "\"";
            }

            /**
             * 基于网上已知的UA实现，并不绝对准确
             * 
             * UA规范
             * 浏览器标识 (操作系统标识; 加密等级标识; 浏览器语言) 渲染引擎标识 版本信息
             */

            /**
             * Chrome
             * Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36
             * Edge中也可能包含Chrome, Safari等字样
             */
            if(userAgent.indexOf("Chrome/") > -1) {
                return "filename=\"" + URLEncoder.encode(fileName, utf8) + "\"";
            }

            /**
             * Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0
             */
            if(userAgent.indexOf("Firefox/") > -1) {
                return "filename*=UTF-8''" + URLEncoder.encode(fileName, utf8);
            }

            /**
             * IE6 - IE8
             * Mozilla/4.0 (compatible;MSIE 8.0; Windows NT 6.1; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; Media Center PC 6.0)
             * 
             * IE11
             * Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko
             * 
             * Edge
             * Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586
             */
            if(userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident/") > -1) {
                return "filename=\"" + URLEncoder.encode(fileName, utf8) + "\"";
            }

            /**
             * 猜测
             */
            if(userAgent.indexOf("Opera/") > -1) {
                return "filename*=UTF-8''" + URLEncoder.encode(fileName, utf8);
            }

            /**
             * Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7Safari/534.57.2
             */
            if(userAgent.indexOf("Safari/") > -1) {
                return "filename=\"" + new String(fileName.getBytes(utf8), "ISO8859-1") + "\"";
            }

            /**
             * 猜测
             */
            if(userAgent.indexOf("AppleWebKit/") > -1) {
                return "filename=\"" + new String(fileName.getBytes(utf8), "ISO8859-1") + "\"";
            }
            return "filename=\"" + URLEncoder.encode(fileName, utf8) + "\"";
        }
        catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "unknown";
    }

    /**
     * @param length
     * @param lastModified
     * @return String
     */
    public static String getETag(long length, long lastModified) {
        return ("W/\"" + length + "-" + lastModified + "\"");
    }

    /**
     * @param request
     * @param response
     * @param eTag
     * @param lastModified
     * @return boolean
     * @throws IOException
     */
    public static boolean checkIfHeaders(HttpServletRequest request, HttpServletResponse response, String eTag, long lastModified) throws IOException {
        if(!checkIfMatch(request, response, eTag, lastModified)) {
            return false;
        }

        if(!checkIfModifiedSince(request, response, eTag, lastModified)) {
            return false;
        }

        if(!checkIfNoneMatch(request, response, eTag, lastModified)) {
            return false;
        }

        if(!checkIfUnmodifiedSince(request, response, eTag, lastModified)) {
            return false;
        }
        return true;
    }

    /**
     * @param request
     * @param response
     * @param eTag
     * @param lastModified
     * @return boolean
     * @throws IOException
     */
    protected static boolean checkIfMatch(HttpServletRequest request, HttpServletResponse response, String eTag, long lastModified) throws IOException {
        String ifMatch = request.getHeader("If-Match");

        if(ifMatch != null && ifMatch.indexOf('*') < 0) {
            if(contains(ifMatch, eTag)) {
                return true;
            }
            else {
                response.sendError(412);
                return false;
            }
        }
        return true;
    }

    /**
     * @param request
     * @param response
     * @param eTag
     * @param lastModified
     * @return boolean
     * @throws IOException
     */
    protected static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response, String eTag, long lastModified) throws IOException {
        try {
            long ifModifiedSince = request.getDateHeader("If-Modified-Since");

            if(ifModifiedSince != -1L) {
                if((request.getHeader("If-None-Match") == null) && (lastModified < ifModifiedSince + 1000L)) {
                    response.setStatus(304);
                    response.setHeader("ETag", eTag);
                    return false;
                }
            }
        }
        catch(IllegalArgumentException e) {
            return true;
        }
        return true;
    }

    /**
     * @param request
     * @param response
     * @param eTag
     * @param lastModified
     * @return boolean
     * @throws IOException
     */
    protected static boolean checkIfNoneMatch(HttpServletRequest request, HttpServletResponse response, String eTag, long lastModified) throws IOException {
        String ifNoneMatch = request.getHeader("If-None-Match");

        if(ifNoneMatch != null) {
            boolean flag = false;

            if(ifNoneMatch.equals("*")) {
                flag = true;
            }
            else {
                flag = contains(ifNoneMatch, eTag);
            }

            if(flag) {
                String method = request.getMethod();

                if(("GET".equalsIgnoreCase(method)) || ("HEAD".equalsIgnoreCase(method))) {
                    response.setStatus(304);
                    response.setHeader("ETag", eTag);
                    return false;
                }
                else {
                    response.sendError(412);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param request
     * @param response
     * @param eTag
     * @param lastModified
     * @return boolean
     * @throws IOException
     */
    protected static boolean checkIfUnmodifiedSince(HttpServletRequest request, HttpServletResponse response, String eTag, long lastModified) throws IOException {
        try {
            long ifUnmodifiedSince = request.getDateHeader("If-Unmodified-Since");

            if((ifUnmodifiedSince != -1L) && (lastModified >= ifUnmodifiedSince + 1000L)) {
                response.sendError(412);
                return false;
            }
        }
        catch(IllegalArgumentException e) {
            return true;
        }
        return true;
    }

    /**
     * @param content
     * @param value
     * @return boolean
     */
    private static boolean contains(String content, String value) {
        if(content != null) {
            StringTokenizer tokenizer = new StringTokenizer(content, ",");

            while(tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();

                if(token.trim().equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }
}
