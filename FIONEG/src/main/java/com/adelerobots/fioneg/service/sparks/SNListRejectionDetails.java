package com.adelerobots.fioneg.service.sparks;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoRejection;
import com.adelerobots.fioneg.entity.rejection.RejectionC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.SparkManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;


/**
 * Servicio de Negocio fachada para recuperar el listado de motivos
 * por los que ha sido rechazado un spark
 * 
 * @author adele
 * 
 */
public class SNListRejectionDetails extends ServicioNegocio{

	private static final int CTE_POSICION_ID_SPARK = 0;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNListRejectionDetails.class);
	
	public SNListRejectionDetails(){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029030: Devolver listado de motivos de rechazo para un spark");
		Date datInicio = new Date();
		
		
		BigDecimal bidCodSpark = datosEntrada.getDecimal(CTE_POSICION_ID_SPARK);
		
		// Convertimos el código a Integer
		Integer intCodSpark = new Integer(bidCodSpark.intValue());		
		
		SparkManager sparkManager = ManagerFactory.getInstance().getSparkManager();		
			
		List <RejectionC> lstRejections = sparkManager.getRejectionList(intCodSpark);
		
		IContexto[] salida = ContextoRejection.rellenarContexto(lstRejections);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029030: Devolver listado de motivos de rechazo para el spark con id" + intCodSpark +". Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
		
	}
	
}
