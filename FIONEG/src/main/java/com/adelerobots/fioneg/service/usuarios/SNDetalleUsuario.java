package com.adelerobots.fioneg.service.usuarios;

import java.math.BigDecimal;

import com.adelerobots.fioneg.context.ContextoUsuarios;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.adelerobots.fioneg.service.security.LogonConstants;
import com.adelerobots.fioneg.util.FunctionUtils;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

/**
 * Servicio encargado de obtener el detalle de un usuario de la 
 * aplicacion por su email
 */
public class SNDetalleUsuario 
	extends 		ServicioNegocio 
	implements 	LogonConstants  
{

	private static FawnaLogHelper logger = FawnaLogHelper.getLog(SNDetalleUsuario.class);

	// Parametros de entrada
	private static final int CTE_POSICION_QMODE = 0;
	private static final int CTE_POSICION_LOGON = 1;
	private static final int CTE_POSICION_ID = 2;
	
	public static final String CTE_QMODE_BY_LOGON ="L";
	public static final String CTE_QMODE_BY_ID ="I";
	public static final String CTE_QMODE_BY_EMAIL ="E";
	public static final String CTE_QMODE_BY_NICKNAME ="N";

	@Override
	public IContexto[] ejecutar(
			final IContextoEjecucion contexto, 
			final IDatosEntradaTx datosEntrada) 
	{
		logger.info("Inicio ejecucion del SN029003: Detalle usuario");
		
		IContexto[] salida = null;
		try
		{
			// Obtener campos de entrada
			String mode = datosEntrada.getString(CTE_POSICION_QMODE); //puede ser I=Identificador, L=Logon, E=email, N=nickname/username
			mode = FunctionUtils.defaultIfBlank(mode, CTE_QMODE_BY_LOGON);//Default L=By logon
			String text = datosEntrada.getString(CTE_POSICION_LOGON); //puede ser email o username. Requerido si mode=L,E,N
			BigDecimal id = datosEntrada.getDecimal(CTE_POSICION_ID); //id de usuario. Requerido si mode=I
			
			UsuarioC usuario = null;
			//Validaciones extra campos entrada
			if (FunctionUtils.equalsIgnoreCase(CTE_QMODE_BY_LOGON, mode)) {
				if (FunctionUtils.isBlank(text)) {
					//userLogon required = Error tecnico de validacion
					ServicioNegocio.rollback(TipoError.TECNICO, new Integer(0)
							, String.format("El campo C%03d (%s)  es obligatorio si el campo C%03d (%s) tiene el valor '%s'.", 
									CTE_POSICION_LOGON, "logon", CTE_POSICION_QMODE, "qmode", CTE_QMODE_BY_LOGON) 
							, String.format("El campo C%03d (%s)  es obligatorio si el campo C%03d (%s) tiene el valor '%s'.", 
									CTE_POSICION_LOGON, "logon", CTE_POSICION_QMODE, "qmode", CTE_QMODE_BY_LOGON) 
							, null);
				} else {
					//OK: Populate user by logon
					UsuariosManager manager = ManagerFactory.getInstance().getUsuariosManager();
					usuario = manager.findByLogon(text);
				}
			} 
			else if (FunctionUtils.equalsIgnoreCase(CTE_QMODE_BY_ID, mode)) {
				if (id == null) {
					//userId required = Error tecnico de validacion
					ServicioNegocio.rollback(TipoError.TECNICO, new Integer(0)
							, String.format("El campo C%03d (%s)  es obligatorio si el campo C%03d (%s) tiene el valor '%s'.", 
									CTE_POSICION_ID, "id", CTE_POSICION_QMODE, "qmode", CTE_QMODE_BY_ID) 
							, String.format("El campo C%03d (%s)  es obligatorio si el campo C%03d (%s) tiene el valor '%s'.", 
									CTE_POSICION_ID, "id", CTE_POSICION_QMODE, "qmode", CTE_QMODE_BY_ID) 
							, null);
				} else {
					//OK: Populate user by id
					UsuariosManager manager = ManagerFactory.getInstance().getUsuariosManager();
					usuario = manager.findById(id);
				}
			} 
			else if (FunctionUtils.equalsIgnoreCase(CTE_QMODE_BY_EMAIL, mode)) {
				if (FunctionUtils.isBlank(text)) {
					//userLogon required = Error tecnico de validacion
					ServicioNegocio.rollback(TipoError.TECNICO, new Integer(0)
							, String.format("El campo C%03d (%s)  es obligatorio si el campo C%03d (%s) tiene el valor '%s'.", 
									CTE_POSICION_LOGON, "email", CTE_POSICION_QMODE, "qmode", CTE_QMODE_BY_EMAIL) 
							, String.format("El campo C%03d (%s)  es obligatorio si el campo C%03d (%s) tiene el valor '%s'.", 
									CTE_POSICION_LOGON, "email", CTE_POSICION_QMODE, "qmode", CTE_QMODE_BY_EMAIL) 
							, null);
				} else {
					//OK: Populate user by logon
					UsuariosManager manager = ManagerFactory.getInstance().getUsuariosManager();
					usuario = manager.findByEmail(text);
				}
			} 
			else if (FunctionUtils.equalsIgnoreCase(CTE_QMODE_BY_NICKNAME, mode)) {
				if (FunctionUtils.isBlank(text)) {
					//userLogon required = Error tecnico de validacion
					ServicioNegocio.rollback(TipoError.TECNICO, new Integer(0)
							, String.format("El campo C%03d (%s)  es obligatorio si el campo C%03d (%s) tiene el valor '%s'.", 
									CTE_POSICION_LOGON, "nickname", CTE_POSICION_QMODE, "qmode", CTE_QMODE_BY_NICKNAME) 
							, String.format("El campo C%03d (%s)  es obligatorio si el campo C%03d (%s) tiene el valor '%s'.", 
									CTE_POSICION_LOGON, "nickname", CTE_POSICION_QMODE, "qmode", CTE_QMODE_BY_NICKNAME) 
							, null);
				} else {
					//OK: Populate user by logon
					UsuariosManager manager = ManagerFactory.getInstance().getUsuariosManager();
					usuario = manager.findByUsername(text);
				}
			} 
			else {
				//Invalid mode = Error tecnico de validacion
				ServicioNegocio.rollback(TipoError.TECNICO, new Integer(0)
						, String.format("El campo C%03d (%s) solo acepta los valores '', '%s'=QueryByID, '%s'=QueryByLogon, '%s'=QueryByEmail, '%s'=QueryByNickname. Por defecto='%s'.", 
								CTE_POSICION_QMODE, "qmode",  CTE_QMODE_BY_ID, CTE_QMODE_BY_LOGON, CTE_QMODE_BY_EMAIL, CTE_QMODE_BY_NICKNAME, CTE_QMODE_BY_LOGON) 
						, String.format("El campo C%03d (%s) solo acepta los valores '', '%s'=QueryByID, '%s'=QueryByLogon, '%s'=QueryByEmail, '%s'=QueryByNickname. Por defecto='%s'.", 
								CTE_POSICION_QMODE, "qmode",  CTE_QMODE_BY_ID, CTE_QMODE_BY_LOGON, CTE_QMODE_BY_EMAIL, CTE_QMODE_BY_NICKNAME, CTE_QMODE_BY_LOGON) 
						, null);
			}
			
			
			if (usuario == null) {
				//No existe detalle de usuario para ese selector = error funcional con ese codigo
				ServicioNegocio.rollback(TipoError.FUNCIONAL, LogonErrorCode.ERR525__NO_SUCH_USER.code, 
						LogonErrorCode.ERR525__NO_SUCH_USER.msg, LogonErrorCode.ERR525__NO_SUCH_USER.desc, null);
			} else {
				// Calcular el crédito de cuenta del usuario
				UsuariosManager manager = ManagerFactory.getInstance().getUsuariosManager();
				Float credit = manager.getAccountUserCredit(usuario);
				//usuario.setFloAccountCredit(credit);
				usuario.setAccountCredit(credit);
				
				//transform to IContexto
				salida = ContextoUsuarios.rellenarContexto(usuario);
			}
			
			return salida;
		} finally {
			logger.info("Inicio ejecucion del SN029003: Detalle usuario");
		}
	}

}
