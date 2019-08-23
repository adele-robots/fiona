package com.adelerobots.web.fiopre.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IRegistro;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;

public class OpenUserTypesDialog extends AbstractUserProcessListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8420197180891648963L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {	
		
		Integer userid = ContextUtils.getUserIdAsInteger();
		
		try {
			IContexto[] ctxTipos =	invokeListUserParamTypes(userid);
			// Objeto necesario para componer la tabla
			List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();						
			
			for(int i = 0;i<ctxTipos.length;i++){			
				IRegistro[] registro = ctxTipos[i].getRegistro("FIONEG018060");
				// Necesario un mapa por registro para almacenar los valores del tipo lista
				Map<String, String> valorLista = new HashMap<String, String>();
				for(Integer j = 0;j<registro.length;j++){
					valorLista.put(registro[j].getString("FIONEG018060010"), j.toString());
				}
				/** Cada fila de la tabla es un mapa. Cargo la columna 'nombre' que es un 
				 *  texto y la columna 'valor_lista' que es un combo, por lo que necesita
				 *  un mapa para cargarlo
				 **/
				Map<String, Object> row = new HashMap<String, Object>();
				row.put("nombre", ctxTipos[i].getString("FIONEG018020"));
				row.put("valor_lista", valorLista);
				row.put("tipo_id", ctxTipos[i].getBigDecimal("FIONEG018010"));
				
				
				items.add(row);			
			}			
			gestorDatos.setValue("userParams", items);
			gestorDatos.setValue("tablaValoresLista", null);
			gestorDatos.setValue("listName", "");
			gestorEstados.openModalAlert("dialogUserTypes");		
			
		} catch (FactoriaDatosException e) {
			e.printStackTrace();
		} catch (PersistenciaException e) {
			e.printStackTrace();
		} catch (FawnaInvokerException e) {
			e.printStackTrace();
		}
		
	}

}
