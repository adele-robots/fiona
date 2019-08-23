package com.adelerobots.serneg.manager;

import java.util.List;

import com.adelerobots.serneg.engine.ProcesoMaquinaEng;
import com.adelerobots.serneg.entity.MaquinaC;
import com.adelerobots.serneg.entity.ProcesoMaquinaC;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class ProcesoMaquinaManager {

	private String conexion;

	private static FawnaLogHelper LOGGER = FawnaLogHelper
			.getLog(ProcesoMaquinaManager.class);

	public ProcesoMaquinaManager(String conexion) {
		super();
		this.conexion = conexion;
	}
	
	/**
	 * Método que inserta un nuevo usuario en la BBDD del ServiceManager
	 * @param maquina Máquina en la que se creará el usuario
	 * @param email Email del usuario
	 * @param avatar Identificador alfanumérico del avatar
	 * @return Se devolverá el usuario recién creado
	 */
	public ProcesoMaquinaC insertProcesoMaquina(MaquinaC maquina, String email, String avatar, Integer numUsuarios){
		ProcesoMaquinaEng procesoMaquinaDAO = new ProcesoMaquinaEng(conexion);
		ProcesoMaquinaC procesoMaquina = new ProcesoMaquinaC();
		procesoMaquina.setMaquina(maquina);
		procesoMaquina.setStrEmail(email);
		procesoMaquina.setStrAvatar(avatar);
		procesoMaquina.setIntNumProcesos(numUsuarios);
		
		procesoMaquinaDAO.insert(procesoMaquina);
		procesoMaquinaDAO.flush();
		
		return procesoMaquina;
	}
	
	/**
	 * Método que permite eliminar de BBDD los registros de los avatares de un usuario
	 * en las máquinas 
	 * @param email Dirección de correo electrónico del usuario
	 */
	public void deleteProcesosMaquinaFromUser(String email){
		ProcesoMaquinaEng procesoMaquinaDAO = new ProcesoMaquinaEng(conexion);
		List <ProcesoMaquinaC> lstProcesoMaquina = procesoMaquinaDAO.getMaquinasUsuario(email);
		
		for(int i = 0; i<lstProcesoMaquina.size(); i++){
			ProcesoMaquinaC procesoMaquina = lstProcesoMaquina.get(i);
			procesoMaquinaDAO.delete(procesoMaquina);
			procesoMaquinaDAO.flush();
		}
		
	}

}
