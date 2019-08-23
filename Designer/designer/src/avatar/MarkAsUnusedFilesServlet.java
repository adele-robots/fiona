package avatar;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MarkAsUnusedFilesServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -348939370495555697L;

	private static final String FILEFAW = "/datos/nfs/users/private/";
	private static final String USER_DATA_DIR = "/UserSparkData/";
	private static final String USER_FILES_DIR = "/UserFiles/";
	
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
		String fileName = request.getParameter("fileToMark");
		String used = request.getParameter("fileIsUsed");
		Boolean isUsed = Boolean.parseBoolean(used);
		Boolean success = Boolean.FALSE;
		
		//String filePath = FILEFAW + user + USER_DATA_DIR + sparkName + USER_FILES_DIR + fileName;
		
			
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		
		// Si el parámetro 'isUsed' nos viene a 'true' quiere decir que se marcó el check para
		// un archivo que hasta ahora tenía extensión .BCK, con lo que antes de comprobar
		// que el archivo exista, le concatenamos a su nombre dicha extensión
		if(isUsed)
			fileName += ".BCK";
		
		File f = new File(FILEFAW, user + USER_DATA_DIR + sparkName + USER_FILES_DIR + fileName);
		
		if(f.exists()){
			// Renombrar archivo poniéndole extensión .BCK si está marcado
			// para no usarse
			if(!isUsed){
				f.renameTo(new File(FILEFAW,user + USER_DATA_DIR + sparkName + USER_FILES_DIR + fileName + ".BCK"));
			}else{
				String newFilename = fileName.substring(0, fileName.indexOf(".BCK"));
				f.renameTo(new File(FILEFAW,user + USER_DATA_DIR + sparkName + USER_FILES_DIR + newFilename));
			}
				
			success = Boolean.TRUE;
		}
		
		JSONObject jsonResponse = new JSONObject();
		
		try {
			jsonResponse.put("success", success);						
			jsonResponse.put("data", jsonArray);
		} catch (JSONException e) {			
			e.printStackTrace();
		}		
		
		
	    out.println(jsonResponse.toString());
        out.close();
		
	}

}
