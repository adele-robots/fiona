package com.adelerobots.fioneg.engine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.ProcesoC;

public class ProcesoEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<ProcesoC>{
	String conexion;
	
	public ProcesoEng(String conexion) {
		super(conexion);
		this.conexion=conexion;
	}

	public ProcesoC addProceso(ProcesoC proceso) {
		this.insert(proceso);
		this.flush();
		this.refresh(proceso);		
		return proceso;
		
	}
	
	public ProcesoC updateProceso(ProcesoC proceso) {
		this.update(proceso);
		this.flush();
		this.refresh(proceso);		
		return proceso;
		
	}
	
	public ProcesoC checkProceso(Integer user, Integer pid, String host){
		
		List<Criterion> lstCriterios = new ArrayList<Criterion>();
		lstCriterios.add(Restrictions.eq("cnUser", user));
		lstCriterios.add(Restrictions.eq("nuPid", pid));
		lstCriterios.add(Restrictions.eq("dcHost", host));
		
		ProcesoC proceso = new ProcesoC();
		
		proceso = this.findByCriteriaUniqueResult(lstCriterios);
		
		return proceso;
	}
	
	public void removeProceso(Integer pid, String host){
		List<Criterion> lstCriterios = new ArrayList<Criterion>();
		lstCriterios.add(Restrictions.eq("nuPid", pid));
		lstCriterios.add(Restrictions.eq("dcHost", host));
		
		ProcesoC proceso = new ProcesoC();
		
		proceso = this.findByCriteriaUniqueResult(lstCriterios);
		
		this.delete(proceso);		
		
	}

	public Integer checkExistingProceso(Integer user) {
		List<Criterion> lstCriterios = new ArrayList<Criterion>();
		lstCriterios.add(Restrictions.eq("cnUser", user));
				
		ProcesoC proceso = new ProcesoC();
		
		proceso = this.findByCriteriaUniqueResult(lstCriterios);
		Integer processnumber = this.findByCriteria(10, lstCriterios).size();
		
		return processnumber;
	}
	
	/**
	 * Método que devolverá el número de procesos en ejecución para un
	 * determinado usuario
	 * 
	 * @param intCodUsuario Identificador único del usuario	 * 
	 * @return Se devolverá el número de procesos activos
	 */
	public Integer getNumProcesosActivosPorUsuario(Integer intCodUsuario){
		Criteria criterios = getCriteria();
		Integer numProcesosActivos = -1;
		
		criterios.add(Restrictions.eq("cnUser", intCodUsuario));
		criterios.setProjection(Projections.countDistinct("cnProceso"));
		
		numProcesosActivos = (Integer)criterios.uniqueResult();
		
		return numProcesosActivos;
	}
	
	

	/**
	 * Método que devolverá el número total de procesos activos
	 * en una máquina dada
	 * 
	 * @param hostName
	 * @return
	 */
	public Integer getNumProcesosActivosPorMaquina(String hostName){
		Criteria criterios = getCriteria();
		Integer numProcesosActivos = -1;
		
		criterios.add(Restrictions.eq("dcHost", hostName));
		criterios.setProjection(Projections.countDistinct("cnProceso"));
		
		numProcesosActivos = (Integer)criterios.uniqueResult();
		
		return numProcesosActivos;
	}
	
	/**
	 * Método que devuelve la lista de procesos de un usuario
	 * 
	 * @param intCodUsuario Id del usuario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProcesoC> getProcessByUser(Integer intCodUsuario){		
		Criteria criteria = this.getCriteria();		 
		Criterion typecrit = Restrictions.eq("cnUser", intCodUsuario);
		criteria.add(typecrit);
		
		return criteria.list();
	}

}
