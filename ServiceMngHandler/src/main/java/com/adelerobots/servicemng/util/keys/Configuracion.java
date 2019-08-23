package com.adelerobots.servicemng.util.keys;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;

import org.apache.log4j.Logger;



public class Configuracion {
	
	public static String CTE_CONF_EMB = "conf/ServiceMngHandler";
	
	// acceso a configuracion en tomcat
	private static final String NAMING_ROOT_TOMCAT = "java:comp/env/";
	// acceso a configuracion en jboss
	private static final String NAMING_ROOT_JBOSS = "";
	
	private final static Logger logger = Logger.getLogger(Configuracion.class);
	
	private Map<String,Properties> propertiesList;
	
	private static Configuracion instance;
	
	private Configuracion() {
		propertiesList = new HashMap<String,Properties>();
	}
	
	public final static Configuracion getInstance() {
		if (instance == null) {
			instance = new Configuracion();
		}

		return instance;
	}
	
	
	/**
	 * Método que permitiría inicializar la configuración
	 * apuntando a un fichero de properties diferente
	 * al fichero por defecto
	 * 
	 * @param initParameter Parámetro que representaría el nombre
	 * del recurso JNDI que indicara la ruta al fichero de properties
	 * que se quiera utilizar para cargar la configuración
	 */
	public void init(String initParameter) {
		
		CTE_CONF_EMB=initParameter;		
	}
	
	/**
	 * Obtiene una propiedad del fichero de configuracion de la aplicacion
	 * 
	 * @param property
	 * @return
	 */
	public String getProperty(String property) {

		String fileName = CTE_CONF_EMB;
		Properties properties = getProperties(fileName);

		if (properties != null) {
			if (properties.getProperty(property) == null) {
				logger
						.error("Excepcion en Configuracion.getProperty: No se ha definido la propiedad '"
								+ property
								+ "'"
								+ " en el recurso '"
								+ fileName + "'");
		
				throw new RuntimeException(
						"No se ha definido la propiedad '" + property + "'"
								+ " en el recurso '" + fileName + "'");
			}
			if ("".equals(properties.getProperty(property)))
				logger.warn("La propiedad '" + property
						+ "' declarada en el fichero de recursos '" + fileName
						+ "' no tiene ningun valor asignado");
			return properties.getProperty(property);
		}
		logger
				.error("Excepcion en Configuracion.getProperty: No se ha definido ninguna propiedad en el fichero de configuracion '"
						+ fileName + "'");
		
		throw new RuntimeException(
				"No se ha definido ninguna propiedad en el fichero de configuracion '"
						+ fileName + "'");

	}
	
	/**
	 * Método que carga todas las propiedades que contenga el fichero cuyo
	 * nombre se pasa como parámetro
	 * 
	 * @param fileName Recurso JNDI
	 * @return Se devolverán las propiedades cargadas
	 */
	private Properties getProperties(String fileName) {

		Properties properties = (Properties) propertiesList.get(fileName);

		if (properties == null) {
			URL url = null;
			String configuracion = null;

			try {
				url = (URL) new InitialContext().lookup(NAMING_ROOT_JBOSS
						+ fileName);

			} catch (Throwable e) {

				try {
					url = (URL) new InitialContext().lookup(NAMING_ROOT_TOMCAT
							+ fileName);
				} catch (Throwable e1) {

				}
			}
			try {
				configuracion = (String) new InitialContext()
						.lookup(NAMING_ROOT_JBOSS + fileName);

				url = new URL(configuracion);
			} catch (Throwable e) {
				try {
					configuracion = (String) new InitialContext()
							.lookup(NAMING_ROOT_TOMCAT + fileName);

					url = new URL(configuracion);
				} catch (Throwable e1) {

				}
			}

			properties = new Properties();

			try {
				if (url != null) {
					properties.load(new FileInputStream(url.getFile()));

					propertiesList.put(fileName, properties);

					logger.info("Se ha cargado el recurso  '" + fileName + "'");
				} else {
					logger.error("No se ha podido cargar el recurso '"
							+ fileName + "'. Recurso no disponible");
			
					throw new RuntimeException(
							"No se ha podido cargar el recurso '" + fileName
									+ "'. Recurso no disponible");

				}

			} catch (FileNotFoundException e) {
				logger.error("No se ha podido cargar el recurso '" + fileName
						+ "'", e);
				
				throw new RuntimeException(
						"No se ha podido cargar el recurso '" + fileName + "'",
						e);

			} catch (IOException e) {
				logger.error("No se ha podido cargar el recurso '" + fileName
						+ "'", e);
				
				throw new RuntimeException(
						"No se ha podido cargar el recurso '" + fileName + "'",
						e);
			}			
			
		}
		
		return properties;
	

	}
}