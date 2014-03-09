package com.talool.website.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.FactoryManager;
import com.talool.core.MerchantAccount;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.website.pages.lists.MerchantsPage;
import com.talool.website.service.PermissionService;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author clintz
 * 
 */
public class AdminLoginPage extends WebPage
{
	private static final Logger LOG = LoggerFactory.getLogger(AdminLoginPage.class);
	static final long serialVersionUID = 3458342019765593083L;

	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	private String email;
	private String pass;

	public AdminLoginPage()
	{
		super();
		SessionUtils.getSession().performBrowserCheck();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		Form<Void> loginForm = new Form<Void>("loginForm")
		{
			private static final long serialVersionUID = 2442156678412552184L;

			@Override
			protected void onSubmit()
			{
				try
				{
					MerchantAccount mAccnt = taloolService.authenticateMerchantAccount(email, pass);
					if (mAccnt != null)
					{
						SessionUtils.getSession().setMerchantAccount(mAccnt);
						continueToOriginalDestination();

						if (PermissionService.get().canViewAnalytics(mAccnt.getEmail()))
						{
							setResponsePage(AnalyticsPage.class);
						}
						else
						{
							setResponsePage(MerchantsPage.class);
						}

					}
				}
				catch (ServiceException e)
				{
					LOG.error(String.format("Problem authenticating %s", email), e);
					setResponsePage(AdminLoginPage.class);
				}
			}

		};
		add(loginForm);

		loginForm.add(new TextField<String>("email", new PropertyModel<String>(this, "email")));
		loginForm.add(new PasswordTextField("pass", new PropertyModel<String>(this, "pass")));

	}
}
