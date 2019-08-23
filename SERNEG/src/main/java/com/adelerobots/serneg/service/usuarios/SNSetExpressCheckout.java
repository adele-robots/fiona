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

public class SNSetExpressCheckout extends ServicioNegocio {

	private static FawnaLogHelper logger = FawnaLogHelper
			.getLog(SNSetExpressCheckout.class);

	// Parametros de entrada
	private static final int CTE_POSICION_USER_EMAIL = 0;
	private static final int CTE_POSICION_RETURN_URL = 1;
	private static final int CTE_POSICION_CANCEL_URL = 2;

	public SNSetExpressCheckout() {
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto,
			IDatosEntradaTx datosEntrada) {
		// TODO Auto-generated method stub
		if (logger.isInfoEnabled()) {
			logger.info("Inicio Ejecucion del SN030005: Llamar a SetExpressCheckout de Paypal para obtener un token");
		}
		final long iniTime = System.currentTimeMillis();

		String userMail = datosEntrada.getString(CTE_POSICION_USER_EMAIL);

		String returnURL = datosEntrada.getString(CTE_POSICION_RETURN_URL);

		String cancelURL = datosEntrada.getString(CTE_POSICION_CANCEL_URL);
		
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
		String decryptedReturnURL = null;
		String decryptedCancelURL = null;
		
		if(key != null){
			try {
				if(userMail != null && !"".equals(userMail)){
					decryptedEmail = EncryptionUtils.decryptBC(userMail,key);
				}
				decryptedReturnURL = EncryptionUtils.decryptBC(returnURL, key);
				decryptedCancelURL = EncryptionUtils.decryptBC(cancelURL, key);
				
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
		
		String encodedUserMail = null;
		String encodedReturnURL = null;
		String encodedCancelURL = null;
		
		if (pKey != null) {
			try {
				
				if(userMail != null && !"".equals(userMail))
					encodedUserMail = EncryptionUtils.encryptBC(decryptedEmail, pKey);
				encodedReturnURL = EncryptionUtils.encryptBC(decryptedReturnURL, pKey);
				encodedCancelURL = EncryptionUtils.encryptBC(decryptedCancelURL, pKey);
				
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
		
		String token = null;
		
		UsuariosManager usuariosManager = ManagerFactory.getInstance().getUsuariosManager(Constantes.CTE_JNDI_DATASOURCE);
		
		try {
			token = usuariosManager.soapCallSetExpressCheckout(encodedUserMail, encodedReturnURL, encodedCancelURL);
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

		IContexto[] salida = ContextoMensaje.rellenarContexto(token);

		if (logger.isInfoEnabled()) {
			logger.info("Fin Ejecucion del SN030005: Llamar a SetExpressCheckout de Paypal para obtener un token. Tiempo total = "
					+ Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		}

		return salida;
	}

}
