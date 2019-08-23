package com.adelerobots.clineg.manager;

import com.adelerobots.clineg.util.keys.Constantes;

/**
 * Factoria de managers (Patron factory)
 */
public class ManagerFactory {

	/** Instancia de la clase */
	private static ManagerFactory instance = new ManagerFactory();

	/** Constructor privado. */
	private ManagerFactory() {
	}

	/**
	 * Retorna una instancia de la factoria.
	 * 
	 * @return ManagerFactory
	 */
	public static ManagerFactory getInstance() {
		return instance;
	}

	//Manager para la gestion de usuarios
	public UsuariosManager getUsuariosManager(String datasource)
	{
		return new UsuariosManager(datasource);
	}
	
	public CuentaManager getCuentaManager(String datasource)
	{
		return new CuentaManager(datasource);
	}

	public RoleUsuarioManager getRoleUsuarioManager(String datasource)
	{
		return new RoleUsuarioManager(datasource);
	}
	
	public AvatarManager getAvatarManager(String datasource){
		return new AvatarManager(datasource);
	}

	
}