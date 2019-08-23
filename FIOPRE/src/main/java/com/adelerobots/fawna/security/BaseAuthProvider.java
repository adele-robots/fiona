package com.adelerobots.fawna.security;

import java.math.BigDecimal;
import java.security.cert.X509Certificate;
import java.util.StringTokenizer;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import com.adelerobots.fioneg.service.security.LogonConstants;
import com.treelogic.fawna.arq.negocio.conector_rmi.ErrorOperacionException;
import com.treelogic.fawna.arq.negocio.core.IContexto;
import com.treelogic.fawna.presentacion.core.exception.FawnaInvokerException;
import com.treelogic.fawna.presentacion.core.exception.PersistenciaException;
import com.treelogic.fawna.presentacion.core.invoker.FawnaInvoker;
import com.treelogic.fawna.presentacion.core.persistencia.ContextoOperacion;
import com.treelogic.fawna.presentacion.core.security.exceptions.ServiceDataAccessException;

/**
 * Proveedor de autenticacion. 
 * Validación de credenciales despues de obtener la informacion de usuario
 * 
 * @author alfonso fernandez
 * @see BaseUserDetailsServiceImpl
 */
public class BaseAuthProvider 
	extends 		DaoAuthenticationProvider 
	implements 	LogonConstants
{

	protected final Log logger = LogFactory.getLog(getClass());

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.authentication.dao.DaoAuthenticationProvider#additionalAuthenticationChecks(org.springframework.security.core.userdetails.UserDetails, org.springframework.security.authentication.UsernamePasswordAuthenticationToken)
	 */
	@Override
	protected void additionalAuthenticationChecks(
			final UserDetails userDetails, 
			final UsernamePasswordAuthenticationToken token) 
		throws AuthenticationException 
	{
		/* Segun la documentacion de Spring Security UserDetailsService: 
		 *  - si no se encuentra el usuario o no tiene roles hay que lanzar una UsernameNotFoundException.
		 *  - si no se puede determinar dichos detalles por error de operacion una DataAccessException. 
		 *  	En nuestro caso cuando hay mas de un registro (subtipo IncorrectResultSizeDataAccessException)
		 */

    	String logon = null;
		String passwd  = null;
    	if(hasCert(token)) {
    		X509Certificate certificate = (X509Certificate) token.getPrincipal();
    		//logon = certificate.getSubjectDN().getName();
        	StringTokenizer st = new StringTokenizer(certificate.getSubjectDN().getName(), ",");
            while (st.hasMoreTokens()) {
            	String tmp = st.nextToken();
                if(tmp.startsWith("EMAILADDRESS=")) {
                	logon = tmp.substring(13);
                	break;
                }
            }
    		passwd = X509CERTIFICATE_LOGON_PASSWORD;
    	}
    	else {
    		logon = token.getName();
			passwd  = (String) token.getCredentials();
    	}

		IContexto[] contextos = null;
		try {
			ContextoOperacion contextoOperacion = new ContextoOperacion();
			contextoOperacion.putDato("0", logon, ContextoOperacion.Cadena);
			contextoOperacion.putDato("1", passwd, ContextoOperacion.Cadena);
			// validacion de credenciales FIONEG
			contextos = FawnaInvoker.getInstance().invoke("029", "998", contextoOperacion);
		} 
		catch (FawnaInvokerException e) {
			final Throwable cause = e.getCause();
			final Throwable rootCause = ExceptionUtils.getRootCause(e);
			// error funcional al obtener el detalle. Unwrap excepcion original de negocio para saber la causa
			if (com.treelogic.fawna.presentacion.core.exception.ExceptionUtils.ARCH_TYPE_ERROR_EXCP_FUNC.equals(e.getErrorInfo().getTipo())
					&& rootCause instanceof ErrorOperacionException) {
				final ErrorOperacionException ex = (ErrorOperacionException) rootCause;
				
				//Tratar codigos de error del logon y pasarlos a Spring Security exceptions
				int code = 0; {
					Integer exCode = ex.getCodigo();
					if (exCode!=null) code = exCode.intValue();
				}
				if (LogonErrorCode.ERR525__NO_SUCH_USER.code == code) {
					if(hasCert(token)) {
						registerUser(logon, passwd);
						additionalAuthenticationChecks(userDetails, token);
						return;
					}
					throw new UsernameNotFoundException(LogonErrorCode.ERR525__NO_SUCH_USER.msg, token);
				} else if (LogonErrorCode.ERR52e6__LOGON_FAILURE.code == code) {
					throw new BadCredentialsException(LogonErrorCode.ERR52e6__LOGON_FAILURE.msg, token);
				} else if (LogonErrorCode.ERR52e7__LOGON_NEED_ACTIVATION.code == code) {
					throw new AccountNeedActivationAuthenticationException(LogonErrorCode.ERR52e7__LOGON_NEED_ACTIVATION.msg, token);
				} else if (LogonErrorCode.ERR530__INVALID_LOGON_HOURS.code == code) {
					throw new InvalidLogonHoursAuthenticationException(LogonErrorCode.ERR530__INVALID_LOGON_HOURS.msg, token);
				} else if (LogonErrorCode.ERR531__INVALID_WORKSTATION.code == code) {
					throw new InvalidWorkstationAuthenticationException(LogonErrorCode.ERR531__INVALID_WORKSTATION.msg, token);
				} else if (LogonErrorCode.ERR532__PASSWORD_EXPIRED.code == code) {
					throw new CredentialsExpiredException(LogonErrorCode.ERR532__PASSWORD_EXPIRED.msg, token);
				} else if (LogonErrorCode.ERR533__ACCOUNT_DISABLED.code == code) {
					throw new DisabledException(LogonErrorCode.ERR533__ACCOUNT_DISABLED.msg, token);
				} else if (LogonErrorCode.ERR701__ACCOUNT_EXPIRED.code == code) {
					throw new AccountExpiredException(LogonErrorCode.ERR701__ACCOUNT_EXPIRED.msg, token);
				} else if (LogonErrorCode.ERR773__PASSWORD_MUST_CHANGE.code == code) {
					throw new CredentialsExpiredException(LogonErrorCode.ERR773__PASSWORD_MUST_CHANGE.msg, token);
				} else if (LogonErrorCode.ERR775__ACCOUNT_LOCKED.code == code) {
					throw new LockedException(LogonErrorCode.ERR775__ACCOUNT_LOCKED.msg, token);
				}
				throw new AuthenticationServiceException(e.getMessage(), e);
			}
			throw new ServiceDataAccessException("029", "998", "Validating user logon.", e);
		} catch(PersistenciaException e) {
			throw new ServiceDataAccessException("029", "998", "Validating user logon.", e);
		}
		//Si no se retorna nada entonces es que no existe un usuario con ese UUID
		if (contextos==null || contextos.length == 0) {
			throw new UsernameNotFoundException(LogonErrorCode.ERR525__NO_SUCH_USER.msg, token);
		} else if (contextos.length != 1 ) {
			throw new IncorrectResultSizeDataAccessException(1, contextos.length);
		}
		

	}

    private boolean hasCert(Authentication authentication) {
        return authentication.getPrincipal() instanceof X509Certificate;
    }
    
    private IContexto[] registerUser(String email, String password) {
		IContexto[] contextos = null;
		try {
			ContextoOperacion contextoOperacion = new ContextoOperacion();
			contextoOperacion.putDato("0", email.substring(0, email.indexOf('@')), ContextoOperacion.Cadena); // Nombre
			contextoOperacion.putDato("2", email, ContextoOperacion.Cadena); // mail
			contextoOperacion.putDato("3", password, ContextoOperacion.Cadena); // pass
			contextoOperacion.putDato("5", new BigDecimal(1), ContextoOperacion.Numero); // Tipo cuenta (free)
			contextoOperacion.putDato("8", email.replace('@', '.'), ContextoOperacion.Cadena); // username
			contextoOperacion.putDato("9", new BigDecimal(2), ContextoOperacion.Numero); // rol (user)
			contextoOperacion.putDato("10", "Mr", ContextoOperacion.Cadena); // title
			contextoOperacion.putDato("11", "0", ContextoOperacion.Cadena); // flag entity (no)
			contextoOperacion.putDato("20", "1", ContextoOperacion.Cadena); // flag certificate (si)
			// validacion de credenciales FIONEG
			contextos = FawnaInvoker.getInstance().invoke("029", "001", contextoOperacion);
		} 
		catch (FawnaInvokerException e) {
			e.printStackTrace();
		} catch (PersistenciaException e) {
			e.printStackTrace();
		}
		return contextos;
    }
    
    private void confirmEmail(BigDecimal id, String code) {
		try {
			ContextoOperacion contextoOperacion = new ContextoOperacion();
			contextoOperacion.putDato("0", id, ContextoOperacion.Numero); // Id
			contextoOperacion.putDato("1", code, ContextoOperacion.Cadena); // code
			FawnaInvoker.getInstance().invoke("029", "039", contextoOperacion);
		} 
		catch (FawnaInvokerException e) {
			e.printStackTrace();
		} catch (PersistenciaException e) {
			e.printStackTrace();
		}
    }

    @Override
    public Authentication authenticate(Authentication authentication)
        throws AuthenticationException
    {
        Assert.isInstanceOf(org.springframework.security.authentication.UsernamePasswordAuthenticationToken.class, authentication, messages.getMessage("AbstractUserDetailsAuthenticationProvider.onlySupports", "Only UsernamePasswordAuthenticationToken is supported"));
        String username = authentication.getPrincipal() != null ? authentication.getName() : "NONE_PROVIDED";

        if(hasCert(authentication)) {
        	X509Certificate certificate = (X509Certificate) authentication.getPrincipal();
        	//username = certificate.getSubjectDN().getName();
        	StringTokenizer st = new StringTokenizer(certificate.getSubjectDN().getName(), ",");
            while (st.hasMoreTokens()) {
            	String tmp = st.nextToken();
                if(tmp.startsWith("EMAILADDRESS=")) {
                	username = tmp.substring(13);
                	break;
                }
            }
        }
		
        boolean cacheWasUsed = true;
        UserDetails user = getUserCache().getUserFromCache(username);
        if(user == null)
        {
            cacheWasUsed = false;
            try
            {
                user = retrieveUser(username, (UsernamePasswordAuthenticationToken)authentication);
            }
            catch(UsernameNotFoundException notFound)
            {
				if(hasCert(authentication)) {
					IContexto context [] = registerUser(username, X509CERTIFICATE_LOGON_PASSWORD);
					if(context == null) {
						throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
					}
					BigDecimal id = context[0].getBigDecimal("FIONEG001010");
					String signupCode = context[0].getString("FIONEG001110");
					confirmEmail(id, signupCode);
	                user = retrieveUser(username, (UsernamePasswordAuthenticationToken)authentication);
				}
				else {
					if(hideUserNotFoundExceptions)
						throw new BadCredentialsException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
					else
						throw notFound;
				}
            }
            Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
        }
        try
        {
        	getPreAuthenticationChecks().check(user);
            additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken)authentication);
        }
        catch(AuthenticationException exception)
        {
            if(cacheWasUsed)
            {
                cacheWasUsed = false;
                user = retrieveUser(username, (UsernamePasswordAuthenticationToken)authentication);
                getPreAuthenticationChecks().check(user);
                additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken)authentication);
            } else
            {
                throw exception;
            }
        }
        getPostAuthenticationChecks().check(user);
        if(!cacheWasUsed)
        	getUserCache().putUserInCache(user);
        Object principalToReturn = user;
        if(isForcePrincipalAsString())
            principalToReturn = user.getUsername();
        return createSuccessAuthentication(principalToReturn, authentication, user);
    }
    

}
