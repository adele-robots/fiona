package com.adelerobots.fioneg.context;

import java.math.BigDecimal;
import java.util.List;

import com.adelerobots.fioneg.entity.DatoListaC;
import com.adelerobots.fioneg.entity.TipoC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IRegistro;

/**
 * Clase destinada a los contextos necesarios para los servicios con funciones
 * sobre Tipos
 * 
 * @author adele
 * 
 */
public class ContextoTipo {
	public static final String TIPO_CTX = "FIONEGN018";	

	public static final String CTX_TIPO_ID = "FIONEG018010";
	public static final String CTX_NOMBRE = "FIONEG018020";
	public static final String CTX_FUNCVALIDACION = "FIONEG018030";
	public static final String CTX_TIPOBASICO = "FIONEG018040";
	public static final String CTX_USUARIO_ID = "FIONEG018050";
	public static final String CTX_DATOLISTA_REG = "FIONEG018060";
	public static final String CTX_DATOLISTA_REG1 = "FIONEG018060010";

	/**
	 * Método destinado a rellenar el contexto con un tipo
	 * 
	 * @param tipo
	 *            tipo a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN018
	 */
	public static IContexto[] rellenarContexto(final TipoC tipo) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (tipo != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoTipo(tipo);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de tipos
	 * 
	 * @param lstTipos
	 *            Lista de tipos a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN018
	 */
	
	public static IContexto[] rellenarContexto(List<TipoC> lstTipos) {
		IContexto[] salida = null;
		final int iSize = lstTipos.size();

		if (lstTipos != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoTipo(lstTipos.get(i));
			}
		}		
		
		return salida;
	}	
	
	/**
	 * Método destinado a rellenar el contexto con un objeto de tipo
	 * TipoC
	 * 
	 * @param tipo
	 *            tipo a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN018
	 */
	private static IContexto rellenarContextoTipo(final TipoC tipo) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				TIPO_CTX);
		
		List <DatoListaC> lstValoresLista = tipo.getLstDatosLista();		
		
		IRegistro[] registros = null;
		final int iSize = lstValoresLista.size();

		if (lstValoresLista != null) {
			registros = new IRegistro[iSize];
			for (int i = 0; i < iSize; i++) {
				IRegistro registroDatoLista = ContextoFactory.getInstance().getRegistro(datos, CTX_DATOLISTA_REG);
				registroDatoLista.put(CTX_DATOLISTA_REG1, lstValoresLista.get(i).getStrNombre());
				registros[i] = registroDatoLista;
			}
		}		
		datos.put(CTX_DATOLISTA_REG, registros);

		datos.put(CTX_TIPO_ID, new BigDecimal(tipo.getCnTipo()));
		datos.put(CTX_NOMBRE, tipo.getStrTipo() );		
		datos.put(CTX_FUNCVALIDACION, tipo.getStrFuncValidacion());		
		
		if(tipo.getChTipoBasico().equals(new Character('0')))			
			datos.put(CTX_TIPOBASICO, "false");
		else
			datos.put(CTX_TIPOBASICO, "true");
		if(tipo.getUsuario()!=null)
			datos.put(CTX_USUARIO_ID, tipo.getUsuario().getCnUsuarioAsBd());	
		
		return datos;
	}	
	
}
