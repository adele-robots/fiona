package com.adelerobots.fioneg.context;

import java.math.BigDecimal;
import java.util.List;

import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.util.StringEncrypterUtilities;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

/**
 * Clase destinada a los contextos necesarios para los servicios con funciones
 * sobre los usuarios
 * 
 * @author
 * 
 */
public class ContextoUsuarios {
	public static final String USERS_CTX = "FIONEGN001";

	public static final String CTX_USER_ID = "FIONEG001010";
	public static final String CTX_USER_NAME = "FIONEG001020";
	public static final String CTX_USER_SURNAME = "FIONEG001030";
	public static final String CTX_USER_EMAIL = "FIONEG001040";
	public static final String CTX_USER_EMAIL_ID = "FIONEG001041";
	public static final String CTX_USER_PASSWORD = "FIONEG001050";
	public static final String CTX_USER_ENTITY_ID = "FIONEG001060";
	public static final String CTX_USER_ENTITY_NAME = "FIONEG001061";
	public static final String CTX_USER_ACCOUNTTYPE_ID = "FIONEG001070";
	public static final String CTX_USER_ACCOUNTTYPE_NAME = "FIONEG001071";
	public static final String CTX_USER_STATUS = "FIONEG001080";
	public static final String CTX_USER_CARD = "FIONEG001090";
	public static final String CTX_USER_CARD_EXPIRATION = "FIONEG001100";
	public static final String CTX_USER_SIGNUPCODE = "FIONEG001110";
	public static final String CTX_USER_USERNAME = "FIONEG001120";
	public static final String CTX_USER_FULLNAME = "FIONEG001121";
	public static final String CTX_USER_DISPLAYNAME = "FIONEG001122";
	public static final String CTX_USER_AVATARBUILDER_UMD5 = "FIONEG001130";
	public static final String CTX_USER_ROLE_ID = "FIONEG001140";
	public static final String CTX_USER_ROLE_NAME = "FIONEG001141";
	public static final String CTX_USER_TITLE = "FIONEG001150";
	public static final String CTX_USER_FLAG_ENTITY = "FIONEG001160";
	public static final String CTX_USER_ACCOUNTTYPE_AMT_MONTHLY = "FIONEG001170";
	public static final String CTX_USER_ACCOUNTTYPE_AMT_YEARLY = "FIONEG001180";
	public static final String CTX_USER_ACCOUNTTYPE_PERIOD = "FIONEG001190";
	public static final String CTX_USER_BILLING_AGREEMENT_ID = "FIONEG001200";
	public static final String CTX_USER_ACCOUNT_CREDIT = "FIONEG001210";


	public static IContexto[] rellenarContexto(final UsuarioC usuario) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (usuario != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoUsuario(usuario);
		}
		return salida;
	}
	
	private static IContexto rellenarContextoUsuario(final UsuarioC bean) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(USERS_CTX);

		datos.put(CTX_USER_ID, bean.getCnUsuarioAsBd());
		datos.put(CTX_USER_NAME, bean.getName());
		datos.put(CTX_USER_SURNAME, bean.getSurname());
		datos.put(CTX_USER_FULLNAME, bean.getFullname());
		datos.put(CTX_USER_EMAIL, bean.getEmail());
		datos.put(CTX_USER_EMAIL_ID, bean.getEmailId());
		datos.put(CTX_USER_PASSWORD, bean.getPassword());
		datos.put(CTX_USER_ENTITY_ID, bean.getEntidadIdAsBd());
		datos.put(CTX_USER_ENTITY_NAME, bean.getEntidadName());
		datos.put(CTX_USER_ACCOUNTTYPE_ID, bean.getCuentaIdAsBd());
		datos.put(CTX_USER_ACCOUNTTYPE_NAME, bean.getCuentaName());
		datos.put(CTX_USER_STATUS, bean.getStrStatus());
		datos.put(CTX_USER_CARD, bean.getCreditCardNumberAsBd());
		datos.put(CTX_USER_CARD_EXPIRATION, bean.getCreditCardExpiration());
		datos.put(CTX_USER_SIGNUPCODE, bean.getSignupCode());
		datos.put(CTX_USER_USERNAME, bean.getUsername());
		datos.put(CTX_USER_DISPLAYNAME, bean.getDisplayname());
		//AvatarBuilder encoded userpath
		datos.put(CTX_USER_AVATARBUILDER_UMD5, bean.getAvatarBuilderUmd5());
		//GroupRole
		datos.put(CTX_USER_ROLE_ID, bean.getRoleIdAsBd());
		datos.put(CTX_USER_ROLE_NAME, bean.getRoleName());

		datos.put(CTX_USER_TITLE, bean.getTitle());
		datos.put(CTX_USER_FLAG_ENTITY, bean.getFlagEntidad());
		datos.put(CTX_USER_ACCOUNTTYPE_AMT_MONTHLY, new BigDecimal(bean.getCuenta().getFloPrecioMensual()).setScale(2,BigDecimal.ROUND_UP));
		datos.put(CTX_USER_ACCOUNTTYPE_AMT_YEARLY, new BigDecimal(bean.getCuenta().getFloPrecioAnual()).setScale(2,BigDecimal.ROUND_UP));
		if(bean.getChPagoAnual() != null){
			String isPagoAnual = bean.getChPagoAnual().toString();
			if(isPagoAnual.equals("1"))				
				datos.put(CTX_USER_ACCOUNTTYPE_PERIOD, "2");
			else
				datos.put(CTX_USER_ACCOUNTTYPE_PERIOD, "1");
		}
		if(bean.getStrBillingId() != null && !"".equals(bean.getStrBillingId())){
			String encodedBillingId = bean.getStrBillingId();
			StringEncrypterUtilities desencrypter= new StringEncrypterUtilities(Constantes.PASS_PHRASE);
			datos.put(CTX_USER_BILLING_AGREEMENT_ID, desencrypter.decrypt(encodedBillingId));
		}
		if(bean.getFloAccountCredit() != null)
			datos.put(CTX_USER_ACCOUNT_CREDIT, new BigDecimal(bean.getAccountCredit()).setScale(2,BigDecimal.ROUND_UP));
		
		return datos;
	}

	public static IContexto[] rellenarContexto(List<UsuarioC> list) {
		IContexto[] salida = null;
		if (list != null) {
			final int iSize = list.size();
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoUsuario(list.get(i));
			}
		}
		return salida;
	}

	
	
}
