package com.adelerobots.web.fiopre.utilidades;

import org.apache.log4j.Logger;

import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.persistencia.ContextoLocator;
import com.treelogic.fawna.presentacion.core.persistencia.DatoFactory;
import com.treelogic.fawna.presentacion.core.persistencia.IDato;


/**
 * @author adele
 *
 */
public final class ContextUtils {
	
	private static final Logger logger = Logger.getLogger(ContextUtils.class);

	private ContextUtils() {
		// Util classes are final and all methods are static
	}

	public static Integer getUserIdAsInteger() {
		String value = getUserIdAsString();
		return value == null ? null : Integer.valueOf(value);
	}

	public static String getUserIdAsString() {
		String value = null;
		try {
			IDato dato = (IDato) ContextoLocator.getInstance().getContextoSesion().getCtxValue("SECURE_USERID");
			value = (String) dato.getValor();
		} catch (PersistenciaException e) {
			logger.error("[getUserIdAsString] Error recuperar el valor de la propiedad persistida SECURE_USERID", e);
		}
		return value;
	}

	public static String getUserPassword() {
		String value = null;
		try {
			IDato dato = (IDato) ContextoLocator.getInstance().getContextoSesion().getCtxValue("SECURE_USER_PASSWORD");
			value = (String) dato.getValor();
		} catch (PersistenciaException e) {
			logger.error("[getUserPassword] Error recuperar el valor de la propiedad persistida SECURE_USER_PASSWORD", e);
		}
		return value;
	}

	public static String getUserMailD5() {
		String value = null;
		try {
			IDato dato = (IDato) ContextoLocator.getInstance().getContextoSesion().getCtxValue("SECURE_USERMAILD5");
			value = (String) dato.getValor();
		} catch (PersistenciaException e) {
			logger.error("[getUserMailD5] Error recuperar el valor de la propiedad persistida SECURE_USERMAILD5", e);
		}
		return value;
	}

	public static Integer getUserShPidAsInteger() {
		String value = getUserShPidAsString();
		return value == null ? null : Integer.valueOf(value);
	}

	public static String getUserShPidAsString() {
		String value = null;
		try {
			IDato dato = (IDato) ContextoLocator.getInstance().getContextoSesion().getCtxValue("USER_PID");
			value = (String) dato.getValor();
		} catch (PersistenciaException e) {
			logger.error("[getUserShPidAsString] Error recuperar el valor de la propiedad persistida USER_PID", e);
		}
		return value;
	}

	public static Integer getUserRunPidAsInteger() {
		String value = getUserRunPidAsString();
		return value == null ? null : Integer.valueOf(value);
	}

	public static String getUserRunPidAsString() {
		String value = null;
		try {
			IDato dato = (IDato) ContextoLocator.getInstance().getContextoSesion().getCtxValue("USER_RUN_PID");
			value = (String) dato.getValor();
		} catch (PersistenciaException e) {
			logger.error("[getUserRunPidAsString] Error recuperar el valor de la propiedad persistida USER_RUN_PID", e);
		}
		return value;
	}

	public static void setUserPidAsString(String pidSH, String pidRUN) {
		IDato dato; 
		dato = DatoFactory.creaDatoSimple();
		dato.setPropiedad("USER_PID");
		dato.setValor(pidSH);
		try {
			ContextoLocator.getInstance().getContextoSesion().putCtxValue(dato);
		} catch (PersistenciaException e) {
			logger.error("[setUserPidAsString] Error al persistir valor de la propiedad USER_PID", e);
		}
		dato = DatoFactory.creaDatoSimple();
		dato.setPropiedad("USER_RUN_PID");
		dato.setValor(pidRUN);
		try {
			ContextoLocator.getInstance().getContextoSesion().putCtxValue(dato);
		} catch (PersistenciaException e) {
			logger.error("[setUserPidAsString] Error al persistir valor de la propiedad USER_RUN_PID", e);
		}
	}

	public static boolean getUserPidChat() {
		boolean value = false;
		try {
			IDato dato = (IDato) ContextoLocator.getInstance().getContextoSesion().getCtxValue("USER_PID_CHAT");
			if (dato.getValor() == null) {
				value = false;
			} else if (dato.getValor() instanceof Boolean) {
				value = (Boolean) dato.getValor();
			} else {
				value = new Boolean(String.valueOf(dato.getValor()));
			}
		} catch (PersistenciaException e) {
			logger.error("[getUserPidChat] Error recuperar el valor de la propiedad persistida USER_PID_CHAT", e);
		}
		return value;
	}
	public static void setUserPidChat(boolean value) {

		IDato dato = DatoFactory.creaDatoSimple();
		dato.setPropiedad("USER_PID_CHAT");
		dato.setValor(value ? Boolean.TRUE : Boolean.FALSE);
		try {
			ContextoLocator.getInstance().getContextoSesion().putCtxValue(dato);
		} catch (PersistenciaException e) {
			logger.error("[setUserPidChat] Error al persistir valor de la propiedad USER_PID_CHAT", e);
		}
	}

	public static boolean getUserVideoInput() {
		boolean value = false;
		try {
			IDato dato = (IDato) ContextoLocator.getInstance().getContextoSesion().getCtxValue("USER_VIDEO_INPUT");
			if (dato.getValor() == null) {
				value = false;
			} else if (dato.getValor() instanceof Boolean) {
				value = (Boolean) dato.getValor();
			} else {
				value = new Boolean(String.valueOf(dato.getValor()));
			}
		} catch (PersistenciaException e) {
			logger.error("[getUserVideoInput] Error recuperar el valor de la propiedad persistida USER_VIDEO_INPUT", e);
		}
		return value;
	}
	
	public static void setUserVideoInput(boolean value) {

		IDato dato = DatoFactory.creaDatoSimple();
		dato.setPropiedad("USER_VIDEO_INPUT");
		dato.setValor(value ? Boolean.TRUE : Boolean.FALSE);
		try {
			ContextoLocator.getInstance().getContextoSesion().putCtxValue(dato);
		} catch (PersistenciaException e) {
			logger.error("[setUserVideoInput] Error al persistir valor de la propiedad USER_VIDEO_INPUT", e);
		}
	}

	public static boolean getUserAudioInput() {
		boolean value = false;
		try {
			IDato dato = (IDato) ContextoLocator.getInstance().getContextoSesion().getCtxValue("USER_AUDIO_INPUT");
			if (dato.getValor() == null) {
				value = false;
			} else if (dato.getValor() instanceof Boolean) {
				value = (Boolean) dato.getValor();
			} else {
				value = new Boolean(String.valueOf(dato.getValor()));
			}
		} catch (PersistenciaException e) {
			logger.error("[getUserAudioInput] Error recuperar el valor de la propiedad persistida USER_AUDIO_INPUT", e);
		}
		return value;
	}
	
	public static void setUserAudioInput(boolean value) {

		IDato dato = DatoFactory.creaDatoSimple();
		dato.setPropiedad("USER_AUDIO_INPUT");
		dato.setValor(value ? Boolean.TRUE : Boolean.FALSE);
		try {
			ContextoLocator.getInstance().getContextoSesion().putCtxValue(dato);
		} catch (PersistenciaException e) {
			logger.error("[setUserAudioInput] Error al persistir valor de la propiedad USER_AUDIO_INPUT", e);
		}
	}
	
	public static String getUserAccountIdAsString() {
		String value = null;
		try {
			IDato dato = (IDato) ContextoLocator.getInstance().getContextoSesion().getCtxValue("SECURE_USER_ACCOUNTTYPE_ID");
			value = (String) dato.getValor();
		} catch (PersistenciaException e) {
			logger.error("[getUserIdAsString] Error recuperar el valor de la propiedad persistida SECURE_USER_ACCOUNTTYPE_ID", e);
		}
		return value;
	}
	
	public static Integer getUserAccountIdAsInteger() {
		String value = getUserAccountIdAsString();
		return value == null ? null : Integer.valueOf(value);
	}

	public static boolean getX509Certificate() {
		boolean value = false;
		try {
			IDato dato = (IDato) ContextoLocator.getInstance().getContextoSesion().getCtxValue("SECURE_FLAG_CERT");
			if (dato.getValor() == null) {
				value = false;
			} else if (dato.getValor() instanceof Boolean) {
				value = (Boolean) dato.getValor();
			} else {
				value = new Boolean("1".equals(String.valueOf(dato.getValor())));
			}
		} catch (PersistenciaException e) {
			logger.error("[getX509Certificate] Error recuperar el valor de la propiedad persistida SECURE_FLAG_CERT", e);
		}
		return value;
	}

}
