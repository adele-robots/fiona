package com.adelerobots.fioneg.service.sparks;

import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoUnit;
import com.adelerobots.fioneg.entity.UnitC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UnitManager;
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
public class SNGetTimeUnits extends ServicioNegocio {
	
	private static final int CTE_POSICION_LOCALE = 0;
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNGetTimeUnits.class);
	
	public SNGetTimeUnits(){
		super();
	}
	

	@Override
	public IContexto[] ejecutar(IContextoEjecucion arg0, IDatosEntradaTx arg1) {
		LOGGER.info("Inicio Ejecucion del SN 029040: Listar las unidades para el cobro tarifa plana de tiempo.");
		Date datInicio = new Date();
		
		UnitManager unitManager = ManagerFactory.getInstance().getUnitManager();
		
		// ids de las unidades de tiempo
//		List<Integer> ids = new ArrayList<Integer>();
//		ids.add(1);
//		ids.add(2);
//		ids.add(3);
		
		// List <UnitC> lstTimeUnits = unitManager.getAllUnits();
		List <UnitC> lstTimeUnits = unitManager.getVisibleUnits();		
		
		LOGGER.info("Se han recuperado " +lstTimeUnits.size() + " units de la BD");
		
		IContexto[] salida = ContextoUnit.rellenarContexto(lstTimeUnits);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029040: Listar las unidades para el cobro tarifa plana de tiempo. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
