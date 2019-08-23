package com.adelerobots.web.fiopre.decisor;

import com.treelogic.fawna.presentacion.core.exception.PresentacionException;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperDecisor;
import com.treelogic.fawna.presentacion.core.webflow.action.IGenericDecisor;

public class SignupAccountTypeDecisor implements IGenericDecisor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8063443817583192462L;

	public String ejecutar(IHelperDecisor helper) throws PresentacionException {
		String resultado = "";
		String tipoCuenta = helper.getValueContext("USER_ACCOUNTTYPE_ID");
		String tipoCuentaAnterior = helper.getValueContext("SECURE_USER_ACCOUNTTYPE_ID");
		String periodoPagoCuenta = helper.getValueContext("USER_ACCOUNTTYPE_METHOD");
		String periodoPagoCuentaAnterior = helper.getValueContext("SECURE_USER_ACCOUNTTYPE_METHOD");
		
		// Si el tipo de cuenta actual es gratuita o el usuario no ha cambiado el tipo de cuenta
		if(tipoCuenta.equals("1") || tipoCuenta.equals(tipoCuentaAnterior)){
			if(!tipoCuenta.equals("1") && tipoCuenta.equals(tipoCuentaAnterior)){
				if(!periodoPagoCuenta.equals(periodoPagoCuentaAnterior))
					resultado = "noFree";
				else
					resultado = "free";
			}else
				resultado = "free";			
		}else{			
			resultado = "noFree";
		}
		
		
		
		return resultado;
	}

}
