package com.talool.website.pages.corporate;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.mobile.MobilePasswordPage;
import com.talool.website.pages.WWWBasePage;
import com.talool.website.panel.customer.ForgotPasswordPanel;


public class WWWPasswordPage extends WWWBasePage {

	private static final long serialVersionUID = 1L;
	
	public WWWPasswordPage()
	{
		super();
		init();
	}
	
	public WWWPasswordPage(PageParameters params) {
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
