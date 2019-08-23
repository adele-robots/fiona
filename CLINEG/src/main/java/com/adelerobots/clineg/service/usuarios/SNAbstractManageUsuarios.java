package com.adelerobots.clineg.service.usuarios;


import java.util.regex.Pattern;

import com.adelerobots.clineg.entity.CuentaC;
import com.adelerobots.clineg.entity.RoleUsuarioC;
import com.adelerobots.clineg.manager.CuentaManager;
import com.adelerobots.clineg.manager.ManagerFactory;
import com.adelerobots.clineg.manager.RoleUsuarioManager;
import com.adelerobots.clineg.manager.UsuariosManager;
import com.adelerobots.clineg.util.FunctionUtils;
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

	

	protected RoleUsuarioC checkRole(Integer id, String datasource) 
		throws RollbackException 
	{
		final RoleUsuarioManager manager = ManagerFactory.getInstance().getRoleUsuarioManager(datasource);
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
	
		
	
	protected void checkFlagEntity(String flagEntity) 
		throws RollbackException 
	{
		if (flagEntity == null || (!flagEntity.equals("0") && !flagEntity.equals("1"))) {
			ServicioNegocio.rollback(TipoError.FUNCIONAL, 1110, 
					"Unknown FlagEntity", "Unknown FlagEntity", null);
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
