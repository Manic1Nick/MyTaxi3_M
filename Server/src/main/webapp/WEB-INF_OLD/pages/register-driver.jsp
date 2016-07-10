<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register Driver Page</title>
</head>
<body>

<h1>Register Driver form</h1>

    <form action="register-driver" method="post">
        <label>Input phone</label>
        <input type="text" name="phone">
        <label>Input pass</label>
        <input type="password" name="pass">
        <label>Input name</label>
        <input type="text" name="name">
        <label>Input type car</label>
        <input type="text" name="carType">
        <label>Input model car</label>
        <input type="text" name="carModel">
        <label>Input number car</label>
        <input type="text" name="carNumber">
        <input type="submit">
    </form>


</body>
</html>