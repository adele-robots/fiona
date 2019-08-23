package com.adelerobots.fioneg.manager;

import com.adelerobots.fioneg.engine.PriceEng;
import com.adelerobots.fioneg.entity.PriceC;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.entity.UnitC;
import com.adelerobots.fioneg.entity.UtilizationC;

public class PriceManager {
	
	private String conexion;

	public PriceManager(String conexion) {
		super();
		this.conexion = conexion;
	}
	
	/**
	 * Método que permite insertar un nuevo objeto
	 * PriceC
	 * @param precio Nuevo precio a insertar en BBDD
	 */
	public void insertarPrecio(PriceC precio){
		PriceEng priceDAO = new PriceEng(conexion);
		
		priceDAO.persist(precio);
		priceDAO.flush();
	}
	
	/**
	 * Método que permite modifica un objeto PriceC existente en la BBDD
	 * @param precioAModificar objeto con los nuevos valores
	 * @return Se devuelve el objeto recién modificado
	 */
	public PriceC modificarPrecio(PriceC precioAModificar){	
		PriceEng priceDAO = new PriceEng(conexion);
		
		PriceC precioModificable = priceDAO.findById(precioAModificar.getIntCodPrice());
		
		precioModificable.setChDevelopment(precioAModificar.getChDevelopment());
		precioModificable.setIntAmount(precioAModificar.getIntAmount());
		precioModificable.setFloPrize(precioAModificar.getFloPrize());
		precioModificable.setIntUtilization(precioAModificar.getIntUtilization());
		precioModificable.setIntUsrConcu(precioAModificar.getIntUsrConcu());
		precioModificable.setSpark(precioAModificar.getSpark());
		precioModificable.setUnit(precioAModificar.getUnit());
		precioModificable.setUtilization(precioAModificar.getUtilization());
		
		priceDAO.update(precioModificable);
		priceDAO.flush();
		
		return 	precioModificable;
	}
	
	/**
	 * Método que devuelve el precio asociado a un spark
	 * @param intCodSpark Identificador único del spark
	 * @return Se devuelve el precio que coincida con
	 * los criterios de búsqueda
	 */
	public PriceC getSparkPrice(Integer intCodSpark){
		PriceEng priceDAO = new PriceEng(conexion);
		
		return priceDAO.getSparkPrice(intCodSpark);
	}
	
	
	/**
	 * Método que permite insertar en BBDD un nuevo precio asociado a un determinado spark
	 * @param intNumUsuarios Número de usuarios concurrentes
	 * @param intIdUnidadTiempo Identificador de la unidad de tiempo a utilizar
	 * @param intIdUnidadUso Identificador de la unidad de uso a utilizar
	 * @param floCantidad Cantidad de la unidad elegida (de tiempo o de uso)
	 * @param floEuros Precio real
	 * @param spark Spark al que debe asociarse el precio
	 * @param esDesarrollo Cadena que indica si el precio a introducir es de desarrollo o de
	 * producción
	 * @return
	 */
	public PriceC addPriceToSpark(Integer intNumUsuarios, Integer intIdUnidadTiempo, Integer intIdUnidadUso, Integer intCantidad, Float floEuros, SparkC spark, String strEsDesarrollo){
				
		PriceC price = new PriceC();		
		price.setSpark(spark);
		price.setIntUsrConcu(intNumUsuarios);
		
		if(strEsDesarrollo != null && !"".equals(strEsDesarrollo)){
			if("1".equals(strEsDesarrollo)){
				price.setChDevelopment('1');
			}else{
				price.setChDevelopment('0');
			}
		}else{
			price.setChDevelopment('0');
		}
		price.setChActivo('1');
		price.setFloPrize(floEuros);
		UnitC unidadTiempo = null;
		if(intIdUnidadTiempo != null){
			unidadTiempo = new UnitC();
			unidadTiempo.setIntCodUnit(intIdUnidadTiempo);
			price.setUnit(unidadTiempo);
			price.setIntAmount(intCantidad);
		}else{
			price.setUnit(null);
			price.setIntAmount(null);
		}
		
		UtilizationC unidadUso = null;
		if(intIdUnidadUso != null){
			unidadUso = new UtilizationC();
			unidadUso.setIntCodUtilization(intIdUnidadUso);
			price.setUtilization(unidadUso);
			price.setIntUtilization(intCantidad);
		}else{
			price.setUtilization(null);
			price.setIntUtilization(null);
		}
		
		insertarPrecio(price);
		
		return price;
		
		
	}
	
	/**
	 * Método que "borra" un spark marcándolo como inactivo
	 * @param intCodPrecio Identificador único del precio
	 * @param intCodSpark Identificador único del spark al que
	 * está asociado el precio
	 */
	public PriceC deletePrice(Integer intCodPrecio, Integer intCodSpark){
		PriceEng priceDAO = new PriceEng(conexion);
		
		PriceC precio = priceDAO.getSparkPrice(intCodPrecio, intCodSpark);
		
		precio.setChActivo('0');
		
		priceDAO.update(precio);
		
		return precio;
	}

}
