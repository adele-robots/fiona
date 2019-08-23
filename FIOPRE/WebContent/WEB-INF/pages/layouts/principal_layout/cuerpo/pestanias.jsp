<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>
<%@taglib uri="http://www.treelogic.com/fawna/tiles" prefix="t"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>

<t:importAttribute scope="request" />
<fwn:tabPanel selectedTab="#{requestScope.pesActiva}">
	<security:authorize ifAnyGranted="ROLE_USER">
		<fwn:tab label="#{msg['FIONA.pestaña.usuarios']}" id="usuarios"
			action="flowRedirect:FIOPRE_flujoInicio-flow">
	
			<t:insertAttribute flush="false" name="contenido"></t:insertAttribute>
		</fwn:tab>
	</security:authorize>
</fwn:tabPanel>