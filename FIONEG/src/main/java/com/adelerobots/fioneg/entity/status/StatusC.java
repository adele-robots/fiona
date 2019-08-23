package com.adelerobots.fioneg.entity.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.adelerobots.fioneg.entity.sparkstatus.SparkStatusC;

@Entity(name = "status")
@Table(name = "status")
@org.hibernate.annotations.Proxy(lazy = true)
/**
 * Clase que representa la entidad de BBDD "status"
 */
public class StatusC extends com.treelogic.fawna.arq.negocio.persistencia.datos.DataC{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_status", nullable = false, length = 11)
	private Integer cnStatus;
	
	@Column(name = "dc_descripcion", nullable = false)
	private String strDescripcion;
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "sparkStatus.status", targetEntity = SparkStatusC.class)
	@JoinColumn(name = "CN_STATUS", nullable = false)
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	private List<SparkStatusC> lstSparkStatusC = new ArrayList<SparkStatusC>(0);
			
	
	
	public StatusC(){
		super();
	}



	public Integer getCnStatus() {
		return cnStatus;
	}



	public void setCnStatus(Integer cnStatus) {
		this.cnStatus = cnStatus;
	}



	public String getStrDescripcion() {
		return strDescripcion;
	}



	public void setStrDescripcion(String strDescripcion) {
		this.strDescripcion = strDescripcion;
	}



	public List<SparkStatusC> getLstSparkStatusC() {
		return lstSparkStatusC;
	}



	public void setLstSparkStatusC(List<SparkStatusC> lstSparkStatusC) {
		this.lstSparkStatusC = lstSparkStatusC;
	}
	
	
	
	
	
	

}
