package com.adelerobots.clineg.engine;

import java.io.Serializable;
import java.util.List;

import com.adelerobots.clineg.entity.CuentaC;

/**
 * Data Access Object for CuentaC entity
 * 
 * @author adele
 *
 */
public class CuentaEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<CuentaC>{

	String conexion;
	
	public CuentaEng(String conexion) {
		super(conexion);
		this.conexion=conexion;
	}

	/**
	 * Obtiene el cuenta por su identificador interno
	 * @param id
	 */
	public CuentaC findById(
			final Integer id) 
	{
		CuentaC ent = this.findById((Serializable)id);
		return ent;	
	}
	
	/**
	 * Método para devolver el listado de todas las cuentas
	 * 
	 * @return List<CuentaC>
	 */	
	public List<CuentaC> getAllCuentas() {
		return this.findAll();
	}
}
