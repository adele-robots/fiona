package com.adelerobots.fioneg.manager;

import java.math.BigDecimal;
import java.util.List;

import com.adelerobots.fioneg.engine.TipoEntidadEng;
import com.adelerobots.fioneg.entity.TipoEntidadC;

/**
 * Manager para gestionar los tipos de entidad
 * @author adele
 *
 */
public class TipoEntidadManager {

	private String conexion;

	public TipoEntidadManager(String conexion) {
		super();
		this.conexion = conexion;
	}


	/**
	 * Obtiene el tipo de entidad por su identificador interno
	 * @param id
	 * @see TipoEntidadEng#findById(Integer)
	 */
	public TipoEntidadC getById(
			final Integer id)
	{
		if (id == null) return null;
		final TipoEntidadEng dao = new TipoEntidadEng(conexion);
		final TipoEntidadC ent = dao.findById(id);
		return ent;
	}

	/**
	 * Obtiene el tipo de entidad por su identificador interno
	 * @param id
	 * @see TipoEntidadEng#findById(Integer)
	 */
	public final TipoEntidadC getById(
			final BigDecimal id)
	{
		if (id == null) return null;
		final TipoEntidadC ent = this.getById(new Integer(id.intValue()));
		return ent;
	}
	
	/**
	 * Método que devuelve todas los tipos de entidad existentes
	 * 
	 */
	public List<TipoEntidadC> getAllTipoEntidad(){
		TipoEntidadEng tipoEntidadDAO = new TipoEntidadEng(conexion);
		return tipoEntidadDAO.getAllTipoEntidad();
	}

}
