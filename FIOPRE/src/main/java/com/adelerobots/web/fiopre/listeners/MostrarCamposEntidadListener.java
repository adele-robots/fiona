package com.adelerobots.web.fiopre.listeners;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;

public class MostrarCamposEntidadListener extends AbstractUserProcessListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1453277730141745275L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		String flagEntity = gestorDatos.getValue("radioTipoEntidad").toString();
		if(flagEntity.equals("0")) {
			gestorEstados.setPropiedad("organizationtype", "required", false);
			gestorEstados.setPropiedad("legalEntityName", "required", false);
			gestorEstados.setPropiedad("website", "required", false);
			gestorEstados.setPropiedad("workEmail", "required", false);
			gestorEstados.setPropiedad("countryCode", "required", false);
			gestorEstados.setPropiedad("phoneNumber", "required", false);
			gestorEstados.setPropiedad("panelAgreement", "rendered", false);
			gestorEstados.setPropiedad("panelEntity", "rendered", false);
			
		}
		else {
			if(flagEntity.equals("1")) {
				gestorEstados.setPropiedad("organizationtype", "required", true);
				gestorEstados.setPropiedad("legalEntityName", "required", true);
				gestorEstados.setPropiedad("website", "required", true);
				gestorEstados.setPropiedad("workEmail", "required", true);
				gestorEstados.setPropiedad("countryCode", "required", true);
				gestorEstados.setPropiedad("phoneNumber", "required", true);
				gestorEstados.setPropiedad("panelAgreement", "rendered", true);
				gestorEstados.setPropiedad("panelEntity", "rendered", true);
			}
		}
	}
}