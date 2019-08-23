package com.adelerobots.web.fiopre.listeners;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;

public class MostrarRenameConfigInputListener extends AbstractUserProcessListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4723126573674807277L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {

		gestorEstados.setPropiedad("renameConfigButton", "rendered", false);
		gestorEstados.setPropiedad("renameConfigInput", "rendered", true);

	}
}