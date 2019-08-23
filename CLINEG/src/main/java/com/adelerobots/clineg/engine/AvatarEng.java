package com.adelerobots.clineg.engine;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.clineg.entity.AvatarC;
import com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine;

public class AvatarEng extends QueryEngine<AvatarC> {
	
	String conexion;

	public AvatarEng(String conexion) {		
		super(conexion);
		this.conexion = conexion;		
	}
	
	/**
	 * Método que devuelve la lista de avatares activos para un usuario
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @return
	 */
	public List<AvatarC> getActiveAvatarsByUser(Integer intCodUsuario){
		Criteria criteria = getCriteria();
		criteria.createAlias("usuario", "usuario");
		
		criteria.add(Restrictions.eq("usuario.cnUsuario", intCodUsuario));
		
		List <AvatarC> lstActiveAvatars = criteria.list();
		
		return lstActiveAvatars;
	}
	
		

}
