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
 * gratuitos o de pago en función del parámetro
 * 
 * @author adele
 * 
 */
public class SNListAllSparksByPrice extends ServicioNegocio {
	
	private static final int CTE_POSICION_ID_TIPO = 0;
	private static final int CTE_POSICION_ID_USER = 1;
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNListAllSparksByPrice.class);

	
	public SNListAllSparksByPrice(){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029022: Listar todos los sparks gratuitos o de pago.");
		Date datInicio = new Date();
		
		//BigDecimal bidIdTipo = datosEntrada.getDecimal(CTE_POSICION_ID_TIPO);
		
		BigDecimal bidCodUsuario = datosEntrada.getDecimal(CTE_POSICION_ID_USER);
		
		// Convertimos el código a Integer
		//Integer intIdTipo = new Integer(bidIdTipo.intValue());
		Integer intCodUsuario = null;
		
		if(bidCodUsuario != null){
			intCodUsuario = new Integer(bidCodUsuario.intValue());
		}
		
		// Buscamos los usuarios del MAA
		SparkManager manager = ManagerFactory.getInstance().getSparkManager();
		
		
		//List <SparkC> lstSparks = manager.getAllSparksByPrice(intIdTipo, intCodUsuario);
		
		List <SparkC> lstSparks = manager.getAllAvailableSparks(intCodUsuario);
		
		
		IContexto[] salida = ContextoSparks.rellenarContexto(lstSparks);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029022: Listar todos los sparks gratuitos o de pago. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
