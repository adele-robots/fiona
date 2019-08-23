package com.adelerobots.fioneg.context;

import java.math.BigDecimal;
import java.util.List;

import com.adelerobots.fioneg.entity.InterfazC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

/**
 * Clase destinada a los contextos necesarios para los servicios con funciones
 * sobre Interfaces
 * 
 * @author adele
 * 
 */
public class ContextoInterfaz {
	public static final String INTERFAZ_CTX = "FIONEGN017";	

	public static final String CTX_INTERFAZ_ID = "FIONEG017010";
	public static final String CTX_NOMBRE = "FIONEG017020";
	public static final String CTX_DESCRIPCION = "FIONEG017030";
	public static final String CTX_TIPO = "FIONEG017040";
	


	/**
	 * Método destinado a rellenar el contexto con una interfaz
	 * 
	 * @param interfaz
	 *            interfaz a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN017
	 */
	public static IContexto[] rellenarContexto(final InterfazC interfaz) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (interfaz != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoInterfaz(interfaz);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de interfaces
	 * 
	 * @param lstInterfaces
	 *            Lista de interfaces a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN017
	 */
	
	public static IContexto[] rellenarContexto(List<InterfazC> lstInterfaces) {
		IContexto[] salida = null;
		final int iSize = lstInterfaces.size();

		if (lstInterfaces != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoInterfaz(lstInterfaces.get(i));
			}
		}		
		
		return salida;
	}
	
	
	
	/**
	 * Método destinado a rellenar el contexto con un objeto de tipo
	 * InterfazC
	 * 
	 * @param interfaz
	 *            interfaz a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN017
	 */
	private static IContexto rellenarContextoInterfaz(final InterfazC interfaz) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				INTERFAZ_CTX);

		datos.put(CTX_INTERFAZ_ID, new BigDecimal(interfaz.getCnInterfaz()));
		datos.put(CTX_NOMBRE, interfaz.getDcNombre());
		datos.put(CTX_DESCRIPCION, interfaz.getStrDescripcion());
		datos.put(CTX_TIPO, interfaz.getStrTipo());	
		
		return datos;
	}
	
	

	

	
	
}
