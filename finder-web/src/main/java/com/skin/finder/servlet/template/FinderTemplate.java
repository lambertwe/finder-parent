/*
 * $RCSfile: FinderTemplate.java,v $
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
import com.skin.finder.i18n.I18N;
import com.skin.finder.i18n.LocalizationContext;

/**
 * <p>Title: FinderTemplate</p>
 * <p>Description: </p>
 * @author JspKit
 * @version 1.0
 */
public class FinderTemplate extends com.skin.finder.web.servlet.JspServlet {
	private static final long			serialVersionUID	= 1L;
	private static final FinderTemplate	instance			= new FinderTemplate();

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

		LocalizationContext i18n = I18N.getBundle(request);

		out.write(_jsp_75457, 0, _jsp_75457.length);
		out.write(_jsp_75458, 0, _jsp_75458.length);
		out.write(_jsp_75459, 0, _jsp_75459.length);
		out.write(_jsp_75460, 0, _jsp_75460.length);
		out.write(_jsp_75461, 0, _jsp_75461.length);
		this.write(out, (App.hash()));
		out.write(_jsp_75463, 0, _jsp_75463.length);
		this.write(out, (App.hash()));
		out.write(_jsp_75465, 0, _jsp_75465.length);
		out.write(_jsp_75466, 0, _jsp_75466.length);
		this.write(out, (App.hash()));
		out.write(_jsp_75468, 0, _jsp_75468.length);
		this.write(out, (App.hash()));
		out.write(_jsp_75470, 0, _jsp_75470.length);
		this.write(out, (App.hash()));
		out.write(_jsp_75472, 0, _jsp_75472.length);
		this.write(out, (App.hash()));
		out.write(_jsp_75474, 0, _jsp_75474.length);
		this.write(out, (i18n.getLang()));
		out.write(_jsp_75476, 0, _jsp_75476.length);
		this.write(out, (App.hash()));
		out.write(_jsp_75478, 0, _jsp_75478.length);
		this.write(out, (App.hash()));
		out.write(_jsp_75480, 0, _jsp_75480.length);
		this.write(out, request.getAttribute("remoteIp"));
		out.write(_jsp_75482, 0, _jsp_75482.length);
		this.write(out, request.getAttribute("host"));
		out.write(_jsp_75484, 0, _jsp_75484.length);
		this.write(out, request.getAttribute("workspace"));
		out.write(_jsp_75486, 0, _jsp_75486.length);
		this.write(out, request.getAttribute("path"));
		out.write(_jsp_75488, 0, _jsp_75488.length);
		out.write(_jsp_75489, 0, _jsp_75489.length);
		out.write(_jsp_75490, 0, _jsp_75490.length);
		this.write(out, (i18n.getString("finder.button.back")));
		out.write(_jsp_75492, 0, _jsp_75492.length);
		this.write(out, (i18n.getString("finder.button.refresh")));
		out.write(_jsp_75494, 0, _jsp_75494.length);
		out.write(_jsp_75495, 0, _jsp_75495.length);
		this.write(out, (i18n.getString("finder.list.view.title")));
		out.write(_jsp_75497, 0, _jsp_75497.length);
		out.write(_jsp_75498, 0, _jsp_75498.length);
		this.write(out, (i18n.getString("finder.list.view.thumbnail")));
		out.write(_jsp_75500, 0, _jsp_75500.length);
		this.write(out, (i18n.getString("finder.list.view.detail")));
		out.write(_jsp_75502, 0, _jsp_75502.length);
		out.write(_jsp_75503, 0, _jsp_75503.length);
		this.write(out, (i18n.getString("finder.list.title.file-name")));
		out.write(_jsp_75505, 0, _jsp_75505.length);
		this.write(out, (i18n.getString("finder.list.title.file-size")));
		out.write(_jsp_75507, 0, _jsp_75507.length);
		this.write(out, (i18n.getString("finder.list.title.file-type")));
		out.write(_jsp_75509, 0, _jsp_75509.length);
		this.write(out, (i18n.getString("finder.list.title.last-modified")));
		out.write(_jsp_75511, 0, _jsp_75511.length);
		out.write(_jsp_75512, 0, _jsp_75512.length);
		out.write(_jsp_75513, 0, _jsp_75513.length);
		this.write(out, (App.getVersion()));
		out.write(_jsp_75515, 0, _jsp_75515.length);
		out.write(_jsp_75516, 0, _jsp_75516.length);
		out.write(_jsp_75517, 0, _jsp_75517.length);
		this.write(out, (i18n.getString("finder.context-menu.open")));
		out.write(_jsp_75519, 0, _jsp_75519.length);
		out.write(_jsp_75520, 0, _jsp_75520.length);
		this.write(out, (i18n.getString("finder.context-menu.edit")));
		out.write(_jsp_75522, 0, _jsp_75522.length);
		out.write(_jsp_75523, 0, _jsp_75523.length);
		out.write(_jsp_75524, 0, _jsp_75524.length);
		this.write(out, (i18n.getString("finder.context-menu.upload")));
		out.write(_jsp_75526, 0, _jsp_75526.length);
		out.write(_jsp_75527, 0, _jsp_75527.length);
		out.write(_jsp_75528, 0, _jsp_75528.length);
		this.write(out, (i18n.getString("finder.context-menu.download")));
		out.write(_jsp_75530, 0, _jsp_75530.length);
		out.write(_jsp_75531, 0, _jsp_75531.length);
		out.write(_jsp_75532, 0, _jsp_75532.length);
		this.write(out, (i18n.getString("finder.context-menu.zip")));
		out.write(_jsp_75534, 0, _jsp_75534.length);
		out.write(_jsp_75535, 0, _jsp_75535.length);
		out.write(_jsp_75536, 0, _jsp_75536.length);
		this.write(out, (i18n.getString("finder.context-menu.unzip")));
		out.write(_jsp_75538, 0, _jsp_75538.length);
		out.write(_jsp_75539, 0, _jsp_75539.length);
		out.write(_jsp_75540, 0, _jsp_75540.length);
		this.write(out, (i18n.getString("finder.context-menu.cut")));
		out.write(_jsp_75542, 0, _jsp_75542.length);
		out.write(_jsp_75543, 0, _jsp_75543.length);
		out.write(_jsp_75544, 0, _jsp_75544.length);
		this.write(out, (i18n.getString("finder.context-menu.copy")));
		out.write(_jsp_75546, 0, _jsp_75546.length);
		out.write(_jsp_75547, 0, _jsp_75547.length);
		out.write(_jsp_75548, 0, _jsp_75548.length);
		this.write(out, (i18n.getString("finder.context-menu.paste")));
		out.write(_jsp_75550, 0, _jsp_75550.length);
		out.write(_jsp_75551, 0, _jsp_75551.length);
		out.write(_jsp_75552, 0, _jsp_75552.length);
		this.write(out, (i18n.getString("finder.context-menu.delete")));
		out.write(_jsp_75554, 0, _jsp_75554.length);
		out.write(_jsp_75555, 0, _jsp_75555.length);
		this.write(out, (i18n.getString("finder.context-menu.rename")));
		out.write(_jsp_75557, 0, _jsp_75557.length);
		out.write(_jsp_75558, 0, _jsp_75558.length);
		out.write(_jsp_75559, 0, _jsp_75559.length);
		this.write(out, (i18n.getString("finder.context-menu.mkdir")));
		out.write(_jsp_75561, 0, _jsp_75561.length);
		out.write(_jsp_75562, 0, _jsp_75562.length);
		out.write(_jsp_75563, 0, _jsp_75563.length);
		this.write(out, (i18n.getString("finder.context-menu.refresh")));
		out.write(_jsp_75565, 0, _jsp_75565.length);
		out.write(_jsp_75566, 0, _jsp_75566.length);
		out.write(_jsp_75567, 0, _jsp_75567.length);
		this.write(out, (i18n.getString("finder.context-menu.info")));
		out.write(_jsp_75569, 0, _jsp_75569.length);
		out.write(_jsp_75570, 0, _jsp_75570.length);
		this.write(out, request.getAttribute("version"));
		out.write(_jsp_75572, 0, _jsp_75572.length);
		out.write(_jsp_75573, 0, _jsp_75573.length);
		this.write(out, ConfigFactory.getAccessCode(), false);
		out.write(_jsp_75575, 0, _jsp_75575.length);
		this.write(out, (ConfigFactory.getEnvName()));
		out.write(_jsp_75577, 0, _jsp_75577.length);
		this.write(out, request.getAttribute("contextPath"));
		out.write(_jsp_75579, 0, _jsp_75579.length);
		this.write(out, request.getAttribute("admin"));
		out.write(_jsp_75581, 0, _jsp_75581.length);
		this.write(out, (App.hash()));
		out.write(_jsp_75583, 0, _jsp_75583.length);
		this.write(out, (i18n.getLang()));
		out.write(_jsp_75585, 0, _jsp_75585.length);
		this.write(out, (ConfigFactory.getTextType()));
		out.write(_jsp_75587, 0, _jsp_75587.length);
		this.write(out, (ConfigFactory.getUploadPartSize()));
		out.write(_jsp_75589, 0, _jsp_75589.length);
		this.write(out, (ConfigFactory.getOperateButton()));
		out.write(_jsp_75591, 0, _jsp_75591.length);
		this.write(out, (App.getVersion()));
		out.write(_jsp_75593, 0, _jsp_75593.length);

		out.flush();
	}

	protected static final byte[]	_jsp_75457	= b(
			"<!DOCTYPE html>\r\n<html lang=\"en\">\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\r\n");
	protected static final byte[]	_jsp_75458	= b("<title>FinderWeb - Powered by FinderWeb</title>\r\n<meta http-equiv=\"Pragma\" content=\"no-cache\"/>\r\n");
	protected static final byte[]	_jsp_75459	= b("<meta http-equiv=\"Cache-Control\" content=\"no-cache\"/>\r\n<meta http-equiv=\"Expires\" content=\"0\"/>\r\n");
	protected static final byte[]	_jsp_75460	= b("<link rel=\"shortcut icon\" href=\"?action=res&path=/finder/images/favicon.png\"/>\r\n");
	protected static final byte[]	_jsp_75461	= b("<link rel=\"stylesheet\" type=\"text/css\" href=\"?action=res&path=/finder/css/finder.css&v=");
	protected static final byte[]	_jsp_75463	= b("\"/>\r\n<link rel=\"stylesheet\" type=\"text/css\" href=\"?action=res&path=/finder/css/widget.css&v=");
	protected static final byte[]	_jsp_75465	= b("\"/>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/jquery-1.12.4.min.js\"></script>\r\n");
	protected static final byte[]	_jsp_75466	= b("<script type=\"text/javascript\" src=\"?action=res&path=/finder/widget.js&v=");
	protected static final byte[]	_jsp_75468	= b("\"></script>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/finder.js&v=");
	protected static final byte[]	_jsp_75470	= b("\"></script>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/fileupload.js&v=");
	protected static final byte[]	_jsp_75472	= b("\"></script>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/component.js&v=");
	protected static final byte[]	_jsp_75474	= b("\"></script>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/lang/");
	protected static final byte[]	_jsp_75476	= b(".js&v=");
	protected static final byte[]	_jsp_75478	= b("\"></script>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/plugins.js&v=");
	protected static final byte[]	_jsp_75480	= b(
			"\"></script>\r\n</head>\r\n<body style=\"background: url(?action=res&path=/finder/images/7a4f.jpg) center center / cover no-repeat fixed; overflow: hidden;\" remoteIp=\"");
	protected static final byte[]	_jsp_75482	= b("\" data-host=\"");
	protected static final byte[]	_jsp_75484	= b("\" data-workspace=\"");
	protected static final byte[]	_jsp_75486	= b("\" data-path=\"");
	protected static final byte[]	_jsp_75488	= b(
			"\">\r\n<div class=\"view-panel\" style=\"top: 0px;\">\r\n<!-- main view -->\r\n<div id=\"loading\" class=\"loading-mask\" style=\"display: block;\"><div class=\"loading\"><img src=\"?action=res&path=/finder/images/loading.gif\"/></div></div>\r\n");
	protected static final byte[]	_jsp_75489	= b("<div id=\"addr-bar\" class=\"addr-bar\" style=\"min-width: 740px;\" draggable=\"false\">\r\n");
	protected static final byte[]	_jsp_75490	= b("    <div class=\"b1\">\r\n        <span id=\"back\" class=\"button\" title=\"");
	protected static final byte[]	_jsp_75492	= b("\"><i class=\"back\"></i></span>\r\n        <span id=\"refresh\" class=\"button\" title=\"");
	protected static final byte[]	_jsp_75494	= b(
			"\"><i class=\"refresh\"></i></span>\r\n    </div>\r\n    <div class=\"b2\">\r\n        <div style=\"float: left;\"><input id=\"address\" type=\"text\" class=\"address\" autocomplete=\"off\" spellcheck=\"false\" draggable=\"false\" value=\"/\"/></div>\r\n");
	protected static final byte[]	_jsp_75495	= b(
			"        <div id=\"finder-suggest\" class=\"suggest\" draggable=\"false\"></div>\r\n        <span id=\"view-button\" class=\"views\" title=\"");
	protected static final byte[]	_jsp_75497	= b("\"><i></i></span>\r\n        <div id=\"view-options\" class=\"list view-menu\">\r\n            <ul>\r\n");
	protected static final byte[]	_jsp_75498	= b("                <li index=\"0\" option-value=\"outline\"><span>");
	protected static final byte[]	_jsp_75500	= b("</span></li>\r\n                <li index=\"1\" option-value=\"detail\" class=\"selected\"><span>");
	protected static final byte[]	_jsp_75502	= b(
			"</span></li>\r\n            </ul>\r\n        </div>\r\n    </div>\r\n</div>\r\n<div id=\"head-view\" class=\"head-view\" draggable=\"false\">\r\n");
	protected static final byte[]	_jsp_75503	= b(
			"    <span class=\"file-name orderable\" orderBy=\"file-name\" unselectable=\"on\" onselectstart=\"return false;\"><em class=\"title\">");
	protected static final byte[]	_jsp_75505	= b(
			"</em><em class=\"order asc\"></em></span>\r\n    <span class=\"file-size orderable\" orderBy=\"file-size\" unselectable=\"on\" onselectstart=\"return false;\"><em class=\"title\">");
	protected static final byte[]	_jsp_75507	= b(
			"</em><em class=\"order\"></em></span>\r\n    <span class=\"file-type orderable\" orderBy=\"file-type\" unselectable=\"on\" onselectstart=\"return false;\"><em class=\"title\">");
	protected static final byte[]	_jsp_75509	= b(
			"</em><em class=\"order\"></em></span>\r\n    <span class=\"modified orderable\" orderBy=\"modified\" unselectable=\"on\" onselectstart=\"return false;\"><em class=\"title\">");
	protected static final byte[]	_jsp_75511	= b("</em><em class=\"order\"></em></span>\r\n    <span class=\"operate\"><em class=\"title\">&nbsp;</em></span>\r\n");
	protected static final byte[]	_jsp_75512	= b(
			"</div>\r\n<div id=\"file-view\" class=\"file-view\" contextmenu=\"true\">\r\n    <div id=\"info-view\" class=\"info-view\" contextmenu=\"false\"></div>\r\n");
	protected static final byte[]	_jsp_75513	= b("    <ul id=\"file-list\" class=\"detail-view\"></ul>\r\n    <div id=\"watermark\" class=\"watermark\">Powered by FinderWeb v");
	protected static final byte[]	_jsp_75515	= b(
			"</div>\r\n</div>\r\n<!-- main view -->\r\n</div>\r\n<div id=\"finder-contextmenu\" class=\"contextmenu\" style=\"top: 50px; left: 100px; display: none;\" draggable=\"false\">\r\n");
	protected static final byte[]	_jsp_75516	= b("    <ul class=\"menu\">\r\n        <li class=\"item\" command=\"open\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_75517	= b("            <span class=\"icon\"></span>\r\n            <a class=\"command\">");
	protected static final byte[]	_jsp_75519	= b("(O)</a>\r\n        </li>\r\n        <li class=\"item\" command=\"edit\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_75520	= b("            <span class=\"icon\"></span>\r\n            <a class=\"command\">");
	protected static final byte[]	_jsp_75522	= b("(O)</a>\r\n        </li>\r\n        <li class=\"item\" command=\"upload\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_75523	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/upload.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_75524	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_75526	= b("(F)</a>\r\n        </li>\r\n        <li class=\"item disabled\" command=\"download\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_75527	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/download.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_75528	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_75530	= b(
			"(G)</a>\r\n        </li>\r\n        <li class=\"separator\"></li>\r\n        <li class=\"item disabled\" command=\"zip\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_75531	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/zip.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_75532	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_75534	= b("(Z)</a>\r\n        </li>\r\n        <li class=\"item disabled\" command=\"unzip\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_75535	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/zip.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_75536	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_75538	= b(
			"(U)</a>\r\n        </li>\r\n        <li class=\"separator\"></li>\r\n        <li class=\"item disabled\" command=\"cut\">\r\n");
	protected static final byte[]	_jsp_75539	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/cut.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_75540	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_75542	= b("(X)</a>\r\n        </li>\r\n        <li class=\"item disabled\" command=\"copy\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_75543	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/copy.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_75544	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_75546	= b("(C)</a>\r\n        </li>\r\n        <li class=\"item disabled\" command=\"paste\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_75547	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/paste.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_75548	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_75550	= b(
			"(V)</a>\r\n        </li>\r\n        <li class=\"separator\"></li>\r\n        <li class=\"item disabled\" command=\"remove\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_75551	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/delete.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_75552	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_75554	= b(
			"(D)</a>\r\n        </li>\r\n        <li class=\"separator\"></li>\r\n        <li class=\"item\" command=\"rename\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_75555	= b("            <span class=\"icon\"></span>\r\n            <a class=\"command\">");
	protected static final byte[]	_jsp_75557	= b("(F2)</a>\r\n        </li>\r\n        <li class=\"item\" command=\"mkdir\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_75558	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/folder.png\"/></span>\r\n");
	protected static final byte[]	_jsp_75559	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_75561	= b("(N)</a>\r\n        </li>\r\n        <li class=\"item\" command=\"refresh\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_75562	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/refresh.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_75563	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_75565	= b(
			"(E)</a>\r\n        </li>\r\n        <li class=\"separator\"></li>\r\n        <li class=\"item\" command=\"info\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_75566	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/info.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_75567	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_75569	= b(
			"(R)</a>\r\n        </li>\r\n    </ul>\r\n</div>\r\n<div id=\"status-bar\" class=\"status-bar\" style=\"display: block;\" draggable=\"false\">\r\n");
	protected static final byte[]	_jsp_75570	= b("    <div class=\"copyright\">Powered by FinderWeb v");
	protected static final byte[]	_jsp_75572	= b(" | Copyright © <a href=\"#\" draggable=\"false\" target=\"_blank\">www.inaction.top</a> All rights reserved.</div>\r\n");
	protected static final byte[]	_jsp_75573	= b("</div>\r\n<div id=\"access-code\" style=\"display: none;\">");
	protected static final byte[]	_jsp_75575	= b("</div>\r\n<div id=\"pageContext\" style=\"display: none;\"\r\n    data-env-name=\"");
	protected static final byte[]	_jsp_75577	= b("\"\r\n    data-context-path=\"");
	protected static final byte[]	_jsp_75579	= b("\"\r\n    data-admin=\"");
	protected static final byte[]	_jsp_75581	= b("\"\r\n    data-hash=\"");
	protected static final byte[]	_jsp_75583	= b("\"\r\n    data-lang=\"");
	protected static final byte[]	_jsp_75585	= b("\"\r\n    data-text-type=\"");
	protected static final byte[]	_jsp_75587	= b("\"\r\n    data-upload-part-size=\"");
	protected static final byte[]	_jsp_75589	= b("\"\r\n    data-display-operate-button=\"");
	protected static final byte[]	_jsp_75591	= b("\"\r\n    data-version=\"");
	protected static final byte[]	_jsp_75593	= b("\"></div>\r\n<!-- http://www.finderweb.net -->\r\n</body>\r\n</html>\r\n");

}
