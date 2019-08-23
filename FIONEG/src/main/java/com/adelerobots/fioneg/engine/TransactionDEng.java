package com.adelerobots.fioneg.engine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.TransactionDC;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.persistencia.dao.HibernateAnnotationsUtil;


/**
 * Clase que mapea las operaciones con hibernate
 * para la tabla "transactiond" de la base de datos
 * 
 * @author adele
 *
 */
public class TransactionDEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<TransactionDC>{
	
	String conexion;
	
	public TransactionDEng(String conexion) {
		super(conexion);
		this.conexion=conexion;
	}
	
	/**
	 * Método que devuelve para un usuario sus transacciones pendientes de notificación para una fecha dada
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @param calRenovacion Fecha de renovación esperada
	 * @return
	 */
	public List<TransactionDC> getNotNotifiedTransactionsByUser(Integer intCodUsuario, Calendar calRenovacion){
		Criteria criteria = getCriteria();
		
		List <TransactionDC> lstTransactions = new ArrayList<TransactionDC>();
		
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
		criteria.createAlias("usuarioSpark","usuarioSpark");
		criteria.add(Restrictions.eq("usuarioSpark.intCodUsuario", intCodUsuario));
		
		lstTransactions = criteria.list();
		
		return lstTransactions;
	}
	
	/**
	 * Método que devuelve para un usuario sus transacciones pendientes de cobro pero notificadas para una fecha dada
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @param calRenovacion Fecha de renovación esperada
	 * @return
	 */
	public List<TransactionDC> getNotifiedButNotChargedTransactionsByUser(Integer intCodUsuario, Calendar calRenovacion){
		Criteria criteria = getCriteria();
		
		List <TransactionDC> lstTransactions = new ArrayList<TransactionDC>();
		
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
		criteria.createAlias("usuarioSpark","usuarioSpark");
		criteria.add(Restrictions.eq("usuarioSpark.intCodUsuario", intCodUsuario));
		
		lstTransactions = criteria.list();
		
		return lstTransactions;
	}
	
	/**
	 * Método que devuelve para un usuario desarrollador los sparks del mes que están siendo usado
	 * y por los que debemos pasarles un pago
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @param calRenovacion Fecha de renovación esperada
	 * @return
	 */
	public List<TransactionDC> getNotPaidTransactionsByUser(Integer intCodUsuario, Calendar calRenovacion){
		Criteria criteria = getCriteria();
		
		List <TransactionDC> lstTransactions = new ArrayList<TransactionDC>();
		
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
		criteria.createAlias("usuarioSpark","usuarioSpark",CriteriaSpecification.INNER_JOIN);
		//criteria.createAlias("usuarioSpark.usuarioSparkPk","usuarioSparkPk",CriteriaSpecification.INNER_JOIN);
		criteria.createAlias("usuarioSpark.spark","spark",CriteriaSpecification.INNER_JOIN);
		criteria.add(Restrictions.eq("spark.cnUsuarioDesarrollador", intCodUsuario));
		
		
//		criteria.createCriteria("usuarioSpark").setFetchMode("spark", FetchMode.JOIN)
//		.add(Restrictions.eq("usuarioSparkPk.spark.cnUsuarioDesarrollador", intCodUsuario));
		
		//criteria.createCriteria("usuarioSpark").createCriteria("spark")
		//.add(Restrictions.eq("cnUsuarioDesarrollador", intCodUsuario));
		
		
		lstTransactions = criteria.list();
		
		return lstTransactions;
	}
	
	/**
	 * Método que devuelve la cantidad de cada spark desarrollado por un usuario asociada al precio total
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @param porcentaje Porcentaje a aplicar al precio inicial
	 * 
	 * @return Se devuelve una lista de arrays de objetos de la forma [nombreSpark, sumatorioPrecio, cantidad]
	 */
	public List<Object[]> getTransactionsDeveloperGroupBySparks(Integer intCodUsuario, Float porcentaje, Calendar calRenovacion){
		// El día,mes y año de la fecha de renovación debería ser el pasado como parámetro	
		Integer numMesSql = calRenovacion.get(Calendar.MONTH) + 1;
		String sql = "SELECT s.dc_nombre, SUM((nu_amount*?)/100), CAST(COUNT(t.cn_spark) AS UNSIGNED)"
			+"FROM transactiond t "
			+"INNER JOIN usuario u ON u.cn_usuario = t.cn_usuario "
			+"INNER JOIN spark s ON s.cn_spark = t.cn_spark "
			+"WHERE s.cn_usuarioDesarrollador = ? "
			+"AND t.fl_renewed = '1' "
			+"AND t.fl_emailsent = '1' "
			+"AND t.fl_paid = '1' "
			+"AND t.fl_charged = '1' "
			+"AND t.fl_renewed = '1' "
			+"AND month(fe_renovation) = ? "
			+"AND day(fe_renovation) = ? "
			+"AND year(fe_renovation) = ? " 
			+"GROUP BY t.cn_spark";
			
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
