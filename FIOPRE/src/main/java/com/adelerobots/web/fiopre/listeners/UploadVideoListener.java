package com.adelerobots.web.fiopre.listeners;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.custom.fileupload.UploadedFile;

import com.adelerobots.web.fiopre.utilidades.ConfigUtils;
import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.presentacion.componentes.component.html.command.interfaces.IUploadFile;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.render.input.UploadFileInfo;

/**
 * Listener para la subida de videos para los sparks del usuario
 * 
 * @author adele
 *
 */
public class UploadVideoListener implements IUploadFile{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5001544172793794598L;


	// MD5 de usuario
	private static final String USERMAIL_MD5 = ContextUtils.getUserMailD5();
	
	
	private static final String FIELD_VIDEO = "video";
	private static final String FIELD_VIDEO_VALUE = "video_value";
		
		
	private Logger logger = Logger.getLogger(UploadIconListener.class);
	

	public void uploadFile(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos, Map<String, IUploadFile> ficheros) {
				
		UploadFileInfo fileInfo = (UploadFileInfo) ficheros.get(FIELD_VIDEO);
		UploadedFile uploadedFileVideo =  fileInfo.getFile();
		
		String fileName = uploadedFileVideo.getName();
		
		final File uploadFolder = ConfigUtils.getNfsUserUploadedBannersFolder(USERMAIL_MD5);
        final File fileIcon = new File(uploadFolder, fileName);
        if (!fileIcon.getAbsolutePath().startsWith(uploadFolder.getAbsolutePath())) {
        	//Evitamos que en fileName haya un "../" y se nos salga del uploadFolder
        	logger.error("[UploadVideoListener]: Se ha producido un error al intentar subir un video. Se intenta salir del path de subida");
        	throw new SecurityException(
        			"You are trying upload your icon outside parent upload folder");
        }
        
        OutputStream outputc = null;
        try {
        	outputc = new FileOutputStream(fileIcon);
			IOUtils.copy(uploadedFileVideo.getInputStream(), outputc);
		} catch (IOException e) {
			logger.error("[UploadVideoListener]: Se ha producido un error al intentar subir un video. No se ha podido guardar el fichero en el directorio" +
					" deseado");
			e.printStackTrace();
		}  finally {
            IOUtils.closeQuietly(outputc);
            // Comprobar existencia de fichero anterior.
            // Eliminarlo si el nombre era distinto al nuevo
            String filePreviousName = (String)gestorDatos.getValue(FIELD_VIDEO_VALUE);
            
            if(filePreviousName != null && !"".equals(filePreviousName)){
	            if(filePreviousName.compareToIgnoreCase(fileName) != 0){
	            	
	            	// Comprobar si el fichero existe, y si existe, borrarlo
	            	File filePrevious = new File(uploadFolder, filePreviousName);
	            		            	
	            	if(filePrevious.exists())
	            		filePrevious.delete();
	            	
	            }
            }
            
            // Establecer el valor para el campo que almacena el nombre del fichero
            gestorDatos.setValue(FIELD_VIDEO_VALUE,fileName);
            gestorEstados.setPropiedad(FIELD_VIDEO_VALUE, "rendered", true);
        
		}	
		
		
	}

}