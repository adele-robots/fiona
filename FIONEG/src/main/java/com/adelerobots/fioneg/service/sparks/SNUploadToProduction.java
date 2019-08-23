package com.adelerobots.fioneg.service.sparks;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

import com.adelerobots.fioneg.context.ContextoMensaje;
import com.adelerobots.fioneg.entity.AvatarC;
import com.adelerobots.fioneg.entity.HostingC;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.entity.UnitC;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.manager.AvatarManager;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.SparkManager;
import com.adelerobots.fioneg.manager.UnitManager;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.adelerobots.fioneg.util.TemplateUtils;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNUploadToProduction extends ServicioNegocio {
	
	private static Integer CTE_POSICION_USUARIO_ID = 0;	
	private static Integer CTE_POSICION_FICHERO_CONF_AVATAR = 1;
	private static Integer CTE_POSICION_NUM_USUARIOS = 2;
	private static Integer CTE_POSICION_ID_UNIDAD_TIEMPO = 3;
	private static Integer CTE_POSICION_RESOLUTION = 4;
	private static Integer CTE_POSICION_HIGH_AVAILABILITY = 5;
	private static Integer CTE_POSICION_PRECIO_TOTAL = 6;
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper.getLog(SNUploadToProduction.class);
	
	
	public SNUploadToProduction(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029063: Subida a producción de una configuración.");
		Date datInicio = new Date();
		
		IContexto[] salida = null;
		
		BigDecimal bidCodUsuario = datosEntrada.getDecimal(CTE_POSICION_USUARIO_ID);
		
		Integer intCodUsuario = new Integer(bidCodUsuario.intValue());		
		
		String nombreFicheroConf = datosEntrada.getString(CTE_POSICION_FICHERO_CONF_AVATAR);
		
		BigDecimal bidNumUsuarios = datosEntrada.getDecimal(CTE_POSICION_NUM_USUARIOS);
		Integer intNumUsuarios = new Integer(bidNumUsuarios.intValue());
		
		BigDecimal bidIdUnidadTiempo = datosEntrada.getDecimal(CTE_POSICION_ID_UNIDAD_TIEMPO);
		Integer intIdUnidadTiempo = new Integer(bidIdUnidadTiempo.intValue());
		
		BigDecimal bidIdResolution = datosEntrada.getDecimal(CTE_POSICION_RESOLUTION);
		Integer intIdResolution = new Integer(bidIdResolution.intValue());
		
		BigDecimal bidHighAvailability = datosEntrada.getDecimal(CTE_POSICION_HIGH_AVAILABILITY);
		Integer intHighAvailability = new Integer(bidHighAvailability.intValue());	
		
		BigDecimal bidPrecioTotal = datosEntrada.getDecimal(CTE_POSICION_PRECIO_TOTAL);
		Float floPrecioTotal = new Float(bidPrecioTotal.floatValue());
		
		// Buscar usuario
		UsuariosManager usuarioManager = ManagerFactory.getInstance().getUsuariosManager();
		
		try {
			UsuarioC usuario = usuarioManager.getUsuario(intCodUsuario);
			
			String usuarioMD5 = usuario.getAvatarBuilderUmd5();
			
			// Buscar archivos a adjuntar
			// 1. Archivo de configuración
			List <String> fileNames = new ArrayList<String>();
			if(nombreFicheroConf == null || "".equals(nombreFicheroConf)){
				nombreFicheroConf = "avatar.xml";
			}
			String rutaPrincipal = Constantes.getAPPLICATION_DATA() + usuarioMD5 + "/";
			fileNames.add(rutaPrincipal+nombreFicheroConf);
			
			
			
			SparkManager sparkManager = ManagerFactory.getInstance().getSparkManager();
			
			List <SparkC> sparksConf = sparkManager.getSparksFromXml(intCodUsuario, nombreFicheroConf);
			
			for(int i = 0; i<sparksConf.size(); i++){
				fileNames.add(rutaPrincipal + sparksConf.get(i).getStrNombre() + ".ini");
			}
			
			// Listar todos los inis del directorio
			/**
			File dirBase = Constantes.getApplicationDataFolder(usuarioMD5);
			File [] files = dirBase.listFiles(new IniFileNameFilter());
			
			for(int i = 0; i<files.length;i++){
				fileNames.add(rutaPrincipal+files[i].getName());
			}*/
			
			UnitManager unitManager = ManagerFactory.getInstance().getUnitManager();
			UnitC unidad = unitManager.getUnit(intIdUnidadTiempo);
			
			String resolution = "";
			switch(intIdResolution){
				case Constantes.SMALL_RESOLUTION:
					resolution = "Small";
					break;
				case Constantes.MEDIUM_RESOLUTION:
					resolution = "Medium";
					break;
				case Constantes.BIG_RESOLUTION:
					resolution = "High";
					break;
			}
			
			// Enviar email
			sendUploadToProductionMailToAdmin(contexto,usuario,fileNames, intNumUsuarios,unidad.getStrContent(), resolution,intHighAvailability, floPrecioTotal);
			
			// Construir las relaciones en BBDD para el nuevo avatar en producción
			AvatarManager avatarManager = ManagerFactory.getInstance().getAvatarManager();
			HostingC hosting = sparkManager.getPrecioHosting(intIdUnidadTiempo, intNumUsuarios, intIdResolution.toString(), (intHighAvailability.intValue()==1)?'1':'0');
			
			// Calcular los precios finales más adecuados para cada spark en producción
			Float cantidad = sparkManager.calculateSparksTimeFeeProductionPrice(sparksConf,intNumUsuarios,intIdUnidadTiempo);
			// TODO ¿Qué hacemos con los de uso? ¿cómo calculamos el precio real?
			
			
			AvatarC avatar = avatarManager.createNewAvatar(sparksConf, usuario, intIdUnidadTiempo, hosting);
			// Cobrar al usuario el importe prorrateado de la subida a producción
			// en función de la unidad temporal de facturación elegida
			Float total = cantidad + hosting.getFloFee();
			usuarioManager.realizarPrimerPagoProduccion(contexto, usuario, intIdUnidadTiempo, total, avatar);
						
			salida = ContextoMensaje.rellenarContexto("OK");
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			salida = ContextoMensaje.rellenarContexto("FAIL");
		}
		
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029063: Subida a producción de una configuración. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}
	
	protected void sendUploadToProductionMailToAdmin(	
			final IContextoEjecucion contexto,
			final UsuarioC user,
			final List<String> fileNames,
			final Integer intNumUsuarios,
			final String strPeriod,
			final String strResolution,
			final Integer intHighAvailability,
			final Float floPrecioTotal) 
		throws Exception 
	{
		
		final Map<String, Object> model = new HashMap<String, Object>();		
		model.put("user", user);	
		model.put("nombreFicheroXML", fileNames.get(0));
		model.put("usuarios", intNumUsuarios);
		model.put("period", strPeriod);
		model.put("resolution", strResolution);
		model.put("highAvailability", (intHighAvailability==1)?"Yes":"No");
		model.put("precioTotal", floPrecioTotal);
		
		final String subject = "[FIONA] Upload to production request";
		final String body = TemplateUtils.processTemplateIntoString(contexto, "mail_team_upload_to_production-html", model);
				
		final String mailTo = Constantes.getMAIL_NOTIFICATION_ADDR();
		
		enviarEmail(mailTo, subject, body, fileNames);		
		
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
	 * @param filenames
	 * 			  Nombres de los ficheros a adjuntar en el mensaje
	 */
	private void enviarEmail(String pEmail, String pSubject,String pBody, List<String> fileNames) throws Exception {
		
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
		
		// Archivos adjuntos
		for(int i=0; i< fileNames.size();i++){
			 messageBodyPart = new MimeBodyPart();
	         String filename = fileNames.get(i);
	         DataSource source = new FileDataSource(filename);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(filename);
	         multipart.addBodyPart(messageBodyPart);
		}

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
