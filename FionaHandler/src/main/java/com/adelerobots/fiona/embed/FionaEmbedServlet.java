package com.adelerobots.fiona.embed;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.soap.MessageFactoryImpl;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.adelerobots.fiona.embed.util.keys.Constantes;

/**
 * Servlet implementation class fionaEmbed
 */
public class FionaEmbedServlet extends HttpServlet {

	private Constantes cfg;
	private static String host_address_user = null;
	private static String host_address_check_published = null;
	private static String host_address_start = null;
	private static String host_address_polling = null;
	private static String host_address_chat = null;
	private static String host_address_chat_receive = null;
	private static String host_address_room = null;
	private static String host_address_operators_available = null;
	private static String host_address_new_connection = null;
	private static String host_address_delete_connection = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = -7191681823948810707L;

	/**
	 * Log
	 */
	private final static Logger logger = Logger
			.getLogger(FionaEmbedServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FionaEmbedServlet() {
		super();
		cfg = Constantes.getInstance();

		host_address_user = cfg.getUserDetailserviceUrl();

		host_address_check_published = cfg.getCheckWebPublishedServiceUrl();

		host_address_start = cfg.getStartProcessServiceUrl();

		host_address_polling = cfg.getPollingServiceUrl();

		host_address_chat = cfg.getChatMessagesServiceUrl();
		
		host_address_chat_receive = cfg.getChatMessagesReceiveServiceUrl();

		host_address_room = cfg.getRoomPrepareServiceUrl();
		
		host_address_operators_available = cfg.getOperatorsAvailableServiceUrl();
		
		host_address_new_connection = cfg.getNewConnectionServiceUrl();
		
		host_address_delete_connection = cfg.getDeleteConnectionServiceUrl();

	}
	
    private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// String ses = request.getSession().getId();
		HttpSession ses = request.getSession(true);
		logger.info("Session id: " + ses.getId());
		String success = "false";

		response.setContentType("text/plain;charset=UTF-8");
		// Para utilizar en combinación con CORS (en el js)
		// response.addHeader("Access-Control-Allow-Origin", "*");// Esto no nos
		// sirve con withCredentials a true
		String requestOrigin = request.getHeader("origin");
		if(requestOrigin == null)
			requestOrigin = request.getScheme() + "://" + request.getHeader("Host");
		response.addHeader("Access-Control-Allow-Origin",requestOrigin);

		//logger.info("Cabecera Access-Control-Allow-Origin" + request.getHeader("origin"));
		logger.info("Request origin: " + requestOrigin);
		// Por el atributo withCredentials del cliente
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Access-Control-Allow-Methods",
				"OPTIONS, GET, POST, PUT, DELETE");
		// response.addHeader("Access-Control-Allow-Headers",
		// "Content-Type, X-Requested-With, Cookie");
		response.addHeader("Access-Control-Allow-Headers", "*");
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control",	"private, no-store, no-cache, must-revalidate");

		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		
		// Set P3P header for CORS in IE . See http://www.w3.org/TR/P3P/
		response.setHeader("P3P", "CP=\"CAO PSA OUR\"");

		PrintWriter out = response.getWriter();
		String accion = request.getParameter("action");

		if ("textsend".equalsIgnoreCase(accion)) {
	        Integer idColaChat= getQueueIds(request, "ChatSpark");
			if(idColaChat == null) {
				// Evitamos llamar al SN de forma innecesaria
				// Porque todavía no sabemos el id de la cola al que enviar el mensaje
				return;
			}
			
			// recuperar el texto
			String textosay = request.getParameter("textosend");
			String textResponse = "";

			logger.info("textosend="+textosay);
			// llamar al SN de envíar al chat
			String SOAP_REQUEST = "	<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN029009?wsdl'>"
					+ "	<soapenv:Header/>"
					+ "	<soapenv:Body>"
					+ "		<sn0:ServicioNegocio>"
					+ "			<sn0:datosEntrada compania='?'>"
					+ "				<sn0:SN029013C001 tipo='PIC' pos='0'>"
					+ idColaChat.toString()
					+ "</sn0:SN029013C001>"
					+ "				<sn0:SN029013C002 tipo='CHAR' pos='1'>"
					+ textosay
					+ "</sn0:SN029013C002>"
					+ "			</sn0:datosEntrada>"
					+ "		</sn0:ServicioNegocio>"
					+ "	</soapenv:Body>"
					+ " 	</soapenv:Envelope>";

			// String respuesta = makeSoapCall(HOST_ADDRESS_CHAT, SOAP_REQUEST);
			// String respuesta =
			// makeSoapCall(cfg.getChatMessagesServiceUrl(),SOAP_REQUEST);
			String respuesta = makeSoapCall(host_address_chat, SOAP_REQUEST);

			if (respuesta.contains("error")) {
				logger.error("Error calling chat");
			} 
		} else if ("textreceive".equalsIgnoreCase(accion)) {
	        Integer idColaChat= getQueueIds(request, "ChatSpark");
			if(idColaChat == null) {
				// Evitamos llamar al SN de forma innecesaria
				// Porque todavía no se inició el ChatSpark
				return;
			}
			
			// recuperar el texto			
			String textResponse = "";

			// llamar al SN de recibir respuesta del chat
			String SOAP_REQUEST = "	<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN029042?wsdl'>"
					+ "	<soapenv:Header/>"
					+ "	<soapenv:Body>"
					+ "		<sn0:ServicioNegocio>"
					+ "			<sn0:datosEntrada compania='?'>"
					+ "				<sn0:SN029042C001 tipo='PIC' pos='0'>"
					+ idColaChat.toString()
					+ "</sn0:SN029042C001>"					
					+ "			</sn0:datosEntrada>"
					+ "		</sn0:ServicioNegocio>"
					+ "	</soapenv:Body>"
					+ " 	</soapenv:Envelope>";

			
			String respuesta = makeSoapCall(host_address_chat_receive, SOAP_REQUEST);
			logger.info("Respuesta llamada al textReceive:\n " + respuesta);

			if (respuesta.contains("FIONEG003010")) {
				
				textResponse = respuesta
						.substring(
								respuesta
										.lastIndexOf("<ns1:campoContexto nombre=\"FIONEG003010\">"),
								respuesta.indexOf("</ns1:campoContexto>"));
				textResponse = textResponse.substring(
						textResponse.indexOf(">") + 1, textResponse.length());
				out.print(textResponse);

			} else if (respuesta.contains("error")) {
				logger.error("Error calling chat");
			} //else {
//				logger.error("Error in the chat answer");
//				logger.error("Respuesta llamada al chat:\n" + respuesta);
//				out.print("Oops!I think there was a problem with my speech functions. Visit me later. Maybe I get over.");
//			}

			// Recuperamos las posiciones del ratón sobre el avatar
			String posX = request.getParameter("px");
			String posY = request.getParameter("py");
			String wheelDeltaFactor = request.getParameter("df");
			String mousePositions = posX+" "+posY+" "+wheelDeltaFactor;
			
			// La cola de mensajes en la que espera 'MouseInfoSpark' tiene PID+5 (PID será la del chat) de key para identificarla.
	        Integer idColaMouse= getQueueIds(request, "MouseInfoSpark");
			if(idColaMouse == null) {
				// Evitamos llamar al SN de forma innecesaria
				// Porque no se inició el MouseInfoSpark
				return;
			}
			
			logger.info("make SOAP CALL: mouse positions -> " + mousePositions + " queueId ->"+ idColaMouse);
			// llamar al SN de envíar a la cola de mensajes (en este caso la del 'MouseInfoSpark', PID+5)
			SOAP_REQUEST = "	<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN029013?wsdl'>"
				+ "	<soapenv:Header/>"
				+ "	<soapenv:Body>"
				+ "		<sn0:ServicioNegocio>"
				+ "			<sn0:datosEntrada compania='?'>"
				+ "				<sn0:SN029013C001 tipo='PIC' pos='0'>"
				+ idColaMouse.toString()
				+ "</sn0:SN029013C001>"
				+ "				<sn0:SN029013C002 tipo='CHAR' pos='1'>"
				+ mousePositions
				+ "</sn0:SN029013C002>"
				+ "			</sn0:datosEntrada>"
				+ "		</sn0:ServicioNegocio>"
				+ "	</soapenv:Body>"
				+ " 	</soapenv:Envelope>";
			
			respuesta = "";
			respuesta = makeSoapCall(host_address_chat, SOAP_REQUEST);

			if (respuesta.contains("error")) {
				logger.error("Error calling 'SendMessagesToQueue'");
			} 			

		}else if ("mousebutton".equalsIgnoreCase(accion)) {
			// Recuperamos la información sobre los botones del mouse por parte del usuario
			String buttonInfo = request.getParameter("info");
			
			// La cola de mensajes en la que espera 'MouseInfoSpark' tiene PID+5 (PID será la del chat) de key para identificarla.
	        Integer idColaMouse= getQueueIds(request, "MouseInfoSpark");
			if(idColaMouse == null) {
				// Evitamos llamar al SN de forma innecesaria
				// Porque no se inició el MouseInfoSpark
				return;
			}
			
			logger.info("make SOAP CALL: mouse button info -> " + buttonInfo + " queueId ->"+ idColaMouse);
			// llamar al SN de envíar a la cola de mensajes (en este caso la del 'MouseInfoSpark', PID+5)
			String SOAP_REQUEST = "	<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN029013?wsdl'>"
				+ "	<soapenv:Header/>"
				+ "	<soapenv:Body>"
				+ "		<sn0:ServicioNegocio>"
				+ "			<sn0:datosEntrada compania='?'>"
				+ "				<sn0:SN029013C001 tipo='PIC' pos='0'>"
				+ idColaMouse.toString()
				+ "</sn0:SN029013C001>"
				+ "				<sn0:SN029013C002 tipo='CHAR' pos='1'>"
				+ buttonInfo
				+ "</sn0:SN029013C002>"
				+ "			</sn0:datosEntrada>"
				+ "		</sn0:ServicioNegocio>"
				+ "	</soapenv:Body>"
				+ " 	</soapenv:Envelope>";
						
			String respuesta = makeSoapCall(host_address_chat, SOAP_REQUEST);

			if (respuesta.contains("error")) {
				logger.error("Error calling 'SendMessagesToQueue'");
			} 
			
		} else if ("checkchat".equalsIgnoreCase(accion)) {
			String usermail = request.getParameter("user");
			String usermd5 = findMD5(usermail);
			out.print(detectChatComponent(usermd5));

		} else if ("checkphoto".equalsIgnoreCase(accion)) {
			String usermail = request.getParameter("user");
			String strCodAvatar = request.getParameter("av");
			String usermd5;
			if(strCodAvatar != null && !"".equals(strCodAvatar))
				usermd5 = findMD5(usermail+strCodAvatar);
			else
				usermd5 = findMD5(usermail);				
			out.print(detectPhotoComponent(usermd5));

		} else if ("sigcheck".equalsIgnoreCase(accion)) {
			String usermail = request.getParameter("user");
			String usermd5 = findMD5(usermail);
			out.print(detectChatComponent(usermd5));
			String userId = "";
			// String pidProcess = request.getParameter("id");
			String pidProcess = (String) ses.getAttribute("PIDProceso");
			logger.info("Acción: sigcheck\nPIDProceso: " + pidProcess);

			// recuperar detalle de usuario
			String SOAP_REQUEST = "	<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN029009?wsdl'>"
					+ "	<soapenv:Header/>"
					+ "	<soapenv:Body>"
					+ "	<sn0:ServicioNegocio>"
					+ "	<sn0:datosEntrada compania='?'>"
					+ " 	<sn0:SN029003C001 tipo='CHAR' pos='0'>E</sn0:SN029003C001>"
					+ " 	<sn0:SN029003C002 tipo='CHAR' pos='1'>"
					+ usermail
					+ "</sn0:SN029003C002>"
					+ "	</sn0:datosEntrada>"
					+ "	</sn0:ServicioNegocio>"
					+ "	</soapenv:Body>"
					+ " 	</soapenv:Envelope>";

			// String respuesta = makeSoapCall(HOST_ADDRESS_USER, SOAP_REQUEST);
			// String respuesta =
			// makeSoapCall(cfg.getUserDetailserviceUrl(),SOAP_REQUEST);
			String respuesta = makeSoapCall(host_address_user, SOAP_REQUEST);

			if (respuesta.contains("FIONEG001010")) {
				userId = respuesta.substring(
						respuesta.lastIndexOf("FIONEG001010"),
						respuesta.lastIndexOf("FIONEG001010") + 20);
				userId = userId.substring(userId.indexOf(">") + 1,
						userId.indexOf("<"));
			} else {
				logger.error("Error getting user and polling");
			}

			// PollingHandler call
			// llamar al SN de PollingHandler (DE: USER y PID numéricos)

			SOAP_REQUEST = "	<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN029009?wsdl'>"
					+ "	<soapenv:Header/>"
					+ "	<soapenv:Body>"
					+ "	<sn0:ServicioNegocio>"
					+ "	<sn0:datosEntrada compania='?'>"
					+ " 	<sn0:SN029012C001 tipo='PIC' pos='0'>"
					+ userId
					+ "</sn0:SN029012C001>"
					+ " 	<sn0:SN029012C002 tipo='PIC' pos='1'>"
					+ pidProcess
					+ "</sn0:SN029012C002>"
					+ "	</sn0:datosEntrada>"
					+ "	</sn0:ServicioNegocio>"
					+ "	</soapenv:Body>"
					+ " 	</soapenv:Envelope>";

			// respuesta = makeSoapCall(HOST_ADDRESS_POLLING, SOAP_REQUEST);
			// respuesta =
			// makeSoapCall(cfg.getPollingServiceUrl(),SOAP_REQUEST);
			respuesta = makeSoapCall(host_address_polling, SOAP_REQUEST);
			logger.info("Respuesta de la llamada al polling sigcheck: \n"
					+ respuesta);
			logger.info("Pid que recibe el polling sigcheck: " + pidProcess
					+ "\n");

		} else if ("init".equalsIgnoreCase(accion)) {
			// llamamos a ejecuta proceso
			if (!ses.isNew()) {
				out.print("session_exists");
				return;
			} else {
				// Tiempo máximo de inactividad de la sesión
				ses.setMaxInactiveInterval(10);
			}
			String usermail = request.getParameter("user");
			String usermd5 = findMD5(usermail);
			String room = request.getParameter("room");
			String userId = "";
			String pidProcess = "";
			Integer userIdInt = 2;
			String accountId = ""; 
			Integer accountIdInt = null;

			// Eliminamos la estructura con los id de las colas, si existe
			// Porque todavía no se garantiza que se llame a endConnection
	        ses.removeAttribute(ses.getId());

			// Almacenar archivo de sesion con id de sesion y room
			File avatarSesFile = new File("/tmp/" + ses.getId() + ".ini");
			if(!avatarSesFile.exists()) {
				avatarSesFile.createNewFile();
			}
			Properties p = new Properties();
		    p.load(new FileInputStream(avatarSesFile));
			p.put("room", "\"" + room + "\";");
			p.put("session", "\"" + ses.getId() + "\";");
    		FileOutputStream output = new FileOutputStream(avatarSesFile);
			p.store(output, "Session properties");
			output.close();

			// OBTENER USER DEC A PARTIR DE USERMAILD5 O USERMAIL

			// llamar al detalle de usuario para obtener el id de usuario

			String SOAP_REQUEST = "	<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN029003?wsdl'>"
					+ "	<soapenv:Header/>"
					+ "	<soapenv:Body>"
					+ "	<sn0:ServicioNegocio>"
					+ "	<sn0:datosEntrada compania='?'>"
					+ " 	<sn0:SN029003C001 tipo='CHAR' pos='0'>E</sn0:SN029003C001>"
					+ " 	<sn0:SN029003C002 tipo='CHAR' pos='1'>"
					+ usermail
					+ "</sn0:SN029003C002>"
					+ "	</sn0:datosEntrada>"
					+ "	</sn0:ServicioNegocio>"
					+ "	</soapenv:Body>"
					+ " 	</soapenv:Envelope>";

			// String respuesta = makeSoapCall(HOST_ADDRESS_USER, SOAP_REQUEST);
			// String respuesta =
			// makeSoapCall(cfg.getUserDetailserviceUrl(),SOAP_REQUEST);
			String respuesta = makeSoapCall(host_address_user, SOAP_REQUEST);
			Boolean scriptletAllowed = null;
			String isPublished;
			if (respuesta.contains("FIONEG001010")) {
				userId = respuesta.substring(
						respuesta.lastIndexOf("FIONEG001010"),
						respuesta.lastIndexOf("FIONEG001010") + 20);
				userId = userId.substring(userId.indexOf(">") + 1,
						userId.indexOf("<"));
				userIdInt = Integer.parseInt(userId);
				logger.info("Recuperado usuario con id: " + userIdInt);
				
				// Obtenemos el id del tipo de cuenta
				//FIONEG001070
				accountId = respuesta.substring(
						respuesta.lastIndexOf("FIONEG001070"),
						respuesta.lastIndexOf("FIONEG001070") + 20);
				accountId = accountId.substring(accountId.indexOf(">") + 1,
						accountId.indexOf("<"));
				accountIdInt = Integer.parseInt(accountId);
				logger.info("Recuperado tipo de cuenta del usuario: " + accountIdInt);

				// Llamada al SN que comprueba si el avatar de un usuario puede
				// ser ejecutado desde un scriplet
				SOAP_REQUEST = "	<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN029035?wsdl'>"
						+ "	<soapenv:Header/>"
						+ "	<soapenv:Body>"
						+ "	<sn0:ServicioNegocio>"
						+ "	<sn0:datosEntrada compania='?'>"
						+ " 	<sn0:SN029035C001 tipo='PIC' pos='0'>"
						+ userIdInt
						+ "</sn0:SN029035C001>"
						+ "	</sn0:datosEntrada>"
						+ "	</sn0:ServicioNegocio>"
						+ "	</soapenv:Body>"
						+ " 	</soapenv:Envelope>";

				respuesta = makeSoapCall(host_address_check_published,
						SOAP_REQUEST);

				if (respuesta.contains("FIONEG013030")) {
					isPublished = respuesta.substring(
							respuesta.indexOf("FIONEG013030\">"),
							respuesta.length() - 1);
					isPublished = isPublished.substring(
							isPublished.indexOf(">") + 1,
							isPublished.indexOf("<"));
					if (isPublished.compareTo("1") == 0) {
						scriptletAllowed = Boolean.TRUE;
					} else {
						scriptletAllowed = Boolean.FALSE;
					}

				} else {
					scriptletAllowed = Boolean.FALSE;
					logger.info("There is no entry in table 'WebPublished' for user: "
							+ userIdInt);
				}

			} else {
				// ERROR recuperando detalle de usuario
				// le cargo el muerto al user 2 que soy yo y que se meta el
				// polling igual para que venga arpia luego
				userId = "2";
				logger.error("Error getting user detail");
				out.print(" user error");
			}

			if (scriptletAllowed != null && scriptletAllowed) {

				// Llamar al servicio de negocio que prepara los archivos para
				// la ejecución del avatar
				/*SOAP_REQUEST = "	<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN029037?wsdl'>"
						+ "	<soapenv:Header/>"
						+ "	<soapenv:Body>"
						+ "	<sn0:ServicioNegocio>"
						+ "	<sn0:datosEntrada compania='?'>"
						+ " 	<sn0:SN029037C001 tipo='CHAR' pos='0'>"
						+ usermd5
						+ "</sn0:SN029037C001>"
						+ " 	<sn0:SN029037C002 tipo='CHAR' pos='1'>"
						+ room
						+ "</sn0:SN029037C002>"						
						+ " 	<sn0:SN029037C003 tipo='PIC' pos='2'>"
						+ accountIdInt
						+ "</sn0:SN029037C003>"
						+ "	</sn0:datosEntrada>"
						+ "	</sn0:ServicioNegocio>"
						+ "	</soapenv:Body>"
						+ " 	</soapenv:Envelope>";

				respuesta = makeSoapCall(host_address_room, SOAP_REQUEST);
				
				logger.info("Respuesta de llamada al regenera" + respuesta);*/

				SOAP_REQUEST = "	<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN029009?wsdl'>"
						+ "	<soapenv:Header/>"
						+ "	<soapenv:Body>"
						+ "	<sn0:ServicioNegocio>"
						+ "	<sn0:datosEntrada compania='?'>"
						+ " 	<sn0:SN029009C001 tipo='CHAR' pos='0'>"
						+ usermd5
						+ "</sn0:SN029009C001>"
						+ " 	<sn0:SN029009C002 tipo='PIC' pos='1'>"
						+ userIdInt
						+ "</sn0:SN029009C002>"
						+ "	<sn0:SN029009C004 tipo='CHAR' pos='3'>"
						+ ses.getId()
						+ "</sn0:SN029009C004>"
						+ "	</sn0:datosEntrada>"
						+ "	</sn0:ServicioNegocio>"
						+ "	</soapenv:Body>"
						+ " 	</soapenv:Envelope>";

				// llamar al SN de start
				// respuesta = makeSoapCall(HOST_ADDRESS_START, SOAP_REQUEST);
				// respuesta =
				// makeSoapCall(cfg.getStartProcessServiceUrl(),SOAP_REQUEST);
				respuesta = makeSoapCall(host_address_start, SOAP_REQUEST);

				if (respuesta.contains("FIONEG002010")
						&& respuesta.contains("FIONEG002020")) {
					String pidScript = respuesta.substring(
							respuesta.lastIndexOf("FIONEG002010"),
							respuesta.lastIndexOf("FIONEG002010") + 20);
					pidScript = pidScript.substring(pidScript.indexOf(">") + 1,
							pidScript.indexOf("<"));

					pidProcess = respuesta.substring(
							respuesta.lastIndexOf("FIONEG002020"),
							respuesta.lastIndexOf("FIONEG002020") + 20);
					pidProcess = pidProcess.substring(
							pidProcess.indexOf(">") + 1,
							pidProcess.indexOf("<"));

					// enviar el PID de vuelta
					// if("99991".equalsIgnoreCase(pidScript) ||
					// "99991".equalsIgnoreCase(pidProcess))
					// out.print("error");
					// else
					out.print("ENABLED" + " ");
					out.print(pidScript + " " + pidProcess);
					logger.info("PIDScript:" + pidScript + " PIDProceso:"
							+ pidProcess);
					ses.setAttribute("PIDScript", pidScript);
					ses.setAttribute("PIDProceso", pidProcess);

					if (pidScript.equals("99991") || pidProcess.equals("99991")) {
						logger.info("[MAXCONCURRENT]["
								+ usermd5
								+ "] Se ha sobrepasado el número total de procesos concurrentes permitidos.");
					} else {
						logger.info("[INIT][" + usermd5 + "] Inicio correcto.");
					}

				} else {
					// ERROR al hacer start del proceso
					// Aquí llegamos si el contexto de salida del SN no nos
					// devuelve el PID, con lo cual estamos jodidos
					// porque puede que el programa arranque y perdamos control
					// sobre él ya que desconocemos el PID
					out.print("error");
					logger.error("ERROR trying to start the process. Answer:"
							+ respuesta);
					System.out.println(respuesta);
					ses.setAttribute("PIDScript", "error");
					ses.setAttribute("PIDProceso", "error");
				}

				// llamar al SN de PollingHandler (DE: USER y PID numéricos)

				SOAP_REQUEST = "	<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN029009?wsdl'>"
						+ "	<soapenv:Header/>"
						+ "	<soapenv:Body>"
						+ "	<sn0:ServicioNegocio>"
						+ "	<sn0:datosEntrada compania='?'>"
						+ " 	<sn0:SN029012C001 tipo='PIC' pos='0'>"
						+ userId
						+ "</sn0:SN029012C001>"
						+ " 	<sn0:SN029012C002 tipo='PIC' pos='1'>"
						+ pidProcess
						+ "</sn0:SN029012C002>"
						+ "	</sn0:datosEntrada>"
						+ "	</sn0:ServicioNegocio>"
						+ "	</soapenv:Body>" + " 	</soapenv:Envelope>";

				if (!"99991".equalsIgnoreCase(pidProcess)) {
					// respuesta = makeSoapCall(HOST_ADDRESS_POLLING,
					// SOAP_REQUEST);
					// respuesta =
					// makeSoapCall(cfg.getPollingServiceUrl(),SOAP_REQUEST);
					respuesta = makeSoapCall(host_address_polling, SOAP_REQUEST);
					logger.info("Polling called");
				} else {
					System.out.println("No meto a base de datos el polling");
					logger.warn("Process might be busy");
				}

				respuesta.split("");
				logger.info("Respuesta de la llamada al polling: \n"
						+ respuesta);
				logger.info("Pid que recibe el polling: " + pidProcess + "\n");
			} else {
				out.print("DISABLED");
				logger.info("This scriptlet is disabled at this moment.");

			}
		} else if ("setcookies".equalsIgnoreCase(accion)) {

			String usermail = request.getParameter("user");
			String strCodAvatar = request.getParameter("av");
			String usermd5;
			if(strCodAvatar != null && !"".equals(strCodAvatar))
				usermd5 = findMD5(usermail+strCodAvatar);
			else
				usermd5 = findMD5(usermail);				
			if(!detectCookieComponent(usermd5)) {
				// No tiene el CookieThreadSpark asi que no hace nada
				return;
			}
			
	        Integer idColaCookie= getQueueIds(request, "CookieSpark");
			if(idColaCookie == null) {
				// Evitamos llamar al SN de forma innecesaria
				// Porque todavía no sabemos el id de la cola al que enviar el mensaje
				// Le pedimos que lo vuelva a intentar en un rato
				out.print("false");
				return;
			}

			// Obtener cookies y enviar texto al chat 
			Cookie[] cookies = request.getCookies();
			
			String textosay = "";
			boolean hasUser = false;
			for(int i = 0; i < cookies.length; i++) {
				if(cookies[i].getName().startsWith("_fio")) {
					String name = cookies[i].getName().substring(4);
					
					// recuperar el texto
					textosay += name + "=" + cookies[i].getValue() + ";";
					
					if("_fioUser".equals(cookies[i].getName()))
						hasUser = true;
				}
			}
			
			if(!hasUser) {
				String user = request.getSession().getId();
				Cookie c = new Cookie("_fioUser", user);
				String domain = request.getServerName();
				if(domain.endsWith(".adelerobots.com"))
					domain = ".adelerobots.com"; // para cualquier servidor *.adelerobots.com
				c.setDomain(domain);
				c.setPath("/"); // para cualquier ruta
				c.setMaxAge(3600 * 24 * 365); // 1 año
				response.addCookie(c); // Almacenamos la cookie en el cliente
				textosay += "User" + "=" + user + ";";
			}

			textosay += "IP" + "=" + getClientIpAddr(request) + ";";
			textosay += "URL" + "=" + request.getHeader("referer") + ";";

			logger.info("textosend="+textosay);
			// llamar al SN de envíar al chat
			String SOAP_REQUEST = "	<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN029013?wsdl'>"
					+ "	<soapenv:Header/>"
					+ "	<soapenv:Body>"
					+ "		<sn0:ServicioNegocio>"
					+ "			<sn0:datosEntrada compania='?'>"
					+ "				<sn0:SN029013C001 tipo='PIC' pos='0'>"
					+ idColaCookie.toString()
					+ "</sn0:SN029013C001>"
					+ "				<sn0:SN029013C002 tipo='CHAR' pos='1'>"
					+ textosay
					+ "</sn0:SN029013C002>"
					+ "			</sn0:datosEntrada>"
					+ "		</sn0:ServicioNegocio>"
					+ "	</soapenv:Body>"
					+ " 	</soapenv:Envelope>";

			String respuesta = makeSoapCall(host_address_chat, SOAP_REQUEST);

			if (respuesta.contains("error")) {
				logger.error("Error calling cookie");
			} 
			else {
				//logger.info("Cookies for user " + cookies[i].getValue() + " set successfully");
			}
		} else if ("getid".equalsIgnoreCase(accion)) {
			// no se recuperó el pid pero si se arrancó el proceso, recuperar
			// pid again
			logger.warn("PID hasn't been recovered but process started normally, so we are about to try to get PID again.");
			String mailmd5 = request.getParameter("user");
			int[] pids = getPidFromPs(mailmd5);
			out.print(pids[0] + " " + pids[1]);
			logger.info("PID[0]:" + pids[0] + " PID[1]:" + pids[1]);
		} else if ("endConnection".equalsIgnoreCase(accion)) {
			String resource = request.getParameter("resource");
			if(resource != null && !"".equals(resource)) {
				String SOAP_REQUEST = 
					  "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN026035?wsdl'>"
					+	"<soapenv:Header/>"
					+	"<soapenv:Body>"
					+		"<sn0:ServicioNegocio>"
					+			"<sn0:datosEntrada compania='?'>"
					+				"<sn0:SN026035C001 tipo='CHAR' pos='0'>"
					+					resource
					+				"</sn0:SN026035C001>"
					+			"</sn0:datosEntrada>"
					+		"</sn0:ServicioNegocio>"
					+	"</soapenv:Body>"
					+ "</soapenv:Envelope>";
	
				String respuesta = makeSoapCall(host_address_delete_connection, SOAP_REQUEST);
			}

			//Eliminamos el archivo de sesion que creamos en init
			File avatarSesFile = new File("/tmp/" + ses.getId() + ".ini");
			avatarSesFile.delete();
	        ses.removeAttribute(ses.getId());
            logger.info("Session info deleted");
			
			logger.info("Llamada a endConnection con resource = " + resource);
			out.print("session_invalidated");
			ses.invalidate();
		} else if ("checkTron".equalsIgnoreCase(accion)) {
			String usermail = request.getParameter("user");
			String strCodAvatar = request.getParameter("av");
			String voice = request.getParameter("voice");
			String usermd5;
			if(strCodAvatar != null && !"".equals(strCodAvatar))
				usermd5 = findMD5(usermail+strCodAvatar);
			else
				usermd5 = findMD5(usermail);
			String idRobot = TronIdRobot(usermd5);
			logger.info("User = " + usermail + " Av = " + strCodAvatar + " Voice = " + voice + " Id_Robot = " + idRobot);
			if(idRobot != null && !"".equals(idRobot)) {
				//Cambiamos		"id";		por		id
				if(idRobot.startsWith("\""))	idRobot = idRobot.substring(1);
				if(idRobot.endsWith(";"))		idRobot = idRobot.substring(0, idRobot.length()-1);
				if(idRobot.endsWith("\""))		idRobot = idRobot.substring(0, idRobot.length()-1);
				String SOAP_REQUEST = 
					  "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN026027?wsdl'>"
					+	"<soapenv:Header/>"
					+	"<soapenv:Body>"
					+		"<sn0:ServicioNegocio>"
					+			"<sn0:datosEntrada compania='?'>"
					+				"<sn0:SN026027C001 tipo='PIC' pos='0'>"
					+					idRobot
					+				"</sn0:SN026027C001>"
					+				"<sn0:SN026027C002 tipo='CHAR' pos='1'>"
					+					voice
					+				"</sn0:SN026027C002>"
					+			"</sn0:datosEntrada>"
					+		"</sn0:ServicioNegocio>"
					+	"</soapenv:Body>"
					+ "</soapenv:Envelope>";

				String respuesta = makeSoapCall(host_address_operators_available, SOAP_REQUEST);
				
				String [] operadoresArray = respuesta.split("<ns1:contexto nombre=\"TRONEGN001\">");
				Integer operadores = operadoresArray.length - 1;
				
				if(operadores.equals(new Integer(0)))
					out.print(operadores);
				else {
					
					String stream = request.getParameter("stream");
					Random rand = new Random();
					String resource = String.valueOf(rand.nextLong());
					out.print(findMD5(resource));
					logger.info("Resource = " + resource);
					
					SOAP_REQUEST = 
						  "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN026028?wsdl'>"
						+	"<soapenv:Header/>"
						+	"<soapenv:Body>"
						+		"<sn0:ServicioNegocio>"
						+			"<sn0:datosEntrada compania='?'>"
						+				"<sn0:SN026028C001 tipo='PIC' pos='0'>"
						+					idRobot
						+				"</sn0:SN026028C001>"
						+				"<sn0:SN026028C002 tipo='CHAR' pos='1'>"
						+					resource
						+				"</sn0:SN026028C002>"
						+				"<sn0:SN026028C003 tipo='CHAR' pos='2'>"
						+					voice
						+				"</sn0:SN026028C003>"
						+				"<sn0:SN026028C004 tipo='CHAR' pos='3'>"
						+					stream
						+				"</sn0:SN026028C004>"
						+			"</sn0:datosEntrada>"
						+		"</sn0:ServicioNegocio>"
						+	"</soapenv:Body>"
						+ "</soapenv:Envelope>";
	
					respuesta = makeSoapCall(host_address_new_connection, SOAP_REQUEST);
					
					guardarTronRandomIni(usermd5, resource);
					ses.invalidate();
				}
			}
			else {
				logger.error("No Id_Robot found");
				out.print("no_robot");
				ses.invalidate();
			}
		}
	}

	private String makeSoapCall(String HOST_ADDRESS, String SOAP_REQUEST)
			throws SOAPException, AxisFault, ServiceException {

		// Create a Stream Source of the Request String
		byte[] reqBytes = SOAP_REQUEST.getBytes();
		ByteArrayInputStream bis = new ByteArrayInputStream(reqBytes);
		StreamSource ss = new StreamSource(bis);

		// Create a SOAP Message Object
		MessageFactoryImpl messageFactory = new MessageFactoryImpl();
		SOAPMessage msg = messageFactory.createMessage();
		SOAPPart soapPart = msg.getSOAPPart();

		// Set the soapPart Content with the stream source
		soapPart.setContent(ss);

		// Create a WebService Call
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(HOST_ADDRESS);

		// Invoke the WebService.
		SOAPEnvelope resp = call.invoke(((org.apache.axis.SOAPPart) soapPart)
				.getAsSOAPEnvelope());

		// recuperar el PID

		String respuesta = "";
		try {
			respuesta = resp.getAsString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("ERROR trying to get an answer from request");
			e.printStackTrace();
		}

		return respuesta;

	}
	
	boolean detectChatComponent(final String usermaild5) throws JDOMException,
	IOException, FileNotFoundException {
		return detectComponent(usermaild5, "ChatThreadSpark");
	}
	
	boolean detectCookieComponent(final String usermaild5) throws JDOMException,
	IOException, FileNotFoundException {
		return detectComponent(usermaild5, "CookieThreadSpark");
	}
	
	boolean detectPhotoComponent(final String usermaild5) throws JDOMException,
	IOException, FileNotFoundException {
		return detectComponent(usermaild5, "RemotePhotoCharacterSpark");
	}

	boolean detectComponent(final String usermaild5, final String componente) throws JDOMException,
			IOException, FileNotFoundException {
		boolean found = false;

		// Creamos el builder basado en SAX
		SAXBuilder builder = new SAXBuilder();
		// Construimos el arbol DOM a partir del fichero xml
		// TODO: Modificar en función de la estructura de directorios que haya
		// cuando un usuario pueda tener varias configuraciones.
		File avatarCfgFile = new File("/datos/nfs/users/private/" + usermaild5
				+ "/", "avatar.xml");

		Document doc = builder.build(new FileInputStream(avatarCfgFile));
		Element root = doc.getRootElement();
		Element declarations = root.getChild("ComponentDeclarations");

		@SuppressWarnings("unchecked")
		List<Element> components = declarations.getChildren();
		for (int i = 0; i < components.size() && !found; i++) {
			Element component = components.get(i);
			String valorType = component.getAttributeValue("type");

			if (valorType != null
					&& componente.compareToIgnoreCase(valorType) == 0) {
				found = true;
				logger.info(componente + " Component detected");
			}
		}
		return found;
	}

	private static int[] getPidFromPs(String mailmd5) {
		// final File applicationDataFolder =
		// Constantes.getApplicationDataFolder(mailmd5);
		// final File config = new File(applicationDataFolder,
		// Constantes.DEFAULT_AVATAR_FILENAME);

		final File applicationDataFolder = new File(
				"/datos/nfs/users/private/", mailmd5);
		final File config = new File(applicationDataFolder, "avatar.xml");

		int[] pids;
		pids = new int[2];

		BufferedReader br = null;
		try {
			Process psfe = Runtime.getRuntime().exec(
					new String[] { "bash", "-c",
							"ps -eo pid,ppid,cmd | grep " + mailmd5 });
			br = new BufferedReader(
					new InputStreamReader(psfe.getInputStream()));

			String line = null;
			while ((line = br.readLine()) != null) {
				String parseo[] = null;
				line = line.trim();
				parseo = line.split("\\s+", 3);
				boolean isRun;
				isRun = parseo[2].endsWith(config.getPath());
				// isRun = parseo[2].trim().endsWith(config.getPath());
				// isRun = parseo[2].contains(mailmd5+"/avatar.xml");
				if (isRun) {

					pids[0] = Integer.parseInt(parseo[1]);
					pids[1] = Integer.parseInt(parseo[0]);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("ERROR in getPidFromPs");
			e.printStackTrace();

		} finally {
			// Close resources quietlly
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
				}
			}
		}
		return pids;
	}

	public static String TronIdRobot (final String usermaild5) throws JDOMException,
			IOException, FileNotFoundException {
		String archivo = "TRONHandlerThreadSpark.ini";
		String entity = "";

		File TronIniFile = new File("/datos/nfs/users/private/" + usermaild5
				+ "/", archivo);
		logger.info("Searching Id_Robot in " + TronIniFile.getAbsolutePath());

		if( TronIniFile.exists() ) {
    		Properties p = new Properties();
		    p.load(new FileInputStream(TronIniFile));
    		entity = p.getProperty("Id_Robot");
		}
		return entity;
	}

	public static void guardarTronRandomIni (final String usermaild5, final String random) throws JDOMException,
			IOException, FileNotFoundException {
		String archivo = "TRONHandlerThreadSpark.ini";

		File TronIniFile = new File("/datos/nfs/users/private/" + usermaild5
				+ "/", archivo);
		logger.info("Saving Rand in " + TronIniFile.getAbsolutePath());

		if( TronIniFile.exists() ) {
    		Properties p = new Properties();
		    p.load(new FileInputStream(TronIniFile));
    		p.remove("Rand");
    		p.put("Rand", "\"" + random + "\";");
    		FileOutputStream out = new FileOutputStream(TronIniFile);
    		String comments=
    			"Modos de teleoperacion:\n" +
    			"#1: Solo habla con el operador.\n" +
				"#2: Habla con Rebecca hasta que se conecta el operador.\n" +
				"#3: Habla con Rebecca hasta que no sepa responder. El operador contestará esa pregunta.\n" +
				"#4: Habla con Rebecca hasta que no sepa responder. El operador continúa el resto de la conversación\n" +
				"#5: Habla con Rebecca hasta que llegue a un número espacífico de mensajes. El operador continúa el resto de la conversación\n" +
				"#6: Habla con Rebecca hasta que recibe una etiqueta, a partir de la cual se conecta al WSServer." +
				"#\n" +
				"#Rand saved in date:";
    		p.store(out, comments);
		}
	}
	
    private void setQueueIds(HttpServletRequest request){

        HttpSession session = request.getSession(true);
        final String sessionId = session.getId();

        File tmpDir = new File("/tmp/");
        File [] foundFiles = tmpDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith(sessionId + "_");
            }
        });
        
        Hashtable sparkQueues = (Hashtable) session.getAttribute(sessionId);
        if(sparkQueues == null)
        	sparkQueues = new Hashtable();
        
        for (File foundFile : foundFiles){
                try {
                        Scanner fileScanner = new Scanner(foundFile);
                        int queueId = fileScanner.nextInt();
                        fileScanner.close();
                        String fileName = foundFile.getName();
                        String[] fileNameTokens = fileName.split("_");
                        sparkQueues.put(fileNameTokens[fileNameTokens.length-1], queueId);
                        logger.info("Session info set: " + sessionId + " " + fileNameTokens[fileNameTokens.length-1] + " : " + queueId);
                        if(foundFile.canWrite() && (! foundFile.delete()))
                        		logger.warn("Could not delete " + foundFile.getName());
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                }
        }
        session.setAttribute(sessionId, sparkQueues);
    }
	
    private Integer getQueueIds(HttpServletRequest request, String spark){

        HttpSession session = request.getSession(true);
        final String sessionId = session.getId();
        Hashtable sparkQueues = (Hashtable) session.getAttribute(sessionId);
        if(sparkQueues == null)
        {
        	setQueueIds(request);
        	sparkQueues = (Hashtable) session.getAttribute(sessionId);
        }
        Integer id = (Integer) sparkQueues.get(spark);
        if(id == null) {
        	setQueueIds(request);
        	return (Integer) sparkQueues.get(spark);
        }
        else
        	return id;
    }
	
	
	public static String findMD5(String arg) {

		MessageDigest algorithm = null;
		try {
			algorithm = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsae) {
			try {
				throw new Exception("Cannot find digest algorithm");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		byte[] defaultBytes = arg.getBytes();
		algorithm.reset();
		algorithm.update(defaultBytes);
		byte messageDigest[] = algorithm.digest();
		StringBuffer hexString = new StringBuffer();

		for (int i = 0; i < messageDigest.length; i++) {
			String hex = Integer.toHexString(0xff & messageDigest[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	// doget y dopost encapsulan cualquier petici�n http hacia processRequest
	// <editor-fold defaultstate="collapsed"
	// desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {
			processRequest(request, response);
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {
			processRequest(request, response);
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getClientIpAddr(HttpServletRequest request) {  
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    } 
}
