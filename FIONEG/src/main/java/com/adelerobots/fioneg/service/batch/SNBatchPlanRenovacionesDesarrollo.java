package com.adelerobots.fioneg.service.batch;

import java.util.Date;

import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;


/**
 * Servicio de negocio que se ejecutará mensualmente para comprobar
 * los usuarios a los que se les debe renovar los sparks de renovación
 * por tiempo
 * 
 * @author adele
 *
 */
public class SNBatchPlanRenovacionesDesarrollo extends ServicioNegocio {

	private static FawnaLogHelper LOGGER = FawnaLogHelper
			.getLog(SNBatchPlanRenovacionesDesarrollo.class);

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto,
			IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029073: Comprobar qué sparks de los usuarios deben ser renovados este mes.");
		Date datInicio = new Date();
		
		UsuariosManager usuariosManager = ManagerFactory.getInstance().getUsuariosManager();
		
		usuariosManager.planificarRenovacionesSparksDesarrollo();		

		IContexto[] salida = null;

		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER.info("Fin Ejecucion del SN 029073: Comprobar qué sparks de los usuarios deben ser renovados este mes. Tiempo total = "
				+ tiempoTotal + "ms");

		return salida;
	}

}
