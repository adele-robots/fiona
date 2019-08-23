package com.adelerobots.fioneg.engine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.UserStatusEnum;
import com.adelerobots.fioneg.entity.usuariospark.UsuarioSparkC;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.persistencia.dao.HibernateAnnotationsUtil;



public class UsuarioSparkEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<UsuarioSparkC> {

	public UsuarioSparkEng(String dataSource) {
		super(dataSource);
	}

	
	/**
	 * Método que busca todos los sparks que figuren con el flag
	 * hidden a 0 para el usuario dado
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * 
	 * @return Devolverá la lista de sparks con el flag hidden a 0
	 * para el usuario cuyo identificador se pasa como parámetro
	 */
	public List<UsuarioSparkC> getNotHiddenSparks(Integer intCodUsuario) {
		List <Criterion>lstCriterios = new ArrayList<Criterion>();
		
		lstCriterios.add(Restrictions.eq("intCodUsuario", intCodUsuario));
		lstCriterios.add(Restrictions.eq("chHidden", '0'));
		
		return this.findByCriteria(lstCriterios);
	}
	
	
	/**
	 * Método que devolverá todos los sparks asociados a un usuario
	 * 
	 * @param intCodUsuario Identificador único de usuario
	 * 
	 * @return Lista de todos los sparks asociados al usuario
	 * cuyo identificador se pasa como parámetro
	 */
	public List<UsuarioSparkC> getAllUserSparks(Integer intCodUsuario){
		List <Criterion>lstCriterios = new ArrayList<Criterion>();
		
		lstCriterios.add(Restrictions.eq("intCodUsuario", intCodUsuario));
		
		return this.findByCriteria(lstCriterios);
	}
	
	
	/**
	 * Método que devuelve el registro de la tabla usuariospark
	 * que relaciona el spark y el usuario pasados como parámetro
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @param intCodSpark Identificador único del spark
	 * 
	 * @return Se devuelve el registro correspondiente al usuario
	 * y al spark o null en caso de no encontrarlo
	 */
	public UsuarioSparkC findUsuarioSpark(Integer intCodUsuario, Integer intCodSpark){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("intCodUsuario", intCodUsuario));
		criteria.add(Restrictions.eq("intCodSpark", intCodSpark));	
		
		
		return (UsuarioSparkC)criteria.uniqueResult();
	}
	
	/**
	 * Método que devuelve los elementos "UsuarioSparkC" cuyo periodo
	 * de prueba haya expirado
	 * 
	 * @return Devuelve listado de elementos que relacionan usuario-spark
	 * e indican que el periodo de prueba del usuario sobre el spark ha
	 * expirado
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<UsuarioSparkC> getExpiredTrialUsuarioSpark(){
		
		String query = "SELECT DISTINCT(us.CN_SPARK), us.* ";
		query += " FROM usuariospark us ";
		query += " INNER JOIN spark sp on us.cn_spark = sp.cn_spark ";
		query += " WHERE sp.fl_trial = 1 AND us.fl_usedtrial = 1 AND us.fl_activo = 1 AND";
		query += " (now()-us.fe_usedtrial) > sp.nu_diastrial ";
		
		Session sess = HibernateAnnotationsUtil.getSession(Constantes.CTE_JNDI_DATASOURCE,"FIONEG");
		SQLQuery sqlQuery = sess.createSQLQuery(query);
		sqlQuery.addEntity(UsuarioSparkC.class);		
		
		List <UsuarioSparkC> lstUsuarioSpark = sqlQuery.list();
		
		return lstUsuarioSpark;
		
	}
	
	
	/**
	 * Método que devuelve los sparks activos que deben cobrarse por periodos
	 * de tiempo y no por uso
	 * 
	 * @return
	 */
	public List<UsuarioSparkC> getTimeRenewableUsuarioSparks(){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("chActivo", '1'));		
		criteria.createAlias("price", "price");
		
		criteria.add(Restrictions.isNotNull("price.floPrize"));
		// Añadido el día 21-11-2013 para evitar planificar
		// para desarrollo los sparks de usuarios de TCRF
		criteria.createAlias("usuario","usuario");
		criteria.add(Restrictions.ne("usuario.strStatus", UserStatusEnum.TCRF.text));
		
		List <UsuarioSparkC> lstUsuarioSpark = criteria.list();
		
		return lstUsuarioSpark;
	}
	
	
	/**
	 * Método que devuelve los sparks activos que deben cobrarse por periodos
	 * de tiempo y no por uso para un usuario concreto
	 * 
	 * @return
	 */
	public List<UsuarioSparkC> getTimeRenewableUsuarioSparksByUser(Integer intCodUsuario){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("chActivo", '1'));
		criteria.add(Restrictions.eq("intCodUsuario", intCodUsuario));
		criteria.createAlias("price", "price");
		
		criteria.add(Restrictions.isNotNull("price.floPrize"));
		
		List <UsuarioSparkC> lstUsuarioSpark = criteria.list();
		
		return lstUsuarioSpark;
	}
	
	/**
	 * Método que devuelve los sparks desarrollados por un determinado usuario
	 * que están siendo usados por otros usuarios en desarrollo
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @return
	 */
	public List<UsuarioSparkC> getActiveUsedSparksDevelopedByUser(Integer intCodUsuario){
		Criteria criteria = getCriteria();
		criteria.createAlias("spark","spark");
		criteria.createAlias("spark.usuarioDesarrollador","usuarioDesarrollador");
		
		// El usuario que está usando el spark no puede ser el propio desarrollador
		criteria.add(Restrictions.ne("intCodUsuario", intCodUsuario));
		// El usuario que ha desarrollado el spark debe ser el pasado como parámetro
		criteria.add(Restrictions.eq("usuarioDesarrollador.cnUsuario", intCodUsuario));
		// El spark debe estar activo para el usuario que lo esté usando
		criteria.add(Restrictions.eq("chActivo", '1'));
		
		List <UsuarioSparkC> lstUsuarioSpark = criteria.list();
		
		return lstUsuarioSpark;
	}
	
	/**
	 * Método que borrará todos los sparks de un usuario
	 * 
	 * @param intCodUsuario Identificador único del usuario a borrar
	 */
	public void deleteUsuarioSparksByUser(Integer intCodUsuario){
		String query = "delete from UsuarioSpark where intCodUsuario = :intCodUsuario";
		
		Session sess = HibernateAnnotationsUtil.getSession(	Constantes.CTE_JNDI_DATASOURCE,"FIONEG");
		sess.createQuery(query).setInteger("intCodUsuario", intCodUsuario).executeUpdate();			
		
	}
	

}
