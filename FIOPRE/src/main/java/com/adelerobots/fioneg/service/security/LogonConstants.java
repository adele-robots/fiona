package com.adelerobots.fioneg.service.security;

/**
 * Logon constans definition
 * 
 * @author adele
 * 
 * TODO: Generar un Jar desde negocio con las partes comunes en la que se incluya esta clase
 */
public interface LogonConstants 
{

	/**
	 * Logon password for users with X509 Certificate
	 */
	public String X509CERTIFICATE_LOGON_PASSWORD = "HFm6pnF@@*UJ9aYS5h2w$Hc!t x9QKUB?$P97VkWTBvpSf%7C";

	/**
	 * Logon error codes.
	 * 
	 * @author adele
	 */
	public enum LogonErrorCode 
	{
		/** 525 ERROR_NO_SUCH_USER (The specified account does not exist.)
		 *  <p>NOTE: Returns when username is invalid. */
		ERR525__NO_SUCH_USER(525, "The specified account does not exist."), 
		
		/** 52e6 ERROR_LOGON_FAILURE (Logon failure: unknown user name or bad password.)
		 *  <p>NOTE: Returns when username is valid but password/credential is invalid.
		 *  <p>Will prevent most other errors from being displayed as noted. */
		ERR52e6__LOGON_FAILURE(526, "Unknown user name or bad password.", 
				"The provided password/credentials are invalid for the specified account."), 
		
		/** 52e7 ERROR_LOGON_NEED_ACTIVATION (Logon failure: known username/password but account need activation by administrator.) 
		 *  <p>NOTE: Returns only when presented with valid username and password/credential. */
		ERR52e7__LOGON_NEED_ACTIVATION(527, "User account not activated yet.", 
				"Your user account first needs a confirmation by the platform manager team."), 
				
		/** 530 ERROR_INVALID_LOGON_HOURS (Logon failure: account logon time restriction violation.)
		 *  <p>NOTE: Returns only when presented with valid username and password/credential. */
		ERR530__INVALID_LOGON_HOURS(530, "Account logon time restriction violation.", 
				"According to your timezone may not be able to use the platform or it is possible we are under maintenance."), 
		
		/** 531 ERROR_INVALID_WORKSTATION (Logon failure: user not allowed to log on to this computer.)
		 *  <p>LDAP[userWorkstations: <multivalued list of workstation names>]
		 *  <p>NOTE: Returns only when presented with valid username and password/credential. */
		ERR531__INVALID_WORKSTATION(531, "User not allowed to log on to this computer.", 
				"The use of platform is restricted to your workstation or internet address (IP)."), 
		
		/** 532 ERROR_PASSWORD_EXPIRED (Logon failure: the specified account password has expired.
		 *  <p>LDAP[userAccountControl: <bitmask=0x00800000>] - PASSWORDEXPIRED
		 *  <p>NOTE: Returns only when presented with valid username and password/credential. */
		ERR532__PASSWORD_EXPIRED(532, "The specified account password has expired.", 
				"It is necessary to establish a new password given the fact that you have not changed it long time ago."), 
		
		/** 533 ERROR_ACCOUNT_DISABLED (Logon failure: account currently disabled.)
		 *  <p>LDAP[userAccountControl: <bitmask=0x00000002>] - ACCOUNTDISABLE
		 *  <p>NOTE: Returns only when presented with valid username and password/credential */
		ERR533__ACCOUNT_DISABLED(533, "Account currently disabled.", 
				"Due to your inactivity period is possible that your user account has been disabled."), 
		
		/** 701 ERROR_ACCOUNT_EXPIRED (The user's account has expired.)
		 *  <p>LDAP[accountExpires: <value of -1, 0, or extemely large value indicates account will not expire>] - ACCOUNTEXPIRED
		 *  <p>NOTE: Returns only when presented with valid username and password/credential. */
		ERR701__ACCOUNT_EXPIRED(701, "Account has expired.", 
				"It is possible that your trial period of the platform has ended."), 
		
		/** 773 ERROR_PASSWORD_MUST_CHANGE (The user's password must be changed before logging on the first time.)
		 *  <p>LDAP[pwdLastSet: <value of 0 indicates admin-required password change>] - MUST_CHANGE_PASSWD
		 *  <p>NOTE: Returns only when presented with valid username and password/credential. */
		ERR773__PASSWORD_MUST_CHANGE(773, "First credentials must be changed.",
				"The user's password must be changed before logging on the first time."), 
		
		/** 775 ERROR_ACCOUNT_LOCKED_OUT (The referenced account is currently locked out and may not be logged on to.)
		 *  <p>LDAP[userAccountControl: <bitmask=0x00000010>] - LOCKOUT
		 *  <p>NOTE: Returns even if invalid password is presented */
		ERR775__ACCOUNT_LOCKED(775, "Account currently locked.", 
				"The referenced account is currently locked out and may not be logged on to because a platform bad use."); 
		
		public final int code;
		public final String msg;
		public final String desc;

		private LogonErrorCode(int code, String msg, String desc) {
			this.code = code;
			this.msg = msg;
			this.desc = desc;
		}
		private LogonErrorCode(int code, String msg) {
			this.code = code;
			this.msg = msg;
			this.desc = msg;
		}
	}


}
