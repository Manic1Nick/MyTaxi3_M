<%@ page import="javax.security.auth.login.LoginException" %>
<%--
  Created by IntelliJ IDEA.
  User: serhii
  Date: 02.07.16
  Time: 11:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Error Page</title>
</head>
<body>
<%--expression language--%>
  <% LoginException exception = (LoginException) request.getAttribute("error");%>
  <h1><%= exception.getClass().getName()%></h1>

  <p>${error.message}</p><%--req.getAttribute(error) error.getMessage--%>
</body>
</html>
