package com.adelerobots.web.fiopre.listeners;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;

public class HabilitaDiasTrialListener implements IProcesadorDeAjaxChangeListener{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		String idComponente =  gestorEstados.getIdComponente();
		
		// Nos aseguramos de estar recibiendo el evento del check adecuado
		if(idComponente.compareToIgnoreCase("checkTrial")==0){
			
			Boolean valueCheck = (Boolean)gestorDatos.getValue(idComponente);
			
			if(valueCheck.equals(new Boolean(Boolean.TRUE)))				
				gestorEstados.setPropiedad("diasTrial", "disabled", Boolean.FALSE);
			else{
				gestorEstados.setPropiedad("diasTrial", "disabled", Boolean.TRUE);
				gestorDatos.setValue("diasTrial", new Integer(0));
			}
		}
		
	}
	
	

}
