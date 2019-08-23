package com.adelerobots.fioneg.util;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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

import com.adelerobots.fioneg.dataclasses.PaymentReceiver;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.util.keys.Constantes;
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
	 * Método que ejecuta la llamada a la API de Paypal con el método
	 * "MassPay" que nos permitirá realizar varios pagos en una sola
	 * llamada (Este método sólo funcionará conrrectamente si el número
	 * de pagos a realizar en una sola llamada no supera los 250)
	 *	  
	 * @param receivers Lista de receptores del pago
	 * @return
	 */
	public String massPay(List<PaymentReceiver> receivers) {
		NVPEncoder encoder = new NVPEncoder();
		String ppresponse = "";

		encoder.add("METHOD", "MassPay");
		// The currency, e.g. US dollars
		encoder.add("CURRENCYCODE", "USD");
		// How to identify receivers
		encoder.add("RECEIVERTYPE", "EmailAddress");
		
		// Construir la lista de 'MassPayItem' con cada usuario a recibir el pago
		for(int i =0; i<receivers.size();i++){
			PaymentReceiver receiver = receivers.get(i);
			
			encoder.add("L_EMAIL"+i, receiver.getEmail());
			BigDecimal bdAmount = new BigDecimal(receiver.getAmount());
			bdAmount.setScale(2,BigDecimal.ROUND_UP);
			encoder.add("L_AMT"+i, bdAmount.toString());
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
	 * Método que permite generar un archivo en formato CSV (comma delimited) con la
	 * información necesaria para ejecutar un pago a varios destinatarios
	 * Formato: Recipient ID:Payment:Currency:Customer ID:Note
	 * 
	 * @param fileName
	 * @param receivers
	 */
	public void generateCSVMassPayFile(String fileName, List<PaymentReceiver> receivers){
		String currencyCode = "USD";
		try
		{
		    FileWriter writer = new FileWriter(fileName);
	 
		    for(int i = 0; i< receivers.size(); i++){
		    	PaymentReceiver receiver = receivers.get(i);
		    	// RECIPIENT ID
		    	writer.append(receiver.getEmail());
		    	writer.append(",");
		    	BigDecimal bdAmount = new BigDecimal(receiver.getAmount());
		    	bdAmount.setScale(2,BigDecimal.ROUND_UP);
		    	// PAYMENT AMOUNT
		    	writer.append(bdAmount.toString());
		    	writer.append(",");
		    	// CURRENCY CODE
		    	writer.append(currencyCode);
		    	writer.append(",");
		    	// CUSTOMER ID (masspay_NUMERO)
		    	writer.append("masspay_" + "_" + i);
		    	if(receiver.getNote() != null && !"".equals(receiver.getNote())){		    		
		    		writer.append(",");
		    		//NOTE
		    		writer.append(receiver.getNote());		    		
		    	}
		    	writer.append("\n");
		    	
		    }	 
		    	 
		    writer.flush();
		    writer.close();
		}
		catch(IOException e)
		{
		     e.printStackTrace();
		} 
	    
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
