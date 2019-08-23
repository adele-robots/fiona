<%@ page language="java"  session="false" %>
<%
final String queryString = request.getQueryString();
final String url = request.getContextPath() + "/j_security_logout" + (queryString != null ? "?" + queryString : "");
response.sendRedirect(response.encodeURL(url));%>
<%-- Si quieres vista, quitar el sendRedirect por eso
<html>
<head>
	<meta http-equiv="Refresh" content="1000; URL=<%=url%>"> 
</head>
<body>
	<!-- Aqui el codigo que quieres me que vea durante lo que dura el timeout del Refresh -->
</body>
</html>
--%>