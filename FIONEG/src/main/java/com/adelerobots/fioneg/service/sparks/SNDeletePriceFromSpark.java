package com.adelerobots.fioneg.service.sparks;

import java.math.BigDecimal;
import java.util.Date;

import com.adelerobots.fioneg.context.ContextoPrice;
import com.adelerobots.fioneg.entity.PriceC;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.PriceManager;
import com.adelerobots.fioneg.manager.SparkManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;


/**
 * Servicio de Negocio fachada para asociar un nuevo precio a un spark
 * 
 * @author adele
 * 
 */
public class SNDeletePriceFromSpark extends ServicioNegocio {
	
	private static final int CTE_POSICION_PRICE_ID = 0;
	private static final int CTE_POSICION_SPARK_ID = 1;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNDeletePriceFromSpark.class);

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029054: Eliminar un precio de un spark existente.");
		Date datInicio = new Date();
		
		BigDecimal bidCodPrecio = datosEntrada.getDecimal(CTE_POSICION_PRICE_ID);
		
		Integer intCodPrecio = new Integer(bidCodPrecio.intValue());
		
				
		BigDecimal bidSparkId = datosEntrada.getDecimal(CTE_POSICION_SPARK_ID);
		
		Integer intSparkId = new Integer(bidSparkId.intValue());
		
		PriceManager priceManager = ManagerFactory.getInstance().getPriceManager();
		
		PriceC precio = priceManager.deletePrice(intCodPrecio,intSparkId);
				
		
		IContexto [] salida = ContextoPrice.rellenarContexto(precio);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029054: Eliminar un precio de un spark existente. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
