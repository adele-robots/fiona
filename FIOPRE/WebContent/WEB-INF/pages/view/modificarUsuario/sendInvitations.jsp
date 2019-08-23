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
			header="#{msg['FIONA.sendInvitations.envio.tituloPanel.valor']}">
			
			<fwn:OutputPanel styleClass="obligatorios">
				<fwn:Label
					value="#{msg['FIONA.sendInvitations.envio.leyendaFriends.valor']}"
					styleClass="etiqueta-img-requerida-azul leyendaFriends"></fwn:Label>				
			</fwn:OutputPanel>
			<%--
			<fwn:OutputPanel styleClass="obligatorios">
				<fwn:Label
					value="#{msg['FIONA.sendInvitations.envio.leyenda.valor']}"
					styleClass="etiqueta-img-requerida-azul"></fwn:Label>
				<fwn:Image value="/images/requerido.gif"></fwn:Image>
				<fwn:Label value=")" styleClass="etiqueta-img-requerida-azul"></fwn:Label>
			</fwn:OutputPanel>  --%>			
			
			
			<fwn:PanelForm id="formEnviarInvitacion" tabOrder="horizontal">
				<fwn:facet name="column1">
					<fwn:OutputPanel>
						<%-- Nombre --%>
						<fwn:OutputPanel>
							<fwn:Label styleClass="etiqueta-img-requerida-azul inputInvitacion"
								value="#{msg['FIONA.sendInvitations.envio.campoNombre.valor']}"></fwn:Label>
							<fwn:Image value="/images/requerido.gif"></fwn:Image>
						</fwn:OutputPanel>
						<fwn:Text id="nombre" label="#{msg['FIONA.sendInvitations.envio.campoNombre.valor']}" value="#{treelogic.nombre}"
							required="true" maxlength="50" size="20" styleClass="inputInvitacion"></fwn:Text>
						<%-- Apellidos --%>	
						<fwn:OutputPanel>
							<fwn:Label styleClass="etiqueta-img-requerida-azul inputInvitacion"
								value="#{msg['FIONA.sendInvitations.envio.campoApellido1.valor']}"></fwn:Label>
							<fwn:Image value="/images/requerido.gif"></fwn:Image>
						</fwn:OutputPanel>
						<fwn:Text id="apellido1" label="#{msg['FIONA.sendInvitations.envio.campoApellido1.valor']}"
							value="#{treelogic.apellido}" required="true" maxlength="100" size="20" styleClass="inputInvitacion"></fwn:Text>
						<%-- Email --%>
						<fwn:OutputPanel>
							<fwn:Label styleClass="etiqueta-img-requerida-azul inputInvitacion"
								value="#{msg['FIONA.sendInvitations.envio.campoEmail.valor']}"></fwn:Label>
							<fwn:Image value="/images/requerido.gif"></fwn:Image>
						</fwn:OutputPanel>
						<fwn:Text id="email" label="#{msg['FIONA.sendInvitations.envio.campoEmail.valor']}" value="#{treelogic.emailInvitado}"
							required="true" maxlength="100" size="23" format="email" toUpper="false" styleClass="inputInvitacion"></fwn:Text>
						<%-- Contenido opcional de la invitación --%>
						<fwn:OutputPanel>
							<fwn:Label value="#{msg['FIONA.sendInvitations.envio.campoTexto.valor']}"></fwn:Label>
						</fwn:OutputPanel>
						<fwn:TextArea id="contenidoEmail" label="Contenido" value="#{treelogic.emailBody}" cols="500" rows="8"
						styleClass="textAreaEmail"></fwn:TextArea>
						
						
					</fwn:OutputPanel>
				</fwn:facet>							
			</fwn:PanelForm>
		</fwn:Panel>


		<%-- Botonera --%>
		<fwn:OutputPanel styleClass="separador" layout="block">
			<fwn:Button action="enviar" styleClass="botonesInvitacion"
				value="#{msg['FIONA.sendInvitations.botonEnviar.valor']}"/>			
			<fwn:Button action="cancelar" styleClass="botonesInvitacion" immediate="true"
				value="#{msg['FIONA.sendInvitations.botonCancelar.valor']}" />
		</fwn:OutputPanel>
		
		</fwn:Form>


</fwn:OutputPanel>