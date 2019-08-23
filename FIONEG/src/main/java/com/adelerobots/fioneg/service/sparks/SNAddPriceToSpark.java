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
public class SNAddPriceToSpark extends ServicioNegocio {
	
	private static final int CTE_POSICION_NUM_USUARIOS = 0;
	private static final int CTE_POSICION_ID_UNIDAD_TIEMPO = 1;
	private static final int CTE_POSICION_ID_UNIDAD_USO = 2;
	private static final int CTE_POSICION_CANTIDAD = 3;
	private static final int CTE_POSICION_EUROS = 4;
	private static final int CTE_POSICION_SPARK_ID = 5;
	private static final int CTE_POSICION_DESARROLLO = 6;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNAddPriceToSpark.class);

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029051: Añadir un precio a un spark existente.");
		Date datInicio = new Date();
		
		BigDecimal bidNumUsuarios = datosEntrada.getDecimal(CTE_POSICION_NUM_USUARIOS);
		
		Integer intNumUsuarios = new Integer(bidNumUsuarios.intValue());
		
		BigDecimal bidIdUnidadTiempo = datosEntrada.getDecimal(CTE_POSICION_ID_UNIDAD_TIEMPO);
		
		Integer intIdUnidadTiempo = null;
		if(bidIdUnidadTiempo != null){
			intIdUnidadTiempo = new Integer(bidIdUnidadTiempo.intValue());
		}
		
		BigDecimal bidIdUnidadUso = datosEntrada.getDecimal(CTE_POSICION_ID_UNIDAD_USO);
		
		Integer intIdUnidadUso = null;
		if(bidIdUnidadUso != null){
			intIdUnidadUso = new Integer(bidIdUnidadUso.intValue());
		}
		
		BigDecimal bidCantidad = datosEntrada.getDecimal(CTE_POSICION_CANTIDAD);
		
		Integer intCantidad = new Integer(bidCantidad.intValue());		
		
		BigDecimal bidEuros = datosEntrada.getDecimal(CTE_POSICION_EUROS);
		
		Float floEuros = new Float(bidEuros.floatValue());		
		
		BigDecimal bidSparkId = datosEntrada.getDecimal(CTE_POSICION_SPARK_ID);
		
		Integer intSparkId = new Integer(bidSparkId.intValue());
		
		String strEsDesarrollo = datosEntrada.getString(CTE_POSICION_DESARROLLO);
		
		
		SparkManager sparkManager = ManagerFactory.getInstance().getSparkManager();
		
		SparkC spark = null;
		try {
			spark = sparkManager.getSpark(intSparkId, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PriceManager priceManager = ManagerFactory.getInstance().getPriceManager();
		
		PriceC precio = priceManager.addPriceToSpark(intNumUsuarios, intIdUnidadTiempo, intIdUnidadUso, intCantidad, floEuros, spark, strEsDesarrollo);
				
		
		IContexto [] salida = ContextoPrice.rellenarContexto(precio);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029051: Añadir un precio a un spark existente. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
