package com.adelerobots.fioneg.service.usuarios;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adelerobots.fioneg.context.ContextoUsuarios;
import com.adelerobots.fioneg.entity.CuentaC;
import com.adelerobots.fioneg.entity.EntidadC;
import com.adelerobots.fioneg.entity.RoleUsuarioC;
import com.adelerobots.fioneg.entity.UserStatusEnum;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.entity.usuarioconfig.UsuarioConfigPk;
import com.adelerobots.fioneg.manager.EntidadManager;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuarioConfigManager;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.adelerobots.fioneg.util.FunctionUtils;
import com.adelerobots.fioneg.util.TemplateUtils;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.RollbackException;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;

/**
 * SN029001: Servicio de negocio para dar de alta usuarios de la plataforma
 * 
 * @author adele
 *
 */
public class SNAltaUsuario extends SNAbstractManageUsuarios {

	private static final int CTE_POSICION_TIRA_NAME = 0;
	private static final int CTE_POSICION_TIRA_SURNAME = 1;
	private static final int CTE_POSICION_TIRA_EMAIL = 2;
	private static final int CTE_POSICION_TIRA_PASSWORD = 3;
	private static final int CTE_POSICION_TIRA_ENTITY = 4;	
	private static final int CTE_POSICION_TIRA_ACCOUNTTYPE = 5;
	private static final int CTE_POSICION_TIRA_CREDITCARD = 6;
	private static final int CTE_POSICION_TIRA_CREDITCARD_EXPIRATION = 7;
	private static final int CTE_POSICION_TIRA_USERNAME = 8;
	private static final int CTE_POSICION_TIRA_ROLE = 9;
	private static final int CTE_POSICION_TIRA_TITLE = 10;
	private static final int CTE_POSICION_TIRA_FLAG_ENTITY = 11;
	private static final int CTE_POSICION_TIRA_ORGANIZATIONTYPE = 12;
	private static final int CTE_POSICION_TIRA_LEGALENTITYNAME = 13;
	private static final int CTE_POSICION_TIRA_WEBSITE = 14;
	private static final int CTE_POSICION_TIRA_WORKEMAIL = 15;
	private static final int CTE_POSICION_TIRA_COUNTRYCODE = 16;
	private static final int CTE_POSICION_TIRA_PHONENUMBER = 17;
	private static final int CTE_POSICION_TIRA_EXT = 18;
	private static final int CTE_POSICION_TIRA_PERIODO_PAGO = 19;
	private static final int CTE_POSICION_TIRA_FLAG_CERTIFICATE = 20;

	public SNAltaUsuario() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see com.treelogic.fawna.arq.negocio.core.ServicioNegocio#ejecutar(com.treelogic.fawna.arq.negocio.core.IContextoEjecucion, com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx)
	 */
	@Override
	public IContexto[] ejecutar(final IContextoEjecucion contexto, final IDatosEntradaTx datosEntrada) 
	{
		if (logger.isInfoEnabled()) {
			logger.info("Inicio Ejecucion del SN029001: Alta usuario");
		}
		final long iniTime = System.currentTimeMillis();

		String name = FunctionUtils.defaultIfEmpty(datosEntrada.getString(CTE_POSICION_TIRA_NAME), null);
		String surname = FunctionUtils.defaultIfEmpty(datosEntrada.getString(CTE_POSICION_TIRA_SURNAME), null);
		String email = FunctionUtils.defaultIfEmpty(datosEntrada.getString(CTE_POSICION_TIRA_EMAIL), null);
		String passwd = null, encPasswd = null; {
			final String value = FunctionUtils.defaultIfEmpty(datosEntrada.getString(CTE_POSICION_TIRA_PASSWORD), null);
			//Comprobar si se ha introducido password o si se tiene que generar aleatoriamente
			if(value != null) {
				passwd = value;
				encPasswd = FunctionUtils.encodePassword(passwd); //Codificar la password
			} else {
				passwd = FunctionUtils.createRandomToken(8); //Generar password aleatoria
				encPasswd = FunctionUtils.encodePassword(passwd); //Codificar la password
			}
		}
		Integer entityId = null; {
			final BigDecimal value = datosEntrada.getDecimal(CTE_POSICION_TIRA_ENTITY);
			if (value != null) entityId = new Integer(value.intValue());
		}
		Integer accountTypeId = null; {
			final BigDecimal value = datosEntrada.getDecimal(CTE_POSICION_TIRA_ACCOUNTTYPE);
			if (value != null) accountTypeId = new Integer(value.intValue());
		}
		Integer cardNum = null; {
			final BigDecimal value = datosEntrada.getDecimal(CTE_POSICION_TIRA_CREDITCARD);
			if (value != null) cardNum = new Integer(value.intValue());
		}
		String cardExp = FunctionUtils.defaultIfEmpty(datosEntrada.getString(CTE_POSICION_TIRA_CREDITCARD_EXPIRATION), null);
		String signupCode = FunctionUtils.createRandomToken(32); //Generar signup code aleatorio
		String username = FunctionUtils.defaultIfEmpty(datosEntrada.getString(CTE_POSICION_TIRA_USERNAME), null);
		Integer roleId = null; {
			final BigDecimal value = datosEntrada.getDecimal(CTE_POSICION_TIRA_ROLE);
			if (value != null) roleId = new Integer(value.intValue());
		}
		String title = FunctionUtils.defaultIfEmpty(datosEntrada.getString(CTE_POSICION_TIRA_TITLE), null);
		String flagEntity = FunctionUtils.defaultIfEmpty(datosEntrada.getString(CTE_POSICION_TIRA_FLAG_ENTITY), null);

		Integer organizationType = null; {
			final BigDecimal value = datosEntrada.getDecimal(CTE_POSICION_TIRA_ORGANIZATIONTYPE);
			if (value != null) organizationType = new Integer(value.intValue());
		}
		String legalEntityName = FunctionUtils.defaultIfEmpty(datosEntrada.getString(CTE_POSICION_TIRA_LEGALENTITYNAME), null);
		String website = FunctionUtils.defaultIfEmpty(datosEntrada.getString(CTE_POSICION_TIRA_WEBSITE), null);
		String workEmail = FunctionUtils.defaultIfEmpty(datosEntrada.getString(CTE_POSICION_TIRA_WORKEMAIL), null);
		String countryCode = FunctionUtils.defaultIfEmpty(datosEntrada.getString(CTE_POSICION_TIRA_COUNTRYCODE), null);
		String phoneNumber = FunctionUtils.defaultIfEmpty(datosEntrada.getString(CTE_POSICION_TIRA_PHONENUMBER), null);
		String ext = FunctionUtils.defaultIfEmpty(datosEntrada.getString(CTE_POSICION_TIRA_EXT), null);
		Character pagoAnual = null;
		String strIdTipoPeriodoPago = datosEntrada.getString(CTE_POSICION_TIRA_PERIODO_PAGO);
		if(strIdTipoPeriodoPago != null && !"".equals(strIdTipoPeriodoPago)){
			if(strIdTipoPeriodoPago.equals("1")){
				pagoAnual = new Character('0');
			}else{
				pagoAnual = new Character('1');
			}
		}
		String flagCertificate = FunctionUtils.defaultIfEmpty(datosEntrada.getString(CTE_POSICION_TIRA_FLAG_CERTIFICATE), "0");
		
		
		IContexto[] salida = null;
		try 
		{
			//Prechecks
			checkEmail(null, email);
			checkUsername(null, username);
			checkCreditCardNumberFormat(FunctionUtils.toString(cardNum, null));
			checkCreditCardExpirationFormat(cardExp);
			EntidadC entity = checkEntity(entityId); //Resolve EntidadC entity (if available)
			CuentaC accountType = checkAccountType(accountTypeId); //Resolve CuentaC entity (if available)
			RoleUsuarioC role = checkRole(roleId); //Resolve RoleUsuarioC entity (if available)
			checkFlagEntity(flagEntity);
			
			final UsuariosManager userManager = ManagerFactory.getInstance().getUsuariosManager();
			final UsuarioConfigManager userconfigManager = ManagerFactory.getInstance().getUsuarioConfigManager();
			// Comprobar si el usuario que se está registrando ya existía en nuestra BBDD
			UsuarioC user = null;
			List<UsuarioC> canceledUsers = userManager.findCanceledUsersByEmail(email);
			if(canceledUsers != null && !canceledUsers.isEmpty()){
				user = canceledUsers.get(0);				
				user = userManager.update(user.getCnUsuario(), legalEntityName, surname, workEmail, 
						encPasswd, UserStatusEnum.UNCONFIRMED, entity, accountType, cardNum, 
						cardExp, signupCode, username, role, title, flagEntity, pagoAnual);
			}else{
				//Persist new properties
				user = userManager.create(
							name, surname, 
							email, encPasswd, 
							entity, accountType, 
							cardNum, cardExp, 
							signupCode, username, 
							role, title, flagEntity,
							pagoAnual);
			}
			
			//Create UsuarioConfigC depending on accountTypeId 
			int configs = Constantes.getConfigNum(accountTypeId);
			for(int i = 0; i<configs; i++) {
				UsuarioConfigPk userConfigPk = new UsuarioConfigPk();
				userConfigPk.setUsuario(user);
				
				userConfigPk.setConfig(i);
				userconfigManager.create(userConfigPk, "Config "+Integer.toString(i+1));
			}
			
			// Si marca cuenta bussines
			if(flagEntity != null && flagEntity.equals("1")) {
				final EntidadManager entityManager = ManagerFactory.getInstance().getEntidadManager();
				EntidadC check = entityManager.getByName(legalEntityName);
				// Si la entidad que escribio no existe la creamos
				if (check == null) {
					EntidadC entidad = entityManager.create(legalEntityName, organizationType, website, workEmail, countryCode, phoneNumber, ext);
					userManager.updateUserEntity(user, entidad);
				}
				// Si ya existe se la asociamos
				else{
					userManager.updateUserEntity(user, check);
				}
			}
			
			//Crear los la estructura de directorios
			checkCreateUserDirectories(user.getAvatarBuilderUmd5(), accountTypeId);
			//Registrar en la tabla usuariospark las entradas para usuario.getIntUserId() con las sparks del core
			IContexto[] datosSalida_coresparks = invokeRegisterUserCoreSparks(contexto, user);
			
			//Correo para la plataforma
			IContexto[] datosSalida_mailadmin = sendSignupMailToAdmin(contexto, user, passwd);
			//Correo para el usuario
			IContexto[] datosSalida_mailuser = null;
			if("0".equals(flagCertificate))
				datosSalida_mailuser = sendSignupMailToUser(contexto, user, passwd);
			
			salida = ContextoUsuarios.rellenarContexto(user);
			
		} catch (Exception e) {
			if (e instanceof RollbackException) {
				//ServicioNegocio.rollback(...) throw this exception, rethrow!!
				throw (RollbackException) e;
			}
			// se ha producido un error de ejecucion
			ServicioNegocio.rollback(TipoError.TECNICO, new Integer(0), e.getMessage(), e.getMessage(), null);
		}
		if (logger.isInfoEnabled()) {
			logger.info("Fin Ejecucion del SN029001: Alta usuario. Tiempo total = " + Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		}
		return salida;
	}



	protected IContexto[] sendSignupMailToAdmin(
			final IContextoEjecucion contexto, 
			final UsuarioC user, final String rawPasswd) 
		throws IOException, TemplateException 
	{
		final BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		final TemplateHashModel enumModels = wrapper.getEnumModels();
		//final TemplateHashModel staticModels = wrapper.getStaticModels();
		
		final Map<String, Object> model = new HashMap<String, Object>();
		model.put("UserStatusEnum", enumModels.get(UserStatusEnum.class.getName()));
		model.put("user", user);
		model.put("rawPasswd", rawPasswd);
		
		final String subject = "[FIONA] Signup incoming";
		final String body = TemplateUtils.processTemplateIntoString(contexto, "mail_team_signup-html", model);
		
		//invocamos el servicio de negocio para enviar mail al admin
		final String mailTo = Constantes.getMAIL_NOTIFICATION_ADDR();
		return invokeSendMailService(contexto, mailTo, subject, body);
	}



	protected IContexto[] sendSignupMailToUser(
			final IContextoEjecucion contexto, 
			final UsuarioC user, final String rawPasswd) 
		throws IOException, TemplateException 
	{
		final BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		final TemplateHashModel enumModels = wrapper.getEnumModels();
		//final TemplateHashModel staticModels = wrapper.getStaticModels();
		
		final Map<String, Object> model = new HashMap<String, Object>();
		model.put("UserStatusEnum", enumModels.get(UserStatusEnum.class.getName()));
		model.put("user", user);
		model.put("rawPasswd", rawPasswd);
		
		final String subject = "[FIONA] Signup confirmation";
		final String body = TemplateUtils.processTemplateIntoString(contexto, "mail_user_signup-html", model);
		
		//invocamos el servicio de negocio para enviar mail al usuario
		final String mailTo = user.getEmail();
		return invokeSendMailService(contexto, mailTo, subject, body);
	}



	protected IContexto[] invokeRegisterUserCoreSparks(
			final IContextoEjecucion contexto, 
			final UsuarioC usuario)
	{
		IContexto[] datosSalida;
		BigDecimal cnUsuario = new BigDecimal(usuario.getCnUsuario());
		
		//Get AddSparksToUser SN 029011 definition
		IDatosEntradaTx datosEntrada = ServicioNegocio.getPrograma(
				contexto, new Integer("029"), new Integer("011"));
		datosEntrada.addCampo(null, cnUsuario, 0);
		datosEntrada.addCampo(null, Constantes.getCORE_SPARKS(), 1);
		// invoke SN 029011
		datosSalida = ServicioNegocio.invocarServicio(contexto, datosEntrada);
		
		return datosSalida;
	}



}
