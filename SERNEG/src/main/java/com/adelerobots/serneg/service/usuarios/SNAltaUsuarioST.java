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
import com.adelerobots.serneg.dataclasses.UsuarioST;
import com.adelerobots.serneg.manager.ManagerFactory;
import com.adelerobots.serneg.manager.UsuariosManager;
import com.adelerobots.serneg.util.EncryptionUtils;
import com.adelerobots.serneg.util.FunctionUtils;
import com.adelerobots.serneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNAltaUsuarioST extends ServicioNegocio {

	private static FawnaLogHelper logger = FawnaLogHelper
			.getLog(SNAltaUsuarioST.class);

	private static final int CTE_POSICION_TIRA_NAME = 0;
	private static final int CTE_POSICION_TIRA_SURNAME = 1;
	private static final int CTE_POSICION_TIRA_EMAIL = 2;// not null
	private static final int CTE_POSICION_TIRA_PASSWORD = 3;// not null
	private static final int CTE_POSICION_TIRA_ACCOUNTTYPE = 4;
	private static final int CTE_POSICION_TIRA_WEBSITE = 5;
	private static final int CTE_POSICION_TIRA_BILLINGID = 6;
	private static final int CTE_POSICION_TIRA_PERIODO = 7; //not null

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto,
			IDatosEntradaTx datosEntrada) {
		if (logger.isInfoEnabled()) {
			logger.info("Inicio Ejecucion del SN030003: Almacenar usuario en BBDD de ST");
		}
		final long iniTime = System.currentTimeMillis();

		String strName = FunctionUtils.defaultIfEmpty(
				datosEntrada.getString(CTE_POSICION_TIRA_NAME), null);
		String strSurname = FunctionUtils.defaultIfEmpty(
				datosEntrada.getString(CTE_POSICION_TIRA_SURNAME), null);
		String strEmail = FunctionUtils.defaultIfEmpty(
				datosEntrada.getString(CTE_POSICION_TIRA_EMAIL), null);
		String strPassword = datosEntrada.getString(CTE_POSICION_TIRA_PASSWORD);
		String strAccountType = datosEntrada
		.getString(CTE_POSICION_TIRA_ACCOUNTTYPE);
		
		
		String strWebsite = FunctionUtils.defaultIfEmpty(
				datosEntrada.getString(CTE_POSICION_TIRA_WEBSITE), null);
		
		
		String strBillingID = datosEntrada.getString(CTE_POSICION_TIRA_BILLINGID);
		
		String strPeriodo = datosEntrada.getString(CTE_POSICION_TIRA_PERIODO);
		
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
		
		String decryptedName = null;
		String decryptedSurname = null;
		String decryptedEmail = null;
		String decryptedPassword = null;
		String decryptedAccountType = null;
		String decryptedWebsite = null;
		String decryptedBillingID = null;
		String decryptedPeriodo = null;
		
		if(key != null){
			try {
				
				if(strName != null && !"".equals(strName)){
					decryptedName = EncryptionUtils.decryptBC(strName, key);
				}
				if(strSurname != null && !"".equals(strSurname)){
					decryptedSurname = EncryptionUtils.decryptBC(strSurname, key);
				}
				decryptedEmail = EncryptionUtils.decryptBC(strEmail, key);
				decryptedPassword = EncryptionUtils.decryptBC(strPassword, key);
				if(strAccountType != null && !"".equals(strAccountType)){
					decryptedAccountType = EncryptionUtils.decryptBC(strAccountType, key);
				}
				if(strWebsite != null && !"".equals(strWebsite)){
					decryptedWebsite = EncryptionUtils.decryptBC(strWebsite, key);
				}
				if(strBillingID != null && !"".equals(strBillingID)){
					decryptedBillingID = EncryptionUtils.decryptBC(strBillingID, key);
				}				
				
				decryptedPeriodo = EncryptionUtils.decryptBC(strPeriodo, key);
				
				
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
		
		

		PublicKey pkey = null;
		try {
			pkey = EncryptionUtils.loadPublicKey(Constantes.getKEYS_PATH(),
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

		String encodedName = null;
		String encodedSurname = null;
		String encodedEmail = null;
		String encodedPassword = null;
		String encodedAccountType = null;
		String encodedWebsite = null;
		String encodedBillingID = null;
		String encodedPeriodo = null;

		if (key != null) {
			try {
				if (strName != null && !"".equals(strName))

					encodedName = EncryptionUtils.encryptBC(decryptedName, pkey);

				if (strSurname != null && !"".equals(strSurname))
					encodedSurname = EncryptionUtils.encryptBC(decryptedSurname, pkey);
				encodedEmail = EncryptionUtils.encryptBC(decryptedEmail, pkey);
				encodedPassword = EncryptionUtils.encryptBC(decryptedPassword, pkey);
				if(strAccountType != null && !"".equals(strAccountType))
					encodedAccountType = EncryptionUtils.encryptBC(decryptedAccountType, pkey);
				if(strWebsite != null && !"".equals(strWebsite)){
					encodedWebsite = EncryptionUtils.encryptBC(decryptedWebsite, pkey);
				}
				if(strBillingID != null && !"".equals(strBillingID)){
					encodedBillingID = EncryptionUtils.encryptBC(decryptedBillingID, pkey);
				}
				encodedPeriodo =  EncryptionUtils.encryptBC(decryptedPeriodo, pkey);
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

		UsuariosManager usuariosManager = ManagerFactory.getInstance()
				.getUsuariosManager(Constantes.CTE_JNDI_DATASOURCE);

		UsuarioST usuario = null;
		try {
			usuario = usuariosManager.soapCallAltaUsuarioST(encodedName,
					encodedSurname, encodedEmail, encodedPassword, encodedAccountType,
					encodedWebsite,encodedBillingID,encodedPeriodo);
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

		
		//IContexto[] salida = ContextoUsuarioST.rellenarContexto(usuario);
		String resultado = Constantes.RESULTADO_ERROR;
		if(usuario != null)
			resultado = Constantes.RESULTADO_OK;
		
		IContexto[] salida = ContextoMensaje.rellenarContexto(resultado);
		

		if (logger.isInfoEnabled()) {
			logger.info("Fin Ejecucion del SN030003: Almacenar usuario en BBDD de ST. Tiempo total = "
					+ Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		}

		return salida;
	}

}
