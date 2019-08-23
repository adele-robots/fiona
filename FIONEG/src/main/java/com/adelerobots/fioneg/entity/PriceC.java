package com.adelerobots.fioneg.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.adelerobots.fioneg.entity.usuariospark.UsuarioSparkC;
import com.treelogic.fawna.arq.negocio.persistencia.datos.DataC;

/**
 * Clase que mapea las operaciones con hibernate
 * para la tabla "price" de la base de datos
 * 
 * @author adele
 *
 */
@Entity(name = "price")
@Table(name = "price")
@org.hibernate.annotations.Proxy(lazy = true)
public class PriceC extends DataC {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_price", nullable = false)
	private Integer intCodPrice;
	
	@Column(name="nu_amount")
	private Integer intAmount;
	
	@Column(name="nu_price")
	private Float floPrize;
	
	@Column(name="nu_utilization")
	private Integer intUtilization;
	
	@Column(name="fl_development")
	private Character chDevelopment;
	
	@Column(name="nu_UsrConcu")
	private Integer intUsrConcu;
	
	@Column(name="fl_activo")
	private Character chActivo; // Campo para dar por borrado un precio por el desarrollador
	
	@ManyToOne(targetEntity=UnitC.class)
	@JoinColumn(name="cn_unit")
	private UnitC unit;
	
	@ManyToOne(targetEntity=UtilizationC.class)
	@JoinColumn(name="cn_utilization")
	private UtilizationC utilization;
	
	@ManyToOne (targetEntity = SparkC.class)
	@JoinColumn(name = "CN_Spark")
	private SparkC spark;
	
	@OneToMany (targetEntity = UsuarioSparkC.class)
	@JoinColumn(name = "CN_Price")
	private List<UsuarioSparkC> lstUsuarioSpark = new ArrayList<UsuarioSparkC>();
	
	@Transient
	/** Propiedad utilizada para determinar si un precio está asociad a algún "UsuarioSpark" */
	private Boolean boolUsado = Boolean.FALSE;
		
	
	public PriceC(){
		super();
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
				+ ((this.intCodPrice == null) ? 0 : this.intCodPrice
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

			final PriceC other = (PriceC) obj;

			if (this.intCodPrice == null) {

				if (other.intCodPrice != null) {

					bEqual = false;

				}

			}

			else if (!this.intCodPrice.equals(other.intCodPrice)) {

				bEqual = false;

			}

		}

		return bEqual;

	}

	public Integer getIntCodPrice() {
		return intCodPrice;
	}

	public void setIntCodPrice(Integer intCodPrice) {
		this.intCodPrice = intCodPrice;
	}

	public Integer getIntAmount() {
		return intAmount;
	}

	public void setIntAmount(Integer intAmount) {
		this.intAmount = intAmount;
	}

	public Float getFloPrize() {
		return floPrize;
	}

	public void setFloPrize(Float floPrize) {
		this.floPrize = floPrize;
	}

	public Integer getIntUtilization() {
		return intUtilization;
	}

	public void setIntUtilization(Integer intUtilization) {
		this.intUtilization = intUtilization;
	}

	public Character getChDevelopment() {
		return chDevelopment;
	}

	public void setChDevelopment(Character chDevelopment) {
		this.chDevelopment = chDevelopment;
	}	

	public Integer getIntUsrConcu() {
		return intUsrConcu;
	}

	public void setIntUsrConcu(Integer intUsrConcu) {
		this.intUsrConcu = intUsrConcu;
	}
		
	public Character getChActivo() {
		return chActivo;
	}


	public void setChActivo(Character chActivo) {
		this.chActivo = chActivo;
	}


	public UnitC getUnit() {
		return unit;
	}

	public void setUnit(UnitC unit) {
		this.unit = unit;
	}

	public UtilizationC getUtilization() {
		return utilization;
	}

	public void setUtilization(UtilizationC utilization) {
		this.utilization = utilization;
	}

	public SparkC getSpark() {
		return spark;
	}

	public void setSpark(SparkC spark) {
		this.spark = spark;
	}
	
	public List<UsuarioSparkC> getLstUsuarioSpark() {
		return lstUsuarioSpark;
	}

	public void setLstUsuarioSpark(List<UsuarioSparkC> lstUsuarioSpark) {
		this.lstUsuarioSpark = lstUsuarioSpark;
	}

	public Boolean getBoolUsado() {
		return (getLstUsuarioSpark() != null && getLstUsuarioSpark().size() > 0);
	}
}
