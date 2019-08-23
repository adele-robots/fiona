package com.adelerobots.fioneg.service.usuarios;

import java.math.BigDecimal;
import java.util.Date;

import com.adelerobots.fioneg.context.ContextoWebPublished;
import com.adelerobots.fioneg.entity.WebPublishedC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.WebPublishedManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;


/**
 * Servicio de Negocio fachada para comprobar si un usuario
 * tiene o no activada la publicación del scriplet
 * 
 * @author adele
 * 
 */
public class SNCheckWebPublished extends ServicioNegocio{

	private static final int CTE_POSICION_USER_ID = 0;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNCheckWebPublished.class);
		
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029035: Comprobación, para un usuario, de la publicación del scriplet de su avatar.");
		Date datInicio = new Date();
		
		BigDecimal bidCodUsuario = datosEntrada.getDecimal(CTE_POSICION_USER_ID);
		
		// Convertimos el código a Integer
		Integer intCodUsuario = new Integer(bidCodUsuario.intValue());
		
		WebPublishedManager webPublishedManager = ManagerFactory.getInstance().getWebPublishedManager();
		
		//Boolean isPublished = webPublishedManager.checkWebPublished(intCodUsuario);
		
		WebPublishedC webPublished = webPublishedManager.getWebPublished(intCodUsuario);

		
		IContexto[] salida = ContextoWebPublished.rellenarContexto(webPublished);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029035: Comprobación, para un usuario, de la publicación del scriplet de su avatar. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
