jQuery(function() {
    var allow = jQuery("#info-view").attr("data-allow");

    if(allow != "true") {
        jQuery("#info-view").show();
    }
});

jQuery(function() {
    var encrypt = function(oldPass, newPass, timestamp) {
        var params = [];
        var encrypt = new JSEncrypt();
        encrypt.setPublicKey(jQuery("#publicKey").val());
        params[params.length] = "oldPass=" + encodeURIComponent(encrypt.encrypt(oldPass + "|" + timestamp));
        params[params.length] = "newPass=" + encodeURIComponent(encrypt.encrypt(newPass + "|" + timestamp));
        params[params.length] = "timestamp=" + timestamp;
        return params.join("&");
    };

    jQuery("#submit").click(function() {
        var timestamp = jQuery.trim(jQuery("#timestamp").val());
        var oldPass = jQuery.trim(jQuery("#s1").val());
        var newPass = jQuery.trim(jQuery("#s2").val());
        var params = encrypt(oldPass, newPass, timestamp);
        var requestURI = window.location.pathname;

        jQuery.ajax({
            type: "post",
            url: requestURI + "?action=user.password.update",
            dataType: "json",
            data: params,
            error: function(req, status, error) {
                alert("系统错误，请稍后再试！");
            },
            success: function(result) {
                if(result.status == 200) {
                    alert("操作成功！");
                    window.top.location.href = requestURI + "?action=finder.login";
                }
                else {
                    alert(result.message);
                }
            }
        });
    });
});
