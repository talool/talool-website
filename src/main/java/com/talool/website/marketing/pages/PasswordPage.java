package com.talool.website.marketing.pages;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.pages.mobile.MobilePasswordPage;
import com.talool.website.marketing.panel.ForgotPasswordPanel;


public class PasswordPage extends BaseMarketingPage {

	private static final long serialVersionUID = 1L;
	
	public PasswordPage()
	{
		super();
		init();
	}
	
	public PasswordPage(PageParameters params) {
		super(params);
		init();
	}
	
	public void init()
	{
		if (isMobile())
		{
			throw new RestartResponseException(MobilePasswordPage.class); 
		}
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new ForgotPasswordPanel("content"));
	}

}
