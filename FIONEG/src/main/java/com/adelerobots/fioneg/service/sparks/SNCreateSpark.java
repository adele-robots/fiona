package com.adelerobots.fioneg.service.sparks;

import java.math.BigDecimal;
import java.util.Date;

import com.adelerobots.fioneg.context.ContextoSparks;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.SparkManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;


/**
 * Servicio de Negocio fachada para modificar la información
 * relativa a un determinado spark
 * 
 * @author adele
 * 
 */
public class SNCreateSpark extends ServicioNegocio {
		 
	private static final int CTE_POSICION_NOMBRE_SPARK = 0; // Not null
	private static final int CTE_POSICION_USUARIO_DESARROLLADOR = 1; // Not null
	private static final int CTE_POSICION_DESCRIPCION_SPARK = 2; 
	private static final int CTE_POSICION_VERSION_SPARK = 3;
	private static final int CTE_POSICION_DESC_CORTA_SPARK = 4;
	private static final int CTE_POSICION_EMAIL_SOPORTE = 5;	
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNCreateSpark.class);

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029047: Crear un spark.");
		Date datInicio = new Date();
				
		String strNombreSpark = datosEntrada.getString(CTE_POSICION_NOMBRE_SPARK);
		BigDecimal bidDesarrollador = datosEntrada.getDecimal(CTE_POSICION_USUARIO_DESARROLLADOR);
		String strDescSpark = datosEntrada.getString(CTE_POSICION_DESCRIPCION_SPARK);
		String strVersion = datosEntrada.getString(CTE_POSICION_VERSION_SPARK);
		String strDescCorta = datosEntrada.getString(CTE_POSICION_DESC_CORTA_SPARK);		
		String strEmailSoporte = datosEntrada.getString(CTE_POSICION_EMAIL_SOPORTE);							
		
		// Convertimos el código a Integer
		Integer intDesarrollador = new Integer(bidDesarrollador.intValue());		
		
		SparkManager sparkManager = ManagerFactory.getInstance().getSparkManager();			
		SparkC spark = new SparkC();
		spark = sparkManager.crearSpark(strNombreSpark, intDesarrollador, strDescSpark, strVersion, strDescCorta, strEmailSoporte);
			
		
		IContexto[] salida = ContextoSparks.rellenarContexto(spark);
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029047: Crear un spark. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
