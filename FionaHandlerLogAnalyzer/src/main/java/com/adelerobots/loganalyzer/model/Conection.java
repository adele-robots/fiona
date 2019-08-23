package com.adelerobots.loganalyzer.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.sql.Timestamp;

import com.mkyong.analysis.location.mode.ServerLocation;

public class Conection {
	private Timestamp fechaConexion;
	private List<Answer> respuestas = new ArrayList<Answer>();
	private Integer numFacilitadas;
	private Integer numFallidas;
	private Integer numIncorrectas;
	private String dialogo;
	private boolean status;
	private String md5;
	private String ip;
	private String country;
	private String city;
	private String region;
	private ServerLocation sl;
	

	public static final boolean ACEPTADO = true;
	public static final boolean RECHAZADO = false;

	public Conection(String ip, String md5, Timestamp fechaConexion, boolean status) {
		super();
		this.md5 = md5;
		this.fechaConexion = fechaConexion;
		this.status = status;
		this.numFacilitadas = 0;
		this.numFallidas = 0;
		this.numIncorrectas = 0;
		this.setDialogo("");
		this.status = status;
		this.sl = new ServerLocation();
		this.ip = ip;
		this.country = "";
		this.city = "";
		this.region = "";
	}
	
	
	public Timestamp getFechaConexion() {
		return fechaConexion;
	}


	public void setFechaConexion(Timestamp fechaConexion) {
		this.fechaConexion = fechaConexion;
	}


	public void setRespuestas(List<Answer> respuestas) {
		this.respuestas = respuestas;
	}


	public void setNumFacilitadas(Integer numFacilitadas) {
		this.numFacilitadas = numFacilitadas;
	}


	public void setNumFallidas(Integer numFallidas) {
		this.numFallidas = numFallidas;
	}


	public void setNumIncorrectas(Integer numIncorrectas) {
		this.numIncorrectas = numIncorrectas;
	}


	public List<Answer> getRespuestas() {
		return respuestas;
	}

	public void aniadirRespuestas(Answer respuesta) {
		respuestas.add(respuesta);
	}

	public Integer getNumFacilitadas() {
		return numFacilitadas;
	}

	public void incNumFacilitadas() {
		this.numFacilitadas++;
	}

	public Integer getNumFallidas() {
		return numFallidas;
	}

	public void incNumFallidas() {
		this.numFallidas++;
	}

	public Integer getNumIncorrectas() {
		return numIncorrectas;
	}

	public void incNumIncorrectas() {
		this.numIncorrectas++;
	}

	/**
	 * @return the dialogo
	 */
	public String getDialogo() {
		return dialogo;
	}

	/**
	 * @param dialogo
	 *            the dialogo to set
	 */
	public void setDialogo(String dialogo) {
		this.dialogo = dialogo;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {

		String con = "CONEXION: " + this.country + " ," + this.city +  "\t" + fechaConexion
				+ "\t Estado: ";

		if (status) {

			String resp = "";

			Iterator<Answer> iterator = respuestas.iterator();

			while (iterator.hasNext()) {
				resp += iterator.next().toString() + "\n";
			}
			con += "ACEPTADA\nNum. Facilitadas:" + numFacilitadas
					+ "\nNum. Fallidas: " + numFallidas + " \nDetalle dialogo:";
			if (dialogo.equals("")) {
				con += "No está disponible\n" + resp;
			} else {
				con += "\n" + dialogo + "\n" + resp;
			}

		} else
			con += "RECHAZADA";

		return con;

	}

	/**
	 * @return the md5
	 */
	public String getMd5() {
		return md5;
	}

	/**
	 * @param md5
	 *            the md5 to set
	 */
	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getPais() {
		return this.sl.getCountryName();
	}

	
	public void setPais(String pais) {
		this.sl.setCountryName(pais);
	}
	
	public String getCiudad() {
		return this.sl.getCity();
	}

	
	public void setCiudad(String ciudad) {
		this.sl.setCity(ciudad);
	}
	public String getIP() {
		return ip;
	}

	public void setIP(String ip) {
		this.ip = ip;
		
	}
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
		
	}
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
		
	}
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
		
	}
	
	public void setServerLocation(ServerLocation location) {
		this.sl = location;
	}
}
