package com.adelerobots.fioneg.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.UserStatusEnum;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.persistencia.dao.HibernateAnnotationsUtil;

/**
 * Data Access Object for UsuarioC entity
 * 
 * @author adele
 *
 */
public class UsuarioEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<UsuarioC>{

	String conexion;
	
	public UsuarioEng(String conexion) {
		super(conexion);
		this.conexion=conexion;
	}

	/**
	 * Obtiene el usuario por su identificador interno
	 * @param id
	 */
	public UsuarioC findById(
			final Integer id) 
	{
		UsuarioC ent = this.findById((Serializable)id);
		return ent;	
	}

	/**
	 * Obtiene el usuario por su uid de logon (email o username)
	 * @param logonUID
	 */
	public UsuarioC findByLogon(
			final String logonUID) 
	{
		final List<Criterion> criterions = new ArrayList<Criterion>(2);
		if (logonUID != null) {
			Criterion byEmail = Restrictions.eq("email", logonUID).ignoreCase();
			//El username puede ser null/empty string, por eso el and
			Criterion byUsername = Restrictions.and(
					Restrictions.and(
							Restrictions.isNotNull("username"),
							Restrictions.ne("username", "")
						), 
					Restrictions.eq("username", logonUID).ignoreCase()
				);
			criterions.add(Restrictions.or(byEmail, byUsername));
		}
		
		final UsuarioC ent = this.findByCriteriaUniqueResult(criterions);
		return ent;
	}

	/**
	 * Obtiene los usuarios que casan con el email indicado
	 * @param email
	 */
	public Collection<UsuarioC> findAllByEmail(
			final String email) 
	{
		final List<Criterion> criterions = new ArrayList<Criterion>(1);
		if (email != null) {
			criterions.add(Restrictions.eq("email", email).ignoreCase());
		} else {
			criterions.add(Restrictions.isNull("email"));
		}
		
		final Collection<UsuarioC> col = this.findByCriteria(criterions);
		return col;
	}

	/**
	 * Obtiene el usuario que casa con el email indicado. 
	 * <p>Esto se puede hacer ya que el campo email es clave unica para un usuario.
	 * @param email
	 */
	public UsuarioC findByEmail(
			final String email) 
	{
		if (email == null) return null;
		final List<Criterion> criterions = new ArrayList<Criterion>(1);
		criterions.add(Restrictions.eq("email", email).ignoreCase());
		final UsuarioC ent = this.findByCriteriaUniqueResult(criterions);
		return ent;
	}

	/**
	 * Obtiene los usuarios que casan con el username/nickname indicado.
	 * <p>Esto se puede hacer ya que el campo email es clave unica para un usuario 
	 * @param username
	 */
	public Collection<UsuarioC> findAllByUsername(
			final String username) 
	{
		final List<Criterion> criterions = new ArrayList<Criterion>(1);
		if (username != null) {
			criterions.add(Restrictions.eq("username", username).ignoreCase());
		} else {
			criterions.add(Restrictions.isNull("username"));
		}
		
		final Collection<UsuarioC> col = this.findByCriteria(criterions);
		return col;
	}

	/**
	 * Obtiene el usuario que casa con el username/nickname indicado
	 * @param username
	 */
	public UsuarioC findByUsername(
			final String username) 
	{
		if (username == null) return null;
		final List<Criterion> criterions = new ArrayList<Criterion>(1);
		criterions.add(Restrictions.eq("username", username).ignoreCase());
		final UsuarioC ent = this.findByCriteriaUniqueResult(criterions);
		return ent;
	}

	/**
	 * Retrieve the user emails by that criteria
	 * @param userId the id to check (this user will be ignored on process), <code>null</code> to ignore this search param
	 * @param email the email to search, <code>null</code> to ignore this search param
	 * @return
	 */
	public Collection<String> getAllEmails(Integer userId, String email) {
		String sql = "SELECT dc_email FROM usuario _this WHERE 1=1 ";
		if (userId != null) {
			sql += " AND _this.cn_usuario <> ?";
		}
		if (email != null) {
			sql += " AND upper(_this.dc_email) = upper(?) AND fl_cancelada = '0' ";
		}
		
		final Session session = HibernateAnnotationsUtil.getSession(this.conexion, Constantes.CTE_APP_FIO);
		final SQLQuery query = session.createSQLQuery(sql)
									.addScalar("dc_email", Hibernate.STRING);
		int pos = 0;
		if (userId != null) {
			query.setInteger(pos++, userId.intValue());
		}
		if (email != null) {
			query.setString(pos++, email);
		}
		final Collection<String> col = query.list();
		return col;
	}

	/**
	 * Retrieve the user nicknames by that criteria
	 * @param userId the id to check (this user will be ignored on process), <code>null</code> to ignore this search param
	 * @param username the nickname to search, <code>null</code> to ignore this search param
	 * @return
	 */
	public Collection<String> getAllUsernames(Integer userId, String username) {
		String sql = "SELECT dc_username FROM usuario _this WHERE 1=1 ";
		if (userId != null) {
			sql += " AND _this.cn_usuario <> ? ";
		}
		if (username != null) {
			sql += " AND upper(_this.dc_username) = upper(?) AND fl_cancelada = '0'";
		}
		
		final Session session = HibernateAnnotationsUtil.getSession(this.conexion, Constantes.CTE_APP_FIO);
		final SQLQuery query = session.createSQLQuery(sql)
									.addScalar("dc_username", Hibernate.STRING);
		int pos = 0;
		if (userId != null) {
			query.setInteger(pos++, userId.intValue());
		}
		if (username != null) {
			query.setString(pos++, username);
		}
		final Collection<String> col = query.list();
		return col;
	}

	/**
	 * Crea un nuevo usuario persistiendo inmediatamente los datos en BBDD
	 * @param ent usuario a crear
	 * @return
	 */
	public UsuarioC create(final UsuarioC ent) {	
		this.insert(ent);
		this.flush();
		this.refresh(ent);
		return ent;
	}



	public List<Object[]> UserSparkInterfazQuery(Integer userId){
		String sql ="SELECT distinct "
			+"spark.dc_nombre, "
			+"interfaz.dc_nombre as dc_interfaz, "
			+"interfaz.dc_tipo "
			+"FROM usuario "
			+"INNER JOIN usuariospark on (usuario.cn_usuario=usuariospark.cn_usuario) "
			+"INNER JOIN spark on (usuariospark.cn_spark=spark.cn_spark) "
			+"INNER JOIN sparkinterfaz on (spark.cn_spark=sparkinterfaz.cn_spark) "
			+"INNER JOIN interfaz on (sparkinterfaz.cn_interfaz=interfaz.cn_interfaz) " 
			+"WHERE usuario.cn_usuario= ? "
			+" ORDER BY spark.dc_nombre ASC";
		final Session session = HibernateAnnotationsUtil.getSession(this.conexion, Constantes.CTE_APP_FIO);
		final SQLQuery query = session.createSQLQuery(sql);
		query.setInteger(0, userId);
		return query.list();
	}
	
	public List<Object[]> UserSparkParamPrimitivosQuery(Integer userId){
		String sql = "SELECT distinct "
			+"spark.dc_nombre, "
			+"configparam.dc_nombre as dc_configparam, "
			+"configparam.dc_defaultvalue, "
			+"configparam.dc_minvalue, "
			+"configparam.dc_maxvalue, "
			+"configparam.cn_tipo "
			+"FROM usuario "
			+"INNER JOIN usuariospark on (usuario.cn_usuario=usuariospark.cn_usuario) "
			+"INNER JOIN spark on (usuariospark.cn_spark=spark.cn_spark) "
			+"INNER JOIN configparam on (spark.cn_spark=configparam.cn_spark) "
			+"WHERE usuario.cn_usuario= ? "
			+" ORDER BY spark.dc_nombre ASC";
		final Session session = HibernateAnnotationsUtil.getSession(this.conexion, Constantes.CTE_APP_FIO);
		final SQLQuery query = session.createSQLQuery(sql);
		query.setInteger(0, userId);
		return query.list();
	}
	
	public List<Object[]> UserSparkParamListaGlobalQuery(Integer userId){
		String sql = "SELECT distinct "
			+"spark.dc_nombre, "
			+"configparam.dc_nombre as configparam, "
			+"datolista.dc_nombre as datolista, "
			+"datolista.cn_tipo "
			+"FROM usuario "
			+"INNER JOIN usuariospark on (usuario.cn_usuario=usuariospark.cn_usuario) "
			+"INNER JOIN spark on (usuariospark.cn_spark=spark.cn_spark) "
			+"INNER JOIN configparam on (spark.cn_spark=configparam.cn_spark) "
			+"INNER JOIN tipo on (tipo.cn_tipo=configparam.cn_tipo) "
			+"INNER JOIN datolista on (datolista.cn_tipo=tipo.cn_tipo) "
			+"WHERE usuario.cn_usuario= ? AND tipo.cn_tipo>=5 "
			+"ORDER BY spark.dc_nombre ASC";
		final Query query = getSQLQuery(sql);
		query.setInteger(0, userId);
		return query.list();
	}
	
	public List<Object[]> UserSparkParamListaPropiosQuery(Integer userId){
		String sql = "SELECT distinct "
			+"spark.dc_nombre, "
			+"configparam.dc_nombre as dc_configparam, "
			+"datolistausuario.dc_nombre as dc_datolistausuario, "
			+"datolistausuario.cn_tipo "
			+"FROM usuario "
			+"INNER JOIN usuariospark on (usuario.cn_usuario=usuariospark.cn_usuario) "
			+"INNER JOIN spark on (usuariospark.cn_spark=spark.cn_spark) "
			+"INNER JOIN configparam on (spark.cn_spark=configparam.cn_spark) "
			+"INNER JOIN tipo on (tipo.cn_tipo=configparam.cn_tipo) "
			+"INNER JOIN datolistausuario on (datolistausuario.cn_tipo=tipo.cn_tipo AND datolistausuario.cn_usuario=?) "
			+"WHERE usuario.cn_usuario= ? AND tipo.cn_tipo>=5 "
			+"ORDER BY spark.dc_nombre ASC";
		final Session session = HibernateAnnotationsUtil.getSession(this.conexion, Constantes.CTE_APP_FIO);
		final SQLQuery query = session.createSQLQuery(sql);
		query.setInteger(0, userId);
		query.setInteger(1, userId);
		return query.list();
	}
	
	/**
	 * Método que devolverá los usuarios registrados en una fecha
	 * cuyo día coincida con el de hoy
	 * 
	 * @return Se devolverá la lista de usuarios que cumplan dicha
	 * condición
	 */
	@SuppressWarnings("unchecked")
	public List<UsuarioC> getUsuariosRegistradosPorDia(Calendar today){
		Criteria criteria = getCriteria();
		List<UsuarioC> usuarios;
		criteria.add(Restrictions.sqlRestriction("day(fe_accountupdate) = " + today.get(Calendar.DAY_OF_MONTH)));
		usuarios = (List<UsuarioC>)criteria.list();
		
		return usuarios;
	}
	
	/**
	 * Método que nos devolverá todos los usuarios activos de la plataforma
	 * 
	 * @return listado de usuarios que cumplan las condiciones deseadas (que
	 * estén activos en la plataforma)
	 */
	public List<UsuarioC> getAllActiveUsers(){
		Criteria criteria = getCriteria();
		List<UsuarioC> usuarios = new ArrayList<UsuarioC>();
		// status del usuario 'offline' u 'online'
		Criterion crit1 = Restrictions.eq("strStatus", UserStatusEnum.OFFLINE.text);
		Criterion crit2 = Restrictions.eq("strStatus", UserStatusEnum.ONLINE.text);
		criteria.add(Restrictions.or(crit1,crit2));
		// cuenta del usuario activa
		criteria.add(Restrictions.eq("chCancelada", "0"));
		
		usuarios = criteria.list();
		
		return usuarios;
	}
	
	/**
	 * Método que nos devolverá todos los usuarios activos de TCRF
	 * 
	 * @return listado de usuarios que cumplan las condiciones deseadas (que
	 * estén activos en la plataforma)
	 */
	public List<UsuarioC> getAllTCRFActiveUsers(){
		Criteria criteria = getCriteria();
		List<UsuarioC> usuarios = new ArrayList<UsuarioC>();		
		
		// Usuarios de TCRF (cuentas gratuitas, o de pago)
		Criterion crit1 = Restrictions.eq("strStatus", UserStatusEnum.TCRF.text);
		Criterion crit2 = Restrictions.eq("strStatus", UserStatusEnum.TCRF_FREE.text);
		criteria.add(Restrictions.or(crit1,crit2));
		
		// cuenta del usuario activa
		criteria.add(Restrictions.eq("chCancelada", "0"));
		
		usuarios = criteria.list();
		
		return usuarios;
	}
	
	/**
	 * Método que encuentra los usuarios con cuenta cancelada cuyo
	 * email coincida con el pasado como parámetro
	 * 
	 * @param email Email del usuario buscado
	 * @return
	 */
	public List<UsuarioC> findCanceledUsersByEmail(String email){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("chCancelada", "1"));
		criteria.add(Restrictions.ilike("email", email));
		
		List <UsuarioC> lstUsuarios = criteria.list();
		
		return lstUsuarios;
	}
	
	
}
