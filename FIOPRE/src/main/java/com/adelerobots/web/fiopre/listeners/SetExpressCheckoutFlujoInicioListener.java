package com.adelerobots.web.fiopre.listeners;

import com.adelerobots.fioneg.util.PaypalUtilities;
import com.adelerobots.web.fiopre.utilidades.ConfigUtils;
import com.paypal.sdk.core.nvp.NVPDecoder;
import com.paypal.sdk.exceptions.PayPalException;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.utilidades.HelperContext;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperCustom;

public class SetExpressCheckoutFlujoInicioListener extends AbstractUserProcessListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7597088990631103048L;

	public void procesarAjaxChangeListener(
			GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		// Identificador del usuario
		String userID = HelperContext.getInstance().getValueContext("SECURE_USERID");
				
		
		
		// Establecemos que cuando el usuario cancele vuelva al inicio de este flujo		
		String returnURL = getRequestURL().toString() + "?u=" + userID;		
		String cancelURL = getRequestURL().toString() + "?u=" + userID + "&cancelled=true";
					

		try {			
			
			String environment = ConfigUtils.getEnvironment();
			// Pasos para activar Reference Transactions para este cliente
			// y obtener el TRANSACTION ID que se usará para cobrarle 
			// en futuras transacciones
			// 1. Set Up the Payment Authorization calling setExpressCheckout			
			// call method will send the request to the server and return the
			// response NVPString
			String ppresponse = PaypalUtilities.getInstance().setExpressCheckoutWithReferenceTransactions(returnURL, cancelURL);

			// NVPDecoder object is created
			NVPDecoder resultValues = new NVPDecoder();

			// decode method of NVPDecoder will parse the request and decode the
			// name and value pair
			resultValues.decode(ppresponse);
			
			//checks for Acknowledgement and redirects accordingly to display error messages		
			String strAck = resultValues.get("ACK"); 
			if(strAck !=null && !(strAck.equals("Success") || strAck.equals("SuccessWithWarning")))
			{				
				PaypalUtilities.getInstance().gestionErrores(Integer.parseInt(userID), "SetExpressCheckout", ppresponse);
				logger.error("Se ha producido un error al intentar completar un 'setExpressCheckoutWithReferenceTransactions' - SetExpressCheckoutStoreListener:\n" + ppresponse);
				// Parar el proceso y mostrar al usuario un error en la página en la que se encuentra (jsp acuerdo paypal).
				HelperContext.getInstance().setValueContext("errorPaypalSEC", "true", IHelperCustom.FLUJO_CTX_TYPE);				
			}else {
				// 2. Redirect the Customer to PayPal for Authorization				
				String urlPaypal = "https://www." + environment + ".paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=" + resultValues.get("TOKEN");
				redirect(urlPaypal);						
			}		
			
			// 3. Create the Billing Agreement
			// 4. Capture Future Payments (do reference transactions en los batch)
			
		} catch (PayPalException ppEx) {
			// TODO Manejar la excepción más correctamente
			ppEx.printStackTrace();
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

	}
	
	

}
