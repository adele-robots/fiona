package com.adelerobots.fioneg.service.security;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.adelerobots.fioneg.context.ContextoMensaje;
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

public class PollingHandler extends ServicioNegocio {
	
	
	private static FawnaLogHelper logger = FawnaLogHelper.getLog(PollingHandler.class);
	private static final int CTE_POSICION_USER= 0;
	private static final int CTE_POSICION_PID= 1;
			
			@Override
	public IContexto[] ejecutar(IContextoEjecucion arg0, IDatosEntradaTx datosEntrada) {
		// TODO Auto-generated method stub
		logger.info("Inicio ejecucion del SN 029012: Polling Handler");
		
		IContexto[] salida = null;
		
		final BigDecimal decUser = datosEntrada.getDecimal(CTE_POSICION_USER);
		Integer user = null;
		if(decUser != null){
			user = new Integer(decUser.intValue());
		}		
		
		final BigDecimal decPid = datosEntrada.getDecimal(CTE_POSICION_PID);
		Integer pid = null;
		if(decPid != null){
			pid = new Integer(decPid.intValue());
		}		
		
		if (pid==99991){return new IContexto[0];
		}
	
		String host = null;
		
		try {
			InetAddress addr = InetAddress.getLocalHost();
			// Get hostname
			host = addr.getHostName();
		} catch (UnknownHostException e) {
		}
		
		// Tiempo actual para marcar o actualizar el del proceso
		long timestamp = System.currentTimeMillis();
		
		final ProcesoManager manager = ManagerFactory.getInstance().getProcesoManager();
		final UsuariosManager usuariosManager = ManagerFactory.getInstance().getUsuariosManager();
		UsuarioC usuario = usuariosManager.findById(user);
		
		ProcesoC proceso = new ProcesoC(); 
		proceso = manager.checkExecutionInfo(user, pid, host);
		
		if(proceso!=null){
			Character killedByARPIA = proceso.getChKilledByARPIA();
			if(killedByARPIA!=null){
				if(killedByARPIA.equals('1')){
					// Si es ARPIA quien para la ejecución del avatar, se marca el proceso como "arpia"
					// para que desde el listener del polling se complete el cierre del avatar
					salida = ContextoMensaje.rellenarContexto("arpia");
					return salida;
				}
				else{
					// Si la base de datos contiene ya una entrada para ese mismo user y pid
					proceso.setNuTimestamp(timestamp);
					manager.updateExecutionInfo(proceso);
				}
			}		
		}else{
			proceso = new ProcesoC();
			// Obtengo el tipo de cuenta del usuario
			Integer userAccountType = usuario.getCuentaId();
			if(userAccountType.equals(Constantes.CTE_ACCOUNT_FREE) || userAccountType.equals(Constantes.CTE_ACCOUNT_BASIC)){
				// Si el usuario es 'free' o 'basic' tendrá un tiempo máximo de ejecución del avatar
				String timeAlive = Constantes.getTimeAlive();				
				proceso.setNuTimealive(Integer.parseInt(timeAlive));
				
				if(userAccountType.equals(Constantes.CTE_ACCOUNT_FREE)){
					String frozenTime = Constantes.getFrozenTime();
					proceso.setNuTimeAllowedExecution(timestamp+Integer.parseInt(timeAlive)*1000+Integer.parseInt(frozenTime)*1000);
				}else{
					proceso.setNuTimeAllowedExecution(0); 
				}
			}else{
				// Si el usuario es 'pro' o 'corporate' no tendrá tiempo máximo de ejecución.
				// Se marca como 0 para que ARPIA sepa que no tiene que matar a estos procesos
				proceso.setNuTimealive(0);
			}				
				proceso.setCnUser(user);
				proceso.setNuPid(pid);
				proceso.setNuTimestamp(timestamp);
				proceso.setDcHost(host);
				proceso.setNuTimestart(timestamp);				
				proceso.setChKilledByARPIA('0');
				
				manager.addExecutionInfo(proceso);			
		}
				
		logger.info("Fin ejecucion del SN 029012: Polling Handler");
		salida = ContextoMensaje.rellenarContexto("poll");
		return salida;
		
	}
}
