package com.adelerobots.fioneg.service.sparks;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoSparkStatus;
import com.adelerobots.fioneg.entity.sparkstatus.SparkStatusC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.StatusManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;


/**
 * Servicio de Negocio fachada para recuperar el listado de sparks
 * desarrollados por un usuario con perfil "desarrollador"
 * 
 * @author adele
 * 
 */
public class SNListStatus extends ServicioNegocio{

	private static final int CTE_POSICION_ID_SPARK = 0;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNListStatus.class);
	
	public SNListStatus(){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029029: Devolver listado de estados del spark");
		Date datInicio = new Date();
		
		
		BigDecimal bidCodSpark = datosEntrada.getDecimal(CTE_POSICION_ID_SPARK);
		
		// Convertimos el código a Integer
		Integer intCodSpark = new Integer(bidCodSpark.intValue());		
		
		StatusManager statusManager = ManagerFactory.getInstance().getStatusManager();		
			
		List <SparkStatusC> lstStatus = statusManager.getListSparkStatus(intCodSpark);
		
		IContexto[] salida = ContextoSparkStatus.rellenarContexto(lstStatus);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029029: Devolver listado de estados del spark con id" + intCodSpark +". Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
		
	}
	
}
