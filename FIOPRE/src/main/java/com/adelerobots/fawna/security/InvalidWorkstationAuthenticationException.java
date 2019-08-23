package com.adelerobots.fawna.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception to represent credentials that have been rejerted 
 * due to according not allowed user workstation or internet address (IP).
 * 
 * @author adele
 */
public class InvalidWorkstationAuthenticationException 
	extends AuthenticationException 
{
	private static final long serialVersionUID = -2706258030970503439L;

    //~ Constructors ===================================================================================================

	/**
     * Constructs a <code>InvalidLogonHoursAuthenticationException</code> with the specified message.
     *
     * @param msg the detail message
     */
    public InvalidWorkstationAuthenticationException(String msg) {
        super(msg);
    }

    /**
     * Constructs a <code>InvalidLogonHoursAuthenticationException</code> with the specified message
     * and root cause.
     *
     * @param msg the detail message
     * @param t root cause
     */
    public InvalidWorkstationAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Constructs a <code>InvalidLogonHoursAuthenticationException</code> with the specified message
     * and extra information about object that causes the exception.
     * @param msg the detail message
     * @param extraInformation
     * @deprecated Use the exception message or use a custom exception if you really need additional information.
     */
    @Deprecated
    public InvalidWorkstationAuthenticationException(String msg, Object extraInformation) {
        super(msg, extraInformation);
    }
}
