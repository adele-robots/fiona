package com.adelerobots.fioneg.engine;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.keyword.KeywordC;




public class KeywordEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<KeywordC> {

	public KeywordEng(String dataSource) {
		super(dataSource);
	}
	
	
	/**
	 * Método que devuelve todos los keywords existentes
	 * @return
	 */
	public List<KeywordC> getAllKeywords(){
		return this.findAll();
	}
	
	/**
	 * Método que devuelve las keywords cuyos ids
	 * coincidan con los pasados como parámetros
	 * 
	 * @param ids Lista de identificadores a buscar
	 * @return Se devuelve la lista de objetos de tipo
	 * KeywordC cuyos ids coinciden con el criterio
	 * de búsqueda
	 */
	@SuppressWarnings("unchecked")
	public List<KeywordC> getKeywords(List <Integer> ids){
		Criteria criteria = getCriteria();		
		
		criteria.add(Restrictions.in("intCodKeyword", ids));
		
		return criteria.list();
	}
	
	

}
