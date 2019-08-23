package com.adelerobots.fioneg.service.avatarbuilder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

/* Este servicio de negocio añade una lista de sparks a un usuario mediante la entrada de datos en la tabla "usuariospark"
 * Su origen es teniendo en mente que al registrarse, un usuario deberá tener entre sus datos iniciales aquellos sparks que correspondan 
 * al core de su tipo de cuenta (en el caso que distintos tipos de cuenta tengan distinto inicio.
 * La lista de coresparks es un String, cargado desde properties a través de Constantes.java donde se tiene una lista de números que corresponden al CN_Spark
 * separados por comas. En registro, este servicio se invoca desde SNAltaUsuario y más adelante cuando tengamos Store, puede ser llamado desde el checkout pasándole la lista de las sparks adquiridas
 * *
 * 
 * DEPRECADO! no usar, en su lugar usar AddSparks (UsuariosManager)
 * 
 *  */ 


public class AddSparksToUser extends ServicioNegocio {

	private static FawnaLogHelper LOGGER = FawnaLogHelper.getLog(AddSparksToUser.class);
	
	private static final int CTE_POSICION_TIRA_USER_ID = 0;
	//la lista de sparks a añadir consiste en un string con los números de spark separados por comas, de modo que se puedan añadir una o varias sparks al usuario en la misma operación
	private static final int CTE_POSICION_TIRA_SPARK_LIST = 1;
	
	public AddSparksToUser(){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto,
			IDatosEntradaTx datosEntrada) {
		
		LOGGER.info("Inicio Ejecucion del SN 029011: AddSparksToUser");
		
		final UsuariosManager manager = ManagerFactory.getInstance().getUsuariosManager();
		
		final BigDecimal decIdUsuario = datosEntrada.getDecimal(CTE_POSICION_TIRA_USER_ID);		
		Integer IdUsuario= null;
		if(decIdUsuario!= null){
			IdUsuario = new Integer(decIdUsuario.intValue());
		}	
	
		String sparkarray = datosEntrada.getString(CTE_POSICION_TIRA_SPARK_LIST);
		List<String> sparklist = Arrays.asList(sparkarray.split("\\s*,\\s*"));
		
		try {
			UsuarioC user = manager.getUsuario(IdUsuario);
			manager.AddSparks(user, sparklist);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		LOGGER.info("Fin Ejecucion del SN 029011: AddSparksToUser");
		
		return null;
	}

}
