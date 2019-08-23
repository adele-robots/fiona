package com.adelerobots.fioneg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "configparam")
@Table(name = "configparam")
@org.hibernate.annotations.Proxy(lazy = true)

public class ConfigParamC extends com.treelogic.fawna.arq.negocio.persistencia.datos.DataC{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_configparam", nullable = false, length = 11)
	private Integer cnConfigParam;
	
	@Column(name = "cn_spark", nullable = false, length = 11)
	private Integer cnSpark;
	
	@Column(name = "dc_nombre", nullable = false)
	private String strNombre;
	
	@Column(name = "cn_tipo", nullable = false, length = 11)
	private Integer cnTipo;
	
	@Column(name = "dc_defaultvalue", nullable = true)
	private String strDefaultValue;
	
	@Column(name = "dc_minvalue", nullable = true)
	private String strMinValue;	
	
	@Column(name = "dc_maxvalue", nullable = true)
	private String strMaxValue;
	
	@Column(name = "nu_isconfigurable", nullable = false)
	private int isConfigurable;
	
	@Column(name="dc_descripcion", nullable=true)
	private String strDescripcion;
	
	public int getIsConfigurable() {
		return isConfigurable;
	}

	public void setIsConfigurable(int isConfigurable) {
		this.isConfigurable = isConfigurable;
	}

	//ManyToOne de Spark
	@ManyToOne (targetEntity = SparkC.class)
	@JoinColumn(name = "cn_spark", insertable=false, updatable=false)
	private SparkC spark;
	
	//ManyToOne a Tipo
	@ManyToOne (targetEntity = TipoC.class)
	@JoinColumn(name = "cn_tipo", insertable=false, updatable=false)
	private TipoC tipo;
	
	public Integer getCnConfigParam() {
		return cnConfigParam;
	}

	public void setCnConfigParam(Integer cnConfigParam) {
		this.cnConfigParam = cnConfigParam;
	}

	public Integer getCnSpark() {
		return cnSpark;
	}

	public void setCnSpark(Integer cnSpark) {
		this.cnSpark = cnSpark;
	}

	public String getStrNombre() {
		return strNombre;
	}

	public void setStrNombre(String strNombre) {
		this.strNombre = strNombre;
	}

	public Integer getCnTipo() {
		return cnTipo;
	}

	public void setCnTipo(Integer cnTipo) {
		this.cnTipo = cnTipo;
	}

	public String getStrDefaultValue() {
		return strDefaultValue;
	}

	public void setStrDefaultValue(String strDefaultValue) {
		this.strDefaultValue = strDefaultValue;
	}

	public SparkC getSpark() {
		return spark;
	}

	public void setSpark(SparkC spark) {
		this.spark = spark;
	}

	public TipoC getTipo() {
		return tipo;
	}

	public void setTipo(TipoC tipo) {
		this.tipo = tipo;
	}

	public String getStrMinValue() {
		return strMinValue;
	}

	public void setStrMinValue(String strMinValue) {
		this.strMinValue = strMinValue;
	}

	public String getStrMaxValue() {
		return strMaxValue;
	}

	public void setStrMaxValue(String strMaxValue) {
		this.strMaxValue = strMaxValue;
	}

	public String getStrDescripcion() {
		return strDescripcion;
	}

	public void setStrDescripcion(String strDescripcion) {
		this.strDescripcion = strDescripcion;
	}
	
	

	
}
