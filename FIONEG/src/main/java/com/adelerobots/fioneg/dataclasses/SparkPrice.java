package com.adelerobots.fioneg.dataclasses;

/**
 * Clase para representar el nombre de un spark y su precio
 * Destinada a ser utilizada principalmente en las plantillas
 * de facturas de freemarker
 * 
 * @author adele
 *
 */
public class SparkPrice {
	
	private String nombreSpark;
	
	private String precioSpark;	
	
	private String periodicidad;
	
	private String cantidad;
	
	private String precioSparkUnitario;
		
	private String comision;
	
	private String precioSparkSinDescuento;
	
	
	public SparkPrice(){
		
	}
	
	
	
	public SparkPrice(String nombreSpark, String precioSpark){
		this.nombreSpark = nombreSpark;
		this.precioSpark = precioSpark;	
	}
	
	public SparkPrice(String nombreSpark, String precioSpark, String cantidad){
		this.nombreSpark = nombreSpark;
		this.precioSpark = precioSpark;
		this.cantidad = cantidad;
	}

	public String getNombreSpark() {
		return nombreSpark;
	}

	public void setNombreSpark(String nombreSpark) {
		this.nombreSpark = nombreSpark;
	}

	public String getPrecioSpark() {
		return precioSpark;
	}

	public void setPrecioSpark(String precioSpark) {
		this.precioSpark = precioSpark;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public String getPeriodicidad() {
		return periodicidad;
	}

	public void setPeriodicidad(String periodicidad) {
		this.periodicidad = periodicidad;
	}

	public String getPrecioSparkUnitario() {
		return precioSparkUnitario;
	}

	public void setPrecioSparkUnitario(String precioSparkUnitario) {
		this.precioSparkUnitario = precioSparkUnitario;
	}

	public String getComision() {
		return comision;
	}

	public void setComision(String comision) {
		this.comision = comision;
	}

	public String getPrecioSparkSinDescuento() {
		return precioSparkSinDescuento;
	}

	public void setPrecioSparkSinDescuento(String precioSparkSinDescuento) {
		this.precioSparkSinDescuento = precioSparkSinDescuento;
	}
	
	
}
