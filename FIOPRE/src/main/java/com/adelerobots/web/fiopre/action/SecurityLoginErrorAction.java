package com.adelerobots.web.fiopre.action;

import org.springframework.util.StringUtils;

import com.treelogic.fawna.presentacion.core.exception.PresentacionException;
import com.treelogic.fawna.presentacion.core.security.utils.FawnaSecurityConstants;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperRender;
import com.treelogic.fawna.presentacion.core.utilidades.Properties;
import com.treelogic.fawna.presentacion.core.webflow.action.IGenericRenderAction;

/**
 * Clase encargada de la gestion de los errores producidos en el
 * proceso de Login.
 * 
 * @author saul.gonzalez
 */
public class SecurityLoginErrorAction 
	implements 	IGenericRenderAction
{
	/** Version UID for serialization */
	private static final long serialVersionUID = 2457771625333509474L;

	/** Param key de donde obtener el codigo del error */
	private final static String SECURITY_ERROR_CODE_VALUE = "securityErrorKey";
	/** Key generica a la que se le concatenara el codigo de error para resolver el mensaje de error correspondiente */
	private final static String SECURITY_ERROR_MSG_KEY = "FWN_Login.mensaje.error_";

	
	public void ejecutar(
			final IHelperRender helper) 
		throws PresentacionException 
	{
		//Si ControlFlujoListener ha detectado errores de Spring Security
		if (StringUtils.hasText(helper.getValueContext(FawnaSecurityConstants.SECURITY_ERROR_KEY))) {
			//El mensaje de la Excepcion de Spring Security capturada por ControlFlujoListener
			String errmsg = helper.getValueContext(FawnaSecurityConstants.SECURITY_ERROR_MSG);
			
			//Si le pasamos el codigo de error al flujo intentamos resolver dicho mensaje en vez del mensaje anterior
			String errorKey = helper.getValueContext(SECURITY_ERROR_CODE_VALUE);
			if (StringUtils.hasText(errorKey)) {
				try {
					Properties messages = new Properties("i18n.messages");
					String resolvedMsg = messages.getValueKey(SECURITY_ERROR_MSG_KEY + errorKey);
					if (StringUtils.hasText(resolvedMsg)) errmsg = resolvedMsg;
				} catch (Exception e) {
					//Por ejemplo si hay un error al resolver la key en el fichero properties
					//NOOP: Nada para usar el mensaje original de ControlFlujoListener
				}
			}
			helper.setValueContext("securityErrorMessage", errmsg, IHelperRender.VENTANA_CTX_TYPE);
		}
	}

}