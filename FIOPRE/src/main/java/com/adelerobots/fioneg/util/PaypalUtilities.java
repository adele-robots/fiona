package com.adelerobots.fioneg.util;

import com.adelerobots.web.fiopre.utilidades.ConfigUtils;
import com.paypal.sdk.core.nvp.NVPEncoder;
import com.paypal.sdk.exceptions.PayPalException;
import com.paypal.sdk.profiles.APIProfile;
import com.paypal.sdk.profiles.ProfileFactory;
import com.paypal.sdk.services.NVPCallerServices;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.presentacion.componentes.event.api.SNInvoker;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.persistencia.DatoFawnaFactory;
import com.treelogic.fawna.presentacion.core.persistencia.IDatosFawna;

public class PaypalUtilities {

	private static PaypalUtilities instance = new PaypalUtilities();

	private static NVPCallerServices caller;

	public PaypalUtilities() {
		caller = new NVPCallerServices();
		APIProfile profile = null;
		try {
			profile = ProfileFactory.createSignatureAPIProfile();

			String apiUserName = ConfigUtils.getUserApi();
			String pwdApi = ConfigUtils.getPwdApi();
			String signature = ConfigUtils.getSignature();
			String environment = ConfigUtils.getEnvironment();

			profile.setAPIUsername(apiUserName);
			profile.setAPIPassword(pwdApi);
			profile.setSignature(signature);
			profile.setEnvironment(environment);

			caller.setAPIProfile(profile);
		} catch (PayPalException ppEx) {
			ppEx.printStackTrace();
		}
	}

	public static PaypalUtilities getInstance() {
		return instance;
	}

	/**
	 * Método que ejecuta la llamada a la API de Paypal con el método
	 * "DoReferenceTransaction" que nos permitirá realizar un cobro en base a
	 * una transacción anterior
	 * 
	 * @param billingId
	 *            Identificador único del acuerdo de cobro con el usuario
	 * @param amount
	 *            Cantidad a cobrar
	 * @return
	 */
	public String doReferenceTransaction(String billingId, String amount, String desc) {
		// Endpoint URL: https://api-3t.sandbox.paypal.com/nvp
		// HTTP method: POST
		// POST data:
		// USER=insert_merchant_user_name_here
		// &PWD=insert_merchant_password_here
		// &SIGNATURE=insert_merchant_signature_value_here
		// &METHOD=DoReferenceTransaction
		// &VERSION=86
		// &AMT=50 #The amount the buyer will pay in a payment period
		// &CURRENCYCODE=USD #The currency, e.g. US dollars
		// &PAYMENTACTION=SALE #The type of payment
		// &REFERENCEID=B-7FB31251F28061234 #Billing agreement ID received in
		// the DoExpressCheckoutPayment call
		NVPEncoder encoder = new NVPEncoder();
		String ppresponse = "";

		encoder.add("METHOD", "DoReferenceTransaction");
		// The amount the buyer will pay in a payment period
		encoder.add("AMT", amount);
		// The currency, e.g. US dollars
		encoder.add("CURRENCYCODE", "USD");
		// The type of payment
		encoder.add("PAYMENTACTION", "Sale");
		// Billing agreement ID received in the DoExpressCheckoutPayment call
		encoder.add("REFERENCEID", billingId);
		if(desc!=null && !"".equals(desc)){
			// Description of items the buyer is purchasing.
			encoder.add("DESC", desc);
		}

		try {
			// encode method will encode the name and value and form NVP string
			// for the request
			String strNVPRequest = encoder.encode();

			// call method will send the request to the server and return the
			// response NVPString
			ppresponse = (String) caller.call(strNVPRequest);
		} catch (PayPalException ppEx) {
			// TODO: Manejar la excepción más correctamente
			ppEx.printStackTrace();
		}

		return ppresponse;
	}

	/**
	 * Método que permite comprobar el estado de un acuerdo de cobro con un
	 * usuario
	 * 
	 * @param billingId
	 *            Identificador del acuerdo de cobro
	 * @return
	 */
	public String baUpdate(String billingId) {
		NVPEncoder encoder = new NVPEncoder();
		String ppresponse = "";

		encoder.add("METHOD", "BillAgreementUpdate");
		// Billing agreement ID
		encoder.add("REFERENCEID", billingId);

		try {
			// encode method will encode the name and value and form NVP string
			// for the request
			String strNVPRequest = encoder.encode();

			// call method will send the request to the server and return the
			// response NVPString
			ppresponse = (String) caller.call(strNVPRequest);
		} catch (PayPalException ppEx) {
			// TODO: Manejar la excepción más correctamente
			ppEx.printStackTrace();
		}

		return ppresponse;
	}

	/**
	 * Método que realiza la llamada a la API para el método "CreateBillingAgreement"
	 * que nos permite crear un acuerdo de cobro con el comprador
	 * @param token Token devuelto al llamar previamente a SetExpressCheckout y recibir
	 * confirmación del cliente
	 * @return Devuelve la respuesta de Paypal codificada
	 */
	public String createBillingAgreementForReferenceTransactions(String token) {

		// Endpoint URL: https://api-3t.sandbox.paypal.com/nvp
		// HTTP method: POST
		// POST data:
		// USER=insert_merchant_user_name_here
		// &PWD=insert_merchant_password_here
		// &SIGNATURE=insert_merchant_signature_value_here
		// &METHOD=CreateBillingAgreement
		// &VERSION=86
		// &TOKEN=insert_token_value_here

		// NVPEncoder object is created and all the name value pairs are
		// loaded into it.
		NVPEncoder encoder = new NVPEncoder();
		String ppresponse = "";

		encoder.add("METHOD", "CreateBillingAgreement");
		// Payment authorization
		encoder.add("TOKEN", token);

		try {
			// encode method will encode the name and value and form NVP string
			// for the request
			String strNVPRequest = encoder.encode();

			// call method will send the request to the server and return the
			// response NVPString
			ppresponse = (String) caller.call(strNVPRequest);
		} catch (PayPalException ppEx) {
			// TODO: Manejar la excepción más correctamente
			ppEx.printStackTrace();
		}

		return ppresponse;

	}

	/**
	 * Método que realiza la llamada a la API para el método "SetExpressCheckout"
	 * que permite iniciar el proceso de establecimiento de un acuerdo de cobro
	 * con el comprador
	 * @param returnURL Debe indicarse a Paypal la URL a la que se volverá si todo
	 * ha ido bien
	 * @param cancelURL Debe indicarse a Paypal la URL a la que se volverá si el 
	 * comprador cancela el acuerdo 
	 * @return Se devolverá la respuesta recibida de Paypal
	 */
	public String setExpressCheckoutWithReferenceTransactions(String returnURL,
			String cancelURL) {

		// Endpoint URL: https://api-3t.sandbox.paypal.com/nvp
		// HTTP method: POST
		// POST data:
		// USER=insert_merchant_user_name_here
		// &PWD=insert_merchant_password_here
		// &SIGNATURE=insert_merchant_signature_value_here
		// &METHOD=SetExpressCheckout [X]
		// &VERSION=86
		// &PAYMENTREQUEST_0_PAYMENTACTION=AUTHORIZATION #Payment authorization
		// [X]
		// &PAYMENTREQUEST_0_AMT=0 #The amount authorized [X]
		// &PAYMENTREQUEST_0_CURRENCYCODE=USD #The currency, e.g. US dollars [X]
		// &L_BILLINGTYPE0=MerchantInitiatedBilling #The type of billing
		// agreement [X]
		// &L_BILLINGAGREEMENTDESCRIPTION0=ClubUsage #The description of the
		// billing agreement [X]
		// &cancelUrl=http://www.yourdomain.com/cancel.html #For use if the
		// consumer decides not to proceed with payment
		// &returnUrl=http://www.yourdomain.com/success.html #For use if the
		// consumer proceeds with payment

		// NVPEncoder object is created and all the name value pairs are
		// loaded into it.
		NVPEncoder encoder = new NVPEncoder();
		String ppresponse = "";

		encoder.add("METHOD", "SetExpressCheckout");
		// For use if the consumer decides not to proceed with payment
		encoder.add("RETURNURL", returnURL);
		// For use if the consumer proceeds with payment
		encoder.add("CANCELURL", cancelURL);
		// Payment authorization
		encoder.add("PAYMENTREQUEST_0_PAYMENTACTION", "AUTHORIZATION");
		// The amount authorized
		// Cantidad a 0 para no cobrar nada en un primer momento
		// y simplemente obtener un billing id para uso posterior
		encoder.add("PAYMENTREQUEST_0_AMT", "0");
		// The currency, e.g. US dollars
		encoder.add("PAYMENTREQUEST_0_CURRENCYCODE", "USD");
		// The type of billing agreement
		encoder.add("L_BILLINGTYPE0", "MerchantInitiatedBilling");
		// The description of the billing agreement
		encoder.add("L_BILLINGAGREEMENTDESCRIPTION0",
				"Fiona account subscription");

		try {
			// encode method will encode the name and value and form NVP string
			// for the request
			String strNVPRequest = encoder.encode();

			// call method will send the request to the server and return the
			// response NVPString
			ppresponse = (String) caller.call(strNVPRequest);
		} catch (PayPalException ppEx) {
			// TODO: Manejar la excepción más correctamente
			ppEx.printStackTrace();
		}

		return ppresponse;

	}
	
	/**
	 * Método que ejecutará las operaciones oportunas en función del grupo al
	 * que pertenezca el error producido
	 *	 
	 * @param resultValues
	 *            Valores recibidos en la respuesta de Paypal
	 * @param intCodUsuario
	 *            Identificador único de usuario
	 * @param strNombreFuncion Nombre de la función de paypal que produce el error
	 * @param strResponse Respuesta de la llamada a paypal que produce el error
	 * @throws FawnaInvokerException 
	 * @throws PersistenciaException 
	 * @throws FactoriaDatosException 
	 */
	public void gestionErrores(
			Integer intCodUsuario, String strNombreFuncion,
			String strResponse) throws FactoriaDatosException, PersistenciaException, FawnaInvokerException {
		
		invokeManagePayPalFailure(intCodUsuario,strNombreFuncion,strResponse);		
		

	}
	
	/**
	 * Método para la invocación del servicio que manda el email con la solicitud
	 * de un usuario de subir una configuración a producción
	 * 
	 * @param intCodSpark
	 *            Identificador único del spark
	 * @param strNombreFichero
	 *            nombre del fichero xml con la configuración
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected IContexto[] invokeManagePayPalFailure(final Integer intCodUsuario,
			final String strNombreFuncion,String strResponse) throws FactoriaDatosException,
			PersistenciaException, FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", intCodUsuario, "BigDecimal");
		if (strNombreFuncion != null && !"".equals(strNombreFuncion)) {
			datosEntrada.putDato("1", strNombreFuncion, "String");
		}	
		datosEntrada.putDato("2", strResponse, "String");				

		IContexto[] salida = invoker.invokeSNParaContextos("029", "072",
				datosEntrada, false);

		return salida;

	}

}
