<%@page import="org.springframework.security.web.util.UrlUtils"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>
<%@taglib uri="http://www.treelogic.com/fawna/tiles" prefix="t"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<fwn:View><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<t:importAttribute scope="request" />
<html lang="${request.locale.language}${empty request.locale.country?'':'-'}${request.locale.country}">
<head>
	<title><t:insertAttribute name="title" flush="false" /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	
	<meta property="og:title" content=".:: Fiona ::.:: Sparking Together ::. Sign-up" />
	<meta property="og:type" content="website" />
	<meta property="og:url" content="<%=UrlUtils.buildFullRequestUrl(request.getScheme(),request.getServerName(), request.getServerPort(), "", null)%>" />
	<meta property="og:image" content="<%=UrlUtils.buildFullRequestUrl(request.getScheme(),request.getServerName(), request.getServerPort(), request.getContextPath()+"/images/120px-world.png", null)%>" />
	<meta property="og:site_name" content="The community for the creation of the artificial mind" /> 
	
	<script type="text/javascript" src="${request.contextPath}/js/lightBox.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/css_browser_selector.js"></script> <%-- http://rafael.adm.br/css_browser_selector/ --%>
	
	<%-- WORKARROUND: https://community.jboss.org/thread/151838
	     Make working your own jQuery plugins in a RichFaces instance --%>
	<%-- Save RichFaces jQuery if any--%><script type="text/javascript">var $rfjquery; try{$rfjquery=jQuery.noConflict();}catch(E){}</script>
	<%-- Load my jQuery --%><script type="text/javascript" src="${request.contextPath}/js/jquery.js"></script>
	<%-- Do stuff with my jQuery --%><script type="text/javascript">
	var $jq=jQuery.noConflict(); /*All your plugins code goes here*/
	</script>
	<%-- Load here other plugins dependent on my jQuery --%>
	<script type="text/javascript" src="${request.contextPath}/js/jquery.shiftenter.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/jquery.frame.animation.js"></script>
	<script type="text/javascript" src="${request.contextPath}/js/jquery.filestyle.js"></script>
	<%-- restore RichFaces jQuery if loaded--%><script type="text/javascript">if (typeof($rfjquery)!="undefined"){var jQuery=$rfjquery;}</script>
	
	<link rel="shortcut icon" type="image/x-icon" href="${request.contextPath}/favicon.ico" />
	<%--<link rel="shortcut icon" href="${request.contextPath}/favicon.png" />--%>
	
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/fawnaFiona.css" />
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/fiona/estilos.css" />
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/jquery.shiftenter.css" />
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/lightBox.css" />
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/estilosBuscador.css" />


	<%-- Attribute used to insert stuff before head tag in a specified tile page --%>
	<t:insertAttribute flush="false" name="fwn_head"></t:insertAttribute>
</head>

<body>
	<%-- cortinilla  --%>
	<fwn:OutputPanel id="lightbox_panel" styleClass="lightbox_noVisible" layout="block">
		<fwn:OutputPanel styleClass="lightbox-fondo" layout="block" id="lightbox_fondo"></fwn:OutputPanel>
		<fwn:OutputPanel styleClass="lightbox-contenedor" id="lightbox_contenedor" layout="block">
			<fwn:Image value="/images/status/ajax-loader.gif" id="lightBox_imagen"></fwn:Image>
		</fwn:OutputPanel>
	</fwn:OutputPanel>
	<%-- Fin cortinilla  --%>

	<%-- Contenido de la pagina  --%>
	<fwn:OutputPanel layout="block" styleClass="contenido">
		<t:insertAttribute flush="false" name="cabecera"></t:insertAttribute>
		<t:insertAttribute flush="false" name="contenido"></t:insertAttribute>
	</fwn:OutputPanel>
	
	
	
	<%-- Attribute used to insert stuff before body tag in a specified tile page --%>
	<t:insertAttribute flush="false" name="fwn_footer"></t:insertAttribute>
</body>
</html></fwn:View>