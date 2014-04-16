package com.talool.website.marketing.pages.mobile;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.panel.HomepagePanel;

public class MobileHomePage extends MobilePage {

	private static final long serialVersionUID = 1L;

	public MobileHomePage(PageParameters parameters)
	{
		super(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		add(new HomepagePanel("content"));
	}
}
