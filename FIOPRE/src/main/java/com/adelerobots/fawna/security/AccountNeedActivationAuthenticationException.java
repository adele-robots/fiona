package com.adelerobots.fawna.security;

import org.springframework.security.authentication.DisabledException;

/**
 * Exception to represent credentials that have been blocked for a reason such
 * as the account creation process needs an admin confirmation before the
 * user credentials are been activated.
 * 
 * @author adele
 *
 */
public class AccountNeedActivationAuthenticationException 
	extends DisabledException 
{
	private static final long serialVersionUID = -5516674942879588591L;

    //~ Constructors ===================================================================================================

	/**
     * Constructs a <code>AccountNeedActivationAuthenticationException</code> with the specified message.
     *
     * @param msg the detail message
     */
    public AccountNeedActivationAuthenticationException(String msg) {
        super(msg);
    }

    /**
     * Constructs a <code>AccountNeedActivationAuthenticationException</code> with the specified message
     * and root cause.
     *
     * @param msg the detail message
     * @param t root cause
     */
    public AccountNeedActivationAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Constructs a <code>AccountNeedActivationAuthenticationException</code> with the specified message
     * and extra information about object that causes the exception.
     * @param msg the detail message
     * @param extraInformation
     * @deprecated Use the exception message or use a custom exception if you really need additional information.
     */
    @Deprecated
    public AccountNeedActivationAuthenticationException(String msg, Object extraInformation) {
        super(msg, extraInformation);
    }
}
