package com.adelerobots.fioneg.engine;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.rejection.RejectionC;




public class RejectionEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<RejectionC> {

	public RejectionEng(String dataSource) {
		super(dataSource);
	}

	
	/**
	 * Método que devuelve a lista de motivos por los que se ha rechazado
	 * un spark por orden decreciente según fecha
	 * 
	 * @param intCodSpark Identificador único del spark
	 * @return Se devuelve la lista de motivos de rechazo para el spark
	 */
	@SuppressWarnings("unchecked")
	public List<RejectionC> getRejectionList (Integer intCodSpark){
		Criteria criteria = getCriteria();
		criteria.addOrder(Order.desc("datRejection"));
		criteria.createCriteria("spark").add(Restrictions.eq("cnSpark", intCodSpark));
		
		return criteria.list();
	}
	

}
