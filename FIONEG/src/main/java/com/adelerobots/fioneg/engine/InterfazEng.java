package com.adelerobots.fioneg.engine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.InterfazC;

public class InterfazEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<InterfazC>{

	String conexion;
	
	public InterfazEng(String conexion) {
		super(conexion);
		this.conexion=conexion;
	}
	
	public InterfazC getInterfaz(Integer cnInterfaz) {
		List<Criterion> lstCriterios = new ArrayList<Criterion>();
		if (cnInterfaz != null) {
			lstCriterios.add(Restrictions.eq("cnInterfaz", cnInterfaz));
		}
		InterfazC interfaz = this.findByCriteriaUniqueResult(lstCriterios);
		return interfaz;
	}
	
	public InterfazC getIfazByNameType(String name, String type){
		Criteria criteria = this.getCriteria();
		Criterion namecrit = Restrictions.eq("dcNombre", name); 
		Criterion typecrit = Restrictions.eq("strTipo", type);
		criteria.add(Restrictions.and(namecrit, typecrit));
		
		InterfazC ifaz = (InterfazC) criteria.uniqueResult();
		return ifaz;
	}
	
	/**
	 * Método que devuelve todas las interfaces existentes
	 * @return
	 */
	public List<InterfazC> getAllInterfaces(){
		return this.findAll();
	}
	
	/**
	 * Método que devuelve todas las interfaces de un tipo
	 * 
	 * @param type Tipo de interfaz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<InterfazC> getInterfacesByType(String type){
		Criteria criteria = this.getCriteria();		 
		Criterion typecrit = Restrictions.eq("strTipo", type);
		criteria.add(typecrit);
		
		return criteria.list();		
	}	
}
