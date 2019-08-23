<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%><%@taglib
	uri="http://java.sun.com/jsf/core" prefix="f"%>
	<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="../css/estilos.css" />
		<link rel="stylesheet" type="text/css" href="../css/estilosBuscador.css" />
		<title>Error</title>
	</head>

	<body >
		
		<fwn:OutputPanel>
			<fwn:Label
				value="#{msg['FWN_Comun.error.valor']}"></fwn:Label>				
		</fwn:OutputPanel>
	
	
		<fwn:Form>
			<fwn:OutputPanel layout="block">
					<fwn:Button action="back" value="" styleClass="fio-back-button" />
			</fwn:OutputPanel>
		</fwn:Form>	
	</body>
</html>