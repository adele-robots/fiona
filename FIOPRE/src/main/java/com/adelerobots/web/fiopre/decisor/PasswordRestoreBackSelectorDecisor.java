package com.adelerobots.web.fiopre.decisor;
import com.adelerobots.fioneg.util.FunctionUtils;
import com.treelogic.fawna.presentacion.core.exception.PresentacionException;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperDecisor;
import com.treelogic.fawna.presentacion.core.webflow.action.IGenericDecisor;

/**
 * Decisor que se encarga de seleccionar la transicion a ejecutar dependiendo del lugar
 * desde el que se lanzó el flujo, por ejemplo, desde el website o desde el flujo de login como subflujo,
 * 
 * @author adele
 *
 */
public class PasswordRestoreBackSelectorDecisor implements IGenericDecisor {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1444087231522979505L;

	/* (non-Javadoc)
	 * @see com.treelogic.fawna.presentacion.core.webflow.action.IGenericDecisor#ejecutar(com.treelogic.fawna.presentacion.core.utilidades.IHelperDecisor)
	 */
	public String ejecutar(
			final IHelperDecisor helper) 
		throws PresentacionException 
	{
		final String backMode = FunctionUtils.defaultIfBlank(helper.getValueContext("PASSWORDRESTORE_BACK_MODE"), "website");
		
		return backMode;
	}

}
