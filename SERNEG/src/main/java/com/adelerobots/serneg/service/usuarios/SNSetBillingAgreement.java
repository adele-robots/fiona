package com.adelerobots.serneg.service.usuarios;

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
import com.adelerobots.serneg.manager.UsuariosManager;
import com.adelerobots.serneg.util.EncryptionUtils;
import com.adelerobots.serneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNSetBillingAgreement extends ServicioNegocio {

	private static FawnaLogHelper logger = FawnaLogHelper
			.getLog(SNSetBillingAgreement.class);

	// Parametros de entrada
	private static final int CTE_POSICION_EMAIL = 0;
	private static final int CTE_POSICION_TOKEN = 1;

	public SNSetBillingAgreement() {
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto,
			IDatosEntradaTx datosEntrada) {
		if (logger.isInfoEnabled()) {
			logger.info("Inicio Ejecucion del SN030006: Almacenar identificador de acuerdo con Paypal");
		}
		final long iniTime = System.currentTimeMillis();

		String strUserMail = datosEntrada.getString(CTE_POSICION_EMAIL);

		final UsuariosManager manager = ManagerFactory.getInstance()
				.getUsuariosManager(Constantes.CTE_JNDI_DATASOURCE);

		String strToken = datosEntrada.getString(CTE_POSICION_TOKEN);
		
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
		
		String decryptedUserMail = null;
		String decryptedToken = null;
		
		if(key != null){
			try {
				
				decryptedUserMail = EncryptionUtils.decryptBC(strUserMail, key);
				decryptedToken = EncryptionUtils.decryptBC(strToken, key);			
				
			}catch (NoSuchAlgorithmException e) {
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
		
		String encodedUserMail = null;
		String encodedToken = null;
		
		if (key != null) {
			try {
				
				encodedUserMail = EncryptionUtils.encryptBC(decryptedUserMail, pKey);
				encodedToken = EncryptionUtils.encryptBC(decryptedToken, pKey);
				
			}catch (NoSuchAlgorithmException e) {
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

		UsuariosManager usuarioManager = ManagerFactory.getInstance()
				.getUsuariosManager(Constantes.CTE_JNDI_DATASOURCE);

		
		
		IContexto[] salida = null;
		
		String resultado = "error";
		try {
			resultado = usuarioManager.soapCallSetBillingAgreement(encodedUserMail, encodedToken);
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

		salida = ContextoMensaje.rellenarContexto(resultado);

		if (logger.isInfoEnabled()) {
			logger.info("Fin Ejecucion del SN030006: Almacenar identificador de acuerdo con Paypal. Tiempo total = "
					+ Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		}

		return salida;
	}
}
