<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" isErrorPage="true"%>
<%!
    private static org.slf4j.Logger logger = null;

    public void jspInit() {
        logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    }

    private String getRequestURL(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        Object requestURI = request.getAttribute("FILTER_REQUEST_URI");

        if(requestURI == null) {
            requestURI = request.getRequestURI();
        }

        StringBuilder buffer = new StringBuilder();
        buffer.append(request.getScheme());
        buffer.append("://");
        buffer.append(request.getServerName());
        buffer.append(contextPath);
        buffer.append(requestURI);
        return buffer.toString();
    }
%>
<%
    int status = response.getStatus();
    String requestURI = this.getRequestURL(request);

    if(exception != null) {
        logger.error("{}, {}", status, requestURI);
        logger.error(exception.getMessage(), exception);
    }
    else {
        logger.error("{}, {}, exception: null", status, requestURI);
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<title><%=status%></title>
</head>
<body>
<!-- error: <%=requestURI%> -->
<div style="margin: 0px auto; width: 400px;">
    <h1><%=status%></h1>
    <p>System error. Please try again later!</p>
</div>
</body>
</html>
