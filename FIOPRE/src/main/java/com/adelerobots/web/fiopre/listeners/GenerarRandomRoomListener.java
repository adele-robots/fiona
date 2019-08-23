package com.adelerobots.web.fiopre.listeners;

import java.math.BigInteger;
import java.security.SecureRandom;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;

public class GenerarRandomRoomListener implements IProcesadorDeAjaxChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9133700942610642834L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		// Generamos código alfanumérico aleatorio de 32 elementos
		String randomRoom = generatedRandomRoom(32);
		
		// Colocamos el código en el elemento del formulario que corresponde
		gestorDatos.setValue("randomRoom", randomRoom);
		
		
	}
	
	
	private String generatedRandomRoom(Integer length){
		SecureRandom random = new SecureRandom();
		String result = new BigInteger(130, random).toString(length); 
		return result;
	}
	
	

}
