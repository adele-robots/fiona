package com.adelerobots.web.fiopre.action;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.adelerobots.web.fiopre.utilidades.ConfigUtils;
import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.presentacion.core.exception.PresentacionException;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperCustom;
import com.treelogic.fawna.presentacion.core.webflow.action.ICustomAction;

/**
 * Clase encargada de restaurar los valores por defecto del designer.
 * @author adele
 */
public class AvatarBuilderRestoreDefaultsCustomAction implements ICustomAction {

	private static final long serialVersionUID = 7076017044652303081L;
	
	protected final Logger logger = Logger.getLogger(getClass());

	public void ejecutar(
			final IHelperCustom helper) 
		throws PresentacionException 
	{
		
		if (logger.isDebugEnabled()) {
			logger.debug(getClass().getSimpleName() + " execution init.");
		}
		
		//Put your code here
		String command;
		final File usersPath = ConfigUtils.getNfsUsersFolder();
		String mailmd5 = ContextUtils.getUserMailD5();
		
		
		//Sustituir out.png
		command = "cp " + usersPath + "/base/out.png " + usersPath + "/private/" + mailmd5 + "/";
		try {
			Process checkFile = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Sustituir avatar.xml
		command = "cp " + usersPath + "/base/avatar.xml " + usersPath + "/private/" + mailmd5 + "/";
		try {
			Process checkFile = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		//Sustituir in.json
		command = "cp " + usersPath + "/base/in.json " + usersPath + "/private/" + mailmd5 + "/";
		try {
			Process checkFile = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		//Sustituir configuracion
		command = "cp " + usersPath + "/base/initialconf " + usersPath + "/private/" + mailmd5 + "/";
		try {
			Process checkFile = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		if (logger.isDebugEnabled()) {
			logger.debug(getClass().getSimpleName() + " execution end.");
		}
	}

}