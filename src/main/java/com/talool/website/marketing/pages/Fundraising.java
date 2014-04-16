package com.talool.website.marketing.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.panel.FundraisingPanel;

public class Fundraising extends BaseMarketingPage
{
	private static final long serialVersionUID = 8390824646913457971L;

	public Fundraising()
	{
		super();

	}

	public Fundraising(PageParameters parameters)
	{
		super(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new FundraisingPanel("fundraising"));
	}


}
