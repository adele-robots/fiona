package com.adelerobots.web.fiopre.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;

public class AddProductionPrice extends AbstractUserProcessListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -892074097112036830L;
	
	private static final String CAMPO_UNIDAD_TIEMPO = "idUnidadTiempoProd";
	private static final String CAMPO_UNIDAD_USO = "idUnidadUsoProd";
	
	private static final String CAMPO_NUM_USUARIOS = "numUsuariosConcuProd";
	
	// Campos de cantidad
	private static final String CAMPO_CANTIDAD = "cantidadUnidadProd";	
	private static final String ID_COMBO_VALOR_MESES_PROD = "idValorMesesProd";
	private static final String ID_COMBO_VALOR_ANIOS_PROD = "idValorAniosProd";
	// FIN CAMPOS CANTIDAD
	
	private static final String CAMPO_DOLARES = "dolaresProd";
	
	private static final String COLUMNA_USUARIOS = "num_usuarios";
	private static final String COLUMNA_UNIDAD_NOMBRE = "unidad_nombre";
	private static final String COLUMNA_CANTIDAD = "cantidad";
	private static final String COLUMNA_EUROS = "euros";
	private static final String COLUMNA_ES_ACTIVO = "es_activo";
	private static final String COLUMNA_ES_USADO = "es_usado";
	private static final String COLUMNA_PRECIO_ID = "price_id";
	
	private static final String TABLA_PRECIOS = "tablaPreciosProd";
	
	private static final String CAMPO_SPARK_ID = "spark_id";

	@SuppressWarnings("unchecked")
	public void procesarAjaxChangeListener(
			GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		List<Map<String, String>> listaPreciosProduccion = (List<Map<String, String>>) gestorDatos
				.getValue(TABLA_PRECIOS);

		if (listaPreciosProduccion == null) {
			listaPreciosProduccion = new ArrayList<Map<String, String>>();
		}

		Map<String, String> fila = new HashMap<String, String>();

		String numUsuarios = (String) gestorDatos.getValue(CAMPO_NUM_USUARIOS);
		String unidadNombre = "";
		// (String)gestorDatos.getValue("numUsuariosConcu");
		String unidadTiempoId = (String) gestorDatos.getValue(CAMPO_UNIDAD_TIEMPO);
		String unidadUsoId = (String) gestorDatos.getValue(CAMPO_UNIDAD_USO);
		//String cantidad = (String) gestorDatos.getValue(CAMPO_CANTIDAD);		
		String euros = (String) gestorDatos.getValue(CAMPO_DOLARES);
		Integer intCodSpark = Integer.valueOf((String) gestorDatos
				.getValue(CAMPO_SPARK_ID));

		fila.put(COLUMNA_USUARIOS, numUsuarios);
		
		// Determinar la clave de la unidad de tiempo
		// o uso seleccionada por el usuario
		Map <String,String> unidadesTiempo = ((Map<String,String>)gestorDatos.getItems(CAMPO_UNIDAD_TIEMPO));
		Map <String,String> unidadesUso = ((Map<String,String>)gestorDatos.getItems(CAMPO_UNIDAD_USO));
		
		String cantidad = "1";
		if(unidadTiempoId != null){			
									
			switch (Integer.valueOf(unidadTiempoId)){
				case 1:
					unidadTiempoId = "1";
					// TODO: Propertizar
					unidadNombre = "Months";
					cantidad = "1";
					break;
				case 2:
					unidadTiempoId = "1";
					// TODO: Propertizar
					unidadNombre = "Months";
					cantidad = "6";
					break;
				case 3:
					unidadTiempoId = "2";
					// TODO: Propertizar
					unidadNombre = "Years";
					cantidad = "1";
					break;
				case 4:
					unidadTiempoId = "2";
					// TODO: Propertizar
					unidadNombre = "Months";
					cantidad = "2";
					break;
			}
			/* Antes de unir cantidad y tiempo
			if(unidadTiempoId.equals("1")){
				cantidad = (String)gestorDatos.getValue(ID_COMBO_VALOR_MESES_PROD);
			}else{
				cantidad = (String)gestorDatos.getValue(ID_COMBO_VALOR_ANIOS_PROD);
			}
			
			for(Map.Entry<String, String> unidadTiempo:unidadesTiempo.entrySet()){
				if(unidadTiempo.getValue().equalsIgnoreCase(unidadTiempoId)){
					unidadNombre = unidadTiempo.getKey();									
				}
			}*/
				
		}else if(unidadUsoId != null){
			
			cantidad = (String)gestorDatos.getValue(CAMPO_CANTIDAD);
			
			for(Map.Entry<String, String> unidadUso:unidadesUso.entrySet()){
				if(unidadUso.getValue().equalsIgnoreCase(unidadUsoId)){
					unidadNombre = unidadUso.getKey();									
				}
			}
			
		}
		
		fila.put(COLUMNA_UNIDAD_NOMBRE, unidadNombre);
		fila.put(COLUMNA_CANTIDAD, cantidad);
		fila.put(COLUMNA_EUROS, euros);
		fila.put(COLUMNA_ES_ACTIVO, "1");
		fila.put(COLUMNA_ES_USADO, "0");

		// Llamada al SN que asocie el precio al spark
		Integer intCodPrecio = null;
		try {
			Integer intNumUsuarios = null;
			if(numUsuarios!=null)
				intNumUsuarios = Integer.valueOf(numUsuarios);
			
			Integer intUnidadTiempoId = null;
			if(unidadTiempoId!=null)
				intUnidadTiempoId = Integer.valueOf(unidadTiempoId);
			
			Integer intUnidadUsoId = null;
			if(unidadUsoId!=null)
				intUnidadUsoId = Integer.valueOf(unidadUsoId);
			
			Float floCantidad = null;
			if(cantidad!=null)
				floCantidad = Float.valueOf(cantidad);
			
			Float floEuros = null;
			if(euros!=null)
				floEuros = Float.valueOf(euros);		
						
			intCodPrecio = invokeAddPriceToSpark(intNumUsuarios,
					intUnidadTiempoId,
					intUnidadUsoId, floCantidad,
					floEuros, intCodSpark, "0");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

		// Recuperar el id del precio insertado
		// fila.put("price_id", Double.toString(Math.random()));
		if(intCodPrecio != null){
			fila.put(COLUMNA_PRECIO_ID, intCodPrecio.toString());
			// Insertar el nuevo precio en la tabla
			listaPreciosProduccion.add(fila);

			// Asignar el nuevo contenido de la lista
			gestorDatos.setValue(TABLA_PRECIOS, listaPreciosProduccion);
			
			// Borrar contenido footers
			gestorDatos.setValue(CAMPO_NUM_USUARIOS,"");
			gestorDatos.setValue(CAMPO_CANTIDAD,"");
			gestorDatos.setValue(CAMPO_DOLARES, "");
			
		}

		

	}

}
