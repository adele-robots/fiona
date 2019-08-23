package com.adelerobots.web.fiopre.listeners;

import java.util.Map;

import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;

public class InsertConfigParamTypes extends AbstractUserProcessListener{	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6821958641562437176L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		// Se recupera el id de usuario
		Integer userid = ContextUtils.getUserIdAsInteger();		
		// Se desactiva el polling
		gestorEstados.setPropiedad("pollInsertConfigParamTypes", "enabled", Boolean.FALSE);
		
		Map<String, String> items;
		
		try {
			items = invokeListAllParamTypesByUser(userid);
			gestorDatos.setItemsMap("configType", items);
		} catch (FactoriaDatosException e) {
			e.printStackTrace();
		} catch (PersistenciaException e) {
			e.printStackTrace();
		} catch (FawnaInvokerException e) {
			e.printStackTrace();
		}
		
	}
	
	

}
