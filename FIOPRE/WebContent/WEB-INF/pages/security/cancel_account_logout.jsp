<%@ page language="java" session="false"%>
<%@page import="org.springframework.security.web.util.UrlUtils"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>

<%
	final String queryString = request.getQueryString();
	final String url = request.getContextPath() + "/j_security_logout"
			+ (queryString != null ? "?" + queryString : "");
	//response.sendRedirect(response.encodeURL(url));
%>
<%-- Si quieres vista, quitar el sendRedirect por eso --%>
<fwn:View>
	<html>
		<head>
			<meta http-equiv="Refresh" content="30; URL=<%=url%>">
			<link rel="shortcut icon" type="image/x-icon" href="${request.contextPath}/favicon.ico" />
			<link rel="stylesheet" type="text/css"
				href="${request.contextPath}/css/estilos.css" />
			<link rel="stylesheet" type="text/css"
				href="${request.contextPath}/css/estilosBuscador.css" />
		</head>
		<body class="cancellation-page">
			<div id="contenedora">
				<div id="contenido"><!-- Aqui el codigo que quieres me que vea durante lo que dura el timeout del Refresh -->		
					<fwn:Image styleClass="formlogo" value="/images/iconos/icono_fiona.png" alt="Fiona" title="Fiona"/>			
					<fwn:OutputPanel layout="block" id="divMensajeCancelacion" styleClass="contenedorTextoCancelAccount">
						<fwn:OutputText id="textoMensajeCancelacion" value="#{msg['FWN_CancelAccountLogout.mensajeFinal.valor']}"
						styleClass="textoCancelAccount">
						</fwn:OutputText>
					</fwn:OutputPanel>
				</div> <%-- Fin contenido --%>
			</div> <%-- Fin contenedora --%>
		</body>
	</html>
</fwn:View>
