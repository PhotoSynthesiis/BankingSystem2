<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Nickname Invalid Page</title>
</head>
<body>
nickname is invalid</br>
<c:url value="/welcome" var="welcome"/>
<a href="${welcome}">back</a>
</body>
</html>