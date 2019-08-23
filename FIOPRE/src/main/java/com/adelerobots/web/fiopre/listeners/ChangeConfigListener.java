package com.adelerobots.web.fiopre.listeners;

import java.io.File;
import java.io.IOException;

import com.adelerobots.web.fiopre.utilidades.ConfigUtils;
import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.persistencia.ContextoLocator;
import com.treelogic.fawna.presentacion.core.persistencia.DatoFactory;
import com.treelogic.fawna.presentacion.core.persistencia.IDato;

public class ChangeConfigListener extends AbstractUserProcessListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3865204912878267805L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		String config = gestorDatos.getValue("config").toString();
		try {
			final File usersPath = ConfigUtils.getNfsUsersFolder();
			String mailmd5 = ContextUtils.getUserMailD5();
			String command = "unlink " + usersPath + "/private/" + mailmd5;
			Process changeDir = Runtime.getRuntime().exec(command);
			changeDir.waitFor();
			command = "ln -s " + usersPath + "/private/" + mailmd5 + "_" + config + " " + usersPath + "/private/" + mailmd5;
			changeDir = Runtime.getRuntime().exec(command);
			changeDir.waitFor();
			// Almacenar la variable config para recuperarla al entrar en mainPage.jsp
			IDato dato1 = DatoFactory.creaDatoSimple();
			dato1.setPropiedad("USER_CONFIG");
			dato1.setValor(config);
			// Cambia el random para rerenderizar la imagen
			IDato dato2 = DatoFactory.creaDatoSimple();
			dato2.setPropiedad("random");
			dato2.setValor(Double.toString(Math.random()).substring(2));
			ContextoLocator.getInstance().getContextoSesion().putCtxValue(dato1);
			ContextoLocator.getInstance().getContextoVentana().putCtxValue(dato2);

			gestorEstados.setPropiedad("renameConfigButton", "rendered", true);
			gestorEstados.setPropiedad("renameConfigInput", "rendered", false);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenciaException e) {
			logger.error("[setUserConfig] Error al persistir valor de la propiedad USER_CONFIG", e);
		}
	}
}