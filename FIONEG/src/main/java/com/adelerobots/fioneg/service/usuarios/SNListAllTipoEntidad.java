package com.adelerobots.fioneg.service.usuarios;

import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoTipoEntidad;
import com.adelerobots.fioneg.entity.TipoEntidadC;
import com.adelerobots.fioneg.manager.TipoEntidadManager;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNListAllTipoEntidad extends ServicioNegocio {
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNListAllTipoEntidad.class);

	
	public SNListAllTipoEntidad(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029061: Listar todos los tipos de entidad");
		Date datInicio = new Date();
				
		TipoEntidadManager manager = ManagerFactory.getInstance().getTipoEntidadManager();
		
		List <TipoEntidadC> lstAllTipoEntidad;		
		lstAllTipoEntidad = manager.getAllTipoEntidad();		
		IContexto[] salida = ContextoTipoEntidad.rellenarContexto(lstAllTipoEntidad);		
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029061: Listar todos los tipos de entidad. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
