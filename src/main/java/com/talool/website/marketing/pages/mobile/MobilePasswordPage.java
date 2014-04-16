package com.talool.website.marketing.pages.mobile;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.panel.ForgotPasswordPanel;

public class MobilePasswordPage extends MobilePage {

	private static final long serialVersionUID = 1L;

	public MobilePasswordPage(PageParameters parameters)
	{
		super(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();	
		add(new ForgotPasswordPanel("content"));
	}
}
