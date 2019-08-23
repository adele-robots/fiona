package com.adelerobots.web.fiopre.listeners;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;

public class AsignaKeywordsListener implements IProcesadorDeAjaxChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8113358114870482213L;
	
	
	private static final String FIELD_KEYWORDS_SELECCIONADAS = "all_keywords";
	private static final String FIELD_ADDED_KEYWORDS = "keys_asignados";
	private static final String FIELD_HIDDEN_KEYWORD_IDS = "idsKeywords";

	@SuppressWarnings({ "rawtypes", "unchecked" })	
	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		
		Map allKeywords = gestorDatos.getItems(FIELD_KEYWORDS_SELECCIONADAS);
		
		
		Map addedKeywords = gestorDatos.getItems(FIELD_ADDED_KEYWORDS);
		
		String [] keywordsSeleccionadas = (String [])gestorDatos.getValue(FIELD_KEYWORDS_SELECCIONADAS);
		
		
		Set <String> claves = allKeywords.keySet();
		
		Iterator<String> itClaves = claves.iterator();
		
		while(itClaves.hasNext()){			
			String clave = (String)itClaves.next();
			String value = (String)allKeywords.get(clave);
			
			for(int i = 0; i< keywordsSeleccionadas.length; i++){				
				if(keywordsSeleccionadas[i].equals(value))
					addedKeywords.put(clave, value);
				
			}
		}
		
		// Recuperamos los ids de las keywords a asignar
		String idsKeywords =  (addedKeywords.values()).toString();
		
		idsKeywords = idsKeywords.replace('[', ' ');
		idsKeywords = idsKeywords.replace(']', ' ');
		idsKeywords = idsKeywords.trim();
		
		gestorDatos.setItemsMap(FIELD_ADDED_KEYWORDS, addedKeywords);
		
		// Asignamos los ids de las keywords al campo hidden
		gestorDatos.setValue(FIELD_HIDDEN_KEYWORD_IDS, idsKeywords);
		

	}

}
