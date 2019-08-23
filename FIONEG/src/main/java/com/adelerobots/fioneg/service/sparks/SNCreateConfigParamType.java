package com.adelerobots.fioneg.service.sparks;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.context.ContextoTipo;
import com.adelerobots.fioneg.entity.DatoListaC;
import com.adelerobots.fioneg.entity.TipoC;
import com.adelerobots.fioneg.manager.DatoListaManager;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.TipoManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.IRegistro;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNCreateConfigParamType extends ServicioNegocio {
	
	public static final String CTX_TIPO_ID = "FIONEG018010";
	public static final String CTX_NOMBRE = "FIONEG018020";
	public static final String CTX_FUNCVALIDACION = "FIONEG018030";
	public static final String CTX_TIPOBASICO = "FIONEG018040";
	public static final String CTX_USUARIO_ID = "FIONEG018050";
	public static final String CTX_DATOLISTA_REG = "FIONEG018060";
	public static final String CTX_DATOLISTA_REG1 = "FIONEG018060010";
	
	private static final int CTE_POSICION_CTX_TIPO = 0;


	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNCreateConfigParamType.class);

	
	public SNCreateConfigParamType(){
		super();
	}

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029055: Insertar un nuevo tipo de parámetro de configuración");
		Date datInicio = new Date();
		
		IContexto entrada = datosEntrada.getContexto(CTE_POSICION_CTX_TIPO);
		String nombreTipo = entrada.getString(CTX_NOMBRE);
		String funcValidacion = entrada.getString(CTX_FUNCVALIDACION);
		String tipoBasico = entrada.getString(CTX_TIPOBASICO);			
		
		BigDecimal bidCodUsuario = entrada.getBigDecimal(CTX_USUARIO_ID);
		Integer intCodUsuario = null;
		if(bidCodUsuario != null){
			intCodUsuario = new Integer(bidCodUsuario.intValue());
		}
		
		TipoManager tipoManager = ManagerFactory.getInstance().getTipoManager();		
		TipoC tipo = new TipoC(); 
		tipo = tipoManager.crearTipo(nombreTipo, funcValidacion, tipoBasico.charAt(0), intCodUsuario);			
		
		// Recogemos los valores de la lista, sí es que el nuevo tipo es un tipo de
		// datoLista y los insertamos en BB.DD
		IRegistro[] registro = entrada.getRegistro(CTX_DATOLISTA_REG);
		
		if (registro != null) {
			List <DatoListaC> lstListValues = new ArrayList<DatoListaC>();
			DatoListaManager datoListaManager = ManagerFactory.getInstance().getDatoListaManager();				 
			lstListValues = datoListaManager.crearValoresDatoLista(registro,tipo.getCnTipo());
			// Actualizo el tipo con su lista de valores posibles
			tipo.setLstDatosLista(lstListValues);
		}		
		
		IContexto[] salida = ContextoTipo.rellenarContexto(tipo);
		 
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029055: Insertar un nuevo tipo de parámetro de configuración. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;		
	}

}
