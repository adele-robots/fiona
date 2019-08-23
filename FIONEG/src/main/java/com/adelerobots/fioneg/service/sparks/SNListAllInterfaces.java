package com.adelerobots.fioneg.service.sparks;

import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoInterfaz;
import com.adelerobots.fioneg.entity.InterfazC;
import com.adelerobots.fioneg.manager.InterfazManager;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNListAllInterfaces extends ServicioNegocio {
	
	private static final int CTE_POSICION_LOCALE = 0;
	
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNListAllInterfaces.class);

	
	public SNListAllInterfaces(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029046: Listar todas las interfaces posibles");
		Date datInicio = new Date();
				
		InterfazManager manager = ManagerFactory.getInstance().getInterfazManager();
		
		List <InterfazC> lstAllInterfaces;		
		lstAllInterfaces = manager.getAllInterfaces();			
		IContexto[] salida = ContextoInterfaz.rellenarContexto(lstAllInterfaces);		
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029046: Listar todas las interfaces posibles. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
