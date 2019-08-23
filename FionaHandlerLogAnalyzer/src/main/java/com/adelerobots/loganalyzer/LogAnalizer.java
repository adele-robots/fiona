package com.adelerobots.loganalyzer;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.adelerobots.loganalyzer.model.Answer;
import com.adelerobots.loganalyzer.model.Avatar;
import com.adelerobots.loganalyzer.model.Conection;

import com.adelerobots.loganalyzer.persistence.AnalizerDB;
import com.adelerobots.loganalyzer.persistence.AnswerDAO;
import com.adelerobots.loganalyzer.persistence.AvatarDAO;
import com.adelerobots.loganalyzer.persistence.ConectionDAO;
import com.mkyong.analysis.location.mode.GetLocation;
import com.mkyong.analysis.location.mode.ServerLocation;

/**
 * Clase que leerá diariamente los logs de FionaHandler y los analizará con el
 * fin de generar una serie de estadísticas de acceso y error
 * 
 * @author x
 * 
 */
public class LogAnalizer {

	private static final String LINEAS_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE log4j:eventSet PUBLIC \"-//APACHE//DTD LOG4J 1.2//ES\" \"http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/xml/doc-files/log4j.dtd\" [<!ENTITY data SYSTEM \"Log_FioHandler\">]>\n<log4j:eventSet version=\"1.2\" xmlns:log4j=\"http://jakarta.apache.org/log4j/\">\n";
	private static final String LINEAS_FIN_XML = "\n</log4j:eventSet>";

	private static final String fichero_log = "Log_FIOHandler.xml";
	private static final String carpeta_log = "/JAVA/log/FionaHandler/";
	private static final String carpeta_dialog = "/datos/nfs/users/private/";

	private static HashMap<String, Avatar> avatares = new HashMap<String, Avatar>();
	private static Date now = new Date();
	private static Date yesterday = null;
	private static Date dateArg = null;
	
	public static AnalizerDB con = new AnalizerDB();

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		
		try {
			try {
				if(args.length > 0) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					dateArg = sdf.parse(args[0]);
				}
			} catch (ParseException e) {
				System.out.println(args[1] + " is not a valid date.");
			}
			
			System.setProperty("user.dir", carpeta_log);
			
			now.setHours(0);
			now.setMinutes(0);
			now.setSeconds(0);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			yesterday = calendar.getTime();
	
			if(dateArg == null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				File fichero = new File(carpeta_log, fichero_log);
		
				sdf = new SimpleDateFormat(".yyyy-MM-dd");
				System.out.println("Fecha hoy: " + sdf.format(new Date()) + "\tFecha "
						+ fichero_log + ": " + sdf.format(fichero.lastModified()));
				if (sdf.format(new Date()).equals(sdf.format(fichero.lastModified()))) {
					fichero = previousLog();
					System.out.println("Como coinciden se coge el anterior: "
							+ fichero.getName());
				}
				if (!isProcessed(fichero)) {
		
					createValidXMLFile(fichero.getAbsolutePath());
		
					cargarXML(fichero);
		
					mostrarInfoRecuperada();
					exportarDB();
				}
			}
			else {
				SimpleDateFormat sdf = new SimpleDateFormat(".yyyy-MM-dd");
				File fichero = new File(carpeta_log, fichero_log + sdf.format(dateArg));
				if (!isProcessed(fichero)) {
		
					createValidXMLFile(fichero.getAbsolutePath());
				}
				cargarXML(fichero);
		
				mostrarInfoRecuperada();
				exportarDB();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			con.desconectar();
		}
	}

	/**
	 * Método que exporta los datos del modelo a la base de datos analytics
	 */
	private static void exportarDB() {
		//Iterator<String> iterator = avatares.keySet().iterator();
		Integer contAvatar = 0;
		Integer contConexion = 0;
		Integer contRespuesta = 0;

		//while (iterator.hasNext()) {
		for(Avatar avatar : avatares.values()) {
			//String key = iterator.next().toString();

			//Avatar avatar = avatares.get(key);
			// Añadir avatar
			if (AvatarDAO.getAvatar(avatar.getMd5()) == null) {
				AvatarDAO.aniadirAvatar(avatar);
				contAvatar++;
			}

			else
				System.out.println("Avatar existente: " + avatar.getMd5());
			for (Conection c : avatar.getConexiones()) {
				// Añadir conexion
				Integer id = ConectionDAO.aniadirConexion(c);
				contConexion++;
				for (Answer a : c.getRespuestas()) {
					// Añadir respuesta
					AnswerDAO.aniadirRespuesta(id, a);
					contRespuesta++;
				}

			}

		}
		System.out.println(contAvatar + " avatares añadidos");
		System.out.println(contConexion + " conexiones añadidas");
		System.out.println(contRespuesta + " respuestas añadidas");
		
	}

	/**
	 * Método que muestra por pantalla la información del modelo procesada y
	 * obtenida
	 */
	private static void mostrarInfoRecuperada() {

		System.out.println("\n\tNum.Total Avatares: " + avatares.size() + "\n");

		//Iterator<String> iterator = avatares.keySet().iterator();

		//while (iterator.hasNext()) {
		for(Avatar avatar: avatares.values()) {
			//String key = iterator.next().toString();

			//System.out.println(avatares.get(key).toString());
			System.out.println(avatar.toString());
		}

		System.out.println("********************************************");

	}

	/**
	 * Método que devuelve el último archivo XML que no se cargó debido a que
	 * había algún avatar ejecutándose durante el cambio de día
	 * 
	 */
	private static File previousLog() {
		SimpleDateFormat sdf = new SimpleDateFormat(".yyyy-MM-dd");
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		File fichero = new File(carpeta_log, fichero_log.concat(
				sdf.format(cal.getTime())));
		while (!fichero.exists()) {
			cal.add(Calendar.DAY_OF_YEAR, -1);
			fichero = new File(carpeta_log, fichero_log.concat(
					sdf.format(cal.getTime())));
		}
		return fichero;

	}

	/**
	 * Método que carga el fichero de log en formato XML y lo analiza para
	 * realizar un conteo de logins con éxito y errores por sobrepasar el número
	 * máximo de usuarios concurrentes. También se almacenarán las distintas
	 * horas a las que los usuarios han intentado ejecutar un avatar
	 * 
	 * @param file
	 *            Archivo de log a analizar (en formato XML)
	 */
	@SuppressWarnings("unchecked")
	private static void cargarXML(File file) {
		SAXBuilder builder = new SAXBuilder();

		// Se crea el documento a través del archivo
		Document document;
		try {
			document = (Document) builder.build(file);
			// Se obtiene la raiz
			Element rootNode = document.getRootElement();

			List<Element> rootChilds = rootNode.getChildren();
			String PID = "";
			for (int i = 0; i < rootChilds.size(); i++) {
				Element child = rootChilds.get(i);
				String nodeText = child.getValue();

				
				 if (nodeText.contains("PIDScript:")) {
					 PID = nodeText.substring( nodeText.indexOf("PIDProceso:") + 11, nodeText.length());
					 PID = PID.substring(0, PID.indexOf("\n"));
				 
				 } else // Buscar líneas de init
				
				if (nodeText.contains("[INIT]")) {
					conexionAceptada(child, nodeText, PID);
					PID = "";
					
				} else if (nodeText.contains("[MAXCONCURRENT]")) {
					conexionRechazada(child, nodeText, PID);
					PID = "";
					
				}/*if (nodeText.contains("[IP_ADDRESS]")) {
					conexionIp(child, nodeText, PID);
					PID = "";
					
				}*/
			}

		} catch (JDOMException jde) {
			jde.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}
	
	
	/**
	 * Método que procesa la información del log si detecta que la conexión fue
	 * rechazada al superar el numero de usuarios concurrentes
	 * 
	 * @param child
	 * @param nodeText
	 * @param pidProceso
	 */
	private static void conexionRechazada(Element child, String nodeText,
			String pidProceso) {
		// Buscar líneas de usuarios concurrentes superados
		String md5 = nodeText.substring(nodeText.indexOf("[MAXCONCURRENT][") + 16);
		md5 = md5.substring(0, md5.indexOf("]"));
		String ip = nodeText.substring(nodeText.indexOf("[MAXCONCURRENT][") + 50);
		ip = ip.substring(0, ip.indexOf("]"));
		
		Long timestampConexion = Long.parseLong(((String) child
				.getAttributeValue("timestamp")));

		Date fechaConexion = new Date(timestampConexion);
		Avatar avatar;

		if (!avatares.containsKey(md5)) {
			avatar = new Avatar(md5);
		} else {

			avatar = avatares.get(md5);
		}

		java.sql.Timestamp sqlDate = new java.sql.Timestamp(fechaConexion.getTime());
		Conection conection = new Conection(ip, md5, sqlDate,
				Conection.RECHAZADO);
		
		ServerLocation location = GetLocation.getLocation(ip);
		conection.setServerLocation(location);
		//conection.setCiudad(location.getCity());
		//conection.setPais(location.getCountryName());
		conection.setCity(location.getCity());
		conection.setCountry(location.getCountryCode());
		conection.setRegion(location.getRegionName());

		avatar.aniadirConexion(conection);
		// avatar.incNumRechazadas();
		avatares.put(md5, avatar);
	}

	/**
	 * Método que procesa la información de una conexión que pudo ser aceptada,
	 * y en la que el usuario trató con el avatar, por lo que debe analizar
	 * también su dialogo
	 * 
	 * 
	 * @param child
	 * @param nodeText
	 * @param pidProceso
	 */
	private static void conexionAceptada(Element child, String nodeText,
			String pidProceso) {
		
		if("ERROR".equals((String) child.getAttributeValue("level")))
				return;
		
		String md5 = nodeText.substring(nodeText.indexOf("[INIT][") + 7);
		md5 = md5.substring(0, md5.indexOf("]"));
		
		String ip = nodeText.substring(nodeText.indexOf("[INIT][") + 41);
		ip = ip.substring(0, ip.indexOf("]"));
		
		ServerLocation location = new ServerLocation();
		try {
			location = GetLocation.getLocation(ip);
		}
		catch(Exception e) {
			//System.out.println("Error getting location");
		}
		//System.out.println(location);
		Long timestampConexion = Long.parseLong(((String) child
				.getAttributeValue("timestamp")));
		Date fechaConexion = new Date(timestampConexion);

		Avatar avatar;

		if (!avatares.containsKey(md5)) {
			avatar = new Avatar(md5);
		} else {
			avatar = avatares.get(md5);
		}
		java.sql.Timestamp sqlDate = new java.sql.Timestamp(fechaConexion.getTime());
		Conection conection = new Conection(ip, md5, sqlDate,
				Conection.ACEPTADO);

		String dialog = obtenerDialogo(md5, pidProceso);
		conection.setDialogo(dialog);
		//conection.setCiudad(location.getCity());
		//conection.setPais(location.getCountryName());
		conection.setCity(location.getCity());
		conection.setCountry(location.getCountryCode());
		conection.setRegion(location.getRegionName());
			
		if (!"".equals(dialog)) {
			parsearDialogo(conection);
		}
		
		conection.setServerLocation(location);

		avatar.aniadirConexion(conection);
		// avatar.incNumAceptadas();
		avatares.put(md5, avatar);

	}

	/**
	 * Método que analiza el dialogo de una conversación y determina si las
	 * respuestas del avatar facilitaron información o fueron fallidas
	 * 
	 * @param conection
	 */
	private static void parsearDialogo(Conection conection) {
		String dialogo = conection.getDialogo();
		String[] dial = dialogo.split("\n");
		String topic = "";
		String questionText = "";
		for (int i = 0; i < dial.length; i++) {
			if(dial[i].length() < 20)
				continue;
			String frase = dial[i].substring(20);
			if(frase.startsWith("User: ")) {
				questionText = frase.substring(6);
				continue;
			}
			if(frase.startsWith("Avatar: ")) {
				if(questionText.isEmpty())
					continue;
				try {
						SimpleDateFormat formatoDeFecha = new SimpleDateFormat(
								"yy-MM-dd HH:mm:ss");
						Date fechaRespuesta = formatoDeFecha.parse(dial[i]
								.substring(0, 17));
						//fechaRespuesta.setYear(fechaRespuesta.getYear() + 2000);
						String respuesta = dial[i].substring(28);

						if (respuesta.startsWith("<topic=\"")) {
							String aux = respuesta.substring(8, respuesta.indexOf('"', 8));

							if (!"".equals(aux)) {
								topic = aux;
								// si el topic es .config, es que fue una frase por defecto, asi que la
								// marcamos como no respondida
								if(".config".equals(topic))
									topic = "error";
							}
							respuesta = respuesta.substring(respuesta.indexOf('>') + 1);
						}
						if(respuesta.contains("<kill>")) {
							respuesta = respuesta.replace("<kill>", "");
						}
						if(respuesta.isEmpty())
							continue;
						java.sql.Timestamp sqlDate = new java.sql.Timestamp(fechaRespuesta.getTime());
						Answer answer = new Answer(sqlDate, respuesta, questionText);
						questionText = "";
						if ("error".equals(topic)) {
							answer.setTopic(topic);
							conection.incNumFallidas();
							answer.setStatus(Answer.FALLIDA);
						} else {

							String nextTopic = null;
							if(i+2 < dial.length) {
								if(dial[i+2].length() > 27) {
										try {
												formatoDeFecha.parse(dial[i+2].substring(0, 17));
												String aux_respuesta = dial[i+2].substring(28);
		
												if (aux_respuesta.startsWith("<topic=\"")) {
													String aux = aux_respuesta.substring(8, aux_respuesta.indexOf('"', 8));
		
													if (!"".equals(aux)) {
														nextTopic = aux;
													}
												}
										} catch (ParseException e) {
											e.printStackTrace();
										}
								}
							}
							if("badAnswer".equals(nextTopic)) {
								answer.setTopic(nextTopic);
								conection.incNumIncorrectas();
								answer.setStatus(Answer.INCORRECTA);
							}
							else {
								/*String[] tema = topic.split("\"");
	
								answer.setTopic(tema[1]);*/
								answer.setTopic(topic);
	
								conection.incNumFacilitadas();
								answer.setStatus(Answer.FACILITADA);
							}
						}

						conection.aniadirRespuestas(answer);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			//topic = "";
		}

	}

	/**
	 * Método que recoge el contenido del dialogo de un fichero según el
	 * PIDProceso de dicho dialogo
	 * 
	 * @param pidProceso
	 * @return
	 */
	private static String obtenerDialogo(String md5, String pidProceso) {
		String dialogo = "";

		try {

			File directorio = new File(carpeta_dialog, md5.concat("/UserSparkData/"));
			if(!directorio.exists())
				directorio.mkdirs();
			File[] ficheros; /* = directorio.listFiles(); */

			// Filtramos los ficheros del directorio para solo procesas los
			// ficheros _chat.log
			FilenameFilter logFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (! name.endsWith(".log")) {
						return false;
					}
					Date fechaLog = new Date();
					fechaLog.setTime(Long.valueOf(name.split("_")[0]) * 1000);
					if(dateArg == null) {
						if(fechaLog.before(yesterday) || fechaLog.after(now))
							return false;
					}
					else {
						if(fechaLog.before(dateArg))
							return false;
					}
					return true;
				}
			};

			ficheros = directorio.listFiles(logFilter);

			for (int x = 0; x < ficheros.length; x++) {

				String fichero_dialog = ficheros[x].getName();

				String[] pid = fichero_dialog.split("_");
				if (pid[2].equals(pidProceso)) {
					File fichero = new File(directorio, fichero_dialog);

					BufferedReader br = new BufferedReader(new FileReader(
							fichero));
					String linea = br.readLine();

					while (linea != null) {
						dialogo += linea + "\n";

						linea = br.readLine();

					}
					br.close();
					
					break;

				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dialogo;
	}

	/**
	 * Método que nos permite borrar un fichero
	 * 
	 * @param file
	 *            Archivo a eliminar
	 */
	private static void deleteFile(File file) {
		try {

			if (file.exists()) {
				file.delete();
				// System.out.println("Fichero Borrado con éxito");
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * Método que nos permite escribir un archivo XML válido a partir del
	 * contenido del log XML generado con log4j
	 * 
	 * @param fileName
	 *            Nombre del fichero de log
	 */
	private static void createValidXMLFile(String fileName) {

		// Primero eliminamos los ^M
		String command = carpeta_log.concat("cleanXML.sh " + fileName);
		Process checkFile;
		try {
			checkFile = Runtime.getRuntime().exec(command);
			checkFile.waitFor();
		} catch (IOException e) {
			System.out.println("Falló la ejecución del script cleanXML.sh");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("Falló la ejecución del script cleanXML.sh");
			e.printStackTrace();
		}

		File oldFile = new File(fileName);

		String newFileName = oldFile.getParent() + "//auxLog" + ".xml";
		// Creamos fichero nuevo
		File newFile = new File(newFileName);

		try {
			if (newFile.exists())
				newFile.delete();
			newFile.createNewFile();

			// Comprobamos que existe el fichero
			if (oldFile.exists()) {
				BufferedReader in = new BufferedReader(new FileReader(oldFile));
				FileOutputStream fileOutput = new FileOutputStream(newFileName,
						true);
				BufferedOutputStream out = new BufferedOutputStream(fileOutput);

				// Línea inicial
				out.write(LINEAS_XML.getBytes());

				String linea;
				while ((linea = in.readLine()) != null) {
					linea += "\n";
					out.write(linea.getBytes());
				}
				// Escribir al final del fichero el cierre del xml
				out.write(LINEAS_FIN_XML.getBytes());
				out.close();

				in.close();
				// Borramos el fichero inicial
				deleteFile(oldFile);
				// Le damos al fichero auxiliar el nombre del fichero de log
				// con el XML inválido
				newFile.renameTo(oldFile);

			} else {
				System.out.println("Fichero No Existe");
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	/**
	 * Método que nos permite determinar si un archivo es un XML válido
	 * 
	 * @param file
	 *            Fichero del xml
	 */
	private static boolean isProcessed(File file) {
		try {
			SAXBuilder builder = new SAXBuilder();
			builder.build(file);
			System.out.println("El fichero " + file.getName()
					+ " ya está procesado");
			return true;
		} catch (JDOMException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

	}
}
