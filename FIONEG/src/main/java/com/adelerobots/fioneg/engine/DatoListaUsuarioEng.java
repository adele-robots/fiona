package com.adelerobots.fioneg.engine;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.DatoListaUsuarioC;

public class DatoListaUsuarioEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<DatoListaUsuarioC>{
	String conexion;
	
	public DatoListaUsuarioEng(String conexion) {
		super(conexion);
		this.conexion=conexion;
	}
	
	public List<DatoListaUsuarioC> getDatosUsuarioLista(Integer tipo, Integer usuario){
		Criteria criteria = getCriteria();
		
//		criteria.createAlias("usuario", "usuarioc");
//		criteria.createAlias("usuarioc.lstSparkc", "usuarioSpark");
		
		criteria.add(Restrictions.eq("cnTipo", tipo));
		criteria.add(Restrictions.eq("cnUsuario", usuario));
//		criteria.add(Restrictions.like("usuarioc.dcmail", "@gmail.com"));
			
		return criteria.list(); 
	}
}
