package com.adelerobots.web.fiopre.decisor;

import org.apache.log4j.Logger;

import com.treelogic.fawna.presentacion.core.exception.PresentacionException;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperDecisor;
import com.treelogic.fawna.presentacion.core.webflow.action.IGenericDecisor;

public class ComprobarLogicDelete implements IGenericDecisor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8453033142821406631L;
	
	private static final Logger logger = Logger
	.getLogger(ComprobarLogicDelete.class);

	private final static String DELETED = "deleted";

	private final static String NOT_DELETED = "notDeleted";

	public String ejecutar(IHelperDecisor helper) throws PresentacionException {
		
		String retorno = "errorIfrt";
		
		logger.debug("ComprobarLogicDelete: Inicio");
		
		
		String deleted = helper.getValueContext("borrado");
		
		if(deleted.equals("1")){
			retorno = DELETED;
		}else{
			retorno = NOT_DELETED;
		}
		
		
		logger.debug("ComprobarLogicDelete: Fin");
		
		return retorno;
	}

}
