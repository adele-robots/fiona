package com.adelerobots.fioneg.context;

import java.util.List;

import com.adelerobots.fioneg.entity.usuarioconfig.UsuarioConfigC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

/**
 * Clase destinada a los contextos necesarios para los servicios con funciones
 * sobre Configuraciones de avatares de los usuarios UsuarioConfig
 * 
 * @author adele
 * 
 */
public class ContextoUsuarioConfig {
	public static final String USUARIOCONFIG_CTX = "FIONEGN028";
	
	public static final String CTX_USUARIO_ID = "FIONEG028010";
	public static final String CTX_CONFIG_ID = "FIONEG028020";
	public static final String CTX_NOMBRE = "FIONEG028030";

	/**
	 * Método destinado a rellenar el contexto con un usuarioconfig
	 * 
	 * @param usuarioconfig
	 *            usuarioconfig a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN028
	 */
	public static IContexto[] rellenarContexto(final UsuarioConfigC usuarioconfig) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (usuarioconfig != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoUsuarioConfig(usuarioconfig);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de configuraciones
	 * 
	 * @param lstUsuarioConfig
	 *            Lista de configuraciones a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN028
	 */
	
	public static IContexto[] rellenarContexto(List<UsuarioConfigC> lstUsuarioConfig) {
		IContexto[] salida = null;
		final int iSize = lstUsuarioConfig.size();
		
		if (lstUsuarioConfig != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoUsuarioConfig(lstUsuarioConfig.get(i));
			}
		}
		
		return salida;
	}	
	
	/**
	 * Método destinado a rellenar el contexto con un usuarioconfig
	 * 
	 * @param usuarioconfig
	 *            usuarioconfig a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN028
	 */
	private static IContexto rellenarContextoUsuarioConfig(final UsuarioConfigC usuarioconfig) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				USUARIOCONFIG_CTX);
		
		datos.put(CTX_USUARIO_ID, usuarioconfig.getIntCodUsuarioAsBd());
		datos.put(CTX_CONFIG_ID, usuarioconfig.getIntCodConfigAsBd());
		datos.put(CTX_NOMBRE, usuarioconfig.getNombre());
		
		return datos;
	}
	
}
