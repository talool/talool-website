package com.talool.website.mobile;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.sales.HomepagePanel;

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
