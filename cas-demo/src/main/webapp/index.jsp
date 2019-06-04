<%@ page import="org.jasig.cas.client.authentication.AttributePrincipal" %>
<%@ page import="org.jasig.cas.client.util.AssertionHolder" %>
<%@ page import="org.jasig.cas.client.validation.Assertion" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: jzb
  Date: 19-6-4
  Time: 下午7:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    final Assertion assertion = AssertionHolder.getAssertion();
    final AttributePrincipal principal = assertion.getPrincipal();
%>
<html>
<head>
    <title>$Title$</title>
</head>
<body>
<p>
    principal.name: <%=principal.getName()%>
</p>
<ul>
    <%
        for (Map.Entry<String, Object> entry : principal.getAttributes().entrySet()) {
    %>
    <li>
        <%=entry.getKey()%>:<%=entry.getValue()%>
    </li>
    <%
        }
    %>
</ul>
</body>
</html>
