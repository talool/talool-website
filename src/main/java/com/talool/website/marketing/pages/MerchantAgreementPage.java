package com.talool.website.marketing.pages;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.pages.mobile.MobileTermsPage;
import com.talool.website.marketing.panel.MerchantAgreementPanel;


public class MerchantAgreementPage extends BaseMarketingPage {

	private static final long serialVersionUID = 1L;
	
	public MerchantAgreementPage()
	{
		super();
		init();
	}
	
	public MerchantAgreementPage(PageParameters params) {
		super(params);
		init();
	}
	
	public void init()
	{
		if (isMobile())
		{
			throw new RestartResponseException(MobileTermsPage.class); 
		}
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new MerchantAgreementPanel("content"));
	}

}
