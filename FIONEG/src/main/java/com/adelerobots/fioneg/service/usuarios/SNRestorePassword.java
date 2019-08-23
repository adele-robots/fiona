package com.adelerobots.fioneg.service.usuarios;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import com.adelerobots.fioneg.context.ContextoUsuarios;
import com.adelerobots.fioneg.entity.UserStatusEnum;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.adelerobots.fioneg.util.FunctionUtils;
import com.adelerobots.fioneg.util.TemplateUtils;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.RollbackException;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;

/**
 * SN029001: Servicio de negocio para dar de alta usuarios de la plataforma
 * 
 * @author adele
 *
 */
public class SNRestorePassword extends SNAbstractManageUsuarios {

	private static final int CTE_POSICION_TIRA_EMAIL = 0;
	
	public SNRestorePassword() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see com.treelogic.fawna.arq.negocio.core.ServicioNegocio#ejecutar(com.treelogic.fawna.arq.negocio.core.IContextoEjecucion, com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx)
	 */
	@Override
	public IContexto[] ejecutar(final IContextoEjecucion contexto, final IDatosEntradaTx datosEntrada) 
	{
		if (logger.isInfoEnabled()) {
			logger.info("Inicio Ejecucion del SN029038: Restaurar la contraseña del usuario");
		}
		final long iniTime = System.currentTimeMillis();

		String email = FunctionUtils.defaultIfEmpty(datosEntrada.getString(CTE_POSICION_TIRA_EMAIL), null);
		
		
		IContexto[] salida = null;
		String newMD5Password, newPassword;
		try 
		{			
			
			final UsuariosManager userManager = ManagerFactory.getInstance().getUsuariosManager();
			// Comprobamos que existe en la BBDD algún usuario con ese email
			UsuarioC user = userManager.findByEmail(email);
			
			if(user != null){
				newPassword = generateRandomPassword(32);
				newMD5Password = FunctionUtils.encodePassword(newPassword);
				// Asignar nueva password al usuario
				user.setPassword(newMD5Password);
				userManager.updateUserPassword(user.getCnUsuario(),newMD5Password);
				
				//Correo para el usuario
				IContexto[] datosSalida_mailuser = sendPasswordRestoreMailToUser(contexto, user, newPassword);
			}else{
				// Si el usuario introducido es inválido
				ServicioNegocio.rollback(TipoError.FUNCIONAL, 1200, "Your email is not registered", "Your email is not registered", null);
			}		
			
			salida = ContextoUsuarios.rellenarContexto(user);
			
		} catch (Exception e) {
			if (e instanceof RollbackException) {
				//ServicioNegocio.rollback(...) throw this exception, rethrow!!
				throw (RollbackException) e;
			}
			// se ha producido un error de ejecucion
			ServicioNegocio.rollback(TipoError.TECNICO, new Integer(0), e.getMessage(), e.getMessage(), null);
		}
		if (logger.isInfoEnabled()) {
			logger.info("Fin Ejecucion del SN029038: Restaurar la contraseña del usuario. Tiempo total = " + Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		}
		return salida;
	}



	protected IContexto[] sendPasswordRestoreMailToUser(
			final IContextoEjecucion contexto, 
			final UsuarioC user, final String rawPasswd) 
		throws IOException, TemplateException 
	{
		final BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		final TemplateHashModel enumModels = wrapper.getEnumModels();
		//final TemplateHashModel staticModels = wrapper.getStaticModels();
		
		final Map<String, Object> model = new HashMap<String, Object>();
		model.put("UserStatusEnum", enumModels.get(UserStatusEnum.class.getName()));
		model.put("user", user);
		model.put("rawPasswd", rawPasswd);
		
		final String subject = "[FIONA] Password Restore confirmation";
		final String body = TemplateUtils.processTemplateIntoString(contexto, "mail_user_passwordrestore-html", model);
		
		//invocamos el servicio de negocio para enviar mail al usuario
		final String mailTo = user.getEmail();
		return invokeSendMailService(contexto, mailTo, subject, body);
	}
	
	private String generateRandomPassword(Integer length){
		SecureRandom random = new SecureRandom();
		String result = new BigInteger(130, random).toString(length); 
		return result;
	}


}
