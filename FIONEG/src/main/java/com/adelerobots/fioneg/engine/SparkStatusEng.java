package com.adelerobots.fioneg.engine;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.sparkstatus.SparkStatusC;
import com.adelerobots.fioneg.entity.status.StatusC;



public class SparkStatusEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<SparkStatusC> {

	public SparkStatusEng(String dataSource) {
		super(dataSource);
	}

	
	/**
	 * Método que nos devolverá el status de un spark
	 * si este existe.
	 * @param intCodSpark Identificador único del spark
	 * @param intCodSatus Identificador único del status
	 * @return Se devolverá el sparkstatus o null si el status
	 * pasado como parámetro nunca fue asignado al spark correspondiente
	 */
	public SparkStatusC getStatus(Integer intCodSpark, Integer intCodSatus) {
		Criteria criteria = getCriteria();
		criteria.add(Restrictions.eq("intCodSpark", intCodSpark));
		criteria.add(Restrictions.eq("intCodStatus", intCodSatus));
		
	
		return (SparkStatusC) criteria.uniqueResult();
	}
	
	/**
	 * Método que devuelve el último estado (y por tanto el válido)
	 * de un spark
	 * 
	 * @param intCodSpark Identificador único del spark
	 * @return Último status del spark, o 'null' si el spark
	 * aún no tiene asociado ningún estado
	 */
	public StatusC getLastSparkStatus(Integer intCodSpark){
		Criteria criteria = getCriteria();

		criteria.add(Restrictions.eq("intCodSpark", intCodSpark));
		criteria.addOrder(Order.desc("dateCambio"));
		
		List<SparkStatusC> lstSparkStatus = criteria.list();
		
		StatusC status = null;
		
		if(lstSparkStatus != null && !lstSparkStatus.isEmpty()){
			status = lstSparkStatus.get(0).getSparkStatus().getStatus();
		}
		
		return status;
		
	}
	
	/**
	 * Método que devuelve la lista de estados por los que
	 * ha pasado un spark
	 * 
	 * @param intCodSpark Identificador único del spark
	 * @return Se devuelve la lista de StatusC del
	 * spark ordenada decrecientemente por fecha
	 */
	public List<SparkStatusC> getListSparkStatus(Integer intCodSpark){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("intCodSpark", intCodSpark)).addOrder(Order.desc("dateCambio"));
		
		return criteria.list();
	}
	

}
