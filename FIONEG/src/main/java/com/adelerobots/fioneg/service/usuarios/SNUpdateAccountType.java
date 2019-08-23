package com.adelerobots.fioneg.service.usuarios;

import java.math.BigDecimal;

import com.adelerobots.fioneg.context.ContextoUsuarios;
import com.adelerobots.fioneg.entity.CuentaC;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNUpdateAccountType extends SNAbstractManageUsuarios {
	
	private static FawnaLogHelper logger = FawnaLogHelper.getLog(SNUpdateAccountType.class);

	// Parametros de entrada
	private static final int CTE_POSICION_ID = 0;	
	private static final int CTE_POSICION_ACCOUNTTYPE_ID = 1;
	private static final int CTE_POSICION_RESETEAR_CREDITO = 2;
	
	public SNUpdateAccountType(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		if (logger.isInfoEnabled()) {
			logger.info("Inicio Ejecucion del SN029065: Actualizar el tipo de cuenta del usuario.");
		}
		final long iniTime = System.currentTimeMillis();
		
		IContexto[] salida = null;
		
		BigDecimal bidCodUsuario = datosEntrada.getDecimal(CTE_POSICION_ID);
		
		Integer intCodUsuario = new Integer(bidCodUsuario.intValue()); 
		
		BigDecimal bidCodTipoCuenta = datosEntrada.getDecimal(CTE_POSICION_ACCOUNTTYPE_ID);
		
		Integer accountTypeId = new Integer(bidCodTipoCuenta.intValue());
		
		BigDecimal bidFlagResetearCredito = datosEntrada.getDecimal(CTE_POSICION_RESETEAR_CREDITO);
		
		Integer intFlagResetearCredito = new Integer(0);
		
		if(bidFlagResetearCredito != null){
			intFlagResetearCredito = new Integer(bidFlagResetearCredito.intValue());
		}
		
		CuentaC cuenta = checkAccountType(accountTypeId); //Resolve CuentaC entity (if available)
		
		final UsuariosManager manager = ManagerFactory.getInstance().getUsuariosManager();
		
		UsuarioC usuario = manager.findById(intCodUsuario);
		
		manager.updateUserAccountTypeId(usuario, cuenta, intFlagResetearCredito);		
		
		salida = ContextoUsuarios.rellenarContexto(usuario);
		
		if (logger.isInfoEnabled()) {
			logger.info("Fin Ejecucion del SN029065: Actualizar el tipo de cuenta del usuario. Tiempo total = " + Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		}
		
		return salida;
	}

}
