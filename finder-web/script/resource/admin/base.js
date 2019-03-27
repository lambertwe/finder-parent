/**
 * admin
 */
var StringUtil = {};
StringUtil.startsWith = function(source, search) {
    if(source.length >= search.length) {
        return (source.substring(0, search.length) == search);
    }
    return false;
};

StringUtil.endsWith = function(source, search) {
    if(source.length >= search.length) {
        return (source.substring(source.length - search.length, source.length) == search);
    }
    return false;
};

StringUtil.trim = function(text) {
    if(text == null || text == undefined) {
        return "";
    }
    return text.replace(/(^\s*)|(\s*$)/g, "");
};

StringUtil.isBlank = function(text) {
    if(text == null || text == undefined) {
        return true;
    }
    return (text.replace(/(^\s*)|(\s*$)/g, "").length == 0);
};

/**
 * @param text
 * @param value
 * @return boolean
 */
StringUtil.contains = function(text, value) {
    if(text == null || value == null) {
        return false;
    }

    var array = text;

    if(typeof(text) == "string") {
        if(text == "*") {
            return true;
        }
        array = text.split(",");
    }

    for(var i = 0; i < array.length; i++) {
        if(array[i] != null) {
            array[i] = StringUtil.trim(array[i]);

            if(array[i].length > 0 && array[i] == value) {
                return true;
            }
        }
    }
    return false;
};

StringUtil.unquote = function(text) {
    if(text == null) {
        return "";
    }

    if(text.length < 1) {
        return;
    }

    var i = 0;
    var j = text.length;
    var c = text.charAt(0);
    var d = text.charAt(j - 1);

    if((c == "\"" && d == "\"") || (c == "'" && d == "'")) {
        i++;
        j--;
    }

    if(i > 0 || j < text.length) {
        if(j <= i) {
            return "";
        }
        return text.substring(i, j);
    }
    return text;
};

var UUID = {};

UUID.random = function() {
    var s = [];
    var chars = "0123456789abcdef";

    for(var i = 0; i < 36; i++) {
        s[i] = chars.charAt(Math.floor(Math.random() * 0x10));
    }

    s[14] = "4"; // chars bits 12-15 of the time_hi_and_version field to 0010
    s[19] = chars.charAt((s[19] & 0x3) | 0x8); // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";
    return s.join("");
};

var CookieUtil = {};
CookieUtil.trim = function(s){return (s != null ? s.replace(/(^\s*)|(\s*$)/g, "") : "");};
CookieUtil.nval = function() {
    var i = 0;
    var e = null;

    for(i = 0; i < arguments.length; i++) {
        e = arguments[i];

        if(e == null || e == undefined) {
            continue;
        }

        if(typeof(e) == "string") {
            e = CookieUtil.trim(e);

            if(e.length > 0) {
                return e;
            }
        }
        else {
            return e;
        }
    }
    return null;
};
CookieUtil.setCookie = function(cookie) {
    var expires = "";
    if(cookie.value == null) {
        cookie.value = "";
        cookie.expires = -1;
    }

    if(cookie.expires != null) {
        var date = null;

        if(typeof(cookie.expires) == "number") {
            date = new Date();
            date.setTime(date.getTime() + cookie.expires * 1000);
        }
        else if(cookie.expires.toUTCString != null) {
            date = cookie.expires;
        }

        if(date != null) {
            expires = "; expires=" + date.toUTCString();
        }
    }
    var path = cookie.path ? "; path=" + (cookie.path) : "";
    var domain = cookie.domain ? "; domain=" + (cookie.domain) : "";
    var secure = cookie.secure ? "; secure" : "";
    document.cookie = [cookie.name, "=", (cookie.value != null ? encodeURIComponent(cookie.value) : ""), expires, path, domain, secure].join("");
};

CookieUtil.getCookie = function(name, defaultValue) {
    var value = null;

    if(document.cookie && document.cookie != "") {
        var cookies = document.cookie.split(';');

        for(var i = 0; i < cookies.length; i++) {
            var cookie = CookieUtil.trim(cookies[i]);

            if(cookie.substring(0, name.length + 1) == (name + "=")) {
                value = decodeURIComponent(cookie.substring(name.length + 1));
                break;
            }
        }
    }
    return CookieUtil.nval(value, defaultValue);
};

CookieUtil.remove = function(name, path) {
    CookieUtil.setCookie({"name": name, "path": path});
};

var DateUtil = {};

DateUtil.format = function(date) {
    if(date == null) {
        date = new Date();
    }

    if(typeof(date) == "number") {
        var temp = new Date();
        temp.setTime(date);
        date = temp;
    }

    var y = date.getFullYear();
    var M = date.getMonth() + 1;
    var d = date.getDate();
    var h = date.getHours();
    var m = date.getMinutes();
    var a = [];

    a[a.length] = y;
    a[a.length] = "-";

    if(M < 10) {
        a[a.length] = "0";
    }

    a[a.length] = M.toString();
    a[a.length] = "-";

    if(d < 10) {
        a[a.length] = "0";
    }

    a[a.length] = d.toString();

    a[a.length] = " ";

    if(h < 10) {
        a[a.length] = "0";
    }

    a[a.length] = h.toString();
    a[a.length] = ":";

    if(m < 10) {
        a[a.length] = "0";
    }

    a[a.length] = m.toString();
    return a.join("");
};

var Response = {};

Response.error = function(result) {
    alert("系统错误，请稍后再试！");
};

Response.success = function(result, success) {
    if(result == null) {
        alert("系统错误，请稍后再试！");
        return false;
    }

    if(result.code == 0 || result.status == 200) {
        if(success != null) {
            success(result.value);
        }
        return true;
    }

    if(result.message != null) {
        alert(result.message);
    }
    else{
        alert("系统错误，请稍后再试！");
    }
    return false;
};

var PageContext = {};

PageContext.getContextPath = function() {
    if(this.contextPath == null || this.contextPath == undefined) {
        var contextPath = this.getAttribute("contextPath");

        if(contextPath == null || contextPath == "/") {
            contextPath = "";
        }
        this.contextPath = contextPath;
    }
    return this.contextPath;
};

PageContext.setAttribute = function(name, value) {
    var e = document.getElementById("pageContext");

    if(e != null) {
        if(value == null || value == undefined) {
            e.removeAttribute(name);
        }
        else {
            e.setAttribute(name, value);
        }
    }
};

PageContext.getAttribute = function(name, defaultValue) {
    var value = null;
    var e = document.getElementById("pageContext");

    if(e != null) {
        value = e.getAttribute(name);
    }

    if(value == null || value == undefined) {
        value = defaultValue;
    }

    if(value != null && value != undefined) {
        return value;
    }
    return null;
};

PageContext.getInteger = function(name, defaultValue) {
    var value = this.getAttribute(name);

    if(value == null || isNaN(value)) {
        return defaultValue;
    }
    return parseInt(value);
};

jQuery(function() {
    /**
     * 下拉框显示默认值
     **/
    jQuery("select").each(function() {
        var selectedValue = this.getAttribute("selected-value");

        if(selectedValue != null && selectedValue != "") {
            var options = this.options;

            for(var i = 0; i < options.length; i++) {
                if(options[i].value == selectedValue) {
                    this.value = selectedValue;
                    break;
                }
            }
        }
    });

    /**
     * 复选框显示选中状态
     **/
    jQuery("input[type=checkbox]").each(function() {
        var checkedValue = this.getAttribute("checked-value");

        if(this.value == checkedValue) {
            this.checked = true;
        }
        else {
            if(StringUtil.contains(checkedValue, this.value)) {
                this.checked = true;
            }
            else {
                this.checked = false;
            }
        }
    });

    /**
     * 单选框显示选中状态
     **/
    jQuery("input[type=radio]").each(function() {
        var checkedValue = this.getAttribute("checked-value");

        if(this.value == checkedValue) {
            this.checked = true;
        }
        else {
            this.checked = false;
        }
    });

    var ByteUtil = {};

    ByteUtil.smart = function(bytes) {
        if(isNaN(bytes)) {
            return "NaN";
        }

        if(bytes < 1024) {
            return bytes + " B";
        }

        if(bytes < 1048576) {
            return Math.floor(bytes / 1024) + "KB";
        }

        if(bytes < 1073741824) {
            return (bytes / 1048576).toFixed(2) + "MB";
        }
        return (bytes / 1073741824).toFixed(2) + "GB";
    };

    jQuery(".bytes").each(function() {
        var value = this.getAttribute("data-bytes");

        if(value == null) {
            return;
        }
        this.innerHTML = ByteUtil.smart(parseInt(value));
    });
});

