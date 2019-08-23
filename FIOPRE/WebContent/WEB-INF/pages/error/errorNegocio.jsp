<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%><%@taglib
	uri="http://java.sun.com/jsf/core" prefix="f"%>
	<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
 
<title>Error Negocio</title>

</head>

<body >

	<f:view>
	<h:messages showDetail="true"  showSummary="false"></h:messages>
	<h:outputText 
		value="ErrorNegocio - Se ha producido un error durante la ejecución de la aplicación" />
	<h:panelGrid>
	<h:outputText  value="#{treelogic.ARCH_ERROR_INFO}" />
	<h:outputText  value="#{treelogic.ARCH_ERROR_RESUMEN}" />
	<h:outputText  value="#{treelogic.ARCH_ERROR_DETALLE}" />
	<h:outputText  value="#{treelogic.ARCH_ERROR_CODE}" />
	<h:outputText  value="#{treelogic.ARCH_ERROR_SEVERIDAD}" />
	<h:outputText  value="#{treelogic.ARCH_ERROR_TIPO}" />
	<h:outputText  value="#{treelogic.ARCH_ERROR_MODULO}" />
	</h:panelGrid>
	<fwn:TraceError></fwn:TraceError>
	</f:view>
</body>
</html>