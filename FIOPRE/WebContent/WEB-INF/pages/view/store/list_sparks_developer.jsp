<%@page import="com.treelogic.fawna.presentacion.core.utilidades.FawnaPropertyConfiguration"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<%@page import="com.adelerobots.web.fiopre.utilidades.ConfigUtils"%>

<script>
jQuery("document").ready(function(){
	
	var jplist = jQuery("#tablaSparksDev").jplist({		
		items_box: ".tabla-listado-sparks-dev",
		item_path: ".tabla-listado-sparks-dev li",
		items_on_page: 8
	});
});
</script>

<%-- Contenido del listado --%>
<fwn:OutputPanel layout="block" styleClass="contenido">

	
	<%-- Formulario de búsqueda y listado --%>
	<fwn:Form id="developerListForm">
		<fwn:Messages showDetail="true" showSummary="false"
				ajaxRendered="false" style="color:red; padding-bottom:20px;"></fwn:Messages>		
		
		<fwn:Panel rendered="#{empty(treelogic.sparksDeveloper)}">
			<fwn:OutputText styleClass="mensajeListaVacia"
				value="#{msg['FWN_Comun.mensajeListaVacia']}">
			</fwn:OutputText>
		</fwn:Panel>
		
		<fwn:OutputPanel styleClass="pestaniaTitulo">
			<fwn:OutputText  value="#{msg['FIONA.store.management.listado.titulo']}"/>
		</fwn:OutputPanel>
		 <div class="tablaSparksDev" id="tablaSparksDev">		 
		 		
				 <fwn:DataList value="#{treelogic.sparksDeveloper}" var="spark" layout="orderedList"
		              		styleClass="tabla-listado-sparks-dev" id="listadoSparks">		              			               		
		               		<fwn:OutputPanel id="imgSpark" styleClass="liSparksPrizeContent">		               			
		               			<fwn:Button action="edit" styleClass="imageLink" rendered="#{!empty(spark.iconPath)}"
									value="" title="#{spark.nombre}" image="/fionasparkimages/#{spark.iconPath}">	              		
		               				<%--<fwn:Image value="/fionasparkimages/#{spark.iconPath}" height="78px" width="78px"/> --%>
		               				<fwn:UpdateActionListener property="#{treelogic.spark_id}"
										value="#{spark.spark_id}">
									</fwn:UpdateActionListener>
		               			</fwn:Button>
		               			<fwn:Button action="edit" styleClass="imageLink" rendered="#{empty(spark.iconPath)}"
									value="" title="#{spark.nombre}" image="/images/iconos/icono_fiona.png">	              		
		               				<%--<fwn:Image value="/fionasparkimages/#{spark.iconPath}" height="78px" width="78px"/> --%>
		               				<fwn:UpdateActionListener property="#{treelogic.spark_id}"
										value="#{spark.spark_id}">
									</fwn:UpdateActionListener>
		               			</fwn:Button>
		               		</fwn:OutputPanel>	               		
		               		<fwn:OutputPanel layout="block" styleClass="devLiContent">
			               		<fwn:OutputPanel id="enlaceDetalle" styleClass="panelEnlace">
			               			<fwn:ActionLink action="edit" styleClass="detailLink"
										value="#{spark.nombre}" title="#{spark.nombre}">
										<fwn:UpdateActionListener property="#{treelogic.spark_id}"
											value="#{spark.spark_id}">
										</fwn:UpdateActionListener>
									</fwn:ActionLink>
			               		</fwn:OutputPanel>
			               		<fwn:OutputPanel id="statusSpark" styleClass="liSparksPrizeContent" style="font-size:13pt;">
			               			<fwn:OutputText value="#{spark.status_nombre}"></fwn:OutputText> - 
			               			<fwn:OutputText value="#{spark.statusDate}" format="fecha" dateseparator="/" yearsformat="yyyy" />
			               		</fwn:OutputPanel>
			               		<fwn:OutputPanel layout="block" styleClass="botoneraDevLi">	
				               		<fwn:Button action="edit" styleClass="botonesDev"
										value="#{msg['FIONA.store.management.listado.accionEditar.valor']}">
										<fwn:UpdateActionListener property="#{treelogic.spark_id}"
											value="#{spark.spark_id}">
										</fwn:UpdateActionListener>
									</fwn:Button>
									<%-- Delete Button --%>
									<fwn:AjaxButton value="#{msg['FIONA.store.management.listado.accionDelete.valor']}" reRender="alertDelete"
									 id="abrirDelete" ajaxonclicklistener="AbrirCerrarAlert" styleClass="botonesDev">
									 	<fwn:UpdateActionListener property="#{treelogic.spark_id}"
											value="#{spark.spark_id}">
										</fwn:UpdateActionListener>				 	
									 </fwn:AjaxButton>
									 
									 <%-- Check button --%>	
									 <fwn:AjaxButton value="#{msg['FIONA.store.management.listado.accionCheck.valor']}" reRender="alertStatus"
									 id="abrirStatusC" ajaxonclicklistener="AbrirCerrarAlert" styleClass="botonesDev" rendered="#{spark.status_id eq 2}">
									 	<fwn:UpdateActionListener property="#{treelogic.spark_id}"
											value="#{spark.spark_id}">
										</fwn:UpdateActionListener>	
										<fwn:UpdateActionListener property="#{treelogic.idStatus}"
											value="7">						
										</fwn:UpdateActionListener>				 	
									 </fwn:AjaxButton>
									 
									 <%-- Publish button --%>	
									 <fwn:AjaxButton value="#{msg['FIONA.store.management.listado.accionPublish.valor']}" reRender="alertStatus"
									 id="abrirStatusP" ajaxonclicklistener="AbrirCerrarAlert" styleClass="botonesDev" rendered="#{spark.status_id eq 5}">
									 	<fwn:UpdateActionListener property="#{treelogic.spark_id}"
											value="#{spark.spark_id}">
										</fwn:UpdateActionListener>	
										<fwn:UpdateActionListener property="#{treelogic.idStatus}"
											value="6">						
										</fwn:UpdateActionListener>				 	
									 </fwn:AjaxButton>
									 
									  <%-- Unpublish button --%>	
									 <fwn:AjaxButton value="#{msg['FIONA.store.management.listado.accionUnpublish.valor']}" reRender="alertStatus"
									 id="abrirStatusU" ajaxonclicklistener="AbrirCerrarAlert" styleClass="botonesDev" rendered="#{spark.status_id eq 6}">
									 	<fwn:UpdateActionListener property="#{treelogic.spark_id}"
											value="#{spark.spark_id}">						
										</fwn:UpdateActionListener>
										<fwn:UpdateActionListener property="#{treelogic.idStatus}"
											value="5">						
										</fwn:UpdateActionListener>				 	
									 </fwn:AjaxButton>
									 
									  <%-- Overwrite button --%>	
									 <fwn:Button value="#{msg['FIONA.store.management.listado.accionOverwrite.valor']}" action="overwrite"
									 id="overwriteButton" styleClass="botonesDev" rendered="#{spark.status_id eq 2 or spark.status_id eq 3 or spark.status_id eq 4 or spark.status_id eq 5}"/>									 				 	
									 
								 </fwn:OutputPanel>
							 </fwn:OutputPanel>	               		
	         	</fwn:DataList>
	         	
	         	<div class="panel">
						<div id="paging">
							<div id="buttons"></div>
							<div id="info"></div>
							<%--<div class="drop-down" id="page-by">
								<ul>
									<li><span class="p8"> <fwn:OutputText value="#{msg['FIONA.store.management.listado.eightPerPage']}"/></span></li>
									<li><span class="p16"> <fwn:OutputText value="#{msg['FIONA.store.management.listado.sixteenPerPage']}"/></span></li>										
								</ul>
							</div> --%>
						</div>
				</div>
	         
	     </div>
		
         
		<fwn:OutputPanel id="botonera">        	
			<fwn:OutputPanel layout="block">
					<fwn:Button action="back" value="" styleClass="fio-back-button" />
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
		
	</fwn:Form>
	
	


</fwn:OutputPanel>