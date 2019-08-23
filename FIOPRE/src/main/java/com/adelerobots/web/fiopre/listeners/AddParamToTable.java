package com.adelerobots.web.fiopre.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;

public class AddParamToTable implements IProcesadorDeAjaxChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4717331589570595394L;
	private Integer paramID = null;

	@SuppressWarnings("unchecked")
	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		/** Recuperar información del parámetro de configuración a insertar **/
		String configName = (String) gestorDatos.getValue("configName");
		String configType = (String) gestorDatos.getValue("configType");
		String configDefault = (String) gestorDatos.getValue("configDefault");
		
		if(!configName.equals("") && !configDefault.equals("")){
			// Extraer el nombre del tipo de parámetro, no el identificador
			String configTypeName = "";
			Map <String,String> configTypes = ((Map<String,String>)gestorDatos.getItems("configType"));					
			if(configType != null){				
				for(Map.Entry<String, String> configTypeMap:configTypes.entrySet()){
					if(configTypeMap.getValue().equalsIgnoreCase(configType)){
						configTypeName = configTypeMap.getKey();									
					}
				}					
			}

			List<Map<String, String>> items = (List<Map<String, String>>)gestorDatos.getValue("tablaParams");
			if(items == null || items.isEmpty()){
				items = new ArrayList<Map<String,String>>();
				paramID = 0;
			}else{
				paramID++;
			}

			Map<String, String> paramRow = new HashMap<String, String>();
			paramRow.put("param_nombre", configName);
			paramRow.put("param_type_id", configType);
			paramRow.put("param_type", configTypeName);
			paramRow.put("param_default", configDefault);
			paramRow.put("param_id", paramID.toString());

			items.add(paramRow);		
			gestorDatos.setValue("tablaParams", items);
			// Hacer un reset de los campos texto
			gestorDatos.setValue("configName", "");
			gestorDatos.setValue("configDefault", "");
		}
	}

}
