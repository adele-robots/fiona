package com.adelerobots.fioneg.context;

import java.math.BigDecimal;
import java.util.List;

import com.adelerobots.fioneg.entity.PriceC;
import com.adelerobots.fioneg.entity.SparkC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IRegistro;

/**
 * Clase destinada a los contextos necesarios para los servicios con funciones
 * sobre sparks y el listado de precios de producción
 * 
 * @author adele
 * 
 */
public class ContextoSparkPrecio {
	
	public static final String SPARK_PRECIO_CTX = "FIONEGN023";
	
	public static final String CTX_SPARK_PRECIO_SPARK_ID = "FIONEG023010";
	public static final String CTX_SPARK_PRECIO_SPARK_NOMBRE = "FIONEG023020";
	
	public static final String CTX_SPARK_PRECIO_PRECIO_REG = "FIONEG023030";
	public static final String CTX_SPARK_PRECIO_PRECIO_ID = "FIONEG023030010";
	public static final String CTX_SPARK_PRECIO_PRECIO_DESC = "FIONEG023030020";
	
	/**
	 * Método destinado a rellenar el contexto con un spark y el registro de sus
	 * precios asociados
	 * 
	 * @param spark
	 *            spark a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN023
	 */
	public static IContexto[] rellenarContexto(final SparkC spark) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (spark != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoSparkPrecio(spark);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de sparks
	 * con sus precios de producción asociados
	 * 
	 * @param lstSparks
	 *            Lista de sparks a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN023
	 */
	
	public static IContexto[] rellenarContexto(List<SparkC> lstSparks) {
		IContexto[] salida = null;
		final int iSize = lstSparks.size();

		if (lstSparks != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoSparkPrecio(lstSparks.get(i));
			}
		}		
		
		return salida;
	}	
	
	/**
	 * Método destinado a rellenar el contexto con un objeto de tipo
	 * SparkC
	 * 
	 * @param spark
	 *            spark a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN023
	 */
	private static IContexto rellenarContextoSparkPrecio(final SparkC spark) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				SPARK_PRECIO_CTX);
		
		datos.put(CTX_SPARK_PRECIO_SPARK_ID, new BigDecimal(spark.getCnSpark()));
		datos.put(CTX_SPARK_PRECIO_SPARK_NOMBRE, spark.getStrNombre());
		
		List <PriceC> lstPrices = spark.getProductionPrices();		
		
		IRegistro[] registros = null;
		if (lstPrices != null) {
			final int iSize = lstPrices.size();

		
			registros = new IRegistro[iSize];
			for (int i = 0; i < iSize; i++) {
				PriceC price = lstPrices.get(i);
				IRegistro registroPrecio = ContextoFactory.getInstance().getRegistro(datos, CTX_SPARK_PRECIO_PRECIO_REG);
				registroPrecio.put(CTX_SPARK_PRECIO_PRECIO_ID, new BigDecimal(price.getIntCodPrice()));
				if(price.getUnit() != null)
					registroPrecio.put(CTX_SPARK_PRECIO_PRECIO_DESC, price.getIntUsrConcu() + " users " + price.getIntAmount() + " " + price.getUnit().getStrContent() + " " + price.getFloPrize() + " $" );
				else
					registroPrecio.put(CTX_SPARK_PRECIO_PRECIO_DESC, price.getIntUsrConcu() + " users " + price.getIntUtilization() + " " + price.getUtilization().getStrContent() + " " + price.getFloPrize() + " $");
				
				registros[i] = registroPrecio;
			}
		}	
		
		datos.put(CTX_SPARK_PRECIO_PRECIO_REG, registros);
		
		
		return datos;
	}	

}
