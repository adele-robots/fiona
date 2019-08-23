package com.adelerobots.serneg.manager;

import com.adelerobots.serneg.util.keys.Constantes;

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
	
	
	public MaquinaManager getMaquinaManager(String conexion) {
		return new MaquinaManager(conexion);
	}
	
	public UsuariosManager getUsuariosManager(String conexion) {
		return new UsuariosManager(conexion);
	}
	
	public SparkManager getSparkManager(String conexion) {
		return new SparkManager(conexion);
	}
	
	public ProcesoMaquinaManager getProcesoMaquinaManager(String conexion){
		return new ProcesoMaquinaManager(conexion);
	}
}