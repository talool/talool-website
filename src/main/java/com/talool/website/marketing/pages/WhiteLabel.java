package com.talool.website.marketing.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.panel.WhiteLabelPanel;

public class WhiteLabel extends BaseMarketingPage
{
	private static final long serialVersionUID = 8390824646913457971L;

	public WhiteLabel()
	{
		super();

	}

	public WhiteLabel(PageParameters parameters)
	{
		super(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new WhiteLabelPanel("whitelabel"));
	}


}
