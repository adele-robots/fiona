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
 * Servicio de Negocio fachada para aniadir una opinion de
 * un usuario sobre un spark
 * 
 * @author adele
 * 
 */
public class SNSendSparkOpinion extends ServicioNegocio {
	
	private static final int CTE_POSICION_ID_SPARK = 0;
	private static final int CTE_POSICION_ID_USUARIO = 1;
	private static final int CTE_POSICION_VALORACION = 2;
	private static final int CTE_POSICION_DESCRIPCION = 3;
	private static final int CTE_POSICION_TITULO = 4;
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNSendSparkOpinion.class);

	
	public SNSendSparkOpinion(){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029024: Creación de opinión de usuario sobre un spark.");
		Date datInicio = new Date();
		
		BigDecimal bidCodSpark = datosEntrada
		.getDecimal(CTE_POSICION_ID_SPARK);
		
		BigDecimal bidCodUsuario = datosEntrada
		.getDecimal(CTE_POSICION_ID_USUARIO);
		
		BigDecimal bidValoracion = datosEntrada
		.getDecimal(CTE_POSICION_VALORACION);
		
		String strDescripcion = datosEntrada.getString(CTE_POSICION_DESCRIPCION);
		
		String strTitulo = datosEntrada.getString(CTE_POSICION_TITULO);
		
		// Convertimos el código a Integer
		Integer intCodSpark = new Integer(bidCodSpark.intValue());
		Integer intCodUsuario = new Integer(bidCodUsuario.intValue());
		Integer intValoracion = null;
		
		if(bidValoracion != null){
			intValoracion = new Integer(bidValoracion.intValue());
		}else{
			intValoracion = new Integer(0);
		}
		
		// Cambiamos el estado del spark que corresponda
		SparkManager manager = ManagerFactory.getInstance().getSparkManager();		
		
		manager.addOpinion(new Date(), intValoracion, strDescripcion, strTitulo, intCodSpark, intCodUsuario);		
		
		SparkC spark = new SparkC();
		try {
			spark = manager.getSpark(intCodSpark,intCodUsuario);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IContexto[] salida = ContextoSparks.rellenarContexto(spark);		
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029024: Creación de opinión de usuario sobre un spark. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
