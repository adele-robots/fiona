package com.adelerobots.fioneg.service.batch;

import java.util.Date;

import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNBatchCobRenovacionesDesarrollo extends ServicioNegocio {
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNBatchCobRenovacionesDesarrollo.class);

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029075: Cobrar a los usuarios por los sparks que deben renovarse este mes.");
		Date datInicio = new Date();
		
		UsuariosManager usuariosManager = ManagerFactory.getInstance().getUsuariosManager();
		
		usuariosManager.cobrarRenovacionesSparksDesarrollo(contexto);		

		IContexto[] salida = null;

		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER.info("Fin Ejecucion del SN 029075: Cobrar a los usuarios por los sparks que deben renovarse este mes. Tiempo total = "
				+ tiempoTotal + "ms");

		return salida;
	}

}
