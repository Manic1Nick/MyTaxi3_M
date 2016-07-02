<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <title>User Info Page</title>
</head>
<body>

  <h1>User Data</h1>

  <table>
    <thead>
      <tr>
        <td>Id</td>
        <td>Type</td>
        <td>Name</td>
        <td>Phone</td>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>${user.id}</td>
        <td>${user.identifier}</td>
        <td>${user.name}</td>
        <td>${user.phone}</td>
      </tr>
    </tbody>
  </table>
</body>
</html>
