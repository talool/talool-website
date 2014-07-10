package com.talool.website;

import java.io.Serializable;
import java.util.UUID;

import org.apache.wicket.ConverterLocator;
import org.apache.wicket.IConverterLocator;
import org.apache.wicket.Session;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.settings.IExceptionSettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.lang.Exceptions;
import org.apache.wicket.util.lang.PackageName;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.talool.website.converter.UUIDConverter;
import com.talool.website.facebook.pages.OpenGraphDeal;
import com.talool.website.facebook.pages.OpenGraphDealOffer;
import com.talool.website.facebook.pages.OpenGraphGift;
import com.talool.website.facebook.pages.OpenGraphLocation;
import com.talool.website.facebook.pages.mobile.MobileOpenGraphDeal;
import com.talool.website.facebook.pages.mobile.MobileOpenGraphDealOffer;
import com.talool.website.facebook.pages.mobile.MobileOpenGraphGift;
import com.talool.website.facebook.pages.mobile.MobileOpenGraphLocation;
import com.talool.website.marketing.pages.Analytics;
import com.talool.website.marketing.pages.ContactPage;
import com.talool.website.marketing.pages.EmailLandingPage;
import com.talool.website.marketing.pages.FAQ;
import com.talool.website.marketing.pages.Fundraising;
import com.talool.website.marketing.pages.HomePage;
import com.talool.website.marketing.pages.MerchantAgreementPage;
import com.talool.website.marketing.pages.PasswordPage;
import com.talool.website.marketing.pages.PasswordResetPage;
import com.talool.website.marketing.pages.PrivacyPolicyPage;
import com.talool.website.marketing.pages.Registration;
import com.talool.website.marketing.pages.TermsOfServicePage;
import com.talool.website.marketing.pages.WhiteLabel;
import com.talool.website.marketing.pages.app.ConsumerServices;
import com.talool.website.marketing.pages.app.Feedback;
import com.talool.website.marketing.pages.app.MerchantServices;
import com.talool.website.marketing.pages.app.PrivacyPolicy;
import com.talool.website.marketing.pages.app.PublisherServices;
import com.talool.website.marketing.pages.app.TermsOfService;
import com.talool.website.marketing.pages.mobile.MobileAnalyticsPage;
import com.talool.website.marketing.pages.mobile.MobileContactPage;
import com.talool.website.marketing.pages.mobile.MobileEmailLandingPage;
import com.talool.website.marketing.pages.mobile.MobileFaqPage;
import com.talool.website.marketing.pages.mobile.MobileFundraisingPage;
import com.talool.website.marketing.pages.mobile.MobileGiftReturnedPage;
import com.talool.website.marketing.pages.mobile.MobileHomePage;
import com.talool.website.marketing.pages.mobile.MobileMerchantAgreementPage;
import com.talool.website.marketing.pages.mobile.MobilePasswordPage;
import com.talool.website.marketing.pages.mobile.MobilePasswordResetPage;
import com.talool.website.marketing.pages.mobile.MobilePrivacyPage;
import com.talool.website.marketing.pages.mobile.MobileRegistrationPage;
import com.talool.website.marketing.pages.mobile.MobileTermsPage;
import com.talool.website.marketing.pages.mobile.MobileWhiteLabelPage;
import com.talool.website.pages.AdminLoginPage;
import com.talool.website.pages.AnalyticsPage;
import com.talool.website.pages.CustomerManagementPage;
import com.talool.website.pages.CustomerSearchPage;
import com.talool.website.pages.CustomerSettingsPage;
import com.talool.website.pages.FundraiserManagementPage;
import com.talool.website.pages.HealthCheckPage;
import com.talool.website.pages.MerchantManagementPage;
import com.talool.website.pages.UploadPage;
import com.talool.website.pages.dashboard.MerchantDashboard;
import com.talool.website.pages.error.AccessDeniedPage;
import com.talool.website.pages.error.BrowserErrorPage;
import com.talool.website.pages.error.InternalErrorPage;
import com.talool.website.pages.error.PageNotFound;
import com.talool.website.pages.lists.CustomersPage;
import com.talool.website.pages.lists.DealHistoryPage;
import com.talool.website.pages.lists.DealOffersPage;
import com.talool.website.pages.lists.FundraisersPage;
import com.talool.website.pages.lists.MerchantAccountsPage;
import com.talool.website.pages.lists.MerchantDealOffersPage;
import com.talool.website.pages.lists.MerchantLocationsPage;
import com.talool.website.pages.lists.MerchantRedemptionCodePage;
import com.talool.website.pages.lists.MerchantsPage;
import com.talool.website.panel.image.upload.FileManageResourceReference;
import com.talool.website.panel.image.upload.FileUploadResourceReference;
import com.talool.website.service.BrowserException;

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

	@SuppressWarnings("unchecked")
	@Override
	protected void init()
	{
		getRequestCycleSettings().setGatherExtendedBrowserInfo(true);

		final IPackageResourceGuard packageResourceGuard = getResourceSettings().getPackageResourceGuard();
		if (packageResourceGuard instanceof SecurePackageResourceGuard)
		{
			final SecurePackageResourceGuard guard = (SecurePackageResourceGuard) packageResourceGuard;
			guard.addPattern("+*.*");
		}

		getComponentInstantiationListeners().add(new SpringComponentInjector(this, getApplicationContext(), false));

		final ResourceReference faviconRef = new PackageResourceReference(this.getClass(), "favicon.ico");
		mountResource("/favicon.ico", faviconRef);

		mountResource("fileManager", new FileManageResourceReference(Config.get().getUploadDir()));
		mountResource("fileUpload", new FileUploadResourceReference(Config.get().getUploadDir()));
		mountPage("/upload", UploadPage.class);

		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");

		mountPage("/admin", AdminLoginPage.class);
		mountPage("/admin/books", DealOffersPage.class);
		mountPage("/admin/dh", DealHistoryPage.class);
		mountPage("/admin/customers", CustomersPage.class);
		mountPage("/admin/analytics", AnalyticsPage.class);
		mountPage("/admin/customer/settings", CustomerSettingsPage.class);
		mountPage("/admin/customer-management", CustomerManagementPage.class);
		mountPage("/admin/merchants", MerchantsPage.class);
		mountPage("/admin/merchant/accounts", MerchantAccountsPage.class);
		mountPage("/admin/merchant/redemptions", MerchantRedemptionCodePage.class);
		mountPage("/admin/merchant/books", MerchantDealOffersPage.class);
		mountPage("/admin/merchant/locations", MerchantLocationsPage.class);
		mountPage("/admin/merchant/dashboard", MerchantDashboard.class);
		mountPage("/admin/merchant/mm", MerchantManagementPage.class);
		mountPage("/admin/customer-search", CustomerSearchPage.class);
		mountPage("/h", HealthCheckPage.class);

		// WebViews for the Apps
		mountPage("/privacy", PrivacyPolicy.class);
		mountPage("/termsofservice", TermsOfService.class);
		mountPage("/feedback", Feedback.class);
		mountPage("/services/merchants", MerchantServices.class);
		mountPage("/services/publishers", PublisherServices.class);
		mountPage("/services/consumers", ConsumerServices.class);

		// Big Web Pages
		mountPage("/corp/privacy", PrivacyPolicyPage.class);
		mountPage("/corp/terms", TermsOfServicePage.class);
		mountPage("/corp/merchantAgreement", MerchantAgreementPage.class);
		mountPage("/contactus", ContactPage.class);
		mountPage("/whitelabel", WhiteLabel.class);
		mountPage("/fundraising", Fundraising.class);
		mountPage("/analytics", Analytics.class);
		mountPage("/getstarted", Registration.class);
		mountPage("/faq", FAQ.class);
		mountPage("/gift", OpenGraphGift.class);
		mountPage("/deal", OpenGraphDeal.class);
		mountPage("/offer", OpenGraphDealOffer.class);
		mountPage("/location", OpenGraphLocation.class);
		mountPage("/password", PasswordPage.class);
		mountPage("/rpw", PasswordResetPage.class);
		mountPage("/landing", EmailLandingPage.class);

		// Mobile Web Pages
		mountPage("/m", MobileHomePage.class);
		mountPage("/m/fundraising", MobileFundraisingPage.class);
		mountPage("/m/analytics", MobileAnalyticsPage.class);
		mountPage("/m/whitelabel", MobileWhiteLabelPage.class);
		mountPage("/m/terms", MobileTermsPage.class);
		mountPage("/m/ma", MobileMerchantAgreementPage.class);
		mountPage("/m/privacy", MobilePrivacyPage.class);
		mountPage("/m/contact", MobileContactPage.class);
		mountPage("/m/password", MobilePasswordPage.class);
		mountPage("/m/rpw", MobilePasswordResetPage.class);
		mountPage("/m/gift", MobileOpenGraphGift.class);
		mountPage("/m/deal", MobileOpenGraphDeal.class);
		mountPage("/m/offer", MobileOpenGraphDealOffer.class);
		mountPage("/m/location", MobileOpenGraphLocation.class);
		mountPage("/m/gift-returned", MobileGiftReturnedPage.class);
		mountPage("/m/landing", MobileEmailLandingPage.class);
		mountPage("/m/faq", MobileFaqPage.class);
		mountPage("/m/getstarted", MobileRegistrationPage.class);

		mountPage("/404", PageNotFound.class);

		mountPage("/admin/fundraisers", FundraisersPage.class);
		mountPage("/admin/fundraisers/fm", FundraiserManagementPage.class);

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

		final AuthStrategy authStrat = new AuthStrategy();
		getSecuritySettings().setAuthorizationStrategy(authStrat);
		getSecuritySettings().setUnauthorizedComponentInstantiationListener(authStrat);
		getMarkupSettings().setStripWicketTags(true);

		if (Config.get().getWebsiteMode().equalsIgnoreCase("deployment"))
		{
			getDebugSettings().setAjaxDebugModeEnabled(false);
		}
		else
		{
			getDebugSettings().setAjaxDebugModeEnabled(true);
		}
		getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
		getApplicationSettings().setPageExpiredErrorPage(getHomePage());
		getApplicationSettings().setAccessDeniedPage(AccessDeniedPage.class);
		getApplicationSettings().setInternalErrorPage(InternalErrorPage.class);

		getRequestCycleListeners().add(new AbstractRequestCycleListener()
		{
			@Override
			public IRequestHandler onException(RequestCycle cycle, Exception ex)
			{
				BrowserException be = Exceptions.findCause(ex, BrowserException.class);
				if (be != null)
				{
					return new RenderPageRequestHandler(new PageProvider(BrowserErrorPage.class));
				}
				return super.onException(cycle, ex);
			}
		});

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
