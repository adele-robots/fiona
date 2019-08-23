package com.adelerobots.web.fiopre.listeners;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Scanner;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

import com.adelerobots.web.fiopre.utilidades.ConfigUtils;
import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.SNInvoker;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.persistencia.ContextoLocator;
import com.treelogic.fawna.presentacion.core.persistencia.ContextoSesion;
import com.treelogic.fawna.presentacion.core.persistencia.DatoFawnaFactory;
import com.treelogic.fawna.presentacion.core.persistencia.IDato;
import com.treelogic.fawna.presentacion.core.persistencia.IDatosFawna;
import com.treelogic.fawna.presentacion.core.utilidades.Properties;
import com.treelogic.fawna.presentacion.core.utilidades.constantes.ConstantesSesion;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Listener base para los procesos de usuario
 * 
 * @author adele
 * 
 */
abstract class AbstractUserProcessListener implements
		IProcesadorDeAjaxChangeListener {
	private static final long serialVersionUID = -6051158875768049506L;
	
	private static final MessageFormat INVALID_MESSAGE_FORMAT = new MessageFormat("");
	
	/**
	 * Cache to hold already generated MessageFormats per message.
	 * Used for passed-in default messages. MessageFormats for resolved
	 * codes are cached on a specific basis in subclasses.
	 */
	private final Map<String, MessageFormat> cachedMessageFormats = new HashMap<String, MessageFormat>();
	
	protected Logger logger = Logger.getLogger(getClass());

	/**
	 * Invocar a SN029009 con datos de entrada usermaild5 para crear el proceso
	 * avatar de un usuario
	 * 
	 * @param usermaild5
	 *            hash unico de usuario
	 * @param useridInt
	 * @return un array con los pid de los procesos avatar 0 => avatar.sh 1 =>
	 *         Run.sh
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected String[] invokeCreateUserAvatarProcess(final String usermaild5,
			Integer useridInt, String session) throws FactoriaDatosException,
			PersistenciaException, FawnaInvokerException {
		// Invoke SN to start and register process
		SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", usermaild5, "String");
		datosEntrada.putDato("1", useridInt, "BigDecimal");
		datosEntrada.putDato("3", session, "String");
		IContexto[] datos = invoker.invokeSNParaContextos("029", "009",
				datosEntrada, false);

		// Update context value
		// String pidsh = datos[0].getString("FIONEG002010");
		String pidsh = datos[0].getBigDecimal("FIONEG002010").toString();
		// String pidrun = datos[0].getString("FIONEG002020");
		String pidrun = datos[0].getBigDecimal("FIONEG002020").toString();
		ContextUtils.setUserPidAsString(pidsh, pidrun);

		return new String[] { pidsh, pidrun };
	}

	/**
	 * Invocar a SN029010 con datos de entrada userId y shPID destruyendo el
	 * proceso avatar de un usuario
	 * 
	 * @param userid
	 *            - identificador de usuario
	 * @param shPID
	 *            - identificador del proceso avatar para ese usuario
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected void invokeDestroyUserAvatarProcess(String userid, String shPID)
			throws FactoriaDatosException, PersistenciaException,
			FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		// Invoke SN to stop and unregister process
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", shPID, "String");
		invoker.invokeSNParaCampo("029", "010", datosEntrada, null, false);
	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession ses = attr.getRequest().getSession();
        ses.removeAttribute(ses.getId());
	}

	/**
	 * Invocar a SN029012 con datos de entrada userId y userPid marcando el
	 * proceso avatar de un usuario como activo
	 * 
	 * @param userid
	 *            - identificador de usuario
	 * @param shPID
	 *            - identificador del proceso avatar para ese usuario
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected String invokeTouchUserAvatarProcess(Integer userid, Integer shPID)
			throws FactoriaDatosException, PersistenciaException,
			FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", userid, "BigDecimal");
		datosEntrada.putDato("1", shPID, "BigDecimal");

		Object dato = invoker.invokeSNParaCampo("029", "012", datosEntrada,
				"FIONEG003010", false);
		
		return dato == null ? null : dato.toString();
		
	}

	
    private void setQueueIds(){

	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession();

        final String sessionId = session.getId();

        File tmpDir = new File("/tmp/");
        File [] foundFiles = tmpDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.startsWith(sessionId + "_");
            }
        });

        Hashtable sparkQueues = (Hashtable) session.getAttribute(sessionId);
        if(sparkQueues == null)
        	sparkQueues = new Hashtable();

        for (File foundFile : foundFiles){

                try {
                        Scanner fileScanner = new Scanner(foundFile);

                        Integer queueId = fileScanner.nextInt();

                        fileScanner.close();

                        String fileName = foundFile.getName();
                        //fileName = fileName.substring(0, fileName.lastIndexOf("."));

                        String[] fileNameTokens = fileName.split("_");

                        sparkQueues.put(fileNameTokens[fileNameTokens.length-1], queueId);

                        if(foundFile.canWrite() && (! foundFile.delete()))
                        		logger.warn("Could not delete " + foundFile.getName());

                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                }
        }

        session.setAttribute(sessionId, sparkQueues);
    }

	/**
	 * Invocar a SN029042 con datos de entrada pId del proceso del avatar para
	 * obtener la respuesta del gestor de chat al mensaje enviado
	 * 
	 * @param runPID
	 *            - identificador del proceso avatar para ese usuario
	 * @return respuesta del gestor de chat
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected String invokeGetGestorAnswer(final String runPID)
			throws FactoriaDatosException, PersistenciaException,
			FawnaInvokerException {

	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession ses = attr.getRequest().getSession();
        Hashtable table = (Hashtable) ses.getAttribute(ses.getId());
        if(table == null)
			setQueueIds();
        Integer idColaChat= (Integer) ( (Hashtable) ses.getAttribute(ses.getId()) ).get("ChatSpark");
		if(idColaChat == null) {
			setQueueIds();
			idColaChat= (Integer) ( (Hashtable) ses.getAttribute(ses.getId()) ).get("ChatSpark");
			if(idColaChat == null)
				return null;
		}
		
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", new BigDecimal(idColaChat.intValue()), "BigDecimal");
		Object dato = invoker.invokeSNParaCampo("029", "042", datosEntrada,
				"FIONEG003010", false);

		return dato == null ? null : dato.toString();
	}
	/**
	 * Invocar a SN029013 con datos de entrada pId y message para enviar mensaje
	 * al gestor de diálogo
	 * 
	 * @param runPID
	 *            - identificador del proceso avatar para ese usuario
	 * @param message
	 *            - mensaje a enviar al gestor de chat
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected void invokeSendMessageToGestor(final String runPID,
			final String message) throws FactoriaDatosException,
			PersistenciaException, FawnaInvokerException {

	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession ses = attr.getRequest().getSession();
        Hashtable table = (Hashtable) ses.getAttribute(ses.getId());
        if(table == null)
			setQueueIds();
		Integer idColaChat= (Integer) ( (Hashtable) ses.getAttribute(ses.getId()) ).get("ChatSpark");
		if(idColaChat == null) {
			setQueueIds();
			idColaChat= (Integer) ( (Hashtable) ses.getAttribute(ses.getId()) ).get("ChatSpark");
			if(idColaChat == null)
				return;
		}
		
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", new BigDecimal(idColaChat.intValue()), "BigDecimal");
		datosEntrada.putDato("1", message, "String");
		invoker.invokeSNParaCampo("029", "013", datosEntrada, null, false);
	}

	/**
	 * Comprueba que el usuario indicado tenga un componente de chat en su
	 * avatar
	 * 
	 * @param usermaild5
	 *            hash unico de usuario
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	protected boolean detectChatComponent(final String usermaild5)
			throws JDOMException, IOException, FileNotFoundException {
		return detectComponent(usermaild5, "ChatThreadSpark");
	}

	/**
	 * Comprueba que el usuario indicado tenga un componente en su
	 * avatar
	 * 
	 * @param usermaild5
	 *            hash unico de usuario
	 * @param componente
	 *            nombre del componente que buscamos
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	protected boolean detectComponent(final String usermaild5, final String componente)
			throws JDOMException, IOException, FileNotFoundException {
		boolean found = false;

		// Creamos el builder basado en SAX
		SAXBuilder builder = new SAXBuilder();
		// Construimos el arbol DOM a partir del fichero xml
		// TODO: Modificar en función de la estructura de directorios que haya
		// cuando un usuario pueda tener varias configuraciones.
		// File avatarCfgFile = new
		// File(ConfigUtils.getNfsUserPrivateFolder(usermaild5), "avatar2.xml");
		File avatarCfgFile = new File(ConfigUtils.getNfsUserFolder(usermaild5),
				"avatar.xml");

		Document doc = builder.build(new FileInputStream(avatarCfgFile));
		Element root = doc.getRootElement();
		Element declarations = root.getChild("ComponentDeclarations");

		@SuppressWarnings("unchecked")
		List<Element> components = declarations.getChildren();
		for (int i = 0; i < components.size() && !found; i++) {
			Element component = components.get(i);
			String valorType = component.getAttributeValue("type");

			if (valorType != null
					&& componente.compareToIgnoreCase(valorType) == 0) {
				found = true;
			}
		}
		return found;
	}

	/**
	 * Método para la invocación del servicio que devuelve la lista de estados
	 * por los que ha pasado un spark
	 * 
	 * @param intCodSpark
	 *            Identificador único del spark
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected Object invokeGetListStatus(final Integer intCodSpark)
			throws FactoriaDatosException, PersistenciaException,
			FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", intCodSpark, "BigDecimal");

		Map<String, String> prop = new HashMap<String, String>();

		prop.put("FIONEG009010", "status_id");
		prop.put("FIONEG009030", "status_date");
		prop.put("FIONEG009050", "status_nombre");
		prop.put("FIONEG009060", "status_nombre_usuario");

		Object dato = invoker.invokeSNParaTabla("029", "029", "FIONEGN009",
				datosEntrada, prop, false);

		return dato;

	}

	/**
	 * Método para la invocación del servicio que devuelve la lista de crash de
	 * un spark
	 * 
	 * @param intCodSpark
	 *            Identificador único del spark
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected Object invokeGetListCrash(final Integer intCodSpark)
			throws FactoriaDatosException, PersistenciaException,
			FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", intCodSpark, "BigDecimal");

		Map<String, String> prop = new HashMap<String, String>();

		prop.put("FIONEG011010", "crash_id");
		prop.put("FIONEG011030", "crash_date");
		prop.put("FIONEG011040", "crash_motivo");

		Object dato = invoker.invokeSNParaTabla("029", "031", "FIONEGN011",
				datosEntrada, prop, false);

		return dato;
	}

	/**
	 * Método para la invocación del servicio que devuelve la lista de motivos
	 * por los que se ha podido rechazar un spark
	 * 
	 * @param intCodSpark
	 *            Identificador único de un spark
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected Object invokeGetListRejection(final Integer intCodSpark)
			throws FactoriaDatosException, PersistenciaException,
			FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", intCodSpark, "BigDecimal");

		Map<String, String> prop = new HashMap<String, String>();

		prop.put("FIONEG010010", "rejection_id");
		prop.put("FIONEG010030", "rejection_date");
		prop.put("FIONEG010040", "rejection_motivo");

		Object dato = invoker.invokeSNParaTabla("029", "030", "FIONEGN010",
				datosEntrada, prop, false);

		return dato;
	}

	/**
	 * Invocar a SN029037 con datos de entrada mailmd5 y roomId para reescribir
	 * el archivo de usuario "generalConfini"
	 * 
	 * @param mailmd5
	 *            - mail de un usuario codificado en md5
	 * @param roomId
	 *            - Room a la que se dirigirá al usuario en el red5
	 * @param userAccountId
	 *            - ID del tipo de cuenta de usuario
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected void invokeSNRewriteGeneralConf(String mailmd5, String roomId, Integer userAccountId)
			throws FactoriaDatosException, PersistenciaException,
			FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", mailmd5, "String");
		datosEntrada.putDato("1", roomId, "String");
		datosEntrada.putDato("2", userAccountId, "BigDecimal");
		Object dato = invoker.invokeSNParaCampo("029", "037", datosEntrada,
				null, false);
	}

	/**
	 * Método para la invocación del servicio que devuelve la lista de precios
	 * de los sparks de una configuración
	 * 
	 * @param intCodSpark
	 *            Identificador único del spark
	 * @param strNombreFichero
	 *            nombre del fichero xml con la configuración
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected IContexto[] invokeGetListPrecios(final Integer intCodUsuario,
			final String strNombreFichero, Integer intNumUsuarios, Integer intIdUnidadTiempo,
			Integer intIdResolution, Integer intHighAvailability) throws FactoriaDatosException,
			PersistenciaException, FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", intCodUsuario, "BigDecimal");
		if (strNombreFichero != null && !"".equals(strNombreFichero)) {
			datosEntrada.putDato("1", strNombreFichero, "String");
		}
		datosEntrada.putDato("2", intNumUsuarios, "BigDecimal");
		datosEntrada.putDato("3", intIdUnidadTiempo, "BigDecimal");
		datosEntrada.putDato("4", intIdResolution, "BigDecimal");
		datosEntrada.putDato("5", intHighAvailability, "BigDecimal");
		
		/**
		Map<String, String> prop = new HashMap<String, String>();

		prop.put("FIONEG016010", "spark_id");
		prop.put("FIONEG016020", "nombre_spark");
		prop.put("FIONEG016030", "usuarios");
		prop.put("FIONEG016040", "precio");
		prop.put("FIONEG016050", "uso");
		prop.put("FIONEG016060", "id_unidad_uso");
		prop.put("FIONEG016070", "unidad_uso");
		prop.put("FIONEG016080", "totalMensual");
		prop.put("FIONEG016090", "totalUso");

		Object dato = invoker.invokeSNParaTabla("029", "045", "FIONEGN016",
				datosEntrada, prop, false);*/
		IContexto [] datos = invoker.invokeSNParaContextos("029", "045", datosEntrada, false);

		return datos;

	}

	/**
	 * Método que permite asociar un nuevo precio a un spark
	 * 
	 * @param intNumUsuarios
	 *            Número de usuarios concurrentes
	 * @param intIdUnidadTiempo
	 *            Identificador de la unidad de tiempo
	 * @param intIdUnidadUso
	 *            Identificador de la unidad de uso
	 * @param floCantidad
	 *            Cantidad asociada a la unidad de tiempo o de uso elegida
	 * @param floEuros
	 *            Precio real del spark
	 * @param intSparkId
	 *            Identificador del spark al que se asociará el precio
	 * @return Se devolverá el identificador del spark recién insertado
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected Integer invokeAddPriceToSpark(final Integer intNumUsuarios,
			final Integer intIdUnidadTiempo, final Integer intIdUnidadUso,
			final Float floCantidad, final Float floEuros,
			final Integer intSparkId, final String strEsDesarrollo) throws FactoriaDatosException,
			PersistenciaException, FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();

		datosEntrada.putDato("0", intNumUsuarios, "BigDecimal");
		if (intIdUnidadTiempo != null)
			datosEntrada.putDato("1", intIdUnidadTiempo, "BigDecimal");
		if (intIdUnidadUso != null)
			datosEntrada.putDato("2", intIdUnidadUso, "BigDecimal");
		datosEntrada.putDato("3", floCantidad, "BigDecimal");
		datosEntrada.putDato("4", floEuros, "BigDecimal");
		datosEntrada.putDato("5", intSparkId, "BigDecimal");
		if(strEsDesarrollo != null)
			datosEntrada.putDato("6", strEsDesarrollo, "String");

		IContexto[] datos = invoker.invokeSNParaContextos("029", "051",
				datosEntrada, false);

		BigDecimal bidCodPrice = datos[0].getBigDecimal("FIONEG019010");

		Integer intCodPrice = null;
		if (bidCodPrice != null)
			intCodPrice = new Integer(bidCodPrice.intValue());

		return intCodPrice;
	}

	
	/**
	 * Método que permite eliminar un precio perteneciente a un spark
	 * @param intCodPrecio Identificador del precio a eliminar
	 * @param intSparkId Identificador del spark cuyo precio se va
	 * a eliminar
	 * @return Se devuelven los datos del spark que ha sido eliminado
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected String[] invokeDeletePriceFromSpark(final Integer intCodPrecio,
			final Integer intSparkId) throws FactoriaDatosException,
			PersistenciaException, FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();

		datosEntrada.putDato("0", intCodPrecio, "BigDecimal");	
		datosEntrada.putDato("1", intSparkId, "BigDecimal");

		IContexto[] datos = invoker.invokeSNParaContextos("029", "054",
				datosEntrada, false);

		BigDecimal bidCodPrice = datos[0].getBigDecimal("FIONEG019010");
		String esActivo = datos[0].getString("FIONEG019090");
		String esUsado = datos[0].getString("FIONEG019100");		

		String [] resultado = new String[3];

		// 0 - Identidicador del precio
		resultado[0] = bidCodPrice.toString();		
		// 1 - Es o no activo
		resultado[1] = esActivo;
		// 2 - Está o no relacionado con un usuariospark
		resultado[2] = esUsado;

		return resultado;
	}


	/**
	 * Invocar a SN029049 para recuperar la lista de
	 * tipos disponibles por usuario
	 * 
	 * @param intCodUsuario - ID del usuario
	 *  
	 * @return Mapa 
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected Map<String, String> invokeListAllParamTypesByUser(final Integer intCodUsuario)
	throws FactoriaDatosException, 
	PersistenciaException,
	FawnaInvokerException 
	{
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();		
		datosEntrada.putDato("0", intCodUsuario, "BigDecimal");

		@SuppressWarnings("unchecked")
		Map<String, String> datos = invoker.invokeSNParaColeccion("029", "049", datosEntrada, "FIONEG018010", "FIONEG018020", true, true);

		return datos;
	}

	/**
	 * Invocar a SN029050 para recuperar la lista de
	 * tipos definidos por el usuario
	 * 
	 * @param intCodUsuario - ID del usuario
	 *  
	 * @return Contexto 
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected IContexto[] invokeListUserParamTypes(final Integer intCodUsuario)
	throws FactoriaDatosException, 
	PersistenciaException,
	FawnaInvokerException 
	{
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();		
		datosEntrada.putDato("0", intCodUsuario, "BigDecimal");

		IContexto[] datos = invoker.invokeSNParaContextos("029", "050", datosEntrada, false);			
		return datos;		
	}

	/**
	 * Invocar a SN029055 para insertar un nuevo tipo de 
	 * parámetro de configuración
	 * 
	 * @param ctx - Contexto con los datos del nuevo tipo
	 *  
	 * @return Contexto 
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected IContexto[] invokeCreateConfigParamType(final IContexto ctx)
	throws FactoriaDatosException, 
	PersistenciaException,
	FawnaInvokerException 
	{
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();		
		datosEntrada.putDato("0", ctx, "Contexto");

		IContexto[] datos = invoker.invokeSNParaContextos("029", "055", datosEntrada, false);			
		return datos;		
	}

	/**
	 * Invocar a SN029056 para eliminar de la base de datos un tipo de 
	 * parámetro de configuración
	 * 
	 * @param intCodTipo - Identificador del tipo
	 *  
	 * @return Contexto 
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected IContexto[] invokeDeleteConfigParamType(final Integer intCodTipo)
	throws FactoriaDatosException, 
	PersistenciaException,
	FawnaInvokerException 
	{
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();		
		datosEntrada.putDato("0", intCodTipo, "BigDecimal");

		IContexto[] datos = invoker.invokeSNParaContextos("029", "056", datosEntrada, false);			
		return datos;		
	}
	
	/**
	 * Invocar a SN029057 con datos de entrada userId para saber si 
	 * le está permitida la ejecución del avatar al usuario
	 * 
	 * @param userid
	 *            - identificador de usuario
	 * 
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected String[] invokeCheckAllowedExecution(Integer userid)
			throws FactoriaDatosException, PersistenciaException,
			FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", userid, "BigDecimal");		

		IContexto[] datos = invoker.invokeSNParaContextos("029", "057",
				datosEntrada, false);			
		
		String isAllowedExecution = datos[0].getBigDecimal("FIONEG002010").toString();
		String timeTowait = datos[0].getBigDecimal("FIONEG002020").toString();		

		return new String[] { isAllowedExecution, timeTowait };
	}
	
	protected String invokeCheckWebPublished(Integer useridInt) throws FactoriaDatosException,
			PersistenciaException, FawnaInvokerException {
		// Invoke SN to start and register process
		SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();		
		datosEntrada.putDato("0", useridInt, "BigDecimal");
		IContexto[] datos = invoker.invokeSNParaContextos("029", "035",
				datosEntrada, false);

		String strIsPublished = "";
		if(datos != null){
			strIsPublished = datos[0].getString("FIONEG013030");
		}else{
			strIsPublished = "0";
		}
		

		return strIsPublished;
	}
	
	/**
	 * Método que permite customizar los atributos de un ModalAlert
	 * @param gestorDatos GestorDatosComponentes de fawna
	 * @param gestorEstados GestorEstadoComponentes de fawna
	 * @param modalAlertId Identificador del alert
	 * @param type Tipo:  confirm,info, warning, error o check
	 * @param alertTitle Título del alert
	 * @param title Mensaje del alert
	 * @param outputTextId Identificador de un elemento html dentro del alert
	 * @param outputText  Texto a introducir en el elemento html de identificador
	 * outputTextId
	 */
	protected void handleModalAlert(GestorDatosComponentes gestorDatos, GestorEstadoComponentes gestorEstados, 
			String modalAlertId, String type, String alertTitle, 
			String title, String outputTextId, String outputText) {
		gestorEstados.setPropiedad(modalAlertId, "type", type);
		gestorEstados.setPropiedad(modalAlertId, "alertTitle", alertTitle);
		gestorEstados.setPropiedad(modalAlertId, "title", title);
		gestorDatos.setValue(outputTextId, outputText);
		gestorEstados.openModalAlert(modalAlertId);				
	}
	
	/**
	 * Try to resolve the message. Return default message if no message was found.
	 * @param code the code to lookup up, such as 'calculator.noRateSet'. Users of
	 * this class are encouraged to base message names on the relevant fully
	 * qualified class name, thus avoiding conflict and ensuring maximum clarity.
	 * @param defaultMessage String to return if the lookup fails
	 * @return the resolved message if the lookup was successful;
	 * otherwise the default message passed as a parameter
	 */
	protected String getMessage(String code, String defaultMessage) {
		final Properties messages = new Properties("i18n.messages");
		try {
			return messages.getValueKey(code);
		} catch (MissingResourceException e) { }
		return defaultMessage;
	}
	
	/**
	 * Try to resolve the message. Return default message if no message was found.
	 * @param code the code to lookup up, such as 'calculator.noRateSet'. Users of
	 * this class are encouraged to base message names on the relevant fully
	 * qualified class name, thus avoiding conflict and ensuring maximum clarity.
	 * @param args array of arguments that will be filled in for params within
	 * the message (params look like "{0}", "{1,date}", "{2,time}" within a message),
	 * or <code>null</code> if none.
	 * @param defaultMessage String to return if the lookup fails
	 * @return the resolved message if the lookup was successful;
	 * otherwise the default message passed as a parameter
	 * @see java.text.MessageFormat
	 */
	protected final String getMessage(String code, Object[] args, String defaultMessage) {
		String source = getMessage(code, defaultMessage);
		return formatMessage(source, args);
	}
	
	/**
	 * Format the given message String, using cached MessageFormats.
	 * By default invoked for passed-in default messages, to resolve
	 * any argument placeholders found in them.
	 * @param msg the message to format
	 * @param args array of arguments that will be filled in for params within
	 * the message, or <code>null</code> if none
	 * @return the formatted message (with resolved arguments)
	 */
	protected final String formatMessage(String msg, Object[] args) {
		return formatMessage(msg, args, getDefaultLocale());
	}
	
	protected Locale getDefaultLocale()
	{
		Locale locale;
		try {
			ContextoSesion sessionContext = ContextoLocator.getInstance().getContextoSesion();
			Object value, o = sessionContext.getCtxValue(ConstantesSesion.ARCH_LOCALE);
			if (o instanceof IDato) {
				value = ((IDato) o).getValor();
			} else {
				value = o;
			}
			if (value instanceof Locale) {
				locale = (Locale) value;
			} else if (value instanceof String) {
				locale = StringUtils.parseLocaleString((String)value);
			} else {
				//Fallback to Spring locale
				locale = LocaleContextHolder.getLocale();
			}
		} catch (PersistenciaException e) {
			//Fallback to Spring locale
			locale = LocaleContextHolder.getLocale();
		}
		return locale;
	}	
	
	/**
	 * Format the given message String, using cached MessageFormats.
	 * By default invoked for passed-in default messages, to resolve
	 * any argument placeholders found in them.
	 * @param msg the message to format
	 * @param args array of arguments that will be filled in for params within
	 * the message, or <code>null</code> if none
	 * @param locale the Locale used for formatting
	 * @return the formatted message (with resolved arguments)
	 */
	protected String formatMessage(String msg, Object[] args, Locale locale) {
		if (msg == null || (args == null || args.length == 0)) {
			return msg;
		}
		MessageFormat messageFormat;
		synchronized (this.cachedMessageFormats) {
			messageFormat = this.cachedMessageFormats.get(msg);
			if (messageFormat == null) {
				try {
					messageFormat = new MessageFormat((msg != null ? msg : ""), locale);
				}
				catch (IllegalArgumentException ex) {
					// invalid message format - probably not intended for formatting,
					// rather using a message structure with no arguments involved
					// silently proceed with raw message
					messageFormat = INVALID_MESSAGE_FORMAT;
				}
				this.cachedMessageFormats.put(msg, messageFormat);
			}
		}
		if (messageFormat == INVALID_MESSAGE_FORMAT) {
			return msg;
		}
		synchronized (messageFormat) {
			return messageFormat.format(args);
		}
	}
	
	/**
	 * Método para la invocación del servicio que devuelve la lista de precios
	 * de los sparks de una configuración
	 * 
	 * @param intCodSpark
	 *            Identificador único del spark
	 * @param strNombreFichero
	 *            nombre del fichero xml con la configuración
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected IContexto[] invokeGetListPreciosProd(final Integer intCodUsuario,
			final String strNombreFichero) throws FactoriaDatosException,
			PersistenciaException, FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", intCodUsuario, "BigDecimal");
		if (strNombreFichero != null && !"".equals(strNombreFichero)) {
			datosEntrada.putDato("1", strNombreFichero, "String");
		}		

		IContexto[] salida = invoker.invokeSNParaContextos("029", "059",
				datosEntrada, false);

		return salida;

	}
	
	
	
	/**
	 * Método para la invocación del servicio que manda el email con la solicitud
	 * de un usuario de subir una configuración a producción
	 * 
	 * @param intCodSpark
	 *            Identificador único del spark
	 * @param strNombreFichero
	 *            nombre del fichero xml con la configuración
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected IContexto[] invokeUploadToProduction(final Integer intCodUsuario,
			final String strNombreFichero,Integer intNumUsuarios, Integer intIdUnidadTiempo,
			Integer intIdResolution, Integer intHighAvailability,
			Float floPrecioTotal) throws FactoriaDatosException,
			PersistenciaException, FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", intCodUsuario, "BigDecimal");
		if (strNombreFichero != null && !"".equals(strNombreFichero)) {
			datosEntrada.putDato("1", strNombreFichero, "String");
		}	
		datosEntrada.putDato("2", intNumUsuarios, "BigDecimal");
		datosEntrada.putDato("3", intIdUnidadTiempo, "BigDecimal");
		datosEntrada.putDato("4", intIdResolution, "BigDecimal");
		datosEntrada.putDato("5", intHighAvailability, "BigDecimal");
		datosEntrada.putDato("6", floPrecioTotal, "BigDecimal");

		IContexto[] salida = invoker.invokeSNParaContextos("029", "063",
				datosEntrada, false);

		return salida;

	}
	/**
	 * Método que permite hacer redirecciones a páginas fuera de nuestro
	 * flujo
	 * @param url Dirección completa a la que hacer la redirección
	 * 
	 * @return
	 */
	public String redirect(String url) {

        FacesContext ctx = FacesContext.getCurrentInstance();

        ExternalContext extContext = ctx.getExternalContext();        
        try {

            extContext.redirect(url);
        } catch (IOException ioe) {
            throw new FacesException(ioe);

        }
        return null;
 
    }
	/**
	 * Método que nos devuelve la URL actual, lo que incluye
	 * el identificador del flujo en el que estamos
	 * 
	 * @return Se devuelve la cadena que representa la URL
	 */
	public StringBuffer getRequestURL(){
		StringBuffer requestURL = null;
		
		FacesContext ctx = FacesContext.getCurrentInstance();

        ExternalContext extContext = ctx.getExternalContext();
        
        requestURL = ((HttpServletRequest) extContext.getRequest()).getRequestURL();
        
        return requestURL;
	}

	/**
	 * Método para la invocación del servicio que cambia el nombre
	 * de una configuración
	 * 
	 * @param intCodUsuario
	 *            Identificador único del usuario
	 * @param intCodConfig
	 *            Identificador de la configuración
	 * @param strName
	 *            Nombre de la configuración
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected void invokeSetConfigName(final Integer intCodUsuario, final Integer intCodConfig, final String strName)
		throws FactoriaDatosException, PersistenciaException,
		FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", intCodUsuario, "BigDecimal");
		datosEntrada.putDato("1", intCodConfig, "BigDecimal");
		datosEntrada.putDato("2", strName, "String");

		invoker.invokeSNParaColeccion("029", "067", datosEntrada, "itemKey", "itemLabel", false, false);
		
	}

	/**
	 * Método para la invocación del servicio que devuelve todas las
	 * configuraciones de un usuario
	 * 
	 * @param intCodUsuario
	 *            Identificador único del usuario
	 * @return
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected Map<String, String> invokeGetUsuarioConfigs(final Integer intCodUsuario)
		throws FactoriaDatosException, PersistenciaException,
		FawnaInvokerException {
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();
		datosEntrada.putDato("0", intCodUsuario, "BigDecimal");

		@SuppressWarnings("unchecked")
		Map<String, String> dato = invoker.invokeSNParaColeccion("029", "066", datosEntrada, "FIONEG028020", "FIONEG028030", false, false);
		
		return dato;
		
	}

}