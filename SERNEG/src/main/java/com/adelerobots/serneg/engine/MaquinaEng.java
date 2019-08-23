package com.adelerobots.serneg.engine;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.serneg.entity.MaquinaC;
import com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine;

public class MaquinaEng extends QueryEngine<MaquinaC> {
	
	String conexion;
	
	public MaquinaEng(String conexion){
		super(conexion);
		this.conexion = conexion;
	}
	
	/**
	 * Método que devuelve una máquina cuyo nombre coincida con el pasado como
	 * parámetro
	 * @param nombre Cadena que representa el nombre de la máquina buscada
	 * @return
	 */
	public MaquinaC getMaquinaByName(String strNombre){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.ilike("strNombre", strNombre));
		
		return (MaquinaC)criteria.uniqueResult();
	}
	
	/**
	 * Método que permite buscar una máquina utilizando su IP
	 * @param strIP Cadena que representa la IP de la máquina buscada
	 * @return
	 */
	public MaquinaC getMaquinaByIP(String strIP){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.ilike("strIp", strIP));
		
		return (MaquinaC)criteria.uniqueResult();
	}

}
