package com.adelerobots.web.fiopre.listeners;

import com.adelerobots.fioneg.util.FunctionUtils;
import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.arq.negocio.core.ValidacionException;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;

public class RenameConfigListener extends AbstractUserProcessListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6242481760714835363L;

	private static final String FIELD_NAME = "configName";
	private static final String FIELD_COMBO = "config";
	
	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {

		try {
			Integer userId = ContextUtils.getUserIdAsInteger();
			String config = (String) gestorDatos.getValue(FIELD_COMBO);
			String name = (String) gestorDatos.getValue(FIELD_NAME);
			if(validate(gestorEstados, gestorDatos)) {
				invokeSetConfigName(userId, Integer.valueOf(config), name);
				gestorEstados.setPropiedad("pollInsertConfig", "enabled", true);
				gestorEstados.setPropiedad("renameConfigButton", "rendered", true);
				gestorEstados.setPropiedad("renameConfigInput", "rendered", false);
			}
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
	
	private Boolean validate(GestorEstadoComponentes gE,
			GestorDatosComponentes gD) {

		String name = FunctionUtils.toString(gD.getValue(FIELD_NAME), null);
		String config = FunctionUtils.toString(gD.getValue(FIELD_COMBO), null);
		try {
			if(name == null || ("").equals(name) || name.length() > 20)
				throw new ValidacionException(getMessage("RenameConfigValidator.validate","The name must be 1-20 characters."));
			if(gD.getItems(FIELD_COMBO).containsKey(name) && !gD.getItems(FIELD_COMBO).get(name).equals(config))
				throw new ValidacionException(getMessage("RenameConfigValidator.validate","You already have a configuration with this name"));
		} catch (ValidacionException e) {
			gD.addErrorValidacionSimple(e.getMessage(), e.getMessage(), FIELD_NAME);
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
}