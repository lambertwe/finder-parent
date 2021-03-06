jQuery(function() {
    var getParameter = function(url, name) {
        var queryString = url;
        var k = url.indexOf("?");

        if(k > -1) {
            queryString = url.substring(k + 1);
        }

        var array = queryString.split("&");

        for(var i = 0; i < array.length; i++) {
            var e = array[i];
            var j = e.indexOf("=");

            if(j > -1) {
                var key = e.substring(0, j);

                if(key == name) {
                    return decodeURIComponent(e.substring(j + 1));
                }
            }
        }
        return null;
    };

    var encrypt = function(userName, password, timestamp) {
        var params = [];
        var encrypt = new JSEncrypt();
        encrypt.setPublicKey(jQuery("#publicKey").val());
        params[params.length] = "userName=" + encodeURIComponent(encrypt.encrypt(userName + "|" + timestamp));
        params[params.length] = "password=" + encodeURIComponent(encrypt.encrypt(password + "|" + timestamp));
        params[params.length] = "timestamp=" + timestamp;
        return params.join("&");
    };

    jQuery("#submit").click(function() {
        var timestamp = jQuery.trim(jQuery("#timestamp").val());
        var userName = jQuery.trim(jQuery("#s1").val());
        var password = jQuery.trim(jQuery("#s2").val());
        var params = encrypt(userName, password, timestamp);
        var requestURI = window.location.pathname;

        jQuery.ajax({
            type: "post",
            url: requestURI + "?action=finder.login",
            dataType: "json",
            data: params,
            error: function(req, status, error) {
                alert("系统错误，请稍后再试！");
            },
            success: function(result) {
                if(result.status == 200) {
                    var redirect = getParameter(window.location.search, "redirect");

                    if(redirect != null) {
                        window.location.href = redirect;
                    }
                    else {
                        window.location.href = requestURI;
                    }
                }
                else {
                    alert(result.message);
                }
            }
        });
    });
});
