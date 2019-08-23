package com.adelerobots.fioneg.manager;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import com.adelerobots.fioneg.engine.EntidadEng;
import com.adelerobots.fioneg.engine.UsuarioEng;
import com.adelerobots.fioneg.entity.EntidadC;

/**
 * Manager para gestionar los Entidad de la aplicacion
 * @author adele
 *
 */
public class EntidadManager {

	private String conexion;

	public EntidadManager(String conexion) {
		super();
		this.conexion = conexion;
	}




	/**
	 * Obtiene todos los entidad registrados en la aplicacion
	 * @see EntidadEng#findAll()
	 */
	public Collection<EntidadC> findAll()
	{
		final EntidadEng dao = new EntidadEng(conexion);
		final Collection<EntidadC> col = dao.findAll();
		return col;
	}

	/**
	 * Obtiene el entidad por su identificador interno
	 * @param id
	 * @see EntidadEng#findById(Integer)
	 */
	public EntidadC getById(
			final Integer id)
	{
		if (id == null) return null;
		final EntidadEng dao = new EntidadEng(conexion);
		final EntidadC ent = dao.findById(id);
		return ent;
	}

	/**
	 * Obtiene el entidad por su identificador interno
	 * @param id
	 * @see EntidadEng#findById(Integer)
	 */
	public final EntidadC getById(
			final BigDecimal id)
	{
		if (id == null) return null;
		final EntidadC ent = this.getById(new Integer(id.intValue()));
		return ent;
	}

	/**
	 * Obtiene la entidad por su nombre
	 * @param name
	 * @see EntidadEng#findByName(String)
	 */
	public EntidadC getByName(
			final String name)
	{
		if (name == null) return null;
		final EntidadEng dao = new EntidadEng(conexion);
		final EntidadC ent = dao.findByName(name);
		return ent;
	}

	/**
	 * 
	 * 
	 * @param name nombre de la entidad
	 * @param entitytype tipo de entidad
	 * @param website sitio web
	 * @param workemail correo electronico de la entidad
	 * @param phonecode prefijo del pais
	 * @param phonenumber numero de telefono
	 * @param phoneext extension de linea
	 * @return
	 */
	public EntidadC create(
			final String name, final Integer entitytype, 
			final String website, final String workemail, 
			final String phonecode, final String phonenumber, 
			final String phoneext) 
	{
		final EntidadEng dao = new EntidadEng(conexion);
		final EntidadC ent = new EntidadC();
		ent.setNombre(name);
		ent.setTipoEntidad(entitytype);
		ent.setWebsite(website);
		ent.setWorkEmail(workemail);
		ent.setPhoneCode(phonecode);
		ent.setPhoneNumber(phonenumber);
		ent.setPhoneExt(phoneext);
				
		dao.create(ent);
		return ent;
	}

	/**
	 * Actualiza los datos de la entidad
	 * 
	 * @param id identificador de la entidad
	 * @param name nombre de la entidad
	 * @param entitytype tipo de entidad
	 * @param website sitio web
	 * @param workemail correo electronico de la entidad
	 * @param phonecode prefijo del pais
	 * @param phonenumber numero de telefono
	 * @param phoneext extension de linea
	 * @return
	 */
	public final EntidadC update(
			final Integer id, final String name,
			final Integer entitytype, final String website,
			final String workemail, final String phonecode,
			final String phonenumber, final String phoneext) 
	{
		final EntidadEng dao = new EntidadEng(conexion);
		final EntidadC ent = dao.findById(id);
		if (ent != null) {
			if(name != null) ent.setNombre(name);
			if(entitytype != null) ent.setTipoEntidad(entitytype);
			if(website != null) ent.setWebsite(website);
			if(workemail != null) ent.setWorkEmail(workemail);
			if(phonecode != null) ent.setPhoneCode(phonecode);
			if(phonenumber != null) ent.setPhoneNumber(phonenumber);
			ent.setPhoneExt(phoneext);
			dao.update(ent);
			return ent;
		}
		return ent;
	}

	/**
	 * Inspects for Legal Entity Name duplicates
	 * @param entityId the id to check (this entity will be ignored on process), <code>null</code> to ignore this search param
	 * @param entityName the Legal Entity Name to check
	 * @return
	 */
	public boolean isLegalEntityNameTaken(Integer entityId, String entityName) {
		List<String> c = getAllEntityNames(entityId, entityName);
		return c != null && !c.isEmpty();
	}

	/**
	 * Retrieve the Entity names by that criteria
	 * @param entityId the id to check (this user will be ignored on process), <code>null</code> to ignore this search param
	 * @param entityName the nickname to search, <code>null</code> to ignore this search param
	 * @return
	 */
	public List<String> getAllEntityNames(Integer entityId, String entityName) {
		EntidadEng entidadDao = new EntidadEng(conexion);
		return entidadDao.getAllEntityNames(entityId, entityName);
	}


}
