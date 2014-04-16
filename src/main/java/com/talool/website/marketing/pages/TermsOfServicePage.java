package com.talool.website.marketing.pages;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.pages.mobile.MobileTermsPage;
import com.talool.website.marketing.panel.TermsPanel;


public class TermsOfServicePage extends BaseMarketingPage {

	private static final long serialVersionUID = 1L;
	
	public TermsOfServicePage()
	{
		super();
		init();
	}
	
	public TermsOfServicePage(PageParameters params) {
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
		add(new TermsPanel("terms"));
	}

}
