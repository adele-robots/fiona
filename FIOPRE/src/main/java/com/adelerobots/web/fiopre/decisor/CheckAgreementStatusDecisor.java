package com.adelerobots.web.fiopre.decisor;

import com.adelerobots.fioneg.util.PaypalUtilities;
import com.paypal.sdk.core.nvp.NVPDecoder;
import com.paypal.sdk.exceptions.PayPalException;
import com.treelogic.fawna.presentacion.core.exception.PresentacionException;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperDecisor;
import com.treelogic.fawna.presentacion.core.webflow.action.IGenericDecisor;

public class CheckAgreementStatusDecisor implements IGenericDecisor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6536120589686785319L;

	public String ejecutar(IHelperDecisor helper) throws PresentacionException {
		String resultado = "noAgreement";
		
		String billingAgreementId = helper.getValueContext("SECURE_USER_BILLING_AGREEMENT_ID");	
		
		
		if(billingAgreementId != null && !"".equals(billingAgreementId)){
			// Hacemos llamada a Paypal para comprobar el estado del acuerdo
			String ppresponse = PaypalUtilities.getInstance().baUpdate(billingAgreementId);
			try{
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
				resultado = "noAgreement";
			} else {
				// En este punto todo ha ido bien así que obtenemos el status
				String status = resultValues.get("BILLINGAGREEMENTSTATUS");
				if(status.compareToIgnoreCase("Active")==0)
					resultado = "agreement";
			}
			}catch(PayPalException ppEx){				
				ppEx.printStackTrace();
			}
			
			
		}
		
		return resultado;
	}

}
