package com.adelerobots.fioneg.service.sparks;





import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoSparks;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.SparkManager;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;



/**
 * Servicio de Negocio fachada para recuperar una lista de los N sparks
 * más vendidos, o de los últimos publicados, dividiéndolos en gratuitos
 * y de pago 
 * 
 * @author adele
 * 
 */
public class SNListSparksByCriteria extends ServicioNegocio {
	
	private static final int CTE_POSICION_ID_CRITERIO = 0;
	private static final int CTE_POSICION_ID_USER = 1;
	
	// Criterios
	private static final int CRITERIO_TOP = 0;
	private static final int CRITERIO_LATEST = 1;
	
	// Status que actualmente debería ser el último del spark
	// publicado
	private static final int STATUS_AVAILABLE = 6;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNListSparksByCriteria.class);

	
	public SNListSparksByCriteria(){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029023: Listar los N sparks más vendidos, o los N últimos sparks publicados");
		Date datInicio = new Date();
		
		BigDecimal bidIdCriterio = datosEntrada.getDecimal(CTE_POSICION_ID_CRITERIO);
		
		BigDecimal bidCodUsuario = datosEntrada.getDecimal(CTE_POSICION_ID_USER);
		
		Integer intCodCriterio = new Integer(bidIdCriterio.intValue());
		Integer intCodUsuario = null;
		
		if(bidCodUsuario != null){
			intCodUsuario = new Integer(bidCodUsuario.intValue());
		}
		
		
		SparkManager manager = ManagerFactory.getInstance().getSparkManager();
		
		// Calculamos número de sparks desarrollados por el usuario
		List <SparkC> lstUserDevelopedSparks = manager.getListSparksByDeveloper(intCodUsuario);
		
		Integer intLstDevelopedSparksSize = new Integer(0);
		if(lstUserDevelopedSparks != null && lstUserDevelopedSparks.size()>0){
			intLstDevelopedSparksSize =lstUserDevelopedSparks.size();
		}
		
		//List <SparkC> lstFreeSparks = null, lstPaidSparks = null;
		List <SparkC> lstSparks = null;
		// Últimos sparks subidos
		if(intCodCriterio == CRITERIO_LATEST){
		
			// Sparks Gratuitos
			//lstFreeSparks = manager.getLastStatusOrderedByDate(STATUS_AVAILABLE, 1, intCodUsuario);
			// Sparks de pago
			//lstPaidSparks = manager.getLastStatusOrderedByDate(STATUS_AVAILABLE, 0, intCodUsuario);
			
			lstSparks = manager.getLastStatusOrderedByDate(STATUS_AVAILABLE,intCodUsuario);
			
		}else if(intCodCriterio == CRITERIO_TOP){
			// Sparks Gratuitos
			// lstFreeSparks = manager.getTopSalesSparks(1, intCodUsuario);
			// Sparks de pago
			//lstPaidSparks = manager.getTopSalesSparks(0, intCodUsuario);
			lstSparks = manager.getTopSalesSparks(intCodUsuario);
		}else{
			// Recuperar sparks básicos
			List<String> coresparks = Arrays.asList(Constantes.getCORE_SPARKS().split("\\s*,\\s*"));
			lstSparks = manager.getSparks(coresparks,intCodUsuario);
			//lstFreeSparks = manager.getSparks(coresparks,1);
			//lstPaidSparks = manager.getSparks(coresparks,0);
			
		}
		
		// Recuperamos los banners aleatorios
		// TODO: Colocar esto en un properties o similar
		List<SparkC> lstBanners = manager.getRandomBanners(5);
		
		
		IContexto[] salida = ContextoSparks.rellenarContexto(lstSparks,lstBanners, intLstDevelopedSparksSize);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029023: Listar todos los sparks. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
