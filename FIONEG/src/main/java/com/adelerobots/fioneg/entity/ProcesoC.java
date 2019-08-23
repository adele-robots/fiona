package com.adelerobots.fioneg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity (name = "proceso")
@Table(name = "proceso")
@org.hibernate.annotations.Proxy(lazy = true)
public class ProcesoC extends com.treelogic.fawna.arq.negocio.persistencia.datos.DataC{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cn_proceso", nullable = false)
	private Integer cnProceso;
	
	@Column(name = "nu_pid", nullable = false)
	private Integer nuPid;
	
	@Column(name = "cn_usuario", nullable = false)
	private Integer cnUser;
	
	@Column(name = "dc_host", nullable = false)
	private String dcHost;
	
	@Column(name = "nu_timestamp", nullable = false)
	private long nuTimestamp;
	
	@Column(name = "nu_timestart", nullable = false)
	private long nuTimestart;
	
	@Column(name = "nu_timealive", nullable = false)
	private long nuTimealive;
	
	@Column(name = "nu_timeallowedexecution")
	private long nuTimeAllowedExecution;
	
	@Column(name="fl_killedbyarpia")
	private Character chKilledByARPIA;

	@ManyToOne (fetch=FetchType.LAZY, targetEntity = UsuarioC.class)
	@JoinColumn(name = "cn_usuario", insertable=false, updatable=false)
	@org.hibernate.annotations.LazyToOne(org.hibernate.annotations.LazyToOneOption.PROXY)
	private UsuarioC usuario;

	public ProcesoC(){
		super();
	}
	
	public ProcesoC(Integer user, Integer pid, String host, long timestamp, long timestart, long timealive) {
		super();
		this.cnUser=user;
		this.dcHost=host;
		this.nuPid=pid;
		this.nuTimestamp=timestamp;
		this.nuTimestart=timestart;
		this.nuTimealive=timealive;
	}
	
	public ProcesoC(Integer idproceso, Integer user, Integer pid, String host, long timestamp) {
		super();
		this.cnProceso=idproceso;
		this.cnUser=user;
		this.dcHost=host;
		this.nuPid=pid;
		this.nuTimestamp=timestamp;
	}


	public Integer getCnProceso() {
		return cnProceso;
	}

	public void setCnProceso(Integer cnProceso) {
		this.cnProceso = cnProceso;
	}

	public UsuarioC getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioC usuario) {
		this.usuario = usuario;
	}

	public Integer getNuPid() {
		return nuPid;
	}

	public void setNuPid(Integer nuPid) {
		this.nuPid = nuPid;
	}

	public Integer getCnUser() {
		return cnUser;
	}

	public void setCnUser(Integer cnUser) {
		this.cnUser = cnUser;
	}

	public String getDcHost() {
		return dcHost;
	}

	public void setDcHost(String dcHost) {
		this.dcHost = dcHost;
	}

	public long getNuTimestamp() {
		return nuTimestamp;
	}

	public void setNuTimestamp(long nuTimestamp) {
		this.nuTimestamp = nuTimestamp;
	}
	
	public long getNuTimestart() {
		return nuTimestart;
	}

	public void setNuTimestart(long nuTimestart) {
		this.nuTimestart = nuTimestart;
	}

	public long getNuTimealive() {
		return nuTimealive;
	}

	public void setNuTimealive(long nuTimealive) {
		this.nuTimealive = nuTimealive;
	}

	public long getNuTimeAllowedExecution() {
		return nuTimeAllowedExecution;
	}

	public void setNuTimeAllowedExecution(long nuTimeAllowedExecution) {
		this.nuTimeAllowedExecution = nuTimeAllowedExecution;
	}

	public Character getChKilledByARPIA() {
		return chKilledByARPIA;
	}

	public void setChKilledByARPIA(Character chKilledByARPIA) {
		this.chKilledByARPIA = chKilledByARPIA;
	}
		

}
