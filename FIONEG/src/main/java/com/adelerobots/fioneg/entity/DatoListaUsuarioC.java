package com.adelerobots.fioneg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity (name = "datolistausuario")
@Table(name = "datolistausuario")
@org.hibernate.annotations.Proxy(lazy = true)

public class DatoListaUsuarioC extends com.treelogic.fawna.arq.negocio.persistencia.datos.DataC{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_datolistausuario", nullable = false, length = 11)
	private Integer cnDatoListaUsuario;
	
	@Column(name = "cn_tipo", nullable = false, length = 11)
	private Integer cnTipo;
	
	@Column(name = "dc_nombre", nullable = false)
	private String strNombre;	
	
	@Column(name = "cn_usuario", nullable = false, length = 11)
	private Integer cnUsuario;

	//MTO tipo
	@ManyToOne (targetEntity = TipoC.class)
	@JoinColumn(name = "cn_tipo", insertable=false, updatable=false)
	private TipoC tipo;

	//MTO usuario
	@ManyToOne (targetEntity = UsuarioC.class)
	@JoinColumn(name = "cn_usuario", insertable=false, updatable=false)
	private UsuarioC usuario;
	
	public Integer getCnDatoListaUsuario() {
		return cnDatoListaUsuario;
	}

	public void setCnDatoListaUsuario(Integer cnDatoListaUsuario) {
		this.cnDatoListaUsuario = cnDatoListaUsuario;
	}

	public Integer getCnTipo() {
		return cnTipo;
	}

	public void setCnTipo(Integer cnTipo) {
		this.cnTipo = cnTipo;
	}

	public String getStrNombre() {
		return strNombre;
	}

	public void setStrNombre(String strNombre) {
		this.strNombre = strNombre;
	}

	public Integer getCnUsuario() {
		return cnUsuario;
	}

	public void setCnUsuario(Integer cnUsuario) {
		this.cnUsuario = cnUsuario;
	}

	public TipoC getTipo() {
		return tipo;
	}

	public void setTipo(TipoC tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the usuario
	 */
	public UsuarioC getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(UsuarioC usuario) {
		this.usuario = usuario;
	}

	
	
	
}
