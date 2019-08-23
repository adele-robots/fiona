package avatar.persistence;

import java.sql.Connection;

public interface ConexionDAO {
	
	Connection getConexion() throws Exception;
	Connection actualizarConexion() throws Exception;
	public void iniciarTransaccion() throws Exception;
	public void commit() throws Exception;
	public void rollback() throws Exception;
	public void finalizar() throws Exception;

}