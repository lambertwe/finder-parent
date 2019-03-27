(function() {
var TreePanel = {};
TreePanel.expand = function() {
};

TreePanel.reload = function() {
};

Finder.getWindow = function() {
    return window.parent;
};

Finder.getSub = function() {
    if(this.sub == null || this.sub.length <= 1) {
        return "";
    }
    else {
        return this.sub.replace(/\\/g, "/");
    }
};

Finder.setSub = function(sub) {
    this.sub = sub;
};

Finder.getParentPath = function() {
    var sub = this.getSub();
    var k = sub.lastIndexOf("/");

    if(k > -1) {
        return sub.substring(0, k);
    }
    else {
        return "/";
    }
};

Finder.back = function() {
    var host = Finder.getHost();
    var workspace = Finder.getWorkspace();
    var path = this.getPath();
    var sub = Finder.getParentPath();
    Finder.load(host, workspace, path, sub);
};

Finder.forward = function(sub) {
};

Finder.load = function(host, workspace, path, sub) {
    this.host = host;
    this.workspace = workspace;
    this.path = path;

    if(StringUtil.isBlank(sub)) {
        this.sub = "/";
    }
    else {
        this.sub = StringUtil.trim(sub);
    }

    if(this.sub.length > 1) {
        jQuery("#back").removeClass("disabled");
        jQuery("#back i").attr("class", "back");
    }
    else {
        jQuery("#back").addClass("disabled");
        jQuery("#back i").attr("class", "back-disabled");
    }

    Finder.status = 0;
    Finder.cache = {};
    Finder.reload();
};

Finder.reload = /* public */ function(callback) {
    if(isNaN(Finder.status)) {
        return;
    }
    else {
        Finder.status = Finder.status + 1;
    }

    var status = Finder.status;
    var sub = Finder.getSub();
    var url = Finder.getURL({"action": "finder.zip.getItemList"}) + "&sub=" + encodeURIComponent(sub);

    jQuery("#loading").show();
    jQuery("#address").val(this.path);
    Finder.getContextMenu().close();

    jQuery.ajax({
        "type": "get",
        "url": url,
        "dataType": "json",
        "error": function() {
            if(status != Finder.status) {
                return;
            }

            jQuery("#loading").hide();
            jQuery("#address").val(Finder.path);
            Finder.error("System error. Please try again later !");
        },
        "success": function(response) {
            if(status != Finder.status) {
                return;
            }

            jQuery("#loading").hide();
            jQuery("#address").val(Finder.path);

            if(response == null || response.status != 200 || response.value == null) {
                Finder.error("System error. Please try again later !");

                if(callback != null) {
                    callback();
                }
                return;
            }

            Finder.list(response.value);
            TreePanel.expand();

            if(callback != null) {
                callback();
            }
        }
    });
};

Finder.open = /* public */ function(file, options) {
    var host = Finder.getHost();
    var workspace = Finder.getWorkspace();

    if(file.path == null) {
        file.path = Finder.getPath();
    }

    if(file.file == 1) {
        if(options.edit == 1) {
            var TextEditor = Finder.getComponent("TextEditor");
            TextEditor.open(host, workspace, file.path);
        }
        else {
            FileType.execute(file, (options || {}));
        }
    }
    else {
        Finder.display(file, (options || {}));
    }
    return true;
};

Finder.display = /* private */ function(file, options) {
    var host = Finder.getHost();
    var workspace = Finder.getWorkspace();
    var path = this.getPath();
    var sub = Finder.getSub();

    if(file.file == 1) {
    }
    else {
        Finder.load(host, workspace, path, sub + "/" + file.name);
    }
};

Finder.detail = /* private */ function() {
    var index = 1;
    var path = this.getPath();
    var mode = Finder.getMode();
    var textType = Finder.getAttribute("data-text-type", "");
    var fileList = Finder.getFileList();
    var b = [];

    for(var i = 0; i < fileList.length; i++) {
        var file = fileList[i];
        var fileName = HtmlUtil.encode(file.name);

        if(file.file != 1) {
            b[b.length] = "<li class=\"item\" data-name=\"" + fileName + "\" data-modified=\"" + file.modified + "\" title=\"" + Finder.getTooltip(path, file) + "\">";
            b[b.length] = "<div class=\"box\">";
            b[b.length] = "   <div class=\"icon\"><img src=\"?action=res&path=/finder/icon/folder.png\"/></div>";
            b[b.length] = "   <div class=\"file-name\">" + fileName + "</div>";
            b[b.length] = "   <div class=\"file-size\">&nbsp;</div>";
            b[b.length] = "   <div class=\"file-type\">文件夹</div>";
            b[b.length] = "   <div class=\"modified\">" + this.format(file.modified) + "</div>";
            b[b.length] = "   <div class=\"operate\">";
            b[b.length] = "<a action=\"finder-open\" href=\"javascript:void(0)\" draggable=\"false\">" + I18N.getLang("finder.list.button.open") +"</a>";
            b[b.length] = "<a action=\"finder-download\" href=\"javascript:void(0)\" draggable=\"false\">" + I18N.getLang("finder.list.button.download") +"</a>";
            b[b.length] = "   </div>";
            b[b.length] = "</div>";
            b[b.length] = "</li>";
        }
        else {
            var fileType = FileType.getType(file.name);
            b[b.length] = "<li class=\"item\" data-file=\"1\" data-icon=\"" + file.icon + "\" data-name=\"" + fileName + "\" data-size=\"" + file.size + "\" data-modified=\"" + file.modified + "\" title=\"" + Finder.getTooltip(path, file) + "\">";
            b[b.length] = "<div class=\"box\"><div class=\"icon\">";
            b[b.length] = "<img src=\"?action=res&path=/finder/icon/" + file.icon + ".png\" data-name=\"" + fileName + "\" data-icon=\"" + file.icon + "\" ondragstart=\"return false;\"/>";
            b[b.length] = "</div><div class=\"file-name\">" + fileName + "</div>";
            b[b.length] = "<div class=\"file-size\">" + ByteUtil.getByteSize(file.size) + "</div>";
            b[b.length] = "<div class=\"file-type\">" + fileType + "文件</div>";
            b[b.length] = "<div class=\"modified\">" + Finder.format(file.modified) + "</div>";
            b[b.length] = "   <div class=\"operate\">";
            b[b.length] = "<a action=\"finder-open\" href=\"javascript:void(0)\" draggable=\"false\">" + I18N.getLang("finder.list.button.open") +"</a>";
            b[b.length] = "<a action=\"finder-download\" href=\"javascript:void(0)\" draggable=\"false\">" + I18N.getLang("finder.list.button.download") +"</a>";
            b[b.length] = "   </div>";
            b[b.length] = "</div>";
            b[b.length] = "</li>";
        }
    }
    jQuery("#file-list").html(b.join(""));
};

/**
 * short cut & contextmenu
 */
jQuery(function() {
    /*
     * $RCSfile: FileListFrame.js,v $$
     * $Revision: 1.1 $
     *
     * Copyright (C) 2008 Skin, Inc. All rights reserved.
     * This software is the proprietary information of Skin, Inc.
     * Use is subject to license terms.
     */
    var FileListFrame = com.skin.framework.Class.create(Dialog, null);

    FileListFrame.prototype.create = function() {
    };

    FileListFrame.prototype.setActiveStyle = function() {
    };

    FileListFrame.prototype.close = function() {
    };

    FileListFrame.prototype.destroy = function() {
    };

    /**
     * root
     */
    var root = new FileListFrame({"container": "file-view"});
    var listener = root.getListener();

    listener.click = function(event) {
        Finder.click(event);
    };

    listener.dblclick = function(event) {
        Finder.click(event, "dblclick");
        return true;
    };

    listener.contextmenu = function(event) {
        return true;
    };

    root.addShortcut("F5", function(event) {
        Finder.reload();
        return false;
    });

    root.addShortcut("BACKSPACE", function(event) {
        Finder.back();
        return false;
    });

    root.addShortcut("HOME", function(event) {
        if(event.shiftKey != true) {
            Finder.select(Finder.getFirst(), false);
        }
        return false;
    });

    root.addShortcut("END", function(event) {
        if(event.shiftKey != true) {
            Finder.select(Finder.getLast(), false);
        }
        return false;
    });

    root.addShortcut("LEFT | UP", function(event) {
        Finder.scroll(-1, (event.shiftKey == true));
        return false;
    });

    root.addShortcut("RIGHT | DOWN", function(event) {
        Finder.scroll(+1, (event.shiftKey == true));
        return false;
    });

    root.addShortcut("ENTER", function(event) {
        Finder.getContextMenu().execute("open", event);
        return false;
    });

    root.addShortcut("SHIFT + G", function(event) {
        Finder.getContextMenu().execute("download");
        return false;
    });

    /**
     * The default shortcut key for the operating system is still returned to true
     */
    root.addShortcut("CTRL + A", function(event) {
        Finder.each(function(e) {
            e.className = "item selected";
        });
        return false;
    });

    root.addShortcut("SHIFT + R", function(event) {
        Finder.getContextMenu().execute("info");
        return false;
    });
    root.setActive(true);
});

jQuery(function() {
    StyleUtil.setStyleValue("div.head-view span.operate", "width", "107px");
    StyleUtil.setStyleValue("ul.detail-view li div.box div.operate", "width", "100px");

    /**
     * 尚未实现的功能先不显示
     */
    StyleUtil.setStyleValue("ul.detail-view li div.box:hover div.operate a", "display", "none");
    StyleUtil.setStyleValue("ul.detail-view li.selected div.box div.operate a", "display", "none");
});
})();
