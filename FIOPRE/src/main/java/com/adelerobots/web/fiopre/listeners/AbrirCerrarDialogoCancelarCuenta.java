package com.adelerobots.web.fiopre.listeners;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;

public class AbrirCerrarDialogoCancelarCuenta implements
		IProcesadorDeAjaxChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1536355678288555022L;

	public void procesarAjaxChangeListener(
			GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {

		String idComponente = gestorEstados.getIdComponente();
		String idAlert = "alertCancelarCuenta";

		// Abrir alert
		if (idComponente.indexOf("cerrar") == 0)
			gestorEstados.closeModalAlert(idAlert);
		else
			gestorEstados.openModalAlert(idAlert);
		// gestorEstados.closeModalAlert(idAlert);

	}

}
