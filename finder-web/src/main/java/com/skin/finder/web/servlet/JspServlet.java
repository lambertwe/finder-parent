/*
 * $RCSfile: JspServlet.java,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 *
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
package com.skin.finder.web.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServlet;

import com.skin.finder.util.Html;

/**
 * <p>Title: JspServlet</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * @author xuesong.net
 * @version 1.0
 */
public class JspServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final byte[] TRUE = "true".getBytes();
    private static final byte[] FALSE = "false".getBytes();
    private static final Charset UTF8 = Charset.forName("utf-8");

    /**
     * @param out
     * @param value
     */
    protected void print(PrintWriter out, Object value) {
        if(value == null) {
            return;
        }

        if(value instanceof Number || value instanceof Boolean) {
            out.write(value.toString());
            return;
        }
        out.write(Html.encode(value.toString()));
    }

    /**
     * @param out
     * @param value
     * @throws IOException
     */
    protected void write(OutputStream out, Object value) throws IOException {
        if(value == null) {
            return;
        }

        if(value instanceof Number) {
            byte[] bytes = value.toString().getBytes();
            out.write(bytes, 0, bytes.length);
            return;
        }

        if(value instanceof Boolean) {
            if(value.equals(Boolean.TRUE)) {
                out.write(TRUE, 0, 4);
            }
            else {
                out.write(FALSE, 0, 5);
            }
            return;
        }

        String content = Html.encode(value.toString());
        byte[] bytes = content.getBytes(UTF8);
        out.write(bytes, 0, bytes.length);
    }

    /**
     * @param out
     * @param value
     * @throws IOException
     */
    protected void write(OutputStream out, Object value, boolean escapeXml) throws IOException {
        if(value == null) {
            return;
        }

        if(escapeXml) {
            this.write(out, value);
        }
        else {
            byte[] bytes = value.toString().getBytes(UTF8);
            out.write(bytes, 0, bytes.length);
        }
    }

    /**
     * @param text
     * @return byte[]
     */
    protected static byte[] b(String text) {
        return text.getBytes(UTF8);
    }
}
