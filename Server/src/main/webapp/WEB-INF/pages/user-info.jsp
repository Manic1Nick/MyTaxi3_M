<%@ include file="include.jsp"%>

<html>
<head>
    <title>User Info page</title>
</head>
<body>

    <c:set var="transfered" value="${user}"/>

    <div class="container">
        <h1>User Info page</h1>

        <ul>
            <li>
                <div class="column">
                    id : ${transfered.id}
                </div>
            </li>

            <li>
                <div class="column">
                    identifier : ${transfered.identifier}
                </div>
            </li>

            <li>
                <div class="column">
                    phone : ${transfered.phone}
                </div>
            </li>

            <li>
                <div class="column">
                    name : ${transfered.name}
                </div>
            </li>

            <c:choose>
                <c:when test="${transfered.identifier == 'P'}">
                    <li>
                        <div class="column">
                            address country : ${transfered.homeAddress.country}
                        </div>
                        <div class="column">
                            address city : ${transfered.homeAddress.city}
                        </div>
                        <div class="column">
                            address street : ${transfered.homeAddress.street}
                        </div>
                        <div class="column">
                            address home number : ${transfered.homeAddress.houseNum}
                        </div>
                    </li>

                    <li>Find taxi:
                        <input type="submit">
                    </li>

                </c:when>
                <c:when test="${transfered.identifier == 'D'}">
                    <li>
                        <div class="column">
                            car type : ${transfered.car.type}
                        </div>
                        <div class="column">
                            car model : ${transfered.car.model}
                        </div>
                        <div class="column">
                            car number : ${transfered.car.number}
                        </div>
                    </li>

                    <li>Find passengers:
                        <input type="submit">
                    </li>

                </c:when>
            </c:choose>

            <li>Show last order:
                <input type="submit">
            </li>
            <li>Show my history:
                <input type="submit">
            </li>
            <li>Change registration data:
                <input type="submit">
            </li>
            <li>Return to login:
                <input type="submit">
            </li>
            <li>Delete user:
                <input type="submit">
            </li>


        </ul>
    </div>

</body>
</html>
