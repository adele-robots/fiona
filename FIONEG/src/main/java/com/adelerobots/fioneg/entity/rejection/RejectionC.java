package com.adelerobots.fioneg.entity.rejection;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.adelerobots.fioneg.entity.SparkC;
import com.treelogic.fawna.arq.negocio.persistencia.datos.DataC;


@Entity(name = "rejection")
@Table(name = "rejection")
@org.hibernate.annotations.Proxy(lazy = true)

/**
 * Clase que representa la entidad "Rejection" de BBDD
 */
public class RejectionC extends DataC {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_rejection", nullable = false, length = 11)
	private Integer intCodRejection;
	
	@Column(name="FE_Rejection", nullable = false)
	private Date datRejection;
	
	@Column(name="DC_Motivo", nullable = false)
	private byte[] strMotivo;
	
	@ManyToOne(targetEntity=SparkC.class)
	@JoinColumn(name="cn_spark", nullable=false, updatable=false)
	private SparkC spark;
	
	
	
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
				+ ((this.intCodRejection == null) ? 0 : this.intCodRejection
						.hashCode());
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

		}

		else if (obj == null) {

			bEqual = false;

		}

		else if (getClass() != obj.getClass()) {

			bEqual = false;

		}

		else {

			final RejectionC other = (RejectionC) obj;

			if (this.intCodRejection == null) {

				if (other.intCodRejection != null) {

					bEqual = false;

				}

			}

			else if (!this.intCodRejection.equals(other.intCodRejection)) {

				bEqual = false;

			}

		}

		return bEqual;

	}

	public Integer getIntCodRejection() {
		return intCodRejection;
	}

	public void setIntCodRejection(Integer intCodRejection) {
		this.intCodRejection = intCodRejection;
	}

	public Date getDatRejection() {
		return datRejection;
	}

	public void setDatRejection(Date datRejection) {
		this.datRejection = datRejection;
	}

	public String getStrMotivo() {
		String strMotivoCadena = "";
		try {
			strMotivoCadena =  new String(strMotivo,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return strMotivoCadena;
	}

	public void setStrMotivo(String strMotivo) {
		try {
			this.strMotivo = strMotivo.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SparkC getSpark() {
		return spark;
	}

	public void setSpark(SparkC spark) {
		this.spark = spark;
	}
	
}
