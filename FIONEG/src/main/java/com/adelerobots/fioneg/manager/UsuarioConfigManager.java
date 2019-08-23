package com.adelerobots.fioneg.manager;

import java.math.BigDecimal;
import java.util.List;

import com.adelerobots.fioneg.engine.UsuarioConfigEng;
import com.adelerobots.fioneg.entity.usuarioconfig.UsuarioConfigC;
import com.adelerobots.fioneg.entity.usuarioconfig.UsuarioConfigPk;

/**
 * Manager para gestionar las configuraciones
 * @author adele
 *
 */
public class UsuarioConfigManager {

	private String conexion;

	public UsuarioConfigManager(String conexion) {
		super();
		this.conexion = conexion;
	}

	public UsuarioConfigC create(
			final Integer idUsuario, final Integer idConfig, final String name) {
		final UsuarioConfigEng dao = new UsuarioConfigEng(conexion);
		final UsuarioConfigC ent = new UsuarioConfigC(idUsuario, idConfig);
		if(name != null)
			ent.setNombre(name);
		dao.create(ent);
		return ent;
	}

	public UsuarioConfigC create(
			final UsuarioConfigPk usuarioConfigPk, final String name) {
		final UsuarioConfigEng dao = new UsuarioConfigEng(conexion);
		final UsuarioConfigC ent = new UsuarioConfigC(usuarioConfigPk);
		if(name != null)
			ent.setNombre(name);
		dao.create(ent);
		return ent;
	}

	public final UsuarioConfigC update(
			final Integer idUsuario, final Integer idConfig, final String name) {
		final UsuarioConfigEng dao = new UsuarioConfigEng(conexion);
		final UsuarioConfigC ent = dao.findUsuarioConfig(idUsuario, idConfig);
		if(name != null)
			ent.setNombre(name);
		dao.update(ent);
		dao.flush();
		return ent;
	}
	/**
	 * Obtiene la configuracion por los identificadores de usuario y configuracion
	 * @param idUsuario
	 * @param idConfig
	 * @see UsuarioConfigEng#findUsuarioConfig(Integer, Integer)
	 */
	public UsuarioConfigC getById(
			final Integer idUsuario, final Integer idConfig)
	{
		if (idUsuario == null || idConfig == null) return null;
		final UsuarioConfigEng dao = new UsuarioConfigEng(conexion);
		final UsuarioConfigC ent = dao.findUsuarioConfig(idUsuario, idConfig);
		return ent;
	}

	/**
	 * Obtiene la configuracion por los identificadores de usuario y configuracion
	 * @param idUsuario
	 * @param idConfig
	 * @see UsuarioConfigEng#findUsuarioConfig(Integer, Integer)
	 */
	public UsuarioConfigC getById(
			final BigDecimal idUsuario, final BigDecimal idConfig)
	{
		if (idUsuario == null || idConfig == null) return null;
		final UsuarioConfigEng dao = new UsuarioConfigEng(conexion);
		final UsuarioConfigC ent = dao.findUsuarioConfig(new Integer(idUsuario.intValue()), new Integer(idConfig.intValue()));
		return ent;
	}
	
	/**
	 * Método que devuelve todas las configuraciones del usuario
	 * 
	 */
	public List<UsuarioConfigC> getAllUsuarioConfigs(Integer idUsuario){
		if (idUsuario == null) return null;
		UsuarioConfigEng usuarioConfigDAO = new UsuarioConfigEng(conexion);
		return usuarioConfigDAO.getAllUserConfigs(idUsuario);
	}

}
