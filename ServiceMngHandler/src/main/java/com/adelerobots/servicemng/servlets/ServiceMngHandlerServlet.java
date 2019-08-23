package com.adelerobots.servicemng.servlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.security.PublicKey;
import java.util.List;

import javax.servlet.ServletException;
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
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import com.adelerobots.servicemng.util.EncryptionUtils;
import com.adelerobots.servicemng.util.keys.Constantes;

/**
 * Servlet implementation
 */
public class ServiceMngHandlerServlet extends HttpServlet {

	private Constantes cfg;
	private static String host_address_maquina_usuario = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = -7191681823948810707L;

	/**
	 * Log
	 */
	private final static Logger logger = Logger
			.getLogger(ServiceMngHandlerServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServiceMngHandlerServlet() {
		super();
		cfg = Constantes.getInstance();

		host_address_maquina_usuario = cfg.getMaquinaAvatarUsuarioServiceUrl();

	}

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// String ses = request.getSession().getId();
		HttpSession ses = request.getSession(true);
		logger.info("Session id: " + ses.getId());

		response.setContentType("text/plain;charset=UTF-8");
		// Para utilizar en combinación con CORS (en el js)
		// response.addHeader("Access-Control-Allow-Origin", "*");// Esto no nos
		// sirve con withCredentials a true
		response.addHeader("Access-Control-Allow-Origin",
				request.getHeader("origin"));
		// Por el atributo withCredentials del cliente
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Access-Control-Allow-Methods",
				"OPTIONS, GET, POST, PUT, DELETE");
		// response.addHeader("Access-Control-Allow-Headers",
		// "Content-Type, X-Requested-With, Cookie");
		response.addHeader("Access-Control-Allow-Headers", "*");
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control",
				"private, no-store, no-cache, must-revalidate");

		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");

		PrintWriter out = response.getWriter();
		String accion = request.getParameter("action");

		if ("init".equalsIgnoreCase(accion)) {
			// Obtenemos el email del usuario
			String userEmail = request.getParameter("user");
			// Recuperamos el identificador del avatar
			String avatarName = request.getParameter("avname");
			String ipmaquina = "";

			logger.info("Acción: init\nUser: " + userEmail + "\nAvatar:"
					+ avatarName + "\n");

			PublicKey key = EncryptionUtils.loadPublicKey(
					cfg.getPublicKeyPath(), "RSA");
			String encodedUserMail = EncryptionUtils.encryptBC(userEmail, key);
			String encodedAvatar = EncryptionUtils.encryptBC(avatarName, key);

			// llamar al SN de envíar al chat
			String SOAP_REQUEST = "	<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN030001?wsdl'>"
					+ "	<soapenv:Header/>"
					+ "	<soapenv:Body>"
					+ "		<sn0:ServicioNegocio>"
					+ "			<sn0:datosEntrada compania='?'>"
					+ "				<sn0:SN030001C001 tipo='CHAR' pos='0'>"
					+ encodedUserMail
					+ "</sn0:SN030001C001>"
					+ "				<sn0:SN030001C002 tipo='CHAR' pos='1'>"
					+ encodedAvatar
					+ "</sn0:SN030001C002>"
					+ "			</sn0:datosEntrada>"
					+ "		</sn0:ServicioNegocio>"
					+ "	</soapenv:Body>" + " 	</soapenv:Envelope>";

			String respuesta = makeSoapCall(host_address_maquina_usuario,
					SOAP_REQUEST);
			
			logger.info("Respuesta SOAP:\n"+respuesta);

			// INICIO Parseo de la respuesta
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new StringReader(respuesta));

			Element root = doc.getRootElement();
			Namespace soap = Namespace
					.getNamespace("http://schemas.xmlsoap.org/soap/envelope/");
			//Namespace ns1 = Namespace
				//	.getNamespace("http://fawna.treelogic.com/ConectorWS/services/SN030001?wsdl");
			//Namespace ns1 = Namespace
				//.getNamespace("http://localhost:8080/ConectorWS/services/SN030001?wsdl");
			Namespace ns1 = Namespace
			.getNamespace(cfg.getHostAddress() + Constantes.GET_MAQUINA_AVATAR_USUARIO_SERVICE_ID + "?wsdl");
			
			Element body = root.getChild("Body", soap);
			Element servicioNegocioResponse = body.getChild(
					"ServicioNegocioResponse", ns1);
			Element outEl = (Element) servicioNegocioResponse.getChildren()
					.get(0);
			if (outEl != null) {
				logger.info("Pasamos el out\n");
				Element contexto = outEl.getChild("contexto", ns1);
				if (contexto != null) {
					List<Element> camposContexto = contexto.getChildren();

					for (int i = 0; i < camposContexto.size(); i++) {
						Element campo = camposContexto.get(i);
						String nombreCampo = campo.getAttributeValue("nombre");
						if (nombreCampo.compareToIgnoreCase("SERNEG001030") == 0) {
							ipmaquina = campo.getText();
						}
					}

				} else {
					// Ha habido un error
				}
			}
			// FIN Parseo de la respuesta
			logger.debug("La máquina encontrada tiene la IP:" + ipmaquina);
			out.print(ipmaquina);

			// if (respuesta.contains("error")) {
			// logger.error("Error trying to get avatar machine for this user.");
			// } else {
			// if (respuesta.contains("SERNEG001010")) {
			// // Si se nos devuelve una máquina para el avatar
			//
			// }
			// }
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
}
