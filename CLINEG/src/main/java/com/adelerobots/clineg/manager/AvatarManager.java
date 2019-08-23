package com.adelerobots.clineg.manager;

import java.util.Date;
import java.util.List;

import com.adelerobots.clineg.engine.AvatarEng;
import com.adelerobots.clineg.engine.HostingEng;
import com.adelerobots.clineg.entity.AvatarC;
import com.adelerobots.clineg.entity.HostingC;
import com.adelerobots.clineg.entity.UsuarioC;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class AvatarManager {
	
	private String conexion;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(AvatarManager.class);
	

	public AvatarManager(String conexion) {
		super();
		this.conexion = conexion;		
	}
	
	/**
	 * Método que inserta una nueva fila en la tabla avatar
	 * @param sparksConf sparks de la configuración que compone el avatar
	 * @param intCodUsuario Identificador único de usuario
	 * @param intIdUnidadTiempo Identificador de la unidad de tiempo
	 * @param hosting Hosting asociado a este avatar
	 */
	public void createNewAvatar(UsuarioC usuario, Integer intIdUnidadTiempo, HostingC hosting){
		AvatarEng avatarDAO = new AvatarEng(conexion);
		AvatarC avatar = new AvatarC();
		
		avatar.setUsuario(usuario);		
		avatar.setHosting(hosting);
		avatar.setChActivo('1');
		avatar.setDatUpload(new Date());
		
		
		avatarDAO.insert(avatar);
		avatarDAO.flush();
		
	}
	
	/**
	 * Método que devuelve la lista de avatares activos para un usuario
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @return
	 */
	public List<AvatarC> getActiveAvatarsByUser(Integer intCodUsuario){
		AvatarEng avatarDAO = new AvatarEng(conexion);
		
		return avatarDAO.getActiveAvatarsByUser(intCodUsuario);
	}
	
	/**
	 * Método que devolverá un objeto de tipo "HostingC" que se ajuste a los parámetros
	 * de búsqueda
	 * @param intCodUnit Identificador de la unidad de tiempo
	 * @param intNumUsers Número de usuarios concurrentes
	 * @param strResolution Cadena que representa la resolución
	 * @param chHighAvailability Se quiere o no alta disponibilidad
	 * @return Se devolverá el objeto buscado
	 */
	public HostingC getPrecioHosting(Integer intCodUnit, Integer intNumUsers, String strResolution, Character chHighAvailability){
		HostingEng hostingDAO = new HostingEng(conexion);
		/* Cambio introducido tras incluir números de usuarios múltiplos de 20 */
		if(intNumUsers > 20)
			intNumUsers = 20;
		HostingC hosting = hostingDAO.getHosting(intCodUnit, intNumUsers, strResolution, chHighAvailability);				
		
		return hosting;
		
	}

}
