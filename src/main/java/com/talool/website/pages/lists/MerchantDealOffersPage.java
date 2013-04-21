package com.talool.website.pages.lists;

import java.util.UUID;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Merchant;
import com.talool.core.MerchantIdentity;
import com.talool.domain.MerchantIdentityImpl;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.MerchantDealOffersPanel;
import com.talool.website.panel.merchant.definition.MerchantDealOfferPanel;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

@SecuredPage
public class MerchantDealOffersPage extends BasePage
{

	private static final long serialVersionUID = 2930251149366666038L;
	private UUID _merchantId;

	public MerchantDealOffersPage()
	{
		_merchantId = SessionUtils.getSession().getMerchantAccount().getMerchant().getId();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		PageParameters params = new PageParameters();
		params.add("id", _merchantId);
		MerchantDealOffersPanel panel = new MerchantDealOffersPanel("list", params);
		add(panel);

	}

	@Override
	public String getHeaderTitle()
	{
		Merchant m = SessionUtils.getSession().getMerchantAccount().getMerchant();
		StringBuilder sb = new StringBuilder(m.getName());
		sb.append(" > Deal Offers");
		return sb.toString();
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
		return "Create New Deal Offer";
	}
}
