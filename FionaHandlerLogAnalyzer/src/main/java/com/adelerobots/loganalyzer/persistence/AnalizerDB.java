package com.adelerobots.loganalyzer.persistence;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;


public class AnalizerDB {

	@SuppressWarnings("unused")
	private static String properties_file = "properties/db_conection.properties";  
	
	Connection connection = null;
	Statement statement = null;
	
	/** Parametros de conexion */
	private static String db="analytics";
	private static String login="root";
	private static String password="Aio0000";
	private static String url="jdbc:mysql://localhost:3306/";  

	/** Constructor de DbConnection */
	public AnalizerDB() {
		try {
			// leemos el properties con los datos de la bbdd
			//readProperties(properties_file);
			
			// obtenemos el driver de para mysql
			Class.forName("com.mysql.jdbc.Driver");
			
			// obtenemos la conexión
			connection = (Connection) DriverManager.getConnection(url+db, login, password);

			if (connection != null) {
			//	System.out.println("Conexión a base de datos " + db + " OK\n");
			}
		} catch (SQLException e) {
			System.out.println(e);
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/** Permite retornar la conexión */
	public Connection getConnection() {
		return connection;
	}

	public void desconectar() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connection = null;
	}

	/** Lee los datos de la bbdd de un fichero properties 
	 * @param properties_file */
	@SuppressWarnings("unused")
	private void readProperties(String properties_file) {
		
	    Properties properties = new Properties();
	    InputStream input = null;
		
	    try {
	         
	    	input = new FileInputStream(properties_file);
	        properties.load(input);

	        
	        db = properties.getProperty("db_name");
	        login = properties.getProperty("db_login");
	        password = properties.getProperty("db_password");
	        url = properties.getProperty("db_url").concat(db);

	    } catch (IOException ex) {
	        ex.printStackTrace();
	    } finally {
	        if (input != null) {
	            try {
	                input.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	
}
