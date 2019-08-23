package com.adelerobots.fawna.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.adelerobots.fioneg.service.security.LogonConstants;
import com.adelerobots.fioneg.util.FunctionUtils;
import com.adelerobots.web.fiopre.utilidades.ConfigUtils;
import com.treelogic.fawna.arq.negocio.conector_rmi.ErrorOperacionException;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.invoker.FawnaInvoker;
import com.treelogic.fawna.presentacion.core.persistencia.ContextoOperacion;
import com.treelogic.fawna.presentacion.core.security.exceptions.ServiceDataAccessException;
import com.treelogic.fawna.presentacion.core.security.userdetails.User;

/**
 * Obtiene la informacion asociada al usuario que se intentan autenticar en el
 * sistema
 * 
 * @author 
 * 
 */
public class BaseUserDetailsServiceImpl 
	implements 	UserDetailsService, 
					LogonConstants
{

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(
			final String username) 
	throws UsernameNotFoundException, DataAccessException 
	{
		
		//Obtenemos el detalle de usuario desde el servicio invocado
		final UserInvk user = UserInvk.load(username);
		
		
		
		/* Componemos el entorno de AvatarBuilder para el usuario
		 * FIXME Será la manera más elegante de hacerlo pero a mi no me convence un pijo hacerlo aqui
		 */
		ensureAvatarEnviroment(user);
		
		
		
		//Rellenamos el objeto usuario de Spring Security

		//Datos del usuario
		final Map<String, String> attributes = user.asAttributes();
		
		//Grupo y roles (acciones) de usuario
		final Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>(); {
			if (FunctionUtils.isNotBlank(user.getRoleName())) {
				authorities.add(new GrantedAuthorityImpl("ROLE_" + user.getRoleName()));
			}
		}

		//Crear informacion del usuario
		final UserDetails userDetails = new User(user.getId().toString(), authorities, attributes);
		return userDetails;
	}
	
	
	/**
	 * Se asegura de que el entorno del AvatarBuilder existe y es correcto
	 * y si no, lo crea correctamente
	 * 
	 * @param user usuario para el que se chequea el entorno
	 * @throws DataAccessException si se produce algun fallo durante el proceso
	 */
	private void ensureAvatarEnviroment(
			final UserInvk user)
		throws DataAccessException 
	{
		try 
		{	/* El siguiente segmento de código crea el arbol de directorios y los sparks disponibles  
			 * del AvatarBuilder mediante una llamada al servicio de negocio CheckUserDirectories */
			ContextoOperacion contextoOperacion = new ContextoOperacion();
			contextoOperacion.putDato("0", user.getId(), ContextoOperacion.Numero);
			/* IContexto[] contextos = */ FawnaInvoker.getInstance().invoke("029", "014", contextoOperacion);
		} catch (FawnaInvokerException e) {
			throw new ServiceDataAccessException("029", "014", "AvatarBuilderCompositer has failed when build your filesystem enviroment.", e);
		} catch (PersistenciaException e) {
			throw new ServiceDataAccessException("029", "014", "AvatarBuilderCompositer has failed when build your filesystem enviroment.", e);
		}
		
		try 
		{	/* El siguiente segmento de código comprueba la consistencia de la base de datos
		 	 * mediante una llamada al servicio de negocio CheckUserBBDD */
			ContextoOperacion contextoOperacion = new ContextoOperacion();
			contextoOperacion.putDato("0", user.getId(), ContextoOperacion.Numero);
			/* IContexto[] contextos = */ FawnaInvoker.getInstance().invoke("029", "069", contextoOperacion);
		} catch (FawnaInvokerException e) {
			throw new ServiceDataAccessException("029", "069", "AvatarBuilderCompositer has failed when checking the BBDD.", e);
		} catch (PersistenciaException e) {
			throw new ServiceDataAccessException("029", "069", "AvatarBuilderCompositer has failed when checking the BBDD.", e);
		}

		try 
		{	/* El siguiente segmento de código hace una llamada al servicio de negocio 
			 * GetSparksByUser que compone el entorno del AvatarBuilder */
			ContextoOperacion contextoOperacion = new ContextoOperacion();
			contextoOperacion.putDato("0", user.getId(), ContextoOperacion.Numero);
			IContexto[] contextos = null;
			contextos = FawnaInvoker.getInstance().invoke("029", "007", contextoOperacion);
			if (contextos==null || contextos.length == 0) {
				throw new ServiceDataAccessException("029", "007", "AvatarBuilderCompositer has failed making your model enviroment.", 
						new IllegalArgumentException("Empty output data"));
			}
		} catch (FawnaInvokerException e) {
			throw new ServiceDataAccessException("029", "007", "AvatarBuilderCompositer has failed when build your model enviroment.", e);
		} catch (PersistenciaException e) {
			throw new ServiceDataAccessException("029", "007", "AvatarBuilderCompositer has failed when build your model enviroment.", e);
		}
	}


}








/**
 * Clase para cargar el detalle de usuario desde el servicio de negocio correspondiente 
 * @author adele
 *
 */
final class UserInvk 
	implements 	LogonConstants
{
	/** Identificador de usuario */
	private final Number id;
	/** Nombre de usuario */
	private final String name;
	/** Apellidos de usuario */
	private final String surname;
	/** Nombre+apellidos */
	private final String fullname;
	/** Direccion de correo de usuario */
	private final String email;
	/** Direccion de correo de usuario transformando @ por (at) */
	private final String emailId;
	/** Password de usuario */
	private final String password;
	/** Identificador de la entidad de usuario */
	private final Number entidadId;
	/** Nombre de la entidad de usuario */
	private final String entidadName;
	/** Identificador del tipo de cuenta de usuario */
	private final Number cuentaId;
	/** Nombre del tipo de cuenta de usuario */
	private final String cuentaName;
	/** Estado del usuario */
	private final String status;
	/** Numero de tarjeta de credito */
	private final Number creditcardNumber;
	/** Fecha de caducidad  de tarjeta de credito */
	private final String creditCardExpiration;
	/** Codigo de confirmacion de registro */
	private final String signupCode;
	/** Alias de usuario */
	private final String username;
	/** Display name de usuario */
	private final String displayname;
	/** AvatarBuilder userpath */
	private final String avatarBuilderUmd5;
	/** RoleGroup. id */
	private final Number roleId;
	/** RoleGroup. Unique name/label */
	private final String roleName;
	/** Titulo del usurio */
	private final String title;
	/** Flag('0' | '1') indicando el tipo de entidad del usurio */
	private final String flagEntity;
	/** Acuerdo de facturación entre FIONA y el usuario */
	private final String strBillingId;
	/** Método de pago de la cuenta elegido por el usuario */
	private final String strPagoAnual;
	/** Crédito que le queda al usuario en la cuenta */
	private final Number numAccountCredit;
	
	
	protected UserInvk(
			Number id, String name, String surname,
			String email, String emailId, 
			String password, 
			Number entidadId, String entidadName, 
			Number cuentaId, String cuentaName, 
			String status, 
			Number creditCardNumber, String creditcardExpiration, 
			String signupCode, 
			String username, String fullname, String displayname, 
			String avatarBuilderUmd5,
			Number roleId, String roleName,
			String title, String flagEntity,
			String strBillingId, String strPagoAnual, Number numAccountCredit) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.emailId = emailId;
		this.password = password;
		this.entidadId = entidadId;
		this.entidadName = entidadName;
		this.cuentaId = cuentaId;
		this.cuentaName = cuentaName;
		this.status = status;
		this.creditcardNumber = creditCardNumber;
		this.creditCardExpiration = creditcardExpiration;
		this.signupCode = signupCode;
		this.username = username;
		this.fullname = fullname;
		this.displayname = displayname;
		this.avatarBuilderUmd5 = avatarBuilderUmd5;
		this.roleId = roleId;
		this.roleName = roleName;
		this.title = title;
		this.flagEntity = flagEntity;
		this.strBillingId = strBillingId;
		this.strPagoAnual = strPagoAnual;
		this.numAccountCredit = numAccountCredit;
	}
	public Number getId() { return id; }
	public String getName() { return name; }
	public String getSurname() { return surname; }
	public String getEmail() { return email; }
	public final String getEmailId() { return emailId; }
	public String getPassword() { return password; }
	public Number getEntidadId() { 	return entidadId; }
	public String getEntidadName() { 	return entidadName; }
	public Number getCuentaId() { return cuentaId; }
	public String getCuentaName() { return cuentaName; }
	public String getStatus() { return status; }
	public Number getCreditcardNumber() { return creditcardNumber; }
	public String getCreditCardExpiration() { return creditCardExpiration; }
	public String getUsername() { return username; }
	public String getDisplayname() { return displayname; }
	public String getFullname() { return fullname; }
	public String getAvatarBuilderUmd5() { return avatarBuilderUmd5; }
	public String getSignupCode() { return signupCode; }
	public Number getRoleId() { return roleId; }
	public String getRoleName() { return roleName; }
	public String getTitle() { return title; }
	public String getFlagEntity() { return flagEntity; }
	public String getUserConfig() {
		String config = "0";
		final File usersPath = ConfigUtils.getNfsUsersFolder();
		String mailmd5 = getAvatarBuilderUmd5();
		String[] command = {
				"/bin/sh",
				"-c",
				//"file " + usersPath + "/private/" + mailmd5 + " | cut -d\'_\' -f2 | cut -d\"'\" -f1"
				"file " + usersPath + "/private/" + mailmd5 + " | tail -c3 | head -c1"
				};
		try {
			Process configDir = Runtime.getRuntime().exec(command);
			BufferedReader in;
			if(configDir.waitFor() != 0) {
				// No debería ocurrir nunca porque previamente se comprobó el sistema de ficheros
				in = new BufferedReader( new InputStreamReader(configDir.getErrorStream()) );
				String response;
				while ((response = in.readLine()) != null) {
					System.out.println(response);
				}
				String cmd = "unlink " + usersPath + "/private/" + mailmd5;
				Process changeDir = Runtime.getRuntime().exec(cmd);
				changeDir.waitFor();
				cmd = "ln -s " + usersPath + "/private/" + mailmd5 + "_0 " + usersPath + "/private/" + mailmd5;
				changeDir = Runtime.getRuntime().exec(cmd);
				if(changeDir.waitFor() != 0) {
					in = new BufferedReader( new InputStreamReader(changeDir.getErrorStream()) );
					while ((response = in.readLine()) != null) {
						System.out.println(response);
					}
				}
			}
			else {
				in = new BufferedReader( new InputStreamReader(configDir.getInputStream()) );
				config=in.readLine();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return config;
	}
	public String getStrBillingId() {	return strBillingId;	}
	public String getStrPagoAnual() {	return strPagoAnual;	}
	public Number getNumAccountCredit() { return numAccountCredit; }
	
	
	public Map<String, String> asAttributes() {
		final Map<String, String> attributes = new LinkedHashMap<String, String>(25);
		attributes.put("SECURE_USERID", FunctionUtils.toString(getId(), null));
		attributes.put("SECURE_USER_FISTNAME", getName());
		attributes.put("SECURE_USER_SURNAME", getSurname());
		attributes.put("SECURE_USERMAIL", getEmail());
		attributes.put("SECURE_USERMAIL_ID", getEmailId());
		attributes.put("SECURE_USER_PASSWORD", getPassword());
		attributes.put("SECURE_USER_ENTITY_ID", FunctionUtils.toString(getEntidadId(), null));
		attributes.put("SECURE_USER_ENTITY_NAME", getEntidadName());
		attributes.put("SECURE_USER_ACCOUNTTYPE_ID", FunctionUtils.toString(getCuentaId(), null));
		attributes.put("SECURE_USER_ACCOUNTTYPE_NAME", getCuentaName());
		attributes.put("SECURE_USER_STATUS", getStatus());
		attributes.put("SECURE_USER_CREDITCARD_NUMBER", FunctionUtils.toString(getCreditcardNumber(), null));
		attributes.put("SECURE_USER_CREDITCARD_EXPIRATION", getCreditCardExpiration());
		attributes.put("SECURE_USER_SIGNUPCODE", getSignupCode());
		attributes.put("SECURE_USER_NICKNAME", getUsername());
		attributes.put("SECURE_USER_FULLNAME", getFullname());
		attributes.put("SECURE_USER_DISPLAYNAME", getDisplayname());
		attributes.put("SECURE_USERMAILD5", getAvatarBuilderUmd5());
		attributes.put("SECURE_USER_USERGROUP", getRoleName());
		attributes.put("SECURE_USER_USERGROUP_ID", FunctionUtils.toString(getRoleId(), null));
		attributes.put("SECURE_USER_TITLE", getTitle());
		attributes.put("SECURE_USER_FLAG_ENTITY", getFlagEntity());
		attributes.put("USER_CONFIG", getUserConfig());
		attributes.put("SECURE_USER_BILLING_AGREEMENT_ID", getStrBillingId());
		attributes.put("SECURE_USER_ACCOUNTTYPE_METHOD", getStrPagoAnual());
		attributes.put("SECURE_USER_ACCOUNT_CREDIT", FunctionUtils.toString(getNumAccountCredit()));

		boolean hasCert = SecurityContextHolder.getContext().getAuthentication() == null || SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof X509Certificate;
		attributes.put("SECURE_FLAG_CERT", hasCert ? "1" : "0");
		
		return attributes;
	}


	/**
	 * Obtiene el detalle de usuario dado un uuid de logon
	 * <p>
	 * Normalmente el logon es el username pero puede ser su email
	 * 
	 * @param IUU identificador de usuario para logon
	 * @return 
	 * @throws UsernameNotFoundException si no existe detalle de usuario para ese uuid
	 * @throws DataAccessException Si se produce un error al invocar el servicio
	 */
	static final UserInvk load(
			final String IUU) 
		throws UsernameNotFoundException, DataAccessException 
	{
		/* Segun la documentacion de Spring Security UserDetailsService: 
		 *  - si no se encuentra el usuario o no tiene roles hay que lanzar una UsernameNotFoundException.
		 *  - si no se puede determinar dichos detalles por error de operacion una DataAccessException. 
		 *  	En nuestro caso cuando hay mas de un registro (subtipo IncorrectResultSizeDataAccessException)
		 */
		
		//precheck para que no lance una excepcion horrible 
		//porque cuando SN029003CO1O (queryMode) es "L"= QueryByLogon, SN029003CO2O (logon) es obligatorio
		if (FunctionUtils.isBlank(IUU)) {
			throw new UsernameNotFoundException("Username is required.", IUU);
		}
		
		
		IContexto[] contextos = null;
		//ejecutamos el SN para obtener el detalle del usuario
		try {
			ContextoOperacion contextoOperacion = new ContextoOperacion();
			contextoOperacion.putDato("0", "L", ContextoOperacion.Cadena);
			contextoOperacion.putDato("1", IUU, ContextoOperacion.Cadena);
			contextos = FawnaInvoker.getInstance().invoke("029", "003", contextoOperacion);
		} catch(FawnaInvokerException e) {
			final Throwable cause = e.getCause();
			final Throwable rootCause = ExceptionUtils.getRootCause(e);
			// error funcional al obtener el detalle. Unwrap excepcion original de negocio para saber la causa
			if (com.treelogic.fawna.presentacion.core.exception.ExceptionUtils.ARCH_TYPE_ERROR_EXCP_FUNC.equals(e.getErrorInfo().getTipo())
					&& rootCause instanceof ErrorOperacionException) {
				final ErrorOperacionException ex = (ErrorOperacionException) rootCause;
				
				//Tratar codigos de error del logon a Spring Security exceptions
				int code = 0; {
					Integer exCode = ex.getCodigo();
					if (exCode!=null) code = exCode.intValue();
				}
				if (LogonErrorCode.ERR525__NO_SUCH_USER.code == code) {
					throw new UsernameNotFoundException(LogonErrorCode.ERR525__NO_SUCH_USER.msg, IUU);
				}
				throw new ServiceDataAccessException("029", "003", e.getMessage(), e);
			}
			throw new ServiceDataAccessException("029", "003", "Retrieving IUU user data.", e);
		} catch(PersistenciaException e) {
			throw new ServiceDataAccessException("029", "003", "Retrieving IUU user data.", e);
		}
		//Si no se retorna nada entonces es que no existe un usuario con ese UUID
		if (contextos==null || contextos.length == 0) {
			throw new UsernameNotFoundException(LogonErrorCode.ERR525__NO_SUCH_USER.msg, IUU);
		} else {
			if (contextos.length != 1 ) {
				throw new IncorrectResultSizeDataAccessException(1, contextos.length);
			}
			
			//Todo ha ido bien si se puede rellenar el bean...
			try {
				final IContexto d = contextos[0];
				String isPagoAnual = d.getString("FIONEG001190");
				String accountPayPeriod = null;
				if(isPagoAnual != null && !"".equals(isPagoAnual)){
					/**if(isPagoAnual.equals("1"))
						accountPayPeriod = "2";
					else
						accountPayPeriod = "1";*/
					accountPayPeriod = isPagoAnual;
				}
				return new UserInvk(
						d.getBigDecimal("FIONEG001010"), 	//id
						d.getString("FIONEG001020"), 		//nombre
						d.getString("FIONEG001030"), 		//apellidos
						d.getString("FIONEG001040"), 		//email
						d.getString("FIONEG001041"), 		//email id
						d.getString("FIONEG001050"), 		//password
						d.getBigDecimal("FIONEG001060"), 	//id entidad
						d.getString("FIONEG001061"), 		//nombre entidad
						d.getBigDecimal("FIONEG001070"), 	//id tipo cuenta
						d.getString("FIONEG001071"), 		//nombre tipo cuenta
						d.getString("FIONEG001080"), 		//status
						d.getBigDecimal("FIONEG001090"), 	//num tarjeta credito
						d.getString("FIONEG001100"), 		//fecha caducidad tarjeta credito
						d.getString("FIONEG001110"), 		//registration confirmation code
						d.getString("FIONEG001120"),		//username
						d.getString("FIONEG001121"),		//fullname
						d.getString("FIONEG001122"),		//displayname
						d.getString("FIONEG001130"),		//AvatarBuilder userpath
						d.getBigDecimal("FIONEG001140"),	//RoleGroup. Id
						d.getString("FIONEG001141"),		//RoleGroup. Unique name/label 
						d.getString("FIONEG001150"),		//titulo 
						d.getString("FIONEG001160"),		//flagEntidad
						d.getString("FIONEG001200"),		//strBillingId 
						accountPayPeriod,					//strPagoanual
						d.getBigDecimal("FIONEG001210")
					);
			} catch (Exception e) {
				throw new ServiceDataAccessException("029", "003", "Retrieving IUU user data.", e);
			}
		}
	}
}
