<%@page import="com.treelogic.fawna.presentacion.core.utilidades.FawnaPropertyConfiguration"%>
<%@page contentType="text/html;charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<fwn:OutputText><script type="text/javascript">
(function($){
	$(function(){
		$("input[type=file]").filestyle({ 
		     image: "${request.contextPath}/images/buttons/boton_puntos.png",
		     imageheight : 38,
		     imagewidth : 55,
		     width : 250,
		 });
	});
})(jQuery);	

</script></fwn:OutputText>
<%-- Contenido del listado --%>
<fwn:OutputPanel layout="block" styleClass="contenido" id="contenido">

	<fwn:Messages showDetail="true" showSummary="false"
				ajaxRendered="false" style="color:red; padding-bottom:20px;"></fwn:Messages>				
			
			
	<%-- Formulario de búsqueda y listado --%>
	<fwn:Form id="formSparkInformation" formValidator="FormValidatorSparkEdition" enctype="multipart/form-data">		
		<fwn:Hidden id="spark_id" rendered="true" value="#{treelogic.spark_id}"></fwn:Hidden>
		
		
				
		<%-- Panel Spark Information --%>		
		<fwn:Panel
			header="#{msg['FIONA.store.integratorBuyer.sparkInformation.cabeceraPanel.valor']}"
			collapsable="false">
			
			<fwn:PanelForm id="formSparkData" tabOrder="horizontal">
				<fwn:facet name="column1">
					<fwn:OutputPanel>
						<%-- Nombre --%>
						<fwn:OutputPanel>
							<fwn:Label styleClass="inputInvitacion" for="name"
								value="#{msg['FIONA.store.integratorBuyer.sparkInformation.campoNombre.valor']}"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:Text id="name" value="#{treelogic.nombre}" label="#{msg['FIONA.store.integratorBuyer.sparkInformation.campoNombre.valor']}"
						required="true" maxlength="100" size="50" styleClass="inputInvitacion"></fwn:Text>
						<%-- Version --%>
						<fwn:OutputPanel>
							<fwn:Label styleClass="inputInvitacion" for="version"
								value="#{msg['FIONA.store.integratorBuyer.sparkInformation.campoVersion.valor']}"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:Text id="version" value="#{treelogic.version}" label="#{msg['FIONA.store.integratorBuyer.sparkInformation.campoVersion.valor']}"
							required="true" maxlength="50" size="5" styleClass="inputInvitacion"></fwn:Text>									
						
					</fwn:OutputPanel>
				</fwn:facet>							
			</fwn:PanelForm>		
			
			<%-- View list status --%>
			<fwn:AjaxButton value="#{msg['FIONA.store.integratorBuyer.detalle.viewStatus.valor']}" reRender="dialogoListStatus"
			 id="abrirDialogoStatus" ajaxonclicklistener="AbrirCerrarDialogoStatus" styleClass="enlaceTabla" immediate="true"/>							 	
			 
			<%-- View Crash Reports --%>
			<fwn:AjaxButton value="#{msg['FIONA.store.integratorBuyer.detalle.viewCrash.valor']}" reRender="dialogoListCrash"
			 id="abrirDialogoCrash" ajaxonclicklistener="AbrirCerrarDialogoCrash" styleClass="enlaceTabla" immediate="true"/>
			 
			<%-- View list of rejections (condicional, sólo si estado spark == 'rejected') --%>
			<fwn:AjaxButton value="#{msg['FIONA.store.integratorBuyer.detalle.viewRejections.valor']}" reRender="dialogoListRejection"
			 id="abrirDialogoRejection" ajaxonclicklistener="AbrirCerrarDialogoRejection" styleClass="enlaceTabla" immediate="true" rendered="#{treelogic.status_id eq 4 or treelogic.status_id eq 3}"/>
			
		</fwn:Panel>
		
		<%-- Panel Spark Details --%>
		<fwn:Panel
			header="#{msg['FIONA.store.integratorBuyer.sparkDetails.cabeceraPanel.valor']}"
			collapsable="true">
			
			<fwn:PanelForm id="formSparkDetails" tabOrder="horizontal">
				<fwn:facet name="column1">
					<fwn:OutputPanel>						
						<%-- Descripción --%>						
						<fwn:OutputPanel>
							<fwn:Label value="#{msg['FIONA.store.integratorBuyer.sparkInformation.campoDescripcion.valor']}" for="contenidoDescripcion"></fwn:Label>
						</fwn:OutputPanel>
						<fwn:TextArea id="contenidoDescripcion" label="#{msg['FIONA.store.integratorBuyer.sparkInformation.campoDescripcion.valor']}" value="#{treelogic.descripcion}" cols="500" rows="8"
						styleClass="textAreaDescripcion" required="true"></fwn:TextArea>	
						<%-- Descripción corta --%>
						<fwn:OutputPanel>
							<fwn:Label styleClass="inputInvitacion" for="descCorta"
								value="#{msg['FIONA.store.integratorBuyer.sparkInformation.campoDescCorta.valor']}"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:Text id="descCorta" value="#{treelogic.descCorta}" label="#{msg['FIONA.store.integratorBuyer.sparkInformation.campoDescCorta.valor']}"
							required="true" maxlength="255" size="50" styleClass="inputInvitacion"></fwn:Text>
						<%-- Novedades --%>
						<fwn:OutputPanel>
							<fwn:Label styleClass="inputInvitacion" for="contenidoNovedades"
								value="#{msg['FIONA.store.integratorBuyer.sparkInformation.campoNovedades.valor']}"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:TextArea id="contenidoNovedades" label="#{msg['FIONA.store.integratorBuyer.sparkInformation.campoNovedades.valor']}" value="#{treelogic.novedades}" cols="500" rows="8"
						styleClass="textAreaDescripcion"></fwn:TextArea>						
						<%-- Icono --%>
						<fwn:OutputPanel>
							<fwn:Label styleClass="inputInvitacion" for="icon_filename"
								value="#{msg['FIONA.store.integratorBuyer.detalle.icono.valor']}"></fwn:Label>							
						</fwn:OutputPanel>												
						<fwn:OutputPanel style="float:left; margin-right: 300px; width: 724px;">
							<fwn:Text value="#{treelogic.icono}" id="icon_value"  rendered="#{!empty(treelogic.icono)}"
								styleClass="inputInvitacion textWithoutBackground" readonly="true"/>
														
							<fwn:InputFileUpload name="icon" label="Upload Icon" id="icon_filename" required="false"/>							
								
							<fwn:OutputPanel styleClass="inputInvitacion" style="float:right; margin-top: 0;">
								<fwn:Button styleClass="botonesInvitacion" onclick="openLightBox();"
									value="#{msg['FWN_Comun.subirFichero']}" id="button_upload_icon">
									<fwn:UploadFileListener classListener="UploadIconListener"/>
								</fwn:Button>
							</fwn:OutputPanel>
						</fwn:OutputPanel>
												
						<%-- Banner --%>
						<fwn:OutputPanel>
							<fwn:Label styleClass="inputInvitacion"
								value="#{msg['FIONA.store.integratorBuyer.detalle.banner.valor']}"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:OutputPanel style="float:left; margin-right: 300px; width: 724px;">
							<fwn:Text value="#{treelogic.banner}" id="banner_value"  rendered="#{!empty(treelogic.banner)}"  
							styleClass="inputInvitacion textWithoutBackground" readonly="true"/>
							
							<fwn:InputFileUpload name="banner" label="Upload Banner" id="banner_filename" required="false"/>							
							
							<fwn:OutputPanel styleClass="inputInvitacion" style="float:right; margin-top: 0;">								
								<fwn:Button styleClass="botonesInvitacion" onclick="openLightBox();"
									value="#{msg['FWN_Comun.subirFichero']}" id="button_upload_banner">
									<fwn:UploadFileListener classListener="UploadBannerListener" />
								</fwn:Button>
							</fwn:OutputPanel>
							
						</fwn:OutputPanel>		
						<%-- Video --%>	
						<fwn:OutputPanel>
							<fwn:Label style="inputInvitacion"
								value="#{msg['FIONA.store.integratorBuyer.detalle.video.valor']}"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:OutputPanel style="float:left; margin-right: 300px; width: 724px;">
							<fwn:Text value="#{treelogic.video}" id="video_value" rendered="#{!empty(treelogic.video)}" 
							styleClass="inputInvitacion textWithoutBackground" readonly="true"/>
							
							<fwn:InputFileUpload name="video" label="Upload Video" id="video_filename" required="false"/>							
								
							<fwn:OutputPanel styleClass="inputInvitacion" style="float:right; margin-top: 0;">
								<fwn:Button styleClass="botonesInvitacion" onclick="openLightBox();"
									value="#{msg['FWN_Comun.subirFichero']}" id="button_upload_video">
									<fwn:UploadFileListener classListener="UploadVideoListener" />
								</fwn:Button>
							</fwn:OutputPanel>
						</fwn:OutputPanel>				
						
					</fwn:OutputPanel>
				</fwn:facet>							
			</fwn:PanelForm>
			
			
			
		</fwn:Panel>
		<%-- Panel Spark Metadata --%>
		<fwn:Panel header="#{msg['FIONA.store.integratorBuyer.sparkMetadata.cabeceraPanel.valor']}"
			collapsable="true">
			
			<fwn:PanelForm id="formSparkMetadata" tabOrder="horizontal">
				<fwn:facet name="column1">
					<fwn:OutputPanel>
						<%-- Keywords --%>
						<fwn:OutputPanel>
							<fwn:Label styleClass="inputInvitacion" for="all_keywords"
								value="#{msg['FIONA.store.integratorBuyer.sparkInformation.campoKeywords.valor']}"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:OutputPanel style="float:left; margin-right: 300px; width: 578px;">
							<fwn:MultiListbox CA="029" CS="034" contextKey="FIONEG012020" contextValue="FIONEG012010" cacheable="false" 
							id="all_keywords" size="5" styleClass="listBoxesLeftStyle" value="#{treelogic.keywordsSeleccionadas}">
							</fwn:MultiListbox>
							
							<fwn:OutputPanel styleClass="botonesVerticales">
								<fwn:AjaxButton ajaxonclicklistener="AsignaKeywordsListener" styleClass="botonesInvitacion"
									reRender="all_keywords,keys_asignados, idsKeywords" value="   >>   "  style="margin-bottom:15px;"/>

								<fwn:AjaxButton ajaxonclicklistener="DesasignaKeywordsListener" styleClass="botonesInvitacion"
									reRender="all_keywords,keys_asignados, idsKeywords" value="   <<   " />
							</fwn:OutputPanel>

							<fwn:MultiListbox styleClass="listBoxesStyle" id="keys_asignados"
								items="#{treelogic.lstSparkKeywords}"
								value="#{treelogic.keywordsAsignadas}" contextKey="FIONEG012020" contextValue="FIONEG012010"></fwn:MultiListbox>
								
							<fwn:Hidden value="#{treelogic.idsKeywords}" id="idsKeywords"></fwn:Hidden>
						</fwn:OutputPanel>
						<%-- Otros keywords --%>
						<fwn:OutputPanel>
							<fwn:Label styleClass="inputInvitacion" for="otrosKeywords"
								value="#{msg['FIONA.store.integratorBuyer.sparkInformation.campoOtrosKeywords.valor']}"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:Text id="otrosKeywords" value="#{treelogic.otrosKeywords}" label="#{msg['FIONA.store.integratorBuyer.sparkInformation.campoOtrosKeywords.valor']}"
							maxlength="255" size="50" styleClass="inputInvitacion"></fwn:Text>
						<%-- Email Soporte --%>
						<fwn:OutputPanel>
							<fwn:Label styleClass="inputInvitacion" for="email"
								value="#{msg['FIONA.store.integratorBuyer.sparkMetadata.campoEmail.valor']}"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:Text id="email" value="#{treelogic.email}" label="#{msg['FIONA.store.integratorBuyer.sparkMetadata.campoEmail.valor']}"
							required="true" maxlength="255" size="50" styleClass="inputInvitacion"></fwn:Text>
						<%-- Marketing (URL) --%>
						<fwn:OutputPanel>
							<fwn:Label styleClass="inputInvitacion" for="marketingURL"
								value="#{msg['FIONA.store.integratorBuyer.sparkMetadata.campoMarketing.valor']}"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:Text id="marketingURL" value="#{treelogic.marketingURL}" label="#{msg['FIONA.store.integratorBuyer.sparkMetadata.campoMarketing.valor']}"
							maxlength="255" size="50" styleClass="inputInvitacion"></fwn:Text>
					</fwn:OutputPanel>
				</fwn:facet>
			</fwn:PanelForm>
		</fwn:Panel>
		
		<%-- Panel Spark EULA --%>
		<%--
		<fwn:Panel  header="#{msg['FIONA.store.integratorBuyer.sparkPricing.cabeceraPanel.valor']}"
			collapsable="true">			
			
			<fwn:OutputPanel>
				<fwn:OutputText value="#{msg['FIONA.store.integratorBuyer.detalle.eulaMsg1.valor']}">
				</fwn:OutputText>
				<fwn:AjaxButton value="#{FIONA.store.integratorBuyer.detalle.eulaClick.valor}" styleClass="detailLink">
				</fwn:AjaxButton>
				<fwn:OutputText value="#{msg['FIONA.store.integratorBuyer.detalle.eulaMsg2.valor']}">
				</fwn:OutputText>
				<fwn:AjaxButton value="#{FIONA.store.integratorBuyer.detalle.eulaTerms.valor}" styleClass="detailLink"
					ajaxonclicklistener="MostrarCondicionesMinimasEula">
				</fwn:AjaxButton>
				<fwn:OutputText value="#{msg['FIONA.store.integratorBuyer.detalle.eulaMsg3.valor']}">
				</fwn:OutputText>
				<fwn:AjaxButton value="#{FIONA.store.integratorBuyer.detalle.eulaTerms.valor}" styleClass="detailLink"
					ajaxonclicklistener="MostrarEulaStandard">
				</fwn:AjaxButton>
				<fwn:OutputText value="#{msg['FIONA.store.integratorBuyer.detalle.eulaMsg4.valor']}">
				</fwn:OutputText>
			</fwn:OutputPanel>
			
		</fwn:Panel>
		 --%>
		<%-- Panel Spark Pricing --%>	
		<fwn:Panel  header="#{msg['FIONA.store.integratorBuyer.sparkPricing.cabeceraPanel.valor']}"
			collapsable="true">
			<fwn:tabPanel switchType="client">	
				<%-- DESARROLLO --%>			
				<fwn:tab label="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.pestDesa.valor']}" id="preciosDesarrollo">
					<fwn:PanelForm id="formSparkPricingDes1" tabOrder="horizontal">
					
						<%--<fwn:facet name="column1">--%>
							<fwn:OutputPanel id="panelColumn1" layout="block" styleClass="feeDiv">
							
								<fwn:Label value="#{msg['FIONA.store.integratorBuyer.sparkPricing.tarifas.valordospuntos']}"
								for="idTipoTarifaProd" style="display: inline-table;margin-top: -9px;"></fwn:Label>
								
								<%-- value="#{!empty(treelogic.idUnidadUso)?2:1}" --%>					
								<fwn:Radio id="idTipoTarifa" value="#{treelogic.idTarifa}" ajaxChangeListener="ChangeTarifaListener" event="onchange"
								reRender="panelValues, panelTablaPrecios, panelInsertPrice" style="display:inline-block;">
									<fwn:SelectItem itemLabel="#{msg['FIONA.store.integratorBuyer.sparkPricing.tarifaPorTiempo.valor']}" itemValue="1"/>															
									<fwn:SelectItem itemLabel="#{msg['FIONA.store.integratorBuyer.sparkPricing.tarifaGratuita.valor']}" itemValue="3" />
								</fwn:Radio>	
							
							</fwn:OutputPanel>		
						
						<%--</fwn:facet>--%>
						
						<fwn:OutputPanel id="panelInsertPrice" layout="block" styleClass="panelLabelsInline">
							<fwn:OutputPanel styleClass="divLabelEtiquetaInline" id="panelUsers" rendered="#{treelogic.idTarifa ne 3}">
								<fwn:OutputPanel>
									<fwn:Label for="numUsuariosConcu" value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.usuariosCol.valor']}">
									</fwn:Label>
								</fwn:OutputPanel>
								<fwn:Combo id="numUsuariosConcu" value="#{treelogic.idUnidadTiempo}" propertyFile="items.concurrent_users" cacheable="false"
															showOptionSeleccionar="false"/>
							</fwn:OutputPanel>
							<%--
							<fwn:OutputPanel styleClass="divLabelEtiquetaInline" id="panelPeriodValue" rendered="#{treelogic.idTarifa ne 3}">
								<fwn:OutputPanel>
									<fwn:Label for="panelPeriodValues" value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.cantidadCol.valor']}">
									</fwn:Label>
								</fwn:OutputPanel>
								<fwn:OutputPanel id="panelPeriodValues">											
									 <fwn:Combo id="idValorMeses" value="#{treelogic.cantidadUnidadIntro}" cacheable="false" disabled="#{treelogic.idTarifa eq '3'}"
											showOptionSeleccionar="false" rendered="#{(treelogic.idTarifa eq '1' || treelogic.idTarifa eq '3') and (treelogic.idUnidadTiempo eq 1 || empty(treelogic.idUnidadTiempo))}"
											propertyFile="items.monthsValue"/>
									 <fwn:Combo id="idValorAnios" value="#{treelogic.cantidadUnidadIntro}" cacheable="false" disabled="#{treelogic.idTarifa eq '3'}"
											showOptionSeleccionar="false" rendered="#{(treelogic.idTarifa eq '1' || treelogic.idTarifa eq '3') && treelogic.idUnidadTiempo eq '2'}"
											propertyFile="items.yearsValue"/>
								</fwn:OutputPanel>
							</fwn:OutputPanel>
							--%>
							<fwn:OutputPanel styleClass="divLabelEtiquetaInline" id="panelPeriod" rendered="#{treelogic.idTarifa ne 3}">
								<fwn:OutputPanel>
									<fwn:Label for="panelValues" value="#{msg['FIONA.tablaPrecio.time.valor']}">
									</fwn:Label>
								</fwn:OutputPanel>
								<fwn:OutputPanel id="panelValues">
									<%-- Tiempo (unidades) --%>
									<%--
									<fwn:Combo id="idUnidadTiempo" CA="029" CS="040" value="#{treelogic.idUnidadTiempo}" cacheable="false" disabled="#{treelogic.idTarifa eq '3'}"
											contextValue="FIONEG014010" contextKey="FIONEG014020" showOptionSeleccionar="false" rendered="#{treelogic.idTarifa eq '1' || treelogic.idTarifa eq '3'}"
											ajaxChangeListener="TipoPeriodoChangeListener" event="onchange" reRender="panelPeriodValues"/>--%>
									<fwn:Combo id="idUnidadTiempo" value="#{treelogic.idUnidadTiempo}" cacheable="false" disabled="#{treelogic.idTarifa eq '3'}"
											showOptionSeleccionar="false" rendered="#{treelogic.idTarifa eq '1' || treelogic.idTarifa eq '3'}"
											propertyFile="items.time"/>												
								</fwn:OutputPanel>
							</fwn:OutputPanel>
							<fwn:OutputPanel styleClass="divLabelEtiquetaInline" id="panelPrecio" rendered="#{treelogic.idTarifa ne 3}">
								<fwn:OutputPanel>
									<fwn:Label for="dolares" value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.precioCol.valordolar']}">
									</fwn:Label>
								</fwn:OutputPanel>
								<fwn:Text id="dolares" value="#{treelogic.dolaresIntro}" format="numerico" decimalPrecision="2"
												 maxlength="7" size="7"/>
							</fwn:OutputPanel>
							
							<fwn:OutputPanel id="botoneraAdd" styleClass="separador">
								
					        	<fwn:OutputPanel layout="block">
										<%-- Add Row Button --%>
										<fwn:AjaxButton value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.addRowButton.valor']}" reRender="tablaPrecios,sc,sc2,dolares"
										 id="addRow" ajaxonclicklistener="AddDevelopmentPrice" styleClass="botonesInvitacion" rendered="#{treelogic.idTarifa ne 3}"
										 style="margin-top: 1%;">					 			 	
										 </fwn:AjaxButton>
								</fwn:OutputPanel>
							</fwn:OutputPanel>
						</fwn:OutputPanel>
																			
						
						<fwn:OutputPanel id="panelTablaPrecios" layout="block">
						
							<fwn:DataScroller align="right" for="tablaPrecios" maxPages="5" rendered="#{treelogic.idTarifa ne 3}"
								id="sc2" reRender="tablaPrecios,sc" renderIfSinglePage="false"
								fastControls="hide" stepControls="auto" boundaryControls="auto" onpagechange="openLightBox();" oncomplete="closeLightBox();">
				
								<fwn:facet name="next">
									<fwn:Image value="/images/iconos/paginacion/derecha.gif"></fwn:Image>
								</fwn:facet>
								<fwn:facet name="previous">
									<fwn:Image value="/images/iconos/paginacion/izquierda.gif"></fwn:Image>
								</fwn:facet>
								<fwn:facet name="last">
									<fwn:Image value="/images/iconos/paginacion/final_drcha.gif"></fwn:Image>
								</fwn:facet>
								<fwn:facet name="first">
									<fwn:Image value="/images/iconos/paginacion/final_izq.gif"></fwn:Image>
								</fwn:facet>
							</fwn:DataScroller>
							<fwn:DataTable id="tablaPrecios" rows="5" styleClass="tablaPrecios"
								value="#{treelogic.preciosList}" var="price"
								reRender="sc,sc2" scrollersId="sc,sc2" rendered="#{treelogic.idTarifa ne 3}">
				
								<fwn:Column sortable="true" style="width:10%;"
									label="nombre" styleClass="nombre" title="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.usuariosCol.valor']}">
									<fwn:facet name="header">
										<fwn:OutputText
											value="#{msg['FIONA.tablaPrecio.usuarios.valor']}"
											format="numerico" decimalPrecision="0" title="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.usuariosCol.valor']}"></fwn:OutputText>
									</fwn:facet>
									<%--<fwn:facet name="footer">										
										 <fwn:Combo id="numUsuariosConcu" value="#{treelogic.idUnidadTiempo}" propertyFile="items.concurrent_users" cacheable="false"
													showOptionSeleccionar="false"/>
									</fwn:facet>--%>
									<fwn:OutputText value="#{price.num_usuarios}"></fwn:OutputText>								
				
								</fwn:Column>
								
								<fwn:Column sortable="true" 
									label="date" styleClass="nombre">
									<fwn:facet name="header">
										<fwn:OutputText value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.cantidadCol.valor']}"
										format = "numerico" numberType="integer"></fwn:OutputText>
									</fwn:facet>
									<%--<fwn:facet name="footer">
										<fwn:OutputPanel id="panelPeriodValues" layout="block">											
											 <fwn:Combo id="idValorMeses" value="#{treelogic.cantidadUnidadIntro}" cacheable="false" disabled="#{treelogic.idTarifa eq '3'}"
													showOptionSeleccionar="false" rendered="#{(treelogic.idTarifa eq '1' || treelogic.idTarifa eq '3') and (treelogic.idUnidadTiempo eq 1 || empty(treelogic.idUnidadTiempo))}"
													propertyFile="items.monthsValue"/>
											 <fwn:Combo id="idValorAnios" value="#{treelogic.cantidadUnidadIntro}" cacheable="false" disabled="#{treelogic.idTarifa eq '3'}"
													showOptionSeleccionar="false" rendered="#{(treelogic.idTarifa eq '1' || treelogic.idTarifa eq '3') && treelogic.idUnidadTiempo eq '2'}"
													propertyFile="items.yearsValue"/>
										</fwn:OutputPanel>
									</fwn:facet> --%>
									<fwn:OutputText value="#{price.cantidad}"></fwn:OutputText>
				
								</fwn:Column>
								
								<fwn:Column sortable="true" 
									label="nombre usuario" styleClass="nombre">
									<fwn:facet name="header">
										<fwn:OutputText
											value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.unidadCol.valor']}"></fwn:OutputText>
									</fwn:facet>
									<%--<fwn:facet name="footer">
										<fwn:OutputPanel id="panelValues" layout="block">											
											<fwn:Combo id="idUnidadTiempo" CA="029" CS="040" value="#{treelogic.idUnidadTiempo}" cacheable="false" disabled="#{treelogic.idTarifa eq '3'}"
													contextValue="FIONEG014010" contextKey="FIONEG014020" showOptionSeleccionar="false" rendered="#{treelogic.idTarifa eq '1' || treelogic.idTarifa eq '3'}"
													ajaxChangeListener="TipoPeriodoChangeListener" event="onchange" reRender="panelPeriodValues"/>												
										</fwn:OutputPanel>
									</fwn:facet>--%>					
									<fwn:OutputText value="#{price.unidad_nombre}"></fwn:OutputText>
				
								</fwn:Column>			
								
								
								<fwn:Column sortable="true"  style="width:5%;"
									label="date" styleClass="nombre">
									<fwn:facet name="header">
										<fwn:OutputText value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.precioCol.valordolar']}"
										format="numerico" decimalPrecision="2">
										</fwn:OutputText>
									</fwn:facet>
									<%--<fwn:facet name="footer">
										<fwn:Text id="dolares" value="#{treelogic.dolaresIntro}" format="numerico" decimalPrecision="2"
										 maxlength="7"/>
									</fwn:facet>--%>
									<fwn:OutputText value="#{price.euros}"></fwn:OutputText>
				
								</fwn:Column>
								
								<fwn:Column sortable="true" 
									label="usado" styleClass="nombre" rendered="false">							
									<fwn:OutputText value="#{price.es_usado}"></fwn:OutputText>		
								</fwn:Column>		
								
								
								<fwn:Column sortable="true" 
									label="acciones" styleClass="nombre" style="width:5%;">
									<fwn:Hidden value="#{price.price_id}" id="precioId"/>
									<fwn:AjaxButton styleClass="boton-delete" reRender="tablaPrecios,sc,sc2"
										ajaxonclicklistener="DeleteDevelopmentPrice" immediate="true" alt="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.botonDelCol.valor']}"
										title="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.botonDelCol.valor']}" rendered="#{price.es_activo eq '1' or (price.es_activo eq '1' and price.es_usado eq '0') }">						
									</fwn:AjaxButton>							
								
								</fwn:Column>
								
				
							</fwn:DataTable>
							<fwn:DataScroller align="right" for="tablaPrecios" maxPages="5" rendered="#{treelogic.idTarifa ne 3}"
								id="sc" reRender="tablaPrecios,sc2" renderIfSinglePage="false"
								fastControls="hide" stepControls="auto" boundaryControls="auto" onpagechange="openLightBox();" oncomplete="closeLightBox();">
				
								<fwn:facet name="next">
									<fwn:Image value="/images/iconos/paginacion/derecha.gif"></fwn:Image>
								</fwn:facet>
								<fwn:facet name="previous">
									<fwn:Image value="/images/iconos/paginacion/izquierda.gif"></fwn:Image>
								</fwn:facet>
								<fwn:facet name="last">
									<fwn:Image value="/images/iconos/paginacion/final_drcha.gif"></fwn:Image>
								</fwn:facet>
								<fwn:facet name="first">
									<fwn:Image value="/images/iconos/paginacion/final_izq.gif"></fwn:Image>
								</fwn:facet>
							</fwn:DataScroller>
										
						</fwn:OutputPanel>			
						
					</fwn:PanelForm>
				</fwn:tab>
				<%-- PRODUCCIÓN --%>
				<fwn:tab label="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.pestProd.valor']}" id="preciosProduccion">
	        		<fwn:PanelForm id="formSparkPricingProd1" tabOrder="horizontal">
					
						<%--<fwn:facet name="column1"> --%>
							<fwn:OutputPanel id="panelColumnProd1" layout="block" styleClass="feeDiv">
								
								<fwn:Label value="#{msg['FIONA.store.integratorBuyer.sparkPricing.tarifas.valordospuntos']}"
								for="idTipoTarifaProd" style="display: inline-table;margin-top: -9px;"></fwn:Label>
								
								
								<%-- value="#{!empty(treelogic.idUnidadUso)?2:1}" --%>					
								<fwn:Radio id="idTipoTarifaProd" value="#{treelogic.idTarifaProd}" ajaxChangeListener="ChangeTarifaProduccionListener" event="onchange"
								reRender="panelValuesProd, panelTablaPreciosProd, panelInsertPriceProd" style="display: inline-block;" 
								label="#{msg['FIONA.store.integratorBuyer.sparkPricing.tarifas.valordospuntos']}">
									<fwn:SelectItem itemLabel="#{msg['FIONA.store.integratorBuyer.sparkPricing.tarifaPorTiempo.valor']}" itemValue="1"/>
									<fwn:SelectItem itemLabel="#{msg['FIONA.store.integratorBuyer.sparkPricing.tarifaPorUso.valor']}" itemValue="2" />						
									<fwn:SelectItem itemLabel="#{msg['FIONA.store.integratorBuyer.sparkPricing.tarifaGratuita.valor']}" itemValue="3" />
								</fwn:Radio>	
							
							</fwn:OutputPanel>		
						
						<%--</fwn:facet>--%> 
						
						<fwn:OutputPanel id="panelInsertPriceProd" layout="block" styleClass="panelLabelsInline">
							<fwn:OutputPanel styleClass="divLabelEtiquetaInline" id="panelUsersProd" rendered="#{treelogic.idTarifaProd ne 3}">
								<fwn:OutputPanel>
									<fwn:Label for="numUsuariosConcuProd" value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.usuariosCol.valor']}">
									</fwn:Label>
								</fwn:OutputPanel>
								<fwn:Combo id="numUsuariosConcuProd" value="#{treelogic.idUnidadTiempo}" propertyFile="items.concurrent_users" cacheable="false"
													showOptionSeleccionar="false"/>
							</fwn:OutputPanel>
							<fwn:OutputPanel styleClass="divLabelEtiquetaInline" id="panelPeriodValueProd" rendered="#{treelogic.idTarifaProd ne 3}">
								<fwn:OutputPanel>
									<fwn:Label for="panelPeriodValuesProd" value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.cantidadCol.valor']}"
									rendered ="#{treelogic.idTarifaProd eq '2'}">
									</fwn:Label>
								</fwn:OutputPanel>
								<fwn:OutputPanel id="panelPeriodValuesProd">											
									<fwn:Text id="cantidadUnidadProd" value="#{treelogic.cantidadUnidadIntro}" format="numerico" numberType="integer"
								 	maxlength="7" size="7" rendered ="#{treelogic.idTarifaProd eq '2'}"/>											
									<%--<fwn:Combo id="idValorMesesProd" value="#{treelogic.cantidadUnidadIntro}" cacheable="false" disabled="#{treelogic.idTarifa eq '3'}"
										showOptionSeleccionar="false" rendered="#{(treelogic.idTarifaProd eq '1' || treelogic.idTarifaProd eq '3') and (treelogic.idUnidadTiempoProd eq 1 || empty(treelogic.idUnidadTiempoProd))}"
										propertyFile="items.monthsValue"/>
									<fwn:Combo id="idValorAniosProd" value="#{treelogic.cantidadUnidadIntro}" cacheable="false" disabled="#{treelogic.idTarifa eq '3'}"
										showOptionSeleccionar="false" rendered="#{(treelogic.idTarifaProd eq '1' || treelogic.idTarifaProd eq '3') && treelogic.idUnidadTiempoProd eq '2'}"
										propertyFile="items.yearsValue"/> --%>
								</fwn:OutputPanel>
							</fwn:OutputPanel>
							<fwn:OutputPanel styleClass="divLabelEtiquetaInline" id="panelPeriodProd" rendered="#{treelogic.idTarifaProd ne 3}">
								<fwn:OutputPanel>
									<fwn:Label for="panelValuesProd" value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.unidadCol.valor']}"
									rendered="#{treelogic.idTarifaProd eq '2'}">
									</fwn:Label>
									<fwn:Label for="panelValuesProd" value="#{msg['FIONA.tablaPrecio.time.valor']}" rendered="#{treelogic.idTarifaProd eq '1'}">
									</fwn:Label>
								</fwn:OutputPanel>
								<fwn:OutputPanel id="panelValuesProd">
									<%--		
									<fwn:Combo id="idUnidadTiempoProd" CA="029" CS="040" value="#{treelogic.idUnidadTiempoProd}" cacheable="false" disabled="#{treelogic.idTarifaProd eq '3'}"
											contextValue="FIONEG014010" contextKey="FIONEG014020" showOptionSeleccionar="false" rendered="#{treelogic.idTarifaProd eq '1' || treelogic.idTarifaProd eq '3'}"
											ajaxChangeListener="TipoPeriodoChangeListener" event="onchange" reRender="panelPeriodValuesProd"/>--%>
									<fwn:Combo id="idUnidadTiempoProd" value="#{treelogic.idUnidadTiempoProd}" cacheable="false" disabled="#{treelogic.idTarifaProd eq '3'}"
											showOptionSeleccionar="false" rendered="#{treelogic.idTarifaProd eq '1' || treelogic.idTarifaProd eq '3'}"
											propertyFile="items.time"/>
									
									<fwn:Combo id="idUnidadUsoProd" CA="029" CS="041" value="#{treelogic.idUnidadUsoProd}" cacheable="false" disabled="#{treelogic.idTarifaProd eq '3'}"
											contextValue="FIONEG015010" contextKey="FIONEG015020" showOptionSeleccionar="false" rendered="#{treelogic.idTarifaProd eq '2'}" />	
								</fwn:OutputPanel>
							</fwn:OutputPanel>
							<fwn:OutputPanel styleClass="divLabelEtiquetaInline" id="panelPrecioProd" rendered="#{treelogic.idTarifaProd ne 3}">
								<fwn:OutputPanel>
									<fwn:Label for="dolaresProd" value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.precioCol.valordolar']}">
									</fwn:Label>
								</fwn:OutputPanel>
								<fwn:Text id="dolaresProd" value="#{treelogic.dolaresIntro}" format="numerico" decimalPrecision="2"
										 maxlength="6" size="6"/>
							</fwn:OutputPanel>
							
							<fwn:OutputPanel id="botoneraAddProd" styleClass="separador">
								
					        	<fwn:OutputPanel layout="block">
										<%-- Add Row Button --%>
										<fwn:AjaxButton value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.addRowButton.valor']}" reRender="tablaPreciosProd,scProd,sc2Prod"
										 id="addRowProd" ajaxonclicklistener="AddProductionPrice" styleClass="botonesInvitacion" rendered="#{treelogic.idTarifaProd ne 3}"
										 style="margin-top: 1%;">					 			 	
										 </fwn:AjaxButton>
								</fwn:OutputPanel>
							</fwn:OutputPanel>
						</fwn:OutputPanel>
						
						<fwn:OutputPanel id="panelTablaPreciosProd" layout="block">
						
							<fwn:DataScroller align="right" for="tablaPreciosProd" maxPages="5" rendered="#{treelogic.idTarifaProd ne 3}"
								id="sc2Prod" reRender="tablaPreciosProd,scProd" renderIfSinglePage="false"
								fastControls="hide" stepControls="auto" boundaryControls="auto" onpagechange="openLightBox();" oncomplete="closeLightBox();">
				
								<fwn:facet name="next">
									<fwn:Image value="/images/iconos/paginacion/derecha.gif"></fwn:Image>
								</fwn:facet>
								<fwn:facet name="previous">
									<fwn:Image value="/images/iconos/paginacion/izquierda.gif"></fwn:Image>
								</fwn:facet>
								<fwn:facet name="last">
									<fwn:Image value="/images/iconos/paginacion/final_drcha.gif"></fwn:Image>
								</fwn:facet>
								<fwn:facet name="first">
									<fwn:Image value="/images/iconos/paginacion/final_izq.gif"></fwn:Image>
								</fwn:facet>
							</fwn:DataScroller>
							<fwn:DataTable id="tablaPreciosProd" rows="5" styleClass="tablaPrecios"
								value="#{treelogic.preciosProdList}" var="price"
								reRender="scProd,sc2Prod" scrollersId="scProd,sc2Prod" rendered="#{treelogic.idTarifaProd ne 3}">
				
								<fwn:Column sortable="true" 
									label="nombre" styleClass="nombre" style="width:10%;"
									title="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.usuariosCol.valor']}">
									<fwn:facet name="header">
										<fwn:OutputText
											value="#{msg['FIONA.tablaPrecio.usuarios.valor']}" title="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.usuariosCol.valor']}"
											format="numerico" decimalPrecision="0"></fwn:OutputText>
									</fwn:facet>
									<%-- <fwn:facet name="footer">
										<fwn:Text id="numUsuariosConcuProd" value="#{treelogic.numUsuariosIntro}" format="numerico" decimalPrecision="0"
										 maxlength="3" />
										 <fwn:Combo id="numUsuariosConcuProd" value="#{treelogic.idUnidadTiempo}" propertyFile="items.concurrent_users" cacheable="false"
													showOptionSeleccionar="false"/>
									</fwn:facet> --%>
									<fwn:OutputText value="#{price.num_usuarios}"></fwn:OutputText>
				
								</fwn:Column>
								
								<fwn:Column sortable="true" 
									label="date" styleClass="nombre">
									<fwn:facet name="header">
										<fwn:OutputText value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.cantidadCol.valor']}"
										format = "numerico" numberType="integer"></fwn:OutputText>
									</fwn:facet>
									<%--<fwn:facet name="footer">										
										 <fwn:OutputPanel id="panelPeriodValuesProd" layout="block">
										 	<fwn:Text id="cantidadUnidadProd" value="#{treelogic.cantidadUnidadIntro}" format="numerico" numberType="integer"
										 	maxlength="7" rendered ="#{treelogic.idTarifaProd eq '2'}"/>											
											 <fwn:Combo id="idValorMesesProd" value="#{treelogic.cantidadUnidadIntro}" cacheable="false" disabled="#{treelogic.idTarifa eq '3'}"
													showOptionSeleccionar="false" rendered="#{(treelogic.idTarifaProd eq '1' || treelogic.idTarifaProd eq '3') and (treelogic.idUnidadTiempoProd eq 1 || empty(treelogic.idUnidadTiempoProd))}"
													propertyFile="items.monthsValue"/>
											 <fwn:Combo id="idValorAniosProd" value="#{treelogic.cantidadUnidadIntro}" cacheable="false" disabled="#{treelogic.idTarifa eq '3'}"
													showOptionSeleccionar="false" rendered="#{(treelogic.idTarifaProd eq '1' || treelogic.idTarifaProd eq '3') && treelogic.idUnidadTiempoProd eq '2'}"
													propertyFile="items.yearsValue"/>													
										</fwn:OutputPanel>
									</fwn:facet>--%>
									<fwn:OutputText value="#{price.cantidad}"></fwn:OutputText>
				
								</fwn:Column>
								
								<fwn:Column sortable="true" 
									label="nombre usuario" styleClass="nombre">
									<fwn:facet name="header">
										<fwn:OutputText
											value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.unidadCol.valor']}"></fwn:OutputText>
									</fwn:facet>
									<%--<fwn:facet name="footer">
										<fwn:OutputPanel id="panelValuesProd" layout="block">
											
											<fwn:Combo id="idUnidadTiempoProd" CA="029" CS="040" value="#{treelogic.idUnidadTiempoProd}" cacheable="false" disabled="#{treelogic.idTarifaProd eq '3'}"
													contextValue="FIONEG014010" contextKey="FIONEG014020" showOptionSeleccionar="false" rendered="#{treelogic.idTarifaProd eq '1' || treelogic.idTarifaProd eq '3'}"
													ajaxChangeListener="TipoPeriodoChangeListener" event="onchange" reRender="panelPeriodValuesProd"/>
											
											<fwn:Combo id="idUnidadUsoProd" CA="029" CS="041" value="#{treelogic.idUnidadUsoProd}" cacheable="false" disabled="#{treelogic.idTarifaProd eq '3'}"
													contextValue="FIONEG015010" contextKey="FIONEG015020" showOptionSeleccionar="false" rendered="#{treelogic.idTarifaProd eq '2'}" />	
										</fwn:OutputPanel>
									</fwn:facet>--%>					
									<fwn:OutputText value="#{price.unidad_nombre}"></fwn:OutputText>
				
								</fwn:Column>								
								
								<fwn:Column sortable="true" 
									label="date" styleClass="nombre">
									<fwn:facet name="header">
										<fwn:OutputText value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.precioCol.valordolar']}"																	  
										format="numerico" decimalPrecision="2">
										</fwn:OutputText>
									</fwn:facet>
									<%--<fwn:facet name="footer">
										<fwn:Text id="dolaresProd" value="#{treelogic.dolaresIntro}" format="numerico" decimalPrecision="2"
										 maxlength="7"/>
									</fwn:facet>--%>
									<fwn:OutputText value="#{price.euros}"></fwn:OutputText>
				
								</fwn:Column>
								
								<fwn:Column sortable="true" style="width:5%;"
									label="usado" styleClass="nombre" rendered="false">							
									<fwn:OutputText value="#{price.es_usado}"></fwn:OutputText>		
								</fwn:Column>		
								
								
								<fwn:Column sortable="true" 
									label="acciones" styleClass="nombre"  style="width:5%;">
									<fwn:Hidden value="#{price.price_id}" id="precioIdProd"/>
									<fwn:AjaxButton styleClass="boton-delete" reRender="tablaPreciosProd"
										ajaxonclicklistener="DeleteProdPrice" immediate="true" alt="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.botonDelCol.valor']}"
										title="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.botonDelCol.valor']}" rendered="#{price.es_activo eq '1' or (price.es_activo eq '1' and price.es_usado eq '0') }">						
									</fwn:AjaxButton>															
								
								</fwn:Column>
								
				
							</fwn:DataTable>
							<fwn:DataScroller align="right" for="tablaPreciosProd" maxPages="5" rendered="#{treelogic.idTarifaProd ne 3}"
								id="scProd" reRender="tablaPreciosProd,sc2Prod" renderIfSinglePage="false"
								fastControls="hide" stepControls="auto" boundaryControls="auto" onpagechange="openLightBox();" oncomplete="closeLightBox();">
				
								<fwn:facet name="next">
									<fwn:Image value="/images/iconos/paginacion/derecha.gif"></fwn:Image>
								</fwn:facet>
								<fwn:facet name="previous">
									<fwn:Image value="/images/iconos/paginacion/izquierda.gif"></fwn:Image>
								</fwn:facet>
								<fwn:facet name="last">
									<fwn:Image value="/images/iconos/paginacion/final_drcha.gif"></fwn:Image>
								</fwn:facet>
								<fwn:facet name="first">
									<fwn:Image value="/images/iconos/paginacion/final_izq.gif"></fwn:Image>
								</fwn:facet>
							</fwn:DataScroller>
										
						</fwn:OutputPanel>			
						
					</fwn:PanelForm>
	        	</fwn:tab>
			</fwn:tabPanel>
			
			<fwn:PanelForm id="formSparkPricing2" tabOrder="horizontal">
				<fwn:facet name="column1">
					<fwn:OutputPanel id="panelColumn2">			
				
						<%-- Trial version --%>
						<fwn:OutputPanel>
							<fwn:Label styleClass="inputInvitacion"
								value="#{msg['FIONA.store.integratorBuyer.sparkPricing.campoEsTrial.valor']}"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:BooleanCheck id="checkTrial" value="#{treelogic.esTrial}" reRender="diasTrial" event="onclick" ajaxChangeListener="HabilitaDiasTrialListener" >
						</fwn:BooleanCheck>						
						
						<%-- Número de días trial --%>
						<fwn:OutputPanel>
							<fwn:Label styleClass="inputInvitacion"
								value="#{msg['FIONA.store.integratorBuyer.sparkPricing.campoDiasTrial.valor']}"></fwn:Label>							
						</fwn:OutputPanel>
						<fwn:Text id="diasTrial" value="#{treelogic.diasTrial}" disabled="#{treelogic.esTrial eq 'false' }" styleClass="inputInvitacion"
							 maxlength="4" size="3" format="numerico" numberType="integer"></fwn:Text>
							 
						<%-- Email para paypal --%>
						<fwn:OutputPanel>
							<fwn:Label styleClass="inputInvitacion"
								value="#{msg['FIONA.store.integratorBuyer.sparkPricing.campoEmailPaypal.valor']}"></fwn:Label>
							<fwn:OutputPanel layout="block">													
								<fwn:Label value="#{msg['FIONA.store.integratorBuyer.sparkPricing.labelCampoEmailPaypal.valor']}" 
									style="font-size:10px;" />
							</fwn:OutputPanel>
						</fwn:OutputPanel>
						<fwn:Text id="emailPaypal" value="#{treelogic.emailPaypal}" styleClass="inputInvitacion"
							 maxlength="100" size="50" required="true"></fwn:Text>
					
					</fwn:OutputPanel>
				</fwn:facet>
			</fwn:PanelForm>
		
			
		</fwn:Panel>
		
				        
		<fwn:OutputPanel id="botonera" styleClass="separador">
					
        	<fwn:OutputPanel layout="block">
					<fwn:Button action="back" value="" styleClass="fio-back-button" immediate="true"/>						
					
					<fwn:Button action="save" value="#{msg['FIONA.store.integratorBuyer.detalle.accionSave.valor']}" styleClass="botonesInvitacion" onclick="jQuery('#idTipoTarifa').hide()"/>
					<%-- Delete Button --%>
					<fwn:AjaxButton value="#{msg['FIONA.store.management.listado.accionDelete.valor']}" reRender="alertDelete"
					 id="abrirDelete" ajaxonclicklistener="AbrirCerrarAlert" styleClass="botonesInvitacion" immediate="true">					 			 	
					 </fwn:AjaxButton>
					 
					 <%-- Check button --%>	
					 <fwn:AjaxButton value="#{msg['FIONA.store.management.listado.accionCheck.valor']}" reRender="alertStatus"
					 id="abrirStatusC" ajaxonclicklistener="AbrirCerrarAlert" styleClass="botonesInvitacion" rendered="#{treelogic.status_id eq 2}">					 		
						<fwn:UpdateActionListener property="#{treelogic.idStatus}"
							value="7">						
						</fwn:UpdateActionListener>				 	
					 </fwn:AjaxButton>
					 
					 <%-- Publish button --%>	
					 <fwn:AjaxButton value="#{msg['FIONA.store.management.listado.accionPublish.valor']}" reRender="alertStatus"
					 id="abrirStatusP" ajaxonclicklistener="AbrirCerrarAlert" styleClass="botonesInvitacion" rendered="#{treelogic.status_id eq 5}">					 		
						<fwn:UpdateActionListener property="#{treelogic.idStatus}"
							value="6">						
						</fwn:UpdateActionListener>				 	
					 </fwn:AjaxButton>
					 
					  <%-- Unpublish button --%>	
					 <fwn:AjaxButton value="#{msg['FIONA.store.management.listado.accionUnpublish.valor']}" reRender="alertStatus"
					 id="abrirStatusU" ajaxonclicklistener="AbrirCerrarAlert" styleClass="botonesInvitacion" rendered="#{treelogic.status_id eq 6}">					 	
						<fwn:UpdateActionListener property="#{treelogic.idStatus}"
							value="5">						
						</fwn:UpdateActionListener>				 	
					 </fwn:AjaxButton>
					 
					  <%-- Overwrite button --%>	
					 <fwn:Button value="#{msg['FIONA.store.management.listado.accionOverwrite.valor']}" action="overwrite"
					 id="overwriteButton" styleClass="botonesInvitacion" rendered="#{treelogic.status_id eq 2 or treelogic.status_id eq 3 or treelogic.status_id eq 4 or treelogic.status_id eq 5}"/>			
			</fwn:OutputPanel>
        </fwn:OutputPanel>       
        
       
       <%-- ALERTS para confirmación de eliminación y change status --%>
		
		<fwn:ModalAlert id="alertDelete" alertTitle="#{msg['FIONA.store.management.sparkDelete.cabeceraPanel.valor']}" title="#{msg['FIONA.store.management.sparkDelete.confirmarCompra.valor']}" type="confirm">
		 <fwn:facet name="buttons">
		   <fwn:OutputPanel>		     
			 <fwn:Button action="deleteOK" value="#{msg['FWN_Comun.botonOK']}" styleClass="botonesInvitacion"/>
			 <fwn:AjaxButton value="#{msg['FWN_Comun.botonNO']}" reRender="alertDelete"
					 id="cerrarDelete" ajaxonclicklistener="AbrirCerrarAlert" styleClass="botonesInvitacion">				 	
					 </fwn:AjaxButton>											
		   </fwn:OutputPanel>
		  </fwn:facet>
		</fwn:ModalAlert>	
		
		<fwn:ModalAlert id="alertStatus" alertTitle="#{msg['FIONA.store.management.sparkStatus.cabeceraPanel.valor']}" title="#{msg['FIONA.store.management.sparkStatus.confirmarCompra.valor']}" type="confirm">
		 <fwn:facet name="buttons">
		   <fwn:OutputPanel>		     
			 <fwn:Button action="changeStatusOK" value="#{msg['FWN_Comun.botonOK']}" styleClass="botonesInvitacion"/>
			 <fwn:AjaxButton value="#{msg['FWN_Comun.botonNO']}" reRender="alertStatus"
					 id="cerrarStatus" ajaxonclicklistener="AbrirCerrarAlert" styleClass="botonesInvitacion">				 	
					 </fwn:AjaxButton>											
		   </fwn:OutputPanel>
		  </fwn:facet>
		</fwn:ModalAlert>	
		
		<%-- Diálogos para mostrar al usuario la lista de estados por los que ha pasado un spark, la lista de errores, y los detalles de rechazo --%>
		<fwn:Dialog header="#{msg['FIONA.store.integratorBuyer.detalle.viewStatus.valor']}" id="dialogoListStatus">
		  <fwn:Form id="FormTablaEstados">
		  
		  	<fwn:DataScroller align="right" for="tablaStatus" maxPages="5"
				id="sc2" reRender="tablaStatus,sc" renderIfSinglePage="false"
				fastControls="hide" stepControls="auto" boundaryControls="auto" onpagechange="openLightBox();" oncomplete="closeLightBox();">

				<fwn:facet name="next">
					<fwn:Image value="/images/iconos/paginacion/derecha.gif"></fwn:Image>
				</fwn:facet>
				<fwn:facet name="previous">
					<fwn:Image value="/images/iconos/paginacion/izquierda.gif"></fwn:Image>
				</fwn:facet>
				<fwn:facet name="last">
					<fwn:Image value="/images/iconos/paginacion/final_drcha.gif"></fwn:Image>
				</fwn:facet>
				<fwn:facet name="first">
					<fwn:Image value="/images/iconos/paginacion/final_izq.gif"></fwn:Image>
				</fwn:facet>
			</fwn:DataScroller>

			<fwn:DataTable id="tablaStatus" rows="15"
				value="#{treelogic.statusList}" var="status"
				reRender="sc,sc2" scrollersId="sc,sc2">

				<fwn:Column sortable="true" sortBy="#{status.status_nombre}"
					label="nombre" styleClass="nombre">
					<fwn:facet name="header">
						<fwn:OutputText
							value="#{msg['FIONA.store.management.listado.status.valor']}"></fwn:OutputText>
					</fwn:facet>
					<fwn:OutputText value="#{status.status_nombre}"></fwn:OutputText>

				</fwn:Column>
				
				<fwn:Column sortable="true" sortBy="#{status.status_nombre_usuario}"
					label="nombre usuario" styleClass="nombre">
					<fwn:facet name="header">
						<fwn:OutputText
							value="#{msg['FIONA.store.management.listado.nombreUsuario.valor']}"></fwn:OutputText>
					</fwn:facet>
					<fwn:OutputText value="#{status.status_nombre_usuario}"></fwn:OutputText>

				</fwn:Column>

				<fwn:Column sortable="true" sortBy="#{status.status_date}"
					label="date" styleClass="nombre">
					<fwn:facet name="header">
						<fwn:OutputText value="#{msg['FIONA.store.integratorBuyer.detalle.colFecha.valor']}"></fwn:OutputText>
					</fwn:facet>
					<fwn:OutputText value="#{status.status_date}"></fwn:OutputText>

				</fwn:Column>
				

			</fwn:DataTable>

			<fwn:DataScroller align="right" for="tablaStatus" maxPages="5"
				id="sc" reRender="tablaStatus,sc2" renderIfSinglePage="false"
				fastControls="hide" stepControls="auto" boundaryControls="auto" onpagechange="openLightBox();" oncomplete="closeLightBox();">

				<fwn:facet name="next">
					<fwn:Image value="/images/iconos/paginacion/derecha.gif"></fwn:Image>
				</fwn:facet>
				<fwn:facet name="previous">
					<fwn:Image value="/images/iconos/paginacion/izquierda.gif"></fwn:Image>
				</fwn:facet>
				<fwn:facet name="last">
					<fwn:Image value="/images/iconos/paginacion/final_drcha.gif"></fwn:Image>
				</fwn:facet>
				<fwn:facet name="first">
					<fwn:Image value="/images/iconos/paginacion/final_izq.gif"></fwn:Image>
				</fwn:facet>
			</fwn:DataScroller>
		  
		  
		  </fwn:Form>
		</fwn:Dialog>
		
		
		<fwn:Dialog header="#{msg['FIONA.store.integratorBuyer.detalle.viewCrash.valor']}" id="dialogoListCrash">
		  <fwn:Form id="FormTablaCrash">
		  
		  	<fwn:DataScroller align="right" for="tablaCrash" maxPages="5"
				id="sc2" reRender="tablaCrash,sc" renderIfSinglePage="false"
				fastControls="hide" stepControls="auto" boundaryControls="auto" onpagechange="openLightBox();" oncomplete="closeLightBox();">

				<fwn:facet name="next">
					<fwn:Image value="/images/iconos/paginacion/derecha.gif"></fwn:Image>
				</fwn:facet>
				<fwn:facet name="previous">
					<fwn:Image value="/images/iconos/paginacion/izquierda.gif"></fwn:Image>
				</fwn:facet>
				<fwn:facet name="last">
					<fwn:Image value="/images/iconos/paginacion/final_drcha.gif"></fwn:Image>
				</fwn:facet>
				<fwn:facet name="first">
					<fwn:Image value="/images/iconos/paginacion/final_izq.gif"></fwn:Image>
				</fwn:facet>
			</fwn:DataScroller>

			<fwn:DataTable id="tablaCrash" rows="15"
				value="#{treelogic.crashList}" var="crash"
				reRender="sc,sc2" scrollersId="sc,sc2">

				<fwn:Column sortable="true" sortBy="#{crash.crash_motivo}"
					label="nombre" styleClass="nombre">
					<fwn:facet name="header">
						<fwn:OutputText
							value="#{msg['FIONA.store.integratorBuyer.detalle.crashMotivo.valor']}"></fwn:OutputText>
					</fwn:facet>
					<fwn:OutputText value="#{crash.crash_motivo}"></fwn:OutputText>

				</fwn:Column>

				<fwn:Column sortable="true" sortBy="#{crash.crash_date}"
					label="date" styleClass="nombre">
					<fwn:facet name="header">
						<fwn:OutputText value="#{msg['FIONA.store.integratorBuyer.detalle.colFecha.valor']}"></fwn:OutputText>
					</fwn:facet>
					<fwn:OutputText value="#{crash.crash_date}"></fwn:OutputText>

				</fwn:Column>
				

			</fwn:DataTable>

			<fwn:DataScroller align="right" for="tablaCrash" maxPages="5"
				id="sc" reRender="tablaCrash,sc2" renderIfSinglePage="false"
				fastControls="hide" stepControls="auto" boundaryControls="auto" onpagechange="openLightBox();" oncomplete="closeLightBox();">

				<fwn:facet name="next">
					<fwn:Image value="/images/iconos/paginacion/derecha.gif"></fwn:Image>
				</fwn:facet>
				<fwn:facet name="previous">
					<fwn:Image value="/images/iconos/paginacion/izquierda.gif"></fwn:Image>
				</fwn:facet>
				<fwn:facet name="last">
					<fwn:Image value="/images/iconos/paginacion/final_drcha.gif"></fwn:Image>
				</fwn:facet>
				<fwn:facet name="first">
					<fwn:Image value="/images/iconos/paginacion/final_izq.gif"></fwn:Image>
				</fwn:facet>
			</fwn:DataScroller>
		  
		  
		  </fwn:Form>
		</fwn:Dialog>
		
		<fwn:Dialog header="#{msg['FIONA.store.integratorBuyer.detalle.viewRejections.valor']}" id="dialogoListRejection">
		  <fwn:Form id="FormTablaRejection">
		  
		  	<fwn:DataScroller align="right" for="tablaRejection" maxPages="5"
				id="sc2" reRender="tablaRejection,sc" renderIfSinglePage="false"
				fastControls="hide" stepControls="auto" boundaryControls="auto" onpagechange="openLightBox();" oncomplete="closeLightBox();">

				<fwn:facet name="next">
					<fwn:Image value="/images/iconos/paginacion/derecha.gif"></fwn:Image>
				</fwn:facet>
				<fwn:facet name="previous">
					<fwn:Image value="/images/iconos/paginacion/izquierda.gif"></fwn:Image>
				</fwn:facet>
				<fwn:facet name="last">
					<fwn:Image value="/images/iconos/paginacion/final_drcha.gif"></fwn:Image>
				</fwn:facet>
				<fwn:facet name="first">
					<fwn:Image value="/images/iconos/paginacion/final_izq.gif"></fwn:Image>
				</fwn:facet>
			</fwn:DataScroller>

			<fwn:DataTable id="tablaRejection" rows="15"
				value="#{treelogic.rejectionList}" var="rejection"
				reRender="sc,sc2" scrollersId="sc,sc2">

				<fwn:Column sortable="true" sortBy="#{rejection.rejection_motivo}"
					label="nombre" styleClass="nombre">
					<fwn:facet name="header">
						<fwn:OutputText
							value="#{msg['FIONA.store.integratorBuyer.detalle.crashMotivo.valor']}"></fwn:OutputText>
					</fwn:facet>
					<fwn:OutputText value="#{rejection.rejection_motivo}"></fwn:OutputText>

				</fwn:Column>

				<fwn:Column sortable="true" sortBy="#{rejection.rejection_date}"
					label="date" styleClass="nombre">
					<fwn:facet name="header">
						<fwn:OutputText value="#{msg['FIONA.store.integratorBuyer.detalle.colFecha.valor']}"></fwn:OutputText>
					</fwn:facet>
					<fwn:OutputText value="#{rejection.rejection_date}"></fwn:OutputText>

				</fwn:Column>
				

			</fwn:DataTable>

			<fwn:DataScroller align="right" for="tablaRejection" maxPages="5"
				id="sc" reRender="tablaRejection,sc2" renderIfSinglePage="false"
				fastControls="hide" stepControls="auto" boundaryControls="auto" onpagechange="openLightBox();" oncomplete="closeLightBox();">

				<fwn:facet name="next">
					<fwn:Image value="/images/iconos/paginacion/derecha.gif"></fwn:Image>
				</fwn:facet>
				<fwn:facet name="previous">
					<fwn:Image value="/images/iconos/paginacion/izquierda.gif"></fwn:Image>
				</fwn:facet>
				<fwn:facet name="last">
					<fwn:Image value="/images/iconos/paginacion/final_drcha.gif"></fwn:Image>
				</fwn:facet>
				<fwn:facet name="first">
					<fwn:Image value="/images/iconos/paginacion/final_izq.gif"></fwn:Image>
				</fwn:facet>
			</fwn:DataScroller>
		  
		  
		  </fwn:Form>
		</fwn:Dialog>
     
		
	</fwn:Form>	


</fwn:OutputPanel>