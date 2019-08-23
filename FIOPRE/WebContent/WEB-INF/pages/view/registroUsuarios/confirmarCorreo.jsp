<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>


<fwn:Form styleClass="aloneMessage">
	<fwn:OutputPanel style="font-size: 20pt;">
		<fwn:OutputText value="#{msg['FIONA_registrarUsuario.formulario.registrooka.valor']}"></fwn:OutputText>		
	</fwn:OutputPanel>						
<br>
	<fwn:OutputPanel style="font-size: 20pt;">
		<fwn:OutputText value="#{msg['FIONA_registrarUsuario.formulario.registrookb.valor']}"></fwn:OutputText>		
	</fwn:OutputPanel>	
						
		<fwn:OutputPanel styleClass="separador" layout="block">
				<fwn:Link value="../../">
					<fwn:Image style="position:relative; top:18px;"value="/images/buttons/boton_back_blue.png"></fwn:Image>
				</fwn:Link>
			</fwn:OutputPanel>	
</fwn:Form>			