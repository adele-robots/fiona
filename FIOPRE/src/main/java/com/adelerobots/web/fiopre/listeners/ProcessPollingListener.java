package com.adelerobots.web.fiopre.listeners;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;


/**
 * Pool listener para detectar si el proceso avatar de un usuario sigue corriendo
 * 
 * @author adele
 *
 */
public class ProcessPollingListener extends AbstractUserProcessListener
{
	private static final long serialVersionUID = 3927423559161819148L;

	public void procesarAjaxChangeListener(
			GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) 
	{
		logger.info("[INI] ProcessPollingListener listener");
		
		Integer userid = ContextUtils.getUserIdAsInteger();
		Integer shPID = ContextUtils.getUserShPidAsInteger();		
		
		boolean ok = false;
		try {
			if (shPID != 0) {
				String processCheck = invokeTouchUserAvatarProcess(userid, shPID);
				// Si el proceso está marcado como "arpia", significa que ya ha sido matado por ARPIA y desde aquí
				// debemos completar los pasos para una correcta finalización de la ejecución del avatar.
				if(processCheck.equals("arpia")){					
					//Reset PID context value
					ContextUtils.setUserPidAsString("0000", "0000");
					
					// Parar pollings
					gestorEstados.setPropiedad("pollAvatarProcess", "enabled", Boolean.FALSE);

					gestorEstados.setPropiedad("pollChatResponse", "enabled", Boolean.FALSE);			
					gestorEstados.setPropiedad("pollErrConsole", "enabled", Boolean.FALSE);
					
					// Sólo reRenderizamos cuando se haya matado al proceso y haya que modificar la vista					
					gestorEstados.setPropiedad("pollAvatarProcess", "reRender", "botonera,panelChatWrap,alertSparksInactivos");
					// Se esconde el botón 'Stop' y se muestra 'Start'				
					gestorEstados.setPropiedad("stopAvatarProcess", "rendered", Boolean.FALSE);					
					gestorEstados.setPropiedad("startAvatarProcess", "rendered", Boolean.TRUE);
					
					// Ocultar el modulo de chat / poner a default sus componentes
					gestorEstados.setPropiedad("panelChat", "rendered", Boolean.FALSE);
					gestorDatos.setValue("respuestasTA", "");
					gestorDatos.setValue("chatTA", "");
					
					// Al finalizar este último polling se llamará a la función js "stopAvatarPlayers()"
					gestorEstados.setPropiedad("pollAvatarProcess", "oncomplete", "stopAvatarPlayers();");					
					
					// Se muestra un alert al usuario informándole que su tiempo de ejecución ha finalizado
					handleModalAlert(gestorDatos, gestorEstados, "alertSparksInactivos", "info",
								getMessage("FIONA.alertAvatarExecution.cabeceraPanel.valor","Avatar execution info"),
								getMessage("FIONA.alertTimeout.mensajeInfo.valor","Free avatar execution timeout"), "", "");
					
					// Limpiar la estructura que almacena los ids de las colas de mensajes en sesion
				    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			        HttpSession ses = attr.getRequest().getSession();
			        ses.removeAttribute(ses.getId());
				}
			}
		} catch (PersistenciaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoriaDatosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FawnaInvokerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (ok) 
		{
			
		} else {
			// TODO: Informar al usuario del error
			// haciendo uso, por ejemplo, de la consola
			// de la web
			gestorDatos.addErrorValidacionCruzada(
					"A error happenned while notifying that your avatar process is alive!!", 
					"A error happenned while notifying that your avatar process is alive!!");
		}
		
		logger.info("[END] ProcessPollingListener listener");
	}

}
