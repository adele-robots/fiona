package com.adelerobots.serneg.entity;

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
 * para la tabla "PROCESOMAQUINA" de la base de datos
 * 
 * @author adele
 *
 */
@Entity(name = "procesomaquina")
@Table(name = "procesomaquina")
@org.hibernate.annotations.Proxy(lazy = true)
public class ProcesoMaquinaC extends DataC {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_procesomaquina", nullable = false)
	private Integer intCodProcesoMaquina;
	
	@Column(name="dc_email")
	private String strEmail;
	
	@Column(name="dc_avatar")
	private String strAvatar;
	
	@Column(name="nu_numprocesos")
	private Integer intNumProcesos;
	
	@ManyToOne(targetEntity=MaquinaC.class)
	@JoinColumn(name="cn_maquina")
	private MaquinaC maquina;
	
	
	public ProcesoMaquinaC(){
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
				+ ((this.intCodProcesoMaquina == null) ? 0 : this.intCodProcesoMaquina
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

			final ProcesoMaquinaC other = (ProcesoMaquinaC) obj;

			if (this.intCodProcesoMaquina == null) {

				if (other.intCodProcesoMaquina != null) {

					bEqual = false;

				}

			}

			else if (!this.intCodProcesoMaquina.equals(other.intCodProcesoMaquina)) {

				bEqual = false;

			}

		}

		return bEqual;

	}

	public Integer getIntCodProcesoMaquina() {
		return intCodProcesoMaquina;
	}

	public void setIntCodProcesoMaquina(Integer intCodProcesoMaquina) {
		this.intCodProcesoMaquina = intCodProcesoMaquina;
	}

	public String getStrEmail() {
		return strEmail;
	}

	public void setStrEmail(String strEmail) {
		this.strEmail = strEmail;
	}

	public String getStrAvatar() {
		return strAvatar;
	}

	public void setStrAvatar(String strAvatar) {
		this.strAvatar = strAvatar;
	}

	public MaquinaC getMaquina() {
		return maquina;
	}

	public void setMaquina(MaquinaC maquina) {
		this.maquina = maquina;
	}

	public Integer getIntNumProcesos() {
		return intNumProcesos;
	}

	public void setIntNumProcesos(Integer intNumProcesos) {
		this.intNumProcesos = intNumProcesos;
	}	
	
}
