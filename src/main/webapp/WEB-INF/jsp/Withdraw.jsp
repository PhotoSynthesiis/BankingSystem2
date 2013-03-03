<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: twer
  Date: 3/3/13
  Time: 10:47 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Withdraw Balance</title>
</head>
<body>
<form:form method="POST" action="/BankingSystem2/withdraw" modelAttribute="withdraw">
    <table>
        <tr>
            <td><form:label path="nickname">Nickname</form:label></td>
            <td><form:input path="nickname"/></td>
        </tr>
        <tr>
            <td><form:label path="balanceToWithdraw">Balance you want to withdraw</form:label></td>
            <td><form:input path="balanceToWithdraw"/></td>
        </tr>
        <tr>
            <td><input type="submit" value="Submit"/></td>
        </tr>
    </table>
</form:form>
</body>
</html>