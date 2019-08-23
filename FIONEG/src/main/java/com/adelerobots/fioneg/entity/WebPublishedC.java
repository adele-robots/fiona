package com.adelerobots.fioneg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;



@Entity(name = "webpublished")
@Table(name = "webpublished")
@org.hibernate.annotations.Proxy(lazy = true)
public class WebPublishedC extends com.treelogic.fawna.arq.negocio.persistencia.datos.DataC{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_webpublished", nullable = false, length = 11)
	private Integer intCodWebPublished;	
	
	@OneToOne (targetEntity = UsuarioC.class)
	@JoinColumn(name = "CN_Usuario")
	private UsuarioC usuario;
	
	@Column(name="fl_published")
	private Character chPublished;
	
	
	
	public WebPublishedC(){
		super();
	}	
		

	public Integer getIntCodWebPublished() {
		return intCodWebPublished;
	}

	public void setIntCodWebPublished(Integer intCodWebPublished) {
		this.intCodWebPublished = intCodWebPublished;
	}

	public UsuarioC getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioC usuario) {
		this.usuario = usuario;
	}

	public Character getChPublished() {
		return chPublished;
	}

	public void setChPublished(Character chPublished) {
		this.chPublished = chPublished;
	}

	
	
	
}
