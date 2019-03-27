/*
 * $RCSfile: ProxyDispatcher.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skin.finder.util.IO;

/**
 * <p>Title: ProxyDispatcher</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class ProxyDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(ProxyDispatcher.class);

    /**
     * @param request
     * @param response
     * @param address
     * @throws IOException
     */
    public static void dispatch(HttpServletRequest request, HttpServletResponse response, String address) throws IOException {
        HttpURLConnection connection = null;
        OutputStream outputStream = response.getOutputStream();
        String method = request.getMethod();
        String queryString = request.getQueryString();
        String requestURL = address;

        logger.debug("address: {}", address);
        logger.debug("queryString: {}", queryString);
        logger.debug("requestURL: {}", requestURL);

        try {
            URL url = new URL(requestURL);
            int port = url.getPort();
            connection = (HttpURLConnection)(url.openConnection());
            connection.setConnectTimeout(1 * 60 * 1000);
            connection.setReadTimeout(1 * 60 * 1000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod(method);
            setRequestHeaders(request, connection);

            if(port > 0 && port != 80) {
                response.setHeader("Host-Name", url.getHost() + ":" + url.getPort());
                connection.setRequestProperty("Host", url.getHost() + ":" + port);
            }
            else {
                response.setHeader("Host-Name", url.getHost());
                connection.setRequestProperty("Host", url.getHost());
            }
            connection.setRequestProperty("Connection", "Close");

            if("post".equalsIgnoreCase(method)) {
                pipe(request, connection);
            }

            int status = connection.getResponseCode();
            int contentLength = connection.getContentLength();
            int bufferSize = IO.getBufferSize(connection.getContentLength(), 8192);

            response.setStatus(status);
            setResponseHeaders(connection, response);

            try {
                if(contentLength < 0) {
                    IO.copy(connection.getInputStream(), outputStream, bufferSize);
                }
                else if(contentLength > 0) {
                    IO.copy(connection.getInputStream(), outputStream, bufferSize, contentLength);
                }
            }
            catch(IOException e) {
            }
        }
        finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * @param request
     * @param connection
     */
    private static void setRequestHeaders(HttpServletRequest request, HttpURLConnection connection) {
        Enumeration<?> enums = request.getHeaderNames();
        logger.debug("=================================================");
        logger.debug("request - {}", request.getMethod());

        while(enums.hasMoreElements()) {
            String name = enums.nextElement().toString();

            if(name == null) {
                continue;
            }

            if(name.equalsIgnoreCase("Host") || name.equalsIgnoreCase("Transfer-Encoding")) {
                continue;
            }

            Enumeration<?> values = request.getHeaders(name);

            if(values != null) {
                setRequestHeaders(connection, name, values);
            }
        }
    }

    /**
     * @param connection
     * @param name
     * @param values
     */
    private static void setRequestHeaders(HttpURLConnection connection, String name, java.util.Enumeration<?> values) {
        while(values.hasMoreElements()) {
            String value = values.nextElement().toString();
            connection.addRequestProperty(name, value);

            if(logger.isDebugEnabled()) {
                if(name.equalsIgnoreCase("cookie")) {
                    logger.debug("request - {}: ******", name);
                }
                else {
                    logger.debug("request - {}: {}", name, value);
                }
            }
        }
    }

    /**
     * @param connection
     * @param response
     * @throws IOException
     */
    private static void setResponseHeaders(HttpURLConnection connection, HttpServletResponse response) throws IOException {
        logger.debug("=================================================");
        logger.debug("response - {}", connection.getResponseCode());
        Map<String, List<String>> map = connection.getHeaderFields();

        for(Map.Entry<String, List<String>> entry : map.entrySet()) {
            String name = entry.getKey();

            /**
             * HTTP/1.1 200
             */
            if(name == null) {
                continue;
            }

            if(name.equalsIgnoreCase("Transfer-Encoding")) {
                continue;
            }

            List<String> values = entry.getValue();

            if(values != null && values.size() > 0) {
                for(String value : values) {
                    logger.debug("response - {}: {}", name, value);
                    response.addHeader(name, value);
                }
            }
        }
    }

    /**
     * @param request
     * @param connection
     * @throws IOException
     */
    protected static void pipe(HttpServletRequest request, HttpURLConnection connection) throws IOException {
        int contentLength = request.getContentLength();

        if(contentLength == 0) {
            return;
        }

        logger.debug("copy request.body");
        String contentType = request.getContentType();
        int bufferSize = IO.getBufferSize(request.getContentLength(), 8192);

        if(contentType == null || !contentType.startsWith("application/x-www-form-urlencoded")) {
            IO.copy(request.getInputStream(), connection.getOutputStream(), bufferSize);
            return;
        }

        InputStream inputStream = request.getInputStream();
        OutputStream outputStream = connection.getOutputStream();

        logger.debug("contentLength: {}, bufferSize: {}", contentLength, bufferSize);
        long count = copy(inputStream, outputStream, bufferSize);

        if(count == 0) {
            String body = getPostBody(request);
            logger.debug("request.body: {}", body);

            if(body != null && body.length() > 0) {
                String encoding = request.getCharacterEncoding();

                if(encoding == null) {
                    encoding = "utf-8";
                }

                byte[] pack = body.getBytes(encoding);
                logger.error("request.contentLength: {}, pack.length: {}", contentLength, pack.length);

                if(pack.length != contentLength) {
                    logger.error("contentLength != pack.length, [{}, {}]", contentLength, pack.length);
                }

                outputStream.write(pack, 0, pack.length);
                outputStream.flush();
            }
        }
    }

    /**
     * @param inputStream
     * @param outputStream
     * @param bufferSize
     * @return long
     * @throws IOException
     */
    public static long copy(InputStream inputStream, OutputStream outputStream, int bufferSize) throws IOException {
        int length = 0;
        long count = 0L;
        byte[] buffer = new byte[bufferSize];

        while((length = inputStream.read(buffer, 0, bufferSize)) > -1) {
            outputStream.write(buffer, 0, length);
            count += length;
        }
        outputStream.flush();
        return count;
    }

    /**
     * @param request
     * @return String
     */
    protected static String getPostBody(HttpServletRequest request) {
        java.util.Enumeration<?> enums = request.getParameterNames();

        if(enums == null) {
            return null;
        }

        StringBuilder buffer = new StringBuilder();
        Map<String, String> names = parseNames(request.getQueryString());
        String encoding = request.getCharacterEncoding();

        if(encoding == null) {
            encoding = "utf-8";
        }

        while(enums.hasMoreElements()) {
            String name = enums.nextElement().toString();

            if(names.get(name) != null) {
                continue;
            }

            String[] values = request.getParameterValues(name);

            if(values != null && values.length > 0) {
                try {
                    for(String value : values) {
                        if(value != null) {
                            buffer.append(name);
                            buffer.append("=");
                            buffer.append(URLEncoder.encode(value, encoding));
                            buffer.append("&");
                        }
                    }
                }
                catch (UnsupportedEncodingException e) {
                }
            }
        }

        if(buffer.length() > 0) {
            buffer.setLength(buffer.length() - 1);
        }
        return buffer.toString();
    }

    /**
     * @param queryString
     * @return Map<String, String>
     */
    protected static Map<String, String> parseNames(String queryString) {
        Map<String, String> map = new HashMap<String, String>();

        if(queryString == null || queryString.length() < 1) {
            return map;
        }

        int i = 0;
        int j = queryString.lastIndexOf('#');
        char c = queryString.charAt(0);

        if(c == '?'|| c == '#') {
            i++;
        }

        if(j < i) {
            j = queryString.length();
        }

        int k = 0;
        String name = null;

        while(i < j) {
            k = queryString.indexOf('=', i);

            if(k > -1) {
                name = queryString.substring(i, k);
                map.put(name, name);

                i = queryString.indexOf('&', k + 1);

                if(i < 0) {
                    break;
                }
                else {
                    i++;
                }
            }
            else {
                name = queryString.substring(i, k);
                map.put(name, name);
                break;
            }
        }
        return map;
    }
}
