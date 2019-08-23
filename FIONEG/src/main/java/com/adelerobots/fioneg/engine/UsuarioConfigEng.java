package com.adelerobots.fioneg.engine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.opinion.OpinionC;
import com.adelerobots.fioneg.entity.usuarioconfig.UsuarioConfigC;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.persistencia.dao.HibernateAnnotationsUtil;

public class UsuarioConfigEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<UsuarioConfigC> {

	public UsuarioConfigEng(String dataSource) {
		super(dataSource);
	}


	/**
	 * Crea una nueva configuracion para el usuario persistiendo inmediatamente los datos en BBDD
	 * @param usuarioConfig Configuracion a crear
	 * @return
	 */
	public UsuarioConfigC create(final UsuarioConfigC usuarioConfig) {	
		this.insert(usuarioConfig);
		this.flush();
		this.refresh(usuarioConfig);
		return usuarioConfig;
	}
	
	
	/**
	 * Método que devolverá todas las configuraciones asociados a un usuario
	 * 
	 * @param intCodUsuario Identificador único de usuario
	 * 
	 * @return Lista de todas las configuraciones asociados al usuario
	 * cuyo identificador se pasa como parámetro
	 */
	public List<UsuarioConfigC> getAllUserConfigs(Integer intCodUsuario){
		List <Criterion>lstCriterios = new ArrayList<Criterion>();
		
		lstCriterios.add(Restrictions.eq("intCodUsuario", intCodUsuario));
		
		return this.findByCriteria(lstCriterios);
	}
	
	
	/**
	 * Método que devuelve el registro de la tabla usuarioconfig
	 * que relaciona la configuracion y el usuario pasados como parámetro
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @param intCodConfig Identificador de la configuracion
	 * 
	 * @return Se devuelve el registro correspondiente al usuario
	 * y a la configuracion o null en caso de no encontrarlo
	 */
	public UsuarioConfigC findUsuarioConfig(Integer intCodUsuario, Integer intCodConfig){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("intCodUsuario", intCodUsuario));
		criteria.add(Restrictions.eq("intCodConfig", intCodConfig));	
		
		
		return (UsuarioConfigC)criteria.uniqueResult();
	}
	
	/**
	 * Método que borrará todos las configuraciones de un usuario
	 * 
	 * @param intCodUsuario Identificador único del usuario a borrar
	 */
	public void deleteUsuarioConfigByUser(Integer intCodUsuario){
		String query = "delete from UsuarioConfig where intCodUsuario = :intCodUsuario";
		
		Session sess = HibernateAnnotationsUtil.getSession(	Constantes.CTE_JNDI_DATASOURCE,"FIONEG");
		sess.createQuery(query).setInteger("intCodUsuario", intCodUsuario).executeUpdate();			
		
	}
	

}
