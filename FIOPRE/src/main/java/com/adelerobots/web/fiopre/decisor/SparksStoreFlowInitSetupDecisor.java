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

public class SparksStoreFlowInitSetupDecisor implements IGenericDecisor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7587845406641800719L;
	
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
			String sparkId = parameters.get("s");
			HelperContext.getInstance().setValueContext("spark_id", sparkId, IHelperCustom.FLUJO_CTX_TYPE);
			String priceID = parameters.get("p");
			HelperContext.getInstance().setValueContext("selected_price", priceID, IHelperCustom.FLUJO_CTX_TYPE);
		}
		
		String cancelled = parameters.get("cancelled");
		
		if(cancelled != null){
			resultado = "cancelled";			
			HelperContext.getInstance().setValueContext("cancelled","true", IHelperCustom.FLUJO_CTX_TYPE);
			// Seteamos el criterio de listado para mostar los sparks en la página de inicio (CRITERIO_TOP)
			HelperContext.getInstance().setValueContext("spark_criterio","1", IHelperCustom.FLUJO_CTX_TYPE);
			
		}
				
		if(HelperContext.getInstance().getValueContext("spark_criterio") == null)
			HelperContext.getInstance().setValueContext("spark_criterio","0", IHelperCustom.FLUJO_CTX_TYPE);
		
		if (logger.isDebugEnabled()) {
			logger.debug(getClass().getSimpleName() + " execution end.");
		}
		
		return resultado;
	}

}
