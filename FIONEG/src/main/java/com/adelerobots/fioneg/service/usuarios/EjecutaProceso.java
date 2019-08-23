package com.adelerobots.fioneg.service.usuarios;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;
import org.jdom.JDOMException;

import com.adelerobots.fioneg.context.ContextoPid;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.entity.usuariospark.UsuarioSparkC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.ProcesoManager;
import com.adelerobots.fioneg.manager.SparkManager;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;


public class EjecutaProceso extends ServicioNegocio{
	
	
	private static final int CTE_POSICION_TIRA_MD5 = 0;
	private static final int CTE_POSICION_TIRA_ID = 1;
	private static final int CTE_POSICION_TIRA_SESSION = 3;

	private static FawnaLogHelper LOGGER = FawnaLogHelper.getLog(EjecutaProceso.class);
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion arg0, IDatosEntradaTx datosEntrada) {
		
		String user_md5=datosEntrada.getString(CTE_POSICION_TIRA_MD5);
		Integer userId = null; {
			final BigDecimal value = datosEntrada.getDecimal(CTE_POSICION_TIRA_ID);
			if (value != null) userId = new Integer(value.intValue());
		}
		
		String session = datosEntrada.getString(CTE_POSICION_TIRA_SESSION);
		
		if (session == null) {
			session = "";
		}
		LOGGER.info("[EjecutaProceso] session("+session+")");
		
		IContexto[] salida = null;
		
		String[] command = {Constantes.getAVATAR_SCRIPT_PATH(), user_md5, session};
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("[EjecutaProceso] Empezando EjecutaProceso("+user_md5+")");
		}
		
		//comprobar que no hay otro proceso corriendo, mirando en tabla proceso usuario
			
		final ProcesoManager procmanager = ManagerFactory.getInstance().getProcesoManager();
		// INICIO - Control del número de procesos permitidos para un usuario y una máquina
		//Integer processqty = procmanager.checkExistingProcess(userId);
		Integer processqty = procmanager.getNumProcesosActivosPorUsuario(userId);
		// Obtenemos el número máximo de procesos para el usuario
		final UsuariosManager usuariosManager = ManagerFactory.getInstance().getUsuariosManager();
		UsuarioC usuario = usuariosManager.findById(userId);
		Integer maxProcessPerMachine = Integer.parseInt(Constantes.getMaxProcessPerMachine());
		String host = null;
		
		try {
		    InetAddress addr = InetAddress.getLocalHost();

		    // Get hostname
		   host = addr.getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		Integer totalProcess =  procmanager.getNumProcesosActivosPorMaquina(host);
		Integer numMaxUserProcess = usuario.getIntNumProcesos();
		
		// Comprobamos que no hayamos llegado ya al tope de procesos para esta máquina
		// y que tampoco se haya traspasado el límite de procesos por usuario
		//if (processqty>0){
		if (totalProcess >= maxProcessPerMachine ||  processqty>=numMaxUserProcess){
			salida = ContextoPid.rellenarContexto(99991, 99991);
			
			return salida;
		} 
		// FIN - Control del número de procesos permitidos para un usuario
		try {		
			// Comprobación sparks activos del usuario
			// 1. Obtener los sparks usados en la configuración a ejecutar
			SparkManager sparkManager = ManagerFactory.getInstance().getSparkManager();
			// TODO: parametrizar para cuando haya varias configuraciones
			List <SparkC> sparksConfiguracion = sparkManager.getSparksFromXml(userId, "avatar.xml");
			
			List <UsuarioSparkC> lstSparksUsuario = usuario.getLstUsuarioSparkC();
			
			
			Boolean todosActivos = sparkManager.isSetUserSparksActive(lstSparksUsuario,sparksConfiguracion);
			
			if(!todosActivos){
				// Alguno de los sparks de la configuración que el usuario intenta ejecutar
				// ya no está activo para él
				salida = ContextoPid.rellenarContexto(99999, 99999);
				
				return salida;
			}
			
			//FIN Comprobación sparks activos del usuario
			
			try{	
				Process avatar = Runtime.getRuntime().exec(command);
				TimeUnit.MILLISECONDS.sleep(300);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int pidfromps[] = getPidFromPs(user_md5);
			if(pidfromps[0]==0 || pidfromps[1]==0){
				pidfromps = getPidFromPs(user_md5);
				LOGGER.info("[EjecutaProceso] OJO!!!!!!!!!!!!! Hemos tenido que hacer un reintento para obtener los PIDS");
			}
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("[EjecutaProceso] PID recuperado por ps : " + pidfromps);
			}
			salida = ContextoPid.rellenarContexto(pidfromps[0], pidfromps[1]);
			
			return salida;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return new IContexto[0];
	}





	/**
	 * Obtiene el proceso avatar para un usuario determinado
	 * configurandolo para su inmediato uso
	 * 
	 * @param usermaild5 hash unico de usuario
	 * @return
	 * @see #getPROCESS_PATH()
	 * @see #getApplicationDataFolder(String)
	 */
	public static ProcessBuilder getProcessAvatar(String usermaild5) {
		ProcessBuilder builder;
		//Create reference to files we need
		final File applicationDataFolder = Constantes.getApplicationDataFolder(usermaild5);
		final File executable = new File(Constantes.getPROCESS_PATH(), "Run");
		final File config = new File(applicationDataFolder, Constantes.DEFAULT_AVATAR_FILENAME);
		//Build the process
		builder = new ProcessBuilder(executable.getPath(), config.getPath());
		//Set the application enviroment vars
		final Map<String, String> enviroment = builder.environment();
		enviroment.put("DISPLAY", ":0");
		enviroment.put("LD_LIBRARY_PATH", Constantes.getLD_LIBRARY_PATH());
		enviroment.put("PSISBAN_APPLICATION_DATA", applicationDataFolder.getPath());
		//Set the process working directory
		builder.directory(applicationDataFolder);
		
		return builder;
	}


	public static int getPidFromStream(Process avatar){
		BufferedReader br = null;
		int pid=0;
		
		try {
			br = new BufferedReader(new InputStreamReader(avatar.getInputStream()));
			
			String line = null;
			while ( (line = br.readLine()) != null) {
				if (line.startsWith("PIDrun:")) {
					String[] parseo;
					parseo = line.split(":");
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info("getting pid from stream: " + parseo[1]);
					}
					pid = Integer.parseInt(parseo[1]);
					break;
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			//Close resources quietlly
			if (br!=null) { try{br.close();} catch (Exception e) {} }
		}
		return pid;
	}


	//deprecated
	public static int getPid(Process p) {
		Field f;
		
		try {
			f = p.getClass().getDeclaredField("pid");
			f.setAccessible(true);
			int pid = (Integer) f.get(p);
			return pid;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}


	public static int[] getPidFromPs(String mailmd5)
	{
		final File applicationDataFolder = Constantes.getApplicationDataFolder(mailmd5);
		final File config = new File(applicationDataFolder, Constantes.DEFAULT_AVATAR_FILENAME);
		
		int[] pids;
		pids = new int[2];
		
		BufferedReader br = null;
		try {
			try{	
				Process psfe = Runtime.getRuntime().exec(new String[] { "bash", "-c", "ps -eo pid,ppid,cmd | grep " + mailmd5 });
				psfe.waitFor();
				br = new BufferedReader(new InputStreamReader(psfe.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
			String line = null;
			while ( (line = br.readLine()) != null) {
				String parseo[] = null;
				line = line.trim();
				parseo = line.split("\\s+", 3);
				boolean isRun;
				isRun = parseo[2].contains(config.getPath());
				//isRun = parseo[2].endsWith(config.getPath());
				//isRun = parseo[2].trim().endsWith(config.getPath());
				//isRun = parseo[2].contains(mailmd5+"/avatar.xml");
				if (isRun) {
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info("[EjecutaProceso] PID Run: " + parseo[0]);
					}
					pids[0] = Integer.parseInt(parseo[1]);
					pids[1] = Integer.parseInt(parseo[0]);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (LOGGER.isEnabledFor(Level.ERROR)) {
				LOGGER.error("Error en ejecutaproceso.", e);
			}
		}
		finally {
			//Close resources quietlly
			if (br!=null) { try{br.close();} catch (Exception e) {} }
		}
		return pids;
	}


}
