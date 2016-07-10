<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <title>User Info page</title>
</head>
<body>

    <table>
        <thead>
            <tr>
                <td>Id</td>
                <td>Type</td>
                <td>Name</td>
                <td>Phone</td>
                <td>HomeAddress</td>
                <td>Car</td>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>${user.id}</td>
                <td>${user.identifier}</td>
                <td>${user.name}</td>
                <td>${user.phone}</td>
                <td>${user.homeAddress}</td>
                <td>${user.car}</td>
            </tr>
        </tbody>
    </table>
</body>
</html>
