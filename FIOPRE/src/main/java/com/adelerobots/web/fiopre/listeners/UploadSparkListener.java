package com.adelerobots.web.fiopre.listeners;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.custom.fileupload.UploadedFile;

import com.adelerobots.web.fiopre.utilidades.ConfigUtils;
import com.adelerobots.web.fiopre.utilidades.ContextUtils;
import com.treelogic.fawna.arq.negocio.contextos.implementation.ContextoImplementationPresentacion;
import com.treelogic.fawna.arq.negocio.contextos.implementation.RegistroImplementationPresentacion;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IRegistro;
import com.treelogic.fawna.presentacion.componentes.component.html.command.interfaces.IUploadFile;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorDatosComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.GestorEstadoComponentes;
import com.treelogic.fawna.presentacion.componentes.event.api.SNInvoker;
import com.treelogic.fawna.presentacion.componentes.render.input.UploadFileInfo;
import com.treelogic.fawna.presentacion.core.exception.FactoriaDatosException;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.persistencia.DatoFawnaFactory;
import com.treelogic.fawna.presentacion.core.persistencia.IDatosFawna;
import com.treelogic.fawna.presentacion.core.utilidades.FawnaPropertyConfiguration;


public class UploadSparkListener implements IUploadFile{

	private static final long serialVersionUID = 5479156947970214268L;
	
	public static final String INTERFACES_CTX = "FIONEGN021";
	public static final String CTX_INTERFACES_REG = "FIONEG021010";	
	public static final String CTX_INTERFACES_REG_ID = "FIONEG021010040";
	
	public static final String CONFIGPARAM_CTX = "FIONEGN020";
	public static final String CTX_CONFIGPARAM_REG = "FIONEG020010";
	public static final String CTX_CONFIGPARAM_REG_NOMBRE = "FIONEG020010010";
	public static final String CTX_CONFIGPARAM_REG_TIPO = "FIONEG020010020";
	public static final String CTX_CONFIGPARAM_REG_VALORDEFECTO = "FIONEG020010030";
	public static final String CTX_CONFIGPARAM_REG_VALORMIN = "FIONEG020010040";
	public static final String CTX_CONFIGPARAM_REG_VALORMAX = "FIONEG020010050";
	public static final String CTX_CONFIGPARAM_REG_CONFIGURABLE = "FIONEG020010060";
	
	
	String FILEFAW = FawnaPropertyConfiguration.getInstance().getProperty("conf/FIOPRE", "com.treelogic.fawna.presentacion.core.users_path");
	protected static Logger logger = Logger.getLogger(UploadSparkListener.class);
	
	
	@SuppressWarnings("unchecked")
	public void uploadFile(GestorEstadoComponentes gestorEstados,
			GestorDatosComponentes gestorDatos, Map<String, IUploadFile> ficheros) {
		
		String auxString = ficheros.keySet().toString();
			
		UploadFileInfo filec = (UploadFileInfo) ficheros.get("sparkC");
		UploadedFile fileInC =  filec.getFile();
		
		/* Recuperar datos proporcionados por el usuario sobre el spark a subir */
		String sparkName = (String) gestorDatos.getValue("sparkName");
		String sparkVersion = (String) gestorDatos.getValue("sparkVersion");
		String longDescription = (String) gestorDatos.getValue("longDescription");
		String shortDescription = (String) gestorDatos.getValue("shortDescription");
		String supportEmail = (String) gestorDatos.getValue("supportEmail");
		Map <String, String> providedInterfaces = gestorDatos.getItems("provided_interfaces");
		Map <String, String> requiredInterfaces = gestorDatos.getItems("required_interfaces");
		List<Map<String, String>> listConfigParams = (List<Map<String, String>>)gestorDatos.getValue("tablaParams");
								
		String usermaild5 = ContextUtils.getUserMailD5();
		Integer userid = ContextUtils.getUserIdAsInteger();
		String fileName = filec.getFileName();
        OutputStream outputc = null;    
		
        //String sparkDir = null;
        final File outputdir = ConfigUtils.getNfsUserUploadedSparksFolder(usermaild5);
        final File packedfile = new File(outputdir, fileName);
        if (!packedfile.getAbsolutePath().startsWith(outputdir.getAbsolutePath())) {
        	//Evitamos que en fileName haya un "../" y se nos salga del uploadFolder
        	gestorDatos.setValue("uploaderr", "An upload error occurred. Error id up_999");
        	throw new SecurityException(
        			"You are trying to upload your builded avatar bundle outside parent upload folder");
        }

        try {
        	outputc = new FileOutputStream(packedfile);        
			IOUtils.copy(fileInC.getInputStream(), outputc);		
		} catch (IOException e) {
			//SN mail: el usuario tal ha intentado subir un spark sin éxito alguno, pubritín
			gestorDatos.setValue("uploaderr", "An upload error occurred. Error id up_007");
			e.printStackTrace();
		}  finally {
            IOUtils.closeQuietly(outputc);
		}
		
		String sparkPath = outputdir.getAbsolutePath();
		try {
			// Subir el Spark a la carpeta 'UploadedSparks' del usuario y verificar su estructura
			String uploadRes = invokeValidateUpload(userid, sparkPath, fileName, sparkName);
			
			if (uploadRes.contains("structure OK")){
				clearUploadMessages(gestorDatos, gestorEstados);				
				setOutputMessage("genericinfo", "The compressed file structure is OK!", gestorDatos, gestorEstados);
				
				/** Invocar siguientes SN **/
				// Crear el spark en BB.DD
				Integer sparkid = invokeCreateSpark(sparkName,userid,longDescription,sparkVersion,shortDescription,supportEmail);
				// Cambiar el estado a 'Uploaded'
				invokeChangeSparkStatus(sparkid,1,userid);
				
				// Introducir interfaces del spark
				if(!providedInterfaces.isEmpty() || !requiredInterfaces.isEmpty()){
					IContexto ctxEntradaInterfaces = new ContextoImplementationPresentacion(INTERFACES_CTX);			
					IRegistro[] registros = null;	
					registros = new IRegistro[providedInterfaces.size()+requiredInterfaces.size()];

					int i = 0;
					if(providedInterfaces != null){
						for (Map.Entry<String, String> elemento : providedInterfaces.entrySet()) {
							System.out.println(elemento.getKey() + " _ " + elemento.getValue());

							IRegistro registroInterfaz = new RegistroImplementationPresentacion(CTX_INTERFACES_REG);
							registroInterfaz.put(CTX_INTERFACES_REG_ID, new BigDecimal(elemento.getValue())); //ojo que ahí espera el reg por un int
							registros[i] = registroInterfaz;
							i++;
						}
					}
					if(requiredInterfaces != null){
						for (Map.Entry<String, String> elemento : requiredInterfaces.entrySet()) {
							System.out.println(elemento.getKey() + " _ " + elemento.getValue());

							IRegistro registroInterfaz = new RegistroImplementationPresentacion(CTX_INTERFACES_REG);
							registroInterfaz.put(CTX_INTERFACES_REG_ID, new BigDecimal(elemento.getValue()));
							registros[i] = registroInterfaz;
							i++;
						}
					}	
					ctxEntradaInterfaces.put(CTX_INTERFACES_REG, registros);					
					//Invocamos el SN para asignar las interfaces al spark			
					invokeInsertSparkInterfaces(sparkid,ctxEntradaInterfaces);		
				}

				// Introducir parámetros de configuración del spark				
				if(!listConfigParams.isEmpty()){
					IContexto ctx = new ContextoImplementationPresentacion(CONFIGPARAM_CTX);

					IRegistro[] registros = null;    
					int listSize = listConfigParams.size();
					registros = new IRegistro[listSize];
					for (int i = 0; i < listSize; i++) {
						IRegistro registroConfigParam = new RegistroImplementationPresentacion(CTX_CONFIGPARAM_REG);
						registroConfigParam.put(CTX_CONFIGPARAM_REG_NOMBRE, listConfigParams.get(i).get("param_nombre"));
						registroConfigParam.put(CTX_CONFIGPARAM_REG_TIPO, new BigDecimal(listConfigParams.get(i).get("param_type_id"))); 
						String defaultParamValue = listConfigParams.get(i).get("param_default");
						registroConfigParam.put(CTX_CONFIGPARAM_REG_VALORDEFECTO, defaultParamValue);
						// De momento como valor máximo y mínimo se mete el mismo que el valor por defecto
						registroConfigParam.put(CTX_CONFIGPARAM_REG_VALORMIN, defaultParamValue);
						registroConfigParam.put(CTX_CONFIGPARAM_REG_VALORMAX, defaultParamValue);
						// El parámetro de configuración que introduzca el desarrollador será configurable por el usuario (en principio)
						registroConfigParam.put(CTX_CONFIGPARAM_REG_CONFIGURABLE, new BigDecimal(1));
						registros[i] = registroConfigParam;
					}
					ctx.put(CTX_CONFIGPARAM_REG, registros);		
					//Invocamos el SN para crear los parámetros de configuración del nuevo spark
					invokeInsertSparkConfigParams(sparkid, ctx);
					listConfigParams.clear();
					gestorDatos.setValue("tablaParams",listConfigParams);
				}				
				
				// Si todo fue bien, reseteo tablas, etc, y muestro mensaje 'success'
				finishSuccessfulUpload(gestorDatos, gestorEstados);
				
			}else if (uploadRes.contains("wrong innerfolder")){
				clearUploadMessages(gestorDatos, gestorEstados);				
				setOutputMessage("genericinfo", "The compressed folder name must be the same as the spark!", gestorDatos, gestorEstados);
				gestorDatos.setValue("infoError", "The compressed folder name must be the same as the spark!");
				gestorEstados.openModalAlert("alertError");
			}else{
				clearUploadMessages(gestorDatos, gestorEstados);
				if (uploadRes.contains("Binary not found")
						&& uploadRes.contains("Source not found")
						&& uploadRes.contains("Headers not found")){
					setOutputMessage("genericinfo", "You must upload either a /bin folder or /src and /include folders", gestorDatos, gestorEstados);
				}else{
					setOutputMessage("genericinfo", "Something went wrong..", gestorDatos, gestorEstados);
				}								
				gestorEstados.openModalAlert("alertError");				
				StringBuffer alertErrorMessage = new StringBuffer();
				if (uploadRes.contains("Binary not found")){	
					setOutputMessage("binfound", "Binary file not found!", gestorDatos, gestorEstados);
					alertErrorMessage.append("*Binary file not found ");
				}
				if (uploadRes.contains("Source not found")){
					setOutputMessage("srcfound", "Source files not found!", gestorDatos, gestorEstados);
					alertErrorMessage.append("*Source files not found ");
				}
				if (uploadRes.contains("Headers not found")){
					setOutputMessage("headersfound", "Headers files not found!", gestorDatos, gestorEstados);
					alertErrorMessage.append("*Headers files not found ");
				}
				if (uploadRes.contains("Config not found")){
					setOutputMessage("configfound", "Config not found!", gestorDatos, gestorEstados);
					alertErrorMessage.append("*Config not found ");
				}
				if (uploadRes.contains("Icon not found")){
					setOutputMessage("iconfound", "Icon not found!", gestorDatos, gestorEstados);
					alertErrorMessage.append("*Icon not found ");
				}
				if (uploadRes.contains("README not found")){
					setOutputMessage("readmefound", "README file not found", gestorDatos, gestorEstados);
					alertErrorMessage.append("*README file not found ");
				}
				gestorDatos.setValue("infoError", alertErrorMessage.toString());
			}
			
		} catch (FactoriaDatosException e1) {
			clearUploadMessages(gestorDatos, gestorEstados);
			setOutputMessage("genericinfo", "An error occurred while saving the file: Error code USL01", gestorDatos, gestorEstados);			
			FileUtils.deleteQuietly(packedfile);
		} catch (PersistenciaException e) {
			clearUploadMessages(gestorDatos, gestorEstados);
			setOutputMessage("genericinfo", "An error occurred while saving the file: Error code USL02", gestorDatos, gestorEstados);			
			FileUtils.deleteQuietly(packedfile);
		} catch (FawnaInvokerException e) {
			clearUploadMessages(gestorDatos, gestorEstados);
			setOutputMessage("genericinfo", "An error occurred while saving the file: Error code USL03", gestorDatos, gestorEstados);			
			FileUtils.deleteQuietly(packedfile);			
		}
	}
	
	void clearUploadMessages(GestorDatosComponentes gestorDatos, GestorEstadoComponentes gestorEstados)
	{
		gestorDatos.setValue("genericinfo", "");	
		gestorEstados.setPropiedad("genericinfoDiv", "rendered", Boolean.FALSE);
		gestorDatos.setValue("binfound", "");
		gestorEstados.setPropiedad("binfoundDiv", "rendered", Boolean.FALSE);
		gestorDatos.setValue("srcfound", "");
		gestorEstados.setPropiedad("srcfoundDiv", "rendered", Boolean.FALSE);
		gestorDatos.setValue("headersfound", "");		
		gestorEstados.setPropiedad("headersfoundDiv", "rendered", Boolean.FALSE);
		gestorDatos.setValue("configfound", "");
		gestorEstados.setPropiedad("configfoundDiv", "rendered", Boolean.FALSE);
		gestorDatos.setValue("iconfound", "");
		gestorEstados.setPropiedad("iconfoundDiv", "rendered", Boolean.FALSE);
		gestorDatos.setValue("readmefound", "");
		gestorEstados.setPropiedad("readmefoundDiv", "rendered", Boolean.FALSE);
	}
	
	void setOutputMessage(String output, String text, GestorDatosComponentes gestorDatos, GestorEstadoComponentes gestorEstados)
	{
		gestorDatos.setValue(output, text);	
		gestorEstados.setPropiedad(output+"Div", "rendered", Boolean.TRUE);		
	}
	
	void finishSuccessfulUpload(GestorDatosComponentes gestorDatos, GestorEstadoComponentes gestorEstados)
	{
		//gestorDatos.setValue("tablaParams",null);		
		gestorDatos.setItemsMap("provided_interfaces", null);
		gestorDatos.setItemsMap("required_interfaces", null);
		gestorDatos.setValue("sparkName", "");
		gestorDatos.setValue("sparkVersion", "");
		gestorDatos.setValue("longDescription", "");
		gestorDatos.setValue("shortDescription", "");
		gestorDatos.setValue("supportEmail", "");		
		
		gestorDatos.setValue("infoSuccess", "Your spark was successfully uploaded! Validation process starts");
		gestorEstados.openModalAlert("alertSuccess");
	}
	
	void deleteSparkFolder(String SparkName, String mailmd5){
				
	}
	
	/**
	 * Invocar a SN029008 para validar la estructura del fichero comprimido
	 * que sube el usuario
	 * 
	 * @param userid - identificador del usuario
	 * @param sparkPath - directorio personal para subir sparks
	 * @param fileName - nombre completo del fichero comprimido
	 * @param sparkName - nombre del spark 
	 * @return respuesta sobre cómo ha ido el proceso
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected String invokeValidateUpload(
			final Integer userid,
			final String sparkPath,
			final String fileName,
			final String sparkName)
	throws FactoriaDatosException, 
			PersistenciaException,
			FawnaInvokerException 
	{
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();		
		datosEntrada.putDato("0", userid, "BigDecimal");
		datosEntrada.putDato("1", sparkPath, "String");
		datosEntrada.putDato("2", fileName, "String");
		datosEntrada.putDato("3", sparkName, "String");
		Object dato = invoker.invokeSNParaCampo("029", "008", datosEntrada, "FIONEG008010", false);
		
		return dato == null ? null : dato.toString();
	}
	
	/**
	 * Invocar a SN029047 para crear un spark
	 * 
	 * @param sparkName - nombre del spark
	 * @param userid - identificador del usuario
	 * @param longDescription - 
	 * @param sparkVersion -   
	 * @param shortDescription - 
	 * @param supportEmail - 
	 * 
	 * @return 
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected Integer invokeCreateSpark(
			final String sparkName,
			final Integer userid,			
			final String longDescription,
			final String sparkVersion,
			final String shortDescription,
			final String supportEmail)
	throws FactoriaDatosException, 
			PersistenciaException,
			FawnaInvokerException 
	{
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();		
		datosEntrada.putDato("0", sparkName, "String");
		datosEntrada.putDato("1", userid, "BigDecimal");
		datosEntrada.putDato("2", longDescription, "String");
		datosEntrada.putDato("3", sparkVersion, "String");
		datosEntrada.putDato("4", shortDescription, "String");
		datosEntrada.putDato("5", supportEmail, "String");
		IContexto[] datos = invoker.invokeSNParaContextos("029", "047", datosEntrada, false);
		
		return datos[0].getBigDecimal("FIONEG004010").intValue();
	}
	
	/**
	 * Invocar a SN029017 para cambiar el estado de un spark
	 * 
	 * @param sparkid - 
	 * @param status - 
	 * @param userid - 
	 * 
	 * @return 
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected void invokeChangeSparkStatus(
			final Integer sparkid,
			final Integer status,			
			final Integer userid)
	throws FactoriaDatosException, 
			PersistenciaException,
			FawnaInvokerException 
	{
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();		
		datosEntrada.putDato("0", sparkid, "BigDecimal");
		datosEntrada.putDato("1", status, "BigDecimal");
		datosEntrada.putDato("2", userid, "BigDecimal");
		
		invoker.invokeSNParaCampo("029", "017", datosEntrada, null, false);
	}
	
	/**
	 * Invocar a SN029053 para insertar parámetros de configuración
	 * 
	 * @param ctx - Contexto con los datos de los parámetros
	 *  
	 * @return Contexto 
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected IContexto[] invokeInsertSparkConfigParams(Integer intCodSpark, final IContexto ctx)
	throws FactoriaDatosException, 
	PersistenciaException,
	FawnaInvokerException 
	{
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();		
		datosEntrada.putDato("0", intCodSpark, "BigDecimal");
		datosEntrada.putDato("1", ctx, "Contexto");
				
		IContexto[] datos = invoker.invokeSNParaContextos("029", "053", datosEntrada, false);			
		return datos;		
	}
	
	/**
	 * Invocar a SN029052 para asignar interfaces a un spark
	 * 
	 * @param intCodSpark
	 * @param ctx - Contexto con las interfaces
	 *  
	 * @return Contexto 
	 * @throws FactoriaDatosException
	 * @throws PersistenciaException
	 * @throws FawnaInvokerException
	 */
	protected IContexto[] invokeInsertSparkInterfaces(Integer intCodSpark, final IContexto ctx)
	throws FactoriaDatosException, 
	PersistenciaException,
	FawnaInvokerException 
	{
		final SNInvoker invoker = new SNInvoker();
		IDatosFawna datosEntrada;
		datosEntrada = DatoFawnaFactory.getDatoFawna();		
		datosEntrada.putDato("0", intCodSpark, "BigDecimal");
		datosEntrada.putDato("1", ctx, "Contexto");
				
		IContexto[] datos = invoker.invokeSNParaContextos("029", "052", datosEntrada, false);			
		return datos;		
	}
}

