package com.adelerobots.fioneg.service.usuarios;

import java.math.BigDecimal;
import java.util.Date;

import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.adelerobots.fioneg.util.PaypalUtilities;
import com.adelerobots.fioneg.util.StringEncrypterUtilities;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.paypal.sdk.core.nvp.NVPDecoder;
import com.paypal.sdk.exceptions.PayPalException;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

/**
 * Servicio de negocio que manera el envío de correos electrónicos
 * a los usuarios y al personal de la plataforma cuando se producen
 * errores durante las llamadas de la API de PayPal
 * 
 * @author adele
 *
 */
public class SNManagePaypalFailure extends ServicioNegocio {
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNManagePaypalFailure.class);
	
	
	// Parametros de entrada
	private static final int CTE_POSICION_ID = 0;	
	private static final int CTE_POSICION_NOMBRE_FUNCION = 1;
	private static final int CTE_POSICION_RESPONSE = 2;
	
	
	
	public SNManagePaypalFailure(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029072: Envío de correos durante errores en llamadas Paypal.");
		Date datInicio = new Date();
		
		BigDecimal bidCodUsuario = datosEntrada.getDecimal(CTE_POSICION_ID);
		Integer intCodUsuario = new Integer(bidCodUsuario.intValue());
		// Nombre función
		String strNombreFuncion = datosEntrada.getString(CTE_POSICION_NOMBRE_FUNCION);
		
		String strResponse = datosEntrada.getString(CTE_POSICION_RESPONSE);
		
		UsuariosManager usuariosManager = ManagerFactory.getInstance().getUsuariosManager();
		// Usuario
		UsuarioC usuario = usuariosManager.findById(intCodUsuario);
		// BillingId
		String encodedBillingAgreementId = usuario.getStrBillingId();
		String billingAgreementId = "";
		if(encodedBillingAgreementId != null){
			StringEncrypterUtilities desencrypter = new StringEncrypterUtilities(Constantes.PASS_PHRASE);
			billingAgreementId = desencrypter.decrypt(encodedBillingAgreementId);
		}
		// Respuesta decodificada
		NVPDecoder resultValues = new NVPDecoder();
		
		// decode method of NVPDecoder will parse the request and decode the
		// name and value pair
		try {
			resultValues.decode(strResponse);
		} catch (PayPalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("Se ha producido un error al intentar decodificar la respuesta PayPal - SNManagePaypalFailure: " + e.getMessage());
		}
		
		
		PaypalUtilities.getInstance().gestionErroresDoReferenceTransaction(contexto, resultValues, usuario, billingAgreementId, strNombreFuncion);
		
		IContexto [] salida = null;
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029072: Envío de correos durante errores en llamadas Paypal. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}

}
