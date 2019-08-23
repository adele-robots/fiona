package com.adelerobots.fioneg.manager;

import java.util.List;

import com.adelerobots.fioneg.engine.DatoListaEng;
import com.adelerobots.fioneg.engine.TipoEng;
import com.adelerobots.fioneg.entity.DatoListaC;
import com.adelerobots.fioneg.entity.TipoC;

public class TipoManager {
	private String conexion;

	public TipoManager(String conexion) {
		super();
		this.conexion = conexion;
	}
	
	public TipoC getTipo(Integer cnTipo) {
		TipoEng tipoDao = new TipoEng (conexion);
		TipoC tipo = tipoDao.getTipo(cnTipo);
		
		return tipo;
	}
	
	/**
	 * Método que devuelve todos los parámetros de configuración
	 * disponibles para el usuario, es decir, los tipos básicos
	 * y los tipos que haya creado.
	 * 
	 * @param intCodUsuario Identificador del usuario 
	 * @return Lista con todos los tipos disponibles
	 */
	public List<TipoC> getAllTypesByUser(Integer intCodUsuario){
		TipoEng tipoDao = new TipoEng (conexion);		
		
		List <TipoC> lstTipos = tipoDao.getAllTypesByUser(intCodUsuario);	
		
		
		return lstTipos;
		
	}
	
	/**
	 * Método que devuelve todos los parámetros de configuración
	 * definidos por el usuario
	 * 
	 * @param intCodUsuario Identificador del usuario 
	 * @return Lista con todos los tipos definidios
	 */
	public List<TipoC> getUserTypes(Integer intCodUsuario){
		TipoEng tipoDao = new TipoEng (conexion);		
		
		List <TipoC> lstTipos = tipoDao.getUserTypes(intCodUsuario);	
		
		
		return lstTipos;
		
	}
	
	/**
	 * Método que permite crear un nuevo tipo
	 * 
	 * @param 
	 * @return Se devolverá el nuevo tipo 
	 * 
	 */
	public TipoC crearTipo(String nombreTipo, String funcValidacion, Character tipoBasico, Integer intCodUsuario){
		TipoEng tipoDao = new TipoEng(conexion);	
		TipoC tipo = new TipoC(nombreTipo, funcValidacion, tipoBasico, intCodUsuario);		
		tipoDao.create(tipo);	
		
		return tipo;
	}
	
	/**
	 * Método que permite borrar un tipo de parámetro de configuración
	 * 
	 * @param 
	 * @return Se devolverá el tipo borrado 
	 * 
	 */
	public TipoC deleteTipo(Integer cnTipo){
		TipoEng tipoDao = new TipoEng(conexion);	
		DatoListaEng datoListaDao = new DatoListaEng(conexion);
		
		TipoC tipo = tipoDao.getTipo(cnTipo);
		//Eliminar primero la lista DatoLista asociada, si tiene		
		List <DatoListaC> lstListValues = tipo.getLstDatosLista();
		if(lstListValues!=null){
			for(int i=0;i<lstListValues.size();i++){
				DatoListaC dato = lstListValues.get(i);
				lstListValues.remove(i);
				datoListaDao.delete(dato);
			}
		}
		tipoDao.delete(tipo);		
		
		return tipo;
	}
}
