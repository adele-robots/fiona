package com.adelerobots.serneg.context;

import java.math.BigDecimal;
import java.util.List;

import com.adelerobots.serneg.dataclasses.UsuarioST;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

public class ContextoUsuarioST {
	
	public static final String USUARIOST_CTX = "SERNEGN002";
	
	public static final String CTX_USUARIOST_ID = "SERNEG002010";
	
	public static final String CTX_USUARIOST_EMAIL = "SERNEG002020";
	
	public static final String CTX_USUARIOST_PASS = "SERNEG002030";
	
	
	
	/**
	 * Método destinado a rellenar el contexto con un usuario de sparking together
	 * 
	 * @param usuario
	 *            Usuario a introducir en contexto
	 * @return Se devuelve el contexto identificado como SERNEGN002
	 */
	public static IContexto[] rellenarContexto(final UsuarioST usuario) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (usuario != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoUsuarioST(usuario);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de usuarios
	 * 
	 * @param lstUsuarios
	 *            Lista de usuarios a introducir en contexto
	 * @return Se devuelve el contexto identificado como SERNEGN002
	 */
	
	public static IContexto[] rellenarContexto(List<UsuarioST> lstUsuarios) {
		IContexto[] salida = null;
		final int iSize = lstUsuarios.size();

		if (lstUsuarios != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoUsuarioST(lstUsuarios.get(i));
			}
		}		
		
		return salida;
	}
	
	/**
	 * Método destinado a rellenar el contexto con un usuario de sparking together
	 * 
	 * @param usuario
	 *            usuario a introducir en contexto
	 * @return Se devuelve el contexto identificado como SERNEGN002
	 */
	private static IContexto rellenarContextoUsuarioST(final UsuarioST usuario) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				USUARIOST_CTX);

		datos.put(CTX_USUARIOST_ID, new BigDecimal(usuario.getUsuarioId()));
		datos.put(CTX_USUARIOST_EMAIL, usuario.getEmail());
		datos.put(CTX_USUARIOST_PASS, usuario.getPassword());	
				
		
		return datos;
	}
	
	

}
