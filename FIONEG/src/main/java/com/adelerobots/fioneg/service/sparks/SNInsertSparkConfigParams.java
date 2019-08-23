package com.adelerobots.fioneg.service.sparks;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoConfigParam;
import com.adelerobots.fioneg.entity.ConfigParamC;
import com.adelerobots.fioneg.manager.ConfigParamManager;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.IRegistro;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNInsertSparkConfigParams extends ServicioNegocio {
	
	public static final String CTX_CONFIGPARAM_REG = "FIONEG020010";	
	
	private static final int CTE_POSICION_ID_SPARK = 0;
	private static final int CTE_POSICION_CTX_CONFIGPARAM = 1;	

	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNInsertSparkConfigParams.class);

	
	public SNInsertSparkConfigParams(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029053: Insertar los parámetros de configuración en el spark");
		Date datInicio = new Date();
		
		BigDecimal bidIdSpark = datosEntrada.getDecimal(CTE_POSICION_ID_SPARK);			
		Integer intCodSpark = null;
		if(bidIdSpark != null){
			intCodSpark = new Integer(bidIdSpark.intValue());
		}
		
		IContexto entrada = datosEntrada.getContexto(CTE_POSICION_CTX_CONFIGPARAM);
		List <ConfigParamC> lstListParams = new ArrayList<ConfigParamC>();
		
		IRegistro[] registros = entrada.getRegistro(CTX_CONFIGPARAM_REG);
		if (registros != null) {			
			ConfigParamManager cpmanager = ManagerFactory.getInstance().getConfigParamManager();
			lstListParams = cpmanager.createConfigParam(intCodSpark, registros);			
		}	
		
		IContexto[] salida = ContextoConfigParam.rellenarContexto(lstListParams);
		 
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029053: Insertar los parámetros de configuración en el spark. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;		
	}

}
