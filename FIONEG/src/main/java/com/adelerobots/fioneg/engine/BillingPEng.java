package com.adelerobots.fioneg.engine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.BillingPC;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.persistencia.dao.HibernateAnnotationsUtil;


/**
 * Clase que mapea las operaciones con hibernate
 * para la tabla "billingp" de la base de datos
 * 
 * @author adele
 *
 */
public class BillingPEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<BillingPC>{
	
	String conexion;
	
	public BillingPEng(String conexion) {
		super(conexion);
		this.conexion=conexion;
	}	
	
	// ################################################################################################ 
	// ########################### Equivalencias con TransactionDEng ##################################
	// ################################################################################################
	
	/**
	 * Método que devuelve para un usuario sus billingP pendientes de notificación para una fecha dada
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @param calRenovacion Fecha de renovación esperada
	 * @return
	 */
	public List<BillingPC> getNotNotifiedBillingPByUser(Integer intCodUsuario, Calendar calRenovacion){
		Criteria criteria = getCriteria();
		
		List <BillingPC> lstBillingPs = new ArrayList<BillingPC>();
		
		// Spark no renovado
		criteria.add(Restrictions.eq("chRenewed", '0'));
		// Email no enviado
		criteria.add(Restrictions.eq("chEmailSent", '0'));
		// Spark no pagado
		criteria.add(Restrictions.eq("chPaid", '0'));
		// Spark no cobrado
		criteria.add(Restrictions.eq("chCharged", '0'));
		// El día,mes y año de la fecha de renovación debería ser el pasado como parámetro	
		Integer numMesSql = calRenovacion.get(Calendar.MONTH) + 1;
		criteria.add(Restrictions.sqlRestriction("month(fe_renovation) = " + numMesSql));
		criteria.add(Restrictions.sqlRestriction("day(fe_renovation) = " + calRenovacion.get(Calendar.DAY_OF_MONTH)));
		criteria.add(Restrictions.sqlRestriction("year(fe_renovation) = " + calRenovacion.get(Calendar.YEAR)));
		// El usuario debe coincidir con el pasado como parámetro
		criteria.createAlias("avatarSpark","avatarSpark");
		criteria.createAlias("avatarSpark.avatar","avatar");
		criteria.createAlias("avatar.usuario","usuario");
		criteria.add(Restrictions.eq("usuario.cnUsuario", intCodUsuario));
		
		lstBillingPs = criteria.list();
		
		return lstBillingPs;
	}
	
	/**
	 * Método que devuelve para un usuario sus transacciones pendientes de cobro pero notificadas para una fecha dada
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @param calRenovacion Fecha de renovación esperada
	 * @return
	 */
	public List<BillingPC> getNotifiedButNotChargedBillingPByUser(Integer intCodUsuario, Calendar calRenovacion){
		Criteria criteria = getCriteria();
		
		List <BillingPC> lstBillingPs = new ArrayList<BillingPC>();
		
		// Spark no renovado
		criteria.add(Restrictions.eq("chRenewed", '0'));
		// Email no enviado
		criteria.add(Restrictions.eq("chEmailSent", '1'));
		// Spark no pagado
		criteria.add(Restrictions.eq("chPaid", '0'));
		// Spark no cobrado
		criteria.add(Restrictions.eq("chCharged", '0'));
		// El día,mes y año de la fecha de renovación debería ser el pasado como parámetro	
		Integer numMesSql = calRenovacion.get(Calendar.MONTH) + 1;
		criteria.add(Restrictions.sqlRestriction("month(fe_renovation) = " + numMesSql));
		criteria.add(Restrictions.sqlRestriction("day(fe_renovation) = " + calRenovacion.get(Calendar.DAY_OF_MONTH)));
		criteria.add(Restrictions.sqlRestriction("year(fe_renovation) = " + calRenovacion.get(Calendar.YEAR)));
		// El usuario debe coincidir con el pasado como parámetro
		criteria.createAlias("avatarSpark","avatarSpark");
		criteria.createAlias("avatarSpark.avatar","avatar");
		criteria.createAlias("avatar.usuario","usuario");
		criteria.add(Restrictions.eq("usuario.cnUsuario", intCodUsuario));
		
		lstBillingPs = criteria.list();
		
		return lstBillingPs;
	}
	
	/**
	 * Método que devuelve para un usuario desarrollador los sparks del mes que están siendo usado
	 * y por los que debemos pasarles un pago
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @param calRenovacion Fecha de renovación esperada
	 * @return
	 */
	public List<BillingPC> getNotPaidBillingPByUser(Integer intCodUsuario, Calendar calRenovacion){
		Criteria criteria = getCriteria();
		
		List <BillingPC> lstBillingPs = new ArrayList<BillingPC>();
		
		// Spark no renovado
		criteria.add(Restrictions.eq("chRenewed", '1'));
		// Email no enviado
		criteria.add(Restrictions.eq("chEmailSent", '1'));
		// Spark no pagado
		criteria.add(Restrictions.eq("chPaid", '0'));
		// Spark no cobrado
		criteria.add(Restrictions.eq("chCharged", '1'));
		// El día,mes y año de la fecha de renovación debería ser el pasado como parámetro	
		Integer numMesSql = calRenovacion.get(Calendar.MONTH) + 1;
		criteria.add(Restrictions.sqlRestriction("month(fe_renovation) = " + numMesSql));
		criteria.add(Restrictions.sqlRestriction("day(fe_renovation) = " + calRenovacion.get(Calendar.DAY_OF_MONTH)));
		criteria.add(Restrictions.sqlRestriction("year(fe_renovation) = " + calRenovacion.get(Calendar.YEAR)));
		// El usuario desarrollador del spark debe coincidir con el pasado como parámetro
		criteria.createAlias("avatarSpark","avatarSpark",CriteriaSpecification.INNER_JOIN);		
		criteria.createAlias("avatarSpark.spark","spark",CriteriaSpecification.INNER_JOIN);
		criteria.add(Restrictions.eq("spark.cnUsuarioDesarrollador", intCodUsuario));
		
		
		lstBillingPs = criteria.list();
		
		return lstBillingPs;
	}
	
	/**
	 * Método que devuelve la cantidad de cada spark desarrollado por un usuario asociada al precio total
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @param porcentaje Porcentaje a aplicar al precio inicial
	 * 
	 * @return Se devuelve una lista de arrays de objetos de la forma [nombreSpark, sumatorioPrecio, cantidad]
	 */
	public List<Object[]> getBillingPDeveloperGroupBySparks(Integer intCodUsuario, Float porcentaje, Calendar calRenovacion){
		// El día,mes y año de la fecha de renovación debería ser el pasado como parámetro	
		Integer numMesSql = calRenovacion.get(Calendar.MONTH) + 1;		
		
		String sql = "SELECT s.dc_nombre, SUM((nu_amount*?)/100), CAST(COUNT(b.cn_spark) AS UNSIGNED) "
		+ "FROM billingp b "
		+ "INNER JOIN avatarspark avsp ON avsp.cn_avatar = b.cn_avatar AND avsp.cn_spark = b.cn_spark "		 
		+ "INNER JOIN avatar av ON av.cn_avatar = b.cn_avatar "
		+ "INNER JOIN spark s ON s.cn_spark = b.cn_spark " 
		+ "WHERE s.cn_usuarioDesarrollador = ? AND av.fl_activo = '1' "
		+ "AND b.fl_renewed = '1' " 
		+ "AND b.fl_emailsent = '1' " 
		+ "AND b.fl_paid = '1' " 
		+ "AND b.fl_charged = '1' " 
		+ "AND b.fl_renewed = '1' " 
		+ "AND month(fe_renovation) = ? "
		+ "AND day(fe_renovation) = ? " 
		+ "AND year(fe_renovation) = ? "
		+ "GROUP BY b.cn_spark ";
			
		final Session session = HibernateAnnotationsUtil.getSession(this.conexion, Constantes.CTE_APP_FIO);
		final SQLQuery query = session.createSQLQuery(sql);
		query.setFloat(0, porcentaje);		
		query.setInteger(1, intCodUsuario);
		query.setInteger(2, numMesSql);
		query.setInteger(3, calRenovacion.get(Calendar.DAY_OF_MONTH));
		query.setInteger(4, calRenovacion.get(Calendar.YEAR));
		
		return query.list();
	}
	

}
