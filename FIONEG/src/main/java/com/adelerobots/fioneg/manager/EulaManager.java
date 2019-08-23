package com.adelerobots.fioneg.manager;

import java.util.List;

import com.adelerobots.fioneg.engine.EulaEng;
import com.adelerobots.fioneg.entity.EulaC;

public class EulaManager {
	
	private String conexion;

	public EulaManager(String conexion) {
		super();
		this.conexion = conexion;
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
		EulaEng eulaDAO = new EulaEng(conexion);
		
		return eulaDAO.getEula(intCodEula);
	}
	
	/**
	 * Método que devuelve todos los EULA existentes
	 * en BBDD
	 * 
	 * @return
	 */
	public List<EulaC> getAllEula(Integer intCodEula){
		EulaEng eulaDAO = new EulaEng(conexion);
		
		return eulaDAO.getAllEula();
	}
	
	

}
