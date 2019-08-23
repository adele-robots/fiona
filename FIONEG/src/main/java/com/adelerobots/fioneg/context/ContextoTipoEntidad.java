package com.adelerobots.fioneg.context;

import java.util.List;

import com.adelerobots.fioneg.entity.TipoEntidadC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

/**
 * Clase destinada a los contextos necesarios para los servicios con funciones
 * sobre Tipos de Entidades
 * 
 * @author adele
 * 
 */
public class ContextoTipoEntidad {
	public static final String TIPOENTIDAD_CTX = "FIONEGN026";
	
	public static final String CTX_TIPOENTIDAD_ID = "FIONEG026010";
	public static final String CTX_DESCRIPCION = "FIONEG026020";

	/**
	 * Método destinado a rellenar el contexto con un tipoentidad
	 * 
	 * @param tipoentidad
	 *            tipoentidad a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN026
	 */
	public static IContexto[] rellenarContexto(final TipoEntidadC tipoentidad) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (tipoentidad != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoTipoEntidad(tipoentidad);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de tipos de entidades
	 * 
	 * @param lstTipoEntidad
	 *            Lista de tipos de entidades a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN026
	 */
	
	public static IContexto[] rellenarContexto(List<TipoEntidadC> lstTipoEntidad) {
		IContexto[] salida = null;
		final int iSize = lstTipoEntidad.size();
		
		if (lstTipoEntidad != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoTipoEntidad(lstTipoEntidad.get(i));
			}
		}
		
		return salida;
	}	
	
	/**
	 * Método destinado a rellenar el contexto con un tipoentidad
	 * 
	 * @param tipoentidad
	 *            tipoentidad a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN026
	 */
	private static IContexto rellenarContextoTipoEntidad(final TipoEntidadC tipoentidad) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				TIPOENTIDAD_CTX);
		
		datos.put(CTX_TIPOENTIDAD_ID, tipoentidad.getIdAsBd());
		datos.put(CTX_DESCRIPCION, tipoentidad.getDescripcion());
		
		return datos;
	}
	
}
