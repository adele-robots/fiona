package com.adelerobots.fioneg.context;

import java.math.BigDecimal;
import java.util.List;

import com.adelerobots.fioneg.entity.ConfigParamC;
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
public class ContextoConfigParam {
	public static final String CONFICPARAM_CTX = "FIONEGN020";
	
	public static final String CTX_CONFIGPARAM_REG = "FIONEG020010";
	public static final String CTX_CONFIGPARAM_REG_NOMBRE = "FIONEG020010010";
	public static final String CTX_CONFIGPARAM_REG_TIPO = "FIONEG020010020";
	public static final String CTX_CONFIGPARAM_REG_VALORDEFECTO = "FIONEG020010030";
	public static final String CTX_CONFIGPARAM_REG_VALORMIN = "FIONEG020010040";
	public static final String CTX_CONFIGPARAM_REG_VALORMAX = "FIONEG020010050";
	public static final String CTX_CONFIGPARAM_REG_CONFIGURABLE = "FIONEG020010060";
	
	/**
	 * Método destinado a rellenar el contexto con un registro de ConfigParams
	 * 
	 * @param configParam
	 *            configParam a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN020
	 */
	public static IContexto[] rellenarContexto(final ConfigParamC configParam) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (configParam != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoConfigParam(configParam);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de parámetros de
	 *  configuración
	 * 
	 * @param lstConfigParams
	 *            Lista de parámetros de configuración a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN020
	 */
	
	public static IContexto[] rellenarContexto(List<ConfigParamC> lstConfigParams) {
		IContexto[] salida = null;
		final int iSize = lstConfigParams.size();

		if (lstConfigParams != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoConfigParam(lstConfigParams.get(i));
			}
		}		
		
		return salida;
	}	
	
	/**
	 * Método destinado a rellenar el contexto con un objeto de tipo
	 * ConfigParamC
	 * 
	 * @param configParam
	 *            configParam a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN020
	 */
	private static IContexto rellenarContextoConfigParam(final ConfigParamC configParam) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				CONFICPARAM_CTX);
				
		IRegistro[] registros = new IRegistro[1];
		
		IRegistro registroConfigParam = ContextoFactory.getInstance().getRegistro(datos, CTX_CONFIGPARAM_REG);
		registroConfigParam.put(CTX_CONFIGPARAM_REG_NOMBRE, configParam.getStrNombre());
		registroConfigParam.put(CTX_CONFIGPARAM_REG_TIPO, new BigDecimal(configParam.getCnTipo()));
		registroConfigParam.put(CTX_CONFIGPARAM_REG_VALORDEFECTO, configParam.getStrDefaultValue());
		registroConfigParam.put(CTX_CONFIGPARAM_REG_VALORMIN, configParam.getStrMinValue());
		registroConfigParam.put(CTX_CONFIGPARAM_REG_VALORMAX, configParam.getStrMaxValue());
		registroConfigParam.put(CTX_CONFIGPARAM_REG_CONFIGURABLE, new BigDecimal(configParam.getIsConfigurable()));
		
		registros[0] = registroConfigParam;
		
		datos.put(CTX_CONFIGPARAM_REG, registros);
		
		return datos;
	}	
	
}
