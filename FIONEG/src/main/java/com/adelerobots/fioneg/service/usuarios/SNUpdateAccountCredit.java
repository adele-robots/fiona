package com.adelerobots.fioneg.service.usuarios;

import java.math.BigDecimal;

import com.adelerobots.fioneg.context.ContextoUsuarios;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNUpdateAccountCredit extends SNAbstractManageUsuarios {
	
	private static FawnaLogHelper logger = FawnaLogHelper.getLog(SNUpdateAccountCredit.class);

	// Parametros de entrada
	private static final int CTE_POSICION_ID = 0;	
	private static final int CTE_POSICION_ACCOUNTCREDIT = 1;
	
	public SNUpdateAccountCredit(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		if (logger.isInfoEnabled()) {
			logger.info("Inicio Ejecucion del SN029071: Actualizar el crédito del usuario.");
		}
		final long iniTime = System.currentTimeMillis();
		
		IContexto[] salida = null;
		
		BigDecimal bidCodUsuario = datosEntrada.getDecimal(CTE_POSICION_ID);
		
		Integer intCodUsuario = new Integer(bidCodUsuario.intValue()); 
		
		BigDecimal bidAccountCredit = datosEntrada.getDecimal(CTE_POSICION_ACCOUNTCREDIT);		
		
		Float floAccountCredit = null;
		if(bidAccountCredit != null)
			floAccountCredit = new Float(bidAccountCredit.floatValue());
						
		final UsuariosManager manager = ManagerFactory.getInstance().getUsuariosManager();
		
		UsuarioC usuario = manager.findById(intCodUsuario);
		
		manager.updateUserAccountCredit(usuario, floAccountCredit);
		
		salida = ContextoUsuarios.rellenarContexto(usuario);
		
		if (logger.isInfoEnabled()) {
			logger.info("Fin Ejecucion del SN029071: Actualizar el crédito del usuario. Tiempo total = " + Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		}
		
		return salida;
	}

}
