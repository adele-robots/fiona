package com.adelerobots.web.fiopre.listeners;

import java.io.IOException;

import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;

public class restoreDefaultConfigListener implements IProcesadorDeAjaxChangeListener{

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		// TODO Auto-generated method stub
		
		String preset = (String) gestorDatos.getValue("confselect");
		
		if ("baseconf".equalsIgnoreCase(preset)){ 
			restoreToBaseConfig();
			gestorDatos.setValue("choosenpreset", "Configuration changed to: Base configuration");
			}
		else if ("ftracker".equalsIgnoreCase(preset)){ 
			restoreToFaceTrackerConfig();
			gestorDatos.setValue("choosenpreset", "Configuration changed to: Face tracker configuration");
			}
		else if ("dialog".equalsIgnoreCase(preset)){ 
			restoreToRebeccaChatConfig();
			gestorDatos.setValue("choosenpreset", "Configuration changed to: Rebbecca dialog configuration");
			}
		
		
	}

	private void restoreToRebeccaChatConfig() {
		// TODO Auto-generated method stub
		restoreConfig("rebbecca");
	}

	private void restoreToFaceTrackerConfig() {
		// TODO Auto-generated method stub
		restoreConfig("facetracker");
	}

	private void restoreToBaseConfig() {
		// TODO Auto-generated method stub
		restoreConfig("base");
	}
	
	private void restoreConfig(String mode) {
		// TODO Auto-generated method stub
		
		String mailmd5 = ContextUtils.getUserMailD5();
		
		String command = "/datos/script/restorePreset.sh " + mailmd5 + " " + mode;
		
		try {
			Process checkFile = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		/*//Sustituir out.png
		command = "cp " + "/datos/nfs/configpresets" + "/"+mode+"/out.png " + usersPath + "/public/" + mailmd5 + "/";
		try {
			Process checkFile = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Sustituir configuracion
		command = "cp " + "/datos/nfs/configpresets" + "/"+mode+"/initialconf/RemoteCharacterEmbodiment3DSpark.ini " + usersPath + "/private/" + mailmd5 + "/";
		try { 																 
			Process checkFile = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		//Sustituir avatar.xml
		command = "cp " + "/datos/nfs/configpresets" + "/"+mode+"/avatar.xml " + usersPath + "/private/" + mailmd5 + "/";
		try {
			Process checkFile = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		//Sustituir in.json
		command = "cp " + "/datos/nfs/configpresets" + "/"+mode+"/in.json " + usersPath + "/private/" + mailmd5 + "/";
		try {
			Process checkFile = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	*/
		

	}
}
