package avatar;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase que obtiene todos los ficheros en el directorio
 * de configuración del usuario y los devuelve con formato
 * JSON
 * 
 * @author adele
 *
 */
public class GetFilesServlet extends HttpServlet{
		
	private static final String FILEFAW = "/datos/nfs/users/private/";
	private static final String USER_DATA_DIR = "/UserSparkData/";
	private static final String USER_FILES_DIR = "/UserFiles/";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4465818116245016837L;
	
	
	@Override
	public void init() throws ServletException {		
		super.init();
	}

	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		doPost(request, response);
	}

	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
				
		String user = request.getParameter("user");
		String sparkName = request.getParameter("sparkName");
		
				
		File folder = new File(FILEFAW, user + USER_DATA_DIR + sparkName + USER_FILES_DIR);
		
		// Asegurar que el directorio va a existir antes de intentar listar ficheros
		if(!folder.exists())
			folder.mkdirs();
		
	    File[] listOfFiles = folder.listFiles();	    
	    
	    JSONArray jsonArray = new JSONArray();

	    if(listOfFiles != null){
		    for(int i = 0;i < listOfFiles.length ; i++){
		    	JSONObject json = new JSONObject();
		    	File f = listOfFiles[i];
		    	
		    	try {
		    		if(f.getName().indexOf(".BCK")>0)
		    			json.put("name", f.getName().substring(0,f.getName().indexOf(".BCK")));
		    		else
		    			json.put("name", f.getName());
					json.put("size", f.length());					
					json.put("date_modified", f.lastModified());
					json.put("c_check", (f.getName().indexOf(".BCK")>0)?Boolean.FALSE:Boolean.TRUE);
					
					jsonArray.put(json);
		    	} catch (JSONException e) {					
					e.printStackTrace();
				}
		    }
	    }
	    
	    PrintWriter out = response.getWriter();
	    out.println(jsonArray.toString());
        out.close();

	}
	
	

}
