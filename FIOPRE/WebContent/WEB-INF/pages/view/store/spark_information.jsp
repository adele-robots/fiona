<%@page
	import="com.treelogic.fawna.presentacion.core.utilidades.FawnaPropertyConfiguration"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>
<%@taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<%-- Contenido del listado --%>
<fwn:OutputPanel layout="block" styleClass="contenido" id="contenido">


	<%-- Formulario de búsqueda y listado --%>
	<fwn:Form id="formSparkInformation" styleClass="formSparkInformation">
		<fwn:Hidden id="spark_id" rendered="true"
			value="#{treelogic.spark_id}"></fwn:Hidden>
		<fwn:Messages showDetail="true" showSummary="false"
			ajaxRendered="false" style="color:red; padding-bottom:50px;"></fwn:Messages>


		<fwn:OutputPanel styleClass="panelsparkInformation" layout="block"
			rendered="true">

			<fwn:OutputPanel
				styleClass="panelsparkInformation panelBasicInformation"
				layout="block" id="basicInformation">

				<fwn:OutputPanel id="imgSpark" styleClass="imgInfoSpark">
					<fwn:Image value="/fionasparkimages/#{treelogic.iconPath}"
						height="78px" width="78px" styleClass="imagenRedondeada" rendered="#{!empty(treelogic.iconPath)}"/>
					<fwn:Image value="/images/iconos/icono_fiona.png"
						height="78px" width="78px" styleClass="imagenRedondeada" rendered="#{empty(treelogic.iconPath)}"/>
				</fwn:OutputPanel>				

				<fwn:OutputPanel layout="block" styleClass="contentInfoSpark"
					id="sparkBasicInfoPanel">
					<%-- Nombre --%>
					<fwn:OutputText id="nombre" value="#{treelogic.nombre}"
						styleClass="float-clear-left"></fwn:OutputText>
					<fwn:OutputPanel id="panelTarifa"
						rendered="#{treelogic.idTarifa ne 3 and treelogic.comprado eq '1'}"
						layout="block">
						<%-- Número de usuarios --%>
						<fwn:OutputText id="precio_usuarios"
							value="(#{treelogic.precio_usuarios} #{msg['FIONA.store.integratorBuyer.sparkPricing.precios.usuariosCol.valor']},"
							styleClass="float-clear-left" format="numerico"
							decimalPrecision="0"
							rendered="#{!empty(treelogic.precio_usuarios)}"></fwn:OutputText>
						<%-- Unidad --%>
						<fwn:OutputText id="precio_unidad"
							value="#{treelogic.precio_cantidad} #{treelogic.precio_unidad},"
							styleClass="float-clear-left" style="clear:none;"
							rendered="#{!empty(treelogic.precio_cantidad) and !empty(treelogic.precio_unidad)}"></fwn:OutputText>
						<%-- Precio --%>
						<fwn:OutputText id="precio_precio"
							value="#{treelogic.precio_precio} $)"
							styleClass="float-clear-left" format="numerico"
							decimalPrecision="2" style="clear:none;"
							rendered="#{!empty(treelogic.precio_precio)}"></fwn:OutputText>
					</fwn:OutputPanel>
					<fwn:OutputPanel id="panelNombreDesarrollador"
						styleClass="float-clear-left">
						<fwn:OutputText id="nombreDesarrollador"
							value="#{treelogic.nombre_usuario_desarrollador}">
						</fwn:OutputText>
					</fwn:OutputPanel>
					<fwn:OutputPanel id="panelOpinionMedia"
						styleClass="float-clear-left">
						<fwn:OutputPanel styleClass="ratingNE">

							<fwn:RadioSingle for="radioRateAverage" index="4"
								showLabel="true" />
							<fwn:RadioSingle for="radioRateAverage" index="3"
								showLabel="true" />
							<fwn:RadioSingle for="radioRateAverage" index="2"
								showLabel="true" />
							<fwn:RadioSingle for="radioRateAverage" index="1"
								showLabel="true" />
							<fwn:RadioSingle for="radioRateAverage" index="0"
								showLabel="true" />


							<fwn:Radio value="#{treelogic.valoracion_media}"
								styleClass="ratingNE" layout="spread" id="radioRateAverage"
								disabled="true">
								<fwn:SelectItem itemValue="1" id="star11" />
								<fwn:SelectItem itemValue="2" id="star12" />
								<fwn:SelectItem itemValue="3" id="star13" />
								<fwn:SelectItem itemValue="4" id="star14" />
								<fwn:SelectItem itemValue="5" id="star15" />
							</fwn:Radio>

						</fwn:OutputPanel>
						<fwn:OutputText value="(#{treelogic.num_opiniones})">
						</fwn:OutputText>
					</fwn:OutputPanel>
				</fwn:OutputPanel>				
			</fwn:OutputPanel>
			<fwn:OutputPanel id="panelMensajeSuccess" layout="block" styleClass="panelMensajeSuccess" rendered="#{!empty(treelogic.sucessBuy)}">
					<fwn:OutputText id="mensajeSucess" value="#{msg['FIONA.store.integratorBuyer.sparkBuyTrial.confirmacion.valor']}">
					</fwn:OutputText>
			</fwn:OutputPanel>
			<fwn:OutputPanel id="panelMensajeErrorPaypal" layout="block" styleClass="panelMensajeError" rendered="#{!empty(treelogic.errorPaypalCBA)}">
					<fwn:OutputText id="errorPaypalCBA" rendered="#{!empty(treelogic.errorPaypalCBA)}" style="color:red; padding-bottom:50px;" 
					value="#{msg['FIONA.store.integratorBuyer.createbillingagreement.error.valor']}"></fwn:OutputText>
			</fwn:OutputPanel>
			<fwn:OutputPanel id="botoneraSuperior" styleClass="separadorSuperior" layout="block">
					<fwn:OutputPanel layout="block">				
						<fwn:AjaxButton
							value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonBuy.valor']}"
							reRender="alertCompra,alertErrorCompra" id="abrirBuy"
							ajaxonclicklistener="AbrirCerrarAlert"
							styleClass="botonesInvitacion"
							rendered="#{treelogic.comprado ne '1' and treelogic.status_id eq 6 and treelogic.idTarifa ne 3}">
						</fwn:AjaxButton>
						<fwn:AjaxButton
							value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonUninstall.valor']}"
							reRender="alertUninstall" id="abrirUninstall"
							ajaxonclicklistener="AbrirCerrarAlert"
							styleClass="botonesInvitacion"
							rendered="#{treelogic.comprado eq '1'}">
						</fwn:AjaxButton>
						<fwn:AjaxButton
							value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonFree.valor']}"
							reRender="alertFree" id="abrirFree"
							ajaxonclicklistener="AbrirCerrarAlert"
							styleClass="botonesInvitacion"
							rendered="#{treelogic.comprado ne '1' and treelogic.status_id eq 6 and treelogic.idTarifa eq 3}">
						</fwn:AjaxButton>
						<fwn:AjaxButton
							value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonTrial.valor']}"
							reRender="alertTrial" id="abrirTrial"
							ajaxonclicklistener="AbrirCerrarAlert"
							styleClass="botonesInvitacion"
							rendered="#{treelogic.esTrial eq 'true' and treelogic.status_id eq 6 and treelogic.comprado eq '0'}">
						</fwn:AjaxButton>						
					</fwn:OutputPanel>
				</fwn:OutputPanel>
			<fwn:OutputPanel layout="block"
				styleClass="tablaSparksLateralIzquierda" id="panelIzquierdo">
				<fwn:OutputText value="#{msg['FIONA.store.integratorBuyer.sparkInformation.cabeceraPanelMore.valor']}" styleClass="label-bold" style="padding-left: 21px;">
				</fwn:OutputText>
				<fwn:DataList value="#{treelogic.lstRelatedSparks}" var="spark"
					layout="unorderedList" styleClass="tabla-listado-sparks-prize-izquierda"
					id="listadoFreeSparks">
					<fwn:OutputPanel id="imgSpark" styleClass="liSparksPrizeContent"
						rendered="#{spark.spark_id ne treelogic.spark_id}">
						<fwn:Button action="viewDetails" styleClass="imageLinkDev" value="" rendered="#{!empty(spark.iconPath)}"
							title="#{spark.nombre}"
							image="/fionasparkimages/#{spark.iconPath}">
							<%--<fwn:Image value="/fionasparkimages/#{spark.iconPath}" height="78px" width="78px"/> --%>
							<fwn:UpdateActionListener property="#{treelogic.spark_id}"
								value="#{spark.spark_id}">
							</fwn:UpdateActionListener>
						</fwn:Button>
						<fwn:Button action="viewDetails" styleClass="imageLinkDev" value="" rendered="#{empty(spark.iconPath)}"
							title="#{spark.nombre}"
							image="/images/iconos/icono_fiona.png">							
							<fwn:UpdateActionListener property="#{treelogic.spark_id}"
								value="#{spark.spark_id}">
							</fwn:UpdateActionListener>
						</fwn:Button>
					</fwn:OutputPanel>
					<fwn:OutputPanel styleClass="rightLiContent"
						rendered="#{spark.spark_id ne treelogic.spark_id}">
						<fwn:OutputPanel id="enlaceDetalle" styleClass="panelEnlace"
							layout="block">
							<fwn:ActionLink value="#{spark.nombre}" action="viewDetails"
								styleClass="titleLink" title="#{spark.nombre}">
								<fwn:UpdateActionListener property="#{treelogic.spark_id}"
									value="#{spark.spark_id}">
								</fwn:UpdateActionListener>
							</fwn:ActionLink>
						</fwn:OutputPanel>

						<fwn:OutputPanel id="compradoSpark"
							styleClass="liSparksPrizeContent multiButtonBordered"
							rendered="#{spark.comprado eq '0'}">
							<%--<fwn:Label rendered="#{spark.comprado eq '1'}" styleClass="labelComprado"
									value="#{msg['FIONA.store.integratorBuyer.comprado.valor']}"></fwn:Label> --%>
							<fwn:Button
								value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonFree.valor']}"
								action="getFree"
								rendered="#{spark.idTarifa eq 3 and spark.comprado eq '0'}"
								styleClass="multiButton">
								<fwn:UpdateActionListener property="#{treelogic.spark_id}"
									value="#{spark.spark_id}">
								</fwn:UpdateActionListener>
							</fwn:Button>
							<fwn:Button
								value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonBuy.valor']}"
								action="buy"
								rendered="#{spark.idTarifa ne 3 and spark.comprado eq '0'}"
								styleClass="multiButton">
								<fwn:UpdateActionListener property="#{treelogic.spark_id}"
									value="#{spark.spark_id}">
								</fwn:UpdateActionListener>
							</fwn:Button>
						</fwn:OutputPanel>
						<fwn:OutputPanel id="compradoSparkGrey"
							styleClass="liSparksPrizeContent multiButtonGreyBordered"
							rendered="#{spark.comprado eq '1'}">
							<fwn:Button
								value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonUsing.valor']}"
								disabled="true" rendered="#{spark.comprado eq '1'}"
								styleClass="multiButtonGrey">
							</fwn:Button>
						</fwn:OutputPanel>
					</fwn:OutputPanel>
				</fwn:DataList>
			</fwn:OutputPanel>


			<fwn:tabPanel switchType="client" styleClass="tabsFloatRight">
				<%-- Pestania de detalles --%>
				<fwn:tab
					label="#{msg['FIONA.store.integratorBuyer.sparkInformation.cabeceraPanel.valor']}"
					id="pestaniaInfospark">
					<fwn:OutputPanel styleClass="panelsparkInformation" layout="block"
						rendered="true">

						<fwn:OutputPanel layout="block" styleClass="contentInfoSparkRight more-less"
							id="sparkInfoPanel">

							<%-- Descripción 				
								<fwn:TextArea id="contenidoDescripcion" label="Contenido" value="#{treelogic.descripcion}" cols="500" rows="8"
								styleClass="textAreaEmail invisible_textarea float-clear-left" readonly="true"></fwn:TextArea> --%>
								
							<fwn:OutputText value="#{treelogic.descripcion}"
								styleClass="textArea-descripcion invisible_textarea float-clear-left more-block"></fwn:OutputText>
								

							<%-- Novedades --%>
							<fwn:OutputPanel styleClass="float-clear-left"
								rendered="#{!empty(treelogic.novedades)}">
								<fwn:Label
									value="#{msg['FIONA.store.integratorBuyer.sparkInformation.campoNovedades.valor']}"></fwn:Label>
							</fwn:OutputPanel>
							<fwn:TextArea id="contenidoNovedades" label="Contenido"
								value="#{treelogic.novedades}" cols="500" rows="8"
								styleClass="textAreaEmail invisible_textarea float-clear-left"
								readonly="true" rendered="#{!empty(treelogic.novedades)}"></fwn:TextArea>


						</fwn:OutputPanel>

						<fwn:OutputPanel layout="block" styleClass="contentInfoSparkRight"
							id="videoPanel">


						</fwn:OutputPanel>

						<fwn:OutputPanel layout="block" styleClass="contentInfoSparkRight"
							id="supportInfoPanel">
							<%-- Email soporte --%>
							<fwn:OutputPanel styleClass="float-clear-left"
								rendered="#{!empty(treelogic.email)}">
								<fwn:Label
									value="#{msg['FIONA.store.integratorBuyer.sparkMetadata.campoSupporInfo.valor']}"></fwn:Label>
							</fwn:OutputPanel>
							<fwn:Link value="mailto:#{treelogic.email}">
								<fwn:OutputText id="email" value="#{treelogic.email}"
									styleClass="float-clear-left"></fwn:OutputText>
							</fwn:Link>

							<%-- Página web soporte --%>
							<fwn:OutputPanel styleClass="float-clear-left"
								rendered="#{!empty(treelogic.marketingURL)}">								
							</fwn:OutputPanel>
							<fwn:Link value="#{treelogic.marketingURL}">
								<fwn:OutputText id="url" value="#{treelogic.marketingURL}"
									styleClass="float-clear-left"></fwn:OutputText>
							</fwn:Link>

						</fwn:OutputPanel>

						<fwn:OutputPanel id="botoneraTabPanel" styleClass="separadorSuperior" layout="block">
							<fwn:OutputPanel layout="block">				
								<fwn:AjaxButton
									value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonBuy.valor']}"
									reRender="alertCompra,alertErrorCompra" id="abrirBuyTab"
									ajaxonclicklistener="AbrirCerrarAlert"
									styleClass="botonesInvitacion"
									style="float: right;"
									rendered="#{treelogic.comprado ne '1' and treelogic.status_id eq 6 and treelogic.idTarifa ne 3}">
								</fwn:AjaxButton>
							</fwn:OutputPanel>
						</fwn:OutputPanel>
						<fwn:tabPanel switchType="client"
							rendered="#{treelogic.comprado eq '0'}"
							style="background-color:#F0FFFF; margin-top:1%;" styleClass="contentInfoSpark">
							<%-- DESARROLLO --%>
							<fwn:tab
								label="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.pestDesa.valor']}"
								id="preciosDesarrollo" rendered="#{treelogic.idTarifa ne 3}">

								<fwn:OutputPanel id="panelPrecios"
									style="margin-top:25px;margin-left:10%;" layout="block"
									styleClass="contentInfoSpark">


									<fwn:DataScroller align="right" for="tablaPrecios" maxPages="5"
										id="sc2" reRender="tablaPrecios,sc" renderIfSinglePage="false"
										fastControls="hide" stepControls="auto"
										boundaryControls="auto" onpagechange="openLightBox();"
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
									<fwn:DataTable id="tablaPrecios" rows="5" style="width:85%;"
										value="#{treelogic.preciosList}" var="price"
										seleccionableType="single"
										seleccionableKey="#{price.price_id}"
										seleccionValue="#{treelogic.selected_price}" reRender="sc,sc2"
										scrollersId="sc,sc2"
										rendered="#{treelogic.idTarifa ne 3 and treelogic.comprado eq '0'}">

										<fwn:Column sortable="true" label="nombre" styleClass="nombre">
											<fwn:facet name="header">
												<fwn:OutputText
													value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.usuariosCol.valor']}"
													format="numerico" decimalPrecision="0"></fwn:OutputText>
											</fwn:facet>
											<fwn:OutputText value="#{price.num_usuarios}"></fwn:OutputText>

										</fwn:Column>

										<fwn:Column sortable="true" label="nombre usuario"
											styleClass="nombre">
											<fwn:facet name="header">
												<fwn:OutputText
													value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.unidadCol.valor']}"></fwn:OutputText>
											</fwn:facet>
											<fwn:OutputText value="#{price.unidad_nombre}"></fwn:OutputText>

										</fwn:Column>

										<fwn:Column sortable="true" label="date" styleClass="nombre">
											<fwn:facet name="header">
												<fwn:OutputText
													value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.cantidadCol.valor']}"
													format="numerico" decimalPrecision="2"></fwn:OutputText>
											</fwn:facet>
											<fwn:OutputText value="#{price.cantidad}"></fwn:OutputText>

										</fwn:Column>

										<fwn:Column sortable="true" label="date" styleClass="nombre">
											<fwn:facet name="header">
												<fwn:OutputText
													value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.precioCol.valor']}"
													format="numerico" decimalPrecision="2">
												</fwn:OutputText>
											</fwn:facet>
											<fwn:OutputText value="#{price.euros}"></fwn:OutputText>

										</fwn:Column>


									</fwn:DataTable>
									<fwn:DataScroller align="right" for="tablaPrecios" maxPages="5"
										id="sc" reRender="tablaPrecios,sc2" renderIfSinglePage="false"
										fastControls="hide" stepControls="auto"
										boundaryControls="auto" onpagechange="openLightBox();"
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
							</fwn:tab>

							<%-- PRODUCCIÓN --%>
							<fwn:tab
								label="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.pestProd.valor']}"
								id="preciosProduccion" rendered="#{treelogic.idTarifaProd ne 3}">


								<fwn:OutputPanel id="panelPreciosProd"
									style="margin-top:25px;margin-left:10%;" layout="block"
									styleClass="contentInfoSpark">

									<fwn:DataScroller align="right" for="tablaPreciosProd"
										maxPages="5" id="sc2Prod" reRender="tablaPreciosProd,scProd"
										renderIfSinglePage="false" fastControls="hide"
										stepControls="auto" boundaryControls="auto"
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
									<fwn:DataTable id="tablaPreciosProd" rows="5"
										style="width:85%;" value="#{treelogic.preciosProdList}"
										var="price" reRender="scProd,sc2Prod"
										scrollersId="scProd,sc2Prod"
										rendered="#{treelogic.idTarifaProd ne 3 and treelogic.comprado eq '0'}">

										<fwn:Column sortable="true" label="nombre" styleClass="nombre">
											<fwn:facet name="header">
												<fwn:OutputText
													value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.usuariosCol.valor']}"
													format="numerico" decimalPrecision="0"></fwn:OutputText>
											</fwn:facet>
											<fwn:OutputText value="#{price.num_usuarios}"></fwn:OutputText>

										</fwn:Column>

										<fwn:Column sortable="true" label="nombre usuario"
											styleClass="nombre">
											<fwn:facet name="header">
												<fwn:OutputText
													value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.unidadCol.valor']}"></fwn:OutputText>
											</fwn:facet>
											<fwn:OutputText value="#{price.unidad_nombre}"></fwn:OutputText>

										</fwn:Column>

										<fwn:Column sortable="true" label="date" styleClass="nombre">
											<fwn:facet name="header">
												<fwn:OutputText
													value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.cantidadCol.valor']}"
													format="numerico" decimalPrecision="2"></fwn:OutputText>
											</fwn:facet>
											<fwn:OutputText value="#{price.cantidad}"></fwn:OutputText>

										</fwn:Column>

										<fwn:Column sortable="true" label="date" styleClass="nombre">
											<fwn:facet name="header">
												<fwn:OutputText
													value="#{msg['FIONA.store.integratorBuyer.sparkPricing.precios.precioCol.valor']}"
													format="numerico" decimalPrecision="2">
												</fwn:OutputText>
											</fwn:facet>
											<fwn:OutputText value="#{price.euros}"></fwn:OutputText>

										</fwn:Column>


									</fwn:DataTable>
									<fwn:DataScroller align="right" for="tablaPreciosProd"
										maxPages="5" id="scProd" reRender="tablaPreciosProd,sc2Prod"
										renderIfSinglePage="false" fastControls="hide"
										stepControls="auto" boundaryControls="auto"
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

								</fwn:OutputPanel>
							</fwn:tab>
						</fwn:tabPanel>

					</fwn:OutputPanel>



					<security:authorize ifAnyGranted="ROLE_ADMIN">
						<%-- Abrir diálogo para cambiar el status de un spark --%>
						<fwn:AjaxButton
							value="#{msg['FIONA.store.globalAdministration.sparkInformation.status.botonChangeStatus']}"
							reRender="dialogoChangeStatus" id="abrirDialogoChangeStatus"
							ajaxonclicklistener="AbrirCerrarDialogoChangeStatus"
							styleClass="botonesInvitacion float-right">
						</fwn:AjaxButton>
					</security:authorize>

					<%-- Versión pre pestañas
						<fwn:OutputPanel id="botonera" styleClass="separador">					
				        	<fwn:OutputPanel layout="block">        			
									 <fwn:Button action="back" value="" styleClass="fio-back-button" immediate="true"/>
									 <fwn:AjaxButton value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonBuy.valor']}" reRender="alertCompra"
									 id="abrirBuy" ajaxonclicklistener="AbrirCerrarAlert" styleClass="botonesInvitacion" 
									 rendered="#{treelogic.comprado ne '1' and treelogic.status_id eq 6 and treelogic.idTarifa ne 3}">				 	
									 </fwn:AjaxButton>
									 <fwn:AjaxButton value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonFree.valor']}" reRender="alertCompra"
									 id="abrirFree" ajaxonclicklistener="AbrirCerrarAlert" styleClass="botonesInvitacion" 
									 rendered="#{treelogic.comprado ne '1' and treelogic.status_id eq 6 and treelogic.idTarifa eq 3}">				 	
									 </fwn:AjaxButton>	
									 <fwn:AjaxButton value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonTrial.valor']}" reRender="alertTrial"
									 id="abrirTrial" ajaxonclicklistener="AbrirCerrarAlert" styleClass="botonesInvitacion" rendered="#{treelogic.esTrial eq '1' and treelogic.status_id eq 6 and treelogic.comprado eq '0'}">				 	
									 </fwn:AjaxButton>
							</fwn:OutputPanel>
				        </fwn:OutputPanel>  --%>
					
					<%-- Diálogo para el listado de rechazos --%>
					<fwn:Dialog
						header="#{msg['FIONA.store.integratorBuyer.detalle.viewRejections.valor']}"
						id="dialogoListRejection">
						<fwn:Form id="FormTablaRejection">

							<fwn:DataScroller align="right" for="tablaRejection" maxPages="5"
								id="sc2" reRender="tablaRejection,sc" renderIfSinglePage="false"
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

							<fwn:DataTable id="tablaRejection" rows="15"
								value="#{treelogic.rejectionList}" var="rejection"
								reRender="sc,sc2" scrollersId="sc,sc2">

								<fwn:Column sortable="true"
									sortBy="#{rejection.rejection_motivo}" label="nombre"
									styleClass="nombre">
									<fwn:facet name="header">
										<fwn:OutputText
											value="#{msg['FIONA.store.integratorBuyer.detalle.crashMotivo.valor']}"></fwn:OutputText>
									</fwn:facet>
									<fwn:OutputText value="#{rejection.rejection_motivo}"></fwn:OutputText>

								</fwn:Column>

								<fwn:Column sortable="true" sortBy="#{rejection.rejection_date}"
									label="date" styleClass="nombre">
									<fwn:facet name="header">
										<fwn:OutputText
											value="#{msg['FIONA.store.integratorBuyer.detalle.colFecha.valor']}"></fwn:OutputText>
									</fwn:facet>
									<fwn:OutputText value="#{rejection.rejection_date}"></fwn:OutputText>

								</fwn:Column>


							</fwn:DataTable>

							<fwn:DataScroller align="right" for="tablaRejection" maxPages="5"
								id="sc" reRender="tablaRejection,sc2" renderIfSinglePage="false"
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


						</fwn:Form>
					</fwn:Dialog>
					<%-- FIN Diálogo que muestra la lista de razones de rechazo del spark --%>

					<%-- Diálogo para cambio de status --%>
					<fwn:Dialog
						header="#{msg['FIONA.store.globalAdministration.sparkInformation.status.tituloPanel']}"
						id="dialogoChangeStatus" size="large">

						<fwn:PanelForm id="formChangeStatus" tabOrder="horizontal">
							<fwn:facet name="column1">
								<fwn:OutputPanel>
									<%-- Estado actual --%>
									<fwn:OutputPanel>
										<fwn:Label styleClass="inputInvitacion"
											value="#{msg['FIONA.store.globalAdministration.sparkInformation.status.label']}"></fwn:Label>
									</fwn:OutputPanel>
									<fwn:OutputText id="currentStatus" value="#{treelogic.status}"></fwn:OutputText>
									<%-- Nuevo estado --%>
									<fwn:OutputPanel>
										<fwn:Label for="idStatus"
											value="#{msg['FIONA.store.globalAdministration.sparkInformation.status.newStatus']}"></fwn:Label>
									</fwn:OutputPanel>
									<fwn:OutputPanel id="panelStatusWrap">
										<fwn:Combo id="idStatus" CA="029" CS="027"
											value="#{treelogic.idStatus}" cacheable="false"
											required="false" contextValue="ARQN000010"
											contextKey="ARQN000020" reRender="panelStatusWrap"
											ajaxChangeListener="CompruebaCambioStatus" event="onchange"
											showOptionSeleccionar="false" />
										<fwn:OutputPanel id="panelRechazos" styleClass="panelRechazos"
											layout="block">
											<%-- Descripción --%>
											<fwn:OutputPanel>
												<fwn:Label id="labelRechazo" for="contenidoRechazo"
													value="#{msg['FIONA.store.globalAdministration.sparkInformation.opinion.campoRechazo.valor']}"
													rendered="false"></fwn:Label>
											</fwn:OutputPanel>
											<fwn:TextArea id="contenidoRechazo" label="Rechazo"
												value="#{treelogic.rechazo}" cols="250" rows="8"
												styleClass="contenidoOpinion" rendered="false"></fwn:TextArea>
										</fwn:OutputPanel>
									</fwn:OutputPanel>
								</fwn:OutputPanel>
							</fwn:facet>
						</fwn:PanelForm>

						<%-- View list of rejections (condicional, sólo si estado spark == 'rejected') --%>
						<fwn:AjaxButton
							value="#{msg['FIONA.store.integratorBuyer.detalle.viewRejections.valor']}"
							reRender="dialogoListRejection" id="abrirDialogoRejection"
							ajaxonclicklistener="AbrirCerrarDialogoRejection"
							styleClass="enlaceTabla" immediate="true"
							rendered="#{treelogic.status_id eq 4 or treelogic.status_id eq 3}" />

						<fwn:OutputPanel id="enlaceChangeStatus"
							styleClass="botoneraEnlaceBoton">
							<%--<fwn:Button action="changeStatus" styleClass="detailLink"
										value="#{msg['FIONA.store.globalAdministration.sparkInformation.status.botonChangeStatus']}">
									</fwn:Button> --%>
							<fwn:Button
								value="#{msg['FIONA.store.globalAdministration.sparkInformation.status.botonChangeStatus']}"
								id="abrirStatusC" styleClass="botonesInvitacion botonesInvitacionAzul" action="changeStatus">

							</fwn:Button>
						</fwn:OutputPanel>

					</fwn:Dialog>
					<%-- FIN Diálogo para cambio de status --%>
				</fwn:tab>
				<%-- Pestania de opiniones --%>
				<security:authorize ifAnyGranted="ROLE_USER">
					<fwn:tab
						label="#{msg['FIONA.store.integratorBuyer.listadoOpinion.titulo']}">
						<security:authorize ifAnyGranted="ROLE_USER">
							<%-- Nueva opinión de usuario --%>
							<fwn:AjaxButton
								value="#{msg['FIONA.store.integratorBuyer.sparkInformation.opinion.botonSend']}"
								reRender="dialogoOpinion" id="abrirDialogoOpinion"
								ajaxonclicklistener="AbrirCerrarDialogoOpinion"
								styleClass="botonesInvitacion float-right"
								rendered="#{treelogic.comprado eq '1'}">
							</fwn:AjaxButton>
						</security:authorize>
						<security:authorize ifAnyGranted="ROLE_USER">
							<%-- Texto para cuando no existen opiniones --%>
							<fwn:OutputPanel layout="block"
								rendered="#{empty(treelogic.lstOpiniones)}"
								styleClass="panelListOpiniones">
								<fwn:OutputText id="mensajeListaVacia"
									value="#{msg['FIONA.store.integratorBuyer.listadoOpinion.listadoVacio.valor']}">
								</fwn:OutputText>
							</fwn:OutputPanel>
							<%-- Listado de opiniones de usuarios --%>
							<fwn:OutputPanel layout="block"
								rendered="#{!empty(treelogic.lstOpiniones)}"
								styleClass="panelListOpiniones">
								<fwn:DataList value="#{treelogic.lstOpiniones}" var="opinion"
									layout="unorderedList" styleClass="tabla-listado-opinion"
									id="listadoOpiniones">
									<%-- Rate --%>
									<fwn:OutputPanel id="rateOpinion"
										styleClass="liSparksPrizeContent">
										<fwn:OutputPanel styleClass="ratingNE">
											<fwn:RadioSingle for="radioRateList" index="4"
												showLabel="true" />
											<fwn:RadioSingle for="radioRateList" index="3"
												showLabel="true" />
											<fwn:RadioSingle for="radioRateList" index="2"
												showLabel="true" />
											<fwn:RadioSingle for="radioRateList" index="1"
												showLabel="true" />
											<fwn:RadioSingle for="radioRateList" index="0"
												showLabel="true" />
											<fwn:Radio value="#{opinion.opinion_rate}" styleClass="ratingNE"
												layout="spread" id="radioRateList" disabled="true">																								
												<fwn:SelectItem itemValue="1" id="star1" />
												<fwn:SelectItem itemValue="2" id="star2" />
												<fwn:SelectItem itemValue="3" id="star3" />
												<fwn:SelectItem itemValue="4" id="star4" />
												<fwn:SelectItem itemValue="5" id="star5" />
											</fwn:Radio>
										</fwn:OutputPanel>
									</fwn:OutputPanel>
									<%-- Titulo --%>
									<fwn:OutputPanel id="tituloOpinion"
										styleClass="liSparksPrizeContent">
										<fwn:OutputText value="#{opinion.opinion_titulo}"
											styleClass="inputInvitacion" style="font-weight:bold;">
										</fwn:OutputText>
									</fwn:OutputPanel>
									<%-- Fecha --%>
									<fwn:OutputPanel id="fechaOpinion"
										styleClass="liSparksPrizeContent" style="font-size:13pt;">
										<fwn:OutputText value="#{opinion.opinion_fecha}" format="fecha"
											dateseparator="/" yearsformat="yyyy">
										</fwn:OutputText>
									</fwn:OutputPanel>
									<%-- Descripción --%>
									<fwn:OutputPanel id="descOpinion"
										styleClass="liSparksPrizeContent">
										<fwn:OutputText
											value="#{opinion.opinion_descripcion}"
											styleClass="opinionListado" style="font-size:13pt;word-wrap: break-word; word-break: break-all;">
											</fwn:OutputText>
									</fwn:OutputPanel>
								</fwn:DataList>
							</fwn:OutputPanel>
	
						</security:authorize>
	
						<%-- Diálogo que muestra el diálogo de introducción de datos para enviar una opinión --%>
						<fwn:Dialog
							header="#{msg['FIONA.store.integratorBuyer.sparkInformation.cabeceraPanelOpinion.valor']}"
							id="dialogoOpinion">
	
	
							<fwn:PanelForm id="formEnviarOpinion" tabOrder="horizontal">
								<fwn:facet name="column1">
									<fwn:OutputPanel>
										<%-- Valoración --%>
										<fwn:OutputPanel>
											<fwn:Label styleClass="inputInvitacion"
												value="#{msg['FIONA.store.integratorBuyer.sparkInformation.opinion.campoValoracion.valor']}"></fwn:Label>
										</fwn:OutputPanel>
										<%-- Rating Widget --%>
										<fwn:OutputPanel styleClass="rating">
	
											<fwn:RadioSingle for="radioRate" index="5" showLabel="true" />
											<fwn:RadioSingle for="radioRate" index="4" showLabel="true" />
											<fwn:RadioSingle for="radioRate" index="3" showLabel="true" />
											<fwn:RadioSingle for="radioRate" index="2" showLabel="true" />
											<fwn:RadioSingle for="radioRate" index="1" showLabel="true" />
											<fwn:RadioSingle for="radioRate" index="0" showLabel="true" />
	
											<fwn:Radio value="#{treelogic.valoracion}" styleClass="rating"
												layout="spread" id="radioRate">
												<fwn:SelectItem itemValue="0" id="star0" itemDisabled="true"/>
												<fwn:SelectItem itemValue="1" id="star1" />
												<fwn:SelectItem itemValue="2" id="star2" />
												<fwn:SelectItem itemValue="3" id="star3" />
												<fwn:SelectItem itemValue="4" id="star4" />
												<fwn:SelectItem itemValue="5" id="star5" />
											</fwn:Radio>
	
											<%--
												    <input type="radio" id="star5" name="rating" value="5" /><label for="star5" title="Rocks!">5 stars</label>
												    <input type="radio" id="star4" name="rating" value="4" /><label for="star4" title="Pretty good">4 stars</label>
												    <input type="radio" id="star3" name="rating" value="3" /><label for="star3" title="Meh">3 stars</label>
												    <input type="radio" id="star2" name="rating" value="2" /><label for="star2" title="Kinda bad">2 stars</label>
												    <input type="radio" id="star1" name="rating" value="1" /><label for="star1" title="Sucks big time">1 star</label>
												      --%>
										</fwn:OutputPanel>
										<%-- FIN Rating Widget --%>
										<%-- Título --%>
										<fwn:OutputPanel>
											<fwn:Label styleClass="inputInvitacion"
												value="#{msg['FIONA.store.integratorBuyer.sparkInformation.opinion.campoTitulo.valor']}"></fwn:Label>
										</fwn:OutputPanel>
										<fwn:Text id="titulo" value="#{treelogic.titulo}"
											required="true" maxlength="45" size="20"
											styleClass="inputInvitacion"></fwn:Text>
										<%-- Descripción --%>
										<fwn:OutputPanel>
											<fwn:Label for="contenidoOpinion"
												value="#{msg['FIONA.store.integratorBuyer.sparkInformation.opinion.campoDescripcion.valor']}"></fwn:Label>
										</fwn:OutputPanel>
										<fwn:TextArea id="contenidoOpinion" label="Opinion"
											value="#{treelogic.opinion}" cols="250" rows="8"
											styleClass="contenidoOpinion"></fwn:TextArea>
	
									</fwn:OutputPanel>
								</fwn:facet>
							</fwn:PanelForm>
							<fwn:OutputPanel id="enlaceSendOpinion"
								styleClass="botoneraEnlaceBoton">
								<fwn:Button action="sendOpinion"
									styleClass="botonesInvitacion botonesInvitacionAzul"
									value="#{msg['FIONA.store.integratorBuyer.sparkInformation.opinion.botonSend']}">
									<fwn:UpdateActionListener property="#{treelogic.spark_rating}"
										value="0">
									</fwn:UpdateActionListener>
								</fwn:Button>
							</fwn:OutputPanel>
	
	
						</fwn:Dialog>
						<%-- FIN Diálogo que muestra el diálogo de introducción de datos para enviar una opinión --%>
	
					</fwn:tab>
				</security:authorize>
			</fwn:tabPanel>
		</fwn:OutputPanel>
		<%-- ALERTS para confirmación de compra, trial y cambio de estado --%>

					<fwn:ModalAlert id="alertCompra"
						alertTitle="#{msg['FIONA.store.integratorBuyer.sparkBuy.cabeceraPanel.valor']}"
						title="#{msg['FIONA.store.integratorBuyer.sparkBuy.confirmarCompra.valor']}"
						type="confirm">
						<fwn:facet name="buttons">
							<fwn:OutputPanel>
								<fwn:Button action="buyOK" value="#{msg['FWN_Comun.botonOK']}"
									styleClass="botonesInvitacion" />
								<fwn:AjaxButton value="#{msg['FWN_Comun.botonNO']}"
									reRender="alertCompra" id="cerrarBuy"
									ajaxonclicklistener="AbrirCerrarAlert"
									styleClass="botonesInvitacion">
								</fwn:AjaxButton>
							</fwn:OutputPanel>
						</fwn:facet>
					</fwn:ModalAlert>
					
					<fwn:ModalAlert id="alertFree"
						alertTitle="#{msg['FIONA.store.integratorBuyer.sparkBuy.cabeceraPanel.valor']}"
						title="#{msg['FIONA.store.integratorBuyer.sparkBuy.confirmarCompra.valor']}"
						type="confirm">
						<fwn:facet name="buttons">
							<fwn:OutputPanel>
								<fwn:Button action="freeOK" value="#{msg['FWN_Comun.botonOK']}"
									styleClass="botonesInvitacion" />
								<fwn:AjaxButton value="#{msg['FWN_Comun.botonNO']}"
									reRender="alertFree" id="cerrarFree"
									ajaxonclicklistener="AbrirCerrarAlert"
									styleClass="botonesInvitacion">
								</fwn:AjaxButton>
							</fwn:OutputPanel>
						</fwn:facet>
					</fwn:ModalAlert>
					
					<fwn:ModalAlert id="alertErrorCompra"
						alertTitle="#{msg['FIONA.store.integratorBuyer.sparkErrorBuy.cabeceraPanel.valor']}"
						title="#{msg['FIONA.store.integratorBuyer.sparkErrorBuy.errorMsg.valor']}"
						type="error">						
					</fwn:ModalAlert>
					
					<fwn:ModalAlert id="alertUninstall"
						alertTitle="#{msg['FIONA.store.integratorBuyer.sparkUninstall.cabeceraPanel.valor']}"
						title="#{msg['FIONA.store.integratorBuyer.sparkUninstall.confirmarUninstall.valor']}"
						type="confirm">
						<fwn:facet name="buttons">
							<fwn:OutputPanel>
								<fwn:Button action="uninstallOK" value="#{msg['FWN_Comun.botonOK']}"
									styleClass="botonesInvitacion" />
								<fwn:AjaxButton value="#{msg['FWN_Comun.botonNO']}"
									reRender="alertUninstall" id="cerrarUninstall"
									ajaxonclicklistener="AbrirCerrarAlert"
									styleClass="botonesInvitacion">
								</fwn:AjaxButton>
							</fwn:OutputPanel>
						</fwn:facet>
					</fwn:ModalAlert>

					<fwn:ModalAlert id="alertTrial"
						alertTitle="#{msg['FIONA.store.integratorBuyer.sparkTrial.cabeceraPanel.valor']}"
						title="#{msg['FIONA.store.integratorBuyer.sparkTrial.confirmarCompra.valor']}"
						type="confirm">
						<fwn:facet name="buttons">
							<fwn:OutputPanel>
								<fwn:Button action="trialOK" value="#{msg['FWN_Comun.botonOK']}"
									styleClass="botonesInvitacion" />
								<fwn:AjaxButton value="#{msg['FWN_Comun.botonNO']}"
									reRender="alertTrial" id="cerrarTrial"
									ajaxonclicklistener="AbrirCerrarAlert"
									styleClass="botonesInvitacion">
								</fwn:AjaxButton>
							</fwn:OutputPanel>
						</fwn:facet>
					</fwn:ModalAlert>

					<fwn:ModalAlert id="alertStatus"
						alertTitle="#{msg['FIONA.store.management.sparkStatus.cabeceraPanel.valor']}"
						title="#{msg['FIONA.store.management.sparkStatus.confirmarCompra.valor']}"
						type="confirm">
						<fwn:facet name="buttons">
							<fwn:OutputPanel>
								<fwn:Button action="changeStatus"
									value="#{msg['FWN_Comun.botonOK']}"
									styleClass="botonesInvitacion" />
								<fwn:AjaxButton value="#{msg['FWN_Comun.botonNO']}"
									reRender="alertStatus" id="cerrarStatus"
									ajaxonclicklistener="AbrirCerrarAlert"
									styleClass="botonesInvitacion">
								</fwn:AjaxButton>
							</fwn:OutputPanel>
						</fwn:facet>
					</fwn:ModalAlert>
		<fwn:OutputPanel id="botonera" styleClass="separador">
			<fwn:OutputPanel layout="block">
				<fwn:Button action="back" value="" styleClass="fio-back-button"
					immediate="true" style="margin-left: -12%;">
					<fwn:UpdateActionListener property="#{treelogic.spark_criterio}" value="0">
					</fwn:UpdateActionListener>	
					<fwn:UpdateActionListener property="#{treelogic.pulsado_top}" value="TRUE">
					</fwn:UpdateActionListener>		
					<fwn:UpdateActionListener property="#{treelogic.pulsado_latest}" value="FALSE">
					</fwn:UpdateActionListener>
				</fwn:Button>
					<%--
				<fwn:AjaxButton
					value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonBuy.valor']}"
					reRender="alertCompra" id="abrirBuy"
					ajaxonclicklistener="AbrirCerrarAlert"
					styleClass="botonesInvitacion"
					rendered="#{treelogic.comprado ne '1' and treelogic.status_id eq 6 and treelogic.idTarifa ne 3}">
				</fwn:AjaxButton>
				<fwn:AjaxButton
					value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonUninstall.valor']}"
					reRender="alertUninstall" id="abrirUninstall"
					ajaxonclicklistener="AbrirCerrarAlert"
					styleClass="botonesInvitacion"
					rendered="#{treelogic.comprado eq '1'}">
				</fwn:AjaxButton>
				<fwn:AjaxButton
					value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonFree.valor']}"
					reRender="alertCompra" id="abrirFree"
					ajaxonclicklistener="AbrirCerrarAlert"
					styleClass="botonesInvitacion"
					rendered="#{treelogic.comprado ne '1' and treelogic.status_id eq 6 and treelogic.idTarifa eq 3}">
				</fwn:AjaxButton>
				<fwn:AjaxButton
					value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonTrial.valor']}"
					reRender="alertTrial" id="abrirTrial"
					ajaxonclicklistener="AbrirCerrarAlert"
					styleClass="botonesInvitacion"
					rendered="#{treelogic.esTrial eq '1' and treelogic.status_id eq 6 and treelogic.comprado eq '0'}">
				</fwn:AjaxButton>
				 --%>
			</fwn:OutputPanel>
		</fwn:OutputPanel>
	</fwn:Form>

</fwn:OutputPanel>