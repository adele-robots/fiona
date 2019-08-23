package com.adelerobots.clineg.manager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.adelerobots.clineg.engine.UsuarioEng;
import com.adelerobots.clineg.entity.CuentaC;
import com.adelerobots.clineg.entity.EntidadC;
import com.adelerobots.clineg.entity.RoleUsuarioC;
import com.adelerobots.clineg.entity.UserStatusEnum;
import com.adelerobots.clineg.entity.UsuarioC;
import com.adelerobots.clineg.util.FunctionUtils;
import com.adelerobots.clineg.util.keys.ConstantesError;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;


public class UsuariosManager 
{

	private String conexion;	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(UsuariosManager.class);
	

	public UsuariosManager(String conexion) {
		super();
		this.conexion = conexion;		
	}



	/**
	 * Obtiene todos los usuarios registrados en la aplicacion
	 * @see UsuarioEng#findAll()
	 */
	public Collection<UsuarioC> findAll()
	{
		final UsuarioEng dao = new UsuarioEng(conexion);
		final Collection<UsuarioC> col = dao.findAll();
		return col;
	}

	/**
	 * Obtiene el usuario por su uid de logon (email o username)
	 * @param logonUID
	 * @see UsuarioEng#findByLogon(String)
	 */
	public UsuarioC findByLogon(
			final String logonUID)
	{
		final UsuarioEng dao = new UsuarioEng(conexion);
		final UsuarioC ent = dao.findByLogon(logonUID);
		return ent;
	}

	/**
	 * Obtiene el usuario por su email
	 * @param email
	 * @see UsuarioEng#findByEmail(String)
	 */
	public UsuarioC findByEmail(
			final String email)
	{
		final UsuarioEng dao = new UsuarioEng(conexion);
		final UsuarioC ent = dao.findByEmail(email);
		return ent;
	}

	/**
	 * Obtiene el usuario por su username / nickname
	 * @param nickname
	 * @see UsuarioEng#findByUsername(String)
	 */
	public UsuarioC findByUsername(
			final String nickname)
	{
		final UsuarioEng dao = new UsuarioEng(conexion);
		final UsuarioC ent = dao.findByUsername(nickname);
		return ent;
	}

	/**
	 * Obtiene el usuario por su identificador interno
	 * @param id
	 * @see UsuarioEng#findById(Integer)
	 */
	public UsuarioC findById(
			final Integer id)
	{
		if (id == null) return null;
		final UsuarioEng dao = new UsuarioEng(conexion);
		final UsuarioC ent = dao.findById(id);
		return ent;
	}

	/**
	 * Obtiene el usuario por su identificador interno
	 * @param id
	 * @see UsuarioEng#findById(Integer)
	 */
	public final UsuarioC findById(
			final BigDecimal id)
	{
		if (id == null) return null;
		final UsuarioC ent = this.findById(new Integer(id.intValue()));
		return ent;
	}

	/**
	 * 
	 * 
	 * @param id identificador del usuario a buscar
	 * @param name nombre de usuario
	 * @param surname apellidos de usuario
	 * @param email correo electronico
	 * @param passwd password
	 * @param status estado del usuario
	 * @param entity entidad
	 * @param accountType tipo de cuenta
	 * @param cardNum numero de tarjeta de credito
	 * @param cardExp caducidad de tarjeta de credito
	 * @param signupCode codigo de validacion de signup
	 * @param username nickname de usuario
	 * @param role grupo de rol al que peternece
	 * @param title titulo del usuario
	 * @param flagEntity flag('0' | '1') que indica el tipo de entidad del usurio
	 * @return
	 */
	public UsuarioC create(
			final String name, final String surname, 
			final String email, final String passwd, 
			final EntidadC entity, final CuentaC accountType, 
			final Integer cardNum, final String cardExp, 
			final String signupCode, final String username, 
			final RoleUsuarioC role, final String title,
			final String flagEntity, final Character isPagoAnual) 
	{
		final UsuarioEng dao = new UsuarioEng(conexion);
		final UsuarioC ent = new UsuarioC(
									name,surname,
									email,passwd, null,
									null, null,
									cardNum, cardExp, 
									signupCode, username);
		ent.setEntidad(entity);
		ent.setCuenta(accountType);
		ent.setRole(role);	
		
		
		/* A los usuarios no se les permite acceder a la plataforma
		 * si no han pinchado en el enlace que se les envia al email proporcionado
		 * para confirmar que son realmente quienes dicen ser */ 
		ent.setStatus(UserStatusEnum.UNCONFIRMED);
		// TODO: Revisar
		ent.setIntNumProcesos(new Integer(2));
		
		//ent.setChCancelada(new Character('0'));
		
		/* XXX El campo AvatarBuilder encoded userpath en BBDD, 
		 * solo se establece cuando se registra un usuario 
		 * y nunca debería ser modificado. Se hace codificando el mail. */
		if (email == null) {
			ent.setAvatarBuilderUmd5(null);
		} else {
			ent.setAvatarBuilderUmd5(FunctionUtils.toAvatarBuilderUmd5(email));
		}
		
		
		
		dao.create(ent);
		return ent;
	}

	/**
	 * Actualiza los datos del usuario
	 * 
	 * @param id identificador del usuario a buscar
	 * @param name nombre de usuario
	 * @param surname apellidos de usuario
	 * @param email correo electronico
	 * @param passwd password
	 * @param status estado del usuario
	 * @param entity entidad
	 * @param accountType tipo de cuenta
	 * @param cardNum numero de tarjeta de credito
	 * @param cardExp caducidad de tarjeta de credito
	 * @param signupCode codigo de validacion de signup
	 * @param username nickname de usuario
	 * @param role grupo de rol al que peternece
	 * @param title titulo del usuario
	 * @param flagEntity flag('0' | '1') que indica el tipo de entidad del usurio
	 * @return
	 */
	public final UsuarioC update(
			final Integer id,
			final String name, final String surname, 
			final String email, final String passwd, final UserStatusEnum status, 
			final EntidadC entity, final CuentaC accountType, 
			final Integer cardNum, final String cardExp, 
			final String signupCode, final String username, 
			final RoleUsuarioC role, final String title,
			final String flagEntity, final Character isPagoAnual) 
	{
		final UsuarioEng userDAO = new UsuarioEng(conexion);
		final UsuarioC user = userDAO.findById(id);
		if (user != null) {
			if(name != null) user.setName(name);
			/*if(surname != null) */user.setSurname(surname);
			if(email != null) user.setEmail(email);
			if(passwd != null) user.setPassword(passwd);
			if(status != null) user.setStatus(status);
			EntidadC.moveUsuarios(user, entity); //Move user to the new entidad if needed
			CuentaC.moveUsuarios(user, accountType); //Move user to the new accountType if needed			
			if(cardNum != null) user.setCreditCardNumber(cardNum);
			if(cardExp != null) user.setCreditCardExpiration(cardExp);
			if(signupCode != null) user.setSignupCode(signupCode);
			if(username != null) user.setUsername(username);			
			
			RoleUsuarioC.moveUsuarios(user, role); //Move user to the new role if needed
			//Mark for update
			userDAO.update(user);
			return user;
		}
		return user;
	}
	
	/**
	 * Método que permite actualizar la contraseña de un usuario
	 * @param intCodUsuario Identificador único del usuario
	 * @param password Password (en MD5) del usuario
	 */
	public void updateUserPassword(Integer intCodUsuario, String password){
		final UsuarioEng userDAO = new UsuarioEng(conexion);
		final UsuarioC user = userDAO.findById(intCodUsuario);
		user.setPassword(password);
		
		userDAO.update(user);
		userDAO.flush();		
	}

	/**
	 * Inspects for user email duplicates
	 * @param userId the id to check (this user will be ignored on process), <code>null</code> to ignore this search param
	 * @param email the email to check
	 * @return
	 */
	public boolean isEmailTaken(Integer userId, String email) {
		Collection<String> c = getAllEmails(userId, email);
		return c != null && !c.isEmpty();
	}

	/**
	 * Inspects for user nickname duplicates
	 * @param userId the id to check (this user will be ignored on process), <code>null</code> to ignore this search param
	 * @param username the username/nickname to check
	 * @return
	 */
	public boolean isUsernameTaken(Integer userId, String username) {
		Collection<String> c = getAllUsernames(userId, username);
		return c != null && !c.isEmpty();
	}

	/**
	 * Retrieve the user emails by that criteria
	 * @param userId the id to check (this user will be ignored on process), <code>null</code> to ignore this search param
	 * @param email the email to search, <code>null</code> to ignore this search param
	 * @return
	 */
	public Collection<String> getAllEmails(Integer userId, String email) {
		UsuarioEng usuarioDao = new UsuarioEng(conexion);
		return usuarioDao.getAllEmails(userId, email);
	}

	/**
	 * Retrieve all user emails
	 * @return
	 */
	public Collection<String> getAllEmails() {
		UsuarioEng usuarioDao = new UsuarioEng(conexion);
		return usuarioDao.getAllEmails(null, null);
	}

	/**
	 * Retrieve the user nicknames by that criteria
	 * @param userId the id to check (this user will be ignored on process), <code>null</code> to ignore this search param
	 * @param username the nickname to search, <code>null</code> to ignore this search param
	 * @return
	 */
	public Collection<String> getAllUsernames(Integer userId, String username) {
		UsuarioEng usuarioDao = new UsuarioEng(conexion);
		return usuarioDao.getAllUsernames(userId, username);
	}

	/**
	 * Retrieve all user nicknames
	 * @return
	 */
	public Collection<String> getAllUsernames() {
		UsuarioEng usuarioDao = new UsuarioEng(conexion);
		return usuarioDao.getAllUsernames(null, null);
	}

	
	public UsuarioC getUsuario(Integer idUsuario) throws Exception{
		UsuarioEng usuarioDao = new UsuarioEng(conexion);
		UsuarioC usuario = usuarioDao.findById(idUsuario);
		if (usuario == null)
			throw new Exception(ConstantesError.ERROR_DETALLE_USUARIO_INEXISTENTE);
		return usuario;
	}
	
	public UsuarioC confirmarUsuario(Integer cnUsuario, String signupCode) throws Exception {
		UsuarioC usuario = getUsuario(cnUsuario);
		if (FunctionUtils.equals(usuario.getSignupCode(), signupCode))
		{	/* Si el usuario no ha confirmado su cuenta todavia cambiamos dicho flag
			 * Comprobamos antes por si da n-clicks en el enlace y el campo tiene otro estado */
			//if (usuario.isUnconfirmed() || usuario.isEmailConfirmed()) {
			if (usuario.isEmailConfirmed()) {
				//usuario.setStatus(null); //Cuando hage logon, indica es su primera vez.
				usuario.setStatus(UserStatusEnum.OFFLINE); //Indicador normal
			}
		}
		else
			throw new Exception(ConstantesError.ERROR_DETALLE_CONFIRMACION_ERRONEA);
		return usuario;
	}
	
	/**
	 * Método que cambia el estado del usuario a "email_confirmed"
	 * @param cnUsuario Identificador único del usuario
	 * @param signupCode Código de confirmación generado en el registro
	 * @return
	 * @throws Exception
	 */
	public UsuarioC confirmarEmailUsuario(Integer cnUsuario, String signupCode) throws Exception {
		UsuarioC usuario = getUsuario(cnUsuario);
		if (FunctionUtils.equals(usuario.getSignupCode(), signupCode))
		{	/* Si el usuario no ha confirmado su cuenta todavia cambiamos dicho flag
			 * Comprobamos antes por si da n-clicks en el enlace y el campo tiene otro estado */
			if (usuario.isUnconfirmed()) {				
				usuario.setStatus(UserStatusEnum.EMAIL_CONFIRMED); 
			}
		}
		else
			throw new Exception(ConstantesError.ERROR_DETALLE_CONFIRMACION_ERRONEA);
		return usuario;
	}
	

	
	
	/**
	 * Método que permite actualizar el tipo de cuenta de un usuario
	 * 
	 * @param usuario Usuario cuyo tipo de cuenta se quiere actualizar
	 * @param cuenta Objeto que representa el nuevo tipo de cuenta que se
	 * le quiere asignar al usuario
	 * 
	 */
	public void updateUserAccountTypeId(UsuarioC usuario, CuentaC cuenta, Integer intFlagResetearCredito){
		final UsuarioEng usuarioDAO = new UsuarioEng(conexion);
		
		if(usuario==null || cuenta == null)
			return;
		
		usuario.setCuenta(cuenta);
				
		usuarioDAO.update(usuario);
		usuarioDAO.flush();
	}
	
				
			
	/**
	 * Método que encuentra los usuarios con cuenta cancelada cuyo
	 * email coincida con el pasado como parámetro
	 * 
	 * @param email Email del usuario buscado
	 * @return
	 */
	public List<UsuarioC> findCanceledUsersByEmail(String email){
		UsuarioEng usuarioDAO = new UsuarioEng(conexion);
		
		return usuarioDAO.findCanceledUsersByEmail(email);
	}
	
	
	
}
