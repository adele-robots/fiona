package com.adelerobots.fioneg.context;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import com.adelerobots.fioneg.entity.PriceC;
import com.adelerobots.fioneg.entity.SparkC;
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
public class ContextoListadoPreciosSparks {
	public static final String PRECIOS_SPARKS_CTX = "FIONEGN016";	

	public static final String CTX_SPARK_ID = "FIONEG016010";
	public static final String CTX_SPARK_NOMBRE = "FIONEG016020";
		
	public static final String CTX_SPARK_PRECIO_NUM_USUARIOS = "FIONEG016030";
	public static final String CTX_SPARK_PRECIO_PRECIO = "FIONEG016040";	
	public static final String CTX_SPARK_PRECIO_UNIDAD_VALOR = "FIONEG016050";	
	public static final String CTX_SPARK_PRECIO_ID_UNIDAD_USO = "FIONEG016060";
	public static final String CTX_SPARK_PRECIO_NOMBRE_UNIDAD_USO = "FIONEG016070";
	public static final String CTX_SPARK_PRECIO_TOTAL_MENSUAL = "FIONEG016080";
	public static final String CTX_SPARK_PRECIO_TOTAL_USO = "FIONEG016090";
	
	private static final int UNIT_DIAS = 0;
	private static final int UNIT_MESES = 1;
	private static final int UNIT_ANIOS = 2;
	
	private static Float totalMensual;
	private static Float totalPorUso ;
		


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
			salida[0] = rellenarContextoSpark(spark);
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
			totalMensual = new Float(0);
			totalPorUso = new Float(0);
			salida = new IContexto[iSize + 1];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoSpark(lstSparks.get(i));
			}
			salida[iSize] = rellenarTotales();
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
	private static IContexto rellenarContextoSpark(final SparkC spark) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				PRECIOS_SPARKS_CTX);		

		datos.put(CTX_SPARK_ID, new BigDecimal(spark.getCnSpark()));
		datos.put(CTX_SPARK_NOMBRE, spark.getStrNombre());
		// TODO: Modificar según la nueva estructura de precios
//		PriceC precio = spark.getPrice();
//		if(precio != null){
//			datos.put(CTX_SPARK_PRECIO_NUM_USUARIOS, new BigDecimal(precio.getIntUsrConcu()));			
//			if(precio.getUtilization()!=null){
//				datos.put(CTX_SPARK_PRECIO_ID_UNIDAD_USO, new BigDecimal(precio.getUtilization().getIntCodUtilization()));
//				datos.put(CTX_SPARK_PRECIO_NOMBRE_UNIDAD_USO, precio.getUtilization().getStrContent());
//				datos.put(CTX_SPARK_PRECIO_UNIDAD_VALOR, new BigDecimal(precio.getFloUtilization()));
//				datos.put(CTX_SPARK_PRECIO_PRECIO, new BigDecimal(precio.getFloPrize()));
//				totalPorUso += precio.getFloPrize();
//			}else{
//				// Cálculo de precios por mes
//				Float precioMensual = calcularPrecioMensual(precio);
//				datos.put(CTX_SPARK_PRECIO_PRECIO, new BigDecimal(precioMensual));
//				totalMensual += precioMensual;
//			}
//			
//		}
		
		return datos;
	}
	
	private static IContexto rellenarTotales(){
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				PRECIOS_SPARKS_CTX);
		
		datos.put(CTX_SPARK_PRECIO_TOTAL_MENSUAL, new BigDecimal(totalMensual));
		datos.put(CTX_SPARK_PRECIO_TOTAL_USO, new BigDecimal(totalPorUso));
		
		return datos;
	}
	
	/**
	 * Método que permite calcular el precio mensual de un spark
	 * 
	 * @param price Entidad precio que se usará para hacer los cálculos
	 * mensuales
	 * 
	 * @return Se devolverá el precio calculado del spark
	 */
	public static Float calcularPrecioMensual(PriceC price){
		Float precioMensual = new Float(0);
		
		// Calcular mensualidad en función de la unidad
		// fijada por el desarrollador
		Integer numUnidad = price.getIntAmount();
		Float precio = price.getFloPrize();
		if(price.getUnit() != null){
			switch(price.getUnit().getIntCodUnit()){
			
				case UNIT_DIAS:
					precioMensual += (30*precio)/numUnidad;
					break;
				case UNIT_MESES:				
					precioMensual += precio/numUnidad;
					break;
				case UNIT_ANIOS:
					precioMensual += precio/(numUnidad*12);				
					break;		
			
			}
		}
		
		return precioMensual;
	}
		
	
}
