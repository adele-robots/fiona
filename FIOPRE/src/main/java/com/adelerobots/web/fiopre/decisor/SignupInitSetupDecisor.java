package com.adelerobots.web.fiopre.decisor;

import org.apache.log4j.Logger;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.execution.FlowExecutionContext;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import com.treelogic.fawna.presentacion.core.exception.PresentacionException;
import com.treelogic.fawna.presentacion.core.utilidades.HelperContext;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperCustom;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperDecisor;
import com.treelogic.fawna.presentacion.core.webflow.action.IGenericDecisor;

public class SignupInitSetupDecisor implements IGenericDecisor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1780090034008962271L;
	
	protected final Logger logger = Logger.getLogger(getClass());
	

	public String ejecutar(IHelperDecisor helper) throws PresentacionException {
		String resultado = "beginning";
		
		if (logger.isDebugEnabled()) {
			logger.debug(getClass().getSimpleName() + " execution init.");
		}
		
		
		//Necesitamos saber desde donde se lanzo el flujo para redirigir el back al finalizarlo
		//Casos: desde el website (nuevo flujo) o desde login (subflujo) 
		String mode = "website";
		
		final RequestContext requestContext = RequestContextHolder.getRequestContext();
		final FlowDefinition activeFlow = requestContext.getActiveFlow();		
		final FlowExecutionContext flowExecutionContext = requestContext.getFlowExecutionContext();
		
		//Se puede mirar si la transition y el evento es null, o, si los id de los flows coinciden
		//para ver si es un flujo nuevo o un flow-state
		if (activeFlow.getId().equals(flowExecutionContext.getDefinition().getId())) {
			mode = "website";
		} else {
			mode = "login";
		}
		HelperContext.getInstance().setValueContext("SIGNUP_BACK_MODE",mode, IHelperCustom.FLUJO_CTX_TYPE);
		
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
			String amt = parameters.get("amt");
			HelperContext.getInstance().setValueContext("AMT",amt, IHelperCustom.FLUJO_CTX_TYPE);
		}
		
		String cancelled = parameters.get("cancelled");
		
		if(cancelled != null){
			resultado = "cancelled";
			// Volvemos a almacenar el identificador del usuario recién registrado
			String userId = parameters.get("u");
			HelperContext.getInstance().setValueContext("USER_ID",userId, IHelperCustom.FLUJO_CTX_TYPE);
			HelperContext.getInstance().setValueContext("cancelled","true", IHelperCustom.FLUJO_CTX_TYPE);
		}
				
		
		if (logger.isDebugEnabled()) {
			logger.debug(getClass().getSimpleName() + " execution end.");
		}
		
		return resultado;
	}

}
