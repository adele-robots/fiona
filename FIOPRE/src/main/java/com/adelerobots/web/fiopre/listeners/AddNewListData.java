package com.adelerobots.web.fiopre.listeners;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.arq.negocio.contextos.implementation.ContextoImplementationPresentacion;
import com.treelogic.fawna.arq.negocio.contextos.implementation.RegistroImplementationPresentacion;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IRegistro;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;

public class AddNewListData extends AbstractUserProcessListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8420197180891648963L;
	
	public static final String TIPO_CTX = "FIONEGN018";	

	public static final String CTX_TIPO_ID = "FIONEG018010";
	public static final String CTX_NOMBRE = "FIONEG018020";
	public static final String CTX_FUNCVALIDACION = "FIONEG018030";
	public static final String CTX_TIPOBASICO = "FIONEG018040";
	public static final String CTX_USUARIO_ID = "FIONEG018050";
	public static final String CTX_DATOLISTA_REG = "FIONEG018060";
	public static final String CTX_DATOLISTA_REG1 = "FIONEG018060010";

	@SuppressWarnings("unchecked")
	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {	
		
		Integer userid = ContextUtils.getUserIdAsInteger();
		
		/** Recuperar información de la lista a insertar **/
		String listName = (String) gestorDatos.getValue("listName");
		List<Map<String, String>> listValues = new ArrayList<Map<String,String>>();	
		if(!listName.equals("")){
			listValues = (List<Map<String, String>>)gestorDatos.getValue("tablaValoresLista");
			if(listValues == null || listValues.isEmpty()){		
				handleModalAlert(gestorDatos, gestorEstados, "alertListTypes",
						"info",	getMessage("FIONA.uploadspark.alert.title.errorInfo","Wrong data entered"),
						getMessage("FIONA.uploadspark.alert.body.errorInfo","Wrong info"), 
						"infoListTypes", getMessage("FIONA.uploadspark.alert.error.noValues","You must insert the list values"));				
				return;
			}else if(listValues.size() < 2){
				handleModalAlert(gestorDatos, gestorEstados, "alertListTypes",
						"info",	getMessage("FIONA.uploadspark.alert.title.errorInfo","Wrong data entered"),
						getMessage("FIONA.uploadspark.alert.body.errorInfo","Wrong info"), 
						"infoListTypes", getMessage("FIONA.uploadspark.alert.error.minValues","You must insert at least two values"));				
				return;
			}		
			try{				
				IContexto ctx = new ContextoImplementationPresentacion(TIPO_CTX);
				ctx.put(CTX_NOMBRE,listName);
				ctx.put(CTX_USUARIO_ID, new BigDecimal(userid.intValue()));
				ctx.put(CTX_TIPOBASICO, "0");

				IRegistro[] registros = null;    
				int listSize = listValues.size();
				registros = new IRegistro[listSize];
				for (int i = 0; i < listSize; i++) {
					IRegistro registroDatoLista = new RegistroImplementationPresentacion(CTX_DATOLISTA_REG);
					registroDatoLista.put(CTX_DATOLISTA_REG1, listValues.get(i).get("nombre"));
					registros[i] = registroDatoLista;
				}
				ctx.put(CTX_DATOLISTA_REG, registros);		
				
				//Invocamos el SN para crear el nuevo tipo
				IContexto[] ctxTipo = invokeCreateConfigParamType(ctx);
				// Si todo fue bien, mostramos mensaje 'success' y actualizamos tabla
				if(ctxTipo != null){
					handleModalAlert(gestorDatos, gestorEstados, "alertListTypes",
							"check",	getMessage("FIONA.uploadspark.alert.title.success","List type creation completed"),
							getMessage("FIONA.uploadspark.alert.body.success","List type creation completed"), 
							"infoListTypes", getMessage("FIONA.uploadspark.alert.message.success","Your list type has been successfully created"));			
					
					List<Map<String, Object>> userTypes = (List<Map<String, Object>>)gestorDatos.getValue("userParams");					
					if(userTypes == null){
						userTypes = new ArrayList<Map<String,Object>>();
					}
					for(int i = 0;i<ctxTipo.length;i++){			
						IRegistro[] registro = ctxTipo[i].getRegistro("FIONEG018060");
						// Necesario un mapa por registro para almacenar los valores del tipo lista
						Map<String, String> valorLista = new HashMap<String, String>();
						for(Integer j = 0;j<registro.length;j++){
							valorLista.put(registro[j].getString("FIONEG018060010"), j.toString());
						}
						/** Cada fila de la tabla es un mapa. Cargo la columna 'nombre' que es un 
						 *  texto y la columna 'valor_lista' que es un combo, por lo que necesita
						 *  un mapa para cargarlo, tipo_id irá a un campo oculto como referencia
						 **/
						Map<String, Object> row = new HashMap<String, Object>();
						row.put("nombre", ctxTipo[i].getString("FIONEG018020"));
						row.put("valor_lista", valorLista);
						row.put("tipo_id", ctxTipo[i].getBigDecimal("FIONEG018010"));
						
						userTypes.add(row);			
					}			
					// Se actualiza la tabla de tipos propios definidos por el usuario con el nuevo tipo
					gestorDatos.setValue("userParams", userTypes);				
					
				}else{
					handleModalAlert(gestorDatos, gestorEstados, "alertListTypes",
							"error",	"Error", "Problems while creating the new type", 
							"infoListTypes", "Something went wrong");
				}

			}catch (FactoriaDatosException e) {
				e.printStackTrace();
			} catch (PersistenciaException e) {
				e.printStackTrace();
			} catch (FawnaInvokerException e) {
				e.printStackTrace();
			}finally{
				// Resetear valores, actualizar tablas
				gestorDatos.setValue("tablaValoresLista", null);
				gestorDatos.setValue("listName", "");
			}

		}else{			
			handleModalAlert(gestorDatos, gestorEstados, "alertListTypes",
							"info",	getMessage("FIONA.uploadspark.alert.title.errorInfo","Wrong data entered"),
							getMessage("FIONA.uploadspark.alert.body.errorInfo","Wrong info"), 
							"infoListTypes", getMessage("FIONA.uploadspark.alert.error.noListname","List name can't be empty"));
		}
	}
	

}
