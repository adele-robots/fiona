package com.adelerobots.web.fiopre.decisor;
import com.adelerobots.fioneg.util.FunctionUtils;
import com.treelogic.fawna.presentacion.core.exception.PresentacionException;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperDecisor;
import com.treelogic.fawna.presentacion.core.webflow.action.IGenericDecisor;

/**
 * Decisor que se encarga de seleccionar la transicion a ejecutar dependiendo de si el flujo se 
 * lanzo desde un sitio, por ejemplo, desde el website o desde el flujo de login como subflujo,
 * 
 * @author adele
 *
 */
public class SignupBackSelectorDecisor implements IGenericDecisor {

	private static final long serialVersionUID = 1510435884601793796L;

	/* (non-Javadoc)
	 * @see com.treelogic.fawna.presentacion.core.webflow.action.IGenericDecisor#ejecutar(com.treelogic.fawna.presentacion.core.utilidades.IHelperDecisor)
	 */
	public String ejecutar(
			final IHelperDecisor helper) 
		throws PresentacionException 
	{
		final String backMode = FunctionUtils.defaultIfBlank(helper.getValueContext("SIGNUP_BACK_MODE"), "website");
		
		return backMode;
	}

}
