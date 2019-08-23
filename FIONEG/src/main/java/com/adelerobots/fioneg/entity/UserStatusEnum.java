package com.adelerobots.fioneg.entity;

import java.util.EnumSet;

/**
 * Enumerated values that defines the user status
 * @author adele 
 */
public enum UserStatusEnum 
{
	/** The user is current logged-out */
	OFFLINE("offline"), 
	/** The user is current logged-in */
	ONLINE("online"), 
	/** The user account is not activated yet (need admin confirmation). */
	UNCONFIRMED("unconfirmed"),
	/** The user email is now confirmed */
	EMAIL_CONFIRMED("email_confirmed"),
	/** The user account has been blocked due to, for example, bad use of application */ 
	ACCOUNT_BLOCKED("blocked"), 
	/** The user needs to change the password */
	CREDENTIALS_EXPIRED("changepass"), 
	/** The user account has been expired, due to, for example, a test period of application */
	ACCOUNT_EXPIRED("expired"), 
	/** The user account has been disabled, due to, for example, innactivity */
	ACCOUNT_DISABLED("disabled"),
	/** The user account has been activated from TCRF with paid services (monthly, yearly) */
	TCRF("tcrf"),
	/** The user account has been activated from TCRF with free services (free) */
	TCRF_FREE("tcrf_free")
	;
	
	
	public String text;
	private UserStatusEnum(final String text) {
		this.text = text;
	}
	
	
	public static UserStatusEnum getValue(final String text) {
		if (text == null || "".equals(text)) {
			//Default value is unconfirmed
			return UNCONFIRMED;
		}
		//Search value with this text
		for (UserStatusEnum value : EnumSet.allOf(UserStatusEnum.class) ) {
			if (text.equals(value.text)) {
				return value;
			}
		}
		throw new IllegalArgumentException("No enum const " + UserStatusEnum.class +"@[text=" + text + "]");
	}
}