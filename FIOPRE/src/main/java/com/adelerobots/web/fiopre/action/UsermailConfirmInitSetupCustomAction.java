package com.adelerobots.web.fiopre.action;

import org.apache.log4j.Logger;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import com.adelerobots.fioneg.util.FunctionUtils;
import com.treelogic.fawna.presentacion.core.exception.ExceptionUtils;
import com.treelogic.fawna.presentacion.core.exception.PresentacionException;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperCustom;
import com.treelogic.fawna.presentacion.core.webflow.action.ICustomAction;

/**
 * Clase encargada de iniciar el flujo de confirmacion de email de usuarios.
 * @author adele
 *
 */
public class UsermailConfirmInitSetupCustomAction implements ICustomAction{

	private static final long serialVersionUID = -6715475038688193771L;
	
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
		
		
		// Obtenemos los parametros pasados al flujo
		final RequestContext requestContext = RequestContextHolder.getRequestContext();
		final ExternalContext externalContext = requestContext.getExternalContext();
		final ParameterMap parameters = externalContext.getRequestParameterMap();
		
		String id = parameters.get("u");
		String code = parameters.get("t");
		
		if (FunctionUtils.isBlank(id)) {
			throw new PresentacionException(
					"Missing 'u' parameter", "Missing 'u' parameter", ExceptionUtils.ARCH_TYPE_EXCP_EXEC_ARCH);
		}
		if (FunctionUtils.isBlank(code)) {
			throw new PresentacionException(
					"Missing 'u' parameter", "Missing 'u' parameter", ExceptionUtils.ARCH_TYPE_EXCP_EXEC_ARCH);
		}
		
		// Persistimos las variables en el contexto de flujo
		helper.setValueContext("USER_ID", id, IHelperCustom.FLUJO_CTX_TYPE);
		helper.setValueContext("USER_SIGNUPCODE", code, IHelperCustom.FLUJO_CTX_TYPE);
		
		if (logger.isDebugEnabled()) {
			logger.debug(getClass().getSimpleName() + " execution end.");
		}
	}

}