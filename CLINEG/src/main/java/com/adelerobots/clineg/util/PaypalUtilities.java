package com.adelerobots.clineg.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.adelerobots.clineg.entity.UsuarioC;
import com.adelerobots.clineg.util.keys.Constantes;
import com.paypal.sdk.core.nvp.NVPDecoder;
import com.paypal.sdk.core.nvp.NVPEncoder;
import com.paypal.sdk.exceptions.PayPalException;
import com.paypal.sdk.profiles.APIProfile;
import com.paypal.sdk.profiles.ProfileFactory;
import com.paypal.sdk.services.NVPCallerServices;
import com.paypal.sdk.util.ResponseBuilder;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;

import freemarker.template.TemplateException;

public class PaypalUtilities {

	private static PaypalUtilities instance = new PaypalUtilities();

	private static NVPCallerServices caller;

	public PaypalUtilities() {
		caller = new NVPCallerServices();
		APIProfile profile = null;
		try {
			profile = ProfileFactory.createSignatureAPIProfile();

			String apiUserName = Constantes.getUserApi();
			String pwdApi = Constantes.getPwdApi();
			String signature = Constantes.getSignature();
			String environment = Constantes.getEnvironment();

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
	 * Método que ejecutará las operaciones oportunas en función del grupo al
	 * que pertenezca el error producido
	 * 
	 * @param ctx
	 *            Contexto de ejecución del servicio
	 * @param resultValues
	 *            Valores recibidos en la respuesta de Paypal
	 * @param intCodUsuario
	 *            Identificador único de usuario
	 * @param billingId
	 * @param funcionPaypal
	 */
	public void gestionErroresDoReferenceTransaction(IContextoEjecucion ctx,
			NVPDecoder resultValues, UsuarioC usuario, String billingId,
			String funcionPaypal) {
		String errorCode = resultValues.get("L_ERRORCODE0");
		Integer intCode = null;
		if (errorCode != null && !"".equals(errorCode)) {
			intCode = Integer.parseInt(errorCode);
		}
		switch (intCode) {
		case 10210:
			// Grupo para cortar servicios --------- USUARIO SIN FONDOS
			try {
				// Grupo envío email a nuestra cuenta y a la suya informando de q se reintentará el cobro en dos días
				// Nuestro email
				final Map<String, Object> model = new HashMap<String, Object>();
				String longMessage = resultValues.get("L_LONGMESSAGE0");
				String shortMessage = resultValues.get("L_SHORTMESSAGE0");
				model.put("cnUser", usuario.getCnUsuario());
				model.put("billingId", billingId);
				model.put("funcionPaypal", funcionPaypal);
				model.put("codError", intCode);
				model.put("longMessage", longMessage);
				model.put("shortMessage", shortMessage);

				final String subject = "[FIONA] PAYPAL TRANSACTION FAILED!!!!";
				String body = TemplateUtils.processTemplateIntoString(ctx,
						"mail_team_paypal_failure-html", model);

				final String mailTo = Constantes.getMAIL_NOTIFICATION_ADDR();

				enviarEmail(ctx, mailTo, subject, body);
				
				// Email al usuario ********************************************
				// ***** TODO: Hasta que cree una función aparte o tengamos una gestión específica
				// de errores, no se envía email al usuario a no ser que la función que devuelva el
				// error sea la doReferenceTransaction
				if("doReferenceTransaction".compareToIgnoreCase(funcionPaypal)==0){
					final Map<String, Object> modelUsuario = new HashMap<String, Object>();				
					modelUsuario.put("usuario", usuario);
					final String subjectUsuario = "[FIONA] PAYPAL TRANSACTION FAILED";
					body = TemplateUtils.processTemplateIntoString(ctx,
							"mail_user_paypal_failure_funds-html", modelUsuario);
	
					final String mailToUsuario = usuario.getEmail();
	
					enviarEmail(ctx, mailToUsuario, subjectUsuario, body);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TemplateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;		
		default:
			try {
				// Grupo envío email a nuestra cuenta y a la suya informando de q se reintentará el cobro en dos días
				// Nuestro email
				final Map<String, Object> model = new HashMap<String, Object>();
				String longMessage = resultValues.get("L_LONGMESSAGE0");
				String shortMessage = resultValues.get("L_SHORTMESSAGE0");
				model.put("cnUser", usuario.getCnUsuario());
				model.put("billingId", billingId);
				model.put("funcionPaypal", funcionPaypal);
				model.put("codError", intCode);
				model.put("longMessage", longMessage);
				model.put("shortMessage", shortMessage);

				final String subject = "[FIONA] PAYPAL TRANSACTION FAILURE!!!!";
				String body = TemplateUtils.processTemplateIntoString(ctx,
						"mail_team_paypal_failure-html", model);

				final String mailTo = Constantes.getMAIL_NOTIFICATION_ADDR();

				enviarEmail(ctx, mailTo, subject, body);
				
				// Email al usuario ********************************************
				// ***** TODO: Hasta que cree una función aparte o tengamos una gestión específica
				// de errores, no se envía email al usuario a no ser que la función que devuelva el
				// error sea la doReferenceTransaction
				if("doReferenceTransaction".compareToIgnoreCase(funcionPaypal)==0){
					final Map<String, Object> modelUsuario = new HashMap<String, Object>();				
					modelUsuario.put("usuario", usuario);
					final String subjectUsuario = "[FIONA] PAYPAL TRANSACTION FAILURE";
					body = TemplateUtils.processTemplateIntoString(ctx,
							"mail_user_paypal_failure-html", modelUsuario);
	
					final String mailToUsuario = usuario.getEmail();
	
					enviarEmail(ctx, mailToUsuario, subjectUsuario, body);
				}
				

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TemplateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		}

	}

	/**
	 * Método para enviar un email
	 * 
	 * @param ctx
	 *            Contexto de ejecución del servicio
	 * @param pEmail
	 *            Email del destinatario
	 * @param pSubject
	 *            Asunto del email
	 * @param pBody
	 *            Cuerpo del email
	 * @param filenames
	 *            Nombres de los ficheros a adjuntar en el mensaje
	 */
	private void enviarEmail(IContextoEjecucion ctx, String pEmail,
			String pSubject, String pBody) throws Exception {

		Properties props = new Properties();

		// Nombre del host de correo, en este caso de adelerobots.com
		// TODO mover a properties
		props.setProperty("mail.smtp.host", "mail.adelerobots.com");

		// TLS si esta disponible
		props.setProperty("mail.smtp.starttls.enable", "false");

		// Puerto para envio de correos
		props.setProperty("mail.smtp.port", "25");

		// Nombre del usuario
		props.setProperty("mail.smtp.user", Constantes.getMAIL_SENDER_ADDR());

		// Si requiere o no usuario y password para conectarse.
		props.setProperty("mail.smtp.auth", "true");

		// Obtenemos instancia de sesion
		// y activamos el debug
		Session session = Session.getDefaultInstance(props);
		session.setDebug(true);

		// Construimos el mensaje a enviar
		MimeMessage message = new MimeMessage(session);

		// Preparamos el contenido HTML
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		Multipart multipart = new MimeMultipart();

		messageBodyPart.setContent(pBody, "text/html; charset=UTF-8");
		multipart.addBodyPart(messageBodyPart);

		
		try {
			// Quien envia el correo
			message.setFrom(new InternetAddress(Constantes
					.getREGISTER_MAIL_SENDER(), "Fiona"));
			// A quien va dirigido
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					pEmail));
			// Asunto del mensaje
			message.setSubject(pSubject);
			// Texto del mensaje
			message.setText(pBody);
			// Contenido HTML
			message.setContent(multipart);

			// Para enviar el mensaje...

			// Indicamos el protocolo a usar
			Transport t = session.getTransport("smtp");
			// Establecer conexion indicando usuario y contraseña
			// propertizar
			t.connect(Constantes.getMAIL_SENDER_ADDR(),
					Constantes.getMAIL_SENDER_PW());

			// Completamos el envio
			t.sendMessage(message, message.getAllRecipients());

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (AddressException e) {

			e.printStackTrace();
		} catch (MessagingException e) {

			e.printStackTrace();
		}

	}
	
	/**
	 * Método que indica si un acuerdo está o no activo en función de la respuesta Paypal
	 * 
	 * @param response Respuesta de Paypal a baUpdate
	 * @return
	 * @throws PayPalException
	 */
	public boolean checkBillingAgreementStatus(String response) throws PayPalException{
		boolean active = true;
		
		NVPDecoder resultValues = new NVPDecoder();
		
		// decode method of NVPDecoder will parse the request and decode the
		// name and value pair
		
		resultValues.decode(response);
		
		
		// checks for Acknowledgement and redirects accordingly to display
		// error messages
		String strAck = resultValues.get("ACK");
		if (strAck != null
				&& !(strAck.equals("Success") || strAck
						.equals("SuccessWithWarning"))) {
			// Error
			active = false;
		} else {
			// En este punto todo ha ido bien
			// Ejecutar cobro Papal
			String status = resultValues.get("BILLINGAGREEMENTSTATUS");
			if(status.compareToIgnoreCase("Active")!=0)
				active = false;
			
		}	
		
		return active;
	}
	
	
	/**
	 * Método que ejecutará las operaciones oportunas en función del grupo al
	 * que pertenezca el error producido
	 * 
	 * @param ctx
	 *            Contexto de ejecución del servicio
	 * @param resultValues
	 *            Valores recibidos en la respuesta de Paypal	
	 * @param funcionPaypal
	 * 			  Nombre de la función paypal que ha fallado
	 */
	public void gestionErroresMassPay(IContextoEjecucion ctx,
			NVPDecoder resultValues, String funcionPaypal) {
		ResponseBuilder rb=new ResponseBuilder();
		try {
			@SuppressWarnings("static-access")
			String resp=rb.BuildResponse(resultValues,"PayPal API Error","A PayPal API has returned an error!");
			final Map<String, Object> model = new HashMap<String, Object>();			
			model.put("respuestaLlamada", resp);
			model.put("funcionPaypal", funcionPaypal);
			
			// Enviar email con la respuesta
			final String subject = "[FIONA] PAYPAL TRANSACTION FAILED!!!!";
			String body = TemplateUtils.processTemplateIntoString(ctx,
					"mail_team_paypal_payment_failure-html", model);

			final String mailTo = Constantes.getMAIL_NOTIFICATION_ADDR();

			enviarEmail(ctx, mailTo, subject, body);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void enviarNotificacionArchivoMassPay(IContextoEjecucion contexto, String nombrePlantilla, String nombreArchivo,
			String subject, String destinatario){		
		
		final Map<String, Object> model = new HashMap<String, Object>();		
		model.put("archivo", nombreArchivo);
				
		
		try {
			final String body = TemplateUtils.processTemplateIntoString(contexto, nombrePlantilla, model);
			
			Properties props = new Properties();

			// Nombre del host de correo, en este caso de adelerobots.com
			// TODO mover a properties
			props.setProperty("mail.smtp.host", "mail.adelerobots.com");

			// TLS si esta disponible
			props.setProperty("mail.smtp.starttls.enable", "false");

			// Puerto para envio de correos
			props.setProperty("mail.smtp.port", "25");

			// Nombre del usuario
			props.setProperty("mail.smtp.user", Constantes.getMAIL_SENDER_ADDR());

			// Si requiere o no usuario y password para conectarse.
			props.setProperty("mail.smtp.auth", "true");

			// Obtenemos instancia de sesion
			// y activamos el debug
			Session session = Session.getDefaultInstance(props);
			session.setDebug(true);

			// Construimos el mensaje a enviar
			MimeMessage message = new MimeMessage(session);
			
			// Preparamos el contenido HTML
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();

			messageBodyPart.setContent(body, "text/html; charset=UTF-8");
			multipart.addBodyPart(messageBodyPart);
			
			// Archivos adjuntos
			messageBodyPart = new MimeBodyPart();	        
	        DataSource source = new FileDataSource(nombreArchivo);
	        messageBodyPart.setDataHandler(new DataHandler(source));
	        messageBodyPart.setFileName(nombreArchivo);
	        multipart.addBodyPart(messageBodyPart);			
			
			
			// Quien envia el correo
			message.setFrom(new InternetAddress(Constantes.getREGISTER_MAIL_SENDER(), "Fiona"));
			// A quien va dirigido
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
			// Asunto del mensaje
			message.setSubject(subject);
			// Texto del mensaje
			message.setText(body);
			// Contenido HTML
			message.setContent(multipart);
			
			// Para enviar el mensaje...

			// Indicamos el protocolo a usar
			Transport t = session.getTransport("smtp");
			// Establecer conexion indicando usuario y contraseña
			//propertizar
			t.connect(Constantes.getMAIL_SENDER_ADDR(), Constantes.getMAIL_SENDER_PW());

			// Completamos el envio
			t.sendMessage(message, message.getAllRecipients());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
