package avatar;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet que permite la descarga de ficheros del servidor
 * 
 * @author adele
 *
 */
public class DownloadFileServlet extends HttpServlet {
	
	private static final String FILEFAW = "/datos/nfs/users/private/";
	private static final String USER_DATA_DIR = "/UserSparkData/";
	private static final String USER_FILES_DIR = "/UserFiles/";
	
       
    /**
	 * 
	 */
	private static final long serialVersionUID = -5295432860115074590L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadFileServlet() {
        super();
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fileName = request.getParameter("fileName");
		String user = request.getParameter("user");
		String sparkName = request.getParameter("sparkName");
		
		String filePath = FILEFAW + user + USER_DATA_DIR + sparkName + USER_FILES_DIR + fileName;
		
		
		FileInputStream fileToDownload = new FileInputStream(filePath);
		ServletOutputStream out = response.getOutputStream();
		//response.setContentType("text/plain");
		response.setHeader("Content-Disposition","attachment; filename="+fileName);
		response.setContentLength(fileToDownload.available());
		int c;
		while((c=fileToDownload.read()) != -1){
			out.write(c);
		}
		out.flush();
		out.close();
		fileToDownload.close();	
		
	}

}
