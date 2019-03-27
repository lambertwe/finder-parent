(function() {
/**
 * import
 */
var Class = com.skin.framework.Class;

/*
 * $RCSfile: PropertyDialog.js,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
var PropertyDialog = Class.create(Dialog, null);

PropertyDialog.prototype.create = function() {
    var b = [];
    b[b.length] = "<div class=\"title\">";
    b[b.length] = "    <span class=\"icon\"><img src=\"?action=res&path=/finder/images/viewtip.gif\"/></span>";
    b[b.length] = "    <h4>&nbsp;</h4>";
    b[b.length] = "    <span class=\"close\" draggable=\"false\"></span>";
    b[b.length] = "</div>";
    b[b.length] = "<div class=\"body\">";
    b[b.length] = "    <div class=\"props-panel\">";
    b[b.length] = "        <div class=\"cp-row cp-file-name\">";
    b[b.length] = "            <span class=\"label\"><img src=\"\" class=\"file-icon\"/></span>";
    b[b.length] = "            <input name=\"fileName\" type=\"text\" class=\"file-name\" spellcheck=\"false\" value=\"\"/>";
    b[b.length] = "        </div>";
    b[b.length] = "        <div class=\"cp-line\"></div>";
    b[b.length] = "        <div class=\"cp-row cp-file-type\">";
    b[b.length] = "            <span class=\"label\">Type:</span>";
    b[b.length] = "            <span class=\"field\">&nbsp;</span>";
    b[b.length] = "        </div>";
    b[b.length] = "        <div class=\"cp-row cp-file-path\">";
    b[b.length] = "            <span class=\"label\">Path:</span>";
    b[b.length] = "            <span class=\"field\">&nbsp;</span>";
    b[b.length] = "        </div>";
    b[b.length] = "        <div class=\"cp-row cp-file-size\">";
    b[b.length] = "            <span class=\"label\">Size:</span>";
    b[b.length] = "            <span class=\"field\">&nbsp;</span>";
    b[b.length] = "        </div>";
    b[b.length] = "        <div class=\"cp-row cp-file-modified\">";
    b[b.length] = "            <span class=\"label\">Modified:</span>";
    b[b.length] = "            <span class=\"field\">&nbsp;</span>";
    b[b.length] = "        </div>";
    b[b.length] = "        <div class=\"cp-line\"></div>";
    b[b.length] = "    </div>";
    b[b.length] = "</div>";
    b[b.length] = "<div class=\"button\" style=\"text-align: right;\">";
    b[b.length] = "    <button type=\"button\" class=\"button ensure\">Ok</button>";
    b[b.length] = "    <button type=\"button\" class=\"button cancel\">Cancel</button>";
    b[b.length] = "</div>";
    this.setContent(b.join(""));

    var self = this;
    var container = this.getContainer();
    var parent = jQuery(container);
    container.className = "dialog props-dialog";

    parent.find("div.button button.ensure").click(function() {
        var input = parent.find("input[name=fileName]");
        var newName = input.val();
        var oldName = input.attr("oldValue");

        if(newName == oldName) {
            self.close();
            return;
        }
        self.rename(newName);
    });

    parent.find("div.button button.cancel").click(function() {
        self.close();
    });

    parent.find("div.title span.close").click(function() {
        self.close();
    });

    this.addShortcut("ESC", function() {
        self.close();
    });
    Draggable.registe(parent.find("div.title").get(0), container);
};

PropertyDialog.prototype.rename = function(newName) {
    var self = this;
    var url = [];
    url[url.length] = "?action=finder.rename";
    url[url.length] = "host=" + encodeURIComponent(this.file.host);
    url[url.length] = "workspace=" + encodeURIComponent(this.file.workspace);
    url[url.length] = "path=" + encodeURIComponent(this.file.path);
    url[url.length] = "newName=" + encodeURIComponent(newName);

    jQuery.ajax({
        "type": "post",
        "url": url.join("&"),
        "dataType": "json",
        "error": function() {
            self.error({"status": 502, "message": "finder.system.error"});
        },
        "success": function(response) {
            self.success(response);
            self.close();
        }
    });
};

PropertyDialog.prototype.error = function(result) {
    if(this.callback.error != null) {
        this.callback.error();
    }
    else {
        DialogUtil.alert(I18N.getLang("finder.list.rename.failed"));
    }
};

PropertyDialog.prototype.success = function(result) {
    if(this.callback.success != null) {
        this.callback.success();
    }
};

PropertyDialog.prototype.display = function(opts) {
    var icon = null;
    var type = null;
    var size = null;
    var panel = jQuery(this.getContainer());

    /**
     * 上下文可能不存在, 本地备份
     */
    var file = this.file = Class.copy(opts.file);
    this.callback = {};
    this.callback.success = opts.success;

    if(file.file != 1) {
        type = "文件夹";
        icon = "?action=res&path=/finder/images/folder.png";
    }
    else {
        type = "文件";
        icon = "?action=res&path=/finder/icon/" + file.icon + ".png";
    }

    if(!isNaN(file.size)) {
        size = ByteUtil.getByteSize(file.size) + " (" + file.size + " 字节)";
    }
    else {
        size = "未知";
    }

    panel.find(".title h4").html(file.name);
    panel.find("div.cp-file-name img.file-icon").attr("src", icon);
    panel.find("div.cp-file-name img.file-icon").attr("src", icon);
    panel.find("div.cp-file-name input.file-name").attr("oldValue", file.name);
    panel.find("div.cp-file-name input.file-name").val(file.name);
    panel.find("div.cp-file-type span.field").html(type);
    panel.find("div.cp-file-path span.field").html(file.path);
    panel.find("div.cp-file-size span.field").html(size);
    panel.find("div.cp-file-modified span.field").html(DateUtil.format(file.modified));
    this.open();
};

/**
 * ProgressDialog
 */
var ProgressDialog = Class.create(Dialog, function() {
    this.icon = "?action=res&path=/finder/images/upload.gif";
    this.title = "上传文件";
});

ProgressDialog.prototype.create = function() {
    var buffer = [];
    buffer[buffer.length] = "<div id=\"" + this.id + "_title_bar\" class=\"title\" onselectstart=\"return false;\">";
    buffer[buffer.length] = "    <span class=\"icon\"><img src=\"?action=res&path=/finder/images/viewtip.gif\"/></span>";
    buffer[buffer.length] = "    <h4>上传文件（文件上传过程中请勿刷新页面）</h4>";
    buffer[buffer.length] = "    <span class=\"min\" draggable=\"false\"></span>";
    buffer[buffer.length] = "    <span id=\"" + this.id + "_close\" class=\"close\" draggable=\"false\"></span>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "<div class=\"body\">";
    buffer[buffer.length] = "    <div style=\"padding: 20px;\">";
    buffer[buffer.length] = "        <div class=\"progress\">";
    buffer[buffer.length] = "            <div class=\"bar\"><div id=\"" + this.id + "_percent\" class=\"percent\" style=\"width: 0%\"></div></div>";
    buffer[buffer.length] = "            <div id=\"" + this.id + "_text\" class=\"text\"><p>上传：0.0M/10M&nbsp;-&nbsp;0%</p><p>速度：0M/秒</p><p>用时：00:00</p></div>";
    buffer[buffer.length] =  "        </div>";
    buffer[buffer.length] = "    </div>";
    buffer[buffer.length] = "    <div class=\"button right\">";
    buffer[buffer.length] = "        <button id=\"" + this.id + "_cancel\" type=\"button\" class=\"button cancel\" href=\"javascript:void(0)\">取 消</button>";
    buffer[buffer.length] = "    </div>";
    buffer[buffer.length] = "</div>";
    this.setContent(buffer.join(""));

    var self = this;
    var container = this.getContainer();
    var parent = jQuery(container);
    parent.css("width", "480px");

    parent.find("div.button button.cancel").click(function() {
        if(self.cancel != null) {
            self.cancel();
        }
    });

    parent.find("div.title span.min").click(function() {
        self.hide();
        TaskManager.add(self);
    });

    parent.find("div.title span.close").click(function() {
        if(self.cancel != null) {
            self.cancel();
        }
    });

    this.addShortcut("ESC", function() {
        if(self.cancel != null) {
            self.cancel();
        }
    });
    Draggable.registe(parent.find("div.title").get(0), container);
};

ProgressDialog.prototype.update = function(percent, message) {
    var e = this.getContainer();

    if(percent != null && message != null) {
        var e1 = document.getElementById(this.id + "_percent");
        var e2 = document.getElementById(this.id + "_text");

        if(e1 != null) {
            e1.style.width = percent;
        }

        if(e2 != null) {
            e2.innerHTML = message;
        }
    }
};

var ImageViewer = function() {
    this.id = "finder-imgviewer";
};

ImageViewer.prototype.create = function() {
    var b = [];
    var c = document.createElement("div");
    c.id = this.id;
    c.className = "img-viewer";
    c.setAttribute("contextmenu", "false");

    b[b.length] = "<div class=\"title\">";
    b[b.length] = "    <h4>0/0</h4>";
    b[b.length] = "    <span class=\"close\" draggable=\"false\"></span>";
    b[b.length] = "</div>";
    b[b.length] = "<div class=\"img-loading\"><img src=\"?action=res&path=/finder/images/loading6.gif\"/></div>";
    b[b.length] = "<div class=\"img-box\">";
    b[b.length] = "    <img id=\"finder_imgviewer_img\" style=\"vertical-align: middle;\"/>";
    b[b.length] = "</div>";
    b[b.length] = "<div class=\"prev-btn\"><span></span></div>";
    b[b.length] = "<div class=\"next-btn\"><span></span></div>";
    b[b.length] = "<div class=\"img-info\">";
    b[b.length] = "    <a id=\"finder_imgviewer_url\" href=\"javascript:void(0)\" target=\"_blank\">&nbsp;</a>";
    b[b.length] = "    <span id=\"finder_imgviewer_size\">&nbsp;</span>";
    b[b.length] = "</div>";
    c.innerHTML = b.join("");
    document.body.appendChild(c);

    var self = this;
    var resize = function() {
        var img = document.getElementById("finder_imgviewer_img");
        var width = parseInt(img.getAttribute("data-real-width"));
        var height = parseInt(img.getAttribute("data-real-height"));
        var maxWidth = jQuery(window).width();
        var maxHeight = jQuery(window).height() - 90;

        if(isNaN(width) || isNaN(height)) {
            width = img.offsetWidth;
            height = img.offsetHeight;
        }

        jQuery("#finder-imgviewer div.img-loading").hide();
        jQuery("#finder-imgviewer div.img-box").css({"height": maxHeight + "px", "lineHeight": maxHeight + "px"});
        self.resize(img, maxWidth, maxHeight);
    };

    jQuery("#" + this.id + " div.title span.close").click(function() {
        self.close();
    });

    jQuery("#" + this.id + " div.next-btn").click(function(event) {
        console.log("next-btn clicked");
        self.next();
    });

    jQuery("#" + this.id + " div.prev-btn").click(function(event) {
        self.prev();
    });

    jQuery("#finder_imgviewer_img").load(function() {
        var width = this.offsetWidth;
        var height = this.offsetHeight;
        var maxWidth = jQuery(window).width();
        var maxHeight = jQuery(window).height() - 90;

        /**
         * h5可以通过新的API获取原始尺寸
         * img.naturalWidth
         * img.naturalHeight
         * 此处使用兼容的方案
         */
        this.setAttribute("data-real-width", width);
        this.setAttribute("data-real-height", height);

        jQuery("#finder-imgviewer div.img-loading").hide();
        jQuery("#finder-imgviewer div.img-box").css({"height": maxHeight + "px", "lineHeight": maxHeight + "px"});
        jQuery("#finder_imgviewer_size").html("&nbsp;(" + width + " * " + height + ")");
        self.resize(this, maxWidth, maxHeight);
    });
    jQuery(window).resize(resize);
    return c;
};

ImageViewer.prototype.getContainer = function() {
    return document.getElementById(this.id);
};

ImageViewer.prototype.resize = function(img, maxWidth, maxHeight) {
    if(img.readyState != "complete") {
    }

    var width = parseInt(img.getAttribute("data-real-width"));
    var height = parseInt(img.getAttribute("data-real-height"));

    if(isNaN(width) || isNaN(height)) {
        width = img.offsetWidth;
        height = img.offsetHeight;
    }

    if(width > maxWidth || height > maxHeight) {
        var scaleWidth = 0;
        var scaleHeight = 0;
        var ratio = width / height;

        if(width > maxWidth) {
            scaleWidth = maxWidth;
            scaleHeight = Math.floor(maxWidth / ratio);

            if(scaleHeight > maxHeight) {
                scaleHeight = maxHeight;
                scaleWidth = Math.floor(maxHeight * ratio);
            }
        }
        else {
            scaleHeight = maxHeight;
            scaleWidth = Math.floor(maxHeight * ratio);

            if(scaleWidth > maxWidth) {
                scaleWidth = maxWidth;
                scaleHeight = Math.floor(maxWidth / ratio);
            }
        }
        img.style.width = scaleWidth + "px";
        img.style.height = scaleHeight + "px";
    }
    img.style.opacity = 100;
};

ImageViewer.prototype.open = function(items, index) {
    this.items = items;
    this.index = index;

    if(this.items == null || this.items.length < 1) {
        this.close();
        return;
    }

    if(isNaN(this.index)) {
        this.index = 0;
    }

    var c = this.getContainer();

    if(c == null) {
        c = this.create();
    }
    this.show();
};

ImageViewer.prototype.show = function() {
    if(this.items == null || this.items.length < 1) {
        this.close();
        return;
    }

    if(isNaN(this.index) || this.index >= this.items.length) {
        this.index = 0;
    }

    if(this.index < 0) {
        this.index = this.items.length - 1;
    }

    var item = this.items[this.index];
    jQuery("#finder-imgviewer").show();
    jQuery("#finder-imgviewer div.img-loading").show();
    jQuery("#finder-imgviewer div.title h4").html((this.index + 1) + " / " + this.items.length);
    jQuery("#finder_imgviewer_img").css({"width": "auto", "height": "auto", "opacity": 0});
    jQuery("#finder_imgviewer_img").attr("src", item.url);
    jQuery("#finder_imgviewer_img").attr("data-real-width", "--");
    jQuery("#finder_imgviewer_img").attr("data-real-height", "--");
    jQuery("#finder_imgviewer_url").html(item.name);
    jQuery("#finder_imgviewer_url").attr("href", item.url);
};

ImageViewer.prototype.prev = function() {
    this.index--;
    this.show();
};

ImageViewer.prototype.next = function() {
    this.index++;
    this.show();
};

ImageViewer.prototype.close = function() {
    this.index = -1;
    this.items = null;
    jQuery("#" + this.id).hide();
};

/*
 * $RCSfile: ZipViewer.js,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
var ZipViewer = {};

ZipViewer.open = function(title, url, width, height) {
    var frameDialog = new FrameDialog();
    frameDialog.block = false;
    frameDialog.setTitle(title);
    frameDialog.setIcon("?action=res&path=/finder/icon/rar.png");
    frameDialog.open(url, width, height);
};

/*
 * $RCSfile: PdfViewer.js,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
var PdfViewer = {};

PdfViewer.open = function(title, url, width, height) {
    var frameDialog = new FrameDialog();
    frameDialog.block = false;
    frameDialog.setTitle(title);
    frameDialog.setIcon("?action=res&path=/finder/icon/pdf.png");

    var iframe = frameDialog.getIFrame();

    iframe.onload = function() {
        if(PdfViewer.check(iframe) != true) {
            PdfViewer.error(iframe);
        }
    }
    frameDialog.open(url, width, height);
};

PdfViewer.check = function(iframe) {
    var doc = iframe.contentWindow.document;
    var scripts = doc.getElementsByTagName("script");

    for(var i = 0, length = scripts.length; i < length; i++) {
        var src = scripts[i].src;

        if(src != null && src != undefined && src.length > 0) {
            if(src.indexOf("build/pdf.js") > -1) {
                return true;
            }
        }
    }
    return false;
};

PdfViewer.error = function(iframe) {
    var buffer = [];
    buffer[buffer.length] = "<!DOCTYPE html>";
    buffer[buffer.length] = "<html lang=\"en\">";
    buffer[buffer.length] = "<head>";
    buffer[buffer.length] = "<meta charset=\"utf-8\">";
    buffer[buffer.length] = "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">";
    buffer[buffer.length] = "<title>PDF Viewer</title>";
    buffer[buffer.length] = "</head>";
    buffer[buffer.length] = "<body>";
    buffer[buffer.length] = "<div style=\"margin: 10% auto 0px auto; width: 300px; font-size: 14px; color: #666666;\">";
    buffer[buffer.length] = "    <h1 style=\"margin: 10px 0px;\">Error</h1>";
    buffer[buffer.length] = "    <p>缺少系统组件，请先安装PDF组件。</p>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "</body>";
    buffer[buffer.length] = "</html>";

    var doc = iframe.contentWindow.document;
    doc.open("text/html; charset=utf-8");
    doc.write(buffer.join("\r\n"));
    doc.close();
};

/*
 * $RCSfile: TextEditor.js,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
var TextEditor = {};

TextEditor.open = function(host, workspace, path) {
    if(this.frameDialog == null) {
        this.frameDialog = new FrameDialog();
        this.frameDialog.block = false;
        this.frameDialog.destroy = function() {
            this.setURL("about:blank");
        };
    }

    var frameDialog = this.frameDialog;
    var iframe = this.frameDialog.getIFrame();
    var url = "?action=finder.editplus";

    frameDialog.setTitle("ACE Editor");
    frameDialog.setIcon("?action=res&path=/finder/icon/ace.png");

    if(typeof(iframe.contentWindow.EditPlus) == "undefined") {
        iframe.onload = function() {
            iframe.contentWindow.componentId = frameDialog.id;

            if(typeof(iframe.contentWindow.EditPlus) != "undefined") {
                iframe.contentWindow.EditPlus.open(host, workspace, path);
            }
        }
        frameDialog.open(url, 960, 560);
    }
    else {
        iframe.contentWindow.EditPlus.open(host, workspace, path);
        frameDialog.show();
    }
};

window.PropertyDialog = PropertyDialog;
window.ProgressDialog = ProgressDialog;
window.ImageViewer = new ImageViewer();
window.ZipViewer = ZipViewer;
window.PdfViewer = PdfViewer;
window.TextEditor = TextEditor;
})();


(function(scope) {
/*
 * $RCSfile: ProgressDialog.js,v $
 * $Revision: 1.1 $
 *
 * Copyright (C) 2008 Skin, Inc. All rights reserved.
 * This software is the proprietary information of Skin, Inc.
 * Use is subject to license terms.
 */
var ProgressDialog = com.skin.framework.Class.create(Dialog, function() {
    this.icon = "?action=res&path=/finder/images/upload.gif";
    this.title = "上传文件";
});

ProgressDialog.prototype.create = function() {
    var buffer = [];
    buffer[buffer.length] = "<div id=\"" + this.id + "_title_bar\" class=\"title\" onselectstart=\"return false;\">";
    buffer[buffer.length] = "    <span class=\"icon\"><img src=\"?action=res&path=/finder/images/viewtip.gif\"/></span>";
    buffer[buffer.length] = "    <h4>上传文件（文件上传过程中请勿刷新页面）</h4>";
    buffer[buffer.length] = "    <span id=\"" + this.id + "_close\" class=\"close\" draggable=\"false\"></span>";
    buffer[buffer.length] = "    <span class=\"max\" draggable=\"false\"></span>";
    buffer[buffer.length] = "    <span class=\"min\" draggable=\"false\"></span>";
    buffer[buffer.length] = "</div>";
    buffer[buffer.length] = "<div class=\"body\">";
    buffer[buffer.length] = "    <div style=\"padding: 20px;\">";
    buffer[buffer.length] = "        <div class=\"progress\">";
    buffer[buffer.length] = "            <div class=\"bar\"><div id=\"" + this.id + "_percent\" class=\"percent\" style=\"width: 0%\"></div></div>";
    buffer[buffer.length] = "            <div id=\"" + this.id + "_text\" class=\"text\"><p>上传：0.0M/10M&nbsp;-&nbsp;0%</p><p>速度：0M/秒</p><p>用时：00:00</p></div>";
    buffer[buffer.length] =  "        </div>";
    buffer[buffer.length] = "    </div>";
    buffer[buffer.length] = "    <div class=\"button\" style=\"text-align: right;\">";
    buffer[buffer.length] = "        <button id=\"" + this.id + "_cancel\" type=\"button\" class=\"button cancel\" href=\"javascript:void(0)\">取 消</button>";
    buffer[buffer.length] = "    </div>";
    buffer[buffer.length] = "</div>";
    this.setContent(buffer.join(""));

    var self = this;
    var container = this.getContainer();
    var parent = jQuery(container);
    parent.css("width", "480px");

    parent.find("div.button button.cancel").click(function() {
        if(self.cancel != null) {
            self.cancel();
        }
    });

    parent.find("div.title span.min").click(function() {
        self.hide();
        TaskManager.add(self);
    });

    parent.find("div.title span.close").click(function() {
        if(self.cancel != null) {
            self.cancel();
        }
    });

    this.addShortcut("ESC", function() {
        if(self.cancel != null) {
            self.cancel();
        }
    });
    Draggable.registe(parent.find("div.title").get(0), container);
};

ProgressDialog.prototype.update = function(percent, message) {
    var e = this.getContainer();

    if(percent != null && message != null) {
        var e1 = document.getElementById(this.id + "_percent");
        var e2 = document.getElementById(this.id + "_text");

        if(e1 != null) {
            e1.style.width = percent;
        }

        if(e2 != null) {
            e2.innerHTML = message;
        }
    }
};

var FileUpload = {};
FileUpload.count = 0;
FileUpload.upload = function(options) {
    if(options == null || options.files == null || options.files.length < 1) {
        return;
    }

    var index = 0;
    var files = options.files;
    var uploadBytes = 0;
    var totalBytes = ByteUtil.getTotalSize(files);
    var startTime = new Date().getTime();
    var progressDialog = new ProgressDialog().open();
    var multipartUpload = new MultipartUpload();
    var partSize = ByteUtil.parse(options.partSize);
    var settings = {
        "url": options.url,
        "name": (options.fileName || "uploadFile"),
        "file": files[index],
        "partSize": (partSize > 0 ? partSize : 5 * 1024 * 1024)
    };

    progressDialog.cancel = function() {
        var self = this;
        var confirmDialog = new ConfirmDialog({parent: this});

        confirmDialog.ensure = function() {
            self.close();
            self.destroy();
            multipartUpload.abort();
        };
        confirmDialog.open("您确定要取消上传吗？");
    };

    /**
     * 瞬时速度计算
     */
    var RT = {timestamp: new Date().getTime(), value: 0, result: 0};

    RT.update = function(newValue) {
        var timeMillis = new Date().getTime();

        if(this.timestamp == null) {
            this.timestamp = timeMillis;
        }

        if(this.value == null) {
            this.value = 0;
        }

        var elapsed = Math.floor((timeMillis - this.timestamp) / 1000);

        if(elapsed > 1) {
            this.result = (newValue - this.value) / elapsed;
            this.timestamp = timeMillis;
            this.value = newValue;
        }
        return this.result;
    };

    multipartUpload.progress = function(file, loaded, total) {
        var bytes = uploadBytes + loaded;
        var percent = Math.floor(bytes / totalBytes * 100);
        var size = ByteUtil.getByteSize(bytes) + "/" + ByteUtil.getByteSize(totalBytes);
        var seconds = (new Date().getTime() - startTime) / 1000;
        var speed1 = RT.update(bytes);
        var speed2 = ByteUtil.getByteSize((seconds > 0 ? (bytes / seconds) : loaded));

        if(speed1 < 1) {
            speed1 = "-- B";
        }
        else {
            speed1 = ByteUtil.getByteSize(speed1);
        }

        if(percent > 100) {
            percent = 100;
        }

        var html = [
            "<p>当前文件：", file.name, "</p>",
            "<p>上传进度：", size, "&nbsp;-&nbsp;", percent, "%</p>",
            "<p>上传速度：", speed1, " / 秒</p>",
            "<p>平均速度：", speed2, " / 秒</p>",
            "<p>合计用时：", ByteUtil.getTimes(seconds), "</p>"
        ];
        progressDialog.update(percent + "%", html.join(""));
    };

    multipartUpload.success = function(file, result) {
        index++;

        if(options.success != null) {
            setTimeout(function() {
                options.success();
            }, 100);
        }

        uploadBytes += file.size;

        if(index < files.length) {
            settings.file = files[index];
            multipartUpload.submit(settings);
        }
        else {
            progressDialog.close();
            progressDialog.destroy();
            FileUpload.count--;
        }
    };

    multipartUpload.cancel = function(xhr) {
        if(options.cancel != null) {
            setTimeout(function() {
                options.cancel();
            }, 1000);
        }
        FileUpload.count--;
    };

    multipartUpload.error = function(xhr, error) {
        progressDialog.close();
        progressDialog.destroy();
        DialogUtil.alert(I18N.getLang("finder.message.file.upload.failed") + "<p>" + error + "</p>");

        if(options.error != null) {
            setTimeout(function() {
                options.cancel();
            }, 1000);
        }
        FileUpload.count--;
    };

    FileUpload.count++;
    multipartUpload.submit(settings);
};
scope.FileUpload = FileUpload;
})(window);

(function() {
    FileType.registe("ico, jpg, jpeg, gif, bmp, png", function(file, options) {
        if(options.download != null) {
            return false;
        }

        var ImageViewer = Finder.getComponent("ImageViewer");

        if(ImageViewer == null) {
            return false;
        }

        var index = 0;
        var items = [];
        var host = Finder.getHost();
        var workspace = Finder.getWorkspace();
        var path = Finder.getPath();
        var fileList = Finder.getFileList();
        var prefix = Finder.getRequestURI() + "?action=finder.get&host=" + encodeURIComponent(host) + "&workspace=" + encodeURIComponent(workspace);

        for(var i = 0; i < fileList.length; i++) {
            var fileName = fileList[i].name;
            var fileType = FileType.getType(fileName);

            if(FileType.contains("ico, jpg, jpeg, gif, bmp, png", fileType)) {
                if(fileName == file.name) {
                    index = items.length;
                }
                items[items.length] = {"name": fileName, "url": prefix + "&path=" + encodeURIComponent(path + "/" + fileName)};
            }
        }
        ImageViewer.open(items, index);
        return true;
    });

    FileType.registe("zip, jar, war", function(file, options) {
        if(options.download != null) {
            return false;
        }

        var ZipViewer = Finder.getComponent("ZipViewer");

        if(ZipViewer != null) {
            var url = [
                "?action=finder.zip.view",
                "host=" + encodeURIComponent(Finder.getHost()),
                "workspace=" + encodeURIComponent(Finder.getWorkspace()),
                "path=" + encodeURIComponent(file.path),
                "sub=" + encodeURIComponent("/")
            ].join("&");
            ZipViewer.open(file.name, url, 800, 480);
        }
    });

    FileType.registe("pdf", function(file, options) {
        if(options.download != null) {
            return false;
        }

        var PdfViewer = Finder.getComponent("PdfViewer");

        if(PdfViewer != null) {
            var path = Finder.getPath() + "/" + file.name;
            var address = [
                "?action=finder.get",
                "host=" + encodeURIComponent(Finder.getHost()),
                "workspace=" + encodeURIComponent(Finder.getWorkspace()),
                "path=" + encodeURIComponent(Finder.getPath() + "/" + file.name)
            ].join("&");
            var url = Finder.getContextPath() + "/pdfjs/web/viewer.html?file=" + encodeURIComponent(Finder.getRequestURI(true) + address);
            PdfViewer.open(file.name, url, 800, 480);
        }
    });
})();

