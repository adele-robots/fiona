package com.adelerobots.web.fiopre.action;

import org.apache.log4j.Logger;

import com.adelerobots.fioneg.util.PaypalUtilities;
import com.paypal.sdk.core.nvp.NVPDecoder;
import com.paypal.sdk.exceptions.PayPalException;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.presentacion.componentes.event.api.SNInvoker;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.exception.PresentacionException;
import com.treelogic.fawna.presentacion.core.persistencia.DatoFawnaFactory;
import com.treelogic.fawna.presentacion.core.persistencia.IDatosFawna;
import com.treelogic.fawna.presentacion.core.utilidades.HelperContext;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperCustom;
import com.treelogic.fawna.presentacion.core.webflow.action.ICustomAction;

public class CreateBillingAgreementStoreCustomAction implements ICustomAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3822125130222662393L;
	
	private Logger logger = Logger.getLogger(getClass());

	public void ejecutar(IHelperCustom helper) throws PresentacionException {
		String userId = helper.getValueContext("USER_ID");
		Integer intUserId = Integer.valueOf(userId);		
		String token = helper.getValueContext("TOKEN");
		
		// Identificador del spark a comprar
		String sparkID = helper.getValueContext("spark_id");
		// Identificador del precio seleccionado por el usuario
		String priceID = helper.getValueContext("selected_price");
		

		// 3. Create the Billing Agreement
		try {

			String ppresponse = PaypalUtilities.getInstance().createBillingAgreementForReferenceTransactions(
					token);

			// NVPDecoder object is created
			NVPDecoder resultValues = new NVPDecoder();

			// decode method of NVPDecoder will parse the request and decode the
			// name and value pair
			resultValues.decode(ppresponse);

			// checks for Acknowledgement and redirects accordingly to display
			// error messages
			String strAck = resultValues.get("ACK");
			if (strAck != null
					&& !(strAck.equals("Success") || strAck
							.equals("SuccessWithWarning"))) {				
				PaypalUtilities.getInstance().gestionErrores(intUserId, "createBillingAgreementForReferenceTransactions", ppresponse);
				logger.error("Se ha producido un error al intentar completar un 'createBillingAgreementForReferenceTransactions' - CreateBillingAgreementCustomAction:\n" + ppresponse);
				// Parar el proceso y mostrar al usuario un error en la página en la que se encuentra (jsp de registro con éxito).
				HelperContext.getInstance().setValueContext("errorPaypalCBA", "true", IHelperCustom.FLUJO_CTX_TYPE);
				// Lanzamos error para evitar que se ejecute el SN de compra
				throw new PresentacionException("Se ha producido un error al intentar completar un 'createBillingAgreementForReferenceTransactions' - CreateBillingAgreementCustomAction:\n" + ppresponse);
				
			} else {
				// En este punto todo ha ido bien así que llamamos al SN que
				// cambie el identificador del acuerdo con el usuario				
				String billingAgreementId = resultValues.get("BILLINGAGREEMENTID");
				
				IContexto[] datos=invokeSNSetbillingAgreement(intUserId, billingAgreementId);
				
				// Establecer el nuevo BILLINGID
				//HelperContext.getInstance().setValueContext("BILLING_AGREEMENT_ID", billingAgreementId, IHelperCustom.FLUJO_CTX_TYPE);
				HelperContext.getInstance().setValueContext("SECURE_USER_BILLING_AGREEMENT_ID", billingAgreementId, IHelperCustom.SESION_CTX_TYPE);
				
			}

		} catch (PayPalException ppEx) {
			// TODO: Manejar la excepción correctamente
			ppEx.printStackTrace();
		}

	}
	

	/**
	 * Método que permite la invocación del servicio que guarda el identificador del acuerdo con
	 * PayPal
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @param strSignupCode Cadena que representa el código de registro
	 * @param strBillingID Identificador del acuerdo
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	private IContexto[] invokeSNSetbillingAgreement(Integer intCodUsuario,
			String strBillingID) throws FactoriaDatosException,
			PersistenciaException, FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", intCodUsuario, "BigDecimal");		
		//Md5PasswordEncoder md5PassEncoder = new Md5PasswordEncoder();
		//String encodeToken = md5PassEncoder.encodePassword(strBillingID, null);
		datosEntrada.putDato("1", strBillingID, "String");
		IContexto [] salida = invoker.invokeSNParaContextos("029", "064",
				datosEntrada, false);
		
		return salida;
	}
	

}
