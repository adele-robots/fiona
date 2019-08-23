package com.adelerobots.fioneg.manager;

import java.util.List;

import com.adelerobots.fioneg.engine.UnitEng;
import com.adelerobots.fioneg.entity.UnitC;

public class UnitManager {
	
	private String conexion;
	
	public UnitManager(String conexion){
		super();
		this.conexion = conexion;
	}	
	
	/**
	 * Método que devuelve todas las unidades
	 * de cobro
	 * @return Listado de objetos UnitC
	 */
	public List<UnitC> getAllUnits(){
		UnitEng unitDAO = new UnitEng(conexion);
		
		return unitDAO.getAllUnits();
	}
	
	/**
	 * Método que devuelve las unidades 
	 * de cobro VISIBLES
	 * @return Listado de objetos UnitC
	 */
	public List<UnitC> getVisibleUnits(){
		UnitEng unitDAO = new UnitEng(conexion);
		
		return unitDAO.getVisibleUnits();
	}
	
	/**
	 * Método que devuelve a lista de unidades cuyos identificadores coincidan
	 * con los pasados como parámetros
	 * @param ids Identificadores únicos de las unidades buscadas
	 * @return Listado de objetos UnitC que se corresponden con los
	 * criterios de búsqueda
	 */
	public List<UnitC> getUnits(List <Integer> ids){
		UnitEng unitDAO = new UnitEng(conexion);
		
		return unitDAO.getUnits(ids);
	}
	
	/**
	 * Método que devuelve la entidad de tipo "UnitC" cuyo identificador
	 * coincida con el pasado como parámetro
	 * @param intCodUnit Identificador único de la entidad buscada
	 * @return Se devolverá el objeto que cumpla los criterios de búsqueda
	 */
	public UnitC getUnit(Integer intCodUnit){
		UnitEng unitDAO = new UnitEng(conexion);
		
		return unitDAO.getUnit(intCodUnit);
		
	}

}
