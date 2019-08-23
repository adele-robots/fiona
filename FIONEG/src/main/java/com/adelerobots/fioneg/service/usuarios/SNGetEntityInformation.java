package com.adelerobots.fioneg.service.usuarios;

import java.math.BigDecimal;
import java.util.Date;

import com.adelerobots.fioneg.context.ContextoEntidad;
import com.adelerobots.fioneg.entity.EntidadC;
import com.adelerobots.fioneg.manager.EntidadManager;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNGetEntityInformation extends ServicioNegocio
{
	private static final int CTE_POSICION_TIRA_ID = 0;

	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNGetEntityInformation.class);
	
	public SNGetEntityInformation(){
		super();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.treelogic.fawna.arq.negocio.core.ServicioNegocio#ejecutar(com.treelogic.fawna.arq.negocio.core.IContextoEjecucion, com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx)
	 */
	@Override
	public IContexto[] ejecutar(final IContextoEjecucion contexto, final IDatosEntradaTx datosEntrada) 
	{
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Inicio Ejecucion del SN029062: Obtener datos de la entidad por Id");
		}
		Date datInicio = new Date();
		
		Integer entityId = null; {
			final BigDecimal value = datosEntrada.getDecimal(CTE_POSICION_TIRA_ID);
			if (value != null) entityId = new Integer(value.intValue());
		}
		final EntidadManager entityManager = ManagerFactory.getInstance().getEntidadManager();
		EntidadC entidad = entityManager.getById(entityId);
		IContexto[] salida = ContextoEntidad.rellenarContexto(entidad);

		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER.info("Fin Ejecucion del SN 029062: Obtener datos de la entidad por Id. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}
		
}