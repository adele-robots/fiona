package com.adelerobots.fioneg.util.keys;

import java.io.File;

import com.adelerobots.fioneg.util.FunctionUtils;
import com.treelogic.fawna.arq.negocio.utilidades.FawnaPropertyConfiguration;

/**
 * Clase de constantes de aplicacion.
 */
public final class Constantes {

	public static final String CTE_APP_FIO = "FIONEG";
	/* Constante JNDI para datasource */
	public static final String CTE_JNDI_DATASOURCE = "conf/FIONEG001_XA";
	public static final String CTE_CONF_FIO = "conf/" + CTE_APP_FIO;
	
	/**Nombre por defecto del fichero con la configuracion del avatar */
	public static final String DEFAULT_AVATAR_FILENAME = "avatar.xml";
	
	/** Tipos de cuenta de usuario **/
	public static final int CTE_ACCOUNT_FREE= 1;
	public static final int CTE_ACCOUNT_BASIC= 2;
	public static final int CTE_ACCOUNT_PRO= 3;
	public static final int CTE_ACCOUNT_CORPORATE= 4;
	
	/** Tipos de cuenta de usuario **/
	public static final String CTE_ACCOUNT_FREE_RESOLUTION= "low_resolution";
	public static final String CTE_ACCOUNT_BASIC_RESOLUTION= "standard_resolution";
	public static final String CTE_ACCOUNT_PRO_RESOLUTION= "high_resolution";
	public static final String CTE_ACCOUNT_CORPORATE_RESOLUTION= "high_resolution";
	
	/** Resoluciones */
	public static final int SMALL_RESOLUTION = 1;
	public static final int MEDIUM_RESOLUTION = 2;
	public static final int BIG_RESOLUTION = 3;
	
	/** Numero de configuraciones */
	public static final int CTE_CONFIGS_FREE = 1;
	public static final int CTE_CONFIGS_BASIC = 1;
	public static final int CTE_CONFIGS_PRO = 5;
	public static final int CTE_CONFIGS_CORPORATE = 10;
	
	/** Unidades de tiempo */
	public static final Integer FREE = new Integer(0);
	public static final Integer MONTHS = new Integer(1);
	public static final Integer YEARS = new Integer(2);
	public static final Integer CHAT_MONTHS = new Integer(4);
	public static final Integer CHAT_YEARS = new Integer(5);
	
	
	/** Pass phrase */
	public static final String PASS_PHRASE = "73t4A5B8103J0AA1";
	
	static {
		// Comprobar los parametros. 
		/* Es lo mismo que si tuvieramos una public static final var como estaba antes 
		 * pero pasandolo a un method conservamos la funcionalidad cache de FAWNA en los properties */
		getR5appserverUri();
		getDesignerIportUri();
		getKILL_COMMAND();
		getUSERS_PATH();
		getUPLOAD_ADMIN_MAIL();
		getPROCESS_PATH();
		getAVATAR_SCRIPT_PATH();
		getLD_LIBRARY_PATH();
		getAPPLICATION_DATA();
		getDESIGNER_PATH();
		getSERVER_IP();
		getMAIL_SENDER_ADDR();
		getMAIL_SENDER_PW();
		getMAIL_NOTIFICATION_ADDR();
		getREGISTER_MAIL_SENDER();
		getUSERDIRS_SCRIPT();
		getUSERDIRS_GEN_INI_SCRIPT();
		getCORE_SPARKS();
	}


	protected static String cfgProp(
			final String resource, final String property) {
		return FawnaPropertyConfiguration.getInstance().getProperty(
					resource == null ? CTE_CONF_FIO : resource, property);
	}

	/**
	 * Obtiene el rtmp URI donde el stream del avatar esta escuchando
	 * @return
	 */
	public static String getR5appserverUri() {
		String value = cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.r5appserver");
		return FunctionUtils.stripQuotes(value);
	}

	/**
	 * Obtiene el URI donde el avatar designer esta instalado
	 * @return
	 */
	public static String getDesignerIportUri() {
		String value = cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.designer_iport");
		return FunctionUtils.stripQuotes(value);
	}
	
	/**
	 * Obtiene la cadena de comando para matar los procesos de avatar de usuario
	 * @return
	 */
	public static String getKILL_COMMAND() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.KILL_COMMAND");
	}
	
	/**
	 * Obtiene el directorio padre donde se guardan los ficheros de configuracion 
	 * del avatar para todos los usuarios
	 * @return
	 */
	public static String getUSERS_PATH() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.USERS_PATH");
	}
	
	/**
	 * Obtiene el time_alive para los procesos
	 * @return
	 */
	public static String getTimeAlive() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.time_alive");
	}
	
	/**
	 * Obtiene el tiempo de espera entre ejecuciones del avatar para usuarios con cuenta 
	 * 'free'
	 * @return
	 */
	public static String getFrozenTime() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.time_frozen");
	}
	
	/**
	 * Obtiene el número máximo de procesos por máquina
	 * @return
	 */
	public static String getMaxProcessPerMachine() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.max_process_per_machine");
	}

	/**
	 * Obtiene el directorio padre donde se guardan los ficheros de configuracion 
	 * del avatar para todos los usuarios
	 * 
	 * @return
	 * @see #getUSERS_PATH()
	 */
	public static File getNfsUsersFolder() {
		return new File(getUSERS_PATH());
	}

	/**
	 * Obtiene el path padre donde los usuarios tienen su configuracion publica
	 * como son los thumbnails del avatar y del UML design. 
	 * @return
	 */
	public static File getNfsUsersPublicFolder() {
		final File parent = getNfsUsersFolder();
		return new File(parent, "public");
	}

	/**
	 * Obtiene el path padre donde los usuarios tienen su parte privada como son
	 * la configuracion de avatares, los logs del stream de consola, etc.
	 * @return
	 */
	public static File getNfsUsersPrivateFolder() {
		final File parent = getNfsUsersFolder();
		return new File(parent, "private");
	}

	/**
	 * Obtiene el path padre donde el usuario especificado tiene su configuracion publica
	 * como son los thumbnails del avatar y del UML design. 
	 * 
	 * @param umd5 hash unico de usuario
	 * @return
	 */
	public static File getNfsUserPublicFolder(final String umd5) {
		final File parent = getNfsUsersPublicFolder();
		return new File(parent, umd5);
	}

	/**
	 * Obtiene el path padre donde el usuario especificado tiene su parte privada como son
	 * la configuracion de avatares, los sparks desarrollados, los logs del stream de consola, 
	 * etc.
	 * @param umd5 hash unico de usuario
	 * @return
	 */
	public static File getNfsUserFolder(final String umd5) {
		final File parent = getNfsUsersPrivateFolder();
		return new File(parent, umd5);
	}

	/**
	 * Obtiene el path padre donde el usuario especificado tiene sus logs de avatares
	 * @param umd5 hash unico de usuario
	 * @return
	 */
	public static File getNfsUserLogsFolder(final String umd5) {
		final File parent = getNfsUserFolder(umd5);
		return new File(parent, "logs");
	}

	/**
	 * Obtiene el path padre donde el usuario especificado sube sus sparks desarrollados
	 * @param umd5 hash unico de usuario
	 * @return
	 */
	public static File getNfsUserUploadedSparksFolder(final String umd5) {
		final File parent = getNfsUserFolder(umd5);
		return new File(parent, "UploadedSparks");
	}
	
	/**
	 * Obtiene la direccion email de notificacion cuando los usuarios suben sparks
	 * @return
	 */
	public static String getUPLOAD_ADMIN_MAIL() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.UPLOAD_ADMIN_MAIL");
	}
	
	/**
	 * Obtiene la cadena de la ubicación del directorio del ejecutable Run
	 * @return
	 */
	public static String getPROCESS_PATH() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.PROCESS_PATH");
	}
	
	/**
	 * Obtiene la cadena de la ubicación del script avatar.sh
	 * @return
	 */
	public static String getAVATAR_SCRIPT_PATH() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.AVATAR_SCRIPT_PATH");
	}
	
	/**
	 * Obtiene la cadena de los paths de librerías a incluir
	 * @return
	 */
	public static String getLD_LIBRARY_PATH() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.LD_LIBRARY_PATH");
	}
	
	/**
	 * Obtiene la cadena de los paths de librerías a incluir
	 * @return
	 */
	public static String getAPPLICATION_DATA() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.APPLICATION_DATA");
	}
	
	/**
	 * Obtiene el directorio padre donde se guardan las librerias 
	 * del avatar para todos los usuarios
	 * 
	 * @return
	 * @see #getAPPLICATION_DATA()
	 */
	public static File getApplicationDataFolder() {
		return new File(getAPPLICATION_DATA());
	}
	
	/**
	 * Obtiene el directorio padre donde se guardan las librerias 
	 * del avatar para un usuario determinado
	 * 
	 * @param usermaild5 hash unico de usuario
	 * @return
	 */
	public static File getApplicationDataFolder(String usermaild5) {
		File parent = getApplicationDataFolder();
		return new File(parent, usermaild5);
	}
	
	/**
	 * Obtiene el directorio padre donde se guardan los ficheros de configuracion 
	 * del avatar designer para un usuario en concreto
	 * 
	 * @param usermaild5 hash unico de usuario
	 * @return
	 */
	public static File getDesignerEnvFolder(String usermaild5) {
		File parent = getApplicationDataFolder();
		return new File(parent, usermaild5 + ".0jbpm/");
	}
	
	/**
	 * Obtiene el fichero donde un usuario concreto tiene los parametros
	 * de configuracion de su avatar designer
	 * 
	 * @param usermaild5 hash unico de usuario
	 * @return
	 */
	public static File getDesignerEnvConfigFile(String usermaild5) {
		File parent = getDesignerEnvFolder(usermaild5);
		return new File(parent, usermaild5 + ".0jbpm.json");
	}
	
	/**
	 * Obtiene el directorio padre donde se guardan los ficheros de configuracion 
	 * del avatar designer para todos los usuarios
	 * @return
	 */
	public static String getDESIGNER_PATH() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.DESIGNER_PATH");
	}
	
	/**
	 * Obtiene el directorio padre donde se guardan los ficheros de configuracion 
	 * del avatar designer para todos los usuarios
	 * @return
	 */
	public static File getUserDesignerFolder() {
		File parent = new File(getDESIGNER_PATH());
		return parent;
	}
	
	/**
	 * Obtiene el directorio padre donde se guardan los ficheros de configuracion 
	 * del avatar designer para un usuario en concreto
	 * 
	 * @param usermaild5 hash unico de usuario
	 * @return
	 */
	public static File getUserDesignerFolder(String usermaild5) {
		File parent = getUserDesignerFolder();
		return new File(parent, usermaild5 + ".0jbpm/");
	}
	
	/**
	 * Obtiene el fichero donde un usuario concreto tiene los parametros
	 * de configuracion de su avatar designer
	 * 
	 * @param usermaild5 hash unico de usuario
	 * @return
	 */
	public static File getUserDesignerConfigFile(String usermaild5) {
		File parent = getUserDesignerFolder(usermaild5);
		return new File(parent, usermaild5 + ".0jbpm.json");
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
	 * Cadena de comando para script de ejecución de creación de directorios de usuario
	 * @return
	 */
	public static String getUSERDIRS_SCRIPT() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.USERDIRS_SCRIPT");
	}
	
	/**
	 * Cadena de comando para script de ejecución de borrado de directorios de usuario
	 * @return
	 */
	public static String getDELETE_USERDIRS_SCRIPT() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.DELETE_USERDIRS_SCRIPT");
	}
	
	/**
	 * Cadena de comando para script de ejecución de modificación de directorios de usuario
	 * @return
	 */
	public static String getMODIFYUSERDIRS_SCRIPT() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.MODIFYUSERDIRS_SCRIPT");
	}
	
	/**
	 * Cadena de comando para script de ejecución de creación de configuracion general de usuario
	 * @return
	 */
	public static String getUSERDIRS_GEN_INI_SCRIPT() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.USERDIRS_GEN_INI");
	}
	
	/**
	 * Identificadores de los sparks habilitados como del nucleo de la aplicacion
	 * @return
	 */
	public static String getCORE_SPARKS() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.CORE_SPARKS");
	}

	/**
	 * Obtiene el directorio padre donde se guardan las plantillas de correo, etc
	 * 
	 * @return
	 */
	public static File getTemplatesFolder() {
		final String prop = cfgProp(CTE_CONF_FIO, "com.adelerobots.fiona.TEMPLATES_PATH");
		return new File(prop);
	}
	
	/**
	 * Obtiene el directorio donde se almacenarán los iconos subidos por los desarrolladores
	 * para sus sparks
	 * @return
	 */
	public static String getIconsFolder() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.icons_path");
		
	}
	
	/**
	 * Obtiene el directorio donde se almacenarán los banners subidos por los desarrolladores
	 * para sus sparks
	 * @return
	 */
	public static String getBannersFolder() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.banners_path");
		
	}
	
	/**
	 * Obtiene el directorio donde se almacenarán los videos subidos por los desarrolladores
	 * para sus sparks
	 * @return
	 */
	public static String getVideosFolder() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.videos_path");
		
	}
	
	
	/**
	 * Obtiene el path padre donde el usuario especificado sube los iconos
	 * de los sparks desarrollados
	 * 
	 * @param umd5 hash unico de usuario
	 * @return
	 */
	public static File getNfsUserUploadedIconsFolder(final String umd5) {
		final File parent = getNfsUserFolder(umd5);
		return new File(parent, "icons");
	}
	
	/**
	 * Obtiene el path padre donde el usuario especificado sube los banners
	 * de los sparks desarrollados
	 * 
	 * @param umd5 hash unico de usuario
	 * @return
	 */
	public static File getNfsUserUploadedBannersFolder(final String umd5) {
		final File parent = getNfsUserFolder(umd5);
		return new File(parent, "banners");
	}
	
	/**
	 * Obtiene el path padre donde el usuario especificado sube los videos
	 * de los sparks desarrollados
	 * 
	 * @param umd5 hash unico de usuario
	 * @return
	 */
	public static File getNfsUserUploadedVideosFolder(final String umd5) {
		final File parent = getNfsUserFolder(umd5);
		return new File(parent, "videos");
	}
	
	/**
	 * Obtiene la resolución y bitrates para ejecutar el avatar dependiendo del tipo de usuario
	 * @return
	 */
	public static String getAvatarResolution(final Integer accountId) {
		switch(accountId){
		case CTE_ACCOUNT_FREE:
			return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.low_resolution");
		case CTE_ACCOUNT_BASIC:
			return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.standard_resolution");
		case CTE_ACCOUNT_PRO:
			return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.high_resolution");
		case CTE_ACCOUNT_CORPORATE:
			return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.high_resolution");
		default:
			return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.low_resolution");
		}				
	}
	
	/**
	 * Obtiene la cadena de comando para matar los procesos de avatar de usuario
	 * @return
	 */
	public static String getDESCRIPCION_PERFIL() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.paypal.DESCRIPCION_PERFIL");
	}
	
	/**
	 * Obtiene el numero de configuraciones dependiendo del tipo de cuenta
	 * @return int Numero de configuraciones del tipo de cuenta
	 */
	public static int getConfigNum(final Integer accountId) {
		switch(accountId){
		case CTE_ACCOUNT_FREE:
			return CTE_CONFIGS_FREE;
		case CTE_ACCOUNT_BASIC:
			return CTE_CONFIGS_BASIC;
		case CTE_ACCOUNT_PRO:
			return CTE_CONFIGS_PRO;
		case CTE_ACCOUNT_CORPORATE:
			return CTE_CONFIGS_CORPORATE;
		default:
			return CTE_CONFIGS_FREE;
		}				
	}
	
	
	/** 
	 * Métodos para la obtención de las credenciales de la API 
	 */
	public static String getUserApi(){
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.user_api");
	}
	
	public static String getPwdApi(){
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.pwd_api");
	}
	
	public static String getSignature(){
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.signature");
	}
	
	public static String getEnvironment(){
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.environment");
	}
	
	public static String getCOMISION_FREE(){
		return cfgProp(CTE_CONF_FIO,"com.treelogic.fawna.presentacion.core.COMISION_FREE");
	}
	
	public static String getCOMISION_BASIC(){
		return cfgProp(CTE_CONF_FIO,"com.treelogic.fawna.presentacion.core.COMISION_BASIC");
	}
	
	public static String getCOMISION_PRO(){
		return cfgProp(CTE_CONF_FIO,"com.treelogic.fawna.presentacion.core.COMISION_PRO");
	}
	
	public static String getCOMISION_CORPORATE(){
		return cfgProp(CTE_CONF_FIO,"com.treelogic.fawna.presentacion.core.COMISION_CORPORATE");
	}
	
	/**
	 * Obtiene la cadena de la ubicación de los archivos de masspay
	 * @return
	 */
	public static String getMASSPAY_FILES_PATH() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.MASSPAY_FILES_PATH");
	}
	
	/**
	 * Obtiene la cadena con la nota a añadir en cada elemento del pago masivo
	 * para desarrollo
	 * @return
	 */
	public static String getMASSPAY_DEVELOPMENT_ANNOTATION() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.MASSPAY_DEVELOPMENT_ANNOTATION");
	}
	
	/**
	 * Obtiene la cadena con la nota a añadir en cada elemento del pago masivo
	 * para producción
	 * @return
	 */
	public static String getMASSPAY_PRODUCTION_ANNOTATION() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.MASSPAY_PRODUCTION_ANNOTATION");
	}
	
	/**
	 * Obtiene la cadena con la descripción del cobro de los sparks en desarrollo	 
	 * @return
	 */
	public static String getDEVEL_SPARKS_RENEWAL_ANNOTATION() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.DEVEL_SPARKS_RENEWAL_ANNOTATION");
	}
	
	/**
	 * Obtiene la cadena con la descripción del cobro de los sparks en producción	 
	 * @return
	 */
	public static String getPROD_SPARKS_RENEWAL_ANNOTATION() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.PROD_SPARKS_RENEWAL_ANNOTATION");
	}
	
	/**
	 * Obtiene la cadena con la descripción del cobro de la suscripción de la cuenta	 
	 * @return
	 */
	public static String getACCOUNT_RENEWAL_ANNOTATION() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.ACCOUNT_RENEWAL_ANNOTATION");
	}
	
	/**
	 * Obtiene la cadena con la descripción del cobro de la subida a producción de un avatar
	 * @return
	 */
	public static String getUPLOAD_PRODUCTION_ANNOTATION() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.UPLOAD_PRODUCTION_ANNOTATION");
	}
	
	/**
	 * Obtiene el día en que se ejecuta el batch de planificación de desarrollo
	 * @return
	 */
	public static String getFECHA_PLANI_DESA() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.FECHA_PLANI_DESA");
	}
	
	/**
	 * Obtiene el día en que se ejecuta el batch de planificación de producción
	 * @return
	 */
	public static String getFECHA_PLANI_PROD() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.FECHA_PLANI_PROD");
	}
	
	/**
	 * Obtiene el día en que se ejecuta el batch de notificación de desarrollo
	 * @return
	 */
	public static String getFECHA_NOTI_DESA() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.FECHA_NOTI_DESA");
	}
	
	/**
	 * Obtiene el día en que se ejecuta el batch de notificación de producción
	 * @return
	 */
	public static String getFECHA_NOTI_PROD() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.FECHA_NOTI_PROD");
	}
	
	/**
	 * Obtiene la direccion email de autentificacion para enviar correos
	 * @return
	 */
	public static String getTCRF_MAIL_SENDER_ADDR() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.TCRF_MAIL_SENDER_ADDR");
	}
	
	/**
	 * Obtiene la password de autentificacion para enviar correos
	 * @return
	 */
	public static String getTCRF_MAIL_SENDER_PW() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.TCRF_MAIL_SENDER_PW");
	}

	/**
	 * Los usuarios que se registren recibirán un correo de esta dirección email
	 * @return
	 */
	public static String getTCRF_REGISTER_MAIL_SENDER() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.TCRF_REGISTER_MAIL_SENDER");
	}
	
}