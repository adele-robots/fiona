package com.adelerobots.fioneg.service.sparks;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoTipo;
import com.adelerobots.fioneg.entity.TipoC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.TipoManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNListAllParamTypesByUser extends ServicioNegocio {
	
	private static final int CTE_POSICION_ID_USER = 0;
	
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNListAllParamTypesByUser.class);

	
	public SNListAllParamTypesByUser(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029049: Listar todas los tipos de parámetros de configuración disponibles del usuario");
		Date datInicio = new Date();

		BigDecimal bidCodUsuario = datosEntrada.getDecimal(CTE_POSICION_ID_USER);
		Integer intCodUsuario = null;
		if(bidCodUsuario != null){
			intCodUsuario = new Integer(bidCodUsuario.intValue());
		}
		
		TipoManager manager = ManagerFactory.getInstance().getTipoManager();
						
		List <TipoC> lstAllTypesByUser;
		lstAllTypesByUser = manager.getAllTypesByUser(intCodUsuario);				
		IContexto[] salida = ContextoTipo.rellenarContexto(lstAllTypesByUser);		
		 
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029049: Listar todas los tipos de parámetros de configuración disponibles del usuario. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
