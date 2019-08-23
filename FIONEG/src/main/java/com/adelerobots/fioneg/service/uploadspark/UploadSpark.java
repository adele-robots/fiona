package com.adelerobots.fioneg.service.uploadspark;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;

import com.adelerobots.fioneg.context.ContextoUploadSpark;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.core.IDatosEntradaTx;
import com.treelogic.fawna.arq.negocio.core.ServicioNegocio;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;


public class UploadSpark extends ServicioNegocio{
	
	private static final int CTE_POSICION_TIRA_USERID = 0;
	private static final int CTE_POSICION_TIRA_SPARKPATH= 1;
	private static final int CTE_POSICION_TIRA_FILENAME= 2;
	private static final int CTE_POSICION_TIRA_SPARKNAME= 3;
	private String uploadInfo = "";
	private String dirToExtract = "dirToExtractFiles";

	private static FawnaLogHelper LOGGER = FawnaLogHelper.getLog(UploadSpark.class);
	
	public UploadSpark(){
		super();
	}
	
	@Override
	public IContexto[] ejecutar(IContextoEjecucion contexto, IDatosEntradaTx datosEntrada) {
		LOGGER.info("Inicio Ejecucion del SN 029008: Validar Subida sparks");

		IContexto[] salida = null;
	
		Integer userid = null;
		final BigDecimal decUserid = datosEntrada.getDecimal(CTE_POSICION_TIRA_USERID);
		if(decUserid != null){
			userid = new Integer(decUserid.intValue());
		}	
		final String sparkPath = datosEntrada.getString(CTE_POSICION_TIRA_SPARKPATH);
		final String fileName = datosEntrada.getString(CTE_POSICION_TIRA_FILENAME);
		final String sparkName = datosEntrada.getString(CTE_POSICION_TIRA_SPARKNAME);
		
		String[] parseSparkPath = sparkPath.split("/");
		String userMd5 = parseSparkPath[parseSparkPath.length-2];
		
		File sparkInfoJsonPath = null;
		File outputdir = new File(sparkPath+"/"+dirToExtract);
		File packedfile = new File(sparkPath, fileName);
	    String sparkDir = null;
		int fileXtension = 0;
        String parseFilename[] = fileName.split("\\.(?=[^\\.]+$)");
        String fileNameNoExt = parseFilename[0];
        String extension = parseFilename[1];

        if (fileNameNoExt.endsWith(".tar")){
        	fileNameNoExt = fileNameNoExt.substring(0, fileNameNoExt.length()-4);        	
        	extension = "tar." + extension;
        }               
        if (extension.equals("rar") )
        	fileXtension=1;
        else if(extension.equals("zip") )
        	fileXtension=2;
        else if(extension.equals("tar.gz") )
        	fileXtension=4;
        else if(extension.equals("tar.bz2") )
        	fileXtension=5;
        else if(extension.equals("tar") )
        	fileXtension=3;        
		
        switch (fileXtension)
        {
        case 1:
        	//rar			
        	break;

        case 2:
        	//zip
        	//unZipIt(packedfile.getAbsolutePath(), outputdir.getAbsolutePath());
        	sparkInfoJsonPath = unzip(packedfile.getAbsolutePath(), outputdir.getAbsolutePath());
        	FileUtils.deleteQuietly(packedfile);
        	break;

        case 3:
        	//tar
        	List<File> tarlist = new LinkedList<File>();
        	//
        	try {
        		tarlist = unTar(packedfile,outputdir);
        	} catch (FileNotFoundException e) {
        		//TODO Para toda la gestión de errores, retornar contexto con uploadInfo
        		uploadInfo = "An upload error occurred. Error id up_004";
        		salida = ContextoUploadSpark.rellenarContexto(uploadInfo);
        		return salida;
        	} catch (IOException e) {
        		// TODO Auto-generated catch block
        		uploadInfo = "An upload error occurred. Error id up_005";
        		salida = ContextoUploadSpark.rellenarContexto(uploadInfo);
        		return salida;
        	} catch (ArchiveException e) {
        		// TODO Auto-generated catch block
        		uploadInfo = "An upload error occurred. Error id up_006";
        		salida = ContextoUploadSpark.rellenarContexto(uploadInfo);
        		return salida;
        	}finally{
        		FileUtils.deleteQuietly(packedfile);
        	}        	
        	break;

        case 4:
        	//tar.gz
        	File tarFromGzip = null;
        	List<File> tarlistFromGzip = new LinkedList<File>();
        	try {
        		tarFromGzip = unGzip(packedfile, outputdir);
        		tarlistFromGzip = unTar(tarFromGzip, outputdir);
        	} catch (FileNotFoundException e) {
        		uploadInfo = "An upload error occurred. Error id up_001";
        		salida = ContextoUploadSpark.rellenarContexto(uploadInfo);
        		return salida;
        	} catch (IOException e) {
        		uploadInfo = "An upload error occurred. Error id up_002";
        		salida = ContextoUploadSpark.rellenarContexto(uploadInfo);
        		return salida;
        	} catch (ArchiveException e) {
        		uploadInfo = "An upload error occurred. Error id up_003";
        		return salida;
        	}finally{        		
        		FileUtils.deleteQuietly(tarFromGzip);
        		FileUtils.deleteQuietly(packedfile);
        	}		
        	break;

        case 5:
        	//tar.bz2			
        	break;

        default:
        	//mal rollo
        	break;
        }
		
		String sparkTempFolder = outputdir + "/" + sparkName;
		File checkName = new File (sparkTempFolder);
		if (!checkName.exists()){
			uploadInfo+= " wrong innerfolder ";
			FileUtils.deleteQuietly(outputdir);
			salida = ContextoUploadSpark.rellenarContexto(uploadInfo);
			return salida;        	
		}

		if(checkSparkStructure(sparkTempFolder)){
			uploadInfo+= " structure OK ";			
			//			File[] files = checkName.listFiles();
			//			//System.out.println(Arrays.toString(strings));    	
			//			File uploadFolder = new File(sparkPath);
			//			for (File f : files) {
			//				try {
			//					FileUtils.moveFileToDirectory(f, uploadFolder, true);
			//				} catch (IOException e) {
			//					// TODO Auto-generated catch block
			//					e.printStackTrace();
			//				}
			//			}

			//renameDirectory(sparkTempFolder, sparkPath);

			String command = "/datos/script/./moveExtractedFiles.sh " + userMd5;
			try {
				Process checkFile = Runtime.getRuntime().exec(command);
				checkFile.waitFor();
				FileUtils.deleteQuietly(outputdir);
			}catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}else{
			FileUtils.deleteQuietly(outputdir);
		}			
		
		salida = ContextoUploadSpark.rellenarContexto(uploadInfo);
		return salida;
	}


	/** Untar an input file into an output file.

	 * The output file is created in the output folder, having the same name
	 * as the input file, minus the '.tar' extension. 
	 * 
	 * @param inputFile     the input .tar file
	 * @param outputDir     the output directory file. 
	 * @throws IOException 
	 * @throws FileNotFoundException
	 *  
	 * @return  The {@link List} of {@link File}s with the untared content.
	 * @throws ArchiveException 
	 */
	private static List<File> unTar(final File inputFile, final File outputDir) throws FileNotFoundException, IOException, ArchiveException {

	    final List<File> untaredFiles = new LinkedList<File>();
	    final InputStream is = new FileInputStream(inputFile); 
	    final TarArchiveInputStream debInputStream = (TarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream("tar", is);
	    TarArchiveEntry entry = null; 
	    while ((entry = (TarArchiveEntry)debInputStream.getNextEntry()) != null) {
	        final File outputFile = new File(outputDir, entry.getName());
	        if (entry.isDirectory()) {
	            if (!outputFile.exists()) {
	                if (!outputFile.mkdirs()) {
	                    throw new IllegalStateException(String.format("Couldn't create directory %s.", outputFile.getAbsolutePath()));
	                }
	            }
	        } else {
	        	if (!outputFile.getParentFile().exists())
	        		outputFile.getParentFile().mkdirs();
	            final OutputStream outputFileStream = new FileOutputStream(outputFile); 
	            IOUtils.copy(debInputStream, outputFileStream);
	            outputFileStream.close();
	        }
	        untaredFiles.add(outputFile);
	    }
	    debInputStream.close(); 

	    return untaredFiles;
	}

	/**
	 * Ungzip an input file into an output file.
	 * The output file is created in the output folder, having the same name
	 * as the input file, minus the '.gz' extension. 
	 * 
	 * @param inputFile     the input .gz file
	 * @param outputDir     the output directory file. 
	 * @throws IOException 
	 * @throws FileNotFoundException
	 *  
	 * @return  The {@File} with the ungzipped content.
	 */
	private static File unGzip(final File inputFile, final File outputDir) throws FileNotFoundException, IOException {	
		final File outputFile = new File(outputDir, inputFile.getName().substring(0, inputFile.getName().length() - 3));
		// Create temporary folder
		if (!outputDir.exists()) {
			if (!outputDir.mkdirs()) {
				throw new IllegalStateException(String.format("Couldn't create directory %s.", outputDir.getAbsolutePath()));
			}
		}
	    final GZIPInputStream in = new GZIPInputStream(new FileInputStream(inputFile));
	    final FileOutputStream out = new FileOutputStream(outputFile);

	    for (int c = in.read(); c != -1; c = in.read()) {
	        out.write(c);
	    }

	    in.close();
	    out.close();

	    return outputFile;
	}
  
    private static File unzip(String strZipFile, String outputdir) {
         File jsonFilePath= null;
         try
         {
                 /*
                  * STEP 1 : Create directory with the name of the zip file
                  *
                  * For e.g. if we are going to extract c:/demo.zip create c:/demo
                  * directory where we can extract all the zip entries
                  *
                  */
                 File fSourceZip = new File(strZipFile);
                 String zipPath = strZipFile.substring(0, strZipFile.length()-4);
                 System.out.println(zipPath + " created");
                
                 /*
                  * STEP 2 : Extract entries while creating required
                  * sub-directories
                  *
                  */
                 ZipFile zipFile = new ZipFile(fSourceZip);
                 Enumeration e = zipFile.entries();
                
                 while(e.hasMoreElements())
                 {
                         ZipEntry entry = (ZipEntry)e.nextElement();
                         File destinationFilePath = new File(outputdir,entry.getName());

                         //create directories if required.
                         destinationFilePath.getParentFile().mkdirs();
                        
                         //if the entry is directory, leave it. Otherwise extract it.
                         if(entry.isDirectory())
                         {
                                 continue;
                         }
                         else
                         {
                                 //System.out.println("Extracting " + destinationFilePath);
                                
                        	 	 if (destinationFilePath.getAbsolutePath().endsWith(".json"))
                        	 	 {
                        	 		 jsonFilePath = destinationFilePath;
                        	 	 }
                                 /*
                                  * Get the InputStream for current entry
                                  * of the zip file using
                                  *
                                  * InputStream getInputStream(Entry entry) method.
                                  */
                                 BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                                                                                                                
                                 int b;
                                 byte buffer[] = new byte[1024];

                                 /*
                                  * read the current entry from the zip file, extract it
                                  * and write the extracted file.
                                  */
                                 FileOutputStream fos = new FileOutputStream(destinationFilePath);
                                 BufferedOutputStream bos = new BufferedOutputStream(fos,
                                                                 1024);

                                 while ((b = bis.read(buffer, 0, 1024)) != -1) {
                                                 bos.write(buffer, 0, b);
                                 }
                                
                                 //flush the output stream and close it.
                                 bos.flush();
                                 bos.close();
                                
                                 //close the input stream.
                                 bis.close();
                         }
                 }
         }
         catch(IOException ioe)
         {
                 System.out.println("IOError :" + ioe);
         }
        
         return jsonFilePath;
 }
    
    private static String readFileAsString(String filePath)
    throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
    
    private Boolean checkSparkStructure(String strCompressedDir){    
    	Boolean binFound, srcFound, includeFound, configFound, iconFound, readmeFound, structureIsOk;
    	binFound = srcFound = includeFound = configFound = iconFound = readmeFound = false;
    	structureIsOk = true;
    	File compressedDir = new File(strCompressedDir);				
		String[] strings = compressedDir.list();
		System.out.println(Arrays.toString(strings));    	
		for (String s : strings) {
			if(s.equals("bin")){
				binFound = true;
			}else if(s.equals("src")){
				srcFound = true;
			}else if(s.equals("include")){
				includeFound = true;
			}else if(s.equals("config")){
				configFound = true;
			}else if(s.equals("icon")){
				iconFound = true;
			}else if(s.equals("README")){
				readmeFound = true;
			}
	    }
		
		if(!binFound && !srcFound && !includeFound){
			uploadInfo+= " Binary not found ";		
		}
		if(!srcFound && !binFound){
			uploadInfo+= " Source not found ";		
		}
		if(!includeFound && !binFound){
			uploadInfo+= " Headers not found ";		
		}
		if(!configFound){
			structureIsOk = false;
			uploadInfo+= " Config not found ";
		}
		if(!iconFound){
			structureIsOk = false;
			uploadInfo+= " Icon not found ";
		}
		if(!readmeFound){
			structureIsOk = false;
			uploadInfo+= " README not found ";
		}
		/* Si no hay binario o no hay fuentes y headers, no es válida la subida */
		if(structureIsOk && !binFound){
			if(!srcFound || !includeFound)
				structureIsOk = false;
		}
		return structureIsOk;
    }
    
    private void renameDirectory(String fromDir, String toDir) {

        File from = new File(fromDir);

        if (!from.exists() || !from.isDirectory()) {

          System.out.println("Directory does not exist: " + fromDir);
          return;
        }

        File to = new File(toDir);

        //Rename
        if (from.renameTo(to))
          System.out.println("Success!");
        else
          System.out.println("Error");     

      }	

	private List<String> listDirectory(File dirtolist){
		List<String> filelist = new ArrayList<String>();
		for (File f : dirtolist.listFiles()){
			if (f.isDirectory()){
				filelist.add(f.getAbsolutePath());
				filelist.addAll(listDirectory(f));				
			}else
				filelist.add(f.getAbsolutePath());
		}
		return filelist;
	}
}
