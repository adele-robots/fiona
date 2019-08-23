package com.adelerobots.fioneg.service.sparks;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jdom.JDOMException;

import com.adelerobots.fioneg.context.ContextoSparksPreciosTotales;
import com.adelerobots.fioneg.entity.HostingC;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.SparkManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNEstimateProductionPrice extends ServicioNegocio {
	
	private static Integer CTE_POSICION_USUARIO_ID = 0;
	private static Integer CTE_POSICION_FICHERO_CONF_AVATAR = 1;
	private static Integer CTE_POSICION_NUM_USUARIOS = 2;
	private static Integer CTE_POSICION_ID_UNIDAD_TIEMPO = 3;
	private static Integer CTE_POSICION_RESOLUTION = 4;
	private static Integer CTE_POSICION_HIGH_AVAILABILITY = 5;
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNEstimateProductionPrice.class);
	
	
	public SNEstimateProductionPrice(){
		super();
	}	
	

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029045: Calcular el precio aproximado del paso de un avatar a producción.");
		Date datInicio = new Date();
		
		BigDecimal bidCodUsuario = datosEntrada.getDecimal(CTE_POSICION_USUARIO_ID);
		Integer intCodUsuario = new Integer(bidCodUsuario.intValue());
		
		String strNombreFichero = datosEntrada.getString(CTE_POSICION_FICHERO_CONF_AVATAR);
		
		BigDecimal bidNumUsuarios = datosEntrada.getDecimal(CTE_POSICION_NUM_USUARIOS);
		Integer intNumUsuarios = new Integer(bidNumUsuarios.intValue());
		
		BigDecimal bidIdUnidadTiempo = datosEntrada.getDecimal(CTE_POSICION_ID_UNIDAD_TIEMPO);
		Integer intIdUnidadTiempo = new Integer(bidIdUnidadTiempo.intValue());
		
		BigDecimal bidIdResolution = datosEntrada.getDecimal(CTE_POSICION_RESOLUTION);
		Integer intIdResolution = new Integer(bidIdResolution.intValue());
		
		BigDecimal bidHighAvailability = datosEntrada.getDecimal(CTE_POSICION_HIGH_AVAILABILITY);
		Integer intHighAvailability = new Integer(bidHighAvailability.intValue());
		
		
		SparkManager sparkManager = ManagerFactory.getInstance().getSparkManager();
		
		// Cálculo del precio
		//sparkManager.estimateProductionPrice(intCodUsuario,strNombreFichero);
		
		List<SparkC> lstSparks = new ArrayList<SparkC>();
		List <SparkC> lstSparksUso = new ArrayList <SparkC>();
		Float precioTotalTime = new Float(0);
		Float precioTotalUso = new Float(0);
		float precioHosting = new Float(0);
		
		try {
			lstSparks = sparkManager.getSparksFromXml(intCodUsuario,strNombreFichero);
			
			lstSparksUso = sparkManager.getSparksWithOnlyUseProductionFee(lstSparks);
			
			precioTotalTime = sparkManager.calculateSparksTimeFeeProductionPrice(lstSparks,intNumUsuarios,intIdUnidadTiempo);
			precioTotalUso = sparkManager.calculateSparksUseFeeProductionPrice(lstSparksUso,intNumUsuarios);
			HostingC hosting = sparkManager.getPrecioHosting(intIdUnidadTiempo, intNumUsuarios, intIdResolution.toString(), (intHighAvailability.intValue()==1)?'1':'0');
			precioHosting = hosting.getFloFee();
			/* Cambio introducido tras incluir números de usuarios múltiplos de 20 */
			if(intNumUsuarios > 20){
				Integer factor = intNumUsuarios/20;
				precioHosting = precioHosting * factor;
			}
			
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
			// se ha producido un error de ejecucion
			ServicioNegocio.rollback(TipoError.TECNICO, 801, e.getMessage(), e.getMessage(), null);
		} catch (JDOMException e) {
			e.printStackTrace();
			// se ha producido un error de ejecucion
			ServicioNegocio.rollback(TipoError.TECNICO, 802, e.getMessage(), e.getMessage(), null);
		} catch (IOException e) {
			e.printStackTrace();
			// se ha producido un error de ejecucion
			ServicioNegocio.rollback(TipoError.TECNICO, 0, e.getMessage(), e.getMessage(), null);
		}
		
		//IContexto[] salida = ContextoListadoPreciosSparks.rellenarContexto(lstSparks);
		IContexto[] salida = ContextoSparksPreciosTotales.rellenarContexto(lstSparks, lstSparksUso, precioTotalTime, precioTotalUso,precioHosting);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029045: Calcular el precio aproximado del paso de un avatar a producción. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
