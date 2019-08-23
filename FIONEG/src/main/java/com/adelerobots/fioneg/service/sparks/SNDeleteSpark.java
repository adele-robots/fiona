package com.adelerobots.fioneg.service.sparks;

import java.math.BigDecimal;
import java.util.Date;

import com.adelerobots.fioneg.context.ContextoSparks;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.SparkManager;
import com.adelerobots.fioneg.util.keys.ConstantesError;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.RollbackException;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;


/**
 * Servicio de Negocio fachada para recuperar el listado de errores asociados
 * a un spark
 * 
 * @author adele
 * 
 */
public class SNDeleteSpark extends ServicioNegocio{

	private static final int CTE_POSICION_ID_SPARK = 0;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNDeleteSpark.class);
	
	public SNDeleteSpark(){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029032: Borrado lógico de spark.");
		Date datInicio = new Date();		
		
		BigDecimal bidCodSpark = datosEntrada.getDecimal(CTE_POSICION_ID_SPARK);
		
		// Convertimos el código a Integer
		Integer intCodSpark = new Integer(bidCodSpark.intValue());		
		
		SparkManager sparkManager = ManagerFactory.getInstance().getSparkManager();		
			
		// Marcamos el spark como borrado
		SparkC sparkDeleted = null;
		try {
			sparkManager.deleteSpark(intCodSpark);
		
			// 	Recuperamos el spark		
			sparkDeleted = sparkManager.getSpark(intCodSpark, null);
		} catch (RollbackException rbe) {
			// TODO Auto-generated catch block
			ServicioNegocio.rollback(TipoError.FUNCIONAL, ConstantesError.ERROR_DELETE_SPARK_COMPRADO, "Error deleting Spark", "You can't delete an spark that has been bought", null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IContexto[] salida = ContextoSparks.rellenarContexto(sparkDeleted);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029032: Borrado lógico del spark con id" + intCodSpark +". Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
		
	}
	
}
