<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Customers</title>
</head>
<body>
<form:form method="POST" action="/BankingSystem2/addCustomer" modelAttribute="customer">
    <table>
        <tr>
            <td><form:label path="nickname">Nickname</form:label></td>
            <td><form:input path="nickname"/></td>
        </tr>
        <tr>
            <td><form:label path="dateOfBirth">dateOfBirth</form:label></td>
            <td><form:input path="dateOfBirth"/></td>
        </tr>
        <tr>
            <td><form:label path="balance">balance</form:label></td>
            <td><form:input path="balance"/></td>
        </tr>
        <tr>
            <td><form:label path="emailAddress">emailAddress</form:label></td>
            <td><form:input path="emailAddress"/></td>
        </tr>
        <tr>
            <td><input type="submit" value="Submit"/></td>
        </tr>
    </table>
</form:form>
</body>
</html>