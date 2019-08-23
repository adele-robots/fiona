package com.adelerobots.fioneg.context;

import java.util.List;

import com.adelerobots.fioneg.entity.status.StatusC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

/**
 * Constexto utilizado para rellenar el combo de status
 */
public class ContextoPropiedadValor {
	
public static final String ARQ_CTX = "ARQN000";
	
	public static final String CTX_STATUS_ID = "ARQN000010";
	public static final String CTX_STATUS_DESC = "ARQN000020";
	

	/**
	 * M�todo destinado a rellenar el contexto con un status
	 * 
	 * @param status
	 *            status a introducir en contexto
	 * @return Se devuelve el contexto identificado como ARQN000
	 */
	public static IContexto[] rellenarContexto(final StatusC status) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (status != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoStatus(status);
		}
		return salida;
	}
	
	
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de status
	 * 
	 * @param lstStatus
	 *            Lista de status a introducir en contexto
	 * @return Se devuelve el contexto identificado como ARQN000
	 */
	
	public static IContexto[] rellenarContexto(List<StatusC> lstStatus) {
		IContexto[] salida = null;
		final int iSize = lstStatus.size();

		if (lstStatus != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoStatus(lstStatus.get(i));
			}
		}		
		
		return salida;
	}
	
	
	/**
	 * Método destinado a rellenar el contexto con un status
	 * 
	 * @param status
	 *            status a introducir en contexto
	 * @return Se devuelve el contexto identificado como ARQN000
	 */
	private static IContexto rellenarContextoStatus(final StatusC status) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				ARQ_CTX);

		datos.put(CTX_STATUS_ID, status.getCnStatus().toString());
		datos.put(CTX_STATUS_DESC, status.getStrDescripcion());
		
		return datos;
	}

}
