package com.adelerobots.fioneg.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.EntidadC;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.persistencia.dao.HibernateAnnotationsUtil;

/**
 * Data Access Object for EntidadC entity
 * 
 * @author adele
 *
 */
public class EntidadEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<EntidadC>{

	String conexion;
	
	public EntidadEng(String conexion) {
		super(conexion);
		this.conexion=conexion;
	}

	/**
	 * Obtiene el cuenta por su identificador interno
	 * @param id
	 */
	public EntidadC findById(
			final Integer id) 
	{
		EntidadC ent = this.findById((Serializable)id);
		return ent;	
	}
	
	/**
	 * Obtiene la entidad por su nombre
	 * @param name
	 */
	public EntidadC findByName(
			final String name) 
	{
		final List<Criterion> criterions = new ArrayList<Criterion>(1);
		criterions.add(Restrictions.eq("nombre", name).ignoreCase());
		EntidadC ent = this.findByCriteriaUniqueResult(criterions);
		return ent;	
	}

	/**
	 * Crea una nueva entidad persistiendo inmediatamente los datos en BBDD
	 * @param ent entidad a crear
	 * @return
	 */
	public EntidadC create(final EntidadC ent) {	
		this.insert(ent);
		this.flush();
		this.refresh(ent);
		return ent;
	}

	/**
	 * Retrieve the Legal Entity Names by that criteria
	 * @param entityId the id to check (this entity will be ignored on process), <code>null</code> to ignore this search param
	 * @param entityName The Legal Entity Name to search, <code>null</code> to ignore this search param
	 * @return
	 */
	public List<String> getAllEntityNames(Integer entityId, String entityName) {
		String sql = "SELECT dc_nombre FROM entidad _this WHERE 1=1 ";
		if (entityId != null) {
			sql += " AND _this.cn_entidad <> ? ";
		}
		if (entityName != null) {
			sql += " AND upper(_this.dc_nombre) = upper(?) ";
		}
		
		final Session session = HibernateAnnotationsUtil.getSession(this.conexion, Constantes.CTE_APP_FIO);
		final SQLQuery query = session.createSQLQuery(sql)
									.addScalar("dc_nombre", Hibernate.STRING);
		int pos = 0;
		if (entityId != null) {
			query.setInteger(pos++, entityId.intValue());
		}
		if (entityName != null) {
			query.setString(pos++, entityName);
		}
		final List<String> list = query.list();
		return list;
	}
}
