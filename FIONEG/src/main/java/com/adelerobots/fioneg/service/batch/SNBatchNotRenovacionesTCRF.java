package com.adelerobots.fioneg.service.batch;

import java.util.Date;

import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNBatchNotRenovacionesTCRF extends ServicioNegocio {
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNBatchNotRenovacionesTCRF.class);

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029084: Notificar a los usuarios de TCRF sobre los sparks que deben renovarse.");
		Date datInicio = new Date();
		
		UsuariosManager usuariosManager = ManagerFactory.getInstance().getUsuariosManager();
		
		usuariosManager.notificarRenovacionesSparksTCRF(contexto);		

		IContexto[] salida = null;

		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER.info("Fin Ejecucion del SN 029084: Notificar a los usuarios de TCRF sobre los sparks que deben renovarse. Tiempo total = "
				+ tiempoTotal + "ms");

		return salida;
	}

}
