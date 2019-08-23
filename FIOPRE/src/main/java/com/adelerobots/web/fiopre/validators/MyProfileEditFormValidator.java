package com.adelerobots.web.fiopre.validators;

import com.adelerobots.fioneg.util.FunctionUtils;
import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.arq.negocio.core.ValidacionException;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;

/**
 * Validador de datos para la edicion de mi perfil
 * @author adele
 *
 */
public class MyProfileEditFormValidator 
	extends 	AbstractUserFormValidator
{

	private static final long serialVersionUID = -6761381372220779145L;


	private static final String FIELD_EMAIL = "email";
	private static final String FIELD_NICKNAME = "nickname";
	private static final String FIELD_CHANGEMYPASSWD = "changeMyPasswd";
	private static final String FIELD_PASSWORD = "password"; 
	private static final String FIELD_NEWPASSWORD1 = "newPassword1";
	private static final String FIELD_NEWPASSWORD2 = "newPassword2";
	private static final String FIELD_CREDITCARD = "creditcard_number";
	private static final String FIELD_CREDITCARD_EXPIRATION = "creditcard_expiration";
	private static final String FIELD_ENTITYTYPE = "radioTipoEntidad";
	private static final String FIELD_AGREED = "agree";
	private static final String FIELD_WEBSITE = "website";
	private static final String FIELD_WORKEMAIL = "workEmail";
	private static final String FIELD_PHONECODE = "countryCode";
	private static final String FIELD_PHONENUMBER = "phoneNumber";
	private static final String FIELD_PHONEEXT = "ext";

	/*
	 * (non-Javadoc)
	 * @see com.treelogic.fawna.presentacion.componentes.validators.form.interfaces.IValidacionFormulario#validate(com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes, com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes)
	 */
	public Boolean validate(
			final GestorEstadoComponentes gE,
			final GestorDatosComponentes gD)
	{
		
		String email = FunctionUtils.toString(gD.getValue(FIELD_EMAIL), null);
		try { validateEmailFormat(email); } catch (ValidacionException e) {
			gD.addErrorValidacionSimple(e.getMessage(), e.getMessage(), FIELD_EMAIL);
			return Boolean.FALSE;
		}
		
		String nickname = FunctionUtils.toString(gD.getValue(FIELD_NICKNAME), null);
		try { validateNicknameFormat(nickname); } catch (ValidacionException e) {
			gD.addErrorValidacionSimple(e.getMessage(), e.getMessage(), FIELD_NICKNAME);
			return Boolean.FALSE;
		}
		
		
		//Only check password changing if enabled
		final boolean changeMyPasswd = Boolean.TRUE.equals((Boolean) gD.getValue(FIELD_CHANGEMYPASSWD));
		if(changeMyPasswd) 
		{
			//If user has X509Certificate this is an invalid option
			if(ContextUtils.getX509Certificate()) {
				String msg = getMessage("MyProfileEditFormValidator.changePasswordWithCert", "Can't change password if you logged in with a certificate.");
				gD.addErrorValidacionSimple(msg, msg, FIELD_CHANGEMYPASSWD);
				return Boolean.FALSE;
			}
			//Test current passwords
			String curPassword = ContextUtils.getUserPassword();
			String password = FunctionUtils.toString(gD.getValue(FIELD_PASSWORD), null);
			if (password != null) password = FunctionUtils.encodePassword(password);
			if(!FunctionUtils.equals(password, curPassword)) {
				String msg = getMessage("MyProfileEditFormValidator.passwordsNotMatch", "Current password do not match.");
				gD.addErrorValidacionCruzada(msg, msg);
				return Boolean.FALSE;
			}
			
			//Test new passwords
			String newPassword1 = FunctionUtils.toString(gD.getValue(FIELD_NEWPASSWORD1), null);
			String newPassword2 = FunctionUtils.toString(gD.getValue(FIELD_NEWPASSWORD2), null);
			if(!FunctionUtils.equals(newPassword1, newPassword2)) {
				String msg = getMessage("MyProfileEditFormValidator.newPasswordsNotMatch", "New passwords do not match.");
				gD.addErrorValidacionCruzada(msg, msg);
				return Boolean.FALSE;
			}
			try { validatePasswordFormat(newPassword1); } catch (ValidacionException e) {
				gD.addErrorValidacionSimple(e.getMessage(), e.getMessage(), FIELD_NEWPASSWORD1);
				return Boolean.FALSE;
			}
			try { validateLength(newPassword1, "Passwords", PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH); } catch (ValidacionException e) {
				gD.addErrorValidacionSimple(e.getMessage(), e.getMessage(), FIELD_NEWPASSWORD1);
				return Boolean.FALSE;
			}
			try { validatePasswordFormat(newPassword2); } catch (ValidacionException e) {
				gD.addErrorValidacionSimple(e.getMessage(), e.getMessage(), FIELD_NEWPASSWORD2);
				return Boolean.FALSE;
			}
			try { validateLength(newPassword2, "Passwords", PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH); } catch (ValidacionException e) {
				gD.addErrorValidacionSimple(e.getMessage(), e.getMessage(), FIELD_NEWPASSWORD2);
				return Boolean.FALSE;
			}
		}
		
		String creditcard = FunctionUtils.toString(gD.getValue(FIELD_CREDITCARD), null);
		try { validateCreditCardNumberFormat(creditcard); } catch (ValidacionException e) {
			gD.addErrorValidacionSimple(e.getMessage(), e.getMessage(), FIELD_CREDITCARD);
			return Boolean.FALSE;
		}
		
		String creditcard_exp = FunctionUtils.toString(gD.getValue(FIELD_CREDITCARD_EXPIRATION), null);
		try { validateCreditCardExpirationFormat(creditcard_exp); } catch (ValidacionException e) {
			gD.addErrorValidacionSimple(e.getMessage(), e.getMessage(), FIELD_CREDITCARD_EXPIRATION);
			return Boolean.FALSE;
		}
		
		//Only check entity fields if business account is set
		final boolean business = ((String) gD.getValue(FIELD_ENTITYTYPE)).equals("1");
		if(business){
			if(!(Boolean) gD.getValue(FIELD_AGREED)){
				gD.addErrorValidacionSimple("You must agree the contract", "You must agree the contract", FIELD_AGREED);
				return Boolean.FALSE;
			}
			
			String website = FunctionUtils.toString(gD.getValue(FIELD_WEBSITE), null);
			try { validateURLFormat(website); } catch (ValidacionException e) {
				gD.addErrorValidacionSimple(e.getMessage(), e.getMessage(), FIELD_WEBSITE);
				return Boolean.FALSE;
			}

			String workEmail = FunctionUtils.toString(gD.getValue(FIELD_WORKEMAIL), null);
			try { validateEmailFormat(workEmail); } catch (ValidacionException e) {
				gD.addErrorValidacionSimple(e.getMessage(), e.getMessage(), FIELD_WORKEMAIL);
				return Boolean.FALSE;
			}

			try { validateWebsiteWorkemailMatch(website, workEmail); } catch (ValidacionException e) {
				gD.addErrorValidacionSimple(e.getMessage(), e.getMessage(), FIELD_WORKEMAIL);
				return Boolean.FALSE;
			}

			String phoneCode = FunctionUtils.toString(gD.getValue(FIELD_PHONECODE), null);
			try { validateCountryCodeFormat(phoneCode); } catch (ValidacionException e) {
				gD.addErrorValidacionSimple(e.getMessage(), e.getMessage(), FIELD_PHONECODE);
				return Boolean.FALSE;
			}

			String phoneNumber = FunctionUtils.toString(gD.getValue(FIELD_PHONENUMBER), null);
			try { validatePhoneNumberFormat(phoneNumber); } catch (ValidacionException e) {
				gD.addErrorValidacionSimple(e.getMessage(), e.getMessage(), FIELD_PHONENUMBER);
				return Boolean.FALSE;
			}

			String phoneExt = FunctionUtils.toString(gD.getValue(FIELD_PHONEEXT), null);
			try { validateExtFormat(phoneExt); } catch (ValidacionException e) {
				gD.addErrorValidacionSimple(e.getMessage(), e.getMessage(), FIELD_PHONEEXT);
				return Boolean.FALSE;
			}
		}
			
		return Boolean.TRUE;
	}



}
