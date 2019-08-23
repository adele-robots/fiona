<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>

<fwn:OutputPanel id="lightbox_panel" styleClass="lightbox_noVisible"
	layout="block">
	<fwn:OutputPanel styleClass="lightbox-fondo" layout="block"
		id="lightbox_fondo"></fwn:OutputPanel>
	<fwn:OutputPanel styleClass="lightbox-contenedor"
		id="lightbox_contenedor" layout="block">
		<fwn:Image value="/images/status/ajax-loader.gif" id="lightBox_imagen"></fwn:Image>
	</fwn:OutputPanel>
</fwn:OutputPanel>