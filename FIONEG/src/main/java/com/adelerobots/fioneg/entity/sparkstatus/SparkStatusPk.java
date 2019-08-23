package com.adelerobots.fioneg.entity.sparkstatus;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.entity.status.StatusC;

@Embeddable
@Table(name = "SPARKSTATUS")
/**
 * Clase para representar la clave primaria de la tabla de bbdd
 * "sparkstatus"
 */
public class SparkStatusPk implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1626715059426721575L;

	@ManyToOne(targetEntity = StatusC.class)
	@JoinColumn(name = "CN_STATUS")
	private StatusC status;
	
	@ManyToOne(targetEntity = SparkC.class)
	@JoinColumn(name = "CN_SPARK", nullable = false)
	private SparkC spark;

	

	public StatusC getStatus() {
		return status;
	}

	public void setStatus(StatusC status) {
		this.status = status;
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
				+ ((status == null) ? 0 : status.hashCode());
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
		final SparkStatusPk other = (SparkStatusPk) obj;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (spark == null) {
			if (other.spark != null)
				return false;
		} else if (!spark.equals(other.spark))
			return false;
		return true;
	}
	
	
	

}
