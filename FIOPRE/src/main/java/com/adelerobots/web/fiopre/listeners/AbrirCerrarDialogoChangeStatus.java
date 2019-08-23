package com.adelerobots.web.fiopre.listeners;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;

public class AbrirCerrarDialogoChangeStatus extends AbstractUserProcessListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 426274935549205619L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		gestorEstados.openModalAlert("dialogoChangeStatus");
		
	}
	
	

}
