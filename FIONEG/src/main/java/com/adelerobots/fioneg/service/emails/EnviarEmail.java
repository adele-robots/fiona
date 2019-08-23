package com.adelerobots.fioneg.service.emails;

import java.util.Date;
import java.util.Properties;

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

import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;


/**
 * Servicio de negocio para enviar un email  
 */
public class EnviarEmail extends ServicioNegocio {

	private static final int CTE_POSICION_TIRA_EMAIL = 0;
	private static final int CTE_POSICION_TIRA_SUBJECT = 1;
	private static final int CTE_POSICION_TIRA_BODY = 2;

	private static FawnaLogHelper LOGGER = FawnaLogHelper.getLog(EnviarEmail.class);

	/**
	 * Constructor por defecto
	 */
	public EnviarEmail() {
		super();
	}

	/**
	 * Ejecuta la logica de negocio del servicio
	 */
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto,
			IDatosEntradaTx datosEntrada) {

		LOGGER
				.info("Inicio Ejecucion del SN 029004: Enviar email");
		Date datInicio = new Date();
		
		final String email = datosEntrada.getString(CTE_POSICION_TIRA_EMAIL);		
		String subject = datosEntrada.getString(CTE_POSICION_TIRA_SUBJECT);
		if(subject == null)
		{
			subject = "";
		}
		final String body = datosEntrada.getString(CTE_POSICION_TIRA_BODY);
		
		
		// Enviar correo
		try {
			enviarEmail(email, subject, body);
		} catch (Exception e) {
			logger.warn("Fallo en el envio del email");
			e.printStackTrace();
		}


		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029004: Enviar email. Tiempo total = "
						+ tiempoTotal + "ms");

		return null;
	}

	/**
	 * Método para enviar un email
	 * 
	 * @param pEmail
	 *            Email del destinatario
	 * @param pSubject
	 *            Asunto del email
	 * @param pBody
	 *            Cuerpo del email
	 */
	private void enviarEmail(String pEmail, String pSubject,String pBody) throws Exception {
		
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
			message.setFrom(new InternetAddress(Constantes.getREGISTER_MAIL_SENDER(), "Fiona"));
			// A quien va dirigido
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(pEmail));
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
			//propertizar
			t.connect(Constantes.getMAIL_SENDER_ADDR(), Constantes.getMAIL_SENDER_PW());

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

}
