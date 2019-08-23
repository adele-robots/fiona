package com.adelerobots.fioneg.service.batch;

import java.util.Date;

import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNBatchCobRenovacionesProduccion extends ServicioNegocio {
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNBatchCobRenovacionesProduccion.class);

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029079: Cobrar a los usuarios por los avatares en producción que deben renovarse este mes.");
		Date datInicio = new Date();
		
		UsuariosManager usuariosManager = ManagerFactory.getInstance().getUsuariosManager();
		
		usuariosManager.cobrarRenovacionesSparksProduccion(contexto);		

		IContexto[] salida = null;

		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER.info("Fin Ejecucion del SN 029079: Cobrar a los usuarios por los avatares en producción que deben renovarse este mes. Tiempo total = "
				+ tiempoTotal + "ms");

		return salida;
	}

}
