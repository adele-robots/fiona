package com.adelerobots.fioneg.service.sparks;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoSparks;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.SparkManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNGetRandomBanners extends ServicioNegocio {
	
	private static final int CTE_POSICION_NUM_BANNERS = 0; // Not null 
	
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNGetRandomBanners.class);

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029043: Recuperar X banners de sparks aleatorios.");
		Date datInicio = new Date();
		
		BigDecimal bidNumBanners = datosEntrada.getDecimal(CTE_POSICION_NUM_BANNERS);	
		
		Integer intNumBanners = new Integer(bidNumBanners.intValue());
		
		
		SparkManager sparkManager = ManagerFactory.getInstance().getSparkManager();
		
		List<SparkC> lstSparks = sparkManager.getRandomBanners(intNumBanners);
		
		IContexto[] salida = ContextoSparks.rellenarContexto(lstSparks);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029043: Recuperar X banners de sparks aleatorios.. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
