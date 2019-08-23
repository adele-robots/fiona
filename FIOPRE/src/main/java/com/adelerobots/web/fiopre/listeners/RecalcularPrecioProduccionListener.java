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

public class RecalcularPrecioProduccionListener extends
		AbstractUserProcessListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3276518781485269210L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		
		/*
		List<Map<String, Object>> datosSparksPrecios = (List<Map<String, Object>>)gestorDatos.getValue("tablaPrecios");
		Float precioTotal = new Float(0.0);
		
		for(int i = 0; i<datosSparksPrecios.size(); i++){
			Map <String,Object> row = datosSparksPrecios.get(i);
			String idPrecioSeleccionado = (String)row.get("precio_seleccionado");
				if(idPrecioSeleccionado != null && !"".equals(idPrecioSeleccionado)){
					//Map <String, String> valoresPrecios = (Map<String,String>)row.get("precios_produccion");
					DatoComplejo datoComplejo = (DatoComplejo)row.get("precios_produccion");
					PresentacionList valoresPrecios = (PresentacionList)datoComplejo.getValor();					
					//String precioSeleccionado = (String)valoresPrecios.getElementAt(idPrecioSeleccionado);
					for(int j = 0; j< valoresPrecios.size(); j++){
						DatoComplejo dc = (DatoComplejo)valoresPrecios.get(j);
						PresentacionList pl = (PresentacionList)dc.getValor();
						DatoSimple ds = (DatoSimple)pl.get(0);
						if(ds.getValor().equals(idPrecioSeleccionado)){
							String label = (String)ds.getEtiqueta();
							// Separar el contenido de la label para obtener el precio
							String [] priceChunks = label.split(" "); 
							String strPrecio = priceChunks[4];
							// Transformar el precio
							Float precio = Float.valueOf(strPrecio);
							precioTotal += precio;
						}
							
					}
				}
			
		}
		
		gestorDatos.setValue("precioTexto", precioTotal.toString() + "$");
		*/		
		gestorEstados.openModalAlert("dialogoPrecios");
		
		// Id de usuario
		Integer intCodUsuario = ContextUtils.getUserIdAsInteger();
		// TODO: Configuración elegida
		
		// Usuarios concurrentes
		String strNumUsersConcurrentes = (String)gestorDatos.getValue("numUsuariosConcu");
		// Primera vez que se abre la ventana de precios
		if(strNumUsersConcurrentes == null || "".equals(strNumUsersConcurrentes))
			strNumUsersConcurrentes = "1";
		Integer intNumUserConcurrentes = Integer.valueOf(strNumUsersConcurrentes);
		// Tiempo
		String strIdUnidadTiempo = (String)gestorDatos.getValue("idUnidadTiempo");
		// Primera vez que se abre la ventana de precios
		if(strIdUnidadTiempo == null ||"".equals(strIdUnidadTiempo)){
			strIdUnidadTiempo = "1";
			gestorDatos.setValue("idUnidadTiempo", strIdUnidadTiempo);
		}
		Integer intIdUnidadTiempo = Integer.valueOf(strIdUnidadTiempo);
		// Resolución
		String strIdResolution = (String)gestorDatos.getValue("resolution");
		// Primera vez que se abre la ventana de precios
		if(strIdResolution == null || "".equals(strIdResolution)){
			strIdResolution = "1";
			gestorDatos.setValue("resolution", strIdResolution);
		}
		Integer intIdResolution = Integer.valueOf(strIdResolution);
		// Disponibilidad
		Boolean highAvailability = (Boolean)gestorDatos.getValue("checkAvailability");
		// Primera vez que se abre la ventana de precios
		if(highAvailability == null){
			highAvailability = Boolean.FALSE;
			gestorDatos.setValue("checkAvailability", highAvailability);
		}
		Integer intHighAvailability = highAvailability?new Integer(1):new Integer(0);
		
		// TODO: Invocar al servicio que nos devuelva el contenido de la tabla
		try {
			IContexto [] contextos= invokeGetListPrecios(intCodUsuario, null,intNumUserConcurrentes,intIdUnidadTiempo, intIdResolution,intHighAvailability);
			
			IContexto contexto = contextos[0];
			
			// Precio total tiempo
			Float precioTotalTiempo = (contexto.getBigDecimal("FIONEG025010")).floatValue();
			if(precioTotalTiempo != null){
				gestorDatos.setValue("precioTotalTiempo", precioTotalTiempo);				
			}
			
			// Precio total uso
			Float precioTotalUso = (contexto.getBigDecimal("FIONEG025020")).floatValue();
			if(precioTotalUso != null){
				gestorDatos.setValue("precioTotalUso", precioTotalUso);				
			}
			
			// Precio total uso
			Float precioTotalHosting = (contexto.getBigDecimal("FIONEG025025")).floatValue();
			if(precioTotalHosting != null){
				gestorDatos.setValue("precioTotalHosting", precioTotalHosting);		
				if(precioTotalTiempo != null){
					precioTotalTiempo += precioTotalHosting;
					gestorDatos.setValue("precioTotalTiempo",precioTotalTiempo);
				}
			}
			
			// Registro de sparks por tiempo
			IRegistro[] registroTiempo = contexto.getRegistro("FIONEG025030");
			List <Map<String,String>> tablaSparksTiempo = new ArrayList <Map<String,String>>();
			if(registroTiempo != null){
				// Necesario un mapa por registro para almacenar los valores del tipo lista					
					for(Integer i = 0;i<registroTiempo.length;i++){
						Map<String, String> rowTime = new HashMap<String, String>();
						rowTime.put("spark_tiempo_id",registroTiempo[i].getBigDecimal("FIONEG025030010").toString());
						rowTime.put("spark_tiempo_nombre",registroTiempo[i].getString("FIONEG025030020").toString());
						rowTime.put("spark_tiempo_precio",registroTiempo[i].getBigDecimal("FIONEG025030030").toString());
						
						tablaSparksTiempo.add(rowTime);
					}				
				
			}
			
			gestorDatos.setValue("tablaSparksTiempo", tablaSparksTiempo);
			if(tablaSparksTiempo.size() > 0){
				gestorEstados.setPropiedad("tablaSparksTiempo", "rendered", true);
				gestorEstados.setPropiedad("sc", "rendered", true);
				gestorEstados.setPropiedad("sc2", "rendered", true);				
				// cambiar el valor de la label
				// Etiqueta seleccionada por el usuario
				String propertie = "";
				if(gestorDatos.getValue("idUnidadTiempo").equals("1")){
					propertie = "FIONA.tablaPrecio.labelPeriodMonth.valor";
				}else{
					propertie = "FIONA.tablaPrecio.labelPeriodYear.valor";
				}
				gestorDatos.setValue("labelPrecioTiempo", getMessage("FIONA.tablaPrecio.precioTotalTiempo.valor", 
							new Object[]{getMessage(propertie,"period"),": "}, 
					"Total price (period)"));
				gestorEstados.setPropiedad("precioTotalTiempo", "rendered", true);
			}else{
				gestorEstados.setPropiedad("tablaSparksTiempo", "rendered", false);
				gestorEstados.setPropiedad("sc", "rendered", false);
				gestorEstados.setPropiedad("sc2", "rendered", false);				
			}
			
			// Registro de sparks por uso
			IRegistro[] registroUso = contexto.getRegistro("FIONEG025040");
			List <Map<String,String>> tablaSparksUso = new ArrayList <Map<String,String>>();
			if(registroUso != null){
				// Necesario un mapa por registro para almacenar los valores del tipo lista					
					for(Integer i = 0;i<registroUso.length;i++){
						Map<String, String> rowUso = new HashMap<String, String>();
						rowUso.put("spark_uso_id",registroUso[i].getBigDecimal("FIONEG025040010").toString());
						rowUso.put("spark_uso_nombre",registroUso[i].getString("FIONEG025040020").toString());
						rowUso.put("spark_uso_precio",registroUso[i].getBigDecimal("FIONEG025040030").toString());
						
						tablaSparksUso.add(rowUso);
					}			
				
			}
			gestorDatos.setValue("tablaSparksUso", tablaSparksUso);			
			if(tablaSparksUso.size() > 0){
				gestorEstados.setPropiedad("tablaSparksUso", "rendered", true);
				gestorEstados.setPropiedad("scUso", "rendered", true);
				gestorEstados.setPropiedad("sc2Uso", "rendered", true);
			}else{
				gestorEstados.setPropiedad("tablaSparksUso", "rendered", false);
				gestorEstados.setPropiedad("scUso", "rendered", false);
				gestorEstados.setPropiedad("sc2Uso", "rendered", false);
				
			}
			
			if(tablaSparksUso.size() > 0 && tablaSparksTiempo.size() > 0){
				gestorEstados.setPropiedad("labelPrecioTotal", "rendered", true);
				gestorEstados.setPropiedad("precioTotal", "rendered", true);
				gestorDatos.setValue("precioTotal", precioTotalTiempo+precioTotalUso);
			}else{
				
			}
			
		} catch (FactoriaDatosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenciaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FawnaInvokerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO: Calcular los totales de dicha tabla
		

	}
	
	

}
