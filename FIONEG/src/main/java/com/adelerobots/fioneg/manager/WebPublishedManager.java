package com.adelerobots.fioneg.manager;

import com.adelerobots.fioneg.engine.WebPublishedEng;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.entity.WebPublishedC;

public class WebPublishedManager {

	

	private String conexion;

	public WebPublishedManager(String conexion) {
		super();
		this.conexion = conexion;
	}
	
	
	/**
	 * Comprobar si un usuario tiene o no una entrada en la tabla
	 * 'webpublished'
	 * 
	 * @param intCodUsuario Idetificador único del usuario cuya información
	 * buscamos
	 * 
	 * @return Se devolverá una entidad 'webpublished' si el usuario
	 * tiene publicado el scriplet, y null en caso contrario
	 * 
	 */
	public WebPublishedC getWebPublished(Integer intCodUsuario){	
		WebPublishedEng webPublishedDao = new WebPublishedEng(conexion);
		
		return webPublishedDao.getWebPublished(intCodUsuario);
	}
	
	
	/**
	 * Método que permite añadir una entrada en la tabla 'webpublished'
	 * 
	 * @param webpublished Entidad que representa el contenido de una
	 * entrada de la tabla
	 * 
	 * @return Se devuelven los datos de la entidad que se acaba de
	 * insertar
	 */
	public WebPublishedC addWebPublished(WebPublishedC webPublished) {
		WebPublishedEng webPublishedDao = new WebPublishedEng(conexion);
		
		return webPublishedDao.addWebPublished(webPublished);
	}
	
	/**
	 * Método que elimina una entrada de la tabla 'webpublished'
	 * 
	 * @param userid Identificador único del usuario cuyos datos
	 * buscamos
	 */
	public void removeWebPublished(Integer intCodUsuario){
		WebPublishedEng webPublishedDao = new WebPublishedEng(conexion);
		
		webPublishedDao.removeWebPublished(intCodUsuario);
	}
	
	
	/**
	 * Método que indica si un usuario tiene marcada o no la opción
	 * de publicar su scriplet
	 * 
	 * @param intCodUsuario Identificador único del usuario cuya
	 * información se busca
	 * 
	 * @return Se devolverá 'true' si el usuario tiene marcada la
	 * opción de publicar su scriplet, 'false' si tiene la opción
	 * desmarcada, o 'null' si el usuario no tiene entradas en la
	 * tabla 'webpublished'
	 */
	public Boolean checkWebPublished(Integer intCodUsuario){
		Boolean isPublished = null;
		WebPublishedC webPublished = getWebPublished(intCodUsuario);
		
		if(webPublished != null){
			if(webPublished.getChPublished() == '1')
				isPublished = Boolean.TRUE;
			else
				isPublished = Boolean.FALSE;
		}
		
		return isPublished;
	}
	
	/**
	 * Método que permitirá activar o desactivar la publicación del
	 * scriplet de un usuario
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @param isPublished Indicador de publicación del scriplet
	 * del usuario
	 * @return Se devolverá el objeto 'webpublished' actualizado, o null
	 * si no existe entrada en la tabla para el usuario pasado como
	 * parámetro
	 */
	public WebPublishedC changeWebPublished(UsuarioC usuario, Character isPublished){
		WebPublishedEng webPublishedDao = new WebPublishedEng(conexion);
		
		WebPublishedC webPublished = getWebPublished(usuario.getCnUsuario());
		// Si existe la entrada se actualiza
		if(webPublished != null){
			webPublished.setChPublished(isPublished);
			
			webPublishedDao.update(webPublished);
			webPublishedDao.flush();
			
		}else{
			// Si no existe entrada en la tabla para este usuario
			// se inserta
			webPublished = new WebPublishedC();
			
			webPublished.setUsuario(usuario);
			webPublished.setChPublished(isPublished);
			
			webPublishedDao.addWebPublished(webPublished);
		}
		
		return webPublished;
	}
	
	
}
