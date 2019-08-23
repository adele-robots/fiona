package com.adelerobots.web.fiopre.action;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.exception.PresentacionException;
import com.treelogic.fawna.presentacion.core.invoker.FawnaInvoker;
import com.treelogic.fawna.presentacion.core.persistencia.ContextoOperacion;
import com.treelogic.fawna.presentacion.core.security.userdetails.User;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperCustom;
import com.treelogic.fawna.presentacion.core.webflow.action.ICustomAction;

/**
 * Clase encargada de eliminar los datos del usuario loggeado, reseteando 
 * de esta manera el contexto de sesion.
 * 
 * @author saul.gonzalez
 */
public class SecurityLogoutCustomAction implements ICustomAction {

	private static final long serialVersionUID = 1L;
	
	protected static Logger logger = Logger.getLogger(SecurityLogoutCustomAction.class);

	public void ejecutar(
			final IHelperCustom helper) 
		throws PresentacionException 
	{
		// Recuperar contexto Spring Security 
		final SecurityContext securityContext = SecurityContextHolder.getContext();
		final Authentication authentication = securityContext.getAuthentication();
		final User user = (User) authentication.getPrincipal();
		
		// Remover el detalle de usuario del contexto de session
		final Map<String,String> userInfo = user.getAttributes();
		if (userInfo != null) {
			for (Map.Entry<String, String> entry : userInfo.entrySet()) {
				helper.setValueContext(entry.getKey(), null, IHelperCustom.SESION_CTX_TYPE);
			}
		}
		// roles
		helper.setValueContext("SECURE_USER_ROLES", null, IHelperCustom.SESION_CTX_TYPE);
		
		//Otra informacion guardada por la app
		ContextUtils.setUserPidAsString("0000", "0000");
		ContextUtils.setUserPidChat(false);
		
		//Do remote logout
		invokeLogout(user.getIuu());
	}






	private void invokeLogout(final String uuid) throws PersistenciaException, FawnaInvokerException {
		final ContextoOperacion datosEntrada = new ContextoOperacion();
		datosEntrada.putDato("0", uuid, ContextoOperacion.Numero);
		FawnaInvoker.getInstance().invoke("029", "999", datosEntrada);
	}

	
	
}