/*
 * $RCSfile: PasswordTemplate.java,v $
 * $Revision: 1.1 $
 *
 * JSP generated by JspCompiler-1.0.0
 * http://www.finderweb.net
 */
package com.skin.finder.servlet.template;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skin.finder.config.App;
import com.skin.finder.config.ConfigFactory;


/**
 * <p>Title: PasswordTemplate</p>
 * <p>Description: </p>
 * @author JspKit
 * @version 1.0
 */
public class PasswordTemplate extends com.skin.finder.web.servlet.JspServlet {
    private static final long serialVersionUID = 1L;
    private static final PasswordTemplate instance = new PasswordTemplate();


    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public static void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        instance.service(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html; charset=utf-8");
        OutputStream out = response.getOutputStream();

        out.write(_jsp_95825, 0, _jsp_95825.length);
        out.write(_jsp_95826, 0, _jsp_95826.length);
        out.write(_jsp_95827, 0, _jsp_95827.length);
        out.write(_jsp_95828, 0, _jsp_95828.length);
        out.write(_jsp_95829, 0, _jsp_95829.length);
        this.write(out, (App.hash()));
        out.write(_jsp_95831, 0, _jsp_95831.length);
        out.write(_jsp_95832, 0, _jsp_95832.length);
        out.write(_jsp_95833, 0, _jsp_95833.length);
        this.write(out, (App.hash()));
        out.write(_jsp_95835, 0, _jsp_95835.length);
        out.write(_jsp_95836, 0, _jsp_95836.length);
        out.write(_jsp_95837, 0, _jsp_95837.length);
        out.write(_jsp_95838, 0, _jsp_95838.length);
        out.write(_jsp_95839, 0, _jsp_95839.length);
        out.write(_jsp_95840, 0, _jsp_95840.length);
        out.write(_jsp_95841, 0, _jsp_95841.length);
        out.write(_jsp_95842, 0, _jsp_95842.length);
        out.write(_jsp_95843, 0, _jsp_95843.length);
        this.write(out, request.getAttribute("allow"));
        out.write(_jsp_95845, 0, _jsp_95845.length);
        out.write(_jsp_95846, 0, _jsp_95846.length);
        this.write(out, (System.currentTimeMillis()));
        out.write(_jsp_95848, 0, _jsp_95848.length);
        this.write(out, (ConfigFactory.getPublicKey()));
        out.write(_jsp_95850, 0, _jsp_95850.length);

        out.flush();
    }

    protected static final byte[] _jsp_95825 = b("<!DOCTYPE html>\r\n<html lang=\"en\">\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\r\n");
    protected static final byte[] _jsp_95826 = b("<meta http-equiv=\"Pragma\" content=\"no-cache\"/>\r\n<meta http-equiv=\"Cache-Control\" content=\"no-cache\"/>\r\n");
    protected static final byte[] _jsp_95827 = b("<meta http-equiv=\"Expires\" content=\"0\"/>\r\n<title>FinderWeb - Powered by FinderWeb</title>\r\n");
    protected static final byte[] _jsp_95828 = b("<link rel=\"shortcut icon\" href=\"?action=res&path=/finder/images/favicon.png\"/>\r\n");
    protected static final byte[] _jsp_95829 = b("<link rel=\"stylesheet\" type=\"text/css\" href=\"?action=res&path=/finder/css/form.css&v=");
    protected static final byte[] _jsp_95831 = b("\"/>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/jquery-1.12.4.min.js\"></script>\r\n");
    protected static final byte[] _jsp_95832 = b("<script type=\"text/javascript\" src=\"?action=res&path=/finder/jsencrypt.min.js\"></script>\r\n");
    protected static final byte[] _jsp_95833 = b("<script type=\"text/javascript\" src=\"?action=res&path=/finder/password.js&v=");
    protected static final byte[] _jsp_95835 = b("\"></script>\r\n</head>\r\n<body>\r\n<div class=\"form\">\r\n    <div class=\"title\"><h4>修改密码</h4></div>\r\n");
    protected static final byte[] _jsp_95836 = b("    <div class=\"form-row\">\r\n        <div class=\"form-label\">Old Password：</div>\r\n");
    protected static final byte[] _jsp_95837 = b("        <div class=\"form-c300\">\r\n            <div class=\"form-field\">\r\n                <input id=\"s1\" type=\"password\" class=\"text w200\" placeholder=\"Old Password\" value=\"\"/>\r\n");
    protected static final byte[] _jsp_95838 = b("            </div>\r\n        </div>\r\n        <div class=\"form-m300\">\r\n            <div class=\"form-comment\">Old Password .</div>\r\n");
    protected static final byte[] _jsp_95839 = b("        </div>\r\n    </div>\r\n    <div class=\"form-row\">\r\n        <div class=\"form-label\">New Password:</div>\r\n");
    protected static final byte[] _jsp_95840 = b("        <div class=\"form-c300\">\r\n            <div class=\"form-field\">\r\n                <input id=\"s2\" type=\"password\" class=\"text w200\" placeholder=\"New Password\" value=\"\"/>\r\n");
    protected static final byte[] _jsp_95841 = b("            </div>\r\n        </div>\r\n        <div class=\"form-m300\">\r\n            <div class=\"form-comment\">New Password .</div>\r\n");
    protected static final byte[] _jsp_95842 = b("        </div>\r\n    </div>\r\n\r\n    <div class=\"button\">\r\n        <button id=\"submit\" class=\"button ensure\">保 存</button>\r\n");
    protected static final byte[] _jsp_95843 = b("    </div>\r\n</div>\r\n<div id=\"info-view\" class=\"info-view\" style=\"display: none;\" data-allow=\"");
    protected static final byte[] _jsp_95845 = b("\">\r\n    <div class=\"info-body\"><p>演示环境不允许修改密码！</p></div>\r\n</div>\r\n<div style=\"display: none;\">\r\n");
    protected static final byte[] _jsp_95846 = b("    <input id=\"timestamp\" type=\"text\" value=\"");
    protected static final byte[] _jsp_95848 = b("\"/>\r\n    <textarea id=\"publicKey\">");
    protected static final byte[] _jsp_95850 = b("</textarea>\r\n</div>\r\n</body>\r\n</html>");

}