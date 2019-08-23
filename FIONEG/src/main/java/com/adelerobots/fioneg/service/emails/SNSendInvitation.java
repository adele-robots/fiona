package com.adelerobots.fioneg.service.emails;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.adelerobots.fioneg.entity.UserStatusEnum;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.adelerobots.fioneg.util.TemplateUtils;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;

/**
 * Servicio de Negocio fachada para enviar invitaciones
 * a unirse a FIONA
 * 
 * @author adele
 * 
 */
public class SNSendInvitation extends ServicioNegocio {
	
	private static final int CTE_POSICION_NOMBRE = 0;
	private static final int CTE_POSICION_APELLIDO = 1;	
	private static final int CTE_POSICION_EMAILINVITADO = 2;
	private static final int CTE_POSICION_USUARIO_ID = 3;
	private static final int CTE_POSICION_CUERPO_EMAIL = 4;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper.getLog(SNSendInvitation.class);
	
	
	public SNSendInvitation(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029021: Enviar una invitación para unirse a FIONA.");
		Date datInicio = new Date();
		
		String nombreInvitado = datosEntrada.getString(CTE_POSICION_NOMBRE);
		String apellidoInvitado = datosEntrada.getString(CTE_POSICION_APELLIDO);
		String emailInvitado = datosEntrada.getString(CTE_POSICION_EMAILINVITADO);
		String usuarioId = datosEntrada.getString(CTE_POSICION_USUARIO_ID);
		String cuerpoEmail = datosEntrada.getString(CTE_POSICION_CUERPO_EMAIL);
		
		UsuariosManager usuariosManager = ManagerFactory.getInstance().getUsuariosManager();
		UsuarioC usuario = null;
		try {
			usuario = usuariosManager.getUsuario(Integer.parseInt(usuarioId));
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		// Enviar correo al destinatario de la invitacion
		try {
			String to = Constantes.getMAIL_NOTIFICATION_ADDR();
			String subject = "[FIONA] I would like to invite you...";
			String body = composeEmail(contexto, usuario, nombreInvitado, apellidoInvitado, emailInvitado, cuerpoEmail);
			invokeSendMailService(contexto, to, subject, body);
		} catch (Exception e) {
			logger.warn("[SN029021]Fallo en el envio de la invitación", e);
		}
		
		
		IContexto[] salida = null;
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029021: Enviar una invitación para unirse a FIONA. Tiempo total = "
						+ tiempoTotal + "ms");
		
		
		return salida;
	}
	
	
	protected String composeEmail(
			final IContextoEjecucion contexto, 
			final UsuarioC user, 
			final String guestName, final String guestSurname, 
			final String guestEmail, 
			final String message) 
		throws IOException, TemplateException 
	{
		final BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		final TemplateHashModel enumModels = wrapper.getEnumModels();
		//final TemplateHashModel staticModels = wrapper.getStaticModels();
		
		final Map<String, Object> model = new HashMap<String, Object>();
		model.put("UserStatusEnum", enumModels.get(UserStatusEnum.class.getName()));
		model.put("user", user);
		model.put("guestName", guestName);
		model.put("guestSurname", guestSurname);
		model.put("guestEmail", guestEmail);
		model.put("message", message);
		
		final String body = TemplateUtils.processTemplateIntoString(contexto, "mail_invitation-html", model);
		
		return body;
	}



	/**
	 * Invoca el servicio de negocio encargado de mandar un correo 
	 * desde el email de la plataforma
	 * 
	 * @param contexto contexto en ejecucion
	 * @param mailTo direccion de correo de a quien va dirigido
	 * @param subject subject del correo
	 * @param body cuerpo del correo
	 * @return
	 * @throws NumberFormatException
	 * @throws IndexOutOfBoundsException
	 */
	protected IContexto[] invokeSendMailService(
			final IContextoEjecucion contexto, 
			String mailTo, String subject, String body) 
	{
		IContexto[] datosSalida;
		IDatosEntradaTx datosEntrada = ServicioNegocio.getPrograma(
				contexto, new Integer("029"), new Integer("004"));
		datosEntrada.addCampo(null, mailTo, 0);
		datosEntrada.addCampo(null, subject, 1);
		datosEntrada.addCampo(null, body, 2);

		// se invoca el SN
		datosSalida = ServicioNegocio.invocarServicio(contexto, datosEntrada);
		return datosSalida;
	}

}
