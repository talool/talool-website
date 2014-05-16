package com.talool.website.marketing.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.panel.RegistrationPanel;

public class Registration extends BaseMarketingPage
{
	private static final long serialVersionUID = 8390824646913457971L;

	private boolean isFundraiser;
	private boolean isPublisher;
	
	public Registration()
	{
		super();
		isFundraiser = false;
		isPublisher = false;
	}

	public Registration(PageParameters parameters)
	{
		super(parameters);
		int flag = parameters.get(0).toInt();
		isFundraiser = flag==1;
		isPublisher = flag==2;
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new RegistrationPanel("registration", isFundraiser, isPublisher));
	}
}
