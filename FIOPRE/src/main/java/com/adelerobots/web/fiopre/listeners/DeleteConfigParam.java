package com.adelerobots.web.fiopre.listeners;

import java.util.List;
import java.util.Map;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;

public class DeleteConfigParam implements IProcesadorDeAjaxChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7549659692304491457L;

	@SuppressWarnings("unchecked")
	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		List <Map<String,String>> listaParametrosConfiguracion = (List <Map<String,String>>)gestorDatos.getValue("tablaParams");
		
		String paramId = (String)gestorDatos.getValue("paramId");
		boolean encontrado = false;
		
		for(int i = 0; i<listaParametrosConfiguracion.size() && !encontrado; i++){
			Map<String,String> row = listaParametrosConfiguracion.get(i);
			String id = row.get("param_id");
			
			if(id.equalsIgnoreCase(paramId)){
				encontrado = true;
				listaParametrosConfiguracion.remove(i);
			}
		}		
		gestorDatos.setValue("tablaParams", listaParametrosConfiguracion);		
	}

}