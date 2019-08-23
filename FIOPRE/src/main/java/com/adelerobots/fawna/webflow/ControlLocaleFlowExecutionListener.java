package com.adelerobots.fawna.webflow;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.FlowExecutionListener;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.RequestContext;

import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.persistencia.ContextoLocator;
import com.treelogic.fawna.presentacion.core.persistencia.ContextoSesion;
import com.treelogic.fawna.presentacion.core.persistencia.DatoFactory;
import com.treelogic.fawna.presentacion.core.persistencia.IDato;
import com.treelogic.fawna.presentacion.core.utilidades.constantes.ConstantesSesion;

/**
 * {@link FlowExecutionListener} para manejar las constantes de Locale
 * almacenadas en la arquitectura de Fawna
 * 
 * @author adele
 * 
 * @see ConstantesSesion#ARCH_LOCALE
 * @see ContextoLocator#getContextoSesion()
 * @see LocaleContextHolder#getLocale()
 */
public class ControlLocaleFlowExecutionListener 
		extends FlowExecutionListenerAdapter
{

	protected final Log logger = LogFactory.getLog(getClass());
	
	/** Locale usado en ultima instancia si no se puede resolver los internos */
	private Locale fallbackLocale;
	/** Obtiene el locale usado en ultima instancia si no se puede resolver a partir de la peticion */
	public Locale getFallbackLocale() {	return fallbackLocale;}
	/** Establece el locale usado en ultima instancia si no se puede resolver a partir de la peticion */
	public void setFallbackLocale(Locale locale) { this.fallbackLocale = locale; }






	/**
	 * Obtiene el locale almacenado por la arquitectura
	 * @return
	 * @see ConstantesSesion#ARCH_LOCALE
	 * @see ContextoLocator#getContextoSesion()
	 */
	protected Locale getArchLocale()
	{
		Locale locale = null;
		try {
			final ContextoSesion sessionContext = ContextoLocator.getInstance().getContextoSesion();
			Object value, o = sessionContext.getCtxValue(ConstantesSesion.ARCH_LOCALE);
			if (o instanceof IDato) {
				value = ((IDato) o).getValor();
			} else {
				value = o;
			}
			if (value instanceof Locale) {
				locale = (Locale) value;
			} else if (value instanceof String) {
				locale = StringUtils.parseLocaleString((String)value);
			}
		} catch (PersistenciaException e) {	}
		return locale;
	}


	/**
	 * Obtiene el locale actual segun la peticion actual
	 * @return
	 */
	protected Locale getCurrentLocale()
	{
		Locale locale = getArchLocale();
		if (locale == null) {
			//Fallback to Spring locale
			locale = LocaleContextHolder.getLocale();
		}
		return locale;
	}

	/**
	 * Resuelve el locale usado para mantener 
	 * esta variable en la arquitectura
	 * @return
	 */
	protected Locale resolveLocale()
	{
		Locale locale = getArchLocale();
		if (locale == null) {
			locale = getFallbackLocale();
		}
		if (locale == null) {
			//Fallback to Spring locale
			locale = LocaleContextHolder.getLocale();
		}
		return locale;
	}

	/**
	 * Almacena el locale indicado en las variables de arquitectura 
	 * @param locale
	 */
	protected void storeLocale(Locale locale) {
		try {
			final ContextoSesion sessionContext = ContextoLocator.getInstance().getContextoSesion();
			
			//Create new dato
			final IDato datoLocale = DatoFactory.creaDatoSimple();
			datoLocale.setPropiedad(ConstantesSesion.ARCH_LOCALE);
			datoLocale.setValor(locale.toString());
			
			//Store
			sessionContext.putCtxValue(datoLocale);
			
		} catch (PersistenciaException e) {	}
	}


	/* (non-Javadoc)
	 * @see org.springframework.webflow.execution.FlowExecutionListenerAdapter#sessionStarting(org.springframework.webflow.execution.RequestContext, org.springframework.webflow.execution.FlowSession, org.springframework.webflow.core.collection.MutableAttributeMap)
	 */
	@Override
	public final void sessionStarting(
			final RequestContext context, 
			final FlowSession session,
			final MutableAttributeMap input) 
	{
		final Locale locale = resolveLocale();
		if (locale != null) {
			storeLocale(locale);
		}
	}





}
