package com.adelerobots.fioneg.context;

import com.treelogic.fawna.arq.negocio.contextos.ContextoFactory;
import com.treelogic.fawna.arq.negocio.core.IContexto;

public class ContextoUploadSpark {
	
	public static final String UPLOAD_CTX = "FIONEGN008";
	
	public static final String CTX_UPLOAD_INFO = "FIONEG008010";
	
	
	public static IContexto[] rellenarContexto(String uploadInfo) {
		IContexto[] salida = null;
		salida = new IContexto[1];
		// Rellenar contexto de salida
		salida[0] = rellenarContextoUpload(uploadInfo);
	
		return salida;
	}
	
	private static IContexto rellenarContextoUpload(String uploadInfo) {
		final IContexto datos = ContextoFactory.getInstance().getContexto(UPLOAD_CTX);

		datos.put(CTX_UPLOAD_INFO, uploadInfo);
				
		return datos;
	}
}
