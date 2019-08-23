package com.adelerobots.fioneg.engine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.ConfigParamC;

public class ConfigParamEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<ConfigParamC>{
	String conexion;
	
	public ConfigParamEng(String conexion) {
		super(conexion);
		this.conexion=conexion;
	}
	
	/**
	 * Crea un parámetro de configuración
	 * @param configParam ConfigParam
	 * @return
	 */
	public ConfigParamC create(final ConfigParamC configParam) {	
		this.insert(configParam);
		this.flush();
		//this.refresh(configParam);
		return configParam;
	}	

	public List<ConfigParamC> getGenConfParamsUser(List<ConfigParamC> lista) {
		List<Criterion> lstCriterios = new ArrayList<Criterion>();
		lstCriterios.add(Restrictions.eq("cnSpark", 0));
		lstCriterios.add(Restrictions.eq("isConfigurable", 1));
		
		lista = this.findByCriteria(lstCriterios);
		
		return lista;
		
	}
	
	public List<ConfigParamC> getGenConfParamsNotUser(List<ConfigParamC> lista) {
		List<Criterion> lstCriterios = new ArrayList<Criterion>();
		lstCriterios.add(Restrictions.eq("cnSpark", 0));
		lstCriterios.add(Restrictions.eq("isConfigurable", 0));
		
		lista = this.findByCriteria(lstCriterios);
		
		return lista;
		
	}
	
	public List<ConfigParamC> getGenConfParamsAll(List<ConfigParamC> lista) {
		List<Criterion> lstCriterios = new ArrayList<Criterion>();
		lstCriterios.add(Restrictions.eq("cnSpark", 0));
		
		lista = this.findByCriteria(lstCriterios);
		
		return lista;
		
	}
}
