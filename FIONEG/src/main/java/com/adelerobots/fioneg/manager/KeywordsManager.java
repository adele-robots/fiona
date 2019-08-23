package com.adelerobots.fioneg.manager;

import java.util.List;

import com.adelerobots.fioneg.engine.KeywordEng;
import com.adelerobots.fioneg.entity.keyword.KeywordC;

public class KeywordsManager {

	private String conexion;

	public KeywordsManager(String conexion) {
		super();
		this.conexion = conexion;
	}
	
	/**
	 * Método que devuelve todas las keywords existentes
	 * 
	 * @return
	 */
	public List<KeywordC> getAllKeywords(){
		KeywordEng keywordsDAO = new KeywordEng(conexion);
		
		return keywordsDAO.getAllKeywords();
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
	public List<KeywordC> getKeywords(List <Integer> ids){
		KeywordEng keywordsDAO = new KeywordEng(conexion);
		
		return keywordsDAO.getKeywords(ids);
	}
	
}