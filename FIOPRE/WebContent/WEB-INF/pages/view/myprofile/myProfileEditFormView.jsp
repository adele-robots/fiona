<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>
<%@taglib uri="http://www.treelogic.com/fawna/jsf/Security" prefix="fwnSec"%>

<fwn:Form id="myProfileEditForm" 
	formValidator="com.adelerobots.web.fiopre.validators.MyProfileEditFormValidator">
	<fwn:Messages showDetail="true" showSummary="false" style="color:red; padding-bottom:20px;"></fwn:Messages>
	
	<fwn:OutputText>
	</fwn:OutputText>
	<fwn:OutputPanel layout="block" style="text-align:center;">
		<fwn:RadioSingle for="radioTipoEntidad" index="0"
				showLabel="true" />
		<fwn:RadioSingle for="radioTipoEntidad" index="1"
				showLabel="true" />
		<fwn:Radio value="#{treelogic.USER_FLAGENTITY}" event="onchange"
			ajaxChangeListener="MostrarCamposEntidadListener" layout="spread"
			id="radioTipoEntidad" reRender="panelAgreementWrap, panelEntityWrap">
			<fwn:SelectItem itemValue="0" id="individual" itemLabel="#{msg['FIONA_myProfile.form.individual.valor']}"/>
			<fwn:SelectItem itemValue="1" id="company" itemLabel="#{msg['FIONA_myProfile.form.company.valor']}"/>
		</fwn:Radio>		
	</fwn:OutputPanel>
			
	<fwn:OutputText></fwn:OutputText>
	<fwn:OutputPanel id="panelAgreementWrap">
		<fwn:OutputPanel id="panelAgreement" rendered="#{treelogic.USER_FLAGENTITY eq '1'}" style="margin:auto auto 15px;width:40%;">
			<fwn:PanelForm tabOrder="vertical" columnClass="cero-columnas">
				<fwn:facet name="column1">
					<fwn:OutputPanel layout="block">
						<fwn:OutputPanel layout="block" style="margin-top:15px;">
							<fwn:BooleanCheck id="agree" label="agreement" 
								styleClass="campo" style="vertical-align:middle;" value="#{treelogic.agree}" 
								immediate="true"/>
						</fwn:OutputPanel>
						<fwn:OutputPanel layout="block" style="margin-top:15px;width:70%;">
							<fwn:Label for="agree" value="#{msg['FIONA_myProfile.form.agreement1.valor']}"
								style="font-size:22px;font-weight:normal;vertical-align:middle;"
								styleClass="campo"/>
							<%--<fwn:Link value="../docs/agreement.pdf">
								<fwn:OutputText value="#{msg['FIONA_myProfile.form.agreement2.valor']}"
									style="font-size:22px;font-weight:normal;vertical-align:middle;"
									styleClass="campo">
								</fwn:OutputText>
							</fwn:Link>--%>
							<fwn:Label for="agree" value="#{msg['FIONA_myProfile.form.agreement2.valor']}"
								style="font-size:22px;font-weight:normal;vertical-align:middle;"
								styleClass="campo"/>
							<fwn:Label for="agree" value="#{msg['FIONA_myProfile.form.agreement3.valor']}"
								style="font-size:22px;font-weight:normal;vertical-align:middle;"
								styleClass="campo"/>
						</fwn:OutputPanel>
					</fwn:OutputPanel>
				</fwn:facet>
			</fwn:PanelForm>
		</fwn:OutputPanel>
	</fwn:OutputPanel>
			
	<fwn:OutputText></fwn:OutputText>
	<fwn:OutputPanel id="panelUserWrap">
		<fwn:OutputPanel id="panelUser">
			<fwn:PanelForm tabOrder="vertical" columnClass="cero-columnas">
				<fwn:facet name="column1">
					<fwn:OutputPanel layout="block">
						<fwn:OutputPanel layout="block">
							<fwn:Label value="* " styleClass="asterisk" for="title" />
							<fwn:Label value="#{msg['FIONA_myProfile.form.title.valor']}" for="title" />
						</fwn:OutputPanel>
						<fwn:Combo id="title" label="#{msg['FIONA_myProfile.form.title.valor']}" 
								styleClass="campo" value="#{treelogic.USER_TITLE}"
								required="true" propertyFile="items.titles"
								showOptionSeleccionar="false"></fwn:Combo>
						
						<fwn:OutputPanel layout="block">
							<fwn:Label value="* " styleClass="asterisk" for="firstname" />
							<fwn:Label value="#{msg['FIONA_myProfile.form.name.valor']}" for="firstname" />
						</fwn:OutputPanel>
						<fwn:Text id="firstname" label="#{msg['FIONA_myProfile.form.name.valor']}" 
								styleClass="campo" value="#{treelogic.USER_FISTNAME}"
								required="true" 
								maxlength="100" size="40" toUpper="false"></fwn:Text>
						
						<fwn:OutputPanel layout="block">
							<fwn:Label value="#{msg['FIONA_myProfile.form.surname.valor']}" for="surname" />
						</fwn:OutputPanel>
						<fwn:Text id="surname" label="#{msg['FIONA_myProfile.form.surname.valor']}" 
								styleClass="campo" value="#{treelogic.USER_SURNAME}"
								required="false" 
								maxlength="200" size="40" toUpper="false"></fwn:Text>
						
						<fwn:OutputPanel layout="block">
							<fwn:Label value="* " styleClass="asterisk" for="email" />
							<fwn:Label value="#{msg['FIONA_myProfile.form.email.valor']}" for="email" />
						</fwn:OutputPanel>
						<fwn:Text id="email" label="#{msg['FIONA_myProfile.form.email.valor']}" 
								styleClass="campo" value="#{treelogic.USER_MAIL}"
								required="true" disabled="true" 
								maxlength="100" size="40" format="email" toUpper="false" autocomplete="off"></fwn:Text>
						
						<fwn:OutputPanel layout="block">
							<fwn:Label value="* " styleClass="asterisk" for="nickname" />
							<fwn:Label value="#{msg['FIONA_myProfile.form.nickname.valor']}" for="nickname" />
						</fwn:OutputPanel>
						<fwn:Text id="nickname" label="#{msg['FIONA_myProfile.form.nickname.valor']}" 
								styleClass="campo" value="#{treelogic.USER_NICKNAME}"
								required="true" 
								maxlength="100" size="40" toUpper="false" autocomplete="off"></fwn:Text>
						
						<fwn:OutputPanel layout="block" style="margin-top:15px;">
							<fwn:Label value=" " for="changeMyPasswd" />
						</fwn:OutputPanel>
						<fwn:OutputPanel layout="block" style="margin-top:15px;">
							<fwn:BooleanCheck id="changeMyPasswd" label="#{msg['FIONA_myProfile.form.changemypassword.valor']}" 
									styleClass="campo" style="vertical-align:middle;" value="#{treelogic.CHANGEMYPASSWD}" 
									event="onchange" ajaxChangeListener="com.adelerobots.web.fiopre.listeners.MyProfileEnableDisableChangeMyPasswdAjaxChangeListener"
									reRender="password,password_lbls,newPassword1,newPassword1_lbls,newPassword2,newPassword2_lbls" 
									oncomplete="try{document.getElementById('myProfileEditForm:password').focus();}catch(E){}" 
									immediate="true" disabled="#{treelogic.FLAG_CERT == '1'}" />
							<fwn:Label value="#{msg['FIONA_myProfile.form.changemypassword.valor']}" for="changeMyPasswd" 
									styleClass="campo" style="font-size:18px;font-weight:normal;vertical-align:middle;" />
						</fwn:OutputPanel>
						
						<fwn:OutputPanel layout="block" id="password_lbls">
							<fwn:OutputPanel layout="block" id="password_lblreq" rendered="#{treelogic.CHANGEMYPASSWD}">
								<fwn:Label value="* " styleClass="asterisk" for="password" />
								<fwn:Label value="#{msg['FIONA_myProfile.form.password.valor']}" for="password" />
							</fwn:OutputPanel>
							<fwn:OutputPanel layout="block" id="password_lbl" rendered="#{not treelogic.CHANGEMYPASSWD}">
								<fwn:Label value="#{msg['FIONA_myProfile.form.password.valor']}" for="password" />
							</fwn:OutputPanel>
						</fwn:OutputPanel>
						<fwn:TextSecret id="password" label="#{msg['FIONA_myProfile.form.password.valor']}" 
								styleClass="campo" value="#{treelogic.USER_PASSWORD}" 
								required="#{treelogic.CHANGEMYPASSWD}" disabled="#{not treelogic.CHANGEMYPASSWD}" 
								maxlength="50" size="40" autocomplete="off"></fwn:TextSecret>
						
						<fwn:OutputPanel layout="block" id="newPassword1_lbls">
							<fwn:OutputPanel layout="block" id="newPassword1_lblreq" rendered="#{treelogic.CHANGEMYPASSWD}">
								<fwn:Label value="* " styleClass="asterisk" for="newPassword1" />
								<fwn:Label value="#{msg['FIONA_myProfile.form.newpassword1.valor']}" for="newPassword1" />
							</fwn:OutputPanel>
							<fwn:OutputPanel layout="block" id="newPassword1_lbl" rendered="#{not treelogic.CHANGEMYPASSWD}">
								<fwn:Label value="#{msg['FIONA_myProfile.form.newpassword1.valor']}" for="newPassword1" />
							</fwn:OutputPanel>
							<fwn:Label value="#{msg['FIONA_myProfile.form.password.tip1']}" 
									style="font-size:10px;" />
						</fwn:OutputPanel>
						<fwn:TextSecret id="newPassword1" label="#{msg['FIONA_myProfile.form.newpassword1.valor']}" 
								styleClass="campo" value="#{treelogic.USER_NEWPASSWORD1}" 
								required="#{treelogic.CHANGEMYPASSWD}" disabled="#{not treelogic.CHANGEMYPASSWD}" 
								maxlength="50" size="40" autocomplete="off"></fwn:TextSecret>
						
						<fwn:OutputPanel layout="block" id="newPassword2_lbls">
							<fwn:OutputPanel layout="block" id="newPassword2_lblreq" rendered="#{treelogic.CHANGEMYPASSWD}">
								<fwn:Label value="* " styleClass="asterisk" for="newPassword2" />
								<fwn:Label value="#{msg['FIONA_myProfile.form.newpassword2.valor']}" for="newPassword2" />
							</fwn:OutputPanel>
							<fwn:OutputPanel layout="block" id="newPassword2_lbl" rendered="#{not treelogic.CHANGEMYPASSWD}">
								<fwn:Label value="#{msg['FIONA_myProfile.form.newpassword2.valor']}" for="newPassword2" />
							</fwn:OutputPanel>
						</fwn:OutputPanel>
						<fwn:TextSecret id="newPassword2" label="#{msg['FIONA_myProfile.form.newpassword2.valor']}" 
								styleClass="campo" value="#{treelogic.USER_NEWPASSWORD2}" 
								required="#{treelogic.CHANGEMYPASSWD}" disabled="#{not treelogic.CHANGEMYPASSWD}" 
								maxlength="50" size="40" autocomplete="off"></fwn:TextSecret>
						<%--
						<fwn:OutputPanel layout="block" style="margin-top:20px;">
							<fwn:Label value="#{msg['FIONA_myProfile.form.entity.valor']}" for="entity" />
						</fwn:OutputPanel>
						<fwn:Text id="entity" label="#{msg['FIONA_myProfile.form.entity.valor']}"  style="margin-top:20px;"
								styleClass="campo" value="#{treelogic.USER_ENTITY_ID}"
								required="false" disabled="true" 
								maxlength="11" size="40" format="numerico" numberType="integer" toUpper="false"></fwn:Text>
						--%>
						<fwn:OutputPanel layout="block"  style="margin-top:20px;">
							<fwn:Label value="#{msg['FIONA_myProfile.form.accounttype.valor']}" for="accounttype"/>
						</fwn:OutputPanel>
						<fwn:OutputPanel id="panelCombosAccount">						
							<fwn:Combo id="accounttype" required="true" label="#{msg['FIONA_myProfile.form.accounttype.valor']}" style="margin-top:20px;"
									CA="029" CS="060" contextKey="FIONEG024020" contextValue="FIONEG024010"
									orderedItems="false" value="#{treelogic.USER_ACCOUNTTYPE_ID}" 
									showOptionSeleccionar="false" disabled="false" ajaxChangeListener="ChangeAccountTypeComboListener" event="onchange"
									reRender="panelCombosAccount"></fwn:Combo>
							<fwn:Combo id="paymentPeriod" required="false" label="" style="margin-top:20px;"									
										orderedItems="false" value="#{treelogic.USER_ACCOUNTTYPE_METHOD}"
										propertyFile="items.payment_periods"
										showOptionSeleccionar="false" disabled="false" rendered="#{!empty(treelogic.USER_ACCOUNTTYPE_ID) and treelogic.USER_ACCOUNTTYPE_ID != '1'}"></fwn:Combo>
						</fwn:OutputPanel>
						<fwn:OutputPanel layout="block" style="margin-top:20px;">							
						</fwn:OutputPanel>
						<fwn:Link value="#{msg['FIONA_signup.form.account_link.valor']}" target="_blank">
							<fwn:OutputText value="#{msg['FIONA_signup.form.account_link.alias.valor']}"></fwn:OutputText>
						</fwn:Link>
						<%--
						<fwn:OutputPanel layout="block">
							<fwn:Label value="* " styleClass="asterisk" for="firstname" />
							<fwn:Label value="#{msg['FIONA_myProfile.form.name.valor']}" for="firstname" />
						</fwn:OutputPanel>
						<fwn:Text id="firstname" label="#{msg['FIONA_myProfile.form.name.valor']}" 
								styleClass="campo" value="#{treelogic.USER_FISTNAME}"
								required="true" 
								maxlength="100" size="40" toUpper="false"></fwn:Text>
						
						<fwn:OutputPanel layout="block">
							<fwn:Label value="#{msg['FIONA_myProfile.form.creditcardexpiration.valor']}" for="creditcard_expiration" />
						</fwn:OutputPanel>
						<fwn:Text id="creditcard_expiration" label="#{msg['FIONA_myProfile.form.creditcardexpiration.valor']}" 
								styleClass="campo formato-numerico" value="#{treelogic.USER_CREDITCARD_EXPIRATION}"
								required="true" disabled="true" 
								maxlength="10" size="40" toUpper="false"></fwn:Text>
			
						<fwn:OutputPanel layout="block" style="margin-top:20px;">
							<fwn:Label value="* " styleClass="asterisk" for="captchaCode" />
							<fwn:Label value="#{msg['FIONA_myProfile.form.captcha.valor']}" for="captchaCode" />
						</fwn:OutputPanel>
						<fwn:OutputPanel layout="block" style="margin-top:20px;">
							<fwnSec:captcha id="captchaCode" labelError="#{msg['FIONA_myProfile.form.captcha.valor']}" 
									type="simple" />
						</fwn:OutputPanel>
						 --%>						 
					</fwn:OutputPanel>
				</fwn:facet>
			</fwn:PanelForm>
		</fwn:OutputPanel>
	</fwn:OutputPanel>
			
	<fwn:OutputText></fwn:OutputText>
	<fwn:OutputPanel id="panelEntityWrap">
		<fwn:OutputPanel id="panelEntity" rendered="#{treelogic.USER_FLAGENTITY eq '1'}">
			<fwn:PanelForm tabOrder="vertical" columnClass="cero-columnas">
				<fwn:facet name="column1">
					<fwn:OutputPanel layout="block">
						<fwn:OutputPanel layout="block">
							<fwn:Label value="* " styleClass="asterisk" for="organizationtype" />
							<fwn:Label value="#{msg['FIONA_myProfile.form.organizationtype.valor']}" for="organizationtype"/>
						</fwn:OutputPanel>
						<fwn:Combo id="organizationtype" label="#{msg['FIONA_myProfile.form.organizationtype.valor']}"
							CA="029" CS="061" contextKey="FIONEG026020" contextValue="FIONEG026010"
							orderedItems="false" value="#{treelogic.USER_ORGANIZATIONTYPE_ID}" 
							showOptionSeleccionar="false" required="#{treelogic.USER_FLAGENTITY eq '1'}"></fwn:Combo>
							
						<fwn:OutputPanel layout="block">
							<fwn:Label value="* " styleClass="asterisk" for="legalEntityName" />
							<fwn:Label value="#{msg['FIONA_myProfile.form.legalEntityName.valor']}" for="legalEntityName" />
						</fwn:OutputPanel>
						<fwn:Text id="legalEntityName" label="#{msg['FIONA_myProfile.form.legalEntityName.valor']}" 
								styleClass="campo" value="#{treelogic.USER_LEGALENTITYNAME}"
								maxlength="50" size="40" toUpper="false" autocomplete="off"
								required="#{treelogic.USER_FLAGENTITY eq '1'}"></fwn:Text>
							
						<fwn:OutputPanel layout="block">
							<fwn:Label value="* " styleClass="asterisk" for="website" />
							<fwn:Label value="#{msg['FIONA_myProfile.form.website.valor']}" for="website" />
						</fwn:OutputPanel>
						<fwn:Text id="website" label="#{msg['FIONA_myProfile.form.website.valor']}" 
								styleClass="campo" value="#{treelogic.USER_WEBSITE}"
								maxlength="100" size="40" toUpper="false" autocomplete="off"
								required="#{treelogic.USER_FLAGENTITY eq '1'}"></fwn:Text>
							
						<fwn:OutputPanel layout="block">
							<fwn:Label value="* " styleClass="asterisk" for="workEmail" />
							<fwn:Label value="#{msg['FIONA_myProfile.form.workEmail.valor']}" for="workEmail" />
						</fwn:OutputPanel>
						<fwn:Text id="workEmail" label="#{msg['FIONA_myProfile.form.workEmail.valor']}" 
								format="email" styleClass="campo" value="#{treelogic.USER_WORKEMAIL}"
								maxlength="100" size="40" toUpper="false" autocomplete="off"
								required="#{treelogic.USER_FLAGENTITY eq '1'}"></fwn:Text>
						
						<fwn:OutputPanel layout="block">
							<fwn:Label value="#{msg['FIONA_myProfile.form.headQuartersPhone.valor']}" for="countryCode" />
						</fwn:OutputPanel>
						<fwn:OutputPanel layout="block">
							<fwn:OutputPanel layout="block" style="float:left;">
								<fwn:OutputPanel layout="block">
									<fwn:Text id="countryCode" label="#{msg['FIONA_myProfile.form.countryCode.valor']}" 
										styleClass="campo" value="#{treelogic.USER_COUNTRYCODE}"
										maxlength="5" size="5" toUpper="false" autocomplete="off" style="margin-right:10px;"
										required="#{treelogic.USER_FLAGENTITY eq '1'}"></fwn:Text>
								</fwn:OutputPanel>
								<fwn:OutputPanel layout="block">
									<fwn:Label value="* " styleClass="asterisk" for="countryCode" style="font-size:10pt;font-weight:normal;"/>
									<fwn:Label value="#{msg['FIONA_myProfile.form.countryCode.valor']}" 
										style="font-size:10pt;margin-right:10px;" for="countryCode"/>
								</fwn:OutputPanel>
							</fwn:OutputPanel>
							<fwn:OutputPanel layout="block" style="float:left;">
								<fwn:OutputPanel layout="block">
									<fwn:Text id="phoneNumber" label="#{msg['FIONA_myProfile.form.phoneNumber.valor']}" 
										styleClass="campo" value="#{treelogic.USER_PHONENUMBER}"
										maxlength="20" size="20" toUpper="false" autocomplete="off" style="margin-right:10px;"
										required="#{treelogic.USER_FLAGENTITY eq '1'}"></fwn:Text>
								</fwn:OutputPanel>
								<fwn:OutputPanel layout="block">
									<fwn:Label value="* " styleClass="asterisk" for="phoneNumber" style="font-size:10pt;font-weight:normal;"/>
									<fwn:Label value="#{msg['FIONA_myProfile.form.phoneNumber.valor']}" 
										style="font-size:10pt;margin-right:10px;" for="phoneNumber"/>
								</fwn:OutputPanel>
							</fwn:OutputPanel>
							<fwn:OutputPanel layout="block" style="float:left;">
								<fwn:OutputPanel layout="block">
									<fwn:Text id="ext" label="#{msg['FIONA_myProfile.form.ext.valor']}" 
										styleClass="campo" value="#{treelogic.USER_EXT}"
										maxlength="5" size="5" toUpper="false" autocomplete="off"></fwn:Text>
								</fwn:OutputPanel>
								<fwn:OutputPanel layout="block">
									<fwn:Label value="#{msg['FIONA_myProfile.form.ext.valor']}" 
										style="font-size:10pt;" for="ext"/>
								</fwn:OutputPanel>
							</fwn:OutputPanel>
						</fwn:OutputPanel>
					</fwn:OutputPanel>
				</fwn:facet>
			</fwn:PanelForm>
		</fwn:OutputPanel>
	</fwn:OutputPanel>
	
	<fwn:OutputText></fwn:OutputText>
	<fwn:OutputPanel layout="block" styleClass="required_field_reminder">
		<fwn:OutputText value="* " styleClass="asterisk" ></fwn:OutputText>
		<fwn:OutputText value="required field" ></fwn:OutputText>
	</fwn:OutputPanel>
			
	<fwn:OutputPanel layout="block" styleClass="div-clean"></fwn:OutputPanel>
	<fwn:OutputPanel layout="block" style="margin-top:20px; text-align:center;">
		<fwn:Button action="submit" value="" styleClass="fio-save-button" style="float:none; font-size:16pt;" />
		<fwn:Button action="back" value="" styleClass="fio-back-button" style="float:none; font-size:16pt;" immediate="true" />
		<%-- Abrir diálogo para confirmar la cancelación de la cuenta --%>
		<%--			
		<fwn:AjaxButton			
			reRender="alertCancelarCuenta" id="abrirAlertCancelarCuenta"
			ajaxonclicklistener="AbrirCerrarDialogoCancelarCuenta"
			styleClass="fio-cancel-account-button" immediate="true"
			style="float:none; font-size:16pt;" >
		</fwn:AjaxButton> --%>
		<fwn:AjaxButton			
			reRender="alertCancelarCuenta" id="abrirAlertCancelarCuenta"
			ajaxonclicklistener="AbrirCerrarDialogoCancelarCuenta"
			immediate="true" styleClass="fio-cancelAccount-button"
			style="float:right; font-size:16pt;" value="#{msg['FIONA_myProfile.form.botonCancelar.valor']}">
		</fwn:AjaxButton>
	</fwn:OutputPanel>
	
	<fwn:OutputPanel layout="block" id="modalAlertCancel" style="font-size:12pt;" styleClass="modalAlertCancel">
		<fwn:ModalAlert id="alertCancelarCuenta"
			alertTitle="#{msg['FIONA_myProfile.cancelarCuenta.tituloPanel']}"
			title="#{msg['FIONA_myProfile.cancelarCuenta.confirmar.valor']}"
			type="confirm">
			<fwn:facet name="buttons">
				<fwn:OutputPanel>
					<fwn:Button action="cancelOK" value="#{msg['FWN_Comun.botonOK']}"
						styleClass="botonesInvitacion" />
					<fwn:AjaxButton value="#{msg['FWN_Comun.botonNO']}"
						reRender="alertCancelarCuenta" id="cerrarCancelar"
						ajaxonclicklistener="AbrirCerrarDialogoCancelarCuenta"
						styleClass="botonesInvitacion">
					</fwn:AjaxButton>
				</fwn:OutputPanel>
			</fwn:facet>
		</fwn:ModalAlert>
	</fwn:OutputPanel>
	
</fwn:Form>
<fwn:OutputText><script type="text/javascript"><!--
(function($){
	$(function() {
		$("input:first", "form#myProfileEditForm:first").focus();
	});
})($jq);
//--></script></fwn:OutputText>
