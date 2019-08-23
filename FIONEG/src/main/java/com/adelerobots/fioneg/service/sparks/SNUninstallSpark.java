package com.adelerobots.fioneg.service.sparks;

import java.math.BigDecimal;
import java.util.Date;

import com.adelerobots.fioneg.context.ContextoSparks;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.SparkManager;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNUninstallSpark extends ServicioNegocio {
	
	private static final int CTE_POSICION_ID_USUARIO = 0;	
	private static final int CTE_POSICION_ID_SPARK = 1;	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNUninstallSpark.class);
	
	/**
	 * Constructor por defecto
	 */
	public SNUninstallSpark(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029058: Desinstalar el spark para el usuario.");
		Date datInicio = new Date();
		
		BigDecimal bidCodUsuario = datosEntrada
		.getDecimal(CTE_POSICION_ID_USUARIO);
		
		BigDecimal bidCodSpark = datosEntrada	
		.getDecimal(CTE_POSICION_ID_SPARK);
		
		
		// Convertimos el código a Integer
		Integer intCodUsuario = new Integer(bidCodUsuario.intValue());
		Integer intCodSpark = new Integer(bidCodSpark.intValue());
		
		// Desasignamos el spark al usuario
		UsuariosManager usuariosManager = ManagerFactory.getInstance().getUsuariosManager();
		
		usuariosManager.uninstallSpark(intCodUsuario, intCodSpark);
		
		SparkManager sparkManager = ManagerFactory.getInstance().getSparkManager();
		SparkC spark = null;
		try {
			spark = sparkManager.getSpark(intCodSpark, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				

		IContexto[] salida = ContextoSparks.rellenarContexto(spark);		
		
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029058: Desinstalar el spark para el usuario. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
