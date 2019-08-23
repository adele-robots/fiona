package com.adelerobots.clineg.engine;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.clineg.entity.HostingC;
import com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine;

public class HostingEng extends QueryEngine<HostingC> {
	
	public HostingEng(String dataSource) {
		super(dataSource);		
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
	public HostingC getHosting(Integer intCodUnit, Integer intNumUsers, String strResolution, Character chHighAvailability){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("intCodUnit", intCodUnit));
		criteria.add(Restrictions.eq("intUsers", intNumUsers));
		criteria.add(Restrictions.ilike("strResolution", strResolution, MatchMode.ANYWHERE));
		criteria.add(Restrictions.eq("chHighAvailability",chHighAvailability));
		
		return (HostingC)criteria.uniqueResult();
	}

}
