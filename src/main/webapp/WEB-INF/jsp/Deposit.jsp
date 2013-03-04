<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Deposit Balance</title>
</head>
<body>
<form:form method="POST" action="/BankingSystem2/deposit" modelAttribute="customer">
    <table>
        <tr>
            <td><form:label path="nickname">Nickname:</form:label></td>
            <td><form:input path="nickname"/></td>
        </tr>
        <tr>
            <td><form:label path="balance">Balance you want to deposit</form:label></td>
            <td><form:input path="balance"/></td>
        </tr>
        <tr>
            <td><input type="submit" value="Submit"/></td>
        </tr>
    </table>
</form:form>
</body>
</html>