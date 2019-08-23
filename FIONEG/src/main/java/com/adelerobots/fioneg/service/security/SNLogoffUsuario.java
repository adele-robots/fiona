package com.adelerobots.fioneg.service.security;

import java.math.BigDecimal;

import com.adelerobots.fioneg.context.ContextoUsuarios;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.RollbackException;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

/**
 * Servicio encargado de hacer el logoff del usuario en la plataforma
 * 
 * @author adele
 */
public class SNLogoffUsuario 
		extends ServicioNegocio
		implements LogonConstants 
{

	private final static FawnaLogHelper logger = FawnaLogHelper.getLog(SNLogoffUsuario.class);

	// Parametros de entrada
	private static final int CTE_POSICION_ID = 0;

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) 
	{
		logger.info("Inicio ejecucion del SN 029999: Logoff usuario");
		final long iniTime = System.currentTimeMillis();
		// Obtener campos de entrada
		BigDecimal userId = datosEntrada.getDecimal(CTE_POSICION_ID);

		IContexto[] salida = new IContexto[0];
		try {
			UsuariosManager manager = ManagerFactory.getInstance().getUsuariosManager();
			UsuarioC user = manager.findById(userId);
			LogonErrorCode errCode = null;
			if (user == null) { //Unknown / no such user
				errCode = LogonErrorCode.ERR525__NO_SUCH_USER;
			}
			//Throw rollback on error
			if (errCode != null) {
				ServicioNegocio.rollback(TipoError.FUNCIONAL, errCode.code, errCode.msg, errCode.desc, null);
			} else {
				if (user.isOnline()) {
					//Only change flags if online to evict change other restrictive flags
					user.putOffline();
				}
				salida = ContextoUsuarios.rellenarContexto(user);
			}
		} catch (Exception e) {
			if (e instanceof RollbackException) {
				//ServicioNegocio.rollback(...) throw this exception, rethrow!!
				throw (RollbackException) e;
			}
			// se ha producido un error de ejecucion
			ServicioNegocio.rollback(TipoError.TECNICO, new Integer(0), e.getMessage(), e.getMessage(), null);
		}

		if (logger.isInfoEnabled()) {
			logger.info("Fin ejecucion del SN 029999: Logoff usuario. Tiempo total = " + Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		}
		return salida;
	}

}
