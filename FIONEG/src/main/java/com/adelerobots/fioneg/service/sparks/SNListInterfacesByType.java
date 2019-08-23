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

public class SNListInterfacesByType extends ServicioNegocio {
	
	private static final int CTE_POSICION_ID_TYPE = 0;
	
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNListInterfacesByType.class);

	
	public SNListInterfacesByType(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029048: Listar interfaces por tipo");
		Date datInicio = new Date();
		
		final String interfaceType = datosEntrada.getString(CTE_POSICION_ID_TYPE);
		
		InterfazManager manager = ManagerFactory.getInstance().getInterfazManager();
		
		List <InterfazC> lstInterfacesByType;		
		lstInterfacesByType = manager.getInterfacesByType(interfaceType);			
		IContexto[] salida = ContextoInterfaz.rellenarContexto(lstInterfacesByType);		
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029048: Listar interfaces por tipo. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
