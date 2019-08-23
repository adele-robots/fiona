package com.adelerobots.fioneg.entity;

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

import com.adelerobots.fioneg.util.FunctionUtils;

/**
 * Entity entidad del usuario
 * @author adele
 *
 */
@Entity(name = "entidad")
@Table(name = "entidad")
@org.hibernate.annotations.Proxy(lazy = true)
public class EntidadC extends com.treelogic.fawna.arq.negocio.persistencia.datos.DataC {

	
	public EntidadC(){
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
		EntidadC other = (EntidadC) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}





	private Integer id;
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_entidad", nullable = false, length = 100)
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	@Transient public BigDecimal getCnEntidadAsBd() { 
		if (id == null) return null;
		return new BigDecimal(id.toString());
	}



	private String nombre;
	@Column(name = "dc_nombre", nullable = true, length = 100)
	public String getNombre() { return nombre; }
	public void setNombre(String strNombre) { this.nombre = strNombre; }



	private Integer tipoEntidad;
	@Column(name = "cn_tipoentidad", nullable = true, length = 11)
	public Integer getTipoEntidad() { return tipoEntidad; }
	public void setTipoEntidad(Integer tipoEntidad) { this.tipoEntidad = tipoEntidad; }
	@Transient public BigDecimal getTipoEntidadAsBd() { 
		if (tipoEntidad == null) return null;
		return new BigDecimal(tipoEntidad.toString());
	}

	

	private String website;
	@Column(name = "dc_website", nullable = true, length = 100)
	public String getWebsite() { return website; }
	public void setWebsite(String website) { this.website = website; }



	private String workEmail;
	@Column(name = "dc_workemail", nullable = true, length = 100)
	public String getWorkEmail() { return workEmail; }
	public void setWorkEmail(String workEmail) { this.workEmail = workEmail; }



	private String phoneCode;
	@Column(name = "dc_phonecode", nullable = true, length = 5)
	public String getPhoneCode() { return phoneCode; }
	public void setPhoneCode(String phoneCode) { this.phoneCode = phoneCode; }



	private String phoneNumber;
	@Column(name = "dc_phonenumber", nullable = true, length = 20)
	public String getPhoneNumber() { return phoneNumber; }
	public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }



	private String phoneExt;
	@Column(name = "dc_phoneext", nullable = true, length = 5)
	public String getPhoneExt() { return phoneExt; }
	public void setPhoneExt(String phoneExt) { this.phoneExt = phoneExt; }



	private Set<UsuarioC> usuarios = new LinkedHashSet<UsuarioC>(0);
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "entidad", targetEntity = UsuarioC.class)	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	public Set<UsuarioC> getUsuarios() { return usuarios; }
	public void setUsuarios(Set<UsuarioC> usuarios) { this.usuarios = usuarios; }
	/** Persistence delegated method to add single UsuarioC to this EntidadC */
	public void addUsuarios(final UsuarioC bean) {
		if (bean == null) return;
		if (getUsuarios() == null) setUsuarios(new LinkedHashSet<UsuarioC>(1));
		bean.setEntidad(this);
		getUsuarios().add(bean);
	}
	/** Persistence delegated method to remove single UsuarioC to this EntidadC */
	public void removeUsuarios(final UsuarioC bean) {
		if (bean == null) return;
		if (getUsuarios() == null) return;
		getUsuarios().remove(bean);
		bean.setEntidad(null);
	}
	/** Persistence delegated method to move (if needed) single UsuarioC from a EntidadC to other */
	public static void moveUsuarios(final UsuarioC bean, final EntidadC to) {
		if (bean == null) return;
		final EntidadC from = bean.getEntidad();
		if (!FunctionUtils.equals(from, to)) {
			if (from != null) from.removeUsuarios(bean);
			if (to != null) to.addUsuarios(bean);
		}
	}



}
