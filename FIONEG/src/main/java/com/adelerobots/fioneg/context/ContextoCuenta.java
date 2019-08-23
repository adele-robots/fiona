package com.adelerobots.fioneg.context;

import java.math.BigDecimal;
import java.util.List;

import com.adelerobots.fioneg.entity.CuentaC;
import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

/**
 * Clase destinada a los contextos necesarios para los servicios con funciones
 * sobre Cuentas
 * 
 * @author adele
 * 
 */
public class ContextoCuenta {
	public static final String CUENTA_CTX = "FIONEGN024";
	
	public static final String CTX_CUENTA_ID = "FIONEG024010";
	public static final String CTX_NOMBRE = "FIONEG024020";
	public static final String CTX_PRECIO_MENSUAL = "FIONEG024030";
	public static final String CTX_PRECIO_ANUAL = "FIONEG024040";

	/**
	 * Método destinado a rellenar el contexto con una cuenta
	 * 
	 * @param cuenta
	 *            cuenta a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN024
	 */
	public static IContexto[] rellenarContexto(final CuentaC cuenta) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (cuenta != null) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoCuenta(cuenta);
		}
		return salida;
	}
	
	/**
	 * Método que permitirá rellenar el contexto con una lista de cuentas
	 * 
	 * @param lstCuentas
	 *            Lista de cuentas a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN024
	 */
	
	public static IContexto[] rellenarContexto(List<CuentaC> lstCuentas) {
		IContexto[] salida = null;
		final int iSize = lstCuentas.size();
		
		if (lstCuentas != null) {
			salida = new IContexto[iSize];
			for (int i = 0; i < iSize; i++) {
				salida[i] = rellenarContextoCuenta(lstCuentas.get(i));
			}
		}
		
		return salida;
	}	
	
	/**
	 * Método destinado a rellenar el contexto con una cuenta
	 * 
	 * @param cuenta
	 *            cuenta a introducir en contexto
	 * @return Se devuelve el contexto identificado como FIONEGN024
	 */
	private static IContexto rellenarContextoCuenta(final CuentaC cuenta) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(
				CUENTA_CTX);
		
		datos.put(CTX_CUENTA_ID, cuenta.getIdAsBd());
		datos.put(CTX_NOMBRE, cuenta.getNombre());
		datos.put(CTX_PRECIO_MENSUAL, new BigDecimal(cuenta.getFloPrecioMensual()));
		datos.put(CTX_PRECIO_ANUAL, new BigDecimal(cuenta.getFloPrecioAnual()));
		
		return datos;
	}
	
}
