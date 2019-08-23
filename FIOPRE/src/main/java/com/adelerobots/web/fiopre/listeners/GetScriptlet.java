package com.adelerobots.web.fiopre.listeners;

import com.adelerobots.web.fiopre.utilidades.ConfigUtils;
import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.SNInvoker;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.persistencia.DatoFawnaFactory;
import com.treelogic.fawna.presentacion.core.persistencia.IDatosFawna;

public class GetScriptlet implements IProcesadorDeAjaxChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6575696481532581098L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		String usermaild5 = ContextUtils.getUserMailD5();
		String userpw = ContextUtils.getUserPassword();
		Integer userId = Integer.parseInt(ContextUtils.getUserIdAsString());
		String usermail = "";
		String resourcesHost = ConfigUtils.getScripletResourcesHost();
		
		String idComponenteDesencadenante = gestorEstados.getIdComponente();
		
		SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		
		if(idComponenteDesencadenante.compareToIgnoreCase("checkPublished") == 0){
			Boolean isPublished = (Boolean)gestorDatos.getValue(idComponenteDesencadenante);
			try{
				datosEntrada = DatoFawnaFactory.getDatoFawna();
				datosEntrada.putDato("0", userId, "BigDecimal");
				
				if(isPublished)
					datosEntrada.putDato("1", "1", "String");
				else
					datosEntrada.putDato("1", "0", "String");
				
				IContexto[] datos = invoker.invokeSNParaContextos("029", "036", datosEntrada, false);
				
			}catch(FactoriaDatosException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PersistenciaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FawnaInvokerException e) {
				// TODO Auto-generated catch blocks
				e.printStackTrace();
			}
		}else if (idComponenteDesencadenante.compareToIgnoreCase("abrirDialogoScriptlet") == 0){				
			
			try {				
				// Llamamos al servicio que comprueba el estado de publicación del scriptlet
				datosEntrada = DatoFawnaFactory.getDatoFawna();
				datosEntrada.putDato("0", userId, "BigDecimal");			
									
				IContexto[] datos = invoker.invokeSNParaContextos("029", "035", datosEntrada, false);
				String published = null;
				if(datos != null){// No existe entrada en la tabla 'webPublished' para este usuario
					published = datos[0].getString("FIONEG013030");
				}else{// Llamamos al SN que crea la entrada y marca como publicado el scriptlet
					datosEntrada.putDato("0", userId, "BigDecimal");
					datosEntrada.putDato("1", "1", "String");
					
					datos = invoker.invokeSNParaContextos("029", "036", datosEntrada, false);
					
					published = "1"; // Marcamos como publicado el scriptlet
				}
				
				if(published!=null && published.compareToIgnoreCase("1")==0){
					gestorDatos.setValue("checkPublished", Boolean.TRUE);
				}else{
					gestorDatos.setValue("checkPublished", Boolean.FALSE);
				}
				
				datosEntrada = DatoFawnaFactory.getDatoFawna();
				datosEntrada.putDato("0", "I", "String");
				datosEntrada.putDato("2", userId , "BigDecimal");
				datos = invoker.invokeSNParaContextos("029", "003", datosEntrada, false);
				usermail= datos[0].getString("FIONEG001040");
			} catch (FactoriaDatosException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (PersistenciaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FawnaInvokerException e) {
				// TODO Auto-generated catch blocks
				e.printStackTrace();
			}

		
			String script = "";
			
			script += "<div id=\"fiona\" class=\"fiona_main\" style=\"display:none;\">\n";			
			script += "</div>\n";
			script += "<input type=\"button\" value=\"myAvatar\" id=\"starttalk\" onclick=\"initFiona(this,usermail,usrid2,usrid1)\">\n";
			script += "<!-- img id='starttalk' src='myImageSource' onclick='initFiona()'-->\n";
				
			script+="\n\n";
			
			script += "<script type='text/javascript'><!--//--><![CDATA[//><!--\n";
			script += "usermail = \""+usermail+"\";\n";
			script += "usrid2 = \""+userpw+"\";\n";
			script += "usrid1 = \""+usermaild5+"\";\n";
			script += "avatar_size = \"big\";//big or small\n\n";
			script += "show_pop_up = true; // true or false\n\n";
			script += "allow_camera = false; // true or false\n\n";
			script += "(function() {\n";
			script += "var fi = document.createElement('script');\n fi.type = 'text/javascript';\n fi.async = true;\n";
			//script += "fi.src = ('http://resources.sparkingtogether.com/fiona-embed-1.0.js');";
			script += "fi.src = ('" + resourcesHost + "/fiona-embed-1.0.js');\n";
			script += "var s = document.getElementsByTagName('script')[0];\n s.parentNode.insertBefore(fi, s);\n})();\n";       
			script += "//--><!]]></script>";
					
			//gestorEstados.setPropiedad("fionaScript","rendered", true);
			gestorDatos.setValue("fionaScript", script);
			
			gestorDatos.setValue("usermailField", usermail);
			gestorDatos.setValue("usrid1Field", usermaild5);
			gestorDatos.setValue("usrid2Field", userpw);
			
			gestorEstados.openModalAlert("dialogoScriptlet");
			
		}
				
	}
}
