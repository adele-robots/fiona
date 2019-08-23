package com.adelerobots.web.fiopre.listeners;

import java.math.BigDecimal;

import com.adelerobots.fioneg.util.PaypalUtilities;
import com.adelerobots.web.fiopre.utilidades.ConfigUtils;
import com.paypal.sdk.core.nvp.NVPDecoder;
import com.paypal.sdk.exceptions.PayPalException;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.SNInvoker;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.persistencia.DatoFawnaFactory;
import com.treelogic.fawna.presentacion.core.persistencia.IDatosFawna;
import com.treelogic.fawna.presentacion.core.utilidades.HelperContext;
import com.treelogic.fawna.presentacion.core.utilidades.IHelperCustom;

public class SetExpressCheckoutListener extends AbstractUserProcessListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7597088990631103048L;

	public void procesarAjaxChangeListener(
			GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		String userID = HelperContext.getInstance().getValueContext("SECURE_USERID");
		if(userID == null || "".equals(userID))
			userID = HelperContext.getInstance().getValueContext("USER_ID");
		//String amtIdPeriod = HelperContext.getInstance().getValueContext("ACCOUNT_PERIOD");
		String amtIdPeriod = HelperContext.getInstance().getValueContext("USER_ACCOUNTTYPE_METHOD");
		String strCodCuenta = HelperContext.getInstance().getValueContext("USER_ACCOUNTTYPE_ID");
		Integer intCodCuenta = Integer.parseInt(strCodCuenta);
		// Invocamos al servicio que nos devuelve info de un tipo de cuenta
		BigDecimal bidAmtM= null, bidAmtY = null; 
		try {
			IContexto[] salida = invokeSNGetAccountTypeData(intCodCuenta);
			bidAmtM = salida[0].getBigDecimal("FIONEG024030");
			bidAmtY = salida[0].getBigDecimal("FIONEG024040");
			
		} catch (FactoriaDatosException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (PersistenciaException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FawnaInvokerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//String amtM = HelperContext.getInstance().getValueContext("ACCOUNT_AMT_MONTHLY");
		//String amtY = HelperContext.getInstance().getValueContext("ACCOUNT_AMT_YEARLY");
		String amtM = (bidAmtM.setScale(2,BigDecimal.ROUND_UP)).toString();
		String amtY = (bidAmtY.setScale(2,BigDecimal.ROUND_UP)).toString();
		
		String amt = amtY;
		if("1".equals(amtIdPeriod))
			amt = amtM;
		
		
		// Establecemos que cuando el usuario cancele vuelva al inicio de este flujo		
		String returnURL = getRequestURL().toString() + "?u=" + userID+"&amt=" + amt;
		returnURL = returnURL.replaceFirst("login-flow", "signup-flow");
		String cancelURL = getRequestURL().toString() + "?u=" + userID + "&cancelled=true";
		cancelURL = cancelURL.replaceFirst("login-flow", "signup-flow");

		// StringBuffer sbErrorMessages = new StringBuffer("");		

		try {			
			
			String environment = ConfigUtils.getEnvironment();
			// Pasos para activar Reference Transactions para este cliente
			// y obtener el TRANSACTION ID que se usará para cobrarle 
			// en futuras transacciones
			// 1. Set Up the Payment Authorization calling setExpressCheckout			
			// call method will send the request to the server and return the
			// response NVPString
			String ppresponse = PaypalUtilities.getInstance().setExpressCheckoutWithReferenceTransactions(returnURL, cancelURL);

			// NVPDecoder object is created
			NVPDecoder resultValues = new NVPDecoder();

			// decode method of NVPDecoder will parse the request and decode the
			// name and value pair
			resultValues.decode(ppresponse);
			
			//checks for Acknowledgement and redirects accordingly to display error messages		
			String strAck = resultValues.get("ACK"); 
			if(strAck !=null && !(strAck.equals("Success") || strAck.equals("SuccessWithWarning")))
			{				
				PaypalUtilities.getInstance().gestionErrores(Integer.parseInt(userID), "SetExpressCheckout", ppresponse);
				logger.error("Se ha producido un error al intentar completar un 'setExpressCheckoutWithReferenceTransactions' - SetExpressCheckoutListener:\n" + ppresponse);
				// Parar el proceso y mostrar al usuario un error en la página en la que se encuentra (jsp acuerdo paypal).
				HelperContext.getInstance().setValueContext("errorPaypalSEC", "true", IHelperCustom.FLUJO_CTX_TYPE);
				invokeSNUpdateAccountType(Integer.parseInt(userID),new Integer(1));
			}else {
				// 2. Redirect the Customer to PayPal for Authorization				
				String urlPaypal = "https://www." + environment + ".paypal.com/cgi-bin/webscr?cmd=_express-checkout&token=" + resultValues.get("TOKEN");
				redirect(urlPaypal);						
			}		
			
			// 3. Create the Billing Agreement
			// 4. Capture Future Payments (do reference transactions en los batch)
			
			// Almacenar en sesión los valores q más tarde tendremos que guardar
			/*
		HelperContext.getInstance().setValueContext("USER_FISTNAME",HelperContext.getInstance().getValueContext("USER_FISTNAME"),IHelperCustom.FLUJO_CTX_TYPE);		
		HelperContext.getInstance().setValueContext("USER_SURNAME",HelperContext.getInstance().getValueContext("USER_SURNAME"),IHelperCustom.FLUJO_CTX_TYPE);		
		HelperContext.getInstance().setValueContext("USER_NEWPASSWORD1",HelperContext.getInstance().getValueContext("USER_NEWPASSWORD1"),IHelperCustom.FLUJO_CTX_TYPE);		
		HelperContext.getInstance().setValueContext("USER_ACCOUNTTYPE_ID",HelperContext.getInstance().getValueContext("USER_ACCOUNTTYPE_ID"),IHelperCustom.FLUJO_CTX_TYPE);
		HelperContext.getInstance().setValueContext("USER_NICKNAME",HelperContext.getInstance().getValueContext("USER_NICKNAME"),IHelperCustom.FLUJO_CTX_TYPE);
		HelperContext.getInstance().setValueContext("USER_TITLE",HelperContext.getInstance().getValueContext("USER_TITLE"),IHelperCustom.FLUJO_CTX_TYPE);		
		HelperContext.getInstance().setValueContext("USER_FLAGENTITY",HelperContext.getInstance().getValueContext("USER_FLAGENTITY"),IHelperCustom.FLUJO_CTX_TYPE);		
		HelperContext.getInstance().setValueContext("USER_ORGANIZATIONTYPE_ID",HelperContext.getInstance().getValueContext("USER_ORGANIZATIONTYPE_ID"),IHelperCustom.FLUJO_CTX_TYPE);		
		HelperContext.getInstance().setValueContext("USER_LEGALENTITYNAME",HelperContext.getInstance().getValueContext("USER_LEGALENTITYNAME"),IHelperCustom.FLUJO_CTX_TYPE);		
		HelperContext.getInstance().setValueContext("USER_WEBSITE",HelperContext.getInstance().getValueContext("USER_WEBSITE"),IHelperCustom.FLUJO_CTX_TYPE);		
		HelperContext.getInstance().setValueContext("USER_WORKEMAIL",HelperContext.getInstance().getValueContext("USER_WORKEMAIL"),IHelperCustom.FLUJO_CTX_TYPE);		
		HelperContext.getInstance().setValueContext("USER_COUNTRYCODE",HelperContext.getInstance().getValueContext("USER_COUNTRYCODE"),IHelperCustom.FLUJO_CTX_TYPE);		
		HelperContext.getInstance().setValueContext("USER_PHONENUMBER",HelperContext.getInstance().getValueContext("USER_PHONENUMBER"),IHelperCustom.FLUJO_CTX_TYPE);		
		HelperContext.getInstance().setValueContext("USER_EXT",HelperContext.getInstance().getValueContext("USER_EXT"),IHelperCustom.FLUJO_CTX_TYPE);		
		HelperContext.getInstance().setValueContext("USER_ACCOUNTTYPE_METHOD",HelperContext.getInstance().getValueContext("USER_ACCOUNTTYPE_METHOD"),IHelperCustom.FLUJO_CTX_TYPE);		
		HelperContext.getInstance().setValueContext("account_credit",HelperContext.getInstance().getValueContext("account_credit"),IHelperCustom.FLUJO_CTX_TYPE);
			*/
		} catch (PayPalException ppEx) {
			// TODO Manejar la excepción más correctamente
			ppEx.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoriaDatosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenciaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FawnaInvokerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
	
	/**
	 * Método que permite la invocación del servicio que devuelve información de
	 * de cuenta de un usuario
	 * 	
	 * @param intCodTipoCuenta Nuevo tipo de cuenta
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	private IContexto[] invokeSNGetAccountTypeData(Integer intCodTipoCuenta) throws FactoriaDatosException,
			PersistenciaException, FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", intCodTipoCuenta, "BigDecimal");		
		IContexto [] salida = invoker.invokeSNParaContextos("029", "081",
				datosEntrada, false);
		
		return salida;
	}
	
	

}
