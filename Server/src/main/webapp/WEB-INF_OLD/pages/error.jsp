<%@ page import="javax.security.auth.login.LoginException" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>Error Page</title>
</head>
<body>

<%--1 version--%>
<%--expression language--%>
    <% LoginException exception = (LoginException) request.getAttribute("error");%>
    <h1><%= request.getAttribute("error")%></h1>

<%--2 version--%>
    <p>${error.message}</p>


</body>
</html>
