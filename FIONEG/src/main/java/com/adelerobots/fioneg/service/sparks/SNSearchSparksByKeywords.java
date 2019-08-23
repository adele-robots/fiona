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
 * cuyas palabras clave coincidan con las pasadas como parámetro
 * 
 * @author adele
 * 
 */
public class SNSearchSparksByKeywords extends ServicioNegocio {
	
	private static final int CTE_POSICION_KEYWORDS = 0;
	private static final int CTE_POSICION_ID_USER = 1;
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNSearchSparksByKeywords.class);

	
	public SNSearchSparksByKeywords(){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029025: Buscar sparks por palabras clave.");
		Date datInicio = new Date();
		
		String palabrasClave = datosEntrada.getString(CTE_POSICION_KEYWORDS);
		BigDecimal bidCodUsuario = datosEntrada.getDecimal(CTE_POSICION_ID_USER);
		Integer intCodUsuario = null;
		
		if(bidCodUsuario != null){
			intCodUsuario = new Integer(bidCodUsuario.intValue());
		}
		
		if(palabrasClave != null)
			palabrasClave = palabrasClave.replace(',', ' ');
				
		SparkManager manager = ManagerFactory.getInstance().getSparkManager();
		
		
		List <SparkC> lstSparks = manager.findSparksByKeywords(palabrasClave, intCodUsuario);		
		
		
		IContexto[] salida = ContextoSparks.rellenarContexto(lstSparks);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029025: Buscar sparks por palabras clave. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
