package com.adelerobots.fioneg.entity.opinion;

import java.io.UnsupportedEncodingException;
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

/**
 * Clase que mapea las operaciones con hibernate
 * para la tabla "opinion" de la base de datos
 * 
 * @author adele
 *
 */
@Entity(name = "opinion")
@Table(name = "opinion")
@org.hibernate.annotations.Proxy(lazy = true)
public class OpinionC extends DataC {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_opinion", nullable = false, length = 11)
	private Integer intCodOpinion;	
	
	@Column(name = "fe_enviada")
	private Date datEnviada;
	
	@Column(name = "nu_valoracion", nullable = false)
	private Integer intValoracion;
	
	@Column(name = "dc_descripcion")
	private byte[] strDescripcion;
	
	@Column(name = "dc_titulo", nullable = false)
	private String strTitulo;
	
	/* Una opinión está asociada a un spark y a un usuario */
	/* Relación mapeada en UsuarioSparkC */	
	
	@ManyToOne (targetEntity = UsuarioSparkC.class)
	@JoinColumns({
		@JoinColumn(name = "CN_USUARIO"),
		@JoinColumn(name = "CN_SPARK")
	})	
	private UsuarioSparkC usuarioSpark;
	
	
	public OpinionC(){
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
				+ ((this.intCodOpinion == null) ? 0 : this.intCodOpinion
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

			final OpinionC other = (OpinionC) obj;

			if (this.intCodOpinion == null) {

				if (other.intCodOpinion != null) {

					bEqual = false;

				}

			}

			else if (!this.intCodOpinion.equals(other.intCodOpinion)) {

				bEqual = false;

			}

		}

		return bEqual;

	}	
	

	public Integer getIntCodOpinion() {
		return intCodOpinion;
	}

	public void setIntCodOpinion(Integer intCodOpinion) {
		this.intCodOpinion = intCodOpinion;
	}

	public Date getDatEnviada() {
		return datEnviada;
	}

	public void setDatEnviada(Date datEnviada) {
		this.datEnviada = datEnviada;
	}

	public Integer getIntValoracion() {
		return intValoracion;
	}

	public void setIntValoracion(Integer intValoracion) {
		this.intValoracion = intValoracion;
	}
	
	public String getStrDescripcion() {
		String strDescripcionCadena = "";
		
		try {
			if(strDescripcion != null)
				strDescripcionCadena =  new String(strDescripcion,"UTF-8");
			else
				strDescripcionCadena = null;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return strDescripcionCadena;
	}

	public void setStrDescripcion(String strDescripcion) {
		try{
		this.strDescripcion = strDescripcion.getBytes("UTF-8");
		}catch (UnsupportedEncodingException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}		

	public String getStrTitulo() {
		return strTitulo;
	}

	public void setStrTitulo(String strTitulo) {
		this.strTitulo = strTitulo;
	}

	public UsuarioSparkC getUsuarioSpark() {
		return usuarioSpark;
	}

	public void setUsuarioSpark(UsuarioSparkC usuarioSpark) {
		this.usuarioSpark = usuarioSpark;
	}
	

}
