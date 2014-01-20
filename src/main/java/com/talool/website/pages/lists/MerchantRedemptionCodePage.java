package com.talool.website.pages.lists;

import java.util.UUID;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.Merchant;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.RedemptionCodeLookupPanel;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

@SecuredPage
public class MerchantRedemptionCodePage extends BasePage
{

	private static final long serialVersionUID = -3832602225330266637L;
	private UUID _merchantId;

	public MerchantRedemptionCodePage()
	{
		_merchantId = SessionUtils.getSession().getMerchantAccount().getMerchant().getId();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		PageParameters params = new PageParameters();
		params.add("id", _merchantId);
		RedemptionCodeLookupPanel panel = new RedemptionCodeLookupPanel("searchPanel", params);
		add(panel);

	}

	@Override
	public String getHeaderTitle()
	{
		return "Redemption Code Search";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return null;
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		return null;
	}

	@Override
	public boolean hasActionLink() {
		return false;
	}
	
	
}
