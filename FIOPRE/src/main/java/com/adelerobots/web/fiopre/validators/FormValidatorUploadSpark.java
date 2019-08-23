package com.adelerobots.web.fiopre.validators;

import java.util.ArrayList;
import java.util.List;

import com.adelerobots.fioneg.util.FunctionUtils;
import com.treelogic.fawna.arq.negocio.core.ValidacionException;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;

public class FormValidatorUploadSpark extends AbstractUserFormValidator {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String FIELD_VERSION = "sparkVersion";	
	private static final String FIELD_EMAIL = "supportEmail";	
	private static final String FIELD_COMPRESSED = "compressed_filename";	
	
	// Extensiones permitidas (upload ficheros)
	private static final List<String> COMPRESSEDFILE_EXTENSIONS = new ArrayList <String>();
	
	public Boolean validate(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		Boolean valido = Boolean.TRUE;				
		
		// Comprobar formato versión numero.numero
		String version = FunctionUtils.toString(gestorDatos.getValue(FIELD_VERSION));
		try{
			validateVersionFormat(version);
		}catch (ValidacionException ve){			
			gestorDatos.addErrorValidacionSimple(ve.getMessage(), ve.getMessage(), FIELD_VERSION);
			valido = Boolean.FALSE;
		}		
		
		// Comprobar formato de email
		String email = FunctionUtils.toString(gestorDatos.getValue(FIELD_EMAIL), null);
		try {
			validateEmailFormat(email); 
		} catch (ValidacionException ve) {
			gestorDatos.addErrorValidacionSimple(ve.getMessage(), ve.getMessage(), FIELD_EMAIL);
			valido = Boolean.FALSE;
		}		

		// Comprobación extensión de fichero comprimido
		// Inicialización de extensiones permitidas
		initCompressedFileExtensions();		
		String fileCompressedName = FunctionUtils.toString(gestorDatos.getValue(FIELD_COMPRESSED));
		if(fileCompressedName != null && !"".equals(fileCompressedName)){
			try{
				validateCompressedFileExtension(fileCompressedName);
			}catch(ValidacionException ve){
				gestorDatos.addErrorValidacionSimple(ve.getMessage(), ve.getMessage(), FIELD_COMPRESSED);
				valido = Boolean.FALSE;
			}
		}else{
			// Comprobar que el campo del fichero no esté vacío
			gestorDatos.addErrorValidacionSimple(getMessage("FormValidatorSparkEdition.validateUploadFileFilled","You must select an icon file"),
					getMessage("FormValidatorSparkEdition.validateUploadFileFilled","You must select an icon file"),FIELD_COMPRESSED);
			valido = Boolean.FALSE;
		}				
		
		return valido;
	}	
	
	/**
	 * Método que permite validar la extensión del fichero a subir
	 * 
	 * @param fileName Nombre del fichero comprimido
	 * @throws ValidacionException
	 */
	private void validateCompressedFileExtension(String fileName) throws ValidacionException{
		int endPos = fileName.lastIndexOf(".");
		String extension = fileName.substring(endPos);
		String fileNameNoExt = fileName.substring(0,endPos);
        if (fileNameNoExt.endsWith(".tar"))        	        	
        	extension = ".tar" + extension;   
		
		if(!COMPRESSEDFILE_EXTENSIONS.contains(extension)){
			throw new ValidacionException(getMessage("FormValidatorUploadSpark.validateCompressedFileExtension","File extension is incorrect."));
		}
	}
	
	/**
	 * Método que inicializa las extensiones permitidas para el fichero de subida
	 */
	private void initCompressedFileExtensions(){
		COMPRESSEDFILE_EXTENSIONS.add(".zip");
		COMPRESSEDFILE_EXTENSIONS.add(".tar");
		COMPRESSEDFILE_EXTENSIONS.add(".tar.gz");
	}

}
