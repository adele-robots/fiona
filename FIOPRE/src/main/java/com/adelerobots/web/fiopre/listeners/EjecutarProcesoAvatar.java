package com.adelerobots.web.fiopre.listeners;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import org.jdom.JDOMException;

import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * Listener que arranca el proceso avatar de un usuario
 * 
 * @author adele
 *
 */
public class EjecutarProcesoAvatar extends AbstractUserProcessListener
{
	
	private static final long serialVersionUID = -9135351912749099194L;
	
	private static final String ONCOMPLETE_HIDDEN_OK = "activarNuevoClickAlert();";
	private static final String ONCOMPLETE_HIDDEN_ERROR = "stopAvatarPlayers();";

	public void procesarAjaxChangeListener(
			GestorEstadoComponentes gestorEstados, 
			GestorDatosComponentes gestorDatos) 
	{
		logger.info("[INI] EjecutarProcesoAvatar listener");
		
//		//Si hay errores de validacion JSF como requireds o el validador de formulario falla no hacer nada
//		if (gestorEstados.getErroresValidacion()) {
//			return;
//		}
		
		String usermaild5 = ContextUtils.getUserMailD5();
		String userid = ContextUtils.getUserIdAsString();
		String shPID = ContextUtils.getUserShPidAsString();
		String runPID = ContextUtils.getUserRunPidAsString();
		Integer useridInt = ContextUtils.getUserIdAsInteger();
		String roomId = (String)gestorDatos.getValue("randomRoom");
		Integer userAccountId = ContextUtils.getUserAccountIdAsInteger();
		
		String isAllowedExecution = "";
		String timeTowait = "";		

	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		String session = attr.getRequest().getSession().getId();
		try {
			File avatarSesFile = new File("/tmp/" + session + ".ini");
			if(!avatarSesFile.exists()) {
				avatarSesFile.createNewFile();
			}
			Properties p = new Properties();
		    p.load(new FileInputStream(avatarSesFile));
			p.put("room", "\"" + roomId + "\";");
			p.put("session", "\"" + session + "\";");
			FileOutputStream output = new FileOutputStream(avatarSesFile);
			p.store(output, "Session properties");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		// Reescribir "generalConf.ini" antes de la ejecución
		try {
			invokeSNRewriteGeneralConf(usermaild5,"room",userAccountId);
		} catch (FactoriaDatosException fde) {
			logger.info("[EjecutarProcesoAvatar] Error al reescribir 'generalConf.ini'", fde);
		} catch (PersistenciaException pe) {
			logger.info("[EjecutarProcesoAvatar] Error al reescribir 'generalConf.ini'", pe);
		} catch (FawnaInvokerException fie) {
			logger.info("[EjecutarProcesoAvatar] Error al reescribir 'generalConf.ini'", fie);
		}		
		
		/*
		 * Comprobamos que el usuario no tiene PID. 
		 * si no lo tiene continuar, si lo tiene, matar proceso y continuar
		 */
		try { //MOVIDA GUILLE, a veces (no sé por qué) coge el pid -1 (o 1) y lo intenta matar
			if (!"0000".equalsIgnoreCase(shPID) && !"-1".equalsIgnoreCase(shPID) && !"1".equalsIgnoreCase(shPID) ) {
				logger.info("[EjecutarProcesoAvatar] Matando proceso avatar anterior: " + shPID);
				//Matamos proceso anterior
				invokeDestroyUserAvatarProcess(userid, shPID);
				//Reset PID context value
				ContextUtils.setUserPidAsString("0000", "0000");
			}
		} catch (PersistenciaException e) {
			logger.info("[EjecutarProcesoAvatar] Error al matar proceso avatar anterior", e);
		} catch (FactoriaDatosException e) {
			logger.info("[EjecutarProcesoAvatar] Error al matar proceso avatar anterior", e);
		} catch (FawnaInvokerException e) {
			logger.info("[EjecutarProcesoAvatar] Error al matar proceso avatar anterior", e);
		}	
		
		try {					
			// Comprobar que el usuario puede iniciar una ejecución del avatar
			String[] checkExecution = invokeCheckAllowedExecution(useridInt);
			
			isAllowedExecution = checkExecution[0];
			timeTowait = checkExecution[1];
		} catch (FactoriaDatosException e1) {
			e1.printStackTrace();
		} catch (PersistenciaException e1) {
			e1.printStackTrace();
		} catch (FawnaInvokerException e1) {
			e1.printStackTrace();
		}		
		// Se puede iniciar la ejecución del avatar
		if(isAllowedExecution.equals("1")){			
			boolean ok = false;
			boolean activaChat = false;
			boolean videoInput = false;
			boolean audioInput = false;
			try {
				// Comprobar antes de nada si el check "Published" ha sido marcado
				String strIsPublished = invokeCheckWebPublished(useridInt);
				
				if(strIsPublished != null && "1".equals(strIsPublished)){					
					gestorEstados.setPropiedad("pollAvatarProcess", "enabled", Boolean.FALSE);
					gestorEstados.setPropiedad("pollChatResponse", "enabled", Boolean.FALSE);
					gestorEstados.setPropiedad("pollErrConsole", "enabled", Boolean.FALSE);
					gestorEstados.setPropiedad("stopAvatarProcess", "rendered", Boolean.FALSE);
					gestorEstados.setPropiedad("startAvatarProcess", "rendered", Boolean.TRUE);
					
					// Mostrar alert informando al usuario del problema
					String idAlert = "alertSparksInactivos";
					// Asignar título y contenido adecuado
					handleModalAlert(gestorDatos, gestorEstados, idAlert, "warning", getMessage("FIONA.alertPublishedChecked.cabeceraPanel.valor", "Published checked!!"),
							getMessage("FIONA.alertPublishedChecked.mensajeError.valor", "Inactive Sparks!!"), "", "");
					
					gestorEstados.setPropiedad("hiddenStartButton", "oncomplete", ONCOMPLETE_HIDDEN_ERROR);					
					
					return;
				}
				
				String[] pids = invokeCreateUserAvatarProcess(usermaild5, useridInt, session);
				shPID = pids[0];
				runPID = pids[1];

				if ("99991".equalsIgnoreCase(shPID) || "99991".equalsIgnoreCase(runPID)){
					//ya hay un proceso abierto así que cambiaré alguna cosa y retornaré
					gestorEstados.setPropiedad("pollAvatarProcess", "enabled", Boolean.FALSE);
					gestorEstados.setPropiedad("pollChatResponse", "enabled", Boolean.FALSE);
					gestorEstados.setPropiedad("pollErrConsole", "enabled", Boolean.FALSE);
					gestorEstados.setPropiedad("stopAvatarProcess", "rendered", Boolean.FALSE);
					gestorEstados.setPropiedad("startAvatarProcess", "rendered", Boolean.TRUE);
					
					// Mostrar alert informando al usuario del problema
					String idAlert = "alertSparksInactivos";
					// Asignar título y contenido adecuado
					handleModalAlert(gestorDatos, gestorEstados, idAlert, "error", getMessage("FIONA.alertTooManyProcess.cabeceraPanel.valor", "Too many clients!!"),
							getMessage("FIONA.alertTooManyProcess.mensajeError.valor", "Too many clients!!"), "", "");				
					//Reset PID context value
					ContextUtils.setUserPidAsString("0000", "0000");
					
					gestorEstados.setPropiedad("hiddenStartButton", "oncomplete", ONCOMPLETE_HIDDEN_ERROR);
					
					return;
				}				
				if ("99999".equalsIgnoreCase(shPID) || "99999".equalsIgnoreCase(runPID)){
					// Alguno de los sparks que el usuario intenta ejecutar en esta configuración
					// está desactivado
					gestorEstados.setPropiedad("pollAvatarProcess", "enabled", Boolean.FALSE);
					gestorEstados.setPropiedad("pollChatResponse", "enabled", Boolean.FALSE);
					gestorEstados.setPropiedad("pollErrConsole", "enabled", Boolean.FALSE);
					gestorEstados.setPropiedad("stopAvatarProcess", "rendered", Boolean.FALSE);
					gestorEstados.setPropiedad("startAvatarProcess", "rendered", Boolean.TRUE);
					
					// Mostrar alert informando al usuario del problema
					String idAlert = "alertSparksInactivos";
					// Asignar título y contenido adecuado
					handleModalAlert(gestorDatos, gestorEstados, idAlert, "error", getMessage("FIONA.alertSparksInactivos.cabeceraPanel.valor", "Inactive Sparks!!"),
							getMessage("FIONA.alertSparksInactivos.mensajeError.valor", "Inactive Sparks!!"), "", "");				
					//Reset PID context value
					ContextUtils.setUserPidAsString("0000", "0000");
					
					gestorEstados.setPropiedad("hiddenStartButton", "oncomplete", ONCOMPLETE_HIDDEN_ERROR);
					
					return;
				}
				try {
					activaChat = detectChatComponent(usermaild5);
					videoInput = detectComponent(usermaild5, "AVInputSpark");
					audioInput = detectComponent(usermaild5, "AudioInputSpark");
				} catch (FileNotFoundException e) {
					logger.info("[EjecutarProcesoAvatar] Error al detectar componente chat en la config", e);
				} catch (JDOMException e) {
					logger.info("[EjecutarProcesoAvatar] Error al detectar componente chat en la config", e);
				} catch (IOException e) {
					logger.info("[EjecutarProcesoAvatar] Error al detectar componente chat en la config", e);
				}

				ok = true;
			}catch (FawnaInvokerException e) {
				logger.info("[EjecutarProcesoAvatar] Error al crear el proceso avatar", e);
			} catch (FactoriaDatosException e) {
				logger.info("[EjecutarProcesoAvatar] Error al crear el proceso avatar", e);
			} catch (PersistenciaException e) {
				logger.info("[EjecutarProcesoAvatar] Error al crear el proceso avatar", e);
			}
			if (ok)
			{
				//Change component status
				gestorEstados.setPropiedad("pollAvatarProcess", "enabled", Boolean.TRUE);
				gestorEstados.setPropiedad("pollAvatarProcess", "oncomplete", "");	
				gestorEstados.setPropiedad("pollAvatarProcess", "reRender", null);
				gestorEstados.setPropiedad("pollChatResponse", "enabled", Boolean.TRUE);
				gestorEstados.setPropiedad("pollErrConsole", "enabled", Boolean.TRUE);
				gestorEstados.setPropiedad("stopAvatarProcess", "rendered", Boolean.TRUE);
				gestorEstados.setPropiedad("startAvatarProcess", "rendered", Boolean.FALSE);

				gestorEstados.setPropiedad("hiddenStartButton", "oncomplete", ONCOMPLETE_HIDDEN_OK);

				ContextUtils.setUserPidChat(activaChat);
				ContextUtils.setUserVideoInput(videoInput);
				ContextUtils.setUserAudioInput(audioInput);
				if (activaChat) {
					// Mostramos los textArea para el módulo de chat
					gestorEstados.setPropiedad("panelChat", "rendered", Boolean.TRUE);
					gestorDatos.setValue("respuestasTA", "");
					gestorDatos.setValue("chatTA", "");
					logger.info("[EjecutarProcesoAvatar] Encontrado componente chat en la config");
				} else {
					// Nos aseguramos de ocultarlo
					gestorEstados.setPropiedad("panelChat", "rendered", Boolean.FALSE);
					logger.info("[EjecutarProcesoAvatar] No encontrado componente chat en la config");
				}
			} else {
				// TODO: Informar al usuario del error
				// haciendo uso, por ejemplo, de la consola
				// de la web
				gestorDatos.addErrorValidacionCruzada(
						"An error happenned while starting avatar process!!", 
				"An error happenned while starting avatar process!!");
			}			
		}else if (isAllowedExecution.equals("-1")){		
			// No se puede ejecutar el avatar, esto es porque el usuario es 'free' y todavía no pasó su tiempo de espera	
			// Se le informa a través de un alert
			handleModalAlert(gestorDatos, gestorEstados, "alertSparksInactivos", "info", "Avatar execution info",
					getMessage("FIONA.alertTimeToWait.mensajeInfo.valor", 
							new Object[]{new Integer((Integer.parseInt(timeTowait))/1000)}, 
					"You must wait for a new avatar execution"),
					"", "");
		}
		
		logger.info("[END] EjecutarProcesoAvatar listener");
	}

}
