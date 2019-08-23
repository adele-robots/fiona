package com.adelerobots.fioneg.service.usuarios;

import java.math.BigDecimal;
import java.util.Date;

import com.adelerobots.fioneg.context.ContextoUsuarioConfig;
import com.adelerobots.fioneg.entity.usuarioconfig.UsuarioConfigC;
import com.adelerobots.fioneg.manager.UsuarioConfigManager;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNEditarUsuarioConfig extends ServicioNegocio {

	private static final int CTE_POSICION_TIRA_USUARIOID = 0;
	private static final int CTE_POSICION_TIRA_CONFIGID = 1;
	private static final int CTE_POSICION_TIRA_NOMBRE = 2;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNEditarUsuarioConfig.class);

	
	public SNEditarUsuarioConfig(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029067: Editar una configuracion de un usuario");
		Date datInicio = new Date();
				
		UsuarioConfigManager manager = ManagerFactory.getInstance().getUsuarioConfigManager();

		Integer idUsuario = null;
		final BigDecimal userid = datosEntrada.getDecimal(CTE_POSICION_TIRA_USUARIOID);
		if (userid != null)
			idUsuario = new Integer(userid.intValue());
		
		Integer idConfig = null;
		final BigDecimal configid = datosEntrada.getDecimal(CTE_POSICION_TIRA_CONFIGID);
		if (configid != null)
			idConfig = new Integer(configid.intValue());
		
		final String name = datosEntrada.getString(CTE_POSICION_TIRA_NOMBRE);
				
		UsuarioConfigC usuarioconfig = manager.update(idUsuario, idConfig, name);
		IContexto[] salida = ContextoUsuarioConfig.rellenarContexto(usuarioconfig);		
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029067: Editar una configuracion de un usuario. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
