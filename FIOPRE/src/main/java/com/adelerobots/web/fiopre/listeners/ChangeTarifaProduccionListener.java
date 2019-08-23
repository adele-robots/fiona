package com.adelerobots.web.fiopre.listeners;

import org.apache.log4j.Logger;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.persistencia.ContextoLocator;
import com.treelogic.fawna.presentacion.core.persistencia.DatoFactory;
import com.treelogic.fawna.presentacion.core.persistencia.IDato;

public class ChangeTarifaProduccionListener implements IProcesadorDeAjaxChangeListener {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3808514202132844869L;
	
	private static final Logger logger = Logger.getLogger(ChangeTarifaProduccionListener.class);

	
	private static final String FIELD_TARIFA = "idTipoTarifaProd";	
	private static final String TARIFA_POR_TIEMPO = "1";
	private static final String TARIFA_POR_USO = "2";
	private static final String TARIFA_GRATUITA= "3";
	
	private static final String FIELD_TIEMPO = "idUnidadTiempoProd";
	private static final String FIELD_USO = "idUnidadUsoProd";
	private static final String FIELD_COMBO_MESES = "idValorMesesProd";
	private static final String FIELD_COMBO_ANIOS = "idValorAniosProd";
	
	// Campos de cantidad
	private static final String FIELD_TEXT_AMOUNT = "cantidadUnidadProd";
	private static final String ID_COMBO_VALOR_MESES_PROD = "idValorMesesProd";
	private static final String ID_COMBO_VALOR_ANIOS_PROD = "idValorAniosProd";
			
	private static final String TABLA_PRECIOS = "tablaPreciosProd";
	private static final String SCROLL1_TABLA = "scProd";
	private static final String SCROLL2_TABLA = "sc2Prod";
	
	private static final String PANEL_INSERT_PRECIO = "panelInsertPriceProd";
	private static final String PANEL_USERS = "panelUsersProd";
	private static final String PANEL_PERIOD_VALUE = "panelPeriodValueProd";
	private static final String PANEL_PERIOD = "panelPeriodProd";
	private static final String PANEL_PRECIO = "panelPrecioProd";

	
	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		String tipoTarifa = (String)gestorDatos.getValue(FIELD_TARIFA);
		IDato dato = DatoFactory.creaDatoSimple();
		if(tipoTarifa.equals(TARIFA_POR_TIEMPO)){
			// Poner a null el campo opuesto
			gestorDatos.setValue(FIELD_USO, null);			
			dato.setPropiedad(FIELD_USO);
			dato.setValor(null);
			// Mostrar / ocultar paneles
			gestorEstados.setPropiedad(PANEL_USERS, "rendered", true);
			gestorEstados.setPropiedad(PANEL_PERIOD_VALUE, "rendered", true);
			gestorEstados.setPropiedad(PANEL_PERIOD, "rendered", true);
			gestorEstados.setPropiedad(PANEL_PRECIO, "rendered", true);
			gestorEstados.setPropiedad(FIELD_TIEMPO, "rendered", true);
						
			gestorEstados.setPropiedad(FIELD_USO, "rendered", false);	
			
			gestorEstados.setPropiedad(ID_COMBO_VALOR_MESES_PROD, "rendered", true);
			
			gestorEstados.setPropiedad(ID_COMBO_VALOR_ANIOS_PROD, "rendered", false);
			
			gestorEstados.setPropiedad(FIELD_TEXT_AMOUNT, "rendered", false);
						
			// Habilitar los paneles			
			gestorEstados.setPropiedad(FIELD_TIEMPO, "disabled", false);
			gestorEstados.setPropiedad(FIELD_COMBO_MESES, "disabled", false);
			gestorEstados.setPropiedad(FIELD_COMBO_ANIOS, "disabled", false);
			
			// Mostrar la tabla de precios
			gestorEstados.setPropiedad(TABLA_PRECIOS, "rendered", true);
			gestorEstados.setPropiedad(SCROLL1_TABLA, "rendered", true);
			gestorEstados.setPropiedad(SCROLL2_TABLA, "rendered", true);
			
		}else if (tipoTarifa.equals(TARIFA_POR_USO)){
			// Poner a null el campo opuesto
			gestorDatos.setValue(FIELD_TIEMPO, null);
			dato.setPropiedad(FIELD_TIEMPO);
			dato.setValor(null);
			// Mostrar / ocultar paneles
			gestorEstados.setPropiedad(PANEL_USERS, "rendered", true);
			gestorEstados.setPropiedad(PANEL_PERIOD_VALUE, "rendered", true);
			gestorEstados.setPropiedad(PANEL_PERIOD, "rendered", true);
			gestorEstados.setPropiedad(PANEL_PRECIO, "rendered", true);
			
			gestorEstados.setPropiedad(FIELD_TIEMPO, "rendered", false);
						
			gestorEstados.setPropiedad(FIELD_USO, "rendered", true);
			
			gestorEstados.setPropiedad(ID_COMBO_VALOR_MESES_PROD, "rendered", false);
			
			gestorEstados.setPropiedad(ID_COMBO_VALOR_ANIOS_PROD, "rendered", false);
			
			gestorEstados.setPropiedad(FIELD_TEXT_AMOUNT, "rendered", true);
			
			// Habilitar los paneles			
			gestorEstados.setPropiedad(FIELD_USO, "disabled", false);	
			
			// Mostrar la tabla de precios
			gestorEstados.setPropiedad(TABLA_PRECIOS, "rendered", true);
			gestorEstados.setPropiedad(SCROLL1_TABLA, "rendered", true);
			gestorEstados.setPropiedad(SCROLL2_TABLA, "rendered", true);
			
		}else if(tipoTarifa.equals(TARIFA_GRATUITA)){
			// Poner a null todos los campos (de ids)
			IDato datoUso = DatoFactory.creaDatoSimple();
			gestorDatos.setValue(FIELD_USO, null);			
			datoUso.setPropiedad(FIELD_USO);
			datoUso.setValor(null);
			
			IDato datoTiempo = DatoFactory.creaDatoSimple();
			gestorDatos.setValue(FIELD_TIEMPO, null);
			datoTiempo.setPropiedad(FIELD_TIEMPO);
			datoTiempo.setValor(null);
			
			try {
				ContextoLocator.getInstance().getContextoVentana().putCtxValue(datoUso);
				ContextoLocator.getInstance().getContextoVentana().putCtxValue(datoTiempo);
			} catch (PersistenciaException e) {
				logger.error("[ChangeTarifaListener] Error al persistir valor de la propiedad", e);
			}
			
			// Deshabilitar los paneles
			gestorEstados.setPropiedad(FIELD_TIEMPO, "disabled", true);			
			
			gestorEstados.setPropiedad(FIELD_USO, "disabled", true);
			
			gestorEstados.setPropiedad(ID_COMBO_VALOR_MESES_PROD, "disabled", true);
			
			gestorEstados.setPropiedad(ID_COMBO_VALOR_ANIOS_PROD, "disabled", true);			
			
			
			// Ocultar la tabla de precios
			gestorEstados.setPropiedad(PANEL_USERS, "rendered", false);
			gestorEstados.setPropiedad(PANEL_PERIOD_VALUE, "rendered", false);
			gestorEstados.setPropiedad(PANEL_PERIOD, "rendered", false);
			gestorEstados.setPropiedad(PANEL_PRECIO, "rendered", false);
			
			gestorEstados.setPropiedad(TABLA_PRECIOS, "rendered", false);
			gestorEstados.setPropiedad(SCROLL1_TABLA, "rendered", false);
			gestorEstados.setPropiedad(SCROLL2_TABLA, "rendered", false);
					
			
		}
		
		try {			
			if(tipoTarifa.equals(TARIFA_POR_TIEMPO) || tipoTarifa.equals(TARIFA_POR_USO)){
				ContextoLocator.getInstance().getContextoVentana().putCtxValue(dato);
			}
		} catch (PersistenciaException e) {
			logger.error("[ChangeTarifaListener] Error al persistir valor de la propiedad", e);
		}

	}

}
