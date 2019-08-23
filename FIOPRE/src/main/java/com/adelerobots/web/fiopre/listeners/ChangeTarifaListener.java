package com.adelerobots.web.fiopre.listeners;

import org.apache.log4j.Logger;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.persistencia.ContextoLocator;
import com.treelogic.fawna.presentacion.core.persistencia.DatoFactory;
import com.treelogic.fawna.presentacion.core.persistencia.IDato;

public class ChangeTarifaListener implements IProcesadorDeAjaxChangeListener {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3808514202132844869L;
	
	private static final Logger logger = Logger.getLogger(ChangeTarifaListener.class);

	
	private static final String FIELD_TARIFA = "idTipoTarifa";	
	private static final String TARIFA_POR_TIEMPO = "1";	
	private static final String TARIFA_GRATUITA= "3";
	
	private static final String FIELD_TIEMPO = "idUnidadTiempo";				
	private static final String FIELD_COMBO_MESES = "idValorMeses";
	private static final String FIELD_COMBO_ANIOS = "idValorAnios";
	
		
	private static final String TABLA_PRECIOS = "tablaPrecios";
	private static final String SCROLL1_TABLA = "sc";
	private static final String SCROLL2_TABLA = "sc2";
	
	private static final String PANEL_INSERT_PRECIO = "panelInsertPrice";
	private static final String PANEL_USERS = "panelUsers";
	private static final String PANEL_PERIOD_VALUE = "panelPeriodValue";
	private static final String PANEL_PERIOD = "panelPeriod";
	private static final String PANEL_PRECIO = "panelPrecio";
	

	
	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		String tipoTarifa = (String)gestorDatos.getValue(FIELD_TARIFA);		
		if(tipoTarifa.equals(TARIFA_POR_TIEMPO)){			
			// Mostrar / ocultar paneles
			gestorEstados.setPropiedad(PANEL_USERS, "rendered", true);
			gestorEstados.setPropiedad(PANEL_PERIOD_VALUE, "rendered", true);
			gestorEstados.setPropiedad(PANEL_PERIOD, "rendered", true);
			gestorEstados.setPropiedad(PANEL_PRECIO, "rendered", true);
			gestorEstados.setPropiedad(FIELD_TIEMPO, "rendered", true);			
			
			// Habilitar los paneles
			gestorEstados.setPropiedad(FIELD_TIEMPO, "disabled", false);	
			gestorEstados.setPropiedad(FIELD_COMBO_MESES, "disabled", false);
			gestorEstados.setPropiedad(FIELD_COMBO_ANIOS, "disabled", false);
						
			// Mostrar la tabla de precios
			gestorEstados.setPropiedad(TABLA_PRECIOS, "rendered", true);
			gestorEstados.setPropiedad(SCROLL1_TABLA, "rendered", true);
			gestorEstados.setPropiedad(SCROLL2_TABLA,"rendered", true);
			
		}else if(tipoTarifa.equals(TARIFA_GRATUITA)){			
			IDato datoTiempo = DatoFactory.creaDatoSimple();
			gestorDatos.setValue(FIELD_TIEMPO, null);
			datoTiempo.setPropiedad(FIELD_TIEMPO);
			datoTiempo.setValor(null);			
			
			
			try {				
				ContextoLocator.getInstance().getContextoVentana().putCtxValue(datoTiempo);				
			} catch (PersistenciaException e) {
				logger.error("[ChangeTarifaListener] Error al persistir valor de la propiedad", e);
			}
			
			// Deshabilitar los paneles
			gestorEstados.setPropiedad(FIELD_TIEMPO, "disabled", true);
			gestorEstados.setPropiedad(FIELD_COMBO_MESES, "disabled", true);
			gestorEstados.setPropiedad(FIELD_COMBO_ANIOS, "disabled", true);
			
			// Ocultar la tabla de precios
			gestorEstados.setPropiedad(PANEL_USERS, "rendered", false);
			gestorEstados.setPropiedad(PANEL_PERIOD_VALUE, "rendered", false);
			gestorEstados.setPropiedad(PANEL_PERIOD, "rendered", false);
			gestorEstados.setPropiedad(PANEL_PRECIO, "rendered", false);
			
			gestorEstados.setPropiedad(TABLA_PRECIOS, "rendered", false);
			gestorEstados.setPropiedad(SCROLL1_TABLA, "rendered", false);
			gestorEstados.setPropiedad(SCROLL2_TABLA, "rendered", false);
					
			
		}		
		

	}

}
