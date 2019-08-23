package com.adelerobots.fioneg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.treelogic.fawna.arq.negocio.persistencia.datos.DataC;


/**
 * Clase que mapea las operaciones con hibernate
 * para la tabla "hosting" de la base de datos
 * 
 * @author adele
 *
 */
@Entity(name = "hosting")
@Table(name = "hosting")
@org.hibernate.annotations.Proxy(lazy = true)
public class HostingC extends DataC {
	
	@Id	
	@Column(name = "cn_hosting", nullable = false)
	private Integer intCodHosting;
	
	@Column(name="cn_unit", nullable=false)
	private Integer intCodUnit;
	
	@Column(name="nu_users", nullable=false)
	private Integer intUsers;
	
	@Column(name="dc_resolution", nullable=false)
	private String strResolution;
	
	@Column(name="dc_timeofuse", nullable=false)
	private String strTimeOfUse;
	
	@Column(name="fl_highavailability", nullable=true)
	private Character chHighAvailability;
		
	@Column(name="nu_fee", nullable=false)
	private Float floFee;
	
	
	public HostingC(){
		super();
	}
	
	/**
	 * Hashcode de objetos persistentes para colecciones que usen hash.
	 * Complementa el metodo {@link #equals(Object)} <br/> En persistencia dos
	 * objetos son iguales si tienen la misma Primary Key
	 * 
	 * @see java.lang.Object#hashCode()
	 * @return entero que representa el codigo hash del objeto
	 */
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((this.intCodHosting == null) ? 0 : this.intCodHosting
						.hashCode());
		return result;

	}

	/**
	 * Comparador de igualdad para objetos persistentes. <br/> En persistencia
	 * dos objetos son iguales si tienen la misma Primary Key
	 * 
	 * @param obj
	 *            Objeto a comparar
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @return verdadero si son iguales, falso si son distintos
	 */
	@Override
	public boolean equals(Object obj) {

		boolean bEqual = true;
		if (this == obj) {

			bEqual = true;

		}

		else if (obj == null) {

			bEqual = false;

		}

		else if (getClass() != obj.getClass()) {

			bEqual = false;

		}

		else {

			final HostingC other = (HostingC) obj;

			if (this.intCodHosting == null) {

				if (other.intCodHosting != null) {

					bEqual = false;

				}

			}

			else if (!this.intCodHosting.equals(other.intCodHosting)) {

				bEqual = false;

			}

		}

		return bEqual;

	}


	public Integer getIntCodHosting() {
		return intCodHosting;
	}


	public void setIntCodHosting(Integer intCodHosting) {
		this.intCodHosting = intCodHosting;
	}


	public Integer getIntCodUnit() {
		return intCodUnit;
	}


	public void setIntCodUnit(Integer intCodUnit) {
		this.intCodUnit = intCodUnit;
	}


	public Integer getIntUsers() {
		return intUsers;
	}


	public void setIntUsers(Integer intUsers) {
		this.intUsers = intUsers;
	}


	public String getStrResolution() {
		return strResolution;
	}


	public void setStrResolution(String strResolution) {
		this.strResolution = strResolution;
	}


	public String getStrTimeOfUse() {
		return strTimeOfUse;
	}


	public void setStrTimeOfUse(String strTimeOfUse) {
		this.strTimeOfUse = strTimeOfUse;
	}


	public Character getChHighAvailability() {
		return chHighAvailability;
	}


	public void setChHighAvailability(Character chHighAvailability) {
		this.chHighAvailability = chHighAvailability;
	}


	public Float getFloFee() {
		return floFee;
	}


	public void setFloFee(Float floFee) {
		this.floFee = floFee;
	}
	
	

}
