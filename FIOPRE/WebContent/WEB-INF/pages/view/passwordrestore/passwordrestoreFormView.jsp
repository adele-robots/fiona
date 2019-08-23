<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>
<%@taglib uri="http://www.treelogic.com/fawna/jsf/Security" prefix="fwnSec"%>

<fwn:OutputPanel layout="block" styleClass="tituloPagina">
	<fwn:OutputText value="#{msg['FIONA_passwordrestore.form.caption.valor']}"></fwn:OutputText>
</fwn:OutputPanel>
<fwn:Messages showDetail="true" showSummary="false"
		ajaxRendered="false" style="color:red; padding-bottom:50px;"></fwn:Messages>
<br/>
<br/>
<br/>
<fwn:Form id="passwordrestoreForm" 
		formValidator="com.adelerobots.web.fiopre.validators.PasswordRestoreFormValidator">
<fwn:PanelForm tabOrder="vertical" columnClass="cero-columnas">
	<fwn:facet name="column1">
		<fwn:OutputPanel layout="block">
			
			<fwn:OutputPanel layout="block">
				<fwn:Label value="* " styleClass="asterisk" for="email" />
				<fwn:Label value="#{msg['FIONA_passwordrestore.form.email.valor']}" for="email" />
			</fwn:OutputPanel>
			<fwn:Text id="email" label="#{msg['FIONA_passwordrestore.form.email.valor']}" 
					styleClass="campo" value="#{treelogic.USER_MAIL}"
					required="true" 
					maxlength="100" size="40" format="email" toUpper="false" autocomplete="off"></fwn:Text>			
			
			<fwn:OutputText></fwn:OutputText>
			<fwn:OutputPanel layout="block" styleClass="required_field_reminder">
				<fwn:OutputText value="* " styleClass="asterisk" ></fwn:OutputText>
				<fwn:OutputText value="required field" ></fwn:OutputText>
			</fwn:OutputPanel>
			
		</fwn:OutputPanel>
	</fwn:facet>
</fwn:PanelForm>
<fwn:OutputPanel layout="block" styleClass="div-clean"></fwn:OutputPanel>
<fwn:OutputPanel layout="block" styleClass="separador" style="margin-top:20px; text-align:center;">
	<fwn:Button action="submit" value="" styleClass="fio-passwordrestore-button" style="float:none; font-size:16pt; margin:0 10px;" />
	<fwn:Button action="back" value="" styleClass="fio-back-button" style="float:none; font-size:16pt; margin:0 10px;" immediate="true" />
</fwn:OutputPanel>
</fwn:Form>