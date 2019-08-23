package com.adelerobots.serneg.service.usuarios;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.NoSuchPaddingException;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;

import org.jdom.JDOMException;

import com.adelerobots.serneg.context.ContextoMensaje;
import com.adelerobots.serneg.dataclasses.UsuarioST;
import com.adelerobots.serneg.manager.ManagerFactory;
import com.adelerobots.serneg.manager.ProcesoMaquinaManager;
import com.adelerobots.serneg.manager.UsuariosManager;
import com.adelerobots.serneg.util.EncryptionUtils;
import com.adelerobots.serneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNCancelarCuentaTCRF extends ServicioNegocio {
	
	private static FawnaLogHelper logger = FawnaLogHelper
	.getLog(SNCancelarCuentaTCRF.class);
	
	private static final int CTE_POSICION_TIRA_EMAIL = 0;

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		if (logger.isInfoEnabled()) {
			logger.info("Inicio Ejecucion del SN030010: Cancelar cuenta de usuario de TCRF en BBDD de ST");
		}
		final long iniTime = System.currentTimeMillis();
		
		String strEncodedEmail = datosEntrada.getString(CTE_POSICION_TIRA_EMAIL);
		
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
		if(key != null){
			try {
				decryptedUserEmail = EncryptionUtils.decryptBC(strEncodedEmail, key);
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
			usuario = usuariosManager.soapCallCancelarUsuarioST(strEncodedEmail);
			if(usuario!=null){
				// Todo ha ido bien así que borramos el registro del usuario en la BBDD
				// del Service Manager
				ProcesoMaquinaManager procesoMaquinaManager = ManagerFactory.getInstance().getProcesoMaquinaManager(Constantes.CTE_JNDI_DATASOURCE);
				procesoMaquinaManager.deleteProcesosMaquinaFromUser(decryptedUserEmail);
			}
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
		if(usuario != null)
			resultado = Constantes.RESULTADO_OK;
		
		IContexto[] salida = ContextoMensaje.rellenarContexto(resultado);
		
		if (logger.isInfoEnabled()) {
			logger.info("Fin Ejecucion del SN030010: Cancelar cuenta de usuario de TCRF en BBDD de ST. Tiempo total = "
					+ Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		}

		return salida;
	}

}
