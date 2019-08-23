package com.adelerobots.fioneg.service.usuarios;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoUsuarioConfig;
import com.adelerobots.fioneg.entity.usuarioconfig.UsuarioConfigC;
import com.adelerobots.fioneg.manager.UsuarioConfigManager;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNGetUserConfigs extends ServicioNegocio {

	private static final int CTE_POSICION_TIRA_ID = 0;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNGetUserConfigs.class);

	
	public SNGetUserConfigs(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029066: Listar las configuraciones de un usuario");
		Date datInicio = new Date();
				
		UsuarioConfigManager manager = ManagerFactory.getInstance().getUsuarioConfigManager();

		Integer userId = null;
		final BigDecimal value = datosEntrada.getDecimal(CTE_POSICION_TIRA_ID);
		if (value != null)
			userId = new Integer(value.intValue());
		
		List <UsuarioConfigC> lstUserConfigs;		
		lstUserConfigs = manager.getAllUsuarioConfigs(userId);		
		IContexto[] salida = ContextoUsuarioConfig.rellenarContexto(lstUserConfigs);		
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029066: Listar las configuraciones de un usuario. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
