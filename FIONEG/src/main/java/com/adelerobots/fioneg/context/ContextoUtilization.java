package com.adelerobots.fioneg.context;

import java.util.List;

import com.adelerobots.fioneg.entity.UtilizationC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

public class ContextoUtilization {
	
	public static final String UTILIZATION_CTX = "FIONEGN015";	

	public static final String CTX_UTILIZATION_ID = "FIONEG015010";
	public static final String CTX_UTILIZATION_CONTENT = "FIONEG015020";
	
	/**
	 * Método destinado a rellenar el contexto con una unidad de uso
	 * 
	 * @param use
	 *            unidad de uso a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN015
	 */
	public static IContexto[] rellenarContexto(final UtilizationC utilization) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (utilization != null) {
			salida = new IContexto[1];			
			salida[0] = rellenarContextoUtilization(utilization);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de objetos UtilizationC
	 * 
	 * @param lstUtilizations
	 *            Lista de unidades de uso a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN015
	 */
	
	public static IContexto[] rellenarContexto(List<UtilizationC> lstUtilizations) {
		IContexto[] salida = null;
		final int iSize = lstUtilizations.size();

		if (lstUtilizations != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoUtilization(lstUtilizations.get(i));
			}
		}		
		
		return salida;
	}
	
	
	
	/**
	 * Método destinado a rellenar el contexto con un objeto de tipo
	 * UtilizationC
	 * 
	 * @param utilization
	 *            unidad de uso a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN015
	 */
	private static IContexto rellenarContextoUtilization(final UtilizationC utilization) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				UTILIZATION_CTX);

		datos.put(CTX_UTILIZATION_ID, utilization.getIntCodUtilization().toString());
		datos.put(CTX_UTILIZATION_CONTENT, utilization.getStrContent());	
		
		
		return datos;
	}

}
