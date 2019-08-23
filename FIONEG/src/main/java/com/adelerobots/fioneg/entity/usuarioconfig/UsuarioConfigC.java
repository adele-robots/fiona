package com.adelerobots.fioneg.entity.usuarioconfig;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Clase que mapea las operaciones con hibernate
 * 
 * @author adele
 * 
 */
@Entity(name = "UsuarioConfig")
@Table(name = "USUARIOCONFIG")
@org.hibernate.annotations.Proxy(lazy = true)
public class UsuarioConfigC extends com.treelogic.fawna.arq.negocio.persistencia.datos.DataC {

	@Id	
	private UsuarioConfigPk usuarioConfig;
	
	@Column(name = "CN_USUARIO", nullable=false, updatable=false, insertable=false)
	private Integer intCodUsuario;
	
	@Column(name = "CN_CONFIG", nullable=false, updatable=false, insertable=false)
	private Integer intCodConfig;

	@Column(name = "DC_NOMBRE", nullable=false, length = 45)
	private String nombre;
	
	

	/**
	 * Constructor por defecto
	 */
	public UsuarioConfigC() {

	}

	/**
	 * Constructor de la clase
	 * @param usuario El id de usuario
	 * @param config El id de la configuracion para ese usuario
	 */
	public UsuarioConfigC(UsuarioConfigPk usuarioConfig) {
		this.usuarioConfig = usuarioConfig;
	}

	/**
	 * Constructor de la clase
	 * @param usuario El id de usuario
	 * @param config El id de la configuracion para ese usuario
	 */
	public UsuarioConfigC(Integer usuario, Integer config) {
		this.intCodUsuario = usuario;
		this.intCodConfig = config;
	}

	/**
	 * Constructor de la clase
	 * @param usuario El id de usuario
	 * @param config El id de la configuracionpara ese usuario
	 * @param nombre El nombre de la configuracion
	 */
	public UsuarioConfigC(Integer usuario, Integer config, String nombre) {
		this.intCodUsuario = usuario;
		this.intCodConfig = config;
		this.nombre = nombre;
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
				+ ((this.usuarioConfig == null) ? 0
						: this.usuarioConfig.hashCode());
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
		} else if (obj == null) {
			bEqual = false;
		} else if (getClass() != obj.getClass()) {
			bEqual = false;
		} else {
			final UsuarioConfigC other = (UsuarioConfigC) obj;
			if (this.usuarioConfig == null) {
				if (other.usuarioConfig != null) {
					bEqual = false;
				}
			} else if (!this.usuarioConfig
					.equals(other.usuarioConfig)) {
				bEqual = false;
			}
		}
		return bEqual;
	}

	public UsuarioConfigPk getUsuarioConfig() {
		return usuarioConfig;
	}

	public void setUsuarioConfig(UsuarioConfigPk usuarioConfig) {
		this.usuarioConfig = usuarioConfig;
	}

	public Integer getIntCodUsuario() {
		return intCodUsuario;
	}

	@Transient public BigDecimal getIntCodUsuarioAsBd() { 
		if (intCodUsuario == null) return null;
		return new BigDecimal(intCodUsuario.toString());
	}

	public void setIntCodUsuario(Integer intCodUsuario) {
		this.intCodUsuario = intCodUsuario;
	}

	public Integer getIntCodConfig() {
		return intCodConfig;
	}

	@Transient public BigDecimal getIntCodConfigAsBd() { 
		if (intCodConfig == null) return null;
		return new BigDecimal(intCodConfig.toString());
	}

	public void setIntCodCOnfig(Integer intCodConfig) {
		this.intCodConfig = intCodConfig;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	
}
