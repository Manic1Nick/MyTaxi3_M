<%--jsp -> servlet -> Tomcat -> html --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login Page</title>
</head>
<body>
  <h1>Login form</h1>
  <%--localhost:8080/app/main--%>
  <%--localhost:8080/app/login--%>

  <form action="login" method="post">
    <label>Input phone</label>>
    <input type="text" name="phone">
    <label>Input pass</label>>
    <input type="password" name="pass">
    <input type="submit">
  </form>

</body>
</html>
