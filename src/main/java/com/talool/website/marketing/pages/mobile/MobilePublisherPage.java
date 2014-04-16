package com.talool.website.marketing.pages.mobile;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.panel.PublisherPanel;

public class MobilePublisherPage extends MobilePage {

	private static final long serialVersionUID = 1L;

	public MobilePublisherPage(PageParameters parameters)
	{
		super(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		add(new PublisherPanel("content"));
	}
}
