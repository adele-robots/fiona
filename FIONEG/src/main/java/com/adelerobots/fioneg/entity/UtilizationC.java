package com.adelerobots.fioneg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.treelogic.fawna.arq.negocio.persistencia.datos.DataC;
/**
 * Clase que mapea las operaciones con hibernate
 * para la tabla "utilization" de la base de datos
 * 
 * @author adele
 *
 */
@Entity(name = "utilization")
@Table(name = "utilization")
@org.hibernate.annotations.Proxy(lazy = true)
public class UtilizationC extends DataC {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_utilization", nullable = false)
	private Integer intCodUtilization;
	
	@Column(name="dc_content")
	private String strContent;
	
	
	public UtilizationC(){
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
				+ ((this.intCodUtilization == null) ? 0 : this.intCodUtilization
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

			final UtilizationC other = (UtilizationC) obj;

			if (this.intCodUtilization == null) {

				if (other.intCodUtilization != null) {

					bEqual = false;

				}

			}

			else if (!this.intCodUtilization.equals(other.intCodUtilization)) {

				bEqual = false;

			}

		}

		return bEqual;

	}

	public Integer getIntCodUtilization() {
		return intCodUtilization;
	}

	public void setIntCodUtilization(Integer intCodUtilization) {
		this.intCodUtilization = intCodUtilization;
	}

	public String getStrContent() {
		return strContent;
	}

	public void setStrContent(String strContent) {
		this.strContent = strContent;
	}
	
	

}
