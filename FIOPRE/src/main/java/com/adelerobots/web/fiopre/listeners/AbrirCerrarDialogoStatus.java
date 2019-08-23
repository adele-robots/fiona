package com.adelerobots.web.fiopre.listeners;

import java.util.List;
import java.util.Map;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;

public class AbrirCerrarDialogoStatus extends AbstractUserProcessListener {



	/**
	 * 
	 */
	private static final long serialVersionUID = 6015290447658311488L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		
		String strCodSpark = (String)gestorDatos.getValue("spark_id");
		
		if(strCodSpark != null){
			Integer intCodSpark = Integer.valueOf(strCodSpark);
			
			try {
				Object datoComplejo = invokeGetListStatus(intCodSpark);
				
				List<Map<String, String>> datosStatus = (List<Map<String, String>>) datoComplejo;
				
				gestorDatos.setValue("tablaStatus", datosStatus);
				
				gestorEstados.openModalAlert("dialogoListStatus");
				
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

}
