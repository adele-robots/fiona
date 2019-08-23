package com.adelerobots.serneg.service.maquinas;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.NoSuchPaddingException;

import com.adelerobots.serneg.context.ContextoMaquina;
import com.adelerobots.serneg.entity.MaquinaC;
import com.adelerobots.serneg.manager.ManagerFactory;
import com.adelerobots.serneg.manager.MaquinaManager;
import com.adelerobots.serneg.util.EncryptionUtils;
import com.adelerobots.serneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNGetMaquinaAvatarUsuario extends ServicioNegocio {
	
	private static FawnaLogHelper logger = FawnaLogHelper.getLog(SNGetMaquinaAvatarUsuario.class);

	// Parámetros de entrada
	private static final int CTE_POSICION_EMAIL = 0;
	private static final int CTE_POSICION_AVATAR = 1;
	
	public SNGetMaquinaAvatarUsuario(){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		if (logger.isInfoEnabled()) {
			logger.info("Inicio Ejecucion del SN030001: Identificación de máquina de avatar de usuario");
		}
		final long iniTime = System.currentTimeMillis();
		
		IContexto[] salida = null;
		
		String strEmail = datosEntrada.getString(CTE_POSICION_EMAIL);
		
		String strCodAvatar = datosEntrada.getString(CTE_POSICION_AVATAR);		
		
		MaquinaManager maquinaManager = ManagerFactory.getInstance().getMaquinaManager(Constantes.CTE_JNDI_DATASOURCE);
		
		PrivateKey key = null;
		try {
			key = EncryptionUtils.loadPrivateKey(Constantes.getKEYS_PATH(),"RSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String decryptedEmail = null;
		String decryptedAvatar = null;
		if(key != null){
			try {
				decryptedEmail = EncryptionUtils.decryptBC(strEmail, key);
				decryptedAvatar = EncryptionUtils.decryptBC(strCodAvatar, key);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchProviderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		MaquinaC maquina = maquinaManager.getMaquinaAvatarUsuario(decryptedEmail, decryptedAvatar);
		
		salida = ContextoMaquina.rellenarContexto(maquina);
		
		if (logger.isInfoEnabled()) {
			logger.info("Fin Ejecucion del SN030001: Identificación de máquina de avatar de usuario. Tiempo total = " + Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		}
		
		return salida;		
	}

}
