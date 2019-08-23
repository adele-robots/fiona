package com.adelerobots.fioneg.manager;

import java.util.ArrayList;
import java.util.List;

import com.adelerobots.fioneg.engine.ConfigParamEng;
import com.adelerobots.fioneg.entity.ConfigParamC;
import com.treelogic.fawna.arq.negocio.core.IRegistro;

public class ConfigParamManager {
	private String conexion;

	public ConfigParamManager(String conexion) {
		super();
		this.conexion = conexion;
	}
	
	public List<ConfigParamC> getGenConfParamsUser(){
		ConfigParamEng dao = new ConfigParamEng(conexion);
		List<ConfigParamC> lista = new ArrayList<ConfigParamC>();
		lista = dao.getGenConfParamsUser(lista);
				
		return lista;
		
	}
	
	public List<ConfigParamC> getGenConfParamsNotUser(){
		ConfigParamEng dao = new ConfigParamEng(conexion);
		List<ConfigParamC> lista = new ArrayList<ConfigParamC>();
		lista = dao.getGenConfParamsNotUser(lista);
				
		return lista;
		
	}
	
	public List<ConfigParamC> getGenConfParamsAll(){
		ConfigParamEng dao = new ConfigParamEng(conexion);
		List<ConfigParamC> lista = new ArrayList<ConfigParamC>();
		lista = dao.getGenConfParamsAll(lista);
				
		return lista;
		
	}
	
	/**
	 * Método que permite crear parámetros de configuración
	 * 
	 * @param intCodSpark
	 * @param registro 
	 * @return 
	 * 
	 */
	public List <ConfigParamC> createConfigParam(Integer intCodSpark, IRegistro[] registros){
		ConfigParamEng dao = new ConfigParamEng(conexion);
		
		
		List <ConfigParamC> lstListParams = new ArrayList<ConfigParamC>();		
		for(Integer j = 0;j<registros.length;j++){
			ConfigParamC configParam = new ConfigParamC();
			configParam.setCnSpark(intCodSpark);
			configParam.setStrNombre(registros[j].getString("FIONEG020010010"));
			configParam.setCnTipo(registros[j].getBigDecimal("FIONEG020010020").intValue());
			configParam.setStrDefaultValue(registros[j].getString("FIONEG020010030"));
			configParam.setStrMinValue(registros[j].getString("FIONEG020010040"));
			configParam.setStrMaxValue(registros[j].getString("FIONEG020010050"));
			configParam.setIsConfigurable(registros[j].getBigDecimal("FIONEG020010060").intValue());
			String descripcion = registros[j].getString("FIONEG020010070");
			if(descripcion != null){
				configParam.setStrDescripcion(descripcion);
			}
			
			lstListParams.add(configParam);
			// Insertar el objeto en BB.DD
			dao.create(configParam);
		}				
		return lstListParams;
	}
}
