package com.adelerobots.fioneg.service.batch;

import java.util.Date;

import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.SparkManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;
/**
 * Servicio de negocio que se ejecutará diariamente para comprobar
 * los usuarios a los que se les ha acabado el periodo de prueba de
 * sus sparks
 * 
 * @author adele
 *
 */
public class SNBatchCheckFinTrial extends ServicioNegocio {
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNBatchCheckFinTrial.class);
	
	public SNBatchCheckFinTrial(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029044: Comprobar si ha expirado algún periodo de prueba.");
		Date datInicio = new Date();
		
		SparkManager sparkManager = ManagerFactory.getInstance().getSparkManager();
		
		sparkManager.deactivateExpiredTrialSparks();
		
		IContexto[] salida = null;
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029044: Comprobar si ha expirado algún periodo de prueba. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
