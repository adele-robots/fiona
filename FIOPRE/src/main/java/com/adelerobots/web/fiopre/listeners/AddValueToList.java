package com.adelerobots.web.fiopre.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;

public class AddValueToList implements IProcesadorDeAjaxChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -719412645714991747L;
	private Integer valorID = null;

	@SuppressWarnings("unchecked")
	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		/** Recuperar información del valor de la lista a insertar **/
		String valorLista = (String) gestorDatos.getValue("valorLista");
		
		
		if(!valorLista.equals("")){

			List<Map<String, String>> items = (List<Map<String, String>>)gestorDatos.getValue("tablaValoresLista");
			if(items == null || items.isEmpty()){
				items = new ArrayList<Map<String,String>>();
				valorID = 0;
			}else{
				valorID++;
			}


			Map<String, String> valorRow = new HashMap<String, String>();
			valorRow.put("nombre", valorLista);
			valorRow.put("valor_id", valorID.toString());

			items.add(valorRow);		
			gestorDatos.setValue("tablaValoresLista", items);
			gestorDatos.setValue("valorLista", "");

		}
	}

}
