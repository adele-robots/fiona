package com.adelerobots.fioneg.entity.usuariospark;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.adelerobots.fioneg.entity.PriceC;
import com.adelerobots.fioneg.entity.SparkC;
import com.adelerobots.fioneg.entity.UsuarioC;
import com.adelerobots.fioneg.entity.opinion.OpinionC;

/**
 * Clase que mapea las operaciones con hibernate
 * 
 * @author adele
 * 
 */
@Entity(name = "UsuarioSpark")
@Table(name = "USUARIOSPARK")
@org.hibernate.annotations.Proxy(lazy = true)
public class UsuarioSparkC extends com.treelogic.fawna.arq.negocio.persistencia.datos.DataC {

	@EmbeddedId	
	private UsuarioSparkPk usuarioSparkPk;

	@Column(name = "FL_HIDDEN", nullable = true)
	private Character chHidden;
	
	@Column(name = "CN_USUARIO", nullable=false, updatable=false, insertable=false)
	private Integer intCodUsuario;
	
	@Column(name = "CN_SPARK", nullable=false, updatable=false, insertable=false)
	private Integer intCodSpark;
	
	@Column(name = "FL_USEDTRIAL", nullable=true)
	private Character chUsedSpark;
	
	@Column(name = "FE_USEDTRIAL", nullable=true)
	private Date datUsedSpark;
	
	@Column(name = "FL_ACTIVO", nullable=false)
	private Character chActivo;
	
	@Column(name = "FE_Purchase", nullable=false)
	private Date datPurchase;	

	@OneToMany(targetEntity = OpinionC.class)
	@JoinColumns ({ 
		@JoinColumn(name = "CN_Usuario"),
		@JoinColumn(name = "CN_Spark")
	})
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	private List<OpinionC> lstOpiniones= new LinkedList<OpinionC>();
	
	@ManyToOne (targetEntity = PriceC.class)
	@JoinColumn(name = "CN_Price")
	@NotFound(action=NotFoundAction.IGNORE)
	private PriceC price;
	
	// Estrategia de Luisín para salvar el bug de Hibernate
	// que impide hacer createAlias o createCriterias para
	// llegar a atributos del objeto mapeado en el PK
	@ManyToOne(targetEntity = SparkC.class)
	@JoinColumn(name = "CN_SPARK", nullable = true, updatable = false, insertable = false)
	private SparkC spark;
	// Modificado el 21-11-2013 para los nuevos batch
	// específicos de TCRF
	@ManyToOne(targetEntity = UsuarioC.class)
	@JoinColumn(name = "CN_USUARIO", nullable = true, updatable = false, insertable = false)
	private UsuarioC usuario;
	
	
	

	/**
	 * Constructor por defecto
	 */
	public UsuarioSparkC() {

	}

	/**
	 * Constructor de la clase
	 * @param chHidden Flag que indica si el usuario podrá o no ver el spark
	 */
	public UsuarioSparkC(Character chHidden) {
		this.chHidden = chHidden;
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
				+ ((this.usuarioSparkPk == null) ? 0
						: this.usuarioSparkPk.hashCode());
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
			final UsuarioSparkC other = (UsuarioSparkC) obj;
			if (this.usuarioSparkPk == null) {
				if (other.usuarioSparkPk != null) {
					bEqual = false;
				}
			} else if (!this.usuarioSparkPk
					.equals(other.usuarioSparkPk)) {
				bEqual = false;
			}
		}
		return bEqual;
	}

	public UsuarioSparkPk getUsuarioSparkPk() {
		return usuarioSparkPk;
	}

	public void setUsuarioSparkPk(UsuarioSparkPk usuarioSparkPk) {
		this.usuarioSparkPk = usuarioSparkPk;
	}

	public Character getChHidden() {
		return chHidden;
	}

	public void setChHidden(Character chHidden) {
		this.chHidden = chHidden;
	}

	public Integer getIntCodUsuario() {
		return intCodUsuario;
	}

	public void setIntCodUsuario(Integer intCodUsuario) {
		this.intCodUsuario = intCodUsuario;
	}

	public Integer getIntCodSpark() {
		return intCodSpark;
	}

	public void setIntCodSpark(Integer intCodSpark) {
		this.intCodSpark = intCodSpark;
	}

	public Character getChUsedSpark() {
		return chUsedSpark;
	}

	public void setChUsedSpark(Character chUsedSpark) {
		this.chUsedSpark = chUsedSpark;
	}

	public Date getDatUsedSpark() {
		return datUsedSpark;
	}

	public void setDatUsedSpark(Date datUsedSpark) {
		this.datUsedSpark = datUsedSpark;
	}

	public List<OpinionC> getLstOpiniones() {
		return lstOpiniones;
	}

	public void setLstOpiniones(List<OpinionC> lstOpiniones) {
		this.lstOpiniones = lstOpiniones;
	}

	public Character getChActivo() {
		return chActivo;
	}

	public void setChActivo(Character chActivo) {
		this.chActivo = chActivo;
	}

	public Date getDatPurchase() {
		return datPurchase;
	}

	public void setDatPurchase(Date datPurchase) {
		this.datPurchase = datPurchase;
	}

	public PriceC getPrice() {
		return price;
	}

	public void setPrice(PriceC price) {
		this.price = price;
	}
	
	public SparkC getSpark() {
		return spark;
	}

	public void setSpark(SparkC spark) {
		this.spark = spark;
	}

	public UsuarioC getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioC usuario) {
		this.usuario = usuario;
	}
	
}
