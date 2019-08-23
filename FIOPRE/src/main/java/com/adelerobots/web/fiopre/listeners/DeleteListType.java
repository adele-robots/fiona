package com.adelerobots.web.fiopre.listeners;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.persistencia.ContextoLocator;
import com.treelogic.fawna.presentacion.core.persistencia.DatoFactory;
import com.treelogic.fawna.presentacion.core.persistencia.IDato;

public class DeleteListType extends AbstractUserProcessListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5628103285540844106L;

	@SuppressWarnings("unchecked")
	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {	
		
		String idComponente = gestorEstados.getIdComponente();
		
		if(idComponente.compareTo("deleteTypeOK")==0){
			gestorEstados.closeModalAlert("alertDeleteListType");
			String ownParamId = null;
			Integer intOwnParamId = null;
			
			try {
				IDato datoOwnParamId = DatoFactory.creaDatoSimple();
				datoOwnParamId = (IDato) ContextoLocator.getInstance().getContextoVentana().getCtxValue("datoOwnParamId");
				ownParamId = (String) datoOwnParamId.getValor();
				intOwnParamId = Integer.parseInt(ownParamId);
//				datoOwnParamId.setValor(null);
//				ContextoLocator.getInstance().getContextoVentana().putCtxValue(datoOwnParamId);				
			} catch (PersistenciaException e1) {
				e1.printStackTrace();
			}
			
			// llamar a SN y quitar de tabla			
			try {
				IContexto[] ctxTipo = invokeDeleteConfigParamType(intOwnParamId);
				
				// Actualizar la tabla
				List <Map<String,Object>> listaTiposPropios = (List <Map<String,Object>>)gestorDatos.getValue("userParams");
				boolean encontrado = false;
				
				for(int i = 0; i<listaTiposPropios.size() && !encontrado; i++){
					Map<String,Object> row = listaTiposPropios.get(i);
					Integer id =  ((BigDecimal)row.get("tipo_id")).intValue();
					
					if(id.equals(intOwnParamId)){
						encontrado = true;
						listaTiposPropios.remove(i);
					}
				}		
				gestorDatos.setValue("userParams", listaTiposPropios);		
				
				
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (FactoriaDatosException e) {
				e.printStackTrace();
			} catch (PersistenciaException e) {
				e.printStackTrace();
			} catch (FawnaInvokerException e) {
				e.printStackTrace();
			}
			
		}else if(idComponente.compareTo("deleteTypeNO")==0){
			gestorEstados.closeModalAlert("alertDeleteListType");
		}	
		
	}

}
