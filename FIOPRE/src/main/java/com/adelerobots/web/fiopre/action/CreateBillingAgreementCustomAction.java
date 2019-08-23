package com.adelerobots.web.fiopre.action;

import org.apache.log4j.Logger;

import com.adelerobots.fioneg.util.PaypalUtilities;
import com.adelerobots.web.fiopre.utilidades.ConfigUtils;
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

public class CreateBillingAgreementCustomAction implements ICustomAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3822125130222662393L;
	
	private Logger logger = Logger.getLogger(getClass());

	public void ejecutar(IHelperCustom helper) throws PresentacionException {
		String userId = helper.getValueContext("SECURE_USERID");
		if(userId== null)
			userId = helper.getValueContext("USER_ID");
		Integer intUserId = Integer.valueOf(userId);		
		String token = helper.getValueContext("TOKEN");
		String amt = helper.getValueContext("AMT");
		

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
				// TODO: Indicar al usuario que se ha producido alguna clase de
				// error
				PaypalUtilities.getInstance().gestionErrores(intUserId, "createBillingAgreementForReferenceTransactions", ppresponse);
				logger.error("Se ha producido un error al intentar completar un 'createBillingAgreementForReferenceTransactions' - CreateBillingAgreementCustomAction:\n" + ppresponse);
				// Parar el proceso y mostrar al usuario un error en la página en la que se encuentra (jsp de registro con éxito).
				HelperContext.getInstance().setValueContext("errorPaypalCBA", "true", IHelperCustom.FLUJO_CTX_TYPE);
				// Convertir la cuenta en Free puesto que no se ha podido crear el acuerdo
				invokeSNUpdateAccountType(Integer.parseInt(userId),new Integer(1));
				
			} else {
				// En este punto todo ha ido bien así que llamamos al SN que
				// cambie el estado del
				// registro del usuario				
				String billingAgreementId = resultValues.get("BILLINGAGREEMENTID");
				
				IContexto[] datos=invokeSNSetbillingAgreement(intUserId, billingAgreementId);
				
				// Hacemos el primer cobro a cuenta								
				ppresponse = PaypalUtilities.getInstance().doReferenceTransaction(billingAgreementId, amt, ConfigUtils.getACCOUNT_PAYMENT_ANNOTATION());
				
				// Comprobar los errores
				resultValues.decode(ppresponse);
				strAck = resultValues.get("ACK");
				if (strAck != null
						&& !(strAck.equals("Success") || strAck
								.equals("SuccessWithWarning"))) {
					// TODO: Indicar al usuario que se ha producido alguna clase de
					// error
					PaypalUtilities.getInstance().gestionErrores(intUserId, "doReferenceTransaction", ppresponse);
					logger.error("Se ha producido un error al intentar completar un 'doReferenceTransaction' - CreateBillingAgreementCustomAction:\n" + ppresponse);
					// Parar el proceso y mostrar al usuario un error en la página en la que se encuentra (jsp de registro con éxito).
					HelperContext.getInstance().setValueContext("errorPaypalDRTIni", "true", IHelperCustom.FLUJO_CTX_TYPE);
				}else{
					// Todo ha ido bien y se ha completado la transacción
					
				}
				
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
	
	/**
	 * Método que permite la invocación del servicio que actualiza el tipo
	 * de cuenta de un usuario
	 * 
	 * @param intCodUsuario Identificador único del usuario
	 * @param strSignupCode Cadena que representa el código de registro
	 * @param intCodTipoCuenta Nuevo tipo de cuenta
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	private IContexto[] invokeSNUpdateAccountType(Integer intCodUsuario,
			Integer intCodTipoCuenta) throws FactoriaDatosException,
			PersistenciaException, FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", intCodUsuario, "BigDecimal");		
		datosEntrada.putDato("1", intCodTipoCuenta, "BigDecimal");
		IContexto [] salida = invoker.invokeSNParaContextos("029", "065",
				datosEntrada, false);
		
		return salida;
	}

}
