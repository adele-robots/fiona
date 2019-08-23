package com.adelerobots.fioneg.entity.usuarioconfig;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.adelerobots.fioneg.entity.UsuarioC;

@Embeddable
@Table(name = "USUARIOCONFIG")
/**
 * Clase para representar la clave primaria de la tabla de bbdd
 * "usuarioconfig"
 */
public class UsuarioConfigPk implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2192012021400514261L;

	@ManyToOne(targetEntity = UsuarioC.class)
	@JoinColumn(name = "CN_USUARIO", nullable=false)
	private UsuarioC usuario;

	@Column(name = "CN_CONFIG", nullable=false)
	private Integer config;
	
	public UsuarioC getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioC usuario) {
		this.usuario = usuario;
	}
	
	public Integer getConfig() {
		return config;
	}

	public void setConfig(Integer config) {
		this.config = config;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result
				+ ((config == null) ? 0 : config.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final UsuarioConfigPk other = (UsuarioConfigPk) obj;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		if (config == null) {
			if (other.config != null)
				return false;
		} else if (!config.equals(other.config))
			return false;
		return true;
	}
	
}
