package com.adelerobots.fioneg.service.sparks;

import java.math.BigDecimal;
import java.util.Date;

import com.adelerobots.fioneg.context.ContextoSparks;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.SparkManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;


/**
 * Servicio de Negocio fachada para modificar la información
 * relativa a un determinado spark
 * 
 * @author adele
 * 
 */
public class SNSaveSpark extends ServicioNegocio {
	
	private static final int CTE_POSICION_ID_SPARK = 0; // Not null 
	private static final int CTE_POSICION_NOMBRE_SPARK = 1; // Not null
	private static final int CTE_POSICION_DESCRIPCION_SPARK = 2; 
	private static final int CTE_POSICION_VERSION_SPARK = 3;
	private static final int CTE_POSICION_DESC_CORTA_SPARK = 4;
	private static final int CTE_POSICION_NOVEDADES_SPARK = 5;
	private static final int CTE_POSICION_OTROS_KEYWORDS = 6;
	private static final int CTE_POSICION_EMAIL_SOPORTE = 7;
	private static final int CTE_POSICION_MARKETING_URL = 8;	
	private static final int CTE_POSICION_ES_TRIAL = 9; // Not null
	private static final int CTE_POSICION_DIAS_TRIAL = 10; // Not null if CTE_POSICION_ES_TRIAL=1
	private static final int CTE_POSICION_ICONO = 11; 
	private static final int CTE_POSICION_BANNER = 12;
	private static final int CTE_POSICION_VIDEO = 13;
	private static final int CTE_POSICION_KEYWORDS_IDS = 14;
	private static final int CTE_POSICION_COD_EULA = 15;	
	private static final int CTE_POSICION_ID_TARIFA_DES = 16;
	private static final int CTE_POSICION_ID_TARIFA_PROD = 17;
	private static final int CTE_POSICION_EMAIL_PAYPAL = 18;
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNSaveSpark.class);

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029033: Modificar la información asociada a un spark.");
		Date datInicio = new Date();
		
		BigDecimal bidCodSpark = datosEntrada.getDecimal(CTE_POSICION_ID_SPARK);
		
		String strNombreSpark = datosEntrada.getString(CTE_POSICION_NOMBRE_SPARK);
		String strDescSpark = datosEntrada.getString(CTE_POSICION_DESCRIPCION_SPARK);
		String strVersion = datosEntrada.getString(CTE_POSICION_VERSION_SPARK);
		String strDescCorta = datosEntrada.getString(CTE_POSICION_DESC_CORTA_SPARK);
		String strNovedades = datosEntrada.getString(CTE_POSICION_NOVEDADES_SPARK);
		String strOtrosKeyw = datosEntrada.getString(CTE_POSICION_OTROS_KEYWORDS);
		String strEmailSoporte = datosEntrada.getString(CTE_POSICION_EMAIL_SOPORTE);
		String strMarketingURL = datosEntrada.getString(CTE_POSICION_MARKETING_URL);
		
				
		String chEsTrial = datosEntrada.getString(CTE_POSICION_ES_TRIAL);
		if(chEsTrial.compareToIgnoreCase("false")==0)
			chEsTrial = "0";
		else
			chEsTrial = "1";
		
		BigDecimal bidNumDiastrial = datosEntrada.getDecimal(CTE_POSICION_DIAS_TRIAL);
		
		String strIcono = datosEntrada.getString(CTE_POSICION_ICONO);
		String strBanner = datosEntrada.getString(CTE_POSICION_BANNER);
		String strVideo = datosEntrada.getString(CTE_POSICION_VIDEO);
		
		String strIdsKeyWords = datosEntrada.getString(CTE_POSICION_KEYWORDS_IDS);		
		
		BigDecimal bidCodEula = datosEntrada.getDecimal(CTE_POSICION_COD_EULA);		
		
		String strIdTarifaDes = datosEntrada.getString(CTE_POSICION_ID_TARIFA_DES);
		
		String strIdTarifaProd = datosEntrada.getString(CTE_POSICION_ID_TARIFA_PROD);
		
		String strPaypalEmail = datosEntrada.getString(CTE_POSICION_EMAIL_PAYPAL);
		
		
		// Convertimos el código a Integer
		Integer intCodSpark = new Integer(bidCodSpark.intValue());
				
		Integer intDiasTrial = new Integer(bidNumDiastrial.intValue());
		Integer intCodEula = null;
		if(bidCodEula != null)
			intCodEula = new Integer(bidCodEula.intValue());
		
		// spark creado a partir de los nuevos datos introducidos por el usuario
		SparkC sparkAModificar = new SparkC(intCodSpark, strNombreSpark, strDescSpark, strVersion, strDescCorta, strNovedades, 
				strOtrosKeyw, strEmailSoporte, strMarketingURL, chEsTrial.charAt(0), intDiasTrial, 
				strIcono, strBanner, strVideo);
		
		SparkManager sparkManager = ManagerFactory.getInstance().getSparkManager();	
		
		SparkC spark = sparkManager.modificarSpark(sparkAModificar, strIdsKeyWords, intCodEula, strIdTarifaDes, strIdTarifaProd, strPaypalEmail);			
		
		
		IContexto[] salida = ContextoSparks.rellenarContexto(spark);
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029033: Modificar la información asociada al spark con id" + intCodSpark +". Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
