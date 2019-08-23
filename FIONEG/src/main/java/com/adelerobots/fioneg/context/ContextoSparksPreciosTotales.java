package com.adelerobots.fioneg.context;

import java.math.BigDecimal;
import java.util.List;

import com.adelerobots.fioneg.entity.SparkC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IRegistro;

public class ContextoSparksPreciosTotales {
	public static final String SPARK_PRECIO_CTX = "FIONEGN025";
	
	public static final String CTX_SPT_PRECIO_TOTAL_TIEMPO = "FIONEG025010";
	public static final String CTX_SPT_PRECIO_TOTAL_USO = "FIONEG025020";
	public static final String CTX_SPT_PRECIO_TOTAL_HOSTING = "FIONEG025025";
	
	public static final String CTX_SPT_REG_TIEMPO = "FIONEG025030";
	public static final String CTX_SPT_REG_TIEMPO_SPARK_ID = "FIONEG025030010";
	public static final String CTX_SPT_REG_TIEMPO_SPARK_NOMBRE = "FIONEG025030020";
	public static final String CTX_SPT_REG_TIEMPO_PRECIO_CALC = "FIONEG025030030";
	
	public static final String CTX_SPT_REG_USO = "FIONEG025040";
	public static final String CTX_SPT_REG_USO_SPARK_ID = "FIONEG025040010";
	public static final String CTX_SPT_REG_USO_SPARK_NOMBRE = "FIONEG025040020";
	public static final String CTX_SPT_REG_USO_PRECIO_CALC = "FIONEG025040030";
	
	public static IContexto[] rellenarContexto(List <SparkC> sparksTiempo, List <SparkC> sparksUso, Float precioTotalTiempo, Float precioTotalUso, Float precioHosting){
		IContexto[] salida = null;
		// Rellenar contexto de salida
		
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoSparksPreciosTotals(sparksTiempo, sparksUso, precioTotalTiempo, precioTotalUso, precioHosting);
		
		return salida;
	}
	
	private static IContexto rellenarContextoSparksPreciosTotals(List <SparkC> sparksTiempo, List <SparkC> sparksUso, Float precioTotalTiempo, Float precioTotalUso,
			Float precioHosting){
		
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				SPARK_PRECIO_CTX);
		
		datos.put(CTX_SPT_PRECIO_TOTAL_TIEMPO, new BigDecimal(precioTotalTiempo));
		datos.put(CTX_SPT_PRECIO_TOTAL_USO, new BigDecimal(precioTotalUso));
		datos.put(CTX_SPT_PRECIO_TOTAL_HOSTING, new BigDecimal(precioHosting));
		
		// Listado de tiempo
		IRegistro[] registrosTiempo = null;
		if (sparksTiempo != null) {
			// Trapicheo para incluir el hosting como spark de tiempo :-S
			final int iSizeTiempo = sparksTiempo.size()+1;
		
		
			registrosTiempo = new IRegistro[iSizeTiempo];
			for (int i = 0; i < sparksTiempo.size(); i++) {		
				SparkC spark = sparksTiempo.get(i);
				IRegistro registroSparkPrecio = ContextoFactory.getInstance().getRegistro(datos, CTX_SPT_REG_TIEMPO);
				registroSparkPrecio.put(CTX_SPT_REG_TIEMPO_SPARK_ID, new BigDecimal(spark.getCnSpark()));
				registroSparkPrecio.put(CTX_SPT_REG_TIEMPO_SPARK_NOMBRE, spark.getStrNombre());
				registroSparkPrecio.put(CTX_SPT_REG_TIEMPO_PRECIO_CALC, new BigDecimal(spark.getFloPrecioSeleccionadoProd()));
				
				registrosTiempo[i] = registroSparkPrecio;
			}
			// Trapicheo para incluir el hosting como spark de tiempo :-S
			IRegistro registroSparkPrecio = ContextoFactory.getInstance().getRegistro(datos, CTX_SPT_REG_TIEMPO);
			registroSparkPrecio.put(CTX_SPT_REG_TIEMPO_SPARK_ID, new BigDecimal(0));
			registroSparkPrecio.put(CTX_SPT_REG_TIEMPO_SPARK_NOMBRE, "Hosting");
			registroSparkPrecio.put(CTX_SPT_REG_TIEMPO_PRECIO_CALC, new BigDecimal(precioHosting));
			registrosTiempo[iSizeTiempo-1] = registroSparkPrecio;
			// FIN Trapicheo para incluir el hosting como spark de tiempo :-S
		}else{
			registrosTiempo = new IRegistro[1];
			IRegistro registroSparkPrecio = ContextoFactory.getInstance().getRegistro(datos, CTX_SPT_REG_TIEMPO);
			registroSparkPrecio.put(CTX_SPT_REG_TIEMPO_SPARK_ID, new BigDecimal(0));
			registroSparkPrecio.put(CTX_SPT_REG_TIEMPO_SPARK_NOMBRE, "Hosting");
			registroSparkPrecio.put(CTX_SPT_REG_TIEMPO_PRECIO_CALC, new BigDecimal(precioHosting));
			registrosTiempo[0] = registroSparkPrecio;
		}
		
		datos.put(CTX_SPT_REG_TIEMPO, registrosTiempo);
		
		// Listado de uso
		IRegistro[] registrosUso = null;
		if (sparksUso != null) {
			final int iSizeUso = sparksUso.size();
		
		
			registrosUso = new IRegistro[iSizeUso];
			for (int i = 0; i < iSizeUso; i++) {		
				SparkC spark = sparksUso.get(i);
				IRegistro registroSparkPrecio = ContextoFactory.getInstance().getRegistro(datos, CTX_SPT_REG_USO);
				registroSparkPrecio.put(CTX_SPT_REG_USO_SPARK_ID, new BigDecimal(spark.getCnSpark()));
				registroSparkPrecio.put(CTX_SPT_REG_USO_SPARK_NOMBRE, spark.getStrNombre());
				registroSparkPrecio.put(CTX_SPT_REG_USO_PRECIO_CALC, new BigDecimal(spark.getFloPrecioSeleccionadoProd()));
				
				registrosUso[i] = registroSparkPrecio;
			}
		}	
		
		datos.put(CTX_SPT_REG_USO, registrosUso);
		
		return datos;
		
	}
	
	
	
}
