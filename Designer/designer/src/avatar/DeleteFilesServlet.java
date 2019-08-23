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

/**
 * Servlet que permite la eliminación de ficheros del servidor
 * 
 * @author adele
 *
 */
public class DeleteFilesServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -392380496056886073L;
	
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
		String fileNames = request.getParameter("filesToDelete");
		Boolean success = Boolean.FALSE;
		
		String [] filesToDelete = fileNames.split(",");
		
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		
		for(int i = 0; i < filesToDelete.length; i++){
			JSONObject jsonFile = new JSONObject();
			File f = new File(FILEFAW, user + USER_DATA_DIR + sparkName + USER_FILES_DIR + filesToDelete[i]);			
			try {
				jsonFile.put("name", filesToDelete[i]);
				jsonArray.put(jsonFile);
			}catch(JSONException e){
				e.printStackTrace();
			}
			
			if(f.exists()){
				f.delete();
				success = Boolean.TRUE;
			}
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
