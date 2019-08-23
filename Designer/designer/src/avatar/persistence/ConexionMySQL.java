package avatar.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL implements ConexionDAO{
	
	private Connection conexion;

	@Override
	public Connection getConexion() throws Exception {
		if(conexion == null)
			crearConexion();
		return conexion;
	}

	@Override
	public Connection actualizarConexion() throws Exception {
		finalizar();
		return getConexion();
	}

	@Override
	public void iniciarTransaccion() throws Exception {
		conexion.setAutoCommit(false);
		
	}

	@Override
	public void commit() throws Exception {
		conexion.commit();
		conexion.setAutoCommit(true);
		
	}

	@Override
	public void rollback() throws Exception {
		conexion.rollback();
		conexion.setAutoCommit(true);
		
	}

	@Override
	public void finalizar() throws Exception {
		if(conexion!= null){
			try{
				conexion.createStatement().executeUpdate("SHUTDOWN");
				conexion.close();
				conexion = null;
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
	}
	
	private void crearConexion() {
		String SQL_DRV = "com.mysql.jdbc.Driver";
		String SQL_URL = "jdbc:mysql://localhost:4046/prompt?characterEncoding=UTF-8";
		
		
			try {
				Class.forName(SQL_DRV);
				conexion = DriverManager.getConnection(SQL_URL, "root", "root");
				
			} catch (SQLException e) {
				  System.out.println("Error de MySQL: " + e.getMessage());
				} catch (Exception e) {
				  System.out.println("Error inesperado: " + e.getMessage());
				}
		
		
	}

}
