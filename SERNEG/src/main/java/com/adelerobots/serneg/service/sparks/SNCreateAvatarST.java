package com.adelerobots.serneg.service.sparks;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.NoSuchPaddingException;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;

import org.jdom.JDOMException;

import com.adelerobots.serneg.context.ContextoMensaje;
import com.adelerobots.serneg.manager.ManagerFactory;
import com.adelerobots.serneg.manager.SparkManager;
import com.adelerobots.serneg.util.EncryptionUtils;
import com.adelerobots.serneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNCreateAvatarST extends ServicioNegocio {
	
	private static FawnaLogHelper logger = FawnaLogHelper
	.getLog(SNCreateAvatarST.class);
	
	private static final int CTE_POSICION_TIRA_USER_EMAIL = 0;
	private static final int CTE_POSICION_NUM_USUARIOS = 1;
	private static final int CTE_POSICION_ID_UNIDAD_TIEMPO = 2;
	private static final int CTE_POSICION_RESOLUTION = 3;
	private static final int CTE_POSICION_HIGH_AVAILABILITY = 4;

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		if (logger.isInfoEnabled()) {
			logger.info("Inicio Ejecucion del SN030004: Almacenar avatar en BBDD de ST");
		}
		final long iniTime = System.currentTimeMillis();
		
		String strUserEmail = datosEntrada.getString(CTE_POSICION_TIRA_USER_EMAIL);
		
		String strNumUsuarios = datosEntrada.getString(CTE_POSICION_NUM_USUARIOS);
		
		
		String strIdUnidadTiempo = datosEntrada.getString(CTE_POSICION_ID_UNIDAD_TIEMPO);
		if(strIdUnidadTiempo == null || "".equals(strIdUnidadTiempo))
			strIdUnidadTiempo = "2";// Anual
		
		
		String strIdResolution = datosEntrada.getString(CTE_POSICION_RESOLUTION);
		
		
		String strHighAvailability = datosEntrada.getString(CTE_POSICION_HIGH_AVAILABILITY);
		
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
		String decryptedUserEmail = null;
		String decryptedNumUsuarios = null;
		String decryptedIdUnidadTiempo = null;
		String decryptedIdResolution = null;
		String decryptedHighAvailability = null;
		
		if(key != null){
			try {
				decryptedUserEmail = EncryptionUtils.decryptBC(strUserEmail, key);
				decryptedNumUsuarios = EncryptionUtils.decryptBC(strNumUsuarios, key);				
				decryptedIdUnidadTiempo = EncryptionUtils.decryptBC(strIdUnidadTiempo, key);				
				decryptedIdResolution = EncryptionUtils.decryptBC(strIdResolution, key);				
				decryptedHighAvailability = EncryptionUtils.decryptBC(strHighAvailability, key);				
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
		
		
		PublicKey pKey = null;
		try {
			pKey = EncryptionUtils.loadPublicKey(Constantes.getKEYS_PATH(),
					"RSA");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidKeySpecException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String encodedEmail = null;
		String encodedNumUsuarios = null;
		String encodedIdUnidadTime = null;
		String encodedIdResolution = null;
		String encodedHighAvailability = null;
		
		if (pKey != null) {
			try {
				
				encodedEmail = EncryptionUtils.encryptBC(decryptedUserEmail, pKey);
				encodedNumUsuarios = EncryptionUtils.encryptBC(decryptedNumUsuarios, pKey);
				encodedIdUnidadTime = EncryptionUtils.encryptBC(decryptedIdUnidadTiempo, pKey);
				encodedIdResolution = EncryptionUtils.encryptBC(decryptedIdResolution, pKey);
				encodedHighAvailability = EncryptionUtils.encryptBC(decryptedHighAvailability, pKey);
				
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
		
		SparkManager sparkManager = ManagerFactory.getInstance().getSparkManager(Constantes.CTE_JNDI_DATASOURCE);
		boolean error = false;
		try {
			error = sparkManager.soapCallCreateAvatarST(encodedEmail, encodedNumUsuarios, encodedIdUnidadTime, encodedIdResolution, encodedHighAvailability);
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
		String resultado = Constantes.RESULTADO_ERROR;
		if(!error)
			resultado = Constantes.RESULTADO_OK;
		
		IContexto[] salida = ContextoMensaje.rellenarContexto(resultado);
		
		if (logger.isInfoEnabled()) {
			logger.info("Fin Ejecucion del SN030004: Almacenar avatar en BBDD de ST. Tiempo total = "
					+ Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		}
		
		return salida;
	}

}
