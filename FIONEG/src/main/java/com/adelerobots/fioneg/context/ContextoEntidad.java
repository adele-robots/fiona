package com.adelerobots.fioneg.context;

import java.util.List;

import com.adelerobots.fioneg.entity.EntidadC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

/**
 * Clase destinada a los contextos necesarios para los servicios con funciones
 * sobre Entidades
 * 
 * @author adele
 * 
 */
public class ContextoEntidad {
	public static final String ENTIDAD_CTX = "FIONEGN027";
	
	public static final String CTX_ENTIDAD_ID = "FIONEG027010";
	public static final String CTX_NOMBRE = "FIONEG027020";
	public static final String CTX_TIPOENTIDAD = "FIONEG027030";
	public static final String CTX_WEBSITE = "FIONEG027040";
	public static final String CTX_WORKEMAIL = "FIONEG027050";
	public static final String CTX_PHONECODE = "FIONEG027060";
	public static final String CTX_PHONENUMBER = "FIONEG027070";
	public static final String CTX_PHONEEXT = "FIONEG027080";

	/**
	 * Método destinado a rellenar el contexto con una entidad
	 * 
	 * @param entidad
	 *            entidad a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN027
	 */
	public static IContexto[] rellenarContexto(final EntidadC entidad) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (entidad != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoEntidad(entidad);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de entidades
	 * 
	 * @param lstEntidad
	 *            Lista de entidades a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN027
	 */
	
	public static IContexto[] rellenarContexto(List<EntidadC> lstEntidad) {
		IContexto[] salida = null;
		final int iSize = lstEntidad.size();
		
		if (lstEntidad != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoEntidad(lstEntidad.get(i));
			}
		}
		
		return salida;
	}	
	
	/**
	 * Método destinado a rellenar el contexto con una entidad
	 * 
	 * @param entidad
	 *            entidad a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN027
	 */
	private static IContexto rellenarContextoEntidad(final EntidadC entidad) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				ENTIDAD_CTX);
		
		datos.put(CTX_ENTIDAD_ID, entidad.getCnEntidadAsBd());
		datos.put(CTX_NOMBRE, entidad.getNombre());
		datos.put(CTX_TIPOENTIDAD, entidad.getTipoEntidadAsBd());
		datos.put(CTX_WEBSITE, entidad.getWebsite());
		datos.put(CTX_WORKEMAIL, entidad.getWorkEmail());
		datos.put(CTX_PHONECODE, entidad.getPhoneCode());
		datos.put(CTX_PHONENUMBER, entidad.getPhoneNumber());
		datos.put(CTX_PHONEEXT, entidad.getPhoneExt());
		
		return datos;
	}
	
}
