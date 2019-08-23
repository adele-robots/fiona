package com.adelerobots.serneg.engine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.serneg.entity.ProcesoMaquinaC;
import com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine;

public class ProcesoMaquinaEng extends QueryEngine<ProcesoMaquinaC> {
	
	String conexion;
	
	public ProcesoMaquinaEng(String conexion){
		super(conexion);
		this.conexion = conexion;
	}
	
	/**
	 * Método que devolverá todos los proceso máquina asignados a un usuario.	 
	 * @param strEmail Email del usuario buscado
	 * @return
	 */
	public List<ProcesoMaquinaC> getMaquinasUsuario(String strEmail){
		List<ProcesoMaquinaC> lstProcesoMaquina = new ArrayList<ProcesoMaquinaC>();
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("strEmail", strEmail));
		
		lstProcesoMaquina = criteria.list();
		
		return lstProcesoMaquina;
	}
	
	/**
	 * Método que devolverá la máquina en la que se encuentre el avatar de un determinado 
	 * usuario
	 * @param strEmail Cadena que identifica al usuario
	 * @param strCodAvatar 
	 * @return
	 */
	public ProcesoMaquinaC getMaquinaAvatarUsuario(String strEmail, String strCodAvatar){		
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("strEmail", strEmail));
		criteria.add(Restrictions.eq("strAvatar", strCodAvatar));	
		
		
		return (ProcesoMaquinaC)criteria.uniqueResult();
	}
	
	/**
	 * Método que devolverá el número de procesos totales para
	 * una máquina
	 * @param intCodMaquina Identificador único de la máquina
	 * @return
	 */
	public Integer getNumUsuarios(Integer intCodMaquina){
		Integer intNumUsuariosRegistrados = new Integer(0);
		Criteria criteria = getCriteria();
		criteria.createAlias("maquina", "maquina");		
		criteria.add(Restrictions.eq("maquina.intCodMaquina", intCodMaquina));
		criteria.setProjection(Projections.sum("intNumProcesos"));
		intNumUsuariosRegistrados = (Integer)criteria.uniqueResult();
		return intNumUsuariosRegistrados;
	}

}
