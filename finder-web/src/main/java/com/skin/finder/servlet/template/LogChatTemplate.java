/*
 * $RCSfile: LogChatTemplate.java,v $
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


/**
 * <p>Title: LogChatTemplate</p>
 * <p>Description: </p>
 * @author JspKit
 * @version 1.0
 */
public class LogChatTemplate extends com.skin.finder.web.servlet.JspServlet {
    private static final long serialVersionUID = 1L;
    private static final LogChatTemplate instance = new LogChatTemplate();


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

        out.write(_jsp_27547, 0, _jsp_27547.length);
        out.write(_jsp_27548, 0, _jsp_27548.length);
        out.write(_jsp_27549, 0, _jsp_27549.length);
        out.write(_jsp_27550, 0, _jsp_27550.length);
        this.write(out, (App.hash()));
        out.write(_jsp_27552, 0, _jsp_27552.length);
        out.write(_jsp_27553, 0, _jsp_27553.length);
        out.write(_jsp_27554, 0, _jsp_27554.length);
        out.write(_jsp_27555, 0, _jsp_27555.length);
        out.write(_jsp_27556, 0, _jsp_27556.length);
        out.write(_jsp_27557, 0, _jsp_27557.length);
        out.write(_jsp_27558, 0, _jsp_27558.length);
        out.write(_jsp_27559, 0, _jsp_27559.length);
        out.write(_jsp_27560, 0, _jsp_27560.length);
        out.write(_jsp_27561, 0, _jsp_27561.length);
        out.write(_jsp_27562, 0, _jsp_27562.length);
        out.write(_jsp_27563, 0, _jsp_27563.length);
        this.write(out, request.getAttribute("remoteIp"));
        out.write(_jsp_27565, 0, _jsp_27565.length);
        this.write(out, request.getAttribute("contextPath"));
        out.write(_jsp_27567, 0, _jsp_27567.length);
        out.write(_jsp_27568, 0, _jsp_27568.length);
        out.write(_jsp_27569, 0, _jsp_27569.length);
        this.write(out, request.getAttribute("userName"));
        out.write(_jsp_27571, 0, _jsp_27571.length);
        out.write(_jsp_27572, 0, _jsp_27572.length);
        out.write(_jsp_27573, 0, _jsp_27573.length);
        out.write(_jsp_27574, 0, _jsp_27574.length);

        out.flush();
    }

    protected static final byte[] _jsp_27547 = b("<!DOCTYPE html>\r\n<html lang=\"en\">\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\r\n");
    protected static final byte[] _jsp_27548 = b("<meta http-equiv=\"Pragma\" content=\"no-cache\"/>\r\n<meta http-equiv=\"Cache-Control\" content=\"no-cache\"/>\r\n");
    protected static final byte[] _jsp_27549 = b("<meta http-equiv=\"Expires\" content=\"0\"/>\r\n<title>FinderWeb - Powered by FinderWeb</title>\r\n");
    protected static final byte[] _jsp_27550 = b("<link rel=\"stylesheet\" type=\"text/css\" href=\"?action=res&path=/finder/css/form.css&v=");
    protected static final byte[] _jsp_27552 = b("\"/>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/jquery-1.12.4.min.js\"></script>\r\n");
    protected static final byte[] _jsp_27553 = b("<script type=\"text/javascript\">\r\n<!--\r\njQuery(function() {\r\n    jQuery(\"#send-btn\").click(function() {\r\n");
    protected static final byte[] _jsp_27554 = b("        var nickName = jQuery(\"input[name=nickName]\").val();\r\n        var message = jQuery(\"textarea[name=message]\").val();\r\n");
    protected static final byte[] _jsp_27555 = b("        var requestURI = window.location.pathname;\r\n\r\n        if(jQuery.trim(message).length < 1) {\r\n");
    protected static final byte[] _jsp_27556 = b("            return;\r\n        }\r\n\r\n        var params = {};\r\n        params.nickName = nickName;\r\n");
    protected static final byte[] _jsp_27557 = b("        params.message = message;\r\n\r\n        jQuery.ajax({\r\n            \"type\": \"post\",\r\n");
    protected static final byte[] _jsp_27558 = b("            \"url\": requestURI + \"?action=finder.chat.send\",\r\n            \"dataType\": \"json\",\r\n");
    protected static final byte[] _jsp_27559 = b("            \"data\": jQuery.param(params, true),\r\n            \"error\": function() {\r\n");
    protected static final byte[] _jsp_27560 = b("                alert(\"System error. Please try again later !\");\r\n            },\r\n");
    protected static final byte[] _jsp_27561 = b("            \"success\": function(response) {\r\n                if(response != null && response.status == 200) {\r\n");
    protected static final byte[] _jsp_27562 = b("                    alert(\"发送成功！\");\r\n                }\r\n            }\r\n        });\r\n");
    protected static final byte[] _jsp_27563 = b("    });\r\n});\r\n//-->\r\n</script>\r\n</head>\r\n<body remoteIp=\"");
    protected static final byte[] _jsp_27565 = b("\" contextPath=\"");
    protected static final byte[] _jsp_27567 = b("\">\r\n<div id=\"finder-panel\" class=\"form\">\r\n    <div class=\"title\"><h4>Hello</h4></div>\r\n");
    protected static final byte[] _jsp_27568 = b("    <div class=\"form-row\">\r\n        <div class=\"form-label\">昵称：</div>\r\n        <div class=\"form-c300\">\r\n");
    protected static final byte[] _jsp_27569 = b("            <div class=\"form-field\">\r\n                <input name=\"nickName\" type=\"text\" class=\"text\" value=\"");
    protected static final byte[] _jsp_27571 = b("\"/>\r\n            </div>\r\n        </div>\r\n    </div>\r\n    <div class=\"form-row\">\r\n");
    protected static final byte[] _jsp_27572 = b("        <div class=\"form-label\">内容：</div>\r\n        <div class=\"form-field\">\r\n            <textarea name=\"message\" style=\"width: 480px; height: 100px;\"></textarea>\r\n");
    protected static final byte[] _jsp_27573 = b("        </div>\r\n    </div>\r\n    <div class=\"button\">\r\n        <button id=\"send-btn\" class=\"button ensure\">发 送</button>\r\n");
    protected static final byte[] _jsp_27574 = b("    </div>\r\n</div>\r\n</body>\r\n</html>\r\n");

}
