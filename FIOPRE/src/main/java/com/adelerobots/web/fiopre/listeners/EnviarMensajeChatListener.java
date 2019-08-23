package com.adelerobots.web.fiopre.listeners;

import org.apache.commons.lang.StringUtils;

import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;

/**
 * Listener que envia un mensaje al gestor de chat para obtener su respuesta predeterminada
 * 
 * @author adele
 *
 */
public class EnviarMensajeChatListener extends AbstractUserProcessListener
{
	
	private static final long serialVersionUID = -4260406884227995226L;

	public void procesarAjaxChangeListener(
			GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) 
	{
		logger.info("[INI] EnviarMensajeChatListener listener");
		
		// Recuperar valores del formulario necesarios para ejecutar el codigo
		String userMsg = (String) gestorDatos.getValue("chatTA");
		//String gestMsg = (String) gestorDatos.getValue("respuestasTA");
		
		// Validaciones
		if (StringUtils.isBlank(userMsg)) {
			String errMsg = String.format("%s is required", gestorEstados.getPropiedad("chatTA", "label"));
			gestorDatos.addErrorValidacionSimple(errMsg, errMsg, "chatTA");
		}
		//Si hay errores de validacion JSF como requireds o el validador de formulario falla no hacer nada
		if (gestorEstados.getErroresValidacion()) {
			return;
		}
		// Recuperamos PID del usuario
		String runPID = ContextUtils.getUserRunPidAsString();
		
		if(runPID != null && !"0000".equals(runPID))
		{
			boolean ok = false;
			try {
				invokeSendMessageToGestor(runPID, userMsg);
				
			//	String contenido = gestMsg;
			//	contenido = msg; //reemplazar mensajes
				//contenido = "";
				//contenido += (gestMsg != null ? gestMsg + "\n" : "") + msg; //Acumular mensajes fwn:TextArea
				//contenido += (gestMsg != null ? gestMsg + "<br/>" : "") + msg; //Acumular mensajes fwn:OutputText
				
				// Actualizar chat components
			//	gestorDatos.setValue("respuestasTA", contenido);
				gestorDatos.setValue("chatTA", "");
				
				ok = true;
			} catch (FactoriaDatosException e) {
				logger.info("[EnviarMensajeChatListener] Error al publicar mensaje en el gestor de chat", e);
			} catch (PersistenciaException e) {
				logger.info("[EnviarMensajeChatListener] Error al publicar mensaje en el gestor de chat", e);
			}  catch (FawnaInvokerException e) {
				logger.info("[EnviarMensajeChatListener] Error al publicar mensaje en el gestor de chat", e);
			}
			
			if (ok) 
			{
				
			} else {
				// TODO: Informar al usuario del error
				// haciendo uso, por ejemplo, de la consola
				// de la web
				gestorDatos.addErrorValidacionCruzada(
						"An error happens while your message was published in your avatar chat manager!!", 
						"An error happens while your message was published in your avatar chat manager!!");
			}
		} else {
			// TODO: Informar al usuario del error
			// haciendo uso, por ejemplo, de la consola
			// de la web
			gestorDatos.addErrorValidacionCruzada(
					"There is not any avatar process running. Start it one!!", 
					"There is not any avatar process running. Start it one!!");
		}
		
		logger.info("[END] EnviarMensajeChatListener listener");
	}

}
