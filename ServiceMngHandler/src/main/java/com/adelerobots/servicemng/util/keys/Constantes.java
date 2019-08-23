package com.adelerobots.servicemng.util.keys;

public class Constantes {
	
	public static final String GET_MAQUINA_AVATAR_USUARIO_SERVICE_ID = "SN030001";
	
	
	public static final String HOST_ADDRESS_PROPERTY = "webservices.config.address";
	public static final String PUBLIC_KEY_PATH = "fiona.config.publickey_path";
	
	private String hostAddress = null;
	private String maquinaAvatarUsuarioServiceUrl = null;	
	private String publicKeyPath = null;
	
		
	// Instancia unica de configuracion
	private static Constantes cfg = null;
	
	public Constantes() {		
				
		Configuracion conf = Configuracion.getInstance();
		
		hostAddress = conf.getProperty(HOST_ADDRESS_PROPERTY);
		maquinaAvatarUsuarioServiceUrl = hostAddress + GET_MAQUINA_AVATAR_USUARIO_SERVICE_ID;
		publicKeyPath = conf.getProperty(PUBLIC_KEY_PATH);
	}
	
	/**
	 * Obtiene una instancia a la configuración de la aplicación
	 *	
	 */
	public static Constantes getInstance(){
		// creamos una única instancia del la configuración
		if (cfg == null) {
			Constantes ccfg = null;
			ccfg = new Constantes();
			cfg = ccfg;
		}
		return cfg;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public String getMaquinaAvatarUsuarioServiceUrl() {
		return maquinaAvatarUsuarioServiceUrl;
	}

	public String getPublicKeyPath() {
		return publicKeyPath;
	}	
	
	

}
