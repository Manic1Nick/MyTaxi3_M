<%@ include file="include.jsp" %>

<html>
<head>
    <title>Order Info page</title>
</head>
<body>

<c:set var="transfered" value="${order}"/>

<div class="container">
    <h1>Order Info page</h1>

    <ul>
        <li>
            <div class="column">
                id : ${transfered.id}
            </div>
        </li>

        <li>
            <div class="column">
                orderStatus : ${transfered.orderStatus}
            </div>
        </li>

        <li>
            <div class="column">
                addressFrom : ${transfered.from.country},
                ${transfered.from.city},
                ${transfered.from.street},
                ${transfered.from.houseNum}

            </div>
        </li>

        <li>
            <div class="column">
                addressTo : ${transfered.to.country},
                ${transfered.to.city},
                ${transfered.to.street},
                ${transfered.to.houseNum}
            </div>
        </li>

        <li>
            <div class="column">
                passenger : ${transfered.passenger.name}
            </div>
        </li>

        <li>
            <div class="column">
                driver : ${transfered.driver.name},
                ${transfered.driver.phone}
            </div>
        </li>

        <li>
            <div class="column">
                distance : ${transfered.distance}
            </div>
        </li>

        <li>
            <div class="column">
                price : ${transfered.price}
            </div>
        </li>

        <li>
            <div class="column">
                message : ${transfered.message}
            </div>
        </li>

    </ul>
</div>

</body>
</html>
