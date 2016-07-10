<%@ include file="include.jsp"%>

<html>
<head>
    <title>Register Driver Page</title>
</head>
<body>

<div class="container">
    <h1>Register Driver form</h1>

    <form method="post" action="register-passenger">
        <ul>
            <li>Input phone:
                <input name="phone" type="text">
            </li>
            <li>Input password:
                <input name="pass" type="password">
            </li>
            <li>Input name:
                <input name="name" type="text">
            </li>
            <li>Input car type:
                <input name="carType" type="text">
            </li>
            <li>Input car model:
                <input name="carModel" type="text">
            </li>
            <li>Input car number:
                <input name="carNumber" type="text">
            </li>

            <li>Submit:
                <input type="submit">
            </li>
        </ul>
    </form>


</div>

</body>
</html>