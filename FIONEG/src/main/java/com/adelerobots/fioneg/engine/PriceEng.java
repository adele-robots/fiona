package com.adelerobots.fioneg.engine;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.PriceC;

public class PriceEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<PriceC>{

	public PriceEng(String dataSource) {
		super(dataSource);		
	}
	
	
	/**
	 * Método que devolverá el precio para un spark
	 * @param intCodSpark Identificador único del spark
	 * @return Se devolverá el objeto PriceC asociado
	 * al spark cuyo identificador se ha pasado como
	 * parámetro
	 */
	public PriceC getSparkPrice(Integer intCodSpark){
		Criteria criteria = getCriteria();
		
		criteria.createAlias("spark", "spark");
		
		criteria.add(Restrictions.eq("spark.cnSpark", intCodSpark));
		
		return (PriceC)criteria.uniqueResult();
	}
	
	public PriceC getSparkPrice(Integer intCodPrecio, Integer intCodSpark){
		Criteria criteria = getCriteria();
		
		criteria.createAlias("spark", "spark");
		
		criteria.add(Restrictions.eq("intCodPrice", intCodPrecio));
		criteria.add(Restrictions.eq("spark.cnSpark", intCodSpark));
		
		return (PriceC)criteria.uniqueResult();
	}

}
