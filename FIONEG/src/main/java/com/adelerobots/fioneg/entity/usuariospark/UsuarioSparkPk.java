package com.adelerobots.fioneg.entity.usuariospark;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.entity.UsuarioC;

@Embeddable
@Table(name = "USUARIOSPARK")
/**
 * Clase para representar la clave primaria de la tabla de bbdd
 * "usuariospark"
 */
public class UsuarioSparkPk implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1626715059426721575L;

	@ManyToOne(targetEntity = UsuarioC.class)
	@JoinColumn(name = "CN_USUARIO")
	private UsuarioC usuario;
	
	@ManyToOne(targetEntity = SparkC.class)
	@JoinColumn(name = "CN_SPARK", nullable = false)
	private SparkC spark;

	


	public UsuarioC getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioC usuario) {
		this.usuario = usuario;
	}

	public SparkC getSpark() {
		return spark;
	}

	public void setSpark(SparkC spark) {
		this.spark = spark;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result
				+ ((spark == null) ? 0 : spark.hashCode());
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
		final UsuarioSparkPk other = (UsuarioSparkPk) obj;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		if (spark == null) {
			if (other.spark != null)
				return false;
		} else if (!spark.equals(other.spark))
			return false;
		return true;
	}
	
	
	

}
