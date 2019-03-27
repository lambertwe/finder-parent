/*
 * $RCSfile: index.js,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
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

var Util = {};
Util.open = function(url, name, width, height, features) {
    var w = width;
    var h = height;
    if(w == null) w = window.screen.availWidth;
    if(h == null) h = window.screen.availHeight;

    var x = Math.floor((screen.availWidth  - w) / 2);
    var y = Math.floor((screen.availHeight - h - 60) / 2);

    if(x < 0) {
        x = 0;
    }

    if(y < 0) {
        y = 0;
    }

    if(features == null || features == "") {
        features = "top=" + y + ",left=" + x + ",width=" + w + ",height=" + h;
    }
    else {
        features = "top=" + y + ",left=" + x + ",width=" + w + ",height=" + h + "," + features;
    }
    return window.open(url, name, features);
};

var App = {};

App.create = function(iframe, leftUrl, mainUrl) {
    var b= [];
    b[b.length] = "<!DOCTYPE html>";
    b[b.length] = "<html lang=\"en\">";
    b[b.length] = "<head>";
    b[b.length] = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>";
    b[b.length] = "<title>FinderWeb - Powered by FinderWeb</title>";
    b[b.length] = "<link rel=\"shortcut icon\" href=\"?action=res&path=/finder/images/favicon.png\"/>";
    b[b.length] = "<link rel=\"stylesheet\" type=\"text/css\" href=\"?action=res&path=/finder/css/finder.css\"/>";
    b[b.length] = "<script type=\"text/javascript\" src=\"?action=res&path=/finder/jquery-1.12.4.min.js\"></script>";
    b[b.length] = "<script type=\"text/javascript\" src=\"?action=res&path=/finder/widget.js\"></script>";
    b[b.length] = "<script type=\"text/javascript\" src=\"?action=res&path=/admin/admin.js\"></script>";
    b[b.length] = "</head>";
    b[b.length] = "<body style=\"overflow: hidden;\">";
    b[b.length] = "<div id=\"view-panel\" split=\"x\">";
    b[b.length] = "    <div id=\"left-panel\" class=\"left-panel\"><iframe id=\"left-frame\" name=\"leftFrame\" class=\"left-frame\"";
    b[b.length] = "        src=\"" + leftUrl + "\" frameborder=\"0\" scrolling=\"no\" marginwidth=\"0\" marginheight=\"0\"></iframe></div>";
    b[b.length] = "    <div id=\"main-panel\" class=\"main-panel\"><iframe id=\"main-frame\" name=\"mainFrame\" class=\"main-frame\"";
    b[b.length] = "        src=\"" + mainUrl + "\" frameborder=\"0\" scrolling=\"auto\" marginwidth=\"0\" marginheight=\"0\"></iframe></div>";
    b[b.length] = "</div>";
    b[b.length] = "</body>";
    b[b.length] = "</html>";

    var doc = iframe.contentWindow.document;
    doc.open("text/html; charset=utf-8");
    doc.write(b.join(""));
    doc.close();
    return iframe;
};

App.show = function(id, leftUrl, mainUrl) {
    var c = document.getElementById(id);

    if(c == null) {
        c = document.createElement("div");
        c.id = id;
        c.className = "inner-frame";
        document.body.appendChild(c);

        c.innerHTML = [
            "<iframe id=\"main-frame\" src=\"about:blank\"",
            "style=\"width: 100%; height: 100%; border: 0px solid #5183dc; background-color: #ffffff; z-index: 100; overflow: hidden;\"",
            "frameborder=\"0\" scrolling=\"no\"></iframe>"].join("");

        var iframe = document.getElementById("main-frame");
        this.create(iframe, leftUrl, mainUrl);
    }
    jQuery(document.body).children("div.inner-frame").hide();
    jQuery(c).show();
};

App.home = function() {
    jQuery(document.body).children("div.inner-frame").hide();
};

App.refresh = function() {
    window.location.reload();
};

App.admin = function() {
    App.show("admin_frame", "?action=admin.menu", "?action=admin.system.info");
};

App.help = function() {
    window.open("?action=finder.help", "_blank");
};

App.logout = function() {
    if(window.confirm("Are you sure you want to quit?")) {
        window.location.href = "?action=finder.logout";
    }
};

(function() {
    jQuery(function() {
        var href = window.location.href;
        var admin = PageContext.getAttribute("data-admin", "false");

        if(href.indexOf("?nav=0") > -1 || href.indexOf("?embed=1") > -1) {
            jQuery("#menu-bar").remove();
            return;
        }

        if(admin != "true") {
            jQuery("#tools-admin").remove();
        }
        jQuery("#tools-menu").show();
    });

    jQuery(function() {
        new SplitPanel({"container": "view-panel"});

        jQuery("#tools-home").click(function() {
            App.home();
        });

        jQuery("#tools-admin").click(function() {
            App.admin();
        });

        jQuery("#tools-setting").click(function() {
            Util.open("?action=finder.setting", "_blank", 960, 600);
        });

        jQuery("#tools-help").click(function() {
            window.open("?action=finder.help");
        });

        jQuery("#tools-logout").click(function() {
            App.logout();
        });
    });

    jQuery(function() {
        var upgradeTips = PageContext.getAttribute("upgrade-tips");

        if(upgradeTips == "true") {
            setTimeout(function() {
                jQuery("#upgrade-tips").fadeIn();
            }, 1000);

            var timer = setTimeout(function() {
                jQuery("#upgrade-tips").fadeOut();
            }, 8000);

            jQuery("#upgrade-tips").mouseover(function() {
                if(timer != null) {
                    clearTimeout(timer);
                }
            });

            jQuery("#upgrade-tips").mouseout(function() {
                timer = setTimeout(function() {
                    jQuery("#upgrade-tips").fadeOut();
                }, 3000);
            });
        }
    });
})();

(function() {
    var Fullscreen = {};

    Fullscreen.request = function(e) {
        try {
            if(e.requestFullscreen) {
                e.requestFullscreen();
            }
            else if(e.webkitRequestFullScreen) {
                if(window.navigator.userAgent.toUpperCase().indexOf("CHROME") >= 0) {
                    e.webkitRequestFullScreen(Element.ALLOW_KEYBOARD_INPUT);
                }
                else {
                    e.webkitRequestFullScreen();
                }
            }
            else if(e.mozRequestFullScreen) {
                e.mozRequestFullScreen();
            }
        }
        catch(e) {
        }
    };

    Fullscreen.cancel = function(e) {
        try {
            if(document.exitFullscreen) {
                document.exitFullscreen();
            }
            else if(document.webkitCancelFullScreen) {
                document.webkitCancelFullScreen();
            }
            else if(document.mozCancelFullScreen) {
                document.mozCancelFullScreen();
            }
        }
        catch(e) {
        }
    };

    var getLabel = function(src) {
        var parent = src;

        while(parent != null && (parent.className == null || parent.className.indexOf("tab-label") < 0)) {
            parent = parent.parentNode;
        };
        return parent;
    };

    var toggle = function() {
        var container = jQuery("#tab-panel-container");

        if(container.hasClass("fullscreen")) {
            container.removeClass("fullscreen");
            container.css({"position": "relative", "zIndex": "auto"});
        }
        else {
            container.addClass("fullscreen");
            container.css({"position": "absolute", "zIndex": "200"});
        }
    };

    function setLabel(label) {
        var e = window.tabPanel.getLabel(label.id);

        if(e == null) {
            return;
        }

        var span = jQuery(e).find("span.label");

        if(label.title != null) {
            span.html(label.title);
        }

        if(label.tooltips != null) {
            span.attr("title", label.tooltips);
        }
    };

    jQuery(function() {
        var ID = {};

        ID.next = function() {
            if(this.seed == null) {
                this.seed = 0;
            }

            this.seed++;
            return this.seed;
        };

        var tabPanel = window.tabPanel = new TabPanel({"container": "tab-panel-container"});

        tabPanel.display = function(host, workspace, path) {
            var title = FileType.getName(path);
            var tooltips = host + "@" + workspace;

            if(title == "" || title == "/") {
                title = workspace;
            }
            else {
                 tooltips = tooltips + "/" + path
            }

            jQuery("#welcome-panel").remove();
            jQuery("#tab-panel-container").show();
            var label = window.tabPanel.getLabel("finder-panel");
            var span = jQuery(label).find("span.label");

            span.html(title);
            span.attr("title", tooltips);
            window.tabPanel.active(label);
            Finder.load(host, workspace, path);
        };

        tabPanel.addTabPanel = function addTabPanel(title, tooltips, url) {
            var tabId = ID.next();
            var iframe = document.createElement("iframe");
            iframe.frameborder = "0";
            iframe.scrolling = "auto";
            iframe.style.cssText = "position: relative; top: 0px; left: 0px; width: 100%; height: 100%; border: 0px solid #ffffff; background-color: transparent; overflow: hidden;";
            iframe.src = url;

            var opts = {"id": tabId, "title": title, "tooltips": tooltips, "content": iframe, "active": true, "closeable": true};
            window.tabPanel.append(opts);
        };

        jQuery("#tab-panel-container ul").dblclick(function(event) {
            var e = (event || window.event);
            var src = (e.srcElement || e.target);

            if(src == null) {
                return;
            }

            var label = getLabel(src);

            if(label != null) {
                toggle();
            }
        });
    });
})();

(function() {
    jQuery(function() {
        jQuery("#menu-bar").show();
        jQuery("#view-panel").show();
        jQuery("#status-bar").show();

        setTimeout(function() {
            jQuery("#splash").fadeOut(500);
        }, 500);
    });
})();
