package com.adelerobots.fioneg.entity.avatarspark;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.adelerobots.fioneg.entity.AvatarC;
import com.adelerobots.fioneg.entity.SparkC;

/**
 * Clase que mapea las operaciones con hibernate
 * 
 * @author adele
 * 
 */
@Entity(name = "AVATARSPARK")
@Table(name = "AVATARSPARK")
@org.hibernate.annotations.Proxy(lazy = true)
public class AvatarSparkC extends com.treelogic.fawna.arq.negocio.persistencia.datos.DataC {

	@EmbeddedId	
	private AvatarSparkPk avatarSparkPk;	
	
	
	@Column(name="nu_price", nullable=true)
	private Float floPrice;
	
	// Estrategia de Luisín para salvar el bug de Hibernate
	// que impide hacer createAlias o createCriterias para
	// llegar a atributos del objeto mapeado en el PK
	@ManyToOne(targetEntity = AvatarC.class)
	@JoinColumn(name = "CN_AVATAR", nullable = true, updatable = false, insertable = false)
	private AvatarC avatar;
	
	// Estrategia de Luisín para salvar el bug de Hibernate
	// que impide hacer createAlias o createCriterias para
	// llegar a atributos del objeto mapeado en el PK
	@ManyToOne(targetEntity = SparkC.class)
	@JoinColumn(name = "CN_SPARK", nullable = true, updatable = false, insertable = false)
	private SparkC spark;
	
	
	
	
	/**
	 * Constructor por defecto
	 */
	public AvatarSparkC() {

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
				+ ((this.avatarSparkPk == null) ? 0
						: this.avatarSparkPk.hashCode());
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
			final AvatarSparkC other = (AvatarSparkC) obj;
			if (this.avatarSparkPk == null) {
				if (other.avatarSparkPk != null) {
					bEqual = false;
				}
			} else if (!this.avatarSparkPk
					.equals(other.avatarSparkPk)) {
				bEqual = false;
			}
		}
		return bEqual;
	}


	public AvatarSparkPk getAvatarSparkPk() {
		return avatarSparkPk;
	}

	public void setAvatarSparkPk(AvatarSparkPk avatarSparkPk) {
		this.avatarSparkPk = avatarSparkPk;
	}	

	public Float getFloPrice() {
		return floPrice;
	}


	public void setFloPrice(Float floPrice) {
		this.floPrice = floPrice;
	}


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
	
}
