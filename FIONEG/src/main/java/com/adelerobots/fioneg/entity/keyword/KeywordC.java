package com.adelerobots.fioneg.entity.keyword;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.adelerobots.fioneg.entity.SparkC;
import com.treelogic.fawna.arq.negocio.persistencia.datos.DataC;

@Entity(name = "keyword")
@Table(name = "keyword")
@org.hibernate.annotations.Proxy(lazy = true)
public class KeywordC extends DataC {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_keyword", nullable = false, length = 11)
	private Integer intCodKeyword;
	
	@Column(name = "dc_descripcion", nullable = false)
	private String strDescripcion;
	
	
	@ManyToMany(targetEntity=SparkC.class, cascade={CascadeType.REFRESH})
	@JoinTable(name="SparkKeyword", joinColumns=@JoinColumn(name="cn_keyword"), 
								   inverseJoinColumns=@JoinColumn(name="cn_spark"))
    private List<SparkC> lstSpaks;
	
	
	public KeywordC(){
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
				+ ((this.intCodKeyword == null) ? 0 : this.intCodKeyword
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

			final KeywordC other = (KeywordC) obj;

			if (this.intCodKeyword == null) {

				if (other.intCodKeyword != null) {

					bEqual = false;

				}

			}

			else if (!this.intCodKeyword.equals(other.intCodKeyword)) {

				bEqual = false;

			}

		}

		return bEqual;

	}

	public Integer getIntCodKeyword() {
		return intCodKeyword;
	}

	public void setIntCodKeyword(Integer intCodKeyword) {
		this.intCodKeyword = intCodKeyword;
	}

	public String getStrDescripcion() {
		return strDescripcion;
	}

	public void setStrDescripcion(String strDescripcion) {
		this.strDescripcion = strDescripcion;
	}

	public List<SparkC> getLstSpaks() {
		return lstSpaks;
	}

	public void setLstSpaks(List<SparkC> lstSpaks) {
		this.lstSpaks = lstSpaks;
	}
	
	
}
