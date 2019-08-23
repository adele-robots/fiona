package com.adelerobots.fioneg.context;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import com.adelerobots.fioneg.entity.rejection.RejectionC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

/**
 * Clase destinada a los contextos necesarios para los servicios con funciones
 * sobre Rejection
 * 
 * @author adele
 * 
 */
public class ContextoRejection {
	public static final String REJECTION_CTX = "FIONEGN010";	

	public static final String CTX_REJECTION_ID = "FIONEG010010";
	public static final String CTX_REJECTION_SPARK_ID = "FIONEG010020";
	public static final String CTX_REJECTION_DATE_REJECTION = "FIONEG010030";		
	public static final String CTX_REJECTION_MOTIVO = "FIONEG010040";
	


	/**
	 * Método destinado a rellenar el contexto con una opinion
	 * 
	 * @param rejection
	 *            rejection a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN010
	 */
	public static IContexto[] rellenarContexto(final RejectionC rejection) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (rejection != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoRejection(rejection);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de rejections
	 * 
	 * @param lstRejections
	 *            Lista de rejections a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN010
	 */
	
	public static IContexto[] rellenarContexto(List<RejectionC> lstRejections) {
		IContexto[] salida = null;
		final int iSize = lstRejections.size();

		if (lstRejections != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoRejection(lstRejections.get(i));
			}
		}		
		
		return salida;
	}
	
	
	
	/**
	 * Método destinado a rellenar el contexto con un objeto de tipo
	 * RejectionC
	 * 
	 * @param rejection
	 *            rejection a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN010
	 */
	private static IContexto rellenarContextoRejection(final RejectionC rejection) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				REJECTION_CTX);

		datos.put(CTX_REJECTION_ID, new BigDecimal(rejection.getIntCodRejection()));
		datos.put(CTX_REJECTION_SPARK_ID, new BigDecimal(rejection.getSpark().getCnSpark()));
		// Fecha
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		datos.put(CTX_REJECTION_DATE_REJECTION, sdf.format(rejection.getDatRejection()));		
		datos.put(CTX_REJECTION_MOTIVO, rejection.getStrMotivo());
		
		
		return datos;
	}
	
	

	

	
	
}
