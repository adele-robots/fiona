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



/**
 * Servicio de Negocio fachada para recuperar una lista de sparks 
 * 
 * @author adele
 * 
 */
public class SNListAllSparks extends ServicioNegocio {
	
	private static final int CTE_POSICION_ID_USER = 0;
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNListAllSparks.class);

	
	public SNListAllSparks(){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029015: Listar todos los sparks");
		Date datInicio = new Date();
		
		BigDecimal bidCodUsuario = datosEntrada.getDecimal(CTE_POSICION_ID_USER);
		
		// Buscamos los usuarios del MAA
		SparkManager manager = ManagerFactory.getInstance().getSparkManager();
		
		List <SparkC> lstAllSparks;
		Integer intCodUsuario = null;
		if(bidCodUsuario != null){
			intCodUsuario = new Integer(bidCodUsuario.intValue());
		}
		
		
		lstAllSparks = manager.getAllSparks(intCodUsuario);	
		
		
		IContexto[] salida = ContextoSparks.rellenarContexto(lstAllSparks);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029015: Listar todos los sparks. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
