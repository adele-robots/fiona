package com.adelerobots.web.fiopre.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.adelerobots.fioneg.util.FunctionUtils;
import com.treelogic.fawna.arq.negocio.core.ValidacionException;
import com.treelogic.fawna.presentacion.componentes.validators.form.interfaces.IValidacionFormulario;


public abstract class AbstractUserFormValidator 
	extends 		AbstractFormValidator
	implements 	IValidacionFormulario
{
	private static final long serialVersionUID = -4593803498096193187L;


	protected final int PASSWORD_MIN_LENGTH = 6;
	protected final int PASSWORD_MAX_LENGTH = 12;




	/**
	 * Validate the format for a nickname/username field
	 * @param candidate string to validate
	 * @throws ValidacionException if not valid
	 */
	protected void validatePasswordFormat(String candidate) throws ValidacionException {
		if (candidate == null) return;
		
		//se admite cualquier otro caracter además de los listados pero se pide que tenga al menos una letra y un número
		final String LEGAL_ALPH = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		final String LEGAL_NUM = "_0123456789";
		
		boolean alphCheck = false;
		boolean numCheck = false;
		for ( int i = 0; i < candidate.length(); i++ ) {
			if (LEGAL_ALPH.indexOf(candidate.charAt(i))>0) alphCheck=true;
			if (LEGAL_NUM.indexOf(candidate.charAt(i))>0) numCheck=true;
			if (alphCheck && numCheck) return;
		}
		throw new ValidacionException(getMessage(
				"FWN_validator.validatePasswordFormat.0", 
				new Object[]{LEGAL_ALPH, LEGAL_NUM}, 
				"Your password must be 6-12 characters and contain letters and numbers."));
	}



	/**
	 * Validate the format for a email field
	 * @param candidate string to validate
	 * @throws ValidacionException if not valid
	 */
	protected void validateEmailFormat(String candidate) throws ValidacionException {
		if (candidate == null) return;
		
		//Validate email format
		if (!FunctionUtils.isEmail(candidate)) {
			throw new ValidacionException(getMessage(
					"FWN_validator.validateEmailFormat.0", 
					"E-mail seems have an invalid format."));
		}
	}



	/**
	 * Validate the format for a creditcard number field
	 * @param candidate string to validate
	 * @throws ValidacionException if not valid
	 */
	protected void validateCreditCardNumberFormat(String candidate) throws ValidacionException {
		if (candidate == null) return;
		
		//Validate email format
		if (!FunctionUtils.isCreditCard(candidate)) {
			throw new ValidacionException(getMessage(
					"FWN_validator.validateCreditCardNumberFormat.0", 
					"Creditcard seems have an invalid format."));
		}
	}



	/**
	 * Validate the format for a creditcard expiration date field
	 * @param candidate string to validate
	 * @throws ValidacionException if not valid
	 */
	protected void validateCreditCardExpirationFormat(String candidate) throws ValidacionException {
		if (candidate == null) return;
		
		//Pattern = MM/YYYY
		Pattern pattern = Pattern.compile("^([0-1][0-9])\\/([0-9]{4})$");
		if (!pattern.matcher(candidate).matches()) {
			throw new ValidacionException(getMessage(
					"FWN_validator.validateCreditCardExpirationFormat.0", 
					new Object[]{"MM/YYYY"}, 
					"Invalid format for creditcard expiration date. Try {0}."));
		}
	}



	/**
	 * Validate the format for a nickname/username field
	 * @param candidate string to validate
	 * @throws ValidacionException if not valid
	 */
	protected void validateNicknameFormat(String candidate) throws ValidacionException {
		if (candidate == null) return;
		
		//Al menos no tiene que tener una "@" para que no colisione internamenete al buscar usuarios por mail o username
		String illegalChars = "@\"#$%&?¿¡!";
		for ( int i = 0; i < candidate.length(); i++ ) {
			if (Character.isWhitespace(candidate.charAt(i))) {
				throw new ValidacionException(getMessage(
						"FWN_validator.validateNicknameFormat.0", 
						"Nicknames can not contain whitespace characters."));
			}
			if (illegalChars.contains(""+candidate.charAt(i))) {
				throw new ValidacionException(getMessage(
						"FWN_validator.validateNicknameFormat.1", 
						new Object[]{illegalChars}, 
						"Nicknames can not contain certain characters: {0}"));
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
		throw new ValidacionException(getMessage(
				"FWN_validator.validateNicknameFormat.2", 
				new Object[]{LEGAL_ALPH, LEGAL_NUM}, 
				"Nicknames must contain at least, letters ({0}) or numbers ({1})."));
	}



	/**
	 * Validate the length of a string. 
	 * @param candidate string to validate
	 * @param label label that identify the candidate string
	 * @param min minimum characters. Negative numbers to ignore
	 * @param max maximum characters. Negative numbers to ignore
	 * @throws ValidacionException if not valid
	 */
	protected void validateLength(String candidate, String label, int min, int max) throws ValidacionException {
		candidate = candidate == null ? "" : candidate;
		int length = candidate.length();
		if (min > 0 && max > 0) {
			if ( length < min || length > max) {
				throw new ValidacionException(getMessage(
						"FWN_validator.validateLength.0", 
						new Object[]{label, new Integer(min), new Integer(max)}, 
						"{0} lenght must be between {1} and {2} characters."));
			}
		} else if (min > 0 && !(max > 0)) {
			if ( length < min) {
				throw new ValidacionException(getMessage(
						"FWN_validator.validateLength.1", 
						new Object[]{label, new Integer(min), new Integer(max)}, 
						"{0} lenght must be at least {1} characters."));
			}
		} else {
			if ( length > max) {
				throw new ValidacionException(getMessage(
						"FWN_validator.validateLength.2", 
						new Object[]{label, new Integer(min), new Integer(max)}, 
						"{0} lenght must be at most {2} characters."));
			}
		}
	}
	
	
	/**
	 * Método que comprueba si el formato de la versión
	 * es correcto
	 * @param version Versión cuyo formato se comprueba
	 * @throws ValidacionException
	 */
	protected void validateVersionFormat(String version) throws ValidacionException{		
		Pattern pat = null;
		Matcher matVersion = null;

		pat = Pattern.compile("[0-9].[0-9]");
		matVersion = pat.matcher(version);

		if (!matVersion.matches())
			throw new ValidacionException(getMessage(
					"FormValidatorSparkEdition.validateVersionFormat","Version must be x.y where 'x' and 'y' are numbers"));


	}
	
	/**
	 * Método que permite asegurar que los días para una versión trial
	 * sean mayores que cero
	 * @param diasTrial días introducidos por el usuario
	 * @throws ValidacionException
	 */
	protected void validateDiasTrialAmount(String diasTrial) throws ValidacionException{		
			if(diasTrial.compareTo("0")==0){				
				throw new ValidacionException(getMessage(
						"FormValidatorSparkEdition.validateDiasTrialAmount","Trial days should be greater than zero."));
			}
		

	}

	/**
	 * Método que permite validar el formato de una URL
	 * 
	 * @param url Dirección cuyo formato se comprobará
	 * @throws ValidacionException
	 */
	protected void validateURLFormat(String url) throws ValidacionException {
		Pattern pat = null;
		Matcher matURL = null;
		
		pat = Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
		matURL = pat.matcher(url);
		
		if(!matURL.matches())
			throw new ValidacionException(getMessage("FormValidatorSparkEdition.validateURLFormat","URL format is incorrect."));
	}

	/**
	 * Método que permite validar el formato de un prefijo de país
	 * 
	 * @param number Prefijo cuyo formato se comprobará
	 * @throws ValidacionException
	 */
	protected void validateCountryCodeFormat(String phoneCode) throws ValidacionException {
		if (phoneCode == null) return;
		Pattern pat = null;
		Matcher matNum = null;
		
		pat = Pattern.compile("^[+]?[0-9]{1,4}");
		matNum = pat.matcher(phoneCode);
		
		if(!matNum.matches())
			throw new ValidacionException(getMessage("FormValidatorSparkEdition.validateCountryCodeFormat","Number format is incorrect."));
	}
	
	/**
	 * Método que permite validar el formato de un numero de teléfono
	 * 
	 * @param number Numero de teléfono cuyo formato se comprobará
	 * @throws ValidacionException
	 */
	protected void validatePhoneNumberFormat(String phoneNumber) throws ValidacionException {
		if (phoneNumber == null) return;
		Pattern pat = null;
		Matcher matNum = null;
		
		pat = Pattern.compile("^[(]?[0-9]{2,20}[)]?[0-9 -.()]*[0-9]{2,20}");
		matNum = pat.matcher(phoneNumber);
		
		if(!matNum.matches())
			throw new ValidacionException(getMessage("FormValidatorSparkEdition.validatePhoneNumberFormat","Number format is incorrect."));
	}
	
	/**
	 * Método que permite validar el formato de una extensión
	 * 
	 * @param number Extensión de teléfono cuyo formato se comprobará
	 * @throws ValidacionException
	 */
	protected void validateExtFormat(String phoneExt) throws ValidacionException {
		if (phoneExt == null || phoneExt.equals("")) return;
		Pattern pat = null;
		Matcher matNum = null;
		
		pat = Pattern.compile("[0-9]{0,5}");
		matNum = pat.matcher(phoneExt);
		
		if(!matNum.matches())
			throw new ValidacionException(getMessage("FormValidatorSparkEdition.validateExtFormat","Number format is incorrect."));
	}
	
	/**
	 * Método que comprueba que el dominio del workEmail está contenido en el website
	 * 
	 * @param website Sitio web de la entidad
	 * @param workEmail Email de la entidad
	 * @throws ValidacionException
	 */
	protected void validateWebsiteWorkemailMatch(String website, String workEmail) throws ValidacionException {
		String domain = workEmail.substring(workEmail.indexOf("@")+1);
		String domainName = domain.substring(0, domain.indexOf("."));
		if(!website.contains(domainName))
			throw new ValidacionException(getMessage("FormValidatorSparkEdition.validateWebsiteWorkemailMatch","WorkEmail must belong to WebSite's domain."));
	}
	

}
