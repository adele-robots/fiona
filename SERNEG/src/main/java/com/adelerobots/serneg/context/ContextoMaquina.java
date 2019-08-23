package com.adelerobots.serneg.context;

import java.math.BigDecimal;
import java.util.List;

import com.adelerobots.serneg.entity.MaquinaC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

public class ContextoMaquina {
	
	public static final String MAQUINA_CTX = "SERNEGN001";
	
	public static final String CTX_MAQUINA_ID = "SERNEG001010";
	
	public static final String CTX_MAQUINA_NOMBRE = "SERNEG001020";
	
	public static final String CTX_MAQUINA_IP = "SERNEG001030";
	
	public static final String CTX_MAQUINA_MAXPROCESOS = "SERNEG001040";
	
	public static final String CTX_MAQUINA_JNDI = "SERNEG001050";
	
	/**
	 * Método destinado a rellenar el contexto con una máquina
	 * 
	 * @param maquina
	 *            Máquina a introducir en contexto
	 * @return Se devuelve el contexto identificado como SERNEGN001
	 */
	public static IContexto[] rellenarContexto(final MaquinaC maquina) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (maquina != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoMaquina(maquina);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de máquinas
	 * 
	 * @param lstMaquinas
	 *            Lista de máquinas a introducir en contexto
	 * @return Se devuelve el contexto identificado como SERNEGN001
	 */
	
	public static IContexto[] rellenarContexto(List<MaquinaC> lstMaquinas) {
		IContexto[] salida = null;
		final int iSize = lstMaquinas.size();

		if (lstMaquinas != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoMaquina(lstMaquinas.get(i));
			}
		}		
		
		return salida;
	}
	
	/**
	 * Método destinado a rellenar el contexto con una máquina
	 * 
	 * @param maquina
	 *            Máquina a introducir en contexto
	 * @return Se devuelve el contexto identificado como SERNEGN001
	 */
	private static IContexto rellenarContextoMaquina(final MaquinaC maquina) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				MAQUINA_CTX);

		datos.put(CTX_MAQUINA_ID, new BigDecimal(maquina.getIntCodMaquina()));
		datos.put(CTX_MAQUINA_NOMBRE, maquina.getStrNombre());
		datos.put(CTX_MAQUINA_IP, maquina.getStrIp());	
		datos.put(CTX_MAQUINA_MAXPROCESOS, new BigDecimal(maquina.getIntMaxProcesos()));
		datos.put(CTX_MAQUINA_JNDI, maquina.getStrJndi());
		
		return datos;
	}
	
	

}
