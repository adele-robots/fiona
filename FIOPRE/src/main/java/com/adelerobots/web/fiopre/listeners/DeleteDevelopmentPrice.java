package com.adelerobots.web.fiopre.listeners;

import java.util.List;
import java.util.Map;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;

public class DeleteDevelopmentPrice extends AbstractUserProcessListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7352245263363339956L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		List <Map<String,String>> listaPreciosDesarrollo = (List <Map<String,String>>)gestorDatos.getValue("tablaPrecios");
		
		String precioId = (String)gestorDatos.getValue("precioId");
		Integer intCodPrecio = Integer.valueOf((String)gestorDatos.getValue("precioId"));
		Integer intCodSpark = Integer.valueOf((String) gestorDatos
				.getValue("spark_id"));
		boolean encontrado = false;
		String [] resultado = new String[3];
		
		// Llamada al SN que borre de BBDD el precio seleccionado
		try {
			resultado = invokeDeletePriceFromSpark(intCodPrecio, intCodSpark);
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
		
		for(int i = 0; i<listaPreciosDesarrollo.size() && !encontrado; i++){
			Map<String,String> row = listaPreciosDesarrollo.get(i);
			String id = row.get("price_id");
			String es_usado = row.get("es_usado"); 
			
			if(id.equalsIgnoreCase(precioId)){
				encontrado = true;
				if(es_usado.equalsIgnoreCase("0"))
					listaPreciosDesarrollo.remove(i);
			}
		}
		
		gestorDatos.setValue("tablaPrecios", listaPreciosDesarrollo);
		
		
		
		
	}

}
