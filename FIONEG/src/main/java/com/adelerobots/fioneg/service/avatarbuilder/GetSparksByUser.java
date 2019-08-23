package com.adelerobots.fioneg.service.avatarbuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.adelerobots.fioneg.context.ContextoUsuarios;
import com.adelerobots.fioneg.dataclasses.FionaComponent;
import com.adelerobots.fioneg.entity.ConfigParamC;
import com.adelerobots.fioneg.entity.DatoListaC;
import com.adelerobots.fioneg.entity.DatoListaUsuarioC;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.manager.ConfigParamManager;
import com.adelerobots.fioneg.manager.DatoListaUsuarioManager;
import com.adelerobots.fioneg.manager.ManagerFactory;
import com.adelerobots.fioneg.manager.UsuariosManager;
import com.adelerobots.fioneg.service.security.LogonConstants.LogonErrorCode;
import com.adelerobots.fioneg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.RollbackException;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;


public class GetSparksByUser extends ServicioNegocio{

	private static FawnaLogHelper LOGGER = FawnaLogHelper.getLog(GetSparksByUser.class);
	
	private static final int CTE_POSICION_TIRA_USER_ID = 0;
	
	public GetSparksByUser (){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(final IContextoEjecucion contexto, final IDatosEntradaTx datosEntrada) 
	{
		if (logger.isInfoEnabled()) {
			LOGGER.info("Inicio Ejecucion del SN 029007: Obtener sparks de usuario");
		}
		final long iniTime = System.currentTimeMillis();
		
		
		//Obtener campos de entrada
		Integer userId = null; {
			final BigDecimal value = datosEntrada.getDecimal(CTE_POSICION_TIRA_USER_ID);
			if (value != null) userId = new Integer(value.intValue());
		}
		
		
		IContexto[] salida = null;
		try 
		{
			final UsuariosManager manager = ManagerFactory.getInstance().getUsuariosManager();
			final ConfigParamManager cpmanager = ManagerFactory.getInstance().getConfigParamManager();
			
			final UsuarioC usuario = manager.findById(userId);
			if (usuario == null) { //Unknown / no such user
				LogonErrorCode errCode = LogonErrorCode.ERR525__NO_SUCH_USER;
				ServicioNegocio.rollback(TipoError.FUNCIONAL, errCode.code, errCode.msg, errCode.desc, null);
			}
			
			List<FionaComponent> fionaComponentList = new ArrayList<FionaComponent>();
			List<ConfigParamC> genConfigParamsList = new ArrayList<ConfigParamC>();
			
//			fionaComponentList = manager.getAvatarBuilderInfo(IdUsuario);
			fionaComponentList = manager.getSparksAndIfaces(userId);
			genConfigParamsList = cpmanager.getGenConfParamsUser();
			
			
			try {
				escribeArchivo(usuario, fionaComponentList, genConfigParamsList);
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			
			salida = ContextoUsuarios.rellenarContexto(usuario);
			
		} catch (Exception e) {
			if (e instanceof RollbackException) {
				//ServicioNegocio.rollback(...) throw this exception, rethrow!!
				throw (RollbackException) e;
			}
			// se ha producido un error de ejecucion
			ServicioNegocio.rollback(TipoError.TECNICO, new Integer(0), e.getMessage(), e.getMessage(), null);
		}
		if (logger.isInfoEnabled()) {
			logger.info("Fin Ejecucion del SN 029007: Obtener sparks de usuario. Tiempo total = " + Long.valueOf(System.currentTimeMillis() - iniTime) + "ms");
		}
		return salida;
	}



	protected void escribeArchivo(
			final UsuarioC user,
			final List<FionaComponent> components, final List<ConfigParamC> genConfigParamsList) 
		throws JSONException, IOException
	{
		
		FileWriter outJsonFile = null;
		try {
			outJsonFile = new FileWriter(Constantes.getDesignerEnvConfigFile(user.getAvatarBuilderUmd5()));
			BufferedWriter bw = new BufferedWriter(outJsonFile);
			
			JSONObject outjson = new JSONObject();
			
			outjson.put("title", "Sparklink");
			outjson.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
			outjson.put("description", "This is the Avatar Builder StencilSet specification");
			
				
			JSONArray propertyPackages = new JSONArray();
			JSONArray stencils = new JSONArray();
			JSONObject rules = new JSONObject();
			
			propertyPackages = propertiesJSON(genConfigParamsList);
			stencils = stencilsJSON(components, user.getCnUsuario());
			rules = rulesJSON(components);
			
			outjson.put("propertyPackages", propertyPackages);
			outjson.put("stencils",stencils);
			outjson.put("rules", rules);
			
			outjson.write(bw);
	
			bw.flush();
			bw.close();
		} finally {
			//Close stream quietly
			try { if (outJsonFile != null) outJsonFile.close(); } catch (Exception e) {}
		}
	}
	
	private JSONArray propertiesJSON(List<ConfigParamC> genConfigParamsList) throws JSONException{
		JSONArray propertyPackages = new JSONArray();
		
		JSONObject baseAttributes = new JSONObject();
		JSONObject conditions = new JSONObject();
		JSONObject bgColor = new JSONObject();
		JSONObject diagram = new JSONObject();
		
		baseAttributes.put("name", "baseAttributes");
		JSONArray baseAttributesArray = new JSONArray();
		JSONObject baseAttributesArrayObject = new JSONObject();
		baseAttributesArrayObject.put("id","name");
		baseAttributesArrayObject.put("type","String");
		baseAttributesArrayObject.put("title","Name");
		baseAttributesArrayObject.put("value","");
		baseAttributesArrayObject.put("description","The descriptive name of the FIONA Component");
		baseAttributesArrayObject.put("description_de","German description");
		baseAttributesArrayObject.put("readonly",false);
		baseAttributesArrayObject.put("optional",true);
		baseAttributesArrayObject.put("length","");
		baseAttributesArrayObject.put("wrapLines",true);
		baseAttributesArrayObject.put("refToView","text_name");
		baseAttributesArray.put(0,baseAttributesArrayObject);
		baseAttributes.put("properties", baseAttributesArray);
		propertyPackages.put(0,baseAttributes);
				
		
		conditions.put("name", "conditions");
		JSONArray conditionsArray = new JSONArray();
		JSONObject conditionsArrayObject = new JSONObject();
		conditionsArrayObject.put("id","condition");
		conditionsArrayObject.put("type","String");
		conditionsArrayObject.put("title","Condicion");
		conditionsArrayObject.put("value","");
		conditionsArrayObject.put("description","Condición de tránsito entre estados");
		conditionsArrayObject.put("readonly",false);
		conditionsArrayObject.put("optional",false);
		conditionsArrayObject.put("length","");
		conditionsArrayObject.put("wrapLines",true);
		conditionsArray.put(0,conditionsArrayObject);
		conditions.put("properties", conditionsArray);
		propertyPackages.put(1,conditions);
				
		
		bgColor.put("name", "bgColor");
		JSONArray bgColorArray = new JSONArray();
		JSONObject bgColorArrayObject = new JSONObject();
		bgColorArrayObject.put("id","bgColor");
		bgColorArrayObject.put("type","Color");
		bgColorArrayObject.put("title","Background Color");
		bgColorArrayObject.put("title_de","");
		bgColorArrayObject.put("value","#ffffff");
		bgColorArrayObject.put("description","");
		bgColorArrayObject.put("readonly",false);
		bgColorArrayObject.put("optional",false);
		bgColorArrayObject.put("refToView","bg_frame");
		bgColorArrayObject.put("fill",false);
		bgColorArrayObject.put("stroke",false);
		bgColorArray.put(0,bgColorArrayObject);
		bgColor.put("properties", bgColorArray);
		propertyPackages.put(2,bgColor);
		
		// dentro de diagram es el sitio para los parámetros de configuración general
		diagram.put("name","diagram");
		JSONArray diagramArray = new JSONArray();
		JSONObject diagramArrayObject1 = new JSONObject();
		JSONObject diagramArrayObject2 = new JSONObject();
		JSONObject diagramArrayObject3 = new JSONObject();
		diagramArrayObject1.put("id","id");
		diagramArrayObject1.put("type","String");
		diagramArrayObject1.put("title","ID");
		diagramArrayObject1.put("title_de","ID");
		diagramArrayObject1.put("value","");
		diagramArrayObject1.put("description","ID");
		diagramArrayObject1.put("description_de","ID");
		diagramArrayObject1.put("readonly",false);
		diagramArrayObject1.put("optional",true);
		
		diagramArrayObject2.put("id","version");
		diagramArrayObject2.put("type","String");
		diagramArrayObject2.put("title","Version");
		diagramArrayObject2.put("value","");
		diagramArrayObject2.put("description","This defines the version number of the diagram");
		diagramArrayObject2.put("description_de","");
		diagramArrayObject2.put("readonly",false);
		diagramArrayObject2.put("optional",true);
		diagramArrayObject2.put("length","50");
		
		
		diagramArrayObject3.put("id","language");
		diagramArrayObject3.put("type","String");
		diagramArrayObject3.put("title","Language");
		diagramArrayObject3.put("title_de","");
		diagramArrayObject3.put("value","Spanish");
		diagramArrayObject3.put("value_de","Spanish");
		diagramArrayObject3.put("description","This holds the name of the language in which text is written");
		diagramArrayObject3.put("description_de","");
		diagramArrayObject3.put("readonly",false);
		diagramArrayObject3.put("optional",true);
		diagramArrayObject3.put("length","50");
		
		diagramArray.put(0,diagramArrayObject1);
		diagramArray.put(1,diagramArrayObject2);
		diagramArray.put(2,diagramArrayObject3);
		
		/////////////////////////////////////////EN OBRAS///////////////////////////////////////////////
		//TODO
		//un nuevo JSON por cada parámetro de configuración general
		//pasar a esta función una lista de parámetros de configuración general
		//for(int i=0; i<GenConfigParamList.size(); i++)
		//diagramArrayObjectN = new JSONObject();
		//object.put movidas
		//diagramArray.put(3->n, diagramArrayObjectN
		
		for (int i=0; i< genConfigParamsList.size(); i++){
			JSONObject diagramArrayGenConfObject = new JSONObject();
			genConfigParamsList.get(i);
			
			diagramArrayGenConfObject.put("id",genConfigParamsList.get(i).getStrNombre());
			diagramArrayGenConfObject.put("type",genConfigParamsList.get(i).getTipo().getStrTipo());
			diagramArrayGenConfObject.put("title", genConfigParamsList.get(i).getStrNombre());
			diagramArrayGenConfObject.put("value", genConfigParamsList.get(i).getStrDefaultValue());
			diagramArrayGenConfObject.put("description",genConfigParamsList.get(i).getStrNombre());
			diagramArrayGenConfObject.put("readonly",false);
			diagramArrayGenConfObject.put("optional",false);
			diagramArrayGenConfObject.put("length","100");
			
			diagramArray.put(i+3, diagramArrayGenConfObject);
			
		}
		///////////////////////////////////////////EN OBRAS///////////////////////////////////////////////
		
		
		diagram.put("properties", diagramArray);
		propertyPackages.put(3, diagram);
			
		
		return propertyPackages;
	}

	private JSONArray stencilsJSON(List<FionaComponent> compList, Integer IdUsuario) throws JSONException{
		int i=0; //indexador de array de stencils
		
		List<String> ifacesUnicas = new ArrayList<String>();
		Map<String, List<String>> mapIfacesPR = new HashMap<String, List<String>>();
		
		JSONArray stencils = new JSONArray(); //JSONArray a devolver
		
		//PRIMER NODO, BASE
		JSONObject diagramNode = new JSONObject();
		JSONArray diagramNodePropertyPackages = new JSONArray();
		JSONArray diagramNodeRoles = new JSONArray();

		diagramNodePropertyPackages.put(0,"baseAttributes");
		diagramNodePropertyPackages.put(1,"diagram");
		diagramNodeRoles.put(0,"diagram");
		
		diagramNode.put("type","node");
		diagramNode.put("id","AvatarDiagram");
		diagramNode.put("title","AvatarDiagram");
		diagramNode.put("description","");
		diagramNode.put("view","diagram.svg");
		diagramNode.put("icon","diagram.png");
		diagramNode.put("mayBeRoot",true);
		diagramNode.put("hide",true);
		diagramNode.put("propertyPackages",diagramNodePropertyPackages);
		diagramNode.put("roles",diagramNodeRoles);
		stencils.put(0,diagramNode);
				
		//COMPONENTES
		
		for (i=0; i<compList.size(); i++){ //para cada componente
			int j=1; //indexador para el número de interfaces
			JSONObject auxComp = new JSONObject();
			JSONArray auxGroups = new JSONArray(); //fijo
			JSONArray auxPropertyPackages = new JSONArray(); //fijo
			JSONArray auxProperties = new JSONArray(); //variable según número de ifaces
			JSONArray auxRoles = new JSONArray(); //fijo
			
			auxGroups.put(0, "Components");
			auxPropertyPackages.put(0, "baseAttributes");
			auxRoles.put(0,"components");
			auxRoles.put(1,"component");
			
			JSONObject auxPropertiesBgColor = new JSONObject();
			auxPropertiesBgColor.put("id","bgColor");
			auxPropertiesBgColor.put("type","Color");
			auxPropertiesBgColor.put("title","Background Color");
			auxPropertiesBgColor.put("title_de","");
			auxPropertiesBgColor.put("value","#ffffff");
			auxPropertiesBgColor.put("description","");
			auxPropertiesBgColor.put("readonly",false);
			auxPropertiesBgColor.put("optional",false);
			auxPropertiesBgColor.put("refToView","bg_frame");
			auxPropertiesBgColor.put("fill",false);
			auxPropertiesBgColor.put("stroke",false);
			auxProperties.put(0,auxPropertiesBgColor);
			
			Iterator<String> ifaceIterator = compList.get(i).getComponentIfaces().iterator();
			
			while (ifaceIterator.hasNext()){
				String ifazName = ifaceIterator.next();
				String type = null;
				List <String> ifazComponents = new ArrayList<String>();
				if (ifazName.endsWith("P")) type="Provided";
				else type = "Required";
				
				//antes de quitarles la p o la r puedo construir la lista/mapa/whatever de interfacesPRunicas
				//de hecho como estoy dentro del componente puedo hacer el map
				//si no hay una key con ifaceName, la añado y añado a su lista de valores el nombre del componente
				if (!mapIfacesPR.containsKey(ifazName)){
					ifazComponents.add(compList.get(i).getComponentName()	);
					mapIfacesPR.put(ifazName, ifazComponents );
				}else if(!mapIfacesPR.get(ifazName).contains(compList.get(i).getComponentName())){
					mapIfacesPR.get(ifazName).add(compList.get(i).getComponentName());
				}
				//si ya hay una key con ifaceName, miro si en su lista de valores está el nombre del componente
				//si no está el nombre del componente, lo añado			
			
				
				ifazName = ifazName.substring(0, ifazName.length() - 1); //quita la P o la R del final del nombre del ifaz
				
				JSONObject auxIface = new JSONObject();
								
				auxIface.put("id", type + " " + ifazName );//Provided / Required + nombre_interfaz (quitar p/r)
				auxIface.put("type", "String");
				auxIface.put("title", type); //provided / required
				auxIface.put("value", ifazName); //nombre interfaz (quitar p/r)
				auxIface.put("description", "Description of the interfaz"); //TODO traer hasta aquí la descripción de la interfaz
				auxIface.put("readonly", true);
				auxIface.put("optional", true);
				auxIface.put("length", "");
				auxIface.put("wrapLines", true);
				auxIface.put("refToView", "");
				
				auxProperties.put(j, auxIface); //indexado de la colección de ifaces por iterator
				j++;
				
				//si el nombre de la interfaz ifazName no está en la lista de ifacesUnicas, añadir
				if (!ifacesUnicas.contains(ifazName)) ifacesUnicas.add(ifazName);
				
			}//fin para cada interfaz
						
			//Parámetros de configuración
			
			Iterator<ConfigParamC> configParamIterator = compList.get(i).getComponentParams().iterator();
			
			while (configParamIterator.hasNext()){
				JSONObject auxParam = new JSONObject();
				ConfigParamC auxConfigParamC = configParamIterator.next();
				
				if (auxConfigParamC.getIsConfigurable()==1){
				
					//cosas comunes a todos los tipos de datos
					auxParam.put("readonly", false);
					auxParam.put("wrapLines", true);
					auxParam.put("optional", false);
					auxParam.put("description", auxConfigParamC.getStrDescripcion());
					auxParam.put("refToView", "bg_frame");
					auxParam.put("title", auxConfigParamC.getStrNombre());
					
					if (auxConfigParamC.getTipo().getStrTipo().startsWith("list")){
						//si estoy aquí, el tipo de dato es para desplegable y tengo que hacer movidas
						JSONArray itemsArray = new JSONArray();
						
						//rellenar itemsArray
						List<DatoListaC> listaItems = auxConfigParamC.getTipo().getLstDatosLista();
						Iterator<DatoListaC> iteratorItems = listaItems.iterator();
						
	//					List<DatoListaUsuarioC> listaItemsUsuario = auxConfigParamC.getTipo().getLstDatosListaUsuario();
	//					Iterator<DatoListaUsuarioC> iteratorItemsUsuario =listaItemsUsuario.iterator();
						
			
						//probar
						final DatoListaUsuarioManager managerDLU = ManagerFactory.getInstance().getDatoListaUsuarioManager();
						List<DatoListaUsuarioC> listaItemsUsuario= null;
						listaItemsUsuario = managerDLU.getDatosListaUsuario(IdUsuario,auxConfigParamC.getCnTipo());
						Iterator<DatoListaUsuarioC> iteratorItemsUsuario =listaItemsUsuario.iterator();
						
						while(iteratorItems.hasNext()){
							
							JSONObject auxItems = new JSONObject();
							DatoListaC auxDatoLista = iteratorItems.next();
														
							auxItems.put("title",  auxDatoLista.getStrNombre());
							auxItems.put("value",  auxDatoLista.getStrNombre());
							auxItems.put("refToView", auxDatoLista.getStrNombre().toLowerCase() );
							itemsArray.put(auxItems);
										
						}
						while(iteratorItemsUsuario.hasNext()){
							JSONObject auxItems = new JSONObject();
							DatoListaUsuarioC auxDatoListaUsuario = iteratorItemsUsuario.next();
							
							if(auxDatoListaUsuario.getCnUsuario().equals(IdUsuario)){
								auxItems.put("title",  auxDatoListaUsuario.getStrNombre());
								auxItems.put("value",  auxDatoListaUsuario.getStrNombre());
								auxItems.put("refToView", auxDatoListaUsuario.getStrNombre().toLowerCase() );
								itemsArray.put(auxItems);
							}
						}
						
						auxParam.put("value", "Choose an option");
						auxParam.put("type", "choice");
						auxParam.put("length", "");
						auxParam.put("id", "string_" + compList.get(i).getComponentName() + "_" + auxConfigParamC.getStrNombre());//con este nombre es con el que se guarda luego en el json de salida
						auxParam.put("items", itemsArray);
					}else{
						//si estoy aquí, el tipo de dato es "simple", primitivo
						
						switch (auxConfigParamC.getCnTipo()){
						
						case 1:
							auxParam.put("value", Boolean.parseBoolean(auxConfigParamC.getStrDefaultValue()));
							auxParam.put("type", "boolean");
							auxParam.put("id", "boolean_" + compList.get(i).getComponentName() + "_" + auxConfigParamC.getStrNombre());
							break;
						case 2: 
							auxParam.put("value", Float.parseFloat(auxConfigParamC.getStrDefaultValue()));
							auxParam.put("type", "float");
							auxParam.put("id", "float_"+ compList.get(i).getComponentName() + "_" + auxConfigParamC.getStrNombre());
							break;
						case 3: 
							auxParam.put("value", auxConfigParamC.getStrDefaultValue());
							auxParam.put("type", "string");
							auxParam.put("id", "string_"+ compList.get(i).getComponentName() + "_" + auxConfigParamC.getStrNombre());
							break;
						case 4:
							auxParam.put("value", Integer.parseInt(auxConfigParamC.getStrDefaultValue()));
							auxParam.put("type", "integer");
							auxParam.put("id", "integer_"+ compList.get(i).getComponentName() + "_" + auxConfigParamC.getStrNombre());
							break;
						case 5:
							auxParam.put("value", auxConfigParamC.getStrDefaultValue());
							auxParam.put("type", "file");
							auxParam.put("id", "file_"+ compList.get(i).getComponentName() + "_" + auxConfigParamC.getStrNombre());
							break;
						case 8:
							auxParam.put("value", auxConfigParamC.getStrDefaultValue());
							auxParam.put("type", "file_Browser");
							auxParam.put("id", "file_Browser_"+ compList.get(i).getComponentName() + "_" + auxConfigParamC.getStrNombre());
							break;
						}
										
						auxParam.put("length", "");
						auxParam.put("description", auxConfigParamC.getStrDescripcion());
											
					}
					auxProperties.put(auxParam);
					
				}
				//Iterar lista de componentes, añadir cada propiedad al JSONArray auxProperties
				
				auxComp.put("type", "node");
				auxComp.put("id", compList.get(i).getComponentName()); //nombre componente
				auxComp.put("title", compList.get(i).getComponentName()); // nombre componente
				auxComp.put("groups", auxGroups);
				auxComp.put("description", "");
				auxComp.put("view", "components/" + compList.get(i).getComponentName()+".svg"); 
				auxComp.put("icon", "components/" + compList.get(i).getComponentName()+".png");
				auxComp.put("propertyPackages", auxPropertyPackages);
				auxComp.put("properties", auxProperties);
				auxComp.put("roles", auxRoles);
				
				
				stencils.put(i+1,auxComp);
			}//fin if isConfigurable
		}//fin para cada componente
		
		i++;
		
		//CONECTORES
		JSONObject union = new JSONObject();
		JSONArray unionGroups = new JSONArray();
		JSONArray unionPropertyPackages = new JSONArray();
		JSONArray unionRoles = new JSONArray();
		
		unionGroups.put(0,"Connectors");
		unionPropertyPackages.put(0,"conditions");
		unionRoles.put(0,"all");
		
		union.put("type","edge" );
		union.put("id","Union" );
		union.put("title","ComponentToInterface" );
		union.put("description","" );
		union.put("groups",unionGroups );
		union.put("view","connector/union.svg" );
		union.put("icon","connector/union.png" );
		union.put("propertyPackages",unionPropertyPackages );
		union.put("roles",unionRoles );
		
		stencils.put(i, union);
		i++;
		
		Iterator<String> ifacesUnicasIterator = ifacesUnicas.iterator();
		while(ifacesUnicasIterator.hasNext()){
			JSONObject auxConnector = new JSONObject();
			JSONArray auxConnectorGroups = new JSONArray();
			JSONArray auxConnectorPropertyPackages = new JSONArray();
			JSONArray auxConnectorRoles = new JSONArray();
			String ifaceName = ifacesUnicasIterator.next();
			
			auxConnectorGroups.put(0,"Connectors");
			auxConnectorPropertyPackages.put(0,"");
			auxConnectorRoles.put(0,"conn_interfaces");
			
			auxConnector.put("type","edge");
			auxConnector.put("id","con" + ifaceName);
			auxConnector.put("title",ifaceName + "To" + ifaceName);
			auxConnector.put("description","");
			auxConnector.put("groups",auxConnectorGroups);
			auxConnector.put("view","interfaces/" + ifaceName + ".svg");
			auxConnector.put("icon","interfaces/connector.png");
			auxConnector.put("propertyPackages",auxConnectorPropertyPackages);
			auxConnector.put("roles",auxConnectorRoles);
			stencils.put(i, auxConnector);
			i++;
		}
		
		//INTERFACES
		
		Set<String> mapIfacesPRKeys = mapIfacesPR.keySet();
		Iterator<String> mapIfacesPRKeysIterator = mapIfacesPRKeys.iterator();
		
		while(mapIfacesPRKeysIterator.hasNext()){
			int k=0;
			JSONObject auxIfacePR = new JSONObject();
			JSONArray auxIfacePRGroups = new JSONArray();
			JSONArray auxIfacePRPropertyPackages = new JSONArray();
			JSONArray auxIfacePRRoles = new JSONArray();
			
			String mapKey = mapIfacesPRKeysIterator.next();
			String mapKeyAlt = mapKey.substring(0, mapKey.length() - 1);
			List<String> auxIfazComps = mapIfacesPR.get(mapKey);
			Iterator<String> auxIfazCompsIterator = auxIfazComps.iterator();
			
			while (auxIfazCompsIterator.hasNext()){
				auxIfacePRRoles.put(k, auxIfazCompsIterator.next() + "Morph");
				k++;
			}
			auxIfacePRRoles.put(k, "interface");
			
			if (mapKey.endsWith("P")) {
				auxIfacePRRoles.put(k+1, "provided");
				auxIfacePRGroups.put(0,"Provided Interfaces");
			}
			else {
				auxIfacePRRoles.put(k+1, "required");
				auxIfacePRGroups.put(0,"Required Interfaces");
			}
			
			auxIfacePRPropertyPackages.put(0,"");
						
			auxIfacePR.put("type", "node");
			auxIfacePR.put("id", mapKey); 
			auxIfacePR.put("title", mapKeyAlt);
			auxIfacePR.put("description", "");
			auxIfacePR.put("groups", auxIfacePRGroups);
			auxIfacePR.put("view", "interface/" + mapKey + ".svg" );
			auxIfacePR.put("icon", "interface/" + mapKey + ".png");
			auxIfacePR.put("propertyPackages", auxIfacePRPropertyPackages);
			auxIfacePR.put("roles", auxIfacePRRoles);
			stencils.put(i, auxIfacePR);			
			i++;
		}
		
		return stencils;
	}//fin stencilsJson

	private JSONObject rulesJSON (List<FionaComponent> compList) throws JSONException{
		int i=2;
		List<String> ifacesUnicas = new ArrayList<String>();
		JSONObject rules = new JSONObject();
		JSONArray cardinalityRules = new JSONArray();
		JSONArray connectionRules = new JSONArray();
		JSONArray containmentRules = new JSONArray();
		JSONArray morphingRules = new JSONArray();
		JSONArray layoutRules = new JSONArray();
		
		
		//CARDINALITY RULES
		JSONObject cardinalityRulesStartevents = new JSONObject();
		JSONArray cardinalityRulesStarteventsArray = new JSONArray();
		JSONObject cardinalityRulesStarteventsArrayObject = new JSONObject();
		
		cardinalityRulesStarteventsArrayObject.put("role","Transicion");
		cardinalityRulesStarteventsArrayObject.put("maximum",1);
		cardinalityRulesStarteventsArray.put(0,cardinalityRulesStarteventsArrayObject);
		cardinalityRulesStartevents.put("role","Startevents_all");
		cardinalityRulesStartevents.put("incomingEdges", cardinalityRulesStarteventsArray);
		cardinalityRules.put(0,cardinalityRulesStartevents);
		
		JSONObject cardinalityRulesinterface = new JSONObject();
		JSONArray cardinalityRulesinterfaceArray = new JSONArray();
		JSONObject cardinalityRulesinterfaceArrayObject1 = new JSONObject();
		JSONObject cardinalityRulesinterfaceArrayObject2 = new JSONObject();
		
		cardinalityRulesinterfaceArrayObject1.put("role","conn_interfaces");
		cardinalityRulesinterfaceArrayObject1.put("maximum",1);
		cardinalityRulesinterfaceArrayObject2.put("role","Union");
		cardinalityRulesinterfaceArrayObject2.put("maximum",1);
		cardinalityRulesinterfaceArray.put(0,cardinalityRulesinterfaceArrayObject1);
		cardinalityRulesinterfaceArray.put(1,cardinalityRulesinterfaceArrayObject2);
		cardinalityRulesinterface.put("role","interface");
		cardinalityRulesinterface.put("incomingEdges", cardinalityRulesinterfaceArray);
		cardinalityRules.put(1,cardinalityRulesinterface);
		
		Iterator<FionaComponent> compListIterator = compList.iterator();
		
		while(compListIterator.hasNext()){
			FionaComponent auxComp = compListIterator.next();
			Iterator<String> ifacesiterator = auxComp.getComponentIfaces().iterator();
			JSONObject cardinalityRulesComp = new JSONObject();
			JSONArray cardinalityRulesCompArray = new JSONArray();
			JSONObject cardinalityRulesCompArrayObject = new JSONObject();
			
			cardinalityRulesCompArrayObject.put("role","interfaces");
			cardinalityRulesCompArrayObject.put("maximum",1);
			cardinalityRulesCompArray.put(0, cardinalityRulesCompArrayObject);
			cardinalityRulesComp.put("role",auxComp.getComponentName());
			cardinalityRulesComp.put("maximumOcurrence",1); //número máximo de instanciaciones TODO incluir en tabla Spark como campo
			cardinalityRulesComp.put("outgoingEdges",cardinalityRulesCompArray);
			
			cardinalityRules.put(i,cardinalityRulesComp);
			i++;
		
			//hacer lista de ifacesUnicas
			while(ifacesiterator.hasNext()){
				String ifazName = ifacesiterator.next();
				ifazName = ifazName.substring(0, ifazName.length() - 1);
				if (!ifacesUnicas.contains(ifazName)) ifacesUnicas.add(ifazName);
			}
			
		}

		//interface connector
		
		//CONNECTION RULES
		

		//una por componente
		i=0;
		Iterator<FionaComponent> compListIterator2 = compList.iterator();
		while(compListIterator2.hasNext()){
			FionaComponent auxComp = compListIterator2.next();
			JSONObject connectionRulesComp= new JSONObject();
			JSONArray connectionRulesCompArray = new JSONArray();
			JSONObject connectionRulesCompArrayObject = new JSONObject();
			JSONArray connectionRulesCompArrayObjectArray = new JSONArray();
			
			connectionRulesCompArrayObjectArray.put(0,auxComp.getComponentName() + "Morph");
			connectionRulesCompArrayObject.put("to",connectionRulesCompArrayObjectArray);
			connectionRulesCompArrayObject.put("from", auxComp.getComponentName());
			connectionRulesCompArray.put(0,connectionRulesCompArrayObject);
			connectionRulesComp.put("role","Union" );
			connectionRulesComp.put("connects",connectionRulesCompArray);
			connectionRules.put(i,connectionRulesComp);
			i++;
		}
		
		//una por interfaz única
		
		Iterator<String> ifazUnicaIterator = ifacesUnicas.iterator();
		while (ifazUnicaIterator.hasNext()){
			String auxIfaz = ifazUnicaIterator.next();
			JSONObject connectionRulesIfaz= new JSONObject();
			JSONArray connectionRulesIfazArray = new JSONArray();
			JSONObject connectionRulesIfazArrayObject = new JSONObject();
			JSONArray connectionRulesIfazArrayObjectArray = new JSONArray();
			
			connectionRulesIfazArrayObjectArray.put(0,auxIfaz+"R");
			connectionRulesIfazArrayObject.put("from",auxIfaz + "P");
			connectionRulesIfazArrayObject.put("to",connectionRulesIfazArrayObjectArray);
			connectionRulesIfazArray.put(0,connectionRulesIfazArrayObject);
			connectionRulesIfaz.put("role", "con" + auxIfaz);
			connectionRulesIfaz.put("connects", connectionRulesIfazArray);
			connectionRules.put(i,connectionRulesIfaz);
			i++;
		}
		//CONTAINMENT RULES
		JSONObject diagram = new JSONObject();
		JSONObject eventSubprocess = new JSONObject();
		JSONObject pool = new JSONObject();
		JSONObject lane = new JSONObject();
		JSONArray diagramArray = new JSONArray();
		JSONArray eventSubpArray = new JSONArray();
		JSONArray poolArray = new JSONArray();
		JSONArray laneArray = new JSONArray();
		
		diagramArray.put(0,"components");
		diagramArray.put(1,"interface");
		diagramArray.put(2,"interfaces");
		diagramArray.put(3,"all");
		diagram.put("contains" , diagramArray);
		diagram.put("role", "diagram");
		
		eventSubpArray.put(0, "eventSubprocessElement");
		eventSubpArray.put(1, "sequence_start");
		eventSubpArray.put(2, "sequence_end");
		eventSubprocess.put("contains",eventSubpArray);
		eventSubprocess.put("role", "EventSubProcess");
		
		poolArray.put(0, "Lane");
		pool.put("contains", poolArray);
		pool.put("role", "Pool");
		
		laneArray.put(0, "all");
		lane.put("contains", laneArray);
		lane.put("role","Lane");
				
		containmentRules.put(0,diagram);
		containmentRules.put(1,eventSubprocess);
		containmentRules.put(2,pool);
		containmentRules.put(3,lane);
		//MORPHING RULES
		
		Iterator<FionaComponent> compListIterator3 = compList.iterator();
		i=0;
		while(compListIterator3.hasNext()){
			int k=0;
			FionaComponent auxComp = compListIterator3.next();
			JSONObject morphingRulesComp = new JSONObject();
			JSONArray morphingRulesCompArray = new JSONArray();
			
			Iterator<String> ifacesIterator = auxComp.getComponentIfaces().iterator();
			while(ifacesIterator.hasNext()){
				morphingRulesCompArray.put(k, ifacesIterator.next());
				k++;
			}
			
			morphingRulesComp.put("role", auxComp.getComponentName() + "Morph");
			morphingRulesComp.put("baseMorphs", morphingRulesCompArray);
			morphingRules.put(i, morphingRulesComp);
			i++;
		}
		
		//LAYOUT RULES
		
		JSONObject layoutRulesObject = new JSONObject();
		JSONArray layoutRulesObjectOutsArray = new JSONArray();
		JSONObject layoutRulesObjectOutsArrayObj1 = new JSONObject();
		JSONObject layoutRulesObjectOutsArrayObj2 = new JSONObject();
		JSONArray layoutRulesObjectInsArray = new JSONArray();
		JSONObject layoutRulesObjectInsArrayObj1 = new JSONObject();
		JSONObject layoutRulesObjectInsArrayObj2 = new JSONObject();
		
		layoutRulesObjectOutsArrayObj1.put("edgeRole","MessageFlow");
		layoutRulesObjectOutsArrayObj1.put("t","2");
		layoutRulesObjectOutsArrayObj1.put("r","1");
		layoutRulesObjectOutsArrayObj1.put("b","2");
		layoutRulesObjectOutsArrayObj1.put("l","1");
		
		layoutRulesObjectOutsArrayObj2.put("t","1");
		layoutRulesObjectOutsArrayObj2.put("r","2");
		layoutRulesObjectOutsArrayObj2.put("b","1");
		layoutRulesObjectOutsArrayObj2.put("l","2");
		
		layoutRulesObjectOutsArray.put(0, layoutRulesObjectOutsArrayObj1);
		layoutRulesObjectOutsArray.put(1, layoutRulesObjectOutsArrayObj2);
		layoutRulesObject.put("outs", layoutRulesObjectOutsArray);
		
		
		layoutRulesObjectInsArrayObj1.put("edgeRole","MessageFlow");
		layoutRulesObjectInsArrayObj1.put("t","2");
		layoutRulesObjectInsArrayObj1.put("r","1");
		layoutRulesObjectInsArrayObj1.put("b","2");
		layoutRulesObjectInsArrayObj1.put("l","1");
		
		layoutRulesObjectInsArrayObj2.put("t","1");
		layoutRulesObjectInsArrayObj2.put("r","2");
		layoutRulesObjectInsArrayObj2.put("b","1");
		layoutRulesObjectInsArrayObj2.put("l","2");
		
		layoutRulesObjectInsArray.put(0,layoutRulesObjectInsArrayObj1);
		layoutRulesObjectInsArray.put(1,layoutRulesObjectInsArrayObj2);
		layoutRulesObject.put("ins", layoutRulesObjectInsArray);
		
		layoutRulesObject.put("role", "ActivitiesMorph");
		
		layoutRules.put(0, layoutRulesObject);
		
		rules.put("cardinalityRules", cardinalityRules);
		rules.put("connectionRules",connectionRules);
		rules.put("containmentRules",containmentRules);
		rules.put("morphingRules",morphingRules);
		rules.put("layoutRules",layoutRules);
		
		return rules;
	}
	
}//fin class TODO nullificar variables

