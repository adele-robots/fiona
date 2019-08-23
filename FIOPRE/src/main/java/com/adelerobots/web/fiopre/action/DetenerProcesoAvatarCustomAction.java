package com.adelerobots.web.fiopre.action;

import org.apache.log4j.Logger;

import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.presentacion.componentes.event.api.SNInvoker;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.exception.PresentacionException;
import com.treelogic.fawna.presentacion.core.persistencia.DatoFawnaFactory;
import com.treelogic.fawna.presentacion.core.persistencia.IDatosFawna;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperCustom;
import com.treelogic.fawna.presentacion.core.webflow.action.ICustomAction;

/**
 * Clase encargada de detener el proceso avatar, de existir alguno activo
 * @author adele
 */
public class DetenerProcesoAvatarCustomAction implements ICustomAction {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3713884305627438645L;
	
	protected final Logger logger = Logger.getLogger(getClass());

	public void ejecutar(
			final IHelperCustom helper) 
		throws PresentacionException 
	{
		logger.info("[INI] DetenerProcesoAvatarCustomAction");
		
		String userid = ContextUtils.getUserIdAsString();
		String shPID = ContextUtils.getUserShPidAsString();
		String runPID = ContextUtils.getUserRunPidAsString();
		
		
		boolean ok = false;
		try {
			
			logger.info("[DetenerProcesoAvatarCustomAction] PID en CustomAction: " + shPID);
			
			if (!"0000".equalsIgnoreCase(shPID))
			{
				invokeDestroyUserAvatarProcess(userid, shPID);
				//Reset PID context value
				ContextUtils.setUserPidAsString("0000", "0000");
			}
			
			
			ok = true;
		} catch (PersistenciaException e) {
			logger.info("[DetenerProcesoAvatarCustomAction] Error al destruir el proceso avatar", e);
		} catch (FactoriaDatosException e) {
			logger.info("[DetenerProcesoAvatarCustomAction] Error al destruir el proceso avatar", e);
		} catch (FawnaInvokerException e) {
			logger.info("[DetenerProcesoAvatarCustomAction] Error al destruir el proceso avatar", e);
		}
		
		if (ok) 
		{	
			
			//Cambiar estado de los avatar components
//			gestorEstados.setPropiedad("pollAvatarProcess", "enabled", Boolean.FALSE);
//			gestorEstados.setPropiedad("stopAvatarProcess", "rendered", Boolean.FALSE);
			
			// Ocultar el modulo de chat / poner a default sus componentes
//			gestorEstados.setPropiedad("panelChat", "rendered", Boolean.FALSE);
//			gestorDatos.setValue("respuestasTA", "");
//			gestorDatos.setValue("chatTA", "");
			
		} else {
			// TODO: Informar al usuario del error
			// haciendo uso, por ejemplo, de la consola
			// de la web
			
		}
		
		logger.info("[END] DetenerProcesoAvatarCustomAction");
	}
	
	/**
	 * Invocar a SN029010 con datos de entrada userId y userPid
	 * destruyendo el proceso avatar de un usuario
	 * 
	 * @param userid - identificador de usuario
	 * @param shPID - identificador del proceso avatar para ese usuario
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	private void invokeDestroyUserAvatarProcess(
			String userid, 
			String shPID) 
	throws FactoriaDatosException, 
			PersistenciaException,
			FawnaInvokerException 
	{
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		//Invoke SN to stop and unregister process
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", shPID, "String");
		invoker.invokeSNParaCampo("029", "010", datosEntrada, null, false);
	}

}