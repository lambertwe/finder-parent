/*
 * $RCSfile: display.js,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
(function() {
var ResourceLoader = {};

ResourceLoader.css = function() {
    var doc, url;

    if(arguments.length == 0) {
        return;
    }
    else if(arguments.length == 1) {
        doc = document;
        url = arguments[0];
    }
    else {
        doc = arguments[0];
        url = arguments[1];
    }

    var scripts = doc.getElementsByTagName("link");

    for(var i = 0, length = scripts.length; i < length; i++) {
        var href = scripts[i].href;

        if(href != null && href != undefined && href.length > 0) {
            if(href.indexOf(url) > -1) {
                return;
            }
        }
    }

    var e = doc.createElement("link");
    e.rel = "stylesheet";
    e.type = "text/css";
    e.href = url;
    (doc.getElementsByTagName("head"))[0].appendChild(e);
};

ResourceLoader.script = function() {
    var doc, url;

    if(arguments.length == 0) {
        return;
    }
    else if(arguments.length == 1) {
        doc = document;
        url = arguments[0];
    }
    else {
        doc = arguments[0];
        url = arguments[1];
    }

    var scripts = doc.getElementsByTagName("script");

    for(var i = 0, length = scripts.length; i < length; i++) {
        var src = scripts[i].src;

        if(src != null && src != undefined && src.length > 0) {
            if(src.indexOf(url) > -1) {
                return;
            }
        }
    }

    var e = doc.createElement("script");
    e.type = "text/javascript";
    e.src = url;
    (doc.getElementsByTagName("head"))[0].appendChild(e);
};

var FileType = {};

/**
 * @param path
 * @return
 */
FileType.getName = function(path) {
    if(path != null && path.length > 0) {
        var c = null;
        var i = path.length - 1;

        for(; i > -1; i--) {
            c = path.charAt(i);

            if(c == "/" || c == "\\" || c == ":") {
                break;
            }
        }
        return path.substring(i + 1);
    }
    return "";
};

/**
 * @param path
 * @return String
 */
FileType.getExtension = function(path) {
    if(path != null && path.length > 0) {
        var c = null;
        var i = path.length - 1;

        for(; i > -1; i--) {
            c = path.charAt(i);

            if(c == ".") {
                return path.substring(i + 1);
            }
            else if(c == "/" || c == "\\" || c == ":") {
                return "";
            }
        }
    }
    return "";
};

/**
 * @param path
 * @return String
 */
FileType.getType = function(path) {
    return FileType.getExtension(path).toLowerCase();
};

var TextViewer = function() {
};

TextViewer.css = function(css) {
    var e = document.createElement("style");
    e.type = "text/css";

    if(e.styleSheet) {
        e.styleSheet.cssText = css;
    }
    else {  
        e.appendChild(document.createTextNode(css));
    }  
    document.getElementsByTagName("head")[0].appendChild(e);
};

TextViewer.getHost = function() {
    return this.host;
};

TextViewer.getWorkspace = function() {
    return this.workspace;
};

TextViewer.getPath = function() {
    if(this.path == null || this.path.length <= 1) {
        this.path = "";
    }
    return this.path;
};

TextViewer.getTheme = function() {
    if(this.theme == null || this.theme.length <= 1) {
        this.theme = TextViewer.getParameter("theme", "Default");
    }
    return this.theme;
};

TextViewer.getType = function() {
    if(this.type == null) {
        this.type = TextViewer.getParameter("type", null);

        /**
         * 可能选择空
         */
        if(this.type == null) {
            this.type = FileType.getType(this.getPath());
        }
    }
    return this.type;
};

TextViewer.getCharset = function() {
    if(this.charset == null || this.charset.length <= 1) {
        this.charset = TextViewer.getParameter("charset", "utf-8");
    }
    return this.charset;
};

TextViewer.getParameter = function(name, defaultValue) {
    var queryString = window.location.search;
    var array = queryString.split("&");
    var prefix = name + "=";
    var value = null;

    for(var i = 0; i < array.length; i++) {
        var item = array[i];

        if(item.length >= prefix.length && item.substring(0, prefix.length) == prefix) {
            value = item.substring(prefix.length);
            break;
        }
    }

    if(value != null) {
        return value;
    }
    return defaultValue;
};

TextViewer.getURL = function(action) {
    var params = [];
    params[params.length] = "?action=" + encodeURIComponent(action);
    params[params.length] = "host=" + encodeURIComponent(this.getHost());
    params[params.length] = "workspace=" + encodeURIComponent(this.getWorkspace());
    params[params.length] = "path=" + encodeURIComponent(this.getPath());
    return params.join("&");
};

TextViewer.load = function() {
    var params = [];
    params[params.length] = "action=finder.getText";
    params[params.length] = "host=" + encodeURIComponent(this.getHost());
    params[params.length] = "workspace=" + encodeURIComponent(this.getWorkspace());
    params[params.length] = "path=" + encodeURIComponent(this.getPath());
    params[params.length] = "charset=" + TextViewer.getCharset();

    jQuery("#loading").show();
    jQuery.ajax({
        "type": "get",
        "url": "?" + params.join("&"),
        "dataType": "text",
        "error": function(req, status, error) {
            TextViewer.error(status, error);
        },
        "success": function(text, status, xhr) {
            TextViewer.show(text, xhr);
        }
    });
};

TextViewer.getResponse = function(xhr) {
    var response = {"status": "500", "message": "System Error."};
    var status = xhr.getResponseHeader("Finder-Status");
    var message = xhr.getResponseHeader("Finder-Message");
    var range = (xhr.getResponseHeader("Finder-Range") || "");
    var modified = xhr.getResponseHeader("Finder-Modified");

    if(message != null) {
        message = decodeURIComponent(message).replace(new RegExp("\\+", "g"), " ");
    }

    if(status == null) {
        return response;
    }

    response.status = status;
    response.message = message;

    if(status != "200") {
        return response;
    }

    if(range == null) {
        response.status = "500";
        response.message = "Bad Response.";
        return response;
    }

    var result = response.value = {"start": 0, "end": 0, "size": 0, "modified": 0};

    try {
        var i = range.lastIndexOf("/");
        var size = parseInt(range.substring(i + 1));
        var range = range.substring(0, i);
        var k = range.indexOf("-");
        var start = range.substring(0, k);
        var end = range.substring(k + 1);

        result.start = parseInt(start);
        result.end = parseInt(end);
        result.size = parseInt(size);
        result.modified = parseInt(modified);

        if(result.size > 0) {
            result.count = (result.end - result.start + 1);
        }
        else {
            result.count = 0;
        }
        return response;
    }
    catch(e) {
        response.status = "500";
        response.message = "Bad Response.";
        return response;
    }
};

TextViewer.show = function(text, xhr) {
    var response = TextViewer.getResponse(xhr);

    if(response.status != "200") {
        TextViewer.error(response.status, response.message);
        return;
    }

    var range = response.value;

    if(range.count < range.size) {
        var url = TextViewer.getURL("finder.less");
        var info = "文件较大，只显示部分数据。要查看全部数据请使用"
            + " <a href=\"" + url + "\" style=\"color: #ff0000;\">less</a> 打开。"
            + "[" + range.start + " - " + range.end + "/" + range.size + "]";
        jQuery("#data-view").html(info).show();
    }

    jQuery("#content pre").text(text);
    TextViewer.init();
};

TextViewer.error = function(status, message) {
    jQuery("#info-view").html("<h1>" + status + "</h1><p>" + message + "</p>").show();
    jQuery("#content").hide();
    jQuery("#loading").hide();
};

TextViewer.init = function() {
    var map = {
        "??": "brush: bash;",
        "as": "brush: actionscript3;",
        "bsh": "brush: bash;",
        "log": "brush: bash;",
        "cpp": "brush: cpp;",
        "cs": "brush: cs;",
        "css": "brush: css;",
        "dhi": "brush: dpi;",
        "diff": "brush: diff;",
        "erl": "brush: erl;",
        "erlang": "brush: erlang;",
        "groovy": "brush: groovy;",
        "java": "brush: java;",
        "js": "brush: javascript;",
        "pl": "brush: perl;",
        "php": "brush: php;",
        "plain": "brush: plain;",
        "sh": "brush: bash;",
        "py": "brush: python;",
        "ruby": "brush: ruby;",
        "sass": "brush: sass;",
        "scala": "brush: scala;",
        "sql": "brush: sql;",
        "vb": "brush: vbscript;",
        "vbs": "brush: vbscript;",
        "xml": "brush: xml;",
        "xhtml": "brush: xml;",
        "xslt": "brush: xml;",
        "html": "brush: xml;",
        "htm": "brush: xml;",
        "jsp": "brush: xml;",
        "jspf": "brush: xml;",
        "asp": "brush: xml;",
        "php": "brush: xml;"
    };

    var type = this.getType();

    if(type == "" || type == "??") {
        jQuery("#content pre").attr("class", "brush: bash;");
    }
    else {
        var brush = map[type];

        if(type != null) {
            jQuery("#content pre").attr("class", brush);
        }
        else {
            jQuery("#content pre").attr("class", "brush: plain;");
        }
    }

    function path() {
        var result = [];
        var args = arguments;
        var requestURI = window.location.pathname;

        for(var i = 0; i < args.length; i++) {
            result.push(args[i].replace("@", requestURI + "?action=res&path=/sh/"));
        }
        return result;
    };

    var args = path(
        "applescript            @shBrushAppleScript.js",
        "actionscript3 as3      @shBrushAS3.js",
        "bash shell             @shBrushBash.js",
        "coldfusion cf          @shBrushColdFusion.js",
        "cpp c                  @shBrushCpp.js",
        "c# c-sharp csharp      @shBrushCSharp.js",
        "css                    @shBrushCss.js",
        "delphi pascal          @shBrushDelphi.js",
        "diff patch pas         @shBrushDiff.js",
        "erl erlang             @shBrushErlang.js",
        "groovy                 @shBrushGroovy.js",
        "java                   @shBrushJava.js",
        "jfx javafx             @shBrushJavaFX.js",
        "js jscript javascript  @shBrushJScript.js",
        "perl pl                @shBrushPerl.js",
        "php                    @shBrushPhp.js",
        "text plain             @shBrushPlain.js",
        "py python              @shBrushPython.js",
        "ruby rails ror rb      @shBrushRuby.js",
        "sass scss              @shBrushSass.js",
        "scala                  @shBrushScala.js",
        "sql                    @shBrushSql.js",
        "vb vbnet               @shBrushVb.js",
        "xml xhtml xslt html    @shBrushXml.js"
    );

    SyntaxHighlighter.defaults["quick-code"] = false;
    SyntaxHighlighter.autoloader.apply(null, args);
    SyntaxHighlighter.all();
};

jQuery(function() {
    TextViewer.host = jQuery("#pageContext").attr("data-host");
    TextViewer.workspace = jQuery("#pageContext").attr("data-workspace");
    TextViewer.path = jQuery("#pageContext").attr("data-path");
});

jQuery(function() {
    var theme = TextViewer.getTheme();
    var charset = TextViewer.getCharset();
    var type = TextViewer.getType();

    jQuery("#uiTypeOption").attr("selected-value", type);
    jQuery("#uiThemeOption").attr("selected-value", theme);
    jQuery("#uiCharsetOption").attr("selected-value", charset);

    /**
     * 加载对应的主题
     */
    ResourceLoader.css("?action=res&path=%2Fsh%2Fstyle%2FshCore" + theme + ".css");
    ResourceLoader.css("?action=res&path=%2Fsh%2Fstyle%2FshTheme" + theme + ".css");
});

jQuery(function() {
    /**
     * 设置下拉框组件
     */
    var setOptions = function(e, list, value) {
        for(var i = e.length - 1; i > -1; i--) {
            e.options.remove(i);
        }

        var flag = false;
        var selected = value;

        if(selected == null || selected == undefined) {
            selected = e.getAttribute("selected-value");
        }

        for(var i = 0; i < list.length; i++) {
            var item = list[i];
            var option = new Option(item, item);

            if(selected != null && item == selected) {
                option.selected = true;
            }
            e.options.add(option);
        }
    };

    var themes = ["Default", "Django", "Eclipse", "Emacs", "FadeToGrey", "MDUltra", "Midnight", "RDark"];
    var types = ["", "as", "sh", "bsh", "bash", "log", "shell", "cpp", "cs", "css", "dpi", "diff", "erl", "erlang", "groovy", "java", "js", "pl", "php", "txt", "text", "py", "ruby", "sass", "scala", "sql", "vb", "vbs", "xml", "xhtml", "xslt", "html", "htm", "asp", "jsp", "jspf", "asp", "php"];
    var charsets = ["utf-8", "gbk", "gb2312", "iso-8859-1"];

    setOptions(document.getElementById("uiTypeOption"), types);
    setOptions(document.getElementById("uiThemeOption"), themes);
    setOptions(document.getElementById("uiCharsetOption"), charsets);
});

jQuery(function() {
    jQuery("#uiTypeOption").change(function() {
        var host = TextViewer.getHost();
        var workspace = TextViewer.getWorkspace();
        var path = TextViewer.getPath();
        var type = jQuery("#uiTypeOption").val();
        var theme = jQuery("#uiThemeOption").val();
        var charset = jQuery("#uiCharsetOption").val();
        var params = [];
        params[params.length] = "action=finder.display";
        params[params.length] = "host=" + encodeURIComponent(host);
        params[params.length] = "workspace=" + encodeURIComponent(workspace);
        params[params.length] = "path=" + encodeURIComponent(path);
        params[params.length] = "type=" + encodeURIComponent(type);
        params[params.length] = "theme=" + encodeURIComponent(theme);
        params[params.length] = "charset=" + encodeURIComponent(charset);
        window.location.href = "?" + params.join("&");
    });

    jQuery("#uiThemeOption").change(function() {
        jQuery("#uiTypeOption").change();
    });

    jQuery("#uiCharsetOption").change(function() {
        jQuery("#uiTypeOption").change();
    });
});

jQuery(function() {
    jQuery(window).resize(function() {
        var c = jQuery("#content div.syntaxhighlighter");
        c.css({"marginBottom": "100px", "overflow": "auto"});

        jQuery("#content").show();
        c.height(jQuery(window).height() - jQuery("#content").position().top);
    });
    TextViewer.load();

    /**
     * 每隔200毫秒检查一次是否初始化完成, 初始化完成则resize窗口
     * 高亮组件似乎是异步处理的, 调用高亮之后, dom并未创建
     */
    var count = 0;
    var handler = function() {
        if(count++ >= 30) {
            return;
        }

        if(jQuery("#content pre").size() < 1) {
            jQuery(window).resize();
            jQuery("#loading").hide();
            return;
        }
        setTimeout(arguments.callee, 200);
    };
    setTimeout(handler, 500);
});
})();
