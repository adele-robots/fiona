package com.adelerobots.clineg.service.usuarios;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.NoSuchPaddingException;

import com.adelerobots.clineg.entity.HostingC;
import com.adelerobots.clineg.entity.RoleUsuarioC;
import com.adelerobots.clineg.entity.UsuarioC;
import com.adelerobots.clineg.manager.AvatarManager;
import com.adelerobots.clineg.manager.ManagerFactory;
import com.adelerobots.clineg.manager.UsuariosManager;
import com.adelerobots.clineg.util.EncryptionUtils;
import com.adelerobots.clineg.util.FunctionUtils;
import com.adelerobots.clineg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.RollbackException;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNAltaUsuarioAIO extends SNAbstractManageUsuarios {

	private static FawnaLogHelper logger = FawnaLogHelper
			.getLog(SNAltaUsuarioAIO.class);

	private static final int CTE_POSICION_TIRA_NAME = 0;
	private static final int CTE_POSICION_TIRA_SURNAME = 1;
	private static final int CTE_POSICION_TIRA_EMAIL = 2;// not null
	private static final int CTE_POSICION_TIRA_PASSWORD = 3;// not null	
	private static final int CTE_POSICION_NUM_USUARIOS = 4;
	private static final int CTE_POSICION_ID_UNIDAD_TIEMPO = 5;
	private static final int CTE_POSICION_RESOLUTION = 6;
	private static final int CTE_POSICION_HIGH_AVAILABILITY = 7;
	private static final int CTE_POSICION_DATASOURCE = 8;
	

	public SNAltaUsuarioAIO() {
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto,
			IDatosEntradaTx datosEntrada) {
		if (logger.isInfoEnabled()) {
			logger.info("Inicio Ejecucion del SN032001: Almacenar usuario en BBDD de AIOXXXX");
		}
		final long iniTime = System.currentTimeMillis();

		String strName = FunctionUtils.defaultIfEmpty(
				datosEntrada.getString(CTE_POSICION_TIRA_NAME), null);
		String strSurname = FunctionUtils.defaultIfEmpty(
				datosEntrada.getString(CTE_POSICION_TIRA_SURNAME), null);
		String strEmail = datosEntrada.getString(CTE_POSICION_TIRA_EMAIL);
		
		String passwd =   FunctionUtils.defaultIfEmpty(
				datosEntrada.getString(CTE_POSICION_TIRA_PASSWORD), null);
		String encPasswd = null;
		String strNumUsuarios = datosEntrada.getString(CTE_POSICION_NUM_USUARIOS);
		String strIdUnidadTiempo = datosEntrada.getString(CTE_POSICION_ID_UNIDAD_TIEMPO);
		String strIdResolution = datosEntrada.getString(CTE_POSICION_RESOLUTION);
		String strHighAvailability = datosEntrada.getString(CTE_POSICION_HIGH_AVAILABILITY);
		String strDatasource = datosEntrada.getString(CTE_POSICION_DATASOURCE);
		
				
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
		String decryptedPasswd = null;
		String decryptedNumUsuarios = null;
		Integer intNumUsuarios = null;
		String decryptedIdUnidadTiempo = null;
		Integer intIdUnidadTiempo = null;
		String decryptedIdResolution = null;
		Integer intIdResolution = null;
		String decryptedHighAvailability = null;
		Integer intHighAvailability = null;
		String decryptedDatasource = null;			
		
		
		if(key != null){
			try {
				if(strName != null && !"".equals(strName))
					decryptedName = EncryptionUtils.decryptBC(strName, key);
				if(strSurname != null && !"".equals(strSurname))
					decryptedSurname = EncryptionUtils.decryptBC(strSurname, key);
				decryptedEmail = EncryptionUtils.decryptBC(strEmail, key);
				decryptedPasswd = EncryptionUtils.decryptBC(passwd, key);
				encPasswd = FunctionUtils.encodePassword(decryptedPasswd); // Codificar la password
				decryptedNumUsuarios = EncryptionUtils.decryptBC(strNumUsuarios, key);
				intNumUsuarios = Integer.parseInt(decryptedNumUsuarios);
				decryptedIdUnidadTiempo = EncryptionUtils.decryptBC(strIdUnidadTiempo, key);
				intIdUnidadTiempo = Integer.parseInt(decryptedIdUnidadTiempo);
				decryptedIdResolution = EncryptionUtils.decryptBC(strIdResolution, key);
				intIdResolution = Integer.parseInt(decryptedIdResolution);
				decryptedHighAvailability = EncryptionUtils.decryptBC(strHighAvailability, key);
				intHighAvailability = Integer.parseInt(decryptedHighAvailability);
				decryptedDatasource = EncryptionUtils.decryptBC(strDatasource, key);
								
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
		
						

		Character pagoAnual = new Character('1');
		
		// Rol usuario
		RoleUsuarioC role = checkRole(new Integer(2), decryptedDatasource); // Resolve RoleUsuarioC
														// entity (if available)


		IContexto[] salida = null;
		try {
			// Comprobamos la existencia del email contra la BBDD de SparkingTogether
			final UsuariosManager userManager = ManagerFactory.getInstance()
			.getUsuariosManager(decryptedDatasource);
			UsuarioC usuario = userManager.findByEmail(decryptedEmail);
			
			// Si el usuario existe sólo insertamos un nuevo avatar en la tabla,
			// pero no al usuario
			if(usuario == null){				
				
				// Comprobar si el usuario que se está registrando ya existía en
				// nuestra BBDD			
				role.setId(new Integer(2));// Rol USUARIO
				String username = decryptedEmail.substring(0, decryptedEmail.indexOf('@'));
				usuario = userManager.create(decryptedName, decryptedSurname, decryptedEmail,
						encPasswd, null, null, null, null, null, username,
						role, null, "0", pagoAnual);
				
			}
			
			// Crear avatar asociado al usuario
			AvatarManager avatarManager = ManagerFactory.getInstance().getAvatarManager(decryptedDatasource);			
			
			HostingC hosting = avatarManager.getPrecioHosting(intIdUnidadTiempo, intNumUsuarios, intIdResolution.toString(), (intHighAvailability.intValue()==1)?'1':'0');
			avatarManager.createNewAvatar(usuario, intIdUnidadTiempo, hosting);
			
			salida = null;
		} catch (Exception e) {
			if (e instanceof RollbackException) {
				// ServicioNegocio.rollback(...) throw this exception, rethrow!!
				throw (RollbackException) e;
			}
			// se ha producido un error de ejecucion
			ServicioNegocio.rollback(TipoError.TECNICO, new Integer(0),
					e.getMessage(), e.getMessage(), null);
		}

		if (logger.isInfoEnabled()) {
			logger.info("Fin Ejecucion del SN032001: Almacenar usuario en BBDD de AIOXXXX. Tiempo total = "
					+ Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		}

		return salida;
	}
	
	
}
