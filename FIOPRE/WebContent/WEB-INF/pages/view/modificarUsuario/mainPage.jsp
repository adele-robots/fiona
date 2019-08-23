<%@page import="com.adelerobots.web.fiopre.utilidades.ConfigUtils"%>
<%@page import="com.adelerobots.web.fiopre.utilidades.ContextUtils"%>
<%@page import="com.adelerobots.web.fiopre.utilidades.AvatarUtils"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>

<fwn:OutputText>
<div id="fb-root"></div>
<script type="text/javascript"><!--
//Register facebook sharer widgets

function sleep(milliseconds) {
  var start = new Date().getTime();
  for (var i = 0; i < 1e7; i++) {
    if ((new Date().getTime() - start) > milliseconds){
      break;
    }
  }
}

(function(d,s,id) {
	var js, fjs=d.getElementsByTagName(s)[0];
	if (d.getElementById(id)) return;
	js=d.createElement(s);
	js.id=id;
	js.src="//connect.facebook.net/en_US/all.js#xfbml=1";
	fjs.parentNode.insertBefore(js,fjs);
})(document,'script','facebook-jssdk');
//Register twitter sharer widgets
(function(d,s,id) {
	var js, fjs=d.getElementsByTagName(s)[0];
	if (d.getElementById(id)) return;
	js=d.createElement(s);
	js.id=id;
	js.src="//platform.twitter.com/widgets.js";
	fjs.parentNode.insertBefore(js,fjs);
})(document,'script','twitter-wjs');

//Init sharer widgets
(function($){
	function fbs_click(event) {
		u=location.href;
		t=document.title;
		window.open('http://www.facebook.com/sharer.php?u='+encodeURIComponent(u)+'&t='+encodeURIComponent(t),'sharer','toolbar=0,status=0,width=626,height=436');
		return false;
	}
	$(document).ready(function(){
		jQuery('#umldiagram').css('width', (screen.width-680-(screen.width/12))+'px');

		console.log('ancho: '+screen.width);
		console.log(screen.width-640-(screen.width/12));
		jQuery("a.fb-sharer").bind('click', fbs_click);
		jQuery('#umldiagram img').one('load', function() {
		  jQuery('#umldiagram img').css('margin-top', (jQuery('#umldiagram').height() - jQuery('#umldiagram img').height()) / 2);
		}).each(function() {
		  if(this.complete) $(this).load();
		});
						
	});
})(jQuery);
//--></script>
</fwn:OutputText>


<fwn:OutputPanel id="mainbotonera" styleClass="fio-botones-nav" layout="block">
	<%--<a href="https://twitter.com/intent/tweet?button_hashtag=Sparking2gether&text=Visit" class="twitter-hashtag-button contenedorLike" data-related="Sparking2gether,Sparking2gether" data-url="http://www.sparkingtogether.com">Tweet #Sparking2gether</a>--%>
	<a href="https://twitter.com/share" class="twitter-share-button contenedorLike" data-url="http://www.sparkingtogether.com" data-text="I'm creating an avatar in Fiona! Do you wanna try it?" data-via="Sparking2gether" data-related="Sparking2gether" data-lang="en">Tweet</a>	
	<div class="fb-like contenedorLike" data-href="http://www.sparkingtogether.com" data-send="false" data-layout="button_count" data-width="450" data-show-faces="false" data-action="recommend" ></div>
	<%--<a class="fb-sharer contenedorLike" href="http://www.facebook.com/sharer.php?u=http://www.sparkingtogether.com" target="_blank"><img src="${request.contextPath}/images/compartir/Facebook-icon.png" alt="Share on Facebook" /></a>--%>
	<fwn:Form>
		<fwn:Button styleClass="fio-sendInvitations-button" value="" action="sendInvitations"></fwn:Button>
		<%--<fwn:Button styleClass="fio-scriplet-button" value="" action="getScriplet"></fwn:Button>  --%>
		<fwn:AjaxButton reRender="dialogoScriptlet"	 id="abrirDialogoScriptlet" ajaxonclicklistener="GetScriptlet" styleClass="fio-scriplet-button">				 	
		</fwn:AjaxButton>
		<%--<fwn:Button styleClass="fio-produccion-button" value="" action="produccion"></fwn:Button>--%>
		<%--<fwn:AjaxButton id="produccionButton" ajaxonclicklistener="GetAvatarPrice" 
						styleClass="fio-produccion-button" reRender="dialogoPrecios"/> --%>						
		<fwn:AjaxButton id="produccionButton" ajaxonclicklistener="RecalcularPrecioProduccionListener"		
						styleClass="fio-produccion-button" reRender="dialogoPrecios,panelPreciosTime, panelPreciosUso, datosCalculoPrecioTiempo, datosCalculoPrecioUso, datosCalculoPrecioTotal"/>
		<fwn:Button styleClass="fio-sparklink-button" value="" action="builder"></fwn:Button>		
		
		
		<%-- Diálogo para el sciplet --%>
		<fwn:Dialog header="#{msg['FIONA.scriptlet.titulo.valor']}" id="dialogoScriptlet" size="normal">
		  
			<fwn:OutputPanel>				
				
					<%-- <fwn:Text id="sparkrenderUrl" label="Url" value="#{treelogic.url}"
					required="true" maxlength="50" size="20"></fwn:Text>--%>					
						
				<fwn:OutputPanel layout="block">								 
					<fwn:BooleanCheck id="checkPublished" value="#{treelogic.isPublished}" event="onclick"  ajaxChangeListener="GetScriptlet">
					</fwn:BooleanCheck>
					<fwn:OutputText value="#{msg['FIONA.scriptlet.checkPublished.valor']}" style="font-size:14pt;"/>
					<fwn:OutputText value="#{msg['FIONA.scriptlet.warn.valor']}" style="font-size:10pt;"/>
				</fwn:OutputPanel>							
				
			</fwn:OutputPanel>
			
			<fwn:tabPanel switchType="client">	
				<fwn:tab label="#{msg['FIONA.scriptlet.tabs.getScriptlet']}" id="tabScriptlet">
					<fwn:OutputPanel style="">
						<fwn:TextArea id="fionaScript" rendered="true" style="width:100%; height:170px; font-size:14px; margin-bottom:5px;float:left;margin-right:8px;"></fwn:TextArea>																
					</fwn:OutputPanel>						
				</fwn:tab>
				<fwn:tab label="#{msg['FIONA.scriptlet.tabs.wordpress']}" id="tabWordpress">					
					<fwn:PanelForm id="formWordpress" tabOrder="horizontal">
						<fwn:facet name="column1">
							<fwn:OutputPanel>
								<fwn:OutputPanel>
									<fwn:Label for="usermailField" value="#{msg['FIONA.scriptlet.tabs.wordpress.usermail']}" styleClass="font12"></fwn:Label>
								</fwn:OutputPanel>
								<fwn:OutputPanel>	
									<fwn:OutputText id="usermailField" styleClass="font12"></fwn:OutputText>											
								</fwn:OutputPanel>
								<fwn:OutputPanel>
									<fwn:Label for="usrid1Field" value="#{msg['FIONA.scriptlet.tabs.wordpress.usrid1']}" styleClass="font12"></fwn:Label>
								</fwn:OutputPanel>
								<fwn:OutputPanel>
									<fwn:OutputText id="usrid1Field" styleClass="font12"></fwn:OutputText>											
								</fwn:OutputPanel>
								<fwn:OutputPanel>
									<fwn:Label for="usrid2Field" value="#{msg['FIONA.scriptlet.tabs.wordpress.usrid2']}" styleClass="font12"></fwn:Label>
								</fwn:OutputPanel>
								<fwn:OutputPanel>	
									<fwn:OutputText id="usrid2Field" styleClass="font12"></fwn:OutputText>											
								</fwn:OutputPanel>
							</fwn:OutputPanel>
						</fwn:facet>
					</fwn:PanelForm>																
				</fwn:tab>	
				<fwn:OutputText value="#{msg['FIONA.scriptlet.warn.valor']}" style="font-size:10pt;"/>			
			</fwn:tabPanel>
			
			
			
		  
		</fwn:Dialog>	
		<%-- FIN Diálogo para el sciplet --%>
		
		
	</fwn:Form>

</fwn:OutputPanel>

<fwn:Messages showDetail="true" showSummary="false" ajaxRendered="false" />


<fwn:OutputPanel layout="block" styleClass="panelAvatar">
	
	<fwn:OutputPanel id="umldiagram" layout="block" styleClass="panelAvatarUML">
		<fwn:Image value="/fionasparkimages/#{treelogic.SECURE_USERMAILD5}_#{treelogic.USER_CONFIG}/out.png?rand=#{treelogic.random}"></fwn:Image>
	</fwn:OutputPanel>

	<fwn:OutputPanel id="players" layout="block" styleClass="panelAvatarPlayers panelAvatarPlayers-flame">
		
	</fwn:OutputPanel>
	
</fwn:OutputPanel>

<fwn:OutputPanel id="panelRender" layout="block">
	
	<%-- Panel con los text area para el chatspark --%><fwn:OutputPanel id="panelChatWrap" layout="block">
	<fwn:OutputPanel id="panelChat" styleClass="panelChat" rendered="#{(not empty treelogic.USER_PID) and (treelogic.USER_PID != '0000') and (treelogic.USER_PID_CHAT) }" layout="block">
		<fwn:Form id="chatForm">
			<%-- <fwn:Label for="respuestasTA" value="Rebecca answers..." styleClass="lbltextAreaChatRespuesta" />
			<fwn:Label for="chatTA" value="Your question is..." styleClass="lbltextAreaChat" /> --%>
			<%-- Text area para las respuestas del gestor --%>
			<%-- <fwn:TextArea id="respuestasTA" label="Rebecca" disabled="true" 
					styleClass="textAreaChatRespuesta" />--%>
			<fwn:OutputText id="respuestasTA" escape="false"
					styleClass="textAreaChatRespuesta" />
			<%-- Text area para la entrada del usuario --%>
			<fwn:TextArea id="chatTA" label="Your question" 
					styleClass="textAreaChat" title="Your question" />
			<fwn:AjaxButton id="sendMessage" 
					ajaxonclicklistener="EnviarMensajeChatListener" reRender="errconsolenfo1,errconsolestream,panelChatWrap"
					styleClass="fio-send-button" oncomplete="setFocusOnChatTA();" style="display:none;"/>  
			<fwn:Messages for="chatTA" showDetail="true" showSummary="false" ajaxRendered="true" styleClass="textAreaChatMessages" />
		</fwn:Form>
		<fwn:OutputText escape="false"><script type="text/javascript">
		function setFocusOnChatTA() {
			jQuery("textarea[name='chatForm:chatTA']:first").focus();
		}
		jQuery("textarea[name='chatForm:chatTA']").shiftenter({
			//focusClass: 'textAreaChatHint', 
			//inactiveClass: 'textAreaChatHint-inactive', 
			hint: 'Write a question and press Enter', 
			metaKey: 'shift',
			submitCallback: function($el) {
				var submitButton = jQuery("input[name='chatForm:sendMessage']");
				submitButton.click();
			}
		});
		</script></fwn:OutputText>
		<f:verbatim><div style="clear:both"></div></f:verbatim>
	</fwn:OutputPanel></fwn:OutputPanel>
	<fwn:Hidden id="randomRoom" rendered="true" value="#{treelogic.randomRoom}"></fwn:Hidden>
	<fwn:OutputPanel id="configurationPanel" styleClass="fio-botones-run" layout="block" rendered="#{treelogic.SECURE_USER_ACCOUNTTYPE_ID > 2 }">
		<fwn:Messages showDetail="true" showSummary="false" for="configName" 
			ajaxRendered="false" style="color:red;"/>
			
		<fwn:Form id="configForm">
			<%-- Polling para cargar las configuraciones al cargarse la p�gina y renombrarlas--%>
			<fwn:poll id="pollInsertConfig" ajaxListener="MostrarUsuarioConfigsListener" interval="1" ajaxSingle="true" enabled="true" reRender="configurationPanel"/>
			<fwn:OutputPanel id="configComboWrap">
				<fwn:Combo id="config" orderedItems="false" event="onchange" ajaxChangeListener="ChangeConfigListener"
					showOptionSeleccionar="false" disabled="#{(not empty treelogic.USER_PID) and (treelogic.USER_PID != '0000') and (treelogic.USER_PID != '99991')}"
					reRender="umldiagram, configurationPanel" value="#{treelogic.USER_CONFIG}" styleClass="fio-config-combo" style="float:left"/>
			</fwn:OutputPanel>
			<fwn:AjaxButton id="renameConfigButton" ajaxonclicklistener="MostrarRenameConfigInputListener" style="float:left"
				styleClass="fio-rename-button" reRender="configurationPanel" rendered="true" oncomplete="document.getElementById('configForm:configName').value=document.getElementById('configForm:config').childNodes[document.getElementById('configForm:config').getValue()].textContent">
			</fwn:AjaxButton>
			<fwn:OutputPanel id="renameConfigInput" rendered="false">
				<script type="text/javascript">document.getElementById('configForm:configName').focus();</script>
				<fwn:Text size="20" id="configName" styleClass="fio-configname-input" style="float:left" maxlength="20"
					onkeydown="if (event.keyCode == 13){event.preventDefault();document.getElementById('configForm:btnRename').click();}">
				</fwn:Text>
				<fwn:AjaxButton ajaxonclicklistener="RenameConfigListener" reRender="configurationPanel" type="submit"
					id="btnRename" value=" " styleClass="fio-rename-button" style="float:left;display:none"></fwn:AjaxButton>
			</fwn:OutputPanel>
		</fwn:Form>
	</fwn:OutputPanel>
		
	<fwn:OutputPanel id="botonera" styleClass="fio-botones-run" layout="block">
		<fwn:Form id="avatarProcessForm">
		<%--
			<fwn:AjaxButton id="getAvatarPrice" ajaxonclicklistener="GetAvatarPrice" 
						styleClass="fio-price-button" reRender="dialogoPrecios"/>--%>	
		<%--
			<fwn:AjaxButton id="getAvatarPrice" ajaxonclicklistener="RecalcularPrecioProduccionListener" 
						styleClass="fio-price-button" reRender="dialogoPrecios,panelPreciosTime, panelPreciosUso, datosCalculoPrecioTiempo, datosCalculoPrecioUso, datosCalculoPrecioTotal"/>
		--%>
			<fwn:AjaxButton id="startAvatarProcess" ajaxonclicklistener="GenerarRandomRoomListener" 
				styleClass="fio-start-button" reRender="randomRoom" rendered="true" oncomplete="startAvatarPlayers(#{treelogic.USER_VIDEO_INPUT}, #{treelogic.USER_AUDIO_INPUT});"/>
			<fwn:AjaxButton id="hiddenStartButton" rendered="true" style="display:none;" ajaxonclicklistener="EjecutarProcesoAvatar" reRender="botonera,panelChatWrap,pruebaId,errconsoleForm,alertSparksInactivos,configComboWrap">
			</fwn:AjaxButton>
			<script type="text/javascript">document.getElementById("avatarProcessForm:hiddenStartButton").className="";</script>
			<fwn:AjaxButton id="stopAvatarProcess" ajaxonclicklistener="DetenerProcesoAvatar" 
				styleClass="fio-stop-button" reRender="botonera,panelChatWrap,configComboWrap" rendered="#{(not empty treelogic.USER_PID) and (treelogic.USER_PID != '0000') and (treelogic.USER_PID != '99991')}" onclick="stopAvatarPlayers();" />
			
			<fwn:poll id="pollAvatarProcess" ajaxListener="ProcessPollingListener" interval="2000" enabled="#{(not empty treelogic.USER_PID) and (treelogic.USER_PID != '0000') and (treelogic.USER_PID != '99991')}"/>
			<fwn:poll id="pollChatResponse" ajaxListener="ChatResponsePolling" interval="500" enabled="#{(not empty treelogic.USER_PID) and (treelogic.USER_PID != '0000') and (treelogic.USER_PID != '99991')}"/>
			
			<%-- Resumen de precios del avatar --%>
			<fwn:Dialog header="#{msg['FIONA.tablaPrecio.titulo.valor']}" id="dialogoPrecios" modal="true" size="extraLarge600">
			
				<%-- Panel con variables de hosting --%>
				<fwn:OutputPanel id="panelUsers" layout="block" styleClass="divVertical">
					<%-- Usuarios --%>
					<fwn:Label value="#{msg['FIONA.tablaPrecio.usuariosTitle.valor']}" style="display:block;" title="#{msg['FIONA.tablaPrecio.usuariosTitle.valor']}">
					</fwn:Label>
					<%-- Usuarios --%>
					<fwn:Combo id="numUsuariosConcu" value="#{treelogic.idUnidadTiempo}" propertyFile="items.concurrent_users" orderedItems="false" cacheable="false"
					showOptionSeleccionar="false" ajaxChangeListener="RecalcularPrecioProduccionListener" event="onchange"
					reRender="panelPreciosTime, panelPreciosUso, datosCalculoPrecioTiempo, datosCalculoPrecioUso, datosCalculoPrecioTotal"/>
				</fwn:OutputPanel>
				<fwn:OutputPanel id="panelTime" layout="block" styleClass="divVertical">
					<%-- Tiempo (unidades) --%>
					<fwn:Label value="#{msg['FIONA.tablaPrecio.time.valor']}" style="display:block;" title="#{msg['FIONA.tablaPrecio.timeTitle.valor']}">
					</fwn:Label>
					<%-- Tiempo (unidades) --%>
					<fwn:Combo id="idUnidadTiempo" CA="029" CS="040" value="#{treelogic.idUnidadTiempoProd}" cacheable="false" contextValue="FIONEG014010" 
					contextKey="FIONEG014020" showOptionSeleccionar="false" ajaxChangeListener="RecalcularPrecioProduccionListener" event="onchange"
					reRender="panelPreciosTime, panelPreciosUso, datosCalculoPrecioTiempo, datosCalculoPrecioUso, datosCalculoPrecioTotal"/>
				</fwn:OutputPanel>
				<fwn:OutputPanel id ="panelResolution" layout="block" styleClass="divVertical">
					<%-- Resolucion --%>
					<fwn:Label value="#{msg['FIONA.tablaPrecios.resolution.valor']}" style="display:block;" title="#{msg['FIONA.tablaPrecios.resolutionTitle.valor']}">
					</fwn:Label>
					<%-- Resolucion --%>
					<fwn:Combo id="resolution" value="#{treelogic.resolution}" propertyFile="items.resolution" cacheable="false" orderedItems="false"
					showOptionSeleccionar="false" ajaxChangeListener="RecalcularPrecioProduccionListener" event="onchange"
					reRender="panelPreciosTime, panelPreciosUso, datosCalculoPrecioTiempo, datosCalculoPrecioUso, datosCalculoPrecioTotal"/>
				</fwn:OutputPanel>
				<fwn:OutputPanel id="panelAvailability" layout="block" styleClass="divVertical">
					<%-- High availability --%>
					<fwn:Label value="#{msg['FIONA.tablaPrecios.disponibilidad.valor']}" style="display:block;" title="#{msg['FIONA.tablaPrecios.disponibilidadTitle.valor']}">
					</fwn:Label>
					<%-- High availability --%>
					<fwn:BooleanCheck id="checkAvailability" value="#{treelogic.highAvailability}" style="height:27px;" ajaxChangeListener="RecalcularPrecioProduccionListener" event="onchange"
					reRender="panelPreciosTime, panelPreciosUso, datosCalculoPrecioTiempo, datosCalculoPrecioUso, datosCalculoPrecioTotal">
					</fwn:BooleanCheck>			
				</fwn:OutputPanel>
				<%--
				<fwn:OutputPanel id="enlaceSendOpinion"
					styleClass="botonera" layout="block" style="width:100%;display: inline-block;">
					<fwn:AjaxButton styleClass="botonesInvitacion botonesInvitacionAzul" style="float:right;"
						value="#{msg['FIONA.tablaPrecio.botonCalcular.valor']}" ajaxonclicklistener="RecalcularPrecioProduccionListener" reRender="panelPreciosTime, panelPreciosUso, datosCalculoPrecioTiempo, datosCalculoPrecioUso, datosCalculoPrecioTotal">						
					</fwn:AjaxButton>
				</fwn:OutputPanel>
				 --%>
													
				<%-- FIN variables de hosting --%>
				<fwn:OutputPanel id="panelPreciosTime" layout="block">
					<fwn:DataScroller align="right" for="tablaSparksTiempo" maxPages="5"
						id="sc2" reRender="tablaSparksTiempo,sc" renderIfSinglePage="false"
						fastControls="hide" stepControls="auto" boundaryControls="auto"
						rendered="false"						 
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
		
					<fwn:DataTable id="tablaSparksTiempo" rows="10"
						value="#{treelogic.preciosTimeList}" var="sparkPrecio" rendered="false" 
						reRender="sc,sc2" scrollersId="sc,sc2" styleClass="tablasPrecios"
						style="margin-top:2%;">
						
						<fwn:Column sortable="true" 
							label="nombre" styleClass="nombre">
							<fwn:facet name="header">
								<fwn:OutputText
									value="#{msg['FIONA.tablaPrecio.nombreSpark.valor']}" styleClass="headersPrecios"></fwn:OutputText>
							</fwn:facet>
							<fwn:OutputText value="#{sparkPrecio.spark_tiempo_nombre}" styleClass="celdasPreciosNombre"
							rendered="#{sparkPrecio.spark_tiempo_nombre ne 'Hosting'}"></fwn:OutputText>
							<fwn:OutputText value="#{sparkPrecio.spark_tiempo_nombre}" styleClass="footersPrecios celdasPreciosNombreHosting"
							rendered="#{sparkPrecio.spark_tiempo_nombre eq 'Hosting'}"></fwn:OutputText>
							<fwn:facet name="footer">
								<fwn:OutputText id="labelPrecioTiempo" styleClass="footersPrecios"></fwn:OutputText>
							</fwn:facet>
						</fwn:Column>
						
						<fwn:Column sortable="true" 
							label="precios" styleClass="nombre">
							<fwn:facet name="header">
								<fwn:OutputText
									value="#{msg['FIONA.tablaPrecio.precio.valor']}" styleClass="headersPrecios"></fwn:OutputText>
							</fwn:facet>						
							<%--<fwn:OutputText value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonFree.valor']}" styleClass="celdasPreciosNombre"
							rendered="#{empty(sparkPrecio.precios_produccion)}">
							</fwn:OutputText>--%>
							<fwn:OutputText value="#{sparkPrecio.spark_tiempo_precio}" styleClass="celdasPreciosNombre"  
							rendered="#{sparkPrecio.spark_tiempo_nombre ne 'Hosting'}" format="numerico" numberType="decimal" decimalPrecision="2"></fwn:OutputText>
							<fwn:OutputText value="#{sparkPrecio.spark_tiempo_precio}" styleClass="footersPrecios celdasPreciosNombreHosting"  
							rendered="#{sparkPrecio.spark_tiempo_nombre eq 'Hosting'}" format="numerico" numberType="decimal" decimalPrecision="2"></fwn:OutputText>
							<fwn:facet name="footer">
								<fwn:OutputText id="precioTotalTiempo" styleClass="footersPrecios" format="numerico" numberType="decimal" decimalPrecision="2"></fwn:OutputText>
							</fwn:facet>
						</fwn:Column>					
		
					</fwn:DataTable>
		
					<fwn:DataScroller align="right" for="tablaSparksTiempo" maxPages="5"
						id="sc" reRender="tablaSparksTiempo,sc2" renderIfSinglePage="false"
						fastControls="hide" stepControls="auto" boundaryControls="auto"
						rendered="false"  
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
					
					<fwn:OutputPanel id="datosCalculoPrecioTiempo" layout="block">
						<fwn:Label value="#{msg['FIONA.tablaPrecio.precioTotalTiempo.valor']}: " for="precioTotalTiempo" rendered="false" id="labelPrecioTiempo">
						</fwn:Label>
						<fwn:OutputText id="precioTotalTiempo" value="0.0" rendered="false"  format="numerico" numberType="decimal" decimalPrecision="2">
						</fwn:OutputText>					
					</fwn:OutputPanel>
				</fwn:OutputPanel>
				<fwn:OutputPanel id="panelPreciosUso" layout="block" style="margin-top:5%;">
					<fwn:DataScroller align="right" for="tablaSparksUso" maxPages="5"
						id="sc2Uso" reRender="tablaSparksUso,scUso" renderIfSinglePage="false"
						fastControls="hide" stepControls="auto" boundaryControls="auto"
						rendered="false" 
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
		
					<fwn:DataTable id="tablaSparksUso" rows="10"
						value="#{treelogic.preciosUseList}" var="sparkPrecio" rendered="false" 
						reRender="scUso,sc2Uso" scrollersId="scUso,sc2Uso" styleClass="tablasPrecios">
						
						<fwn:Column sortable="true" 
							label="nombre" styleClass="nombre">
							<fwn:facet name="header">
								<fwn:OutputText
									value="#{msg['FIONA.tablaPrecio.nombreSpark.valor']}" styleClass="headersPrecios"></fwn:OutputText>
							</fwn:facet>
							<fwn:OutputText value="#{sparkPrecio.spark_uso_nombre}" styleClass="celdasPreciosNombre"  format="numerico" numberType="decimal" decimalPrecision="2">
							</fwn:OutputText>
							<fwn:facet name="footer">
								<fwn:OutputText id="labelPrecioUso" value="#{msg['FIONA.tablaPrecio.precioTotalUso.valor']}" styleClass="footersPrecios"></fwn:OutputText>							
							</fwn:facet>
						</fwn:Column>
						
						<fwn:Column sortable="true" 
							label="precios" styleClass="nombre">
							<fwn:facet name="header">
								<fwn:OutputText
									value="#{msg['FIONA.tablaPrecio.precio.valor']}" styleClass="headersPrecios"></fwn:OutputText>
							</fwn:facet>						
							<%--<fwn:OutputText value="#{msg['FIONA.store.integratorBuyer.sparkInformation.botonFree.valor']}" styleClass="celdasPreciosNombre"
							rendered="#{empty(sparkPrecio.precios_produccion)}">
							</fwn:OutputText>--%>
							<fwn:OutputText value="#{sparkPrecio.spark_uso_precio}" styleClass="celdasPreciosNombre"></fwn:OutputText>
							<fwn:facet name="footer">
								<fwn:OutputText id="precioTotalUso" styleClass="footersPrecios" format="numerico" numberType="decimal" decimalPrecision="2"></fwn:OutputText>
							</fwn:facet>
						</fwn:Column>					
		
					</fwn:DataTable>
		
					<fwn:DataScroller align="right" for="tablaSparksUso" maxPages="5"
						id="scUso" reRender="tablaSparksUso,sc2Uso" renderIfSinglePage="false"
						fastControls="hide" stepControls="auto" boundaryControls="auto"
						rendered="false"  
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
					<%--
					<fwn:OutputPanel id="datosCalculoPrecioUso" layout="block">					
						<fwn:Label value="#{msg['FIONA.tablaPrecio.precioTotalUso.valor']}: $" for="precioTotalUso" rendered="false" id="labelPrecioUso">
						</fwn:Label>
						<fwn:OutputText id="precioTotalUso" value="0.0" rendered="false"  format="numerico" numberType="decimal" decimalPrecision="2">
						</fwn:OutputText>
					</fwn:OutputPanel> --%>
					
					<fwn:OutputPanel id="datosCalculoPrecioTotal" layout="block">					
						<fwn:Label value="#{msg['FIONA.tablaPrecio.precioTimeMasUso.valor']}: $" for="precioTotal" rendered="false" id="labelPrecioTotal">
						</fwn:Label>
						<fwn:OutputText id="precioTotal" value="0.0" rendered="false" format="numerico" numberType="decimal" decimalPrecision="2">
						</fwn:OutputText>
					</fwn:OutputPanel>
					<fwn:OutputPanel id="botonProd" styleClass="botonera" layout="block" style="width:100%;display: inline-block;">
						<fwn:AjaxButton id="botonAProd" styleClass="botonesInvitacion botonesInvitacionAzul" style="float:right;"
						value="#{msg['FIONA.tablaPrecio.botonAProduccion.valor']}" ajaxonclicklistener="AbrirCerrarAlert" reRender="alertUploadConfirm,dialogoPrecios,panelPreciosTime, panelPreciosUso, datosCalculoPrecioTiempo, datosCalculoPrecioUso, datosCalculoPrecioTotal,alertInfoUploadProduction,alertInfoBillingAgreement">						
						</fwn:AjaxButton>
					</fwn:OutputPanel>					
				</fwn:OutputPanel>
			
			</fwn:Dialog>
			
			<%-- Alert para mostrar al usuario la ventana de confirmación de subida a producción --%>
			<fwn:ModalAlert id="alertUploadConfirm" alertTitle="#{msg['FIONA.tablaPrecio.uploadConfirm.cabeceraPanel.valor']}" title="#{msg['FIONA.tablaPrecio.uploadConfirm.confirmarUpload.valor']}" type="confirm">
			  <fwn:facet name="buttons">
			   <fwn:OutputPanel>		     
				 <fwn:AjaxButton styleClass="botonesInvitacion"
						value="#{msg['FWN_Comun.botonOK']}" ajaxonclicklistener="UploadToProductionListener" reRender="alertUploadConfirm,dialogoPrecios,panelPreciosTime, panelPreciosUso, datosCalculoPrecioTiempo, datosCalculoPrecioUso, datosCalculoPrecioTotal,alertInfoUploadProduction,alertInfoBillingAgreement">						
				</fwn:AjaxButton>
				 <fwn:AjaxButton value="#{msg['FWN_Comun.botonNO']}" reRender="alertUploadConfirm"
						 id="cerrarUploadConfirm" ajaxonclicklistener="AbrirCerrarAlert" styleClass="botonesInvitacion">				 	
				</fwn:AjaxButton>											
			   </fwn:OutputPanel>
			  </fwn:facet>
			</fwn:ModalAlert>
						
			<%-- Alert para mostrar al usuario el error producido cuando alguno de los sparks de su configuración han sido desactivados --%>
			<fwn:ModalAlert id="alertSparksInactivos" alertTitle="" title="" type="">
			 <fwn:facet name="buttons">
			 			 
			 </fwn:facet>
			</fwn:ModalAlert>	
			<%-- Alert para mostrar al usuario el éxito o error producido cuando se solicita la subida a producción de una configuración --%>
			<fwn:ModalAlert id="alertInfoUploadProduction" alertTitle="" title="" type="">
			 <fwn:facet name="buttons">
			 			 
			 </fwn:facet>
			</fwn:ModalAlert>	
			<%-- Alert para mostrar al usuario la petición de firma del acuerdo de pagos --%>
			<fwn:ModalAlert id="alertInfoBillingAgreement" alertTitle="" title="" type="">
			 <fwn:facet name="buttons">
	 			 <fwn:OutputPanel>
					<fwn:Button action="sign" value="#{msg['FWN_Comun.botonOK']}"
						styleClass="botonesInvitacion" />
				</fwn:OutputPanel>
			 </fwn:facet>
			</fwn:ModalAlert>	
		</fwn:Form>
	</fwn:OutputPanel>
	
	
	<f:verbatim><div style="clear:both"></div></f:verbatim>
</fwn:OutputPanel> 

<fwn:Form id="errconsoleForm">
	<fwn:Panel header="Console" id="errconsole" collapsable="true" showTextCollapse="true" collapse="true" styleClass="panel-console" style="max-height: 155px;overflow: scroll;">
		<br/>This console displays information about the execution. Please include this information when reporting an error.
		<hr/>
		
		<fwn:OutputPanel id="errconsolenfo1" layout="block">
			<!-- AvatarProcUUID: <fwn:OutputText rendered="#{treelogic.USER_PID=='0000'}" value="#{treelogic.USER_PID}"/><fwn:OutputText rendered="#{treelogic.USER_PID!='0000'}" value="None"/> -->
			<!-- AvatarProcIntercomUUID: <fwn:OutputText rendered="#{treelogic.USER_RUN_PID=='0000'}" value="#{treelogic.USER_RUN_PID}"/><fwn:OutputText rendered="#{treelogic.USER_RUN_PID!='0000'}" value="None"/> -->
			<!-- AvatarWithChatSpark: <fwn:OutputText value="#{treelogic.USER_PID_CHAT}"/> -->
			<%--
			<fwn:OutputText rendered="#{treelogic.USER_PID == '0000'}" escape="false">There is no avatar process running. Click Start button to launch it.<br/></fwn:OutputText>
			<fwn:OutputText rendered="#{treelogic.USER_PID != '0000'}" escape="false">Your current avatar process UUID is: <strong style="color:green"><fwn:OutputText value="#{treelogic.USER_PID}"/></strong>. Stop it whenever you want!!<br/></fwn:OutputText>
			<fwn:OutputText rendered="#{treelogic.USER_PID_CHAT}" escape="false">ChatSpark is <strong style="color:green">active</strong> in your current avatar configuration.<br/></fwn:OutputText>
			<fwn:OutputText rendered="#{not treelogic.USER_PID_CHAT}" escape="false">ChatSpark is <strong style="color:red">not active</strong> in your current avatar configuration.<br/></fwn:OutputText>
			<hr/>
			--%>
		</fwn:OutputPanel>
		
		<fwn:Label for="loglevelselector" value="Logging level:" />
		<fwn:Combo id="loglevelselector" value="INFO" showOptionSeleccionar="false">
			<fwn:SelectItem itemLabel="FATAL" itemValue="FATAL"/>
			<fwn:SelectItem itemLabel="ERROR" itemValue="ERROR"/>
			<fwn:SelectItem itemLabel="WARN" itemValue="WARN"/>
			<fwn:SelectItem itemLabel="INFO" itemValue="INFO"/>
			<fwn:SelectItem itemLabel="DEBUG" itemValue="DEBUG"/>
			<fwn:SelectItem itemLabel="ALL" itemValue="ALL"/>
		</fwn:Combo>
		
		<fwn:Label for="maxlines" value="Number of lines:" />
		<fwn:Combo id="maxlines" value="25" showOptionSeleccionar="false">
			<fwn:SelectItem itemLabel="OFF" itemValue="0"/>
			<fwn:SelectItem itemLabel="10" itemValue="10"/>
			<fwn:SelectItem itemLabel="25" itemValue="25"/>
			<fwn:SelectItem itemLabel="50" itemValue="50"/>
			<fwn:SelectItem itemLabel="100" itemValue="100"/>
		</fwn:Combo>
		
		<fwn:OutputText rendered="true" escape="false" id="errconsolestream" value="" styleClass="texto-consola">
		</fwn:OutputText>
		<hr/>
	</fwn:Panel>
	<fwn:poll id="pollErrConsole" ajaxListener="ErrConsolePollingListener" interval="2500" enabled="#{(not empty treelogic.USER_PID) and (treelogic.USER_PID != '0000') and (treelogic.USER_PID != '99991')}"/>
</fwn:Form>

<fwn:OutputText><script type="text/javascript"><!--
<%-- 
Este script se utiliza para configurar e incrustar los flashplayers.
usermail, userpass y usermaild5 son variables de sesion
userscope y usercamscope definen el ultimo nivel del stream de red5 formado de la siguiente manera:
	  "rtmp://url_del_servidor/FionaRed5/md5_mail_usuario/scope (avatar o usercam)
--%>

usermail = "<fwn:OutputText value="#{treelogic.SECURE_USERMAIL}" escape="true"/>";
userpass = "<fwn:OutputText value="#{treelogic.SECURE_USER_PASSWORD}" escape="true"/>";
usermaild5 = "<fwn:OutputText value="#{treelogic.SECURE_USERMAILD5}" escape="true"/>";

/* Se extraen los par�metros de resoluci�n (width x height) de reproducci�n del avatar dependiendo del tipo de usuario */
var resolutionArrayStr = "<%= StringEscapeUtils.escapeJavaScript(ConfigUtils.getAvatarResolution(ContextUtils.getUserAccountIdAsInteger())) %>";
var resolutionArray = resolutionArrayStr.split(",");
var avatarWidth = resolutionArray[0];
var avatarHeight = resolutionArray[1];

userscope = "avatar";
usercamscope = "usercam";
r5appserver = "<%= StringEscapeUtils.escapeJavaScript(ConfigUtils.getR5appserverUri()) %>";
var r5server = r5appserver + usermaild5;

var flashvarscam = { 
	stream: usercamscope, 
	width: '200',
	height: '130',			//size de la imagen
	windowwidth:'214',
	windowheight:'160',	
	streamtype:'live',
	silencelevel:'0',
	server: r5server,
	username: usermail,
	password : userpass
};
var flashvarsrcv = { 
	stream: userscope, 
	width: avatarWidth,
	height: avatarHeight,
	windowwidth:'200',
	windowheight:'160',
	streamtype:'live',
	silencelevel:'0',
	server: r5server,
	buffertime: '0',
	username: usermail,
	password : userpass
};


var paramscam = {
	wmode: 'transparent', 
	server: r5server,
	allowScriptAccess: 'sameDomain',
	bgcolor : '#000',
	quality : 'high'
};
var paramsrcv = {
	wmode: 'transparent', 
	server: r5server,
	allowScriptAccess: 'sameDomain',
	bgcolor : '#000',
	quality : 'high'
};
	
	
var attributescam = {
	id : 'camsender',
	name : 'camsender'
};
/* Se a�ade la propiedad 'margin-top' para centrar el avatar dependiendo de su resoluci�n */
var attributesrcv = {
	id : 'avatarimg',
	name : 'avatarimg',
	style : 'margin-top:'+(474-avatarHeight)/2+'px'
};

var timerLlama;


function showAvatarFlameBox(numRepeat, msdelay){
	jQuery("#flameBoxAnim").frameAnimation({hoverMode:false, repeat:numRepeat, delay:msdelay});
}

function avatarstream_onLoadCallkback(event){
	if(event.success) {
		jQuery('#flameBoxAnim').hide();
		jQuery('#flameBox').hide();
		jQuery('#flameBoxWrap').hide();
		showAvatarFlameBox(-1, 80);
	} else {
		//alert("Error");
		//showAvatarFlameBox(-1, 80);
	}
}

function simulateClick() {
	   var evt = document.createEvent("MouseEvents");
	   evt.initMouseEvent("click", true, true, window,
	     0, 0, 0, 0, 0, false, false, false, false, 0, null);
	   var button = document.getElementById("avatarProcessForm:hiddenStartButton").firstChild; 
	   button.dispatchEvent(evt);
}


function startAvatarPlayers(hasVideo, hasAudio){	
	var room = jQuery("#randomRoom").val();
	// r5server = r5appserver + usermaild5 + fecha;
	r5server = r5appserver + room;
	console.log(r5server);
	flashvarscam.server = r5server;
	flashvarsrcv.server = r5server;
	paramscam.server = r5server;
	paramsrcv.server = r5server;	
	objectWidth = parseInt(flashvarsrcv.width) + 6;
	objectHeight = parseInt(flashvarsrcv.height) + 6;
	// ocultar imagen
	jQuery("#flameBoxImage").hide();
	// Mostrar enlace para animacion
	jQuery('#flameBoxAnim').show();
	// Iniciar animacion
	showAvatarFlameBox(-1, 100);	
	timerLlama = setInterval(function() {
		if (typeof swfobject != "undefined") {			
			swfobject.embedSWF('${request.contextPath}/flash/receivevideo.swf', 'avatarstream', objectWidth, objectHeight, '9.0.0',false,flashvarsrcv, paramsrcv, attributesrcv, avatarstream_onLoadCallkback);
			clearInterval(timerLlama);
			timerLlama = null;
		}
	}, 3500); 
	
	if(hasVideo) {
		//el size del objeto tiene que ser como minimo 214x137 para poder mostrar el cuadro de configuracion donde se pide acceso a camara y micro
		swfobject.embedSWF('${request.contextPath}/flash/vidpublish.swf', 'usercam', '220', '160', '9.0.0',false,flashvarscam, paramscam, attributescam );
	}
	else if(hasAudio) {
		//el size del objeto tiene que ser como minimo 214x137 para poder mostrar el cuadro de configuracion donde se pide acceso a camara y micro
		swfobject.embedSWF('${request.contextPath}/flash/micpublish.swf', 'usercam', '220', '160', '9.0.0',false,flashvarscam, paramscam, attributescam );
	}
	
	
	// Simular click de otro botón
	 if(navigator.appName.indexOf("Explorer") > -1){              
		   document.getElementById("avatarProcessForm:hiddenStartButton").firstChild.click();	
	 }else{
		   	simulateClick();   			
	 }
}

function stopAvatarPlayers(){
	swfobject.removeSWF('usercam');
	swfobject.removeSWF('avatarstream');
	clearInterval(timerLlama);
	renderPanelAvatarPlayers();
}


function activarNuevoClickAlert(){	
	jQuery(".alertImagenCabecera").attr("onclick",jQuery('.alertImagenCabecera').attr("onclick") + ';stopAvatarPlayers()');		
}



function renderPanelAvatarPlayers(){
	var flamebox = jQuery(
			'<div id="flameBoxWrap" class="bgflame">' + 
				'<div id="flameBox" class="bgflame-inner">' + 
					'<img id="flameBoxImage" src="${request.contextPath}/images/fiona/flame.png" alt="Powered by FIONA"/>' + 
					'<a id="flameBoxAnim" href="#" style="display:none;"></a>' + 
				'</div>' + 
			'</div>'
		);
	jQuery('#players').removeClass('panelAvatarPlayers-flame')
		.empty()
		.append(jQuery('<div id="usercam"></div>'))
		.append(jQuery('<div id="avatarstream"></div>').append(flamebox));
}


renderPanelAvatarPlayers();
<%-- If there are stored any USER_PID, run players again. Happens when navigate without stop avatar process --%>
<f:verbatim escape="false" rendered="#{(not empty treelogic.USER_PID) and (treelogic.USER_PID != '0000')}">startAvatarPlayers("#{treelogic.USER_VIDEO_INPUT}", "#{treelogic.USER_AUDIO_INPUT}");</f:verbatim>



//--></script></fwn:OutputText>
