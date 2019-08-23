package com.adelerobots.web.fiopre.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.presentacion.core.exception.PresentacionException;
import com.treelogic.fawna.presentacion.core.security.userdetails.User;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperCustom;
import com.treelogic.fawna.presentacion.core.webflow.action.ICustomAction;

/**
 * Clase encargada de recuperar los datos del usuario loggeado y almacenarlos 
 * en el contexto de sesion.
 * @author saul.gonzalez
 */
public class SecurityLandingCustomAction implements ICustomAction {

	private static final long serialVersionUID = 1L;

	protected static Logger logger = Logger.getLogger(SecurityLandingCustomAction.class);

	public void ejecutar(
			final IHelperCustom helper) 
		throws PresentacionException 
	{
		// Recuperar contexto Spring Security 
		final SecurityContext securityContext = SecurityContextHolder.getContext();
		final Authentication authentication = securityContext.getAuthentication();
		final User user = (User) authentication.getPrincipal();
		// Setear el detalle de usuario en el contexto de session
		final Map<String,String> userInfo = user.getAttributes();
		if (userInfo != null) {
			for (Map.Entry<String, String> entry : userInfo.entrySet()) {
				helper.setValueContext(entry.getKey(), entry.getValue(), IHelperCustom.SESION_CTX_TYPE);
			}
		}
		// ROLES
		final Collection<GrantedAuthority> authorities = user.getAuthorities();
		final Map<String, String> authoritiesMap = new HashMap<String, String>();
		if (null != authorities) {
			for (GrantedAuthority ga : authorities) {
				authoritiesMap.put(ga.getAuthority(), ga.getAuthority());
			}
		}
		helper.setItemsValueContext("SECURE_USER_ROLES", authoritiesMap, IHelperCustom.SESION_CTX_TYPE);
		
		helper.setValueContext("CONFIG_PRESET", "", IHelperCustom.SESION_CTX_TYPE);
		
		//Valores propios de la aplicacion
		ContextUtils.setUserPidAsString("0000", "0000");
		ContextUtils.setUserPidChat(false);
	}

}