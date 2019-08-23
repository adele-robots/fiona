package com.adelerobots.fioneg.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.adelerobots.fioneg.engine.CrashEng;
import com.adelerobots.fioneg.engine.EulaEng;
import com.adelerobots.fioneg.engine.HostingEng;
import com.adelerobots.fioneg.engine.InterfazEng;
import com.adelerobots.fioneg.engine.KeywordEng;
import com.adelerobots.fioneg.engine.OpinionEng;
import com.adelerobots.fioneg.engine.RejectionEng;
import com.adelerobots.fioneg.engine.SparkEng;
import com.adelerobots.fioneg.engine.SparkStatusEng;
import com.adelerobots.fioneg.engine.StatusEng;
import com.adelerobots.fioneg.engine.UsuarioEng;
import com.adelerobots.fioneg.engine.UsuarioSparkEng;
import com.adelerobots.fioneg.entity.EulaC;
import com.adelerobots.fioneg.entity.HostingC;
import com.adelerobots.fioneg.entity.InterfazC;
import com.adelerobots.fioneg.entity.PriceC;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.entity.crash.CrashC;
import com.adelerobots.fioneg.entity.keyword.KeywordC;
import com.adelerobots.fioneg.entity.opinion.OpinionC;
import com.adelerobots.fioneg.entity.rejection.RejectionC;
import com.adelerobots.fioneg.entity.sparkstatus.SparkStatusC;
import com.adelerobots.fioneg.entity.sparkstatus.SparkStatusPk;
import com.adelerobots.fioneg.entity.status.StatusC;
import com.adelerobots.fioneg.entity.usuariospark.UsuarioSparkC;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IRegistro;
import com.treelogic.fawna.arq.negocio.core.RollbackException;

public class SparkManager {

	private String conexion;
	
	private static final int MONTHS = 1;
	private static final int YEARS = 2;

	public SparkManager(String conexion) {
		super();
		this.conexion = conexion;
	}
	
	/*
	public SparkC altaSpark(SparkC spark){
		SparkEng sparkDao = new SparkEng(conexion);
		spark = sparkDao.altaSpark(spark);
		return spark;
		
	}
	*/
	/**
	 * Método que recupera la información de un determinado spark
	 * 
	 * @param cnSpark Identificador único del spark
	 * @param intCodUsuario Identificador único de un usuario. Si este
	 * parámetro no es null se averigua si este spark ya ha sido
	 * adquirido por el usuario, y se "marca" en consecuencia
	 * 
	 * @return Se devolverá el spark cuyo id se pasara como parámetro
	 * @throws Exception
	 */
	public SparkC getSpark (Integer cnSpark, Integer intCodUsuario) throws Exception{
		SparkEng sparkDao = new SparkEng (conexion);
		SparkC spark = sparkDao.getSpark(cnSpark);
		
		if(intCodUsuario != null){
			UsuarioSparkEng usuarioSparkDao = new UsuarioSparkEng(conexion);
			UsuarioSparkC usuarioSpark = usuarioSparkDao.findUsuarioSpark(intCodUsuario, spark.getCnSpark());
			if(usuarioSpark != null && usuarioSpark.getChActivo().equals('1'))
				spark.setBoolAdquirido(true);
		}
		
		if (spark==null)
		{
			throw new Exception("Invalid Spark code getting spark");
		}
		return spark;
		
	}
	
	/**
	 * Método que devuelve todos los elementos de tipo "Spark"
	 * existentes en la base de datos.
	 * Si el parámetro "intCodUsuario" no es null, los sparks
	 * de la lista que el usuario haya adquirido aparecen
	 * "marcados"
	 * 
	 * @param intCodUsuario Identificador del usuario cuyos
	 * sparks comprados queremos identificar
	 * @return Lista con todos los sparks de base de datos
	 */
	public List<SparkC> getAllSparks(Integer intCodUsuario){
		SparkEng sparkDao = new SparkEng (conexion);
		UsuarioSparkEng usuarioSparkDao = new UsuarioSparkEng(conexion);
		
		List <SparkC> lstSparks = sparkDao.getAllSparks();
		
		if(intCodUsuario != null){
			for(int i=0;i< lstSparks.size(); i++){
				SparkC spark = lstSparks.get(i);				
				UsuarioSparkC usuarioSpark = usuarioSparkDao.findUsuarioSpark(intCodUsuario,spark.getCnSpark());
				
				if(usuarioSpark != null && usuarioSpark.getChActivo().equals('1'))
					spark.setBoolAdquirido(true);
			}
		}
		
		
		return lstSparks;
		
	}	
	
	
	/**
	 * Método que permite cambiar el status de un spark
	 * @param cnSpark Identificador único del spark a actualizar
	 * @param cnStatus Identificador único del nuevo status a asignar
	 * @param strMotivoRechazo Cadena que representa el motivo de rechazo
	 * cuando el status al que se pasa el spark es 'Rejected'
	 * @param intCodUsuarioModifica Identificador único del usuario
	 * que modifica el estado del spark
	 * al spark
	 */
	public void changeSparkStatus(Integer cnSpark, Integer cnStatus, String strMotivoRechazo, Integer intCodUsuarioModifica){
		SparkEng sparkDao = new SparkEng (conexion);
		UsuarioEng usuarioDao = new UsuarioEng(conexion);
		StatusEng statusDao = new StatusEng (conexion);
		SparkStatusEng sparkStatusDao = new SparkStatusEng (conexion);
		// Comprobamos si el spark ya tenía asociado ese status anteriormente
		SparkStatusC sparkStatus = sparkStatusDao.getStatus(cnSpark, cnStatus);
		
		SparkC spark = sparkDao.getSpark(cnSpark);
		UsuarioC usuarioModificador = usuarioDao.findById(intCodUsuarioModifica);
		
		// Comprobamos si el cambio de estado ha sido realizado
		// por el usuario creador del spark
		Character cambioFiona = new Character('1');
		if(usuarioModificador != null && usuarioModificador.getCnUsuario().equals(spark.getUsuarioDesarrollador().getCnUsuario())){
			cambioFiona = new Character('0');
		}
		
		if(sparkStatus != null){			
				// El cambio ya existe así que se modifica la fecha únicamente				
				sparkStatus.setDateCambio(new Date());
				sparkStatus.setCharCambioFiona(cambioFiona);
				sparkStatusDao.update(sparkStatus);
				sparkStatusDao.flush();					
			
		}else{
			// Si el spark pasado como parámetro nunca se había asociado
			// con el status, se crea dicha asociación
			sparkStatus = new SparkStatusC();			 
			StatusC status = statusDao.getStatus(cnStatus);
				
			SparkStatusPk sparkStatusPk = new SparkStatusPk();
			sparkStatusPk.setSpark(spark);
			sparkStatusPk.setStatus(status);
			sparkStatus.setSparkStatus(sparkStatusPk);
			sparkStatus.setDateCambio(new Date());	
			sparkStatus.setCharCambioFiona(cambioFiona);
			
			// Asigno el sparkstatus a la lista de cambios
			// de estado del spark
			spark.getLstSparkStatusC().add(sparkStatus);					
		}
		
		// Comprobar si existe motivo de rechazo
		if(strMotivoRechazo != null && !"".equals(strMotivoRechazo)){
			RejectionC rejection = new RejectionC();
			rejection.setDatRejection(new Date());
			rejection.setStrMotivo(strMotivoRechazo);
			rejection.setSpark(spark);
			spark.getLstRejections().add(rejection);
		}	
		
		sparkDao.update(spark);
		sparkDao.flush();
		
	}
	
	
	/**
	 * Método que devuelve los N sparks más vendidos,
	 * gratuitos o de pago en función del parámetro
	 *
	 * @param intCodUsuario Identificador único del usuario. Si
	 * este parámetro es distinto de null, cada spark de la lista
	 * devuelta tendrá indicado si ha sido o no comprado
	 * por el usuario
	 * @return Se devolverá la lista de sparks correspondiente
	 * a los parámetros de búsqueda
	 */
	public List<SparkC> getTopSalesSparks(Integer intCodUsuario){
		SparkEng sparkDao = new SparkEng (conexion);
		UsuarioSparkEng usuarioSparkDao = new UsuarioSparkEng(conexion);
		
		List <SparkC> lstSparks = sparkDao.getTopSaleSparks();
		
		if(intCodUsuario != null){
			for(int i = 0; i< lstSparks.size();i++){
				SparkC spark = lstSparks.get(i);				
				UsuarioSparkC usuarioSpark = usuarioSparkDao.findUsuarioSpark(intCodUsuario,spark.getCnSpark());
				
				if(usuarioSpark != null && usuarioSpark.getChActivo().equals('1'))
					spark.setBoolAdquirido(true);
			}
		}
		
		return lstSparks;
	}
	
	/**
	 * Método que devuelve los N últimos sparks publicados,
	 * gratuitos o de pago en función del parámetro
	 * 
	 * @param intCodSatus Identificador único del estado	 
	 * @param intCodUsuario Identificador único del usuario. Si
	 * este parámetro es distinto de null cada elemento de la lista
	 * de sparks tiene indicado si ha sido comprado o no por el usuario
	 * @return Se devolverá la lista de sparks correspondiente
	 * a los parámetros de búsqueda
	 */
	public List<SparkC> getLastStatusOrderedByDate(Integer intCodStatus, Integer intCodUsuario){
		SparkEng sparkDao = new SparkEng (conexion);
		UsuarioSparkEng usuarioSparkDao = new UsuarioSparkEng(conexion);
		
		List <SparkC> lstSparks =  sparkDao.getLastStatusOrderedByDate(intCodStatus);
		
		if(intCodUsuario != null){
			for(int i = 0; i< lstSparks.size();i++){
				SparkC spark = lstSparks.get(i);				
				UsuarioSparkC usuarioSpark = usuarioSparkDao.findUsuarioSpark(intCodUsuario,spark.getCnSpark());
				
				if(usuarioSpark != null && usuarioSpark.getChActivo().equals('1'))
					spark.setBoolAdquirido(true);
			}
		}
		
		return lstSparks;		
		
	}
	
	
	/**
	 * Método que devuelve los sparks básicos
	 * 
	 * @return Lista de sparks obligatorios para un
	 * avatar funcional
	 */
	public List<SparkC> getBasicSparks(){
		List <SparkC> basicSparks = new ArrayList<SparkC>();
		
		return basicSparks;		
	}
	
	/**
	 * Método que devolverá todos los sparks gratuitos o
	 * todos los sparks de pago en función del parámetro
	 * pasado
	 * 
	 * @param intIsFree parámetro que indica si se quieren
	 * obtener todos los sparks gratuitos, o todos los de pago
	 * @param intCodUsuario identificador único del usuario. Si
	 * este parámetro es distinto de null, cada elemento de la lista
	 * indicará si el spark ha sido o no adquirido por el usuario
	 * 
	 * @return
	 */
	public List<SparkC> getAllSparksByPrice(Integer intIsFree, Integer intCodUsuario){
		SparkEng sparkDao = new SparkEng (conexion);
		UsuarioSparkEng usuarioSparkDao = new UsuarioSparkEng(conexion);
		
		List<SparkC> lstSparks = new ArrayList<SparkC>();
		
		lstSparks = sparkDao.getAllSparksByPrice(intIsFree);
		
		if(intCodUsuario != null){
			for(int i = 0; i< lstSparks.size();i++){
				SparkC spark = lstSparks.get(i);				
				UsuarioSparkC usuarioSpark = usuarioSparkDao.findUsuarioSpark(intCodUsuario,spark.getCnSpark());
				
				if(usuarioSpark != null && usuarioSpark.getChActivo().equals('1'))
					spark.setBoolAdquirido(true);
			}
		}
		
		
		return lstSparks;
	}
	
	/**
	 * Devuelve todos los sparks no borrados cuyo último estado
	 * sea "Available"
	 * @param intCodUsuario Identificador único del usuario que solicita
	 * la lista
	 * @return
	 */	
	public List<SparkC> getAllAvailableSparks(Integer intCodUsuario){
		SparkEng sparkDao = new SparkEng (conexion);
		UsuarioSparkEng usuarioSparkDao = new UsuarioSparkEng(conexion);
		
		List<SparkC> lstSparks = new ArrayList<SparkC>();
		
		lstSparks = sparkDao.getAllAvailableSparks();
		
		if(intCodUsuario != null){
			for(int i = 0; i< lstSparks.size();i++){
				SparkC spark = lstSparks.get(i);				
				UsuarioSparkC usuarioSpark = usuarioSparkDao.findUsuarioSpark(intCodUsuario,spark.getCnSpark());
				
				if(usuarioSpark != null && usuarioSpark.getChActivo().equals('1'))
					spark.setBoolAdquirido(true);
			}
		}
		
		return lstSparks;
	}
	
	
	/**
	 * Método que permite aniadir una nueva opinión de un usuario a un spark
	 * 
	 * @param datEnviada Fecha de envío de la opinión
	 * @param intValoracion Valoración del spark (de 0 a 5)
	 * @param strDescripcion Contenido en texto de la opinión
	 * @param intCodSpark Identificador único del spark
	 * @param intCodUsuario Identificador único del usuario
	 */
	public void addOpinion(Date datEnviada, Integer intValoracion, String strDescripcion, String strTitulo, Integer intCodSpark, Integer intCodUsuario){		
		UsuarioSparkEng usuarioSparkDao = new UsuarioSparkEng(conexion);
		OpinionEng opinionDao = new OpinionEng(conexion);
		
		// Crear opinion
		OpinionC opinion = new OpinionC();
		
		opinion.setDatEnviada(datEnviada);
		opinion.setIntValoracion(intValoracion);
		opinion.setStrTitulo(strTitulo);
		opinion.setStrDescripcion(strDescripcion);		
		
		// Buscar usuarioSpark
		UsuarioSparkC usuarioSpark = usuarioSparkDao.findUsuarioSpark(intCodUsuario, intCodSpark);
		
		if(usuarioSpark != null){
			opinion.setUsuarioSpark(usuarioSpark);
			
			opinionDao.insert(opinion);
			
			opinionDao.flush();
			
		}
		
		
	}
	
	/**
	 * Método que permite buscar los sparks por palabras clave
	 * En caso de que la lista de palabras llegue vacía, se devolverán
	 * todos los sparks existentes
	 * 	
	 * @param keywords Lista de palabras clave de búsqueda
	 * @param intCodUsuario Identificador único de usuario. Si este parámetro
	 * es distinto de null, se marcará cada spark de la lista para indicar
	 * si ya ha sido o no adquirido por el usuario
	 * @return
	 */
	public List<SparkC> findSparksByKeywords(String keywords, Integer intCodUsuario){
		SparkEng sparkDao = new SparkEng(conexion);
		UsuarioSparkEng usuarioSparkDao = new UsuarioSparkEng(conexion);
		
		List <SparkC> lstSparks = new ArrayList<SparkC>();		
		String [] lstKeywords = null;
		
		if(keywords != null && !"".equals(keywords)){
			lstKeywords = keywords.split(" ");
		}
		
		if(lstKeywords != null && lstKeywords.length > 0){
			lstSparks = sparkDao.findSparksByKeywords(lstKeywords);
		}else {			
			lstSparks = sparkDao.getAllSparks();			
		}
		
		// Comprobar si el estado de esos sparks es el que necesitamos
		for(int i =0; i<lstSparks.size(); i++){
			if(!lstSparks.get(i).getLastStatus().getCnStatus().equals(6))
				lstSparks.remove(i);
		}
		
		if(intCodUsuario != null){
			for(int i = 0; i< lstSparks.size();i++){
				SparkC spark = lstSparks.get(i);				
				UsuarioSparkC usuarioSpark = usuarioSparkDao.findUsuarioSpark(intCodUsuario,spark.getCnSpark());
				
				if(usuarioSpark != null && usuarioSpark.getChActivo().equals('1'))
					spark.setBoolAdquirido(true);
			}
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
	public List<OpinionC> getNSparkOpinions(Integer intCodSpark, Integer numOpinions){
//		SparkEng sparkDao = new SparkEng(conexion);
//		
//		return sparkDao.getNSparkOpinions(intCodSpark, numOpinions);
		
		OpinionEng opinionDao = new OpinionEng(conexion);
		
		return opinionDao.getNSparkOpinions(intCodSpark, numOpinions);	
	}
	
	/**
	 * Método que devuelve una lista con los sparks cuyos identificadores
	 * coincidan con los pasados como parámetro
	 * 
	 * @param ids Lista de identificadores
	 * @return
	 */
	public List<SparkC> getSparks(List <String> ids, Integer intCodUsuario){
		SparkEng sparkDao = new SparkEng(conexion);
		UsuarioSparkEng usuarioSparkDao = new UsuarioSparkEng(conexion);
		
		List <Integer> idsSparks = new ArrayList<Integer>();
		
		for(int i=0;i<ids.size();i++){
			idsSparks.add(Integer.parseInt(ids.get(i)));
		}
		
		List <SparkC> lstSparks = sparkDao.getSparks(idsSparks);
		
		if(intCodUsuario != null){
			for(int i = 0; i< lstSparks.size();i++){
				SparkC spark = lstSparks.get(i);				
				UsuarioSparkC usuarioSpark = usuarioSparkDao.findUsuarioSpark(intCodUsuario,spark.getCnSpark());
			
				if(usuarioSpark != null && usuarioSpark.getChActivo().equals('1'))
					spark.setBoolAdquirido(true);
			}
		}
		
		return lstSparks;
	}
	
	/**
	 * Método que devuelve el último estado (y por tanto el válido)
	 * de un spark
	 * 
	 * @param intCodSpark Identificador único del spark
	 * @return Último status del spark, o 'null' si el spark
	 * aún no tiene asociado ningún estado
	 */
	public StatusC getLastSparkStatus(Integer intCodSpark){
		SparkStatusEng sparStatuskDao = new SparkStatusEng(conexion);
		
		StatusC status = sparStatuskDao.getLastSparkStatus(intCodSpark);
		
		return status;
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
	public List<SparkC> getListSparksByDeveloper(Integer intCodUsuarioDes){
		SparkEng sparkDao = new SparkEng(conexion);
		
		return sparkDao.getListSparksByDeveloper(intCodUsuarioDes);
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
	public List<SparkC> getListSparksByDeveloper(Integer intCodUsuarioDes, List<Integer> idsExcluidos, Integer maxResults){
		SparkEng sparkDao = new SparkEng(conexion);
		UsuarioSparkEng usuarioSparkDao = new UsuarioSparkEng(conexion);
		
		List <SparkC> sparksDeveloped = sparkDao.getListSparksByDeveloper(intCodUsuarioDes, idsExcluidos, maxResults);
		
		if(intCodUsuarioDes != null){
			for(int i = 0; i< sparksDeveloped.size();i++){
				SparkC spark = sparksDeveloped.get(i);				
				UsuarioSparkC usuarioSpark = usuarioSparkDao.findUsuarioSpark(intCodUsuarioDes,spark.getCnSpark());
			
				if(usuarioSpark != null && usuarioSpark.getChActivo().equals('1'))
					spark.setBoolAdquirido(true);
			}
		}
		
		return sparksDeveloped;
	}
	
	/**
	 * Método que devuelve a lista de motivos por los que se ha rechazado
	 * un spark por orden decreciente según fecha
	 * 
	 * @param intCodSpark Identificador único del spark
	 * @return Se devuelve la lista de motivos de rechazo para el spark
	 */	
	public List<RejectionC> getRejectionList (Integer intCodSpark){
		RejectionEng rejectionDao = new RejectionEng(conexion);
		
		return rejectionDao.getRejectionList(intCodSpark);
	}
	
	/**
	 * Método que devuelve a lista de errores asociados a
	 * un spark por orden decreciente según fecha
	 * 
	 * @param intCodSpark Identificador único del spark
	 * @return Se devuelve la lista de errores para el spark
	 */	
	public List<CrashC> getCrashList (Integer intCodSpark){
		CrashEng crashDao = new CrashEng(conexion);
		
		return crashDao.getCrashList(intCodSpark);
	}
	
	/**
	 * Método que permite el borrado lógico de un spark
	 * poniendo el flag de borrado a 1.
	 * Sólo se podrán borrar sparks que no hayan sido
	 * comprados por algún usuario
	 * 
	 * @param intCodSpark Identificador único del spark
	 */
	public void deleteSpark(Integer intCodSpark) throws RollbackException{
		SparkEng sparkDao = new SparkEng(conexion);
		
		SparkC spark = sparkDao.getSpark(intCodSpark);
		
		if(spark.getLstUsuarioSparkC() == null || spark.getLstUsuarioSparkC().isEmpty()){
			spark.setChBorrado('1');
			sparkDao.update(spark);
			sparkDao.flush();
		}else{
			// TODO: lanzar error para informar al usuario de que ese spark no puede ser borrado
			// por haber sido comprado por algún usuario
			throw new RollbackException();
		}
		
	}
	
	/**
	 * Método que permite modificar la información de un spark
	 * 
	 * @param sparkAModificar Spark con los nuevos datos
	 * @return Se devolverá el nuevo spark con la información
	 * modificada
	 */
	public SparkC modificarSpark(SparkC sparkAModificar, String strKeywords, Integer intCodEula, String strIdTarifaDes, String strIdTarifaProd,
			String paypalEmail){
		SparkEng sparkDao = new SparkEng(conexion);
		KeywordEng keywordDAO = new KeywordEng(conexion);
				
		SparkC sparkModificable = sparkDao.getSpark(sparkAModificar.getCnSpark());
		
		// Nombre
		sparkModificable.setStrNombre(sparkAModificar.getStrNombre());
		// Descripción
		sparkModificable.setStrDescripcion(sparkAModificar.getStrDescripcion());
		// Versión
		sparkModificable.setStrVersion(sparkAModificar.getStrVersion());
		// Descripción corta
		if(sparkAModificar.getStrDescripcionCorta() != null)
			sparkModificable.setStrDescripcionCorta(sparkAModificar.getStrDescripcionCorta());
		// Novedades versión
		if(sparkAModificar.getStrNovedadesVersion() != null)
			sparkModificable.setStrNovedadesVersion(sparkAModificar.getStrNovedadesVersion());
		// Preparación de keywords
		if(strKeywords != null && !"".equals(strKeywords)){
			String [] keywords = strKeywords.split(",");
			
			List<Integer> idsKeywords = new ArrayList<Integer>();
			
			for(int i= 0; i < keywords.length; i++){
				String keyword = keywords[i].trim();
				
				idsKeywords.add(Integer.valueOf(keyword));
			}
			
			List <KeywordC> lstKeywords = new ArrayList<KeywordC>();
			lstKeywords = keywordDAO.getKeywords(idsKeywords);
			
			sparkModificable.setLstKeywords(lstKeywords);
		}
		
		
		// Otros Keywords
		if(sparkAModificar.getStrOtrosKeywords() != null)
			sparkModificable.setStrOtrosKeywords(sparkAModificar.getStrOtrosKeywords());		
		// Email Soporte
		sparkModificable.setStrEmailSoporte(sparkAModificar.getStrEmailSoporte());
		// Marketing URL
		if(sparkAModificar.getStrMarketingUrl() != null)
			sparkModificable.setStrMarketingUrl(sparkAModificar.getStrMarketingUrl());
		// Precio desarrollo
//		sparkModificable.setFlPrecioDesarrollo(sparkAModificar.getFlPrecioDesarrollo());
//		// Precio producción
//		sparkModificable.setFlPrecioProduccion(sparkAModificar.getFlPrecioProduccion());
		
		// Comprobar el tipo de tarifa elegido para saber si debemos desactivar
		// los precios de desarrollo del spark (de existir)
		if(strIdTarifaDes != null && strIdTarifaDes.equalsIgnoreCase("3")){
			// Tarifa gratuita => Debemos borrar los precios establecidos
			// anteriormente para este spark
			List <PriceC> lstPrecios = sparkModificable.getLstPrices();
			
			for(int i = 0;i<lstPrecios.size();i++){
				if(lstPrecios.get(i).getChDevelopment().equals('1'))
					lstPrecios.get(i).setChActivo('0');
			}
			sparkModificable.setLstPrices(lstPrecios);
			
		}
		
		// Comprobar el tipo de tarifa elegido para saber si debemos desactivar
		// los precios de producción del spark (de existir)
		if(strIdTarifaProd != null && strIdTarifaProd.equalsIgnoreCase("3")){
			// Tarifa gratuita => Debemos borrar los precios establecidos
			// anteriormente para este spark
			List <PriceC> lstPrecios = sparkModificable.getLstPrices();
			
			for(int i = 0;i<lstPrecios.size();i++){
				if(lstPrecios.get(i).getChDevelopment().equals('0'))
					lstPrecios.get(i).setChActivo('0');
			}
			sparkModificable.setLstPrices(lstPrecios);
			
		}
		
		// Es trial
		sparkModificable.setChTrial(sparkAModificar.getChTrial());
		// Días trial
		sparkModificable.setIntDiasTrial(sparkAModificar.getIntDiasTrial());
		
		// Si se envía dirección de paypal, actualizar en el usuario desarrollador del spark
		if(paypalEmail != null && !"".equals(paypalEmail)){
			sparkModificable.getUsuarioDesarrollador().setPaypalEmail(paypalEmail);
		}
		
		// Icono
		if(sparkAModificar.getStrIcono() != null)
			sparkModificable.setStrIcono(sparkAModificar.getStrIcono());
		// Banner
		if(sparkAModificar.getStrBanner() != null)
			sparkModificable.setStrBanner(sparkAModificar.getStrBanner());
		// Video
		if(sparkAModificar.getStrVideo() != null)
			sparkModificable.setStrVideo(sparkAModificar.getStrVideo());
		
		// Eula
		if(intCodEula != null){
			EulaEng eulaDAO = new EulaEng(conexion);
			// Obtenemos el eula asociado al id
			EulaC eula = eulaDAO.getEula(intCodEula);
			sparkModificable.setEula(eula);
		}		
		
		//sparkModificable.setPrice(price);
		sparkDao.update(sparkModificable);
		sparkDao.flush();
		
		//sparkModificable.setPrice(price);
		
		return sparkModificable;
	}
	
	/**
	 * Método que permite crear un spark
	 * 
	 * @param 
	 * @return Se devolverá el nuevo spark 
	 * 
	 */
	public SparkC crearSpark(
			final String strNombreSpark, final Integer intDesarrollador, 
			final String strDescSpark, final String strVersion,			 
			final String strDescCorta, final String strEmailSoporte)
	{
		SparkEng sparkDao = new SparkEng(conexion);
		UsuarioEng usuarioDao = new UsuarioEng(conexion);
		
		Character chTrial = '0';
		Character chBorrado = '0';
		SparkC sparkEula = sparkDao.getSpark(0);
		EulaC noSenseEula = sparkEula.getEula();
		SparkC spark = new SparkC(strNombreSpark, intDesarrollador, strDescSpark, strVersion, strDescCorta,	strEmailSoporte, chTrial,
								chBorrado, noSenseEula);		
		
		//spark.setEula(noSenseEula);			
		UsuarioC usuario = new UsuarioC();
		usuario = usuarioDao.findById(intDesarrollador);
		spark.setUsuarioDesarrollador(usuario);
		
		sparkDao.create(spark);
		
		return spark;
	}
	
	/**
	 * Método que devolverá una serie de banners de sparks aleatorios 
	 * 
	 * @param intNumBanners Número de banners que se buscan
	 * @return
	 */
	public List<SparkC> getRandomBanners(Integer intNumBanners){
		SparkEng sparkDao = new SparkEng(conexion);
		
		return sparkDao.getRandomBanners(intNumBanners);
	}
	
	/**
	 * Método que extraerá la lista de sparks de un archivo XML de
	 * configuración
	 * 
	 * @param intCodUsuario Identificador único de usuario
	 * @param strRutaXml Nombre del archivo XML que
	 * almacena la configuración arevisar
	 * 
	 * @return Se devolverá una lista de objetos SparkC
	 * @throws IOException 
	 * @throws JDOMException 
	 * @throws FileNotFoundException 
	 */
	public List<SparkC> getSparksFromXml(Integer intCodUsuario, String strRutaXml) throws FileNotFoundException, JDOMException, IOException{
		SparkEng sparkDao = new SparkEng(conexion);
		UsuariosManager usuarioManager = ManagerFactory.getInstance().getUsuariosManager();
		List <SparkC> lstSparks = new ArrayList<SparkC>();
		String usermd5 = "";
		UsuarioC usuario = null;
		try {
			usuario = usuarioManager.getUsuario(intCodUsuario);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		if(usuario!=null){
			usermd5 = usuario.getAvatarBuilderUmd5();
		}
		
		// Creamos el builder basado en SAX  
		SAXBuilder builder = new SAXBuilder();
		String ruta = "";
		
		if(strRutaXml != null)
			ruta = strRutaXml;
		else
			ruta = "avatar.xml";
		
		File avatarCfgFile = new File(Constantes.getNfsUserFolder(usermd5), ruta);
		
		// Construimos el arbol DOM a partir del fichero xml
		Document doc = builder.build(new FileInputStream(avatarCfgFile));		
		Element root = doc.getRootElement();
		Element declarations = root.getChild("ComponentDeclarations");
		
		List<Element> components = declarations.getChildren(); 
		SparkC spark = null;
		for(int i=0; i<components.size();i++ ){			
			Element component = components.get(i);
			String valorType = component.getAttributeValue("type");
			
			spark = sparkDao.getSpark(valorType);
			
			if(spark!=null)
				lstSparks.add(spark);
			
		}
		
		
		return lstSparks;
		
	}
	
	
	
	/**
	 * Método que permite estimar el precio de un avatar en producción
	 * a partir de los sparks que forman la configuración
	 * 
	 * @param intCodUsuario
	 * @param strNombreFichero
	 * @throws FileNotFoundException
	 * @throws JDOMException
	 * @throws IOException
	 */
//	public void estimateProductionPrice(Integer intCodUsuario, String strNombreFichero) throws FileNotFoundException, JDOMException, IOException{
//		// Recuperar la lista de sparks del archivo xml de configuración
//		List <SparkC> lstSparks = getSparksFromXml(intCodUsuario,strNombreFichero);
//		Float precioTarifaPlanaMensual = new Float(0);
//				
//		for(int i = 0; i< lstSparks.size(); i++){
//			SparkC spark = lstSparks.get(i);			
//			PriceC price = spark.getPrice();
//			
//			if(price != null){
//				// Comprobar si el precio se ha establecido por uso o
//				// por tarifa plana de tiempo
//				if(price.getUnit() != null){
//					// Tarifa Plana
//					precioTarifaPlanaMensual += calcularPrecioMensual(price);
//				}else{
//					// Por uso del spark
//				}
//					
//			}
//		}
//	}
	
	/**
	 * Método que marcará como inactivos los sparks de usuarios
	 * que hayan superado el periodo de prueba de los mismos 
	 */
	public void deactivateExpiredTrialSparks(){
		UsuarioSparkEng usuarioSparkDAO = new UsuarioSparkEng(conexion);		
		// Buscar los usuarios con sparks en periodo de prueba
		// que haya caducado
		List <UsuarioSparkC> lstUsuarioSparksCaducados = usuarioSparkDAO.getExpiredTrialUsuarioSpark();
		// Marcar cada miembro de la lista como inactivo
		for(int i=0;i<lstUsuarioSparksCaducados.size();i++){
			UsuarioSparkC usuarioSpark = lstUsuarioSparksCaducados.get(i);
			usuarioSpark.setChActivo('0');
			usuarioSparkDAO.update(usuarioSpark);
		}
		
	}
	
	/**
	 * Método que comprueba si todos los Sparks del usuario están activos
	 * 
	 * @param usuarioSparks Lista de sparks del usuario
	 * @param sparksConfiguracion Lista de sparks pertenecientes a la configuración
	 * del archivo xml a ejecutar
	 * 
	 * @return Se devolverá FALSE si alguno de los sparks de la configuración del
	 * usuario no está activo
	 */
	public Boolean isSetUserSparksActive(List <UsuarioSparkC> usuarioSparks, List <SparkC> sparksConfiguracion){
		Boolean todosActivos = Boolean.TRUE;
		
		for(int i = 0;i< usuarioSparks.size() && todosActivos;i++){
			UsuarioSparkC usuarioSpark = usuarioSparks.get(i);
			if(sparksConfiguracion.contains(usuarioSpark.getUsuarioSparkPk().getSpark())){
				if(usuarioSpark.getChActivo().equals('0'))
					todosActivos = Boolean.FALSE;
			}
		}
		
		return todosActivos;
		
	}
	
	/**
	 * Método que permite asignar interfaces a un spark
	 * 
	 * @param intCodSpark
	 * @param registros
	 * @return 
	 * 
	 */
	public List <InterfazC> addSparkInterfaces(Integer intCodSpark, IRegistro[] registros){		
		SparkEng sparkDao = new SparkEng(conexion);
		InterfazEng ifazDAO = new InterfazEng(conexion);		
		
		List <InterfazC> lstSparkInterfaces = new ArrayList<InterfazC>();		
		for(Integer i = 0;i<registros.length;i++){
			InterfazC interfaz = new InterfazC();
			interfaz = ifazDAO.getInterfaz(registros[i].getBigDecimal("FIONEG021010040").intValue());			
			lstSparkInterfaces.add(interfaz);
		}
		SparkC spark = new SparkC();
		spark = sparkDao.getSpark(intCodSpark);
		spark.setLstinterfazc(lstSparkInterfaces);
		sparkDao.update(spark);
		
		return lstSparkInterfaces;
	}

	public UsuarioSparkC getUsuarioSpark(Integer intCodUsuario, Integer intCodSpark){
		UsuarioSparkEng usuarioSparkDAO = new UsuarioSparkEng(conexion);
		
		return usuarioSparkDAO.findUsuarioSpark(intCodUsuario, intCodSpark);
	}
	
	public Float calcularOpinionPromedio(List <OpinionC> lstOpiniones){		
		Integer intNumOpiniones = lstOpiniones.size();
		
		Float valoracionTotal = new Float(0);
		
		for(int i = 0; i < intNumOpiniones; i++){
			valoracionTotal += lstOpiniones.get(i).getIntValoracion();
		}
		
		if(intNumOpiniones > 0){
			valoracionTotal = valoracionTotal / intNumOpiniones;
		}	
			
		
		return valoracionTotal;
	}
	
	/**
	 * Método que extrae de la lista pasada como parámetro aquellos sparks que sólo
	 * puedan ser tarificados por uso. Debe tenerse en cuenta que la lista pasada como
	 * parámetro será modificada eliminando de ella aquellos sparks que cumplan la condición
	 * de tener tarifas de uso únicamente
	 * @param lstSparks Listado de sparks inicial
	 * @return Se devolverá el grupo de sparks del listado inicial, que sólo puedan ser
	 * tarificados por uso
	 */
	public List<SparkC> getSparksWithOnlyUseProductionFee(List <SparkC> lstSparks){
		
		List <SparkC> lstUseFeeSparks = new ArrayList<SparkC>();
		
		// Recorremos sparks
		for(int i = 0; i<lstSparks.size();i++){
			SparkC spark = lstSparks.get(i);
			
			// Buscar lista de precios de producción
			List <PriceC> lstProductionPrices = spark.getProductionPrices();
			
			// Comprobar si alguno de los precios tiene asociada un tipo de
			// tarifa distinta de uso
			int contadorPreciosInvalidos = 0;
			for(int j = 0; j<lstProductionPrices.size()&&contadorPreciosInvalidos ==0; j++){
				PriceC price = lstProductionPrices.get(j);
				// Consideramos precios inválidos los que tienen algún precio con tarifa por tiempo
				// o simplemente son gratuitos
				if(price.getChActivo().equals('1')){
					if(price.getUnit() != null)
						contadorPreciosInvalidos++;
				}
			}
			if(contadorPreciosInvalidos == 0 && lstProductionPrices.size()>1){
				// Añadimos el spark al grupo que sólo tarifica por uso
				lstUseFeeSparks.add(spark);
				// Eliminamos el spark de la lista inicial
				lstSparks.remove(i);
			}
		}
		
		return lstUseFeeSparks;
	}
	
	/**
	 * Método que calcula el total del precio del listado de sparks pasados como parámetro,
	 * en base al número de usuarios concurrentes y a la unidad de tiempo seleccionada
	 * @param lstSparks Listado de sparks a evaluar
	 * @param intNumUsuarios Número de usuarios concurrentes solicitado por el usuario
	 * @param intIdUnidadTiempo Unidad de tiempo seleccionada por el usuario
	 * @return Se devuelve el precio total
	 */
	public Float calculateSparksTimeFeeProductionPrice(List<SparkC> lstSparks, Integer intNumUsuarios, Integer intIdUnidadTiempo){		
		Float precioTotal = new Float(0);
		
		for(int i=0; i<lstSparks.size();i++){
			// Comprobamos el caso del spark en función del número de usuarios
			SparkC spark = lstSparks.get(i);
			
			List <PriceC> lstPrecios = spark.getProductionPrices();
			PriceC selectedPrice = null;
			for(int j = 0; j<lstPrecios.size();j++){
				PriceC price = lstPrecios.get(j);
				if(price.getChActivo().equals('1')){
					// Primero aseguramos que el precio es por tiempo
					if(price.getUnit() != null){
						// Comprobaciones contra precioSeleccionado
						if(selectedPrice != null){
							if(price.getIntUsrConcu().equals(intNumUsuarios)){
								// Antes de cambiar el precio seleccionado
								// se comprueba la unidad temporal en el caso de que el número
								// de usuarios concurrentes del precio seleccionado sea el mismo
								// que el del precio a analizar
								if(price.getIntUsrConcu().equals(selectedPrice.getIntUsrConcu())){
									// Si la unidad del precio a analizar es la misma que la seleccionada
									// por el usuario
									if(price.getUnit().getIntCodUnit().equals(intIdUnidadTiempo)){
										// Si la unidad del precio a analizar es la misma que la del precio anterior seleccionado
										if(price.getUnit().getIntCodUnit().equals(selectedPrice.getUnit().getIntCodUnit())){
											// Si la cantidad de la unidad (ejem_cantidad de meses) del precio a analizar
											// es más pequeña que la del anterior precio seleccionado, entonces el analizado
											// debe sustituir al seleccionado anteriormente
											if(price.getIntAmount() < selectedPrice.getIntAmount()){
												selectedPrice = price;
											}
										// Si la unidad del precio a analizar no coincide con la del precio anterior seleccionado
										// pero sí con la seleccionada por el usuario en el combo
										}else{
											selectedPrice = price;
										}										
									}else{
										// Las unidades de tiempo del precio analizado y la seleccionada por el usuario no coinciden
										// pero debe comprobarse si es la misma o más cercana a lo elegido por el usuario
										// que el anterior precio seleccionado
										
										// Si la unidad del precio a analizar es la misma que la del precio anterior seleccionado
										if(price.getUnit().getIntCodUnit().equals(selectedPrice.getUnit().getIntCodUnit())){
											// Si la cantidad de la unidad (ejem_cantidad de meses) del precio a analizar
											// es más pequeña que la del anterior precio seleccionado, entonces el analizado
											// debe sustituir al seleccionado anteriormente
											if(price.getIntAmount() < selectedPrice.getIntAmount()){
												selectedPrice = price;
											}
										// Si la unidad del precio a analizar no coincide con la del precio anterior seleccionado
										// ni con la seleccionada por el usuario en el combo
										}else{
											// Esto sólo tendría sentido si hubiera una tercera unidad de tiempo (¿días?)
											// Hasta que llegue ese día nos quedamos en blanco
											
										}
									}
									
								}else{								
									selectedPrice = price;
								}
							}else{
								// EL número de usuarios no es exactamente el mismo que el seleccionado por el usuario
								// en el combo
								int diff1 = Math.abs(price.getIntUsrConcu() - intNumUsuarios);
								int diff2 = Math.abs(selectedPrice.getIntUsrConcu() - intNumUsuarios);
								if(diff1<diff2){
									selectedPrice = price;
								}else{
									// Nos encontramos en el caso de que el número de usuarios del precio a analizar y el del
									// precio previamente seleccionado sea el mismo o el del precio a analizar sea menor que el
									// del precio seleccionado
									if(diff1 == diff2){
										if(!selectedPrice.getIntUsrConcu().equals(intNumUsuarios) ){
											// Si la unidad del precio a analizar es la misma que la seleccionada
											// por el usuario
											if(price.getUnit().getIntCodUnit().equals(intIdUnidadTiempo)){
												// Si la unidad del precio a analizar es la misma que la del precio anterior seleccionado
												if(price.getUnit().getIntCodUnit().equals(selectedPrice.getUnit().getIntCodUnit())){
													// Si la cantidad de la unidad (ejem_cantidad de meses) del precio a analizar
													// es más pequeña que la del anterior precio seleccionado, entonces el analizado
													// debe sustituir al seleccionado anteriormente
													if(price.getIntAmount() < selectedPrice.getIntAmount()){
														selectedPrice = price;
													}
												// Si la unidad del precio a analizar no coincide con la del precio anterior seleccionado
												// pero sí con la seleccionada por el usuario en el combo
												}else{
													selectedPrice = price;
												}										
											}else{
												// Las unidades de tiempo del precio analizado y la seleccionada por el usuario no coinciden
												// pero debe comprobarse si es la misma o más cercana a lo elegido por el usuario
												// que el anterior precio seleccionado
												
												// Si la unidad del precio a analizar es la misma que la del precio anterior seleccionado
												if(price.getUnit().getIntCodUnit().equals(selectedPrice.getUnit().getIntCodUnit())){
													// Si la cantidad de la unidad (ejem_cantidad de meses) del precio a analizar
													// es más pequeña que la del anterior precio seleccionado, entonces el analizado
													// debe sustituir al seleccionado anteriormente
													if(price.getIntAmount() < selectedPrice.getIntAmount()){
														selectedPrice = price;
													}
												// Si la unidad del precio a analizar no coincide con la del precio anterior seleccionado
												// ni con la seleccionada por el usuario en el combo
												}else{
													// Esto sólo tendría sentido si hubiera una tercera unidad de tiempo (¿días?)
													// Hasta que llegue ese día nos quedamos en blanco
													
												}
											}
										}
									}
								}
							}
						}else{
							selectedPrice = price;
						}
					}
				}
			}
			if(selectedPrice != null){
				Float precioCalculado = selectedPrice.getFloPrize();
				// Cuentas previas a la selección temporal
				if(selectedPrice.getIntUsrConcu() != intNumUsuarios){
					precioCalculado = (intNumUsuarios * precioCalculado)/selectedPrice.getIntUsrConcu();
				}
				
				// Llegados a este punto tenemos un precio seleccionado para el spark
				// Ahora debe tenerse en cuenta la variable TIEMPO
				switch(intIdUnidadTiempo){
					case MONTHS:
						switch(selectedPrice.getUnit().getIntCodUnit()){
							case MONTHS:
								if(selectedPrice.getIntAmount() > 1){
									precioCalculado = precioCalculado/selectedPrice.getIntAmount();
								}
								break;
							case YEARS:
								if(selectedPrice.getIntAmount() > 1){
									precioCalculado = precioCalculado/(12*selectedPrice.getIntAmount());
								}else{
									precioCalculado = precioCalculado/12;
								}
								break;
						}
						break;
					case YEARS:
						switch(selectedPrice.getUnit().getIntCodUnit()){
							case MONTHS:
								if(selectedPrice.getIntAmount() > 1){
									precioCalculado = (precioCalculado*12)/selectedPrice.getIntAmount();
								}else{
									precioCalculado = precioCalculado * 12;
								}
								break;
							case YEARS:
								if(selectedPrice.getIntAmount() > 1){
									precioCalculado = precioCalculado/selectedPrice.getIntAmount();
								}
								break;
							}
						
						break;
				}
			
			
				precioTotal += precioCalculado;
				spark.setFloPrecioSeleccionadoProd(precioCalculado);
			
			}
		}
		
		return precioTotal;
		
	}
	
	/**
	 * Método que calcula el total del precio del listado de sparks pasados como parámetro,
	 * en base al número de usuarios concurrentes.
	 * @param lstSparks Listado de sparks a evaluar
	 * @param intNumUsuarios Número de usuarios concurrentes solicitado por el usuario	 
	 * @return Se devuelve el precio total
	 */
	public Float calculateSparksUseFeeProductionPrice(List<SparkC> lstSparks, Integer intNumUsuarios){
		Float precioTotal = new Float(0);
		
		for(int i = 0; i< lstSparks.size(); i++){
			// Comprobamos el caso del spark en función del número de usuarios
			SparkC spark = lstSparks.get(i);
			
			List <PriceC> lstPrecios = spark.getProductionPrices();
			PriceC selectedPrice = null;
			for(int j = 0; j<lstPrecios.size();j++){
				PriceC price = lstPrecios.get(j);
				if(price.getChActivo().equals('1')){
					// Primero aseguramos que el precio es por uso
					if(price.getUtilization() != null){
						// Comprobaciones contra precioSeleccionado
						if(selectedPrice != null){
							if(price.getIntUsrConcu().equals(intNumUsuarios)){
								selectedPrice = price;
							}else{
								int diff1 = Math.abs(price.getIntUsrConcu() - intNumUsuarios);
								int diff2 = Math.abs(selectedPrice.getIntUsrConcu() - intNumUsuarios);
								if(diff1<diff2){
									selectedPrice = price;
								}
							}
						}else{
							selectedPrice = price;
						}
					}
				}
			}
			if(selectedPrice != null){
				Float precioCalculado = selectedPrice.getFloPrize();
				// Cuentas previas a la selección temporal
				if(selectedPrice.getIntUsrConcu() != intNumUsuarios){
					precioCalculado = (intNumUsuarios * precioCalculado)/selectedPrice.getIntUsrConcu();
				}
				
				precioTotal += precioCalculado;
				spark.setFloPrecioSeleccionadoProd(precioCalculado);
			}
			
		}
		
		return precioTotal;
		
	}
	
	/**
	 * Método que devolverá un objeto de tipo "HostingC" que se ajuste a los parámetros
	 * de búsqueda
	 * @param intCodUnit Identificador de la unidad de tiempo
	 * @param intNumUsers Número de usuarios concurrentes
	 * @param strResolution Cadena que representa la resolución
	 * @param chHighAvailability Se quiere o no alta disponibilidad
	 * @return Se devolverá el objeto buscado
	 */
	public HostingC getPrecioHosting(Integer intCodUnit, Integer intNumUsers, String strResolution, Character chHighAvailability){
		HostingEng hostingDAO = new HostingEng(conexion);
		/* Cambio introducido tras incluir números de usuarios múltiplos de 20 */
		if(intNumUsers > 20)
			intNumUsers = 20;
		HostingC hosting = hostingDAO.getHosting(intCodUnit, intNumUsers, strResolution, chHighAvailability);				
		
		return hosting;
		
	}
	
	/**
	 * Método que devuelve los sparks desarrollados por un determinado usuario
	 * que están siendo usados por otros usuarios en desarrollo
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @return
	 */
	public List<UsuarioSparkC> getActiveUsedSparksDevelopedByUser(Integer intCodUsuario){
		UsuarioSparkEng usuarioSparkDAO = new UsuarioSparkEng(conexion);
		
		return usuarioSparkDAO.getActiveUsedSparksDevelopedByUser(intCodUsuario);
	}
	
	
}
