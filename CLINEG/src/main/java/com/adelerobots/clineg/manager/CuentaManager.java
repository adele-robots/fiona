package com.adelerobots.clineg.manager;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import com.adelerobots.clineg.engine.CuentaEng;
import com.adelerobots.clineg.entity.CuentaC;

/**
 * Manager para gestionar los account types a los que estan suscritos los usuarios
 * @author adele
 *
 */
public class CuentaManager {

	private String conexion;

	public CuentaManager(String conexion) {
		super();
		this.conexion = conexion;
	}




	/**
	 * Obtiene todos los cuenta type registrados en la aplicacion
	 * @see CuentaEng#findAll()
	 */
	public Collection<CuentaC> findAll()
	{
		final CuentaEng dao = new CuentaEng(conexion);
		final Collection<CuentaC> col = dao.findAll();
		return col;
	}

	/**
	 * Obtiene el cuenta type por su identificador interno
	 * @param id
	 * @see CuentaEng#findById(Integer)
	 */
	public CuentaC getById(
			final Integer id)
	{
		if (id == null) return null;
		final CuentaEng dao = new CuentaEng(conexion);
		final CuentaC ent = dao.findById(id);
		return ent;
	}

	/**
	 * Obtiene el cuenta type por su identificador interno
	 * @param id
	 * @see CuentaEng#findById(Integer)
	 */
	public final CuentaC getById(
			final BigDecimal id)
	{
		if (id == null) return null;
		final CuentaC ent = this.getById(new Integer(id.intValue()));
		return ent;
	}
	
	/**
	 * Método que devuelve todas las cuentas existentes
	 * 
	 */
	public List<CuentaC> getAllCuentas(){
		CuentaEng cuentaDAO = new CuentaEng(conexion);
		return cuentaDAO.getAllCuentas();
	}

}
