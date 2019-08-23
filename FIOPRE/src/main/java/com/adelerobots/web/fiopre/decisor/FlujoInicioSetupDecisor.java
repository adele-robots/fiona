package com.adelerobots.web.fiopre.decisor;

import org.apache.log4j.Logger;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import com.treelogic.fawna.presentacion.core.exception.PresentacionException;
import com.treelogic.fawna.presentacion.core.utilidades.HelperContext;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperCustom;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperDecisor;
import com.treelogic.fawna.presentacion.core.webflow.action.IGenericDecisor;

public class FlujoInicioSetupDecisor implements IGenericDecisor{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 5163508274528915813L;
	protected final Logger logger = Logger.getLogger(getClass());

	public String ejecutar(IHelperDecisor arg0) throws PresentacionException {
		String resultado = "beginning";
		
		if (logger.isDebugEnabled()) {
			logger.debug(getClass().getSimpleName() + " execution init.");
		}		
		
		
		final RequestContext requestContext = RequestContextHolder.getRequestContext();				
		
		// Comprobamos si volvemos de una cancelación de Paypal o de una vuelta de la aprobación
		// Obtenemos los parametros pasados al flujo		
		final ExternalContext externalContext = requestContext.getExternalContext();
		final ParameterMap parameters = externalContext.getRequestParameterMap();
		
		String token_id = parameters.get("token");
		
		if(token_id != null){
			HelperContext.getInstance().setValueContext("TOKEN",token_id, IHelperCustom.FLUJO_CTX_TYPE);
			resultado = "returned";
			// Volvemos a almacenar el identificador del usuario recién registrado
			String userId = parameters.get("u");
			HelperContext.getInstance().setValueContext("USER_ID",userId, IHelperCustom.FLUJO_CTX_TYPE);			
		}
		
		String cancelled = parameters.get("cancelled");
		
		if(cancelled != null){
			resultado = "cancelled";			
			HelperContext.getInstance().setValueContext("cancelled","true", IHelperCustom.FLUJO_CTX_TYPE);			
		}
				
		
		if (logger.isDebugEnabled()) {
			logger.debug(getClass().getSimpleName() + " execution end.");
		}
		
		return resultado;
	}
	
}
