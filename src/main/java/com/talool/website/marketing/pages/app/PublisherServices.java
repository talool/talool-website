package com.talool.website.marketing.pages.app;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.panel.PublisherPanel;


public class PublisherServices extends BaseAppPage {

	private static final long serialVersionUID = 1L;
	
	public PublisherServices(PageParameters params) {
		super(params);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new PublisherPanel("benefits"));
	}

}
