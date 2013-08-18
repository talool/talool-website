package com.talool.website;

import java.io.Serializable;
import java.util.UUID;

import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
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
import org.apache.wicket.util.lang.PackageName;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.talool.website.converter.UUIDConverter;
import com.talool.website.pages.AdminLoginPage;
import com.talool.website.pages.CustomerManagementPage;
import com.talool.website.pages.CustomerSettingsPage;
import com.talool.website.pages.HealthCheckPage;
import com.talool.website.pages.HomePage;
import com.talool.website.pages.MerchantManagementPage;
import com.talool.website.pages.UploadPage;
import com.talool.website.pages.corporate.ConsumerServices;
import com.talool.website.pages.corporate.Feedback;
import com.talool.website.pages.corporate.MerchantServices;
import com.talool.website.pages.corporate.PrivacyPolicy;
import com.talool.website.pages.corporate.PublisherServices;
import com.talool.website.pages.corporate.TermsOfService;
import com.talool.website.pages.dashboard.MerchantDashboard;
import com.talool.website.pages.facebook.OpenGraphDeal;
import com.talool.website.pages.facebook.OpenGraphDealOffer;
import com.talool.website.pages.facebook.OpenGraphGift;
import com.talool.website.pages.facebook.OpenGraphLocation;
import com.talool.website.pages.lists.CustomersPage;
import com.talool.website.pages.lists.DealHistoryPage;
import com.talool.website.pages.lists.DealOffersPage;
import com.talool.website.pages.lists.MerchantAccountsPage;
import com.talool.website.pages.lists.MerchantDealOffersPage;
import com.talool.website.pages.lists.MerchantLocationsPage;
import com.talool.website.pages.lists.MerchantsPage;
import com.talool.website.pages.payment.braintree.VenmoSaveCardPage;
import com.talool.website.pages.payment.braintree.VenmoUseCardPage;
import com.talool.website.panel.image.upload.FileManageResourceReference;
import com.talool.website.panel.image.upload.FileUploadResourceReference;

/**
 * @author clintz
 * 
 */
public class TaloolApplication extends WebApplication implements Serializable
{

	private static final long serialVersionUID = 1954532829422211028L;

	@Override
	protected IConverterLocator newConverterLocator()
	{
		ConverterLocator cloc = (ConverterLocator) super.newConverterLocator();
		cloc.set(UUID.class, UUIDConverter.get());
		return cloc;
	}

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

		mountResource("fileManager", new FileManageResourceReference(Config.get().getUploadDir()));
		mountResource("fileUpload", new FileUploadResourceReference(Config.get().getUploadDir()));
		mountPage("/upload", UploadPage.class);

		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");

		mountPage("/admin", AdminLoginPage.class);
		mountPage("/admin/books", DealOffersPage.class);
		mountPage("/admin/dh", DealHistoryPage.class);
		mountPage("/admin/customers", CustomersPage.class);
		mountPage("/admin/customer/settings", CustomerSettingsPage.class);
		mountPage("/admin/customer-management", CustomerManagementPage.class);
		mountPage("/admin/merchants", MerchantsPage.class);
		mountPage("/admin/merchant/accounts", MerchantAccountsPage.class);
		mountPage("/admin/merchant/books", MerchantDealOffersPage.class);
		mountPage("/admin/merchant/locations", MerchantLocationsPage.class);
		mountPage("/admin/merchant/dashboard", MerchantDashboard.class);
		mountPage("/admin/merchant/mm", MerchantManagementPage.class);
		mountPage("/h", HealthCheckPage.class);

		mountPage("/gift", OpenGraphGift.class);
		mountPage("/deal", OpenGraphDeal.class);
		mountPage("/offer", OpenGraphDealOffer.class);
		mountPage("/location", OpenGraphLocation.class);
		
		mountPage("/privacy", PrivacyPolicy.class);
		mountPage("/termsofservice", TermsOfService.class);
		mountPage("/feedback", Feedback.class);
		mountPage("/services/merchants", MerchantServices.class);
		mountPage("/services/publishers", PublisherServices.class);
		mountPage("/services/consumers", ConsumerServices.class);
		
		mountPage("/mobile/payment/save", VenmoSaveCardPage.class);
		mountPage("/mobile/payment/use", VenmoUseCardPage.class);

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
		getMarkupSettings().setStripWicketTags(true);

		if (Config.get().getWebsiteMode().equalsIgnoreCase("deployment"))
		{
			getDebugSettings().setAjaxDebugModeEnabled(false);
			getExceptionSettings().setUnexpectedExceptionDisplay(
					IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
		}
		else
		{
			getDebugSettings().setAjaxDebugModeEnabled(true);
			getExceptionSettings().setUnexpectedExceptionDisplay(
					IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
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

	private void mount(String string, PackageName forClass)
	{
		// TODO Auto-generated method stub

	}
}
