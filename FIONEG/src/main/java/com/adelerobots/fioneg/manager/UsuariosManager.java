package com.adelerobots.fioneg.manager;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.adelerobots.fioneg.dataclasses.AvatarPrice;
import com.adelerobots.fioneg.dataclasses.FionaComponent;
import com.adelerobots.fioneg.dataclasses.PaymentReceiver;
import com.adelerobots.fioneg.dataclasses.SparkPrice;
import com.adelerobots.fioneg.engine.AvatarEng;
import com.adelerobots.fioneg.engine.AvatarSparkEng;
import com.adelerobots.fioneg.engine.BillingPEng;
import com.adelerobots.fioneg.engine.PriceEng;
import com.adelerobots.fioneg.engine.SparkEng;
import com.adelerobots.fioneg.engine.TransactionDEng;
import com.adelerobots.fioneg.engine.UsuarioConfigEng;
import com.adelerobots.fioneg.engine.UsuarioEng;
import com.adelerobots.fioneg.engine.UsuarioSparkEng;
import com.adelerobots.fioneg.entity.AvatarC;
import com.adelerobots.fioneg.entity.BillingPC;
import com.adelerobots.fioneg.entity.ConfigParamC;
import com.adelerobots.fioneg.entity.CuentaC;
import com.adelerobots.fioneg.entity.EntidadC;
import com.adelerobots.fioneg.entity.HostingC;
import com.adelerobots.fioneg.entity.InterfazC;
import com.adelerobots.fioneg.entity.PriceC;
import com.adelerobots.fioneg.entity.RoleUsuarioC;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.entity.TransactionDC;
import com.adelerobots.fioneg.entity.UserStatusEnum;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.entity.avatarspark.AvatarSparkC;
import com.adelerobots.fioneg.entity.usuariospark.UsuarioSparkC;
import com.adelerobots.fioneg.entity.usuariospark.UsuarioSparkPk;
import com.adelerobots.fioneg.util.FunctionUtils;
import com.adelerobots.fioneg.util.PaypalUtilities;
import com.adelerobots.fioneg.util.StringEncrypterUtilities;
import com.adelerobots.fioneg.util.TemplateUtils;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.adelerobots.fioneg.util.keys.ConstantesError;
import com.paypal.sdk.core.nvp.NVPDecoder;
import com.paypal.sdk.exceptions.PayPalException;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.RollbackException;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

import freemarker.template.TemplateException;


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
		ent.setTitle(title);
		ent.setFlagEntidad(flagEntity);
		ent.setChPagoAnual(isPagoAnual);
		ent.setDatRegister(new Date());
		ent.setDatAccountUpdate(new Date());
		
		/* A los usuarios no se les permite acceder a la plataforma
		 * si no han pinchado en el enlace que se les envia al email proporcionado
		 * para confirmar que son realmente quienes dicen ser */ 
		ent.setStatus(UserStatusEnum.UNCONFIRMED);
		
		ent.setChCancelada(new Character('0'));
		
		/* XXX El campo AvatarBuilder encoded userpath en BBDD, 
		 * solo se establece cuando se registra un usuario 
		 * y nunca debería ser modificado. Se hace codificando el mail. */
		if (email == null) {
			ent.setAvatarBuilderUmd5(null);
		} else {
			ent.setAvatarBuilderUmd5(FunctionUtils.toAvatarBuilderUmd5(email));
		}
		
		// Calcular el crédito inicial al darse de alta
		//Float credit = getAccountUserCredit(ent);
		
		//ent.setFloAccountCredit(credit);
		ent.setFloAccountCredit(new Float(0));
		
		
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
			if(title != null) user.setTitle(title);
			if(flagEntity != null) user.setFlagEntidad(flagEntity);
			if(isPagoAnual != null) {
				user.setChPagoAnual(isPagoAnual);
				user.setDatAccountUpdate(new Date());
			}
			if(user.getChCancelada().equals('1'))
				user.setChCancelada('0');
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





	public UsuarioC AddSparks(UsuarioC userObj, List<String> coresparks){
		
		final SparkManager managerspark = ManagerFactory.getInstance().getSparkManager();
		UsuarioEng usuarioDao = new UsuarioEng(conexion);
		
		//Recuperar sparks del usuario
		//List<SparkC> sparkList = userObj.getLstSparkc();
		List<SparkC> sparkList = getAllUserSparks(userObj.getCnUsuario());

		//Comprobar que el usuario tiene las sparks base
		Iterator<SparkC> iterOwnedSparks= sparkList.iterator();
	
		List<String> usersparks = new ArrayList<String>();
				
		Iterator<String> itercoresparks = coresparks.iterator();
		
		
		while(iterOwnedSparks.hasNext()){
			usersparks.add(Integer.toString(iterOwnedSparks.next().getCnSpark()));
		}
		
		 while(itercoresparks.hasNext()){
		    	String aux = itercoresparks.next();
		    	if (!usersparks.contains(aux)){
		    		try {
		    			UsuarioSparkPk usuarioSparkPk = new UsuarioSparkPk();
		    			
						SparkC auxSpark = managerspark.getSpark(Integer.parseInt(aux),null);
						UsuarioSparkC usuarioSpark = new UsuarioSparkC();
						
						usuarioSparkPk.setUsuario(userObj);
						usuarioSparkPk.setSpark(auxSpark);
						
						usuarioSpark = new UsuarioSparkC();
						usuarioSpark.setUsuarioSparkPk(usuarioSparkPk);
						usuarioSpark.setChHidden('0');
						usuarioSpark.setChUsedSpark('0');
						usuarioSpark.setChActivo('1');
						usuarioSpark.setDatPurchase(new Date());
						
						userObj.getLstUsuarioSparkC().add(usuarioSpark);
						
						usuarioDao.update(userObj);
						usuarioDao.flush();
						
												
						//sparkList.add(auxSpark);
					} catch (NumberFormatException e) {						
						e.printStackTrace();
					} catch (Exception e) {						
						e.printStackTrace();
					}
		    	}
			}
		
		//userObj.persist();

		return userObj;
	
	
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
				usuario.setStatus(UserStatusEnum.OFFLINE);
			}
		}
		else
			throw new Exception(ConstantesError.ERROR_DETALLE_CONFIRMACION_ERRONEA);
		return usuario;
	}
	
	//la siguiente función es para hacer una sola consulta a base de datos y acelerar el proceso
	@SuppressWarnings("unused")
	public List<FionaComponent> getAvatarBuilderInfo(Integer userId){
		List<FionaComponent> componentList = new ArrayList<FionaComponent>();
		System.out.println("getAvatarBuilderInfo");
		UsuarioEng dao = new UsuarioEng(conexion);
		List<Object[]> lista1 = dao.UserSparkInterfazQuery(userId);
		List<Object[]> lista2 = dao.UserSparkParamPrimitivosQuery(userId);
		List<Object[]> lista3 = dao.UserSparkParamListaGlobalQuery(userId);
		List<Object[]> lista4 = dao.UserSparkParamListaPropiosQuery(userId);
		
		//lógica para hacer un componentlist
		
		return componentList;
	}
	
	public List<FionaComponent> getSparksAndIfaces(Integer userId){
		System.out.println("getsparksAndIfaces");
		UsuarioEng dao = new UsuarioEng(conexion);
		UsuarioC usuario = dao.findById(userId);
		
		List<FionaComponent> componentList = new ArrayList<FionaComponent>();
		
		
		//List<SparkC> sparkList = usuario.getLstSparkc();
		List<SparkC> sparkList = getNotHiddenSparks(usuario.getCnUsuario());
		Iterator<SparkC> iterSparks = sparkList.iterator();
		
		
				
		while (iterSparks.hasNext()){ //para cada componente
			FionaComponent auxComp = new FionaComponent();
			Collection<String> ifacesCollection = new ArrayList<String>();
			List<ConfigParamC> componentParams = new ArrayList<ConfigParamC>();
			
			SparkC auxSpark = iterSparks.next();
			String sparkName = auxSpark.getStrNombre();
			
			componentParams = auxSpark.getLstConfigParams();
			
			List<InterfazC> ifacesList = auxSpark.getLstinterfazc();
			Iterator<InterfazC> iterIfaces = ifacesList.iterator();
			
			
			while (iterIfaces.hasNext()){ //para cada interfaz
				InterfazC auxIfaz = iterIfaces.next();
				String interfazName = auxIfaz.getDcNombre() + auxIfaz.getStrTipo();
				//lo añado a la colección de strings
				ifacesCollection.add(interfazName);	
				
			}
			auxComp.setComponentParams(componentParams);
			auxComp.setComponentName(sparkName);
			auxComp.setComponentIfaces(ifacesCollection);
			componentList.add(auxComp);
		}
							
		return componentList;
				
	}
	
	
	/**
	 * Método que devolverá la lista de sparks con el flag hidden
	 * a '0' para un usuario dado
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * 
	 * @return Se devuelve la lista de sparks con el flag 'Hidden'
	 * a 0 para el usuario cuyo identificador se ha pasado como
	 * parámetro
	 */
	public List<SparkC> getNotHiddenSparks(Integer intCodUsuario) {
//		UsuarioSparkEng dao = new UsuarioSparkEng(conexion);
//		
//		List<SparkC> sparks = new ArrayList<SparkC>();
//		
//		List<UsuarioSparkC> lstUsuarioSpark = dao.getNotHiddenSparks(intCodUsuario);
//		
//		if(lstUsuarioSpark != null && !lstUsuarioSpark.isEmpty()){
//			// Recuperar los sparkC de cada elemento
//			for(int i=0; i< lstUsuarioSpark.size(); i++){
//				SparkC spark = lstUsuarioSpark.get(i).getUsuarioSpark().getSpark();
//				sparks.add(spark);
//			}
//		}
		
		SparkEng sparkDao = new SparkEng(conexion);
		
		List <SparkC> notHiddensparks = sparkDao.getNotHiddenSparks(intCodUsuario);
		
		return notHiddensparks;
	}
	
	/**
	 * Método que devolverá la lista de sparks para un usuario dado
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * 
	 * @return Se devuelve la lista de sparks asociados al usuario 
	 * cuyo identificador se ha pasado como parámetro
	 */
	public List<SparkC> getAllUserSparks(Integer intCodUsuario) {
//		UsuarioSparkEng dao = new UsuarioSparkEng(conexion);
//		
//		List<SparkC> sparks = new ArrayList<SparkC>();
//		
//		List<UsuarioSparkC> lstUsuarioSpark = dao.getAllUserSparks(intCodUsuario);
//		
//		if(lstUsuarioSpark != null && !lstUsuarioSpark.isEmpty()){
//			// Recuperar los sparkC de cada elemento
//			for(int i=0; i< lstUsuarioSpark.size(); i++){
//				SparkC spark = lstUsuarioSpark.get(i).getUsuarioSpark().getSpark();
//				sparks.add(spark);
//			}
//		}
		
		SparkEng sparkDao = new SparkEng(conexion);
		
		List <SparkC> allUserSparks = sparkDao.getAllUserSparks(intCodUsuario);
		
		return allUserSparks;
	}
	
	/**
	 * Método que actualiza el flag 'hidden' de un registro
	 * usuariospark
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @param intCodSpark Identificador único del spark
	 */
	public void hideSparkToUser(Integer intCodUsuario, Integer intCodSpark){
		UsuarioSparkEng dao = new UsuarioSparkEng(conexion);
		
		UsuarioSparkC usuarioSpark = dao.findUsuarioSpark(intCodUsuario, intCodSpark);
		
		// Si encontramos el registro lo actualizamos
		// seteando a 1 el flag hidden (si antes estaba a 0)
		// seteando a 0 el flag hidden (si antes estaba a 1)
		if(usuarioSpark != null){
			if(usuarioSpark.getChHidden() == '0')
				usuarioSpark.setChHidden('1');
			else
				usuarioSpark.setChHidden('0');
			
			dao.update(usuarioSpark);
			dao.flush();
		}
	}
	
	
	/**
	 * Método que incluirá el spark correspondiente a un usuario
	 * en el modo prueba, indicando la fecha en la que fue
	 * iniciado el período "Trial"
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @param intCodSpark Identificador únido del spark
	 */
	public void useTrialSpark(Integer intCodUsuario, Integer intCodSpark){
		UsuarioSparkEng dao = new UsuarioSparkEng(conexion);
		UsuarioEng usuarioDao = new UsuarioEng(conexion);
		SparkEng sparkDao = new SparkEng(conexion);
		
		UsuarioSparkC usuarioSpark = dao.findUsuarioSpark(intCodUsuario, intCodSpark);
		
		if(usuarioSpark != null){
			// Comprobar si ya ha sido utilizado como trial previamente
			if(usuarioSpark.getChUsedSpark() == '0'){
				usuarioSpark.setChUsedSpark('1');
				usuarioSpark.setDatUsedSpark(new Date());
				
				dao.update(usuarioSpark);
				dao.flush();
			}
		}else{
			// Creamos la nueva entidad
			UsuarioSparkPk usuarioSparkPk = new UsuarioSparkPk();
			
			UsuarioC usuario = usuarioDao.findById(intCodUsuario);
			SparkC spark = sparkDao.getSpark(intCodSpark);
			
			usuarioSparkPk.setUsuario(usuario);
			usuarioSparkPk.setSpark(spark);
			
			usuarioSpark = new UsuarioSparkC();
			usuarioSpark.setUsuarioSparkPk(usuarioSparkPk);
			usuarioSpark.setChHidden('0');
			usuarioSpark.setChUsedSpark('0');
			
			// trial Version
			usuarioSpark.setChUsedSpark('1');
			usuarioSpark.setDatUsedSpark(new Date());
						
			usuario.getLstUsuarioSparkC().add(usuarioSpark);
			
			usuarioDao.update(usuario);
			usuarioDao.flush();
		}
	}
	
	/**
	 * Método que permite planificar para su renovación en desarrollo un único
	 * spark
	 * @param usuarioSpark Spark a planificar
	 */
	public void planificarSparkDesa(UsuarioSparkC usuarioSpark){		
		Date fechaToday = new Date();
		Calendar calToday = Calendar.getInstance();
		calToday.setTime(fechaToday);						
		
		// Si el usuario que desarrolló el spark no es el mismo que lo está usando
		if(!usuarioSpark.getUsuarioSparkPk().getUsuario().getCnUsuario().equals(usuarioSpark.getUsuarioSparkPk().getSpark().getCnUsuarioDesarrollador())){
			// Sólo se renovarán el próximo mes los sparks de renovación
			// mensual no gratuitos y los anuales si estamos en diciembre
			
			PriceC price = usuarioSpark.getPrice();
			if(price!=null){
				if(price.getUnit().getIntCodUnit().equals(Constantes.MONTHS) || (price.getUnit().getIntCodUnit().equals(Constantes.YEARS) && calToday.get(Calendar.MONTH)==Calendar.DECEMBER)){
					TransactionDC transaction = new TransactionDC();
					// Fecha del batch de notificación
					Integer intDiaNotif = Integer.parseInt(Constantes.getFECHA_NOTI_DESA());
					Calendar calNotificacionDesa = Calendar.getInstance();
					calNotificacionDesa.setTime(new Date());
					calNotificacionDesa.set(Calendar.DAY_OF_MONTH,intDiaNotif);
					char emailSent = '0';
					if(calNotificacionDesa.getTimeInMillis()<calToday.getTimeInMillis())
						emailSent = '1';
					
					transaction.setUsuarioSpark(usuarioSpark);
					Calendar calRenovacion = Calendar.getInstance();
					calRenovacion.add(Calendar.MONTH, 1);
					calRenovacion.set(Calendar.DAY_OF_MONTH, calRenovacion.getActualMinimum(Calendar.DAY_OF_MONTH));
					transaction.setDatRenovation(calRenovacion.getTime());
					transaction.setChCharged('0');
					transaction.setChEmailSent(emailSent);
					transaction.setChPaid('0');
					transaction.setChRenewed('0');
					transaction.setFloAmount(usuarioSpark.getPrice().getFloPrize());
					
					TransactionDEng transactionDAO = new TransactionDEng(conexion);
					transactionDAO.insert(transaction);
					transactionDAO.flush();
					
				}
			}
		
		}
			
		
	}
	
	/**
	 * Método que asigna un spark a un usuario
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @param intCodSpark Identificador único del spark
	 * @param intCodPrice Identificador del precio elegido por el usuario
	 */
	public void buySpark(IContextoEjecucion contexto, Integer intCodUsuario, Integer intCodSpark, Integer intCodPrice)throws RollbackException{
		UsuarioSparkEng usuarioSparkDao = new UsuarioSparkEng(conexion);
		UsuarioEng usuarioDao = new UsuarioEng(conexion);
		SparkEng sparkDao = new SparkEng(conexion);
		PriceEng priceDao = new PriceEng(conexion);
		
		UsuarioSparkC usuarioSpark = usuarioSparkDao.findUsuarioSpark(intCodUsuario, intCodSpark);
		UsuarioC usuario = usuarioDao.findById(intCodUsuario);
		// Asegurar que el usuario no había adquirido el spark previamente
		if(usuarioSpark == null){
			// Creamos la nueva entidad
			UsuarioSparkPk usuarioSparkPk = new UsuarioSparkPk();
			
			//UsuarioC usuario = usuarioDao.findById(intCodUsuario);
			SparkC spark = sparkDao.getSpark(intCodSpark);
			
			usuarioSparkPk.setUsuario(usuario);
			usuarioSparkPk.setSpark(spark);
			
			usuarioSpark = new UsuarioSparkC();
			usuarioSpark.setUsuarioSparkPk(usuarioSparkPk);
			usuarioSpark.setChHidden('0');
			usuarioSpark.setChUsedSpark('0');
			usuarioSpark.setChActivo('1');
			usuarioSpark.setDatPurchase(new Date());
			
			// Recuperamos el precio del usuarioSpark			
			if(intCodPrice != null){
				PriceC price = priceDao.findById(intCodPrice);
				
				usuarioSpark.setPrice(price);
			}
						
			usuario.getLstUsuarioSparkC().add(usuarioSpark);
			
			usuarioDao.update(usuario);
			usuarioDao.flush();
			
		}else{
			// Si en algún momento el usuario había comprado el spark
			// pero por cualquier motivo lo desinstaló
			if(usuarioSpark.getChActivo().equals('0')){
				usuarioSpark.setChHidden('0');
				usuarioSpark.setChUsedSpark('0');
				usuarioSpark.setChActivo('1');
				usuarioSpark.setDatPurchase(new Date());
				
				// Recuperamos el precio del usuarioSpark
				if(intCodPrice != null){
					PriceC price = priceDao.findById(intCodPrice);
					
					usuarioSpark.setPrice(price);
				}
				
				usuarioSparkDao.update(usuarioSpark);
				usuarioSparkDao.flush();
			}
		}
		
		// Muy importante: Reducir el saldo del usuario en
		// su estimada cuenta bancaria si el spark no es gratuito
		if(intCodPrice != null){
			SparkC spark = sparkDao.getSpark(intCodSpark);
			String ppresponse = "";
			// Recuperamos el billingid codificado
			String encodedBillingAgreementId = usuario.getStrBillingId();
			StringEncrypterUtilities desencrypter = new StringEncrypterUtilities(Constantes.PASS_PHRASE);
			String billingAgreementId = desencrypter.decrypt(encodedBillingAgreementId);		
			//Float cantidad = usuarioSpark.getPrice().getFloPrize(); //  PRE-PRORRATEO
			Float cantidad = getPrecioSparkProrrateado(usuarioSpark); // POST-PRORRATEO
			if(cantidad != null && cantidad > 0){
				// Comprobar estado del acuerdo
				ppresponse = PaypalUtilities.getInstance().baUpdate(billingAgreementId);
				try{
					if(PaypalUtilities.getInstance().checkBillingAgreementStatus(ppresponse)){
						// En este punto todo ha ido bien
						// Ejecutar cobro Papal
						// Formatear la cantidad a dos decimales
						BigDecimal bidCantidad = new BigDecimal(cantidad.floatValue()).setScale(2,BigDecimal.ROUND_UP);
						ppresponse = PaypalUtilities.getInstance().doReferenceTransaction(billingAgreementId, bidCantidad.toString(), spark.getStrNombre());
						
						// Tratar posibles errores en la respuesta					
						
						NVPDecoder resultValues = new NVPDecoder();
					
						// decode method of NVPDecoder will parse the request and decode the
						// name and value pair
						resultValues.decode(ppresponse);
			
						// checks for Acknowledgement and redirects accordingly to display
						// error messages
						String strAck = resultValues.get("ACK");
						if (strAck != null
								&& !(strAck.equals("Success") || strAck
										.equals("SuccessWithWarning"))) {
							// Error
							LOGGER.error("Se ha producido un error al intentar cobrar el spark al usuario " + usuario.getCnUsuario());
							PaypalUtilities.getInstance().gestionErroresDoReferenceTransaction(contexto, resultValues, usuario, usuario.getStrBillingId(), "doReferenceTransaction");
							throw new RollbackException();
						} else {
							// En este punto todo ha ido bien							
							// Comprobamos si la fecha de hoy es posterior a la fecha en que se realizan las planificaciones
							// de desarrollo, para realizar la planificación "manualmente" en ese caso
							Calendar calToday = Calendar.getInstance();
							calToday.setTime(new Date());
							Calendar calPlanificacionDesa = Calendar.getInstance();
							calPlanificacionDesa.setTime(new Date());
							Integer diaPlaniDesa = Integer.parseInt(Constantes.getFECHA_PLANI_DESA()); 
							calPlanificacionDesa.set(Calendar.DAY_OF_MONTH, diaPlaniDesa);
							
							if(calToday.getTimeInMillis()>calPlanificacionDesa.getTimeInMillis()){
								// Realizar planificación porque la fecha de hoy es posterior
								// a la fecha de planificación del próximo mes
								planificarSparkDesa(usuarioSpark);
							}
							
						}
						
					}else{
						// Error
						LOGGER.error("Se ha producido un error al intentar cobrar un spark al usuario - comprobación estado acuerdo " + usuario.getCnUsuario());
						NVPDecoder resultValues = new NVPDecoder();								
						
						resultValues.decode(ppresponse);
						PaypalUtilities.getInstance().gestionErroresDoReferenceTransaction(contexto, resultValues, usuario, usuario.getStrBillingId(), "baUpdate");
						throw new RollbackException();
					}
				}catch (PayPalException ppEx) {
					// TODO: handle exception
					ppEx.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Método que permite desligar un spark de un usuario
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @param intCodSpark Identificador único del spark
	 */
	public void uninstallSpark(Integer intCodUsuario, Integer intCodSpark){
		UsuarioSparkEng usuarioSparkDao = new UsuarioSparkEng(conexion);
		UsuarioSparkC sparkUsuario = usuarioSparkDao.findUsuarioSpark(intCodUsuario, intCodSpark);
		
		sparkUsuario.setChActivo('0');
		
		usuarioSparkDao.update(sparkUsuario);
		usuarioSparkDao.flush();
	}
	
	/**
	 * Método que permite actualizar la entidad de un usuario
	 * @param user Usuario
	 * @param entidad Entidad del usuario
	 */
	public void updateUserEntity(UsuarioC user, EntidadC entidad){
		final UsuarioEng userDAO = new UsuarioEng(conexion);
		
		if(user == null)
			return;
		
		user.setEntidad(entidad);
		userDAO.update(user);
		userDAO.flush();		
	}
	
	/**
	 * Método que permite actualizar el campo que nos indica el
	 * identificador del acuerdo del usuario con FIONA a través
	 * de PayPal
	 * 
	 * @param usuario Usuario cuyo acuerdo se actualiza
	 * @param token Identificador del acuerdo
	 */
	public void updateUserBillingAgreement(UsuarioC usuario, String token){
		final UsuarioEng usuarioDAO = new UsuarioEng(conexion);
		
		if(usuario==null)
			return;
		
		usuario.setStrBillingId(token);
		usuarioDAO.update(usuario);
		usuarioDAO.flush();
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
		
		if(intFlagResetearCredito != null && !intFlagResetearCredito.equals(0)){
			usuario.setFloAccountCredit(new Float(0));
		}
		
		usuarioDAO.update(usuario);
		usuarioDAO.flush();
	}
	
	/**
	 * Método que buscará a los usuarios que deban pagar la suscripción a la
	 * plataforma en el día actual
	 */
	public void cobrarSuscripciones(IContextoEjecucion contexto){
		Calendar cal = Calendar.getInstance();
		Date fechaActual = new Date();
		cal.setTime(fechaActual);
		UsuarioEng usuarioDAO = new UsuarioEng(conexion);
		List <UsuarioC> usuarios = usuarioDAO.getUsuariosRegistradosPorDia(cal);
		
		boolean ejecutarPago = false;
		for(int i = 0;i < usuarios.size(); i++){
			// Para cada usuario comprobar el tipo de pago elegido
			UsuarioC usuario = usuarios.get(i);
			Character pagoAnual = usuario.getChPagoAnual();
			ejecutarPago = false;
			// Comprobar que la cuenta del usuario no sea Free			
			if(!usuario.getCuenta().getId().equals(1)){
				String ppresponse = "";
				String encodedBillingAgreementId = usuario.getStrBillingId();
				StringEncrypterUtilities desencrypter = new StringEncrypterUtilities(Constantes.PASS_PHRASE);
				String billingAgreementId = desencrypter.decrypt(encodedBillingAgreementId);
				// Comprobar la cantidad a cobrar en función de lo elegido
				// por el usuario
				Float cantidad = new Float (0);
				if(pagoAnual != null){
					if("1".equals(pagoAnual)){
						// Pago anual
						cantidad = usuario.getCuenta().getFloPrecioAnual();						
						//Comprobar si el mes de actualización de cuenta también se corresponde con el actual
						Calendar calUsuario = Calendar.getInstance();
						calUsuario.setTime(usuario.getDatAccountUpdate());
						if(cal.get(Calendar.MONTH) == calUsuario.get(Calendar.MONTH)){
							// Asegurar que el año sea distinto
							if(cal.get(Calendar.YEAR) != calUsuario.get(Calendar.YEAR)){
								ejecutarPago = true;
								// Comprobar estado del acuerdo
								//ppresponse = PaypalUtilities.getInstance().baUpdate(billingAgreementId);
								// Ejecutar cobro Papal
								//ppresponse = PaypalUtilities.getInstance().doReferenceTransaction(billingAgreementId, cantidad.toString());
							}
						}
						
					}else{
						// Pago mensual						
						// Comprobamos que no sea la fecha de actualización la fecha de hoy
						Calendar calUsuario = Calendar.getInstance();
						calUsuario.setTime(usuario.getDatAccountUpdate());
						
						if(cal.get(Calendar.MONTH) == calUsuario.get(Calendar.MONTH)){
							// Asegurar que el año sea distinto
							if(cal.get(Calendar.YEAR) != calUsuario.get(Calendar.YEAR)){
								ejecutarPago = true;
							}else{
								ejecutarPago = false;
							}
						}else{
							ejecutarPago = true;
						}
						cantidad = usuario.getCuenta().getFloPrecioMensual();
						// Proceder al pago vía Paypal
						//ppresponse = PaypalUtilities.getInstance().doReferenceTransaction(billingAgreementId, cantidad.toString());
					}
					
					// Ejecutar el cobro
					try{
						if(ejecutarPago){
							// Comprobar crédito del usuario
							Float credito = usuario.getFloAccountCredit();
							if((cantidad - credito)>=0){
								// Establecemos la cantidad a cobrar teniendo en cuenta
								// el crédito del usuario
								cantidad = cantidad - credito;
								// Reseteamos el crédito a 0
								usuario.setFloAccountCredit(new Float(0));							
							}else{
								// Si el crédito es mayor que la cantidad a pagar
								// la cantidad a pagar pasa a ser 0.
								cantidad = new Float (0);
								// Resetear crédito
								usuario.setFloAccountCredit(usuario.getFloAccountCredit() - cantidad);
							}
							if(cantidad > 0){
								// Comprobar estado del acuerdo
								ppresponse = PaypalUtilities.getInstance().baUpdate(billingAgreementId);
								
								if(PaypalUtilities.getInstance().checkBillingAgreementStatus(ppresponse)){									
									// En este punto todo ha ido bien
									// Ejecutar cobro Papal
									ppresponse = PaypalUtilities.getInstance().doReferenceTransaction(billingAgreementId, cantidad.toString(),Constantes.getACCOUNT_RENEWAL_ANNOTATION());
									
									// Tratar posibles errores en la respuesta					
									
									NVPDecoder resultValues = new NVPDecoder();
								
									// decode method of NVPDecoder will parse the request and decode the
									// name and value pair
									resultValues.decode(ppresponse);
						
									// checks for Acknowledgement and redirects accordingly to display
									// error messages
									String strAck = resultValues.get("ACK");
									if (strAck != null
											&& !(strAck.equals("Success") || strAck
													.equals("SuccessWithWarning"))) {
										// Error
										LOGGER.error("Se ha producido un error al intentar cobrar la suscripción del usuario " + usuario.getCnUsuario());
										PaypalUtilities.getInstance().gestionErroresDoReferenceTransaction(contexto, resultValues, usuario, usuario.getStrBillingId(), "doReferenceTransaction");
									} else {
										// En este punto todo ha ido bien 
										
									}
									
								}else{
									// Error
									LOGGER.error("Se ha producido un error al intentar cobrar la suscripción del usuario - comprobación estado acuerdo " + usuario.getCnUsuario());
									NVPDecoder resultValues = new NVPDecoder();								
									
									resultValues.decode(ppresponse);
									PaypalUtilities.getInstance().gestionErroresDoReferenceTransaction(contexto, resultValues, usuario, usuario.getStrBillingId(), "baUpdate");
								}
							}
						}
						
						
					}catch(PayPalException ppEx){
						LOGGER.error("Error en UsuariosManager - cobrarSuscripciones():\n" + ppEx.getMessage());
					}
				}
				
			}
			
		}
			
	}
	
	/**
	 * Método que nos permite calcular el cŕedito de cuenta que
	 * le queda a un usuario
	 * 
	 * @param usuario Usuario cuyo crédito se va a calcular
	 * @return Se devuelve el crédito restante en la cuenta del usuario
	 */
	public Float getAccountUserCredit(UsuarioC usuario){
		Float credit = new Float(0);
		
		// Fecha de última actualización de cuenta
		Date feUpdate = usuario.getDatAccountUpdate();
		// Fecha de hoy
		Date feToday = new Date();
		
		// Valor de suscripción del tipo de cuenta del usuario;
		Float cuota = new Float(0);
		if(!usuario.getCuenta().getId().equals(1)){
			if(usuario.getChPagoAnual().equals(new Character('1'))){
				cuota = usuario.getCuenta().getFloPrecioAnual();				
			}else{
				cuota = usuario.getCuenta().getFloPrecioMensual();
			}
		
		
			// Contamos el número de días pagados
			Calendar cal = Calendar.getInstance();
			Calendar calUpdate = Calendar.getInstance();
			calUpdate.setTime(feUpdate);
			Calendar calToday = Calendar.getInstance();
			calToday.setTime(feToday);
			
			Integer intNumDiasPagados = new Integer(0);
			if(!usuario.getCuenta().getId().equals(1)){
				if(usuario.getChPagoAnual().equals(new Character('1'))){				
					cal.setTimeInMillis(feToday.getTime() - feUpdate.getTime());
					intNumDiasPagados = cal.get(Calendar.DAY_OF_YEAR);
					Float pagado = (cuota*intNumDiasPagados)/365;
					credit = cuota - pagado;
				}else{
					// Calculamos los meses pagados con respecto a la fecha de actualización
					Integer intNumMesesPagados = new Integer(0);
					cal.setTimeInMillis(calToday.getTime().getTime() - calUpdate.getTime().getTime());
					intNumMesesPagados = cal.get(Calendar.DAY_OF_YEAR)/30;
					
					calUpdate.add(Calendar.MONTH, intNumMesesPagados);
					cal.setTimeInMillis(calToday.getTime().getTime() - calUpdate.getTime().getTime());
					intNumDiasPagados = cal.get(Calendar.DAY_OF_YEAR);
					Float pagado = (cuota*intNumDiasPagados)/30;
					credit = cuota - pagado;
					
				}
			}
		}
		
		if(credit < 0)
			credit = new Float(0);
		else
			credit = credit + usuario.getFloAccountCredit();		
		
		
		return credit;
	}
	
	/**
	 * Método que permite cancelar la cuenta de un usuario
	 * 
	 * @param usuario Usuario cuya cuenta pasará a invalidarse
	 */
	public void cancelarCuentaUsuario(UsuarioC usuario){
		final UsuarioEng usuarioDAO = new UsuarioEng(conexion);
		final UsuarioConfigEng usuarioConfigDAO = new UsuarioConfigEng(conexion);
		final UsuarioSparkEng usuarioSparkDAO = new UsuarioSparkEng(conexion);
		// Cancelar la cuenta del usuario
		usuario.setChCancelada('1');
		// Cambiar el status a 'UNCONFIRMED' para evitar que pueda loguearse
		usuario.setStatus(UserStatusEnum.UNCONFIRMED);		
		// Borrar configuraciones del usuario		
		usuarioConfigDAO.deleteUsuarioConfigByUser(usuario.getCnUsuario());
		// Borrar los sparks asociados al usuario
		usuarioSparkDAO.deleteUsuarioSparksByUser(usuario.getCnUsuario());
		
		usuarioDAO.update(usuario);
		usuarioDAO.flush();		
	}
	
	/**
	 * Método que permite actualizar el cŕedito de cuenta de un usuario
	 * 
	 * @param usuario Usuario cuyo crédito va a actualizarse
	 * @param floAccountCredit Nuevo crédito de cuenta del usuario
	 */
	public void updateUserAccountCredit(UsuarioC usuario, Float floAccountCredit){
		final UsuarioEng usuarioDAO = new UsuarioEng(conexion);
		if(floAccountCredit != null){
			usuario.setFloAccountCredit(floAccountCredit);
			usuario.setAccountCredit(getAccountUserCredit(usuario));
		}else{
			usuario.setFloAccountCredit(getAccountUserCredit(usuario));
		}
		
		usuarioDAO.update(usuario);
		usuarioDAO.flush();
	}
	
	/**
	 * Método que calcula la cantidad prorrateada que pagará un usuario por un spark
	 * 
	 * @param usuarioSpark Objeto con la información sobre el pago del spark
	 * 
	 * @return Devolverá el precio calculado que debe pagarse en el momento de
	 * ejecutar la compra
	 */
	private Float getPrecioSparkProrrateado(UsuarioSparkC usuarioSpark){
		Float precio = new Float(0);
		
		PriceC price = usuarioSpark.getPrice();
		
		// Si el spark no era gratuito y no es por uso
		if(price!=null && price.getUnit() != null){
			// Cantidad fijada para el spark
			Float floPrice = price.getFloPrize();
			// Periodo de cobro
			Integer unit = price.getUnit().getIntCodUnit();
			
			// Fecha de compra (hoy)
			Date feCompra = new Date();
			Calendar calCompra = Calendar.getInstance();
			calCompra.setTime(feCompra);
			if(unit.equals(Constantes.MONTHS)){ // Periodo de cobro mensual
				// Fecha último día del mes
				Calendar calLastDayOfMonth = Calendar.getInstance();
				calLastDayOfMonth.set(calLastDayOfMonth.get(Calendar.YEAR),
						calLastDayOfMonth.get(Calendar.MONTH),
						calLastDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH),
						calLastDayOfMonth.getMaximum(Calendar.HOUR_OF_DAY),
						calLastDayOfMonth.getMaximum(Calendar.MINUTE),
						calLastDayOfMonth.getMaximum(Calendar.SECOND));
				
				// Número de días que faltan para final de mes
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(calLastDayOfMonth.getTime().getTime() - calCompra.getTime().getTime());
				Integer intNumDiasACobrar = cal.get(Calendar.DAY_OF_YEAR);
				precio = (floPrice*intNumDiasACobrar)/30;
				
			}else{// Periodo de cobro anual
				// Fecha último día del mes
				Calendar calLastDayOfYear = Calendar.getInstance();
				calLastDayOfYear.set(calLastDayOfYear.get(Calendar.YEAR),
						calLastDayOfYear.getActualMaximum(Calendar.MONTH),
						calLastDayOfYear.getActualMaximum(Calendar.DAY_OF_MONTH),
						calLastDayOfYear.getMaximum(Calendar.HOUR_OF_DAY),
						calLastDayOfYear.getMaximum(Calendar.MINUTE),
						calLastDayOfYear.getMaximum(Calendar.SECOND));
				
				// Número de días que faltan para final de año
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(calLastDayOfYear.getTime().getTime() - calCompra.getTime().getTime());
				Integer intNumDiasACobrar = cal.get(Calendar.DAY_OF_YEAR);
				precio = (floPrice*intNumDiasACobrar)/365;
			}
			
		}
		
		
		return precio;
	}
	
	
	/**
	 * Método que ejecuta el primer pago del avatar en producción 
	 * 
	 * @param contexto Contexto de ejecuciń
	 * @param usuario Usuario que ha solicitado el paso a producción de un avatar
	 * @param intCodUnidadTemporal Unidad temporal elegida para el pago
	 * @param cantidad Cantidad sin prorratear
	 */
	public void realizarPrimerPagoProduccion(IContextoEjecucion contexto, UsuarioC usuario, Integer intCodUnidadTemporal, Float cantidad, AvatarC avatar){
		// Prorratear el primer pago en función de la unidad temporal elegida por el usuario
		String ppresponse = "";
		Calendar calToday = Calendar.getInstance();
		calToday.setTime(new Date());
		Float precio = new Float(0);
		if(intCodUnidadTemporal.equals(Constantes.MONTHS)){
			// Fecha último día del mes
			Calendar calLastDayOfMonth = Calendar.getInstance();
			calLastDayOfMonth.set(calLastDayOfMonth.get(Calendar.YEAR),
					calLastDayOfMonth.get(Calendar.MONTH),
					calLastDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH),
					calLastDayOfMonth.getMaximum(Calendar.HOUR_OF_DAY),
					calLastDayOfMonth.getMaximum(Calendar.MINUTE),
					calLastDayOfMonth.getMaximum(Calendar.SECOND));
			
			// Número de días que faltan para final de mes
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(calLastDayOfMonth.getTime().getTime() - calToday.getTime().getTime());
			Integer intNumDiasACobrar = cal.get(Calendar.DAY_OF_YEAR);
			precio = (cantidad*intNumDiasACobrar)/30;
		}else{
			// Fecha último día del mes
			Calendar calLastDayOfYear = Calendar.getInstance();
			calLastDayOfYear.set(calLastDayOfYear.get(Calendar.YEAR),
					calLastDayOfYear.getActualMaximum(Calendar.MONTH),
					calLastDayOfYear.getActualMaximum(Calendar.DAY_OF_MONTH),
					calLastDayOfYear.getMaximum(Calendar.HOUR_OF_DAY),
					calLastDayOfYear.getMaximum(Calendar.MINUTE),
					calLastDayOfYear.getMaximum(Calendar.SECOND));
			
			// Número de días que faltan para final de año
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(calLastDayOfYear.getTime().getTime() - calToday.getTime().getTime());
			Integer intNumDiasACobrar = cal.get(Calendar.DAY_OF_YEAR);
			precio = (cantidad*intNumDiasACobrar)/365;
		}
		
		// Llamada a la API de Paypal
		// Comprobar estado del acuerdo
		// Recuperamos el billingid codificado
		String encodedBillingAgreementId = usuario.getStrBillingId();
		StringEncrypterUtilities desencrypter = new StringEncrypterUtilities(Constantes.PASS_PHRASE);
		String billingAgreementId = desencrypter.decrypt(encodedBillingAgreementId);		
		ppresponse = PaypalUtilities.getInstance().baUpdate(billingAgreementId);
		try{
			if(PaypalUtilities.getInstance().checkBillingAgreementStatus(ppresponse)){
				// En este punto todo ha ido bien
				// Ejecutar cobro Papal
				// Formatear la cantidad a dos decimales
				BigDecimal bidCantidad = new BigDecimal(precio.floatValue()).setScale(2,BigDecimal.ROUND_UP);
				ppresponse = PaypalUtilities.getInstance().doReferenceTransaction(billingAgreementId, bidCantidad.toString(),Constantes.getUPLOAD_PRODUCTION_ANNOTATION());
				
				// Tratar posibles errores en la respuesta					
				
				NVPDecoder resultValues = new NVPDecoder();
			
				// decode method of NVPDecoder will parse the request and decode the
				// name and value pair
				resultValues.decode(ppresponse);
	
				// checks for Acknowledgement and redirects accordingly to display
				// error messages
				String strAck = resultValues.get("ACK");
				if (strAck != null
						&& !(strAck.equals("Success") || strAck
								.equals("SuccessWithWarning"))) {
					// Error
					LOGGER.error("Se ha producido un error al intentar cobrar el spark al usuario " + usuario.getCnUsuario());
					PaypalUtilities.getInstance().gestionErroresDoReferenceTransaction(contexto, resultValues, usuario, usuario.getStrBillingId(), "doReferenceTransaction");
					throw new RollbackException();
				} else {
					// En este punto todo ha ido bien 
					// Comprobamos si la fecha de hoy es posterior a la fecha en que se realizan las planificaciones
					// de producción, para realizar la planificación "manualmente" en ese caso
					Calendar calPlanificacionProduccion = Calendar.getInstance();
					calPlanificacionProduccion.setTime(new Date());
					Integer diaPlaniProd = Integer.parseInt(Constantes.getFECHA_PLANI_PROD()); 
					calPlanificacionProduccion.set(Calendar.DAY_OF_MONTH, diaPlaniProd);
					
					if(calToday.getTimeInMillis()>calPlanificacionProduccion.getTimeInMillis()){
						// Realizar planificación porque la fecha de hoy es posterior
						// a la fecha de planificación del próximo mes
						planificarAvatarProd(avatar);
					}					
				}
				
			}else{
				// Error
				LOGGER.error("Se ha producido un error al intentar cobrar el paso a producción del avatar - comprobación estado acuerdo " + usuario.getCnUsuario());
				NVPDecoder resultValues = new NVPDecoder();								
				
				resultValues.decode(ppresponse);
				PaypalUtilities.getInstance().gestionErroresDoReferenceTransaction(contexto, resultValues, usuario, usuario.getStrBillingId(), "baUpdate");
				throw new RollbackException();
			}
		}catch (PayPalException ppEx) {
			// TODO: handle exception
			ppEx.printStackTrace();
		}
		
	}
	
	/**
	 * Método que permite planificar de manera individual un avatar
	 * @param avatar Avatar cuyos elementos deben planificarse
	 */
	public void planificarAvatarProd(AvatarC avatar){
		List <AvatarSparkC> lstAvatarsSpark = avatar.getLstAvatarSparkC();
		
		Calendar calToday = Calendar.getInstance();
		calToday.setTime(new Date());
		
		for(int i = 0; i < lstAvatarsSpark.size(); i++){			
			AvatarSparkC avatarSpark = lstAvatarsSpark.get(i);			
			
			Float price = avatarSpark.getFloPrice();
			HostingC hosting = avatarSpark.getAvatarSparkPk().getAvatar().getHosting();
					
			if(price!=null){
				if(hosting.getIntCodUnit().equals(Constantes.MONTHS) || (hosting.getIntCodUnit().equals(Constantes.YEARS) && calToday.get(Calendar.MONTH)==Calendar.DECEMBER)){
					BillingPC billingP = new BillingPC();
					// Fecha del batch de notificación
					Integer intDiaNotif = Integer.parseInt(Constantes.getFECHA_NOTI_PROD());
					Calendar calNotificacionProd = Calendar.getInstance();
					calNotificacionProd.setTime(new Date());
					calNotificacionProd.set(Calendar.DAY_OF_MONTH,intDiaNotif);
					char emailSent = '0';
					if(calNotificacionProd.getTimeInMillis()<calToday.getTimeInMillis())
						emailSent = '1';
					
					// billingP.setUsuarioSpark(usuarioSpark);
					Calendar calRenovacion = Calendar.getInstance();
					calRenovacion.add(Calendar.MONTH, 1);
					calRenovacion.set(Calendar.DAY_OF_MONTH, calRenovacion.getActualMinimum(Calendar.DAY_OF_MONTH));					
					billingP.setDatRenovation(calRenovacion.getTime());
					
					billingP.setAvatarSpark(avatarSpark);
					billingP.setChCharged('0');
					billingP.setChEmailSent(emailSent);
					billingP.setChPaid('0');
					billingP.setChRenewed('0');					
					// El precio del spark en producción no es el precio asociado al spark, sino el precio seleccionado en función de las opciones
					// en el momento de hacer el upload (tabla hosting)
					billingP.setFloAmount(avatarSpark.getFloPrice());					
					
					BillingPEng billingPDAO = new BillingPEng(conexion);
					billingPDAO.insert(billingP);
					billingPDAO.flush();						
				}
			}
			
			
		}
	}
	
	// ##############################################################################################
	// ############################## MÉTODOS BATCH DESARROLLO ######################################
	// ##############################################################################################
	
	/**
	 * Método que planifica las renovaciones de sparks en desarrollo
	 * Este método "ignora" aquellos usuariosparks cuyo propietario
	 * sea el mismo que desarrolló el spark
	 */
	public void planificarRenovacionesSparksDesarrollo(){
		final UsuarioSparkEng usuarioSparkDAO = new UsuarioSparkEng(conexion);
		
		List <UsuarioSparkC> lstUsuariosSpark = usuarioSparkDAO.getTimeRenewableUsuarioSparks();
		
		Date fechaToday = new Date();
		Calendar calToday = Calendar.getInstance();
		calToday.setTime(fechaToday);
		
		for(int i = 0; i < lstUsuariosSpark.size(); i++){			
			UsuarioSparkC usuarioSpark = lstUsuariosSpark.get(i);
			// Si el usuario que desarrolló el spark no es el mismo que lo está usando
			if(!usuarioSpark.getUsuarioSparkPk().getUsuario().getCnUsuario().equals(usuarioSpark.getUsuarioSparkPk().getSpark().getCnUsuarioDesarrollador())){
				// Sólo se renovarán el próximo mes los sparks de renovación
				// mensual no gratuitos y los anuales si estamos en diciembre
				
				PriceC price = usuarioSpark.getPrice();
				if(price!=null){
					if(price.getUnit().getIntCodUnit().equals(Constantes.MONTHS) || (price.getUnit().getIntCodUnit().equals(Constantes.YEARS) && calToday.get(Calendar.MONTH)==Calendar.DECEMBER)){
						TransactionDC transaction = new TransactionDC();
						
						transaction.setUsuarioSpark(usuarioSpark);
						Calendar calRenovacion = Calendar.getInstance();
						calRenovacion.add(Calendar.MONTH, 1);
						calRenovacion.set(Calendar.DAY_OF_MONTH, calRenovacion.getActualMinimum(Calendar.DAY_OF_MONTH));
						transaction.setDatRenovation(calRenovacion.getTime());
						transaction.setChCharged('0');
						transaction.setChEmailSent('0');
						transaction.setChPaid('0');
						transaction.setChRenewed('0');
						transaction.setFloAmount(usuarioSpark.getPrice().getFloPrize());
						
						TransactionDEng transactionDAO = new TransactionDEng(conexion);
						transactionDAO.insert(transaction);
						transactionDAO.flush();
						
					}
				}
			
			}
			
		}
		
	}
	
	
	/**
	 * Método que envía a los usuarios un correo con el resumen de los sparks en desarrollo que 
	 * serán renovados al mes siguiente
	 */
	public void notificarRenovacionesSparksDesarrollo(IContextoEjecucion contexto){
		UsuarioEng usuarioDAO = new UsuarioEng(conexion);		
		TransactionDEng transactionDDAO = new TransactionDEng(conexion);
		// 1. Recuperamos los usuarios activos de la plataforma
		List <UsuarioC> lstUsuarios = usuarioDAO.getAllActiveUsers();
		
		// 2.  Para cada usuario obtenemos los objetos "usuarioSpark"
		// que sigan activos y tengan un precio
//		for(int i = 0; i<lstUsuarios.size(); i++){
//			UsuarioC usuario = lstUsuarios.get(i);
//			
//			List <UsuarioSparkC> lstUsuariosSpark = usuarioSparkDAO.getTimeRenewableUsuarioSparksByUser(usuario.getCnUsuario());
//			
//			// 3. Para cada uno de los sparks comprobar cuáles están en TransactionD esperando ser renovados el próximo mes
//			for(int j = 0; j<lstUsuariosSpark.size(); j++){
//				
//				
//								
//			}
//			
//		}
		
		// 2.  Para cada usuario obtenemos los objetos "transactiond"
		// que se correspondan con el próximo mes, que no tengan el flag
		// 'fl_emailsent' marcado, y que no hayan sido cobrados
		Calendar calRenovacion = Calendar.getInstance();
		calRenovacion.setTime(new Date());
		calRenovacion.add(Calendar.MONTH, 1);
		calRenovacion.set(Calendar.DAY_OF_MONTH, calRenovacion.getActualMinimum(Calendar.DAY_OF_MONTH));
		for(int i = 0; i<lstUsuarios.size(); i++){
			UsuarioC usuario = lstUsuarios.get(i);
			List <TransactionDC> lstTransactions = transactionDDAO.getNotNotifiedTransactionsByUser(usuario.getCnUsuario(), calRenovacion);
			
			// 3. Obtener los datos que necesitamos de cada transacción para construir el listado detallado de sparks con sus precios
			// y actualizar los campos necesarios de la tabla transacción
			List <SparkPrice> lstSparkPrecio = new ArrayList<SparkPrice>();
			Float total = new Float(0);
			for(int j = 0; j<lstTransactions.size(); j++){				
				TransactionDC transaction = lstTransactions.get(j);			
				
				String nombreSpark = transaction.getUsuarioSpark().getUsuarioSparkPk().getSpark().getStrNombre();
				String periodicidad = (transaction.getUsuarioSpark().getPrice().getUnit().getIntCodUnit().equals(Constantes.MONTHS))?"per month":"per year";
				
				BigDecimal bidAmount = new BigDecimal(transaction.getFloAmount()).setScale(2,BigDecimal.ROUND_UP);
				total += transaction.getFloAmount();
				String precioSpark = bidAmount.toString();
				
				SparkPrice sparkPrice = new SparkPrice(nombreSpark,precioSpark);
				
				sparkPrice.setPeriodicidad(periodicidad);
				
				lstSparkPrecio.add(sparkPrice);
				
				// 4. Actualizar el campo 'fl_emailsent' de la transacción
				transaction.setChEmailSent('1');
				transactionDDAO.update(transaction);
				transactionDDAO.flush();
			}
			// 5. Enviar email
			if(!lstSparkPrecio.isEmpty()){
				BigDecimal bd = new BigDecimal(total).setScale(2,BigDecimal.ROUND_UP);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String fecha = sdf.format(new Date());
				enviarNotificacionSparksDesarrollo(contexto,usuario,lstSparkPrecio,bd.toString(),"mail_user_notif_sparks_desa-html","[FIONA] Development Sparks Renovation",fecha);
			}
		}		
		
	}	
	
	
	/**
	 * Método que ejecuta el cobro de la cantidad total que el usuario debe pagar
	 * este mes por todos los sparks que corresponda renovar y que, además, 
	 * envía un correo al usuario con el detalle de los sparks 
	 */
	public void cobrarRenovacionesSparksDesarrollo(IContextoEjecucion contexto){
		UsuarioEng usuarioDAO = new UsuarioEng(conexion);		
		TransactionDEng transactionDDAO = new TransactionDEng(conexion);
		// 1. Recuperamos los usuarios activos de la plataforma
		List <UsuarioC> lstUsuarios = usuarioDAO.getAllActiveUsers();		
		
		// 2.  Para cada usuario obtenemos los objetos "transactiond"
		// que se correspondan con el próximo mes, que tengan el flag
		// 'fl_emailsent' marcado, y que no hayan sido cobrados ni renovados
		Calendar calRenovacion = Calendar.getInstance();
		calRenovacion.setTime(new Date());		
		calRenovacion.set(Calendar.DAY_OF_MONTH, calRenovacion.getActualMinimum(Calendar.DAY_OF_MONTH));
		for(int i = 0; i<lstUsuarios.size(); i++){
			UsuarioC usuario = lstUsuarios.get(i);
			
			List <TransactionDC> lstTransactions = transactionDDAO.getNotifiedButNotChargedTransactionsByUser(usuario.getCnUsuario(), calRenovacion);
			
			// 3. Obtener los datos que necesitamos de cada transacción para construir el listado detallado de sparks con sus precios
			// y actualizar los campos necesarios de la tabla transacción
			List <SparkPrice> lstSparkPrecio = new ArrayList<SparkPrice>();
			Float total = new Float(0);
			// Comprobar primero que el usuario tiene un billing id
			if(usuario.getStrBillingId() != null && !lstTransactions.isEmpty()){
				// Si tiene un billingId asegurar que el acuerdo no esté cancelado
				String ppresponse = "";
				// Recuperamos el billingid codificado
				String encodedBillingAgreementId = usuario.getStrBillingId();				
				StringEncrypterUtilities desencrypter = new StringEncrypterUtilities(Constantes.PASS_PHRASE);
				String billingAgreementId = desencrypter.decrypt(encodedBillingAgreementId);
				// Comprobar estado del acuerdo
				ppresponse = PaypalUtilities.getInstance().baUpdate(billingAgreementId);
				try{
					if(PaypalUtilities.getInstance().checkBillingAgreementStatus(ppresponse)){
				
						for(int j = 0; j<lstTransactions.size(); j++){
							TransactionDC transaction = lstTransactions.get(j);			
							
							String nombreSpark = transaction.getUsuarioSpark().getUsuarioSparkPk().getSpark().getStrNombre();
							BigDecimal bidAmount = new BigDecimal(transaction.getFloAmount()).setScale(2,BigDecimal.ROUND_UP);
							total += transaction.getFloAmount();
							String precioSpark = bidAmount.toString();
							
							SparkPrice sparkPrice = new SparkPrice(nombreSpark,precioSpark);
							
							lstSparkPrecio.add(sparkPrice);
							
							// 4. Actualizar el campo 'fl_charged' de la transacción
							// transaction.setChCharged('1');
							// 5. Actualizar el campo 'fl_renewed' de la transacción
							// transaction.setChRenewed('1');
							// transactionDDAO.update(transaction);
							// transactionDDAO.flush();				
						}
						
						if(!lstSparkPrecio.isEmpty()){
							// 4. Realizar el cobro del total de las transacciones de este usuario
							// DOREFERENCETRANSACTION				
							BigDecimal bd = new BigDecimal(total).setScale(2,BigDecimal.ROUND_UP);
													
							if(bd != null && bd.floatValue() > 0){
								// En este punto todo ha ido bien
								// Ejecutar cobro Papal
								ppresponse = PaypalUtilities.getInstance().doReferenceTransaction(billingAgreementId, bd.toString(),Constantes.getDEVEL_SPARKS_RENEWAL_ANNOTATION());
								
								// Tratar posibles errores en la respuesta					
								
								NVPDecoder resultValues = new NVPDecoder();
							
								// decode method of NVPDecoder will parse the request and decode the
								// name and value pair
								resultValues.decode(ppresponse);
					
								// checks for Acknowledgement and redirects accordingly to display
								// error messages
								String strAck = resultValues.get("ACK");
								if (strAck != null
										&& !(strAck.equals("Success") || strAck
												.equals("SuccessWithWarning"))) {
									// Error
									LOGGER.error("Se ha producido un error al intentar cobrar el spark al usuario " + usuario.getCnUsuario());
									PaypalUtilities.getInstance().gestionErroresDoReferenceTransaction(contexto, resultValues, usuario, usuario.getStrBillingId(), "doReferenceTransaction");								
								} else {
									// En este punto todo ha ido bien 
									// Actualizar transacciones del usuario
									for(int j = 0; j<lstTransactions.size(); j++){
										TransactionDC transaction = lstTransactions.get(j);
										// 5. Actualizar el campo 'fl_charged' de la transacción
										transaction.setChCharged('1');
										// 6. Actualizar el campo 'fl_renewed' de la transacción
										transaction.setChRenewed('1');
										transactionDDAO.update(transaction);
										transactionDDAO.flush();		
									}
									// 7. Enviar email
									SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
									String fecha = sdf.format(new Date());
									enviarNotificacionSparksDesarrollo(contexto,usuario,lstSparkPrecio,bd.toString(),"mail_user_notifcobro_sparks_desa-html","[FIONA] Development Sparks Renovation",fecha);
								}										
							}
						}					
					}else{
						// El acuerdo ha sido cancelado
						// El usuario tiene transacciones pendientes pero no disponemos de un identificador de contrato para pasarle el pago
						LOGGER.error("El usuario" + usuario.getCnUsuario() + "tiene un billingId que se corresponde con un acuerdo cancelado");
						enviarEmail(contexto,"mail_user_without_or_cancelled_billingId-html",usuario,"Problems trying to charge your no free sparks - Canceled agreement",usuario.getEmail());
						enviarEmail(contexto,"mail_team_without_or_cancelled_billingId-html",usuario,"Problems trying to charge a user no free sparks - Canceled agreement",Constantes.getMAIL_NOTIFICATION_ADDR());
					}
				}catch (PayPalException ppEx) {
					// TODO: handle exception
					ppEx.printStackTrace();
				}
			}else{
				// El usuario tiene transacciones pendientes pero no disponemos de un identificador de contrato para pasarle el pago
				if(usuario.getStrBillingId() == null && !lstTransactions.isEmpty()){
					LOGGER.error("El usuario" + usuario.getCnUsuario() + "no tiene BillingID pero tiene transacciones pendientes");
					enviarEmail(contexto,"mail_user_without_or_cancelled_billingId-html",usuario,"Problems trying to charge your no free sparks",usuario.getEmail());
					enviarEmail(contexto,"mail_team_without_or_cancelled_billingId-html",usuario,"Problems trying to charge a user no free sparks",Constantes.getMAIL_NOTIFICATION_ADDR());
				}
			}
		}		
	}	
	
	/**
	 * Método genérico para enviar emails usando una plantilla pasada como parámetro
	 * 
	 * @param contexto
	 * @param nombrePlantilla
	 * @param usuario
	 * @param subject
	 * @param destinatario
	 */
	private void enviarEmail(IContextoEjecucion contexto, String nombrePlantilla, UsuarioC usuario,
			String subject, String destinatario){		
		
		final Map<String, Object> model = new HashMap<String, Object>();		
		model.put("usuario", usuario);
				
		
		try {
			final String body = TemplateUtils.processTemplateIntoString(contexto, nombrePlantilla, model);
			
			Properties props = new Properties();

			// Nombre del host de correo, en este caso de adelerobots.com
			// TODO mover a properties
			props.setProperty("mail.smtp.host", "mail.adelerobots.com");

			// TLS si esta disponible
			props.setProperty("mail.smtp.starttls.enable", "false");

			// Puerto para envio de correos
			props.setProperty("mail.smtp.port", "25");

			// Nombre del usuario
			props.setProperty("mail.smtp.user", Constantes.getMAIL_SENDER_ADDR());

			// Si requiere o no usuario y password para conectarse.
			props.setProperty("mail.smtp.auth", "true");

			// Obtenemos instancia de sesion
			// y activamos el debug
			Session session = Session.getDefaultInstance(props);
			session.setDebug(true);

			// Construimos el mensaje a enviar
			MimeMessage message = new MimeMessage(session);
			
			// Preparamos el contenido HTML
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();

			messageBodyPart.setContent(body, "text/html; charset=UTF-8");
			multipart.addBodyPart(messageBodyPart);
			
			
			// Quien envia el correo
			message.setFrom(new InternetAddress(Constantes.getREGISTER_MAIL_SENDER(), "Fiona"));
			// A quien va dirigido
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
			// Asunto del mensaje
			message.setSubject(subject);
			// Texto del mensaje
			message.setText(body);
			// Contenido HTML
			message.setContent(multipart);
			
			// Para enviar el mensaje...

			// Indicamos el protocolo a usar
			Transport t = session.getTransport("smtp");
			// Establecer conexion indicando usuario y contraseña
			//propertizar
			t.connect(Constantes.getMAIL_SENDER_ADDR(), Constantes.getMAIL_SENDER_PW());

			// Completamos el envio
			t.sendMessage(message, message.getAllRecipients());
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	
		
	/**
	 * Método que determina a qué sparkers debe hacérseles un pago por sus sparks en
	 * desarrollo
	 * 
	 * @param contexto
	 */
	public void pagarRenovacionesSparksDesarrollo(IContextoEjecucion contexto){
		UsuarioEng usuarioDAO = new UsuarioEng(conexion);		
		TransactionDEng transactionDDAO = new TransactionDEng(conexion);
		// 1. Recuperamos los usuarios activos de la plataforma
		List <UsuarioC> lstUsuarios = usuarioDAO.getAllActiveUsers();		
		
		// 2.  Para cada usuario obtenemos los objetos "transactiond"
		// que se correspondan con el mes presente, que tengan los flags
		// 'fl_emailsent', 'fl_charged' y 'fl_renewed' a 1
		Calendar calRenovacion = Calendar.getInstance();
		calRenovacion.setTime(new Date());		
		calRenovacion.add(Calendar.MONTH, -1);
		calRenovacion.set(Calendar.DAY_OF_MONTH, calRenovacion.getActualMinimum(Calendar.DAY_OF_MONTH));
		List <PaymentReceiver> receivers = new ArrayList<PaymentReceiver>();
		for(int i = 0; i<lstUsuarios.size(); i++){
			UsuarioC usuario = lstUsuarios.get(i);
			
			// Creamos un objeto de tipo destinatario de pago
			PaymentReceiver paymentReceiver = new PaymentReceiver();
			paymentReceiver.setEmail(usuario.getPaypalEmail());
			paymentReceiver.setNote(Constantes.getMASSPAY_DEVELOPMENT_ANNOTATION());
			
			List <TransactionDC> lstTransactions = transactionDDAO.getNotPaidTransactionsByUser(usuario.getCnUsuario(), calRenovacion);
			
			// 3. Obtener los datos que necesitamos de cada transacción para construir el listado detallado de sparks con sus precios
			// y actualizar los campos necesarios de la tabla transacción					
			for(int j = 0; j < lstTransactions.size(); j++){
				TransactionDC transaction = lstTransactions.get(j);
				
				// 4. Actualizar el campo 'fl_paid' de la transacción
				transaction.setChPaid('1');				
				transactionDDAO.update(transaction);
				transactionDDAO.flush();				
			}
			
			// 5. Enviar email
			List <SparkPrice> lstSparkPrecio = new ArrayList<SparkPrice>();
			Float total = new Float(0);
			if(!lstTransactions.isEmpty()){				
				// Calcular el porcentaje que se le paga al sparker en función del tipo de cuenta
				// Free: 40%
				// Basic: 30%
				// Pro: 20%
				// Corporate: 15%
				Float porcentaje = 100 - getComision(usuario.getCuentaId());
				List <Object[]> lstNamePriceAmount = transactionDDAO.getTransactionsDeveloperGroupBySparks(usuario.getCnUsuario(), porcentaje,calRenovacion);
				for(int k = 0; k<lstNamePriceAmount.size(); k++){
					Object [] fila = lstNamePriceAmount.get(k);
					String nombreSpark = (String)fila[0];
					Double columna2 = (Double)fila[1];
					BigDecimal precioSpark = new BigDecimal(columna2).setScale(2,BigDecimal.ROUND_UP);
					BigInteger cantidad = (BigInteger)fila[2];
					
					SparkPrice sparkPrice = new SparkPrice(nombreSpark,precioSpark.toString(),cantidad.toString());
					
					sparkPrice.setComision((getComision(usuario.getCuentaId()).toString()));
					
					Float precioSparkUnitario = new Float(((precioSpark.floatValue() / cantidad.floatValue())*100)/porcentaje);
					
					BigDecimal bidPrecioUnitario = new BigDecimal(precioSparkUnitario.floatValue()).setScale(2,BigDecimal.ROUND_UP);
					
					sparkPrice.setPrecioSparkUnitario(bidPrecioUnitario.toString());
					
					sparkPrice.setPrecioSparkSinDescuento((new BigDecimal(precioSparkUnitario.floatValue()*cantidad.floatValue()).setScale(2, BigDecimal.ROUND_UP)).toString());
					
					lstSparkPrecio.add(sparkPrice);
					
					total += columna2.floatValue();
				}
				
				BigDecimal bd = new BigDecimal(total).setScale(2,BigDecimal.ROUND_UP);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String fecha = sdf.format(new Date());
				enviarNotificacionSparksDesarrollo(contexto,usuario,lstSparkPrecio,bd.toString(),"mail_user_notifpago_sparks_desa-html","[FIONA] Payment for the sparks you have developed",fecha);
				paymentReceiver.setAmount(bd.toString());
				receivers.add(paymentReceiver);				
			}			
		}
		// Comprobar el sistema MASS_PAY a utilizar
		if(!receivers.isEmpty()){
			if(receivers.size() <= 250){
				// Llamada a la API de Paypal
				String ppresponse = "";
				try{
					ppresponse = PaypalUtilities.getInstance().massPay(receivers);		
					
					// Tratar posibles errores en la respuesta					
					
					NVPDecoder resultValues = new NVPDecoder();
				
					// decode method of NVPDecoder will parse the request and decode the
					// name and value pair
					resultValues.decode(ppresponse);
		
					// checks for Acknowledgement and redirects accordingly to display
					// error messages
					String strAck = resultValues.get("ACK");
					if (strAck != null
							&& !(strAck.equals("Success") || strAck
									.equals("SuccessWithWarning"))) {
						// Error
						LOGGER.error("Se ha producido un error al intentar realizar el pago de sparks en desarrollo - pagarRenovacionesSparksDesarrollo");
						PaypalUtilities.getInstance().gestionErroresMassPay(contexto, resultValues, "MassPay");								
					} else {
						// En este punto todo ha ido bien				
					}		
				}catch(PayPalException ppEx){
					
				}
				
			}else{
				// Construcción de un archivo que se subirá a Paypal								
				Calendar cal = Calendar.getInstance();
				String nombreArchivo = Constantes.getMASSPAY_FILES_PATH() + "/" + "massPay_" + cal.get(Calendar.YEAR) + (cal.get(Calendar.MONTH)+1) +cal.get(Calendar.DAY_OF_MONTH) +".csv";
				PaypalUtilities.getInstance().generateCSVMassPayFile(nombreArchivo,receivers);
				// Enviarnos email a nosotros mismos para recordar subir archivo a Paypal y enviar los pagos
				PaypalUtilities.getInstance().enviarNotificacionArchivoMassPay(contexto, "mail_team_notif_masspay_file-html", nombreArchivo, "[FIONA] New MassPay File for development sparks has been created !!", Constantes.getMAIL_NOTIFICATION_ADDR());
			}
		}
	}
	
	/**
	 * Método que permite enviar la notificación al usuario con todos los sparks que se le han cobrado
	 * 
	 * @param contexto
	 * @param usuario
	 * @param lstSparksPrecios
	 */
	private void enviarNotificacionSparksDesarrollo(IContextoEjecucion contexto, UsuarioC usuario, List<SparkPrice> lstSparksPrecios,
			String total, String nombrePlantilla, String subject, String fecha){		
		
		final Map<String, Object> model = new HashMap<String, Object>();		
		model.put("usuario", usuario);
		model.put("lstSparksPrecios", lstSparksPrecios);
		model.put("total", total);
		if(fecha!= null && !"".equals(fecha))
			model.put("fecha", fecha);
		
		try {
			final String body = TemplateUtils.processTemplateIntoString(contexto, nombrePlantilla, model);
			
			Properties props = new Properties();

			// Nombre del host de correo, en este caso de adelerobots.com
			// TODO mover a properties
			props.setProperty("mail.smtp.host", "mail.adelerobots.com");

			// TLS si esta disponible
			props.setProperty("mail.smtp.starttls.enable", "false");

			// Puerto para envio de correos
			props.setProperty("mail.smtp.port", "25");

			// Nombre del usuario
			props.setProperty("mail.smtp.user", Constantes.getMAIL_SENDER_ADDR());

			// Si requiere o no usuario y password para conectarse.
			props.setProperty("mail.smtp.auth", "true");

			// Obtenemos instancia de sesion
			// y activamos el debug
			Session session = Session.getDefaultInstance(props);
			session.setDebug(true);

			// Construimos el mensaje a enviar
			MimeMessage message = new MimeMessage(session);
			
			// Preparamos el contenido HTML
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();

			messageBodyPart.setContent(body, "text/html; charset=UTF-8");
			multipart.addBodyPart(messageBodyPart);
			
			
			// Quien envia el correo
			message.setFrom(new InternetAddress(Constantes.getREGISTER_MAIL_SENDER(), "Fiona"));
			// A quien va dirigido
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(usuario.getEmail()));
			// Asunto del mensaje
			message.setSubject(subject);
			// Texto del mensaje
			message.setText(body);
			// Contenido HTML
			message.setContent(multipart);
			
			// Para enviar el mensaje...

			// Indicamos el protocolo a usar
			Transport t = session.getTransport("smtp");
			// Establecer conexion indicando usuario y contraseña
			//propertizar
			t.connect(Constantes.getMAIL_SENDER_ADDR(), Constantes.getMAIL_SENDER_PW());

			// Completamos el envio
			t.sendMessage(message, message.getAllRecipients());
			
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (TemplateException e) {			
			e.printStackTrace();
		} catch (MessagingException e) {			
			e.printStackTrace();
		}
		
	}
	
	private Float getComision(Integer intCodCuenta){
		
		Float comision = new Float(0); 
		switch(intCodCuenta){
			case Constantes.CTE_ACCOUNT_FREE:
				comision = Float.parseFloat(Constantes.getCOMISION_FREE());						
				break;
			case Constantes.CTE_ACCOUNT_BASIC:
				comision = Float.parseFloat(Constantes.getCOMISION_BASIC());
				break;
			case Constantes.CTE_ACCOUNT_PRO:
				comision = Float.parseFloat(Constantes.getCOMISION_PRO());
				break;
			case Constantes.CTE_ACCOUNT_CORPORATE:
				comision = Float.parseFloat(Constantes.getCOMISION_CORPORATE());
				break;
		}
		
		return comision;
		
	}
	
	
	
	// ##############################################################################################
	// ############################## MÉTODOS BATCH PRODUCCIÓN ######################################
	// ##############################################################################################
	
	/**
	 * Método que planifica las renovaciones de sparks en desarrollo
	 * Este método "ignora" aquellos usuariosparks cuyo propietario
	 * sea el mismo que desarrolló el spark
	 */
	public void planificarRenovacionesSparksProduccion(){
		//final UsuarioSparkEng usuarioSparkDAO = new UsuarioSparkEng(conexion);
		final AvatarSparkEng avatarSparkDAO = new AvatarSparkEng(conexion);		
		
		//List <UsuarioSparkC> lstUsuariosSpark = usuarioSparkDAO.getTimeRenewableProductionUsuarioSparks();
		List <AvatarSparkC> lstAvatarsSpark = avatarSparkDAO.getTimeRenewableProductionAvatarSparks();
		
		Date fechaToday = new Date();
		Calendar calToday = Calendar.getInstance();
		calToday.setTime(fechaToday);
		calToday.set(Calendar.HOUR_OF_DAY, 0);
		calToday.set(Calendar.MINUTE, 0);
		calToday.set(Calendar.SECOND, 0);
		calToday.set(Calendar.MILLISECOND, 0);

		
		for(int i = 0; i < lstAvatarsSpark.size(); i++){			
			AvatarSparkC avatarSpark = lstAvatarsSpark.get(i);
			
			// Sólo se renovarán el próximo mes los sparks de renovación
			// mensual no gratuitos y los anuales si estamos en diciembre
			// Añadido del día 14-11-2013
			// No se planificarán aquellos avatares pertenecientes a usuarios
			// del backoffice, que además tengan una fecha de subida (FE_UPDATE)
			// posterior o igual(día mes año sin horas) a la fecha en la que se
			// ejecuta este batch
			//UsuarioC usuario = avatarSpark.getAvatar().getUsuario();
			Date fechaUploadAvatar = avatarSpark.getAvatar().getDatUpload();
			Calendar calUpload = Calendar.getInstance();
			calUpload.setTime(fechaUploadAvatar);	
			calUpload.set(Calendar.HOUR_OF_DAY, 0);
			calUpload.set(Calendar.MINUTE, 0);
			calUpload.set(Calendar.SECOND, 0);
			calUpload.set(Calendar.MILLISECOND, 0);
			// De momento no coloco la opción del Backoffice porque
			// parece innecesaria. Este batch tiene que ejecutarse para
			// todos los usuarios con independencia de que pertenezcan
			// o no al backoffice. La particularidad estriba en que los usuarios
			// del backoffice tendrán una fecha de subida "trucada" con 30 días
			// más de la fecha actual
			if(calToday.getTimeInMillis()>calUpload.getTimeInMillis()){
			
				Float price = avatarSpark.getFloPrice();
				HostingC hosting = avatarSpark.getAvatarSparkPk().getAvatar().getHosting();
				// Corregido el día 25-09-2013 porque si seleccionamos sólo aquellos cuyo precio
				// sea distinto de cero no se cobrará el hosting de los avatares que estén compuestos
				// por sparks gratuitos
				//if(price!=null && price > 0){			
				if(price!=null){
					if(hosting.getIntCodUnit().equals(Constantes.MONTHS) || (hosting.getIntCodUnit().equals(Constantes.YEARS) && calToday.get(Calendar.MONTH)==Calendar.DECEMBER)){
						BillingPC billingP = new BillingPC();
						
						// billingP.setUsuarioSpark(usuarioSpark);
						Calendar calRenovacion = Calendar.getInstance();
						calRenovacion.add(Calendar.MONTH, 1);
						calRenovacion.set(Calendar.DAY_OF_MONTH, calRenovacion.getActualMinimum(Calendar.DAY_OF_MONTH));					
						billingP.setDatRenovation(calRenovacion.getTime());
						
						billingP.setAvatarSpark(avatarSpark);
						billingP.setChCharged('0');
						billingP.setChEmailSent('0');
						billingP.setChPaid('0');
						billingP.setChRenewed('0');					
						// El precio del spark en producción no es el precio asociado al spark, sino el precio seleccionado en función de las opciones
						// en el momento de hacer el upload (tabla hosting)
						billingP.setFloAmount(avatarSpark.getFloPrice());					
						
						BillingPEng billingPDAO = new BillingPEng(conexion);
						billingPDAO.insert(billingP);
						billingPDAO.flush();						
					}
				}
			}
			
		}
		
	}
	
	/**
	 * Método que planifica y ejecuta los primeros pagos prorrateados
	 * del hosting de los usuarios de TCRF
	 */
	public void planificarPrimerPagoTCRFProduccion(IContextoEjecucion contexto){		
		final AvatarEng avatarDAO = new AvatarEng(conexion);
		
		List <AvatarC> lstAvatars = avatarDAO.getActiveAvatarsFromTCRFUsers();
		
		Date fechaToday = new Date();
		Calendar calToday = Calendar.getInstance();
		calToday.setTime(fechaToday);
		calToday.set(Calendar.HOUR_OF_DAY, 0);
		calToday.set(Calendar.MINUTE, 0);
		calToday.set(Calendar.SECOND, 0);
		calToday.set(Calendar.MILLISECOND, 0);

		
		for(int i = 0; i < lstAvatars.size(); i++){			
			AvatarC avatar = lstAvatars.get(i);
			UsuarioC usuario = avatar.getUsuario();
			// Sólo se planificarán aquellos avatares pertenecientes a usuarios
			// del backoffice, que además tengan una fecha de subida (FE_UPDATE)
			// igual(día mes año sin horas) a la fecha en la que se
			// ejecuta este batch
			//UsuarioC usuario = avatarSpark.getAvatar().getUsuario();
			Date fechaUploadAvatar = avatar.getDatUpload();
			Calendar calUpload = Calendar.getInstance();
			calUpload.setTime(fechaUploadAvatar);	
			calUpload.set(Calendar.HOUR_OF_DAY, 0);
			calUpload.set(Calendar.MINUTE, 0);
			calUpload.set(Calendar.SECOND, 0);
			calUpload.set(Calendar.MILLISECOND, 0);
			// Este batch tiene que ejecutarse para
			// todos los usuarios del backoffice cuya fecha de upload del avatar se
			// corresponda con el día de hoy (ejecución del batch)
			if(calToday.getTimeInMillis()==calUpload.getTimeInMillis()){				
				HostingC hosting = avatar.getHosting();
				
				if(hosting.getIntCodUnit().equals(Constantes.MONTHS) || (hosting.getIntCodUnit().equals(Constantes.YEARS) && calToday.get(Calendar.MONTH)==Calendar.DECEMBER)){
					// Prorratear hosting
					String ppresponse = "";
					Float precio = new Float(0);
					float cantidad = hosting.getFloFee();
					if(hosting.getIntCodUnit().equals(Constantes.MONTHS)){
						// Fecha último día del mes
						Calendar calLastDayOfMonth = Calendar.getInstance();
						calLastDayOfMonth.set(calLastDayOfMonth.get(Calendar.YEAR),
								calLastDayOfMonth.get(Calendar.MONTH),
								calLastDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH),
								0,// HOUR_OF_DAY
								0,//MINUTE
								0);//SECOND
						
						// Número de días que faltan para final de mes
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(calLastDayOfMonth.getTime().getTime() - calToday.getTime().getTime());
						Integer intNumDiasACobrar = cal.get(Calendar.DAY_OF_YEAR);
						precio = (cantidad*intNumDiasACobrar)/30;
					}else{
						// Fecha último día del mes
						Calendar calLastDayOfYear = Calendar.getInstance();
						calLastDayOfYear.set(calLastDayOfYear.get(Calendar.YEAR),
								calLastDayOfYear.getActualMaximum(Calendar.MONTH),
								calLastDayOfYear.getActualMaximum(Calendar.DAY_OF_MONTH),
								0,// HOUR_OF_DAY
								0,//MINUTE
								0);//SECOND
						
						// Número de días que faltan para final de año
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(calLastDayOfYear.getTime().getTime() - calToday.getTime().getTime());
						Integer intNumDiasACobrar = cal.get(Calendar.DAY_OF_YEAR);
						precio = (cantidad*intNumDiasACobrar)/365;
					}
					
					// Llamada a la API de Paypal
					// Comprobar estado del acuerdo
					// Recuperamos el billingid codificado
					String encodedBillingAgreementId = usuario.getStrBillingId();
					StringEncrypterUtilities desencrypter = new StringEncrypterUtilities(Constantes.PASS_PHRASE);
					String billingAgreementId = desencrypter.decrypt(encodedBillingAgreementId);		
					ppresponse = PaypalUtilities.getInstance().baUpdate(billingAgreementId);
					try{
						if(PaypalUtilities.getInstance().checkBillingAgreementStatus(ppresponse)){
							// En este punto todo ha ido bien
							// Ejecutar cobro Papal
							// Formatear la cantidad a dos decimales
							BigDecimal bidCantidad = new BigDecimal(precio.floatValue()).setScale(2,BigDecimal.ROUND_UP);
							ppresponse = PaypalUtilities.getInstance().doReferenceTransaction(billingAgreementId, bidCantidad.toString(),Constantes.getUPLOAD_PRODUCTION_ANNOTATION());
							
							// Tratar posibles errores en la respuesta					
							
							NVPDecoder resultValues = new NVPDecoder();
						
							// decode method of NVPDecoder will parse the request and decode the
							// name and value pair
							resultValues.decode(ppresponse);
				
							// checks for Acknowledgement and redirects accordingly to display
							// error messages
							String strAck = resultValues.get("ACK");
							if (strAck != null
									&& !(strAck.equals("Success") || strAck
											.equals("SuccessWithWarning"))) {
								// Error
								LOGGER.error("Se ha producido un error al intentar cobrar el spark al usuario " + usuario.getCnUsuario());
								PaypalUtilities.getInstance().gestionErroresDoReferenceTransaction(contexto, resultValues, usuario, usuario.getStrBillingId(), "doReferenceTransaction");
								throw new RollbackException();
							} else {
								// En este punto todo ha ido bien								
								// Comprobamos si la fecha de hoy es posterior a la fecha en que se realizan las planificaciones
								// de producción, para realizar la planificación "manualmente" en ese caso
								Calendar calPlanificacionProduccion = Calendar.getInstance();
								calPlanificacionProduccion.setTime(new Date());
								Integer diaPlaniProd = Integer.parseInt(Constantes.getFECHA_PLANI_PROD()); 
								calPlanificacionProduccion.set(Calendar.DAY_OF_MONTH, diaPlaniProd);
								
								if(calToday.getTimeInMillis()>calPlanificacionProduccion.getTimeInMillis()){
									// Realizar planificación porque la fecha de hoy es posterior
									// a la fecha de planificación del próximo mes
									planificarAvatarProd(avatar);
								}	
								
							}
							
						}else{
							// Error
							LOGGER.error("Se ha producido un error al intentar cobrar el paso a producción del avatar - comprobación estado acuerdo " + usuario.getCnUsuario());
							NVPDecoder resultValues = new NVPDecoder();								
							
							resultValues.decode(ppresponse);
							PaypalUtilities.getInstance().gestionErroresDoReferenceTransaction(contexto, resultValues, usuario, usuario.getStrBillingId(), "baUpdate");
							throw new RollbackException();
						}
					}catch (PayPalException ppEx) {
						// TODO: handle exception
						ppEx.printStackTrace();
					}
				}
				
			}
			
		}
		
	}
	
	
	/**
	 * Método que envía a los usuarios un correo con el resumen de los sparks en producción que 
	 * serán renovados al mes siguiente
	 */
	public void notificarRenovacionesSparksProduccion(IContextoEjecucion contexto){
		UsuarioEng usuarioDAO = new UsuarioEng(conexion);		
		BillingPEng billingPDAO = new BillingPEng(conexion);
		
		// 1. Recuperamos los usuarios activos de la plataforma
		List <UsuarioC> lstUsuarios = usuarioDAO.getAllActiveUsers();
				
		
		// 2.  Para cada usuario obtenemos las transacciones de producción
		// que se correspondan con el próximo mes, que no tengan el flag
		// 'fl_emailsent' marcado, y que no hayan sido cobrados
		Calendar calRenovacion = Calendar.getInstance();
		calRenovacion.setTime(new Date());
		calRenovacion.add(Calendar.MONTH, 1);
		calRenovacion.set(Calendar.DAY_OF_MONTH, calRenovacion.getActualMinimum(Calendar.DAY_OF_MONTH));
		for(int i = 0; i<lstUsuarios.size(); i++){
			UsuarioC usuario = lstUsuarios.get(i);
			
			List <BillingPC> lstbillingP = billingPDAO.getNotNotifiedBillingPByUser(usuario.getCnUsuario(), calRenovacion);
			
			// 3. Actualizar los flags necesarios del objeto BillingP y construir la lista de avatares para el envío del email
			// a los usuarios					
			List <AvatarC> lstAvatars = new ArrayList<AvatarC>();
			Float granTotal = new Float(0);
			for(int j = 0; j < lstbillingP.size(); j++){
				BillingPC billingP = lstbillingP.get(j);
				// Recuperamos el avatar relacionado				
				AvatarC avatar = billingP.getAvatarSpark().getAvatar();
				// Comprobamos si el avatar existe en la lista
				if(!lstAvatars.contains(avatar)){
					lstAvatars.add(avatar);					
				}							
				
				// 4. Actualizar el campo 'fl_emailsent' de la transacción
				billingP.setChEmailSent('1');
				billingPDAO.update(billingP);
				billingPDAO.flush();				
			}
			
			List<AvatarPrice> lstAvatarPrecio = new ArrayList<AvatarPrice>();
			for(int j = 0; j< lstAvatars.size();j++){
				AvatarC avatar = lstAvatars.get(j);
				
				AvatarPrice avatarPrice = new AvatarPrice();
				avatarPrice.setNombreAvatar("Av_"+avatar.getIntCodAvatar().toString());
				
				// 5. Construir el listado detallado que se enviará en el email
				List <SparkPrice> lstSparkPrecio = new ArrayList<SparkPrice>();
				Float total = new Float(0);
				
				for(int k = 0; k < avatar.getLstAvatarSparkC().size(); k++){
					AvatarSparkC avatarSpark = avatar.getLstAvatarSparkC().get(k);
					String nombreSpark = avatarSpark.getAvatarSparkPk().getSpark().getStrNombre();
					BigDecimal bidAmount = new BigDecimal(avatarSpark.getFloPrice()).setScale(2,BigDecimal.ROUND_UP);
					total += avatarSpark.getFloPrice();
					String precioSpark = bidAmount.toString();
					
					SparkPrice sparkPrice = new SparkPrice(nombreSpark,precioSpark);
					
					lstSparkPrecio.add(sparkPrice);
				}
				
				// 6. Añadir el precio del hosting como si de un spark más se tratara
				SparkPrice sparkHosting = new SparkPrice();
				sparkHosting.setNombreSpark("Hosting");
				BigDecimal bidAmount = new BigDecimal(avatar.getHosting().getFloFee()).setScale(2,BigDecimal.ROUND_UP);
				total += avatar.getHosting().getFloFee();	
				BigDecimal bidTotalAvatarAmount = new BigDecimal(total).setScale(2,BigDecimal.ROUND_UP);
				avatarPrice.setPrecioTotalAvatar(bidTotalAvatarAmount.toString());
				granTotal += total;
				String precioHosting = bidAmount.toString();
				sparkHosting.setPrecioSpark(precioHosting);
				lstSparkPrecio.add(sparkHosting);
				
				// 7. Añadir la lista de sparks detallados al avatar
				avatarPrice.setLstSparksPrice(lstSparkPrecio);
				
				// 8. Añadir el avatarPrice a la lista de avatares del usuario
				lstAvatarPrecio.add(avatarPrice);
			}		
		

			// 9. Enviar email
			if(!lstAvatarPrecio.isEmpty()){
				BigDecimal bd = new BigDecimal(granTotal).setScale(2,BigDecimal.ROUND_UP);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String fecha = sdf.format(new Date());
				enviarNotificacionSparksProduccion(contexto,usuario,lstAvatarPrecio,bd.toString(),"mail_user_notif_sparks_prod-html","[FIONA] Production Avatars Renovation",fecha);
			}
		}		
		
	}	
	
	/**
	 * Método que ejecuta el cobro de la cantidad total que el usuario debe pagar
	 * este mes por todos los sparks que corresponda renovar y que, además, 
	 * envía un correo al usuario con el detalle de los sparks 
	 */
	public void cobrarRenovacionesSparksProduccion(IContextoEjecucion contexto){
		UsuarioEng usuarioDAO = new UsuarioEng(conexion);		
		//AvatarEng avatarDAO = new AvatarEng(conexion);
		BillingPEng billingPDAO = new BillingPEng(conexion);
		
		// 1. Recuperamos los usuarios activos de la plataforma
		List <UsuarioC> lstUsuarios = usuarioDAO.getAllActiveUsers();
				
		
		// 2.  Para cada usuario obtenemos los objetos "avatar"
		// que se correspondan con el próximo mes, que no tengan el flag
		// 'fl_emailsent' marcado, y que no hayan sido cobrados
		Calendar calRenovacion = Calendar.getInstance();
		calRenovacion.setTime(new Date());		
		calRenovacion.set(Calendar.DAY_OF_MONTH, calRenovacion.getActualMinimum(Calendar.DAY_OF_MONTH));
		for(int i = 0; i<lstUsuarios.size(); i++){
			UsuarioC usuario = lstUsuarios.get(i);			
			List <BillingPC> lstBillingPs = billingPDAO.getNotifiedButNotChargedBillingPByUser(usuario.getCnUsuario(), calRenovacion);
			
			// 3. Obtener los datos que necesitamos de cada avatar para construir el listado detallado de avatares y sparks con sus precios
			// y actualizar los campos necesarios de la tabla billingp			
			// Comprobar primero que el usuario tiene un billing id
			if(usuario.getStrBillingId() != null && !lstBillingPs.isEmpty()){
				// Si tiene un billingId asegurar que el acuerdo no esté cancelado
				String ppresponse = "";
				// Recuperamos el billingid codificado
				String encodedBillingAgreementId = usuario.getStrBillingId();				
				StringEncrypterUtilities desencrypter = new StringEncrypterUtilities(Constantes.PASS_PHRASE);
				String billingAgreementId = desencrypter.decrypt(encodedBillingAgreementId);
				// Comprobar estado del acuerdo
				ppresponse = PaypalUtilities.getInstance().baUpdate(billingAgreementId);
				try{
					if(PaypalUtilities.getInstance().checkBillingAgreementStatus(ppresponse)){
						List <AvatarC> lstAvatars = new ArrayList<AvatarC>();
						for(int j = 0; j < lstBillingPs.size(); j++){
							BillingPC billingP = lstBillingPs.get(j);						
							
							// Recuperamos el avatar relacionado				
							AvatarC avatar = billingP.getAvatarSpark().getAvatar();
							// Comprobamos si el avatar existe en la lista
							if(!lstAvatars.contains(avatar)){
								lstAvatars.add(avatar);					
							}		
														
//							billingP.setChCharged('1');
//							billingP.setChRenewed('1');
//							billingPDAO.update(billingP);
//							billingPDAO.flush();							
						}
						
						List<AvatarPrice> lstAvatarPrecio = new ArrayList<AvatarPrice>();
						Float granTotal = new Float(0);
						for(int j=0; j < lstAvatars.size(); j++){
							AvatarC avatar = lstAvatars.get(j);			
							AvatarPrice avatarPrice = new AvatarPrice();
							avatarPrice.setNombreAvatar("Av_"+avatar.getIntCodAvatar().toString());
							// 4. Construir el listado detallado que se enviará en el email
							List <SparkPrice> lstSparkPrecio = new ArrayList<SparkPrice>();
							Float total = new Float(0);
							
							for(int k = 0; k < avatar.getLstAvatarSparkC().size(); k++){
								AvatarSparkC avatarSpark = avatar.getLstAvatarSparkC().get(k);
								String nombreSpark = avatarSpark.getAvatarSparkPk().getSpark().getStrNombre();
								BigDecimal bidAmount = new BigDecimal(avatarSpark.getFloPrice()).setScale(2,BigDecimal.ROUND_UP);
								total += avatarSpark.getFloPrice();
								String precioSpark = bidAmount.toString();
								
								SparkPrice sparkPrice = new SparkPrice(nombreSpark,precioSpark);
								
								lstSparkPrecio.add(sparkPrice);
							}
							
							// 5. Añadir el precio del hosting como si de un spark más se tratara
							SparkPrice sparkHosting = new SparkPrice();
							sparkHosting.setNombreSpark("Hosting");
							BigDecimal bidAmount = new BigDecimal(avatar.getHosting().getFloFee()).setScale(2,BigDecimal.ROUND_UP);
							total += avatar.getHosting().getFloFee();	
							BigDecimal bidTotalAvatarAmount = new BigDecimal(total).setScale(2,BigDecimal.ROUND_UP);
							avatarPrice.setPrecioTotalAvatar(bidTotalAvatarAmount.toString());
							granTotal += total;
							String precioHosting = bidAmount.toString();
							sparkHosting.setPrecioSpark(precioHosting);
							lstSparkPrecio.add(sparkHosting);
							
							// 6. Añadir la lista de sparks detallados al avatar
							avatarPrice.setLstSparksPrice(lstSparkPrecio);
							
							// 7. Añadir el avatarPrice a la lista de avatares del usuario
							lstAvatarPrecio.add(avatarPrice);
						}
						// 8. Realizar el cobro al usuario por el total de todos los avatares
						BigDecimal bd = new BigDecimal(granTotal).setScale(2,BigDecimal.ROUND_UP);
						ppresponse = PaypalUtilities.getInstance().doReferenceTransaction(billingAgreementId, bd.toString(),Constantes. getPROD_SPARKS_RENEWAL_ANNOTATION());
						
						// Tratar posibles errores en la respuesta					
						
						NVPDecoder resultValues = new NVPDecoder();
					
						// decode method of NVPDecoder will parse the request and decode the
						// name and value pair
						resultValues.decode(ppresponse);
			
						// checks for Acknowledgement and redirects accordingly to display
						// error messages
						String strAck = resultValues.get("ACK");
						if (strAck != null
								&& !(strAck.equals("Success") || strAck
										.equals("SuccessWithWarning"))) {
							// Error
							LOGGER.error("Se ha producido un error al intentar cobrar los avatares en producción al usuario " + usuario.getCnUsuario());
							PaypalUtilities.getInstance().gestionErroresDoReferenceTransaction(contexto, resultValues, usuario, usuario.getStrBillingId(), "doReferenceTransaction");								
						} else {
							// En este punto todo ha ido bien 
							// Actualizar los billingp (transacciones) del usuario
							for(int j = 0; j < lstBillingPs.size(); j++){
								BillingPC billingP = lstBillingPs.get(j);
								// 9. Actualizar campo 'fl_charged'
								billingP.setChCharged('1');
								// 10. Actualizar campo 'fl_renewed'
								billingP.setChRenewed('1');
								billingPDAO.update(billingP);
								billingPDAO.flush();
							}
							// 11. Enviar email		
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							String fecha = sdf.format(new Date());
							enviarNotificacionSparksProduccion(contexto,usuario,lstAvatarPrecio,bd.toString(),"mail_user_notifcobro_sparks_prod-html","[FIONA] Production Sparks Renovation",fecha);
						}					
						
					}else{						
						// El acuerdo ha sido cancelado
						// El usuario tiene transacciones pendientes pero no disponemos de un identificador de contrato para pasarle el pago
						LOGGER.error("El usuario" + usuario.getCnUsuario() + "tiene un billingId que se corresponde con un acuerdo cancelado");
						enviarEmail(contexto,"mail_user_without_or_cancelled_billingId-html",usuario,"Problems trying to charge your no free avatars on production environment - Canceled agreement",usuario.getEmail());
						enviarEmail(contexto,"mail_team_without_or_cancelled_billingId-html",usuario,"Problems trying to charge a user no free avatars on production environment - Canceled agreement",Constantes.getMAIL_NOTIFICATION_ADDR());
					}
				}catch(PayPalException ppEx){
					// TODO: handle exception
					ppEx.printStackTrace();
				}
			
			}
			
		}
	}
	
	/**
	 * Método que determina a qué sparkers debe hacérseles un pago por sus avatares en
	 * produccion
	 * 
	 * @param contexto
	 */
	public void pagarRenovacionesSparksProduccion(IContextoEjecucion contexto){
		UsuarioEng usuarioDAO = new UsuarioEng(conexion);		
		BillingPEng billingPDAO = new BillingPEng(conexion);
		// 1. Recuperamos los usuarios activos de la plataforma
		List <UsuarioC> lstUsuarios = usuarioDAO.getAllActiveUsers();		
		
		// 2.  Para cada usuario obtenemos los objetos "transactiond"
		// que se correspondan con el mes presente, que tengan los flags
		// 'fl_emailsent', 'fl_charged' y 'fl_renewed' a 1
		Calendar calRenovacion = Calendar.getInstance();
		calRenovacion.setTime(new Date());	
		calRenovacion.add(Calendar.MONTH, -1);
		calRenovacion.set(Calendar.DAY_OF_MONTH, calRenovacion.getActualMinimum(Calendar.DAY_OF_MONTH));
		List <PaymentReceiver> receivers = new ArrayList<PaymentReceiver>();
		for(int i = 0; i<lstUsuarios.size(); i++){
			UsuarioC usuario = lstUsuarios.get(i);
			
			// Creamos un objeto de tipo destinatario de pago
			PaymentReceiver paymentReceiver = new PaymentReceiver();
			paymentReceiver.setEmail(usuario.getPaypalEmail());
			paymentReceiver.setNote(Constantes.getMASSPAY_PRODUCTION_ANNOTATION());
			
			List <BillingPC> lstBillingPs = billingPDAO.getNotPaidBillingPByUser(usuario.getCnUsuario(), calRenovacion);
			
			// 3. Obtener los datos que necesitamos de cada transacción para construir el listado detallado de sparks con sus precios
			// y actualizar los campos necesarios de la tabla transacción					
			for(int j = 0; j < lstBillingPs.size(); j++){
				BillingPC billingP = lstBillingPs.get(j);
				
				// 4. Actualizar el campo 'fl_paid' de la transacción
				billingP.setChPaid('1');				
				billingPDAO.update(billingP);
				billingPDAO.flush();				
			}
			
			// 5. Enviar email
			List <SparkPrice> lstSparkPrecio = new ArrayList<SparkPrice>();
			Float total = new Float(0);
			if(!lstBillingPs.isEmpty()){				
				// Calcular el porcentaje que se le paga al sparker en función del tipo de cuenta
				// Free: 40%
				// Basic: 30%
				// Pro: 20%
				// Corporate: 15%
				Float porcentaje = 100 - getComision(usuario.getCuentaId());
				List <Object[]> lstNamePriceAmount = billingPDAO.getBillingPDeveloperGroupBySparks(usuario.getCnUsuario(), porcentaje,calRenovacion);
				for(int k = 0; k<lstNamePriceAmount.size(); k++){
					Object [] fila = lstNamePriceAmount.get(k);
					String nombreSpark = (String)fila[0];
					Double columna2 = (Double)fila[1];
					BigDecimal precioSpark = new BigDecimal(columna2).setScale(2,BigDecimal.ROUND_UP);
					BigInteger cantidad = (BigInteger)fila[2];
					
					SparkPrice sparkPrice = new SparkPrice(nombreSpark,precioSpark.toString(),cantidad.toString());
					
					sparkPrice.setComision((getComision(usuario.getCuentaId()).toString()));
					
					Float precioSparkUnitario = new Float(((precioSpark.floatValue() / cantidad.floatValue())*100)/porcentaje);
					
					BigDecimal bidPrecioUnitario = new BigDecimal(precioSparkUnitario.floatValue()).setScale(2,BigDecimal.ROUND_UP);
					
					sparkPrice.setPrecioSparkUnitario(bidPrecioUnitario.toString());
					
					sparkPrice.setPrecioSparkSinDescuento((new BigDecimal(precioSparkUnitario.floatValue()*cantidad.floatValue()).setScale(2, BigDecimal.ROUND_UP)).toString());
					
					lstSparkPrecio.add(sparkPrice);
					
					total += columna2.floatValue();
				}
				
				BigDecimal bd = new BigDecimal(total).setScale(2,BigDecimal.ROUND_UP);				
				enviarNotificacionSparksDesarrollo(contexto,usuario,lstSparkPrecio,bd.toString(),"mail_user_notifpago_sparks_prod-html","[FIONA] Payment for the sparks you have developed and others users are using in production avatars","");
				paymentReceiver.setAmount(bd.toString());
				receivers.add(paymentReceiver);
			}			
		}
		// Comprobar el sistema MASS_PAY a utilizar
		if(!receivers.isEmpty()){
			if(receivers.size() <= 250){
				// Llamada a la API de Paypal
				String ppresponse = "";
				try{
					ppresponse = PaypalUtilities.getInstance().massPay(receivers);		
					
					// Tratar posibles errores en la respuesta					
					
					NVPDecoder resultValues = new NVPDecoder();
				
					// decode method of NVPDecoder will parse the request and decode the
					// name and value pair
					resultValues.decode(ppresponse);
		
					// checks for Acknowledgement and redirects accordingly to display
					// error messages
					String strAck = resultValues.get("ACK");
					if (strAck != null
							&& !(strAck.equals("Success") || strAck
									.equals("SuccessWithWarning"))) {
						// Error
						LOGGER.error("Se ha producido un error al intentar realizar el pago de sparks en producción - pagarRenovacionesSparksProduccion");
						PaypalUtilities.getInstance().gestionErroresMassPay(contexto, resultValues, "MassPay");								
					} else {
						// En este punto todo ha ido bien				
					}		
				}catch(PayPalException ppEx){
					
				}
				
			}else{
				// Construcción de un archivo que se subirá a Paypal								
				Calendar cal = Calendar.getInstance();
				String nombreArchivo = Constantes.getMASSPAY_FILES_PATH() + "/" + "massPay_" + cal.get(Calendar.YEAR) + (cal.get(Calendar.MONTH)+1) +cal.get(Calendar.DAY_OF_MONTH) +".csv";
				PaypalUtilities.getInstance().generateCSVMassPayFile(nombreArchivo,receivers);
				// Enviarnos email a nosotros mismos para recordar subir archivo a Paypal y enviar los pagos
				PaypalUtilities.getInstance().enviarNotificacionArchivoMassPay(contexto, "mail_team_notif_masspay_file-html", nombreArchivo, "[FIONA] New MassPay File for production sparks has been created!!", Constantes.getMAIL_NOTIFICATION_ADDR());
			}
		}
	}	
	
	
	/**
	 * Método que permite enviar la notificación al usuario con todos los sparks que se le han cobrado
	 * 
	 * @param contexto
	 * @param usuario
	 * @param lstSparksPrecios
	 */
	private void enviarNotificacionSparksProduccion(IContextoEjecucion contexto, UsuarioC usuario, List<AvatarPrice> lstAvatarsPrecios,
			String total, String nombrePlantilla, String subject, String fecha){		
		
		final Map<String, Object> model = new HashMap<String, Object>();		
		model.put("usuario", usuario);
		model.put("lstAvatarPrecios", lstAvatarsPrecios);
		model.put("total", total);
		if(fecha != null && !"".equals(fecha))
			model.put("fecha", fecha);
		
		try {
			final String body = TemplateUtils.processTemplateIntoString(contexto, nombrePlantilla, model);
			
			Properties props = new Properties();

			// Nombre del host de correo, en este caso de adelerobots.com
			// TODO mover a properties
			props.setProperty("mail.smtp.host", "mail.adelerobots.com");

			// TLS si esta disponible
			props.setProperty("mail.smtp.starttls.enable", "false");

			// Puerto para envio de correos
			props.setProperty("mail.smtp.port", "25");

			// Nombre del usuario
			props.setProperty("mail.smtp.user", Constantes.getMAIL_SENDER_ADDR());

			// Si requiere o no usuario y password para conectarse.
			props.setProperty("mail.smtp.auth", "true");

			// Obtenemos instancia de sesion
			// y activamos el debug
			Session session = Session.getDefaultInstance(props);
			session.setDebug(true);

			// Construimos el mensaje a enviar
			MimeMessage message = new MimeMessage(session);
			
			// Preparamos el contenido HTML
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();

			messageBodyPart.setContent(body, "text/html; charset=UTF-8");
			multipart.addBodyPart(messageBodyPart);
			
			
			// Quien envia el correo
			message.setFrom(new InternetAddress(Constantes.getREGISTER_MAIL_SENDER(), "Fiona"));
			// A quien va dirigido
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(usuario.getEmail()));
			// Asunto del mensaje
			message.setSubject(subject);
			// Texto del mensaje
			message.setText(body);
			// Contenido HTML
			message.setContent(multipart);
			
			// Para enviar el mensaje...

			// Indicamos el protocolo a usar
			Transport t = session.getTransport("smtp");
			// Establecer conexion indicando usuario y contraseña
			//propertizar
			t.connect(Constantes.getMAIL_SENDER_ADDR(), Constantes.getMAIL_SENDER_PW());

			// Completamos el envio
			t.sendMessage(message, message.getAllRecipients());
			
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (TemplateException e) {			
			e.printStackTrace();
		} catch (MessagingException e) {			
			e.printStackTrace();
		}
		
	}
	
	// ##############################################################################################
	// ################################# MÉTODOS BATCH TCRF #########################################
	// ##############################################################################################
	/**
	 * Método que planifica las renovaciones de sparks en desarrollo
	 * Este método "ignora" aquellos usuariosparks cuyo propietario
	 * sea el mismo que desarrolló el spark
	 */
	public void planificarRenovacionesSparksTCRF(){
		//final UsuarioSparkEng usuarioSparkDAO = new UsuarioSparkEng(conexion);
		final AvatarSparkEng avatarSparkDAO = new AvatarSparkEng(conexion);
		final AvatarEng avatarDAO = new AvatarEng(conexion);
		
		//List <UsuarioSparkC> lstUsuariosSpark = usuarioSparkDAO.getTimeRenewableProductionUsuarioSparks();
		List <AvatarSparkC> lstAvatarsSpark = avatarSparkDAO.getTimeRenewableTCRFAvatarSparks();
		
		Date fechaToday = new Date();
		Calendar calToday = Calendar.getInstance();
		calToday.setTime(fechaToday);
		calToday.set(Calendar.HOUR_OF_DAY, 0);
		calToday.set(Calendar.MINUTE, 0);
		calToday.set(Calendar.SECOND, 0);
		calToday.set(Calendar.MILLISECOND, 0);
		
		// Lista de avatares cuya fecha de próximo cobro debe actualizarse
		List<AvatarC> lstAvatars = new ArrayList<AvatarC>();

		
		for(int i = 0; i < lstAvatarsSpark.size(); i++){			
			AvatarSparkC avatarSpark = lstAvatarsSpark.get(i);
			
			// Sólo se renovarán el próximo mes los sparks de renovación
			// mensual no gratuitos y los anuales
			
			//Date fechaUploadAvatar = avatarSpark.getAvatar().getDatUpload();
			Date fechaNextCharge = avatarSpark.getAvatar().getDatNextCharge();
			if(fechaNextCharge == null)
				fechaNextCharge = avatarSpark.getAvatar().getDatUpload();
			
			Calendar calUpload = Calendar.getInstance();
			calUpload.setTime(fechaNextCharge);	
			calUpload.set(Calendar.HOUR_OF_DAY, 0);
			calUpload.set(Calendar.MINUTE, 0);
			calUpload.set(Calendar.SECOND, 0);
			calUpload.set(Calendar.MILLISECOND, 0);
			
			Float price = avatarSpark.getFloPrice();
			HostingC hosting = avatarSpark.getAvatarSparkPk().getAvatar().getHosting();			
			Calendar cal = Calendar.getInstance();
			cal.setTime(calToday.getTime());
			if(price!=null){				
				// Planificar únicamente aquellos avatarSpark que tienen como fecha de renovación el día de hoy + 10 días
				cal.add(Calendar.DAY_OF_MONTH, 10);
				
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				
				String f1 = sdf.format(cal.getTime());
				String f2 = sdf.format(calUpload.getTime());

				System.out.println(f1 + " - " + f2);
				
				if(cal.getTimeInMillis() == calUpload.getTimeInMillis()){
					// Añadir avatar asociado a la lista de avatares
					// cuya fecha de próximo cobro debe actualizarse
					if(!lstAvatars.contains(avatarSpark.getAvatar()))
						lstAvatars.add(avatarSpark.getAvatar());
					
					BillingPC billingP = new BillingPC();
					
					// billingP.setUsuarioSpark(usuarioSpark);
					Calendar calRenovacion = Calendar.getInstance();
					calRenovacion.setTime(calUpload.getTime());								
					billingP.setDatRenovation(calRenovacion.getTime());
					
					billingP.setAvatarSpark(avatarSpark);
					billingP.setChCharged('0');
					billingP.setChEmailSent('0');
					billingP.setChPaid('0');
					billingP.setChRenewed('0');
					// El precio del spark en producción no es el precio asociado al spark, sino el precio seleccionado en función de las opciones
					// en el momento de hacer el upload (tabla hosting)
					billingP.setFloAmount(avatarSpark.getFloPrice());					
					
					BillingPEng billingPDAO = new BillingPEng(conexion);
					billingPDAO.insert(billingP);
					billingPDAO.flush();
				}
				
			}			
			
		}
		
		// Actualizamos lo avatares planificados		
		for(int i = 0; i<lstAvatars.size();i++){
			AvatarC avatar = lstAvatars.get(i);
			Calendar calNextCharge = Calendar.getInstance();
			calNextCharge.setTime(calToday.getTime());
			calNextCharge.add(Calendar.DAY_OF_MONTH,10);
			if(avatar.getHosting().getIntCodUnit().equals(Constantes.MONTHS) ||
			   avatar.getHosting().getIntCodUnit().equals(Constantes.CHAT_MONTHS) ||
			   avatar.getHosting().getIntCodUnit().equals(Constantes.FREE)){
				// Actualizar el campo "FE_Next_Charge" con el valor de la renovación futura
				calNextCharge.add(Calendar.MONTH, 1);
				avatar.setDatNextCharge(calNextCharge.getTime());
				avatarDAO.update(avatar);
				avatarDAO.flush();
			}else{
				
				// Se decide dejar que las cuentas free pase por aquí
				
				// Actualizar el campo "FE_Next_Charge" con el valor de la renovación futura
				calNextCharge.add(Calendar.YEAR, 1);
				avatar.setDatNextCharge(calNextCharge.getTime());
				avatarDAO.update(avatar);
				avatarDAO.flush();
			}
		}
		
	}
	
	/**
	 * Método que envía a los usuarios de TCRF un correo con el resumen de los sparks en producción que 
	 * serán renovados al mes siguiente
	 */
	public void notificarRenovacionesSparksTCRF(IContextoEjecucion contexto){
		UsuarioEng usuarioDAO = new UsuarioEng(conexion);		
		BillingPEng billingPDAO = new BillingPEng(conexion);
		
		// 1. Recuperamos los usuarios activos de TCRF
		List <UsuarioC> lstUsuarios = usuarioDAO.getAllTCRFActiveUsers();
				
		
		// 2.  Para cada usuario de TCRF obtenemos las transacciones de producción
		// que se correspondan con el día de hoy + 5, que no tengan el flag
		// 'fl_emailsent' marcado, y que no hayan sido cobrados
		Calendar calRenovacion = Calendar.getInstance();
		calRenovacion.setTime(new Date());
		calRenovacion.add(Calendar.DAY_OF_MONTH, 5);		
		for(int i = 0; i<lstUsuarios.size(); i++){
			UsuarioC usuario = lstUsuarios.get(i);
			
			List <BillingPC> lstbillingP = billingPDAO.getNotNotifiedBillingPByUser(usuario.getCnUsuario(), calRenovacion);
			
			// 3. Actualizar los flags necesarios del objeto BillingP y construir la lista de avatares para el envío del email
			// a los usuarios					
			List <AvatarC> lstAvatars = new ArrayList<AvatarC>();
			Float granTotal = new Float(0);
			for(int j = 0; j < lstbillingP.size(); j++){
				BillingPC billingP = lstbillingP.get(j);
				// Recuperamos el avatar relacionado				
				AvatarC avatar = billingP.getAvatarSpark().getAvatar();
				// Comprobamos si el avatar existe en la lista
				if(!lstAvatars.contains(avatar)){
					lstAvatars.add(avatar);					
				}							
				
				// 4. Actualizar el campo 'fl_emailsent' de la transacción
				billingP.setChEmailSent('1');
				billingPDAO.update(billingP);
				billingPDAO.flush();				
			}
			
			List<AvatarPrice> lstAvatarPrecio = new ArrayList<AvatarPrice>();
			for(int j = 0; j< lstAvatars.size();j++){
				AvatarC avatar = lstAvatars.get(j);
				
				AvatarPrice avatarPrice = new AvatarPrice();
				avatarPrice.setNombreAvatar("Av_"+avatar.getIntCodAvatar().toString());
				
				// 5. Construir el listado detallado que se enviará en el email
				List <SparkPrice> lstSparkPrecio = new ArrayList<SparkPrice>();
				Float total = new Float(0);
				
				for(int k = 0; k < avatar.getLstAvatarSparkC().size(); k++){
					AvatarSparkC avatarSpark = avatar.getLstAvatarSparkC().get(k);
					String nombreSpark = avatarSpark.getAvatarSparkPk().getSpark().getStrNombre();
					BigDecimal bidAmount = new BigDecimal(avatarSpark.getFloPrice()).setScale(2,BigDecimal.ROUND_UP);
					total += avatarSpark.getFloPrice();
					String precioSpark = bidAmount.toString();
					
					SparkPrice sparkPrice = new SparkPrice(nombreSpark,precioSpark);
					
					lstSparkPrecio.add(sparkPrice);
				}
				
				// 6. Añadir el precio del hosting como si de un spark más se tratara
				SparkPrice sparkHosting = new SparkPrice();
				sparkHosting.setNombreSpark("Hosting");
				BigDecimal bidAmount = new BigDecimal(avatar.getHosting().getFloFee()).setScale(2,BigDecimal.ROUND_UP);
				total += avatar.getHosting().getFloFee();	
				BigDecimal bidTotalAvatarAmount = new BigDecimal(total).setScale(2,BigDecimal.ROUND_UP);
				avatarPrice.setPrecioTotalAvatar(bidTotalAvatarAmount.toString());
				granTotal += total;
				String precioHosting = bidAmount.toString();
				sparkHosting.setPrecioSpark(precioHosting);
				lstSparkPrecio.add(sparkHosting);
				
				// 7. Añadir la lista de sparks detallados al avatar
				avatarPrice.setLstSparksPrice(lstSparkPrecio);
				
				// 8. Añadir el avatarPrice a la lista de avatares del usuario
				lstAvatarPrecio.add(avatarPrice);
			}		
		

			// 9. Enviar email
			if(!lstAvatarPrecio.isEmpty()){
				BigDecimal bd = new BigDecimal(granTotal).setScale(2,BigDecimal.ROUND_UP);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String fecha = sdf.format(new Date());
				enviarNotificacionSparksTCRF(contexto,usuario,lstAvatarPrecio,bd.toString(),"mail_user_notif_sparks_tcrf-html","[TCRF] Production Avatars Renovation",fecha);
			}
		}		
		
	}
	
	/**
	 * Método que ejecuta el cobro de la cantidad total que el usuario de TCRF debe pagar
	 * este mes por todos los sparks que corresponda renovar y que, además, 
	 * envía un correo al usuario con el detalle de los sparks 
	 */
	public void cobrarRenovacionesSparksTCRF(IContextoEjecucion contexto){
		UsuarioEng usuarioDAO = new UsuarioEng(conexion);		
		//AvatarEng avatarDAO = new AvatarEng(conexion);
		BillingPEng billingPDAO = new BillingPEng(conexion);
		
		// 1. Recuperamos los usuarios activos de TCRF
		List <UsuarioC> lstUsuarios = usuarioDAO.getAllTCRFActiveUsers();
				
		
		// 2.  Para cada usuario obtenemos los objetos "avatar"
		// que tienen por fecha de renovación el día d hoy, que no tengan el flag
		// 'fl_emailsent' marcado, y que no hayan sido cobrados
		Calendar calRenovacion = Calendar.getInstance();
		calRenovacion.setTime(new Date());
		for(int i = 0; i<lstUsuarios.size(); i++){
			UsuarioC usuario = lstUsuarios.get(i);			
			List <BillingPC> lstBillingPs = billingPDAO.getNotifiedButNotChargedBillingPByUser(usuario.getCnUsuario(), calRenovacion);
			
			// 3. Obtener los datos que necesitamos de cada avatar para construir el listado detallado de avatares y sparks con sus precios
			// y actualizar los campos necesarios de la tabla billingp			
			// Comprobar primero que el usuario tiene un billing id
			if(usuario.getStrBillingId() != null && !lstBillingPs.isEmpty()){
				// Si tiene un billingId asegurar que el acuerdo no esté cancelado
				String ppresponse = "";
				// Recuperamos el billingid codificado
				String encodedBillingAgreementId = usuario.getStrBillingId();				
				StringEncrypterUtilities desencrypter = new StringEncrypterUtilities(Constantes.PASS_PHRASE);
				String billingAgreementId = desencrypter.decrypt(encodedBillingAgreementId);
				// Comprobar estado del acuerdo
				ppresponse = PaypalUtilities.getInstance().baUpdate(billingAgreementId);
				try{
					if(PaypalUtilities.getInstance().checkBillingAgreementStatus(ppresponse)){
						List <AvatarC> lstAvatars = new ArrayList<AvatarC>();
						for(int j = 0; j < lstBillingPs.size(); j++){
							BillingPC billingP = lstBillingPs.get(j);						
							
							// Recuperamos el avatar relacionado				
							AvatarC avatar = billingP.getAvatarSpark().getAvatar();
							// Comprobamos si el avatar existe en la lista
							if(!lstAvatars.contains(avatar)){
								lstAvatars.add(avatar);					
							}		
														
//							billingP.setChCharged('1');
//							billingP.setChRenewed('1');
//							billingPDAO.update(billingP);
//							billingPDAO.flush();							
						}
						
						List<AvatarPrice> lstAvatarPrecio = new ArrayList<AvatarPrice>();
						Float granTotal = new Float(0);
						for(int j=0; j < lstAvatars.size(); j++){
							AvatarC avatar = lstAvatars.get(j);			
							AvatarPrice avatarPrice = new AvatarPrice();
							avatarPrice.setNombreAvatar("Av_"+avatar.getIntCodAvatar().toString());
							// 4. Construir el listado detallado que se enviará en el email
							List <SparkPrice> lstSparkPrecio = new ArrayList<SparkPrice>();
							Float total = new Float(0);
							
							for(int k = 0; k < avatar.getLstAvatarSparkC().size(); k++){
								AvatarSparkC avatarSpark = avatar.getLstAvatarSparkC().get(k);
								String nombreSpark = avatarSpark.getAvatarSparkPk().getSpark().getStrNombre();
								BigDecimal bidAmount = new BigDecimal(avatarSpark.getFloPrice()).setScale(2,BigDecimal.ROUND_UP);
								total += avatarSpark.getFloPrice();
								String precioSpark = bidAmount.toString();
								
								SparkPrice sparkPrice = new SparkPrice(nombreSpark,precioSpark);
								
								lstSparkPrecio.add(sparkPrice);
							}
							
							// 5. Añadir el precio del hosting como si de un spark más se tratara
							SparkPrice sparkHosting = new SparkPrice();
							sparkHosting.setNombreSpark("Hosting");
							BigDecimal bidAmount = new BigDecimal(avatar.getHosting().getFloFee()).setScale(2,BigDecimal.ROUND_UP);
							total += avatar.getHosting().getFloFee();	
							BigDecimal bidTotalAvatarAmount = new BigDecimal(total).setScale(2,BigDecimal.ROUND_UP);
							avatarPrice.setPrecioTotalAvatar(bidTotalAvatarAmount.toString());
							granTotal += total;
							String precioHosting = bidAmount.toString();
							sparkHosting.setPrecioSpark(precioHosting);
							lstSparkPrecio.add(sparkHosting);
							
							// 6. Añadir la lista de sparks detallados al avatar
							avatarPrice.setLstSparksPrice(lstSparkPrecio);
							
							// 7. Añadir el avatarPrice a la lista de avatares del usuario
							lstAvatarPrecio.add(avatarPrice);
						}
						// 8. Realizar el cobro al usuario por el total de todos los avatares
						BigDecimal bd = new BigDecimal(granTotal).setScale(2,BigDecimal.ROUND_UP);
						ppresponse = PaypalUtilities.getInstance().doReferenceTransaction(billingAgreementId, bd.toString(),Constantes. getPROD_SPARKS_RENEWAL_ANNOTATION());
						
						// Tratar posibles errores en la respuesta					
						
						NVPDecoder resultValues = new NVPDecoder();
					
						// decode method of NVPDecoder will parse the request and decode the
						// name and value pair
						resultValues.decode(ppresponse);
			
						// checks for Acknowledgement and redirects accordingly to display
						// error messages
						String strAck = resultValues.get("ACK");
						if (strAck != null
								&& !(strAck.equals("Success") || strAck
										.equals("SuccessWithWarning"))) {
							// Error
							LOGGER.error("Se ha producido un error al intentar cobrar los avatares en producción al usuario " + usuario.getCnUsuario());
							PaypalUtilities.getInstance().gestionErroresDoReferenceTransaction(contexto, resultValues, usuario, usuario.getStrBillingId(), "doReferenceTransaction");								
						} else {
							// En este punto todo ha ido bien 
							// Actualizar los billingp (transacciones) del usuario
							for(int j = 0; j < lstBillingPs.size(); j++){
								BillingPC billingP = lstBillingPs.get(j);
								// 9. Actualizar campo 'fl_charged'
								billingP.setChCharged('1');
								// 10. Actualizar campo 'fl_renewed'
								billingP.setChRenewed('1');
								billingPDAO.update(billingP);
								billingPDAO.flush();
							}
							// 11. Enviar email		
							SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
							String fecha = sdf.format(new Date());
							enviarNotificacionSparksTCRF(contexto,usuario,lstAvatarPrecio,bd.toString(),"mail_user_notifcobro_sparks_tcrf-html","[TCRF] Production Sparks Renovation",fecha);
						}					
						
					}else{						
						// El acuerdo ha sido cancelado
						// El usuario tiene transacciones pendientes pero no disponemos de un identificador de contrato para pasarle el pago
						LOGGER.error("El usuario" + usuario.getCnUsuario() + "tiene un billingId que se corresponde con un acuerdo cancelado");
						enviarEmailTCRF(contexto,"mail_user_without_or_cancelled_billingId_tcrf-html",usuario,"Problems trying to charge your no free avatars on production environment - Canceled agreement",usuario.getEmail());
						enviarEmailTCRF(contexto,"mail_team_without_or_cancelled_billingId_tcrf-html",usuario,"Problems trying to charge a user no free avatars on production environment - Canceled agreement",Constantes.getMAIL_NOTIFICATION_ADDR());
					}
				}catch(PayPalException ppEx){
					// TODO: handle exception
					ppEx.printStackTrace();
				}
			
			}
			
		}
	}
	
	/**
	 * Método genérico para enviar emails usando una plantilla pasada como parámetro
	 * 
	 * @param contexto
	 * @param nombrePlantilla
	 * @param usuario
	 * @param subject
	 * @param destinatario
	 */
	private void enviarEmailTCRF(IContextoEjecucion contexto, String nombrePlantilla, UsuarioC usuario,
			String subject, String destinatario){		
		
		final Map<String, Object> model = new HashMap<String, Object>();		
		model.put("usuario", usuario);
				
		
		try {
			final String body = TemplateUtils.processTemplateIntoString(contexto, nombrePlantilla, model);
			
			Properties props = new Properties();

			// Nombre del host de correo, en este caso de adelerobots.com
			// TODO mover a properties
			props.setProperty("mail.smtp.host", "mail.adelerobots.com");

			// TLS si esta disponible
			props.setProperty("mail.smtp.starttls.enable", "false");

			// Puerto para envio de correos
			props.setProperty("mail.smtp.port", "25");

			// Nombre del usuario
			props.setProperty("mail.smtp.user", Constantes.getTCRF_MAIL_SENDER_ADDR());

			// Si requiere o no usuario y password para conectarse.
			props.setProperty("mail.smtp.auth", "true");

			// Obtenemos instancia de sesion
			// y activamos el debug
			Session session = Session.getDefaultInstance(props);
			session.setDebug(true);

			// Construimos el mensaje a enviar
			MimeMessage message = new MimeMessage(session);
			
			// Preparamos el contenido HTML
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();

			messageBodyPart.setContent(body, "text/html; charset=UTF-8");
			multipart.addBodyPart(messageBodyPart);
			
			
			// Quien envia el correo
			message.setFrom(new InternetAddress(Constantes.getTCRF_REGISTER_MAIL_SENDER(), "TCRF"));
			// A quien va dirigido
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
			// Asunto del mensaje
			message.setSubject(subject);
			// Texto del mensaje
			message.setText(body);
			// Contenido HTML
			message.setContent(multipart);
			
			// Para enviar el mensaje...

			// Indicamos el protocolo a usar
			Transport t = session.getTransport("smtp");
			// Establecer conexion indicando usuario y contraseña
			//propertizar
			t.connect(Constantes.getTCRF_MAIL_SENDER_ADDR(), Constantes.getTCRF_MAIL_SENDER_PW());

			// Completamos el envio
			t.sendMessage(message, message.getAllRecipients());
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Método que permite enviar la notificación al usuario con todos los sparks que se le han cobrado
	 * 
	 * @param contexto
	 * @param usuario
	 * @param lstSparksPrecios
	 */
	private void enviarNotificacionSparksTCRF(IContextoEjecucion contexto, UsuarioC usuario, List<AvatarPrice> lstAvatarsPrecios,
			String total, String nombrePlantilla, String subject, String fecha){		
		
		final Map<String, Object> model = new HashMap<String, Object>();		
		model.put("usuario", usuario);
		model.put("lstAvatarPrecios", lstAvatarsPrecios);
		model.put("total", total);
		if(fecha != null && !"".equals(fecha))
			model.put("fecha", fecha);
		
		try {
			final String body = TemplateUtils.processTemplateIntoString(contexto, nombrePlantilla, model);
			
			Properties props = new Properties();

			// Nombre del host de correo, en este caso de adelerobots.com
			// TODO mover a properties
			props.setProperty("mail.smtp.host", "mail.adelerobots.com");

			// TLS si esta disponible
			props.setProperty("mail.smtp.starttls.enable", "false");

			// Puerto para envio de correos
			props.setProperty("mail.smtp.port", "25");

			// Nombre del usuario
			props.setProperty("mail.smtp.user", Constantes.getTCRF_MAIL_SENDER_ADDR());

			// Si requiere o no usuario y password para conectarse.
			props.setProperty("mail.smtp.auth", "true");

			// Obtenemos instancia de sesion
			// y activamos el debug
			Session session = Session.getDefaultInstance(props);
			session.setDebug(true);

			// Construimos el mensaje a enviar
			MimeMessage message = new MimeMessage(session);
			
			// Preparamos el contenido HTML
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();

			messageBodyPart.setContent(body, "text/html; charset=UTF-8");
			multipart.addBodyPart(messageBodyPart);
			
			
			// Quien envia el correo
			message.setFrom(new InternetAddress(Constantes.getTCRF_REGISTER_MAIL_SENDER(), "TCRF"));
			// A quien va dirigido
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(usuario.getEmail()));
			// Asunto del mensaje
			message.setSubject(subject);
			// Texto del mensaje
			message.setText(body);
			// Contenido HTML
			message.setContent(multipart);
			
			// Para enviar el mensaje...

			// Indicamos el protocolo a usar
			Transport t = session.getTransport("smtp");
			// Establecer conexion indicando usuario y contraseña
			//propertizar
			t.connect(Constantes.getTCRF_MAIL_SENDER_ADDR(), Constantes.getTCRF_MAIL_SENDER_PW());

			// Completamos el envio
			t.sendMessage(message, message.getAllRecipients());
			
		} catch (IOException e) {			
			e.printStackTrace();
		} catch (TemplateException e) {			
			e.printStackTrace();
		} catch (MessagingException e) {			
			e.printStackTrace();
		}
		
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
