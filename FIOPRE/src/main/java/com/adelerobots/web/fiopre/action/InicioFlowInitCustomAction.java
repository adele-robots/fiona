package com.adelerobots.web.fiopre.action;

import java.util.Date;

import org.apache.log4j.Logger;

import com.treelogic.fawna.presentacion.core.exception.PresentacionException;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperCustom;
import com.treelogic.fawna.presentacion.core.webflow.action.ICustomAction;

/**
 * Clase encargada de iniciar el flujo de inicio.
 * @author adele
 */
public class InicioFlowInitCustomAction implements ICustomAction {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3713884305627438645L;
	
	protected final Logger logger = Logger.getLogger(getClass());

	public void ejecutar(
			final IHelperCustom helper) 
		throws PresentacionException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug(getClass().getSimpleName() + " execution init.");
		}
		
		Date currentDate = new Date();
		helper.setValueContext("random", Long.toString(currentDate.getTime()), IHelperCustom.FLUJO_CTX_TYPE);
		
		
		if (logger.isDebugEnabled()) {
			logger.debug(getClass().getSimpleName() + " execution end.");
		}
	}

}