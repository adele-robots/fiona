package com.adelerobots.fioneg.dataclasses;

import java.util.List;


/**
 * Clase para representar el nombre de un avatar y su precio total,
 * además de los precios de los sparks que lo forman.
 * Destinada a ser utilizada principalmente en las plantillas
 * de facturas de freemarker
 * 
 * @author adele
 *
 */
public class AvatarPrice {
	
	private String nombreAvatar;
	
	private String precioTotalAvatar;
	
	private List<SparkPrice> lstSparksPrice;
	
	public AvatarPrice(){
		
	}
	
	public AvatarPrice(String nombreAvatar, String precioTotalAvatar){
		this.nombreAvatar = nombreAvatar;
		this.precioTotalAvatar = precioTotalAvatar;
	}
	
	public AvatarPrice(String nombreAvatar, String precioTotalAvatar, List<SparkPrice> lstSparksPrice){
		this.nombreAvatar = nombreAvatar;
		this.precioTotalAvatar = precioTotalAvatar;
		this.lstSparksPrice = lstSparksPrice;
	}

	public String getNombreAvatar() {
		return nombreAvatar;
	}

	public void setNombreAvatar(String nombreAvatar) {
		this.nombreAvatar = nombreAvatar;
	}

	public String getPrecioTotalAvatar() {
		return precioTotalAvatar;
	}

	public void setPrecioTotalAvatar(String precioTotalAvatar) {
		this.precioTotalAvatar = precioTotalAvatar;
	}

	public List<SparkPrice> getLstSparksPrice() {
		return lstSparksPrice;
	}

	public void setLstSparksPrice(List<SparkPrice> lstSparksPrice) {
		this.lstSparksPrice = lstSparksPrice;
	}
		
}
