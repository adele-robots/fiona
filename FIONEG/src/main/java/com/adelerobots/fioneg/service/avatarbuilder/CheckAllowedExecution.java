package com.adelerobots.fioneg.service.avatarbuilder;

import java.math.BigDecimal;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoPid;
import com.adelerobots.fioneg.entity.ProcesoC;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.ProcesoManager;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class CheckAllowedExecution extends ServicioNegocio {
	
	
	private static FawnaLogHelper logger = FawnaLogHelper.getLog(CheckAllowedExecution.class);
	private static final int CTE_POSICION_USER= 0;
				
			@Override
	public IContexto[] ejecutar(IContextoEjecucion arg0, IDatosEntradaTx datosEntrada) {
		logger.info("Inicio ejecucion del SN 029057: CheckAllowedExecution");
		
		IContexto[] salida = null;
		
		final BigDecimal decUser = datosEntrada.getDecimal(CTE_POSICION_USER);
		Integer user = null;
		if(decUser != null){
			user = new Integer(decUser.intValue());
		}						
		
		// Se obtiene el tiempo actual
		long timeNow = System.currentTimeMillis();
		// 1 true, -1 false
		Integer isAllowedExecution = 1;
		// -1 significa que no habrá tiempo de espera
		Integer timeToWait = -1;
		
		final ProcesoManager manager = ManagerFactory.getInstance().getProcesoManager();
		final UsuariosManager usuariosManager = ManagerFactory.getInstance().getUsuariosManager();
		UsuarioC usuario = usuariosManager.findById(user);
		
		if(usuario.getCuentaId().equals(Constantes.CTE_ACCOUNT_FREE)){
			List <ProcesoC> lstProcessByUser;		
			lstProcessByUser = manager.getProcessByUser(user);
			// TODO: Comprobar bien procesos/usuario !
			if(!lstProcessByUser.isEmpty()){
				long TimeAllowedExecution = lstProcessByUser.get(0).getNuTimeAllowedExecution();
				if(timeNow < TimeAllowedExecution){
					// No se permitirá la ejecución del avatar si todavía no pasó el tiempo 
					// que debe esperar el usuario free (-1 False, 1 True)
					isAllowedExecution = -1;
					timeToWait = (int) (TimeAllowedExecution - timeNow);
					salida = ContextoPid.rellenarContexto(isAllowedExecution, timeToWait);
					
					return salida;
				}
			}
		}
		
		// Se permite la ejecución del avatar
		salida = ContextoPid.rellenarContexto(isAllowedExecution, timeToWait);
		logger.info("Fin ejecucion del SN 029057: CheckAllowedExecution");
		return salida;	
	}
}
