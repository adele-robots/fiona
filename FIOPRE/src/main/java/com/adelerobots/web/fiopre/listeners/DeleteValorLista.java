package com.adelerobots.web.fiopre.listeners;

import java.util.List;
import java.util.Map;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;

public class DeleteValorLista implements IProcesadorDeAjaxChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3934170600808409380L;

	@SuppressWarnings("unchecked")
	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		List <Map<String,String>> listaValoresTipoLista = (List <Map<String,String>>)gestorDatos.getValue("tablaValoresLista");
		
		String valorId = (String)gestorDatos.getValue("valorId");
		boolean encontrado = false;
		
		for(int i = 0; i<listaValoresTipoLista.size() && !encontrado; i++){
			Map<String,String> row = listaValoresTipoLista.get(i);
			String id = row.get("valor_id");
			
			if(id.equalsIgnoreCase(valorId)){
				encontrado = true;
				listaValoresTipoLista.remove(i);
			}
		}		
		gestorDatos.setValue("tablaValoresLista", listaValoresTipoLista);		
	}

}