/*
 * $RCSfile: IndexTemplate.java,v $
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
 * <p>Title: IndexTemplate</p>
 * <p>Description: </p>
 * @author JspKit
 * @version 1.0
 */
public class IndexTemplate extends com.skin.finder.web.servlet.JspServlet {
	private static final long			serialVersionUID	= 1L;
	private static final IndexTemplate	instance			= new IndexTemplate();

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

		out.write(_jsp_92640, 0, _jsp_92640.length);
		out.write(_jsp_92641, 0, _jsp_92641.length);
		out.write(_jsp_92642, 0, _jsp_92642.length);
		out.write(_jsp_92643, 0, _jsp_92643.length);
		out.write(_jsp_92644, 0, _jsp_92644.length);
		this.write(out, (App.hash()));
		out.write(_jsp_92646, 0, _jsp_92646.length);
		this.write(out, (App.hash()));
		out.write(_jsp_92648, 0, _jsp_92648.length);
		this.write(out, (App.hash()));
		out.write(_jsp_92650, 0, _jsp_92650.length);
		out.write(_jsp_92651, 0, _jsp_92651.length);
		this.write(out, (App.hash()));
		out.write(_jsp_92653, 0, _jsp_92653.length);
		this.write(out, (App.hash()));
		out.write(_jsp_92655, 0, _jsp_92655.length);
		this.write(out, (App.hash()));
		out.write(_jsp_92657, 0, _jsp_92657.length);
		this.write(out, (App.hash()));
		out.write(_jsp_92659, 0, _jsp_92659.length);
		this.write(out, (App.hash()));
		out.write(_jsp_92661, 0, _jsp_92661.length);
		this.write(out, (App.hash()));
		out.write(_jsp_92663, 0, _jsp_92663.length);
		this.write(out, (App.hash()));
		out.write(_jsp_92665, 0, _jsp_92665.length);
		this.write(out, (i18n.getLang()));
		out.write(_jsp_92667, 0, _jsp_92667.length);
		this.write(out, (App.hash()));
		out.write(_jsp_92669, 0, _jsp_92669.length);
		this.write(out, (App.hash()));
		out.write(_jsp_92671, 0, _jsp_92671.length);
		this.write(out, (App.hash()));
		out.write(_jsp_92673, 0, _jsp_92673.length);
		this.write(out, request.getAttribute("remoteIp"));
		out.write(_jsp_92675, 0, _jsp_92675.length);
		this.write(out, request.getAttribute("userName"));
		out.write(_jsp_92677, 0, _jsp_92677.length);
		out.write(_jsp_92678, 0, _jsp_92678.length);
		this.write(out, request.getAttribute("requestURI"));
		out.write(_jsp_92680, 0, _jsp_92680.length);
		out.write(_jsp_92681, 0, _jsp_92681.length);
		out.write(_jsp_92682, 0, _jsp_92682.length);
		this.write(out, (i18n.format("finder.index.home")));
		out.write(_jsp_92684, 0, _jsp_92684.length);
		this.write(out, (i18n.format("finder.index.admin")));
		out.write(_jsp_92686, 0, _jsp_92686.length);
		this.write(out, (i18n.format("finder.index.setting")));
		out.write(_jsp_92688, 0, _jsp_92688.length);
		this.write(out, (i18n.format("finder.index.help")));
		out.write(_jsp_92690, 0, _jsp_92690.length);
		this.write(out, (i18n.format("finder.index.logout")));
		out.write(_jsp_92692, 0, _jsp_92692.length);
		out.write(_jsp_92693, 0, _jsp_92693.length);
		out.write(_jsp_92694, 0, _jsp_92694.length);
		out.write(_jsp_92695, 0, _jsp_92695.length);
		out.write(_jsp_92696, 0, _jsp_92696.length);
		out.write(_jsp_92697, 0, _jsp_92697.length);
		out.write(_jsp_92698, 0, _jsp_92698.length);
		out.write(_jsp_92699, 0, _jsp_92699.length);
		out.write(_jsp_92700, 0, _jsp_92700.length);
		this.write(out, (i18n.getString("finder.button.back")));
		out.write(_jsp_92702, 0, _jsp_92702.length);
		this.write(out, (i18n.getString("finder.button.refresh")));
		out.write(_jsp_92704, 0, _jsp_92704.length);
		out.write(_jsp_92705, 0, _jsp_92705.length);
		this.write(out, (i18n.getString("finder.list.view.title")));
		out.write(_jsp_92707, 0, _jsp_92707.length);
		out.write(_jsp_92708, 0, _jsp_92708.length);
		this.write(out, (i18n.getString("finder.list.view.thumbnail")));
		out.write(_jsp_92710, 0, _jsp_92710.length);
		this.write(out, (i18n.getString("finder.list.view.detail")));
		out.write(_jsp_92712, 0, _jsp_92712.length);
		out.write(_jsp_92713, 0, _jsp_92713.length);
		this.write(out, (i18n.getString("finder.list.title.file-name")));
		out.write(_jsp_92715, 0, _jsp_92715.length);
		this.write(out, (i18n.getString("finder.list.title.file-size")));
		out.write(_jsp_92717, 0, _jsp_92717.length);
		this.write(out, (i18n.getString("finder.list.title.file-type")));
		out.write(_jsp_92719, 0, _jsp_92719.length);
		this.write(out, (i18n.getString("finder.list.title.last-modified")));
		out.write(_jsp_92721, 0, _jsp_92721.length);
		out.write(_jsp_92722, 0, _jsp_92722.length);
		out.write(_jsp_92723, 0, _jsp_92723.length);
		this.write(out, (App.getVersion()));
		out.write(_jsp_92725, 0, _jsp_92725.length);
		out.write(_jsp_92726, 0, _jsp_92726.length);
		out.write(_jsp_92727, 0, _jsp_92727.length);
		out.write(_jsp_92728, 0, _jsp_92728.length);
		this.write(out, (i18n.getString("finder.context-menu.open")));
		out.write(_jsp_92730, 0, _jsp_92730.length);
		out.write(_jsp_92731, 0, _jsp_92731.length);
		this.write(out, (i18n.getString("finder.context-menu.edit")));
		out.write(_jsp_92733, 0, _jsp_92733.length);
		out.write(_jsp_92734, 0, _jsp_92734.length);
		out.write(_jsp_92735, 0, _jsp_92735.length);
		this.write(out, (i18n.getString("finder.context-menu.upload")));
		out.write(_jsp_92737, 0, _jsp_92737.length);
		out.write(_jsp_92738, 0, _jsp_92738.length);
		out.write(_jsp_92739, 0, _jsp_92739.length);
		this.write(out, (i18n.getString("finder.context-menu.download")));
		out.write(_jsp_92741, 0, _jsp_92741.length);
		out.write(_jsp_92742, 0, _jsp_92742.length);
		out.write(_jsp_92743, 0, _jsp_92743.length);
		this.write(out, (i18n.getString("finder.context-menu.zip")));
		out.write(_jsp_92745, 0, _jsp_92745.length);
		out.write(_jsp_92746, 0, _jsp_92746.length);
		out.write(_jsp_92747, 0, _jsp_92747.length);
		this.write(out, (i18n.getString("finder.context-menu.unzip")));
		out.write(_jsp_92749, 0, _jsp_92749.length);
		out.write(_jsp_92750, 0, _jsp_92750.length);
		out.write(_jsp_92751, 0, _jsp_92751.length);
		this.write(out, (i18n.getString("finder.context-menu.cut")));
		out.write(_jsp_92753, 0, _jsp_92753.length);
		out.write(_jsp_92754, 0, _jsp_92754.length);
		out.write(_jsp_92755, 0, _jsp_92755.length);
		this.write(out, (i18n.getString("finder.context-menu.copy")));
		out.write(_jsp_92757, 0, _jsp_92757.length);
		out.write(_jsp_92758, 0, _jsp_92758.length);
		out.write(_jsp_92759, 0, _jsp_92759.length);
		this.write(out, (i18n.getString("finder.context-menu.paste")));
		out.write(_jsp_92761, 0, _jsp_92761.length);
		out.write(_jsp_92762, 0, _jsp_92762.length);
		out.write(_jsp_92763, 0, _jsp_92763.length);
		this.write(out, (i18n.getString("finder.context-menu.delete")));
		out.write(_jsp_92765, 0, _jsp_92765.length);
		out.write(_jsp_92766, 0, _jsp_92766.length);
		this.write(out, (i18n.getString("finder.context-menu.rename")));
		out.write(_jsp_92768, 0, _jsp_92768.length);
		out.write(_jsp_92769, 0, _jsp_92769.length);
		out.write(_jsp_92770, 0, _jsp_92770.length);
		this.write(out, (i18n.getString("finder.context-menu.mkdir")));
		out.write(_jsp_92772, 0, _jsp_92772.length);
		out.write(_jsp_92773, 0, _jsp_92773.length);
		out.write(_jsp_92774, 0, _jsp_92774.length);
		this.write(out, (i18n.getString("finder.context-menu.refresh")));
		out.write(_jsp_92776, 0, _jsp_92776.length);
		out.write(_jsp_92777, 0, _jsp_92777.length);
		out.write(_jsp_92778, 0, _jsp_92778.length);
		this.write(out, (i18n.getString("finder.context-menu.info")));
		out.write(_jsp_92780, 0, _jsp_92780.length);
		out.write(_jsp_92781, 0, _jsp_92781.length);
		this.write(out, request.getAttribute("version"));
		out.write(_jsp_92783, 0, _jsp_92783.length);
		out.write(_jsp_92784, 0, _jsp_92784.length);
		out.write(_jsp_92785, 0, _jsp_92785.length);
		this.write(out, request.getAttribute("version"));
		out.write(_jsp_92787, 0, _jsp_92787.length);
		out.write(_jsp_92788, 0, _jsp_92788.length);
		this.write(out, request.getAttribute("newVersion"));
		out.write(_jsp_92790, 0, _jsp_92790.length);
		this.write(out, request.getAttribute("downloadUrl"));
		out.write(_jsp_92792, 0, _jsp_92792.length);
		this.write(out, request.getAttribute("newVersion"));
		out.write(_jsp_92794, 0, _jsp_92794.length);
		this.write(out, ConfigFactory.getAccessCode(), false);
		out.write(_jsp_92796, 0, _jsp_92796.length);
		this.write(out, (ConfigFactory.getEnvName()));
		out.write(_jsp_92798, 0, _jsp_92798.length);
		this.write(out, request.getAttribute("contextPath"));
		out.write(_jsp_92800, 0, _jsp_92800.length);
		this.write(out, request.getAttribute("admin"));
		out.write(_jsp_92802, 0, _jsp_92802.length);
		this.write(out, (App.hash()));
		out.write(_jsp_92804, 0, _jsp_92804.length);
		this.write(out, (i18n.getLang()));
		out.write(_jsp_92806, 0, _jsp_92806.length);
		this.write(out, (ConfigFactory.getTextType()));
		out.write(_jsp_92808, 0, _jsp_92808.length);
		this.write(out, (ConfigFactory.getUploadPartSize()));
		out.write(_jsp_92810, 0, _jsp_92810.length);
		this.write(out, (ConfigFactory.getOperateButton()));
		out.write(_jsp_92812, 0, _jsp_92812.length);
		this.write(out, (App.getVersion()));
		out.write(_jsp_92814, 0, _jsp_92814.length);
		this.write(out, request.getAttribute("hasNewVersion"));
		out.write(_jsp_92816, 0, _jsp_92816.length);

		out.flush();
	}

	protected static final byte[]	_jsp_92640	= b(
			"<!DOCTYPE html>\r\n<html lang=\"en\">\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\r\n");
	protected static final byte[]	_jsp_92641	= b("<title>FinderWeb - Powered by FinderWeb</title>\r\n<meta http-equiv=\"Pragma\" content=\"no-cache\"/>\r\n");
	protected static final byte[]	_jsp_92642	= b("<meta http-equiv=\"Cache-Control\" content=\"no-cache\"/>\r\n<meta http-equiv=\"Expires\" content=\"0\"/>\r\n");
	protected static final byte[]	_jsp_92643	= b("<link rel=\"shortcut icon\" href=\"?action=res&path=/finder/images/favicon.png\"/>\r\n");
	protected static final byte[]	_jsp_92644	= b("<link rel=\"stylesheet\" type=\"text/css\" href=\"?action=res&path=/finder/css/finder.css&v=");
	protected static final byte[]	_jsp_92646	= b("\"/>\r\n<link rel=\"stylesheet\" type=\"text/css\" href=\"?action=res&path=/finder/css/widget.css&v=");
	protected static final byte[]	_jsp_92648	= b("\"/>\r\n<link rel=\"stylesheet\" type=\"text/css\" href=\"?action=res&path=/htree/css/middle.css&v=");
	protected static final byte[]	_jsp_92650	= b("\"/>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/jquery-1.12.4.min.js\"></script>\r\n");
	protected static final byte[]	_jsp_92651	= b("<script type=\"text/javascript\" src=\"?action=res&path=/htree/htree.js&v=");
	protected static final byte[]	_jsp_92653	= b("\"></script>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/htree/htree.util.js&v=");
	protected static final byte[]	_jsp_92655	= b("\"></script>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/tree.js&v=");
	protected static final byte[]	_jsp_92657	= b("\"></script>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/widget.js&v=");
	protected static final byte[]	_jsp_92659	= b("\"></script>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/finder.js&v=");
	protected static final byte[]	_jsp_92661	= b("\"></script>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/fileupload.js&v=");
	protected static final byte[]	_jsp_92663	= b("\"></script>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/component.js&v=");
	protected static final byte[]	_jsp_92665	= b("\"></script>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/lang/");
	protected static final byte[]	_jsp_92667	= b(".js&v=");
	protected static final byte[]	_jsp_92669	= b("\"></script>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/plugins.js&v=");
	protected static final byte[]	_jsp_92671	= b("\"></script>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/index.js&v=");
	protected static final byte[]	_jsp_92673	= b(
			"\"></script>\r\n</head>\r\n<body style=\"background: url(?action=res&path=/finder/images/7a4f.jpg) center center / cover no-repeat fixed; overflow: hidden;\" remoteIp=\"");
	protected static final byte[]	_jsp_92675	= b("\" userName=\"");
	protected static final byte[]	_jsp_92677	= b("\">\r\n<div id=\"menu-bar\" class=\"apptop unselect\" style=\"display: none;\" draggable=\"false\">\r\n");
	protected static final byte[]	_jsp_92678	= b("    <a href=\"");
	protected static final byte[]	_jsp_92680	= b("\" title=\"免费开源的文件管理系统\"><div class=\"logo\"></div></a>\r\n    <div style=\"margin-left: 300px; height: 100%;\">\r\n");
	protected static final byte[]	_jsp_92681	= b("        <div id=\"tools-menu\" class=\"tools-menu\" style=\"display: none;\">\r\n            <ul>\r\n");
	protected static final byte[]	_jsp_92682	= b("                <li id=\"tools-home\">");
	protected static final byte[]	_jsp_92684	= b("</li>\r\n                <li id=\"tools-admin\">");
	protected static final byte[]	_jsp_92686	= b("</li>\r\n                <li id=\"tools-setting\">");
	protected static final byte[]	_jsp_92688	= b("</li>\r\n                <li id=\"tools-help\">");
	protected static final byte[]	_jsp_92690	= b("</li>\r\n                <li id=\"tools-logout\">");
	protected static final byte[]	_jsp_92692	= b(
			"</li>\r\n            </ul>\r\n        </div>\r\n    </div>\r\n</div>\r\n<div id=\"view-panel\" class=\"view-panel\" split=\"x\" style=\"display: none;\">\r\n");
	protected static final byte[]	_jsp_92693	= b("    <div id=\"left-panel\" class=\"left-panel\">\r\n        <div class=\"tree-panel\">\r\n");
	protected static final byte[]	_jsp_92694	= b("            <div id=\"htree\" class=\"htree unselect\"></div>\r\n        </div>\r\n    </div>\r\n");
	protected static final byte[]	_jsp_92695	= b(
			"    <div id=\"main-panel\" class=\"main-panel\">\r\n        <div id=\"welcome-panel\" class=\"blank\">Welcome to Finder.</div>\r\n");
	protected static final byte[]	_jsp_92696	= b("        <div id=\"tab-panel-container\" class=\"tab-component\" style=\"display: none;\">\r\n");
	protected static final byte[]	_jsp_92697	= b(
			"            <div class=\"tab-label-wrap\"><ul><li class=\"tab-label\" tabId=\"finder-panel\"><span class=\"label\"></span></li></ul></div>\r\n");
	protected static final byte[]	_jsp_92698	= b(
			"            <div class=\"tab-panel\" tabId=\"finder-panel\">\r\n<!-- main view -->\r\n<div id=\"loading\" class=\"loading-mask\" style=\"display: block;\"><div class=\"loading\"><img src=\"?action=res&path=/finder/images/loading.gif\"/></div></div>\r\n");
	protected static final byte[]	_jsp_92699	= b("<div id=\"addr-bar\" class=\"addr-bar\" style=\"min-width: 740px;\" draggable=\"false\">\r\n");
	protected static final byte[]	_jsp_92700	= b("    <div class=\"b1\">\r\n        <span id=\"back\" class=\"button\" title=\"");
	protected static final byte[]	_jsp_92702	= b("\"><i class=\"back\"></i></span>\r\n        <span id=\"refresh\" class=\"button\" title=\"");
	protected static final byte[]	_jsp_92704	= b(
			"\"><i class=\"refresh\"></i></span>\r\n    </div>\r\n    <div class=\"b2\">\r\n        <div style=\"float: left;\"><input id=\"address\" type=\"text\" class=\"address\" autocomplete=\"off\" spellcheck=\"false\" draggable=\"false\" value=\"/\"/></div>\r\n");
	protected static final byte[]	_jsp_92705	= b(
			"        <div id=\"finder-suggest\" class=\"suggest\" draggable=\"false\"></div>\r\n        <span id=\"view-button\" class=\"views\" title=\"");
	protected static final byte[]	_jsp_92707	= b("\"><i></i></span>\r\n        <div id=\"view-options\" class=\"list view-menu\">\r\n            <ul>\r\n");
	protected static final byte[]	_jsp_92708	= b("                <li index=\"0\" option-value=\"outline\"><span>");
	protected static final byte[]	_jsp_92710	= b("</span></li>\r\n                <li index=\"1\" option-value=\"detail\" class=\"selected\"><span>");
	protected static final byte[]	_jsp_92712	= b(
			"</span></li>\r\n            </ul>\r\n        </div>\r\n    </div>\r\n</div>\r\n<div id=\"head-view\" class=\"head-view\" draggable=\"false\">\r\n");
	protected static final byte[]	_jsp_92713	= b(
			"    <span class=\"file-name orderable\" orderBy=\"file-name\" unselectable=\"on\" onselectstart=\"return false;\"><em class=\"title\">");
	protected static final byte[]	_jsp_92715	= b(
			"</em><em class=\"order asc\"></em></span>\r\n    <span class=\"file-size orderable\" orderBy=\"file-size\" unselectable=\"on\" onselectstart=\"return false;\"><em class=\"title\">");
	protected static final byte[]	_jsp_92717	= b(
			"</em><em class=\"order\"></em></span>\r\n    <span class=\"file-type orderable\" orderBy=\"file-type\" unselectable=\"on\" onselectstart=\"return false;\"><em class=\"title\">");
	protected static final byte[]	_jsp_92719	= b(
			"</em><em class=\"order\"></em></span>\r\n    <span class=\"modified orderable\" orderBy=\"modified\" unselectable=\"on\" onselectstart=\"return false;\"><em class=\"title\">");
	protected static final byte[]	_jsp_92721	= b("</em><em class=\"order\"></em></span>\r\n    <span class=\"operate\"><em class=\"title\">&nbsp;</em></span>\r\n");
	protected static final byte[]	_jsp_92722	= b(
			"</div>\r\n<div id=\"file-view\" class=\"file-view\" contextmenu=\"true\">\r\n    <div id=\"info-view\" class=\"info-view\" contextmenu=\"false\"></div>\r\n");
	protected static final byte[]	_jsp_92723	= b("    <ul id=\"file-list\" class=\"detail-view\"></ul>\r\n    <div id=\"watermark\" class=\"watermark\">Powered by FinderWeb v");
	protected static final byte[]	_jsp_92725	= b("</div>\r\n</div>\r\n<!-- main view -->\r\n            </div>\r\n        </div>\r\n    </div>\r\n");
	protected static final byte[]	_jsp_92726	= b(
			"</div>\r\n<div id=\"finder-contextmenu\" class=\"contextmenu\" style=\"top: 50px; left: 100px; display: none;\" draggable=\"false\">\r\n");
	protected static final byte[]	_jsp_92727	= b("    <ul class=\"menu\">\r\n        <li class=\"item\" command=\"open\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_92728	= b("            <span class=\"icon\"></span>\r\n            <a class=\"command\">");
	protected static final byte[]	_jsp_92730	= b("(O)</a>\r\n        </li>\r\n        <li class=\"item\" command=\"edit\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_92731	= b("            <span class=\"icon\"></span>\r\n            <a class=\"command\">");
	protected static final byte[]	_jsp_92733	= b("(O)</a>\r\n        </li>\r\n        <li class=\"item\" command=\"upload\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_92734	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/upload.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_92735	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_92737	= b("(F)</a>\r\n        </li>\r\n        <li class=\"item disabled\" command=\"download\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_92738	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/download.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_92739	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_92741	= b(
			"(G)</a>\r\n        </li>\r\n        <li class=\"separator\"></li>\r\n        <li class=\"item disabled\" command=\"zip\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_92742	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/zip.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_92743	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_92745	= b("(Z)</a>\r\n        </li>\r\n        <li class=\"item disabled\" command=\"unzip\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_92746	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/zip.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_92747	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_92749	= b(
			"(U)</a>\r\n        </li>\r\n        <li class=\"separator\"></li>\r\n        <li class=\"item disabled\" command=\"cut\">\r\n");
	protected static final byte[]	_jsp_92750	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/cut.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_92751	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_92753	= b("(X)</a>\r\n        </li>\r\n        <li class=\"item disabled\" command=\"copy\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_92754	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/copy.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_92755	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_92757	= b("(C)</a>\r\n        </li>\r\n        <li class=\"item disabled\" command=\"paste\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_92758	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/paste.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_92759	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_92761	= b(
			"(V)</a>\r\n        </li>\r\n        <li class=\"separator\"></li>\r\n        <li class=\"item disabled\" command=\"remove\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_92762	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/delete.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_92763	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_92765	= b(
			"(D)</a>\r\n        </li>\r\n        <li class=\"separator\"></li>\r\n        <li class=\"item\" command=\"rename\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_92766	= b("            <span class=\"icon\"></span>\r\n            <a class=\"command\">");
	protected static final byte[]	_jsp_92768	= b("(F2)</a>\r\n        </li>\r\n        <li class=\"item\" command=\"mkdir\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_92769	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/folder.png\"/></span>\r\n");
	protected static final byte[]	_jsp_92770	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_92772	= b("(N)</a>\r\n        </li>\r\n        <li class=\"item\" command=\"refresh\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_92773	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/refresh.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_92774	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_92776	= b(
			"(E)</a>\r\n        </li>\r\n        <li class=\"separator\"></li>\r\n        <li class=\"item\" command=\"info\" unselectable=\"on\">\r\n");
	protected static final byte[]	_jsp_92777	= b("            <span class=\"icon\"><img src=\"?action=res&path=/finder/images/info.gif\"/></span>\r\n");
	protected static final byte[]	_jsp_92778	= b("            <a class=\"command\">");
	protected static final byte[]	_jsp_92780	= b(
			"(R)</a>\r\n        </li>\r\n    </ul>\r\n</div>\r\n<div id=\"status-bar\" class=\"status-bar\" style=\"display: none;\" draggable=\"false\">\r\n");
	protected static final byte[]	_jsp_92781	= b("    <div class=\"copyright\">Powered by FinderWeb v");
	protected static final byte[]	_jsp_92783	= b(" | Copyright © <a href=\"#\" draggable=\"false\" target=\"_blank\">www.inaction.top</a> All rights reserved.</div>\r\n");
	protected static final byte[]	_jsp_92784	= b(
			"</div>\r\n<div id=\"splash\" class=\"splash\" draggable=\"false\">\r\n    <div class=\"welcome\"><p class=\"loading\">loading...</p></div>\r\n");
	protected static final byte[]	_jsp_92785	= b("    <div class=\"copyright\">Powered by FinderWeb v");
	protected static final byte[]	_jsp_92787	= b(" | Copyright © <a href=\"#\" draggable=\"false\" target=\"_blank\">www.inaction.top</a> All rights reserved.</div>\r\n");
	protected static final byte[]	_jsp_92788	= b("</div>\r\n<div id=\"upgrade-tips\" class=\"upgrade-tips\">New Upgrade ");
	protected static final byte[]	_jsp_92790	= b("！<a href=\"");
	protected static final byte[]	_jsp_92792	= b("\" title=\"");
	protected static final byte[]	_jsp_92794	= b("\" target=\"_blank\">Click for download.</a></div>\r\n<div id=\"access-code\" style=\"display: none;\">");
	protected static final byte[]	_jsp_92796	= b("</div>\r\n<div id=\"pageContext\" style=\"display: none;\"\r\n    data-env-name=\"");
	protected static final byte[]	_jsp_92798	= b("\"\r\n    data-context-path=\"");
	protected static final byte[]	_jsp_92800	= b("\"\r\n    data-admin=\"");
	protected static final byte[]	_jsp_92802	= b("\"\r\n    data-hash=\"");
	protected static final byte[]	_jsp_92804	= b("\"\r\n    data-lang=\"");
	protected static final byte[]	_jsp_92806	= b("\"\r\n    data-text-type=\"");
	protected static final byte[]	_jsp_92808	= b("\"\r\n    data-upload-part-size=\"");
	protected static final byte[]	_jsp_92810	= b("\"\r\n    data-display-operate-button=\"");
	protected static final byte[]	_jsp_92812	= b("\"\r\n    data-version=\"");
	protected static final byte[]	_jsp_92814	= b("\"\r\n    data-upgrade-tips=\"");
	protected static final byte[]	_jsp_92816	= b("\"></div>\r\n<!-- http://www.finderweb.net -->\r\n</body>\r\n</html>\r\n");

}
