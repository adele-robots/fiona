package com.adelerobots.serneg.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.treelogic.fawna.arq.negocio.persistencia.datos.DataC;

/**
 * Clase que mapea las operaciones con hibernate
 * para la tabla "MAQUINA" de la base de datos
 * 
 * @author adele
 *
 */
@Entity(name = "maquina")
@Table(name = "maquina")
@org.hibernate.annotations.Proxy(lazy = true)
public class MaquinaC extends DataC {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_maquina", nullable = false)
	private Integer intCodMaquina;
	
	@Column(name="dc_nombre")
	private String strNombre;
	
	@Column(name="dc_ip")
	private String strIp;
	
	@Column(name="nu_maxprocesos")
	private Integer intMaxProcesos;
	
	@Column(name="dc_jndi")
	private String strJndi;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "maquina", targetEntity = ProcesoMaquinaC.class)	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	private List<ProcesoMaquinaC> lstProcesoMaquina= new ArrayList<ProcesoMaquinaC>();
	
	public MaquinaC(){
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
				+ ((this.intCodMaquina == null) ? 0 : this.intCodMaquina
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

			final MaquinaC other = (MaquinaC) obj;

			if (this.intCodMaquina == null) {

				if (other.intCodMaquina != null) {

					bEqual = false;

				}

			}

			else if (!this.intCodMaquina.equals(other.intCodMaquina)) {

				bEqual = false;

			}

		}

		return bEqual;

	}

	public Integer getIntCodMaquina() {
		return intCodMaquina;
	}

	public void setIntCodMaquina(Integer intCodMaquina) {
		this.intCodMaquina = intCodMaquina;
	}

	public String getStrNombre() {
		return strNombre;
	}

	public void setStrNombre(String strNombre) {
		this.strNombre = strNombre;
	}

	public String getStrIp() {
		return strIp;
	}

	public void setStrIp(String strIp) {
		this.strIp = strIp;
	}

	public Integer getIntMaxProcesos() {
		return intMaxProcesos;
	}

	public void setIntMaxProcesos(Integer intMaxProcesos) {
		this.intMaxProcesos = intMaxProcesos;
	}

	public List<ProcesoMaquinaC> getLstProcesoMaquina() {
		return lstProcesoMaquina;
	}

	public void setLstProcesoMaquina(List<ProcesoMaquinaC> lstProcesoMaquina) {
		this.lstProcesoMaquina = lstProcesoMaquina;
	}

	public String getStrJndi() {
		return strJndi;
	}

	public void setStrJndi(String strJndi) {
		this.strJndi = strJndi;
	}
	

}
