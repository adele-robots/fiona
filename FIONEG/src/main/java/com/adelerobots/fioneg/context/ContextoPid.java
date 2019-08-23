package com.adelerobots.fioneg.context;

import java.math.BigDecimal;

import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

public class ContextoPid {
	public static final String PID_CTX = "FIONEGN002";
	
	public static final String CTX_PID_SCRIPT= "FIONEG002010";
	public static final String CTX_PID_PROCESS = "FIONEG002020";
	// Usado en FIONEGEPI para el tiempo de vida del avatar
	public static final String CTX_AVATAR_TIMEALIVE = "FIONEG002030";
	
	public static IContexto[] rellenarContexto(final int pid_script, final int pid_proceso) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (0!=pid_script && 0!=pid_proceso) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoPid(pid_script, pid_proceso);
		}else{
			salida = new IContexto[1];
			salida[0] = rellenarContextoPid(-1, -1);
		}
		return salida;
	}
	
	private static IContexto rellenarContextoPid(final int pid_s, final int pid_p) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(PID_CTX);

		datos.put(CTX_PID_SCRIPT, new BigDecimal(pid_s));
		datos.put(CTX_PID_PROCESS, new BigDecimal(pid_p));
		
		return datos;
	}
	
	
}
