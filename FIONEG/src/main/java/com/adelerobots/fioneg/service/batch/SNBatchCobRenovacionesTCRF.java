package com.adelerobots.fioneg.service.batch;

import java.util.Date;

import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;


/**
 * Servicio de negocio que se ejecutará diariamente para comprobar
 * a qué usuarios debe pasárseles el cobro de la suscripción
 * mensual o anual a la plataforma
 * @author adele
 *
 */
public class SNBatchCobRenovacionesTCRF extends ServicioNegocio{

	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNBatchCobRenovacionesTCRF.class);
	
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {		
		LOGGER.info("Inicio Ejecucion del SN 029082: Comprobar a qué usuarios debe pasárseles el cobro de la suscripción al backoffice.");
		Date datInicio = new Date();
		
		UsuariosManager usuariosManager = ManagerFactory.getInstance().getUsuariosManager();
		
		//usuariosManager.planificarPrimerPagoTCRFProduccion(contexto);
				
		usuariosManager.cobrarRenovacionesSparksTCRF(contexto);
		
		IContexto[] salida = null;
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029082: Comprobar a qué usuarios debe pasárseles el cobro de la suscripción al backoffice. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
