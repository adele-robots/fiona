package com.adelerobots.fioneg.entity.sparkstatus;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Clase que mapea las operaciones con hibernate
 * 
 * @author adele
 * 
 */
@Entity(name = "sparkstatus")
@Table(name = "SPARKSTATUS")
@org.hibernate.annotations.Proxy(lazy = true)
public class SparkStatusC extends com.treelogic.fawna.arq.negocio.persistencia.datos.DataC {

	
	@Id	
	private SparkStatusPk sparkStatus;

	@Column(name = "FE_CAMBIADA", nullable = true)
	private Date dateCambio;
	
	@Column(name = "FL_CAMBIOFIONA", nullable = false)
	private Character charCambioFiona = new Character('0');
	
	@Column(name = "CN_STATUS", nullable=false, updatable=false, insertable=false)
	private Integer intCodStatus;
	
	@Column(name = "CN_SPARK", nullable=false, updatable=false, insertable=false)
	private Integer intCodSpark;

	/**
	 * Constructor por defecto
	 */
	public SparkStatusC() {

	}

	/**
	 * Constructor de la clase
	 * @param dateCambio Fecha de cambio de status de un spark 
	 */
	public SparkStatusC(Date dateCambio) {
		this.dateCambio = dateCambio;
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
				+ ((this.sparkStatus == null) ? 0
						: this.sparkStatus.hashCode());
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
		} else if (obj == null) {
			bEqual = false;
		} else if (getClass() != obj.getClass()) {
			bEqual = false;
		} else {
			final SparkStatusC other = (SparkStatusC) obj;
			if (this.sparkStatus == null) {
				if (other.sparkStatus != null) {
					bEqual = false;
				}
			} else if (!this.sparkStatus
					.equals(other.sparkStatus)) {
				bEqual = false;
			}
		}
		return bEqual;
	}

	public SparkStatusPk getSparkStatus() {
		return sparkStatus;
	}

	public void setSparkStatus(SparkStatusPk sparkStatus) {
		this.sparkStatus = sparkStatus;
	}

	public Date getDateCambio() {
		return dateCambio;
	}

	public void setDateCambio(Date dateCambio) {
		this.dateCambio = dateCambio;
	}

	public Character getCharCambioFiona() {
		return charCambioFiona;
	}

	public void setCharCambioFiona(Character charCambioFiona) {
		this.charCambioFiona = charCambioFiona;
	}

	public Integer getIntCodStatus() {
		return intCodStatus;
	}

	public void setIntCodStatus(Integer intCodStatus) {
		this.intCodStatus = intCodStatus;
	}

	public Integer getIntCodSpark() {
		return intCodSpark;
	}

	public void setIntCodSpark(Integer intCodSpark) {
		this.intCodSpark = intCodSpark;
	}
		

}
