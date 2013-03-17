<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: twer
  Date: 3/3/13
  Time: 10:47 PM
  To change this dao use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Withdraw Balance</title>
</head>
<body>
<form:form method="POST" action="/BankingSystem2/withdraw" modelAttribute="customer">
    <table>
        <tr>
            <td>Nickname:</td>
            <td><form:input path="nickname" /></td>
        </tr>
        <tr>
            <td>Balance you want to withdraw</td>
            <td><form:input path="balance" /></td>
        </tr>
        <tr>
            <td><input type="submit" value="Submit"/></td>
        </tr>
    </table>
</form:form>


</body>
</html>