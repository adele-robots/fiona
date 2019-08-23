package com.adelerobots.web.fiopre.listeners;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;

public class AbrirCerrarDialogoOpinion extends AbstractUserProcessListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2021458178583414927L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		gestorDatos.setValue("radioRate", "0");
		
		gestorEstados.openModalAlert("dialogoOpinion");		
		
	}

	
	
}
