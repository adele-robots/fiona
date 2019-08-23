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

public class SNGetMaquinaLibre extends ServicioNegocio {
	
	private static FawnaLogHelper logger = FawnaLogHelper.getLog(SNGetMaquinaLibre.class);

	// Parámetros de entrada
	private static final int CTE_POSICION_EMAIL = 0;
	private static final int CTE_POSICION_NUM_USUARIOS = 1;
	private static final int CTE_POSICION_ID_UNIDAD_TIEMPO = 2;
	private static final int CTE_POSICION_RESOLUTION = 3;
	private static final int CTE_POSICION_HIGH_AVAILABILITY = 4;
	
	public SNGetMaquinaLibre(){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		if (logger.isInfoEnabled()) {
			logger.info("Inicio Ejecucion del SN030002: Selección de máquina libre para usuario");
		}
		final long iniTime = System.currentTimeMillis();
		
		IContexto[] salida = null;
		
		String strEmail = datosEntrada.getString(CTE_POSICION_EMAIL);
		
		String strNumUsuarios = datosEntrada.getString(CTE_POSICION_NUM_USUARIOS);
		Integer intNumUsuarios = null;
		
		String strIdUnidadTiempo = datosEntrada.getString(CTE_POSICION_ID_UNIDAD_TIEMPO);
		Integer intIdUnidadTiempo = null;
		
		String strIdResolution = datosEntrada.getString(CTE_POSICION_RESOLUTION);
		Integer intIdResolution = null;
		
		String strHighAvailability = datosEntrada.getString(CTE_POSICION_HIGH_AVAILABILITY);
		Integer intHighAvailability = null;	
				
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
		String decryptedNumUsuarios = null;
		String decryptedIdUnidadTiempo = null;
		String decryptedIdResolution = null;
		String decryptedHighAvailability = null;
		
		if(key != null){
			try {
				decryptedEmail = EncryptionUtils.decryptBC(strEmail, key);
				decryptedNumUsuarios = EncryptionUtils.decryptBC(strNumUsuarios, key);
				intNumUsuarios = Integer.parseInt(decryptedNumUsuarios);
				decryptedIdUnidadTiempo = EncryptionUtils.decryptBC(strIdUnidadTiempo, key);
				intIdUnidadTiempo = Integer.parseInt(decryptedIdUnidadTiempo);
				decryptedIdResolution = EncryptionUtils.decryptBC(strIdResolution, key);
				intIdResolution = Integer.parseInt(decryptedIdResolution);
				decryptedHighAvailability = EncryptionUtils.decryptBC(strHighAvailability, key);
				intHighAvailability = Integer.parseInt(decryptedHighAvailability);
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
		
		MaquinaManager maquinaManager = ManagerFactory.getInstance().getMaquinaManager(Constantes.CTE_JNDI_DATASOURCE);
		
		// TODO: Cuando se decida qué mecanismo utilizar para poder repartir un mismo avatar
		// de un usuario en varias máquinas, probablemente haya que aumentar el número 
		// de parámetros involucrados en la selección
		MaquinaC maquina = maquinaManager.getMaquinaDisponible(intNumUsuarios);
		
				
		salida = ContextoMaquina.rellenarContexto(maquina);
		
		if (logger.isInfoEnabled()) {
			logger.info("Fin Ejecucion del SN030002: Selección de máquina libre para usuario. Tiempo total = " + Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		}
		
		return salida;		
	}

}
