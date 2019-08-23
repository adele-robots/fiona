package com.adelerobots.web.fiopre.action;

import java.math.BigDecimal;

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

public class DoReferenceTransactionCustomAction implements ICustomAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9152986210261991545L;
	
	private Logger logger = Logger.getLogger(getClass());
	
	

	public void ejecutar(IHelperCustom helper) throws PresentacionException {
		
		String billingAgreementId = helper.getValueContext("BILLING_AGREEMENT_ID");
		String amtM = HelperContext.getInstance().getValueContext("ACCOUNT_AMT_MONTHLY");
		String amtY = HelperContext.getInstance().getValueContext("ACCOUNT_AMT_YEARLY");
		String amtIdPeriod = HelperContext.getInstance().getValueContext("ACCOUNT_PERIOD");
		String accountCredit = HelperContext.getInstance().getValueContext("account_credit");	
		String userId = helper.getValueContext("SECURE_USERID");
		String amt = amtY;
		if("1".equals(amtIdPeriod))
			amt = amtM;
		

		// 3. Create the Billing Agreement
		try {
			Float floAmount = Float.parseFloat(amt);
			Float floAccountCredit = Float.parseFloat(accountCredit);
			Float credit = new Float(0);
			//BigDecimal bd = null;
			BigDecimal bd = new BigDecimal(0.0).setScale(2,BigDecimal.ROUND_UP);
			if(floAccountCredit > 0){
				Float floTotal = floAmount - floAccountCredit;
				IContexto[] salida = null;
				
				if(floTotal > 0){
					bd = new BigDecimal(floTotal).setScale(2,BigDecimal.ROUND_UP);
					amt = bd.toString();
					// Resetear el crédito	
					// Llamar al SN que actualiza el crédito del usuario (que pasará a ser cero)
					salida = invokeUpdateAccountCredit(Integer.parseInt(userId),new Float(0));
				}else{
					// El crédito es suficiente para varios pagos
					credit = floAccountCredit - floAmount;
					// Resetear el crédito	
					// Llamar al SN que actualiza el crédito del usuario
					salida = invokeUpdateAccountCredit(Integer.parseInt(userId),credit);
					amt = "0";
				}
				bd = salida[0].getBigDecimal("FIONEG001210").setScale(2,BigDecimal.ROUND_UP);
			}
			
			// Resetear el crédito				
			//BigDecimal bd = new BigDecimal(credit).setScale(2,BigDecimal.ROUND_UP);
			HelperContext.getInstance().setValueContext("account_credit",bd.toString(),IHelperCustom.FLUJO_CTX_TYPE);
			HelperContext.getInstance().setValueContext("SECURE_USER_ACCOUNT_CREDIT",bd.toString(),IHelperCustom.SESION_CTX_TYPE);
			if(Float.parseFloat(amt)>0){				
				// Hacemos el cobro a cuenta	
				String ppresponse = PaypalUtilities.getInstance().doReferenceTransaction(billingAgreementId, amt, ConfigUtils.getACCOUNT_PAYMENT_ANNOTATION());
	
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
					PaypalUtilities.getInstance().gestionErrores(Integer.parseInt(userId), "doReferenceTransaction", ppresponse);
					logger.error("Se ha producido un error al intentar completar un 'doReferenceTransaction' - DoReferenceTransactionCustomAction:\n" + ppresponse);
					// Parar el proceso y mostrar al usuario un error en la página en la que se encuentra (jsp de registro con éxito).
					HelperContext.getInstance().setValueContext("errorPaypalDRT", "true", IHelperCustom.FLUJO_CTX_TYPE);
				} else {
					// En este punto todo ha ido bien
				}
			}

		} catch (PayPalException ppEx) {
			// TODO: Manejar la excepción correctamente
			ppEx.printStackTrace();
		}
		

	}
	
	/**
	 * Método para la invocación del servicio que actualiza el crédito del usuario
	 * 
	 * @param intCodUsuario
	 *            Identificador único del usuario
	 * @param floAccountCredit
	 *           Nuevo crédito del usuario
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected IContexto[] invokeUpdateAccountCredit(final Integer intCodUsuario,
			final Float floAccountCredit) throws FactoriaDatosException,
			PersistenciaException, FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", intCodUsuario, "BigDecimal");
		if(floAccountCredit != null)
			datosEntrada.putDato("1", floAccountCredit, "BigDecimal");		

		IContexto[] salida = invoker.invokeSNParaContextos("029", "071",
				datosEntrada, false);

		return salida;

	}
	

}
