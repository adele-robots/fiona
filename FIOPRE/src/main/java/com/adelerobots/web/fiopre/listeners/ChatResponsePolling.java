package com.adelerobots.web.fiopre.listeners;

import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;


/**
 * Pool listener para recibir mensajes del Gestor de diálogo de manera asíncrona
 * 
 * @author adele
 *
 */
public class ChatResponsePolling extends AbstractUserProcessListener
{
	private static final long serialVersionUID = 1804628378500373331L;

	public void procesarAjaxChangeListener(
			GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) 
	{
		//logger.info("[INI] ChatResponsePolling listener");		
		
		// Recuperamos PID del proceso Avatar del usuario para obtener el id de la cola de mensajes
		String runPID = ContextUtils.getUserRunPidAsString();
		//String msg = (String) gestorDatos.getValue("respuestasTA");	
		
		if (runPID != null && !"0000".equals(runPID) && !"-1".equals(runPID) && !"1".equals(runPID) ) 
		{
			boolean ok = false;
			try {
				String answer = invokeGetGestorAnswer(runPID);
				//Actualizamos el valor siempre que se obtenga un nuevo mensaje
				if(answer!=null && !"".equalsIgnoreCase(answer)){
					// Actualizar mensajes del gestor de diálogo
					gestorDatos.setValue("respuestasTA", answer);		
					// Sólo reRenderizamos si obtenemos mensajes
					gestorEstados.setPropiedad("pollChatResponse", "reRender", "respuestasTA");
				}else{
					// Si no hay nuevos mensajes, no se reRenderiza, consiguiendo bajar el tráfico de datos
					// de red en el polling, y que no reRenderice mismos mensajes (scroll no funciona bien)
					gestorEstados.setPropiedad("pollChatResponse", "reRender", null);
				}
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
			gestorEstados.setPropiedad("pollChatResponse", "reRender", null);
			// TODO: Informar al usuario del error
			// haciendo uso, por ejemplo, de la consola
			// de la web
			gestorDatos.addErrorValidacionCruzada(
					"There is not any avatar process running. Start it one!!", 
					"There is not any avatar process running. Start it one!!");
		}
		
		//logger.info("[END] ChatResponsePolling listener");
	}

}
