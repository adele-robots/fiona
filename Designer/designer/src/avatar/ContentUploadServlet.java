package avatar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


/**
 * Servlet que permite la subida de ficheros al servidor
 * 
 * @author adele
 *
 */
public class ContentUploadServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8379160320853749461L;
	
	/* Aquí se guarda el fichero, en un directorio que tomcat pueda servir */
	private static final String FILEFAW = "/datos/nfs/users/private/";
	private static final String USER_DATA_DIR = "/UserSparkData/";
	private static final String USER_FILES_DIR = "/UserFiles/";
	
	/**
     * @see HttpServlet#HttpServlet()
     */
    public ContentUploadServlet() {
        super();
        // TODO Auto-generated constructor stub
    } 
	
	@Override
	public void init() throws ServletException {		
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// Parse the request		
		List<FileItem> items = new ArrayList<FileItem>();
		try {			
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			// TODO Gestión de errores
			e.printStackTrace();
		}
		
		String fileName = request.getParameter("fileName");
		String user = request.getParameter("user");
		String sparkName = request.getParameter("sparkName");
		
		// Process the uploaded items
		if(items != null && !items.isEmpty()){
			Iterator <FileItem> iter = items.iterator();
			while (iter.hasNext()) {
			    FileItem item = (FileItem) iter.next();
	
			    if (item.getFieldName().equals("content")) {
			    	try {
						processUploadedFile(item, fileName, user,sparkName);
					} catch (Exception e) {
						// TODO Gestión de errores
						e.printStackTrace();
					}
			    } 
			}
		}else{			
			// TODO Gestión del error
		}
		
		
	}
	
	/**
	 * Método que permite almacenar el archivo en
	 * el disco
	 * @param fItem Representación del archivo
	 * que se pretende "subir"
	 * @param fileName Nombre que tendrá el fichero cuando se guarde
	 * en disco
	 * @throws Exception 
	 */
	private void processUploadedFile(FileItem fItem, String fileName, String user, String sparkName) throws Exception{
		
	    // Almacenamos el fichero en disco
		File userFilesDirectory = new File(FILEFAW, user + USER_DATA_DIR + sparkName + USER_FILES_DIR);
		if(!userFilesDirectory.exists())
			userFilesDirectory.mkdirs();
		
	    File uploadedFile = new File(FILEFAW, user + USER_DATA_DIR + sparkName + USER_FILES_DIR + fileName);
	    fItem.write(uploadedFile);
	    
	    
	}

	

}
