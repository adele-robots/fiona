package com.adelerobots.fioneg.context;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import com.adelerobots.fioneg.entity.opinion.OpinionC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

/**
 * Clase destinada a los contextos necesarios para los servicios con funciones
 * sobre opiniones
 * 
 * @author adele
 * 
 */
public class ContextoOpinion {
	public static final String OPINIONS_CTX = "FIONEGN006";	

	public static final String CTX_OPINION_ID = "FIONEG006010";
	public static final String CTX_OPINION_RATE = "FIONEG006020";
	public static final String CTX_OPINION_DESCRIPCION = "FIONEG006030";
	public static final String CTX_OPINION_TITULO = "FIONEG006040";
	public static final String CTX_OPINION_DATE = "FIONEG006050";
	


	/**
	 * Método destinado a rellenar el contexto con una opinion
	 * 
	 * @param opinion
	 *            Opinion a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN006
	 */
	public static IContexto[] rellenarContexto(final OpinionC opinion) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (opinion != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoOpinion(opinion);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de opiniones
	 * 
	 * @param lstSparks
	 *            Lista de sparks a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN006
	 */
	
	public static IContexto[] rellenarContexto(List<OpinionC> lstOpiniones) {
		IContexto[] salida = null;
		final int iSize = lstOpiniones.size();

		if (lstOpiniones != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoOpinion(lstOpiniones.get(i));
			}
		}		
		
		return salida;
	}
	
	
	
	/**
	 * M�todo destinado a rellenar el contexto con una opinion
	 * 
	 * @param spark
	 *            Spark a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN006
	 */
	private static IContexto rellenarContextoOpinion(final OpinionC opinion) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				OPINIONS_CTX);

		datos.put(CTX_OPINION_ID, new BigDecimal(opinion.getIntCodOpinion()));
		datos.put(CTX_OPINION_RATE, new BigDecimal(opinion.getIntValoracion()));
		if(opinion.getStrDescripcion()!=null)
			datos.put(CTX_OPINION_DESCRIPCION, opinion.getStrDescripcion());	
		datos.put(CTX_OPINION_TITULO, opinion.getStrTitulo());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
		datos.put(CTX_OPINION_DATE,sdf.format(opinion.getDatEnviada()));
		
		
		return datos;
	}
	
	

	

	
	
}
