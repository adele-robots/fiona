package com.adelerobots.fioneg.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Entity tipos de entidades
 * @author adele
 *
 */
@Entity(name = "tipoentidad")
@Table(name = "tipoentidad")
@org.hibernate.annotations.Proxy(lazy = true)
public class TipoEntidadC extends com.treelogic.fawna.arq.negocio.persistencia.datos.DataC{

	
	public TipoEntidadC(){
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
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TipoEntidadC other = (TipoEntidadC) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}





	private Integer id;
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_tipoentidad", nullable = false, length = 11)
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	@Transient public BigDecimal getIdAsBd() { 
		if (id == null) return null;
		return new BigDecimal(id.toString());
	}



	private String descripcion;
	@Column(name = "dc_descripcion", nullable = true, length = 100)
	public String getDescripcion() { return descripcion; }
	public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

	
	//TODO:

	//private Set<EntidadC> entidades;
	//public Set<EntidadC> getEntidades();
	//public void setEntidades(Set<EntidadC> entidades);
	//public void addEntidades(final EntidadC bean);
	//public void removeEntidades(final EntidadC bean);
	//public static void moveEntidades(final EntidadC bean, final TipoEntidadC to);


}
