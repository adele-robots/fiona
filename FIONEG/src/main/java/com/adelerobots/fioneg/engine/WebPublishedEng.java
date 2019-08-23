package com.adelerobots.fioneg.engine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.WebPublishedC;

public class WebPublishedEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<WebPublishedC>{

		
	public WebPublishedEng(String dataSource) {
		super(dataSource);		
	}
	
	/**
	 * Método que permite añadir una entrada en la tabla 'webpublished'
	 * 
	 * @param webpublished Entidad que representa el contenido de una
	 * entrada de la tabla
	 * 
	 * @return Se devuelven los datos de la entidad que se acaba de
	 * insertar
	 */
	public WebPublishedC addWebPublished(WebPublishedC webPublished) {
		this.persist(webPublished);
		this.flush();
		this.refresh(webPublished);		
		return webPublished;
	}
	
	
	/**
	 * Método que elimina una entrada de la tabla 'webpublished'
	 * 
	 * @param userid Identificador único del usuario cuyos datos
	 * buscamos
	 */
	public void removeWebPublished(Integer intCodUsuario){
		List<Criterion> lstCriterios = new ArrayList<Criterion>();
		lstCriterios.add(Restrictions.eq("cnUsuario", intCodUsuario));
				
		WebPublishedC webpublished = new WebPublishedC();
		webpublished = this.findByCriteriaUniqueResult(lstCriterios);
		
		this.delete(webpublished);		
	}
	
	
	/**
	 * Comprobar si un usuario tiene o no una entrada en la tabla
	 * 'webpublished'
	 * 
	 * @param intCodUsuario Idetificador único del usuario cuya información
	 * buscamos
	 * 
	 * @return Se devolverá una entidad 'webpublished' si el usuario
	 * tiene publicado el scriplet, y null en caso contrario
	 * 
	 */
	public WebPublishedC getWebPublished(Integer intCodUsuario){	
		Criteria criteria = getCriteria();
		criteria.createAlias("usuario", "usuario");
		
		criteria.add(Restrictions.eq("usuario.cnUsuario", intCodUsuario));
		
		
		return (WebPublishedC)criteria.uniqueResult();		
				
	}
	
	
}
