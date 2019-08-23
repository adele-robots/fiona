package com.adelerobots.fioneg.engine;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.entity.opinion.OpinionC;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.persistencia.dao.HibernateAnnotationsUtil;

public class SparkEng extends com.treelogic.fawna.arq.negocio.persistencia.datos.QueryEngine<SparkC> {
	
	String conexion;
	
	public SparkEng(String conexion) {
		super(conexion);
		this.conexion=conexion;
	}

	public SparkC getSpark(Integer cnSpark) {
		List<Criterion> lstCriterios = new ArrayList<Criterion>();
		if (cnSpark != null) {
			lstCriterios.add(Restrictions.eq("cnSpark", cnSpark));
		}
		SparkC spark = this.findByCriteriaUniqueResult(lstCriterios);
		return spark;
	}
	
	
	/**
	 * Método que devuelve un spark utilizando su nombre
	 * como parámetro de búsqueda
	 * 
	 * @param strNombre Nombre del spark buscado
	 * @return Se devolverá el spark cuyo nombre coincida
	 * con el pasado como parámetro o null en caso contrario
	 */
	public SparkC getSpark(String strNombre){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("strNombre", strNombre));
		
		return (SparkC)criteria.uniqueResult();
	}
	
	/**
	 * Crea un nuevo spark persistiendo inmediatamente los datos en BBDD
	 * @param spark spark a crear
	 * @return
	 */
	public SparkC create(final SparkC spark) {	
		this.insert(spark);
		this.flush();
		//this.refresh(spark);
		return spark;
	}
	
	/**
	 * Método que devuelve todos los elementos de tipo "Spark"
	 * existentes en la base de datos
	 * 
	 * @return Lista con todos los sparks de base de datos
	 */
	public List<SparkC> getAllSparks(){
		return this.findAll();
	}
	
		
		
	/**
	 * Método que devolverá todos los sparks gratuitos o
	 * todos los sparks de pago en función del parámetro
	 * pasado
	 * 
	 * @param intIsFree parámetro que indica si se quieren
	 * obtener todos los sparks gratuitos, o todos los de pago
	 * @return
	 */
	public List<SparkC> getAllSparksByPrice(Integer intIsFree){
		List <Criterion>lstCriterios = new ArrayList<Criterion>();
		
		lstCriterios.add(Restrictions.eq("chBorrado", '0'));
		
		if(intIsFree.equals(0))
			lstCriterios.add(Restrictions.gt("flPrecioProduccion", new Float(0)));
		else
			lstCriterios.add(Restrictions.eq("flPrecioProduccion", new Float(0)));
		
		
		return this.findByCriteria(lstCriterios);
		
		
	}
	
	/**
	 * Método que devuelve los N últimos sparks publicados,
	 * gratuitos o de pago en función del parámetro
	 * 
	 * @param intCodSatus Identificador único del estado
	 * @return Se devolverá la lista de sparks correspondiente
	 * a los parámetros de búsqueda
	 */
	@SuppressWarnings("unchecked")
	public List<SparkC> getLastStatusOrderedByDate(Integer intCodStatus){		

		
			String query = "SELECT sp.* ";
			query += 		"FROM fionadb.spark sp ";
			query += 		"INNER JOIN fionadb.sparkstatus st ON sp.CN_spark = st.CN_spark ";		
			query += 		"INNER JOIN ";
			query += 		"(SELECT max(FE_Cambiada) as fechaMaxima, CN_spark ";
			query += 		"FROM fionadb.sparkstatus ";
			query +=		"GROUP BY CN_spark) maxresults ON st.CN_spark = maxresults.CN_spark  ";			
			query += 		" AND st.FE_Cambiada= maxresults.fechaMaxima AND CN_status = " + intCodStatus;
//			if(isFree.equals(1))
//				query +=		" WHERE sp.NU_PrecioProduccion = " + new Float(0);
//			else
//				query +=		" WHERE sp.NU_PrecioProduccion > " + new Float(0);
			query +=		" ORDER BY st.FE_Cambiada desc ";	
			
			
			
			
			Session sess = HibernateAnnotationsUtil.getSession(	Constantes.CTE_JNDI_DATASOURCE,"FIONEG");
			SQLQuery sqlQuery = sess.createSQLQuery(query);
			sqlQuery.addEntity(SparkC.class);			
			sqlQuery.setMaxResults(9);
			
			List<SparkC> sparks = sqlQuery.list();
			
			// ------------------------------------------------------------
			
//			Criteria criteria = getCriteria();
//			criteria.add(Restrictions.eq("chBorrado",'0'));
			/*
			if(isFree.equals(1))
				criteria.add(Restrictions.eq("flPrecioProduccion", new Float(0)));
			else
				criteria.add(Restrictions.gt("flPrecioProduccion", new Float(0)));
				*/			
//			criteria.createCriteria("lstSparkStatusC").add(Restrictions.eq("intCodStatus",intCodStatus))
//														.addOrder(Order.desc("dateCambio")).setMaxResults(9);
			
			
			
			return sparks;
			//return criteria.list();
			
	}
	
	/**
	 * Método que devuelve los N sparks más vendidos,
	 * gratuitos o de pago en función del parámetro
	 *
	 * @return Se devolverá la lista de sparks correspondiente
	 * a los parámetros de búsqueda
	 */
	@SuppressWarnings("unchecked")
	public List<SparkC> getTopSaleSparks(){

		
			String query = "SELECT DISTINCT(sp.CN_spark),sp.* ";
			query += 		"FROM fionadb.spark sp ";
			query += 		"INNER JOIN fionadb.usuariospark us ON sp.CN_spark = us.CN_spark ";		
			query += 		"INNER JOIN ";
			query += 		"(SELECT COUNT(CN_usuario) as num_usuarios, CN_spark ";
			query += 		"FROM fionadb.usuariospark ";
			query +=		"GROUP BY CN_spark) resultsnumusuarios ON sp.CN_spark = resultsnumusuarios.CN_spark  ";
			query += 		"INNER JOIN fionadb.sparkstatus st ON sp.CN_spark = st.CN_spark ";	
			query += 		"INNER JOIN ";
			query += 		"(SELECT max(FE_Cambiada) as fechaMaxima, CN_spark ";
			query += 		"FROM fionadb.sparkstatus ";
			query +=		"GROUP BY CN_spark) maxresults ON st.CN_spark = maxresults.CN_spark  ";			
			query += 		" AND st.FE_Cambiada= maxresults.fechaMaxima AND CN_status = 6";
//			if(isFree.equals(1))
//				query +=		" WHERE sp.NU_PrecioProduccion = " + new Float(0);
//			else
//				query +=		" WHERE sp.NU_PrecioProduccion > " + new Float(0);
			
			query += 		" AND sp.fl_borrado = '0' ";
			query +=		" ORDER BY resultsnumusuarios.num_usuarios desc ";    
			                             
			
			
			
			Session sess = HibernateAnnotationsUtil.getSession(	Constantes.CTE_JNDI_DATASOURCE,"FIONEG");
			SQLQuery sqlQuery = sess.createSQLQuery(query);
			sqlQuery.addEntity(SparkC.class);			
			sqlQuery.setMaxResults(9);
			
			
			List<SparkC> sparks = sqlQuery.list();		
			
			
			
			return sparks;
			
	}	
	
	
	/**
	 * Método que permite buscar los sparks por palabras clave 
	 * 	
	 * @param keywords Lista de palabras clave de búsqueda
	 * @return Devuelve la lista de sparks que coincidan con
	 * las palabras clave pasadas como parámetro
	 */
	@SuppressWarnings("unchecked")
	public List<SparkC> findSparksByKeywords(String[] keywords){		
		List <SparkC> lstSparks = new ArrayList<SparkC>();
		
		if(keywords.length > 0){
//			String query = "SELECT DISTINCT(sp.CN_spark),sp.* ";
//			query += 	   "FROM spark sp ";
//			query += 	   "INNER JOIN sparkkeyword sk ON sk.cn_spark = sp.cn_spark ";
//			query +=	   "INNER JOIN keyword k ON k.cn_keyword = sk.cn_keyword ";			
//			query += 	   "WHERE ";
//			query += 	   " k.dc_descripcion like '" + keywords[0] + "' ";
//			for(int i = 1; i < keywords.length; i++){
//				query +=   " OR k.dc_descripcion like '" + keywords[i] + "' ";
//			}	
//			
//			Session sess = HibernateAnnotationsUtil.getSession(	Constantes.CTE_JNDI_DATASOURCE,"FIONEG");
//			SQLQuery sqlQuery = sess.createSQLQuery(query);
//			sqlQuery.addEntity(SparkC.class);
//			
//			lstSparks = sqlQuery.list();
			
			// -------------------------------------------------------------------------
			
			// Campos a tener en cuenta durante la búsqueda
			// 1. Nombre
			// 2. Descripción
			// 3. Descripción corta
			// 4. Keywords
			// 5. Otros keywords
			
			
			Criteria criteria = getCriteria();
			
			criteria.add(Restrictions.eq("chBorrado", '0'));
			
			//criteria.createCriteria("lstKeywords").add(Restrictions.in("strDescripcion",keywords));
			// Conjunto de OR
			Disjunction disjunctionKeywords = Restrictions.disjunction();
			Disjunction disjunctionOtros = Restrictions.disjunction();
			
			
			criteria.createAlias("lstKeywords", "lstKeywords");
			
			for(int i = 0; i < keywords.length; i++){
				disjunctionKeywords.add(Restrictions.like("lstKeywords.strDescripcion", keywords[i]));
				disjunctionOtros.add(Restrictions.ilike("strOtrosKeywords", "%" + keywords[i]+"%"));
				disjunctionOtros.add(Restrictions.ilike("strNombre", "%" + keywords[i]+"%"));				
				//try {
					//byte [] bDesc = keywords[i].getBytes("UTF8");
					//disjunctionOtros.add(Restrictions.ilike("strDescripcion", keywords[i],MatchMode.ANYWHERE));
				
				//disjunctionOtros.add(Expression.sql("DBMS_LOB.COMPARE(COLUMN_NAME,DC_Descripcion)=0"));
				disjunctionOtros.add(Expression.sql("lower(this_.dc_descripcion) like '%" + keywords[i] +"%'"));
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				disjunctionOtros.add(Restrictions.ilike("strDescripcionCorta", "%" + keywords[i]+"%"));
			}
						
						
			//criteria.createCriteria("lstKeywords").add(disjunctionKeywords);
			
			criteria.add(Restrictions.or(disjunctionKeywords, disjunctionOtros));
			
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			
			
			lstSparks = criteria.list();
			
		}
			
		return lstSparks;
		
	}
	
	/**
	 * Método que obtiene las n opiniones más recientes
	 * asociadas a un spark
	 * 
	 * @param intCodSpark Identificador único del spark
	 * @param numOpinions Número de opiniones que se quieren recuperar
	 * Si este parámetro es nulo, se obtienen todas las opiniones
	 * asociadas al spark
	 * 
	 * @return Lista de opiniones encontradas
	 */ 
	@SuppressWarnings("unchecked")
	public List<OpinionC> getNSparkOpinions(Integer intCodSpark, Integer numOpinions){
		List<OpinionC> lstOpiniones = new ArrayList<OpinionC>();
		
		String query = 	" SELECT DISTINCT(op.cn_opinion), op.* ";
		query +=		" FROM spark sp ";
		query += 		" INNER JOIN usuariospark usp ON usp.cn_spark = sp.cn_spark ";
		query += 		" INNER JOIN opinion op ON op.cn_spark = usp.cn_spark ";
		query += 		" WHERE sp.cn_spark = " + intCodSpark;	
		query +=		" AND op.dc_descripcion IS NOT NULL ";
		query +=		" ORDER BY op.FE_enviada desc ";
		
		Session sess = HibernateAnnotationsUtil.getSession(	Constantes.CTE_JNDI_DATASOURCE,"FIONEG");
		SQLQuery sqlQuery = sess.createSQLQuery(query);
		sqlQuery.addEntity(OpinionC.class);
		
		if( numOpinions != null )
			sqlQuery.setMaxResults(numOpinions);
		
		lstOpiniones = sqlQuery.list();
		
		return lstOpiniones;
	}
	
	/**
	 * Método que devuelve una lista con los sparks cuyos identificadores
	 * coincidan con los pasados como parámetro
	 * 
	 * @param ids Lista de identificadores
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SparkC> getSparks(List <Integer> ids){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("chBorrado", '0'));
		criteria.add(Restrictions.in("cnSpark", ids));
//		if(isFree.equals(1))
//			criteria.add(Restrictions.eq("flPrecioDesarrollo", new Float(0)));
//		else
//			criteria.add(Restrictions.gt("flPrecioDesarrollo", new Float(0)));
		
		criteria.setMaxResults(9);	
		
		return criteria.list();
	}
	
	/**
	 * Método que devolverá todos aquellos sparks cuyo usuario desarrollador
	 * coincida con el pasado como parámetro
	 * 
	 * @param intCodUsuarioDes Identificador único que representa al usuario
	 * desarrollador
	 * @return Se devolverá la lista de sparks correspondiente al usuario
	 * indicado
	 */
	@SuppressWarnings("unchecked")
	public List<SparkC> getListSparksByDeveloper(Integer intCodUsuarioDes){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("chBorrado", '0'));
		criteria.createCriteria("usuarioDesarrollador").add(Restrictions.eq("cnUsuario", intCodUsuarioDes));
		criteria.addOrder(Order.asc("strNombre"));
		
		return criteria.list();
	}
	
	/**
	 * Método que devolverá todos aquellos sparks cuyo usuario desarrollador
	 * coincida con el pasado como parámetro
	 * 
	 * @param intCodUsuarioDes Identificador del usuario desarrollador
	 * @param idsExcluidos Ids de los sparks que no queremos que aparezcan en el listado
	 * @param maxResults Número máximo de resultados
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SparkC> getListSparksByDeveloper(Integer intCodUsuarioDes, List<Integer> idsExcluidos, Integer maxResults){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("chBorrado", '0'));
		criteria.add(Restrictions.not(Restrictions.in("cnSpark", idsExcluidos)));
		criteria.createCriteria("usuarioDesarrollador").add(Restrictions.eq("cnUsuario", intCodUsuarioDes));
		criteria.addOrder(Order.asc("strNombre"));
		criteria.setMaxResults(maxResults);
		
		return criteria.list();
	}
	
	/**
	 * Devuelve un listado de sparks de un usuario
	 * que no han sido ocultados ni borrados
	 * 
	 * @param intCodUsuario Identificador único de usuario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SparkC> getNotHiddenSparks(Integer intCodUsuario){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("chBorrado", '0'));		
		criteria.createCriteria("lstUsuarioSparkC").add(Restrictions.eq("intCodUsuario",intCodUsuario)).add(Restrictions.eq("chHidden",'0'))
		.add(Restrictions.eq("chActivo", '1'));
		
		return criteria.list();
	}
	
	/**
	 * Método que devuelve todos los sparks de un determinado usuario
	 * que no hayan sido borrados
	 * 
	 * @param intCodUsuario Identificador único de usuario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SparkC> getAllUserSparks(Integer intCodUsuario){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.eq("chBorrado", '0'));		
		criteria.createCriteria("lstUsuarioSparkC").add(Restrictions.eq("intCodUsuario",intCodUsuario)).add(Restrictions.eq("chActivo", '1'));
		
		return criteria.list();
		
	}
	
	
	/**
	 * Devuelve todos los sparks no borrados cuyo último estado
	 * sea "Available"
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SparkC> getAllAvailableSparks(){
		
//		Criteria criteria = getCriteria();
//		DetachedCriteria maxDate = DetachedCriteria.forClass(SparkStatusC.class, "maxDate");		
//		ProjectionList projectionList = Projections.projectionList();
//		projectionList.add(Projections.max("dateCambio"));
//		projectionList.add(Projections.groupProperty("intCodSpark"));		
//		projectionList.add(Projections.property("intCodStatus"));
//		//maxDate.add(Restrictions.eqProperty("intCodSpark", "cnSpark"));		
//		maxDate.setProjection(projectionList);		
//		
//		DetachedCriteria listSparks = DetachedCriteria.forClass(SparkStatusC.class, "listSparks");
//		listSparks.add(Restrictions.eqProperty("listSparks.intCodSpark", "maxDate.intCodSpark"));
//		listSparks.add(Restrictions.eqProperty("listSparks.intCodStatus", "maxDate.intCodStatus"));		
//		listSparks.add(Restrictions.eq("intCodStatus", 6));
//		listSparks.setProjection(Projections.property("intCodSpark"));
//		
//		
//		// Sparks no borrados
//		criteria.add(Restrictions.eq("chBorrado", '0'));
//		criteria.add(Subqueries.propertyIn("cnSpark", listSparks));		
		
		
		// Sparks cuyo último status sea "Available"
		//criteria.createCriteria("lstSparkStatusC").addOrder(Order.desc("dateCambio")).
			//										add(Restrictions.eq("intCodStatus",6));
		
		
//		return criteria.list();
		
		
		String query = "SELECT DISTINCT(sp.CN_spark),sp.* ";
		query += 		"FROM fionadb.spark sp ";		
		query += 		"INNER JOIN fionadb.sparkstatus st ON sp.CN_spark = st.CN_spark ";	
		query += 		"INNER JOIN ";
		query += 		"(SELECT max(FE_Cambiada) as fechaMaxima, CN_spark ";
		query += 		"FROM fionadb.sparkstatus ";
		query +=		"GROUP BY CN_spark) maxresults ON st.CN_spark = maxresults.CN_spark  ";			
		query += 		" AND st.FE_Cambiada= maxresults.fechaMaxima AND CN_status = 6";
//		if(isFree.equals(1))
//			query +=		" WHERE sp.NU_PrecioProduccion = " + new Float(0);
//		else
//			query +=		" WHERE sp.NU_PrecioProduccion > " + new Float(0);
		
		query += 		" AND sp.fl_borrado = '0' ";
		query +=		" ORDER BY st.FE_Cambiada desc ";    
		                             
				
		Session sess = HibernateAnnotationsUtil.getSession(Constantes.CTE_JNDI_DATASOURCE,"FIONEG");
		SQLQuery sqlQuery = sess.createSQLQuery(query);
		sqlQuery.addEntity(SparkC.class);		
		
		
		List<SparkC> sparks = sqlQuery.list();	
		
		
		return sparks;
	}
	
	/**
	 * Método que devolverá una serie de banners de sparks aleatorios 
	 * 
	 * @param intNumBanners Número de banners que se buscan
	 * @return
	 */
	public List<SparkC> getRandomBanners(Integer intNumBanners){
		Criteria criteria = getCriteria();
		
		criteria.add(Restrictions.isNotNull("strBanner"));
		criteria.add(Restrictions.ne("strBanner", ""));
		criteria.add(Restrictions.sqlRestriction("1=1 order by rand()"));
		criteria.setMaxResults(intNumBanners);		
		
		return criteria.list();
	}
	
		
	
}
