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

import com.adelerobots.serneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SparkManager {

	private String conexion;

	private static FawnaLogHelper LOGGER = FawnaLogHelper
			.getLog(SparkManager.class);

	public SparkManager(String conexion) {
		super();
		this.conexion = conexion;
	}

	public boolean soapCallCreateAvatarST(String email, String numUsuarios, String idUnidadTiempo, String idResolucion, String highAvailability) throws SOAPException, ServiceException, JDOMException, IOException {
		boolean error = false;
		String soapRequest = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://fawna.treelogic.com/ConectorWS/services/SN031004?wsdl'>"
								+ "<soapenv:Header/>"
									+ "<soapenv:Body>"
										+ "<sn0:ServicioNegocio>"
											+ "<sn0:datosEntrada compania='?'>"
													+ "<sn0:SN031004C001 tipo='CHAR' pos='0'>" + email + "</sn0:SN031004C001>"
													+ "<sn0:SN031004C002 tipo='CHAR' pos='1'>" + numUsuarios + "</sn0:SN031004C002>"
													+ "<sn0:SN031004C003 tipo='CHAR' pos='2'>" + idUnidadTiempo + "</sn0:SN031004C003>"
													+ "<sn0:SN031004C004 tipo='CHAR' pos='3'>" + idResolucion + "</sn0:SN031004C004>"
													+ "<sn0:SN031004C005 tipo='CHAR' pos='4'>" + highAvailability + "</sn0:SN031004C005>"
											+ "</sn0:datosEntrada>"
										+ "</sn0:ServicioNegocio>"
									+ "</soapenv:Body>"
							+ "</soapenv:Envelope>";
		
		String respuesta = makeSoapCall(Constantes.getPRINEG_HOST()+Constantes.SN_CREATE_AVATAR_ST,
				soapRequest);
		
		LOGGER.info("Respuesta SOAP:\n"+respuesta);
		
		// INICIO Parseo de la respuesta
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(respuesta));

		Element root = doc.getRootElement();
		Namespace soap = Namespace
				.getNamespace("http://schemas.xmlsoap.org/soap/envelope/");
		
		Namespace ns1 = Namespace
			.getNamespace(Constantes.getSN_NAMESPACE() + Constantes.SN_CREATE_AVATAR_ST + "?wsdl");
		
		Element body = root.getChild("Body", soap);
		Element servicioNegocioResponse = body.getChild(
				"ServicioNegocioResponse", ns1);
		Element outEl = (Element) servicioNegocioResponse.getChildren()
				.get(0);
		
		if (outEl != null) {
			// Todo ha ido bien
			Element contexto = outEl.getChild("contexto", ns1);
			if (contexto != null) {
				
			}else{
				// Error
				error = true;
			}
		}else{
			// Error
			error = true;
		}
		// FIN Parseo de la respuesta
		return error;		
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
	
	public boolean soapCallUpdateAvatarHostingST(String email, String numUsuarios, String idUnidadTiempo, String idResolucion, String highAvailability, String codigoAvatar) throws SOAPException, ServiceException, JDOMException, IOException {
		boolean error = false;
		String soapRequest = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://fawna.treelogic.com/ConectorWS/services/SN031005?wsdl'>"
								+ "<soapenv:Header/>"
									+ "<soapenv:Body>"
										+ "<sn0:ServicioNegocio>"
											+ "<sn0:datosEntrada compania='?'>"
													+ "<sn0:SN031005C001 tipo='CHAR' pos='0'>" + email + "</sn0:SN031005C001>"
													+ "<sn0:SN031005C002 tipo='CHAR' pos='1'>" + numUsuarios + "</sn0:SN031005C002>"
													+ "<sn0:SN031005C003 tipo='CHAR' pos='2'>" + idUnidadTiempo + "</sn0:SN031005C003>"
													+ "<sn0:SN031005C004 tipo='CHAR' pos='3'>" + idResolucion + "</sn0:SN031005C004>"
													+ "<sn0:SN031005C005 tipo='CHAR' pos='4'>" + highAvailability + "</sn0:SN031005C005>";
		if(codigoAvatar != null && !"".equals(codigoAvatar)){
			soapRequest += "<sn0:SN031008C006 tipo='CHAR' pos='5'>" + codigoAvatar + "</sn0:SN031008C006>";
		}
											soapRequest += "</sn0:datosEntrada>"
										+ "</sn0:ServicioNegocio>"
									+ "</soapenv:Body>"
							+ "</soapenv:Envelope>";
		
		String respuesta = makeSoapCall(Constantes.getPRINEG_HOST()+Constantes.SN_UPDATE_AVATAR_HOSTING_ST,
				soapRequest);
		
		LOGGER.info("Respuesta SOAP:\n"+respuesta);
		
		// INICIO Parseo de la respuesta
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(respuesta));

		Element root = doc.getRootElement();
		Namespace soap = Namespace
				.getNamespace("http://schemas.xmlsoap.org/soap/envelope/");
		
		Namespace ns1 = Namespace
			.getNamespace(Constantes.getSN_NAMESPACE() + Constantes.SN_UPDATE_AVATAR_HOSTING_ST + "?wsdl");
		
		Element body = root.getChild("Body", soap);
		Element servicioNegocioResponse = body.getChild(
				"ServicioNegocioResponse", ns1);
		Element outEl = (Element) servicioNegocioResponse.getChildren()
				.get(0);
		
		if (outEl != null) {
			// Todo ha ido bien
			Element contexto = outEl.getChild("contexto", ns1);
			if (contexto != null) {
				
			}else{
				// Error
				error = true;
			}
		}else{
			// Error
			error = true;
		}
		// FIN Parseo de la respuesta
		return error;		
	}
	
	public String soapCallGetHostingPrize(String numUsuarios, String idUnidadTiempo, String idResolucion, String highAvailability) throws SOAPException, ServiceException, JDOMException, IOException {
		String precio = null;
		String soapRequest = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:sn0='http://fawna.treelogic.com/ConectorWS/services/SN031006?wsdl'>"
								+ "<soapenv:Header/>"
									+ "<soapenv:Body>"
										+ "<sn0:ServicioNegocio>"
											+ "<sn0:datosEntrada compania='?'>"													
													+ "<sn0:SN031006C001 tipo='CHAR' pos='0'>" + numUsuarios + "</sn0:SN031006C001>"
													+ "<sn0:SN031006C002 tipo='CHAR' pos='1'>" + idUnidadTiempo + "</sn0:SN031006C002>"
													+ "<sn0:SN031006C003 tipo='CHAR' pos='2'>" + idResolucion + "</sn0:SN031006C003>"
													+ "<sn0:SN031006C004 tipo='CHAR' pos='3'>" + highAvailability + "</sn0:SN031006C004>"
											+ "</sn0:datosEntrada>"
										+ "</sn0:ServicioNegocio>"
									+ "</soapenv:Body>"
							+ "</soapenv:Envelope>";
		
		String respuesta = makeSoapCall(Constantes.getPRINEG_HOST()+Constantes.SN_GET_HOSTING_PRIZE,
				soapRequest);
		
		LOGGER.info("Respuesta SOAP:\n"+respuesta);
		
		// INICIO Parseo de la respuesta
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new StringReader(respuesta));

		Element root = doc.getRootElement();
		Namespace soap = Namespace
				.getNamespace("http://schemas.xmlsoap.org/soap/envelope/");
		
		Namespace ns1 = Namespace
			.getNamespace(Constantes.getSN_NAMESPACE() + Constantes.SN_GET_HOSTING_PRIZE + "?wsdl");
		
		Element body = root.getChild("Body", soap);
		Element servicioNegocioResponse = body.getChild(
				"ServicioNegocioResponse", ns1);
		Element outEl = (Element) servicioNegocioResponse.getChildren()
				.get(0);
		
		if (outEl != null) {
			// Todo ha ido bien
			Element contexto = outEl.getChild("contexto", ns1);
			if (contexto != null) {
				List<Element> camposContexto = contexto.getChildren();

				for (int i = 0; i < camposContexto.size(); i++) {
					Element campo = camposContexto.get(i);
					String nombreCampo = campo.getAttributeValue("nombre");
					if (nombreCampo.compareToIgnoreCase("FIONEG003010") == 0) {
						precio = campo.getText();
					}
				}
			}else{
				// Error
				precio = "-1";
			}
		}else{
			// Error
			precio = "-1";
		}
		// FIN Parseo de la respuesta
		return precio;		
	}

}
