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
import com.treelogic.fawna.presentacion.componentes.component.html.command.interfaces.IUploadFile;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.render.input.UploadFileInfo;
import com.treelogic.fawna.presentacion.core.utilidades.FawnaPropertyConfiguration;


public class FIONA_UploadSpark implements IUploadFile{

	private static final long serialVersionUID = 5479156947970214268L;
	
	//private static final String FILEFAW = "C:\\JAVA\\proyectos\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\FIOPRE\\users\\"; 

	String FILEFAW = FawnaPropertyConfiguration.getInstance().getProperty("conf/FIOPRE", "com.treelogic.fawna.presentacion.core.users_path");
	protected static Logger logger = Logger.getLogger(FIONA_UploadSpark.class);
	

	
	public void uploadFile(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos, Map<String, IUploadFile> ficheros) {
		// TODO Auto-generated method stub
		
		String auxString = ficheros.keySet().toString();
		auxString = auxString.replace("[", "");
		auxString = auxString.replace("]", "");
		String partes[] = auxString.split(",");
		String usermail_md5 = partes[0].split("_")[1];
		String usermail = partes[0].split("_")[2];
		
		
		
		UploadFileInfo filec = (UploadFileInfo) ficheros.get("sparkC_"+usermail_md5+"_"+usermail);
		UploadedFile fileInC =  filec.getFile();
		
			
		
		//llamar a sn que guarde el archivo con datos de usuario (sufijo, carpeta...) y que envíe un mail
		//a una cuenta que notifique al equipo de fiona de la nueva subida.
		
		String fileName = filec.getFileName();
		
        
        //Comprobaciones
        if (!fileName.endsWith(".rar") && 
        	!fileName.endsWith(".zip") && 
        	!fileName.endsWith(".tar") && 
        	!fileName.endsWith(".tar.gz")){
        	return ;
        }
        
        final File uploadFolder = ConfigUtils.getNfsUserUploadedSparksFolder(usermail_md5);
        final File filefc = new File(uploadFolder, fileName);
        if (!filefc.getAbsolutePath().startsWith(uploadFolder.getAbsolutePath())) {
        	//Evitamos que en fileName haya un "../" y se nos salga del uploadFolder
        	throw new SecurityException(
        			"You are trying upload your builded avatar bundle outside parent upload folder");
        }
        
        OutputStream outputc = null;
        try {
        	outputc = new FileOutputStream(filefc);
			IOUtils.copy(fileInC.getInputStream(), outputc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
            IOUtils.closeQuietly(outputc);
        
		}
  
		//invocar SN
		
	}
	

}
