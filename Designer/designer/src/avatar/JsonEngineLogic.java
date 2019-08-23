package avatar;

/*Damas y caballero bienvenidos al WebService JsonEngineLogic!!
 * Este webservice ha sido perpetrado por Luis D�az a partir de un servicio constru�do por Treelogic IDi sobre
 * la aplicaci�n designer que es una parte de una historia llamada Guvnor para generar gr�ficos Jbpm sobre lo que
 * se est� montando el AvatarBuilder (o como se est� llamando ahora)
 * 
 * En principio este webservice, que se llama a trav�s de un bot�n en el footer de designer que pone "save",
 * lo que hac�a era guardar el layout de los bloques y conexiones en un json. Y tambi�n lo cargaba al iniciar
 * la aplicaci�n.
 * 
 * La primera modificaci�n que se le hizo fue para que admitiese un par�metro "usuario" que permitiese diferenciar
 * lo que se estaba guardando o leyendo seg�n el usuario (par�metro que se incluye en la llamada al designer con 
 * &username=... 
 * 
 * Ojito que cuando se modifican los par�metros que admite cada una de las funciones registradas en el WS (read y write)
 * hay que cambiar el mapeo en el fichero JsonEngineLogic.wsdl (WebContent->wsdl) y server-config.wsdd
 * (WebContent->WEB-INF)
 * 
 * Pero claro, aparecieron m�s cosas que hacer a la vez que el usuario guardaba su esquema de componentes, con 
 * sus conexiones y dem�s. As� que JsonEngineLogic fue creciendo y creciendo cual gigante despu�s de ir a un 
 * buffet de sparks.
 * 
 * Lo siguiente que hubo que hacer fu� que cuando el usuario guardaba su esquema, deber�a tambi�n generar con esa
 * informaci�n un Xml de tipo ChusXml (www.w3c.org/CHUSXML/Schema) para poder pasarle al software que levanta 
 * los distintos componentes. 
 * 
 * Por �ltimo se implement� una funcionzuca que espero que sea temporal y no ver este c�digo comentado dentro de
 * un par de a�os con un t�pico "temporal mis co..., temporal" que lo que hace es convertir un svg en un archivo
 * de imagen.png con fondo transparente, lo que se pretende sirva para una de las pantalla de visualizaci�n, en
 * la que se ve el avatar ya movi�ndose y se para y se lanza, y con la intenci�n de que sirva como gu�a para
 * ver que sparks deber�a estar utilizando y dem�s. Lo del fondo transparente es porque exportar el fondo ser�a
 * un poco locura y aumentar�a la transferencia de datos, as� que transparente y si FAWNA quiere, con un z-index
 * de css se podr�a superponer a una imagen que hiciese de fondo y funcionando.
 * 
 * 
 * http://goo.gl/TiuwD */

/*
 * TODO: en caso de que ocurra un error por el cual no se guarde el xml, que no se guarde tampoco el in.json
 * */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang.WordUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.adelerobots.fiona.Connector;
import com.adelerobots.fiona.FionaComponent;
import com.adelerobots.fiona.FionaIface;
import com.adelerobots.fiona.Iface;
import com.adelerobots.fiona.MultipleIfaz;
import com.adelerobots.fiona.Union;

public class JsonEngineLogic {
	//Aqu� se guarda el png, en un directorio que tomcat pueda servir
	private static final String FILEFAW = "/datos/nfs/users/private/";
	private static final String FILEFAWpublic = "/datos/nfs/users/public/";
	private static final String USER_DATA_DIR = "/UserSparkData/";
	private static final String USER_FILES_DIR = "/UserFiles/";
	private static final String USER_SPARK_PROPERTY = "User_Spark_Data";
	
	//Se usa tanto para almacenar info en la lectura como para recorrer en la escritura, guarda los componentes
	static Vector <FionaComponent> componentes = new Vector<FionaComponent>(); 
	//Se usa s�lo en la escritura, ver package com.adelerobots.fiona
	static Vector <FionaIface> ifaces = new Vector<FionaIface>();
	//Este s�lo se usa para almacenar la info al leer
	static Vector <Iface> interfaces = new Vector<Iface>();
	//Almacena la informaci�n de elementos union y el siguiente de conectores
	static Vector <Union> uniones = new Vector<Union>();
	static Vector <Connector> conectores = new Vector<Connector>();
	
	static Vector <String> conIds = new Vector <String>();
	//Utilizado para casos de multi-instanciaci�n en los que un componente puede estar instanciado m�s de una vez
	//static Vector <String> compMultiNames = new Vector<String>();
	static Map<String, Integer> compMultiMap = new HashMap<String,Integer>();
	//Utilizamos este vector de esta clase para el tema de las interfaces que pueden tener más de una ifaz proveida
	static Vector<MultipleIfaz> multifaces = new Vector<MultipleIfaz>();
	
	//esta funci�n se llama desde view.js en designer para cargar al iniciar la config de componentes que hubiera
	public String read(String user){ //MOD luis muliuser
		InputStream stream=null;
		BufferedReader in=null;
		
		String contenidoJSON="";
						
		System.out.println("Valor leido de usuario en read: " + user);
				
		try {
			
			stream = new FileInputStream(getProcessJsonFile(user));
			InputStreamReader isr = new InputStreamReader(stream);
			
			if(stream!=null){
				
				in = new BufferedReader(isr);
					while(in.ready()){
						contenidoJSON += in.readLine();
					}
				in.close();
				stream.close();
			}
			
			
			} catch (IOException e) {
				System.err.println("Error: " + e.getMessage() + "::IOException en read()");
				return "";
			} catch (URISyntaxException e) {
				System.err.println("Error: " + e.getMessage()+ "::URISyntaxException en read()");
				return "";
			}finally{
				try {
					clearGlobalVariables();
					stream.close();
				} catch (Exception e) {
					
				}			
			}

		//contenidoJSON.replace("<", "");
		//contenidoJSON.replace(">", "");
		return contenidoJSON;
	}

	//Mapa para friendly names publisher y tal
	//este mapa, tendra como key, el nombre de la interfaz, y como value, el tipo
	static Map<String,String> typemap = new HashMap<String, String>();
			
	static{
		typemap.put("iText ",					"&lt;char*>");
		typemap.put("iAudio",					"&lt;AudioWrap*>");
		typemap.put("iVideoIn",					"&lt;Image*>");
		typemap.put("iVideoOut",				"&lt;OutgoingImage*>");
		typemap.put("IFlow&lt;char*&gt;",			"&lt;char*>");
		typemap.put("IFlow&lt;AudioWrap*&gt;",		"&lt;AudioWrap*>");
		typemap.put("IFlow&lt;Image*&gt;",			"&lt;Image*>");
		typemap.put("IFlow&lt;OutgoingImage*&gt;",	"&lt;OutgoingImage*>");
	}
	
	//Y esta se llama cuando se clicka publish y se guarda lo que tenga en ese momento
	//La gestión de errores es infernal
	public String write(String json, String user, String fsvg){
		BufferedWriter out=null;
		//boolean exception = false;
		String exception = "OK";
		System.out.println("Valor leído de usuario en write: " + user);
		try{
			out = new BufferedWriter(new FileWriter(getProcessJsonFile(user)));
			json = json.replace("<", "&lt;").replace(">","&gt;");
			out.write(json);
			out.close();
			saveAsPng(user, fsvg);
			writeXml(json,user);
			//getProperties(json,user);
			
		} catch (IOException e) {
			
			clearGlobalVariables();
			//exception = true;
			exception = "ErrorCode.1";
			try {
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
		} catch (URISyntaxException e) {
			
			clearGlobalVariables();
			//exception = true;
			exception = "ErrorCode.2";
			try {
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} catch (JSONException e) {
			
			clearGlobalVariables();
			//exception = true;
			exception = "ErrorCode.3";
			try {
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} catch (TranscoderException e) {
			
			clearGlobalVariables();
			//exception = true;
			exception = "ErrorCode.4";
			try {
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}finally{
			try {
				clearGlobalVariables();
				out.close();
				//if (exception) return false;
				//else return true;
				return exception;
			} catch (final Exception e) {
				e.printStackTrace();
			}			
		}
		return exception;
	}
	
	private void saveAsPng(String user, String fsvg) throws TranscoderException, IOException{
		OutputStream ostream = new FileOutputStream(FILEFAW + user + "/out.png");
		
		// Acciones para mantener el aspect-ratio
		//Rectangle aoi = new Rectangle(227,183,989,1062);
		// FIN Acciones para mantener el aspect-ratio
				
		PNGTranscoder t = new PNGTranscoder();
        t.addTranscodingHint(ImageTranscoder.KEY_MEDIA, "screen");
        //t.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, new Float(aoi.height));
        //t.addTranscodingHint(ImageTranscoder.KEY_WIDTH, new Float(aoi.width));
        
        // Acciones para mantener el aspect-ratio
        /*t.addTranscodingHint(ImageTranscoder.KEY_MAX_HEIGHT, new Float(aoi.height));
        t.addTranscodingHint(ImageTranscoder.KEY_MAX_WIDTH, new Float(aoi.width));
        t.addTranscodingHint(ImageTranscoder.KEY_AOI, aoi );*/
        // FIN Acciones para mantener el aspect-ratio
        
        TranscoderInput input = new TranscoderInput(new StringReader(fsvg));
        TranscoderOutput output = new TranscoderOutput(ostream);
        t.transcode(input, output);
        ostream.flush();
	}
	
	private void writeXml(String injson, String user) throws IOException, JSONException {
		
		System.out.println("[Badlogger]: Entrando en writeXml ");
		
		JSONArray entrys = null;
		JSONObject jsonin = new JSONObject(injson);
		entrys = jsonin.getJSONArray("childShapes");	
	
		for(int i=0;i< entrys.length();i++){
			JSONObject elemento;
			elemento = (JSONObject)entrys.get(i);
			String stencilId = elemento.getJSONObject("stencil").getString("id");
				
			//el orden de los siguientes ifs puede alterar el funcionamiento si no se define bien una nomenclatura de nombres
			if (stencilId.endsWith("Spark")) {
				int multi = procesaComponente(elemento);
				procesaConfiguracion(elemento, user, multi);
				}
			else if (stencilId.startsWith("I")) procesaInterfaz(elemento);
			else if (stencilId.equalsIgnoreCase("Union")) procesaUnion(elemento);
			else if (stencilId.startsWith("con")) procesaConector(elemento);
		}
	
	createIfaceObjects();
	getInterfaceConnections();
	detectMultipleRequired();
	writeComponentXml(user);
	//writeGeneralIni(user);
	clearGlobalVariables();
	printFionaIfaces();
	printFionaComponents();
	}
	
	private static void clearGlobalVariables() {
		// TODO Auto-generated method stub
		componentes.clear();
		ifaces.clear();
		interfaces.clear();
		uniones.clear();
		conIds.clear();
		conectores.clear();
		multifaces.clear();
		//compMultiNames.clear();
		compMultiMap.clear();
	}

	private File getProcessJsonFile(String user) throws URISyntaxException, IOException{
		File file = new File(FILEFAW + user + "/in.json");
		if(!file.exists())
			System.out.println("in.json does not exists, creating file");
			file.createNewFile();
		return file;
	}
	
	private static File getComponentConfFile(String user, String sparkname, int multi) 
	{
		System.out.println("Calling getComponentConfFile for user: " + user + " and spark: " + sparkname);
		File file;
		if (multi<1){
			file = new File(FILEFAW + user +"/" + sparkname + ".ini");
		}
		else{
			file = new File(FILEFAW + user +"/" + sparkname + multi + ".ini");
		}
		
//		if(!file.exists()){
//			System.out.println(sparkname + ".ini does not exists, creating file");
//			try {
//				file.createNewFile();
//			} catch (IOException e) {
//				System.err.println("Error: " + e.getMessage()+"::IOException en getComponentConfFile()");	
//			}
//		}
		
		if(file.exists()){
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else{
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
			
		return file;
		
	}

	private static File getComponentConfCoreFile(String sparkname){
		File file = new File("/datos/nfs/baseconfigs/" + sparkname + ".ini");
		
		return file;
	}
	
	static void writeComponentXml(String user) throws IOException{
		//crear file y/o bufferedwriter y tal y pas�rselo a las funciones siguientes
		FileWriter outXml = null;
		outXml = new FileWriter(FILEFAW + user +"/avatar.xml");
		BufferedWriter bw = new BufferedWriter(outXml);
		
		printFionaComponents();
		printFionaIfaces();
		
		writeXmlHeader(bw);
		writeXmlComponentDeclarations(bw);
		writeXmlComponentInstantiations(bw);
		writeXmlIfaceConnections(bw);
		bw.flush();
		bw.close();
		outXml.close();
	}
	
	static void writeXmlHeader(BufferedWriter bw) throws IOException{
		bw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		bw.newLine();
		bw.write("<ComponentNetwork xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"ComponentNetwork.xsd\">");
		bw.newLine();
		bw.newLine();
	}
	
	static void writeXmlComponentDeclarations(BufferedWriter bw) throws IOException{
		bw.write("<!-- COMPONENT DECLARATION -->");
		bw.newLine();
		bw.write("<ComponentDeclarations>");
		
		//se declara blankspark que es el agujero negro de la arquitectura
		bw.newLine();
		bw.write("<Component id=\"idBlankSpark\"  type=\"BlankSpark\" plugin=\"libBlankSpark.so\">");
		bw.newLine();
		bw.write("<ProvidedInterfaces>");
		bw.newLine();
		bw.write("</ProvidedInterfaces>");
		bw.newLine();
		bw.write("<RequiredInterfaces>");
		bw.newLine();
		bw.write("</RequiredInterfaces>");
		bw.newLine();
		bw.write("</Component>");
		bw.newLine();
		
		//declaro todos los componentes
		for (int i=0 ; i<componentes.size() ; i++){ //para cada componente
			if(componentes.get(i).getInstanceNumber()<1){
				bw.newLine();
				bw.write("<Component  id=\"id" + componentes.get(i).getName() + "\"  type=\"" + componentes.get(i).getName() + "\" plugin=\"lib"+componentes.get(i).getName() +".so\">");
				bw.newLine();
				bw.write("<ProvidedInterfaces>");
				bw.newLine();
				for (int j=0 ; j<componentes.get(i).getProvidedIfaces().size() ; j++){
					bw.write("<Interface name=\"" + componentes.get(i).getProvidedIfaces().get(j) + "\" />");
					bw.newLine();
				}
				bw.write("</ProvidedInterfaces>");
				bw.newLine();
				bw.write("<RequiredInterfaces>");
				bw.newLine();
				for (int j=0 ; j<componentes.get(i).getRequiredIfaces().size() ; j++){
					bw.write("<Interface name=\"" + componentes.get(i).getRequiredIfaces().get(j) + "\" />");
					bw.newLine();
				}
				bw.write("</RequiredInterfaces>");
				bw.newLine();
				bw.write("</Component>");
				bw.newLine();
			}
		}
		
		//y ahora declaro los publishers y los suscribers por multiproviding
		for (int i=0; i<multifaces.size(); i++){
			//instancio un publisher
			MultipleIfaz auxMulti = multifaces.get(i);
			
			bw.newLine();
			bw.write("<Component  id=\"idPublisher" + auxMulti.getOriginSpark() + "\"  type=\"PublisherSpark" + auxMulti.getType() + "\" plugin=\"lib"+ "PublisherSpark" +".so\">");
			bw.newLine();
			bw.write("<ProvidedInterfaces>");
			bw.newLine();
			
				//Una es la ifaz "real"
				bw.write("<Interface name=\"" + auxMulti.getIfazMulti() + "\" />");
				bw.newLine();
				//La otra es la ifaz de publisher adecuada al tipo de la "real"
				bw.write("<Interface name=\"" + auxMulti.getIfazPubSub() + "\" />");
				bw.newLine();
			
			bw.write("</ProvidedInterfaces>");
			bw.newLine();
			
			bw.write("<RequiredInterfaces>");
			bw.newLine();
			bw.write("</RequiredInterfaces>");
			bw.newLine();
			bw.write("</Component>");
			bw.newLine();
			
			//declaro n suscribers siendo n el número de componentes conectados que proveen la interfaz
			
			for (int j=0; j<multifaces.get(i).getDestinySparks().size(); j++){
				bw.newLine();
				bw.write("<Component  id=\"idSubscriber" + auxMulti.getOriginSpark() + auxMulti.getDestinySparks().get(j) +"\"  type=\"SubscriberSpark" + auxMulti.getType() + "\" plugin=\"lib"+ "SubscriberSpark" +".so\">");
				bw.newLine();
				bw.write("<ProvidedInterfaces>");
				bw.newLine();
				bw.write("</ProvidedInterfaces>");
				bw.newLine();
				
				bw.write("<RequiredInterfaces>");
				bw.newLine();

					//Una es la ifaz "real"
					bw.write("<Interface name=\"" + auxMulti.getIfazMulti() + "\" />");
					bw.newLine();
					//La otra es la ifaz de publisher adecuada al tipo de la "real"
					bw.write("<Interface name=\"" + auxMulti.getIfazPubSub() + "\" />");
					bw.newLine();
				
				bw.write("</RequiredInterfaces>");
				bw.newLine();
				bw.write("</Component>");
				bw.newLine();
				
			}
		}
		
		bw.write("</ComponentDeclarations>");
		bw.newLine();
		
	}
	
	static void writeXmlComponentInstantiations(BufferedWriter bw) throws IOException{
		bw.newLine();
		bw.write("<!-- APPLICATION DESCRIPTION -->");
		bw.newLine();
		bw.write("<ApplicationDescription applicationComponent=\"instanceRemoteCharacterEmbodiment3DSpark\" applicationConfig=\"/generalConf.ini\">");
		bw.newLine();
		bw.write("<!-- Paso 1/2: Instantiate components -->");
		bw.newLine();
		bw.newLine();
		bw.write("<ComponentInstances>");
		bw.newLine();
		// Instanciación del blankspark
		bw.write("<Instance instanceName=\"instanceBlankSpark\" componentID =\"idBlankSpark\"/>");
		bw.newLine();
		
		/*	Ñapa para el orden de instanciación:
		 * 	Existe un problema con el orden de instanciación de los Sparks, a la hora de instanciarse, pueden ocurrir problemas ya que ocurra que
		 * al realizar la instanciación, el spark que se esté instanciando necesite de otro que se haya instanciado previamente y si esto no ocurre así... pete que nos llevamos
		 * Esto yo (luis, bueno por el tono del comment se sabe quien soy) recuerdo haberlo hecho hace tiempo, pero se evaporó el código o yo que se que, lo milagroso es que no haya aparecido el problema otra vez hasta octubre 
		 * */

		// manera de abordar esto... 
		// quiero que remote sea el primero y poder definir algún otro 
		// que no dependa del numero de compon
		
		Vector <FionaComponent> componentesInstanciacion = new Vector<FionaComponent>();
		componentesInstanciacion = componentes;
		// Instanciación del scriptv8thread spark (de existir)
		for (int i=0; i<componentesInstanciacion.size(); i++){
			if (componentesInstanciacion.get(i).getName().equalsIgnoreCase("scriptv8threadspark")){
				if (componentesInstanciacion.get(i).getInstanceNumber()<1){
					bw.write("<Instance instanceName=\"instance"+ componentesInstanciacion.get(i).getName() +"\" componentID =\"id"+componentesInstanciacion.get(i).getName() +"\" configuration=\"/" + componentesInstanciacion.get(i).getName() +".ini\"/>");
					bw.newLine();
					componentesInstanciacion.remove(i);
					}else if (componentesInstanciacion.get(i).getInstanceNumber()>=1){
						for (int j=1; j<=componentesInstanciacion.get(i).getInstanceNumber(); j++){
							bw.write("<Instance instanceName=\"instance"+ componentesInstanciacion.get(i).getInstanceName() +"\" componentID =\"id"+componentesInstanciacion.get(i).getName() +"\" configuration=\"/" + componentesInstanciacion.get(i).getInstanceName() +".ini\"/>");
							bw.newLine();
							componentesInstanciacion.remove(i);
						}
					}
			}
		}
		
		// Instanciación del remotecharacterembodiment
		for (int i=0; i<componentesInstanciacion.size(); i++){
			if (componentesInstanciacion.get(i).getName().equalsIgnoreCase("remotecharacterembodiment3dspark")){
				if (componentesInstanciacion.get(i).getInstanceNumber()<1){
					bw.write("<Instance instanceName=\"instance"+ componentesInstanciacion.get(i).getName() +"\" componentID =\"id"+componentesInstanciacion.get(i).getName() +"\" configuration=\"/" + componentesInstanciacion.get(i).getName() +".ini\"/>");
					bw.newLine();
					componentesInstanciacion.remove(i);
					}else if (componentesInstanciacion.get(i).getInstanceNumber()>=1){
						for (int j=1; j<=componentesInstanciacion.get(i).getInstanceNumber(); j++){
							bw.write("<Instance instanceName=\"instance"+ componentesInstanciacion.get(i).getInstanceName() +"\" componentID =\"id"+componentesInstanciacion.get(i).getName() +"\" configuration=\"/" + componentesInstanciacion.get(i).getInstanceName() +".ini\"/>");
							bw.newLine();
							componentesInstanciacion.remove(i);
						}
					}
			}
		}
		// Instanciación del resto de componentes	
		for (int i=0; i<componentesInstanciacion.size(); i++){
			
			if (componentesInstanciacion.get(i).getInstanceNumber()<1){
			bw.write("<Instance instanceName=\"instance"+ componentesInstanciacion.get(i).getName() +"\" componentID =\"id"+componentesInstanciacion.get(i).getName() +"\" configuration=\"/" + componentesInstanciacion.get(i).getName() +".ini\"/>");
			bw.newLine();
			}else if (componentesInstanciacion.get(i).getInstanceNumber()>=1){
				for (int j=1; j<=componentesInstanciacion.get(i).getInstanceNumber(); j++){
					bw.write("<Instance instanceName=\"instance"+ componentesInstanciacion.get(i).getInstanceName() +"\" componentID =\"id"+componentesInstanciacion.get(i).getName() +"\" configuration=\"/" + componentesInstanciacion.get(i).getInstanceName() +".ini\"/>");
					bw.newLine();
				}
			}
		}

		//Instanciación de los sparks de publish y suscribe
		for (int i=0; i<multifaces.size(); i++){
		//instancio un publisher por multifaz
			MultipleIfaz auxMulti = multifaces.get(i);
			
			bw.write("<Instance instanceName=\"instancePublisher"+ auxMulti.getOriginSpark() +"\" componentID =\"idPublisher"+ auxMulti.getOriginSpark() +"\" />");
			bw.newLine();
								
			for (int j=0; j<multifaces.get(i).getDestinySparks().size(); j++){
				//instancio un subscriber por cada spark que se conecta
				bw.write("<Instance instanceName=\"instanceSubscriber"+ auxMulti.getOriginSpark() + auxMulti.getDestinySparks().get(j) +"\" componentID =\"idSubscriber"+ auxMulti.getOriginSpark() + auxMulti.getDestinySparks().get(j) +"\" />");
				bw.newLine();
				
			}
		}
	
		bw.write("</ComponentInstances>");
		bw.newLine();
		bw.newLine();

	}
	
	static void writeXmlIfaceConnections(BufferedWriter bw) throws IOException{
		//Aquí hay un cambio respecto a las otras dos funciones de escritura del xml, ya que el tema de la instanciación elimina otras conexiones
		//en declaracion e instanciación simplemente era añadir

		List<String> multifacenames = new ArrayList<String>();
		
		for (int i=0; i<multifaces.size(); i++){
			multifacenames.add(multifaces.get(i).getIfazMulti());
		}		
		
		
		bw.write("<!-- Paso 2/2: Connect interfaces -->");
		bw.newLine();
		bw.write("<InterfaceConnections>");
	    bw.newLine();
	       
	    
	    for (int i=0; i<ifaces.size(); i++){
	    	Boolean isoneofthemulti = false;
	    	
	    	for (int j=0; j<multifaces.size(); j++){	
	    		
	    		if (multifaces.get(j).getOriginSpark().equals(ifaces.get(i).getRequiredBy()) && multifacenames.get(j).equals(ifaces.get(i).getIfaceName()))
	    			isoneofthemulti = true;
	    		
	    	}
	    	
	    	if (!ifaces.get(i).getIfaceName().equalsIgnoreCase("IFrameEventPublisher") //si está conectada me da igual porque va por Connectall
	    			&& !ifaces.get(i).getIfaceName().equalsIgnoreCase("IAsyncFatalErrorHandler") //si está conectada me da igual porque va por Connectall
	    			&& !isoneofthemulti) //si es "one of the multi!!!" pera un poco que ya me la proceso luego
	    	{
 
		    	bw.write(" <Connect interface=\""+ifaces.get(i).getIfaceName() +"\" providedBy=\"instance"+ifaces.get(i).getProvidedBy() +"\" requiredBy=\"instance"+ ifaces.get(i).getRequiredBy() +"\" />");
		    	bw.newLine();
	    	}
	    }
	    
	    //conexiones por multiprovided
		for (int i=0; i<multifaces.size(); i++){
			MultipleIfaz auxMulti = multifaces.get(i);
		    //una conexión por cada publisher de la ifaz real provided by publisher required by originspark 	 
			bw.write(" <Connect interface=\""+ auxMulti.getIfazMulti() +"\" providedBy=\"instancePublisher"+ auxMulti.getOriginSpark() +"\" requiredBy=\"instance"+ auxMulti.getOriginSpark() +"\" />");
			bw.newLine();
			//dos conexiones por cada subscriber
			for (int j=0; j<auxMulti.getDestinySparks().size(); j++){
				bw.write(" <Connect interface=\""+ auxMulti.getIfazPubSub() +"\" providedBy=\"instancePublisher"+ auxMulti.getOriginSpark() +"\" requiredBy=\"instanceSubscriber"+ auxMulti.getOriginSpark() + auxMulti.getDestinySparks().get(j) +"\" />");
			    //1.-ifaz publish provided by publisher required by subscriber
				bw.newLine();
			    //2.-ifaz real provided by destinyspark required by subscriber				
				bw.write(" <Connect interface=\""+ auxMulti.getIfazMulti() +"\" providedBy=\"instance"+ auxMulti.getDestinySparks().get(j) +"\" requiredBy=\"instanceSubscriber"+ auxMulti.getOriginSpark() + auxMulti.getDestinySparks().get(j) +"\" />");
				bw.newLine();
			}
		}		

		bw.write("<ConnectAll interface=\"IAsyncFatalErrorHandler\" providedBy=\"instanceRemoteCharacterEmbodiment3DSpark\"/>");
	    bw.newLine();
	    bw.write("<ConnectAll interface=\"IFrameEventPublisher\" providedBy=\"instanceRemoteCharacterEmbodiment3DSpark\"/>");
	    bw.newLine();
	    bw.write("</InterfaceConnections>");
	    bw.newLine();
	    bw.write("</ApplicationDescription>");
	    bw.newLine();
	    bw.write("</ComponentNetwork>");
	    bw.newLine();
	}
	
	//esta función es auxiliar y sirve para debugging. y la siguiente lo mismo
	static void printFionaIfaces(){
		for (int i=0; i<ifaces.size(); i++){
			System.out.println("Interfaz: " + ifaces.get(i).getIfaceName());
			System.out.println("Provided by: " + ifaces.get(i).getProvidedBy());
			System.out.println("Required by: " + ifaces.get(i).getRequiredBy());
			System.out.println("conId: " + ifaces.get(i).getConId());
		}
	}
	
	static void printFionaComponents(){
		for (int i=0; i<componentes.size(); i++){
			System.out.println("Nombre del componente: " + componentes.get(i).getName());
			System.out.println("Outgoing Id's: " + componentes.get(i).getOutgoingIds());
			System.out.println("Provided Ifaces: " + componentes.get(i).getProvidedIfaces());
			System.out.println("Required Ifaces: " + componentes.get(i).getRequiredIfaces());
			System.out.println("Multiple instantiation number: " + componentes.get(i).getInstanceNumber());
		}
	}
	
	static void createIfaceObjects(){
		for (int i=0; i<interfaces.size();i++)
		{
			Iface aux = new Iface();
			FionaIface aux2 = new FionaIface();
			aux = interfaces.get(i);
			if (aux.getName().endsWith("P")){
				String auxname = aux.getName();
				auxname = auxname.substring(0, auxname.length() - 1); //quita �ltima letra, checkear
				aux2.setIfaceName(auxname);
				ifaces.addElement(aux2);
			}
		}
	}
	
	static void procesaInterfaz(JSONObject elementoInterfaz) throws JSONException {
		Iface aux = new Iface();
		
		String ifaceName = elementoInterfaz.getJSONObject("stencil").getString("id");
		String ifaceId = elementoInterfaz.getString("resourceId");
		JSONArray outjsa = elementoInterfaz.getJSONArray("outgoing");

		aux.setId(ifaceId);
		aux.setName(ifaceName);
		
		//Nombre de la interfaz para saber si es provided o required
		if (ifaceName.endsWith("P")) aux.setProvided(true);
		else aux.setProvided(false);
					
		for(int i=0;i< outjsa.length();i++){
			JSONObject outg;
			outg = (JSONObject)outjsa.get(i);
			String outgoingId = outg.getString("resourceId");
			aux.setOutgoing(outgoingId);
		}
		
		interfaces.add(aux);
	}
	
	static void procesaUnion(JSONObject elementoUnion) throws JSONException {
		Union aux = new Union();
		
		String unionId = elementoUnion.getString("resourceId");
		JSONArray outjsa = elementoUnion.getJSONArray("outgoing");
						
		//System.out.println("Id del componente: " + compId); //id del componente
		aux.setId(unionId);

		//get outgoing
		JSONObject outg;

		outg = (JSONObject)outjsa.get(0);
		String outgoingId = outg.getString("resourceId");
		//System.out.println("Salidas hacia ids: " + outgoingId); //id de conexiones de salida
		aux.setOutgoing(outgoingId);
		

		uniones.add(aux);
	}
	
	static void procesaConector(JSONObject elementoConector) throws JSONException {
		Connector aux = new Connector();
		
		String conectorId = elementoConector.getString("resourceId");
		JSONArray outjsa = elementoConector.getJSONArray("outgoing");
						
		//System.out.println("Id del componente: " + compId); //id del componente
		aux.setId(conectorId);
						
		//get outgoing
		JSONObject outg;

		outg = (JSONObject)outjsa.get(0);
		String outgoingId = outg.getString("resourceId");
		//System.out.println("Salidas hacia ids: " + outgoingId); //id de conexiones de salida
		aux.setOutgoing(outgoingId);

		conectores.add(aux);
	}
	
	static int procesaComponente(JSONObject elementoComponente) throws JSONException{
		FionaComponent aux = new FionaComponent();
	
		String compId = elementoComponente.getString("resourceId");
		JSONArray outjsa = elementoComponente.getJSONArray("outgoing");
		String compName = elementoComponente.getJSONObject("stencil").getString("id");
		
		int multi = 0;
		
		//System.out.println("Nombre del componente: " + compName); //nombre del componente
		aux.setName(compName);
		//System.out.println("Id del componente: " + compId); //id del componente
		aux.setId(compId);
				
		//get outgoing, OJO hay que cambiarlo que de momento s�lo lee uno
		for(int i=0;i< outjsa.length();i++){
			JSONObject outg;

			outg = (JSONObject)outjsa.get(i);
			String outgoingId = outg.getString("resourceId");
			aux.addToOutgoingIds(outgoingId);
			
		}
		
		JSONObject ifaces = elementoComponente.getJSONObject("properties");
		
		String ifacestr = ifaces.toString();
		String[] parseo = ifacestr.split(",");
		for(int i=0; i<parseo.length;i++){
			String[] parseo2 = parseo[i].split(":");
			for (int j=0; j<parseo2.length;j++){
				parseo2[j] = parseo2[j].replace("\""," ");
				parseo2[j] = parseo2[j].replace("{"," ");
				parseo2[j] = parseo2[j].replace("}"," ");
				parseo2[j] = parseo2[j].trim();
			}
			if (parseo2[0].startsWith("required")){
				aux.addToRequiredIfaces(parseo2[1]);
			}else if (parseo2[0].startsWith("provided")){
				aux.addToProvidedIfaces(parseo2[1]);
			}
		}
		
		
			if (compMultiMap.containsKey(compName)) {
				multi=0;
				multi = compMultiMap.get(compName) + 1;
				compMultiMap.put(compName, multi);
				aux.setInstanceNumber(multi-1);
				compName += multi;
			}else
				compMultiMap.put(compName, 1);
	
		
//		if (compMultiNames.contains(compName)){
//			while(compMultiNames.contains(compName)){
//				aux.setInstanceNumber(aux.getInstanceNumber()+1);
//				compName=compName + aux.getInstanceNumber();
//			}
//		}
//		else compMultiNames.addElement(compName);
	
	aux.setInstanceName(compName);
	componentes.add(aux);
	return multi;
	}

	/*Oh lord de los "fors" for i=now * ; i>=eternidad; i++ que tus ojos no ardan por la presencia del siguiente 
	anidamiento mort�fero, y si tus manos se sienten firmes ad�ntrate para optimizar la siguiente iteraci�n
	del demonio en la b�squeda de aquellos puntos donde poder insertar el grito de BREAK! introd�cete en la mazmorra 
	para evitar ejecutar instrucciones de manera innecesaria.*/
	static void getInterfaceConnections(){ //OJO, HACER ITERACI�N DE MEJORA BUSCANDO BREAKS EN FORS	
		for (int i=0; i<componentes.size(); i++){//para cada componente
			for (int j=0; j<componentes.get(i).getOutgoingIds().size(); j++){//para cada outgoingid
				String idObjetivo = componentes.get(i).getOutgoingIds().get(j); 
				String conId=null;
				//buscar idObjetivo entre el vector de uniones
				for (int k=0; k<uniones.size(); k++){
					if (uniones.get(k).getId().equalsIgnoreCase(idObjetivo)) {
						//OJO, que estoy cambiando la variable de comprobación, funciona pero no está bien así
						idObjetivo=uniones.get(k).getOutgoing();
						//break; //comprobar que se pueda poner
					}
				}//fin for k (union)
				for (int k=0; k<interfaces.size(); k++){
					if (interfaces.get(k).getId().equalsIgnoreCase(idObjetivo)){//hasta aqu� se que el componente apunta hacia esta ifaz
						if (interfaces.get(k).isProvided()){ //si es provided, tengo que seguir hasta el conector, el outgoing del conector es conId
							for (int m=0; m<conectores.size(); m++){
								if(conectores.get(m).getId().equalsIgnoreCase(interfaces.get(k).getOutgoing())){
									conId=conectores.get(m).getOutgoing();//conId
								}
							}
						}else if (!interfaces.get(k).isProvided()){ //si es required la id de esta ifaz es conId
							conId=interfaces.get(k).getId();//conId
						}
						for (int m=0; m<ifaces.size(); m++){
							if ((interfaces.get(k).getName().contains(ifaces.get(m).getIfaceName()))&&((ifaces.get(m).getConId().equalsIgnoreCase(conId))||(ifaces.get(m).getConId().equalsIgnoreCase("unknown")))){
								if(interfaces.get(k).isProvided() && !interfaces.get(k).isFilled()){
									ifaces.get(m).setProvidedBy(componentes.get(i).getInstanceName());
									interfaces.get(k).setFilled(true);
									if(ifaces.get(m).getConId().equalsIgnoreCase("unknown"))ifaces.get(m).setConId(conId);
								}else if (!interfaces.get(k).isProvided() && !interfaces.get(k).isFilled()){
									ifaces.get(m).setRequiredBy(componentes.get(i).getInstanceName());
									interfaces.get(k).setFilled(true);
									if(ifaces.get(m).getConId().equalsIgnoreCase("unknown"))ifaces.get(m).setConId(conId);
								}
							}
						}
					}
				}
			}//fin for j (outg)	 	
		}//fin for i (comp)
	}
	
	//escribe el fichero *.ini del componente dado en la carpeta del usuario
	static void procesaConfiguracion(JSONObject elementoComponente, String user, int multi) throws JSONException, IOException{

		//de momento sólo se procesa la configuración editable por el usuario, habría que añadir también los parámetros de configuración no editables
		//por el usuario pero que necesitan ir en los inis. 
		
		//opción A: leer de un archivo y copiar
		//opción B: leer de DB
		
		String compName = elementoComponente.getJSONObject("stencil").getString("id");
		JSONObject properties = elementoComponente.getJSONObject("properties");
		
		File sparkIniFile = getComponentConfFile(user, compName, multi);
		FileWriter outIni = null;
		outIni = new FileWriter(sparkIniFile, true);
		BufferedWriter bw = new BufferedWriter(outIni);
		
		File sparkCoreIniFile = getComponentConfCoreFile(compName);
		FileReader baseIni = null;
		baseIni = new FileReader(sparkCoreIniFile);
		BufferedReader br = new BufferedReader(baseIni);
		
		//en las properties pueden estar ifaces que empiezan por provided o required
		//y tengo que sacar tambi�n los tipos de datos existentes bool int float string list#
		
		String keys[] = JSONObject.getNames(properties);
		List<String> propertykeys = new ArrayList<String>();
		//System.out.println(keys);
		for (int i=0; i<keys.length; i++){
			if (!(keys[i].startsWith("provided") || keys[i].startsWith("required")|| keys[i].startsWith("name")|| keys[i].startsWith("bgcolor"))){
				 propertykeys.add(keys[i]);
			}
		}
		for (int i=0; i<propertykeys.size(); i++){
			System.out.println(propertykeys.get(i).toString() + properties.getString(propertykeys.get(i).toString()));
			String propname[] = propertykeys.get(i).toString().split("_");
			//String propertyname = propertykeys.get(i).toString().substring(propname[0].length() + propname[1].length() + 2, propertykeys.get(i).toString().length());
			
			String propertyname= "";
			
			//TODO: esto es una ñapa para salir con la beta 0.5, el problema viene de que in.json tiene todo su contenido en minúsculas
			//incluyendo los parámetros de configuración que son case sensitive para libconfigcpp
			
			//convierto a uppercase la primera del 2 hacia adelante
			for (int j=2; j<propname.length; j++){
				propname[j] = WordUtils.capitalize(propname[j]);
				propertyname+=propname[j] + "_";
			}
								
			propertyname = propertyname.substring(0, propertyname.length() - 1);
			//concateno todas del 2 hacia adelante poniendo un _ entre cada una
						
			//elimino el _ final
			/*
			if(propertykeys.get(i).toString().startsWith("string") || propertykeys.get(i).toString().startsWith("list")){
				
				bw.write(propertyname + "=\"" + properties.getString(propertykeys.get(i).toString()) + "\";");
				bw.newLine();
			}else{
				bw.write(propertyname+ "=" + properties.getString(propertykeys.get(i).toString()) + ";");
				bw.newLine();
			}*/
			
			//FIXÑAPA PARA 0.5B Y BOOLEANS
			if(  (propertykeys.get(i).toString().startsWith("string") || propertykeys.get(i).toString().startsWith("file") || propertykeys.get(i).toString().startsWith("list")) && (!properties.getString(propertykeys.get(i).toString()).equalsIgnoreCase("true") && !properties.getString(propertykeys.get(i).toString()).equalsIgnoreCase("false")) ){

				bw.write(propertyname + "=\"" + properties.getString(propertykeys.get(i).toString()) + "\";");
				bw.newLine();
			}else{
				bw.write(propertyname+ "=" + properties.getString(propertykeys.get(i).toString()) + ";");
				bw.newLine();
			}
			
		}
		
		String line = br.readLine();
		while (line!=null){
			bw.write(line);
			bw.newLine();
			line = br.readLine();
		}
		
		// [INICIO] Necesidad de propiedad que indique el path donde estarán los ficheros subidos por un usuario (User_Spark_Data)
		// Añadir como última propiedad en el archivo .ini del usuario, el path donde se encontrarán (de existir alguno) los archivos
		// de configuración añadidos por el usuario		
		line = USER_SPARK_PROPERTY + "=\"" + FILEFAW + user + USER_DATA_DIR + compName + USER_FILES_DIR + "\";";
		bw.write(line);
		bw.newLine();
		// [FIN]  Necesidad de propiedad que indique el path donde estarán los ficheros subidos por un usuario (User_Spark_Data)
		
		br.close();
		bw.flush();
		bw.close();
		
	}

	static void writeGeneralIni(String user){
		
		File userFile = getComponentConfFile(user, "general", 0);
		FileWriter userIni;
		try {
			userIni = new FileWriter(userFile, true);
			BufferedWriter bw = new BufferedWriter(userIni);
			
			File coreFile = getComponentConfCoreFile("general", user);
			FileReader coreIni = new FileReader(coreFile);
			BufferedReader br = new BufferedReader (coreIni);
			//copiar
			
			String line = br.readLine();
			while (line!=null){
				bw.write(line);
				bw.newLine();
				line = br.readLine();
			}
			
			br.close();
			bw.flush();
			bw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			clearGlobalVariables();
		}

		

	}

	private static File getComponentConfCoreFile(String general, String user) {
		File file = new File(FILEFAW + user +"coreconf/generalConf.ini");
		
		return file;
	}
	
	private static void detectMultipleRequired(){
		//esta función cogerá el vector ifaces y sacará las ifaces multiproveidas y entre qué sparks conecta
		//recorro ifaces, buscando parejas interfaz/sparkquelorequiere, en la primera ocurrencia los añado en algún sitio
		//en la segunda ocurrencia los añado en otro sitio contando cuántas veces van
		List<String> sparkIfazReq = new ArrayList<String>();
		List<String[]> detectedMulti = new ArrayList<String[]>();
			
		for (int i=0; i<ifaces.size(); i++){
			MultipleIfaz aux = new MultipleIfaz();
			String[] pareja = {"",""};
			pareja[0] = ifaces.get(i).getRequiredBy(); //saco quien la requiere
			pareja[1] = ifaces.get(i).getIfaceName(); // y el nombre de la ifaz
			String comp = pareja[0] + pareja[1]; //apaño de la lista de strings
			if (!sparkIfazReq.contains(comp)){ //si no está en la lista del apaño
				sparkIfazReq.add(comp); //la añado
				aux.setOriginSpark(pareja[0]); //pongo el spark del que parte la required
				aux.setIfazMulti(pareja[1]); //pongo el nombre de la ifaz multiplicada
				List<String> auxlist = aux.getDestinySparks(); //obtengo la lista de las sparks que la implementan
				auxlist.add(ifaces.get(i).getProvidedBy()); //le añado esta
				aux.setDestinySparks(auxlist); //y la setteo
				multifaces.add(aux); //y añado el objeto a multifaces
			}
			else{
				for (int j=0; j<multifaces.size(); j++){
					if(multifaces.get(j).getOriginSpark().equalsIgnoreCase(pareja[0]) 
					&& multifaces.get(j).getIfazMulti().equalsIgnoreCase(pareja[1])) {
						multifaces.get(j).setMulti(true);
						List<String> auxlist = multifaces.get(j).getDestinySparks();
						auxlist.add(ifaces.get(i).getProvidedBy());
						multifaces.get(j).setDestinySparks(auxlist);
					}	
				}
			}
		}
		//limpiar multifaces 
		for (int i=multifaces.size()-1; i>=0; i--){
			if (!multifaces.get(i).getMulti()) multifaces.remove(i);
		}
		
		//sacar el type de la interfaz y la interfaz para el publisher y suscriber asociada
		for (int i=0; i<multifaces.size(); i++){
			//obtengo el nombre de la interfaz original
			String ifazorig = multifaces.get(i).getIfazMulti();
			
			String type = typemap.get(ifazorig);
			
			if (type==null)
				type ="&lt;int>";
				
			multifaces.get(i).setType(type);
			
				
			//interfaz asociada para publish subscribe
			multifaces.get(i).setIfazPubSub("IPublisher" + type);
		}
	}
	
	//static void, solucionar movidas con las urls de streaming
}
