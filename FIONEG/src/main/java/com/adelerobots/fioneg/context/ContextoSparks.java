package com.adelerobots.fioneg.context;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import com.adelerobots.fioneg.entity.PriceC;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.entity.opinion.OpinionC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

/**
 * Clase destinada a los contextos necesarios para los servicios con funciones
 * sobre sparks
 * 
 * @author adele
 * 
 */
public class ContextoSparks {
	public static final String SPARKS_CTX = "FIONEGN004";	

	public static final String CTX_SPARK_ID = "FIONEG004010";
	public static final String CTX_SPARK_NOMBRE = "FIONEG004020";
	public static final String CTX_SPARK_USER_DESARROLLADOR = "FIONEG004030";
	public static final String CTX_SPARK_DESCRIPCION = "FIONEG004040";	
	
	public static final String CTX_SPARK_VERSION = "FIONEG004050";
	public static final String CTX_SPARK_SHORT_DESC = "FIONEG004060";
	public static final String CTX_SPARK_NOVEDADES = "FIONEG004070";
	public static final String CTX_SPARK_OTROS_KEY_WORDS = "FIONEG004080";
	public static final String CTX_SPARK_EMAIL_SOPORTE = "FIONEG004090";
	public static final String CTX_SPARK_MARKETING_URL = "FIONEG004100";
	
	//public static final String CTX_SPARK_EULA = "FIONEG004110";	
	
	public static final String CTX_SPARK_PRECIO_NUM_USUARIOS = "FIONEG004120";
	public static final String CTX_SPARK_PRECIO_UNIDAD_NOMBRE = "FIONEG004130";	
	public static final String CTX_SPARK_TRIAL = "FIONEG004140";	
	public static final String CTX_SPARK_DIAS_TRIAL = "FIONEG004150";
	
	public static final String CTX_SPARK_ICONO = "FIONEG004160";
	public static final String CTX_SPARK_BANNER = "FIONEG004170";
	public static final String CTX_SPARK_VIDEO = "FIONEG004180";
	
	public static final String CTX_SPARK_ADQUIRIDO = "FIONEG004190";
	
	public static final String CTX_SPARK_STATUS_ID = "FIONEG004200";
	public static final String CTX_SPARK_STATUS_NOMBRE = "FIONEG004210";	
	
	public static final String CTX_SPARK_BORRADO = "FIONEG004220";
	
	public static final String CTX_SPARK_ICON_PATH = "FIONEG004230";
	
	public static final String CTX_SPARK_STATUS_FECHA = "FIONEG004240";
	
	public static final String CTX_SPARK_PRECIO_UNIDAD_VALOR = "FIONEG004250";
	public static final String CTX_SPARK_PRECIO_PRECIO = "FIONEG004260";
	//public static final String CTX_SPARK_PRECIO_ID_UNIDAD_USO = "FIONEG004270";
	
	public static final String CTX_SPARK_USUARIO_DESARROLLADOR_MD5 = "FIONEG004280";
	
	public static final String CTX_SPARK_PRECIO_TIPO_TARIFA_DES = "FIONEG004290";
	
	public static final String CTX_SPARK_PRECIO_TIPO_TARIFA_PROD = "FIONEG004300";
	
	public static final String CTX_SPARK_OPINION_MEDIA_VALORACION = "FIONEG004310";
	public static final String CTX_SPARK_OPINION_MEDIA_NUM_OPINIONES = "FIONEG004320";
	
	public static final String CTX_SPARK_USUARIO_DESARROLLADOR_NOMBRE = "FIONEG004330";
	
	public static final String CTX_SPARK_USUARIO_DESARROLLADOR_EMAIL_PAYPAL = "FIONEG004340";
		
	
	
	// ------------------------------- PAID SPARKS -------------------------------------- //
	public static final String PAID_SPARKS_CTX = "FIONEGN005";
	
	public static final String CTX_PAID_SPARK_ID = "FIONEG005010";
	public static final String CTX_PAID_SPARK_NOMBRE = "FIONEG005020";
	public static final String CTX_PAID_SPARK_USER_DESARROLLADOR = "FIONEG005030"; 
	public static final String CTX_PAID_SPARK_DESCRIPCION = "FIONEG005040";
	
	public static final String CTX_PAID_SPARK_VERSION = "FIONEG005050";
	public static final String CTX_PAID_SPARK_SHORT_DESC = "FIONEG005060";
	public static final String CTX_PAID_SPARK_NOVEDADES = "FIONEG005070";
	public static final String CTX_PAID_SPARK_OTROS_KEY_WORDS = "FIONEG005080";
	public static final String CTX_PAID_SPARK_EMAIL_SOPORTE = "FIONEG005090";
	public static final String CTX_PAID_SPARK_MARKETING_URL = "FIONEG005100";
	
	public static final String CTX_PAID_SPARK_PRECIO_NUM_USUARIOS = "FIONEG005120";
	public static final String CTX_PAID_SPARK_PRECIO_UNIDAD_NOMBRE = "FIONEG005130";
	public static final String CTX_PAID_SPARK_TRIAL = "FIONEG005140";	
	public static final String CTX_PAID_SPARK_DIAS_TRIAL = "FIONEG005150";
	
	public static final String CTX_PAID_SPARK_ICONO = "FIONEG005160";
	public static final String CTX_PAID_SPARK_BANNER = "FIONEG005170";
	public static final String CTX_PAID_SPARK_VIDEO = "FIONEG005180";
	
	public static final String CTX_PAID_SPARK_ADQUIRIDO = "FIONEG005190";
	
	public static final String CTX_PAID_SPARK_BORRADO = "FIONEG005220";
	
	public static final String CTX_PAID_SPARK_ICON_PATH = "FIONEG005230";
	
	public static final String CTX_PAID_SPARK_STATUS_FECHA = "FIONEG005240";
	
	public static final String CTX_PAID_SPARK_PRECIO_UNIDAD_VALOR = "FIONEG005250";
	public static final String CTX_PAID_SPARK_PRECIO_PRECIO = "FIONEG005260";
	//public static final String CTX_PAID_SPARK_PRECIO_ID_UNIDAD_USO = "FIONEG005270";
	
	public static final String CTX_PAID_SPARK_USUARIO_DESARROLLADOR_MD5 = "FIONEG005280";
	
	public static final String CTX_PAID_SPARK_PRECIO_TIPO_TARIFA_DES = "FIONEG005290";
	
	public static final String CTX_PAID_SPARK_PRECIO_TIPO_TARIFA_PROD = "FIONEG005300";
	
	public static final String CTX_PAID_SPARK_OPINION_MEDIA_VALORACION = "FIONEG005310";
	public static final String CTX_PAID_SPARK_OPINION_MEDIA_NUM_OPINIONES = "FIONEG005320";
	
	public static final String CTX_PAID_SPARK_USUARIO_DESARROLLADOR_NOMBRE = "FIONEG005330";
	
	public static final String CTX_PAID_SPARK_USUARIO_DESARROLLADOR_EMAIL_PAYPAL = "FIONEG005340";
	


	/**
	 * Método destinado a rellenar el contexto con un spark
	 * 
	 * @param spark
	 *            Spark a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEG004
	 */
	public static IContexto[] rellenarContexto(final SparkC spark) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (spark != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoSpark(spark,null,null);
		}
		return salida;
	}
	
	/**
	 * Método destinado a rellenar el contexto con un spark
	 * 
	 * @param spark
	 *            Spark a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEG004
	 */
	public static IContexto[] rellenarContexto(final SparkC spark, final PriceC precio) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (spark != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoSpark(spark,precio,null);
		}
		return salida;
	}
	
	/**
	 * Método destinado a rellenar el contexto con un spark
	 * 
	 * @param spark
	 *            Spark a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEG004
	 */
	public static IContexto[] rellenarContexto(final SparkC spark, final List<OpinionC> opiniones, final PriceC precioSeleccionado, Integer intNumTotalOpiniones,
			List <SparkC> lstSparksRelacionados) {
		IContexto[] salida = null;
		
		// Rellenar contexto de salida
		if (spark != null) {
			if(opiniones != null && opiniones.size() > 0){				
				salida = new IContexto[1+opiniones.size()+spark.getLstKeywords().size()+spark.getLstPrices().size()+lstSparksRelacionados.size()];
				// Metemos en el contexto la categoria
				salida[0] = rellenarContextoSpark(spark,precioSeleccionado, intNumTotalOpiniones);
				
				// Lista de opiniones del spark
				IContexto [] contextoOpiniones = ContextoOpinion.rellenarContexto(opiniones);
				for (int i = 0; i < contextoOpiniones.length; i++) {
					salida[i + 1] = contextoOpiniones[i];
				}			
				
				// Lista de keywords del spark
				IContexto[] contextoKeywords = ContextoKeyword.rellenarContexto(spark.getLstKeywords());
				for(int i = 0; i<contextoKeywords.length;i++){
					salida[i+1+contextoOpiniones.length] = contextoKeywords[i];
				}
				
				// Lista de precios de desarrollo
				IContexto[] contextoPrecios = ContextoPrice.rellenarContexto(spark.getDevelopmentPrices(),spark.getProductionPrices());
				for(int i = 0; i<contextoPrecios.length;i++){
					salida[i+1+contextoOpiniones.length+contextoKeywords.length] = contextoPrecios[i];
				}	
			
				// Lista de sparks desarrollados por el mismo usuario
				IContexto [] contextoSparksRelacionados = rellenarContexto(lstSparksRelacionados);
				
				for(int i = 0; i<contextoSparksRelacionados.length;i++){
					salida[i+1+contextoOpiniones.length+contextoKeywords.length+contextoPrecios.length] = contextoSparksRelacionados[i];
				}
				
			}else {
				salida = new IContexto[1+spark.getLstKeywords().size()+spark.getLstPrices().size() + lstSparksRelacionados.size()];
				salida[0] = rellenarContextoSpark(spark,precioSeleccionado,0);
				
				// Lista de keywords del spark
				IContexto[] contextoKeywords = ContextoKeyword.rellenarContexto(spark.getLstKeywords());
				for(int i = 0; i<contextoKeywords.length;i++){
					salida[i+1] = contextoKeywords[i];
				}
				
				// Lista de precios
				IContexto[] contextoPrecios = ContextoPrice.rellenarContexto(spark.getDevelopmentPrices(),spark.getProductionPrices());
				for(int i = 0; i<contextoPrecios.length;i++){
					salida[i+1+contextoKeywords.length] = contextoPrecios[i];
				}	
				
				// Lista de sparks desarrollados por el mismo usuario
				IContexto [] contextoSparksRelacionados = rellenarContexto(lstSparksRelacionados);
				
				for(int i = 0; i<contextoSparksRelacionados.length;i++){
					salida[i+1+contextoKeywords.length+contextoPrecios.length] = contextoSparksRelacionados[i];
				}
				
			}
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de sparks
	 * 
	 * @param lstSparks
	 *            Lista de sparks a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEG004
	 */
	
	public static IContexto[] rellenarContexto(List<SparkC> lstSparks) {
		IContexto[] salida = null;
		final int iSize = lstSparks.size();

		if (lstSparks != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoSpark(lstSparks.get(i),null,null);
			}
		}		
		
		return salida;
	}
	
	
	
	public static IContexto[] rellenarContexto(List<SparkC> lstFreeSparks,List<SparkC> lstPaidSparks, Integer intSizeLstDevelopedSparks) {
		IContexto[] salida = null;
		final int iSizeFree = lstFreeSparks.size();
		final int iSizePaid = lstPaidSparks.size();

		salida = new IContexto[iSizeFree+iSizePaid+1];
		if (lstFreeSparks != null) {			
			for (int i = 0; i < iSizeFree; i++) {
				salida[i] = rellenarContextoSpark(lstFreeSparks.get(i),null,null);
			}
		}		
				
		
		if (lstPaidSparks != null) {		
			for (int i = iSizeFree; i < iSizeFree+iSizePaid; i++) {
				salida[i] = rellenarContextoPaidSpark(lstPaidSparks.get(i-iSizeFree),null,null);
			}
		}
		
		IContexto [] contextoMensaje = ContextoMensaje.rellenarContexto(intSizeLstDevelopedSparks.toString());
		salida[iSizeFree+iSizePaid] = contextoMensaje[0];
		
		return salida;
	}
	
	
	/**
	 * Método destinado a rellenar el contexto con un spark
	 * 
	 * @param spark
	 *            Spark a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEG004
	 */
	private static IContexto rellenarContextoSpark(final SparkC spark, final PriceC price, final Integer intNumOpiniones) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				SPARKS_CTX);

		datos.put(CTX_SPARK_ID, new BigDecimal(spark.getCnSpark()));
		datos.put(CTX_SPARK_NOMBRE, spark.getStrNombre());
		datos.put(CTX_SPARK_USER_DESARROLLADOR, new BigDecimal(spark.getCnUsuarioDesarrollador()));
		datos.put(CTX_SPARK_DESCRIPCION, spark.getStrDescripcion());
		
		datos.put(CTX_SPARK_VERSION, spark.getStrVersion());
		datos.put(CTX_SPARK_SHORT_DESC, spark.getStrDescripcionCorta());
		datos.put(CTX_SPARK_NOVEDADES, spark.getStrNovedadesVersion());
		datos.put(CTX_SPARK_OTROS_KEY_WORDS, spark.getStrOtrosKeywords());
		datos.put(CTX_SPARK_EMAIL_SOPORTE, spark.getStrEmailSoporte());
		datos.put(CTX_SPARK_MARKETING_URL, spark.getStrMarketingUrl());
		
		if(price != null){
			datos.put(CTX_SPARK_PRECIO_NUM_USUARIOS, new BigDecimal(price.getIntUsrConcu()));
			if(price.getUnit()!=null){
				datos.put(CTX_SPARK_PRECIO_UNIDAD_NOMBRE, price.getUnit().getStrContent());
				datos.put(CTX_SPARK_PRECIO_UNIDAD_VALOR, new BigDecimal(price.getIntAmount()));
			}else{
				datos.put(CTX_SPARK_PRECIO_UNIDAD_NOMBRE, price.getUtilization().getStrContent());
				datos.put(CTX_SPARK_PRECIO_UNIDAD_VALOR, new BigDecimal(price.getIntUtilization()));
			}
			datos.put(CTX_SPARK_PRECIO_PRECIO, new BigDecimal(price.getFloPrize()));
		}
		
//		datos.put(CTX_SPARK_PRECIO_DESARROLLO, new BigDecimal(spark.getFlPrecioDesarrollo()));
//		datos.put(CTX_SPARK_PRECIO_PRODUCCION, new BigDecimal(spark.getFlPrecioProduccion()));
//		PriceC precio = spark.getPrice();
//		if(precio != null){
//			datos.put(CTX_SPARK_PRECIO_NUM_USUARIOS, new BigDecimal(precio.getIntUsrConcu()));
//			if(precio.getUnit()!=null){
//				datos.put(CTX_SPARK_PRECIO_TIPO_TARIFA, new BigDecimal(1));
//				datos.put(CTX_SPARK_PRECIO_ID_UNIDAD_TIEMPO, new BigDecimal(precio.getUnit().getIntCodUnit()));
//				datos.put(CTX_SPARK_PRECIO_UNIDAD_VALOR, new BigDecimal(precio.getFloAmount()));
//			}
//			if(precio.getUtilization()!=null){
//				datos.put(CTX_SPARK_PRECIO_TIPO_TARIFA, new BigDecimal(2));
//				datos.put(CTX_SPARK_PRECIO_ID_UNIDAD_USO, new BigDecimal(precio.getUtilization().getIntCodUtilization()));
//				datos.put(CTX_SPARK_PRECIO_UNIDAD_VALOR, new BigDecimal(precio.getFloUtilization()));
//			}
//			if(precio.getUnit()==null && precio.getUtilization()==null){
//				datos.put(CTX_SPARK_PRECIO_TIPO_TARIFA, new BigDecimal(3));
//			}
//			
//			datos.put(CTX_SPARK_PRECIO_PRECIO, new BigDecimal(precio.getFloPrize()));		
//			
//		}else{
//			datos.put(CTX_SPARK_PRECIO_TIPO_TARIFA, new BigDecimal(1));
//		}
		
		if(!spark.getBoolGratuitoDes()){
			datos.put(CTX_SPARK_PRECIO_TIPO_TARIFA_DES, new BigDecimal(1));
		}else{
			datos.put(CTX_SPARK_PRECIO_TIPO_TARIFA_DES, new BigDecimal(3));
		}
		
		if(!spark.getBoolGratuitoProd()){
			datos.put(CTX_SPARK_PRECIO_TIPO_TARIFA_PROD, new BigDecimal(1));
		}else{
			datos.put(CTX_SPARK_PRECIO_TIPO_TARIFA_PROD, new BigDecimal(3));
		}
		
		if(spark.getChTrial().equals(new Character('0')))			
			datos.put(CTX_SPARK_TRIAL, "false");
		else
			datos.put(CTX_SPARK_TRIAL, "true");
		
		if(spark.getIntDiasTrial() != null)
			datos.put(CTX_SPARK_DIAS_TRIAL, new BigDecimal(spark.getIntDiasTrial()));
		else{
			datos.put(CTX_SPARK_DIAS_TRIAL, new BigDecimal(0));
		}
		
		datos.put(CTX_SPARK_ICONO, spark.getStrIcono());
		datos.put(CTX_SPARK_BANNER, spark.getStrBanner());
		datos.put(CTX_SPARK_VIDEO, spark.getStrVideo());		
		
		if(spark.getBoolAdquirido())
			datos.put(CTX_SPARK_ADQUIRIDO, new String("1"));
		else
			datos.put(CTX_SPARK_ADQUIRIDO, new String("0"));
		if(spark.getLastStatus() != null){
			datos.put(CTX_SPARK_STATUS_ID, new BigDecimal(spark.getLastStatus().getCnStatus()));
			datos.put(CTX_SPARK_STATUS_NOMBRE, spark.getLastStatus().getStrDescripcion());
		}
		
		datos.put(CTX_SPARK_BORRADO, spark.getChBorrado().toString());
		if(spark.getStrIcono() != null)
			datos.put(CTX_SPARK_ICON_PATH, spark.getStrIconPath());	
			
		
		// Fecha
		if(spark.getLstSparkStatusC() != null && !spark.getLstSparkStatusC().isEmpty()){
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			datos.put(CTX_SPARK_STATUS_FECHA, sdf.format(spark.getLstSparkStatusC().get(0).getDateCambio()));
			
			//datos.put(CTX_SPARK_STATUS_FECHA, Long.toString(spark.getLstSparkStatusC().get(0).getDateCambio().getTime()));
		}
		
		datos.put(CTX_SPARK_USUARIO_DESARROLLADOR_MD5, spark.getUsuarioDesarrollador().getAvatarBuilderUmd5());
		
		if(intNumOpiniones != null){
			datos.put(CTX_SPARK_OPINION_MEDIA_VALORACION, new BigDecimal(Math.round(spark.getFloValoracionMedia())));
			
			datos.put(CTX_SPARK_OPINION_MEDIA_NUM_OPINIONES, new BigDecimal(intNumOpiniones));
		}
		
		String nombreUsuarioDesarrollador = "";
		UsuarioC usuarioDesarrollador = spark.getUsuarioDesarrollador();
		
		if(usuarioDesarrollador.getFlagEntidad().compareToIgnoreCase("1")==0){
			// Mostrar el nombre de la empresa
			nombreUsuarioDesarrollador = usuarioDesarrollador.getEntidadName();
		}else{
			// El desarrollador es un usuario normal
			nombreUsuarioDesarrollador = ((spark.getUsuarioDesarrollador().getName()!=null)?spark.getUsuarioDesarrollador().getName() + " ":" ") + ((spark.getUsuarioDesarrollador().getSurname() != null)?spark.getUsuarioDesarrollador().getSurname():" ");
		}		
		datos.put(CTX_SPARK_USUARIO_DESARROLLADOR_NOMBRE, nombreUsuarioDesarrollador);
		
		if(spark.getUsuarioDesarrollador().getPaypalEmail() != null && !"".equals(spark.getUsuarioDesarrollador().getPaypalEmail()))
			datos.put(CTX_SPARK_USUARIO_DESARROLLADOR_EMAIL_PAYPAL, spark.getUsuarioDesarrollador().getPaypalEmail());
		
		return datos;
	}
	
	/**
	 * Método destinado a rellenar el contexto con un spark
	 * 
	 * @param spark
	 *            Spark a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEG004
	 */
	private static IContexto rellenarContextoPaidSpark(final SparkC spark, final PriceC price, Integer intNumOpiniones) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				PAID_SPARKS_CTX);

		datos.put(CTX_PAID_SPARK_ID, new BigDecimal(spark.getCnSpark()));
		datos.put(CTX_PAID_SPARK_NOMBRE, spark.getStrNombre());
		datos.put(CTX_PAID_SPARK_USER_DESARROLLADOR, new BigDecimal(spark.getUsuarioDesarrollador().getCnUsuario()));
		datos.put(CTX_PAID_SPARK_DESCRIPCION, spark.getStrDescripcion());
		
		datos.put(CTX_PAID_SPARK_VERSION, spark.getStrVersion());
		datos.put(CTX_PAID_SPARK_SHORT_DESC, spark.getStrDescripcionCorta());
		datos.put(CTX_PAID_SPARK_NOVEDADES, spark.getStrNovedadesVersion());
		datos.put(CTX_PAID_SPARK_OTROS_KEY_WORDS, spark.getStrOtrosKeywords());
		datos.put(CTX_PAID_SPARK_EMAIL_SOPORTE, spark.getStrEmailSoporte());
		datos.put(CTX_PAID_SPARK_MARKETING_URL, spark.getStrMarketingUrl());
		
		if(price != null){
			datos.put(CTX_PAID_SPARK_PRECIO_NUM_USUARIOS, new BigDecimal(price.getIntUsrConcu()));
			if(price.getUnit() != null){
				datos.put(CTX_PAID_SPARK_PRECIO_UNIDAD_NOMBRE, price.getUnit().getStrContent());
				datos.put(CTX_PAID_SPARK_PRECIO_UNIDAD_VALOR, new BigDecimal(price.getIntAmount()));
			}else{
				datos.put(CTX_PAID_SPARK_PRECIO_UNIDAD_NOMBRE, price.getUtilization().getStrContent());
				datos.put(CTX_PAID_SPARK_PRECIO_UNIDAD_VALOR, new BigDecimal(price.getIntAmount()));
			}
			datos.put(CTX_PAID_SPARK_PRECIO_PRECIO, new BigDecimal(price.getFloPrize()));
		}
		
//		datos.put(CTX_PAID_SPARK_PRECIO_DESARROLLO, new BigDecimal(spark.getFlPrecioDesarrollo()));
//		datos.put(CTX_PAID_SPARK_PRECIO_PRODUCCION, new BigDecimal(spark.getFlPrecioProduccion()));
		
//		PriceC precio = spark.getPrice();
//		if(precio != null){
//			datos.put(CTX_PAID_SPARK_PRECIO_NUM_USUARIOS, new BigDecimal(precio.getIntUsrConcu()));
//			if(precio.getUnit()!=null){
//				datos.put(CTX_PAID_SPARK_PRECIO_TIPO_TARIFA, new BigDecimal(1));
//				datos.put(CTX_PAID_SPARK_PRECIO_ID_UNIDAD_TIEMPO, new BigDecimal(precio.getUnit().getIntCodUnit()));
//				datos.put(CTX_PAID_SPARK_PRECIO_UNIDAD_VALOR, new BigDecimal(precio.getFloAmount()));
//			}
//			if(precio.getUtilization()!=null){
//				datos.put(CTX_PAID_SPARK_PRECIO_TIPO_TARIFA, new BigDecimal(2));
//				datos.put(CTX_PAID_SPARK_PRECIO_ID_UNIDAD_USO, new BigDecimal(precio.getUtilization().getIntCodUtilization()));
//				datos.put(CTX_PAID_SPARK_PRECIO_UNIDAD_VALOR, new BigDecimal(precio.getFloUtilization()));
//			}
//			if(precio.getUnit()==null && precio.getUtilization()==null){
//				datos.put(CTX_PAID_SPARK_PRECIO_TIPO_TARIFA, new BigDecimal(3));
//			}
//			
//			datos.put(CTX_PAID_SPARK_PRECIO_PRECIO, new BigDecimal(precio.getFloPrize()));
//			
//			
//		}else{
//			datos.put(CTX_PAID_SPARK_PRECIO_TIPO_TARIFA, new BigDecimal(1));
//		}

		if(spark.getChTrial().equals(new Character('0')))			
			datos.put(CTX_PAID_SPARK_TRIAL, "false");
		else
			datos.put(CTX_PAID_SPARK_TRIAL,"true");
		
		if(spark.getIntDiasTrial() != null)
			datos.put(CTX_PAID_SPARK_DIAS_TRIAL, new BigDecimal(spark.getIntDiasTrial()));
		
		datos.put(CTX_PAID_SPARK_ICONO, spark.getStrIcono());
		datos.put(CTX_PAID_SPARK_BANNER, spark.getStrBanner());
		datos.put(CTX_PAID_SPARK_VIDEO, spark.getStrVideo());
		
		if(spark.getBoolAdquirido())
			datos.put(CTX_PAID_SPARK_ADQUIRIDO, new String("1"));
		else
			datos.put(CTX_PAID_SPARK_ADQUIRIDO, new String("0"));
		
		datos.put(CTX_PAID_SPARK_BORRADO, spark.getChBorrado().toString());
		
		datos.put(CTX_PAID_SPARK_ICON_PATH, spark.getStrIconPath());
		
		if(spark.getLstSparkStatusC() != null && !spark.getLstSparkStatusC().isEmpty()){
			// Fecha
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			datos.put(CTX_PAID_SPARK_STATUS_FECHA, sdf.format(spark.getLstSparkStatusC().get(0).getDateCambio()));			
			//datos.put(CTX_PAID_SPARK_STATUS_FECHA, Long.toString(spark.getLstSparkStatusC().get(0).getDateCambio().getTime()));
		}
		
		datos.put(CTX_PAID_SPARK_USUARIO_DESARROLLADOR_MD5, spark.getUsuarioDesarrollador().getAvatarBuilderUmd5());
		
		if(intNumOpiniones != null){
			datos.put(CTX_PAID_SPARK_OPINION_MEDIA_VALORACION, new BigDecimal(spark.getFloValoracionMedia()));
			
			datos.put(CTX_PAID_SPARK_OPINION_MEDIA_NUM_OPINIONES, new BigDecimal(intNumOpiniones));
		}
		
		String nombreUsuarioDesarrollador = (spark.getUsuarioDesarrollador().getName()!=null)?spark.getUsuarioDesarrollador().getName() + " ":" " + ((spark.getUsuarioDesarrollador().getSurname() != null)?spark.getUsuarioDesarrollador().getSurname():" ");
		datos.put(CTX_PAID_SPARK_USUARIO_DESARROLLADOR_NOMBRE, nombreUsuarioDesarrollador);
		
		if(spark.getUsuarioDesarrollador().getPaypalEmail() != null && !"".equals(spark.getUsuarioDesarrollador().getPaypalEmail()))
			datos.put(CTX_PAID_SPARK_USUARIO_DESARROLLADOR_EMAIL_PAYPAL, spark.getUsuarioDesarrollador().getPaypalEmail());
		
		return datos;
	}

	
	
	
}
