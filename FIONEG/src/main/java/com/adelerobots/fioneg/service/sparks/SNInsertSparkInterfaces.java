package com.adelerobots.fioneg.service.sparks;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoInterfaz;
import com.adelerobots.fioneg.entity.InterfazC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.SparkManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.IRegistro;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNInsertSparkInterfaces extends ServicioNegocio {
	
	public static final String CTX_INTERFACES_REG = "FIONEG021010";	
	
	private static final int CTE_POSICION_ID_SPARK = 0;
	private static final int CTE_POSICION_CTX_INTERFACES = 1;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNInsertSparkInterfaces.class);
	
	public SNInsertSparkInterfaces(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029052: Insertar las interfaces al spark correspondiente");
		Date datInicio = new Date();
		
		BigDecimal bidIdSpark = datosEntrada.getDecimal(CTE_POSICION_ID_SPARK);			
		Integer intCodSpark = null;
		if(bidIdSpark != null){
			intCodSpark = new Integer(bidIdSpark.intValue());
		}
		
		IContexto entrada = datosEntrada.getContexto(CTE_POSICION_CTX_INTERFACES);
		
		List <InterfazC> lstSparkInterfaces = new ArrayList<InterfazC>();
		IRegistro[] registros = entrada.getRegistro(CTX_INTERFACES_REG);
		if (registros != null) {			
			SparkManager manager = ManagerFactory.getInstance().getSparkManager();
			lstSparkInterfaces = manager.addSparkInterfaces(intCodSpark, registros);
		}	
						
		IContexto[] salida = ContextoInterfaz.rellenarContexto(lstSparkInterfaces);		
		 
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029052: Insertar las interfaces al spark correspondiente. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;		
	}
}
