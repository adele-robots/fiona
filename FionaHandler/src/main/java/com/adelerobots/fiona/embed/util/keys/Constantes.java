package com.adelerobots.fiona.embed.util.keys;




public class Constantes {
	
	public static final String USER_DETAIL_SERVICE_ID = "SN029003";
	public static final String CHECK_WEBPUBLISHED_SERVICE_ID = "SN029035";
	public static final String START_PROCESS_SERVICE_ID = "SN029009";
	public static final String POLLING_SERVICE_ID = "SN029012";
	public static final String CHAT_MESSAGES_SERVICE_ID = "SN029013";
	public static final String CHAT_MESSAGES_RECEIVE_SERVICE_ID = "SN029042";
	public static final String ROOM_PREPARE_SERVICE_ID = "SN029037";
	public static final String OPERATORS_AVAILABLE_SERVICE_ID = "SN026027";
	public static final String NEW_CONNECTION_SERVICE_ID = "SN026028";
	public static final String DELETE_CONNECTION_SERVICE_ID = "SN026035";
	
	public static final String HOST_ADDRESS_PROPERTY = "embed.config.address";
	
	private String hostAddress = null;
	private String userDetailserviceUrl = null;
	private String checkWebPublishedServiceUrl = null;
	private String startProcessServiceUrl = null;
	private String pollingServiceUrl = null;
	private String chatMessagesServiceUrl = null;
	private String chatMessagesReceiveServiceUrl = null;
	private String roomPrepareServiceUrl = null;
	private String operatorsAvailableServiceUrl = null;
	private String newConnectionServiceUrl = null;
	private String deleteConnectionServiceUrl = null;
	
		
	// Instancia unica de configuracion
	private static Constantes cfg = null;
	
	public Constantes() {		
				
		Configuracion conf = Configuracion.getInstance();
		
		hostAddress = conf.getProperty(HOST_ADDRESS_PROPERTY);
		userDetailserviceUrl = hostAddress + USER_DETAIL_SERVICE_ID;
		checkWebPublishedServiceUrl = hostAddress + CHECK_WEBPUBLISHED_SERVICE_ID;
		startProcessServiceUrl = hostAddress + START_PROCESS_SERVICE_ID;
		pollingServiceUrl = hostAddress + POLLING_SERVICE_ID;
		chatMessagesServiceUrl = hostAddress + CHAT_MESSAGES_SERVICE_ID;
		chatMessagesReceiveServiceUrl = hostAddress + CHAT_MESSAGES_RECEIVE_SERVICE_ID;
		roomPrepareServiceUrl = hostAddress + ROOM_PREPARE_SERVICE_ID;
		operatorsAvailableServiceUrl = hostAddress + OPERATORS_AVAILABLE_SERVICE_ID;
		newConnectionServiceUrl = hostAddress + NEW_CONNECTION_SERVICE_ID;
		deleteConnectionServiceUrl = hostAddress + DELETE_CONNECTION_SERVICE_ID;

	}
	
	/**
	 * Obtiene una instancia a la configuración de la aplicación
	 *	
	 */
	public static Constantes getInstance(){
		// creamos una única instancia del la configuración
		if (cfg == null) {
			Constantes ccfg = null;
			ccfg = new Constantes();
			cfg = ccfg;
		}
		return cfg;
	}

	public String getHostAddress() {
		return hostAddress;
	}

	public String getUserDetailserviceUrl() {
		return userDetailserviceUrl;
	}
	
	public String getCheckWebPublishedServiceUrl() {
		return checkWebPublishedServiceUrl;
	}

	public String getStartProcessServiceUrl() {
		return startProcessServiceUrl;
	}

	public String getPollingServiceUrl() {
		return pollingServiceUrl;
	}

	public String getChatMessagesServiceUrl() {
		return chatMessagesServiceUrl;
	}	

	public String getChatMessagesReceiveServiceUrl() {
		return chatMessagesReceiveServiceUrl;
	}

	public String getRoomPrepareServiceUrl() {
		return roomPrepareServiceUrl;
	}

	public String getOperatorsAvailableServiceUrl() {
		return operatorsAvailableServiceUrl;
	}
	
	public String getNewConnectionServiceUrl() {
		return newConnectionServiceUrl;
	}
	
	public String getDeleteConnectionServiceUrl() {
		return deleteConnectionServiceUrl;
	}
	
	
	

}
