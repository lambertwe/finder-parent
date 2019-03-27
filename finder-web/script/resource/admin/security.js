jQuery(function() {
    jQuery("button.key-generate").click(function() {
        var name = jQuery(this).attr("data-for-name");

        if(name == null) {
            return;
        }

        jQuery.ajax({
            "type": "get",
            "url": "?action=admin.random.uuid",
            "dataType": "json",
            "error": function() {
                alert("系统错误，请稍后再试！");
            },
            "success": function(response) {
                if(response.status != 200) {
                    alert(response.message);
                    return;
                }
                jQuery("input[name=" + name + "]").val(response.value);
            }
        });
    });

    jQuery("button.rsa-generate").click(function() {
        jQuery.ajax({
            "type": "get",
            "url": "?action=admin.security.rsa.generate",
            "dataType": "json",
            "error": function() {
                alert("系统错误，请稍后再试！");
            },
            "success": function(response) {
                if(response.status != 200) {
                    alert(response.message);
                    return;
                }

                var keyPair = response.value;
                jQuery("textarea[name=publicKey]").val(keyPair.publicKey);
                jQuery("textarea[name=privateKey]").val(keyPair.privateKey);
            }
        });
    });

    jQuery("#submit").click(function() {
        var params = {};
        params.adminName = jQuery.trim(jQuery("input[name=adminName]").val());
        params.securityKey = jQuery.trim(jQuery("input[name=securityKey]").val());
        params.sessionName = jQuery.trim(jQuery("input[name=sessionName]").val());
        params.sessionKey = jQuery.trim(jQuery("input[name=sessionKey]").val());
        params.publicKey = jQuery.trim(jQuery("textarea[name=publicKey]").val());
        params.privateKey = jQuery.trim(jQuery("textarea[name=privateKey]").val());

        jQuery.ajax({
            "type": "post",
            "url": "?action=admin.security.config.save",
            "dataType": "json",
            "data": jQuery.param(params, true),
            "error": function() {
                alert("系统错误，请稍后再试！");
            },
            "success": function(response) {
                if(response.status != 200) {
                    alert(response.message);
                }
                else {
                    alert("保存并同步成功！");
                }
                window.location.reload();
            }
        });
    });
});
