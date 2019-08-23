package com.adelerobots.fioneg.entity;

import java.util.LinkedList;
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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity(name = "tipo")
@Table(name = "tipo")
@org.hibernate.annotations.Proxy(lazy = true)

public class TipoC extends com.treelogic.fawna.arq.negocio.persistencia.datos.DataC{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_tipo", nullable = false, length = 11)
	private Integer cnTipo;
	
	@Column(name = "dc_tipo", nullable = false)
	private String strTipo;
	
	@Column(name = "dc_funcvalidacion", nullable = true)
	private String strFuncValidacion;
	
	@Column(name = "cn_usuario", length = 11)
	private Integer cnUsuario;

	//OTM de configparam
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo", targetEntity = ConfigParamC.class)	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	private List<ConfigParamC> lstConfigParams= new LinkedList<ConfigParamC>();
	
	//OTM de DatoLista
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo", targetEntity = DatoListaC.class)	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	private List<DatoListaC> lstDatosLista= new LinkedList<DatoListaC>();
	
	//OTM de DatoListaUsuario
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipo", targetEntity = DatoListaUsuarioC.class)	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	private List<DatoListaUsuarioC> lstDatosListaUsuario= new LinkedList<DatoListaUsuarioC>();
	
	//MTO usuario	
	@ManyToOne (targetEntity = UsuarioC.class)
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name = "cn_usuario", insertable=false, updatable=false)
	private UsuarioC usuario;
	
	public TipoC(){
		super();
	}
	
	public TipoC(String nombreTipo, String funcValidacion, Character tipoBasico, Integer intCodUsuario){

		super();	
		this.strTipo = nombreTipo;
		this.strFuncValidacion = funcValidacion;
		this.chTipoBasico = tipoBasico;
		this.cnUsuario = intCodUsuario;		
		
	}
	
	@Column(name = "fl_tipobasico")
	private Character chTipoBasico;
	
	public Integer getCnTipo() {
		return cnTipo;
	}

	public void setCnTipo(Integer cnTipo) {
		this.cnTipo = cnTipo;
	}

	public String getStrTipo() {
		return strTipo;
	}

	public void setStrTipo(String strTipo) {
		this.strTipo = strTipo;
	}

	public String getStrFuncValidacion() {
		return strFuncValidacion;
	}

	public void setStrFuncValidacion(String strFuncValidacion) {
		this.strFuncValidacion = strFuncValidacion;
	}

	public List<ConfigParamC> getLstConfigParams() {
		return lstConfigParams;
	}

	public void setLstConfigParams(List<ConfigParamC> lstConfigParams) {
		this.lstConfigParams = lstConfigParams;
	}

	public List<DatoListaC> getLstDatosLista() {
		return lstDatosLista;
	}

	public void setLstDatosLista(List<DatoListaC> lstDatosLista) {
		this.lstDatosLista = lstDatosLista;
	}

	public List<DatoListaUsuarioC> getLstDatosListaUsuario() {
		return lstDatosListaUsuario;
	}

	public void setLstDatosListaUsuario(List<DatoListaUsuarioC> lstDatosListaUsuario) {
		this.lstDatosListaUsuario = lstDatosListaUsuario;
	}

	public UsuarioC getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioC usuario) {
		this.usuario = usuario;
	}	

	public Integer getCnUsuario() {
		return cnUsuario;
	}

	public void setCnUsuario(Integer cnUsuario) {
		this.cnUsuario = cnUsuario;
	}

	public Character getChTipoBasico() {
		return chTipoBasico;
	}

	public void setChTipoBasico(Character chTipoBasico) {
		this.chTipoBasico = chTipoBasico;
	}	
	
}
