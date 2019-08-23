package com.adelerobots.fioneg.service.usuarios;

import java.math.BigDecimal;

import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.adelerobots.fioneg.util.StringEncrypterUtilities;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNSetBillingAgreement extends ServicioNegocio{
	
	private static FawnaLogHelper logger = FawnaLogHelper.getLog(SNSetBillingAgreement.class);

	// Parametros de entrada
	private static final int CTE_POSICION_ID = 0;	
	private static final int CTE_POSICION_TOKEN = 1;
	
	public SNSetBillingAgreement(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		if (logger.isInfoEnabled()) {
			logger.info("Inicio Ejecucion del SN029064: Almacenar identificador de acuerdo con Paypal");
		}
		final long iniTime = System.currentTimeMillis();
		
		Integer userId = null; {
			final BigDecimal value = datosEntrada.getDecimal(CTE_POSICION_ID);
			if (value != null) userId = new Integer(value.intValue());
		}		
		
		final UsuariosManager manager = ManagerFactory.getInstance().getUsuariosManager();
		
		String strBillingId = datosEntrada.getString(CTE_POSICION_TOKEN);
		
		StringEncrypterUtilities encrypter = new StringEncrypterUtilities(Constantes.PASS_PHRASE); 
		 
		String encodedBillingId = encrypter.encrypt(strBillingId);
		
		UsuarioC usuario = manager.findById(userId);
		
		manager.updateUserBillingAgreement(usuario, encodedBillingId);
		
		IContexto[] salida = null;
		
		if (logger.isInfoEnabled()) {
			logger.info("Fin Ejecucion del SN029064: Almacenar identificador de acuerdo con Paypal. Tiempo total = " + Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		}
		
		return salida;
	}

}
