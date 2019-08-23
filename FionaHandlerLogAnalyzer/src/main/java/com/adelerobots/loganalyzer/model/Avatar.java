package com.adelerobots.loganalyzer.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Avatar {
	private List<Conection> conexiones = new ArrayList<Conection>();
	private String md5;
	private String name;
	private String email;
	//private Integer numAceptadas;
	//private Integer numRechazadas;
	//private Double disponibilidad;
/*
	public void setDisponibilidad(Double disponibilidad) {
		this.disponibilidad = disponibilidad;
	}*/

	public Avatar(String md5) {
		super();
		this.md5 = md5;
		this.name = md5;
		this.email = "";
	//	this.numAceptadas = 0;
		//this.numRechazadas = 0;
	}

	public Avatar(String name, String md5, String email) {
		super();
		this.name = name;
		this.md5 = md5;
		this.email = email;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Conection> getConexiones() {
		return conexiones;
	}

	public void aniadirConexion(Conection conexion) {
		conexiones.add(conexion);
	}
/*
	public Integer getNumAceptadas() {
		return numAceptadas;
	}

	public void setNumAceptadas(Integer numAceptadas) {
		this.numAceptadas = numAceptadas;
	}

	public void incNumAceptadas() {
		this.numAceptadas++;
		updateDisponibilidad();
	}

	public Integer getNumRechazadas() {
		return numRechazadas;

	}

	public void setNumRechazadas(Integer numRechazadas) {
		this.numRechazadas = numRechazadas;
	}

	public void incNumRechazadas() {
		this.numRechazadas++;
		updateDisponibilidad();
	}
*/
	/**
	 * @return the disponibilidad
	 *//*
	public Double getDisponibilidad() {
		return disponibilidad;
	}
	
	*/

	/**
	 * @param disponibilidad
	 *            the disponibilidad to set
	 *//*
	public void updateDisponibilidad() {
		int total = numAceptadas + numRechazadas;
		disponibilidad = (double) (numAceptadas * 100 / total);
	}
	*/

	@Override
	public String toString() {

		String conex = "";

		Iterator<Conection> iterator = conexiones.iterator();

		while (iterator.hasNext()) {
			conex += iterator.next().toString() + "\n";
		}

		//return "AVATAR: " + md5 + "\nNum. Aceptadas: " + numAceptadas				+ "\nNum. Rechazadas: " + numRechazadas + "\n\n" + conex				+ "********************************************";
		return "AVATAR: " + md5 + "\n\n" + conex
				+ "********************************************";
	}

}
