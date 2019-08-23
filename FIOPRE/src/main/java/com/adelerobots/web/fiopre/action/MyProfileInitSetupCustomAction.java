package com.adelerobots.web.fiopre.action;

import org.apache.log4j.Logger;

import com.treelogic.fawna.presentacion.core.exception.PresentacionException;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperCustom;
import com.treelogic.fawna.presentacion.core.webflow.action.ICustomAction;

/**
 * Clase encargada de iniciar el flujo de mi perfil.
 * @author adele
 */
public class MyProfileInitSetupCustomAction implements ICustomAction {

	private static final long serialVersionUID = 693381492680629683L;
	
	protected final Logger logger = Logger.getLogger(getClass());

	public void ejecutar(
			final IHelperCustom helper) 
		throws PresentacionException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug(getClass().getSimpleName() + " execution init.");
		}
		
		//Put your code here
		
		
		if (logger.isDebugEnabled()) {
			logger.debug(getClass().getSimpleName() + " execution end.");
		}
	}

}