<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>
<%@taglib uri="http://www.treelogic.com/fawna/tiles" prefix="t"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>

<fwn:View>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		
		<script type="text/javascript" src="../js/lightBox.js"></script>
		<script type="text/javascript" src="../js/jwplayer.js"></script>
		<script type="text/javascript" src="../js/swfobject.js"></script>		
		
		<link rel="shortcut icon" href="../favicon.png">
		
		<link rel="stylesheet" type="text/css" href="../css/fawnaFiona.css" />
		<link rel="stylesheet" type="text/css" href="../css/fiona/estilos.css" />
		<link rel="stylesheet" type="text/css" href="../css/lightBox.css" />
		<link rel="stylesheet" type="text/css" href="../css/estilosBuscador.css" />
	
		<t:importAttribute scope="request" />
		<title><t:insertAttribute name="title" flush="false" /></title>
	</head>
	
	<body>
		<%-- cortinilla  --%>
		<fwn:OutputPanel id="lightbox_panel" styleClass="lightbox_noVisible"
			layout="block">
			<fwn:OutputPanel styleClass="lightbox-fondo" layout="block"
				id="lightbox_fondo"></fwn:OutputPanel>
			<fwn:OutputPanel styleClass="lightbox-contenedor"
				id="lightbox_contenedor" layout="block">
				<fwn:Image value="/images/status/ajax-loader.gif"
					id="lightBox_imagen"></fwn:Image>
			</fwn:OutputPanel>
		</fwn:OutputPanel>
		<%-- Fin cortinilla  --%>

		<%-- Contenido de la pagina  --%>
		<fwn:OutputPanel layout="block" styleClass="contenido">
			
			<%-- Cabecera de la pagina  --%>
			<t:insertAttribute flush="false" name="cabecera"></t:insertAttribute>
	
			<%-- Pestanias de la pagina --%>
			<t:insertAttribute flush="false" name="pestanias"></t:insertAttribute>
			
			<%-- Pie de la pagina --%>
			<t:insertAttribute flush="false" name="pie"></t:insertAttribute>
		</fwn:OutputPanel>
	</body>
</html>

</fwn:View>