package com.adelerobots.fioneg.manager;

import java.util.List;

import com.adelerobots.fioneg.engine.UnitEng;
import com.adelerobots.fioneg.engine.UtilizationEng;
import com.adelerobots.fioneg.entity.UnitC;
import com.adelerobots.fioneg.entity.UtilizationC;

public class UtilizationManager {
	
	private String conexion;
	
	public UtilizationManager(String conexion){
		super();
		this.conexion = conexion;
	}	
	
	/**
	 * Método que devuelve todas las unidades
	 * de uso
	 * @return Listado de objetos UtilizationC
	 */
	public List<UtilizationC> getAllUtilization(){
		UtilizationEng utilizationDAO = new UtilizationEng(conexion);
		
		return utilizationDAO.getAllUtilization();
	}
	
	/**
	 * Método que devuelve a lista de unidades cuyos identificadores coincidan
	 * con los pasados como parámetros
	 * @param ids Identificadores únicos de las unidades buscadas
	 * @return Listado de objetos UnitC que se corresponden con los
	 * criterios de búsqueda
	 */
	public List<UtilizationC> getUtilization(List <Integer> ids){
		UtilizationEng useDAO = new UtilizationEng(conexion);
		
		return useDAO.getUtilization(ids);
	}

}
