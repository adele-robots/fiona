package com.adelerobots.fioneg.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.treelogic.fawna.arq.negocio.persistencia.datos.DataC;

@Entity(name = "eula")
@Table(name = "eula")
@org.hibernate.annotations.Proxy(lazy = true)
public class EulaC extends DataC{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_eula", nullable = false, length = 11)
	private Integer intCodEula;
	
	@Column(name="DC_Descripcion")
	private String strDescripcion;
	
	@Column(name="DC_Contenido")
	private String strNombre;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "eula", targetEntity = SparkC.class)
	@JoinColumn(name = "CN_EULA", nullable = false)
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	private List<SparkC> lstSparkC;
	
	public EulaC(){
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
				+ ((this.intCodEula == null) ? 0 : this.intCodEula
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

			final EulaC other = (EulaC) obj;

			if (this.intCodEula == null) {

				if (other.intCodEula != null) {

					bEqual = false;

				}

			}

			else if (!this.intCodEula.equals(other.intCodEula)) {

				bEqual = false;

			}

		}

		return bEqual;

	}

	public Integer getIntCodEula() {
		return intCodEula;
	}

	public void setIntCodEula(Integer intCodEula) {
		this.intCodEula = intCodEula;
	}

	public String getStrDescripcion() {
		return strDescripcion;
	}

	public void setStrDescripcion(String strDescripcion) {
		this.strDescripcion = strDescripcion;
	}

	public String getStrNombre() {
		return strNombre;
	}

	public void setStrNombre(String strNombre) {
		this.strNombre = strNombre;
	}

	public List<SparkC> getLstSparkC() {
		return lstSparkC;
	}

	public void setLstSparkC(List<SparkC> lstSparkC) {
		this.lstSparkC = lstSparkC;
	}	
	

}
