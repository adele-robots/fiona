package com.adelerobots.fawna.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception to represent credentials that have been rejerted 
 * due to according user timezone may not be able to use the platform 
 * or it is possible we are under maintenance.
 * 
 * @author adele
 */
public class InvalidLogonHoursAuthenticationException 
	extends AuthenticationException 
{
	private static final long serialVersionUID = -5516674942879588591L;

    //~ Constructors ===================================================================================================

	/**
     * Constructs a <code>InvalidLogonHoursAuthenticationException</code> with the specified message.
     *
     * @param msg the detail message
     */
    public InvalidLogonHoursAuthenticationException(String msg) {
        super(msg);
    }

    /**
     * Constructs a <code>InvalidLogonHoursAuthenticationException</code> with the specified message
     * and root cause.
     *
     * @param msg the detail message
     * @param t root cause
     */
    public InvalidLogonHoursAuthenticationException(String msg, Throwable t) {
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
    public InvalidLogonHoursAuthenticationException(String msg, Object extraInformation) {
        super(msg, extraInformation);
    }
}
