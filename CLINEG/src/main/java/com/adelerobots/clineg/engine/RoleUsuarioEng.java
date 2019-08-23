package com.adelerobots.clineg.engine;

import java.io.Serializable;

import com.adelerobots.clineg.entity.RoleUsuarioC;

/**
 * Data Access Object for RoleUsuarioC entity
 * 
 * @author adele
 *
 */
public class RoleUsuarioEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<RoleUsuarioC>{

	String conexion;
	
	public RoleUsuarioEng(String conexion) {
		super(conexion);
		this.conexion=conexion;
	}

	/**
	 * Obtiene el role group por su identificador interno
	 * @param id
	 */
	public RoleUsuarioC findById(
			final Integer id) 
	{
		RoleUsuarioC ent = this.findById((Serializable)id);
		return ent;	
	}
}
