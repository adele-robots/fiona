package com.adelerobots.fioneg.service.sparks;





import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoSparks;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;



/**
 * Servicio de Negocio fachada para recuperar los sparks comprados por un usuario
 * 
 * @author adele
 * 
 */
public class SNGetBuyLog extends ServicioNegocio {
	
	private static final int CTE_POSICION_ID_USUARIO = 0;
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNGetBuyLog.class);

	
	public SNGetBuyLog(){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029026: Devolver listado de sparks adquiridos por un usuario.");
		Date datInicio = new Date();
		
		BigDecimal bidCodUsuario = datosEntrada
		.getDecimal(CTE_POSICION_ID_USUARIO);
		
		// Convertimos el código a Integer
		Integer intCodUsuario = new Integer(bidCodUsuario.intValue());
		
		// Buscamos el spark
		UsuariosManager manager = ManagerFactory.getInstance().getUsuariosManager();		
		
		List<SparkC> lstSparks = manager.getAllUserSparks(intCodUsuario);			
		
		IContexto[] salida = ContextoSparks.rellenarContexto(lstSparks);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029026: Devolver listado de sparks adquiridos por un usuario. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
