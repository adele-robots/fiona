package com.adelerobots.fioneg.engine;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.status.StatusC;

public class StatusEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<StatusC> {
	
	String conexion;
	
	public StatusEng(String conexion) {
		super(conexion);
		this.conexion=conexion;
	}

	/**
	 * Método que devuelve la información de un estado concreto
	 * 
	 * @param cnStatus Identificador único del estado
	 * @return Se devolverá el objeto de tipo "StatusC" cuyo
	 * id coincida con el pasado como parámetro
	 */
	public StatusC getStatus(Integer cnStatus) {
		return this.findById(cnStatus);
	}
	
	/**
	 * Método que devuelve todos los elementos de tipo "Status"
	 * existentes en la base de datos
	 * 
	 * @return Lista con todos los status de base de datos
	 */
	public List<StatusC> getAllStatus(){
		return this.findAll();
	}
	
	/**
	 * Método que devuelve la lista de estados por los que
	 * ha pasado un spark
	 * 
	 * @param intCodSpark Identificador único del spark
	 * @return Se devuelve la lista de StatusC del
	 * spark ordenada decrecientemente por fecha
	 */
	public List<StatusC> getListStatusBySpark(Integer intCodSpark){
		Criteria criteria = getCriteria();
		
		criteria.createCriteria("lstSparkStatusC").add(Restrictions.eq("intCodSpark", intCodSpark)).addOrder(Order.desc("dateCambio"));
		
		return criteria.list();
	}
	
}
