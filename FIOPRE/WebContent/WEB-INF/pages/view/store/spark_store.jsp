<%@page import="com.treelogic.fawna.presentacion.core.utilidades.FawnaPropertyConfiguration"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>



<%-- Contenido del listado --%>
<fwn:OutputPanel layout="block" >

<%-- título del panel --%>
<%--
	<fwn:OutputPanel layout="block" styleClass="tituloPagina">
		<fwn:OutputText
			value="#{msg['FIONA.store.integratorBuyer.listado.titulo']}"></fwn:OutputText>
	</fwn:OutputPanel>  --%>
	
	<%-- Formulario de búsqueda y listado --%>
	<fwn:Form>
		<fwn:Messages showDetail="true" showSummary="false"
				ajaxRendered="false"></fwn:Messages>
			<%--
			<fwn:OutputPanel  layout="block" id="panelBanners" style="text-align:center;">
				<fwn:DataTable id="tablaBanners" rows="1" 
				 value="#{treelogic.listadoBanners}" var="spark"
				reRender="bannerScI" scrollersId="bannerScI"				
				rendered="#{!empty(treelogic.listadoBanners)}" styleClass="listBanners">
					<fwn:Column>
						<fwn:Image value="/fionasparkimages/#{spark.usermd5}/banners/#{spark.bannerPath}" styleClass="bannerimg">
						</fwn:Image>
					</fwn:Column>
				</fwn:DataTable>
				<fwn:DataScroller for="tablaBanners" maxPages="5" styleClass="scrollBanners"
				id="bannerScI" reRender="tablaBanners" renderIfSinglePage="false"
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
				 --%>
				 
			<div id="slideshow" style="text-align:center;">
			
				<fwn:DataList value="#{treelogic.listadoBanners}" var="spark" layout="unorderedList"
		              		styleClass="listBanners" id="tablaBanners">              			               			
               			<fwn:Image value="/fionasparkimages/#{spark.usermd5}/banners/#{spark.bannerPath}" styleClass="bannerimg">
						</fwn:Image>
						<%-- Amago para hacer clickables los banners que impide que se muevan            
						<fwn:Button action="viewDetails" styleClass="bannerimg"
							value="" title="#{spark.nombre}" image="/fionasparkimages/#{spark.usermd5}/banners/#{spark.bannerPath}">
	             					<fwn:UpdateActionListener property="#{treelogic.spark_id}"
								value="#{spark.spark_id}">
							</fwn:UpdateActionListener>
	             		</fwn:Button>--%> 		
	             		 
		        </fwn:DataList>
		        
		        <span class="arrow previous"></span>
	    		<span class="arrow next"></span>
				
			</div>
		
			<fwn:OutputPanel  layout="block" styleClass="panelBusquedaSparks">	
																			
				<fwn:Text id="keywords_busqueda" label="Cuenta" styleClass="alignRight"
					value="#{treelogic.keywords}"></fwn:Text>
				<fwn:Button action="search" styleClass="botonesInvitacion alignRight"
					value="#{msg['FIONA.store.integratorBuyer.listado.botonBuscar.valor']}">
					<%--<fwn:UpdateActionListener property="#{e2000.pulsado}"
						value="#{msg['MAA_Gestion_ParticipesFlow.listado.botonLimpiar.flag.valor']}">
					</fwn:UpdateActionListener> --%>
				</fwn:Button>		
				
									
			</fwn:OutputPanel>
					
		
		
		<!-- Botonera superior -->
		<fwn:OutputPanel layout="block" styleClass="boton-row botonera_superior">
					
        	<fwn:Button action="top" styleClass="botonesInvitacionPestania"
				value="#{msg['FIONA.store.integratorBuyer.listado.botonTop.valor']}" rendered="#{treelogic.pulsado_top eq 'FALSE'}">			
				<fwn:UpdateActionListener property="#{treelogic.spark_criterio}" value="0">
				</fwn:UpdateActionListener>
				<fwn:UpdateActionListener property="#{treelogic.pulsado_top}" value="TRUE">
				</fwn:UpdateActionListener>			
				<fwn:UpdateActionListener property="#{treelogic.pulsado_latest}" value="FALSE">
				</fwn:UpdateActionListener>	
				<fwn:UpdateActionListener property="#{treelogic.pulsado_basic}" value="FALSE">
				</fwn:UpdateActionListener>					
			</fwn:Button>
			
			<fwn:Button action="top" styleClass="botonesInvitacionPestaniaPulsados"
				value="#{msg['FIONA.store.integratorBuyer.listado.botonTop.valor']}" rendered="#{treelogic.pulsado_top eq 'TRUE' || empty(treelogic.pulsado_top)}">			
				<fwn:UpdateActionListener property="#{treelogic.spark_criterio}" value="0">
				</fwn:UpdateActionListener>
				<fwn:UpdateActionListener property="#{treelogic.pulsado_top}" value="TRUE">
				</fwn:UpdateActionListener>			
				<fwn:UpdateActionListener property="#{treelogic.pulsado_latest}" value="FALSE">
				</fwn:UpdateActionListener>	
				<fwn:UpdateActionListener property="#{treelogic.pulsado_basic}" value="FALSE">
				</fwn:UpdateActionListener>					
			</fwn:Button>
			
			<fwn:Button action="latest" styleClass="botonesInvitacionPestania" rendered="#{treelogic.pulsado_latest eq 'FALSE' || empty(treelogic.pulsado_latest)}"
				value="#{msg['FIONA.store.integratorBuyer.listado.botonLatest.valor']}">
				<fwn:UpdateActionListener property="#{treelogic.spark_criterio}" value="1">
				</fwn:UpdateActionListener>
				<fwn:UpdateActionListener property="#{treelogic.pulsado_latest}" value="TRUE">
				</fwn:UpdateActionListener>	
				<fwn:UpdateActionListener property="#{treelogic.pulsado_top}" value="FALSE">
				</fwn:UpdateActionListener>		
				<fwn:UpdateActionListener property="#{treelogic.pulsado_basic}" value="FALSE">
				</fwn:UpdateActionListener>					
			</fwn:Button>
			
			<fwn:Button action="latest" styleClass="botonesInvitacionPestaniaPulsados" rendered="#{treelogic.pulsado_latest eq 'TRUE'}"
				value="#{msg['FIONA.store.integratorBuyer.listado.botonLatest.valor']}">
				<fwn:UpdateActionListener property="#{treelogic.spark_criterio}" value="1">
				</fwn:UpdateActionListener>
				<fwn:UpdateActionListener property="#{treelogic.pulsado_latest}" value="TRUE">
				</fwn:UpdateActionListener>	
				<fwn:UpdateActionListener property="#{treelogic.pulsado_top}" value="FALSE">
				</fwn:UpdateActionListener>
				<fwn:UpdateActionListener property="#{treelogic.pulsado_basic}" value="FALSE">
				</fwn:UpdateActionListener>											
			</fwn:Button>
			<fwn:Button action="basics" styleClass="botonesInvitacionPestania" rendered="#{treelogic.pulsado_basic eq 'FALSE' || empty(treelogic.pulsado_basic)}"
				value="#{msg['FIONA.store.integratorBuyer.listado.botonBasics.valor']}">
				<fwn:UpdateActionListener property="#{treelogic.spark_criterio}" value="2">
				</fwn:UpdateActionListener>
				<fwn:UpdateActionListener property="#{treelogic.pulsado_latest}" value="FALSE">
				</fwn:UpdateActionListener>	
				<fwn:UpdateActionListener property="#{treelogic.pulsado_top}" value="FALSE">
				</fwn:UpdateActionListener>
				<fwn:UpdateActionListener property="#{treelogic.pulsado_basic}" value="TRUE">
				</fwn:UpdateActionListener>						
			</fwn:Button>
			
			<fwn:Button action="basics" styleClass="botonesInvitacionPestaniaPulsados" rendered="#{treelogic.pulsado_basic eq 'TRUE'}"
				value="#{msg['FIONA.store.integratorBuyer.listado.botonBasics.valor']}">
				<fwn:UpdateActionListener property="#{treelogic.spark_criterio}" value="2">
				</fwn:UpdateActionListener>
				<fwn:UpdateActionListener property="#{treelogic.pulsado_latest}" value="FALSE">
				</fwn:UpdateActionListener>	
				<fwn:UpdateActionListener property="#{treelogic.pulsado_top}" value="FALSE">
				</fwn:UpdateActionListener>			
				<fwn:UpdateActionListener property="#{treelogic.pulsado_basic}" value="TRUE">
				</fwn:UpdateActionListener>			
			</fwn:Button>
			<%--
			<fwn:Button action="basics" styleClass="botonesInvitacion"
				value="#{msg['FIONA.store.integratorBuyer.listado.botonBasics.valor']}">
				<fwn:UpdateActionListener property="#{treelogic.spark_criterio}" value="2">
				</fwn:UpdateActionListener>					
			</fwn:Button>  --%>
					
		</fwn:OutputPanel>
		<%-- Mensaje para cuando la lista esté vacía --%>
		<%--
		<fwn:Panel rendered="#{empty(treelogic.sparks)}">
			<fwn:OutputText styleClass="mensajeListaVacia"
				value="#{msg['FIONA.generico.listadoVacio.mensaje.valor']}">
			</fwn:OutputText>
		</fwn:Panel> --%>
		
		<!-- Gratuitos -->
		 <fwn:OutputPanel layout="block" styleClass="tablaSparksInicio">
		 		<fwn:OutputPanel id="wrapListSparks" styleClass="wrapList" layout="block">			
					 <fwn:DataList value="#{treelogic.lstInitialSparks}" var="spark" layout="unorderedList"
			              		styleClass="tabla-listado-sparks-prize" id="listadoFreeSparks">	 
			              		<fwn:OutputPanel id="imgSpark" styleClass="liSparksPrizeContent">		               			
			               			<fwn:Button action="viewDetails" styleClass="imageLink" rendered="#{!empty(spark.iconPath)}"
										value="" title="#{spark.nombre}" image="/fionasparkimages/#{spark.iconPath}">	              		
		               					<%--<fwn:Image value="/fionasparkimages/#{spark.iconPath}" height="78px" width="78px"/> --%>
		               					<fwn:UpdateActionListener property="#{treelogic.spark_id}"
											value="#{spark.spark_id}">
										</fwn:UpdateActionListener>
		               				</fwn:Button>
		               				<fwn:Button action="viewDetails" styleClass="imageLink" rendered="#{empty(spark.iconPath)}"
										value="" title="#{spark.nombre}" image="/images/iconos/icono_fiona.png">	              		
		               					<%--<fwn:Image value="/fionasparkimages/#{spark.iconPath}" height="78px" width="78px"/> --%>
		               					<fwn:UpdateActionListener property="#{treelogic.spark_id}"
											value="#{spark.spark_id}">
										</fwn:UpdateActionListener>
		               				</fwn:Button>
			               		</fwn:OutputPanel>
			               		<fwn:OutputPanel styleClass="rightLiContent">		               		
				               		<fwn:OutputPanel id="enlaceDetalle" styleClass="panelEnlace" layout="block">
				               			<fwn:ActionLink value="#{spark.nombre}" action="viewDetails" styleClass="titleLink" title="#{spark.nombre}">
				               				<fwn:UpdateActionListener property="#{treelogic.spark_id}"
												value="#{spark.spark_id}">
											</fwn:UpdateActionListener>
				               			</fwn:ActionLink>			               			
				               		</fwn:OutputPanel>		               			               			
										
									<fwn:OutputPanel id="compradoSpark" styleClass="liSparksPrizeContent multiButtonBordered" rendered="#{spark.comprado eq '0'}">
										<%--<fwn:Label rendered="#{spark.comprado eq '1'}" styleClass="labelComprado"
											value="#{msg['FIONA.store.integratorBuyer.comprado.valor']}"></fwn:Label> --%>		               			
										<fwn:Button value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonFree.valor']}" action="getFree"
										rendered="#{spark.idTarifa eq 3 and spark.comprado eq '0'}" styleClass="multiButton">
											<fwn:UpdateActionListener property="#{treelogic.spark_id}"
												value="#{spark.spark_id}">
											</fwn:UpdateActionListener>
										</fwn:Button>
										<fwn:Button value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonBuy.valor']}" action="buy"
										rendered="#{spark.idTarifa ne 3 and spark.comprado eq '0'}" styleClass="multiButton">
											<fwn:UpdateActionListener property="#{treelogic.spark_id}"
												value="#{spark.spark_id}">
											</fwn:UpdateActionListener>
										</fwn:Button>
				               		</fwn:OutputPanel>
				               		<fwn:OutputPanel id="compradoSparkGrey" styleClass="liSparksPrizeContent multiButtonGreyBordered" rendered="#{spark.comprado eq '1'}"> 
										<fwn:Button value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonUsing.valor']}" disabled="true" 
										rendered="#{spark.comprado eq '1'}" styleClass="multiButtonGrey">
										</fwn:Button>
				               		</fwn:OutputPanel>
			               		</fwn:OutputPanel>
		         	</fwn:DataList>
	         	</fwn:OutputPanel>
	         	<fwn:OutputPanel id="enlaceAllFree" styleClass="botoneraEnlace" layout="block">
		        	<fwn:Button action="viewAll" styleClass="detailLink"
						value="#{msg['FIONA.store.integratorBuyer.listado.viewAll.valor']}">
						<fwn:UpdateActionListener property="#{treelogic.spark_free}"
										value="1">
						</fwn:UpdateActionListener>				
					</fwn:Button>
				</fwn:OutputPanel>
	         
	        
	         <!-- De pago -->
	         <%--
	         <fwn:Panel header="#{msg['FIONA.store.integratorBuyer.listadoPaid.cabecera.valor']}"
				collapsable="false" styleClass="listadoSparksReducidoPaid">
				
				 <fwn:DataList value="#{treelogic.paidSparks}" var="spark" layout="unorderedList"
		              		styleClass="tabla-listado-sparks" id="listadoPaidSparks">	               		
		               		<fwn:OutputPanel id="nombreSpark" styleClass="liSparksContent">
		               			<fwn:Label
									value="#{spark.nombre}"></fwn:Label>
								<fwn:Label rendered="#{spark.comprado eq '1'}" styleClass="labelCompradoRight"
									value="#{msg['FIONA.store.integratorBuyer.comprado.valor']}"></fwn:Label>
		               		</fwn:OutputPanel>		               	
		               		<fwn:OutputPanel id="enlaceDetalle" styleClass="panelEnlace">
		               			<fwn:Button action="viewDetails" styleClass="detailLink"
									value="#{msg['FIONA.store.integratorBuyer.listado.accionesCol.valor']}">
									<fwn:UpdateActionListener property="#{treelogic.spark_id}"
										value="#{spark.spark_id}">
									</fwn:UpdateActionListener>
								</fwn:Button>
		               		</fwn:OutputPanel>
	         	</fwn:DataList> 
	         	<fwn:OutputPanel id="enlaceAllPaid" styleClass="botoneraEnlace">
		        	<fwn:Button action="viewAll" styleClass="detailLink"
						value="#{msg['FIONA.store.integratorBuyer.listado.viewAll.valor']}">		
						<fwn:UpdateActionListener property="#{treelogic.spark_free}"
										value="0">
						</fwn:UpdateActionListener>			
					</fwn:Button>
				</fwn:OutputPanel>
	         </fwn:Panel>
	         --%>
	     </fwn:OutputPanel>
          
		<fwn:OutputPanel id="botonera">        	
			<fwn:OutputPanel layout="block" styleClass="separador">
					<fwn:Button action="back" value="" styleClass="fio-back-button" />
					<%-- En un futuro securizado para que aparezc sólo para el integrator --%>
					<fwn:Button action="buyMovements" styleClass="botonesInvitacion"
						value="#{msg['FIONA.store.integratorBuyer.listado.botonBuyMovements.valor']}">								
					</fwn:Button>
					<%-- En un futuro securizado para que aparezc sólo para el global administration --%>
					<security:authorize ifAnyGranted="ROLE_ADMIN">
						<fwn:Button action="allSparks" styleClass="botonesInvitacion"
							value="#{msg['FIONA.store.globalAdministration.listado.botonAllSparks.valor']}">								
						</fwn:Button>
					</security:authorize>
					
					<security:authorize ifAnyGranted="ROLE_USER">
						<fwn:Button action="listSparksDeveloper" styleClass="botonesInvitacion"
							value="#{msg['FIONA.store.management.listado.botonSparksDeveloper.valor']}" 
							rendered="#{treelogic.sizeLstDevelopedSparks ne '0'}">								
						</fwn:Button>					
						
						<fwn:AjaxButton value="#{msg['FIONA.store.management.listado.botonSparksDeveloper.valor']}" reRender="alertNoDevelopedSparks"
							 id="abrirNoDevelopedSparks" ajaxonclicklistener="AbrirCerrarAlert" styleClass="botonesInvitacion" 
							 rendered="#{treelogic.sizeLstDevelopedSparks eq '0'}">				 	
						</fwn:AjaxButton>
					</security:authorize>
			</fwn:OutputPanel>			
        </fwn:OutputPanel>
        
        <fwn:ModalAlert id="alertNoDevelopedSparks" alertTitle="#{msg['FIONA.store.management.listado.alertDevelopedsparks.title']}" title="#{msg['FIONA.store.management.listado.alertDevelopedsparks.valor']}" type="info">		 
		</fwn:ModalAlert>	
		
	</fwn:Form>
	
	


</fwn:OutputPanel>