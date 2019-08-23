package com.adelerobots.fioneg.manager;

import java.util.List;

import com.adelerobots.fioneg.engine.DatoListaUsuarioEng;
import com.adelerobots.fioneg.entity.DatoListaUsuarioC;

public class DatoListaUsuarioManager {
	private String conexion;

	public DatoListaUsuarioManager(String conexion) {
		super();
		this.conexion = conexion;
	}
	
	public List<DatoListaUsuarioC> getDatosListaUsuario(Integer userId, Integer tipo){
		List<DatoListaUsuarioC> lista;
		
		DatoListaUsuarioEng dao = new DatoListaUsuarioEng(conexion);
		lista = dao.getDatosUsuarioLista(tipo, userId);
		
		return lista;
		
	}
}
