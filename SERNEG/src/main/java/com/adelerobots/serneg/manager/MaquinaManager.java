package com.adelerobots.serneg.manager;

import java.util.List;

import com.adelerobots.serneg.engine.MaquinaEng;
import com.adelerobots.serneg.engine.ProcesoMaquinaEng;
import com.adelerobots.serneg.entity.MaquinaC;
import com.adelerobots.serneg.entity.ProcesoMaquinaC;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class MaquinaManager {
	
	private String conexion;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(MaquinaManager.class);
	
	public MaquinaManager(String conexion){
		super();
		this.conexion = conexion;
	}
	
	/**
	 * Método que en función de los parámetros decide a qué máquina asignar
	 * el nuevo avatar
	 * 
	 * @param intNumUsuariosConcurrentes Número de usuarios concurrentes que
	 * solicita el usuario
	 * 
	 * @return
	 */
	public MaquinaC getMaquinaDisponible(Integer intNumUsuariosConcurrentes){
		MaquinaEng maquinaDAO = new MaquinaEng(conexion);
		ProcesoMaquinaEng procesoMaquinaDAO = new ProcesoMaquinaEng(conexion);
		MaquinaC maquina = null;
		// Recuperamos todas las máquinas
		List<MaquinaC> lstMaquinas = maquinaDAO.findAll();
		boolean encontrada = false;
		
		for(int i = 0; i<lstMaquinas.size() && !encontrada;i++){
			MaquinaC maquinaCheck = lstMaquinas.get(i);
			// Comprobamos si hay espacio en la máquina para todos los
			// usuarios concurrentes
			// TODO: Esto cambiará cuando implementemos un mecanismo de balanceo
			// que permita tener un mismo avatar en dos máquinas
			// Número de usuarios ya registrados en la máquina
			//Integer intNumUsuariosRegistrados = maquinaCheck.getLstProcesoMaquina().size();
			Integer intNumUsuariosRegistrados = procesoMaquinaDAO.getNumUsuarios(maquinaCheck.getIntCodMaquina());
			if(intNumUsuariosRegistrados == null)
				intNumUsuariosRegistrados = new Integer(0);
			if((maquinaCheck.getIntMaxProcesos() > intNumUsuariosRegistrados) && 
					((maquinaCheck.getIntMaxProcesos() - intNumUsuariosRegistrados)>=intNumUsuariosConcurrentes)){
				encontrada = true;
				maquina = maquinaCheck;
			}
		}
		return maquina;
	}
	
	/**
	 * Método que devuelve la máquina en la que se encuentra el avatar de un usuario
	 * @param email Cadena que representa el email del usuario
	 * @param strCodAvatar Cadena que identifica al avatar en la máquina que corresponda
	 * @return
	 */
	public MaquinaC getMaquinaAvatarUsuario(String email, String strCodAvatar){
		ProcesoMaquinaEng procesoMaquinaDAO = new ProcesoMaquinaEng(conexion);
		MaquinaC maquina = null;
		
		ProcesoMaquinaC procesoMaquina = procesoMaquinaDAO.getMaquinaAvatarUsuario(email, strCodAvatar);
		
		if(procesoMaquina != null)
			maquina = procesoMaquina.getMaquina();
		
		return maquina;
	}
	
	/**
	 * Método que devuelve una máquina cuyo identificador coincida con el pasado
	 * como parámetro
	 * @param intCodMaquina Identificador de la máquina buscada
	 * @return
	 */
	public MaquinaC getMaquina(Integer intCodMaquina){
		MaquinaEng maquinaDAO = new MaquinaEng(conexion);
		
		MaquinaC maquina = maquinaDAO.findById(intCodMaquina);
		
		return maquina;
	}

}
