package com.adelerobots.fioneg.service.usuarios;

import java.math.BigDecimal;
import java.util.Date;

import com.adelerobots.fioneg.context.ContextoWebPublished;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.entity.WebPublishedC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.adelerobots.fioneg.manager.WebPublishedManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

/**
 * Servicio de Negocio fachada para marcar como publicado
 * o no el scriplet de un usuario
 * 
 * @author adele
 * 
 */
public class SNChangeWebPublished extends ServicioNegocio {
	
	private static final int CTE_POSICION_ID_USUARIO = 0;
	private static final int CTE_POSICION_IS_PUBLISHED = 1;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNChangeWebPublished.class);
	
	
	public SNChangeWebPublished(){
		super();
	}
	
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029036: Marcar/Desmarcar publicación del scriplet de un usuario.");
		Date datInicio = new Date();
		
		BigDecimal bidCodUsuario = datosEntrada.getDecimal(CTE_POSICION_ID_USUARIO);
		String strIsPublished = datosEntrada.getString(CTE_POSICION_IS_PUBLISHED);
		
		// Pasamos el código a integer
		Integer intCodUsuario = bidCodUsuario.intValue();
		
		Character chIsPublished = strIsPublished.charAt(0);		
		
		UsuariosManager usuariosManager = ManagerFactory.getInstance().getUsuariosManager();
		WebPublishedManager webPublishedManager = ManagerFactory.getInstance().getWebPublishedManager();
		
		UsuarioC usuario = null;
		WebPublishedC webPublished = null;
		try {
			usuario = usuariosManager.getUsuario(intCodUsuario);
			
			webPublished = webPublishedManager.changeWebPublished(usuario, chIsPublished);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
		IContexto[] salida = ContextoWebPublished.rellenarContexto(webPublished);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029036: Marcar/Desmarcar publicación del scriplet de un usuario. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
