package com.adelerobots.web.fiopre.action;

import org.apache.log4j.Logger;

import com.treelogic.fawna.presentacion.core.utilidades.IHelperCustom;
import com.treelogic.fawna.presentacion.core.webflow.action.ICustomAction;

/**
 * Prepara el flujo de logout para ser ejecutado
 * 
 * @author adele
 * @see SecurityLogoutCustomAction
 */
public class SecurityLogoutInitCustomAction 
	implements 	ICustomAction
{

	protected static Logger logger = Logger.getLogger(SecurityLogoutInitCustomAction.class);

	/*
	 * (non-Javadoc)
	 * @see com.treelogic.fawna.presentacion.core.webflow.action.ICustomAction#ejecutar(com.treelogic.fawna.presentacion.core.utilidades.IHelperCustom)
	 */
	public void ejecutar(
			final IHelperCustom helper)
	{
		logger.debug("SecurityLogoutInitCustomAction.ejecutar: Inicio");
		
		
		logger.debug("SecurityLogoutInitCustomAction.ejecutar: Fin");
	}




}
