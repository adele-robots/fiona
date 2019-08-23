package com.adelerobots.clineg.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.adelerobots.clineg.util.FunctionUtils;


/**
 * Define los atributos de un usuario de la plataforma
 * 
 * @author adele
 */
@Entity(name = "usuario")
@Table(name = "usuario", uniqueConstraints = {
		@javax.persistence.UniqueConstraint(columnNames = "dc_email"), 
		@javax.persistence.UniqueConstraint(columnNames = "dc_username")})
@org.hibernate.annotations.Proxy(lazy = true)
public class UsuarioC extends com.treelogic.fawna.arq.negocio.persistencia.datos.DataC {



	/**
	 * Constructor por defecto
	 */
	public UsuarioC() {
		super();
	}

	/**
	 * Constructor de la clase
	 */
	public UsuarioC(String name, String surname, String email, String password,
			Integer intEntidad, Integer intCuenta, String strStatus, 
			Integer creditCardNumber, String creditCardExpiration, 
			String signupCode, String username) 
	{
		super();
		setName(name);
		setSurname(surname);
		setEmail(email);
		setPassword(password);
		setEntidadId(intEntidad);
		setCuentaId(intCuenta);
		setStrStatus(strStatus);
		setCreditCardNumber(creditCardNumber);
		setCreditCardExpiration(creditCardExpiration);
		setSignupCode(signupCode);
		setUsername(username);
	}




	/**
	 * Hashcode de objetos persistentes para colecciones que usen hash.
	 * Complementa el metodo {@link #equals(Object)} <br/> En persistencia dos
	 * objetos son iguales si tienen la misma Primary Key
	 * 
	 * @see java.lang.Object#hashCode()
	 * @return entero que representa el codigo hash del objeto
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getCnUsuario() == null) ? 0 : getCnUsuario().hashCode());
		return result;
	}

	/**
	 * Comparador de igualdad para objetos persistentes. <br/> En persistencia
	 * dos objetos son iguales si tienen la misma Primary Key
	 * 
	 * @param obj
	 *            Objeto a comparar
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @return verdadero si son iguales, falso si son distintos
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioC other = (UsuarioC) obj;
		if (getCnUsuario() == null) {
			if (other.getCnUsuario() != null)
				return false;
		} else if (!getCnUsuario().equals(other.getCnUsuario()))
			return false;
		return true;
	}





	/** Campo identificador */
	private Integer cnUsuario;
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_usuario", nullable = false)
	public Integer getCnUsuario() { return cnUsuario; }
	public void setCnUsuario(Integer cnUsuario) { this.cnUsuario = cnUsuario; }
	@Transient public BigDecimal getCnUsuarioAsBd() { 
		if (cnUsuario == null) return null;
		return new BigDecimal(cnUsuario.toString());
	}



	/** Campo nombre */
	private String name;
	@Column(name = "dc_nombre", nullable = true, length = 100)
	public String getName() { 	return name; }
	public void setName(String name) { this.name = name; }



	/** Campo surname / apellidos */
	private String surname;
	@Column(name = "dc_apellido", nullable = true, length = 200)
	public String getSurname() { return surname; }
	public void setSurname(String surname) { this.surname = surname; }



	/** Campo fullname (name+surname) */
	@Transient public String getFullname() {
		if (name == null && surname == null) return null;
		StringBuffer buff = new StringBuffer();
		buff.append(FunctionUtils.defaultIfBlank(name, ""));
		if (!FunctionUtils.isBlank(username) && !FunctionUtils.isBlank(surname)) {
			buff.append(" ");
		}
		buff.append(FunctionUtils.defaultIfBlank(surname, ""));
		return buff.toString();
	}



	/** Campo displayname (fullname/username/email-id depending of which are empty) */
	@Transient public String getDisplayname() {
		String s = null;
		s = getUsername();
		if (!FunctionUtils.isBlank(s)) return s;
		s = getFullname();
		if (!FunctionUtils.isBlank(s)) return s;
		s = getEmailId();
		if (!FunctionUtils.isBlank(s)) return s;
		return s;
	}



	/** Campo email */
	private String email;
	@Column(name = "dc_email", nullable = false, unique=true, length = 100)
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email;
		/* TODO Cuando se habilite el campo AvatarBuilder encoded userpath en BBDD, 
		 * eliminar el bloque de codigo siguiente */
		if (email == null) {
			setAvatarBuilderUmd5(null);
		} else {
			setAvatarBuilderUmd5(FunctionUtils.toAvatarBuilderUmd5(email));
		}
	}
	
	

	/** Campo email-id */
	@Transient public String getEmailId() { 
		if (email == null) return null;
		return email.replace("@", "(at)");
	}





	/** Campo AvatarBuilder encoded userpath (antiguo maild5 que arrastrabamos por toda la aplicacion) */
	/* TODO Cuando se habilite el campo AvatarBuilder encoded userpath en BBDD, 
	 * descomentar el @Column y quitar @Transient para mapear el campo */
	private String avatarBuilderUmd5;
	//@Column(name = "dc_avatarumd5", nullable = false, unique=true, length = 100)
	@Transient public String getAvatarBuilderUmd5() { return avatarBuilderUmd5; }
	public void setAvatarBuilderUmd5(String umd5) { this.avatarBuilderUmd5 = umd5; }



	/** Campo password */
	private String password;
	@Column(name = "dc_password", nullable = false, length = 300)
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }



	/** Campo Entidad **/
	private EntidadC entidad;
	@ManyToOne (fetch=FetchType.LAZY, targetEntity = EntidadC.class)
	@JoinColumn(name = "cn_entidad", insertable=false, updatable=true)
	@org.hibernate.annotations.LazyToOne(org.hibernate.annotations.LazyToOneOption.PROXY)
	public EntidadC getEntidad() { return entidad; }
	public void setEntidad(EntidadC entidad) { this.entidad = entidad; }
	/** Persistence delegated method to setter access basic entidad.cnEntidad property */
	@Transient public Integer getEntidadId() { 
		final EntidadC bean = getEntidad();
		if (bean == null) return null;
		return bean.getId();
	}
	@Transient public BigDecimal getEntidadIdAsBd() { 
		final EntidadC bean = getEntidad();
		if (bean == null) return null;
		return bean.getCnEntidadAsBd();
	}
	/** Persistence delegated method to setter access basic entidad.id property */
	protected void setEntidadId(Integer entidadId) { 
		if (getEntidad() == null) setEntidad(new EntidadC());
		getEntidad().setId(entidadId);
	}
	/** Persistence delegated method to getter access basic entidad.nombre property */
	@Transient public String getEntidadName() {
		final EntidadC bean = getEntidad();
		if (bean == null) return null;
		return bean.getNombre();
	}



	/** Campo Cuenta **/
	private CuentaC cuenta;
	@ManyToOne (fetch=FetchType.LAZY, targetEntity = CuentaC.class)
	@JoinColumn(name = "cn_cuenta", updatable=true)
	@org.hibernate.annotations.LazyToOne(org.hibernate.annotations.LazyToOneOption.PROXY)
	public CuentaC getCuenta() { return cuenta; }
	public void setCuenta(CuentaC cuenta) { this.cuenta = cuenta; }
	/** Persistence delegated method to getter access basic cuenta.id property */
	@Transient public Integer getCuentaId() { 
		final CuentaC bean = getCuenta();
		if (bean == null) return null;
		return bean.getId();
	}
	@Transient public BigDecimal getCuentaIdAsBd() { 
		final CuentaC bean = getCuenta();
		if (bean == null) return null;
		return bean.getIdAsBd();
	}
	/** Persistence delegated method to setter access basic cuenta.id property */
	protected void setCuentaId(Integer cuentaId) { 
		if (getCuenta() == null) setCuenta(new CuentaC());
		getCuenta().setId(cuentaId);
	}
	/** Persistence delegated method to getter access basic cuenta.nombre property */
	@Transient public String getCuentaName() {
		final CuentaC bean = getCuenta();
		if (bean == null) return null;
		return bean.getNombre();
	}



	/** Campo estado usuario */
	private String strStatus;
	@Column(name = "dc_status", nullable = true, length = 50)
	public String getStrStatus() { 	return strStatus; }
	public void setStrStatus(String strStatus) { this.strStatus = strStatus; }



	/** Campo numero tarjeta credito */
	private Integer creditCardNumber;
	@Column(name = "nu_tarjeta", nullable = true, length = 11)
	public Integer getCreditCardNumber() { return creditCardNumber; }
	public void setCreditCardNumber(Integer creditCardNumber) { this.creditCardNumber = creditCardNumber; }
	@Transient public BigDecimal getCreditCardNumberAsBd() { 
		if (creditCardNumber == null) return null;
		return new BigDecimal(creditCardNumber.toString());
	}



	/** Campo fecha caducidad tarjeta credito */
	private String creditCardExpiration;
	@Column(name = "fe_caducidad", nullable = true, length = 10)
	public String getCreditCardExpiration() { return creditCardExpiration; }
	public void setCreditCardExpiration(String creditCardExpiration) { this.creditCardExpiration = creditCardExpiration; }



	/** Campo codigo de confirmacion registro usuario */
	private String signupCode;
	@Column(name = "dc_confirmacion", nullable = true, length = 100)
	public String getSignupCode() { return signupCode; }
	public void setSignupCode(String signupCode) { this.signupCode = signupCode; }



	/** Campo nickname/alias de logon de usuario */
	private String username;
	@Column(name = "dc_username", nullable = false, unique=true, length=50)
	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }
	
	
	/** Campo numero de procesos permitidos para un usuario */
	private Integer intNumProcesos;
	@Column(name = "nu_numprocesos", length = 5)
	public Integer getIntNumProcesos() { return intNumProcesos; }
	public void setIntNumProcesos(Integer intNumProcesos) { this.intNumProcesos = intNumProcesos; }
	



	/** Campo GroupRole **/
	private RoleUsuarioC role;
	@ManyToOne (fetch=FetchType.LAZY, targetEntity = RoleUsuarioC.class)
	@JoinColumn(name = "CN_role", nullable=false, insertable=false, updatable=true)
	@org.hibernate.annotations.LazyToOne(org.hibernate.annotations.LazyToOneOption.PROXY)
	public RoleUsuarioC getRole() { return role; }
	public void setRole(RoleUsuarioC role) { this.role = role; }
	/** Persistence delegated method to getter access basic role.id property */
	@Transient public Integer getRoleId() { 
		final RoleUsuarioC bean = getRole();
		if (bean == null) return null;
		return bean.getId();
	}
	@Transient public BigDecimal getRoleIdAsBd() { 
		final RoleUsuarioC bean = getRole();
		if (bean == null) return null;
		return bean.getIdAsBd();
	}
	/** Persistence delegated method to setter access basic role.id property */
	protected void setRoleId(Integer roleId) { 
		if (getRole() == null) setRole(new RoleUsuarioC());
		getRole().setId(roleId);
	}
	/** Persistence delegated method to getter access basic role.name property */
	@Transient public String getRoleName() {
		final RoleUsuarioC bean = getRole();
		if (bean == null) return null;
		return bean.getName();
	}

	private List<ProcesoC> lstProceso= new LinkedList<ProcesoC>();
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "usuario", targetEntity = ProcesoC.class)	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	public List<ProcesoC> getLstProceso() { return lstProceso; }
	public void setLstProceso(List<ProcesoC> lstProceso) { this.lstProceso = lstProceso; }
	/** Persistence delegated method to add ProcesoC to this user */
	public void addLstProceso(ProcesoC bean) {
		if (bean != null) {
			if (getLstProceso() == null) setLstProceso(new LinkedList<ProcesoC>());
			bean.setUsuario(this);
			bean.setCnUser(this.getCnUsuario());
			getLstProceso().add(bean);
		}
	}
	
	private List<AvatarC> lstAvatars;
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "usuario", targetEntity = AvatarC.class)
	@JoinColumn(name = "CN_USUARIO", nullable = false)
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	public List<AvatarC> getLstAvatars() { return lstAvatars; }
	public void setLstAvatars(List<AvatarC> lstAvatars) { this.lstAvatars = lstAvatars; }
	
	
	

	/* 
	 * =======     Status field accesors 
	 * ========================================================================
	 */


	/**
	 * Obtiene el valor enumerado que representa el valor retornado por {@link #getStrStatus()}
	 * @return
	 * @exception IllegalArgumentException si no existe un enumerado definido para ese valor
	 */
	@Transient public UserStatusEnum getStatus() {
		final String status = getStrStatus();
		return UserStatusEnum.getValue(status);
	}
	
	
	@Transient public void setStatus(UserStatusEnum status) {
		if (status == null) {
			setStrStatus(null);
		} else {
			setStrStatus(status.text);
		}
	}

	/**
	 * Checks if the user is current for first logon. 
	 * This case only happens after users confirm their accounts
	 * @return if status is empty or blank
	 * @see #getStatus()
	 * @see UserStatusEnum#OFFLINE
	 */
	@Transient public boolean isFistLogon() {
		return getStatus() == null;
	}

	/**
	 * Puts the status of the user to {@link UserStatusEnum#OFFLINE}
	 */
	@Transient public UsuarioC putOffline() {
		setStatus(UserStatusEnum.OFFLINE);
		return this;
	}
	
	/**
	 * Checks if the user is current logged-out
	 * @return
	 * @see #getStatus()
	 * @see UserStatusEnum#OFFLINE
	 */
	@Transient public boolean isOffline() {
		return UserStatusEnum.OFFLINE.equals(getStatus());
	}

	/**
	 * Puts the status of the user to {@link UserStatusEnum#ONLINE}
	 */
	@Transient public UsuarioC putOnline() {
		setStatus(UserStatusEnum.ONLINE);
		return this;
	}
	
	/**
	 * Checks if the user is current logged-in
	 * @return
	 * @see #getStatus()
	 * @see UserStatusEnum#ONLINE
	 */
	@Transient public boolean isOnline() {
		return UserStatusEnum.ONLINE.equals(getStatus());
	}
	
	/**
	 * Checks if the user account is not activated yet (need admin confirmation)
	 * @return
	 * @see #getStatus()
	 * @see UserStatusEnum#UNCONFIRMED
	 */
	@Transient public boolean isUnconfirmed() {
		return UserStatusEnum.UNCONFIRMED.equals(getStatus());
	}
	
	/**
	 * Checks if the user email is confirmed
	 * @return
	 * @see #getStatus()
	 * @see UserStatusEnum#EMAIL_CONFIRMED
	 */
	@Transient public boolean isEmailConfirmed() {
		return UserStatusEnum.EMAIL_CONFIRMED.equals(getStatus());
	}
	
	/**
	 * Checks if the user account has been blocked due to, for example, bad use of application
	 * @return
	 * @see #getStatus()
	 * @see UserStatusEnum#ACCOUNT_BLOCKED
	 */
	@Transient public boolean isAccountBlocked() {
		return UserStatusEnum.ACCOUNT_BLOCKED.equals(getStatus());
	}
	
	/**
	 * Checks if the user account has been expired, due to, for example, a test period of application
	 * @return
	 * @see #getStatus()
	 * @see UserStatusEnum#ACCOUNT_EXPIRED
	 */
	@Transient public boolean isAccountExpired() {
		return UserStatusEnum.ACCOUNT_EXPIRED.equals(getStatus());
	}
	
	/**
	 * Check if the user needs to change the password
	 * @return
	 * @see #getStatus()
	 * @see UserStatusEnum#CREDENTIALS_EXPIRED
	 */
	@Transient public boolean isCredentialsExpired() {
		return UserStatusEnum.CREDENTIALS_EXPIRED.equals(getStatus());
	}
	
	/**
	 * Checks if the user account has been disabled, due to, for example, innactivity
	 * @return
	 * @see #getStatus()
	 * @see UserStatusEnum#ACCOUNT_DISABLED
	 */
	@Transient public boolean isAccountDisabled() {
		return UserStatusEnum.ACCOUNT_DISABLED.equals(getStatus());
	}

}
