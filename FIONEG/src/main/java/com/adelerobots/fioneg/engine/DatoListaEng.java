package com.adelerobots.fioneg.engine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.DatoListaC;

public class DatoListaEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<DatoListaC>{
	String conexion;
	
	public DatoListaEng(String conexion) {
		super(conexion);
		this.conexion=conexion;
	}
	
	public DatoListaC getDatoLista(Integer cnDatoLista) {
		List<Criterion> lstCriterios = new ArrayList<Criterion>();
		if (cnDatoLista != null) {
			lstCriterios.add(Restrictions.eq("cnDatoLista", cnDatoLista));
		}
		DatoListaC datoLista = this.findByCriteriaUniqueResult(lstCriterios);
		return datoLista;
	}
	
	/**
	 * Crea un  DatoLista
	 * @param datoLista DatoLista
	 * @return
	 */
	public DatoListaC create(final DatoListaC datoLista) {	
		this.insert(datoLista);
		this.flush();
		//this.refresh(datoLista);
		return datoLista;
	}	
}
