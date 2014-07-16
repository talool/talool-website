package com.talool.website.marketing.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.website.marketing.panel.TrackingRegistrationPanel;

public class FundraiserTrackingRegistration extends BaseMarketingPage
{
	private static final long serialVersionUID = 8390824646913457971L;

	public FundraiserTrackingRegistration()
	{
		super();

	}

	public FundraiserTrackingRegistration(PageParameters parameters)
	{
		super(parameters);
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		add(new TrackingRegistrationPanel("trackme"));
	}


}
