package com.adelerobots.serneg.context;

import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

public class ContextoMensaje {
	public static final String MENSAJE_CTX = "FIONEGN003";
	
	public static final String CTX_MENSAJE_CONTENIDO = "FIONEG003010";
	
	public static IContexto[] rellenarContexto(final String contenido) {
		IContexto[] salida = null;
		// Rellenar contexto de salida
		if (contenido != null && !"".equals(contenido)) {
			salida = new IContexto[1];
			// Metemos en el contexto la categoria
			salida[0] = rellenarContextoMensaje(contenido);
		}
		return salida;
	}
	
	private static IContexto rellenarContextoMensaje(final String contenido) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(MENSAJE_CTX);

		datos.put(CTX_MENSAJE_CONTENIDO, contenido);
		
		return datos;
	}
}
