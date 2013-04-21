package com.talool.website.pages.lists;

import java.util.UUID;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Merchant;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.MerchantAccountsPanel;
import com.talool.website.panel.merchant.definition.MerchantAccountPanel;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

@SecuredPage
public class MerchantAccountsPage extends BasePage
{

	private static final long serialVersionUID = -3832602225330266637L;
	private UUID _merchantId;

	public MerchantAccountsPage()
	{
		_merchantId = SessionUtils.getSession().getMerchantAccount().getMerchant().getId();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		PageParameters params = new PageParameters();
		params.add("id", _merchantId);
		MerchantAccountsPanel panel = new MerchantAccountsPanel("list", params);
		add(panel);

	}

	@Override
	public String getHeaderTitle()
	{
		Merchant m = SessionUtils.getSession().getMerchantAccount().getMerchant();
		StringBuilder sb = new StringBuilder(m.getName());
		sb.append(" > Accounts");
		return sb.toString();
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		Merchant m = SessionUtils.getSession().getMerchantAccount().getMerchant();
		return new MerchantAccountPanel(contentId, m.getId(), callback);
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		return "Create New Account";
	}
}
