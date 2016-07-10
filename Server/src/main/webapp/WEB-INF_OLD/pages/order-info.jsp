<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <title>Order Info page</title>
</head>
<body>

    <table>
        <thead>
            <tr>
                <td>Id</td>
                <td>Status</td>
                <td>AddressFrom</td>
                <td>AddressTo</td>
                <td>Passenger</td>
                <td>Driver</td>
                <td>Distance</td>
                <td>Price</td>
                <td>Message</td>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>${order.id}</td>
                <td>${order.orderStatus}</td>
                <td>${order.addressFrom}</td>
                <td>${order.addressTo}</td>
                <td>${order.passenger}</td>
                <td>${order.driver}</td>
                <td>${order.distance}</td>
                <td>${order.price}</td>
                <td>${order.message}</td>
            </tr>
        </tbody>
    </table>
</body>
</html>
