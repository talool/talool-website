package com.talool.website.pages.lists;

import java.util.UUID;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Merchant;
import com.talool.core.MerchantIdentity;
import com.talool.domain.MerchantIdentityImpl;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.MerchantDealsPanel;
import com.talool.website.panel.merchant.definition.MerchantDealOfferPanel;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

@SecuredPage
public class MerchantDealsPage extends BasePage
{

	private static final long serialVersionUID = 2930251149366666038L;
	private UUID _merchantId;

	public MerchantDealsPage()
	{
		_merchantId = SessionUtils.getSession().getMerchantAccount().getMerchant().getId();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		PageParameters params = new PageParameters();
		params.add("id", _merchantId);
		MerchantDealsPanel panel = new MerchantDealsPanel("list", params);
		add(panel);

	}

	@Override
	public String getHeaderTitle()
	{
		return "My Deals";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		Merchant m = SessionUtils.getSession().getMerchantAccount().getMerchant();
		MerchantIdentity mi = new MerchantIdentityImpl(m.getId(), m.getName());
		return new MerchantDealOfferPanel(contentId, mi, callback);
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		return "Create New Deal";
	}
}
