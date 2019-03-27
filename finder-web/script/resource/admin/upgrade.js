/*
 * $RCSfile: upgrade.js,v $
 * $Revision: 1.1 $
 */
var HostManager = {};

/**
 * 升级指定的主机
 * @param list tr的rowIndex数组
 */
HostManager.upgrade = function(list, file, callback) {
    var HostList = {};
    HostList.index = 0;
    HostList.list = list;

    HostList.hasNext = function() {
        return (this.index < this.list.length)
    };

    HostList.next = function() {
        if(this.index < this.list.length) {
            return this.list[this.index++];
        }
        return null;
    };

    /**
     * 每次更新所有机器共用同一个token
     */
    var complete = false;
    var token = UUID.random();
    var handler = function(file) {
        var index = HostList.next();
        var table = document.getElementById("host-table");
        var rows = table.rows;

        if(index == null || index >= rows.length) {
            return;
        }

        var tr = rows[index];
        var hostName = tr.getAttribute("hostName");
        var uploadUrl = "?action=admin.upgrade.upload&host=" + encodeURIComponent(hostName);
        jQuery(tr).find("a.restart").addClass("disabled").html("正在上传...");

        /**
         * 上传升级包
         */
        Upgrade.upload(uploadUrl, token, file, function() {
            /**
             * 上传速度太快了, 此处延时3秒钟再继续
             */
            setTimeout(function() {
                Upgrade.execute(tr, token, file.name, function() {
                    /**
                     * 执行重启
                     */
                    Upgrade.restart(tr);

                    if(HostList.hasNext()) {
                        handler(file);
                    }
                    else {
                        if(complete == false) {
                            complete = true;

                            if(callback != null) {
                                callback();
                            }
                        }
                    }
                });
            }, 3000);
        });
    };

    /**
     * 每次启动三个更新
     */
    handler(file);
    handler(file);
    handler(file);
};

HostManager.setLoading = function(list) {
    var table = document.getElementById("host-table");
    var rows = table.rows;

    for(var i = 0; i < list.length; i++) {
        var tr = jQuery(rows[list[i]]);
        tr.find("td input[name=hostName]").prop("disabled", true);
        tr.find("td a.sys-inf").addClass("disabled");
        tr.find("td a.restart").addClass("disabled").html("等待中...");
    }
};

var Upgrade = {};

Upgrade.check = function(index, callback) {
    if(index == null || index == undefined) {
        return;
    }

    var table = document.getElementById("host-table");
    var rows = table.rows;

    if(index >= rows.length) {
        return;
    }

    var tr = rows[index];
    var hostName = tr.getAttribute("hostName");
    var version = PageContext.getAttribute("cluster-master-version");

    if(hostName == null || hostName == undefined) {
        return;
    }

    jQuery.ajax({
        "type": "get",
        "url": "?action=agent.host.status&host=" + encodeURIComponent(hostName),
        "dataType": "json",
        "error": function() {
            jQuery(tr).find("td.version").html("连接失败");

            if(callback != null) {
                callback();
            }
        },
        "success": function(response) {
            if(response.status != 200) {
                jQuery(tr).attr("title", "主机不可用");
                jQuery(tr).find("td.version").attr("title", response.message);
                jQuery(tr).find("td.version").html("连接失败");

                if(callback != null) {
                    callback(response);
                }
                return;
            }

            var status = response.value;
            var startTime = DateUtil.format(status.timestamp);

            jQuery(tr).removeClass("disabled");
            jQuery(tr).find("input[name=hostName]").prop("disabled", false);
            jQuery(tr).find("a.sys-inf").removeClass("disabled");
            jQuery(tr).find("a.restart").removeClass("disabled");
            jQuery(tr).attr("hostVersion", status.version);
            jQuery(tr).attr("hostTimestamp", status.timestamp);

            if(status.version != version) {
                jQuery(tr).find("td.version").html("<span class=\"red\">" + status.version + "</span>");
                jQuery(tr).find("td.startTime").html("<span class=\"red\">" + startTime + "</span>");
            }
            else {
                jQuery(tr).find("td.version").html("<span>" + status.version + "</span>");
                jQuery(tr).find("td.startTime").html("<span>" + startTime + "</span>");
            }

            if(callback != null) {
                callback(response);
            }
        }
    });
};

Upgrade.upload = function(uploadUrl, token, file, callback) {
    /**
     * 一般容器默认maxPostBody大小为2M
     * finder.jar目前的大小为2M左右
     * 为确保能够上传成功, 此处使用1M的PartSize
     */
    var multipartUpload = new MultipartUpload();
    var settings = {
        "url": uploadUrl,
        "name": "uploadFile",
        "file": file,
        "data": {"token": token},
        "partSize": 1 * 1024 * 1024
    };

    multipartUpload.success = function() {
        if(callback != null) {
            callback(token, file);
        }
    };

    multipartUpload.cancel = function(xhr) {
    };

    multipartUpload.error = function(xhr, error) {
        alert("上传升级包失败，请稍后再试！");
    };
    multipartUpload.submit(settings);
};

/**
 * 执行更新
 */
Upgrade.execute = function(tr, token, fileName, callback) {
    var hostName = jQuery(tr).attr("hostName");
    jQuery(tr).find("a.restart").addClass("disabled").html("正在更新...");

    jQuery.ajax({
        "type": "post",
        "url": "?action=admin.upgrade.execute&host=" + encodeURIComponent(hostName) + "&token=" + encodeURIComponent(token) + "&fileName=" + encodeURIComponent(fileName),
        "dataType": "json",
        "error": function() {
            jQuery(tr).find("a.restart").addClass("disabled").html("升级失败");
        },
        "success": function(response) {
            if(response.status != 200) {
                jQuery(tr).find("a.restart").addClass("disabled").attr("title", response.message);
                return;
            }

            /**
             * 回调
             */
            if(callback != null) {
                callback();
            }
        }
    });
};

/**
 * 重启
 */
Upgrade.restart = function(tr) {
    var src = jQuery(tr);
    var button = jQuery(tr).find("td a.restart");
    var hostName = src.attr("hostName");
    var hostTimestamp = parseInt(src.attr("hostTimestamp"));

    button.addClass("disabled").html("重启中(30s)");

    /**
     * 忽略所有响应结果
     */
    jQuery.ajax({
        "type": "post",
        "url": "?action=agent.node.restart&host=" + encodeURIComponent(hostName),
        "dataType": "json"
    });

    var count = 30;
    var handler = function() {
        count--;
        button.addClass("disabled").html("重启中(" + count + "s)");

        if(count > 0) {
            setTimeout(handler, 1000);
            return;
        }

        Upgrade.check(tr.rowIndex, function(response) {
            if(response == null || response.status != 200) {
                button.removeClass("disabled").html("系统错误");
                return;
            }

            var status = response.value;

            if(status.timestamp > hostTimestamp) {
                button.removeClass("disabled").html("重启成功");
            }
            else {
                button.removeClass("disabled").html("重启失败");
            }
        });
    };
    setTimeout(handler, 1000);
};

var FileDialog = {};

FileDialog.select = function(accept, callback) {
    var input = document.getElementById("_finder_file_input");

    if(input == null) {
        input = document.createElement("input");
        input.id = "_finder_file_input";
        input.type = "file";
        input.name = "uploadFile";
        input.accept = accept;

        var div = document.createElement("div");
        div.style.display = "none";
        div.appendChild(input);
        document.body.appendChild(div);
    }

    input.value = "";
    jQuery(input).unbind();
    jQuery(input).change(function() {
        var files = this.files;

        if(files.length > 0) {
            callback(files[0]);
        }
    });
    input.click();
};

jQuery(function() {
    /**
     * 1. 遍历所有host, 发起ajax请求获取目标主机的finder版本号和启动时间
     */
    var index = 1;
    var table = document.getElementById("host-table");
    var rows = table.rows;
    var handler = function() {
        if((index + 1) >= rows.length) {
            return;
        }
        index++;
        Upgrade.check(index, handler);
    };
    Upgrade.check(index, handler);
});

jQuery(function() {
    jQuery("div.menu-bar a.back").click(function() {
        if(jQuery(this).hasClass("disabled")) {
            return;
        }
        window.history.back();
    });

    jQuery("div.menu-bar a.refresh").click(function() {
        if(jQuery(this).hasClass("disabled")) {
            return;
        }
        window.location.reload();
    });

    jQuery("div.menu-bar a.upgrade").click(function() {
        if(jQuery(this).hasClass("disabled")) {
            alert("正在执行升级，请稍后！");
            return;
        }

        /**
         * 1. 获取选中的主机列表
         */
        var list = [];

        jQuery("#host-table tr td input[name=hostName]:checked").each(function() {
            list[list.length] = jQuery(this).closest("tr").get(0).rowIndex;
        });

        if(list.length < 1) {
            alert("请选择要升级的主机！");
            return;
        }

        /**
         * 选择文件
         */
        FileDialog.select("application/jar", function(file) {
            var name = file.name;

            if(StringUtil.endsWith(name, ".jar") != true) {
                alert("请选择一个.jar文件！");
                return;
            }

            /**
             * 1. 禁用升级按钮
             * 2. 禁用被选择的行
             */
            jQuery("div.menu-bar a.back").addClass("disabled");
            jQuery("div.menu-bar a.refresh").addClass("disabled");
            jQuery("div.menu-bar a.upgrade").addClass("disabled");

            HostManager.setLoading(list);
            HostManager.upgrade(list, file, function() {
                jQuery("div.menu-bar a.back").removeClass("disabled");
                jQuery("div.menu-bar a.refresh").removeClass("disabled");
                jQuery("div.menu-bar a.upgrade").removeClass("disabled");
            });
        });
    });
});

jQuery(function() {
    jQuery("#checkall").click(function() {
        jQuery("#host-table tr td input[name=hostName]").each(function() {
            if(jQuery(this).prop("disabled") == true) {
                return;
            }
            jQuery(this).prop("checked", true);
        });
    });

    jQuery("#uncheck").click(function() {
        jQuery("#host-table tr td input[name=hostName]").each(function() {
            if(jQuery(this).prop("disabled") == true) {
                return;
            }
            jQuery(this).prop("checked", false);
        });
    });

    jQuery("table tr td a.sys-inf").click(function() {
        var hostName = jQuery(this).attr("hostName");
        window.location.href = "?action=admin.system.info&host=" + encodeURIComponent(hostName);
    });

    jQuery("table tr td a.restart").click(function() {
        if(jQuery(this).hasClass("disabled")) {
            return;
        }

        var tr = jQuery(this).closest("tr");
        var hostName = tr.attr("hostName");

        if(!window.confirm("确定要重启 [" + hostName + "] 吗？")) {
            return;
        }
        Upgrade.restart(tr.get(0));
    });
});

