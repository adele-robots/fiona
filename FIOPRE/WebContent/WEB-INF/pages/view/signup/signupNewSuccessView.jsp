<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>


<fwn:Form id="signupSuccessForm" styleClass="aloneMessage">
	<fwn:OutputPanel layout="block" style="font-size: 20pt;">
		<fwn:OutputText value="#{msg['FIONA_signup.success.message.valor.0']}"></fwn:OutputText>
	</fwn:OutputPanel>
	<fwn:OutputPanel layout="block" style="font-size: 20pt;">
		<fwn:OutputText value="#{msg['FIONA_signup.success.message.valor.1']}"></fwn:OutputText>
	</fwn:OutputPanel>
	<fwn:OutputPanel layout="block" style="font-size: 20pt;color:red;" rendered="#{!empty(treelogic.cancelled)}">
		<fwn:OutputText value="#{msg['FIONA_signup.success.cancelledMessage.valor']}"></fwn:OutputText>
	</fwn:OutputPanel>
	<fwn:OutputPanel layout="block" style="text-align: center;">
		<fwn:OutputText id="errorPaypalCBA" rendered="#{!empty(treelogic.errorPaypalCBA)}" style="color:red; padding-bottom:50px;" value="#{msg['FIONA_signup.createbillingagreement.error.valor']}"></fwn:OutputText>			
		<fwn:OutputPanel layout="block" styleClass="div-clean"></fwn:OutputPanel>
	</fwn:OutputPanel>
	<fwn:OutputPanel layout="block" style="text-align: center;">
		<fwn:OutputText id="errorPaypalDRT" rendered="#{!empty(treelogic.errorPaypalDRTIni)}" style="color:red; padding-bottom:50px;" value="#{msg['FIONA_signup.doreferenceini.error.valor']}"></fwn:OutputText>			
		<fwn:OutputPanel layout="block" styleClass="div-clean"></fwn:OutputPanel>
	</fwn:OutputPanel>
	
	<fwn:OutputPanel layout="block" styleClass="div-clean"></fwn:OutputPanel>
	<fwn:OutputPanel layout="block" styleClass="separador" style="margin-top:20px; text-align:center;">
		<fwn:Button action="back" value="" styleClass="fio-back-button" style="float:none; font-size:16pt;" immediate="true" />
	</fwn:OutputPanel>
</fwn:Form>
<fwn:OutputText><script type="text/javascript"><!--
(function($){
	$(function() {
		$("input:first", "form#signupSuccessForm:first").focus();
	});
})($jq);
//--></script></fwn:OutputText>
