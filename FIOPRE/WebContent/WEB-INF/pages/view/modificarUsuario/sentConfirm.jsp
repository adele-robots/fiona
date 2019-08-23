<%@page import="com.treelogic.fawna.presentacion.core.utilidades.FawnaPropertyConfiguration"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>

<%-- Contenido del formulario --%>
<fwn:OutputPanel>
	
	<fwn:Form>

		<fwn:Panel
			header="#{msg['FIONA.sendInvitations.envioCorrecto.tituloPanel.valor']}">
			
			<fwn:OutputPanel styleClass="obligatorios">
				<fwn:Label
					value="#{msg['FIONA.sendInvitations.envio.leyendaEnvioCorrecto.valor']}"
					styleClass="etiqueta-img-requerida-azul leyendaFriends"></fwn:Label>				
			</fwn:OutputPanel>
			
		</fwn:Panel>


		<%-- Botonera --%>
		<fwn:OutputPanel styleClass="separador" layout="block">						
			<fwn:Button action="ok" styleClass="botonesInvitacion" immediate="true"
				value="#{msg['FIONA.sendInvitations.botonOK.valor']}" />
		</fwn:OutputPanel>
		
		</fwn:Form>


</fwn:OutputPanel>