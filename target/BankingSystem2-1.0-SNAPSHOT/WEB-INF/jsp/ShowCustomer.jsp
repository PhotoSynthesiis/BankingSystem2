<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>Show Customers</title>
</head>
<body>
customer name : ${customer.nickname} <br>
customer dateOfBirth : ${customer.dateOfBirth} <br>
balance : ${customer.balance}<br>
emailAddress : ${customer.emailAddress}<br>

<c:url value="/welcome" var="welcome"/>
1. <a href="${welcome}">back</a>
</body>
</html>