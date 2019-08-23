package com.adelerobots.fioneg.manager;

import java.util.List;

import com.adelerobots.fioneg.engine.SparkStatusEng;
import com.adelerobots.fioneg.engine.StatusEng;
import com.adelerobots.fioneg.entity.sparkstatus.SparkStatusC;
import com.adelerobots.fioneg.entity.status.StatusC;

public class StatusManager {

	private String conexion;

	public StatusManager(String conexion) {
		super();
		this.conexion = conexion;
	}
	
	
	/**
	 * Método que devuelve la información de un estado concreto
	 * 
	 * @param cnStatus Identificador único del estado
	 * @return Se devolverá el objeto de tipo "StatusC" cuyo
	 * id coincida con el pasado como parámetro
	 */
	public StatusC getStatus(Integer cnStatus) {
		StatusEng statusDao = new StatusEng (conexion);
		
		return statusDao.getStatus(cnStatus);
	}
	
	/**
	 * Método que devuelve todos los estados posibles
	 * por los que pasa un spark
	 * 
	 * @return	 
	 */
	public List<StatusC> getAllStatus (){
		StatusEng statusDao = new StatusEng (conexion);
		
		
		return statusDao.getAllStatus();		
		
	}
	
	/**
	 * Método que devuelve la lista de estados por los que
	 * ha pasado un spark
	 * 
	 * @param intCodSpark Identificador único del spark
	 * @return Se devuelve la lista de StatusC del
	 * spark ordenada decrecientemente por fecha
	 */
	public List<StatusC> getListStatus(Integer intCodSpark){
		StatusEng statusDao = new StatusEng(conexion);
		
		return statusDao.getListStatusBySpark(intCodSpark);
	}
	
	/**
	 * Método que devuelve la lista de estados por los que
	 * ha pasado un spark
	 * 
	 * @param intCodSpark Identificador único del spark
	 * @return Se devuelve la lista de SparkStatusC del
	 * spark ordenada decrecientemente por fecha
	 */
	public List<SparkStatusC> getListSparkStatus(Integer intCodSpark){
		SparkStatusEng sparkStatusDao = new SparkStatusEng(conexion);
		
		return sparkStatusDao.getListSparkStatus(intCodSpark);
	}
	
}