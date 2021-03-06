<%@ include file="include.jsp"%>

<html>
<head>
    <title>User History page</title>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>

    <script>
        function showOrderInfo(id) {
            $.ajax({
                type: "POST",
                url: "/${APP_NAME}/order/get",
                data: {
                    id : id
                },
                success: function(resp){
                    if (resp == "SUCCESS") {
                        window.location = "/${APP_NAME}/order/get";
                    } else {
                        alert(resp);
                    }
                },
                error: function(resp){
                    alert(resp);
                }

            });
        }
    </script>

    <script>
        function redirectUserInfo() {
            window.location = "/${APP_NAME}/user-info";
        }
    </script>

    <style>
        h1 {
            font-family: arial, sans-serif;
            color: darkslateblue;
        }

        table {
            font-family: arial, sans-serif;
            border-collapse: collapse;
            width: 100%;
        }

        td, th {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
        }

        tr:nth-child(even) {
            background-color: #dddddd;
        }
    </style>

</head>
<body>
    <c:set var="list" value="${orders}"/>

    <div class="container">
        <h1>USER HISTORY</h1>

        <ul>
            <p>
                <button onclick="redirectUserInfo()" style="background-color:lightgrey">
                    RETURN TO MENU</button>
            </p>
        </ul>

        <%--todo table for 10 positions with next and previous--%>
        <table>
            <tr>
                <th>id</th>
                <th>status</th>
                <th>time create</th>
                <th>address from</th>
                <th>address to</th>
                <th>distance, km</th>
                <th>price, uah</th>
                <th>show</th>
            </tr>

            <c:forEach items="${list}" var="order">
                <tr>
                    <td><c:out value="${order.id}" /></td>
                    <td><c:out value="${order.orderStatus}"/></td>
                    <td><c:out value="${order.timeCreate}"/></td>
                    <td><c:out value="${order.from.country} ${order.from.city} ${order.from.street} ${order.from.houseNum}"/></td>
                    <td><c:out value="${order.to.country} ${order.to.city} ${order.to.street} ${order.to.houseNum}"/></td>
                    <td><c:out value="${order.distance}"/></td>
                    <td><c:out value="${order.price}"/></td>
                    <td><button onclick="showOrderInfo(${order.id})">SHOW</button></td>
                </tr>
            </c:forEach>
        </table>

    </div>

</body>
</html>
