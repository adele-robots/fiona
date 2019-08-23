package com.adelerobots.fioneg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "datolista")
@Table(name = "datolista")
@org.hibernate.annotations.Proxy(lazy = true)

public class DatoListaC extends com.treelogic.fawna.arq.negocio.persistencia.datos.DataC{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_datolista", nullable = false, length = 11)
	private Integer cnDatoLista;
	
	@Column(name = "cn_tipo", nullable = false, length = 11)
	private Integer cnTipo;
	
	@Column(name = "dc_nombre", nullable = false)
	private String strNombre;	
	
	//MTO tipoc
	@ManyToOne (targetEntity = TipoC.class)
	@JoinColumn(name = "cn_tipo", insertable=false, updatable=false)
	private TipoC tipo;

	public Integer getCnDatoLista() {
		return cnDatoLista;
	}

	public void setCnDatoLista(Integer cnDatoLista) {
		this.cnDatoLista = cnDatoLista;
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

	public TipoC getTipo() {
		return tipo;
	}

	public void setTipo(TipoC tipo) {
		this.tipo = tipo;
	}




}
