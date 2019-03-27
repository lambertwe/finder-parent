/*
 * $RCSfile: HostListTemplate.java,v $
 * $Revision: 1.1 $
 *
 * JSP generated by JspCompiler-1.0.0
 * http://www.finderweb.net
 */
package com.skin.finder.admin.servlet.template;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import com.skin.finder.config.App;
import com.skin.finder.cluster.Cluster;
import com.skin.finder.cluster.Host;


/**
 * <p>Title: HostListTemplate</p>
 * <p>Description: </p>
 * @author JspKit
 * @version 1.0
 */
public class HostListTemplate extends com.skin.finder.web.servlet.JspServlet {
    private static final long serialVersionUID = 1L;
    private static final HostListTemplate instance = new HostListTemplate();


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


    Cluster cluster = (Cluster)(request.getAttribute("cluster"));
    List<Host> hosts = cluster.getHosts();

        out.write(_jsp_18407, 0, _jsp_18407.length);
        out.write(_jsp_18408, 0, _jsp_18408.length);
        out.write(_jsp_18409, 0, _jsp_18409.length);
        out.write(_jsp_18410, 0, _jsp_18410.length);
        out.write(_jsp_18411, 0, _jsp_18411.length);
        this.write(out, (App.hash()));
        out.write(_jsp_18413, 0, _jsp_18413.length);
        out.write(_jsp_18414, 0, _jsp_18414.length);
        this.write(out, (App.hash()));
        out.write(_jsp_18416, 0, _jsp_18416.length);
        this.write(out, (App.hash()));
        out.write(_jsp_18418, 0, _jsp_18418.length);
        this.write(out, request.getAttribute("contextPath"));
        out.write(_jsp_18420, 0, _jsp_18420.length);
        out.write(_jsp_18421, 0, _jsp_18421.length);
        out.write(_jsp_18422, 0, _jsp_18422.length);
        out.write(_jsp_18423, 0, _jsp_18423.length);
        out.write(_jsp_18424, 0, _jsp_18424.length);
        out.write(_jsp_18425, 0, _jsp_18425.length);
        out.write(_jsp_18426, 0, _jsp_18426.length);
        out.write(_jsp_18427, 0, _jsp_18427.length);

        for(Host host : hosts) {
    
        out.write(_jsp_18429, 0, _jsp_18429.length);
        this.write(out, (host.getName()));
        out.write(_jsp_18431, 0, _jsp_18431.length);
        this.write(out, (host.getName()));
        out.write(_jsp_18433, 0, _jsp_18433.length);
        this.write(out, (host.getName()));
        out.write(_jsp_18435, 0, _jsp_18435.length);
        this.write(out, (host.getOrderNum()));
        out.write(_jsp_18437, 0, _jsp_18437.length);
        this.write(out, (host.getName()));
        out.write(_jsp_18439, 0, _jsp_18439.length);
        this.write(out, (host.getDisplayName()));
        out.write(_jsp_18441, 0, _jsp_18441.length);
        this.write(out, (host.getUrl()));
        out.write(_jsp_18443, 0, _jsp_18443.length);
        this.write(out, (host.getName()));
        out.write(_jsp_18445, 0, _jsp_18445.length);
        this.write(out, (host.getName()));
        out.write(_jsp_18447, 0, _jsp_18447.length);
        this.write(out, (host.getName()));
        out.write(_jsp_18449, 0, _jsp_18449.length);
        this.write(out, (host.getName()));
        out.write(_jsp_18451, 0, _jsp_18451.length);

        }
    
        out.write(_jsp_18453, 0, _jsp_18453.length);
        this.write(out, request.getAttribute("masterName"));
        out.write(_jsp_18455, 0, _jsp_18455.length);
        this.write(out, (cluster.getVersion()));
        out.write(_jsp_18457, 0, _jsp_18457.length);

        out.flush();
    }

    protected static final byte[] _jsp_18407 = b("<!DOCTYPE html>\r\n<html lang=\"en\">\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\r\n");
    protected static final byte[] _jsp_18408 = b("<meta http-equiv=\"Pragma\" content=\"no-cache\"/>\r\n<meta http-equiv=\"Cache-Control\" content=\"no-cache\"/>\r\n");
    protected static final byte[] _jsp_18409 = b("<meta http-equiv=\"Expires\" content=\"0\"/>\r\n<title>FinderWeb - Powered by FinderWeb</title>\r\n");
    protected static final byte[] _jsp_18410 = b("<link rel=\"shortcut icon\" href=\"?action=res&path=/finder/images/favicon.png\"/>\r\n");
    protected static final byte[] _jsp_18411 = b("<link rel=\"stylesheet\" type=\"text/css\" href=\"?action=res&path=/admin/css/form.css&v=");
    protected static final byte[] _jsp_18413 = b("\"/>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/jquery-1.12.4.min.js\"></script>\r\n");
    protected static final byte[] _jsp_18414 = b("<script type=\"text/javascript\" src=\"?action=res&path=/admin/base.js&v=");
    protected static final byte[] _jsp_18416 = b("\"></script>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/admin/host-list.js&v=");
    protected static final byte[] _jsp_18418 = b("\"></script>\r\n</head>\r\n<body contextPath=\"");
    protected static final byte[] _jsp_18420 = b("\">\r\n<div class=\"menu-bar\">\r\n    <a class=\"button back\" href=\"javascript:void(0)\"><span class=\"back\"></span>返回</a>\r\n");
    protected static final byte[] _jsp_18421 = b("    <a class=\"button refresh\" href=\"javascript:void(0)\"><span class=\"refresh\"></span>刷新</a>\r\n");
    protected static final byte[] _jsp_18422 = b("    <span class=\"seperator\"></span>\r\n    <a class=\"button add-host\" href=\"javascript:void(0)\"><span class=\"add\"></span>添加主机</a>\r\n");
    protected static final byte[] _jsp_18423 = b("    <a class=\"button syn-host\" href=\"javascript:void(0)\"><span class=\"syn\"></span>从Master同步</a>\r\n");
    protected static final byte[] _jsp_18424 = b("</div>\r\n<table id=\"host-table\" class=\"list\">\r\n    <tr class=\"head\">\r\n        <td class=\"w080 center\"><a id=\"checkall\" href=\"javascript:void(0)\">全 选</a> / <a id=\"uncheck\" href=\"javascript:void(0)\">取 消</a></td>\r\n");
    protected static final byte[] _jsp_18425 = b("        <td class=\"w060\">Order</td>\r\n        <td class=\"w200\">Name</td>\r\n        <td class=\"w200\">Display Name</td>\r\n");
    protected static final byte[] _jsp_18426 = b("        <td class=\"w240\">URL</td>\r\n        <td class=\"w100\">Version</td>\r\n        <td>操作</td>\r\n");
    protected static final byte[] _jsp_18427 = b("    </tr>\r\n    ");
    protected static final byte[] _jsp_18429 = b("    <tr class=\"disabled\" hostName=\"");
    protected static final byte[] _jsp_18431 = b("\">\r\n        <td style=\"text-align: center;\"><input name=\"hostName\" type=\"checkbox\" disabled=\"true\" value=\"");
    protected static final byte[] _jsp_18433 = b("\"/></td>\r\n        <td class=\"center\"><input name=\"orderNum\" type=\"text\" class=\"text1 w060\" hostName=\"");
    protected static final byte[] _jsp_18435 = b("\" value=\"");
    protected static final byte[] _jsp_18437 = b("\"/></td>\r\n        <td>");
    protected static final byte[] _jsp_18439 = b("</td>\r\n        <td>");
    protected static final byte[] _jsp_18441 = b("</td>\r\n        <td><input type=\"text\" class=\"text2\" readonly=\"true\" value=\"");
    protected static final byte[] _jsp_18443 = b("\"/></td>\r\n        <td class=\"version\">...</td>\r\n        <td class=\"operate\">\r\n            <a class=\"btn host-edit\" href=\"javascript:void(0)\" hostName=\"");
    protected static final byte[] _jsp_18445 = b("\">编辑</a>\r\n            <a class=\"btn work-list\" href=\"javascript:void(0)\" hostName=\"");
    protected static final byte[] _jsp_18447 = b("\">工作空间管理</a>\r\n            <a class=\"btn reload\" href=\"javascript:void(0)\" hostName=\"");
    protected static final byte[] _jsp_18449 = b("\">重新载入</a>\r\n            <a class=\"btn delete\" href=\"javascript:void(0)\" hostName=\"");
    protected static final byte[] _jsp_18451 = b("\">刪 除</a>\r\n        </td>\r\n    </tr>\r\n    ");
    protected static final byte[] _jsp_18453 = b("</table>\r\n<div id=\"pageContext\" style=\"display: none;\" cluster-master-name=\"");
    protected static final byte[] _jsp_18455 = b("\" cluster-master-version=\"");
    protected static final byte[] _jsp_18457 = b("\"></div>\r\n</body>\r\n</html>\r\n");

}