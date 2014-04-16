package com.talool.website.marketing.pages;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.pages.mobile.MobilePrivacyPage;
import com.talool.website.marketing.panel.PrivacyPanel;


public class PrivacyPolicyPage extends BaseMarketingPage {

	private static final long serialVersionUID = 1L;
	
	public PrivacyPolicyPage()
	{
		super();
		init();
	}
	
	public PrivacyPolicyPage(PageParameters params) {
		super(params);
		init();
	}
	
	public void init()
	{
		if (isMobile())
		{
			throw new RestartResponseException(MobilePrivacyPage.class); 
		}
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new PrivacyPanel("privacy"));
	}

}
