package com.adelerobots.serneg.util.keys;

import com.treelogic.fawna.arq.negocio.utilidades.FawnaPropertyConfiguration;

/**
 * Clase de constantes de aplicacion.
 */
public final class Constantes {

	public static final String CTE_APP_FIO = "SERNEG";
	/* Constante JNDI para datasource */
	public static final String CTE_JNDI_DATASOURCE = "conf/SERNEG001_XA";
	public static final String CTE_JNDI_DATASOURCE_BACKUP = "conf/SERNEG002_XA";
	public static final String CTE_CONF_FIO = "conf/" + CTE_APP_FIO;
	
	// Servicios de negocio de PRINEG
	public static final String SN_SET_EXPRESS_CHECKOUT="SN031001";
	public static final String SN_SET_BILLING_AGREEMENT="SN031002";
	public static final String SN_ALTA_USUARIO_ST="SN031003";
	public static final String SN_CREATE_AVATAR_ST="SN031004";
	public static final String SN_UPDATE_AVATAR_HOSTING_ST="SN031005";
	public static final String SN_GET_HOSTING_PRIZE="SN031006";
	public static final String SN_ALTA_USUARIO_AIO = "SN032001";
	public static final String SN_CANCELAR_USUARIO_ST = "SN031007";
	
	// Constantes para devolución de ERROR/ÉXITO en los SN
	public static final String RESULTADO_OK = "OK";
	public static final String RESULTADO_ERROR = "ERROR";
	
	// Constantes auxiliares
	public static final String HHTP_PROTOCOL = "http://";
	
	
		
	static {				
		getSERVER_IP();
		getMAIL_SENDER_ADDR();
		getMAIL_SENDER_PW();
		getMAIL_NOTIFICATION_ADDR();
		getREGISTER_MAIL_SENDER();		
	}


	protected static String cfgProp(
			final String resource, final String property) {
		return FawnaPropertyConfiguration.getInstance().getProperty(
					resource == null ? CTE_CONF_FIO : resource, property);
	}
	
	/**
	 * Ip y puerto (server:port) de la máquina 
	 * en la que esta la web fiona (FIOPRE) 
	 * 
	 * @return
	 */
	public static String getSERVER_IP() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.SERVER_IP");
	}
	
	/**
	 * Obtiene la direccion email de autentificacion para enviar correos
	 * @return
	 */
	public static String getMAIL_SENDER_ADDR() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.MAIL_SENDER_ADDR");
	}
	
	/**
	 * Obtiene la password de autentificacion para enviar correos
	 * @return
	 */
	public static String getMAIL_SENDER_PW() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.MAIL_SENDER_PW");
	}
	
	/**
	 * Las notificaciones de registro se enviarán a la siguiente dirección email
	 * @return
	 */
	public static String getMAIL_NOTIFICATION_ADDR() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.MAIL_NOTIFICATION_ADDR");
	}
	
	/**
	 * Los usuarios que se registren recibirán un correo de esta dirección email
	 * @return
	 */
	public static String getREGISTER_MAIL_SENDER() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.REGISTER_MAIL_SENDER");
	}
	
	/**
	 * Devolverá la ruta donde se almacenan las claves pública y privada
	 * @return
	 */
	public static String getKEYS_PATH(){
		return cfgProp(CTE_CONF_FIO,"com.treelogic.fawna.presentacion.core.KEYS_PATH");
	}
	
	public static String getPRINEG_HOST(){
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.PRINEG_HOST");
	}
	
	public static String getCLINEG_HOST(){
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.CLINEG_HOST");
	}
	
	public static String getSN_NAMESPACE(){
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.SN_NAMESPACE");
	}
	
}