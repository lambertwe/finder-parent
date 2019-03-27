/*
 * $RCSfile: EditPlusTemplate.java,v $
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
 * <p>Title: EditPlusTemplate</p>
 * <p>Description: </p>
 * @author JspKit
 * @version 1.0
 */
public class EditPlusTemplate extends com.skin.finder.web.servlet.JspServlet {
    private static final long serialVersionUID = 1L;
    private static final EditPlusTemplate instance = new EditPlusTemplate();


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

        out.write(_jsp_76074, 0, _jsp_76074.length);
        out.write(_jsp_76075, 0, _jsp_76075.length);
        this.write(out, (App.hash()));
        out.write(_jsp_76077, 0, _jsp_76077.length);
        this.write(out, request.getAttribute("contextPath"));
        out.write(_jsp_76079, 0, _jsp_76079.length);
        out.write(_jsp_76080, 0, _jsp_76080.length);
        this.write(out, (App.hash()));
        out.write(_jsp_76082, 0, _jsp_76082.length);
        out.write(_jsp_76083, 0, _jsp_76083.length);
        out.write(_jsp_76084, 0, _jsp_76084.length);
        out.write(_jsp_76085, 0, _jsp_76085.length);
        out.write(_jsp_76086, 0, _jsp_76086.length);
        out.write(_jsp_76087, 0, _jsp_76087.length);
        out.write(_jsp_76088, 0, _jsp_76088.length);
        out.write(_jsp_76089, 0, _jsp_76089.length);
        out.write(_jsp_76090, 0, _jsp_76090.length);
        out.write(_jsp_76091, 0, _jsp_76091.length);
        out.write(_jsp_76092, 0, _jsp_76092.length);
        out.write(_jsp_76093, 0, _jsp_76093.length);
        out.write(_jsp_76094, 0, _jsp_76094.length);
        out.write(_jsp_76095, 0, _jsp_76095.length);
        out.write(_jsp_76096, 0, _jsp_76096.length);
        out.write(_jsp_76097, 0, _jsp_76097.length);
        out.write(_jsp_76098, 0, _jsp_76098.length);
        out.write(_jsp_76099, 0, _jsp_76099.length);
        out.write(_jsp_76100, 0, _jsp_76100.length);
        out.write(_jsp_76101, 0, _jsp_76101.length);
        out.write(_jsp_76102, 0, _jsp_76102.length);
        out.write(_jsp_76103, 0, _jsp_76103.length);
        out.write(_jsp_76104, 0, _jsp_76104.length);

        out.flush();
    }

    protected static final byte[] _jsp_76074 = b("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<meta charset=\"UTF-8\">\n<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n");
    protected static final byte[] _jsp_76075 = b("<title>ACE Editor - Powered by FinderWeb</title>\n<link rel=\"stylesheet\" type=\"text/css\" href=\"?action=res&path=/finder/css/editplus.css&v=");
    protected static final byte[] _jsp_76077 = b("\"/>\n<script type=\"text/javascript\" src=\"");
    protected static final byte[] _jsp_76079 = b("/lib/ace-min/ace.js\"></script>\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/jquery-1.12.4.min.js\"></script>\n");
    protected static final byte[] _jsp_76080 = b("<script type=\"text/javascript\" src=\"?action=res&path=/finder/editplus.js&v=");
    protected static final byte[] _jsp_76082 = b("\"\"></script>\n</head>\n<body>\n<div id=\"menu-bar\" class=\"menu-bar\" onselectstart=\"return false;\">\n");
    protected static final byte[] _jsp_76083 = b("    <div id=\"menu-save\" class=\"menu-item\">Save</div>\n    <div id=\"menu-reload\" class=\"menu-item\">Reload</div>\n");
    protected static final byte[] _jsp_76084 = b("    <div class=\"menu-line\"></div>\n    <div id=\"menu-undo\" class=\"menu-item\">Undo</div>\n");
    protected static final byte[] _jsp_76085 = b("    <div id=\"menu-redo\" class=\"menu-item\">Redo</div>\n    <div class=\"menu-line\"></div>\n");
    protected static final byte[] _jsp_76086 = b("    <div id=\"menu-jump\" class=\"menu-item\">Jump</div>\n    <div id=\"menu-find\" class=\"menu-item\">Find</div>\n");
    protected static final byte[] _jsp_76087 = b("    <div id=\"menu-replace\" class=\"menu-item\">Replace</div>\n    <div class=\"menu-line\"></div>\n");
    protected static final byte[] _jsp_76088 = b("    <div id=\"menu-view\" class=\"menu-item\">View</div>\n    <div id=\"menu-font\" class=\"menu-item\">Font</div>\n");
    protected static final byte[] _jsp_76089 = b("    <div id=\"menu-theme\" class=\"menu-item\">Theme</div>\n    <div class=\"menu-line\"></div>\n");
    protected static final byte[] _jsp_76090 = b("    <div id=\"menu-setting\" class=\"menu-item disabled\">Help</div>\n</div>\n<div id=\"tab-panel-container\" class=\"tab-component\">\n");
    protected static final byte[] _jsp_76091 = b("    <div class=\"tab-label-wrap\"><ul></ul></div>\n    <div class=\"tab-panel-wrap resize-d\"></div>\n");
    protected static final byte[] _jsp_76092 = b("</div>\n<div class=\"widget-mask menu-mask\">\n    <div id=\"contextmenu-edit\" class=\"contextmenu\">\n");
    protected static final byte[] _jsp_76093 = b("        <ul class=\"menu\">\n            <li class=\"item\" command=\"undo\" unselectable=\"on\">\n");
    protected static final byte[] _jsp_76094 = b("                <span class=\"icon\"></span>\n                <a class=\"command\" title=\"Undo\">Undo</a>\n");
    protected static final byte[] _jsp_76095 = b("            </li>\n            <li class=\"item\" command=\"redo\" unselectable=\"on\">\n");
    protected static final byte[] _jsp_76096 = b("                <span class=\"icon\"></span>\n                <a class=\"command\" title=\"Redo\">Redo</a>\n");
    protected static final byte[] _jsp_76097 = b("            </li>\n            <li class=\"line\"></li>\n\n        </ul>\n    </div>\n</div>\n");
    protected static final byte[] _jsp_76098 = b("<div class=\"widget-mask menu-mask\">\n    <div id=\"contextmenu-view\" class=\"contextmenu\">\n");
    protected static final byte[] _jsp_76099 = b("        <ul class=\"menu\"></ul>\n    </div>\n</div>\n<div class=\"widget-mask menu-mask\">\n");
    protected static final byte[] _jsp_76100 = b("    <div id=\"contextmenu-font\" class=\"contextmenu\">\n        <ul class=\"menu\"></ul>\n");
    protected static final byte[] _jsp_76101 = b("    </div>\n</div>\n<div class=\"widget-mask menu-mask\">\n    <div id=\"contextmenu-theme\" class=\"contextmenu\">\n");
    protected static final byte[] _jsp_76102 = b("        <div class=\"scroll-up\"><img src=\"?action=res&path=/finder/images/up.png\"/></div>\n");
    protected static final byte[] _jsp_76103 = b("        <ul class=\"menu\"></ul>\n        <div class=\"scroll-down\"><img src=\"?action=res&path=/finder/images/down.png\"/></div>\n");
    protected static final byte[] _jsp_76104 = b("    </div>\n</div>\n</body>\n</html>\n");

}