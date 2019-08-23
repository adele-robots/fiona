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
 * SN029006: Servicio encargado de confirmar el registro de un usuario y activar su validación de credenciales
 * 
 * @author adele
 */
public class SNUsermailConfirm extends ServicioNegocio {

	private static FawnaLogHelper logger = FawnaLogHelper.getLog(SNUsermailConfirm.class);

	// Parametros de entrada
	private static final int CTE_POSICION_ID = 0;
	private static final int CTE_POSICION_CONFIRMATION = 1;
	
	public SNUsermailConfirm() {
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
			logger.info("Inicio Ejecucion del SN029039: Confirmar email usuario");
		}
		final long iniTime = System.currentTimeMillis();
		
		Integer userId = null; {
			final BigDecimal value = datosEntrada.getDecimal(CTE_POSICION_ID);
			if (value != null) userId = new Integer(value.intValue());
		}
		final String signupCode = datosEntrada.getString(CTE_POSICION_CONFIRMATION);
		
		final UsuariosManager manager = ManagerFactory.getInstance().getUsuariosManager();
		
		IContexto[] salida = null;
		try {
			
			final UsuarioC user = manager.confirmarEmailUsuario(userId, signupCode);
			salida = ContextoUsuarios.rellenarContexto(user);
			
		} catch (Exception e) {
			if (e instanceof RollbackException) {
				//ServicioNegocio.rollback(...) throw this exception, rethrow!!
				throw (RollbackException) e;
			}
			// se ha producido un error de ejecucion
			ServicioNegocio.rollback(TipoError.FUNCIONAL, 800, e.getMessage(), e.getMessage(), null);
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("Fin Ejecucion del SN029039: Confirmar email usuario. Tiempo total = " + Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
			}
		}
		return salida;
	}

}
