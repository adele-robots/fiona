<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>
<%@taglib uri="http://www.treelogic.com/fawna/tiles" prefix="t"%>

<fwn:OutputPanel layout="block" styleClass="cabecera">

		<fwn:Image styleClass="barra_azul_platform" value="/images/fiona/barra_azul_platform.png"></fwn:Image>
		<fwn:Link value="/adele/FIOPRE_flujoInicio-flow">
			<fwn:Image styleClass="logo_topleft" style="position:fixed;" value="/images/fiona/logo_fiona_white.png"></fwn:Image>
		</fwn:Link>
		<fwn:Form>
			<fwn:Link value="/adele/logout-flow">
				<fwn:Image styleClass="image-logout" value="/images/buttons/boton_logout.png"></fwn:Image>
			</fwn:Link>
			<fwn:Button styleClass="fio-store-button" value="" action="store">
				<fwn:UpdateActionListener property="#{treelogic.spark_criterio}" value="0">
				</fwn:UpdateActionListener>	
				<fwn:UpdateActionListener property="#{treelogic.pulsado_top}" value="TRUE">
				</fwn:UpdateActionListener>		
				<fwn:UpdateActionListener property="#{treelogic.pulsado_latest}" value="FALSE">
				</fwn:UpdateActionListener>		
			</fwn:Button>
			<fwn:Button styleClass="fio-dev-button" value="" action="develop"></fwn:Button>
			<fwn:Button styleClass="fio-usetts-button" value="" action="manageMyProfile" ></fwn:Button>
			
		</fwn:Form>
	

</fwn:OutputPanel>