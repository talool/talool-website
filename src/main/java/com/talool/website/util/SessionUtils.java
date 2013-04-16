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
	public static void infoMessage(final String... parts)
	{
		getSession().info(sessionMessage(parts));
	}

	public static void errorMessage(final String... parts)
	{
		getSession().error(sessionMessage(parts));
	}

	public static void warningMessage(final String... parts)
	{
		getSession().warn(sessionMessage(parts));
	}

	public static void fatalMessage(final String... parts)
	{
		getSession().fatal(sessionMessage(parts));
	}

	public static void successMessage(final String... parts)
	{
		getSession().success(sessionMessage(parts));
	}

	public static TaloolSession getSession()
	{
		return (TaloolSession) Session.get();
	}

	private static String sessionMessage(final String... parts)
	{
		if (parts != null && parts.length == 1)
		{
			return parts[0];
		}
		final StringBuilder sb = new StringBuilder();
		for (String part : parts)
		{
			sb.append(part);
		}

		return sb.toString();
	}

}
