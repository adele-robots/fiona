package com.adelerobots.web.fiopre.listeners;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.adelerobots.web.fiopre.utilidades.ConfigUtils;
import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;

public class ErrConsolePollingListener extends AbstractUserProcessListener
{
	private static final long serialVersionUID = -8053109604517873119L;
	private List<String> lastConsoleLog = new ArrayList<String>();
	private int lastNumLines = 0;
	private Boolean isConsoleOff = false;

	protected final static Map<Object, Integer> LOG_LEVELS = new HashMap<Object, Integer>();
	static {
		LOG_LEVELS.put("OFF",   0);
		LOG_LEVELS.put("FATAL", 1);
		LOG_LEVELS.put("ERROR", 2);
		LOG_LEVELS.put("WARN",  3);
		LOG_LEVELS.put("DEBUG", 4);
		LOG_LEVELS.put("INFO",  5);
		LOG_LEVELS.put("ALL",   6);
	}


	public void procesarAjaxChangeListener(
			final GestorEstadoComponentes gestorEstados,
			final GestorDatosComponentes gestorDatos) 
	{
		//Get data
		
		final String usermaild5 = ContextUtils.getUserMailD5();
		
		int maxlines = 0; {
			String value = (String) gestorDatos.getValue("errconsoleForm:maxlines");
			if (value != null && !"".equals(value.trim())) {
				try {
					maxlines = Integer.parseInt(value);
				} catch (NumberFormatException e) {
					//On exception use default
					e.printStackTrace();
				}
			}
		}
		
		Integer logLevel = LOG_LEVELS.get(gestorDatos.getValue("loglevelselector"));
		
		//Validate data
		
		if (usermaild5 == null || "".equals(usermaild5.trim())) {
			gestorDatos.setValue("errconsolestream", "No user console stream");
			gestorEstados.setPropiedad("pollErrConsole", "reRender", "errconsolestream");
			return;
		}
		if (maxlines==0 && !isConsoleOff){
			isConsoleOff = true;
			// Si la consola está a OFF y no estaba previamente
			gestorDatos.setValue("errconsolestream", "Console turned off");
			gestorEstados.setPropiedad("pollErrConsole", "reRender", "errconsolestream");
			return;
		}else{
			// Si ya estaba a OFF evitamos renderizar continuamente			
			gestorEstados.setPropiedad("pollErrConsole", "reRender", null);
		}
		if (logLevel==null){
			gestorDatos.setValue("errconsolestream", "Please select log level");
			gestorEstados.setPropiedad("pollErrConsole", "reRender", "errconsolestream");
			return;
		}

		//Extract data
		final List<String> logLines;
		try {
			logLines = extractLogLines(usermaild5, logLevel);
		} catch (Exception e) {
			gestorDatos.setValue("errconsolestream", "Error reading console stream");
			return;
		}
		
		if (!lastConsoleLog.equals(logLines) || lastNumLines != maxlines){
			// Si hay nuevos mensajes o se cambia el nº de líneas a visualizar, actualizo la consola
			lastConsoleLog = logLines;
			lastNumLines = maxlines;
			isConsoleOff = false;
			//ToString
			final int lineCount = logLines.size();
			final StringBuffer buffer = new StringBuffer("");
			if (lineCount <= maxlines) {
				for (int i=0; i < lineCount; i++) {
					buffer.append(logLines.get(i));
				}
				gestorDatos.setValue("errconsolestream", 
						buffer.length() == 0 ? "No info to display" : buffer.toString());
			}
			else if(maxlines != 0) {
				for (int i=lineCount-maxlines-1; i<lineCount;i++) {
					buffer.append(logLines.get(i));
				}
				gestorDatos.setValue("errconsolestream", 
						buffer.length() == 0 ? "No info to display" : buffer.toString());
			}
			gestorEstados.setPropiedad("pollErrConsole", "reRender", "errconsolestream");
		}else{
			// Si es igual, no vuelvo a renderizar la consola entera en el polling			
			gestorEstados.setPropiedad("pollErrConsole", "reRender", null);
		}		
		
		//gestorDatos.setValue("errconsolestream", buffer.toString() + "<br/>");
	}


	protected List<String> extractLogLines(
			final String usermaild5, 
			final Integer logLvl)
		throws IOException
	{
		final List<String> lines = new ArrayList<String>();
		
		// fecha al inicio de la línea de logging
		final Pattern patt = Pattern.compile("(\\d+)(\\/)(\\d+)(\\/)(\\d+)", 
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		
		final File file = new File(ConfigUtils.getNfsUserLogsFolder(usermaild5), "errcon.log");
		BufferedReader reader = null;
		try 
		{
			reader = new BufferedReader(new FileReader(file));
			
			Integer lineLogLvl = 0;
			Boolean addline = false;
			
			String line;
			while((line=reader.readLine())!=null)
			{
				Matcher m = patt.matcher(line);
				String split[] = line.split(" ");
				
				if ( (m.find() && m.end()==8) && (split.length>4 && split!=null) 
						&& line.contains("[FIONA-logger]") )
				{
					lineLogLvl= LOG_LEVELS.get(split[3]);
					if(lineLogLvl!=null && lineLogLvl <= logLvl)
						addline = true;
				}
				
				m = patt.matcher(line);
				if ( (m.find() && m.end()==8) && (split.length>4 && split!=null) 
						&& !line.contains("[FIONA-logger]") )
					addline = false;
				if (addline) {
					line = line.replace("[FIONA-logger]", "");
					line = line.replace("LoggerError", "");
					line = line.replace("LoggerInfo", "");
					lines.add(line + "<br/>");
				}
			}//fin while

		}
		finally { 
			//Close resources quietlly
			if (reader!=null) { try{reader.close();} catch (Exception e) {} }
		}
		
		return lines;
	}

}
