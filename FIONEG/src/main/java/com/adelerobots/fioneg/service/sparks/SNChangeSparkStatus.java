package com.adelerobots.fioneg.service.sparks;





import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.adelerobots.fioneg.context.ContextoSparks;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.entity.UserStatusEnum;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.entity.rejection.RejectionC;
import com.adelerobots.fioneg.entity.status.StatusC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.SparkManager;
import com.adelerobots.fioneg.manager.StatusManager;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.adelerobots.fioneg.util.TemplateUtils;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;



/**
 * Servicio de Negocio fachada para cambiar el estado de un spark
 * concreto
 * 
 * @author adele
 * 
 */
public class SNChangeSparkStatus extends ServicioNegocio {
	
	private static final int CTE_POSICION_ID_SPARK = 0;
	private static final int CTE_POSICION_NUEVO_STATUS = 1;
	private static final int CTE_POSICION_ID_USUARIO_MODIFICA = 2;
	private static final int CTE_POSICION_MOTIVO_RECHAZO = 3;
	
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper
	.getLog(SNChangeSparkStatus.class);

	
	public SNChangeSparkStatus(){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029017: Modificación del status de un spark.");
		Date datInicio = new Date();
		
		BigDecimal bidIdSpark = datosEntrada
		.getDecimal(CTE_POSICION_ID_SPARK);
		
		BigDecimal bidIdNuevoStatus = datosEntrada
		.getDecimal(CTE_POSICION_NUEVO_STATUS);
		
		BigDecimal bidCodUsuarioModifica = datosEntrada.getDecimal(CTE_POSICION_ID_USUARIO_MODIFICA);
		
		// Convertimos el código a Integer
		Integer intCodSpark = new Integer(bidIdSpark.intValue());
		Integer intCodNuevoStatus = new Integer(bidIdNuevoStatus.intValue());
		Integer intCodUsuarioModifica = new Integer(bidCodUsuarioModifica.intValue());
		
		String strMotivoRechazo = datosEntrada.getString(CTE_POSICION_MOTIVO_RECHAZO);
		
		// Cambiamos el estado del spark que corresponda
		SparkManager sparkManager = ManagerFactory.getInstance().getSparkManager();	
		UsuariosManager usuarioManager = ManagerFactory.getInstance().getUsuariosManager();
		StatusManager statusManager = ManagerFactory.getInstance().getStatusManager();
		
		sparkManager.changeSparkStatus(intCodSpark, intCodNuevoStatus, strMotivoRechazo, intCodUsuarioModifica);
		
				
		UsuarioC usuarioModificador = null;
		try {
			usuarioModificador = usuarioManager.getUsuario(intCodUsuarioModifica);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		SparkC spark = new SparkC();
		try {
			spark = sparkManager.getSpark(intCodSpark, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StatusC status = statusManager.getStatus(intCodNuevoStatus); 
		
		// Se intenta enviar el correo de notificación 
		// indicando que el estado de un spark
		// Enviar correo al destinatario de la invitacion
		try {
			String to = Constantes.getMAIL_NOTIFICATION_ADDR();
			String subject = "[FIONA] Someone changed an spark status!!";
			String body = composeEmail(contexto, usuarioModificador, spark.getStrNombre(), status.getStrDescripcion());
			invokeSendMailService(contexto, to, subject, body);
		} catch (Exception e) {
			logger.warn("[SN029021]Fallo en el envio de la invitación", e);
		}
							
		// Ponemos en el contexto los datos del spark cuyo status
		// acaba de ser modificado
		IContexto[] salida = ContextoSparks.rellenarContexto(spark);
		
		long tiempoTotal = new Date().getTime() - datInicio.getTime();
		LOGGER
				.info("Fin Ejecucion del SN 029017: Modificación del status de un spark. Tiempo total = "
						+ tiempoTotal + "ms");
		
		return salida;
	}
	
	
	protected String composeEmail(
			final IContextoEjecucion contexto, 
			final UsuarioC user, 
			final String sparkName, 
			final String newStatus) 
		throws IOException, TemplateException 
	{
		final BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		final TemplateHashModel enumModels = wrapper.getEnumModels();
		//final TemplateHashModel staticModels = wrapper.getStaticModels();
		
		final Map<String, Object> model = new HashMap<String, Object>();
		model.put("UserStatusEnum", enumModels.get(UserStatusEnum.class.getName()));
		model.put("user", user);
		model.put("sparkName", sparkName);
		model.put("newStatus", newStatus);
				
		
		final String body = TemplateUtils.processTemplateIntoString(contexto, "mail_status_changed-html", model);
		
		return body;
	}



	/**
	 * Invoca el servicio de negocio encargado de mandar un correo 
	 * desde el email de la plataforma
	 * 
	 * @param contexto contexto en ejecucion
	 * @param mailTo direccion de correo de a quien va dirigido
	 * @param subject subject del correo
	 * @param body cuerpo del correo
	 * @return
	 * @throws NumberFormatException
	 * @throws IndexOutOfBoundsException
	 */
	protected IContexto[] invokeSendMailService(
			final IContextoEjecucion contexto, 
			String mailTo, String subject, String body) 
	{
		IContexto[] datosSalida;
		IDatosEntradaTx datosEntrada = ServicioNegocio.getPrograma(
				contexto, new Integer("029"), new Integer("004"));
		datosEntrada.addCampo(null, mailTo, 0);
		datosEntrada.addCampo(null, subject, 1);
		datosEntrada.addCampo(null, body, 2);

		// se invoca el SN
		datosSalida = ServicioNegocio.invocarServicio(contexto, datosEntrada);
		return datosSalida;
	}

}
