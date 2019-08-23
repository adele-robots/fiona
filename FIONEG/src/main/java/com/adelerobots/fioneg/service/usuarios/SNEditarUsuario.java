package com.adelerobots.fioneg.service.usuarios;


import java.math.BigDecimal;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoUsuarios;
import com.adelerobots.fioneg.entity.CuentaC;
import com.adelerobots.fioneg.entity.EntidadC;
import com.adelerobots.fioneg.entity.RoleUsuarioC;
import com.adelerobots.fioneg.entity.UserStatusEnum;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.entity.usuarioconfig.UsuarioConfigC;
import com.adelerobots.fioneg.entity.usuarioconfig.UsuarioConfigPk;
import com.adelerobots.fioneg.manager.EntidadManager;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.adelerobots.fioneg.manager.UsuarioConfigManager;
import com.adelerobots.fioneg.service.security.LogonConstants.LogonErrorCode;
import com.adelerobots.fioneg.util.FunctionUtils;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.RollbackException;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;

/**
 * SN029002: Servicio de negocio para modificar usuarios de la plataforma
 * 
 * @author adele
 *
 */
public class SNEditarUsuario extends SNAbstractManageUsuarios 
{


	private static final int CTE_POSICION_TIRA_ID = 0;
	private static final int CTE_POSICION_TIRA_NAME = 1;
	private static final int CTE_POSICION_TIRA_SURNAME = 2;
	private static final int CTE_POSICION_TIRA_EMAIL = 3;
	private static final int CTE_POSICION_TIRA_PASSWORD = 4;
	private static final int CTE_POSICION_TIRA_STATUS = 5;
	private static final int CTE_POSICION_TIRA_ENTITY = 6;
	private static final int CTE_POSICION_TIRA_ACCOUNTTYPE = 7;
	private static final int CTE_POSICION_TIRA_CREDITCARD = 8;
	private static final int CTE_POSICION_TIRA_CREDITCARD_EXPIRATION = 9;
	private static final int CTE_POSICION_TIRA_SIGNUPCODE = 10;
	private static final int CTE_POSICION_TIRA_USERNAME = 11;
	private static final int CTE_POSICION_TIRA_ROLE = 12;
	private static final int CTE_POSICION_TIRA_TITLE = 13;
	private static final int CTE_POSICION_TIRA_FLAG_ENTITY = 14;
	private static final int CTE_POSICION_TIRA_ORGANIZATIONTYPE = 15;
	private static final int CTE_POSICION_TIRA_LEGALENTITYNAME = 16;
	private static final int CTE_POSICION_TIRA_WEBSITE = 17;
	private static final int CTE_POSICION_TIRA_WORKEMAIL = 18;
	private static final int CTE_POSICION_TIRA_COUNTRYCODE = 19;
	private static final int CTE_POSICION_TIRA_PHONENUMBER = 20;
	private static final int CTE_POSICION_TIRA_EXT = 21;
	private static final int CTE_POSICION_TIRA_PERIODO_PAGO = 22;
	
	public SNEditarUsuario(){
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
			logger.info("Inicio Ejecucion del SN029002: Modificar usuario");
		}
		final long iniTime = System.currentTimeMillis();
		
		Integer userId = null; {
			final BigDecimal value = datosEntrada.getDecimal(CTE_POSICION_TIRA_ID);
			if (value != null) userId = new Integer(value.intValue());
		}
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
				passwd = value; //FunctionUtils.createRandomToken(8); //Generar password aleatoria
				encPasswd = value; //FunctionUtils.encodePassword(passwd); //Codificar la password
			}
		}
		UserStatusEnum status = null; {
			final String value = datosEntrada.getString(CTE_POSICION_TIRA_STATUS);
			if(value != null) {
				try { status = UserStatusEnum.getValue(value); } catch (IllegalArgumentException e) {
					ServicioNegocio.rollback(TipoError.TECNICO, new Integer(0)
						, String.format("El campo C%03d (%s) tiene un valor no permitido '%s'.", 
								CTE_POSICION_TIRA_STATUS, "status", value) 
						, String.format("El campo C%03d (%s) tiene un valor no permitido '%s'.", 
								CTE_POSICION_TIRA_STATUS, "status", value) 
						, null);
				}
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
		String signupCode = FunctionUtils.defaultIfEmpty(datosEntrada.getString(CTE_POSICION_TIRA_SIGNUPCODE), null);
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
		
		IContexto[] salida = null;
		try 
		{
			//Prechecks
			checkEmail(userId, email);
			checkUsername(userId, username);
			checkCreditCardNumberFormat(FunctionUtils.toString(cardNum, null));
			checkCreditCardExpirationFormat(cardExp);
			EntidadC entity = checkEntity(entityId); //Resolve EntidadC entity (if available)
			CuentaC accountType = checkAccountType(accountTypeId); //Resolve CuentaC entity (if available)
			RoleUsuarioC role = checkRole(roleId); //Resolve RoleUsuarioC entity (if available)
			checkFlagEntity(flagEntity);
			
			final UsuariosManager userManager = ManagerFactory.getInstance().getUsuariosManager();
			final UsuarioConfigManager userconfigManager = ManagerFactory.getInstance().getUsuarioConfigManager();
			//Persist new properties
			UsuarioC user = userManager.update(userId, 
						name, surname, 
						email, encPasswd, status, 
						entity, accountType, 
						cardNum, cardExp, 
						signupCode, username, role,
						title, flagEntity, pagoAnual);
			if (user == null) { //Unknown / no such user
				LogonErrorCode errCode = LogonErrorCode.ERR525__NO_SUCH_USER;
				ServicioNegocio.rollback(TipoError.FUNCIONAL, errCode.code, errCode.msg, errCode.desc, null);
			} else {
				//Change UsuarioConfigC depending on accountType
				int configs = Constantes.getConfigNum(accountTypeId);
				List<UsuarioConfigC> list = userconfigManager.getAllUsuarioConfigs(userId);
				if(list.size() != configs) {
					if(list.size() > configs) {
						for(int i = configs; i < list.size(); i++) {
							userconfigManager.getById(userId, i).delete();
						}
					}
					else {
						for(Integer i = list.size(); i<configs; i++) {
							UsuarioConfigPk userConfigPk = new UsuarioConfigPk();
							userConfigPk.setUsuario(user);
							userConfigPk.setConfig(i);
							userconfigManager.create(userConfigPk, "Config "+Integer.toString(i+1));
						}
					}
				}
				// Si marca cuenta bussines
				if(flagEntity != null && flagEntity.equals("1")) {
					final EntidadManager entityManager = ManagerFactory.getInstance().getEntidadManager();
					EntidadC entidad = entityManager.getById(entityId);
					// Si no tenia ya una entidad asociada
					if(entidad == null) {
						EntidadC check = entityManager.getByName(legalEntityName);
						// Si la entidad que escribio no existe la creamos
						if (check == null) {
							entidad = entityManager.create(legalEntityName, organizationType, website, workEmail, countryCode, phoneNumber, ext);
							userManager.updateUserEntity(user, entidad);
						}
						// Si ya existe se la asociamos
						else{
							userManager.updateUserEntity(user, check);
						}
					}
					// Si ya tenia entidad la actualizamos
					else {
						checkLegalEntityName(entityId, legalEntityName);
						entityManager.update(entityId, legalEntityName, organizationType, website, workEmail, countryCode, phoneNumber, ext);
					}
				}
				// Si no marca cuenta bussines le ponemos la entidad a null
				else {
					userManager.updateUserEntity(user, null);
				}
				// Calcular crédito del usuario
				Float credit = userManager.getAccountUserCredit(user);
				user.setAccountCredit(credit);
				checkModifyUserDirectories(user.getAvatarBuilderUmd5(), accountTypeId);				
				salida = ContextoUsuarios.rellenarContexto(user);
			}
		} catch (Exception e) {
			if (e instanceof RollbackException) {
				//ServicioNegocio.rollback(...) throw this exception, rethrow!!
				throw (RollbackException) e;
			}
			// se ha producido un error de ejecucion
			ServicioNegocio.rollback(TipoError.TECNICO, new Integer(0), e.getMessage(), e.getMessage(), null);
		}
		if (logger.isInfoEnabled()) {
			logger.info("Fin Ejecucion del SN029002: Modificar usuario. Tiempo total = " + Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		}
		return salida;
	}


}
