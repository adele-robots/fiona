package com.adelerobots;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IClient;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.stream.IServerStream;
import org.slf4j.Logger;
import org.slf4j.impl.StaticLoggerBinder;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.selector.ContextSelector;



public class Application extends MultiThreadedApplicationAdapter {

	//private static Logger log1 = Red5LoggerFactory.getLogger( Application.class, "FionaRed5");
	 public static Logger log1;
	    static {
	        ContextSelector selector = StaticLoggerBinder.getSingleton().getContextSelector();
	        LoggerContext ctx = selector.getLoggerContext("FionaRed5");
	        log1 = ctx.getLogger(Application.class);
	    }
	
	private IScope appScope;

	private IServerStream serverStream;
	
	//Configuraci�n de la conexi�n con la base de datos MySQL
	private static Connection getConnection() throws IOException, SQLException
	{
	   Properties props = new Properties();
	   // Looks for the file 'database.properties' in red5/conf
	   FileInputStream in = new FileInputStream(System.getProperty("red5.config_root") + "/database.properties");
	   log1.info("Config root directory : " + System.getProperty("red5.config_root"));
	   props.load(in);
	   in.close();
	   
	   // It will load the driver String from properties
	   String drivers = props.getProperty("jdbc.drivers");
	   if(drivers != null) {
		//System.setProperty("jdbc.drivers", drivers); // If drivers are not set in properties, set them now.
		//Class.forName("com.mysql.jdbc.Driver").newInstance();
		   try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			log1.error("No se encuentra el driver mysql");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
	   
	   String url = props.getProperty("jdbc.url");
	   String username = props.getProperty("jdbc.username");
	   String password = props.getProperty("jdbc.password");
	   
	    

	   return DriverManager.getConnection(url, username, password);

	
	}

	IScope getDestinationScope(String[] path){
		
		
		
		return appScope;
	}
	
	//m�todo que comprueba la existencia del par username/pass en la base de datos de usuarios
	private boolean authenticate(String email, String pw){
		 Statement stmt = null;
		 int count=0; //ojo problemas de concurrencia
		 //String email = username.replace("(at)", "@");
		 String sqlCmd="SELECT * FROM usuario WHERE DC_Email='"+ email +"' and DC_Password = '"+ pw +"'";
		 
		 try{
			Connection conn = getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sqlCmd); //comprobar en tabla auth_red5 que existe el par username-pw
			 
			while (rs.next()) 
			{ 
			    log1.info(rs.getInt(1) + " " + rs.getString(4)+ " " + rs.getString(5)); 
				count++;
			}
					 
			stmt.close();
			stmt = null;
				
			conn.close();
			conn = null;
						
			} catch (IOException ex) {
	            log.error("SQLException: " + ex.getMessage());
			} catch (SQLException ex) {
				log1.error("SQLException: " + ex.getMessage());
				log1.error("SQLState: " + ex.getSQLState());
				log1.error("VendorError: " + ex.getErrorCode());
			} 
			
			if (count==1) return true;
			else return false;
	 }
	//Escribe un registro en base de datos (pruebas ddbb)
	private void logMessage(String msg) {
	        Statement stmt = null;

	        log1.info("Ejecutando logMessage");
	        
	        try {
	        	   Connection conn = getConnection(); // Get connection from properties file
		
				// Do something with the Connection
				stmt = conn.createStatement();
				stmt.execute(
				    "INSERT INTO pruebas (campo1, campo2) "
				    + "VALUES('" + msg+ "'," + "'" + msg +"2' )");
				stmt.close();
				stmt = null;
				
				conn.close();
				conn = null;
						
	        } catch (IOException ex) {
	            log.error("SQLException: " + ex.getMessage());
			} catch (SQLException ex) {
				log1.error("SQLException: " + ex.getMessage());
				log1.error("SQLState: " + ex.getSQLState());
				log1.error("VendorError: " + ex.getErrorCode());
			}               
		} // end logMessage
	
 /*   public void FCSubscribe (String streamName)
    {
            
            ObjectMap<Object,Object> param=new ObjectMap<Object,Object>();
            param.put("code", "NetStream.Play.Start");//forStream
            param.put("forStream", streamName);
            ((IServiceCapableConnection)Red5.getConnectionLocal()).invoke("onFCSubscribe", new Object[] { param });
    	
    	System.out.println(streamName);
            
    }
*/	
	
	/** {@inheritDoc} */
	//Se ejecuta al inicializar el servidor cuando se inicializa la aplicaci�n
    @Override
	public boolean appStart(IScope app) {
	    super.appStart(app);
		log1.info("[FIONAR5][INFO]Fiona appStart (app)" + app);
		appScope = app;
		//logMessage("appStart");
		return true;
	}

    /** {@inheritDoc} */
    @Override
	public boolean roomJoin(IClient client, IScope room) {
    	log1.info("[FIONAR5][INFO] roomJoin (room)" + room + "(client)" + client);
    return super.roomJoin(client,room);
    }
    
    /** {@inheritDoc} */
    @Override
	public boolean roomConnect(IConnection conn, Object[] params ) {
    	  	
    	log1.info("[FIONAR5][INFO] roomConnect (conn)" + conn);
    	
    	IScope destino = conn.getScope(); //obtenemos el scope al que se dirige la conexi�n
    	String ruta = destino.getContextPath(); //obtenemos el nombre de la room en la forma "/Room" 
    	ruta=ruta.replace("/"," ");
    	ruta=ruta.trim(); //obtenemos un string "Room"
    	
    	String username = params[0].toString();//Obtenemos el nombre de usuario
		String password = params[1].toString();//y el password
		String username_md5;
		log1.info("[FIONAR5][INFO]Checking User " + username+" "+ password + " for ruta: " + ruta);

		//TODO: la ruta no contendr� el mail sino el md5 del mail, sin embargo los flashplayers enviar�n como par�metros
		//el mail sin codificar y el passwordmd5 para poder autenticar contra la bd sin embargo ahora a la hora de comparar la ruta
		//hay que comparar el md5 del mail con la ruta
		//String usermail = username.replace("(at)", "@");
		username_md5 = md5(username);
		
		log1.info("[FIONAR5][DEBUG] username_md5: " + username_md5);
		
		//log1.info("[FIONAR5][DEBUG] username: " + username + " password: " + password +  " username_md5: " + username_md5 + " ruta: " +ruta);
		
		// Si creamos las rooms dinámicas nos sobra la parte de la comprobación ¿¿¿¿¿¿NO???????
		// donde se pide que la room coincida con el md5 del usuario!!!
		// Sólo queremos autenticarnos
		//if( (authenticate(username,password)&&username_md5.equals(ruta))||username.equals("masteruser") ){ //si el usuario est� registrado, el password es correcto y se dirige a una room que iguala su nombre de usuario o si el usuario es el masteruser (proceso) conectamos
		if( (authenticate(username,password))||username.equals("masteruser") ){ //si el usuario est� registrado, el password es correcto o si el usuario es el masteruser (proceso) conectamos
			log1.info("[FIONAR5][INFO] User "+ username +" exists! And on the right way!");
						
		}else{ //si no se cumplen las condiciones, rechazamos al cliente
			if (!authenticate(username,password)) log1.info("[FIONAR5][INFO] Authenticate failed :(");
			else log1.info("[FIONAR5][INFO] path not valid username: " + username_md5 + " not matching scope: " + ruta );
			log1.info("[FIONAR5][INFO] User not found or invalid Stream:(");
			rejectClient("Invalid User/Pass/Scope");
		}
    	    	    	
    	
    return super.roomConnect(conn, params);
    }
    	    	
    /** {@inheritDoc} */
    @Override
    public boolean roomStart(IScope room){
    	
    	log1.info("[FIONAR5][INFO] roomStart (room) "+ room);
    	return super.roomStart(room);		
    }
        
	/** {@inheritDoc} */
    //se inicializa una vez cuando el cliente intentfa conectar
    @Override
	public boolean appConnect(IConnection conn, Object[] params) {
		log1.info("[FIONAR5][INFO] appConnect (conn)" + conn);
		
	
		/*C�digo que se ejecuta cuando hay una nueva conexi�n */
		
		if (params == null || params.length == 0) {
			log1.info("No Username Passed");
			//Reject client terminates the execution of current client
			rejectClient("Username required, client rejected.");
		}
	   	
		String username = params[0].toString();//Obtenemos el nombre de usuario
		String password = params[1].toString();//y el password
				
		//if (username!="masteruser"){
		if (username.compareToIgnoreCase("masteruser")!=0){
			authenticate(username, password);
		}
				
			return super.appConnect(conn, params);
	}

	/** {@inheritDoc} */
    @Override
	public void appDisconnect(IConnection conn) {
		log1.info("[FIONAR5][INFO] appDisconnect");
		if (appScope == conn.getScope() && serverStream != null) {
			serverStream.close();
		}
		super.appDisconnect(conn);
	}

	   private static String md5(String arg) {
			  
		   MessageDigest algorithm = null;
	       try {
	           algorithm = MessageDigest.getInstance("MD5");
	       } catch (NoSuchAlgorithmException nsae) {
	           try {
				throw new Exception("Cannot find digest algorithm");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       }
	       byte[] defaultBytes = arg.getBytes();
	       algorithm.reset();
	       algorithm.update(defaultBytes);
	       byte messageDigest[] = algorithm.digest();
	       StringBuffer hexString = new StringBuffer();

	       for (int i = 0; i < messageDigest.length; i++) {
	           String hex = Integer.toHexString(0xff & messageDigest[i]);
	           if (hex.length() == 1) {
	               hexString.append('0');
	           }
	           hexString.append(hex);
	       }
	       return hexString.toString();
		}
	   
	   public void roomDisconnect(IConnection conn)
	   {
		   log1.info("[FIONAR5][INFO] roomDisconnect (conn)" + conn);
		   
	   }
	   
	   public void roomStop(IScope room)
	   {
		   log1.info("[FIONAR5][INFO] roomStop (room)" + room );
		   
	   }
	   
	   public void roomLeave (IClient user, IScope room)
	   {
		   log1.info("[FIONAR5][INFO] roomLeave (room)" + room + "(client)" + user);   
		   
	   }
}
