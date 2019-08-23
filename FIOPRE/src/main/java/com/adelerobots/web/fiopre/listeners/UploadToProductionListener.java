package com.adelerobots.web.fiopre.listeners;

import java.math.BigDecimal;
import java.math.RoundingMode;


import com.adelerobots.fioneg.util.PaypalUtilities;
import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.paypal.sdk.core.nvp.NVPDecoder;
import com.paypal.sdk.exceptions.PayPalException;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.utilidades.HelperContext;

public class UploadToProductionListener extends AbstractUserProcessListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4101597371551859765L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		// Id de usuario
		Integer intCodUsuario = ContextUtils.getUserIdAsInteger();
		
		// Usuarios concurrentes
		String strNumUsersConcurrentes = (String)gestorDatos.getValue("numUsuariosConcu");		
		Integer intNumUserConcurrentes = Integer.valueOf(strNumUsersConcurrentes);
		
		// Tiempo
		String strIdUnidadTiempo = (String)gestorDatos.getValue("idUnidadTiempo");		
		Integer intIdUnidadTiempo = Integer.valueOf(strIdUnidadTiempo);
		
		// Resolución
		String strIdResolution = (String)gestorDatos.getValue("resolution");		
		Integer intIdResolution = Integer.valueOf(strIdResolution);
		
		// Disponibilidad
		Boolean highAvailability = (Boolean)gestorDatos.getValue("checkAvailability");		
		Integer intHighAvailability = highAvailability?new Integer(1):new Integer(0);
		
		Float precioTotal = null;
		if(gestorDatos.getValue("precioTotal") != null && !"".equals(gestorDatos.getValue("precioTotal")) && !"0.0".equals(gestorDatos.getValue("precioTotal"))){
			precioTotal = (Float)gestorDatos.getValue("precioTotal");
			BigDecimal bidPrecioTotal = new BigDecimal(precioTotal);
			bidPrecioTotal = bidPrecioTotal.setScale(2, RoundingMode.HALF_EVEN);
			precioTotal = bidPrecioTotal.floatValue();		
		}else{
			if(gestorDatos.getValue("precioTotalTiempo") != null && !"".equals(gestorDatos.getValue("precioTotalTiempo")) & !"0.0".equals(gestorDatos.getValue("precioTotalTiempo"))){
				Float precioTotalTiempo = (Float)gestorDatos.getValue("precioTotalTiempo");
				BigDecimal bidPrecioTotal = new BigDecimal(precioTotalTiempo);
				bidPrecioTotal = bidPrecioTotal.setScale(2, RoundingMode.HALF_EVEN);
				precioTotal = bidPrecioTotal.floatValue();				
			}else{
				Float precioTotalUso = (Float)gestorDatos.getValue("precioTotalUso");
				Float precioHosting = (Float)gestorDatos.getValue("precioTotalHosting");
				BigDecimal bidPrecioTotal = new BigDecimal(precioTotalUso+precioHosting);
				bidPrecioTotal = bidPrecioTotal.setScale(2, RoundingMode.HALF_EVEN);
				precioTotal = bidPrecioTotal.floatValue();
				
			}
			 
		}
				
		// TODO cambiar el segundo parámetro cuando podamos distinguir
		// entre ficheros de varias configuraciones
		try {
			String billingAgreementId = HelperContext.getInstance().getValueContext("SECURE_USER_BILLING_AGREEMENT_ID");
			String resultadoAcuerdo = "noAgreement";
			
			if(billingAgreementId != null && !"".equals(billingAgreementId)){
				// Hacemos llamada a Paypal para comprobar el estado del acuerdo
				String ppresponse = PaypalUtilities.getInstance().baUpdate(billingAgreementId);
				
				// NVPDecoder object is created
				NVPDecoder resultValues = new NVPDecoder();

				// decode method of NVPDecoder will parse the request and decode the
				// name and value pair
				resultValues.decode(ppresponse);

				// checks for Acknowledgement and redirects accordingly to display
				// error messages
				String strAck = resultValues.get("ACK");
				if (strAck != null
						&& !(strAck.equals("Success") || strAck
								.equals("SuccessWithWarning"))) {
					// TODO: Indicar al usuario que el acuerdo previo ha sido cancelado y será necesaria la creación de uno nuevo				
					resultadoAcuerdo = "noAgreement";
				} else {
					// En este punto todo ha ido bien así que obtenemos el status
					String status = resultValues.get("BILLINGAGREEMENTSTATUS");
					if(status.compareToIgnoreCase("Active")==0){
						resultadoAcuerdo = "agreement";						
					}
						
				}
			}
			
			
			// Comprobación del resultado del check del acuerdo
			
			String idAlert = "alertInfoUploadProduction";
			if(resultadoAcuerdo.compareToIgnoreCase("noAgreement")==0){
				idAlert = "alertInfoBillingAgreement";
				// Mostrar mensaje éxito
				// Asignar título y contenido adecuado
				handleModalAlert(gestorDatos, gestorEstados, idAlert, "info", getMessage("FIONA.alertBillAgreement.cabeceraPanel.valor", "Billing agreement needed!"),
						getMessage("FIONA.alertBillAgreement.mensajeOK.valor", "You need to sign a new billing agreement!!"), "", "");				
				
			}else if(resultadoAcuerdo.compareToIgnoreCase("agreement")==0){// Acuerdo activo
				IContexto[] salida = invokeUploadToProduction(intCodUsuario, null,intNumUserConcurrentes,intIdUnidadTiempo, intIdResolution,intHighAvailability, precioTotal);
				String mensaje = salida[0].getString("FIONEG003010");
				
				// Mostrar alert informando al usuario del problema				
				
				if(mensaje.compareToIgnoreCase("OK")==0){
					// Mostrar mensaje éxito
					// Asignar título y contenido adecuado
					handleModalAlert(gestorDatos, gestorEstados, idAlert, "info", getMessage("FIONA.alertUploadProdOk.cabeceraPanel.valor", "Success!"),
							getMessage("FIONA.alertUploadProdOk.mensajeOK.valor", "Request completed!!"), "", "");				
				}else{
					// Mostrar mensaje error
					// Asignar título y contenido adecuado
					handleModalAlert(gestorDatos, gestorEstados, idAlert, "error", getMessage("FIONA.alertUploadProdOk.cabeceraPanelError.valor", "Error!"),
							getMessage("FIONA.alertUploadProdOk.mensajeError.valor", "Something went wrong..."), "", "");
				}				
			}
			// Cerramos la ventana de confirmación
			gestorEstados.closeModalAlert("alertUploadConfirm");
			// Cerramos el diálogo de precios
			gestorEstados.closeModalAlert("dialogoPrecios");
			
		} catch (FactoriaDatosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenciaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FawnaInvokerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(PayPalException ppEx){				
			ppEx.printStackTrace();
		}		

	}

}
