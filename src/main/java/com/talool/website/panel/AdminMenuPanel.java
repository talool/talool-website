package com.talool.website.panel;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.talool.website.pages.AdminLoginPage;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.lists.MerchantAccountsPage;
import com.talool.website.pages.lists.MerchantLocationsPage;
import com.talool.website.panel.merchant.definition.MerchantAccountResetPasswordPanel;
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
		
		BasePage page = (BasePage) this.getPage();
		final AdminModalWindow modal = page.getModal();
		final SubmitCallBack callback = page.getCallback(modal);
		
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
		add(new AjaxLink<Void>("changePasswordLink")
		{

			private static final long serialVersionUID = -7125595810304603489L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				MerchantAccountResetPasswordPanel panel = new MerchantAccountResetPasswordPanel(modal.getContentId(), callback,
						SessionUtils.getSession().getMerchantAccount().getId());
				modal.setContent(panel);
				modal.setTitle("Change Password");
				modal.show(target);
			}

		});
		add(new BookmarkablePageLink<MerchantAccountsPage>("accountsLink",MerchantAccountsPage.class));
		add(new BookmarkablePageLink<MerchantLocationsPage>("locationsLink",MerchantLocationsPage.class));
	}

	public String getSignedInAs()
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(SessionUtils.getSession().getMerchantAccount().getEmail()).append(" / ");
		//sb.append(SessionUtils.getSession().getMerchantAccount().getRoleTitle()).append(" / ");
		sb.append(SessionUtils.getSession().getMerchantAccount().getMerchant().getName());
		return sb.toString();
	}
}
