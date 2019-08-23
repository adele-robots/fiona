package com.adelerobots.fioneg.manager;

import java.math.BigDecimal;
import java.util.Collection;

import com.adelerobots.fioneg.engine.RoleUsuarioEng;
import com.adelerobots.fioneg.entity.RoleUsuarioC;


/**
 * Manager para la gestion de groups/roles de usuario
 * @author adele
 */
public class RoleUsuarioManager {

	private String conexion;

	public RoleUsuarioManager(String conexion) {
		super();
		this.conexion = conexion;
	}




	/**
	 * Obtiene todos los group role registrados en la aplicacion
	 * @see RoleUsuarioEng#findAll()
	 */
	public Collection<RoleUsuarioC> findAll()
	{
		final RoleUsuarioEng dao = new RoleUsuarioEng(conexion);
		final Collection<RoleUsuarioC> col = dao.findAll();
		return col;
	}

	/**
	 * Obtiene el group role por su identificador interno
	 * @param id
	 * @see RoleUsuarioEng#findById(Integer)
	 */
	public RoleUsuarioC getById(
			final Integer id)
	{
		if (id == null) return null;
		final RoleUsuarioEng dao = new RoleUsuarioEng(conexion);
		final RoleUsuarioC ent = dao.findById(id);
		return ent;
	}

	/**
	 * Obtiene el group role por su identificador interno
	 * @param id
	 * @see RoleUsuarioEng#findById(Integer)
	 */
	public final RoleUsuarioC getById(
			final BigDecimal id)
	{
		if (id == null) return null;
		final RoleUsuarioC ent = this.getById(new Integer(id.intValue()));
		return ent;
	}




}
