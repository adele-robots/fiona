package com.adelerobots.fioneg.engine;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.UnitC;
import com.adelerobots.fioneg.entity.UtilizationC;

public class UtilizationEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<UtilizationC>{

	public UtilizationEng(String dataSource) {
		super(dataSource);		
	}
	
	
	/**
	 * Método que devuelve todas las unidades
	 * de uso
	 * @return Listado de objetos UtilizationC
	 */
	public List<UtilizationC> getAllUtilization(){
		return this.findAll();
	}
	
	/**
	 * Método que devuelve a lista de unidades de uso cuyos identificadores coincidan
	 * con los pasados como parámetros
	 * @param ids Identificadores únicos de las unidades buscadas
	 * @return Listado de objetos UnitC que se corresponden con los
	 * criterios de búsqueda
	 */
	public List<UtilizationC> getUtilization(List <Integer> ids){
		Criteria criteria = getCriteria();		
		
		criteria.add(Restrictions.in("intCodUtilization", ids));
		
		return criteria.list();
	}
	
	

}
