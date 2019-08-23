package com.adelerobots.fioneg.service.usuarios;

import java.math.BigDecimal;
import java.util.Date;

import com.adelerobots.fioneg.context.ContextoCuenta;
import com.adelerobots.fioneg.entity.CuentaC;
import com.adelerobots.fioneg.manager.CuentaManager;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;


/**
 * Servicio de negocio que devuelve información de un tipo de cuenta
 * 
 * @author adele
 *
 */
public class SNGetAccountTypeData extends ServicioNegocio{
	
	private static final int CTE_POSICION_ACCOUNTTYPE_ID = 0;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNGetAccountTypeData.class);

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029081: Obtener los datos del tipo de cuenta.");
		Date datInicio = new Date();
		
		BigDecimal bidCodCuenta = datosEntrada.getDecimal(CTE_POSICION_ACCOUNTTYPE_ID);
		
		Integer intCodCuenta = new Integer(bidCodCuenta.intValue());
		
		CuentaManager cuentaManager = ManagerFactory.getInstance().getCuentaManager();
		
		CuentaC cuenta = cuentaManager.getById(intCodCuenta);
		
		IContexto[] salida = ContextoCuenta.rellenarContexto(cuenta);
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029081: Ontener los datos del tipo de cuenta: "+intCodCuenta+". Tiempo total = "
						+ tiempoTotal + "ms");
		return salida;
	}
	
	
	
	

}
