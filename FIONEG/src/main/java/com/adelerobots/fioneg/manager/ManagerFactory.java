package com.adelerobots.fioneg.manager;

import com.adelerobots.fioneg.util.keys.Constantes;

/**
 * Factoria de managers (Patron factory)
 */
public class ManagerFactory {

	/** Instancia de la clase */
	private static ManagerFactory instance = new ManagerFactory();

	/** Constructor privado. */
	private ManagerFactory() {
	}

	/**
	 * Retorna una instancia de la factoria.
	 * 
	 * @return ManagerFactory
	 */
	public static ManagerFactory getInstance() {
		return instance;
	}

	//Manager para la gestion de usuarios
	public UsuariosManager getUsuariosManager()
	{
		return new UsuariosManager(Constantes.CTE_JNDI_DATASOURCE);
	}

	public EntidadManager getEntidadManager()
	{
		return new EntidadManager(Constantes.CTE_JNDI_DATASOURCE);
	}

	public CuentaManager getCuentaManager()
	{
		return new CuentaManager(Constantes.CTE_JNDI_DATASOURCE);
	}

	public RoleUsuarioManager getRoleUsuarioManager()
	{
		return new RoleUsuarioManager(Constantes.CTE_JNDI_DATASOURCE);
	}
	
	public SparkManager getSparkManager()
	{
		return new SparkManager(Constantes.CTE_JNDI_DATASOURCE);
	}
	
	public DatoListaUsuarioManager getDatoListaUsuarioManager()
	{
		return new DatoListaUsuarioManager(Constantes.CTE_JNDI_DATASOURCE);
	}
	
	public ProcesoManager getProcesoManager()
	{
		return new ProcesoManager(Constantes.CTE_JNDI_DATASOURCE);
	}
	
	public ConfigParamManager getConfigParamManager()
	{
		return new ConfigParamManager(Constantes.CTE_JNDI_DATASOURCE);
	}
	
	public StatusManager getStatusManager()
	{
		return new StatusManager(Constantes.CTE_JNDI_DATASOURCE);
	}
	
	public KeywordsManager getKeywordsManager()
	{
		return new KeywordsManager(Constantes.CTE_JNDI_DATASOURCE);
	}

	public InterfazManager getInterfazManager()
	{
		return new InterfazManager(Constantes.CTE_JNDI_DATASOURCE);
	}
	
	public WebPublishedManager getWebPublishedManager()
	{
		return new WebPublishedManager(Constantes.CTE_JNDI_DATASOURCE);
	}
	
	public UnitManager getUnitManager(){
		return new UnitManager(Constantes.CTE_JNDI_DATASOURCE);
	}
	
	public UtilizationManager getUtilizationManager(){
		return new UtilizationManager(Constantes.CTE_JNDI_DATASOURCE);
	}
	
	public PriceManager getPriceManager(){
		return new PriceManager(Constantes.CTE_JNDI_DATASOURCE);
	}
	
	public TipoManager getTipoManager(){
		return new TipoManager(Constantes.CTE_JNDI_DATASOURCE);
	}

	public DatoListaManager getDatoListaManager() {
		return new DatoListaManager(Constantes.CTE_JNDI_DATASOURCE);
	}

	public TipoEntidadManager getTipoEntidadManager() {
		return new TipoEntidadManager(Constantes.CTE_JNDI_DATASOURCE);
	}
	
	public UsuarioConfigManager getUsuarioConfigManager() {
		return new UsuarioConfigManager(Constantes.CTE_JNDI_DATASOURCE);
	}
	
	public AvatarManager getAvatarManager() {
		return new AvatarManager(Constantes.CTE_JNDI_DATASOURCE);
	}
}