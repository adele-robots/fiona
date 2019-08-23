package com.adelerobots.fioneg.service.usuarios;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class SNReWriteGeneralConf extends ServicioNegocio {
	
	private static final int CTE_POSICION_USER_ID_MD5 = 0;
	private static final int CTE_POSICION_ROOM_ID = 1;
	private static final int CTE_POSICION_ACCOUNT_ID = 2;
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNReWriteGeneralConf.class);

	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029037: Reescribir el archivo generalConf.ini con la nueva room del usuario.");
		Date datInicio = new Date();
		
		String command;
		
		String md5 = datosEntrada.getString(CTE_POSICION_USER_ID_MD5);
		String roomId = datosEntrada.getString(CTE_POSICION_ROOM_ID);
		BigDecimal bAccountUserId = datosEntrada.getDecimal(CTE_POSICION_ACCOUNT_ID);
		Integer intAccountUserId = null;
		if(bAccountUserId != null){
			intAccountUserId = new Integer(bAccountUserId.intValue());
		}
		
		List<String> avatarResolutionArray = Arrays.asList(Constantes.getAvatarResolution(intAccountUserId).split("\\s*,\\s*"));
		String width = avatarResolutionArray.get(0);
		String height = avatarResolutionArray.get(1);
		String videoBitRate = avatarResolutionArray.get(2);
		String audioBitRate = avatarResolutionArray.get(3);
		
		String hasAdvertising = "";
		String resolution = "";
		switch(intAccountUserId){
		case Constantes.CTE_ACCOUNT_FREE:
			hasAdvertising = "TRUE";
			resolution = Constantes.CTE_ACCOUNT_FREE_RESOLUTION;
			break;
		case Constantes.CTE_ACCOUNT_BASIC:
			hasAdvertising = "FALSE";
			resolution = Constantes.CTE_ACCOUNT_BASIC_RESOLUTION;
			break;
		case Constantes.CTE_ACCOUNT_PRO:
			hasAdvertising = "FALSE";
			resolution = Constantes.CTE_ACCOUNT_PRO_RESOLUTION;
			break;
		case Constantes.CTE_ACCOUNT_CORPORATE:
			hasAdvertising = "FALSE";
			resolution = Constantes.CTE_ACCOUNT_CORPORATE_RESOLUTION;
			break;			
		default:
			hasAdvertising = "TRUE";
			resolution = Constantes.CTE_ACCOUNT_BASIC_RESOLUTION;	
		}		
		
		command = "/datos/script/./reGeneraIni.sh " + md5 + " " + roomId + " " + width + " " + height + " " + videoBitRate + " " + audioBitRate
					+ " " + hasAdvertising + " " + resolution;
		try {
			Process checkFile = Runtime.getRuntime().exec(command);
			checkFile.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		IContexto[] salida = new IContexto[0];
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029037: Reescribir el archivo generalConf.ini con la nueva room del usuario. Tiempo total = "
						+ tiempoTotal + "ms");
		return salida;
	}

}
