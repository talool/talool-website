package com.talool.website;

import java.io.Serializable;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.talool.website.pages.AdminLoginPage;
import com.talool.website.pages.HomePage;
import com.talool.website.pages.lists.CustomersPage;
import com.talool.website.pages.lists.DealOffersPage;
import com.talool.website.pages.lists.MerchantsPage;

/**
 * @author clintz
 * 
 */
public class TaloolApplication extends WebApplication implements Serializable
{
	private final String mode = "development";
	private static final long serialVersionUID = 1954532829422211028L;

	@Override
	public Session newSession(Request request, Response response)
	{
		return new TaloolSession(request);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getHomePage()
	{
		return HomePage.class;
	}

	public ApplicationContext getApplicationContext()
	{
		return WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	}

	@Override
	protected void init()
	{
		final IPackageResourceGuard packageResourceGuard = getResourceSettings()
				.getPackageResourceGuard();
		if (packageResourceGuard instanceof SecurePackageResourceGuard)
		{
			final SecurePackageResourceGuard guard = (SecurePackageResourceGuard) packageResourceGuard;
			guard.addPattern("+*.*");
		}

		getComponentInstantiationListeners().add(
				new SpringComponentInjector(this, getApplicationContext(), false));

		final ResourceReference faviconRef = new PackageResourceReference(this.getClass(),
				"favicon.ico");
		mountResource("/favicon.ico", faviconRef);

		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");

		mountPage("/admin", AdminLoginPage.class);
		mountPage("/admin/books", DealOffersPage.class);
		mountPage("/admin/customers", CustomersPage.class);
		mountPage("/admin/merchants", MerchantsPage.class);

		/*
		 * We need a ONE_PASS_RENDER strategy because pages like search need
		 * parameters to stay in the query string. Keep in mind the default strategy
		 * solves the double submit problem
		 */
		// getRequestCycleSettings().setRenderStrategy(IRequestCycleSettings.ONE_PASS_RENDER);
		// getSessionSettings().setPageMapEvictionStrategy(new
		// LeastRecentlyAccessedEvictionStrategy(1));
		getPageSettings().setVersionPagesByDefault(false);
		// getRequestCycleSettings().setRenderStrategy(RenderStrategy.ONE_PASS_RENDER);
		// getExceptionSettings().setUnexpectedExceptionDisplay(
		// IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
		// getApplicationSettings().setPageExpiredErrorPage(getHomePage());

		// getApplicationSettings().setInternalErrorPage(ErrorPage.class);

		final AuthStrategy authStrat = new AuthStrategy();
		getSecuritySettings().setAuthorizationStrategy(authStrat);
		getSecuritySettings().setUnauthorizedComponentInstantiationListener(authStrat);

		if (mode.equalsIgnoreCase("deployment"))
		{
			getDebugSettings().setAjaxDebugModeEnabled(false);
			getExceptionSettings().setUnexpectedExceptionDisplay(
					IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
			getMarkupSettings().setStripWicketTags(false);
		}
		else
		{
			getDebugSettings().setAjaxDebugModeEnabled(true);
			getExceptionSettings().setUnexpectedExceptionDisplay(
					IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
			getMarkupSettings().setStripWicketTags(false);
		}
		// else
		// {
		// // love this hack for working locally and changing HTML and have it
		// // reload immediately
		// // because it is outside the WAR
		// final Path path = new Path("src/main/resources");
		//
		// List<Path> resourceFinders = new ArrayList<Path>();
		// resourceFinders.add(path);
		//
		// IResourceFinder finder = new IResourceFinder()
		// {
		//
		// @Override
		// public IResourceStream find(Class<?> arg0, String arg1)
		// {
		// File f = new File("src/main/resources");
		// if (f.exists())
		// {
		// return new FileResourceStream(f);
		// }
		// return null;
		// }
		// };
		//
		// final List<IResourceFinder> finders = new ArrayList<IResourceFinder>();
		// finders.add(finder);
		// getResourceSettings().setResourceFinders(finders);
		//
		// getMarkupSettings().setStripWicketTags(false);
		// }
	}
}
