<%@ page language="java"  session="false" %>
<%
final String queryString = request.getQueryString();
final String url = request.getContextPath() + "/adele/ARQ_Default_Landing-flow" + (queryString != null ? "?" + queryString : "");
response.sendRedirect(response.encodeURL(url));%>