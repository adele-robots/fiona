package com.adelerobots.web.fiopre.action;

import org.apache.log4j.Logger;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.definition.TransitionDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.FlowExecutionContext;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import com.treelogic.fawna.presentacion.core.exception.PresentacionException;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperCustom;
import com.treelogic.fawna.presentacion.core.webflow.action.ICustomAction;

/**
 * Clase encargada de iniciar el flujo de registro de usuarios.
 * @author adele
 */
public class SignupInitSetupCustomAction implements ICustomAction {

	private static final long serialVersionUID = -7752810696735639651L;
	
	protected final Logger logger = Logger.getLogger(getClass());


	/*
	 * (non-Javadoc)
	 * @see com.treelogic.fawna.presentacion.core.webflow.action.ICustomAction#ejecutar(com.treelogic.fawna.presentacion.core.utilidades.IHelperCustom)
	 */
	public void ejecutar(
			final IHelperCustom helper) 
		throws PresentacionException 
	{
		if (logger.isDebugEnabled()) {
			logger.debug(getClass().getSimpleName() + " execution init.");
		}
		
		
		//Necesitamos saber desde donde se lanzo el flujo para redirigir el back al finalizarlo
		//Casos: desde el website (nuevo flujo) o desde login (subflujo) 
		String mode = "website";
		
		final RequestContext requestContext = RequestContextHolder.getRequestContext();
		final FlowDefinition activeFlow = requestContext.getActiveFlow();
		final Event currentEvent = requestContext.getCurrentEvent();
		final StateDefinition currentState = requestContext.getCurrentState();
		final TransitionDefinition currentTransition = requestContext.getCurrentTransition();
		final FlowExecutionContext flowExecutionContext = requestContext.getFlowExecutionContext();
		
		//Se puede mirar si la transition y el evento es null, o, si los id de los flows coinciden
		//para ver si es un flujo nuevo o un flow-state
		if (activeFlow.getId().equals(flowExecutionContext.getDefinition().getId())) {
			mode = "website";
		} else {
			mode = "login";
		}
		
		helper.setValueContext("SIGNUP_BACK_MODE", mode, IHelperCustom.FLUJO_CTX_TYPE);
		
		if (logger.isDebugEnabled()) {
			logger.debug(getClass().getSimpleName() + " execution end.");
		}
	}

}