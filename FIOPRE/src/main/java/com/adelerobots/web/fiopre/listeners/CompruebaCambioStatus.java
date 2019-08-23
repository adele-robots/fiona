package com.adelerobots.web.fiopre.listeners;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;

public class CompruebaCambioStatus implements IProcesadorDeAjaxChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2609158668352685794L;
	
	private static final String LABEL_STATUS = "labelRechazo";
	
	private static final String FIELD_RECHAZO = "contenidoRechazo";
	
	

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		String idComponente = gestorEstados.getIdComponente();
		
		String idStatus = null;
		if(idComponente != null && !"".equals(idComponente)){
			idStatus = (String)gestorDatos.getValue(idComponente);
			
			
			if(idStatus.equals("4") || idStatus.equals("3")){
				gestorEstados.setPropiedad(LABEL_STATUS, "rendered", Boolean.TRUE);
				gestorEstados.setPropiedad(FIELD_RECHAZO, "rendered", Boolean.TRUE);
			}else{
				gestorEstados.setPropiedad(LABEL_STATUS, "rendered", Boolean.FALSE);
				gestorEstados.setPropiedad(FIELD_RECHAZO, "rendered", Boolean.FALSE);
			}
		}		

	}

}
