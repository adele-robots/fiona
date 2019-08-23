<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%><%@taglib
	uri="http://java.sun.com/jsf/core" prefix="f"%>
	<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="../css/estilos.css" />
		<link rel="stylesheet" type="text/css" href="../css/estilosBuscador.css" />
		<title>Error Ifrt</title>
	</head>

	<body >
	<f:view>
	<div id="contenedora">
		<fwn:OutputPanel layout="block" styleClass="cabecera error">
			<fwn:Image value="/images/logo_fawna.png"></fwn:Image>
		</fwn:OutputPanel>
		<div class="contenido error">	
			<fwn:OutputPanel layout="block" styleClass="tituloPagina error">
				<fwn:OutputText value="Se ha producido un error"></fwn:OutputText>
			</fwn:OutputPanel>
			<div style="clear:both"></div>
			<fwn:OutputPanel layout="block" styleClass="cuerpo">
				<h:messages showDetail="true"  showSummary="false"></h:messages>
				<fwn:OutputPanel layout="block" styleClass="">
					<fwn:OutputText value="ErrorIfrt - Se ha producido un error durante la ejecución de la aplicación"></fwn:OutputText>
				</fwn:OutputPanel>	
				
				<fwn:OutputPanel layout="block" styleClass="textoError">
					<h:outputText  value="#{treelogic.ARCH_ERROR_INFO}" />
					<h:outputText  value="#{treelogic.ARCH_ERROR_RESUMEN}" />
					<h:outputText  value="#{treelogic.ARCH_ERROR_DETALLE}" />
					<h:outputText  value="#{treelogic.ARCH_ERROR_CODE}" />
					<h:outputText  value="#{treelogic.ARCH_ERROR_SEVERIDAD}" />
					<h:outputText  value="#{treelogic.ARCH_ERROR_TIPO}" />
					<h:outputText  value="#{treelogic.ARCH_ERROR_MODULO}" />
				</fwn:OutputPanel>
				
				<fwn:TraceError></fwn:TraceError>
			</fwn:OutputPanel>	
			<div style="clear:both"></div>
		</div>
		<div style="clear:both"></div>
	</div>
	<div class="contenidoPie">
		<fwn:OutputPanel layout="block" styleClass="foot error">
			<fwn:OutputText styleClass="foot-nocolor-text" value="Copyright 2011"></fwn:OutputText>
			<fwn:Link styleClass="foot-color-text" title="#{msg['FWN_Comun.que.valor']}">
				<fwn:OutputText value="#{msg['FWN_Comun.que.valor']}"></fwn:OutputText>
			</fwn:Link>
			&nbsp;|
			<fwn:Link styleClass="foot-color-text" title="#{msg['FWN_Comun.como.valor']}">
				<fwn:OutputText value="#{msg['FWN_Comun.como.valor']}"></fwn:OutputText>
			</fwn:Link>
		</fwn:OutputPanel>
	</div>
	</f:view>
	</body>
</html>