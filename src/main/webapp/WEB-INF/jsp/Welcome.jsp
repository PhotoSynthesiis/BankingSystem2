<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Banking System</title>
</head>
<body>
Welcome to the Banking System! <br/>
<c:url value="/addCustomer" var="addCustomer"/>
1. <a href="${addCustomer}">add Customer</a>

<c:url value="/withdraw" var="withdraw"/>
2. <a href="${withdraw}">withdraw</a>
</body>
</html>