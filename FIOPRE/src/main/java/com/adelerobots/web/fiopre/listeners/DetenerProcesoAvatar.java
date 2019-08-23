package com.adelerobots.web.fiopre.listeners;

import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;

/**
 * Listener que destruye el proceso avatar de un usuario
 * 
 * @author adele
 *
 */
public class DetenerProcesoAvatar extends AbstractUserProcessListener
{
	
	private static final long serialVersionUID = -4678606898080996624L;

	public void procesarAjaxChangeListener(
			GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) 
	{
		logger.info("[INI] DetenerProcesoAvatar listener");
		
		//Si hay errores de validacion JSF como requireds o el validador de formulario falla no hacer nada
		if (gestorEstados.getErroresValidacion()) {
			return;
		}
		
		String userid = ContextUtils.getUserIdAsString();
		String shPID = ContextUtils.getUserShPidAsString();
		String runPID = ContextUtils.getUserRunPidAsString();
		
		
		boolean ok = false;
		try {			
			logger.info("[DetenerProcesoAvatar] PID en listener: " + shPID);
			/*
			 * MOVIDA GUILLE, a veces (no sé por qué) coge el pid -1 y lo intenta matar
			 * por eso pongo el && !"-1".equalsIgnoreCase(shPID)
			 */
			if (!"0000".equalsIgnoreCase(shPID) && !"-1".equalsIgnoreCase(shPID) && !"1".equalsIgnoreCase(shPID))
			{
				invokeDestroyUserAvatarProcess(userid, shPID);
				//Reset PID context value
				ContextUtils.setUserPidAsString("0000", "0000");
			}
			
			
			ok = true;
		} catch (PersistenciaException e) {
			logger.info("[DetenerProcesoAvatar] Error al destruir el proceso avatar", e);
		} catch (FactoriaDatosException e) {
			logger.info("[DetenerProcesoAvatar] Error al destruir el proceso avatar", e);
		} catch (FawnaInvokerException e) {
			logger.info("[DetenerProcesoAvatar] Error al destruir el proceso avatar", e);
		}
		
		if (ok) 
		{
			//Cambiar estado de los avatar components
			gestorEstados.setPropiedad("pollAvatarProcess", "enabled", Boolean.FALSE);
			gestorEstados.setPropiedad("pollChatResponse", "enabled", Boolean.FALSE);			
			gestorEstados.setPropiedad("pollErrConsole", "enabled", Boolean.FALSE);
			gestorEstados.setPropiedad("stopAvatarProcess", "rendered", Boolean.FALSE);
			gestorEstados.setPropiedad("startAvatarProcess", "rendered", Boolean.TRUE);
			
			// Ocultar el modulo de chat / poner a default sus componentes
			gestorEstados.setPropiedad("panelChat", "rendered", Boolean.FALSE);
			gestorDatos.setValue("respuestasTA", "");
			gestorDatos.setValue("chatTA", "");
			
		} else {
			// TODO: Informar al usuario del error
			// haciendo uso, por ejemplo, de la consola
			// de la web
			gestorDatos.addErrorValidacionCruzada(
					"A error happens while stopping avatar process!!", 
					"A error happens while stopping avatar process!!");
		}
		
		logger.info("[END] DetenerProcesoAvatar listener");
	}

}
