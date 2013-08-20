package com.talool.website.pages.corporate;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.panel.sales.PublisherPanel;


public class PublisherServices extends BaseCorporatePage {

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
