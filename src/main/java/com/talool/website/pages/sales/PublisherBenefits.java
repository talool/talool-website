package com.talool.website.pages.sales;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.pages.WWWBasePage;
import com.talool.website.panel.sales.PublisherPanel;

public class PublisherBenefits extends WWWBasePage
{
	private static final long serialVersionUID = 8390824646913457971L;

	public PublisherBenefits()
	{
		super();

	}

	public PublisherBenefits(PageParameters parameters)
	{
		super(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new PublisherPanel("benefits"));
	}


}
