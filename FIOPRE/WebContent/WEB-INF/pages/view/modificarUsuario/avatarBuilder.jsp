<%@page import="com.adelerobots.web.fiopre.utilidades.ConfigUtils"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>

<fwn:OutputPanel id="designerpanel" layout="block">
	<iframe id="editorGrafico" frameborder="0"> 
	</iframe>
</fwn:OutputPanel>

<fwn:OutputPanel>
	<fwn:Form style="text-align:right; margin-top:10px;">
		<fwn:Button action="volver" value="" styleClass="fio-back-button" style="float:left;" />
		<fwn:Button action="restoreDefault" value="" styleClass="fio-restore-button" style="float:left;" />
	</fwn:Form>
</fwn:OutputPanel>

<fwn:OutputText><script type="text/javascript">
(function($){
	var u = "<%= "http".equals(request.getScheme()) ? StringEscapeUtils.escapeJavaScript(ConfigUtils.getDesignerIportUri()) : StringEscapeUtils.escapeJavaScript(ConfigUtils.getDesignerIportUriHttps()) %><fwn:OutputText value="#{treelogic.SECURE_USERMAILD5}" escape="true"/>";
	u += "&acc=";
	u += <fwn:OutputText value="#{treelogic.SECURE_USER_ACCOUNTTYPE_ID}" escape="true"/>;
	u += "&sparks=";
	u += "<%= StringEscapeUtils.escapeJavaScript(ConfigUtils.getFreeSparksNumber()) %>";
	$("#editorGrafico")[0].src= u;
})(jQuery);
</script></fwn:OutputText>
