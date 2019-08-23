package com.adelerobots.fioneg.service.sparks;





import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoSparks;
import com.adelerobots.fioneg.entity.PriceC;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.entity.opinion.OpinionC;
import com.adelerobots.fioneg.entity.usuariospark.UsuarioSparkC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.SparkManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;



/**
 * Servicio de Negocio fachada para recuperar la información de un spark
 * concreto
 * 
 * @author adele
 * 
 */
public class SNGetSparkInformation extends ServicioNegocio {
	
	private static final int CTE_POSICION_ID_SPARK = 0;
	private static final int CTE_POSICION_ID_USER = 1;
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNGetSparkInformation.class);

	
	public SNGetSparkInformation(){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029016: Devolver información de un spark concreto.");
		Date datInicio = new Date();
		
		BigDecimal bidIdSpark = datosEntrada
		.getDecimal(CTE_POSICION_ID_SPARK);
		
		BigDecimal bidCodUsuario = datosEntrada.getDecimal(CTE_POSICION_ID_USER);
		
		// Convertimos el código a Integer
		Integer intCodSpark = new Integer(bidIdSpark.intValue());
		Integer intCodUsuario = null;
		if(bidCodUsuario != null){
			intCodUsuario = new Integer(bidCodUsuario.intValue());
		}
		
		// Buscamos el spark
		SparkManager manager = ManagerFactory.getInstance().getSparkManager();	
				
		
		SparkC spark= null;
		try {
			spark = manager.getSpark(intCodSpark, intCodUsuario);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Buscamos la lista de opiniones asociadas al spark
		List <OpinionC> lstOpiniones = manager.getNSparkOpinions(intCodSpark, 5);
		
		// Buscamos el precio seleccionado por el usuario indicado para este spark
		PriceC precioSeleccionado = null;
		if(intCodUsuario != null){
			UsuarioSparkC usuarioSpark = manager.getUsuarioSpark(intCodUsuario, intCodSpark);
			
			if(usuarioSpark != null){
				precioSeleccionado = usuarioSpark.getPrice();
			}
		}
		
		// Buscamos los sparks desarrollados por el mismo desarrollador
		List<Integer> idsExcluidos = new ArrayList<Integer>();
		idsExcluidos.add(intCodSpark);
		List <SparkC> lstSparksRelacionados = manager.getListSparksByDeveloper(spark.getUsuarioDesarrollador().getCnUsuario(),idsExcluidos,new Integer(3));
		
			
		// Calcular opinión promedio del spark
		Float valoracionMedia = manager.calcularOpinionPromedio(lstOpiniones);
		spark.setFloValoracionMedia(valoracionMedia);
		
		Integer intNumTotalOpiniones = (manager.getNSparkOpinions(intCodSpark, null)).size();
		
		IContexto[] salida = ContextoSparks.rellenarContexto(spark, lstOpiniones,precioSeleccionado, intNumTotalOpiniones,lstSparksRelacionados);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029016: Devolver información de un spark concreto. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
