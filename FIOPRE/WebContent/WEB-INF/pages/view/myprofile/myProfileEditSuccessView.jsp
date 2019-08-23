<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>


<fwn:Form id="myProfileEditSuccessForm" >
	<fwn:OutputPanel layout="block" style="text-align:center;">
		<fwn:OutputText value="#{msg['FIONA_myProfile.editsuccess.message.valor']}" />
	</fwn:OutputPanel>
	<fwn:OutputPanel layout="block" styleClass="div-clean"></fwn:OutputPanel>
	<fwn:OutputPanel layout="block" style="text-align: center;">
		<fwn:OutputText id="errorPaypalDRT" rendered="#{!empty(treelogic.errorPaypalDRT)}" style="color:red; padding-bottom:50px;" value="#{msg['FIONA_signup.doreferenceini.error.valor']}"></fwn:OutputText>			
		<fwn:OutputPanel layout="block" styleClass="div-clean"></fwn:OutputPanel>
	</fwn:OutputPanel>
	<fwn:OutputPanel layout="block" style="margin-top:20px; text-align:center;">
		<fwn:Button action="manageMyProfile" value="" styleClass="fio-back-button" style="float:none; font-size:16pt;" immediate="true" />
	</fwn:OutputPanel>	
</fwn:Form>
<fwn:OutputText><script type="text/javascript"><!--
(function($){
	$(function() {
		$("input:first", "form#myProfileEditSuccessForm:first").focus();
	});
})($jq);
//--></script></fwn:OutputText>
