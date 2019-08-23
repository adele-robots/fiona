package com.adelerobots.fioneg.service.usuarios;

import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoCuenta;
import com.adelerobots.fioneg.entity.CuentaC;
import com.adelerobots.fioneg.manager.CuentaManager;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNListAllCuentas extends ServicioNegocio {
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNListAllCuentas.class);

	
	public SNListAllCuentas(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029060: Listar todas las cuentas");
		Date datInicio = new Date();
				
		CuentaManager manager = ManagerFactory.getInstance().getCuentaManager();
		
		List <CuentaC> lstAllCuentas;		
		lstAllCuentas = manager.getAllCuentas();		
		IContexto[] salida = ContextoCuenta.rellenarContexto(lstAllCuentas);		
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029060: Listar todas las cuentas. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
