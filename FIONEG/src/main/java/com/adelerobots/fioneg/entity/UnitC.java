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
 * para la tabla "unit" de la base de datos
 * 
 * @author adele
 *
 */
@Entity(name = "unit")
@Table(name = "unit")
@org.hibernate.annotations.Proxy(lazy = true)
public class UnitC extends DataC {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_unit", nullable = false)
	private Integer intCodUnit;
	
	@Column(name="dc_content")
	private String strContent;
	
	@Column(name = "fl_visible", length=1)
	private String flagVisible;
	
	public UnitC(){
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
				+ ((this.intCodUnit == null) ? 0 : this.intCodUnit
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

			final UnitC other = (UnitC) obj;

			if (this.intCodUnit == null) {

				if (other.intCodUnit != null) {

					bEqual = false;

				}

			}

			else if (!this.intCodUnit.equals(other.intCodUnit)) {

				bEqual = false;

			}

		}

		return bEqual;

	}

	public Integer getIntCodUnit() {
		return intCodUnit;
	}

	public void setIntCodUnit(Integer intCodUnit) {
		this.intCodUnit = intCodUnit;
	}

	public String getStrContent() {
		return strContent;
	}

	public void setStrContent(String strContent) {
		this.strContent = strContent;
	}
	
	public String getFlagVisible() {
		return flagVisible;
	}
	
	public void setFlagVisible(String flagVisible) {
		this.flagVisible = flagVisible;
	}
	

}
