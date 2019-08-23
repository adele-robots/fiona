package com.adelerobots.fioneg.engine;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.EulaC;

public class EulaEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<EulaC>{

	public EulaEng(String dataSource) {
		super(dataSource);		
	}
	
	/**
	 * Método que devuelve el EULA cuyo
	 * identificador coincida con el pasado
	 * como parámetro
	 * 
	 * @param intCodEula Identificador único del
	 * EULA buscado
	 * @return Se devolverá el EULA encontrado o
	 * null en caso contrario
	 */
	public EulaC getEula(Integer intCodEula){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("intCodEula", intCodEula));
		
		EulaC eula = (EulaC)criteria.uniqueResult();
		
		return eula;		
	}
	
	/**
	 * Método que devuelve todos los EULA existentes
	 * en BBDD
	 * 
	 * @return
	 */
	public List<EulaC> getAllEula(){
		return findAll();
	}

	
	
}
