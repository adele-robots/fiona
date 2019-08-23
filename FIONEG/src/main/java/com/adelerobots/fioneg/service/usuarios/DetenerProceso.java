package com.adelerobots.fioneg.service.usuarios;

import java.io.IOException;
import java.net.InetAddress;

import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.ProcesoManager;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class DetenerProceso extends ServicioNegocio{
	//OJO debug windows 
	//private static final String KILL = "taskkill /F /PID ";
		
	private static FawnaLogHelper LOGGER = FawnaLogHelper.getLog(DetenerProceso.class);
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion arg0, IDatosEntradaTx datosEntrada) {
		 
		LOGGER.error("[DetenerProceso] Empezando servicio");
		LOGGER.info("[DetenerProceso] Empezando servicio");

		String servicePid = datosEntrada.getString(0);

		LOGGER.error("[DetenerProceso] PID recibido a detener: " +servicePid);
		LOGGER.info("[DetenerProceso] PID recibido a detener: " +servicePid);
					
		if(!servicePid.equals("0000")){
			try{
				Runtime.getRuntime().exec(new String[]{Constantes.getKILL_COMMAND(), servicePid});
				
				final ProcesoManager manager = ManagerFactory.getInstance()	.getProcesoManager();
				
			    InetAddress addr = InetAddress.getLocalHost();
			    String host = addr.getHostName();
			    
			    Integer pid = Integer.parseInt(servicePid);
			    manager.removeExecutionInfo(pid, host);
			
				
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
		
		return new IContexto[0];
	}

}
