package com.adelerobots.fioneg.engine;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.UnitC;

public class UnitEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<UnitC>{

	public UnitEng(String dataSource) {
		super(dataSource);		
	}
	
	
	/**
	 * Método que devuelve todas las unidades
	 * de cobro
	 * @return Listado de objetos UnitC
	 */
	public List<UnitC> getAllUnits(){
		return this.findAll();
	}
	
	/**
	 * Método que devuelve a lista de unidades con fl_visible a 1
	 * @param ids Identificadores únicos de las unidades buscadas
	 * @return Listado de objetos UnitC que se corresponden con los
	 * criterios de búsqueda
	 */
	public List<UnitC> getVisibleUnits(){
		
		Criteria criteria = getCriteria();		
		criteria.add(Restrictions.eq("flagVisible", "1"));
		return criteria.list();	
	}
	
	/**
	 * Método que devuelve a lista de unidades cuyos identificadores coincidan
	 * con los pasados como parámetros
	 * @param ids Identificadores únicos de las unidades buscadas
	 * @return Listado de objetos UnitC que se corresponden con los
	 * criterios de búsqueda
	 */
	public List<UnitC> getUnits(List <Integer> ids){
		Criteria criteria = getCriteria();		
		
		criteria.add(Restrictions.in("intCodUnit", ids));
		
		return criteria.list();
	}
	
	/**
	 * Método que devuelve la entidad de tipo "UnitC" cuyo identificador
	 * coincida con el pasado como parámetro
	 * @param intCodUnit Identificador único de la entidad buscada
	 * @return Se devolverá el objeto que cumpla los criterios de búsqueda
	 */
	public UnitC getUnit(Integer intCodUnit){
		return this.findById(intCodUnit);
	}
	
	

}
