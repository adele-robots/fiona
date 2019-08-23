package com.adelerobots.fioneg.service.usuarios;

import java.math.BigDecimal;
import java.util.List;

import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.entity.usuarioconfig.UsuarioConfigC;
import com.adelerobots.fioneg.entity.usuarioconfig.UsuarioConfigPk;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuarioConfigManager;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNCheckUserBBDD extends ServicioNegocio{
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper.getLog(SNCheckUserBBDD.class);

	private static final int CTE_POSICION_TIRA_USER_ID = 0;
	
	public SNCheckUserBBDD(){
		super();
	}
	
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contextoEjecucion, IDatosEntradaTx datosEntrada) {
		
		LOGGER.info("Inicio Ejecucion del SN 029069: Comprobar consistencia en BBDD de usuario");

		final BigDecimal userId = datosEntrada.getDecimal(CTE_POSICION_TIRA_USER_ID);
		final Integer intUserId = userId.intValue();

		final UsuariosManager managerusuario = ManagerFactory.getInstance().getUsuariosManager();
		final UsuarioConfigManager managerusuarioConfig = ManagerFactory.getInstance().getUsuarioConfigManager();

		UsuarioC userObj = null; 
		userObj = managerusuario.findById(userId);
		if (userObj==null)
			return new IContexto[0];

		// Comprueba que el numero de entradas en usuarioconfig es cocherente con su tipo de cuenta
		final Integer accounttypeId = userObj.getCuentaId();
		final List<UsuarioConfigC> list = managerusuarioConfig.getAllUsuarioConfigs(intUserId);
		final int configs = Constantes.getConfigNum(accounttypeId);
		
		if (list.size() == configs)
			return new IContexto[0];
		
		if(list.size() > configs) {
			for(int i = configs; i < list.size(); i++) {
				managerusuarioConfig.getById(intUserId, i).delete();
			}
			return new IContexto[0];
		}
		
		//if(list.size() < configs)
			for(Integer i = list.size(); i<configs; i++) {
				UsuarioConfigPk userConfigPk = new UsuarioConfigPk();
				userConfigPk.setUsuario(userObj);
				userConfigPk.setConfig(i);
				managerusuarioConfig.create(userConfigPk, "Config "+Integer.toString(i+1));
			}
		
		
		return new IContexto[0];
	}
	
	
}



