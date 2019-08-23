package com.adelerobots.web.fiopre.listeners;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;

public class ChangeAccountTypeComboListener implements
		IProcesadorDeAjaxChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 868767159070277705L;
	
	private static final String COMBO_ACCOUNT_TYPE = "accounttype";
	private static final String COMBO_PAYMENT_PERIOD = "paymentPeriod";

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		String valorAccountType = (String)gestorDatos.getValue(COMBO_ACCOUNT_TYPE);
		
		if(!valorAccountType.equals("1")){
			gestorEstados.setPropiedad(COMBO_PAYMENT_PERIOD, "rendered", true);
		}else{
			gestorEstados.setPropiedad(COMBO_PAYMENT_PERIOD, "rendered", false);
		}

	}

}
