package com.adelerobots.fioneg.service.sparks;

import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoKeyword;
import com.adelerobots.fioneg.entity.keyword.KeywordC;
import com.adelerobots.fioneg.manager.KeywordsManager;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNGetAllKeywords extends ServicioNegocio {
	
	private static final int CTE_POSICION_LOCALE = 0;
	
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNGetAllKeywords.class);

	
	public SNGetAllKeywords(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029034: Listar todos los keywords posibles");
		Date datInicio = new Date();
		
				
		// Buscamos los usuarios del MAA
		KeywordsManager manager = ManagerFactory.getInstance().getKeywordsManager();
		
		List <KeywordC> lstAllKeywords;
		
		
		lstAllKeywords = manager.getAllKeywords();	
		
		
		IContexto[] salida = ContextoKeyword.rellenarContexto(lstAllKeywords);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029034: Listar todos los keywords posibles. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
