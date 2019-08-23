<%@taglib uri="http://www.treelogic.com/fawna/jsf/html" prefix="fwn"%>

<fwn:OutputPanel style="text-align:center;">
The restore button will affect your avatar's composition and configuration by restoring it to one of the three available configurations.
<br>
Change your avatar by choosing any of the three configurations and click "Restore Process" to save changes.
<br><br>
</fwn:OutputPanel>


<fwn:OutputPanel style="text-align:left; font-size:16pt;width: 60%;display: block;margin-left: 17%;">
<b>Base Configuration:</b> A very simple configuration with a 3D model speaking a text at start.
<br>
<b>Face Tracking Configuration:</b> The avatar will face track you and will keep eye contact, using the your camera to detect face position.
<br>
<b>Rebecca Dialog Configuration:</b> Talk to your avatar using a chat-box.
<br>
</fwn:OutputPanel>


<fwn:OutputPanel>
	<fwn:Form style="margin-top:30px;">
		<fwn:OutputPanel style="width: 60%;display: block;margin-left: 17%;">
			<fwn:Label for="confselect" value="Select Configuration:" />
				<fwn:Combo style="font-size:16pt;" id="confselect" value="Base Configuration" showOptionSeleccionar="false">
					<fwn:SelectItem itemLabel="Base Configuration" itemValue="baseconf"/>
					<fwn:SelectItem itemLabel="FaceTracker Configuration" itemValue="ftracker"/>
					<fwn:SelectItem itemLabel="Rebbecca Dialog Configuration" itemValue="dialog"/>
				</fwn:Combo>
			<fwn:AjaxButton id="changeconfig" ajaxonclicklistener="restoreDefaultConfigListener" 
					styleClass="fio-restore-button" rendered="true" reRender="choosenpreset" />
		</fwn:OutputPanel>
		<fwn:OutputPanel>
			<fwn:OutputText rendered="true" escape="false" id="choosenpreset" style="padding-left: 271px;"value="No changes made"></fwn:OutputText>
		</fwn:OutputPanel>

		<fwn:Button action="cancel" value="" styleClass="fio-back-button" style="margin-top: 55px;" />
		
	</fwn:Form>
	
	
	
</fwn:OutputPanel>

