package com.adelerobots.fioneg.engine;

import java.io.Serializable;
import java.util.List;

import com.adelerobots.fioneg.entity.TipoEntidadC;

/**
 * Data Access Object for TipoEntidadC entity
 * 
 * @author adele
 *
 */
public class TipoEntidadEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<TipoEntidadC>{

	String conexion;
	
	public TipoEntidadEng(String conexion) {
		super(conexion);
		this.conexion=conexion;
	}

	/**
	 * Obtiene el tipo de entidad por su identificador interno
	 * @param id
	 */
	public TipoEntidadC findById(
			final Integer id) 
	{
		TipoEntidadC ent = this.findById((Serializable)id);
		return ent;	
	}
	
	/**
	 * Método para devolver el listado de todos los tipos de entidad
	 * 
	 * @return List<TipoEntidadC>
	 */	
	public List<TipoEntidadC> getAllTipoEntidad() {
		return this.findAll();
	}
}
