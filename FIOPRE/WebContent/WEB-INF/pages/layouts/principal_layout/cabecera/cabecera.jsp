<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>
<%@taglib uri="http://www.treelogic.com/fawna/tiles" prefix="t"%>

<fwn:OutputPanel layout="block" styleClass="cabecera">
	<fwn:OutputPanel layout="block" id="datosUsuario">
		<fwn:OutputPanel layout="block" styleClass="usuario">
			<fwn:Image value="/images/iconos/usuario.gif"></fwn:Image>
			<fwn:OutputText
				value="#{msg['FWN_Comun.usuario.conectado.valor']}"
				styleClass="label-bold" />
			<fwn:OutputText id="usuario"
				value="#{treelogic.SECURE_USER_DISPLAYNAME} [#{treelogic.SECURE_USER_USERGROUP}]"></fwn:OutputText>
		</fwn:OutputPanel>
		<fwn:OutputPanel layout="block" styleClass="logout">
			<fwn:Link value="/j_security_logout">
				<fwn:OutputText
					value="#{msg['FWN_Comun.boton.desconectar.valor']}" />
			</fwn:Link>
			<fwn:Link value="/j_security_logout">
				<fwn:Image value="/images/iconos/icono-salir.gif"></fwn:Image>
			</fwn:Link>
		</fwn:OutputPanel>
		<fwn:Image value="/images/fiona/logo_fiona.png"></fwn:Image>
	</fwn:OutputPanel>

</fwn:OutputPanel>