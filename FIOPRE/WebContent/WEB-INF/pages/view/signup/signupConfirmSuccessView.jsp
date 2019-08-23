<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>


<fwn:Form id="signupConfirmSuccessForm" styleClass="aloneMessage">
	<fwn:OutputPanel layout="block" style="font-size: 20pt;">
		<fwn:OutputText value="#{msg['FIONA_signup_confirm.success.message.valor.0']}"></fwn:OutputText>
	</fwn:OutputPanel>
	<fwn:OutputPanel layout="block" style="font-size: 20pt;">
		<fwn:OutputText value="#{msg['FIONA_signup_confirm.success.message.valor.1']}"></fwn:OutputText>
	</fwn:OutputPanel>
	
	<fwn:OutputPanel layout="block" styleClass="div-clean"></fwn:OutputPanel>
	<fwn:OutputPanel layout="block" styleClass="separador" style="margin-top:20px; text-align:center;">
		<fwn:Button action="back" value="" styleClass="fio-back-button" style="float:none; font-size:16pt;" immediate="true" />
	</fwn:OutputPanel>
</fwn:Form>
<fwn:OutputText><script type="text/javascript"><!--
(function($){
	$(function() {
		$("input:first", "form#signupConfirmSuccessForm:first").focus();
	});
})($jq);
//--></script></fwn:OutputText>
