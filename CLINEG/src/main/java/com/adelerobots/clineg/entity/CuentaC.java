package com.adelerobots.clineg.entity;

import java.math.BigDecimal;
import java.util.Date;
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
 * Entity tipo de cuenta del usuario
 * @author adele
 *
 */
@Entity(name = "cuenta")
@Table(name = "cuenta")
@org.hibernate.annotations.Proxy(lazy = true)
public class CuentaC extends com.treelogic.fawna.arq.negocio.persistencia.datos.DataC{

	
	public CuentaC(){
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
		CuentaC other = (CuentaC) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}





	private Integer id;
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_cuenta", nullable = false, length = 11)
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	@Transient public BigDecimal getIdAsBd() { 
		if (id == null) return null;
		return new BigDecimal(id.toString());
	}



	private String nombre;
	@Column(name = "dc_nombre", nullable = true, length = 100)
	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }
	
	private Float floPrecioMensual;
	@Column(name = "nu_preciomensual", length = 100)
	public Float getFloPrecioMensual() {	return new Float(Math.round(floPrecioMensual*100.0)/100.0); }
	public void setFloPrecioMensual(Float floPrecioMensual) {	this.floPrecioMensual = floPrecioMensual; }
	
	private Float floPrecioAnual;
	@Column(name = "nu_precioanual", length = 100)
	public Float getFloPrecioAnual() { 	return new Float(Math.round(floPrecioAnual*100.0)/100.0); }
	public void setFloPrecioAnual(Float floPrecioAnual) { this.floPrecioAnual = floPrecioAnual; }


	private Set<UsuarioC> usuarios = new LinkedHashSet<UsuarioC>(0);


	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cuenta", targetEntity = UsuarioC.class)	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	public Set<UsuarioC> getUsuarios() { return usuarios; }
	public void setUsuarios(Set<UsuarioC> usuarios) { this.usuarios = usuarios; }
	/** Persistence delegated method to add single UsuarioC to this CuentaC */
	public void addUsuarios(final UsuarioC bean) {
		if (bean == null) return;
		if (getUsuarios() == null) setUsuarios(new LinkedHashSet<UsuarioC>(1));
		bean.setCuenta(this);
		getUsuarios().add(bean);
	}
	/** Persistence delegated method to remove single UsuarioC to this CuentaC */
	public void removeUsuarios(final UsuarioC bean) {
		if (bean == null) return;
		if (getUsuarios() == null) return;
		getUsuarios().remove(bean);
		bean.setCuenta(null);
	}
	/** Persistence delegated method to move (if needed) single UsuarioC from a CuentaC to other */
	public static void moveUsuarios(final UsuarioC bean, final CuentaC to) {
		if (bean == null) return;
		final CuentaC from = bean.getCuenta();
		if (!FunctionUtils.equals(from, to)) {
			if (from != null) from.removeUsuarios(bean);
			if (to != null) to.addUsuarios(bean);
			
		}
	}



}
