/*
 * $RCSfile: grep.js,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
if(typeof(window.localStorage) == "undefined") {
    window.localStorage = {};
}

var Grep = {};
Grep.rows = 0;
Grep.length = 0;
Grep.loadding = 0;
Grep.maxRows = 1000;
Grep.charset = "utf-8";
Grep.editorId = "less-editor";
Grep.containerId = "less-container";
Grep.grepURL = window.location.pathname + "?action=grep.find&";

Grep.getContextPath = function() {
    if(this.contextPath == null || this.contextPath == undefined) {
        var contextPath = document.body.getAttribute("contextPath");

        if(contextPath == null || contextPath == "/") {
            contextPath = "";
        }
        this.contextPath = contextPath;
    }
    return this.contextPath;
};

Grep.getGrepURL = function(keyword, regexp, position, rows) {
    var params = [];
    params[params.length] = "host=" + encodeURIComponent(this.host);
    params[params.length] = "workspace=" + encodeURIComponent(this.workspace);
    params[params.length] = "path=" + encodeURIComponent(this.path);
    params[params.length] = "keyword=" + encodeURIComponent(keyword);
    params[params.length] = "regexp=" + (regexp == true);
    params[params.length] = "position=" + position;
    params[params.length] = "rows=" + rows;
    params[params.length] = "charset=" + this.charset;
    return this.grepURL + params.join("&");
};

Grep.parse = function(text, xhr) {
    var status = xhr.getResponseHeader("Finder-Status");

    if(status == null) {
        return {"status": 999, "message": "BadResponse."};
    }

    if(status == "302") {
        window.location.href = xhr.getResponseHeader("Finder-Location");
        return;
    }

    var message = xhr.getResponseHeader("Finder-Message");
    var lastModified = xhr.getResponseHeader("Finder-Modified");
    var contentRange = (xhr.getResponseHeader("Finder-Range") || "");
    var rows = xhr.getResponseHeader("Finder-Rows");
    var start = 0;
    var end = 0;
    var length = 0;
    var i = contentRange.lastIndexOf("/");

    if(i > -1) {
        length = parseInt(contentRange.substring(i + 1));
        contentRange = contentRange.substring(0, i);
        i = contentRange.indexOf("-");

        if(i > -1) {
            start = contentRange.substring(0, i);
            end = contentRange.substring(i + 1);
        }
    }

    var result = {"status": parseInt(status), "message": message, "value": {}};
    var range = result.value;
    range.start = parseInt(start);
    range.end = parseInt(end);
    range.rows = parseInt(rows);
    range.length = parseInt(length);

    if(lastModified != null) {
        range.lastModified = parseInt(lastModified, 16);
    }

    range.content = text;
    return result;
};

/**
 * 拉取数据
 */
Grep.poll = function(url, callback) {
    this.setStatus(1);

    jQuery.ajax({
        type: "get",
        url: url,
        dataType: "text",
        error: function(req, status, error) {
            Grep.setStatus(0, status + ": " + error);
        },
        success: function(text, status, xhr) {
            var result = Grep.parse(text, xhr);
            var range = result.value;

            if(result.status == 200 && range != null) {
                callback(range);
                Grep.setStatus(0);
            }
            else {
                Grep.setStatus(0, result.message);
            }
        }
    });
};

/**
 * 请求下一块内容
 */
Grep.next = function(callback) {
    var keyword = this.getKeyword();
    var regexp = this.getRegexp(keyword);

    if(keyword.length < 1) {
        alert("请输入查找内容！");
        return;
    }

    var position = this.getEnd();
    var rows = this.getRows();
    var url = this.getGrepURL(keyword, regexp, position, rows);

    Grep.poll(url, function(range) {
        Grep.success(range);
        Grep.append(range, keyword, regexp);

        if(callback != null) {
            callback();
        }
    });
};

/**
 * 请求指定位置的内容
 * @param percent - 百分比位置
 */
Grep.load = function(position) {
    var keyword = this.getKeyword();
    var regexp = this.getRegexp(keyword);

    if(keyword.length < 1) {
        alert("请输入查找内容！");
        return;
    }

    var rows = this.getRows();
    var url = this.getGrepURL(keyword, regexp, position, rows);

    Grep.poll(url, function(range) {
        Grep.clear();
        Grep.success(range);
        Grep.append(range, keyword, regexp);
        Grep.scroll(160);
    });
};

/**
 * 显示区域追加内容
 * @param range - 服务端返回的文件片段对象
 */
Grep.append = function(range, keyword, regexp) {
    /**
     * 此处与less功能不同
     * 无论何种情况都追加
     */
    if(range == null) {
        return;
    }

    var e = this.getEditor();
    var b = (range.rows > 0 && range.content != null && range.content.length > 0);

    if(b) {
        e.appendChild(this.create("append", range, keyword, regexp));
        return;
    }

    var size = e.childNodes.length;

    if(size > 0) {
        var last = e.childNodes[size - 1];
        last.setAttribute("end", range.end);
        last.setAttribute("length", range.length);
        return;
    }
    else {
        range.rows = 1;
        range.content = "";
        e.appendChild(this.create("append", range, keyword, regexp));
    }
};

Grep.cut1 = function() {
    var flag = false;
    var e = this.getEditor();

    while(e.childNodes.length > 200) {
        var node = e.childNodes[0];
        var count = parseInt(node.getAttribute("rows"));
        e.removeChild(node);
        this.rows -= count;
        flag = true;
    }

    while(this.rows > this.maxRows) {
        if(e.childNodes.length > 1) {
            var node = e.childNodes[0];
            var count = parseInt(node.getAttribute("rows"));
            e.removeChild(node);
            this.rows -= count;
            flag = true;
        }
        else {
            break;
        }
    }
    return flag;
};

/**
 * 当成功接收到文件片段时调用
 * @param range - 服务端返回的文件片段对象
 */
Grep.success = function(range) {
    this.rows += range.rows;
    this.length = range.length;
};

/**
 * 清除显示区域全部内容
 */
Grep.clear = function() {
    this.rows = 0;
    this.length = 0;
    this.getEditor().innerHTML = "";
};

Grep.getKeyword = function() {
    if(this.keyword != null) {
        return this.keyword;
    }
    else {
        return "";
    }
};

Grep.getRegexp = function(keyword) {
    /**
     * 尽可能的不使用正则, 这样可以避免再去取消勾选正则
     * 如果存在查找的文本, 那么客户端做一个校验
     * 如果查找的文本一定不是正则, 那么直接返回false
     */
    if(keyword != null) {
        var regExp = new RegExp("^[0-9a-zA-Z]+$");

        /**
         * 如果全部是字母或者数字, 说明一定不是正则
         */
        if(regExp.test(keyword)) {
            return false;
        }
    }
    return (this.regexp == true);
};

/**
 * 获取当前显示区域显示的起始位置
 */
Grep.getStart = function() {
    var e = this.getEditor();
    var list = e.childNodes;

    if(list.length > 0) {
        var node = list[0];
        return parseInt(node.getAttribute("start"));
    }
    else {
        return 0;
    }
};

/**
 * 获取当前显示区域显示的终止位置
 */
Grep.getEnd = function() {
    var e = this.getEditor();
    var list = e.childNodes;

    if(list.length > 0) {
        var node = list[list.length - 1];
        return parseInt(node.getAttribute("end"));
    }
    else {
        return this.length - 1;
    }
};

/**
 * 获取发送请求时请求的行数
 * 每次请求时请求的行数为当前显示区域可显示行数的12倍
 * 表达式中的16为行高, 如果显示区域的行高调整, 需要调整此参数
 * 暂时先写死吧
 */
Grep.getRows = function() {
    var c = this.getContainer();
    return Math.max(3 * Math.floor(jQuery(c).height() / 16), 300);
};

/**
 * 根据当前显示区域显示的内容显示当前显示的文件进度
 */
Grep.showProgress = function() {
    var c = this.getContainer();
    var top = jQuery(c).position().top;
    var height = jQuery(c).height();
    var scrollTop = jQuery(c).scrollTop();
    var list = c.childNodes;

    for(var i = list.length - 1; i > -1; i--) {
        var e = list[i];
        var offsetTop = e.offsetTop - scrollTop;
        var bottom = offsetTop + jQuery(e).height();

        if((offsetTop >= 0 && offsetTop < height)
            || (bottom > 0 && bottom <= height)
            || (offsetTop <= 0 && bottom >= height)) {
            var end = parseInt(e.getAttribute("end"));
            this.setProgress(end);
            break;
        }
    }
};

Grep.scroll = function(height) {
    this.getContainer().scrollTop = height;
};

Grep.getByteSize = function(size) {
    if(isNaN(size)) {
        return "NaN";
    }

    if(size < 1024) {
        return size + " B";
    }

    if(size < 1048576) {
        return Math.floor(size / 1024) + "KB";
    }

    if(size < 1073741824) {
        return (size / 1048576).toFixed(2) + "MB";
    }
    return (size / 1073741824).toFixed(2) + "GB";
};

/**
 * 显示进度
 * @param end - 当前位置
 */
Grep.setProgress = function(position) {
    var length = this.length;
    var percent = (length > 0 ? (position / length) : 0);
    var ratio = percent * 100;

    jQuery("#less-progress-bar").find(".progress .pace").css("width", ratio + "%");
    jQuery("#less-progress-bar").find(".progress .slider a").css("left", ratio + "%").attr("percent", Math.floor(ratio) + "%");

    if(jQuery("#less-info").attr("active") != "1") {
        jQuery("#less-info").val(position + "/" + length + " B");
        jQuery("#less-info").attr("title", this.getByteSize(length));
    }
};

/**
 * 设置状态
 * @param status - 状态, 0: 空闲, 1: 正在加载
 * @param message - 显示信息
 */
Grep.setStatus = function(status, message) {
    if(status == 0) {
        Grep.loadding = 0;

        if(message != null) {
            jQuery("#less-status").val(message);
        }
        else {
            jQuery("#less-status").val("READY");
        }
    }
    else {
        Grep.loadding = 1;

        if(message != null) {
            jQuery("#less-status").val(message);
        }
        else {
            jQuery("#less-status").val("loading...");
        }
    }
};

/**
 * 获取显示区域的容器
 */
Grep.getContainer = function() {
    if(this.container == null) {
        this.container = document.getElementById(this.containerId);
    }
    return this.container;
};

/**
 * 获取显示区域的容器
 */
Grep.getEditor = function() {
    if(this.container == null) {
        this.container = document.getElementById(this.containerId);
    }
    return this.container;
};

Grep.replace = function(source, search, replacement) {
    if(source == null) {
        return "";
    }

    if(search == null) {
        return source;
    }

    var s = 0;
    var e = 0;
    var d = search.length;
    var buffer = [];

    while(true) {
        e = source.indexOf(search, s);

        if(e == -1) {
            buffer[buffer.length] = source.substring(s);
            break;
        }
        buffer[buffer.length] = source.substring(s, e);
        buffer[buffer.length] = replacement;
        s = e + d;
    }
    return buffer.join("");
};

Grep.highlight1 = function(element, content, keyword) {
    if(content == null) {
        return;
    }

    if(keyword == null || keyword.length < 1) {
        element.appendChild(document.createTextNode(content));
        return;
    }

    var s = 0;
    var e = 0;
    var d = keyword.length;

    while(true) {
        e = content.indexOf(keyword, s);

        if(e == -1) {
            element.appendChild(document.createTextNode(content.substring(s)));
            break;
        }

        var span = document.createElement("span");
        span.className = "keyword";
        span.appendChild(document.createTextNode(keyword));

        element.appendChild(document.createTextNode(content.substring(s, e)));
        element.appendChild(span);
        s = e + d;
    }
};

Grep.highlight2 = function(element, content, keyword) {
    if(content == null) {
        return;
    }

    if(keyword == null || keyword.length < 1) {
        element.appendChild(document.createTextNode(content));
        return;
    }

    var start = 0;
    var end = 0;
    var regexp = new RegExp().compile(keyword, "g");
    var array = content.match(regexp);

    if(array == null) {
        return;
    }

    for(var i = 0; i < array.length; i++) {
        var item = array[i];
        end = content.indexOf(item, start);

        if(start > -1) {
            var span = document.createElement("span");
            span.className = "keyword";
            span.appendChild(document.createTextNode(item));
            element.appendChild(document.createTextNode(content.substring(start, end)));
            element.appendChild(span);
            start = end + item.length;
        }
        else {
            break;
        }
    }
    if((start + 1) < content.length) {
        element.appendChild(document.createTextNode(content.substring(start)));
    }
};

/**
 * 创建块内容
 * range: {"start": 269027, "end": 269361, "length": 269389, "rows": 2, "content": "1 0123456789\r\n2 0123456789\r\n"}}
 * start  - content中的第一个字符的位置
 * end    - content中最后一个字符的位置
 * rows   - content中包含的行数
 * length - 文件总大小, 对于实时变化的系统日志文件, 每次请求返回的文件总大小也总在变化.
 *          一般情况下日志文件会不断增大, 但也有可能从很大变为很小, 例如按天滚动的日志文件, 在凌晨时会重新产生新文件.
 * @param range - 服务器返回的文本块
 */
Grep.create = function(type, range, keyword, regexp) {
    var p = document.createElement("pre");
    p.setAttribute("action", type);
    p.setAttribute("start", range.start);
    p.setAttribute("end",   range.end);
    p.setAttribute("rows",  range.rows);
    p.setAttribute("length",  range.length);

    if(keyword == null || keyword.length < 1) {
        p.appendChild(document.createTextNode(range.content));
    }
    else {
        if(regexp != true) {
            Grep.highlight1(p, range.content, keyword);
        }
        else {
            Grep.highlight2(p, range.content, keyword);
        }
    }

    if(range.truncate == true) {
        p.setAttribute("rows",  range.rows + 1);
        p.appendChild(document.createTextNode("<<< data truncated >>>"));
    }
    return p;
};

Grep.init = function() {
    this.host = document.body.getAttribute("host");
    this.workspace = document.body.getAttribute("workspace");
    this.path = document.body.getAttribute("path");
    this.charset = document.body.getAttribute("charset");
    this.fontFamily = Finder.getConfig("less.fontFamily", "Lucida Console");
    this.fontColor = Finder.getConfig("less.fontColor", "#999999");
    this.backgroundColor = Finder.getConfig("less.backgroundColor", "#000000");

    if(this.charset == null || this.charset.length < 1) {
        this.charset = Finder.getConfig("global.charset", "utf-8");
    }

    var container = this.getContainer();
    container.style.color = this.fontColor;
    container.style.fontFamily = this.fontFamily;
    container.style.backgroundColor = this.backgroundColor;
    Charset.setup(jQuery("select[name=charset]").get(0), this.charset);

    jQuery("select[name=charset]").change(function() {
        Grep.charset = this.value;
    });

    /**
     * 修改回车键的默认操作
     */
    jQuery(container).keydown(function(event) {
        var keyCode = event.keyCode;

        if(keyCode == 13) {
            document.execCommand("InsertHtml", false, "\r\n");
            return false;
        }
        return true;
    });

    var lastScrollTop = 0;

    jQuery(container).bind("scroll", function() {
        if(Grep.loadding != 0 || Grep.flag == true) {
            return;
        }

        /**
         * 计算当前显示的进度
         */
        Grep.showProgress();

        var c = Grep.getContainer();
        var scrollTop = c.scrollTop;
        var scrollHeight = c.scrollHeight;
        var clientHeight = c.clientHeight;
        var offset = 128 * 16;

        if(lastScrollTop <= scrollTop) {
            /**
             * offset = 128 * 16, 提前n行加载，16为行高
             * 当还有n行未显示时提前加载，避免滚动到内容时加载的延迟
             */
            if((clientHeight + scrollTop + offset) >= scrollHeight) {
                Grep.cut1();
                Grep.next(function() {
                    Grep.showProgress();
                });

            }
        }
        lastScrollTop = c.scrollTop;
    });

    jQuery(container).bind("mousewheel", function(event) {
        if(Grep.loadding != 0 || Grep.flag == true) {
            return;
        }

        /**
         * 计算当前显示的进度
         */
        Grep.showProgress();

        var wheelDelta = (event.wheelDelta || event.detail);
        var c = Grep.getContainer();
        var scrollTop = c.scrollTop;
        var scrollHeight = c.scrollHeight;
        var clientHeight = c.clientHeight;
        var offset = 128 * 16;

        if(wheelDelta < 0) {
            /**
             * offset = 128 * 16, 提前n行加载，16为行高
             * 当还有n行未显示时提前加载，避免滚动到内容时加载的延迟
             */
            if((clientHeight + scrollTop + offset) >= scrollHeight) {
                Grep.cut1();
                Grep.next(function() {
                    Grep.showProgress();
                });
            }
        }
        lastScrollTop = c.scrollTop;
    });

    jQuery("#less-info").focus(function() {
        this.setAttribute("active", "1");
    });

    jQuery("#less-info").blur(function() {
        this.setAttribute("active", "0");
    });

    jQuery("#less-info").change(function() {
        var position = parseInt(this.value);

        if(isNaN(position)) {
            this.value = (this.getAttribute("data-value") || "0 B");
            return;
        }
        Grep.setProgress(position);
        Grep.load(position);
    });

    jQuery("#less-progress-bar .progress .slider .mask").unbind();
    jQuery("#less-progress-bar .progress .slider .mask").click(function(event) {
        if(event.target != this) {
            return;
        }

        var x = event.offsetX;
        var width = jQuery(this).width();
        var position = Math.floor(x / width * Grep.length);
        Grep.setProgress(position);
        Grep.load(position);
    });

    jQuery("#less-progress-bar .progress .slider .mask").mousemove(function(event) {
        if(event.target != this) {
            return;
        }

        var x = event.offsetX;
        var top = (event.clientY - 30) + "px";
        var left = (event.clientX - 16) + "px";
        var width = jQuery(this).width();
        var percent = Math.floor(x / width * 100) + "%";
        jQuery("#less-tooltip").css({"top": top, "left": left}).html(percent).show();
    });

    jQuery("#less-progress-bar .progress .slider .mask").mouseout(function() {
        jQuery("#less-tooltip").hide();
    });

    /**
     * grep
     */
    jQuery("#grep-ensure").click(function() {
        Grep.keyword = document.getElementById("grep-keyword").value;
        Grep.regexp = document.getElementById("grep-regexp").checked;
        var keyword = Grep.getKeyword();

        if(jQuery.trim(keyword).length > 0) {
            Grep.clear();
            jQuery("#find-panel").hide();

            Grep.next(function() {
                Grep.showProgress();
            });
        }
    });

    jQuery("#grep-close").click(function() {
        jQuery("#find-panel").hide();
    });

    jQuery(document.body).keydown(function(event) {
        var keyCode = event.keyCode;

        if(event.ctrlKey == true && keyCode == 66) {
            if(jQuery("#find-panel").is(":hidden")) {
                jQuery("#find-panel").show();

                try {
                    document.getElementById("grep-keyword").select();
                }
                catch(e) {
                }
            }
            else {
                jQuery("#find-panel").hide();
            }
            return false;
        }
    });

    setTimeout(function() {
        setInterval(function() {
            var keyword = Grep.getKeyword();

            if(jQuery.trim(keyword).length > 0) {
                jQuery(container).scroll();
            }
        }, 3000);
    }, 5000);
};

jQuery(function() {
    var b = [
        "Welcome to finder. http://www.finderweb.net",
        "Copyright (C) 2008 Skin, Inc. All rights reserved.",
        "This software is the proprietary information of Skin, Inc.",
        "Use is subject to license terms.",
        "================================================================================"
    ];
    console.clear();
    console.log(b.join("\r\n"));
});

jQuery(function() {
    var container = Grep.getContainer();

    jQuery(window).bind("resize", function() {
        jQuery(container).height(jQuery(window).height() - 60);
    });
    jQuery(window).trigger("resize");
});

jQuery(function() {
    Grep.init();
});
