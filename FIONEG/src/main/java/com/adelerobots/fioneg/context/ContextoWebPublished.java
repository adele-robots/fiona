package com.adelerobots.fioneg.context;

import java.math.BigDecimal;
import java.util.List;

import com.adelerobots.fioneg.entity.WebPublishedC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

public class ContextoWebPublished {
	
	public static final String WEB_PUBLISHED_CTX = "FIONEGN013";
	
	public static final String CTX_WEB_PUBLISHED_ID = "FIONEG013010";
	
	public static final String CTX_WEB_PUBLISHED_USUARIO_ID = "FIONEG013020";
	
	public static final String CTX_IS_PUBLISHED = "FIONEG013030";
	
	
	/**
	 * Método destinado a rellenar el contexto con un sparkstatus
	 * 
	 * @param webPublished
	 *            webPublished a introducir en contexto
	 * @return Se devuelve el contexto identificado como 
	 */
	public static IContexto[] rellenarContexto(final WebPublishedC webPublished) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (webPublished != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoWebPublished(webPublished);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de sparkStatus
	 * 
	 * @param lstSparkStatus
	 *            Lista de sparks a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN009
	 */
	
	public static IContexto[] rellenarContexto(List<WebPublishedC> lstWebPublished) {
		IContexto[] salida = null;
		final int iSize = lstWebPublished.size();

		if (lstWebPublished != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoWebPublished(lstWebPublished.get(i));
			}
		}		
		
		return salida;
	}
	
	/**
	 * Método destinado a rellenar el contexto con un objeto de tipo
	 * SparkStatusC
	 * 
	 * @param sparkStatus
	 *            sparkStatus a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN009
	 */
	private static IContexto rellenarContextoWebPublished(final WebPublishedC webPublished) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				WEB_PUBLISHED_CTX);

		datos.put(CTX_WEB_PUBLISHED_ID, new BigDecimal(webPublished.getIntCodWebPublished()));
		datos.put(CTX_WEB_PUBLISHED_USUARIO_ID, new BigDecimal(webPublished.getUsuario().getCnUsuario()));		
		datos.put(CTX_IS_PUBLISHED, webPublished.getChPublished().toString());
				
		
		return datos;
	}
	
	

}
