package com.adelerobots.fioneg.service.sparks;

import java.math.BigDecimal;
import java.util.Date;

import com.adelerobots.fioneg.context.ContextoTipo;
import com.adelerobots.fioneg.entity.TipoC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.TipoManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNDeleteConfigParamType extends ServicioNegocio {
	
	private static final int CTE_POSICION_ID_TYPE = 0;


	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNDeleteConfigParamType.class);

	
	public SNDeleteConfigParamType(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029056: Eliminar un tipo de parámetro de configuración de la base de datos");
		Date datInicio = new Date();
		
		BigDecimal bidType = datosEntrada.getDecimal(CTE_POSICION_ID_TYPE);
		Integer idType = null;
		if(bidType != null){
			idType = new Integer(bidType.intValue());
		}
		
		TipoManager tipoManager = ManagerFactory.getInstance().getTipoManager();
		TipoC tipo = new TipoC();
		tipo = tipoManager.deleteTipo(idType);		
		
		IContexto[] salida = ContextoTipo.rellenarContexto(tipo);
		 
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029056: Eliminar un tipo de parámetro de configuración de la base de datos. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;		
	}

}
