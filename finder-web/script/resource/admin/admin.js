var PageContext = {};

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

PageContext.getContextPath = function() {
    if(this.contextPath == null || this.contextPath == undefined) {
        var contextPath = document.body.getAttribute("contextPath");

        if(contextPath == null || contextPath == "/") {
            contextPath = "";
        }
        this.contextPath = contextPath;
    }
    return this.contextPath;
};

var App = {};

App.show = function(leftUrl, mainUrl, viewType) {
    try {
        var doc = window.document;
        var leftFrame = doc.getElementById("left-frame");
        var mainFrame = doc.getElementById("main-frame");

        if(leftFrame != null && leftUrl != null) {
            leftFrame.src = leftUrl;
        }

        if(mainFrame != null && mainUrl != null) {
            mainFrame.src = mainUrl;
        }
    }
    catch(e) {
        if(typeof(window.console) != "undefined") {
            window.console.error(e.name + ": " + e.message);
        }
    }
};

jQuery(function() {
    jQuery(window).resize(function() {
        var offset = document.getElementById("view-panel").offsetTop;
        var clientHeight = document.documentElement.clientHeight;
        document.getElementById("left-panel").style.height = (clientHeight - offset) + "px";
        document.getElementById("main-panel").style.height = (clientHeight - offset) + "px";
    });
    jQuery(window).resize();
});

jQuery(function() {
    new SplitPanel({"container": "view-panel"});
});
