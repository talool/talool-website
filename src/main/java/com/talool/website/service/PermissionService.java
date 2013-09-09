package com.talool.website.service;

/**
 * Fake permission service . To be replaced by a fully backed permission
 * 
 * @author clintz
 * 
 */
public final class PermissionService
{
	private static final PermissionService instance = new PermissionService();

	public static PermissionService get()
	{
		return instance;
	}

	/**
	 * Hard coded permission for now. Only allow internal Talool users delete
	 * permission.
	 * 
	 * TODO Replace with real persistent permissions
	 * 
	 * @param signedInEmail
	 * @return
	 */
	public boolean canDeleteCustomer(final String signedInEmail)
	{
		if (signedInEmail.equals("chris@talool.com") || signedInEmail.equals("doug@talool.com")
				|| signedInEmail.equals("cory@talool.com"))
		{
			return true;
		}

		return false;

	}

	public boolean canViewAnalytics(final String signedInEmail)
	{
		if (signedInEmail.contains("@talool.com"))
		{
			return true;
		}

		return false;

	}
}
