package com.adelerobots.fioneg.entity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OrderBy;

import com.adelerobots.fioneg.entity.avatarspark.AvatarSparkC;
import com.adelerobots.fioneg.entity.crash.CrashC;
import com.adelerobots.fioneg.entity.keyword.KeywordC;
import com.adelerobots.fioneg.entity.rejection.RejectionC;
import com.adelerobots.fioneg.entity.sparkstatus.SparkStatusC;
import com.adelerobots.fioneg.entity.status.StatusC;
import com.adelerobots.fioneg.entity.usuariospark.UsuarioSparkC;
import com.adelerobots.fioneg.util.keys.Constantes;

@Entity(name = "spark")
@Table(name = "spark")
@org.hibernate.annotations.Proxy(lazy = true)
 

public class SparkC extends com.treelogic.fawna.arq.negocio.persistencia.datos.DataC{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cn_spark", nullable = false, length = 11)
	private Integer cnSpark;
	
	@Column(name = "dc_nombre", nullable = false)
	private String strNombre;
	
	@Column(name = "cn_usuariodesarrollador", nullable = false, length = 100)
	private Integer cnUsuarioDesarrollador;
	
	@Lob
	@Column(name = "dc_descripcion",length = 100)	
	private byte[] strDescripcion;
	
	@Column(name = "dc_version")
	private String strVersion;
	
	@Column(name="dc_descripcionCorta")
	private String strDescripcionCorta;
	
	@Column(name="dc_novedadesVersion")
	private byte[] strNovedadesVersion;
	
	@Column(name="dc_otrosKeywords")
	private String strOtrosKeywords;
	
	@Column(name="dc_emailSoporte")
	private String strEmailSoporte;
	
	@Column(name="dc_marketingUrl")
	private String strMarketingUrl;	
	
//	@Column(name = "nu_preciodesarrollo")
//	private Float flPrecioDesarrollo;
//	
//	@Column(name = "nu_precioproduccion")
//	private Float flPrecioProduccion;
	
	@Column(name = "fl_trial", nullable = false)
	private Character chTrial;
	
	@Column(name = "nu_diastrial")
	private Integer intDiasTrial;
	
	@Column(name="fl_borrado", nullable=false)
	private Character chBorrado;
	
	@Column(name="dc_icono")
	private String strIcono;
	
	@Column(name="dc_banner")
	private String strBanner;
	
	@Column(name="dc_video")
	private String strVideo;
	
	//MTO usuario
	@ManyToOne (targetEntity = UsuarioC.class)
	@JoinColumn(name = "cn_usuariodesarrollador", insertable=false, updatable=false)
	private UsuarioC usuarioDesarrollador;
	
	@ManyToOne (targetEntity = EulaC.class)
	@JoinColumn(name = "cn_eula")
	private EulaC eula;
	
	/*
	@ManyToMany(targetEntity=SparkC.class, cascade={CascadeType.REFRESH})
	@JoinTable(name="UsuarioSpark", joinColumns=@JoinColumn(name="cn_spark"), 
								   inverseJoinColumns=@JoinColumn(name="cn_usuario"))
    private List<SparkC> lstusuarioc;*/
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "usuarioSparkPk.spark", targetEntity = UsuarioSparkC.class)
	@JoinColumn(name = "CN_SPARK", nullable = false)
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	private List<UsuarioSparkC> lstUsuarioSparkC;
	
	@ManyToMany(targetEntity=InterfazC.class, cascade={CascadeType.REFRESH})
	@JoinTable(name="SparkInterfaz", joinColumns=@JoinColumn(name="cn_spark"), 
								   inverseJoinColumns=@JoinColumn(name="cn_interfaz"))
    private List<InterfazC> lstinterfazc;

	//OneToMany a ConfigParam
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "spark", targetEntity = ConfigParamC.class)	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	private List<ConfigParamC> lstConfigParams= new LinkedList<ConfigParamC>();
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "sparkStatus.spark", targetEntity = SparkStatusC.class)
	@JoinColumn(name = "CN_SPARK", nullable = false)
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	@OrderBy(clause="FE_Cambiada desc")
	private List<SparkStatusC> lstSparkStatusC;
	
	@ManyToMany(targetEntity=KeywordC.class, cascade={CascadeType.REFRESH})
	@JoinTable(name="SparkKeyword", joinColumns=@JoinColumn(name="cn_spark"), 
								   inverseJoinColumns=@JoinColumn(name="cn_keyword"))
    private List<KeywordC> lstKeywords;
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "spark", targetEntity = RejectionC.class)
	@JoinColumn(name = "CN_SPARK", nullable = false)
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	private List<RejectionC> lstRejections;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "spark", targetEntity = CrashC.class)
	@JoinColumn(name = "CN_SPARK", nullable = false)
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	private List<CrashC> lstCrashes;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "spark", targetEntity = PriceC.class)
	@JoinColumn(name = "CN_SPARK", nullable = false)
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	private List<PriceC> lstPrices = new ArrayList<PriceC>();
	/*
	@ManyToMany(targetEntity=SparkC.class, cascade={CascadeType.REFRESH})
	@JoinTable(name="avatarSpark", joinColumns=@JoinColumn(name="cn_spark"), 
								   inverseJoinColumns=@JoinColumn(name="cn_avatar"))
    private List<SparkC> lstAvatars;*/
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "avatarSparkPk.spark", targetEntity = AvatarSparkC.class)
	@JoinColumn(name = "CN_SPARK", nullable = false)
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)
	private List<AvatarSparkC> lstAvatarSparkC;
	
	
	@Transient
	/** Propiedad utilizada para determinar si un spark ha sido comprado por un determinado usuario */
	private Boolean boolAdquirido = Boolean.FALSE;
	
	@Transient
	/** Propiedad utilizada para almacenar el status actual de un spark */
	private StatusC lastStatus;
	
	@Transient
	/** Propiedad auxiliar para recuperar la ruta a la imagen del spark */
	private String strIconPath;
	
	@Transient
	private Boolean boolGratuitoDes = Boolean.TRUE;
	
	@Transient
	private Boolean boolGratuitoProd = Boolean.TRUE;	
	
	@Transient
	/** Propiedad usada para  */
	private Float floValoracionMedia = new Float(0);
	
	@Transient
	private Float floPrecioSeleccionadoProd = new Float(0);
		

	public SparkC(){
		super();
	}
	
	
	public SparkC(Integer cnSpark, String strNombre, String strDescripcion, String strVersion, String strDescripcionCorta, String strNovedadesVersion, 
			String strOtrosKeywords, String strEmailSoporte, String strMarketingUrl, Character chTrial, 
			Integer intDiasTrial, String strIcono, String strBanner, String strVideo){
		
		super();
		
		this.cnSpark = cnSpark;
		this.strNombre = strNombre;
		try {
			if(strDescripcion != null)
				this.strDescripcion = strDescripcion.getBytes("UTF-8");				
			if(this.strNovedadesVersion != null)
				this.strNovedadesVersion = strNovedadesVersion.getBytes("UTF-8");
//			if(strOtrosKeywords != null)
//				this.strOtrosKeywords = strOtrosKeywords.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.strOtrosKeywords = strOtrosKeywords;
		this.strDescripcionCorta = strDescripcionCorta;
		this.strVersion = strVersion;		
		this.strEmailSoporte = strEmailSoporte;
		this.strMarketingUrl = strMarketingUrl;		
		this.chTrial = chTrial;
		this.intDiasTrial = intDiasTrial;
		this.strIcono = strIcono;
		this.strBanner = strBanner;
		this.strVideo = strVideo;		
		
	}
	
	public SparkC(String strNombre, Integer cnUsuarioDesarrollador, String strDescripcion, String strVersion, String strDescripcionCorta, 
			String strEmailSoporte, Character chTrial, Character chBorrado, EulaC eula){
		
		super();
		
		this.strNombre = strNombre;
		this.cnUsuarioDesarrollador = cnUsuarioDesarrollador;
		try {
			if(strDescripcion != null)
				this.strDescripcion = strDescripcion.getBytes("UTF-8");				
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.strDescripcionCorta = strDescripcionCorta;
		this.strVersion = strVersion;			
		this.strEmailSoporte = strEmailSoporte;	
		this.chTrial = chTrial;
		this.chBorrado = chBorrado;
		this.eula = eula;
	}

	public Integer getCnSpark() {
		return cnSpark;
	}

	public void setCnSpark(Integer cnSpark) {
		this.cnSpark = cnSpark;
	}

	public String getStrNombre() {
		return strNombre;
	}

	public void setStrNombre(String strNombre) {
		this.strNombre = strNombre;
	}

	public Integer getCnUsuarioDesarrollador() {
		return cnUsuarioDesarrollador;
	}

	public void setCnUsuarioDesarrollador(Integer cnUsuarioDesarrollador) {
		this.cnUsuarioDesarrollador = cnUsuarioDesarrollador;
	}
		
	public EulaC getEula() {
		return eula;
	}

	public void setEula(EulaC eula) {
		this.eula = eula;
	}

	public String getStrDescripcion() {
		String strDescripcionCadena = "";
		try {
			if(strDescripcion != null)
				strDescripcionCadena = new String(strDescripcion,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strDescripcionCadena;
	}

	public void setStrDescripcion(String strDescripcion) {
		try {
			if(strDescripcion != null)
				this.strDescripcion = strDescripcion.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public String getStrVersion() {
		return strVersion;
	}


	public void setStrVersion(String strVersion) {
		this.strVersion = strVersion;
	}


	public String getStrDescripcionCorta() {
		/*
		String strDescCortaCadena = "";
		try {
			if(strDescripcionCorta != null)
				strDescCortaCadena = new String(strDescripcionCorta,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strDescCortaCadena;
		*/
		return strDescripcionCorta;
	}


	public void setStrDescripcionCorta(String strDescripcionCorta) {
		/*
		try {
			if(strDescripcionCorta != null)
				this.strDescripcionCorta = strDescripcionCorta.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		this.strDescripcionCorta = strDescripcionCorta;
	}


	public String getStrNovedadesVersion() {
		String strNovedadesVersionCadena = "";
		try {
			if(strNovedadesVersion != null)
				strNovedadesVersionCadena = new String(strNovedadesVersion,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strNovedadesVersionCadena;
	}


	public void setStrNovedadesVersion(String strNovedadesVersion) {
		try {
			if(strNovedadesVersion != null)
				this.strNovedadesVersion = strNovedadesVersion.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public String getStrOtrosKeywords() {
		/**
		String strOtrosKeywordsCadena = "";
		try {
			if(strOtrosKeywords != null)
				strOtrosKeywordsCadena = new String(strOtrosKeywords,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strOtrosKeywordsCadena;
		*/
		return strOtrosKeywords;
	}


	public void setStrOtrosKeywords(String strOtrosKeywords) {
		/*
		try {
			if(strOtrosKeywords != null)
				this.strOtrosKeywords = strOtrosKeywords.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		this.strOtrosKeywords = strOtrosKeywords;
	}


	public String getStrEmailSoporte() {
		return strEmailSoporte;
	}


	public void setStrEmailSoporte(String strEmailSoporte) {
		this.strEmailSoporte = strEmailSoporte;
	}


	public String getStrMarketingUrl() {
		return strMarketingUrl;
	}


	public void setStrMarketingUrl(String strMarketingUrl) {
		this.strMarketingUrl = strMarketingUrl;
	}


//	public Float getFlPrecioDesarrollo() {
//		return flPrecioDesarrollo;
//	}
//
//	public void setFlPrecioDesarrollo(Float flPrecioDesarrollo) {
//		this.flPrecioDesarrollo = flPrecioDesarrollo;
//	}
//
//	public Float getFlPrecioProduccion() {
//		return flPrecioProduccion;
//	}
//
//	public void setFlPrecioProduccion(Float flPrecioProduccion) {
//		this.flPrecioProduccion = flPrecioProduccion;
//	}		
	
	public Character getChTrial() {
		return chTrial;
	}

	public void setChTrial(Character chTrial) {
		this.chTrial = chTrial;
	}
	
	public Integer getIntDiasTrial() {
		return intDiasTrial;
	}

	public void setIntDiasTrial(Integer intDiasTrial) {
		this.intDiasTrial = intDiasTrial;
	}
	
			
	public String getStrIcono() {
		return strIcono;
	}


	public void setStrIcono(String strIcono) {
		this.strIcono = strIcono;
	}


	public String getStrBanner() {
		return strBanner;
	}


	public void setStrBanner(String strBanner) {
		this.strBanner = strBanner;
	}


	public String getStrVideo() {
		return strVideo;
	}


	public void setStrVideo(String strVideo) {
		this.strVideo = strVideo;
	}

	public UsuarioC getUsuarioDesarrollador() {
		return usuarioDesarrollador;
	}

	public void setUsuarioDesarrollador(UsuarioC usuarioDesarrollador) {
		this.usuarioDesarrollador = usuarioDesarrollador;
	}

	public Character getChBorrado() {
		return chBorrado;
	}

	public void setChBorrado(Character chBorrado) {
		this.chBorrado = chBorrado;
	}

	

	
	public List<UsuarioSparkC> getLstUsuarioSparkC() {
		return lstUsuarioSparkC;
	}

	public void setLstUsuarioSparkC(List<UsuarioSparkC> lstUsuarioSparkC) {
		this.lstUsuarioSparkC = lstUsuarioSparkC;
	}

	public List<InterfazC> getLstinterfazc() {
		return lstinterfazc;
	}

	public void setLstinterfazc(List<InterfazC> lstinterfazc) {
		this.lstinterfazc = lstinterfazc;
	}
	

	public List<ConfigParamC> getLstConfigParams() {
		return lstConfigParams;
	}

	public void setLstConfigParams(List<ConfigParamC> lstConfigParams) {
		this.lstConfigParams = lstConfigParams;
	}

	public List<SparkStatusC> getLstSparkStatusC() {
		return lstSparkStatusC;
	}

	public void setLstSparkStatusC(List<SparkStatusC> lstSparkStatusC) {
		this.lstSparkStatusC = lstSparkStatusC;
	}

	public List<KeywordC> getLstKeywords() {
		return lstKeywords;
	}

	public void setLstKeywords(List<KeywordC> lstKeywords) {
		this.lstKeywords = lstKeywords;
	}
		
	public List<RejectionC> getLstRejections() {
		return lstRejections;
	}

	public void setLstRejections(List<RejectionC> lstRejections) {
		this.lstRejections = lstRejections;
	}
	
	public List<CrashC> getLstCrashes() {
		return lstCrashes;
	}

	public void setLstCrashes(List<CrashC> lstCrashes) {
		this.lstCrashes = lstCrashes;
	}	
	

	public List<PriceC> getLstPrices() {
		return lstPrices;
	}


	public void setLstPrices(List<PriceC> lstPrices) {
		this.lstPrices = lstPrices;
	}

	public List<AvatarSparkC> getLstAvatarSparkC() {
		return lstAvatarSparkC;
	}

	public void setLstAvatarSparkC(List<AvatarSparkC> lstAvatarSparkC) {
		this.lstAvatarSparkC = lstAvatarSparkC;
	}

	public Boolean getBoolAdquirido() {
		return boolAdquirido;
	}

	public void setBoolAdquirido(Boolean boolAdquirido) {
		this.boolAdquirido = boolAdquirido;
	}

	public StatusC getLastStatus() {
		return (lastStatus!=null)?lastStatus:((lstSparkStatusC!=null && !lstSparkStatusC.isEmpty())?(lstSparkStatusC.get(0).getSparkStatus().getStatus()):null);
	}

	public void setLastStatus(StatusC lastStatus) {
		this.lastStatus = lastStatus;
	}


	public String getStrIconPath() {
		return usuarioDesarrollador.getAvatarBuilderUmd5()+"/"+Constantes.getIconsFolder()+"/"+strIcono;
	}


	public void setStrIconPath(String strIconPath) {
		this.strIconPath = strIconPath;
	}


	public Boolean getBoolGratuitoDes() {
		List<PriceC> precios = getLstPrices();
		
		for(int i = 0; i<precios.size() && boolGratuitoDes;i++){
			if(precios.get(i).getChActivo().equals('1') && precios.get(i).getChDevelopment().equals('1'))
				boolGratuitoDes = Boolean.FALSE;
		}
		
		return boolGratuitoDes;
	}	
	
	public Boolean getBoolGratuitoProd() {
		List<PriceC> precios = getLstPrices();
		
		for(int i = 0; i<precios.size() && boolGratuitoProd;i++){
			if(precios.get(i).getChActivo()=='1' && precios.get(i).getChDevelopment().equals('0'))
				boolGratuitoProd = Boolean.FALSE;
		}
		
		return boolGratuitoProd;
	}
	
	/**
	 * Método que devolverá los precios de desarrollo del spark
	 * @return
	 */
	public List<PriceC> getDevelopmentPrices(){
		List <PriceC> lstAllPrices = getLstPrices();
		List <PriceC> lstDevPrices = new ArrayList<PriceC>();
		
		for(int i = 0; i < lstAllPrices.size(); i++){
			if(lstAllPrices.get(i).getChDevelopment().equals('1')){
				lstDevPrices.add(lstAllPrices.get(i));
			}
		}
		
		return lstDevPrices;
		
	}
	
	public List<PriceC> getProductionPrices(){
		List <PriceC> lstAllPrices = getLstPrices();
		List <PriceC> lstProdPrices = new ArrayList<PriceC>();
		
		for(int i = 0; i< lstAllPrices.size(); i++){
			if(lstAllPrices.get(i).getChDevelopment().equals('0')){
				lstProdPrices.add(lstAllPrices.get(i));
			}
		}
		
		return lstProdPrices;
	}


	public Float getFloValoracionMedia() {
		return floValoracionMedia;
	}


	public void setFloValoracionMedia(Float floValoracionMedia) {
		this.floValoracionMedia = floValoracionMedia;
	}


	public Float getFloPrecioSeleccionadoProd() {
		return floPrecioSeleccionadoProd;
	}


	public void setFloPrecioSeleccionadoProd(Float floPrecioSeleccionadoProd) {
		this.floPrecioSeleccionadoProd = floPrecioSeleccionadoProd;
	}

}
