<%@page import="com.treelogic.fawna.presentacion.core.utilidades.FawnaPropertyConfiguration"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>

<%-- Contenido del listado --%>
<fwn:OutputPanel layout="block" styleClass="contenido">

	
	<%-- Formulario de búsqueda y listado --%>
	<fwn:Form>
		<fwn:Hidden id="spark_id" rendered="true" value="#{treelogic.spark_id}"></fwn:Hidden>
		<fwn:Messages showDetail="true" showSummary="false"
				ajaxRendered="false"></fwn:Messages>
				
		<fwn:Panel
			header="#{msg['FIONA.store.integratorBuyer.sparkBuyTrial.cabeceraPanel.valor']}"
			collapsable="false">
			
			
				<fwn:OutputPanel>
					<fwn:Label styleClass="inputInvitacion"
						value="#{msg['FIONA.store.integratorBuyer.sparkBuyTrial.confirmacion.valor']}"></fwn:Label>							
				</fwn:OutputPanel>				
						
					
			
		</fwn:Panel>		
		
        
		<fwn:OutputPanel id="botonera" styleClass="separador">
        	<fwn:OutputPanel layout="block">					
					<fwn:Button action="ok" styleClass="botonesInvitacion" 
					value="#{msg['FWN_Comun.botonOK']}"  immediate="true"/>
			</fwn:OutputPanel>
        </fwn:OutputPanel>
		
	</fwn:Form>
	
	


</fwn:OutputPanel>