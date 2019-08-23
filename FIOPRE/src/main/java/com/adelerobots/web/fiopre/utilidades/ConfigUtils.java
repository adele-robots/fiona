package com.adelerobots.web.fiopre.utilidades;

import java.io.File;

import org.apache.log4j.Logger;

import com.adelerobots.fioneg.util.FunctionUtils;
import com.treelogic.fawna.presentacion.core.utilidades.FawnaPropertyConfiguration;

/**
 * @author adele
 *
 */
public class ConfigUtils 
{

	private static final Logger logger = Logger.getLogger(ConfigUtils.class);
	public static final String CTE_CONF_FIO = "conf/FIOPRE";

	public ConfigUtils() {
		// Util classes are final and all methods are static
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
	 * Obtiene el URI donde el avatar designer esta instalado
	 * @return
	 */
	public static String getDesignerIportUriHttps() {
		String value = cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.designer_iport_https");
		return FunctionUtils.stripQuotes(value);
	}

	/**
	 * Obtiene el directorio padre donde se guardan los ficheros de configuracion 
	 * del avatar para todos los usuarios
	 * @return
	 */
	protected static String getUSERS_PATH() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.USERS_PATH");
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
	 * Obtiene el host en el que se encuentran los recursos que utiliza el Scriplet
	 * 
	 * @return
	 */
	public static String getScripletResourcesHost(){
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.scriplet.host");
	}
	
	/**
	 * Obtiene el time_alive para los procesos
	 * @return
	 */
	public static String getTimeAlive() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.time_alive");
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
	 * Obtiene el URI donde el avatar designer esta instalado
	 * @return
	 */
	public static String getFreeSparksNumber() {
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.free_sparks_number");		
	}
	
	/**
	 * Obtiene la resolución y bitrates para ejecutar el avatar dependiendo del tipo de usuario
	 * @return
	 */
	public static String getAvatarResolution(final Integer accountId) {
		switch(accountId){
		case 1:
			return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.low_resolution");
		case 2:
			return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.standard_resolution");
		case 3:
			return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.high_resolution");
		case 4:
			return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.high_resolution");
		default:
			return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.low_resolution");
		}				
	}
	
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
	
	public static String getACCOUNT_PAYMENT_ANNOTATION(){
		return cfgProp(CTE_CONF_FIO, "com.treelogic.fawna.presentacion.core.ACCOUNT_PAYMENT_ANNOTATION");
	}

}
