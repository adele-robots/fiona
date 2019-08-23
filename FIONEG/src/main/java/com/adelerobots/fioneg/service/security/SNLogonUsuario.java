package com.adelerobots.fioneg.service.security;

import com.adelerobots.fioneg.context.ContextoUsuarios;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.adelerobots.fioneg.util.FunctionUtils;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.RollbackException;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

/**
 * Servicio encargado de hacer el logon del usuario en la plataforma
 * 
 * @author adele
 */
public class SNLogonUsuario 
		extends ServicioNegocio
		implements LogonConstants 
{

	private static FawnaLogHelper logger = FawnaLogHelper.getLog(SNLogonUsuario.class);

	// Parametros de entrada
	private static final int CTE_POSICION_LOGON = 0;
	private static final int CTE_POSICION_PASSWORD = 1;

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) 
	{
		logger.info("Inicio ejecucion del SN 029998: Logon usuario");
		final long iniTime = System.currentTimeMillis();
		// Obtener campos de entrada
		String userLogon = datosEntrada.getString(CTE_POSICION_LOGON); //puede ser email o username
		String userPassword = datosEntrada.getString(CTE_POSICION_PASSWORD);

		IContexto[] salida = new IContexto[0];
		try {
			String encodedPasswd = FunctionUtils.encodePassword(userPassword);

			UsuariosManager manager = ManagerFactory.getInstance().getUsuariosManager();
			UsuarioC user = manager.findByLogon(userLogon);
			LogonErrorCode errCode = null;
			if (user == null) { //Unknown / no such user
				errCode = LogonErrorCode.ERR525__NO_SUCH_USER;
			}
			else if(!user.getPassword().equals(encodedPasswd)) { //Credentials dont match
				errCode = LogonErrorCode.ERR52e6__LOGON_FAILURE;
			}
			else if(user.isUnconfirmed() || user.isEmailConfirmed()) { //The account has not been activated
				errCode = LogonErrorCode.ERR52e7__LOGON_NEED_ACTIVATION;
			}
			else if(user.isAccountBlocked()) { //The account has been blocked/banned
				errCode = LogonErrorCode.ERR775__ACCOUNT_LOCKED;
			}
			else if(user.isAccountDisabled()) { //The account has been disabled
				errCode = LogonErrorCode.ERR533__ACCOUNT_DISABLED;
			}
			else if(user.isAccountExpired()) { //The account has been expired
				errCode = LogonErrorCode.ERR701__ACCOUNT_EXPIRED;
			}
			else if(user.isCredentialsExpired() ) { //Need password change
				if (user.isFistLogon()) {
					errCode = LogonErrorCode.ERR773__PASSWORD_MUST_CHANGE;//First time
				} else {
					errCode = LogonErrorCode.ERR532__PASSWORD_EXPIRED;//Next times
				}
			}
			
			//Throw rollback on error
			if (errCode != null) {
				ServicioNegocio.rollback(TipoError.FUNCIONAL, errCode.code, errCode.msg, errCode.desc, null);
			} else {
				user.putOnline(); //Mark user as online
				salida = ContextoUsuarios.rellenarContexto(user);
			}
		} catch (Exception e) {
			if (e instanceof RollbackException) {
				//ServicioNegocio.rollback(...) throw this exception, rethrow!!
				throw (RollbackException) e;
			}
			// se ha producido un error de ejecucion
			ServicioNegocio.rollback(TipoError.TECNICO, ServicioNegocio.CODIGO_ERROR_GENERICO, e.getMessage(), e.getMessage(), null);
		}

		logger.info("[Login] Se ha realizado el login con éxito del usuario con NetID: " + userLogon);
		logger.info("Fin ejecucion del SN029998: Logon usuario. Tiempo total = " + Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		return salida;
	}

}
