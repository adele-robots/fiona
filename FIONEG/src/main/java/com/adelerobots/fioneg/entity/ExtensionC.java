package com.adelerobots.fioneg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.treelogic.fawna.arq.negocio.persistencia.datos.DataC;

/**
 * Clase que mapea las operaciones con hibernate
 * para la tabla "extension" de la base de datos
 * 
 * @author adele
 *
 */
@Entity(name = "extension")
@Table(name = "extension")
@org.hibernate.annotations.Proxy(lazy = true)
public class ExtensionC extends DataC {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_extension", nullable = false)
	private Integer intCodExtenxion;
	
	@Column(name="dc_propiedad")
	private String strPropiedad;
	
	@ManyToOne (targetEntity = SparkC.class)
	@JoinColumn(name = "cn_spark", insertable=false, updatable=false)
	private SparkC spark;
	
	public ExtensionC(){
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
				+ ((this.intCodExtenxion == null) ? 0 : this.intCodExtenxion
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

			final ExtensionC other = (ExtensionC) obj;

			if (this.intCodExtenxion == null) {

				if (other.intCodExtenxion != null) {

					bEqual = false;

				}

			}

			else if (!this.intCodExtenxion.equals(other.intCodExtenxion)) {

				bEqual = false;

			}

		}

		return bEqual;

	}

	public Integer getIntCodExtenxion() {
		return intCodExtenxion;
	}

	public void setIntCodExtenxion(Integer intCodExtenxion) {
		this.intCodExtenxion = intCodExtenxion;
	}

	public String getStrPropiedad() {
		return strPropiedad;
	}

	public void setStrPropiedad(String strPropiedad) {
		this.strPropiedad = strPropiedad;
	}

	public SparkC getSpark() {
		return spark;
	}

	public void setSpark(SparkC spark) {
		this.spark = spark;
	}	
	
	
	

}
