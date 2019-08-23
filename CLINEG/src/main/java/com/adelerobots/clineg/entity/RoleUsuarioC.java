package com.adelerobots.clineg.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.adelerobots.clineg.util.FunctionUtils;

/**
 * Entity to manage user role data
 * @author adele
 *
 */
@Entity(name = "role")
@Table(name = "role", uniqueConstraints = {
		@javax.persistence.UniqueConstraint(columnNames = "DC_name")})
@org.hibernate.annotations.Proxy(lazy = true)
public class RoleUsuarioC 
	extends 		com.treelogic.fawna.arq.negocio.persistencia.datos.DataC
	implements 	Serializable 
{

	private static final long serialVersionUID = 1626715059426721575L;



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
		RoleUsuarioC other = (RoleUsuarioC) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}




	/**Campo identificador */
	private Integer id;
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CN_role", nullable = false, unique = true)
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	@Transient public BigDecimal getIdAsBd() { 
		if (id == null) return null;
		return new BigDecimal(id.toString());
	}



	/**Campo nombre/label unico*/
	private String name;
	@Column(name = "DC_name", nullable = false, unique=true, length = 45)
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }



	/**Campo relacion usuario con este rols */
	private Set<UsuarioC> usuarios = new LinkedHashSet<UsuarioC>(0);
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "role", targetEntity = UsuarioC.class)
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	public Set<UsuarioC> getUsuarios() { return usuarios; }
	public void setUsuarios(final Set<UsuarioC> usuarios) { this.usuarios = usuarios; }
	/** Persistence delegated method to add UsuarioC to this RoleUsuarioC */
	public void addUsuarios(final UsuarioC bean) {
		if (bean == null) return;
		if (getUsuarios() == null) setUsuarios(new LinkedHashSet<UsuarioC>(1));
		bean.setRole(this);
		getUsuarios().add(bean);
	}
	/** Persistence delegated method to remove single UsuarioC to this RoleUsuarioC */
	public void removeUsuarios(final UsuarioC bean) {
		if (bean == null) return;
		if (getUsuarios() == null) return;
		getUsuarios().remove(bean);
		bean.setRole(null);
	}
	/** Persistence delegated method to move (if needed) single UsuarioC from a RoleUsuarioC to other */
	public static void moveUsuarios(final UsuarioC bean, final RoleUsuarioC to) {
		if (bean == null) return;
		final RoleUsuarioC from = bean.getRole();
		if (!FunctionUtils.equals(from, to)) {
			if (from != null) from.removeUsuarios(bean);
			if (to != null) to.addUsuarios(bean);
		}
	}





}
