package com.talool.website.pages.dashboard;

import java.util.UUID;

import org.apache.wicket.markup.html.panel.Panel;

import com.talool.website.pages.BasePage;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.MerchantSummaryPanel;
import com.talool.website.util.SecuredPage;
import com.talool.website.util.SessionUtils;

@SecuredPage
public class MerchantDashboard extends BasePage {

	private static final long serialVersionUID = 1L;
	private UUID _merchantId;
	
	public MerchantDashboard()
	{
		super();
		_merchantId = SessionUtils.getSession().getMerchantAccount().getMerchant().getId();
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		getPageParameters().set("id", _merchantId);
		add(new MerchantSummaryPanel("summary", getPageParameters()));
	}

	@Override
	public String getHeaderTitle() {
		return SessionUtils.getSession().getMerchantAccount().getMerchant().getName();
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNewDefinitionPanelTitle() {
		return null;
	}
	
	
	
}
