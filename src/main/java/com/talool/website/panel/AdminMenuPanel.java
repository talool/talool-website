package com.talool.website.panel;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.talool.website.pages.AdminLoginPage;
import com.talool.website.pages.lists.MerchantAccountsPage;
import com.talool.website.pages.lists.MerchantDealOffersPage;
import com.talool.website.pages.lists.MerchantLocationsPage;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author clintz
 * 
 */
public class AdminMenuPanel extends Panel
{
	private static final long serialVersionUID = -8704038043657157579L;

	public AdminMenuPanel(String id)
	{
		super(id);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new Label("signedInAs", new PropertyModel<String>(this, "signedInAs")));
		add(new Link<Void>("signOffLink")
		{

			private static final long serialVersionUID = -2823957001336926943L;

			@Override
			public void onClick()
			{
				Session.get().invalidate();
				setResponsePage(AdminLoginPage.class);
			}

		});
		add(new BookmarkablePageLink<MerchantAccountsPage>("accountsLink",MerchantAccountsPage.class));
		add(new BookmarkablePageLink<MerchantDealOffersPage>("dealOffersLink",MerchantDealOffersPage.class));
		add(new BookmarkablePageLink<MerchantLocationsPage>("locationsLink",MerchantLocationsPage.class));
	}

	public String getSignedInAs()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(SessionUtils.getSession().getMerchantAccount().getEmail()).append(" / ");
		sb.append(SessionUtils.getSession().getMerchantAccount().getRoleTitle()).append(" / ");
		sb.append(SessionUtils.getSession().getMerchantAccount().getMerchant().getName());
		return sb.toString();
	}
}
