package com.adelerobots.fioneg.engine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.opinion.OpinionC;




public class OpinionEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<OpinionC> {

	public OpinionEng(String dataSource) {
		super(dataSource);
	}
	
	/**
	 * Método que obtiene las n opiniones más recientes
	 * asociadas a un spark
	 * 
	 * @param intCodSpark Identificador único del spark
	 * @param numOpinions Número de opiniones que se quieren recuperar
	 * Si este parámetro es nulo, se obtienen todas las opiniones
	 * asociadas al spark
	 * 
	 * @return Lista de opiniones encontradas
	 */ 
	@SuppressWarnings("unchecked")
	public List<OpinionC> getNSparkOpinions(Integer intCodSpark, Integer numOpinions){
		List<OpinionC> lstOpiniones = new ArrayList<OpinionC>();
		
		Criteria criteria = getCriteria();
		
		criteria.createAlias("usuarioSpark", "usuarioSpark");
		
		criteria.add(Restrictions.eq("usuarioSpark.intCodSpark", intCodSpark));
		
		criteria.addOrder(Order.desc("datEnviada"));
		
		if(numOpinions != null)		
			criteria.setMaxResults(numOpinions);
		
		lstOpiniones = criteria.list();
						
		
		return lstOpiniones;
	}

	
	
	

}
