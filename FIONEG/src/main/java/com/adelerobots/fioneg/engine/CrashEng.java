package com.adelerobots.fioneg.engine;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.crash.CrashC;




public class CrashEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<CrashC> {

	public CrashEng(String dataSource) {
		super(dataSource);
	}

	
	/**
	 * Método que devuelve a lista de errores asociados a
	 * un spark por orden decreciente según fecha
	 * 
	 * @param intCodSpark Identificador único del spark
	 * @return Se devuelve la lista de errores para el spark
	 */
	@SuppressWarnings("unchecked")
	public List<CrashC> getCrashList (Integer intCodSpark){
		Criteria criteria = getCriteria();
		criteria.addOrder(Order.desc("datCrash"));
		criteria.createCriteria("spark").add(Restrictions.eq("cnSpark", intCodSpark));
		
		return criteria.list();
	}
	

}
