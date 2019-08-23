package com.adelerobots.fioneg.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.engine.AvatarEng;
import com.adelerobots.fioneg.engine.AvatarSparkEng;
import com.adelerobots.fioneg.entity.AvatarC;
import com.adelerobots.fioneg.entity.HostingC;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.entity.avatarspark.AvatarSparkC;
import com.adelerobots.fioneg.entity.avatarspark.AvatarSparkPk;
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
	public AvatarC createNewAvatar(List <SparkC> sparksConf, UsuarioC usuario, Integer intIdUnidadTiempo, HostingC hosting){
		AvatarEng avatarDAO = new AvatarEng(conexion);
		AvatarC avatar = new AvatarC();
		
		avatar.setUsuario(usuario);		
		avatar.setHosting(hosting);
		avatar.setChActivo('1');
		avatar.setDatUpload(new Date());
		
		//avatar.setLstSparks(sparksConf);
		// Para cada spark en la configuración creo un objeto AvatarSparkC
		List <AvatarSparkC> lstAvatarSpark = new ArrayList<AvatarSparkC>();
		for(int i = 0; i< sparksConf.size(); i++){
			SparkC spark = sparksConf.get(i);
			
			AvatarSparkPk avatarSparkPk = new AvatarSparkPk();
			avatarSparkPk.setAvatar(avatar);
			avatarSparkPk.setSpark(spark);
			
			AvatarSparkC avatarSpark = new AvatarSparkC();
			avatarSpark.setAvatarSparkPk(avatarSparkPk);
			avatarSpark.setFloPrice(spark.getFloPrecioSeleccionadoProd());
			
			lstAvatarSpark.add(avatarSpark);
		}		
		
		avatar.setLstAvatarSparkC(lstAvatarSpark);
		avatarDAO.insert(avatar);
		avatarDAO.flush();
		return avatar;
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
	 * Método que devuelve los sparks desarrollados por un determinado usuario
	 * que están siendo usados por otros usuarios en desarrollo
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @return
	 */
	public List<AvatarSparkC> getActiveUsedSparksDevelopedByUser(Integer intCodUsuario){
		AvatarSparkEng avatarSparkDAO = new AvatarSparkEng(conexion);
		
		return avatarSparkDAO.getActiveUsedSparksDevelopedByUser(intCodUsuario);
	}

}
