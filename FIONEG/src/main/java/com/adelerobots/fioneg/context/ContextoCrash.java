package com.adelerobots.fioneg.context;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import com.adelerobots.fioneg.entity.crash.CrashC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

/**
 * Clase destinada a los contextos necesarios para los servicios con funciones
 * sobre Crash
 * 
 * @author adele
 * 
 */
public class ContextoCrash {
	public static final String CRASH_CTX = "FIONEGN011";	

	public static final String CTX_CRASH_ID = "FIONEG011010";
	public static final String CTX_CRASH_SPARK_ID = "FIONEG011020";
	public static final String CTX_CRASH_DATE_CRASH = "FIONEG011030";		
	public static final String CTX_CRASH_MOTIVO = "FIONEG011040";
	


	/**
	 * Método destinado a rellenar el contexto con un crash
	 * 
	 * @param crash
	 *            crash a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN011
	 */
	public static IContexto[] rellenarContexto(final CrashC crash) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (crash != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoCrash(crash);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de crash
	 * 
	 * @param lstCrashes
	 *            Lista de crash a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN011
	 */
	
	public static IContexto[] rellenarContexto(List<CrashC> lstCrashes) {
		IContexto[] salida = null;
		final int iSize = lstCrashes.size();

		if (lstCrashes != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoCrash(lstCrashes.get(i));
			}
		}		
		
		return salida;
	}
	
	
	
	/**
	 * Método destinado a rellenar el contexto con un objeto de tipo
	 * CrashC
	 * 
	 * @param crash
	 *            crash a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN011
	 */
	private static IContexto rellenarContextoCrash(final CrashC crash) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				CRASH_CTX);

		datos.put(CTX_CRASH_ID, new BigDecimal(crash.getIntCodCrash()));
		datos.put(CTX_CRASH_SPARK_ID, new BigDecimal(crash.getSpark().getCnSpark()));
		// Fecha
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		datos.put(CTX_CRASH_DATE_CRASH, sdf.format(crash.getDatCrash()));		
		datos.put(CTX_CRASH_MOTIVO, crash.getStrDescripcion());
		
		
		return datos;
	}
	
	

	

	
	
}
