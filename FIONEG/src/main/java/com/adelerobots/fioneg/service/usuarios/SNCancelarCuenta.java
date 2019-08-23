package com.adelerobots.fioneg.service.usuarios;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adelerobots.fioneg.entity.AvatarC;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.entity.avatarspark.AvatarSparkC;
import com.adelerobots.fioneg.entity.usuariospark.UsuarioSparkC;
import com.adelerobots.fioneg.manager.AvatarManager;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.SparkManager;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.adelerobots.fioneg.util.TemplateUtils;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.RollbackException;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio.TipoError;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateException;

public class SNCancelarCuenta extends ServicioNegocio {
	
	private static FawnaLogHelper logger = FawnaLogHelper.getLog(SNCancelarCuenta.class);

	// Parametros de entrada
	private static final int CTE_POSICION_ID = 0;
	
	public SNCancelarCuenta(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		if (logger.isInfoEnabled()) {
			logger.info("Inicio Ejecucion del SN029070: Cancelación de cuenta de usuario.");
		}
		final long iniTime = System.currentTimeMillis();
		
		BigDecimal bidCodUsuario = datosEntrada.getDecimal(CTE_POSICION_ID);
		
		Integer intCodUsuario = new Integer(bidCodUsuario.intValue());
		
		UsuariosManager usuariosManager = ManagerFactory.getInstance().getUsuariosManager();
		
		UsuarioC usuario = usuariosManager.findById(intCodUsuario);
		
		// Realizar comprobaciones para saber si podemos cancelar la cuenta
		// 1. No se cancelará la cuenta si el usuario es un sparker con sparks desarrollados
		// y comprados por otros clientes
		// 1.1. Buscar los sparks desarrollados por el usuario y usados en DESARROLLO
		SparkManager sparkManager = ManagerFactory.getInstance().getSparkManager();
		List <UsuarioSparkC> lstUsuarioSpark = sparkManager.getActiveUsedSparksDevelopedByUser(intCodUsuario);
		if(lstUsuarioSpark != null && !lstUsuarioSpark.isEmpty()){			
			ServicioNegocio.rollback(TipoError.FUNCIONAL, new Integer(1400), "You have developed sparks that are being used by others.", "You have developed sparks that are being used by others.", null);
			throw new RollbackException("You have developed sparks that are being used by others.");
		}
		// 1.2. Buscar los sparks desarrollados por el usuario y usados en PRODUCCIÓN
		AvatarManager avatarManager = ManagerFactory.getInstance().getAvatarManager();
		List<AvatarSparkC> lstAvatarSpark = avatarManager.getActiveUsedSparksDevelopedByUser(intCodUsuario);
		if(lstAvatarSpark != null && !lstAvatarSpark.isEmpty()){
			ServicioNegocio.rollback(TipoError.FUNCIONAL, new Integer(1401), "You have developed sparks that are being used by others.", "You have developed sparks that are being used by others.", null);
			throw new RollbackException("You have developed sparks that are being used by others.");
		}
		
		// 2. No se cancelará la cuenta si el usuario tiene avatares ACTIVOS en producción		
		List <AvatarC> lstActiveAvatars = avatarManager.getActiveAvatarsByUser(intCodUsuario);
		if(lstActiveAvatars!= null && !lstActiveAvatars.isEmpty()){
			ServicioNegocio.rollback(TipoError.FUNCIONAL, new Integer(1402), "You have active avatars in production environment.", "You have active avatars in production environment.", null);
			throw new RollbackException("You have active avatars in production environment.");
		}
		
		
		// Cancelamos la cuenta del usuario
		usuariosManager.cancelarCuentaUsuario(usuario);
		// Borrar directorios del usuario
		try {
			Process userdirs = Runtime.getRuntime().exec(new String[]{Constantes.getDELETE_USERDIRS_SCRIPT(), usuario.getAvatarBuilderUmd5()});			
			userdirs.waitFor();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);			
		}
		
		//Correo para la plataforma
		try {
			IContexto[] datosSalida_mailadmin = sendCancellationMailToAdmin(contexto, usuario);
			//Correo para el usuario
			IContexto[] datosSalida_mailuser = sendCancellationMailToUser(contexto, usuario);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		IContexto[] salida = null;
		if (logger.isInfoEnabled()) {
			logger.info("Fin Ejecucion del SN029070: Cancelación de cuenta de usuario con id" + intCodUsuario +". Tiempo total = " + Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		}
		return salida;
	}
	
	protected IContexto[] sendCancellationMailToAdmin(
			final IContextoEjecucion contexto, 
			final UsuarioC user) 
		throws IOException, TemplateException 
	{
		final BeansWrapper wrapper = BeansWrapper.getDefaultInstance();		
		//final TemplateHashModel staticModels = wrapper.getStaticModels();
		
		final Map<String, Object> model = new HashMap<String, Object>();		
		model.put("user", user);		
		
		final String subject = "[FIONA] Account cancellation incoming";
		final String body = TemplateUtils.processTemplateIntoString(contexto, "mail_team_account_cancellation-html", model);
		
		//invocamos el servicio de negocio para enviar mail al admin
		final String mailTo = Constantes.getMAIL_NOTIFICATION_ADDR();
		return invokeSendMailService(contexto, mailTo, subject, body);
	}



	protected IContexto[] sendCancellationMailToUser(
			final IContextoEjecucion contexto, 
			final UsuarioC user) 
		throws IOException, TemplateException 
	{
		final BeansWrapper wrapper = BeansWrapper.getDefaultInstance();		
		//final TemplateHashModel staticModels = wrapper.getStaticModels();
		
		final Map<String, Object> model = new HashMap<String, Object>();		
		model.put("user", user);
				
		final String subject = "[FIONA] Account cancellation confirmation";
		final String body = TemplateUtils.processTemplateIntoString(contexto, "mail_user_account_cancellation-html", model);
		
		//invocamos el servicio de negocio para enviar mail al usuario
		final String mailTo = user.getEmail();
		return invokeSendMailService(contexto, mailTo, subject, body);
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
