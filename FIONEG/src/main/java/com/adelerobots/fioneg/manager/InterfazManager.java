package com.adelerobots.fioneg.manager;

import java.util.List;

import com.adelerobots.fioneg.engine.InterfazEng;
import com.adelerobots.fioneg.entity.InterfazC;


public class InterfazManager {

	private String conexion;

	public InterfazManager(String conexion) {
		super();
		this.conexion = conexion;
	}
	
	public InterfazC getInterfaz(Integer cnInterfaz) {
		InterfazEng ifazDAO = new InterfazEng(conexion);				
		InterfazC interfaz = ifazDAO.getInterfaz(cnInterfaz);
		
		return interfaz;
	}
	
	public InterfazC getIfazByNameType(String ifazName, String ifazType){
		final InterfazEng ifazDAO = new InterfazEng(conexion);
		final InterfazC ifaz = ifazDAO.getIfazByNameType(ifazName, ifazType);
		return ifaz;
	}
	
	/**
	 * Método que devuelve todas las interfaces existentes
	 * 
	 * @return
	 */
	public List<InterfazC> getAllInterfaces(){
		InterfazEng ifazDAO = new InterfazEng(conexion);
		
		return ifazDAO.getAllInterfaces();
	}
	
	/**
	 * Método que devuelve todas las interfaces por tipo
	 * 
	 * @param interfaceType Tipo de interfaz
	 * @return
	 */
	public List<InterfazC> getInterfacesByType(String interfaceType){
		InterfazEng ifazDAO = new InterfazEng(conexion);
		
		return ifazDAO.getInterfacesByType(interfaceType);
	}
	
}
