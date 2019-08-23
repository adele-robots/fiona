package com.adelerobots.web.fiopre.listeners;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.persistencia.ContextoLocator;
import com.treelogic.fawna.presentacion.core.persistencia.DatoSimple;

public class AbrirCerrarAlert implements IProcesadorDeAjaxChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2266614538282238684L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		String idComponente = gestorEstados.getIdComponente();
		String idAlert = null;
		
		if(idComponente.compareToIgnoreCase("abrirBuy")==0 || idComponente.compareToIgnoreCase("cerrarBuy") == 0 || idComponente.compareToIgnoreCase("abrirBuyTab")==0){
			// En el caso del alert de compra debe comprobarse antes si se ha seleccionado algún precio
			DatoSimple datIdPrecioSeleccionado = null;
			String idPrecioSeleccionado = null;
			try {
				datIdPrecioSeleccionado = (DatoSimple)ContextoLocator.getInstance().getContextoVentana().getCtxValue("selected_price");
				if(datIdPrecioSeleccionado != null)
					idPrecioSeleccionado = (String)datIdPrecioSeleccionado.getValor();
				
			} catch (PersistenciaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if((idComponente.compareToIgnoreCase("abrirBuy")==0 || idComponente.compareToIgnoreCase("abrirBuyTab")==0) && (idPrecioSeleccionado == null || "".equals(idPrecioSeleccionado)))
				idAlert = "alertErrorCompra";
			else
				idAlert = "alertCompra";
		}else if(idComponente.compareToIgnoreCase("abrirFree")==0 || idComponente.compareToIgnoreCase("cerrarFree") == 0){
			idAlert = "alertFree";
		}else if(idComponente.compareToIgnoreCase("abrirTrial")==0 || idComponente.compareToIgnoreCase("cerrarTrial") == 0){
			idAlert = "alertTrial";
		}else if(idComponente.compareToIgnoreCase("abrirDelete")==0 || idComponente.compareToIgnoreCase("cerrarDelete") == 0){
			idAlert = "alertDelete";
		}else if(idComponente.indexOf("Status")>=0){
			idAlert = "alertStatus";
		}else if(idComponente.compareToIgnoreCase("abrirNoDevelopedSparks") == 0){
			idAlert = "alertNoDevelopedSparks";
		}else if(idComponente.compareToIgnoreCase("abrirUninstall")==0 || idComponente.compareToIgnoreCase("cerrarUninstall") == 0){
			idAlert = "alertUninstall";
		}else if(idComponente.compareToIgnoreCase("botonAProd")==0 || idComponente.compareToIgnoreCase("cerrarUploadConfirm") == 0){
			idAlert = "alertUploadConfirm";
		}else{
			idAlert = "alertOpinion";
		}
						
		// Abrir alert
		if(idAlert != null && !"".equals(idAlert)){
			if(idComponente.indexOf("cerrar") == 0)
				gestorEstados.closeModalAlert(idAlert);
			else
				gestorEstados.openModalAlert(idAlert);
			//gestorEstados.closeModalAlert(idAlert);
		}
		
	}

}
