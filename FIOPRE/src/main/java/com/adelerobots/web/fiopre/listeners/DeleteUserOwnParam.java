package com.adelerobots.web.fiopre.listeners;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.persistencia.ContextoLocator;
import com.treelogic.fawna.presentacion.core.persistencia.DatoFactory;
import com.treelogic.fawna.presentacion.core.persistencia.IDato;

public class DeleteUserOwnParam implements IProcesadorDeAjaxChangeListener{	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1013078475703550469L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {

		gestorEstados.openModalAlert("alertDeleteListType");
		String userOwnParamId = (String)gestorDatos.getValue("userOwnParamId");	
		
		try {
			/** Se guarda el valor del id del tipo a borrar al contexto para poder recuperarlo
			 * 	desde otro listener
			 */
			IDato datoOwnParamId = DatoFactory.creaDatoSimple();						
			datoOwnParamId.setPropiedad("datoOwnParamId");
			datoOwnParamId.setValor(userOwnParamId);
			
			ContextoLocator.getInstance().getContextoVentana().putCtxValue(datoOwnParamId);
		} catch (PersistenciaException e) {
			e.printStackTrace();
		}
	}
}