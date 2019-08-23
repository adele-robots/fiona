package com.adelerobots.fioneg.service.sparks;





import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoPropiedadValor;
import com.adelerobots.fioneg.entity.status.StatusC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.StatusManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;



/**
 * Servicio de Negocio fachada para recuperar una lista de estados
 * de sparks
 * 
 * @author adele
 * 
 */
public class SNGetStatusList extends ServicioNegocio {
	
	private static final int CTE_POSICION_LOCALE = 0;
	
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNGetStatusList.class);

	
	public SNGetStatusList(){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029027: Listar todos los status posibles");
		Date datInicio = new Date();
		
				
		StatusManager manager = ManagerFactory.getInstance().getStatusManager();
		
		List <StatusC> lstAllStatus;
		
		
		lstAllStatus = manager.getAllStatus();	
		
		
		IContexto[] salida = ContextoPropiedadValor.rellenarContexto(lstAllStatus);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029027: Listar todos los status posibles. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
