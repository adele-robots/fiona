<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>


<fwn:Form id="signupFailureForm" styleClass="aloneMessage">
	<fwn:OutputPanel layout="block" style="font-size: 20pt;">
		<fwn:OutputText value="#{msg['FIONA_signup.failure.message.valor.0']}"></fwn:OutputText>
	</fwn:OutputPanel>
	
	<fwn:OutputPanel layout="block" styleClass="div-clean"></fwn:OutputPanel>
</fwn:Form>
<fwn:OutputText><script type="text/javascript"><!--
(function($){
	$(function() {
		$("input:first", "form#signupFailureForm:first").focus();
	});
})($jq);
//--></script></fwn:OutputText>
	