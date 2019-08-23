package com.adelerobots.clineg.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.treelogic.fawna.arq.negocio.persistencia.datos.DataC;

/**
 * Clase que mapea las operaciones con hibernate
 * para la tabla "avatar" de la base de datos
 * 
 * @author adele
 *
 */
@Entity(name = "avatar")
@Table(name = "avatar")
@org.hibernate.annotations.Proxy(lazy = true)
public class AvatarC extends DataC {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_avatar", nullable = false)
	private Integer intCodAvatar;
	
	@ManyToOne(targetEntity=UsuarioC.class)
	@JoinColumn(name="cn_usuario")
	private UsuarioC usuario;
		
	@Column(name="fl_activo")
	private Character chActivo;
	
	@Column(name="fe_upload")
	private Date datUpload;
	
	
	@ManyToOne(targetEntity=HostingC.class)
	@JoinColumn(name="cn_hosting")
	private HostingC hosting;	

	
	public AvatarC(){
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
				+ ((this.intCodAvatar == null) ? 0 : this.intCodAvatar
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

			final AvatarC other = (AvatarC) obj;

			if (this.intCodAvatar == null) {

				if (other.intCodAvatar != null) {

					bEqual = false;

				}

			}

			else if (!this.intCodAvatar.equals(other.intCodAvatar)) {

				bEqual = false;

			}

		}

		return bEqual;

	}

	public Integer getIntCodAvatar() {
		return intCodAvatar;
	}

	public void setIntCodAvatar(Integer intCodAvatar) {
		this.intCodAvatar = intCodAvatar;
	}

	public UsuarioC getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioC usuario) {
		this.usuario = usuario;
	}
/**
	public SparkC getSpark() {
		return spark;
	}

	public void setSpark(SparkC spark) {
		this.spark = spark;
	}
	*/	

	public HostingC getHosting() {
		return hosting;
	}

	
	public void setHosting(HostingC hosting) {
		this.hosting = hosting;
	}

	public Character getChActivo() {
		return chActivo;
	}

	public void setChActivo(Character chActivo) {
		this.chActivo = chActivo;
	}

	public Date getDatUpload() {
		return datUpload;
	}

	public void setDatUpload(Date datUpload) {
		this.datUpload = datUpload;
	}	

}
