package com.adelerobots.serneg.manager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

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
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import com.adelerobots.serneg.dataclasses.UsuarioST;
import com.adelerobots.serneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class UsuariosManager {

	private String conexion;

	private static FawnaLogHelper LOGGER = FawnaLogHelper
			.getLog(UsuariosManager.class);

	public UsuariosManager(String conexion) {
		super();
		this.conexion = conexion;
	}

	public UsuarioST soapCallAltaUsuarioST(String nombre, String apellidos,
			String email, String password, String accountType, String website, String billingAgreementId,
			String periodo) throws SOAPException, ServiceException, JDOMException, IOException {

		String soapRequest = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN031003?wsdl'>"
				+ "<soapenv:Header/>"
				+ "<soapenv:Body>"
				+ "<sn0:ServicioNegocio>" + "<sn0:datosEntrada compania='?'>";
		if (nombre != null && !"".equals(nombre)) {
			// "<!--Optional:-->"
			soapRequest += "<sn0:SN031003C001 tipo='CHAR' pos='0'>" + nombre
					+ "</sn0:SN031003C001>";
		}
		if (apellidos != null && !"".equals(apellidos)) {
			// "<!--Optional:-->"
			soapRequest += "<sn0:SN031003C002 tipo='CHAR' pos='1'>" + apellidos
					+ "</sn0:SN031003C002>";
		}
		soapRequest += "<sn0:SN031003C003 tipo='CHAR' pos='2'>" + email
				+ "</sn0:SN031003C003>"
				+ "<sn0:SN031003C004 tipo='CHAR' pos='3'>" + password
				+ "</sn0:SN031003C004>";
		if (accountType != null) {
			// "<!--Optional:-->"
			soapRequest += "<sn0:SN031003C005 tipo='CHAR' pos='4'>"
					+ accountType + "</sn0:SN031003C005>";
		}
		if (website != null && !"".equals(website)) {
			// "<!--Optional:-->"
			soapRequest += "<sn0:SN031003C006 tipo='CHAR' pos='5'>" + website
					+ "</sn0:SN031003C006>";
		}
		if (billingAgreementId != null && !"".equals(billingAgreementId)) {
			// "<!--Optional:-->"
			soapRequest += "<sn0:SN031003C007 tipo='CHAR' pos='6'>" + billingAgreementId
					+ "</sn0:SN031003C007>";
		}
		soapRequest += "<sn0:SN031003C008 tipo='CHAR' pos='7'>" + periodo
		+ "</sn0:SN031003C008>";
		soapRequest += "</sn0:datosEntrada>" + "</sn0:ServicioNegocio>"
				+ "</soapenv:Body>" + "</soapenv:Envelope>";
		
		String respuesta = makeSoapCall(Constantes.getPRINEG_HOST()+Constantes.SN_ALTA_USUARIO_ST,
				soapRequest);
		
		LOGGER.info("Respuesta SOAP:\n"+respuesta);
		
		// INICIO Parseo de la respuesta
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(respuesta));

		Element root = doc.getRootElement();
		Namespace soap = Namespace
				.getNamespace("http://schemas.xmlsoap.org/soap/envelope/");
		//Namespace ns1 = Namespace
			//	.getNamespace("http://fawna.treelogic.com/ConectorWS/services/SN031003?wsdl");
		Namespace ns1 = Namespace
			.getNamespace(Constantes.getSN_NAMESPACE() + Constantes.SN_ALTA_USUARIO_ST + "?wsdl");
		
		Element body = root.getChild("Body", soap);
		Element servicioNegocioResponse = body.getChild(
				"ServicioNegocioResponse", ns1);
		Element outEl = (Element) servicioNegocioResponse.getChildren()
				.get(0);
		UsuarioST usuario = new UsuarioST();
		if (outEl != null) {
			//LOGGER.info("Pasamos el out\n");
			Element contexto = outEl.getChild("contexto", ns1);
			if (contexto != null) {
				List<Element> camposContexto = contexto.getChildren();

				for (int i = 0; i < camposContexto.size(); i++) {
					Element campo = camposContexto.get(i);
					String nombreCampo = campo.getAttributeValue("nombre");
					if (nombreCampo.compareToIgnoreCase("FIONEG001010") == 0) {
						usuario.setUsuarioId(Integer.parseInt(campo.getText()));
					}else if (nombreCampo.compareToIgnoreCase("FIONEG001040") == 0) {
						usuario.setEmail(campo.getText());
					}else if (nombreCampo.compareToIgnoreCase("FIONEG001050") == 0) {
						usuario.setPassword(campo.getText());
					}
				}

			} else {
				// Ha habido un error
				usuario = null;
			}
		}
		// FIN Parseo de la respuesta
		
		return usuario;

	}
	
	public String soapCallSetExpressCheckout(String email, String returnURL, String cancelURL) throws SOAPException, ServiceException, JDOMException, IOException {
		String token = null;
		String soapRequest = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://fawna.treelogic.com/ConectorWS/services/SN031001?wsdl'>"
								+ "<soapenv:Header/>"
									+ "<soapenv:Body>"
										+ "<sn0:ServicioNegocio>"
											+ "<sn0:datosEntrada compania='?'>";
													if(email != null && !"".equals(email)){
														//"<!--Optional:-->"
													soapRequest += "<sn0:SN031001C001 tipo='CHAR' pos='0'>" + email + "</sn0:SN031001C001>";
													}
													soapRequest += "<sn0:SN031001C002 tipo='CHAR' pos='1'>" + returnURL + "</sn0:SN031001C002>"
													+ "<sn0:SN031001C003 tipo='CHAR' pos='2'>" + cancelURL + "</sn0:SN031001C003>"
											+ "</sn0:datosEntrada>"
										+ "</sn0:ServicioNegocio>"
									+ "</soapenv:Body>"
							+ "</soapenv:Envelope>";
		
		String respuesta = makeSoapCall(Constantes.getPRINEG_HOST()+Constantes.SN_SET_EXPRESS_CHECKOUT,
				soapRequest);
		
		LOGGER.info("Respuesta SOAP:\n"+respuesta);
		
		// INICIO Parseo de la respuesta
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(respuesta));

		Element root = doc.getRootElement();
		Namespace soap = Namespace
				.getNamespace("http://schemas.xmlsoap.org/soap/envelope/");
		//Namespace ns1 = Namespace
			//	.getNamespace("http://fawna.treelogic.com/ConectorWS/services/SN031003?wsdl");
		Namespace ns1 = Namespace
			.getNamespace(Constantes.getSN_NAMESPACE() + Constantes.SN_SET_EXPRESS_CHECKOUT + "?wsdl");
		
		Element body = root.getChild("Body", soap);
		Element servicioNegocioResponse = body.getChild(
				"ServicioNegocioResponse", ns1);
		Element outEl = (Element) servicioNegocioResponse.getChildren()
				.get(0);
		
		if (outEl != null) {
			//LOGGER.info("Pasamos el out\n");
			Element contexto = outEl.getChild("contexto", ns1);
			if (contexto != null) {
				List<Element> camposContexto = contexto.getChildren();

				for (int i = 0; i < camposContexto.size(); i++) {
					Element campo = camposContexto.get(i);
					String nombreCampo = campo.getAttributeValue("nombre");
					if (nombreCampo.compareToIgnoreCase("FIONEG003010") == 0) {
						token = campo.getText();
					}
				}

			} else {
				// Ha habido un error
			}
		}
		// FIN Parseo de la respuesta
		
		return token;

	}
	
	public String soapCallSetBillingAgreement(String email, String token) throws SOAPException, ServiceException, JDOMException, IOException {
		String resultado = null;
		String soapRequest = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://fawna.treelogic.com/ConectorWS/services/SN031002?wsdl'>"
								+"<soapenv:Header/>"
									+ "<soapenv:Body>"
										+"<sn0:ServicioNegocio>"
											+"<sn0:datosEntrada compania='?'>"
													+ "<sn0:SN031002C001 tipo='CHAR' pos='0'>" + email + "</sn0:SN031002C001>"
													+ "<sn0:SN031002C002 tipo='CHAR' pos='1'>" + token + "</sn0:SN031002C002>"
											+ "</sn0:datosEntrada>"
										+ "</sn0:ServicioNegocio>"
									+ "</soapenv:Body>"
							+ "</soapenv:Envelope>";
		
		String respuesta = makeSoapCall(Constantes.getPRINEG_HOST()+Constantes.SN_SET_BILLING_AGREEMENT,
				soapRequest);
		
		LOGGER.info("Respuesta SOAP:\n"+respuesta);
		
		// INICIO Parseo de la respuesta
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(respuesta));

		Element root = doc.getRootElement();
		Namespace soap = Namespace
				.getNamespace("http://schemas.xmlsoap.org/soap/envelope/");
		//Namespace ns1 = Namespace
			//	.getNamespace("http://fawna.treelogic.com/ConectorWS/services/SN031003?wsdl");
		Namespace ns1 = Namespace
			.getNamespace(Constantes.getSN_NAMESPACE() + Constantes.SN_SET_BILLING_AGREEMENT + "?wsdl");
		
		Element body = root.getChild("Body", soap);
		Element servicioNegocioResponse = body.getChild(
				"ServicioNegocioResponse", ns1);
		Element outEl = (Element) servicioNegocioResponse.getChildren()
				.get(0);
		
		if (outEl != null) {
			//LOGGER.info("Pasamos el out\n");
			Element contexto = outEl.getChild("contexto", ns1);
			if (contexto != null) {
				List<Element> camposContexto = contexto.getChildren();

				for (int i = 0; i < camposContexto.size(); i++) {
					Element campo = camposContexto.get(i);
					String nombreCampo = campo.getAttributeValue("nombre");
					if (nombreCampo.compareToIgnoreCase("FIONEG003010") == 0) {
						resultado = campo.getText();
					}
				}

			} else {
				// Ha habido un error
			}
		}
		// FIN Parseo de la respuesta
		
		return resultado;

	}
	
	public UsuarioST soapCallAltaUsuarioAio(String nombre, String apellidos,
			String email, String password, String numUsuarios, String idUnidadTiempo, String idResolution, 
			String highAvailability, String datasource, String strIp) throws SOAPException, ServiceException, JDOMException, IOException {

		String soapRequest = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN032001?wsdl'>"
				+ "<soapenv:Header/>"
				+ "<soapenv:Body>"
				+ "<sn0:ServicioNegocio>" + "<sn0:datosEntrada compania='?'>";
		if (nombre != null && !"".equals(nombre)) {
			// "<!--Optional:-->"
			soapRequest += "<sn0:SN032001C001 tipo='CHAR' pos='0'>" + nombre
					+ "</sn0:SN032001C001>";
		}
		if (apellidos != null && !"".equals(apellidos)) {
			// "<!--Optional:-->"
			soapRequest += "<sn0:SN032001C002 tipo='CHAR' pos='1'>" + apellidos
					+ "</sn0:SN032001C002>";
		}
			soapRequest += "<sn0:SN032001C003 tipo='CHAR' pos='2'>" + email
					+ "</sn0:SN032001C003>"
					+ "<sn0:SN032001C004 tipo='CHAR' pos='3'>" + password
					+ "</sn0:SN032001C004>"				
					+ "<sn0:SN032001C005 tipo='CHAR' pos='4'>" + numUsuarios + "</sn0:SN032001C005>"
					+ "<sn0:SN032001C006 tipo='CHAR' pos='5'>" + idUnidadTiempo + "</sn0:SN032001C006>"
					+ "<sn0:SN032001C007 tipo='CHAR' pos='6'>" + idResolution + "</sn0:SN032001C007>"
					+ "<sn0:SN032001C008 tipo='CHAR' pos='7'>" + highAvailability + "</sn0:SN032001C008>"
					+ "<sn0:SN032001C009 tipo='CHAR' pos='8'>" + datasource + "</sn0:SN032001C009>"
				+ "</sn0:datosEntrada>"
			+ "</sn0:ServicioNegocio>"
		+ "</soapenv:Body>"
	+ "</soapenv:Envelope>";
		
		
		
		String respuesta = makeSoapCall(Constantes.HHTP_PROTOCOL + strIp + Constantes.getCLINEG_HOST()+Constantes.SN_ALTA_USUARIO_AIO,
				soapRequest);
		
		LOGGER.info("Respuesta SOAP:\n"+respuesta);
		
		// INICIO Parseo de la respuesta
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(respuesta));

		Element root = doc.getRootElement();
		Namespace soap = Namespace
				.getNamespace("http://schemas.xmlsoap.org/soap/envelope/");
		//Namespace ns1 = Namespace
			//	.getNamespace("http://fawna.treelogic.com/ConectorWS/services/SN031003?wsdl");
		//Namespace ns1 = Namespace
			//.getNamespace("http://localhost:8080/ConectorWS/services/" + Constantes.SN_ALTA_USUARIO_AIO + "?wsdl");
		
		Namespace ns1 = Namespace
		.getNamespace(Constantes.getSN_NAMESPACE() + Constantes.SN_ALTA_USUARIO_AIO + "?wsdl");
		
		Element body = root.getChild("Body", soap);
		Element servicioNegocioResponse = body.getChild(
				"ServicioNegocioResponse", ns1);
		Element outEl = (Element) servicioNegocioResponse.getChildren()
				.get(0);
		UsuarioST usuario = new UsuarioST();
		if (outEl != null) {
			//LOGGER.info("Pasamos el out\n");
			Element contexto = outEl.getChild("contexto", ns1);
			if (contexto != null) {
				List<Element> camposContexto = contexto.getChildren();

				for (int i = 0; i < camposContexto.size(); i++) {
					Element campo = camposContexto.get(i);
					String nombreCampo = campo.getAttributeValue("nombre");
					if (nombreCampo.compareToIgnoreCase("FIONEG001010") == 0) {
						usuario.setUsuarioId(Integer.parseInt(campo.getText()));
					}else if (nombreCampo.compareToIgnoreCase("FIONEG001040") == 0) {
						usuario.setEmail(campo.getText());
					}else if (nombreCampo.compareToIgnoreCase("FIONEG001050") == 0) {
						usuario.setPassword(campo.getText());
					}
				}

			} else {
				// Ha habido un error
			}
		}
		// FIN Parseo de la respuesta
		
		return usuario;

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
			LOGGER.error("ERROR trying to get an answer from request");
			e.printStackTrace();
		}

		return respuesta;

	}
	
	public UsuarioST soapCallCancelarUsuarioST(	String email) throws SOAPException, ServiceException, JDOMException, IOException {

		String soapRequest = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://localhost:8080/ConectorWS/services/SN031007?wsdl'>"
				+ "<soapenv:Header/>"
				+ "<soapenv:Body>"
				+ "<sn0:ServicioNegocio>" + "<sn0:datosEntrada compania='?'>";
		if (email != null && !"".equals(email)) {
			// "<!--Optional:-->"
			soapRequest += "<sn0:SN031007C001 tipo='CHAR' pos='0'>" + email
					+ "</sn0:SN031007C001>";
		}		
		soapRequest += "</sn0:datosEntrada>" + "</sn0:ServicioNegocio>"
				+ "</soapenv:Body>" + "</soapenv:Envelope>";
		
		String respuesta = makeSoapCall(Constantes.getPRINEG_HOST()+Constantes.SN_CANCELAR_USUARIO_ST,
				soapRequest);
		
		LOGGER.info("Respuesta SOAP:\n"+respuesta);
		
		// INICIO Parseo de la respuesta
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(respuesta));

		Element root = doc.getRootElement();
		Namespace soap = Namespace
				.getNamespace("http://schemas.xmlsoap.org/soap/envelope/");
		//Namespace ns1 = Namespace
			//	.getNamespace("http://fawna.treelogic.com/ConectorWS/services/SN031003?wsdl");
		Namespace ns1 = Namespace
			.getNamespace(Constantes.getSN_NAMESPACE() + Constantes.SN_CANCELAR_USUARIO_ST + "?wsdl");
		
		Element body = root.getChild("Body", soap);
		Element servicioNegocioResponse = body.getChild(
				"ServicioNegocioResponse", ns1);
		Element outEl = (Element) servicioNegocioResponse.getChildren()
				.get(0);
		UsuarioST usuario = new UsuarioST();
		if (outEl != null) {
			//LOGGER.info("Pasamos el out\n");
			Element contexto = outEl.getChild("contexto", ns1);
			if (contexto != null) {
				List<Element> camposContexto = contexto.getChildren();

				for (int i = 0; i < camposContexto.size(); i++) {
					Element campo = camposContexto.get(i);
					String nombreCampo = campo.getAttributeValue("nombre");
					if (nombreCampo.compareToIgnoreCase("FIONEG001010") == 0) {
						usuario.setUsuarioId(Integer.parseInt(campo.getText()));
					}else if (nombreCampo.compareToIgnoreCase("FIONEG001040") == 0) {
						usuario.setEmail(campo.getText());
					}else if (nombreCampo.compareToIgnoreCase("FIONEG001050") == 0) {
						usuario.setPassword(campo.getText());
					}
				}

			} else {
				// Ha habido un error
				usuario = null;
			}
		}
		// FIN Parseo de la respuesta
		
		return usuario;

	}

}
