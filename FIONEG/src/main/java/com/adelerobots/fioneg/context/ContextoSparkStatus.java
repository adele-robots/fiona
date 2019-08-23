package com.adelerobots.fioneg.context;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import com.adelerobots.fioneg.entity.sparkstatus.SparkStatusC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

/**
 * Clase destinada a los contextos necesarios para los servicios con funciones
 * sobre SparkStatus
 * 
 * @author adele
 * 
 */
public class ContextoSparkStatus {
	public static final String SPARK_STATUS_CTX = "FIONEGN009";	

	public static final String CTX_STATUS_ID = "FIONEG009010";
	public static final String CTX_SPARK_ID = "FIONEG009020";
	public static final String CTX_DATE_CAMBIO = "FIONEG009030";
	public static final String CTX_CAMBIO_FIONA = "FIONEG009040";	
	public static final String CTX_NOMBRE_STATUS = "FIONEG009050";
	public static final String CTX_NOMBRE_USUARIO_MODIFICA = "FIONEG009060";
	
	private static final String NOMBRE_PLATAFORMA = "FIONA";
	private static final Character CAMBIO_PLATAFORMA = '1';
	


	/**
	 * Método destinado a rellenar el contexto con un sparkstatus
	 * 
	 * @param sparkStatus
	 *            sparkStatus a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN009
	 */
	public static IContexto[] rellenarContexto(final SparkStatusC sparkStatus) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (sparkStatus != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoSparkStatus(sparkStatus);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de sparkStatus
	 * 
	 * @param lstSparkStatus
	 *            Lista de sparks a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN009
	 */
	
	public static IContexto[] rellenarContexto(List<SparkStatusC> lstSparkStatus) {
		IContexto[] salida = null;
		final int iSize = lstSparkStatus.size();

		if (lstSparkStatus != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoSparkStatus(lstSparkStatus.get(i));
			}
		}		
		
		return salida;
	}
	
	
	
	/**
	 * Método destinado a rellenar el contexto con un objeto de tipo
	 * SparkStatusC
	 * 
	 * @param sparkStatus
	 *            sparkStatus a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN009
	 */
	private static IContexto rellenarContextoSparkStatus(final SparkStatusC sparkStatus) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				SPARK_STATUS_CTX);

		datos.put(CTX_STATUS_ID, new BigDecimal(sparkStatus.getIntCodStatus()));
		datos.put(CTX_SPARK_ID, new BigDecimal(sparkStatus.getIntCodSpark()));
		// Fecha
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		datos.put(CTX_DATE_CAMBIO, sdf.format(sparkStatus.getDateCambio()));
		datos.put(CTX_CAMBIO_FIONA, sparkStatus.getCharCambioFiona().toString());
		datos.put(CTX_NOMBRE_STATUS, sparkStatus.getSparkStatus().getStatus().getStrDescripcion());
		
		if(sparkStatus.getCharCambioFiona().equals(CAMBIO_PLATAFORMA))
			datos.put(CTX_NOMBRE_USUARIO_MODIFICA, NOMBRE_PLATAFORMA);
		else
			datos.put(CTX_NOMBRE_USUARIO_MODIFICA, sparkStatus.getSparkStatus().getSpark().getUsuarioDesarrollador().getName());
		
		
		return datos;
	}
	
	

	

	
	
}
