/*
 * $RCSfile: WorkspaceEditTemplate.java,v $
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

import com.skin.finder.config.App;
import com.skin.finder.cluster.Workspace;


/**
 * <p>Title: WorkspaceEditTemplate</p>
 * <p>Description: </p>
 * @author JspKit
 * @version 1.0
 */
public class WorkspaceEditTemplate extends com.skin.finder.web.servlet.JspServlet {
    private static final long serialVersionUID = 1L;
    private static final WorkspaceEditTemplate instance = new WorkspaceEditTemplate();


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


    Workspace workspace = (Workspace)(request.getAttribute("workspace"));

    if(workspace == null) {
        workspace = new Workspace();
    }

        out.write(_jsp_20194, 0, _jsp_20194.length);
        out.write(_jsp_20195, 0, _jsp_20195.length);
        out.write(_jsp_20196, 0, _jsp_20196.length);
        out.write(_jsp_20197, 0, _jsp_20197.length);
        out.write(_jsp_20198, 0, _jsp_20198.length);
        this.write(out, (App.hash()));
        out.write(_jsp_20200, 0, _jsp_20200.length);
        out.write(_jsp_20201, 0, _jsp_20201.length);
        this.write(out, (App.hash()));
        out.write(_jsp_20203, 0, _jsp_20203.length);
        out.write(_jsp_20204, 0, _jsp_20204.length);
        out.write(_jsp_20205, 0, _jsp_20205.length);
        out.write(_jsp_20206, 0, _jsp_20206.length);
        out.write(_jsp_20207, 0, _jsp_20207.length);
        out.write(_jsp_20208, 0, _jsp_20208.length);
        out.write(_jsp_20209, 0, _jsp_20209.length);
        out.write(_jsp_20210, 0, _jsp_20210.length);
        out.write(_jsp_20211, 0, _jsp_20211.length);
        out.write(_jsp_20212, 0, _jsp_20212.length);
        out.write(_jsp_20213, 0, _jsp_20213.length);
        out.write(_jsp_20214, 0, _jsp_20214.length);
        out.write(_jsp_20215, 0, _jsp_20215.length);
        out.write(_jsp_20216, 0, _jsp_20216.length);
        out.write(_jsp_20217, 0, _jsp_20217.length);
        out.write(_jsp_20218, 0, _jsp_20218.length);
        out.write(_jsp_20219, 0, _jsp_20219.length);
        out.write(_jsp_20220, 0, _jsp_20220.length);
        out.write(_jsp_20221, 0, _jsp_20221.length);
        out.write(_jsp_20222, 0, _jsp_20222.length);
        out.write(_jsp_20223, 0, _jsp_20223.length);
        out.write(_jsp_20224, 0, _jsp_20224.length);
        out.write(_jsp_20225, 0, _jsp_20225.length);
        out.write(_jsp_20226, 0, _jsp_20226.length);
        this.write(out, request.getAttribute("hostName"));
        out.write(_jsp_20228, 0, _jsp_20228.length);
        out.write(_jsp_20229, 0, _jsp_20229.length);
        this.write(out, request.getAttribute("oldName"));
        out.write(_jsp_20231, 0, _jsp_20231.length);
        this.write(out, request.getAttribute("hostName"));
        out.write(_jsp_20233, 0, _jsp_20233.length);
        this.write(out, (workspace.getName()));
        out.write(_jsp_20235, 0, _jsp_20235.length);
        out.write(_jsp_20236, 0, _jsp_20236.length);
        out.write(_jsp_20237, 0, _jsp_20237.length);
        this.write(out, (workspace.getDisplayName()));
        out.write(_jsp_20239, 0, _jsp_20239.length);
        out.write(_jsp_20240, 0, _jsp_20240.length);
        out.write(_jsp_20241, 0, _jsp_20241.length);
        out.write(_jsp_20242, 0, _jsp_20242.length);
        this.write(out, (workspace.getWork()));
        out.write(_jsp_20244, 0, _jsp_20244.length);
        out.write(_jsp_20245, 0, _jsp_20245.length);
        this.write(out, (workspace.getCharset()));
        out.write(_jsp_20247, 0, _jsp_20247.length);
        out.write(_jsp_20248, 0, _jsp_20248.length);
        out.write(_jsp_20249, 0, _jsp_20249.length);
        this.write(out, (workspace.getReadonly()));
        out.write(_jsp_20251, 0, _jsp_20251.length);
        out.write(_jsp_20252, 0, _jsp_20252.length);
        out.write(_jsp_20253, 0, _jsp_20253.length);
        out.write(_jsp_20254, 0, _jsp_20254.length);
        this.write(out, request.getAttribute("contextPath"));
        out.write(_jsp_20256, 0, _jsp_20256.length);

        out.flush();
    }

    protected static final byte[] _jsp_20194 = b("<!DOCTYPE html>\r\n<html lang=\"en\">\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>\r\n");
    protected static final byte[] _jsp_20195 = b("<meta http-equiv=\"Pragma\" content=\"no-cache\"/>\r\n<meta http-equiv=\"Cache-Control\" content=\"no-cache\"/>\r\n");
    protected static final byte[] _jsp_20196 = b("<meta http-equiv=\"Expires\" content=\"0\"/>\r\n<title>FinderWeb - Powered by FinderWeb</title>\r\n");
    protected static final byte[] _jsp_20197 = b("<link rel=\"shortcut icon\" href=\"?action=res&path=/finder/images/favicon.png\"/>\r\n");
    protected static final byte[] _jsp_20198 = b("<link rel=\"stylesheet\" type=\"text/css\" href=\"?action=res&path=/admin/css/form.css&v=");
    protected static final byte[] _jsp_20200 = b("\"/>\r\n<script type=\"text/javascript\" src=\"?action=res&path=/finder/jquery-1.12.4.min.js\"></script>\r\n");
    protected static final byte[] _jsp_20201 = b("<script type=\"text/javascript\" src=\"?action=res&path=/admin/base.js&v=");
    protected static final byte[] _jsp_20203 = b("\"></script>\r\n<script type=\"text/javascript\">\r\n<!--\r\njQuery(function() {\r\n    jQuery(\"#submit\").click(function() {\r\n");
    protected static final byte[] _jsp_20204 = b("        var oldName = jQuery.trim(jQuery(\"input[name=oldName]\").val());\r\n        var hostName = jQuery.trim(jQuery(\"input[name=hostName]\").val());\r\n");
    protected static final byte[] _jsp_20205 = b("        var workspaceName = jQuery.trim(jQuery(\"input[name=workspaceName]\").val());\r\n");
    protected static final byte[] _jsp_20206 = b("        var displayName = jQuery.trim(jQuery(\"input[name=displayName]\").val());\r\n");
    protected static final byte[] _jsp_20207 = b("        var work = jQuery.trim(jQuery(\"input[name=work]\").val());\r\n        var charset = jQuery.trim(jQuery(\"input[name=charset]\").val());\r\n");
    protected static final byte[] _jsp_20208 = b("        var readonly = jQuery(\"input[name=readonly]\").prop(\"checked\");\r\n\r\n        if(hostName.length < 1) {\r\n");
    protected static final byte[] _jsp_20209 = b("            alert(\"主机名称不能为空！\");\r\n            return;\r\n        }\r\n\r\n        if(workspaceName.length < 1) {\r\n");
    protected static final byte[] _jsp_20210 = b("            alert(\"工作空间名称不能为空！\");\r\n            return;\r\n        }\r\n\r\n        if(work.length < 1) {\r\n");
    protected static final byte[] _jsp_20211 = b("            alert(\"工作目录不能为空！\");\r\n            return;\r\n        }\r\n\r\n        var params = [];\r\n");
    protected static final byte[] _jsp_20212 = b("        var requestURI = window.location.pathname;\r\n        params[params.length] = \"oldName=\" + encodeURIComponent(oldName);\r\n");
    protected static final byte[] _jsp_20213 = b("        params[params.length] = \"hostName=\" + encodeURIComponent(hostName);\r\n        params[params.length] = \"workspaceName=\" + encodeURIComponent(workspaceName);\r\n");
    protected static final byte[] _jsp_20214 = b("        params[params.length] = \"displayName=\" + encodeURIComponent(displayName);\r\n");
    protected static final byte[] _jsp_20215 = b("        params[params.length] = \"work=\" + encodeURIComponent(work);\r\n        params[params.length] = \"charset=\" + encodeURIComponent(charset);\r\n");
    protected static final byte[] _jsp_20216 = b("        params[params.length] = \"readonly=\" + encodeURIComponent(readonly);\r\n\r\n        jQuery.ajax({\r\n");
    protected static final byte[] _jsp_20217 = b("            type: \"post\",\r\n            url: requestURI + \"?action=admin.workspace.save\",\r\n");
    protected static final byte[] _jsp_20218 = b("            dataType: \"json\",\r\n            data: params.join(\"&\"),\r\n            error: function(req, status, error) {\r\n");
    protected static final byte[] _jsp_20219 = b("                alert(\"系统错误，请稍后再试！\");\r\n            },\r\n            success: function(result) {\r\n");
    protected static final byte[] _jsp_20220 = b("                if(result.status != 200) {\r\n                    alert(result.message);\r\n");
    protected static final byte[] _jsp_20221 = b("                    return;\r\n                }\r\n                alert(\"操作成功！\");\r\n");
    protected static final byte[] _jsp_20222 = b("                window.location.href = \"?action=admin.workspace.list&hostName=\" + encodeURIComponent(hostName);\r\n");
    protected static final byte[] _jsp_20223 = b("            }\r\n        });\r\n    });\r\n});\r\n//-->\r\n</script>\r\n</head>\r\n<body>\r\n<div class=\"menu-bar\">\r\n");
    protected static final byte[] _jsp_20224 = b("    <a class=\"button\" href=\"javascript:void(0)\" onclick=\"window.history.back();\"><span class=\"back\"></span>返回</a>\r\n");
    protected static final byte[] _jsp_20225 = b("    <a class=\"button\" href=\"javascript:void(0)\" onclick=\"window.location.reload();\"><span class=\"refresh\"></span>刷新</a>\r\n");
    protected static final byte[] _jsp_20226 = b("    <span class=\"seperator\"></span>\r\n</div>\r\n<div class=\"form\">\r\n    <div class=\"title\"><h4>工作空间编辑(");
    protected static final byte[] _jsp_20228 = b(")</h4></div>\r\n    <div class=\"form-row\">\r\n        <div class=\"form-label\">Workspace Name：</div>\r\n");
    protected static final byte[] _jsp_20229 = b("        <div class=\"form-c300\">\r\n            <div class=\"form-field\">\r\n                <input name=\"oldName\" type=\"hidden\" value=\"");
    protected static final byte[] _jsp_20231 = b("\"/>\r\n                <input name=\"hostName\" type=\"hidden\" value=\"");
    protected static final byte[] _jsp_20233 = b("\"/>\r\n                <input name=\"workspaceName\" type=\"text\" class=\"text w200\" placeholder=\"Workspace Name\" value=\"");
    protected static final byte[] _jsp_20235 = b("\"/>\r\n            </div>\r\n        </div>\r\n        <div class=\"form-m300\">\r\n            <div class=\"form-comment\">工作空间名。字母和数字开头，允许包含的字符: [a-z], [A-Z], [0-9], [-_.:@].</div>\r\n");
    protected static final byte[] _jsp_20236 = b("        </div>\r\n    </div>\r\n    <div class=\"form-row\">\r\n        <div class=\"form-label\">Display Name：</div>\r\n");
    protected static final byte[] _jsp_20237 = b("        <div class=\"form-c300\">\r\n            <div class=\"form-field\">\r\n                <input name=\"displayName\" type=\"text\" class=\"text w200\" placeholder=\"Display Name\" value=\"");
    protected static final byte[] _jsp_20239 = b("\"/>\r\n            </div>\r\n        </div>\r\n        <div class=\"form-m300\">\r\n            <div class=\"form-comment\">显示名。允许任意可见字符。如果为空则显示为工作空间名。</div>\r\n");
    protected static final byte[] _jsp_20240 = b("        </div>\r\n    </div>\r\n    <div class=\"form-row\">\r\n        <div class=\"form-label\">Work:</div>\r\n");
    protected static final byte[] _jsp_20241 = b("        <div class=\"form-comment\">当前主机的本地磁盘目录。contextPath:前缀表示finder自己的应用目录，无前缀表示磁盘的绝对地址。</div>\r\n");
    protected static final byte[] _jsp_20242 = b("        <div class=\"form-field\">\r\n            <input name=\"work\" type=\"text\" class=\"text w400\" placeholder=\"Work Directory\" value=\"");
    protected static final byte[] _jsp_20244 = b("\"/>\r\n        </div>\r\n    </div>\r\n    <div class=\"form-row\">\r\n        <div class=\"form-label\">Charset：</div>\r\n");
    protected static final byte[] _jsp_20245 = b("        <div class=\"form-c300\">\r\n            <div class=\"form-field\">\r\n                <input name=\"charset\" type=\"text\" class=\"text w200\" placeholder=\"Charset\" value=\"");
    protected static final byte[] _jsp_20247 = b("\"/>\r\n            </div>\r\n        </div>\r\n        <div class=\"form-m300\">\r\n            <div class=\"form-comment\">工作空间文本文件的字符集。</div>\r\n");
    protected static final byte[] _jsp_20248 = b("        </div>\r\n    </div>\r\n    <div class=\"form-row\">\r\n        <div class=\"form-label\">Readonly：</div>\r\n");
    protected static final byte[] _jsp_20249 = b("        <div class=\"form-c300\">\r\n            <div class=\"form-field\">\r\n                <input name=\"readonly\" type=\"checkbox\" checked-value=\"");
    protected static final byte[] _jsp_20251 = b("\" value=\"true\"/>\r\n            </div>\r\n        </div>\r\n        <div class=\"form-m300\">\r\n");
    protected static final byte[] _jsp_20252 = b("            <div class=\"form-comment\">是否只读。只读模式：包括管理员在内的所有用户都无写权限。</div>\r\n        </div>\r\n");
    protected static final byte[] _jsp_20253 = b("    </div>\r\n\r\n    <div class=\"button\">\r\n        <button id=\"submit\" class=\"button ensure\">保 存</button>\r\n");
    protected static final byte[] _jsp_20254 = b("    </div>\r\n</div>\r\n<div id=\"pageContext\" style=\"display: none;\" contextPath=\"");
    protected static final byte[] _jsp_20256 = b("\"></div>\r\n</body>\r\n</html>");

}