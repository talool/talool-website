package com.talool.website.util;

import org.apache.wicket.Session;

import com.talool.website.TaloolSession;

/**
 * Session Convenience
 * 
 * @author clintz
 * 
 */
public final class SessionUtils
{
	public static TaloolSession getSession()
	{
		return (TaloolSession) Session.get();
	}

}
