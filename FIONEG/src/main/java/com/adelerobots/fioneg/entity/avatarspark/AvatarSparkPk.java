package com.adelerobots.fioneg.entity.avatarspark;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.adelerobots.fioneg.entity.AvatarC;
import com.adelerobots.fioneg.entity.SparkC;

@Embeddable
@Table(name = "AVATARSPARK")
/**
 * Clase para representar la clave primaria de la tabla de bbdd
 * "avatarspark"
 */
public class AvatarSparkPk implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1626715059426721575L;

	@ManyToOne(targetEntity = AvatarC.class)
	@JoinColumn(name = "CN_AVATAR")
	private AvatarC avatar;
	
	@ManyToOne(targetEntity = SparkC.class)
	@JoinColumn(name = "CN_SPARK", nullable = false)
	private SparkC spark;
	

	public AvatarC getAvatar() {
		return avatar;
	}

	public void setAvatar(AvatarC avatar) {
		this.avatar = avatar;
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
				+ ((avatar == null) ? 0 : avatar.hashCode());
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
		final AvatarSparkPk other = (AvatarSparkPk) obj;
		if (avatar == null) {
			if (other.avatar != null)
				return false;
		} else if (!avatar.equals(other.avatar))
			return false;
		if (spark == null) {
			if (other.spark != null)
				return false;
		} else if (!spark.equals(other.spark))
			return false;
		return true;
	}
	
	
	

}
