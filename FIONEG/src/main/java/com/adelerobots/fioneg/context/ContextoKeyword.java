package com.adelerobots.fioneg.context;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import com.adelerobots.fioneg.entity.keyword.KeywordC;
import com.adelerobots.fioneg.entity.sparkstatus.SparkStatusC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

/**
 * Clase destinada a los contextos necesarios para los servicios con funciones
 * sobre Keyword
 * 
 * @author adele
 * 
 */
public class ContextoKeyword {
	public static final String KEYWORD_CTX = "FIONEGN012";	

	public static final String CTX_KEYWORD_ID = "FIONEG012010";
	public static final String CTX_DESCRIPCION = "FIONEG012020";	
	


	/**
	 * Método destinado a rellenar el contexto con una keyword
	 * 
	 * @param keyword
	 *            keyword a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN012
	 */
	public static IContexto[] rellenarContexto(final KeywordC keyword) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (keyword != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoKeyword(keyword);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de sparkStatus
	 * 
	 * @param lstKeywords
	 *            Lista de keywords a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN012
	 */
	
	public static IContexto[] rellenarContexto(List<KeywordC> lstKeywords) {
		IContexto[] salida = null;
		final int iSize = lstKeywords.size();

		if (lstKeywords != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoKeyword(lstKeywords.get(i));
			}
		}		
		
		return salida;
	}
	
	
	
	/**
	 * Método destinado a rellenar el contexto con un objeto de tipo
	 * KeywordC
	 * 
	 * @param keyword
	 *            keyword a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN012
	 */
	private static IContexto rellenarContextoKeyword(final KeywordC keyword) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				KEYWORD_CTX);

		datos.put(CTX_KEYWORD_ID, new BigDecimal(keyword.getIntCodKeyword()));
		datos.put(CTX_DESCRIPCION, keyword.getStrDescripcion());
		
		
		
		return datos;
	}
	
	

	

	
	
}
