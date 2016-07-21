<%@ include file="include.jsp"%>

<html>
<head>
    <title>Register Passenger Page</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
</head>
<body>

<div class="container">
    <h1>Register Passenger form</h1>

    <form id="register" action="register-passenger" method="post">
        <ul>
            <li>Input phone:
                <input id="phone" name="phone"  type="text">
            </li>
            <li>Input password:
                <input id="pass" name="pass" type="password">
            </li>
            <li>Input name:
                <input id="name" name="name" type="text">
            </li>
            <li>Input country:
                <input id="country" name="country" type="text">
            </li>
            <li>Input city:
                <input id="city" name="city" type="text">
            </li>
            <li>Input street:
                <input id="street" name="street" type="text">
            </li>
            <li>Input house number:
                <input id="houseNum" name="houseNum" type="text">
            </li>
            <li>
                <input type="submit" value="Register">
            </li>
        </ul>
    </form>

    <div id="responseText"></div>

</div>

<script>

    $(document).on("submit", "#register", function(event) {
        var $form = $(this);

        $.post($form.attr("action"),
                $form.serialize(),
                function(response) {
                    $('#responseText').html(response);

                });

        event.preventDefault(); // Important! Prevents submitting the form.



    });





</script>

</body>
</html>