<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.text.SimpleDateFormat"%><%@page import="java.text.DateFormat"%><%@page import="org.springframework.security.web.util.UrlUtils"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>
<fwn:View><html lang="${request.locale.language}${empty request.locale.country?'':'-'}${request.locale.country}">
<head>
	<title>.:: Fiona ::.:: Sparking Together ::.</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	
	<meta property="og:title" content=".:: Fiona ::.:: Sparking Together ::." />
	<meta property="og:type" content="website" />
	<meta property="og:url" content="<%=UrlUtils.buildFullRequestUrl(request.getScheme(),request.getServerName(), request.getServerPort(), "", null)%>" />
	<meta property="og:image" content="<%=UrlUtils.buildFullRequestUrl(request.getScheme(),request.getServerName(), request.getServerPort(), request.getContextPath()+"/images/120px-world.png", null)%>" />
	<meta property="og:site_name" content="The community for the creation of the artificial mind" /> 
	
	<script type="text/javascript" src="${request.contextPath}/js/css_browser_selector.js"></script> <%-- http://rafael.adm.br/css_browser_selector/ --%>
	
	<%-- WORKARROUND: https://community.jboss.org/thread/151838
	     Make working your own jQuery plugins in a RichFaces instance --%>
	<%-- Save RichFaces jQuery if any--%><script type="text/javascript">var $rfjquery; try{$rfjquery=jQuery.noConflict();}catch(E){}</script>
	<%-- Load my jQuery --%><script type="text/javascript" src="${request.contextPath}/js/jquery.js"></script>
	<%-- Do stuff with my jQuery --%><script type="text/javascript">
	var $jq=jQuery.noConflict(); /*All your plugins code goes here*/
	</script>
	<%-- Load here other plugins dependent on my jQuery --%>
	<%-- restore RichFaces jQuery if loaded--%><script type="text/javascript">if (typeof($rfjquery)!="undefined"){var jQuery=$rfjquery;}</script>
	
	<link rel="shortcut icon" type="image/x-icon" href="${request.contextPath}/favicon.ico" />
	<%--<link rel="shortcut icon" href="${request.contextPath}/favicon.png" />--%>
	
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/estilos.css" />
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/estilosBuscador.css" />
	<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/componentes/button.css" />
	
	<script type="text/javascript">
	(function($){
		$(function() {
			$("input[name=j_username]:first", "form.formbox:first").focus();
		});
	})($jq);
	</script>
</head>

<body class="login-page no-js<fwn:OutputText rendered="#{not empty(treelogic.securityErrorMsg)}" value=" has-errors"/>">
	<script type="text/javascript">document.body.className = document.body.className.replace('no-js', 'js');</script>
	
	<div id="contenedora">
	<div id="contenido">
	
	
	
	
		<fwn:Image styleClass="formlogo" value="/images/iconos/icono_fiona.png" alt="Fiona" title="Fiona"/>
		
		<fwn:OutputPanel layout="block" styleClass="errors" rendered="#{not empty(treelogic.securityErrorMsg)}">
			<p class="error-<fwn:OutputText value="#{treelogic.securityErrorKey}" />" style="font-weight:bold;font-size:1.3em"><fwn:OutputText value="#{msg['FWN_Login.mensaje.help_1']}" /></p>
			<p style="margin-top:8px; font-weight:italic;font-size: 1em;padding: 2% 0% 2% 0;"><fwn:OutputText value="#{msg['FWN_Login.mensaje.help']}" /></p>
		</fwn:OutputPanel>
		
		<form class="formbox" action="${request.contextPath}/j_security_check" method="post" <%--autocomplete="off"--%>>
			
			<fieldset>
			<dl>
				<dt><label for="usuario"><fwn:OutputText value="#{msg['FWN_Login.usuario.valor']}" /></label></dt>
				<dd><input type="text" name="j_username" id="username" value="" size="30" class="textbox"/></dd>
			</dl>
			<dl>
				<dt><label for="password"><fwn:OutputText value="#{msg['FWN_Login.password.valor']}" /></label></dt>
				<dd><input type="password" name="j_password" id="password" value="" size="30" class="textbox"/></dd>
			</dl>
			<dl>
				<dt>
					<input class="fio-enter-button" type="submit" name="entrar" id="entrar" value="" />
				</dt>
				<dd></dd>
			</dl>
			</fieldset>
			<%--
			<fieldset>
				<fwn:OutputPanel layout="block" styleClass="separador_portada">
					<fwn:OutputPanel styleClass="button">
						<input class="fio-clear-button" type="reset" name="borrar" id="borrar" 
							value="" />
					</fwn:OutputPanel>
					<fwn:OutputPanel styleClass="button">
						<input class="fio-enter-button" type="submit" name="entrar" id="entrar" 
							value="" />
					</fwn:OutputPanel>
				</fwn:OutputPanel>
			</fieldset>
			--%>
			
		</form>
		
		
		<fwn:Form styleClass="formbox-signup">
			<fieldset>
				<fwn:Button value="" styleClass="fio-register-button" action="signup"/>
			</fieldset>
		</fwn:Form>
		
		<fwn:Form styleClass="formbox-signup" style="margin-top: 264px;">
			<fieldset>
				<fwn:Button value="" styleClass="fio-passwordrestore-button" action="passwordrestore"/>
			</fieldset>
		</fwn:Form>		
		
		
		
		<fwn:OutputPanel layout="block" styleClass="div-clean"></fwn:OutputPanel>
		
		<fwn:OutputPanel layout="block" styleClass="contenidoPie">
			<fwn:OutputPanel layout="block" styleClass="foot">
				<span class="foot-nocolor-text">&copy;Copyright <%= (new java.text.SimpleDateFormat("yyyy")).format(new java.util.Date()) %></span>
			</fwn:OutputPanel>
		</fwn:OutputPanel>
	
	
	
	
	</div><!-- /fin contenido --> 
	</div><!-- /fin contenedora -->
	
</body>
</html></fwn:View>