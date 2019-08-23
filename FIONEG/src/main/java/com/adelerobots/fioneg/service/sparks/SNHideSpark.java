package com.adelerobots.fioneg.service.sparks;

import java.math.BigDecimal;
import java.util.Date;

import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

/**
 * Servicio de Negocio fachada para ocultar un spark
 * del editor para un usuario dado
 * 
 * @author adele
 * 
 */
public class SNHideSpark extends ServicioNegocio {
	
	private static final int CTE_POSICION_ID_USUARIO = 0;
	private static final int CTE_POSICION_ID_SPARK = 1;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNHideSpark.class);
	
	/**
	 * Constructor por defecto
	 */
	public SNHideSpark(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029018: Ocultar un spark en el editor para un usuario.");
		Date datInicio = new Date();
		
		BigDecimal bidCodUsuario = datosEntrada
		.getDecimal(CTE_POSICION_ID_USUARIO);
		
		BigDecimal bidCodSpark = datosEntrada
		.getDecimal(CTE_POSICION_ID_SPARK);
		
		// Convertimos el código a Integer
		Integer intCodUsuario = new Integer(bidCodUsuario.intValue());
		Integer intCodSpark = new Integer(bidCodSpark.intValue());
		
				
		// Ocultamos el spark para el usuario
		UsuariosManager usuariosManager = ManagerFactory.getInstance().getUsuariosManager();
		
		usuariosManager.hideSparkToUser(intCodUsuario, intCodSpark);
		
		IContexto[] salida = null;
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029018: Ocultar un spark en el editor para un usuario. Tiempo total = "
						+ tiempoTotal + "ms");
		
		
		return salida;
	}

}
