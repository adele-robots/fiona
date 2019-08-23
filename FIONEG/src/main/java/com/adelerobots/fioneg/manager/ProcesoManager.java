package com.adelerobots.fioneg.manager;

import java.util.List;

import com.adelerobots.fioneg.engine.ProcesoEng;
import com.adelerobots.fioneg.entity.ProcesoC;

public class ProcesoManager {
	private String conexion;

	public ProcesoManager(String conexion) {
		super();
		this.conexion = conexion;
	}

	public ProcesoC addExecutionInfo(ProcesoC proceso) {
		ProcesoEng dao = new ProcesoEng(conexion);
		dao.addProceso(proceso);
		return proceso;
		
	}
	
	public ProcesoC updateExecutionInfo(ProcesoC proceso) {
		ProcesoEng dao = new ProcesoEng(conexion);
		dao.updateProceso(proceso);
		return proceso;
		
	}
	
	public ProcesoC checkExecutionInfo(Integer user, Integer pid, String host){
		ProcesoEng dao = new ProcesoEng(conexion);
		return dao.checkProceso(user, pid, host);
	}
	
	public Integer checkExistingProcess(Integer user){
		ProcesoEng dao = new ProcesoEng(conexion);
		return dao.checkExistingProceso(user);
	}
	
	public void removeExecutionInfo(Integer pid, String host){
		ProcesoEng dao = new ProcesoEng(conexion);
		dao.removeProceso(pid, host);
	
	}
	
	/**
	 * Método que devolverá el número de procesos en ejecución para un
	 * determinado usuario
	 * 
	 * @param intCodUsuario Identificador único del usuario	 * 
	 * @return Se devolverá el número de procesos activos
	 */
	public Integer getNumProcesosActivosPorUsuario(Integer intCodUsuario){
		ProcesoEng dao = new ProcesoEng(conexion);
		return dao.getNumProcesosActivosPorUsuario(intCodUsuario);		
	}
	
	/**
	 * Método que devolverá el número total de procesos activos
	 * en una máquina dada
	 * 
	 * @param hostName
	 * @return
	 */
	public Integer getNumProcesosActivosPorMaquina(String hostName){
		ProcesoEng dao = new ProcesoEng(conexion);
		return dao.getNumProcesosActivosPorMaquina(hostName);		
	}
	
	/**
	 * Método que devuelve la lista de procesos de un usuario
	 * 
	 * @param intCodUsuario Id del usuario
	 * @return
	 */
	public List<ProcesoC> getProcessByUser(Integer intCodUsuario){		
		ProcesoEng dao = new ProcesoEng(conexion);
		
		return dao.getProcessByUser(intCodUsuario);
	}
}
