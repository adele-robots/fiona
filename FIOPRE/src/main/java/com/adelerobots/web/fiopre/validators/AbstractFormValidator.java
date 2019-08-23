package com.adelerobots.web.fiopre.validators;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

import com.treelogic.fawna.presentacion.componentes.validators.form.interfaces.IValidacionFormulario;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.persistencia.ContextoLocator;
import com.treelogic.fawna.presentacion.core.persistencia.ContextoSesion;
import com.treelogic.fawna.presentacion.core.persistencia.IDato;
import com.treelogic.fawna.presentacion.core.utilidades.Properties;
import com.treelogic.fawna.presentacion.core.utilidades.constantes.ConstantesSesion;


/**
 * Clase base para las validaciones de formulario
 * @author adele
 *
 */
public abstract class AbstractFormValidator implements IValidacionFormulario
{

	private static final long serialVersionUID = -1471516368267838887L;
	
	private static final MessageFormat INVALID_MESSAGE_FORMAT = new MessageFormat("");

	/**
	 * Cache to hold already generated MessageFormats per message.
	 * Used for passed-in default messages. MessageFormats for resolved
	 * codes are cached on a specific basis in subclasses.
	 */
	private final Map<String, MessageFormat> cachedMessageFormats = new HashMap<String, MessageFormat>();




	protected Locale getDefaultLocale()
	{
		Locale locale;
		try {
			ContextoSesion sessionContext = ContextoLocator.getInstance().getContextoSesion();
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
			} else {
				//Fallback to Spring locale
				locale = LocaleContextHolder.getLocale();
			}
		} catch (PersistenciaException e) {
			//Fallback to Spring locale
			locale = LocaleContextHolder.getLocale();
		}
		return locale;
	}




	/**
	 * Format the given message String, using cached MessageFormats.
	 * By default invoked for passed-in default messages, to resolve
	 * any argument placeholders found in them.
	 * @param msg the message to format
	 * @param args array of arguments that will be filled in for params within
	 * the message, or <code>null</code> if none
	 * @return the formatted message (with resolved arguments)
	 */
	protected final String formatMessage(String msg, Object[] args) {
		return formatMessage(msg, args, getDefaultLocale());
	}


	/**
	 * Format the given message String, using cached MessageFormats.
	 * By default invoked for passed-in default messages, to resolve
	 * any argument placeholders found in them.
	 * @param msg the message to format
	 * @param args array of arguments that will be filled in for params within
	 * the message, or <code>null</code> if none
	 * @param locale the Locale used for formatting
	 * @return the formatted message (with resolved arguments)
	 */
	protected String formatMessage(String msg, Object[] args, Locale locale) {
		if (msg == null || (args == null || args.length == 0)) {
			return msg;
		}
		MessageFormat messageFormat;
		synchronized (this.cachedMessageFormats) {
			messageFormat = this.cachedMessageFormats.get(msg);
			if (messageFormat == null) {
				try {
					messageFormat = new MessageFormat((msg != null ? msg : ""), locale);
				}
				catch (IllegalArgumentException ex) {
					// invalid message format - probably not intended for formatting,
					// rather using a message structure with no arguments involved
					// silently proceed with raw message
					messageFormat = INVALID_MESSAGE_FORMAT;
				}
				this.cachedMessageFormats.put(msg, messageFormat);
			}
		}
		if (messageFormat == INVALID_MESSAGE_FORMAT) {
			return msg;
		}
		synchronized (messageFormat) {
			return messageFormat.format(args);
		}
	}




	/**
	 * Try to resolve the message. Return default message if no message was found.
	 * @param code the code to lookup up, such as 'calculator.noRateSet'. Users of
	 * this class are encouraged to base message names on the relevant fully
	 * qualified class name, thus avoiding conflict and ensuring maximum clarity.
	 * @param defaultMessage String to return if the lookup fails
	 * @return the resolved message if the lookup was successful;
	 * otherwise the default message passed as a parameter
	 */
	protected String getMessage(String code, String defaultMessage) {
		final Properties messages = new Properties("i18n.messages");
		try {
			return messages.getValueKey(code);
		} catch (MissingResourceException e) { }
		return defaultMessage;
	}

	/**
	 * Try to resolve the message. Return <tt>null</tt> if no message was found.
	 * @param code the code to lookup up, such as 'calculator.noRateSet'. Users of
	 * this class are encouraged to base message names on the relevant fully
	 * qualified class name, thus avoiding conflict and ensuring maximum clarity.
	 * @return the resolved message if the lookup was successful;
	 * otherwise <tt>null</tt>.
	 */
	protected final String getMessage(String code) {
		return getMessage(code, (String) null);
	}

	/**
	 * Try to resolve the message. Return default message if no message was found.
	 * @param code the code to lookup up, such as 'calculator.noRateSet'. Users of
	 * this class are encouraged to base message names on the relevant fully
	 * qualified class name, thus avoiding conflict and ensuring maximum clarity.
	 * @param args array of arguments that will be filled in for params within
	 * the message (params look like "{0}", "{1,date}", "{2,time}" within a message),
	 * or <code>null</code> if none.
	 * @param defaultMessage String to return if the lookup fails
	 * @return the resolved message if the lookup was successful;
	 * otherwise the default message passed as a parameter
	 * @see java.text.MessageFormat
	 */
	protected final String getMessage(String code, Object[] args, String defaultMessage) {
		String source = getMessage(code, defaultMessage);
		return formatMessage(source, args);
	}

	/**
	 * Try to resolve the message. Treat as an error if the message can't be found.
	 * @param code the code to lookup up, such as 'calculator.noRateSet'
	 * @param args Array of arguments that will be filled in for params within
	 * the message (params look like "{0}", "{1,date}", "{2,time}" within a message),
	 * or <code>null</code> if none.
	 * @return the resolved message
	 * @see java.text.MessageFormat
	 */
	protected final String getMessage(String code, Object[] args) {
		String source = getMessage(code);
		return formatMessage(source, args);
	}




}
