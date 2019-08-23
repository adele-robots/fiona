package com.adelerobots.fiona.embed;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;

/**
 * Servlet para inicializar el archivo de log de la aplicación
 */
public class Log4jInit extends HttpServlet {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6093453361529128006L;

	public void init(){
		String prefix =  getServletContext().getRealPath("/");
		String file = getInitParameter("log4j-init-file");

		if(file != null) {
			PropertyConfigurator.configure(prefix+file);
		}
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Log4jInit() {
        super();        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	

}
