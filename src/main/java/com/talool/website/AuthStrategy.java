package com.talool.website;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.strategies.page.AbstractPageAuthorizationStrategy;

import com.talool.website.pages.AdminLoginPage;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author clintz
 * 
 */
public class AuthStrategy extends AbstractPageAuthorizationStrategy implements
		IUnauthorizedComponentInstantiationListener
{

	@Override
	protected <T extends Page> boolean isPageAuthorized(Class<T> pageClass)
	{
		if (pageClass.getAnnotation(SecuredPage.class) != null)
		{
			return SessionUtils.getSession().isSignedOn();
		}
		else
		{
			return true;
		}
	}

	@Override
	public void onUnauthorizedInstantiation(final Component component)
	{
		if (component instanceof Page)
		{
			throw new RestartResponseAtInterceptPageException(AdminLoginPage.class);
		}
	}

}
