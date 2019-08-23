<%@page import="com.treelogic.fawna.presentacion.core.utilidades.FawnaPropertyConfiguration"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="security"%>

<%-- Contenido del listado --%>
<fwn:OutputPanel layout="block" styleClass="contenido">

		
	<%-- Formulario de búsqueda y listado --%>
	<fwn:Form>
		<fwn:Messages showDetail="true" showSummary="false"
				ajaxRendered="false"></fwn:Messages>		
		
		
		<fwn:OutputPanel styleClass="pestaniaTitulo">
			<fwn:OutputText  value="#{msg['FIONA.store.globalAdministration.listado.botonAllSparks.valor']}"/>
		 </fwn:OutputPanel>		
		 <fwn:OutputPanel layout="block" styleClass="tablaSparksInicio">
		 	<fwn:OutputPanel id="wrapListSparks" styleClass="wrapList" layout="block">			
				 <fwn:DataList value="#{treelogic.lstSparks}" var="spark" layout="orderedList"
		              		styleClass="tabla-listado-sparks-prize" id="listadoSparks">	 
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
			               		<fwn:OutputPanel id="enlaceDetalle" styleClass="panelEnlace">
			               			<fwn:ActionLink action="viewDetails" styleClass="detailLink"
										value="#{spark.nombre}" title="#{spark.nombre}">
										<fwn:UpdateActionListener property="#{treelogic.spark_id}"
											value="#{spark.spark_id}">
										</fwn:UpdateActionListener>
									</fwn:ActionLink>
			               		</fwn:OutputPanel>
			               		<fwn:OutputPanel id="compradoSpark" styleClass="liSparksPrizeContent multiButtonBordered" rendered="#{spark.comprado eq '0'}">		               			
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
									rendered="#{spark.comprado eq '1'}" styleClass="multiButton">
									</fwn:Button>
								</fwn:OutputPanel>
							</fwn:OutputPanel>
		               				               				               		
	         	</fwn:DataList>        
	         </fwn:OutputPanel>
	     </fwn:OutputPanel>
         
		<fwn:OutputPanel id="botonera">        	
			<fwn:OutputPanel layout="block">
					<fwn:Button action="back" value="" styleClass="fio-back-button" />
			</fwn:OutputPanel>
        </fwn:OutputPanel>
		
	</fwn:Form>
	
	


</fwn:OutputPanel>