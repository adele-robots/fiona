package com.adelerobots.web.fiopre.validators;

import com.adelerobots.fioneg.util.FunctionUtils;
import com.treelogic.fawna.arq.negocio.core.ValidacionException;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;

/**
 * Validador de datos para el proceso de restore password
 * @author adele
 *
 */
public class PasswordRestoreFormValidator 
	extends 	AbstractUserFormValidator
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5962526868690885158L;
	private static final String FIELD_EMAIL = "email";
	

	/*
	 * (non-Javadoc)
	 * @see com.treelogic.fawna.presentacion.componentes.validators.form.interfaces.IValidacionFormulario#validate(com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes, com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes)
	 */
	public Boolean validate(
			final GestorEstadoComponentes gE,
			final GestorDatosComponentes gD)
	{
		
		String email = FunctionUtils.toString(gD.getValue(FIELD_EMAIL), null);
		try { validateEmailFormat(email); } catch (ValidacionException e) {
			gD.addErrorValidacionSimple(e.getMessage(), e.getMessage(), FIELD_EMAIL);
			return Boolean.FALSE;
		}
		
				
		return Boolean.TRUE;
	}



}
