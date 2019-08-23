package com.adelerobots.web.fiopre.listeners;

import java.util.Map;

import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;

public class MostrarUsuarioConfigsListener extends AbstractUserProcessListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6242481760714835363L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {

		try {
			Integer userId = ContextUtils.getUserIdAsInteger();
			Map<String, String> items = invokeGetUsuarioConfigs(userId);
			gestorDatos.setItemsMap("config", items);
			// Desactivamos el polling
			gestorEstados.setPropiedad("pollInsertConfig", "enabled", Boolean.FALSE);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoriaDatosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenciaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FawnaInvokerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}