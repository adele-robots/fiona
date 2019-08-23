package com.adelerobots.fioneg.service.sparks;

import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoUtilization;
import com.adelerobots.fioneg.entity.UtilizationC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UtilizationManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;


/**
 * Servicio de Negocio fachada para recuperar una lista de unidades
 * de tiempo (cobro por tarifa plana)
 * 
 * @author adele
 * 
 */
public class SNGetUseUnits extends ServicioNegocio {
	
	private static final int CTE_POSICION_LOCALE = 0;
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNGetUseUnits.class);
	
	public SNGetUseUnits(){
		super();
	}
	

	@Override
	public IContexto[] ejecutar(IContextoEjecucion arg0, IDatosEntradaTx arg1) {
		LOGGER.info("Inicio Ejecucion del SN 029041: Listar las unidades para el cobro por unidades de uso de sparks.");
		Date datInicio = new Date();
		
		UtilizationManager useManager = ManagerFactory.getInstance().getUtilizationManager();
		
//		// ids de las unidades de tiempo
//		List<Integer> ids = new ArrayList<Integer>();
//		ids.add(4);
//		ids.add(5);		
		
		List <UtilizationC> lstTimeUnits = useManager.getAllUtilization();
		
		
		IContexto[] salida = ContextoUtilization.rellenarContexto(lstTimeUnits);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029041:  Listar las unidades para el cobro por unidades de uso de sparks. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
