<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>





<fwn:OutputPanel layout="block" styleClass="tituloPagina">
	<fwn:OutputText value="#{msg['FIONA_registrarUsuario.formulario.mensaje.valor']}"></fwn:OutputText>
</fwn:OutputPanel>
<fwn:Messages showDetail="true" showSummary="false"
		ajaxRendered="false" style="color:red; padding-bottom:50px;"></fwn:Messages>
<br>
<br>
<br>
<fwn:OutputPanel layout="block" style="margin-left:25%;">
<fwn:Form formValidator="InfoPassValidador">				
	<fwn:PanelForm tabOrder="vertical">
				<fwn:facet name="column1">
					<fwn:OutputPanel>
						<fwn:OutputPanel>
							<fwn:Label value="* " styleClass="asterisk"></fwn:Label>
							<fwn:Label value="Name" 
								onclick="this.form.elements[0].focus();"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:Text id="nombre" label="Name" 
							styleClass="campo" value="#{treelogic.nombre}"
							required="true" maxlength="100" size="40" toUpper="false"></fwn:Text>
						
						<fwn:OutputPanel>
							<fwn:Label value="Last Name"
								onclick="this.form.elements[1].focus();"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:Text id="apellido" label="Last Name" 
							styleClass="campo" value="#{treelogic.apellido}"
							required="false" maxlength="100" size="40" toUpper="false"></fwn:Text>
						
						<fwn:OutputPanel>
							<fwn:Label value="* " styleClass="asterisk" ></fwn:Label>
							<fwn:Label value="email"
								onclick="this.form.elements[2].focus();"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:Text id="email" label="email" 
							styleClass="campo" value="#{treelogic.email}"
							required="true" maxlength="100" size="40" format="email" toUpper="false"></fwn:Text>
						
						
						<fwn:OutputPanel>
							<fwn:Label value="* " styleClass="asterisk"></fwn:Label>
							<fwn:Label value="Username" 
								onclick="this.form.elements[3].focus();"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:Text id="username" label="Username" 
							styleClass="campo" value="#{treelogic.username}"
							required="true" maxlength="100" size="40" toUpper="false"></fwn:Text>
						
						
						<fwn:OutputPanel>
							<fwn:Label value="* " styleClass="asterisk"></fwn:Label>
							<fwn:Label value="Password"
								onclick="this.form.elements[4].focus();"></fwn:Label>
								<br>
							<fwn:Label value="(Must contain letters and numbers, 6-12 characters)" 
								style="font-size:10px;" onclick="this.form.elements[3].focus();"></fwn:Label>
															
						</fwn:OutputPanel>
						<fwn:TextSecret id="password" label="Password"
							 styleClass="campo_pw"	value="#{treelogic.password}" required="true" maxlength="50" size="40">
						</fwn:TextSecret>		
						
						<fwn:OutputPanel>
							<fwn:Label value="* " styleClass="asterisk" ></fwn:Label>
							<fwn:Label value="Confirm password"
								onclick="this.form.elements[5].focus();"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:TextSecret id="repPassword" label="Confirm password" styleClass="campo_pw"
								value="#{treelogic.password}" required="true" maxlength="50" size="40">
						</fwn:TextSecret><!--			
							
											
						<fwn:OutputPanel>
							<fwn:Label value="#{msg['FIONA_registrarUsuario.formulario.entidad.valor']}"
								onclick="this.form.elements[6].focus();"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:Text id="entidad" label="#{msg['FIONA_registrarUsuario.formulario.entidad.valor']}" 
							styleClass="campo" value="#{treelogic.entidad}"
							required="false" maxlength="50" size="40" format="numerico" numberType="integer" toUpper="false"></fwn:Text>
						-->
						
						<fwn:OutputPanel style="margin-bottom:5px;">
							<fwn:Label value="Account type"
								onclick="this.form.elements[7].focus();"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:Combo id="cuenta" required="false" label="Account type"
							value="#{treelogic.cuenta}" propertyFile="items.cuentas" disabled="true">
						</fwn:Combo>												
						
						
						<fwn:OutputPanel>
							<fwn:Label value="Credit card number"
								onclick="this.form.elements[8].focus();"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:Text id="tarjeta" label="Credit card number"
							styleClass="campo" value="#{treelogic.tarjeta}" 
							required="false" maxlength="50" size="40" format="numerico" numberType="integer" toUpper="false" disabled="true"></fwn:Text>
						
						<fwn:OutputPanel>
							<fwn:Label value="Expiration date"
								onclick="this.form.elements[9].focus();"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:Text id="caducidad" label="Expiration date" 
							styleClass="campo" value="#{treelogic.caducidad}"
							required="false" maxlength="50" size="40" format="fecha" toUpper="false" disabled="true"></fwn:Text>
					</fwn:OutputPanel>
				</fwn:facet>							
			</fwn:PanelForm>	
			
<fwn:OutputPanel layout="block" styleClass="required_field_reminder">
	<fwn:OutputText value="* " styleClass="asterisk" ></fwn:OutputText>
	<fwn:OutputText value="required field" ></fwn:OutputText>
		
</fwn:OutputPanel>
			
			
			<fwn:OutputPanel styleClass="separador" layout="block">
				<fwn:Button action="registrar" value="" style="position:relative; right:150px;" styleClass="fio-register-button" />
			  	<fwn:Link styleClass="register_back_link" value="../../">
					<fwn:Image value="/images/buttons/boton_back_blue.png"></fwn:Image>
				</fwn:Link>
			</fwn:OutputPanel>	
</fwn:Form> 

</fwn:OutputPanel>