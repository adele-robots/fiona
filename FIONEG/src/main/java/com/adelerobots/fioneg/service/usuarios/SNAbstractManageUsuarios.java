package com.adelerobots.fioneg.service.usuarios;


import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import com.adelerobots.fioneg.entity.CuentaC;
import com.adelerobots.fioneg.entity.EntidadC;
import com.adelerobots.fioneg.entity.RoleUsuarioC;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.manager.CuentaManager;
import com.adelerobots.fioneg.manager.EntidadManager;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.RoleUsuarioManager;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.adelerobots.fioneg.util.FunctionUtils;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.RollbackException;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public abstract class SNAbstractManageUsuarios 
	extends ServicioNegocio 
{
	
	protected final FawnaLogHelper logger = FawnaLogHelper.getLog(getClass());
	
	public SNAbstractManageUsuarios() {
		super();
	}


	protected void checkEmail(
			final Integer userId, 
			final String email) 
		throws RollbackException
	{
		//Validate email format
		if (!FunctionUtils.isEmail(email)) {
			ServicioNegocio.rollback(TipoError.FUNCIONAL, 1100, 
					"E-mail seems have an invalid format", "E-mail seems have an invalid format", null);
			return;
		}
		
		//Find duplicates
		final UsuariosManager manager = ManagerFactory.getInstance().getUsuariosManager();
		if(manager.isEmailTaken(userId, email)) {
			ServicioNegocio.rollback(TipoError.FUNCIONAL, 1101, 
					"E-mail already registered, please try another one", "E-mail already registered, please try another one", null);
			return;
		}
	}

	protected void checkUsername(
			final Integer userId, 
			final String username) 
		throws RollbackException
	{
		//Validate username format
		validateNicknameFormat(username);
		
		//Find duplicates
		UsuariosManager manager = ManagerFactory.getInstance().getUsuariosManager();
		if (manager.isUsernameTaken(userId, username)) {
			ServicioNegocio.rollback(TipoError.FUNCIONAL, 1102, 
					"Username taken, please try another one", "Username taken, please try another one", null);
			return;
		}
	}

	/**
	 * Validate the format for a nickname/username field
	 * @param candidate string to validate
	 * @throws RollbackException if not valid
	 */
	protected void validateNicknameFormat(String candidate) 
		throws RollbackException 
	{
		if (candidate == null) return;
		//Al menos no tiene que tener una "@" para que no colisione internamenete al buscar usuarios por mail o username
		String illegalChars = "@\"#$%&?¿¡!";
		for ( int i = 0; i < candidate.length(); i++ ) {
			if (Character.isWhitespace(candidate.charAt(i))) {
				ServicioNegocio.rollback(TipoError.FUNCIONAL, 1103, 
						"Nicknames can not contain whitespace characters.", "Nicknames can not contain whitespace characters.", null);
			}
			if (illegalChars.contains(""+candidate.charAt(i))) {
				ServicioNegocio.rollback(TipoError.FUNCIONAL, new Integer(0), 
						"Nicknames can not contain certain characters: "+illegalChars, 
						"Nicknames can not contain certain characters: "+illegalChars, null);
			}
		}
		
		//se admite cualquier otro caracter además de los listados pero se pide que tenga al menos una letra y un número
		final String LEGAL_ALPH = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final String LEGAL_NUM = "_0123456789";
		boolean alphCheck = false;
		boolean numCheck = false;
		for ( int i = 0; i < candidate.length(); i++ ) {
			if (LEGAL_ALPH.indexOf(candidate.charAt(i))>0) alphCheck=true;
			if (LEGAL_NUM.indexOf(candidate.charAt(i))>0) numCheck=true;
			if (alphCheck || numCheck) return;
		}
		ServicioNegocio.rollback(TipoError.FUNCIONAL, new Integer(0), 
				"Nicknames must contain at least, letters ("+LEGAL_ALPH+") or numbers ("+LEGAL_NUM+").", 
				"Nicknames must contain at least, letters ("+LEGAL_ALPH+") or numbers ("+LEGAL_NUM+").", null);
	}

	/**
	 * Validate the format for a creditcard number field
	 * @param candidate string to validate
	 * @throws RollbackException if not valid
	 */
	protected void checkCreditCardNumberFormat(String candidate) 
		throws RollbackException 
	{
		if (candidate == null) return;
		//Validate creditcard number format
		if (!FunctionUtils.isCreditCard(candidate)) {
			ServicioNegocio.rollback(TipoError.FUNCIONAL, 1104, 
					"Creditcard number seems have an invalid format.", "Creditcard number seems have an invalid format.", null);
			return;
		}
	}

	/**
	 * Validate the format for a creditcard expiration date field
	 * @param candidate string to validate
	 * @throws RollbackException if not valid
	 */
	protected void checkCreditCardExpirationFormat(String candidate) 
		throws RollbackException 
	{
		if (candidate == null) return;
		
		//Pattern = MM/YYYY
		Pattern pattern = Pattern.compile("^([0-1][0-9])\\/([0-9]{4})$");
		if (!pattern.matcher(candidate).matches()) {
			ServicioNegocio.rollback(TipoError.FUNCIONAL, 1105, 
					"Invalid format for creditcard expiration date. Try MM/YYYY.", 
					"Invalid format for creditcard expiration date. Try MM/YYYY.", null);
			return;
		}
	}

	protected EntidadC checkEntity(Integer id) 
		throws RollbackException 
	{
		final EntidadManager manager = ManagerFactory.getInstance().getEntidadManager();
		EntidadC ent = null; {
			if (id != null) {
				ent = manager.getById(id);
				if (ent == null) {
					ServicioNegocio.rollback(TipoError.FUNCIONAL, 1106, 
							"Unknown user entity", "Unknown user entity", null);
				}
			}
		}
		return ent;
	}

	protected CuentaC checkAccountType(Integer id) 
		throws RollbackException 
	{
		final CuentaManager manager = ManagerFactory.getInstance().getCuentaManager();
		CuentaC ent = null; { //Resolve CuentaC entity (if available)
			if (id != null) {
				ent = manager.getById(id);
				if (ent == null) {
					ServicioNegocio.rollback(TipoError.FUNCIONAL, 1107, 
							"Unknown user account type", "Unknown user account type", null);
				}
			}
		}
		return ent;
	}

	protected RoleUsuarioC checkRole(Integer id) 
		throws RollbackException 
	{
		final RoleUsuarioManager manager = ManagerFactory.getInstance().getRoleUsuarioManager();
		RoleUsuarioC ent = null; { //Resolve RoleUsuarioC entity (if available)
			if (id != null) {
				ent = manager.getById(id);
				if (ent == null) {
					ServicioNegocio.rollback(TipoError.FUNCIONAL, 1108, 
							"Unknown user role group", "Unknown user role group", null);
				}
			}
		}
		return ent;
	}
	
	protected boolean createUserDirectories(String umd5, Integer accountId) {
		try {
			Process userdirs = Runtime.getRuntime().exec(new String[]{Constantes.getUSERDIRS_SCRIPT(), umd5, String.valueOf(Constantes.getConfigNum(accountId))});
			//Process usergeneralconf = Runtime.getRuntime().exec(new String[]{Constantes.getUSERDIRS_GEN_INI_SCRIPT(), umd5});
			userdirs.waitFor();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	} 
	
	protected void checkCreateUserDirectories(String umd5, Integer accountId) 
		throws RollbackException 
	{
		if (!createUserDirectories(umd5, accountId)) {
			ServicioNegocio.rollback(TipoError.FUNCIONAL, 1109, 
					"Problem during account creation", "Problem during account creation", null);
			return;
		}
	}
	
	protected boolean modifyUserDirectories(String umd5, Integer accountId) {
		try {
			Process userdirs = Runtime.getRuntime().exec(new String[]{Constantes.getMODIFYUSERDIRS_SCRIPT(), umd5, String.valueOf(Constantes.getConfigNum(accountId))});
			userdirs.waitFor();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	} 
	
	protected void checkModifyUserDirectories(String umd5, Integer accountId) 
		throws RollbackException 
	{
		if (!modifyUserDirectories(umd5, accountId)) {
			ServicioNegocio.rollback(TipoError.FUNCIONAL, 1113, 
					"Problem during account modification", "Problem during account modification", null);
			return;
		}
	}
	
	protected void checkFlagEntity(String flagEntity) 
		throws RollbackException 
	{
		if (flagEntity == null || (!flagEntity.equals("0") && !flagEntity.equals("1"))) {
			ServicioNegocio.rollback(TipoError.FUNCIONAL, 1110, 
					"Unknown FlagEntity", "Unknown FlagEntity", null);
			return;
		}
	}

	protected void checkLegalEntityName(
			final Integer entityId, 
			final String entityName) 
		throws RollbackException
	{
		//Find duplicates
		EntidadManager manager = ManagerFactory.getInstance().getEntidadManager();
		if (manager.isLegalEntityNameTaken(entityId, entityName)) {
			ServicioNegocio.rollback(TipoError.FUNCIONAL, 1111, 
					"The entity already exists", "The entity already exists", null);
			return;
		}
	}

	protected void checkURL(String url)
		throws RollbackException 
	{
		if (!FunctionUtils.isURL(url)) {
			ServicioNegocio.rollback(TipoError.FUNCIONAL, 1112, 
					"The URL seems have an invalid format", "The URL seems have an invalid format", null);
			return;
		}
	}

	/**
	 * Invoca el servicio de negocio encargado de mandar un correo 
	 * desde el email de la plataforma
	 * 
	 * @param contexto contexto en ejecucion
	 * @param mailTo direccion de correo de a quien va dirigido
	 * @param subject subject del correo
	 * @param body cuerpo del correo
	 * @return
	 * @throws NumberFormatException
	 * @throws IndexOutOfBoundsException
	 */
	protected IContexto[] invokeSendMailService(
			final IContextoEjecucion contexto, 
			String mailTo, String subject, String body) 
	{
		IContexto[] datosSalida;
		IDatosEntradaTx datosEntrada = ServicioNegocio.getPrograma(
				contexto, new Integer("029"), new Integer("004"));
		datosEntrada.addCampo(null, mailTo, 0);
		datosEntrada.addCampo(null, subject, 1);
		datosEntrada.addCampo(null, body, 2);

		// se invoca el SN
		datosSalida = ServicioNegocio.invocarServicio(contexto, datosEntrada);
		return datosSalida;
	}


}
