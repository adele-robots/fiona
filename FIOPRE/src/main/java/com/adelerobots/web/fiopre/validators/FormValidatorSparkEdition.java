package com.adelerobots.web.fiopre.validators;

import java.util.ArrayList;
import java.util.List;

import com.adelerobots.fioneg.util.FunctionUtils;
import com.treelogic.fawna.arq.negocio.core.ValidacionException;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;

public class FormValidatorSparkEdition extends AbstractUserFormValidator {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String FIELD_VERSION = "version";
	private static final String FIELD_ES_TRIAL = "checkTrial";
	private static final String FIELD_DIAS_TRIAL = "diasTrial";
	private static final String FIELD_EMAIL = "email";
	private static final String FIELD_URL = "marketingURL";
	private static final String FIELD_ICON = "icon_filename";
	private static final String FIELD_BANNER = "banner_filename";
	private static final String FIELD_VIDEO = "video_filename";
	
	// Botones que pueden hacer saltar el validador (upload ficheros)
	private static final String BUTTON_ICON = "button_upload_icon";
	private static final String BUTTON_BANNER = "button_upload_banner";
	private static final String BUTTON_VIDEO = "button_upload_video";
	
	// Extensiones permitidas (upload ficheros)
	private static final List<String> ICON_EXTENSIONS = new ArrayList <String>();
	private static final List<String> BANNER_EXTENSIONS = new ArrayList <String>();
	private static final List<String> VIDEO_EXTENSIONS = new ArrayList <String>();
	
	// Campos de la parte del pricing
	private static final String FIELD_TIPO_TARIFA = "idTipoTarifa";
	private static final String FIELD_NUM_USUARIOS = "numUsuariosConcu";
	private static final String FIELD_ID_UNIDAD_TIEMPO = "idUnidadTiempo";
	private static final String FIELD_ID_UNIDAD_USO = "idUnidadUso";
	private static final String FIELD_UNIDAD_VALOR = "cantidadUnidad";
	private static final String FIELD_PRECIO = "dolares";
	

	public Boolean validate(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		Boolean valido = Boolean.TRUE;	
		Boolean pulsadoAddPrice = ((String)gestorEstados.getIdComponente()).equals("addRow");
		
		if(!pulsadoAddPrice){
			// Comprobar formato versión numero.numero
			String version = FunctionUtils.toString(gestorDatos.getValue(FIELD_VERSION));
			try{
				validateVersionFormat(version);
			}catch (ValidacionException ve){			
				gestorDatos.addErrorValidacionSimple(ve.getMessage(), ve.getMessage(), FIELD_VERSION);
				valido = Boolean.FALSE;
			}
			
			// Comprobar que si se ha seleccionado "trial" el número de días sea mayor que 0
			Boolean esTrial = (Boolean)gestorDatos.getValue(FIELD_ES_TRIAL);
			if(esTrial){			
				String diasTrial = (String)gestorDatos.getValue(FIELD_DIAS_TRIAL);
				try{				
					validateDiasTrialAmount(diasTrial);
				}catch (ValidacionException ve){				
					gestorDatos.addErrorValidacionSimple(ve.getMessage(), ve.getMessage(), FIELD_DIAS_TRIAL);
					valido = Boolean.FALSE;
				}
			}
			
			// Comprobar formato de email
			String email = FunctionUtils.toString(gestorDatos.getValue(FIELD_EMAIL), null);
			try {
				validateEmailFormat(email); 
			} catch (ValidacionException ve) {
				gestorDatos.addErrorValidacionSimple(ve.getMessage(), ve.getMessage(), FIELD_EMAIL);
				valido = Boolean.FALSE;
			}
			
			// Comprobar formato de la URL
			String url = FunctionUtils.toString(gestorDatos.getValue(FIELD_URL));
			if(url != null && !"".equals(url)){
				try{
					validateURLFormat(url);
				}catch(ValidacionException ve){
					gestorDatos.addErrorValidacionSimple(ve.getMessage(), ve.getMessage(), FIELD_URL);
					valido = Boolean.FALSE;
				}
			}
			
			// Comprobaciones sobre subida de icono
			String botonPulsado = gestorEstados.getIdComponente(); 
			if(botonPulsado.equals(BUTTON_ICON)){		
				// Inicialización de extensiones permitidas
				initIconExtensions();
				
				// Comprobación extensión de icono
				String fileIconName = FunctionUtils.toString(gestorDatos.getValue(FIELD_ICON));
				if(fileIconName != null && !"".equals(fileIconName)){
					try{
						validateIconExtension(fileIconName);
					}catch(ValidacionException ve){
						gestorDatos.addErrorValidacionSimple(ve.getMessage(), ve.getMessage(), FIELD_ICON);
						valido = Boolean.FALSE;
					}
				}else{
					// Comprobar que el campo del fichero no esté vacío
					gestorDatos.addErrorValidacionSimple(getMessage("FormValidatorSparkEdition.validateUploadFileFilled","You must select an icon file"),
							getMessage("FormValidatorSparkEdition.validateUploadFileFilled","You must select an icon file"),FIELD_ICON);
					valido = Boolean.FALSE;
				}
			}
			
			// Comprobaciones sobre subida de banner
			if(botonPulsado.equals(BUTTON_BANNER)){
				// Inicialización de extensiones permitidas
				initBannerExtensions();
				// Comprobación extensión de banner
				String fileIconName = FunctionUtils.toString(gestorDatos.getValue(FIELD_BANNER));
				if(fileIconName != null && !"".equals(fileIconName)){
					try{
						validateBannerExtension(fileIconName);
					}catch(ValidacionException ve){
						gestorDatos.addErrorValidacionSimple(ve.getMessage(), ve.getMessage(), FIELD_BANNER);
						valido = Boolean.FALSE;
					}
				}else{
					// Comprobar que el campo del fichero no esté vacío
					gestorDatos.addErrorValidacionSimple(getMessage("FormValidatorSparkEdition.validateUploadFileFilled","You must select a banner file"),
							getMessage("FormValidatorSparkEdition.validateUploadFileFilled","You must select a banner file"),FIELD_ICON);
					valido = Boolean.FALSE;
					
				}
			}
			
			// Comprobaciones sobre subida de video
			if(botonPulsado.equals(BUTTON_VIDEO)){
				// Inicialización de extensiones permitidas
				initVideoExtensions();
				// Comprobación extensión de video
				String fileIconName = FunctionUtils.toString(gestorDatos.getValue(FIELD_VIDEO));
				if(fileIconName != null && !"".equals(fileIconName)){
					try{
						validateVideoExtension(fileIconName);
					}catch(ValidacionException ve){
						gestorDatos.addErrorValidacionSimple(ve.getMessage(), ve.getMessage(), FIELD_VIDEO);
						valido = Boolean.FALSE;
					}
				}else{
					// Comprobar que el campo del fichero no esté vacío
					gestorDatos.addErrorValidacionSimple(getMessage("FormValidatorSparkEdition.validateUploadFileFilled","You must select a video file"),
							getMessage("FormValidatorSparkEdition.validateUploadFileFilled","You must select a video file"),FIELD_ICON);
					valido = Boolean.FALSE;
					
				}
			}
		}
		
		// Comprobaciones para price
		if(pulsadoAddPrice){
			String valorTipoTarifa = (String)gestorDatos.getValue(FIELD_TIPO_TARIFA);
			String numUsuarios = (String)gestorDatos.getValue(FIELD_NUM_USUARIOS);
			String unidadTiempo = (String)gestorDatos.getValue(FIELD_ID_UNIDAD_TIEMPO);
			String unidadUso = (String)gestorDatos.getValue(FIELD_ID_UNIDAD_USO);
			String unidadValor = (String)gestorDatos.getValue(FIELD_UNIDAD_VALOR);
			String precio = (String)gestorDatos.getValue(FIELD_PRECIO);
			if(valorTipoTarifa.equals("1")){
				// Tiempo
				if(isBlankOrNull(numUsuarios) || isBlankOrNull(unidadTiempo) || isBlankOrNull(unidadValor) || isBlankOrNull(precio) )
					valido = Boolean.FALSE;
			}else if(valorTipoTarifa.equals("2")){
				// Uso
				if(isBlankOrNull(numUsuarios) || isBlankOrNull(unidadUso) || isBlankOrNull(unidadValor) || isBlankOrNull(precio))
					valido = Boolean.FALSE;
			}
		}
		
		return valido;
	}
	
	/**
	 * Método que permite validar la extensión del icono a subir
	 * 
	 * @param fileName Nombre del fichero del icono
	 * @throws ValidacionException
	 */
	private void validateIconExtension(String fileName) throws ValidacionException{
		String extension = fileName.substring(fileName.lastIndexOf("."));
		if(!ICON_EXTENSIONS.contains(extension)){
			throw new ValidacionException(getMessage("FormValidatorSparkEdition.validateIconExtension","Icon extension is incorrect."));
		}
	}
	
	/**
	 * Método que permite validar la extensión del banner a subir
	 * 
	 * @param fileName Nombre del fichero del banner
	 * @throws ValidacionException
	 */
	private void validateBannerExtension(String fileName) throws ValidacionException{
		String extension = fileName.substring(fileName.lastIndexOf("."));
		if(!BANNER_EXTENSIONS.contains(extension)){
			throw new ValidacionException(getMessage("FormValidatorSparkEdition.validateBannerExtension","Banner extension is incorrect."));
		}
	}
	
	/**
	 * Método que permite validar la extensión del video a subir
	 * 
	 * @param fileName Nombre del fichero del video
	 * @throws ValidacionException
	 */
	private void validateVideoExtension(String fileName) throws ValidacionException{
		String extension = fileName.substring(fileName.lastIndexOf("."));
		if(!VIDEO_EXTENSIONS.contains(extension)){
			throw new ValidacionException(getMessage("FormValidatorSparkEdition.validateVideoExtension","Video extension is incorrect."));
		}
	}
	
	/**
	 * Método que inicializa las extensiones permitidas para los iconos
	 */
	private void initIconExtensions(){
		ICON_EXTENSIONS.add(".png");
		ICON_EXTENSIONS.add(".jpg");
		ICON_EXTENSIONS.add(".bmp");
	}
	
	/**
	 * Método que inicializa las extensiones permitidas para los banners
	 */
	private void initBannerExtensions(){
		BANNER_EXTENSIONS.add(".gif");
		BANNER_EXTENSIONS.add(".jpg");
		BANNER_EXTENSIONS.add(".bmp");
	}

	/**
	 * Método que inicializa las extensiones permitidas para los videos
	 */
	private void initVideoExtensions(){
		VIDEO_EXTENSIONS.add(".mp4");
	}
	
	/**
	 * Función auxiliar que comprueba si una cadena es null o está vacía
	 * @param cadena
	 * @return
	 */
	private boolean isBlankOrNull(String cadena){
		boolean esBlancoOVacio = false;
		if("".equals(cadena) || cadena == null)
			esBlancoOVacio = true;
			
		return esBlancoOVacio;	
	}
	
	

}
