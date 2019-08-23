package com.adelerobots.fioneg.engine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.TipoC;

public class TipoEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<TipoC>{
	String conexion;
	
	public TipoEng(String conexion) {
		super(conexion);
		this.conexion=conexion;
	}
	
	public TipoC getTipo(Integer cnTipo) {
		List<Criterion> lstCriterios = new ArrayList<Criterion>();
		if (cnTipo != null) {
			lstCriterios.add(Restrictions.eq("cnTipo", cnTipo));
		}
		TipoC tipo = this.findByCriteriaUniqueResult(lstCriterios);
		return tipo;
	}
	
	/**
	 * Método que devuelve todos los parámetros de configuración
	 * disponibles para el usuario, es decir, los tipos básicos
	 * y los tipos que haya creado.
	 * 
	 * @param intCodUsuario Identificador del usuario 
	 * @return Lista con todos los tipos disponibles
	 */
	@SuppressWarnings("unchecked")
	public List<TipoC> getAllTypesByUser(Integer intCodUsuario){
		Criteria criteria = getCriteria();		
		
		criteria.createAlias("usuario", "usuario",CriteriaSpecification.LEFT_JOIN);		
		criteria.add(Restrictions.or(Restrictions.eq("chTipoBasico", '1'), Restrictions.eq("usuario.cnUsuario", intCodUsuario)));						
		
		return criteria.list();
	}
	
	/**
	 * Método que devuelve todos los parámetros de configuración
	 * definidos por el usuario
	 * 
	 * @param intCodUsuario Identificador del usuario 
	 * @return Lista con todos los tipos definidios
	 */
	@SuppressWarnings("unchecked")
	public List<TipoC> getUserTypes(Integer intCodUsuario){
		Criteria criteria = getCriteria();	
		
		criteria.createCriteria("usuario").add(Restrictions.eq("cnUsuario", intCodUsuario));
		
		return criteria.list();
	}
	
	/**
	 * Crea un nuevo tipo persistiendo inmediatamente los datos en BBDD
	 * @param tipo tipo a crear
	 * @return
	 */
	public TipoC create(final TipoC tipo) {	
		this.insert(tipo);
		this.flush();
		//this.refresh(tipo);
		return tipo;
	}
}
