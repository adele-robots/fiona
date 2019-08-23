package avatar;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Servlet implementation class Data
 */

public class PandoraServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String pandoraUrl = "http://pandorabots.com/botmaster/en/mostactive";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PandoraServlet() {
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
    	Document doc = Jsoup.connect(pandoraUrl).get();
    	// Extract all links
    	Elements links = doc.select("a[href]");    	 	
    	// Create table to show name/bot id/interactions
    	response.getWriter().write("<table border=\"1\">" +
        		"<tr>" +
        		"<th><b>Name</b></th>" +
        		"<th><b>Bot Id</b></th>" +        		
        		"<th><b>Interactions</b></th>" +
        		"</tr>");

    	for (Element link : links) {  
    		
            String[] parseLink = link.attr("abs:href").split("botid=");
            if(parseLink.length > 1){            	
            	response.getWriter().write("<tr>" +
            			"<td>"+link.text()+"</td>" +
            			"<td>"+parseLink[1]+"</td>" +
            			"<td>"+link.parent().nextElementSibling().text()+"</td>" +
            			"</tr>");    
            }          
        }   
        response.getWriter().write("</table>");
    }
}
