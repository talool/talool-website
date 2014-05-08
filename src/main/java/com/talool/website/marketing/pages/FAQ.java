package com.talool.website.marketing.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.panel.FaqPanel;

public class FAQ extends BaseMarketingPage
{
	private static final long serialVersionUID = 8390824646913457971L;
	private String topic;

	public FAQ()
	{
		super();
	}

	public FAQ(PageParameters parameters)
	{
		super(parameters);
		topic = parameters.get(0).toString();
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new FaqPanel("faq"));
	}


}
