package com.adelerobots.fioneg.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity(name = "interfaz")
@Table(name = "interfaz")
@org.hibernate.annotations.Proxy(lazy = true)

public class InterfazC extends com.treelogic.fawna.arq.negocio.persistencia.datos.DataC{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_interfaz", nullable = false, length = 11)
	private Integer cnInterfaz;
	
	@Column(name = "dc_nombre", nullable = false, length = 100)
	private String dcNombre;
	
	@Column(name = "dc_tipo", nullable = false, length = 20)
	private String strTipo;
	
	@Column(name = "dc_descripcion", nullable = false, length = 100)
	private String strDescripcion;
	
	@ManyToMany(targetEntity=SparkC.class, cascade={CascadeType.REFRESH})
	@JoinTable(name="SparkInterfaz", joinColumns=@JoinColumn(name="cn_interfaz"), 
								   inverseJoinColumns=@JoinColumn(name="cn_spark"))
    private List<SparkC> lstsparkc;
	
	public InterfazC(){
		super();
	}

	public Integer getCnInterfaz() {
		return cnInterfaz;
	}

	public void setCnInterfaz(Integer cnInterfaz) {
		this.cnInterfaz = cnInterfaz;
	}

	public String getDcNombre() {
		return dcNombre;
	}

	public void setDcNombre(String cnNombre) {
		this.dcNombre = cnNombre;
	}

	public String getStrTipo() {
		return strTipo;
	}

	public void setStrTipo(String strTipo) {
		this.strTipo = strTipo;
	}

	public String getStrDescripcion() {
		return strDescripcion;
	}

	public void setStrDescripcion(String strDescripcion) {
		this.strDescripcion = strDescripcion;
	}

	public List<SparkC> getLstsparkc() {
		return lstsparkc;
	}

	public void setLstsparkc(List<SparkC> lstsparkc) {
		this.lstsparkc = lstsparkc;
	}

	
}
