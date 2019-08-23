package com.adelerobots.fioneg.manager;

import java.util.ArrayList;
import java.util.List;

import com.adelerobots.fioneg.engine.DatoListaEng;
import com.adelerobots.fioneg.entity.DatoListaC;
import com.treelogic.fawna.arq.negocio.core.IRegistro;

public class DatoListaManager {
	private String conexion;

	public DatoListaManager(String conexion) {
		super();
		this.conexion = conexion;
	}
	
	
	public DatoListaC getDatoLista(Integer cnDatoLista) {
		DatoListaEng datoListaDao = new DatoListaEng(conexion);		
		DatoListaC datoLista = datoListaDao.getDatoLista(cnDatoLista);
		
		return datoLista;
	}
	
	/**
	 * Método que permite crear los valores de
	 * datoLista asociados a un tipo
	 * 
	 * @param 
	 * @return Se devolverá el nuevo tipo 
	 * 
	 */
	public List <DatoListaC> crearValoresDatoLista(IRegistro[] registro, Integer idTipo){	
		DatoListaEng datoListaDao = new DatoListaEng(conexion);
		
		List <DatoListaC> lstListValues = new ArrayList<DatoListaC>();		
		for(Integer j = 0;j<registro.length;j++){
			DatoListaC listValue = new DatoListaC();
			listValue.setStrNombre(registro[j].getString("FIONEG018060010"));
			listValue.setCnTipo(idTipo);
			lstListValues.add(listValue);
			// Insertar el objeto en BB.DD
			datoListaDao.create(listValue);
		}				
		return lstListValues;
	}
	
	/**
	 * Método que permite borrar una entrada de DatoLista
	 * 
	 * @param cnDatoLista Identificador del DatoLista
	 * @return 
	 * 
	 */
	public DatoListaC deleteDatoLista(Integer cnDatoLista){
		DatoListaEng datoListaDao = new DatoListaEng(conexion);		
		DatoListaC datoLista = datoListaDao.getDatoLista(cnDatoLista);
		
		datoListaDao.delete(datoLista);				
		
		return datoLista;
	}
	
}
