package com.adelerobots.web.fiopre.listeners;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;

public class AsignaInterfaz implements IProcesadorDeAjaxChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8113358114870482213L;
	
	
	private static final String FIELD_SELECTED_INTERFACES = "interfaces_source";
	private static final String FIELD_PROVIDED_INTERFACES = "provided_interfaces";
	private static final String FIELD_REQUIRED_INTERFACES = "required_interfaces";

	@SuppressWarnings({ "rawtypes", "unchecked" })	
	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		String interfaceType = (String) gestorDatos.getValue("interfaceType");			
		
		Map allInterfaces = gestorDatos.getItems(FIELD_SELECTED_INTERFACES);
		//Recupero las interfaces seleccionadas para añadir
		String [] selectedInterfaces = (String [])gestorDatos.getValue(FIELD_SELECTED_INTERFACES);
		
		// Distingo si se quieren añadir interfaces 'provided' o 'required'
		if(interfaceType.compareTo("1")==0){
			//Recupero las interfaces que ya hay en la lista de las proveídas del usuario
			Map providedInterfaces = gestorDatos.getItems(FIELD_PROVIDED_INTERFACES);			

			Set <String> claves = allInterfaces.keySet();
			Iterator<String> itClaves = claves.iterator();

			while(itClaves.hasNext()){			
				String clave = (String)itClaves.next();
				String value = (String)allInterfaces.get(clave);

				for(int i = 0; i< selectedInterfaces.length; i++){				
					if(selectedInterfaces[i].equals(value))
						providedInterfaces.put(clave, value);
				}
			}	
			gestorDatos.setItemsMap(FIELD_PROVIDED_INTERFACES, providedInterfaces);
		}else if(interfaceType.compareTo("2")==0){
			//Recupero las interfaces que ya hay en la lista de las requeridas del usuario
			Map requiredInterfaces = gestorDatos.getItems(FIELD_REQUIRED_INTERFACES);			

			Set <String> claves = allInterfaces.keySet();
			Iterator<String> itClaves = claves.iterator();

			while(itClaves.hasNext()){			
				String clave = (String)itClaves.next();
				String value = (String)allInterfaces.get(clave);

				for(int i = 0; i< selectedInterfaces.length; i++){				
					if(selectedInterfaces[i].equals(value))
						requiredInterfaces.put(clave, value);
				}
			}
			gestorDatos.setItemsMap(FIELD_REQUIRED_INTERFACES, requiredInterfaces);
		}
	}

}
