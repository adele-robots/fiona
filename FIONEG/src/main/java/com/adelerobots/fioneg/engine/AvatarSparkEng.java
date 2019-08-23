package com.adelerobots.fioneg.engine;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.UserStatusEnum;
import com.adelerobots.fioneg.entity.avatarspark.AvatarSparkC;



public class AvatarSparkEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<AvatarSparkC> {

	public AvatarSparkEng(String dataSource) {
		super(dataSource);
	}

	
		
	
	/**
	 * Método que devuelve los sparks activos que deben cobrarse por periodos
	 * de tiempo y no por uso
	 * 
	 * @return
	 */
	public List<AvatarSparkC> getTimeRenewableProductionAvatarSparks(){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.isNotNull("floPrice"));
		// Corregido el día 25-09-2013 porque si seleccionamos sólo aquellos cuyo precio
		// sea distinto de cero no se cobrará el hosting de los avatares que estén compuestos
		// por sparks gratuitos
		//criteria.add(Restrictions.ne("floPrice",new Float(0)));
		
		criteria.createAlias("avatar", "avatar");
		
		criteria.add(Restrictions.eq("avatar.chActivo", '1'));
		
		// Añadido el día 21-11-2013 para evitar planificar
		// para producción los sparks de usuarios de TCRF
		criteria.createAlias("avatar.usuario","usuario");
		criteria.add(Restrictions.ne("usuario.strStatus", UserStatusEnum.TCRF.text));
				
		
		List <AvatarSparkC> lstAvatarSpark = criteria.list();
		
		return lstAvatarSpark;
	}
	
	/**
	 * Método que devuelve los sparks activos que deben cobrarse por periodos
	 * de tiempo y no por uso para los usuarios de TCRF
	 * 
	 * @return
	 */
	public List<AvatarSparkC> getTimeRenewableTCRFAvatarSparks(){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.isNotNull("floPrice"));
		// Corregido el día 25-09-2013 porque si seleccionamos sólo aquellos cuyo precio
		// sea distinto de cero no se cobrará el hosting de los avatares que estén compuestos
		// por sparks gratuitos
		//criteria.add(Restrictions.ne("floPrice",new Float(0)));
		
		criteria.createAlias("avatar", "avatar");
		
		criteria.add(Restrictions.eq("avatar.chActivo", '1'));
		
		// Añadido el día 21-11-2013 para evitar planificar
		// para producción los sparks de usuarios de TCRF
		criteria.createAlias("avatar.usuario","usuario");
		
		criteria.add(Restrictions.or(
				Restrictions.eq("usuario.strStatus", UserStatusEnum.TCRF.text),
				Restrictions.eq("usuario.strStatus", UserStatusEnum.TCRF_FREE.text)
				)
			);
		
		List <AvatarSparkC> lstAvatarSpark = criteria.list();
		
		return lstAvatarSpark;
	}
	
	/**
	 * Método que devuelve los sparks desarrollados por un determinado usuario
	 * que están siendo usados por otros usuarios en desarrollo
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @return
	 */
	public List<AvatarSparkC> getActiveUsedSparksDevelopedByUser(Integer intCodUsuario){
		Criteria criteria = getCriteria();
		criteria.createAlias("avatar", "avatar");		
		criteria.createAlias("spark","spark");
		criteria.createAlias("spark.usuarioDesarrollador","usuarioDesarrollador");
				
		// El usuario que ha desarrollado el spark debe ser el pasado como parámetro
		criteria.add(Restrictions.eq("usuarioDesarrollador.cnUsuario", intCodUsuario));
		// El avatar al que pertenezca el spark debe estar activo para el usuario que lo esté usando
		criteria.add(Restrictions.eq("avatar.chActivo", '1'));
		
		List <AvatarSparkC> lstAvatarSpark = criteria.list();
		
		return lstAvatarSpark;
	}
	
	
	

}
