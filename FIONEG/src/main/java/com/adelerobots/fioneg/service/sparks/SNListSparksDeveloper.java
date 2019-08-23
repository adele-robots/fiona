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
 * Servicio de Negocio fachada para recuperar el listado de sparks
 * desarrollados por un usuario con perfil "desarrollador"
 * 
 * @author adele
 * 
 */
public class SNListSparksDeveloper extends ServicioNegocio{

	private static final int CTE_POSICION_ID_USER = 0;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNListSparksDeveloper.class);
	
	public SNListSparksDeveloper(){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029028: Devolver listado de sparks del desarrollador");
		Date datInicio = new Date();
		
		
		BigDecimal bidCodUsuario = datosEntrada.getDecimal(CTE_POSICION_ID_USER);
		
		// Convertimos el código a Integer
		Integer intCodUsuarioDes = new Integer(bidCodUsuario.intValue());		
		
		SparkManager sparkManager = ManagerFactory.getInstance().getSparkManager();		
			
		List <SparkC> lstSparks = sparkManager.getListSparksByDeveloper(intCodUsuarioDes);
		
		IContexto[] salida = ContextoSparks.rellenarContexto(lstSparks);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029028: Devolver listado de sparks del desarrollador con id" + intCodUsuarioDes +". Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
		
	}
	
}
