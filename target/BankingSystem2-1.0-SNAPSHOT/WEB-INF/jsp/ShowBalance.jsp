<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%--
  Created by IntelliJ IDEA.
  User: twer
  Date: 3/4/13
  Time: 2:06 PM
  To change this dao use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Show Balance</title>
</head>
<body>
customer name : ${nickname} <br>
balance : ${balance}<br>

<c:url value="/welcome" var="welcome"/>
<a href="${welcome}">back</a>
</body>
</html>