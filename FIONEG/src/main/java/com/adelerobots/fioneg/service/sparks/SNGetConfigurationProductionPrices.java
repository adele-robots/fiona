package com.adelerobots.fioneg.service.sparks;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jdom.JDOMException;

import com.adelerobots.fioneg.context.ContextoSparkPrecio;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.SparkManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNGetConfigurationProductionPrices extends ServicioNegocio {
	

	private static Integer CTE_POSICION_USUARIO_ID = 0;
	private static Integer CTE_POSICION_FICHERO_CONF_AVATAR = 1;
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNGetConfigurationProductionPrices.class);
	
	
	public SNGetConfigurationProductionPrices(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029059: Obtener la lista de sparks de una configuración junto con sus posibles precios en producción.");
		Date datInicio = new Date();
		
		BigDecimal bidCodUsuario = datosEntrada.getDecimal(CTE_POSICION_USUARIO_ID);
		Integer intCodUsuario = new Integer(bidCodUsuario.intValue());
		
		String strNombreFichero = datosEntrada.getString(CTE_POSICION_FICHERO_CONF_AVATAR);
		
		
		SparkManager sparkManager = ManagerFactory.getInstance().getSparkManager();
		
		// Cálculo del precio
		//sparkManager.estimateProductionPrice(intCodUsuario,strNombreFichero);
		
		List<SparkC> lstSparks = new ArrayList<SparkC>();
		try {
			lstSparks = sparkManager.getSparksFromXml(intCodUsuario,strNombreFichero);
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
			// se ha producido un error de ejecucion
			ServicioNegocio.rollback(TipoError.TECNICO, ServicioNegocio.CODIGO_ERROR_GENERICO, e.getMessage(), e.getMessage(), null);
		} catch (JDOMException e) {
			e.printStackTrace();
			// se ha producido un error de ejecucion
			ServicioNegocio.rollback(TipoError.TECNICO, ServicioNegocio.CODIGO_ERROR_GENERICO, e.getMessage(), e.getMessage(), null);
		} catch (IOException e) {
			e.printStackTrace();
			// se ha producido un error de ejecucion
			ServicioNegocio.rollback(TipoError.TECNICO, ServicioNegocio.CODIGO_ERROR_GENERICO, e.getMessage(), e.getMessage(), null);
		}
		
		IContexto[] salida = ContextoSparkPrecio.rellenarContexto(lstSparks);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029059: Obtener la lista de sparks de una configuración junto con sus posibles precios en producción. Tiempo total = "
						+ tiempoTotal + "ms");
		
		
		return salida;
	}

}
