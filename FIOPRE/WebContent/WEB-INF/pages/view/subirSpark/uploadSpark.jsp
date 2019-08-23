<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>

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


<%--
<fwn:Label>
Ready to test your work? Welcome to the SparkUp zone, this is where you can upload files to the platform. By now, this process is manual but we are looking forward to make it automatic for the full release version.
<br><br>
If you did not read our developers guide, check it out <fwn:Link value="http://www.sparkingtogether.com/wiki/index.php/Spark_Developer_Guide">here.</fwn:Link> you will find guidelines on how to develop you own Sparks as well as the templates and requisites to upload a valid file to Fiona
<br><br><br><br>
</fwn:Label>
 --%>

<fwn:Messages showDetail="true" showSummary="false"
				ajaxRendered="false" style="color:red; padding-bottom:20px;"></fwn:Messages>

<fwn:Form formValidator="FormValidatorUploadSpark" enctype="multipart/form-data">
	<fwn:Panel header="Upload your Spark!" collapsable="false" collapse="false" showTextCollapse="false"
	styleClass="panel-importar" bodyClass="caja-importar">

		<fwn:ModalAlert id="alertError" alertTitle="#{msg['FIONA.uploadspark.alert.title']}" title="#{msg['FIONA.uploadspark.alert.error']}"
			type="error">
			<fwn:facet name="buttons">
				<fwn:OutputPanel>
					<fwn:OutputText id="infoError" value="" style="font-size: 16px;" >
					</fwn:OutputText>
				</fwn:OutputPanel>
			</fwn:facet>
		</fwn:ModalAlert>
		
		<fwn:ModalAlert id="alertSuccess" alertTitle="#{msg['FIONA.uploadspark.alert.title']}" title="#{msg['FIONA.uploadspark.alert.exito']}"
			type="check">
			<fwn:facet name="buttons">
				<fwn:OutputPanel>
					<fwn:OutputText id="infoSuccess" value="" style="font-size: 16px;" >
					</fwn:OutputText>
				</fwn:OutputPanel>
			</fwn:facet>
		</fwn:ModalAlert>

		<fwn:Label>Please select your file:</fwn:Label>
		<br>

		<fwn:OutputPanel styleClass="tooltip">
			<fwn:InputFileUpload id="compressed_filename" name="sparkC" label="#{msg['FIONA.uploadspark.ficheroSubida']}"	required="true" />
			<span id="spaan">Uploaded file must match following structure:<br>-/bin or [/src & /include]<br>-/config<br>-/icon<br>[-/dependencies]<br>-README file</span>
		</fwn:OutputPanel>		
		<br>
		<fwn:Label styleClass="labelInfoText">Remember that you must upload a valid file format (.tar .tar.gz .zip)</fwn:Label>
		<br>
		<fwn:Label styleClass="labelInfoText">Have a look at the 
		<fwn:Link styleClass="labelInfoText" value="http://www.sparkingtogether.com/wiki/index.php/Spark_Developer_Guide#How_to_upload_a_Spark">Spark Developer Guide</fwn:Link>
		, where you will find useful info on how to develop your own Sparks and requisites to upload a valid one to FIONA</fwn:Label>
		<br>
		<%-- <fwn:Label style="font-size: 14px;">Why is my browser showing C:\fakepath\ as the path for my file?</fwn:Label>
		<br>--%>
		<fwn:OutputPanel layout="block" id="genericinfoDiv" rendered="false">
			<fwn:OutputText id="genericinfo" value="">
			</fwn:OutputText>
		</fwn:OutputPanel>
		<fwn:OutputPanel layout="block" id="binfoundDiv" rendered="false">
			<fwn:OutputText id="binfound" value="">
			</fwn:OutputText>
		</fwn:OutputPanel>
		<fwn:OutputPanel layout="block" id="srcfoundDiv" rendered="false">
			<fwn:OutputText id="srcfound" value="">
			</fwn:OutputText>
		</fwn:OutputPanel>
		<fwn:OutputPanel layout="block" id="headersfoundDiv" rendered="false">
			<fwn:OutputText id="headersfound" value="">
			</fwn:OutputText>
		</fwn:OutputPanel>
		<fwn:OutputPanel layout="block" id="configfoundDiv" rendered="false">
			<fwn:OutputText id="configfound" value="">
			</fwn:OutputText>
		</fwn:OutputPanel>
		<fwn:OutputPanel layout="block" id="iconfoundDiv" rendered="false">
			<fwn:OutputText id="iconfound" value="">
			</fwn:OutputText>
		</fwn:OutputPanel>
		<fwn:OutputPanel layout="block" id="readmefoundDiv" rendered="false">
			<fwn:OutputText id="readmefound" value="">
			</fwn:OutputText>
		</fwn:OutputPanel>
	</fwn:Panel>
		
	<%-- Panel Spark Information --%>
	<fwn:Panel
		header="#{msg['FIONA.store.integratorBuyer.sparkInformation.cabeceraPanel.valor']}"
		collapsable="true">
		<fwn:PanelForm id="formSparkData" tabOrder="horizontal">
			<fwn:facet name="column1">
				<fwn:OutputPanel>
					<%-- Nombre Spark --%>
					<fwn:OutputPanel>
						<fwn:Label value="* " styleClass="asterisk" for="sparkName" />
						<fwn:Label styleClass="inputInvitacion"
							value="#{msg['FIONA.store.integratorBuyer.sparkInformation.campoNombre.valor']}"></fwn:Label>
					</fwn:OutputPanel>
					<fwn:Text id="sparkName"
						label="#{msg['FIONA.uploadspark.nombreSpark']}" value=""
						required="true" maxlength="100" size="50"
						styleClass="inputInvitacion"></fwn:Text>
					<%-- Version --%>
					<fwn:OutputPanel>
						<fwn:Label value="* " styleClass="asterisk" for="sparkVersion" />
						<fwn:Label styleClass="inputInvitacion"
							value="#{msg['FIONA.store.integratorBuyer.sparkInformation.campoVersion.valor']}"></fwn:Label>
					</fwn:OutputPanel>
					<fwn:Text id="sparkVersion"
						label="#{msg['FIONA.uploadspark.versionSpark']}" value=""
						required="true" maxlength="50" size="5"
						styleClass="inputInvitacion"></fwn:Text>
					<%-- Descripción --%>
					<fwn:OutputPanel>
						<fwn:Label value="* " styleClass="asterisk" for="longDescription" />
						<fwn:Label
							value="#{msg['FIONA.store.integratorBuyer.sparkInformation.campoDescripcion.valor']}"></fwn:Label>
					</fwn:OutputPanel>
					<fwn:TextArea id="longDescription"
						label="#{msg['FIONA.uploadspark.descripcionLarga']}" value=""
						cols="500" rows="8" styleClass="textAreaDescripcion" required="true"></fwn:TextArea>
					<%-- Descripción corta --%>
					<fwn:OutputPanel>
						<fwn:Label value="* " styleClass="asterisk" for="shortDescription" />
						<fwn:Label styleClass="inputInvitacion"
							value="#{msg['FIONA.store.integratorBuyer.sparkInformation.campoDescCorta.valor']}"></fwn:Label>
					</fwn:OutputPanel>
					<fwn:Text id="shortDescription"
						label="#{msg['FIONA.uploadspark.descripcionCorta']}" value=""
						required="true" maxlength="255" size="50"
						styleClass="inputInvitacion"></fwn:Text>
					<%-- Email Soporte --%>
					<fwn:OutputPanel>
						<fwn:Label value="* " styleClass="asterisk" for="supportEmail" />
						<fwn:Label styleClass="inputInvitacion"
							value="#{msg['FIONA.store.integratorBuyer.sparkMetadata.campoEmail.valor']}"></fwn:Label>
					</fwn:OutputPanel>
					<fwn:Text id="supportEmail"
						label="#{msg['FIONA.uploadspark.emailSoporte']}" value=""
						required="true" maxlength="255" size="50"
						styleClass="inputInvitacion"></fwn:Text>
					<fwn:OutputText></fwn:OutputText>
					<fwn:OutputPanel layout="block" style="font-size:18px">
						<fwn:OutputText value="* " styleClass="asterisk"></fwn:OutputText>
						<fwn:OutputText value="required field"></fwn:OutputText>
					</fwn:OutputPanel>
				</fwn:OutputPanel>
			</fwn:facet>
		</fwn:PanelForm>
	</fwn:Panel>
	<%-- Panel interfaces --%>
	<fwn:Panel header="#{msg['FIONA.uploadspark.panel.interfaces']}" collapsable="true" collapse="true">
		<fwn:PanelForm id="formSparkInterfaces" tabOrder="horizontal">
			<fwn:facet name="column1">
				<fwn:OutputPanel>
					<%-- Interfaces --%>					
					<fwn:OutputPanel>
						<fwn:Label styleClass="inputInvitacion"
							value="#{msg['FIONA.uploadspark.tipoInterfaces']}" ></fwn:Label>
					</fwn:OutputPanel>
					<fwn:OutputPanel styleClass="interfacesContent">
						<fwn:Combo id="interfaceType" required="false"
							label="#{msg['FIONA.uploadspark.tipoInterfaces']}" showOptionSeleccionar="false"
							value="" propertyFile="items.tiposInterfaz" ajaxChangeListener="ChangeInterfaceType"
							event="onchange" reRender="interfaces_source,provided_interfaces,required_interfaces"
							></fwn:Combo>					
						<fwn:Label value="Provided" style = "padding-left:244px"></fwn:Label>
						<fwn:Label value="Required" style = "padding-left:195px"></fwn:Label>
					</fwn:OutputPanel>					
					<fwn:OutputPanel>
						<fwn:Label styleClass="inputInvitacion"
							value="#{msg['FIONA.uploadspark.interfaces']}"></fwn:Label>
					</fwn:OutputPanel>
					<fwn:OutputPanel styleClass="interfacesContent">
						<fwn:MultiListbox CA="029" CS="046" contextKey="FIONEG017020"
							contextValue="FIONEG017010" cacheable="false" id="interfaces_source"
							size="5" styleClass="listAllInterfaces" value="patapúm" style="font-size=14pt;" >
						</fwn:MultiListbox>

						<fwn:OutputPanel styleClass="botonesVerticales">
							<fwn:AjaxButton ajaxonclicklistener="AsignaInterfaz"
								styleClass="botonesInvitacion"
								reRender="interfaces_source,provided_interfaces,required_interfaces"
								value="   >>   " style="margin-bottom:15px;" />

							<fwn:AjaxButton ajaxonclicklistener="DesasignaInterfaz"
								styleClass="botonesInvitacion"
								reRender="interfaces_source,provided_interfaces,required_interfaces"
								value="   <<   "   />
						</fwn:OutputPanel>

						<fwn:MultiListbox styleClass="listProvidedInterfaces" id="provided_interfaces"							
							value="" contextKey="FIONEG017020"
							contextValue="FIONEG017010"></fwn:MultiListbox>
							
						<fwn:MultiListbox styleClass="listRequiredInterfaces" id="required_interfaces"
							items="#{treelogic.lstSparkKeywords2}"
							value="" contextKey="FIONEG017020"
							contextValue="FIONEG017010" disabled="true"></fwn:MultiListbox>
					
					</fwn:OutputPanel>
				</fwn:OutputPanel>
			</fwn:facet>
		</fwn:PanelForm>
	</fwn:Panel>
	<%-- Panel config --%>
	<fwn:Panel
		header="Spark configuration"
		collapsable="true" collapse="true">
		<fwn:PanelForm id="formSparkConfig" tabOrder="horizontal">
			<fwn:facet name="column1">
				<fwn:OutputPanel>					
						<fwn:DataScroller align="right" for="tablaParams" maxPages="5"
							id="sc2" reRender="tablaParams,sc"
							fastControls="hide" stepControls="auto" boundaryControls="auto"
							onpagechange="openLightBox();" oncomplete="closeLightBox();">
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
						<fwn:DataTable id="tablaParams" rows="15"
							value="#{treelogic.listaParametros}" var="parametro" reRender="sc,sc2"
							scrollersId="sc,sc2">

							<fwn:Column sortable="true" sortBy="#{parametro.param_nombre}"
								label="nombre" styleClass="nombre">
								<fwn:facet name="header">
									<fwn:OutputText
										value="#{msg['FIONA.uploadspark.nombreParam']}"></fwn:OutputText>
								</fwn:facet>
								<fwn:facet name="footer">
									<fwn:Text id="configName" value="" />
								</fwn:facet>
								<fwn:OutputText value="#{parametro.param_nombre}"></fwn:OutputText>
							</fwn:Column>
							
							<fwn:Column sortable="true"
								sortBy="#{parametro.param_type}" label="nombre usuario"
								styleClass="nombre">
								<fwn:facet name="header">
									<fwn:OutputText
										value="#{msg['FIONA.uploadspark.tipoParam']}"></fwn:OutputText>
								</fwn:facet>
								<fwn:facet name="footer">									
									<fwn:Combo id="configType" value="#{treelogic.configType}"
										label="#{msg['FIONA.uploadspark.tipoParam']}"
										showOptionSeleccionar="false"></fwn:Combo>
								</fwn:facet>
								<fwn:OutputText value="#{parametro.param_type}"></fwn:OutputText>
								<fwn:Hidden value="#{parametro.param_type_id}" id="configTypeId" />
							</fwn:Column>
							
							<fwn:Column sortable="true" sortBy="#{parametro.param_default}"
								label="date" styleClass="nombre">
								<fwn:facet name="header">
									<fwn:OutputText
										value="#{msg['FIONA.uploadspark.valorDefecto']}"></fwn:OutputText>
								</fwn:facet>
								<fwn:facet name="footer">
									<fwn:Text id="configDefault" value="" />
								</fwn:facet>
								<fwn:OutputText value="#{parametro.param_default}"></fwn:OutputText>
							</fwn:Column>
							<fwn:Column sortable="true" label="acciones" styleClass="nombre">
								<fwn:Hidden value="#{parametro.param_id}" id="paramId" />
								<fwn:AjaxButton styleClass="boton-delete"
									reRender="tablaParams"
									ajaxonclicklistener="DeleteConfigParam" immediate="true">
								</fwn:AjaxButton>

							</fwn:Column>
						</fwn:DataTable>
						<fwn:DataScroller align="right" for="tablaParams" maxPages="5"
							id="sc" reRender="tablaParams,sc2"
							fastControls="hide" stepControls="auto" boundaryControls="auto"
							onpagechange="openLightBox();" oncomplete="closeLightBox();">
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
					
					<fwn:OutputText></fwn:OutputText>
					<fwn:OutputPanel id="botoneraAdd" styleClass="separador">
						<fwn:OutputPanel layout="block" styleClass="configButtons" >
							<%-- Botón añadir --%>
							<fwn:AjaxButton
								value="#{msg['FIONA.uploadspark.button.addParam']}"
								id="addParamButton" ajaxonclicklistener="AddParamToTable"
								reRender="tablaParams" styleClass="botonesInvitacion">
							</fwn:AjaxButton>
							<%-- Botón Tipos propios --%>
							<fwn:AjaxButton value="#{msg['FIONA.uploadspark.button.type']}"
								id="ownTypesButton" ajaxonclicklistener="OpenUserTypesDialog"
								reRender="dialogUserTypes" styleClass="botonesInvitacion">
							</fwn:AjaxButton>
						</fwn:OutputPanel>
					</fwn:OutputPanel>
					<fwn:OutputText></fwn:OutputText>		
					</fwn:OutputPanel>		
			 </fwn:facet>			 
		</fwn:PanelForm>
	</fwn:Panel>

	<fwn:OutputPanel id="botonera" styleClass="separador">
		<fwn:OutputPanel layout="block">		 
			<fwn:Button action="back" value="" styleClass="fio-back-button"
				immediate="true" />
			<%--
			 <fwn:Button styleClass="fio-upload-button" onclick="openLightBox();"
				value="">
			 --%>
			<fwn:Button styleClass="botonesInvitacion" onclick="openLightBox();"
				value="#{msg['FIONA.uploadspark.button.upload']}">
				<fwn:UploadFileListener classListener="UploadSparkListener" />
			</fwn:Button>
		</fwn:OutputPanel>
	</fwn:OutputPanel>

	<%-- Pollings para cargar interfaces y tipos de parámetros de configuración al cargarse la página --%>
	<fwn:poll id="pollInsertInterfaces" ajaxListener="ChangeInterfaceType" interval="200" ajaxSingle="true" enabled="true" reRender="interfaces_source"/>
	<%--<fwn:poll id="pollInsertConfigParamTypes" ajaxListener="InsertConfigParamTypes" interval="200" ajaxSingle="true" enabled="true" reRender="configType"/>--%>
	<fwn:poll id="pollInsertConfigParamTypes" ajaxListener="InsertConfigParamTypes" interval="200" enabled="true" reRender="configType"/>

	<%-- Diálogo para gestión de tipos propios --%>
	<fwn:Dialog header="#{msg['FIONA.uploadspark.dialog.title']}"
		id="dialogUserTypes" size="extraLarge">
		<%-- Ventana modal para mostrar diversos mensajes de validaciones --%>
		<fwn:ModalAlert id="alertListTypes" alertTitle="" title="" type="info">
			<fwn:facet name="buttons">
				<fwn:OutputPanel>
					<fwn:OutputText id="infoListTypes" value=""
						style="font-size: 16px;">
					</fwn:OutputText>
				</fwn:OutputPanel>
			</fwn:facet>
		</fwn:ModalAlert>
		<%-- Ventana modal de confirmación de borrado de tipo --%>
		<fwn:ModalAlert id="alertDeleteListType"
			alertTitle="#{msg['FIONA.uploadspark.alert.deleteList']}"
			title="#{msg['FIONA.uploadspark.alert.confirmDelete']}"
			type="confirm">
			<fwn:facet name="buttons">
				<fwn:OutputPanel>
					<fwn:AjaxButton value="#{msg['FWN_Comun.botonOK']}"
						reRender="alertDeleteListType,userParams,sc4" id="deleteTypeOK"
						ajaxonclicklistener="DeleteListType"
						styleClass="botonesInvitacion">
					</fwn:AjaxButton>
					<fwn:AjaxButton value="#{msg['FWN_Comun.botonNO']}"
						reRender="alertDeleteListType" id="deleteTypeNO"
						ajaxonclicklistener="DeleteListType"
						styleClass="botonesInvitacion">
					</fwn:AjaxButton>
				</fwn:OutputPanel>
			</fwn:facet>
		</fwn:ModalAlert>

		<fwn:Panel header="#{msg['FIONA.uploadspark.dialog.listManagement']}">

			<fwn:OutputPanel layout="block" styleClass="userParamsTable">
				<%-- TABLA | Tipos lista propios --%>
				<fwn:DataScroller align="right" for="userParams" maxPages="5"
					rendered="true" id="sc4" reRender="userParams"
					fastControls="hide" stepControls="auto" boundaryControls="auto"
					onpagechange="openLightBox();" oncomplete="closeLightBox();">
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
				<fwn:DataTable id="userParams" rows="3"
					value="#{treelogic.listaTiposUsuario}" var="tipoLista"
					reRender="sc4" scrollersId="sc4">

					<fwn:Column sortable="true" sortBy="#{tipoLista.nombre}"
						label="nombre" styleClass="nombre">
						<fwn:facet name="header">
							<fwn:OutputText
								value="#{msg['FIONA.uploadspark.dialog.listName']}"></fwn:OutputText>
						</fwn:facet>
						<fwn:OutputText value="#{tipoLista.nombre}"></fwn:OutputText>
					</fwn:Column>
					<fwn:Column sortable="true" sortBy="#{tipoLista.valor_lista}"
						label="nombre" styleClass="nombre">
						<fwn:facet name="header">
							<fwn:OutputText
								value="#{msg['FIONA.uploadspark.dialog.listValues']}"></fwn:OutputText>
						</fwn:facet>
						<fwn:Combo id="defaultValue" items="#{tipoLista.valor_lista}"
							label="#{msg['FIONA.uploadspark.valorDefecto']}"
							showOptionSeleccionar="false"></fwn:Combo>
					</fwn:Column>
					<fwn:Column sortable="true" label="acciones" styleClass="nombre">
						<fwn:Hidden value="#{tipoLista.tipo_id}" id="userOwnParamId" />
						<fwn:AjaxButton styleClass="boton-delete"
							reRender="alertDeleteListType,userParams,userOwnParamId,sc4"
							ajaxonclicklistener="DeleteUserOwnParam" immediate="true">
						</fwn:AjaxButton>
					</fwn:Column>
				</fwn:DataTable>
			</fwn:OutputPanel>

			<fwn:OutputPanel layout="block">
				<%-- Nombre lista --%>
				<fwn:OutputPanel>
					<fwn:Label styleClass="inputInvitacion"
						value="#{msg['FIONA.uploadspark.dialog.listName']}"></fwn:Label>
				</fwn:OutputPanel>
				<fwn:Text id="listName" label="List type name" value=""
					maxlength="20" size="20" styleClass="inputInvitacion"></fwn:Text>

				<%-- Botón añadir nueva lista --%>
				<fwn:AjaxButton value="#{msg['FIONA.uploadspark.button.addList']}"
					id="addNewTypeButton" ajaxonclicklistener="AddNewListData"
					reRender="userParams,configType,alertListTypes,tablaValoresLista,listName,dialogUserTypes,sc4"
					styleClass="botonesInvitacion">
				</fwn:AjaxButton>
			</fwn:OutputPanel>
			
			<fwn:OutputPanel layout="block"
				styleClass="tablaValoresLista">
								
				<%-- TABLA | Valores de la lista --%>
				<fwn:DataTable id="tablaValoresLista" rows="4"
					value="#{treelogic.valoresLista}" var="valorLista"
					reRender="sc5" scrollersId="sc5">
					
					<fwn:Column sortable="true" sortBy="#{valorLista.nombre}"
						label="nombre" styleClass="nombre">
						<fwn:facet name="header">
							<fwn:OutputText value="#{msg['FIONA.uploadspark.dialog.value']}"></fwn:OutputText>
						</fwn:facet>
						<fwn:facet name="footer">
							<fwn:Text id="valorLista" value="" />
						</fwn:facet>
						<fwn:OutputText value="#{valorLista.nombre}"></fwn:OutputText>
					</fwn:Column>

					<fwn:Column sortable="true" label="acciones" styleClass="nombre">
						<fwn:Hidden value="#{valorLista.valor_id}" id="valorId" />
						<fwn:AjaxButton styleClass="boton-delete"
							reRender="tablaValoresLista,sc5"
							ajaxonclicklistener="DeleteValorLista" immediate="true">
						</fwn:AjaxButton>

					</fwn:Column>
				</fwn:DataTable>

				<fwn:DataScroller align="right" for="tablaValoresLista" maxPages="5"
					id="sc5" reRender="tablaValoresLista" fastControls="hide"
					stepControls="auto" boundaryControls="auto"
					renderIfSinglePage="false" onpagechange="openLightBox();"
					oncomplete="closeLightBox();">
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

			<fwn:OutputPanel layout="block" styleClass="addNewListValueButton">
				<%-- Botón añadir nuevo valor a la lista --%>
				<fwn:AjaxButton value="#{msg['FIONA.uploadspark.button.addValue']}"
					id="addNewListValueButton" ajaxonclicklistener="AddValueToList"
					reRender="tablaValoresLista,sc5" styleClass="botonesInvitacion">
				</fwn:AjaxButton>
			</fwn:OutputPanel>

		</fwn:Panel>
	</fwn:Dialog>
</fwn:Form>
