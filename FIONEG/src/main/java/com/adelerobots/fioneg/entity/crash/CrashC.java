package com.adelerobots.fioneg.entity.crash;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.adelerobots.fioneg.entity.SparkC;
import com.treelogic.fawna.arq.negocio.persistencia.datos.DataC;


@Entity(name = "crash")
@Table(name = "crash")
@org.hibernate.annotations.Proxy(lazy = true)

/**
 * Clase que representa la entidad "Crash" de BBDD
 */
public class CrashC extends DataC {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_crash", nullable = false, length = 11)
	private Integer intCodCrash;
	
	@Column(name="FE_Crash")
	private Date datCrash;
	
	@Column(name="DC_Descripcion")
	private String strDescripcion;
	
	@ManyToOne(targetEntity=SparkC.class)
	@JoinColumn(name="cn_spark", nullable=false,insertable=false,updatable=false)
	private SparkC spark;
	
	
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
				+ ((this.intCodCrash == null) ? 0 : this.intCodCrash
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

			final CrashC other = (CrashC) obj;

			if (this.intCodCrash == null) {

				if (other.intCodCrash != null) {

					bEqual = false;

				}

			}

			else if (!this.intCodCrash.equals(other.intCodCrash)) {

				bEqual = false;

			}

		}

		return bEqual;

	}

	public Integer getIntCodCrash() {
		return intCodCrash;
	}

	public void setIntCodCrash(Integer intCodCrash) {
		this.intCodCrash = intCodCrash;
	}

	public Date getDatCrash() {
		return datCrash;
	}

	public void setDatCrash(Date datCrash) {
		this.datCrash = datCrash;
	}

	public String getStrDescripcion() {
		return strDescripcion;
	}

	public void setStrDescripcion(String strDescripcion) {
		this.strDescripcion = strDescripcion;
	}

	public SparkC getSpark() {
		return spark;
	}

	public void setSpark(SparkC spark) {
		this.spark = spark;
	}
	
	
	
	
	
	
	

}
