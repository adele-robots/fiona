package com.adelerobots.fioneg.context;

import java.util.List;

import com.adelerobots.fioneg.entity.UnitC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

public class ContextoUnit {
	
	public static final String UNIT_CTX = "FIONEGN014";	

	public static final String CTX_UNIT_ID = "FIONEG014010";
	public static final String CTX_UNIT_CONTENT = "FIONEG014020";
	
	/**
	 * Método destinado a rellenar el contexto con una unidad
	 * 
	 * @param unit
	 *            unit a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN014
	 */
	public static IContexto[] rellenarContexto(final UnitC unit) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (unit != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoUnit(unit);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de objetos UnitC
	 * 
	 * @param lstUnits
	 *            Lista de unidades a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN014
	 */
	
	public static IContexto[] rellenarContexto(List<UnitC> lstUnits) {
		IContexto[] salida = null;
		final int iSize = lstUnits.size();

		if (lstUnits != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoUnit(lstUnits.get(i));
			}
		}		
		
		return salida;
	}
	
	
	
	/**
	 * Método destinado a rellenar el contexto con un objeto de tipo
	 * UnitC
	 * 
	 * @param unit
	 *            unit a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN014
	 */
	private static IContexto rellenarContextoUnit(final UnitC unit) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				UNIT_CTX);

		datos.put(CTX_UNIT_ID, unit.getIntCodUnit().toString());
		datos.put(CTX_UNIT_CONTENT, unit.getStrContent());	
		
		
		return datos;
	}

}
