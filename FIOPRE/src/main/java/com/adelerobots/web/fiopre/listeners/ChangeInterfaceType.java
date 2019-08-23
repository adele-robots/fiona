package com.adelerobots.web.fiopre.listeners;

import java.util.Map;

import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.SNInvoker;
import com.treelogic.fawna.presentacion.componentes.event.interfaces.IProcesadorDeAjaxChangeListener;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.persistencia.DatoFawnaFactory;
import com.treelogic.fawna.presentacion.core.persistencia.IDatosFawna;

public class ChangeInterfaceType implements IProcesadorDeAjaxChangeListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3893018612421054503L;

	public void procesarAjaxChangeListener(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos) {
		
		//Para comprobar desde donde es invocado el listener
		String idComponente = gestorEstados.getIdComponente();		
		Map<String, String> items;		
		String interfaceType = (String) gestorDatos.getValue("interfaceType");
		
		// Si se invoca desde el polling (single shot) 'interfaceType' estará vacío y 
		// le damos el valor para que al iniciar la página cargue interfaces 'provided'
		if(idComponente.compareTo("pollInsertInterfaces")==0){
			interfaceType = "1";
			// Desactivamos el polling
			gestorEstados.setPropiedad("pollInsertInterfaces", "enabled", Boolean.FALSE);
		}
		try {
			if(interfaceType.compareTo("1")==0){
				items = invokeListInterfacesByType("P");
				gestorDatos.setItemsMap("interfaces_source", items);
				gestorEstados.setPropiedad("interfaces_source", "style", "color: #069D43; background-color: #C9D4D4;");
				gestorEstados.setPropiedad("provided_interfaces", "disabled", Boolean.FALSE);
				gestorEstados.setPropiedad("required_interfaces", "disabled", Boolean.TRUE);
			}else if(interfaceType.compareTo("2")==0){
				items = invokeListInterfacesByType("R");
				gestorDatos.setItemsMap("interfaces_source", items);
				gestorEstados.setPropiedad("interfaces_source", "style", "color: #4F338B; background-color: #C9D4D4;");
				gestorEstados.setPropiedad("provided_interfaces", "disabled", Boolean.TRUE);
				gestorEstados.setPropiedad("required_interfaces", "disabled", Boolean.FALSE);
			}		
		} catch (FactoriaDatosException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PersistenciaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FawnaInvokerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Invocar a SN029048 para recuperar la lista de
	 * interfaces por tipo
	 * 
	 * @param interfaceType - tipo de interfaz
	 *  
	 * @return Mapa 
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected Map<String, String> invokeListInterfacesByType(final String interfaceType)
	throws FactoriaDatosException, 
			PersistenciaException,
			FawnaInvokerException 
	{
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();		
		datosEntrada.putDato("0", interfaceType, "String");
		
		@SuppressWarnings("unchecked")
		Map<String, String> datos = invoker.invokeSNParaColeccion("029", "048", datosEntrada, "FIONEG017010", "FIONEG017020", true, true);
				
		return datos;
	}

}
