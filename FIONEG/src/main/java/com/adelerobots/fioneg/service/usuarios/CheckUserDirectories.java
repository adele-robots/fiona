package com.adelerobots.fioneg.service.usuarios;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

public class CheckUserDirectories extends ServicioNegocio{
	
	private static FawnaLogHelper LOGGER = FawnaLogHelper.getLog(CheckUserDirectories.class);

	private static final int CTE_POSICION_TIRA_USER_ID = 0;
	
	public CheckUserDirectories(){
		super();
	}
	
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contextoEjecucion, IDatosEntradaTx datosEntrada) {
		
		LOGGER.info("Inicio Ejecucion del SN 029014: Comprobar sistema archivos usuario");
		
		final BigDecimal userId = datosEntrada.getDecimal(CTE_POSICION_TIRA_USER_ID);
		final String usersPath = Constantes.getUSERS_PATH();
		final String designerPath = Constantes.getDESIGNER_PATH();
		File fileToCheck=null;
		String command;
		
		//Obtener el objeto UsuarioC correspondiente para poder llamar al método getLstSparkC() que nos da sus sparks disponibles 
		
		final UsuariosManager managerusuario = ManagerFactory.getInstance().getUsuariosManager();
		UsuarioC userObj = null; 
		//Coger email de usuario y pasar a md5
		String mailmd5 = null;
		try {
			
			userObj = managerusuario.findById(userId);
			if (userObj==null) return new IContexto[0];
			
			mailmd5 = userObj.getAvatarBuilderUmd5();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> coresparks = Arrays.asList(Constantes.getCORE_SPARKS().split("\\s*,\\s*"));
					
		int numConfigs = Constantes.getConfigNum(userObj.getCuentaId());
		
		userObj = managerusuario.AddSparks(userObj, coresparks);
		
		//Recuperar sparks del usuario
		//List<SparkC> sparkList = userObj.getLstSparkc();
		List<SparkC> sparkList = managerusuario.getAllUserSparks(userObj.getCnUsuario());

		//comprobar que existe /datos/nfs/users/private/md5
		fileToCheck = new File(usersPath + "private/" + mailmd5);
		//y si no ejecutar mkdir
		if (!fileToCheck.exists()){
			command = "ln -s " + usersPath + "private/" + mailmd5 + "_0 " + usersPath + "private/" + mailmd5;
			try {
				Process checkFile = Runtime.getRuntime().exec(command);
				checkFile.waitFor();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(int i=0; i<numConfigs; i++) {
			//comprobar que existe /datos/nfs/users/private/md5_i
			fileToCheck = new File(usersPath + "private/" + mailmd5 + "_" + i);
			//y si no ejecutar mkdir
			if (!fileToCheck.exists()){
				command = "mkdir " + usersPath + "private/" + mailmd5 + "_" + i;
				try {
					Process checkFile = Runtime.getRuntime().exec(command);
					checkFile.waitFor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			//comprobar que existe /datos/nfs/users/private/md5/logs
			fileToCheck = new File(usersPath + "private/" + mailmd5 + "_" + i + "/logs");
			//y si no ejecutar mkdir
			if (!fileToCheck.exists()){
				command = "mkdir " + usersPath + "private/" + mailmd5 + "_" + i + "/logs";
				try {
					Process checkFile = Runtime.getRuntime().exec(command);
					checkFile.waitFor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//comprobar que existe /datos/nfs/users/private/md5/icons
			fileToCheck = new File(usersPath + "private/" + mailmd5 + "_" + i + "/icons");
			//y si no ejecutar mkdir
			if (!fileToCheck.exists()){
				if(i==0)
					command = "mkdir " + usersPath + "private/" + mailmd5 + "_0/icons";
				else
					command = "ln -s " + usersPath + "private/" + mailmd5 + "_0/icons " + usersPath + "private/" + mailmd5 + "_" + i + "/icons";
				try {
					Process checkFile = Runtime.getRuntime().exec(command);
					checkFile.waitFor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//comprobar que existe /datos/nfs/users/private/md5/banners
			fileToCheck = new File(usersPath + "private/" + mailmd5 + "_" + i + "/banners");
			//y si no ejecutar mkdir
			if (!fileToCheck.exists()){
				if(i==0)
					command = "mkdir " + usersPath + "private/" + mailmd5 + "_0/banners";
				else
					command = "ln -s " + usersPath + "private/" + mailmd5 + "_0/banners " + usersPath + "private/" + mailmd5 + "_" + i + "/banners";
				try {
					Process checkFile = Runtime.getRuntime().exec(command);
					checkFile.waitFor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//comprobar que existe /datos/nfs/users/private/md5/videos
			fileToCheck = new File(usersPath + "private/" + mailmd5 + "_" + i + "/videos");
			//y si no ejecutar mkdir
			if (!fileToCheck.exists()){
				if(i==0)
					command = "mkdir " + usersPath + "private/" + mailmd5 + "_0/videos";
				else
					command = "ln -s " + usersPath + "private/" + mailmd5 + "_0/videos " + usersPath + "private/" + mailmd5 + "_" + i + "/videos";
				try {
					Process checkFile = Runtime.getRuntime().exec(command);
					checkFile.waitFor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/*
			//comprobar que existe /datos/nfs/users/public/md5
			fileToCheck = new File(usersPath + "public/" + mailmd5);
			//y si no ejecutar mkdir
			if (!fileToCheck.exists()){
				command = "mkdir " + usersPath + "public/" + mailmd5;
				try {
					Process checkFile = Runtime.getRuntime().exec(command);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			*/
					
			//comprobar que existe out.png
			fileToCheck = new File(usersPath + "private/" + mailmd5 + "_" + i + "/out.png");
			//y si no ejecutar mkdir
			if (!fileToCheck.exists()){
				command = "cp " + usersPath + "/base/out.png " + usersPath + "/private/" + mailmd5 + "_" + i + "/";
				try {
					Process checkFile = Runtime.getRuntime().exec(command);
					checkFile.waitFor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			
			//comprobar que existe avatar.xml
			fileToCheck = new File(usersPath + "private/" + mailmd5 + "_" + i +"/avatar.xml");
			//y si no copiarlo de user base
			if (!fileToCheck.exists()){
				command = "cp " + usersPath + "base/avatar.xml " + usersPath + "private/" + mailmd5 + "_" + i;
				try {
					Process checkFile = Runtime.getRuntime().exec(command);
					checkFile.waitFor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//comprobar que existe generalConf.ini
			fileToCheck = new File("" + usersPath + "private/" + mailmd5 + "_" + i + "/generalConf.ini");
			//y si no generarlo a través del script
			if (!fileToCheck.exists()){
				if(i==0)
					command = "/datos/script/./generaini.sh " + mailmd5;
				else
					command = "ln -s " + usersPath + "private/" + mailmd5 + "_0/generalConf.ini " + usersPath + "private/" + mailmd5 + "_" + i + "/generalConf.ini";
				try {
					Process checkFile = Runtime.getRuntime().exec(command);
					checkFile.waitFor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//comprobar que existe /datos/nfs/users/private/md5/UploadedSparks
			fileToCheck = new File("" + usersPath + "private/" + mailmd5 + "_" + i + "/UploadedSparks");
			//y si no
			if (!fileToCheck.exists()){
				if(i==0)
					command = "mkdir " + usersPath + "private/" + mailmd5 + "_0/UploadedSparks";
				else
					command = "ln -s " + usersPath + "private/" + mailmd5 + "_0/UploadedSparks " + usersPath + "private/" + mailmd5 + "_" + i + "/UploadedSparks";
				try {
					Process checkFile = Runtime.getRuntime().exec(command);
					checkFile.waitFor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//comprobar que existen el simbólico ApplicationData 
			fileToCheck = new File("" + usersPath + "private/" + mailmd5 + "_" + i + "/ApplicationData");
			//y si no
			if (!fileToCheck.exists()){
				command = "ln -s /adele/dev/workspace/ApplicationData " + usersPath + "private/" + mailmd5 + "_" + i + "/ApplicationData";
				try {
					Process checkFile = Runtime.getRuntime().exec(command);
					checkFile.waitFor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//comprobar psisban-logger.properties y si no generar
			fileToCheck = new File("" + usersPath + "private/" + mailmd5 + "_" + i + "/psisban-logger.properties");
			//y si no
			if (!fileToCheck.exists()){
				if(i==0)
					command = "/datos/script/./makelogger_one.sh " + mailmd5;
				else
					command = "ln -s " + usersPath + "private/" + mailmd5 + "_0/psisban-logger.properties " + usersPath + "private/" + mailmd5 + "_" + i + "/psisban-logger.properties";
				try {
					Process checkFile = Runtime.getRuntime().exec(command);
					checkFile.waitFor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			fileToCheck = new File("" + usersPath + "private/" + mailmd5 + "_" + i + "/in.json");
			//y si no
			if (!fileToCheck.exists()){
				command = "cp /datos/nfs/users/base/in.json "+usersPath+"private/" + mailmd5 + "_" + i + "/in.json";
				try {
					Process checkFile = Runtime.getRuntime().exec(command);
					checkFile.waitFor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//Para cada Spark comprobar que existe su fichero de configuración
			Iterator<SparkC> iterSparks = sparkList.iterator();
			while (iterSparks.hasNext()){
				SparkC auxSpark = iterSparks.next();
				String sparkname = auxSpark.getStrNombre();
				fileToCheck = new File("" + usersPath + "private/" + mailmd5 + "_" + i + "/" + sparkname + ".ini");
				if (!fileToCheck.exists()){
					command = "cp " + usersPath + "base/initialconf/" + sparkname + ".ini " + usersPath + "private/" + mailmd5 + "_" + i + "/";
					try {
						Process checkFile = Runtime.getRuntime().exec(command);
						checkFile.waitFor();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		}
		
		//comprobar que existe /datos/nfs/users/private/md5.0jbpm
		fileToCheck = new File(usersPath + "private/" + mailmd5 + ".0jbpm");
		//y si no ejecutar mkdir
		if (!fileToCheck.exists()){
			command = "mkdir " + usersPath + "private/" + mailmd5 + ".0jbpm";
			try {
				Process checkFile = Runtime.getRuntime().exec(command);
				checkFile.waitFor();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		fileToCheck = new File("" + designerPath + "/" + mailmd5 + ".0jbpm");
		//y si no
		if (!fileToCheck.exists()){
			command = "mkdir " + designerPath + "/"+ mailmd5 + ".0jbpm";
			try {
				Process checkFile = Runtime.getRuntime().exec(command);
				checkFile.waitFor();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
			
		fileToCheck = new File("" + designerPath + "/"+ mailmd5 + ".0jbpm/" + mailmd5 + ".0jbpm.json");
		//y si no
		if (!fileToCheck.exists()){
			command = "ln -s " +usersPath+ "private/" +mailmd5+ ".0jbpm/" +mailmd5+ ".0jbpm.json " +designerPath+ "/"+mailmd5+ ".0jbpm/" +mailmd5+ ".0jbpm.json";
			try {
				Process checkFile = Runtime.getRuntime().exec(command);
				checkFile.waitFor();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		fileToCheck = new File("" + designerPath + "/" + mailmd5 + ".0jbpm/" + "src");
		//y si no
		if (!fileToCheck.exists()){
			command = "ln -s /datos/nfs/basedesigner/src " +designerPath+ "/"+mailmd5+ ".0jbpm/src";
			try {
				Process checkFile = Runtime.getRuntime().exec(command);
				checkFile.waitFor();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		fileToCheck = new File("" + designerPath + "/"+ mailmd5 + ".0jbpm/" + "icons");
		//y si no
		if (!fileToCheck.exists()){
			command = "ln -s /datos/nfs/basedesigner/icons " +designerPath+ "/"+mailmd5+ ".0jbpm/icons";
			try {
				Process checkFile = Runtime.getRuntime().exec(command);
				checkFile.waitFor();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		 
		fileToCheck = new File("" + designerPath + "/" + mailmd5 + ".0jbpm/" + "view");
		//y si no
		if (!fileToCheck.exists()){
			command = "ln -s /datos/nfs/basedesigner/view " +designerPath+ "/"+mailmd5+ ".0jbpm/view";
			try {
				Process checkFile = Runtime.getRuntime().exec(command);
				checkFile.waitFor();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		fileToCheck = new File("" + designerPath + "/"+ mailmd5 + ".0jbpm/" + "stencildata");
		//y si no
		if (!fileToCheck.exists()){
			command = "ln -s /datos/nfs/basedesigner/stencildata " +designerPath+ "/"+mailmd5+ ".0jbpm/stencildata";
			try {
				Process checkFile = Runtime.getRuntime().exec(command);
				checkFile.waitFor();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return new IContexto[0];
	}
	
	
}



