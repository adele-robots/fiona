<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>


<fwn:Form id="myProfileEditSuccessForm" >
	<fwn:OutputPanel>
		<fwn:PanelForm tabOrder="vertical" columnClass="cero-columnas">
				<fwn:facet name="column1">
					<fwn:OutputPanel layout="block">
						<fwn:OutputPanel layout="block">
							<fwn:Label value="* " styleClass="asterisk" for="title" />
							<fwn:Label value="#{msg['FIONA_myProfile.form.title.valor']}" for="title" />
						</fwn:OutputPanel>
						<fwn:Combo id="title" label="#{msg['FIONA_myProfile.form.title.valor']}" 
								styleClass="campo" value="#{treelogic.USER_TITLE}"
								propertyFile="items.titles"
								showOptionSeleccionar="false" disabled="true"></fwn:Combo>
						
						<fwn:OutputPanel layout="block">
							<fwn:Label value="* " styleClass="asterisk" for="firstname" />
							<fwn:Label value="#{msg['FIONA_myProfile.form.name.valor']}" for="firstname" />
						</fwn:OutputPanel>
						<fwn:Text id="firstname" label="#{msg['FIONA_myProfile.form.name.valor']}" 
								styleClass="campo" value="#{treelogic.USER_FISTNAME}"								
								maxlength="100" size="40" toUpper="false" disabled="true"></fwn:Text>
						
						<fwn:OutputPanel layout="block">
							<fwn:Label value="#{msg['FIONA_myProfile.form.surname.valor']}" for="surname" />
						</fwn:OutputPanel>
						<fwn:Text id="surname" label="#{msg['FIONA_myProfile.form.surname.valor']}" 
								styleClass="campo" value="#{treelogic.USER_SURNAME}"								
								maxlength="200" size="40" toUpper="false" disabled="true"></fwn:Text>
						
						<fwn:OutputPanel layout="block">
							<fwn:Label value="* " styleClass="asterisk" for="email" />
							<fwn:Label value="#{msg['FIONA_myProfile.form.email.valor']}" for="email" />
						</fwn:OutputPanel>
						<fwn:Text id="email" label="#{msg['FIONA_myProfile.form.email.valor']}" 
								styleClass="campo" value="#{treelogic.USER_MAIL}"
								disabled="true" 
								maxlength="100" size="40" format="email" toUpper="false" autocomplete="off"></fwn:Text>
						
						<fwn:OutputPanel layout="block">
							<fwn:Label value="* " styleClass="asterisk" for="nickname" />
							<fwn:Label value="#{msg['FIONA_myProfile.form.nickname.valor']}" for="nickname" />
						</fwn:OutputPanel>
						<fwn:Text id="nickname" label="#{msg['FIONA_myProfile.form.nickname.valor']}" 
								styleClass="campo" value="#{treelogic.USER_NICKNAME}"
								disabled="true"
								maxlength="100" size="40" toUpper="false" autocomplete="off"></fwn:Text>						
						
									
						<fwn:OutputPanel layout="block"  style="margin-top:20px;">
							<fwn:Label value="#{msg['FIONA_myProfile.form.accounttype.valor']}" for="accounttype"/>
						</fwn:OutputPanel>
						<fwn:OutputPanel id="panelCombosAccount">						
							<fwn:Combo id="accounttype" required="true" label="#{msg['FIONA_myProfile.form.accounttype.valor']}" style="margin-top:20px;"
									CA="029" CS="060" contextKey="FIONEG024020" contextValue="FIONEG024010"
									orderedItems="false" value="#{treelogic.USER_ACCOUNTTYPE_ID}" 
									showOptionSeleccionar="false" disabled="true" ajaxChangeListener="ChangeAccountTypeComboListener" event="onchange"
									reRender="panelCombosAccount"></fwn:Combo>
							<fwn:Combo id="paymentPeriod" required="false" label="" style="margin-top:20px;"									
										orderedItems="false" value="#{treelogic.USER_ACCOUNTTYPE_METHOD}"
										propertyFile="items.payment_periods"
										showOptionSeleccionar="false" disabled="true" rendered="#{!empty(treelogic.USER_ACCOUNTTYPE_ID) and treelogic.USER_ACCOUNTTYPE_ID != '1'}"></fwn:Combo>
						</fwn:OutputPanel>
						<fwn:Label></fwn:Label>						
						<fwn:OutputPanel id="panelTxtCredito" layout="block" styleClass="textoCredito">
							<fwn:OutputText id="txtCredito" value="#{msg['FIONA_myProfile.dialogoCuenta.textoCredito.valor']}" styleClass="textoCredito">
							</fwn:OutputText>							
						</fwn:OutputPanel>
						<fwn:Label id="label_acount_credit" for="account_credit" value="#{msg['FIONA_myProfile.form.accountCredit.valor']}"></fwn:Label>
						<fwn:OutputText id="account_credit" value="#{treelogic.account_credit}"></fwn:OutputText>																											 
					</fwn:OutputPanel>
				</fwn:facet>
			</fwn:PanelForm>
	</fwn:OutputPanel>	
	<fwn:OutputPanel id="botonesDialog" layout="block" style="margin-top:20px; text-align:center;">
		<fwn:Button action="continue" value="Continue"  styleClass="botonesInvitacion botonesInvitacionAzul" style="float:none; font-size:16pt;" />
		<fwn:Button action="manageMyProfile" value="Cancel"  styleClass="botonesInvitacion botonesInvitacionAzul" immediate="true" style="float:none; font-size:16pt;" />
		<%--<fwn:Button action="cancel" value="Cancel"  styleClass="botonesInvitacion botonesInvitacionAzul" immediate="true" style="float:none; font-size:16pt;" /> --%>
	</fwn:OutputPanel>	
</fwn:Form>
<fwn:OutputText><script type="text/javascript"><!--
(function($){
	$(function() {
		$("input:first", "form#myProfileEditSuccessForm:first").focus();
	});
})($jq);
//--></script></fwn:OutputText>
