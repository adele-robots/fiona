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

public class GetAvatarPrice extends AbstractUserProcessListener{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8787870184117747823L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		logger.info("[GetAvatarPrice] Inicio recuperación precios de la configuración del usuario");
		Integer intCodUsuario = ContextUtils.getUserIdAsInteger();
		//TODO: Modificar para recuperar el nombre del archivo XML que se corresponda con la configuración
		// seleccionada cuando el usuario pueda tener varias configuraciones
		//try {
			/* Versión inicial (antes de que hubiera varios precios posibles)
			Object datoComplejo = invokeGetListPrecios(intCodUsuario, null);
			
			@SuppressWarnings("unchecked")
			List<Map<String, String>> datosStatus = (List<Map<String, String>>) datoComplejo;	
			
			Map<String,String> datosTotales = (Map<String, String>)datosStatus.get(datosStatus.size()-1);			
			
			gestorDatos.setValue("tablaPrecios", datosStatus);
			
			gestorDatos.setValue("footerPrecioMensual",datosTotales.get("totalMensual"));
			
			gestorDatos.setValue("footerPrecioUso",datosTotales.get("totalUso"));
			
			datosStatus.remove(datosStatus.size()-1);
			
			gestorEstados.openModalAlert("dialogoPrecios");*/
			
			/*
			
			IContexto[] datos = invokeGetListPreciosProd(intCodUsuario, null);
			
			
			if(datos != null){
				List<Map<String, Object>> datosSparksPrecios = new ArrayList<Map<String, Object>>();	
				
				
				for(int i = 0; i< datos.length; i++){
					Map<String, Object> row = new HashMap<String, Object>();
					
					row.put("spark_id", datos[i].getBigDecimal("FIONEG023010")); // ID DEL SPARK
					row.put("nombre_spark", datos[i].getString("FIONEG023020")); // NOMBRE DEL SPARK
					
					IRegistro[] registro = datos[i].getRegistro("FIONEG023030");
					if(registro != null){
					// Necesario un mapa por registro para almacenar los valores del tipo lista
						Map<String, String> datosPreciosSpark = new HashMap<String, String>();
						for(Integer j = 0;j<registro.length;j++){
							datosPreciosSpark.put(registro[j].getString("FIONEG023030020"),registro[j].getBigDecimal("FIONEG023030010").toString());					
						}
					
					row.put("precios_produccion", datosPreciosSpark); // Contenido del combo con los precios de producción posibles de cada spark
					}
					
					datosSparksPrecios.add(row);
				}
				
				gestorDatos.setValue("tablaPrecios", datosSparksPrecios);
				*/
				gestorEstados.openModalAlert("dialogoPrecios");
				
//			}
//			
//		} catch (FactoriaDatosException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (PersistenciaException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FawnaInvokerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
	}

}
