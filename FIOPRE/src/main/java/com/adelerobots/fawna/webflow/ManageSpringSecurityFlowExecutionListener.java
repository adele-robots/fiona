package com.adelerobots.fawna.webflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.core.collection.SharedAttributeMap;
import org.springframework.webflow.execution.FlowExecutionListener;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.RequestContext;

/**
 * {@link FlowExecutionListener} to manage the variables handled
 * by the Spring Security authentication process,
 * like the happened errors, authenticated user, etc
 * <p>
 * This variables will be putted in application contexts and so, 
 * available inside the flows, to be used for example example, 
 * render some message in the views.
 * 
 * @author adele
 * 
 * @see #ARCH_SECURITY_LAST_EXCEPTION_KEY
 * @see #ARCH_SECURITY_LAST_USERNAME_KEY
 */
public class ManageSpringSecurityFlowExecutionListener 
		extends FlowExecutionListenerAdapter
{

	protected final Log logger = LogFactory.getLog(getClass());
	
	/** Key under the last authentication exception is stored */
	public static final String ARCH_SECURITY_LAST_EXCEPTION_KEY = "ARCH_SECURITY_LAST_EXCEPTION";
	/** Key under the last authentication username is stored */
	public static final String ARCH_SECURITY_LAST_USERNAME_KEY = "ARCH_SECURITY_LAST_USERNAME";
	
	
	/* (non-Javadoc)
	 * @see org.springframework.webflow.execution.FlowExecutionListenerAdapter#sessionStarting(org.springframework.webflow.execution.RequestContext, org.springframework.webflow.execution.FlowSession, org.springframework.webflow.core.collection.MutableAttributeMap)
	 */
	@Override
	public void sessionStarting(
			final RequestContext context, 
			final FlowSession session,
			final MutableAttributeMap input) 
	{
		/*final Exception secEx = */ handleSpringSecurityLastException(context, session, input);
		/*final Object secUname = */ handleSpringSecurityLastUsername(context, session, input);
		
		super.sessionStarting(context, session, input);
	}


	/**
	 * 
	 * @param context the current flow request context
	 * @param session the session that was created
	 * @param input a mutable input map - attributes placed in this map are eligible for input mapping by the flow
	 *             definition at startup
	 */
	protected Exception handleSpringSecurityLastException(
			final RequestContext context, 
			final FlowSession session,
			final MutableAttributeMap input) 
	{
		Exception ex = null;
		final ExternalContext externalContext = context.getExternalContext();
		final SharedAttributeMap sessionMap = externalContext.getSessionMap();
		
		//Recover Spring Security authentication exception
		boolean hasSecEx = sessionMap.contains(
				AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY, Exception.class);
		if (hasSecEx) {
			ex = (Exception) sessionMap.get(
					AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY, Exception.class);
			input.put(ARCH_SECURITY_LAST_EXCEPTION_KEY, ex);
			sessionMap.put(ARCH_SECURITY_LAST_EXCEPTION_KEY, ex);
		} else {
			sessionMap.remove(ARCH_SECURITY_LAST_EXCEPTION_KEY);
		}
		return ex;
	}


	/**
	 * 
	 * @param context the current flow request context
	 * @param session the session that was created
	 * @param input a mutable input map - attributes placed in this map are eligible for input mapping by the flow
	 *             definition at startup
	 */
	protected String handleSpringSecurityLastUsername(
			final RequestContext context, 
			final FlowSession session,
			final MutableAttributeMap input) 
	{
		String uname = null;
		final ExternalContext externalContext = context.getExternalContext();
		final SharedAttributeMap sessionMap = externalContext.getSessionMap();
		
		//Recover Spring Security authentication user name
		boolean hasSecUname = sessionMap.contains(
				UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY, String.class);
		if (hasSecUname) {
			uname = (String) sessionMap.get(
					UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY, String.class);
			input.put(ARCH_SECURITY_LAST_USERNAME_KEY, uname);
			sessionMap.put(ARCH_SECURITY_LAST_USERNAME_KEY, uname);
		} else {
			sessionMap.remove(ARCH_SECURITY_LAST_USERNAME_KEY);
		}
		return uname;
	}

}
