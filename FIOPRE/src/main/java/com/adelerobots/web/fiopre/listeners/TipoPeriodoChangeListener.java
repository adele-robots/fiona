package com.adelerobots.web.fiopre.listeners;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;

public class TipoPeriodoChangeListener extends AbstractUserProcessListener {
	
	private static final String ID_COMBO_VALOR_MESES = "idValorMeses";
	private static final String ID_COMBO_VALOR_ANIOS = "idValorAnios";
	private static final String ID_COMBO_VALOR_MESES_PROD = "idValorMesesProd";
	private static final String ID_COMBO_VALOR_ANIOS_PROD = "idValorAniosProd";

	/**
	 * 
	 */
	private static final long serialVersionUID = -1808593800158453374L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		String idComponente = gestorEstados.getIdComponente();
		
		String idUnidadTiempo = (String)gestorDatos.getValue(idComponente);
		
		String idComboValorMeses = ID_COMBO_VALOR_MESES;
		String idComboValorAnios = ID_COMBO_VALOR_ANIOS;
		if(idComponente.indexOf("Prod") > 0){
			idComboValorMeses = ID_COMBO_VALOR_MESES_PROD;
			idComboValorAnios = ID_COMBO_VALOR_ANIOS_PROD;
		}
		
		switch (Integer.valueOf(idUnidadTiempo)) {
		case 1:
			gestorEstados.setPropiedad(idComboValorMeses, "rendered", true);
			gestorEstados.setPropiedad(idComboValorAnios, "rendered", false);
			break;

		case 2:
			gestorEstados.setPropiedad(idComboValorMeses, "rendered", false);
			gestorEstados.setPropiedad(idComboValorAnios, "rendered", true);
			break;
		}

	}

}
