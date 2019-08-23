package avatar.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class PromptDAO{
	
	private ConexionDAO conexionDAO;
	
	public void setConexionDAO(ConexionDAO conexion) {
		this.conexionDAO = conexion;
	}

	public List<Prompt> getPrompts() throws Exception {
		List<Prompt> prompts = new LinkedList<Prompt>();
		Prompt aux;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		
		
		try{
			ps= conexionDAO.getConexion().prepareStatement("select * from prompt order by id asc");
			rs = ps.executeQuery();
			
			while(rs.next()){
				aux = new Prompt();
				aux.setId(rs.getInt("ID"));
				aux.setFrase(rs.getString("FRASE"));
				prompts.add(aux);
				
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw (e);
		} finally {
			try {
				ps.close();
				conexionDAO.commit();
			} catch (Exception e) {
			}
		}
		return prompts;
	}
	
	public boolean insertPrompt(String frase){
		PreparedStatement ps = null;
		
		try{
			ps= conexionDAO.getConexion().prepareStatement("insert into prompt values(0,?)");
			ps.setString(1, frase);
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				conexionDAO.commit();
				return false;
			} catch (Exception e) {
			}
		}
		
		return true;
		
	}
		
	}

