package com.adelerobots.fioneg.context;

import java.math.BigDecimal;
import java.util.List;

import com.adelerobots.fioneg.entity.PriceC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

/**
 * Clase destinada a los contextos necesarios para los servicios con funciones
 * sobre precios
 * 
 * @author adele
 * 
 */
public class ContextoPrice {
	
	public static final String PRICE_CTX = "FIONEGN019";	

	public static final String CTX_PRICE_ID = "FIONEG019010";
	public static final String CTX_PRICE_NUM_USUARIOS = "FIONEG019020";
	public static final String CTX_PRICE_ID_UNIDAD_TIEMPO = "FIONEG019030";
	public static final String CTX_PRICE_ID_UNIDAD_USO = "FIONEG019040";
	public static final String CTX_PRICE_CANTIDAD = "FIONEG019050";
	public static final String CTX_PRICE_EUROS = "FIONEG019060";
	public static final String CTX_PRICE_SPARK_ID = "FIONEG019070";
	public static final String CTX_PRICE_NOMBRE_UNIDAD = "FIONEG019080";
	public static final String CTX_PRICE_ES_ACTIVO = "FIONEG019090";
	public static final String CTX_PRICE_ES_USADO = "FIONEG019100";
	
	//****************************************//
	public static final String PRICE_PROD_CTX = "FIONEGN022";	

	public static final String CTX_PRICE_PROD_ID = "FIONEG022010";
	public static final String CTX_PRICE_PROD_NUM_USUARIOS = "FIONEG022020";
	public static final String CTX_PRICE_PROD_ID_UNIDAD_TIEMPO = "FIONEG022030";
	public static final String CTX_PRICE_PROD_ID_UNIDAD_USO = "FIONEG022040";
	public static final String CTX_PRICE_PROD_CANTIDAD = "FIONEG022050";
	public static final String CTX_PRICE_PROD_EUROS = "FIONEG022060";
	public static final String CTX_PRICE_PROD_SPARK_ID = "FIONEG022070";
	public static final String CTX_PRICE_PROD_NOMBRE_UNIDAD = "FIONEG022080";
	public static final String CTX_PRICE_PROD_ES_ACTIVO = "FIONEG022090";
	public static final String CTX_PRICE_PROD_ES_USADO = "FIONEG022100";
	
	/**
	 * Método destinado a rellenar el contexto con un precio
	 * 
	 * @param precio
	 *            Precio a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN019
	 */
	public static IContexto[] rellenarContexto(final PriceC price) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (price != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoPrecio(price);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de precios
	 * 
	 * @param lstPrecios
	 *            Lista de precios a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN019
	 */
	
	public static IContexto[] rellenarContexto(List<PriceC> lstPrecios) {
		IContexto[] salida = null;
		final int iSize = lstPrecios.size();

		if (lstPrecios != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoPrecio(lstPrecios.get(i));
			}
		}		
		
		return salida;
	}
	
	public static IContexto[] rellenarContexto(List<PriceC> lstDevPrices, List<PriceC> lstProdPrices) {
		IContexto[] salida = null;
		final int iSizeDev = lstDevPrices.size();
		final int iSizeProd = lstProdPrices.size();

		salida = new IContexto[iSizeDev+iSizeProd];
		if (lstDevPrices != null) {			
			for (int i = 0; i < iSizeDev; i++) {
				salida[i] = rellenarContextoPrecio(lstDevPrices.get(i));
			}
		}		
				
		
		if (lstProdPrices != null) {		
			for (int i = iSizeDev; i < iSizeDev+iSizeProd; i++) {
				salida[i] = rellenarContextoPrecioProd(lstProdPrices.get(i-iSizeDev));
			}
		}
		
		return salida;
	}
	
	
	/**
	 * Método destinado a rellenar el contexto con un precio
	 * 
	 * @param precio
	 *            Precio a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN019
	 */
	private static IContexto rellenarContextoPrecio(final PriceC precio) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				PRICE_CTX);

		datos.put(CTX_PRICE_ID, new BigDecimal(precio.getIntCodPrice()));
		datos.put(CTX_PRICE_NUM_USUARIOS, new BigDecimal(precio.getIntUsrConcu()));
		if(precio.getUnit() != null){
			datos.put(CTX_PRICE_ID_UNIDAD_TIEMPO, new BigDecimal(precio.getUnit().getIntCodUnit()));
			datos.put(CTX_PRICE_CANTIDAD, new BigDecimal(precio.getIntAmount().toString()));
			datos.put(CTX_PRICE_NOMBRE_UNIDAD,precio.getUnit().getStrContent());
		}
		if(precio.getUtilization() != null){
			datos.put(CTX_PRICE_ID_UNIDAD_USO, new BigDecimal(precio.getUtilization().getIntCodUtilization()));
			datos.put(CTX_PRICE_CANTIDAD, new BigDecimal(precio.getIntUtilization().toString()));
			datos.put(CTX_PRICE_NOMBRE_UNIDAD, precio.getUtilization().getStrContent());
		}		
		datos.put(CTX_PRICE_EUROS, new BigDecimal(precio.getFloPrize().toString()));
		datos.put(CTX_PRICE_SPARK_ID, new BigDecimal(precio.getSpark().getCnSpark()));	
		datos.put(CTX_PRICE_ES_ACTIVO, precio.getChActivo().toString());
		datos.put(CTX_PRICE_ES_USADO, (precio.getBoolUsado()?"1":"0"));
		
		return datos;
	}
	
	/**
	 * Método destinado a rellenar el contexto con un precio
	 * 
	 * @param precio
	 *            Precio a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN019
	 */
	private static IContexto rellenarContextoPrecioProd(final PriceC precio) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				PRICE_PROD_CTX);

		datos.put(CTX_PRICE_PROD_ID, new BigDecimal(precio.getIntCodPrice()));
		datos.put(CTX_PRICE_PROD_NUM_USUARIOS, new BigDecimal(precio.getIntUsrConcu()));
		if(precio.getUnit() != null){
			datos.put(CTX_PRICE_PROD_ID_UNIDAD_TIEMPO, new BigDecimal(precio.getUnit().getIntCodUnit()));
			datos.put(CTX_PRICE_PROD_CANTIDAD, new BigDecimal(precio.getIntAmount().toString()));
			datos.put(CTX_PRICE_PROD_NOMBRE_UNIDAD,precio.getUnit().getStrContent());
		}
		if(precio.getUtilization() != null){
			datos.put(CTX_PRICE_PROD_ID_UNIDAD_USO, new BigDecimal(precio.getUtilization().getIntCodUtilization()));
			datos.put(CTX_PRICE_PROD_CANTIDAD, new BigDecimal(precio.getIntUtilization().toString()));
			datos.put(CTX_PRICE_PROD_NOMBRE_UNIDAD, precio.getUtilization().getStrContent());
		}		
		datos.put(CTX_PRICE_PROD_EUROS, new BigDecimal(precio.getFloPrize().toString()));
		datos.put(CTX_PRICE_PROD_SPARK_ID, new BigDecimal(precio.getSpark().getCnSpark()));	
		datos.put(CTX_PRICE_PROD_ES_ACTIVO, precio.getChActivo().toString());
		datos.put(CTX_PRICE_PROD_ES_USADO, (precio.getBoolUsado()?"1":"0"));
		
		return datos;
	}	
	

}
