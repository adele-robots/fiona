package com.adelerobots.fioneg.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.adelerobots.fioneg.entity.usuariospark.UsuarioSparkC;
import com.treelogic.fawna.arq.negocio.persistencia.datos.DataC;

@Entity(name = "transactiond")
@Table(name = "transactiond")
@org.hibernate.annotations.Proxy(lazy = true)
public class TransactionDC extends DataC {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_transaction", nullable = false, length = 11)
	private Integer intCodTransaction;

	@Column(name = "fe_renovation", nullable = false)
	private Date datRenovation;

	@Column(name = "fl_renewed", nullable = false)
	private Character chRenewed;

	@Column(name = "fl_emailsent", nullable = false)
	private Character chEmailSent;

	@Column(name = "fl_paid", nullable = false)
	private Character chPaid;

	@Column(name = "fl_charged", nullable = false)
	private Character chCharged;

	@Column(name = "nu_amount", nullable = false)
	private Float floAmount;

	@ManyToOne(targetEntity = UsuarioSparkC.class)
	@JoinColumns({ @JoinColumn(name = "CN_USUARIO"),
			@JoinColumn(name = "CN_SPARK") })
	private UsuarioSparkC usuarioSpark;

	public TransactionDC() {
		super();
	}

	/**
	 * Hashcode de objetos persistentes para colecciones que usen hash.
	 * Complementa el metodo {@link #equals(Object)} <br/>
	 * En persistencia dos objetos son iguales si tienen la misma Primary Key
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
				+ ((this.intCodTransaction == null) ? 0
						: this.intCodTransaction.hashCode());
		return result;

	}

	/**
	 * Comparador de igualdad para objetos persistentes. <br/>
	 * En persistencia dos objetos son iguales si tienen la misma Primary Key
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

			final TransactionDC other = (TransactionDC) obj;

			if (this.intCodTransaction == null) {

				if (other.intCodTransaction != null) {

					bEqual = false;

				}

			}

			else if (!this.intCodTransaction.equals(other.intCodTransaction)) {

				bEqual = false;

			}

		}

		return bEqual;

	}

	public Integer getIntCodTransaction() {
		return intCodTransaction;
	}

	public void setIntCodTransaction(Integer intCodTransaction) {
		this.intCodTransaction = intCodTransaction;
	}

	public Date getDatRenovation() {
		return datRenovation;
	}

	public void setDatRenovation(Date datRenovation) {
		this.datRenovation = datRenovation;
	}

	public Character getChRenewed() {
		return chRenewed;
	}

	public void setChRenewed(Character chRenewed) {
		this.chRenewed = chRenewed;
	}

	public Character getChEmailSent() {
		return chEmailSent;
	}

	public void setChEmailSent(Character chEmailSent) {
		this.chEmailSent = chEmailSent;
	}

	public Character getChPaid() {
		return chPaid;
	}

	public void setChPaid(Character chPaid) {
		this.chPaid = chPaid;
	}

	public Character getChCharged() {
		return chCharged;
	}

	public void setChCharged(Character chCharged) {
		this.chCharged = chCharged;
	}

	public Float getFloAmount() {
		return floAmount;
	}

	public void setFloAmount(Float floAmount) {
		this.floAmount = floAmount;
	}

	public UsuarioSparkC getUsuarioSpark() {
		return usuarioSpark;
	}

	public void setUsuarioSpark(UsuarioSparkC usuarioSpark) {
		this.usuarioSpark = usuarioSpark;
	}

}
