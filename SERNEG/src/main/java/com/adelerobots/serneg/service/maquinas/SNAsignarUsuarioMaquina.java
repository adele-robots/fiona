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
import com.adelerobots.serneg.manager.ProcesoMaquinaManager;
import com.adelerobots.serneg.util.EncryptionUtils;
import com.adelerobots.serneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNAsignarUsuarioMaquina extends ServicioNegocio {
	
	private static FawnaLogHelper logger = FawnaLogHelper.getLog(SNAsignarUsuarioMaquina.class);

	// Parámetros de entrada
	private static final int CTE_POSICION_EMAIL = 0;
	private static final int CTE_POSICION_ID_MAQUINA = 1;
	private static final int CTE_POSICION_AVATAR = 2;
	private static final int CTE_POSICION_NUM_USUARIOS = 3;
	private static final int CTE_POSICION_ID_UNIDAD_TIEMPO = 4;
	private static final int CTE_POSICION_RESOLUTION = 5;
	private static final int CTE_POSICION_HIGH_AVAILABILITY = 6;
	private static final int CTE_POSICION_PASS = 7;
	
	public SNAsignarUsuarioMaquina(){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		if (logger.isInfoEnabled()) {
			logger.info("Inicio Ejecucion del SN030007: Asignación de máquina para nuevo usuario");
		}
		final long iniTime = System.currentTimeMillis();
		
		IContexto[] salida = null;
		
		String strEmail = datosEntrada.getString(CTE_POSICION_EMAIL);
		
		String strIdMaquina = datosEntrada.getString(CTE_POSICION_ID_MAQUINA);
		Integer intCodMaquina = null;
		
		String strAvatar = datosEntrada.getString(CTE_POSICION_AVATAR); 
		
		String strNumUsuarios = datosEntrada.getString(CTE_POSICION_NUM_USUARIOS);
		Integer intNumUsuarios = null;
		
		String strIdUnidadTiempo = datosEntrada.getString(CTE_POSICION_ID_UNIDAD_TIEMPO);
		Integer intIdUnidadTiempo = null;
		
		String strIdResolution = datosEntrada.getString(CTE_POSICION_RESOLUTION);
		Integer intIdResolution = null;
		
		String strHighAvailability = datosEntrada.getString(CTE_POSICION_HIGH_AVAILABILITY);
		Integer intHighAvailability = null;	
		
		String strPass = datosEntrada.getString(CTE_POSICION_PASS);
				
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
		// TODO: descomentar cuando se haya probado
		String decryptedEmail = null;
		String decryptedIdMaquina = null;
		String decryptedAvatar = null;
		String decryptedNumUsuarios = null;
		String decryptedIdUnidadTiempo = null;
		String decryptedIdResolution = null;
		String decryptedHighAvailability = null;
		String decryptedPass = null;
		
		if(key != null){
			try {
				decryptedEmail = EncryptionUtils.decryptBC(strEmail, key);
				decryptedIdMaquina = EncryptionUtils.decryptBC(strIdMaquina, key);
				intCodMaquina = Integer.parseInt(decryptedIdMaquina);
				decryptedAvatar = EncryptionUtils.decryptBC(strAvatar, key);
				decryptedNumUsuarios = EncryptionUtils.decryptBC(strNumUsuarios, key);
				intNumUsuarios = Integer.parseInt(decryptedNumUsuarios);
				decryptedIdUnidadTiempo = EncryptionUtils.decryptBC(strIdUnidadTiempo, key);
				intIdUnidadTiempo = Integer.parseInt(decryptedIdUnidadTiempo);
				decryptedIdResolution = EncryptionUtils.decryptBC(strIdResolution, key);
				intIdResolution = Integer.parseInt(decryptedIdResolution);
				decryptedHighAvailability = EncryptionUtils.decryptBC(strHighAvailability, key);
				intHighAvailability = Integer.parseInt(decryptedHighAvailability);
				decryptedPass = EncryptionUtils.decryptBC(strPass, key);
				logger.info("DecryptedPass:"+decryptedPass);
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
		MaquinaC maquina = maquinaManager.getMaquina(intCodMaquina);
		
		ProcesoMaquinaManager procesoMaquinaManager = ManagerFactory.getInstance().getProcesoMaquinaManager(Constantes.CTE_JNDI_DATASOURCE);
		//ProcesoMaquinaManager procesoMaquinaManagerBU = ManagerFactory.getInstance().getProcesoMaquinaManager(Constantes.CTE_JNDI_DATASOURCE_BACKUP);
		procesoMaquinaManager.insertProcesoMaquina(maquina, decryptedEmail, decryptedAvatar,intNumUsuarios);
		//procesoMaquinaManagerBU.insertProcesoMaquina(maquina, decryptedEmail, decryptedAvatar);
		/** ELIMINACIÓN DE LA LLAMADA A CLINEG PARA INSERTAR USUARIO EN AIOXXXX (se hará vía ejecución de script)
		UsuariosManager usuariosManager = ManagerFactory.getInstance().getUsuariosManager(Constantes.CTE_JNDI_DATASOURCE);
		if(maquina != null){
			// Codificar parámetros con clave pública antes de llamar al SN de CLINEG
			PublicKey publicKey = null;
			try {
				publicKey = EncryptionUtils.loadPublicKey(Constantes.getKEYS_PATH(),"RSA");
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
			String encodedEmail = null;			
			//String autoGeneratedPass = FunctionUtils.createRandomToken(8);
			String encodedPass = null;
			String encodedNumUsuarios = null;
			String encodedIdUnidadTiempo = null;
			String encodedIdResolution = null;
			String encodedHighAvailability = null;
			String encodedDatasource = null;
			
			try {
				encodedEmail = EncryptionUtils.encryptBC(decryptedEmail, publicKey);		
				encodedPass = EncryptionUtils.encryptBC(decryptedPass, publicKey);
				encodedNumUsuarios = EncryptionUtils.encryptBC(intNumUsuarios.toString(), publicKey);
				encodedIdUnidadTiempo = EncryptionUtils.encryptBC(intIdUnidadTiempo.toString(), publicKey);
				encodedIdResolution = EncryptionUtils.encryptBC(intIdResolution.toString(), publicKey);
				encodedHighAvailability = EncryptionUtils.encryptBC(intHighAvailability.toString(), publicKey);
				encodedDatasource = EncryptionUtils.encryptBC(maquina.getStrJndi(), publicKey);
				
				UsuarioST usuario = usuariosManager.soapCallAltaUsuarioAio(null, null, encodedEmail, encodedPass, encodedNumUsuarios, encodedIdUnidadTiempo, encodedIdResolution, encodedHighAvailability, encodedDatasource,maquina.getStrIp());
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchProviderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		
		}*/
		
		salida = ContextoMaquina.rellenarContexto(maquina);
		
		if (logger.isInfoEnabled()) {
			logger.info("Fin Ejecucion del SN030007: Asignación de máquina para nuevo usuario. Tiempo total = " + Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		}
		
		return salida;		
	}

}
