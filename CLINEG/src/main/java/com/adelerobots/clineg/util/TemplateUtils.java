package com.adelerobots.clineg.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import com.adelerobots.clineg.util.keys.Constantes;
import com.treelogic.fawna.arq.negocio.core.IContextoEjecucion;
import com.treelogic.fawna.arq.negocio.log.FawnaLogHelper;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;

/**
 * Utils to manage freemarker mail templates
 * 
 * @author adele
 *
 */
public abstract class TemplateUtils 
{

	protected final static FawnaLogHelper logger = FawnaLogHelper.getLog("com.adelerobots.fawna.templates");
	
	
	/**
	 * Get a Freemarker configuration already prepared for 
	 * that Fawna service execution context
	 * @param ctx the service execution context
	 * @return
	 * @throws freemarker.template.TemplateException if configuring failed
	 */
	public static Configuration configure(
			final IContextoEjecucion ctx) 
		throws TemplateException
	{
		final Locale locale = (Locale) FunctionUtils.defaultIfNull(ctx.getLocale(), 
				FunctionUtils.defaultIfNull(FunctionUtils.toLocale(ctx.getIdioma()), Locale.getDefault()));
		final Configuration cfg = new Configuration();
		//Prepare the configuration to resolve template with locales. Tipically like ResourceBundle behaviour
		cfg.setLocale(locale);
		cfg.setLocalizedLookup(true);
		
		cfg.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
		
		//Define the template load strategy
		TemplateLoader[] tpls = new TemplateLoader[] { };
		try { //Try first external loader
			tpls = (TemplateLoader[]) ArrayUtils.add(tpls, 
					new FileTemplateLoader(Constantes.getTemplatesFolder()));
		} catch (Exception e) {
			if (logger.isWarnEnabled()) {
				logger.warn("The external template system could not be loaded. " + e.getMessage(), e);
			}
		}
		//Default internal loader in this jar
		tpls = (TemplateLoader[]) ArrayUtils.add(tpls, 
				new ClassTemplateLoader(TemplateUtils.class, "/templates/"));
		cfg.setTemplateLoader(new MultiTemplateLoader(tpls));
		
		return cfg;
	}

	/**
	 * Compose the template name using the provided parameters
	 * @param name the name of mail template
	 * @return
	 */
	public static String generateTemplateName(final String name) {
		return name + ".ftl";
	}

	/**
	 * Load the template using the provided parameters
	 * @param ctx the service execution context
	 * @param name the name of mail template
	 * @return
	 * @throws IOException if the template wasn't found or couldn't be read
	 * @throws freemarker.template.TemplateException if configuring template failed
	 */
	public static Template loadTemplate(
			final IContextoEjecucion ctx,
			final String name) 
	throws IOException, TemplateException 
	{
		return configure(ctx).getTemplate(generateTemplateName(name));
	}

	/**
	 * Process the specified FreeMarker template with the given model and write
	 * the result to the given Writer.
	 * @param model the model object, typically a Map that contains model names
	 *               as keys and model objects as values
	 * @return the result as String
	 * @throws IOException if the template wasn't found or couldn't be read
	 * @throws freemarker.template.TemplateException if rendering failed
	 */
	public static String processTemplateIntoString(
			final Template template, final Map<String, Object> model)
		throws IOException, TemplateException 
	{
		BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		TemplateHashModel staticModels = wrapper.getStaticModels();
		
		final SimpleHash root = new SimpleHash(model);
		//Add templateModels for certain utility classes
		root.put("FunctionUtils", staticModels.get(FunctionUtils.class.getName()));
		root.put("ConfigUtils", staticModels.get(Constantes.class.getName()));
		
		final StringWriter result = new StringWriter();
		template.process(root, result);
		return result.toString();
	}

	/**
	 * Process the specified FreeMarker template with the given model and write
	 * the result to the given Writer.
	 * @param ctx the service execution context
	 * @param name the name of mail template
	 * @param model the model object, typically a Map that contains model names
	 *               as keys and model objects as values
	 * @return the result as String
	 * @throws IOException if the template wasn't found or couldn't be read
	 * @throws freemarker.template.TemplateException if rendering failed
	 */
	public static String processTemplateIntoString(
			final IContextoEjecucion ctx,
			final String name, final Map<String, Object> model)
		throws IOException, TemplateException 
	{
		final Template template = loadTemplate(ctx, name);
		return processTemplateIntoString(template, model);
	}

}
