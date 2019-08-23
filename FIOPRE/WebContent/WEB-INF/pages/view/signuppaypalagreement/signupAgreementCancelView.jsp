<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>


<fwn:Form id="signupFailureForm" styleClass="aloneMessage">
	<fwn:OutputPanel layout="block" style="font-size: 20pt;">
		<fwn:OutputText value="Operación de pago cancelada por el usuario"></fwn:OutputText>
	</fwn:OutputPanel>
	<fwn:OutputText value="Ha cancelado usted la operación. No podrá acceder a la plataforma hasta que
	no nos autorice el cobro de la suscripción periódica de su cuenta.">	
	</fwn:OutputText>
	<fwn:OutputPanel layout="block" styleClass="div-clean"></fwn:OutputPanel>
	<fwn:OutputPanel layout="block" styleClass="separador" style="margin-top:20px; text-align:center;">
		<fwn:Button action="back" value="" styleClass="fio-back-button" style="float:none; font-size:16pt;" immediate="true" />
	</fwn:OutputPanel>
</fwn:Form>

	