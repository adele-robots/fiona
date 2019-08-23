<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>
<%@taglib uri="http://www.treelogic.com/fawna/jsf/Security" prefix="fwnSec"%>

<fwn:OutputPanel layout="block" styleClass="tituloPagina" >
	<fwn:OutputText value="Periodic subscription agreement with Adele Robots through your Paypal account"></fwn:OutputText>
</fwn:OutputPanel>
<fwn:Messages showDetail="true" showSummary="false"
		ajaxRendered="false" style="color:red; padding-bottom:50px;"></fwn:Messages>
<br/>
<br/>
<br/>
<fwn:OutputPanel id="panelWrapperPaypal">
	<fwn:Form id="signupAgreementForm" 
			formValidator="com.adelerobots.web.fiopre.validators.PasswordRestoreFormValidator">
	
		<fwn:OutputPanel id="panelTextoPaypal" layout="block">
			<fwn:OutputPanel style="text-align: justify;">		
				<fwn:OutputText rendered="#{empty(treelogic.errorPaypalSEC)}">
					Clicking on this button you will be redirected to PayPal's website for signing the subscription agreement.
				</fwn:OutputText>
			</fwn:OutputPanel>
			<fwn:OutputPanel layout="block" style="text-align: center;">
				<fwn:OutputText id="errorPaypal" rendered="#{!empty(treelogic.errorPaypalSEC)}" style="color:red; padding-bottom:50px;" value="#{msg['FIONA_signup.setexpresscheckout.error.valor']}"></fwn:OutputText>			
				<fwn:OutputPanel layout="block" styleClass="div-clean"></fwn:OutputPanel>
			</fwn:OutputPanel>
		</fwn:OutputPanel>
	
	<fwn:OutputPanel layout="block" styleClass="div-clean"></fwn:OutputPanel>
	<fwn:OutputPanel layout="block" styleClass="separador" style="margin-top:20px; text-align:center;">
		<fwn:AjaxButton ajaxonclicklistener="SetExpressCheckoutFlujoInicioListener" styleClass="paypalECButton" value="" style="float:none; font-size:16pt; margin:0 10px;" 
		image="https://www.paypal.com/en_US/i/btn/btn_xpressCheckout.gif" reRender="panelWrapperPaypal" rendered="#{empty(treelogic.errorPaypalSEC)}"/>
		<%--<img src="https://www.paypal.com/en_US/i/btn/btn_xpressCheckout.gif" align="left" style="margin-right:7px;"> --%>	
	</fwn:OutputPanel>
	</fwn:Form>
</fwn:OutputPanel>