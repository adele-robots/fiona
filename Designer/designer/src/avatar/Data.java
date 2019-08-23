package avatar;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import avatar.persistence.ConexionDAO;
import avatar.persistence.ConexionMySQL;
import avatar.persistence.Prompt;
import avatar.persistence.PromptDAO;

/**
 * Servlet implementation class Data
 */

public class Data extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Data() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException,IOException
    {
       try {
		procesarPeticion(servletRequest,servletResponse);
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException,IOException
    {
       doGet(servletRequest, servletResponse);
    }
    
    protected void procesarPeticion(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	ConexionDAO con = new ConexionMySQL();
    	
  	
    	PromptDAO dao= new PromptDAO();
    	dao.setConexionDAO(con);
    	
    	
    	    	
       JSONArray lista = new JSONArray();

       for(Prompt p : dao.getPrompts())
       {
    	   JSONObject obj = new JSONObject();
    	   obj.put("frase",p.getFrase());
           obj.put("id",p.getId());
           lista.put(obj);
       }
       JSONObject resultado = new JSONObject();
       resultado.put("list",lista);
       response.setContentType("text/html");
       response.setCharacterEncoding("UTF-8");
       
       Writer out = response.getWriter();
       out.write(resultado.toString());
    }

}
