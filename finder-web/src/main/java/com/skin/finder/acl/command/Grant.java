/*
 * $RCSfile: Grant.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.acl.command;

import java.io.IOException;
import java.io.StringReader;

import com.skin.finder.util.Stream;

/**
 * <p>Title: Grant</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class Grant {
    private String userName;
    private String pattern;
    private String action;

    /**
     * @param userName
     * @param pattern
     * @param action
     */
    public Grant(String userName, String pattern, String action) {
        this.userName = userName;
        this.pattern = pattern;
        this.action = action;
    }

    /**
     * grant read on 192.168.1.1@finder.log/test/test.log to admin;
     * @param command
     * @return Grant
     */
    public static Grant parse(String command) {
        try {
            Stream stream = Stream.getStream(new StringReader(command), 64);
            String token = getToken(stream);

            if(!token.equalsIgnoreCase("grant")) {
                throw new RuntimeException("bad command, expect \"grant\".");
            }

            String action = getToken(stream);
            stream.skipWhitespace();

            if(!stream.tryread("on", true)) {
                throw new RuntimeException("bad command, expect \"on\".");
            }

            String pattern = getToken(stream);
            stream.skipWhitespace();

            if(!stream.tryread("to", true)) {
                throw new RuntimeException("bad command, expect \"to\".");
            }

            String userName = getToken(stream);
            return new Grant(userName, pattern, action);
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param stream
     * @return String
     * @throws IOException
     */
    public static String getToken(Stream stream) throws IOException {
        stream.skipWhitespace();

        int q = 32;
        int c = stream.peek();
        StringBuilder buffer = new StringBuilder();

        if(c == '\'' || c == '"') {
            q = c;
            stream.read();
        }

        while((c = stream.peek()) != -1) {
            if(q == 32 && (Character.isWhitespace(c) || c == ';')) {
                break;
            }
            else if(c == q) {
                break;
            }
            else {
                buffer.append((char)c);
                stream.read();
            }
        }

        c = stream.peek();

        if(q != 32 && c != q) {
            throw new RuntimeException("expect '" + (char)q + "', but found: '" + (char)c + "', ascii: " + c);
        }

        if(c == q) {
            stream.read();
        }
        return buffer.toString().trim();
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the pattern
     */
    public String getPattern() {
        return this.pattern;
    }

    /**
     * @param pattern the pattern to set
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return this.action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }
}
